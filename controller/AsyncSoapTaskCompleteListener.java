package com.contactpoint.controller;

public interface AsyncSoapTaskCompleteListener<T> {

	public void onTaskComplete(T result);
	public void onPreExecuteSoap();
}
