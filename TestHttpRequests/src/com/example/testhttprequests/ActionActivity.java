package com.example.testhttprequests;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.EnumSet;

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.testhttprequests.api.HootcasterApiClient;
import com.example.testhttprequests.api.handlers.transaction.CreateReactionHandler;
import com.example.testhttprequests.api.handlers.transaction.CreateTransactionHandler.CreateTransactionError;
import com.example.testhttprequests.storage.FileStash;

public class ActionActivity extends Activity {

	public static final String IMAGE_FILENAME = "com.example.testhttprequests.ActionActivity.imageData";
	public static final String TRANSACTION_ID = "com.example.testhttprequests.ActionActivity.transactionId";
	public static final String USERNAME = "com.example.testhttprequests.ActionActivity.username";

	private String transactionId;
	private HootcasterApiClient client;

	private ImageView imageView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_action);
		// Show the Up button in the action bar.
		setupActionBar();

		this.client = new HootcasterApiClient(this);

		Bundle bundle = getIntent().getExtras();
		transactionId = (String) bundle.get(TRANSACTION_ID);
		String imageFilename = (String) bundle.get(IMAGE_FILENAME);

		imageView = (ImageView) findViewById(R.id.imageView1);

		byte[] data;
		try {
			data = FileStash.getFile(getApplication(), imageFilename, true);
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}
		imageView.setImageBitmap(BitmapFactory.decodeByteArray(data, 0, data.length));
	}

	public void onSendReactionClick(View view) {
		InputStream inputStream = getResources().openRawResource(R.raw.quickpan);
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();

		int nRead;
		byte[] data = new byte[8192];

		try {
			while ((nRead = inputStream.read(data, 0, data.length)) != -1)
				buffer.write(data, 0, nRead);
			buffer.flush();
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}

		client.createReaction(
				transactionId,
				"quickpan.mp4", buffer.toByteArray(), "video/mp4",
				new CreateReactionHandler() {
					@Override
					public void handleConnectionFailure() {
						throw new RuntimeException("connection failure?!");
					}

					@Override
					public void handleUnknownException(Throwable ex) {
						Toast.makeText(getApplication(), "Aw, peas: " + ex, Toast.LENGTH_SHORT).show();
					}

					@Override
					public void handleNeedsLogin() {
						Toast.makeText(getApplication(), "Needs login!", Toast.LENGTH_SHORT).show();										
					}

					@Override
					public void handleErrors(
							EnumSet<CreateReactionError> errors) {
						Toast.makeText(getApplication(), "Got errors: " + errors, Toast.LENGTH_SHORT).show();		
					}

					@Override
					public void handleSuccess() {
						Toast.makeText(getApplication(), "Reaction sent!", Toast.LENGTH_SHORT).show();
					}			
				});
	}

	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.action, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
