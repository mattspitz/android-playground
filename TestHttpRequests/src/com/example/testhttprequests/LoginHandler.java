package com.example.testhttprequests;

import java.util.EnumSet;

public interface LoginHandler {
	public void handleSuccess();
	public void handleConnectionFailure();

	public void handleErrors(EnumSet<LoginError> errors);

	public static enum LoginError {
		USERNAME_EXISTS,
		EMAIL_EXISTS,
		PHONE_EXISTS,
		REGISTRATION_ID_EXISTS;
	}
}