package com.example.testhttprequests.api.handlers.transaction;

import java.util.EnumSet;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;

import com.example.testhttprequests.api.handlers.HootcasterApiErrorHandler;
import com.example.testhttprequests.api.handlers.HootcasterApiHandler;
import com.example.testhttprequests.api.handlers.HootcasterApiLoggedInHandler;
import com.example.testhttprequests.api.handlers.HootcasterErrorResponse;
import com.example.testhttprequests.api.handlers.transaction.CreateReactionHandler.CreateReactionError;

public interface CreateReactionHandler extends HootcasterApiHandler, HootcasterApiErrorHandler<CreateReactionError>, HootcasterApiLoggedInHandler {
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

	public static final class CreateReactionResponse extends HootcasterErrorResponse<Void, CreateReactionError> {
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