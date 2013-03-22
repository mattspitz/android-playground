package com.example.testhttprequests.api.models;

import java.util.Date;
import java.util.List;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;

public abstract class Transaction {

	private final String transactionId;
	private final Date creationDate;

	protected Transaction(
			final String transactionId,
			final Date creationDate
			) {
		this.transactionId = transactionId;
		this.creationDate = creationDate;
	}

	public String getTransactionId() { return transactionId; }
	public Date getCreationDate() { return creationDate; }

	@JsonCreator
	public static Transaction fromJson(
			@JsonProperty("transaction_type") final TransactionType transactionType,
			@JsonProperty("transaction_id") final String transactionId,
			@JsonProperty("created_ts") final long createdTs,

			@JsonProperty("username") final String username,
			@JsonProperty("num_seconds") final Integer numSeconds,
			@JsonProperty("viewed") final Boolean viewed,

			@JsonProperty("responses") final List<TransactionResponse> responses
			) {

		final Date creationDate = new Date(createdTs * 1000);
		
		switch (transactionType) {
		case RECEIVED:
			return new TransactionReceived(transactionId, creationDate, username, numSeconds, viewed);
		case SENT:
			return new TransactionSent(transactionId, creationDate, responses);
		default:
			throw new RuntimeException("Unhandled transaction type: " + transactionType);
		}
	}
}
