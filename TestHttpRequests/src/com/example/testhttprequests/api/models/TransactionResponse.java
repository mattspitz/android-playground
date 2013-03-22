package com.example.testhttprequests.api.models;

import java.util.Date;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;

public final class TransactionResponse {
	
	private final String username;
	private final Date creationTime;
	private final boolean viewedBySender;
	
	@JsonCreator
	private TransactionResponse(
			@JsonProperty("username") final String username,
			@JsonProperty("created_ts") final Long createdTs,
			@JsonProperty("viewed_by_sender") final boolean viewedBySender) {
		this.username = username;
		this.creationTime = createdTs != null ? new Date(createdTs * 1000) : null;
		this.viewedBySender = viewedBySender;
	}

	public String getUsername() { return username; }
	public Date getCreationTime() { return creationTime; }
	public boolean isViewedBySender() { return viewedBySender; }

	@Override
	public String toString() {
		return "TransactionResponse [username=" + username + 
				", creationTime=" + creationTime
				+ ", viewedBySender=" + viewedBySender + "]";
	}
}
