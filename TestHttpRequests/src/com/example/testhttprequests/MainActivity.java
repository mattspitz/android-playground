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
		HootcasterApiClient client = new HootcasterApiClient(this);
		String registrationId = "blahblahregistrationid";
		String phone = "4151234567";
		client.createAccount(
				getTextFieldValue(R.id.create_user), getTextFieldValue(R.id.create_pass),
				getTextFieldValue(R.id.create_person), registrationId,
				getTextFieldValue(R.id.create_emailaddress), phone,
				new LoginHandler() {

					@Override
					public void handleSuccess() {
						Log.i(TAG, "Oh my fuck, it worked.");
					}

					@Override
					public void handleConnectionFailure() {
						throw new RuntimeException("connection failure?!");
					}

					@Override
					public void handleUsernameAlreadyExists() {
						throw new RuntimeException("username already exists!");
					}

					@Override
					public void handleEmailAlreadyExists() {
						throw new RuntimeException("email already exists!");
					}

					@Override
					public void handleRegistrationIdAlreadyExists() {
						throw new RuntimeException("registrationId already exists!");
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
		final Activity thisActivity = this;
		getAsyncHttpClient().get(BASE_URL + "testauth", null, new AsyncHttpResponseHandler() {
			@Override
			public void onStart() {
				Log.i(TAG, "Started request!");
			}

			@Override
			public void onSuccess(String response) {
				Log.i(TAG, "Got success response: " + response);
				startActivity(new Intent(thisActivity, SecondaryTestActivity.class));
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
