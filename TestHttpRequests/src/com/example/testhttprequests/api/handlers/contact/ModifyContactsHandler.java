package com.example.testhttprequests.api.handlers.contact;

import java.util.EnumSet;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;

import com.example.testhttprequests.api.handlers.ErrorHandler;
import com.example.testhttprequests.api.handlers.BasicHandler;
import com.example.testhttprequests.api.handlers.LoggedInHandler;
import com.example.testhttprequests.api.handlers.ErrorResponse;
import com.example.testhttprequests.api.handlers.contact.ModifyContactsHandler.ModifyContactsError;

public interface ModifyContactsHandler extends BasicHandler, ErrorHandler<ModifyContactsError>, LoggedInHandler {
	public void handleSuccess();

	public static enum ModifyContactsError {
		UNKNOWN("other");

		private final String value;
		private ModifyContactsError(String value) { this.value = value; }

		@JsonCreator
		public static ModifyContactsError fromValue(String value) {
			for (ModifyContactsError error : ModifyContactsError.values()) {
				if (error.value.equals(value))
					return error;
			}
			return UNKNOWN;
		}
	}

	public static final class ModifyContactsResponse extends ErrorResponse<Void, ModifyContactsError> {
		@JsonCreator
		private ModifyContactsResponse(
				@JsonProperty("okay") boolean okay,
				@JsonProperty("data") Void data,
				@JsonProperty("errors") EnumSet<ModifyContactsError> errors) {
			super(okay, data, errors);
		}

		public static Class<ModifyContactsResponse> getResponseClass() {
			return ModifyContactsResponse.class;
		}
	}
}
