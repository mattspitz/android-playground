package com.example.testhttprequests.api.models;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;

public final class Contact {
	private final String displayName;
	private final String username;
	
	@JsonCreator
	private Contact(
		@JsonProperty("display_name") final String displayName,
		@JsonProperty("username") final String username
			) {
		this.displayName = displayName;
		this.username = username;
	}

	public String getDisplayName() { return displayName; }
	public String getUsername() { return username; }

	@Override
	public String toString() {
		return "Contact [displayName=" + displayName + ", username=" + username + "]";
	}
}
