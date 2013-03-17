package com.example.testhttprequests;

import java.io.UnsupportedEncodingException;
import java.net.ConnectException;

import org.apache.http.HttpEntity;
import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

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

	public HootcasterApiClient(Context context) {
		this.context = context;
		this.asyncHttpClient = new AsyncHttpClient();
		this.asyncHttpClient.setCookieStore(new PersistentCookieStore(this.context));
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

	public void createAccount(
			final String username, final String password,
			final String fullname, final String registrationId,
			final String email, final String phone, final LoginHandler loginHandler) {
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
			public void onStart() {
				Log.i(TAG, "onStart");
			}

			@Override
			public void onSuccess(final String response) {
				Log.i(TAG, "Success!: " + response);
				loginHandler.handleSuccess();
			}

			@Override
			public void onFailure(final Throwable ex, final String response) {
				Log.i(TAG, "error: " + ex);
				if (response != null)
					Log.i(TAG, "content: " + response);
				if (ex instanceof ConnectException)
					loginHandler.handleConnectionFailure();
			}

			@Override
			public void onFinish() {
				Log.i(TAG, "onFinish");
			}
		});
	}
}