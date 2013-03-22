package com.example.testhttprequests.api.models;

import java.util.Date;
import java.util.List;

public final class TransactionSent extends Transaction {
	private final List<TransactionResponse> responses;
	
	public TransactionSent(
			final String transactionId,
			final Date creationDate,
			final List<TransactionResponse> responses
			) {
		super(transactionId, creationDate);
		this.responses = responses;
	}

	public List<TransactionResponse> getResponses() { return responses; }

	@Override
	public String toString() {
		return "TransactionSent [responses=" + responses + ", transactionId="
				+ getTransactionId() + ", creationDate=" + getCreationDate() + "]";
	}
}
