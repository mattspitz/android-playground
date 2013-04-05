package com.example.testhttprequests.api.handlers;

public interface BasicHandler {	
	public void handleConnectionFailure();
	public void handleUnknownException(Throwable ex);
}