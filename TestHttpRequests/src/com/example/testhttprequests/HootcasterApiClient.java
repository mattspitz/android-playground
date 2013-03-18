package com.example.testhttprequests;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpEntity;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.type.TypeFactory;
import org.codehaus.jackson.type.JavaType;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.example.testhttprequests.account.CreateAccountHandler;
import com.example.testhttprequests.account.CreateAccountHandler.CreateAccountError;
import com.google.common.base.Preconditions;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;

public class HootcasterApiClient {
	private static final String TAG = "HootcasterApiClient";

	private static final String HOST = "api.hootcaster.com";
	private static final String DEV_HOST = "10.0.2.2"; // adb-connected localhost on android

	private final Context context;
	private final AsyncHttpClient asyncHttpClient;
	private final ObjectMapper objectMapper;

	public HootcasterApiClient(Context context) {
		this.context = context;
		this.objectMapper = new ObjectMapper();
		this.asyncHttpClient = new AsyncHttpClient();
		this.asyncHttpClient.setCookieStore(new PersistentCookieStore(this.context));

		if (isDevEnvironment()) {
			// accept certificates from all hostnames
			KeyStore trustStore;
			try {
				trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
			} catch (KeyStoreException ex) {
				throw new RuntimeException(ex);
			}
			try {
				trustStore.load(null, null);
			} catch (NoSuchAlgorithmException ex) {
				throw new RuntimeException(ex);
			} catch (CertificateException ex) {
				throw new RuntimeException(ex);
			} catch (IOException ex) {
				throw new RuntimeException(ex);
			}

			SSLSocketFactory socketFactory;
			try {
				socketFactory = new AcceptAllSSLSocketFactory(trustStore);
			} catch (KeyManagementException ex) {
				throw new RuntimeException(ex);
			} catch (UnrecoverableKeyException ex) {
				throw new RuntimeException(ex);
			} catch (NoSuchAlgorithmException ex) {
				throw new RuntimeException(ex);
			} catch (KeyStoreException ex) {
				throw new RuntimeException(ex);
			}
			socketFactory.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
			this.asyncHttpClient.setSSLSocketFactory(socketFactory);
		}
	}

	private static boolean IS_DEV_ENVIRONMENT = true; // TODO automatically determine this
	private boolean isDevEnvironment() {
		return IS_DEV_ENVIRONMENT;
	}

	private void jsonPost(
			final String path, final boolean isHttps,
			final JSONObject json, final AsyncHttpResponseHandler handler) {
		final String url = getUrl(path, isHttps);
		HttpEntity httpEntity;
		try {
			httpEntity = new StringEntity(json.toString(), "UTF-8");
		} catch (UnsupportedEncodingException ex) {
			throw new RuntimeException(ex); // shouldn't ever happen
		}

		Log.i(TAG, "URL: " + url);
		Log.i(TAG, "Data: " + json.toString());
		asyncHttpClient.post(
				context, url, 
				httpEntity, "application/json", 
				handler
				);
	}

	private String getUrl(String path, boolean isHttps) {
		final String scheme = isHttps ? "https" : "http";
		final String host = isDevEnvironment() ? DEV_HOST : HOST;
		return String.format("%s://%s/v1/%s", scheme, host, path);
	}

	private <T, E extends Enum<E>> HootcasterResponse<T, E> deserialize(String responseStr, Class<T> dataClass, Class<E> errorClass) throws JsonParseException, JsonMappingException, IOException {
		@SuppressWarnings("deprecation")
		JavaType responseType = TypeFactory.parametricType(HootcasterResponse.class, dataClass, errorClass);
		return objectMapper.readValue(responseStr, responseType);
	}

	public void createAccount(
			final String username, final String password,
			final String fullname, final String registrationId,
			final String email, final String phone, final CreateAccountHandler createAccountHandler) {

		JSONObject json = new JSONObject();
		try {
			json.put("username", Preconditions.checkNotNull(username));
			json.put("password", Preconditions.checkNotNull(password));
			json.put("fullname", Preconditions.checkNotNull(fullname));
			json.put("registration_id", Preconditions.checkNotNull(registrationId));
			json.put("email", Preconditions.checkNotNull(email));

			if (phone != null)
				json.put("phone", phone);

		} catch (JSONException ex) {
			throw new RuntimeException(ex); // shouldn't ever happen
		}

		jsonPost("account/create", true, json, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(final String responseStr) {
				HootcasterResponse<Void, CreateAccountError> response;
				try {
					response = deserialize(responseStr, Void.class, CreateAccountError.class);
				} catch (Exception ex) {
					// allegedly, we got a good response!
					throw new RuntimeException(ex);
				}

				if (response.isOkay())
					createAccountHandler.handleSuccess();
				else
					createAccountHandler.handleErrors(response.getErrors());
			}

			@Override
			public void onFailure(final Throwable throwable, final String responseStr) {
				if (throwable instanceof ConnectException) {
					createAccountHandler.handleConnectionFailure();
				} else {
					HootcasterResponse<Void, CreateAccountError> response;
					// try to deserialize; if we can't, have them handle our exception
					try {
						response = deserialize(responseStr, Void.class, CreateAccountError.class);
					} catch (Exception e) {
						createAccountHandler.handleUnknownException(throwable);
						return;
					}
					createAccountHandler.handleErrors(response.getErrors());
				}
			}
		});
	}

	private static class AcceptAllSSLSocketFactory extends SSLSocketFactory {
		SSLContext sslContext = SSLContext.getInstance("TLS");

		public AcceptAllSSLSocketFactory(KeyStore truststore) throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException, UnrecoverableKeyException {
			super(truststore);

			TrustManager tm = new X509TrustManager() {
				public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
				}

				public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
				}

				public X509Certificate[] getAcceptedIssuers() {
					return null;
				}
			};

			sslContext.init(null, new TrustManager[] { tm }, null);
		}

		@Override
		public Socket createSocket(Socket socket, String host, int port, boolean autoClose) throws IOException, UnknownHostException {
			return sslContext.getSocketFactory().createSocket(socket, host, port, autoClose);
		}

		@Override
		public Socket createSocket() throws IOException {
			return sslContext.getSocketFactory().createSocket();
		}
	}
}