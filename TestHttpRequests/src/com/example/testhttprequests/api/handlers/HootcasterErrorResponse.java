package com.example.testhttprequests.api.handlers;

import java.util.EnumSet;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;

public abstract class HootcasterErrorResponse<T, E extends Enum<E>> extends HootcasterResponse<T> {
	private final EnumSet<E> errors;
	
	@JsonCreator
	protected HootcasterErrorResponse(
		@JsonProperty("okay") final boolean okay,
		@JsonProperty("data") final T data,
		@JsonProperty("errors") final EnumSet<E> errors
			) {
		super(okay, data);
		this.errors = errors;
	}

	public EnumSet<E> getErrors() { return errors; }

	@Override
	public String toString() {
		return "HootcasterErrorResponse [okay=" + isOkay() + ", data=" + getData()
				+ ", errors=" + errors + "]";
	}
}
