package com.example.testhttprequests.api.handlers;

import java.util.EnumSet;

public interface ErrorHandler<T extends Enum<T>> {
	public void handleErrors(EnumSet<T> errors);
}
