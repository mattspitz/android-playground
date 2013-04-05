package com.example.testhttprequests.api.handlers.transaction;

import com.example.testhttprequests.api.handlers.BasicHandler;
import com.example.testhttprequests.api.handlers.LoggedInHandler;

public interface ViewActionHandler extends BasicHandler, LoggedInHandler {
	public void handleSuccess(byte[] imageData);
}
