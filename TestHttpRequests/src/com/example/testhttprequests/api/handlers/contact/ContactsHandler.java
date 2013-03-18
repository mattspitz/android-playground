package com.example.testhttprequests.api.handlers.contact;

import java.util.List;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;

import com.example.testhttprequests.api.handlers.HootcasterApiHandler;
import com.example.testhttprequests.api.handlers.HootcasterApiLoggedInHandler;
import com.example.testhttprequests.api.handlers.HootcasterResponse;
import com.example.testhttprequests.api.models.Contact;

public abstract class ContactsHandler implements HootcasterApiHandler, HootcasterApiLoggedInHandler {
	public abstract void handleSuccess(List<Contact> contacts);
	
	public static class ContactsData {
		private final List<Contact> contacts;
		
		@JsonCreator
		public ContactsData(
				@JsonProperty("contacts") final List<Contact> contacts
				) {
			this.contacts = contacts;
		}

		public List<Contact> getContacts() { return contacts; }
		
		@Override
		public String toString() { return "AllContactsData [contacts=" + contacts + "]"; }
	}
	
	public static final class ContactsResponse extends HootcasterResponse<ContactsData> {
		@JsonCreator
		private ContactsResponse(
				@JsonProperty("okay") final boolean okay,
				@JsonProperty("data") final ContactsData data) {
			super(okay, data);
		}
		
		public static Class<ContactsResponse> getResponseClass() {
			return ContactsResponse.class;
		}
	}
}
