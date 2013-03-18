package com.example.testhttprequests;

public interface HootcasterApiHandler {
	public void handleConnectionFailure();
	public void handleUnknownException(Throwable ex);
}
