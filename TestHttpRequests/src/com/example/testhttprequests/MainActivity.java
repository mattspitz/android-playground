package com.example.testhttprequests;

import java.util.EnumSet;
import java.util.Random;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import com.example.testhttprequests.account.CreateAccountHandler;
import com.example.testhttprequests.account.CreateAccountHandler.CreateAccountError;
import com.example.testhttprequests.account.LoginHandler;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;

public class MainActivity extends Activity {

	private static final String TAG = "MainActivity";

	private HootcasterApiClient client;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		this.client = new HootcasterApiClient(this);
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
		String registrationId = Long.toString((new Random()).nextLong());
		String phone = "";

		client.createAccount(
				getTextFieldValue(R.id.create_user), getTextFieldValue(R.id.create_pass),
				getTextFieldValue(R.id.create_person), registrationId,
				getTextFieldValue(R.id.create_emailaddress), phone,
				new CreateAccountHandler() {
					@Override
					public void handleSuccess() {
						Log.i(TAG, "Oh my fuck, it worked.");
					}

					@Override
					public void handleConnectionFailure() {
						throw new RuntimeException("connection failure?!");
					}

					@Override
					public void handleErrors(final EnumSet<CreateAccountError> errors) {
						throw new RuntimeException("Balls: " + errors);
					}

					@Override
					public void handleUnknownException(Throwable ex) {
						throw new RuntimeException(ex);
					}
				});
	}

	public void onLoginClick(View view) {
		String registrationId = Long.toString((new Random()).nextLong());

		client.login(
				getTextFieldValue(R.id.login_username), getTextFieldValue(R.id.login_password),
				registrationId,
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
					public void handleErrors(final EnumSet<LoginError> errors) {
						throw new RuntimeException("Balls: " + errors);
					}

					@Override
					public void handleUnknownException(Throwable ex) {
						throw new RuntimeException(ex);
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
