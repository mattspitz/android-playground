package com.example.testhttprequests;

import java.util.EnumSet;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;

public final class HootcasterResponse<T, E extends Enum<E>> {
	private final boolean okay;
	private final T data;
	private final EnumSet<E> errors;
	
	@JsonCreator
	private HootcasterResponse(
		@JsonProperty("okay") final boolean okay,
		@JsonProperty("data") final T data,
		@JsonProperty("errors") final EnumSet<E> errors
			) {
		this.okay = okay;
		this.data = data;
		this.errors = errors;
	}

	public boolean isOkay() { return okay; }
	public T getData() { return data; }
	public EnumSet<E> getErrors() { return errors; }

	@Override
	public String toString() {
		return "HootcasterResponse [okay=" + okay + ", data=" + data
				+ ", errors=" + errors + "]";
	}
}
