package com.example.testhttprequests.api.handlers;

import java.util.EnumSet;

public interface HootcasterApiErrorHandler<T extends Enum<T>> {
	public void handleErrors(EnumSet<T> errors);
}
