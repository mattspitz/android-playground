package com.example.testhttprequests.api;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpEntity;
import org.apache.http.client.HttpResponseException;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.StringBody;
import org.codehaus.jackson.map.ObjectMapper;

import android.content.Context;
import android.util.Log;

import com.example.testhttprequests.api.handlers.HootcasterApiHandler;
import com.example.testhttprequests.api.handlers.HootcasterApiLoggedInHandler;
import com.example.testhttprequests.api.handlers.HootcasterResponse;
import com.example.testhttprequests.api.handlers.account.CreateAccountHandler;
import com.example.testhttprequests.api.handlers.account.CreateAccountHandler.CreateAccountResponse;
import com.example.testhttprequests.api.handlers.account.LoginHandler;
import com.example.testhttprequests.api.handlers.account.LoginHandler.LoginResponse;
import com.example.testhttprequests.api.handlers.contact.ContactsHandler;
import com.example.testhttprequests.api.handlers.contact.ContactsHandler.ContactsResponse;
import com.example.testhttprequests.api.handlers.contact.FindContactsHandler;
import com.example.testhttprequests.api.handlers.contact.FindContactsHandler.FindContactsResponse;
import com.example.testhttprequests.api.handlers.contact.ModifyContactsHandler;
import com.example.testhttprequests.api.handlers.contact.ModifyContactsHandler.ModifyContactsResponse;
import com.example.testhttprequests.api.handlers.transaction.CreateTransactionHandler;
import com.example.testhttprequests.api.handlers.transaction.CreateTransactionHandler.CreateTransactionResponse;
import com.example.testhttprequests.api.models.PotentialContact;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;

public class HootcasterApiClient {
	private static final String TAG = "HootcasterApiClient";

	private static final String HOST = "api.hootcaster.com";
	private static final String DEV_HOST = "10.0.2.2"; // adb-connected localhost on android

	private final Context context;
	private final AsyncHttpClient asyncHttpClient;
	private static final ObjectMapper objectMapper = new ObjectMapper();

