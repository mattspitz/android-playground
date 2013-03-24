package com.example.testhttprequests.api.handlers.transaction;

import com.example.testhttprequests.api.handlers.HootcasterApiHandler;
import com.example.testhttprequests.api.handlers.HootcasterApiLoggedInHandler;

public interface ViewActionHandler extends HootcasterApiHandler, HootcasterApiLoggedInHandler {
	public void handleSuccess(byte[] imageData);
}
