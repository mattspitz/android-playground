package com.example.testhttprequests.api.models;

/**
 * @author mspitz
 * Represents the type of action we're creating (e.g. photo capture or imported image).  Keep this in sync with ActionType on the python side of things.
 */
public enum ActionType {
	PHOTO_CAPTURE(0);
	
	private final int value;
	
	private ActionType(final int value) {
		this.value = value;
	}
	public int getVal() { return value; }
}
