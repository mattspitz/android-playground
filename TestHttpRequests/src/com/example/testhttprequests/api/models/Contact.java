package com.example.testhttprequests.api.models;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;

public final class Contact {
	private final String fullname;
	private final String username;
	
	@JsonCreator
	private Contact(
		@JsonProperty("fullname") final String fullname,
		@JsonProperty("username") final String username
			) {
		this.fullname = fullname;
		this.username = username;
	}

	public String getFullname() { return fullname; }
	public String getUsername() { return username; }

	@Override
	public String toString() {
		return "Contact [fullname=" + fullname + ", username=" + username + "]";
	}
}
