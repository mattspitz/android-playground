package com.example.testhttprequests.api.handlers.account;

import java.util.EnumSet;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;

import com.example.testhttprequests.api.handlers.HootcasterApiErrorHandler;
import com.example.testhttprequests.api.handlers.HootcasterApiHandler;
import com.example.testhttprequests.api.handlers.HootcasterErrorResponse;
import com.example.testhttprequests.api.handlers.account.LoginHandler.LoginError;

public abstract class LoginHandler implements HootcasterApiHandler, HootcasterApiErrorHandler<LoginError> {
	public abstract void handleSuccess();
	
	public static enum LoginError {
		INVALID_LOGIN("invalid_login"),
		
		UNKNOWN("other");

		private final String value;
		private LoginError(String value) { this.value = value; }
		
		@JsonCreator
		public static LoginError fromValue(String value) {
			for (LoginError error : LoginError.values()) {
				if (error.value.equals(value))
					return error;
			}
			return UNKNOWN;
		}
	}
	
	public static final class LoginResponse extends HootcasterErrorResponse<Void, LoginError> {
		@JsonCreator
		private LoginResponse(
				@JsonProperty("okay") boolean okay,
				@JsonProperty("data") Void data,
				@JsonProperty("errors") EnumSet<LoginError> errors) {
			super(okay, data, errors);
		}
		
		public static Class<LoginResponse> getResponseClass() {
			return LoginResponse.class;
		}
	}
}