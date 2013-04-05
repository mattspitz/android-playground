package com.example.testhttprequests.api.handlers;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;

public abstract class Response<T> {
	private final boolean okay;
	private final T data;
	
	@JsonCreator
	protected Response(
		@JsonProperty("okay") final boolean okay,
		@JsonProperty("data") final T data
			) {
		this.okay = okay;
		this.data = data;
	}

	public boolean isOkay() { return okay; }
	public T getData() { return data; }

	@Override
	public String toString() {
		return "Response [okay=" + okay + ", data=" + data + "]";
	}
}
