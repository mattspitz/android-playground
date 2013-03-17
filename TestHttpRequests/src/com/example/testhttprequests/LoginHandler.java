package com.example.testhttprequests;

public interface LoginHandler {
	public void handleSuccess();
	public void handleConnectionFailure();

	public void handleUsernameAlreadyExists();
	public void handleEmailAlreadyExists();
	public void handleRegistrationIdAlreadyExists();
}