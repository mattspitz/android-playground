package com.example.testhttprequests;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;

public class MainActivity extends Activity {

	private static final String TAG = "MainActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	private AsyncHttpClient asyncClient = null;
	private static final String BASE_URL = "http://10.0.2.2:5000/v1/"; // localhost!
	private AsyncHttpClient getAsyncHttpClient() {
		if (asyncClient == null) {
			asyncClient = new AsyncHttpClient();
			asyncClient.setCookieStore(new PersistentCookieStore(getApplication()));
		}
		return asyncClient;
	}

	private String getTextFieldValue(int viewId) {
		return ((TextView)findViewById(viewId)).getText().toString();
	}

	public void onCreateClick(View view) {		
		RequestParams params = new RequestParams();
		params.put("realname", getTextFieldValue(R.id.create_person));
		params.put("username", getTextFieldValue(R.id.create_user));
		params.put("password", getTextFieldValue(R.id.create_pass));
		params.put("email", getTextFieldValue(R.id.create_emailaddress));

		Log.e(TAG, "Go create!");
		getAsyncHttpClient().post(BASE_URL + "account/create", params, new AsyncHttpResponseHandler() {
			@Override
			public void onStart() {
				Log.i(TAG, "Started request!");
			}

			@Override
			public void onSuccess(String response) {
				Log.i(TAG, "Got success response: " + response);
			}

			@Override
			public void onFailure(Throwable e, String response) {
				Log.i(TAG, "Got failure response: " + response + ". Throwable: " + e);
			}

			@Override
			public void onFinish() {
				Log.i(TAG, "Done with request!");
			}
		});
	}

	public void onLoginClick(View view) {
		RequestParams params = new RequestParams();
		params.put("username", getTextFieldValue(R.id.login_username));
		params.put("password", getTextFieldValue(R.id.login_password));

		Log.e(TAG, "Go login!");
		getAsyncHttpClient().post(BASE_URL + "account/login", params, new AsyncHttpResponseHandler() {
			@Override
			public void onStart() {
				Log.i(TAG, "Started request!");
			}

			@Override
			public void onSuccess(String response) {
				Log.i(TAG, "Got success response: " + response);
			}

			@Override
			public void onFailure(Throwable e, String response) {
				Log.i(TAG, "Got failure response: " + response + ". Throwable: " + e);
			}

			@Override
			public void onFinish() {
				Log.i(TAG, "Done with request!");
			}
		});
	}

	public void onAuthTestClick(View view) {
		Log.e(TAG, "Go testauth!");
		getAsyncHttpClient().get(BASE_URL + "testauth", new RequestParams(), new AsyncHttpResponseHandler() {
			@Override
			public void onStart() {
				Log.i(TAG, "Started request!");
			}

			@Override
			public void onSuccess(String response) {
				Log.i(TAG, "Got success response: " + response);
			}

			@Override
			public void onFailure(Throwable e, String response) {
				Log.i(TAG, "Got failure response: " + response + ". Throwable: " + e);
			}

			@Override
			public void onFinish() {
				Log.i(TAG, "Done with request!");
			}
		});
	}

	public void onAnotherClick(View view) {
		startActivity(new Intent(this, SecondaryTestActivity.class));
	}
}
