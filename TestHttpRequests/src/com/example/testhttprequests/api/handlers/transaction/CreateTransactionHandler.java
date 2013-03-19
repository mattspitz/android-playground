package com.example.testhttprequests.api.handlers.transaction;

import java.util.EnumSet;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;

import com.example.testhttprequests.api.handlers.HootcasterApiErrorHandler;
import com.example.testhttprequests.api.handlers.HootcasterApiHandler;
import com.example.testhttprequests.api.handlers.HootcasterApiLoggedInHandler;
import com.example.testhttprequests.api.handlers.HootcasterErrorResponse;
import com.example.testhttprequests.api.handlers.transaction.CreateTransactionHandler.CreateTransactionError;

public interface CreateTransactionHandler extends HootcasterApiHandler, HootcasterApiErrorHandler<CreateTransactionError>, HootcasterApiLoggedInHandler {
	public void handleSuccess();
	
	public static enum CreateTransactionError {
		UNKNOWN("other");

		private final String value;
		private CreateTransactionError(String value) { this.value = value; }
		
		@JsonCreator
		public static CreateTransactionError fromValue(String value) {
			for (CreateTransactionError error : CreateTransactionError.values()) {
				if (error.value.equals(value))
					return error;
			}
			return UNKNOWN;
		}
	}
	
	public static final class CreateTransactionResponse extends HootcasterErrorResponse<Void, CreateTransactionError> {
		@JsonCreator
		private CreateTransactionResponse(
				@JsonProperty("okay") boolean okay,
				@JsonProperty("data") Void data,
				@JsonProperty("errors") EnumSet<CreateTransactionError> errors) {
			super(okay, data, errors);
		}
		
		public static Class<CreateTransactionResponse> getResponseClass() {
			return CreateTransactionResponse.class;
		}
	}
}
