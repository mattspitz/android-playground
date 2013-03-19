package com.example.testhttprequests.api.handlers.contact;

import java.util.List;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;

import com.example.testhttprequests.api.handlers.HootcasterApiHandler;
import com.example.testhttprequests.api.handlers.HootcasterApiLoggedInHandler;
import com.example.testhttprequests.api.handlers.HootcasterResponse;
import com.example.testhttprequests.api.models.MatchedContact;

public interface FindContactsHandler extends HootcasterApiHandler, HootcasterApiLoggedInHandler {
	public void handleSuccess(List<MatchedContact> matchedContacts);
	
	public static class MatchedContactsData {
		private final List<MatchedContact> matchedContacts;
		
		@JsonCreator
		public MatchedContactsData(
				@JsonProperty("matches") final List<MatchedContact> contacts
				) {
			this.matchedContacts = contacts;
		}

		public List<MatchedContact> getMatchedContacts() { return matchedContacts; }
		
		@Override
		public String toString() { return "MatchedContactsData [matchedContacts=" + matchedContacts + "]"; }
	}
	
	public static final class FindContactsResponse extends HootcasterResponse<MatchedContactsData> {
		@JsonCreator
		private FindContactsResponse(
				@JsonProperty("okay") final boolean okay,
				@JsonProperty("data") final MatchedContactsData data) {
			super(okay, data);
		}
		
		public static Class<FindContactsResponse> getResponseClass() {
			return FindContactsResponse.class;
		}
	}
}