	public HootcasterApiClient(Context context) {
		this.context = context;
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

	public void createAccount(
			final String username, final String password,
			final String email, final String registrationId,
			final CreateAccountHandler createAccountHandler) {

		final Map<String, String> postData = ImmutableMap.of(
				"username", Preconditions.checkNotNull(username),
				"password", Preconditions.checkNotNull(password),
				"email", Preconditions.checkNotNull(email),
				"registration_id", Preconditions.checkNotNull(registrationId)
				);

		jsonPostHttps("account/create", postData,
				new HootcasterHttpResponseHandler<CreateAccountResponse>(
						CreateAccountResponse.getResponseClass(),
						createAccountHandler,
						new ResponseHandler<CreateAccountResponse>() {

							@Override
							public void handleSuccess(CreateAccountResponse response) {
								createAccountHandler.handleSuccess();
							}

							@Override
							public void handleFailure(CreateAccountResponse response) {
								createAccountHandler.handleErrors(response.getErrors());
							}
						}));
	}

	public void login(
			final String username, final String password,
			final String registrationId, final LoginHandler loginHandler) {	
		
		final Map<String, String> postData = ImmutableMap.of(
				"username", Preconditions.checkNotNull(username),
				"password", Preconditions.checkNotNull(password),
				"registration_id", Preconditions.checkNotNull(registrationId)
				);

		jsonPostHttps("account/login", postData,
				new HootcasterHttpResponseHandler<LoginResponse>(
						LoginResponse.getResponseClass(),
						loginHandler,
						new ResponseHandler<LoginResponse>() {
							@Override
							public void handleSuccess(LoginResponse response) {
								loginHandler.handleSuccess();
							}

							@Override
							public void handleFailure(LoginResponse response) {
								loginHandler.handleErrors(response.getErrors());
							}
						}));
	}

	private void getContacts(
			final String path,
			final ContactsHandler contactsHandler) {
		get(path,
				new HootcasterHttpResponseHandler<ContactsResponse>(
						ContactsResponse.getResponseClass(),
						contactsHandler,
						new ResponseHandler<ContactsResponse>() {
							@Override
							public void handleSuccess(ContactsResponse response) {
								contactsHandler.handleSuccess(response.getData().getContacts());
							}

							@Override
							public void handleFailure(ContactsResponse response) {
								throw new RuntimeException("Unexpectedly failed with correctly deserialized response: " + response);
							}
						}));
	}

	public void allContacts(
			final ContactsHandler contactsHandler) {
		getContacts("contacts", contactsHandler);
	}

	public void blockedContacts(
			final ContactsHandler contactsHandler) {
		getContacts("contacts/blocked", contactsHandler);
	}

	private void modifyContacts(
			final String path,
			final List<String> usernames,
			final ModifyContactsHandler modifyContactsHandler) {
		
		Preconditions.checkNotNull(usernames);
		if (usernames.isEmpty())
			throw new IllegalArgumentException("Must pass at least one username!");
		
		final Map<String, ? extends List<String>> postData = ImmutableMap.of(
				"usernames", ImmutableList.copyOf(usernames)
				);

		jsonPost(path, postData,
				new HootcasterHttpResponseHandler<ModifyContactsResponse>(
						ModifyContactsResponse.getResponseClass(),
						modifyContactsHandler,
						new ResponseHandler<ModifyContactsResponse>() {
							@Override
							public void handleSuccess(ModifyContactsResponse response) {
								modifyContactsHandler.handleSuccess();
							}

							@Override
							public void handleFailure(ModifyContactsResponse response) {
								throw new RuntimeException("Unexpectedly failed with correctly deserialized response: " + response);
							}
						}));
	}

	public void addContacts(
			final List<String> usernames,
			final ModifyContactsHandler modifyContactsHandler) {
		modifyContacts("contacts/add", usernames, modifyContactsHandler);
	}

	public void removeContacts(
			final List<String> usernames,
			final ModifyContactsHandler modifyContactsHandler) {
		modifyContacts("contacts/remove", usernames, modifyContactsHandler);
	}

	public void blockContacts(
			final List<String> usernames,
			final ModifyContactsHandler modifyContactsHandler) {
		modifyContacts("contacts/block", usernames, modifyContactsHandler);
	}

	public void unblockContacts(
			final List<String> usernames,
			final ModifyContactsHandler modifyContactsHandler) {
		modifyContacts("contacts/unblock", usernames, modifyContactsHandler);
	}

	public void findContacts(
			final Map<String, PotentialContact> potentialContacts,
			final FindContactsHandler findContactsHandler) {
		Preconditions.checkNotNull(potentialContacts);
		if (potentialContacts.isEmpty())
			throw new IllegalArgumentException("Must pass at least one potential contact!");
		
		final Map<String, ? extends Map<String, PotentialContact >> postData = ImmutableMap.of(
				"contactables", ImmutableMap.copyOf(potentialContacts)
				);

		jsonPost("contacts/find_new", postData,
				new HootcasterHttpResponseHandler<FindContactsResponse>(
						FindContactsResponse.getResponseClass(),
						findContactsHandler,
						new ResponseHandler<FindContactsResponse>() {
							@Override
							public void handleSuccess(FindContactsResponse response) {
								findContactsHandler.handleSuccess(response.getData().getMatchedContacts());
							}

							@Override
							public void handleFailure(FindContactsResponse response) {
								throw new RuntimeException("Unexpectedly failed with correctly deserialized response: " + response);
							}
						}));
	}
	
	public void createTransaction(
			final String imageFilename,
			final byte[] image,
			final String imageMimeType,
			final Collection<String> recipients,
			final CreateTransactionHandler createTransactionHandler) {
		
		Preconditions.checkNotNull(image);
		Preconditions.checkNotNull(recipients);
		if (recipients.isEmpty())
			throw new IllegalArgumentException("Must pass at least one recipient!");
		
		final Map<String, ? extends List<String>> postData = ImmutableMap.of(
				"recipients", ImmutableList.copyOf(recipients)
				);

		jsonPost("transaction/action/create", postData,
				imageFilename, image, imageMimeType,
				new HootcasterHttpResponseHandler<CreateTransactionResponse>(
						CreateTransactionResponse.getResponseClass(),
						createTransactionHandler,
						new ResponseHandler<CreateTransactionResponse>() {
							@Override
							public void handleSuccess(CreateTransactionResponse response) {
								createTransactionHandler.handleSuccess();
							}

							@Override
							public void handleFailure(CreateTransactionResponse response) {
								createTransactionHandler.handleErrors(response.getErrors());
							}
						}));
	}
	
	private void jsonPost(
			final String path, final boolean isHttps, final Map<?,?> jsonData, 
			final String attachmentFilename, final byte[] attachmentBytes, final String attachmentMimeType,
			final AsyncHttpResponseHandler handler) {
		final String url = getUrl(path, isHttps);
		String jsonPostData;
		try {
			jsonPostData = objectMapper.writeValueAsString(jsonData);
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}

		HttpEntity httpEntity;
		String contentType;
		if (attachmentBytes == null) {
			try {
				httpEntity = new StringEntity(jsonPostData, "UTF-8");
			} catch (UnsupportedEncodingException ex) {
				throw new RuntimeException(ex); // shouldn't ever happen
			}
			contentType = "application/json";
		} else {
			MultipartEntity multipartEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
			try {
				multipartEntity.addPart("jsonPayload", new StringBody(jsonPostData, "application/json", Charset.forName("UTF-8")));
			} catch (UnsupportedEncodingException ex) {
				throw new RuntimeException(ex); // shouldn't ever happen
			}
			multipartEntity.addPart("media", new ByteArrayBody(attachmentBytes, attachmentMimeType, attachmentFilename));
			
			httpEntity = multipartEntity;
			contentType = multipartEntity.getContentType().getValue();
			Log.i(TAG, "got me my multipart TODO remove");
		}

		Log.i(TAG, "URL: " + url);
		Log.i(TAG, "Data: " + jsonPostData);
		asyncHttpClient.post(
				context, url, 
				httpEntity, contentType,
				handler
				);
	}
	
	private void jsonPost(
			final String path, final Map<?,?> jsonData, final AsyncHttpResponseHandler handler) {
		jsonPost(path, false, jsonData, null, null, null, handler);
	}

	private void jsonPost(
			final String path, final Map<?,?> jsonData,
			final String attachmentFilename, final byte[] attachmentBytes, final String attachmentMimeType,
			final AsyncHttpResponseHandler handler) {
		jsonPost(path, false, jsonData, attachmentFilename, attachmentBytes, attachmentMimeType, handler);
	}
	
	private void jsonPostHttps(
			final String path,
			final Map<?,?> jsonData,
			final AsyncHttpResponseHandler handler) {
		jsonPost(path, true, jsonData, null, null, null, handler);
	}

	private void get(
			final String path, final AsyncHttpResponseHandler handler) {
		final String url = getUrl(path, false);
		asyncHttpClient.get(
				context, url, handler
				);
	}

	private String getUrl(String path, boolean isHttps) {
		final String scheme = isHttps ? "https" : "http";
		final String host = isDevEnvironment() ? DEV_HOST : HOST;
		return String.format("%s://%s/v1/%s", scheme, host, path);
	}

	private interface ResponseHandler<R extends HootcasterResponse<?>> {
		public void handleSuccess(R response);
		public void handleFailure(R response);
	}

	public static class HootcasterHttpResponseHandler<R extends HootcasterResponse<?>> extends AsyncHttpResponseHandler {
		private final Class<R> responseClass;
		private final HootcasterApiHandler apiHandler;
		private final ResponseHandler<R> responseHandler;

		public HootcasterHttpResponseHandler(
				final Class<R> responseClass,
				final HootcasterApiHandler apiHandler,
				final ResponseHandler<R> responseHandler) {
			super();
			this.responseClass = responseClass;
			this.apiHandler = apiHandler;
			this.responseHandler = responseHandler;
		}

		@Override
		public final void onSuccess(final String responseStr) {
			Log.i(TAG, "Success response: " + responseStr);
			R response;
			try {
				response = objectMapper.readValue(responseStr, responseClass);
			} catch (Exception ex) {
				// I thought we got a good response!
				throw new RuntimeException(ex);
			}
			if (response.isOkay())
				responseHandler.handleSuccess(response);
			else 
				responseHandler.handleFailure(response);
		}

		@Override
		public void onFailure(final Throwable throwable, final String responseStr) {
			Log.e(TAG, "Failure", throwable);
			if (throwable instanceof HttpResponseException) {
				HttpResponseException httpResponseException = (HttpResponseException) throwable;
				if (httpResponseException.getStatusCode() == 403 &&
						(apiHandler instanceof HootcasterApiLoggedInHandler)) {
					((HootcasterApiLoggedInHandler) apiHandler).handleNeedsLogin();
				} else {
					apiHandler.handleUnknownException(throwable);
				}
			} else if (throwable instanceof ConnectException) {
				apiHandler.handleConnectionFailure();
			} else {
				R response;
				try {
					response = objectMapper.readValue(responseStr, responseClass);
				} catch (Exception e) {
					apiHandler.handleUnknownException(throwable);
					return;
				}
				responseHandler.handleFailure(response);
			}
		}
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