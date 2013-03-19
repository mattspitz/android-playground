package com.example.testhttprequests.api.models;

import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;

/**
 * Use this with the FindContacts API.  This represents an email address and/or phone that lives in the user's contact list and may have a Hootcaster account.
 * @author mspitz
 */
@JsonSerialize(include=Inclusion.NON_NULL)
public final class PotentialContact {
	private final String email;
	private final String phone;

	public PotentialContact(
			final String email,
			final String phone
			) {
		this.email = email;
		this.phone = phone;
	}
	
	public String getEmail() { return email; }
	public String getPhone() { return phone; }

	@Override
	public String toString() {
		return "PotentialContact [email=" + email + ", phone=" + phone + "]";
	}
}
