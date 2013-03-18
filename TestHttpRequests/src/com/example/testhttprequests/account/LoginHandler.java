package com.example.testhttprequests.account;

import java.util.EnumSet;

import org.codehaus.jackson.annotate.JsonCreator;

import com.example.testhttprequests.HootcasterApiHandler;

public interface LoginHandler extends HootcasterApiHandler {
	public void handleSuccess();
	public void handleErrors(EnumSet<LoginError> errors);

	public static enum LoginError {
		INVALID_LOGIN("invalid_login"),
		
		UNKNOWN("other");

		private final String value;
		private LoginError(String value) { this.value = value; }
		
		@JsonCreator
		public static LoginError fromValue(String value) {
			for (LoginError loginError : LoginError.values()) {
				if (loginError.value.equals(value))
					return loginError;
			}
			return UNKNOWN;
		}
	}
}