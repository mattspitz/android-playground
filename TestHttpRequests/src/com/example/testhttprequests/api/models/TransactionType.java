package com.example.testhttprequests.api.models;

import org.codehaus.jackson.annotate.JsonCreator;

public enum TransactionType {
	SENT(0),
	RECEIVED(1);
	
	private final int value;
	private TransactionType(final int value) {
		this.value = value;
	}

	@JsonCreator
	public static TransactionType fromValue(int value) {
		for (TransactionType error : TransactionType.values()) {
			if (error.value == value)
				return error;
		}
		throw new RuntimeException("Unrecognized transaction type: " + value);
	}
	
}
