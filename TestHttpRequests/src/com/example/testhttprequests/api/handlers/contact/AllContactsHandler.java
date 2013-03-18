package com.example.testhttprequests.api.handlers.contact;

import java.util.List;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.type.TypeFactory;
import org.codehaus.jackson.type.JavaType;

import com.example.testhttprequests.api.handlers.HootcasterApiHandler;
import com.example.testhttprequests.api.handlers.HootcasterResponse;
import com.example.testhttprequests.api.models.Contact;

public abstract class AllContactsHandler implements HootcasterApiHandler {
	public abstract void handleSuccess(List<Contact> contacts);
	
	public static class AllContactsData {
		private final List<Contact> contacts;
		
		@JsonCreator
		public AllContactsData(
				@JsonProperty("contacts") final List<Contact> contacts
				) {
			this.contacts = contacts;
		}

		public List<Contact> getContacts() { return contacts; }
		
		@Override
		public String toString() { return "AllContactsData [contacts=" + contacts + "]"; }
	}
	
	public static final class AllContactsResponse extends HootcasterResponse<AllContactsData> {
		@JsonCreator
		private AllContactsResponse(
				@JsonProperty("okay") final boolean okay,
				@JsonProperty("data") final AllContactsData data) {
			super(okay, data);
		}
		
		@SuppressWarnings("deprecation")
		public static JavaType getResponseType() {
			return TypeFactory.parametricType(HootcasterResponse.class, AllContactsData.class, Void.class);
		}
	}
}
