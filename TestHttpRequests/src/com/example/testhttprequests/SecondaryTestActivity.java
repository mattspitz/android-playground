package com.example.testhttprequests;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

public class SecondaryTestActivity extends Activity {
	private static final String TAG = "SecondaryTestActivity";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_secondary_test);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.secondary_test, menu);
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
	
	public void onAuthTestClick(View view) {
		Log.e(TAG, "Go testauth!");
		getAsyncHttpClient().get(BASE_URL + "testauth", null, new AsyncHttpResponseHandler() {
		     @Override
		     public void onStart() {
		    	 Log.i(TAG, "Started request!!");
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
	
	public void onGoBackClick(View view) {
		startActivity(new Intent(this, MainActivity.class));
	}
}
