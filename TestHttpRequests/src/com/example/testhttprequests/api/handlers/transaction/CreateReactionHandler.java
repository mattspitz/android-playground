package com.example.testhttprequests.api.handlers.transaction;

import java.util.EnumSet;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;

import com.example.testhttprequests.api.handlers.ErrorHandler;
import com.example.testhttprequests.api.handlers.BasicHandler;
import com.example.testhttprequests.api.handlers.LoggedInHandler;
import com.example.testhttprequests.api.handlers.ErrorResponse;
import com.example.testhttprequests.api.handlers.transaction.CreateReactionHandler.CreateReactionError;

public interface CreateReactionHandler extends BasicHandler, ErrorHandler<CreateReactionError>, LoggedInHandler {
	public void handleSuccess();

	public static enum CreateReactionError {
		UNKNOWN("other");

		private final String value;
		private CreateReactionError(String value) { this.value = value; }

		@JsonCreator
		public static CreateReactionError fromValue(String value) {
			for (CreateReactionError error : CreateReactionError.values()) {
				if (error.value.equals(value))
					return error;
			}
			return UNKNOWN;
		}
	}

	public static final class CreateReactionResponse extends ErrorResponse<Void, CreateReactionError> {
		@JsonCreator
		private CreateReactionResponse(
				@JsonProperty("okay") boolean okay,
				@JsonProperty("data") Void data,
				@JsonProperty("errors") EnumSet<CreateReactionError> errors) {
			super(okay, data, errors);
		}

		public static Class<CreateReactionResponse> getResponseClass() {
			return CreateReactionResponse.class;
		}
	}
}