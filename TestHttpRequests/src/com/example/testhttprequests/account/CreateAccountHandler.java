package com.example.testhttprequests.account;

import java.util.EnumSet;

import org.codehaus.jackson.annotate.JsonCreator;

import com.example.testhttprequests.HootcasterApiHandler;

public interface CreateAccountHandler extends HootcasterApiHandler {
	public void handleSuccess();
	public void handleErrors(EnumSet<CreateAccountError> errors);

	public static enum CreateAccountError {
		USERNAME_INVALID("username_invalid"),
		PASSWORD_INVALID("password_invalid"),
		EMAIL_INVALID("email_invalid"),
		REGISTRATION_ID_INVALID("registration_id_invalid"),
		
		USERNAME_EXISTS("username_exists"),
		EMAIL_EXISTS("email_exists"),
		PHONE_EXISTS("phone_exists"),
		REGISTRATION_ID_EXISTS("registration_id_exists"),
		
		UNKNOWN("other");

		private final String value;
		private CreateAccountError(String value) { this.value = value; }
		
		@JsonCreator
		public static CreateAccountError fromValue(String value) {
			for (CreateAccountError loginError : CreateAccountError.values()) {
				if (loginError.value.equals(value))
					return loginError;
			}
			return UNKNOWN;
		}
	}
}