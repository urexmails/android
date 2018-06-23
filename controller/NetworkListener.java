package com.contactpoint.controller;

import android.content.Context;

public interface NetworkListener {
	public static final int ERR_OFFLINE = -1;
	public static final int SUCCESS = 0;
	public static final String AUTH_ERR_MSG = "Authentication Failure";
	
	public final int LOADING_MSG = 0;
	public final int UPDATING_MSG = 1;
	
	public void showLoadingDialog(int message);
	public void onTaskComplete();
	public void showErrorDialog(String errorMessage);
	public void showResult(String result);
	public void logoutCallback();
	public boolean isOnline();
	public void callback(int param);
	public Context getContext();
}
