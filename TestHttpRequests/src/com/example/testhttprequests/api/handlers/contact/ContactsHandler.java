package com.example.testhttprequests.api.handlers.contact;

import java.util.List;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;

import com.example.testhttprequests.api.handlers.BasicHandler;
import com.example.testhttprequests.api.handlers.LoggedInHandler;
import com.example.testhttprequests.api.handlers.Response;
import com.example.testhttprequests.api.models.Contact;

public interface ContactsHandler extends BasicHandler, LoggedInHandler {
	public void handleSuccess(List<Contact> contacts);
	
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
		public String toString() { return "ContactsData [contacts=" + contacts + "]"; }
	}
	
	public static final class ContactsResponse extends Response<ContactsData> {
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
