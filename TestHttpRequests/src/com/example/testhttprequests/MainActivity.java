package com.example.testhttprequests;

import java.util.EnumSet;
import java.util.List;
import java.util.Random;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.testhttprequests.api.HootcasterApiClient;
import com.example.testhttprequests.api.handlers.account.CreateAccountHandler;
import com.example.testhttprequests.api.handlers.account.LoginHandler;
import com.example.testhttprequests.api.handlers.contact.ContactsHandler;
import com.example.testhttprequests.api.models.Contact;

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
						Toast.makeText(getApplication(), "Logged in as: " + getTextFieldValue(R.id.create_user), Toast.LENGTH_SHORT).show();
					}

					@Override
					public void handleConnectionFailure() {
						throw new RuntimeException("connection failure?!");
					}

					@Override
					public void handleErrors(final EnumSet<CreateAccountError> errors) {
						Toast.makeText(getApplication(), "Login failed: " + errors.toString(), Toast.LENGTH_LONG).show();
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
						Toast.makeText(getApplication(), "Logged in as: " + getTextFieldValue(R.id.login_username), Toast.LENGTH_SHORT).show();
					}

					@Override
					public void handleConnectionFailure() {
						throw new RuntimeException("connection failure?!");
					}

					@Override
					public void handleErrors(final EnumSet<LoginError> errors) {
						Toast.makeText(getApplication(), "Login failed: " + errors.toString(), Toast.LENGTH_LONG).show();
					}

					@Override
					public void handleUnknownException(Throwable ex) {
						throw new RuntimeException(ex);
					}
				});
	}

	public void onContactsClick(View view) {
		client.allContacts(
				new ContactsHandler() {
					@Override
					public void handleConnectionFailure() {
						throw new RuntimeException("connection failure?!");
					}

					@Override
					public void handleUnknownException(Throwable ex) {
						throw new RuntimeException(ex);
					}

					@Override
					public void handleNeedsLogin() {
						Toast.makeText(getApplication(), "Needs login!", Toast.LENGTH_SHORT).show();
					}

					@Override
					public void handleSuccess(List<Contact> contacts) {
						Toast.makeText(getApplication(), "Contacts: " + contacts, Toast.LENGTH_SHORT).show();
					}
				});
	}
	
	public void onBlockedContactsClick(View view) {
		client.blockedContacts(
				new ContactsHandler() {
					@Override
					public void handleConnectionFailure() {
						throw new RuntimeException("connection failure?!");
					}

					@Override
					public void handleUnknownException(Throwable ex) {
						throw new RuntimeException(ex);
					}

					@Override
					public void handleNeedsLogin() {
						Toast.makeText(getApplication(), "Needs login!", Toast.LENGTH_SHORT).show();
					}

					@Override
					public void handleSuccess(List<Contact> contacts) {
						Toast.makeText(getApplication(), "Blocked contacts: " + contacts, Toast.LENGTH_SHORT).show();
					}
				});
	}
}
