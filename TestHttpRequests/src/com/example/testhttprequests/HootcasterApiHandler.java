package com.example.testhttprequests;

import java.util.EnumSet;

public interface HootcasterApiHandler<T extends Enum<T>> {
	public void handleErrors(EnumSet<T> errors);
	
	public void handleConnectionFailure();
	public void handleUnknownException(Throwable ex);
}
