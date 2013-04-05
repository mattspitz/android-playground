package com.example.testhttprequests.api.handlers.transaction;

import java.util.List;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;

import com.example.testhttprequests.api.handlers.BasicHandler;
import com.example.testhttprequests.api.handlers.LoggedInHandler;
import com.example.testhttprequests.api.handlers.Response;
import com.example.testhttprequests.api.models.Transaction;

public interface AllTransactionsHandler extends BasicHandler, LoggedInHandler {
	public void handleSuccess(List<Transaction> transactions);
	
	public static class AllTransactionsData {
		private final List<Transaction> transactions;
		
		@JsonCreator
		public AllTransactionsData(
				@JsonProperty("transactions") final List<Transaction> transactions
				) {
			this.transactions = transactions;
		}

		public List<Transaction> getTransactions() { return transactions; }
		
		@Override
		public String toString() { return "AllTransactionsData [transactions=" + transactions + "]"; }
	}
	
	public static final class AllTransactionsResponse extends Response<AllTransactionsData> {
		@JsonCreator
		private AllTransactionsResponse(
				@JsonProperty("okay") boolean okay,
				@JsonProperty("data") AllTransactionsData data) {
			super(okay, data);
		}
		
		public static Class<AllTransactionsResponse> getResponseClass() {
			return AllTransactionsResponse.class;
		}
	}
}
