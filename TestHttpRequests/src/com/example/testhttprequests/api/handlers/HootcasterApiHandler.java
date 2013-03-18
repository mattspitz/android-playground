package com.example.testhttprequests.api.handlers;


public interface HootcasterApiHandler {	
	public void handleConnectionFailure();
	public void handleUnknownException(Throwable ex);
}