package com.example.testhttprequests.account;

import java.util.EnumSet;

import org.codehaus.jackson.annotate.JsonCreator;

public interface CreateAccountHandler {
	public void handleSuccess();
	public void handleConnectionFailure();

	public void handleErrors(EnumSet<CreateAccountError> errors);

	public static enum CreateAccountError {
		USERNAME_EXISTS("username_exists"),
		EMAIL_EXISTS("email_exists"),
		PHONE_EXISTS("phone_exists"),
		REGISTRATION_ID_EXISTS("registration_id_exists");

		private final String value;
		private CreateAccountError(String value) { this.value = value; }
		
		@JsonCreator
		public static CreateAccountError fromValue(String value) {
			for (CreateAccountError loginError : CreateAccountError.values()) {
				if (loginError.value.equals(value))
					return loginError;
			}
			throw new RuntimeException("Couldn't find enum for value: " + value);
		}
	}
}