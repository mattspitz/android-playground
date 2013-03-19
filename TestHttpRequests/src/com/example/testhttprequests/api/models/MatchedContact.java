package com.example.testhttprequests.api.models;

import java.util.Collection;
import java.util.List;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;

import com.google.common.collect.ImmutableList;

public final class MatchedContact {
	private final String username;
	private final List<String> matchedTags;
	
	@JsonCreator
	private MatchedContact(
		@JsonProperty("username") final String username,
		@JsonProperty("matched_tags") final Collection<String> matchedTags
			) {
		this.username = username;
		this.matchedTags = ImmutableList.copyOf(matchedTags);
	}

	public String getUsername() { return username; }
	public List<String> getMatchedTags() { return matchedTags; }

	@Override
	public String toString() {
		return "MatchedContact [username=" + username + ", matchedTags=" + matchedTags + "]";
	}
	
}
