package com.contactpoint.model.service.impl;

import com.contactpoint.controller.AsyncSoapTaskCompleteListener;
import com.contactpoint.controller.NetworkListener;
import com.contactpoint.model.ModelFactory;
import com.contactpoint.model.client.authentication.RequestLogin;
import com.contactpoint.model.service.AuthenticationService;
import com.contactpoint.model.util.SoapClient;
import com.contactpoint.model.util.SoapEnvelopeGenerator;

public class AuthenticationServiceImpl implements 
AuthenticationService, AsyncSoapTaskCompleteListener<String> {

	private SoapClient mClient;
	private NetworkListener mListener;
	private int mOperation;
	
	private RequestLogin mLoginInDTO;

	@Override
	public void onTaskComplete(String result) {
		switch (mOperation) {
		case LOGIN:
			//mListener.showResult(result);
			if (mClient.getMessage() != null) {
				mOperation = ERROR;
			} else if (result.compareTo(AUTHENTICATED) != 0) {
				mOperation = FAILURE;
			} else {
				ModelFactory.getCIService().setAuthenticationToken(mLoginInDTO.token);
				ModelFactory.getPRVService().setAuthenticationToken(mLoginInDTO.token);
				ModelFactory.getPhotoService().setAuthenticationToken(mLoginInDTO.token);
			}
			break;
		}
		mListener.callback(mOperation);
	}

	@Override
	public void onPreExecuteSoap() {
		mListener.showLoadingDialog(NetworkListener.LOADING_MSG);
	}

	@Override
	public void setNetworkListener(NetworkListener listener) {
		mListener = listener;
	}

	@Override
	public int executeSoapRequest() {
		if (!mListener.isOnline()) {
			return NetworkListener.ERR_OFFLINE;
		}

		mClient = null;
		mClient = new SoapClient(this);

		switch (mOperation) {
		case LOGIN:
			mClient.setEnvelope(SoapEnvelopeGenerator.getSoapEnvelope(mLoginInDTO));
			mClient.execute("http://TMSConsultant.Model/2013/TMSAuthentication/Authentication/AuthenticateUser", PATH);
			break;
		}
		return NetworkListener.SUCCESS;
	}

	@Override
	public void prepareLogin(RequestLogin param) {
		mOperation = LOGIN;
		mLoginInDTO = param;
	}

	@Override
	public String getResponseMessage() {
		return mClient.getResponse().toString();
	}

	@Override
	public String getClientMessage() {
		return mClient.getMessage();
	}

}
