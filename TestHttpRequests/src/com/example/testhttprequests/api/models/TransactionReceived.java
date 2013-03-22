package com.example.testhttprequests.api.models;

import java.util.Date;

public final class TransactionReceived extends Transaction {
	private final String username;
	private final int numSeconds;
	private final boolean viewed;
	
	public TransactionReceived(
			final String transactionId,
			final Date creationDate,
			final String username,
			final int numSeconds,
			final boolean viewed
			) {
		super(transactionId, creationDate);
		this.username = username;
		this.numSeconds = numSeconds;
		this.viewed = viewed;
	}

	public String getUsername() { return username; }
	public int getNumSeconds() { return numSeconds; }
	public boolean isViewed() { return viewed; }

	@Override
	public String toString() {
		return "TransactionReceived [username=" + username + ", numSeconds="
				+ numSeconds + ", viewed=" + viewed + ", transactionId="
				+ getTransactionId() + ", creationDate="
				+ getCreationDate() + "]";
	}
}
