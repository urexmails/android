package com.contactpoint.model.service.impl;

import org.json.JSONObject;

import com.contactpoint.controller.AsyncSoapTaskCompleteListener;
import com.contactpoint.controller.NetworkListener;
import com.contactpoint.model.DTO.azure.AzureLoginDTO;
import com.contactpoint.model.DTO.azure.AzureTokenDTO;
import com.contactpoint.model.service.AzureAuthenticationService;
import com.contactpoint.model.util.AzureAuthClient;

public class AzureAuthenticationServiceImpl implements 
AzureAuthenticationService, AsyncSoapTaskCompleteListener<String> {

	private AzureAuthClient mClient;
	private NetworkListener mListener;
	private int mOperation;
	
	private AzureLoginDTO mLoginInDTO;
	private AzureTokenDTO mTokenDTO;
	private boolean mIsUAT;

	@Override
	public void onTaskComplete(String result) {
		switch (mOperation) {
		case AZURE_LOGIN:
			//mListener.showResult(result);
			if (mClient.getMessage() != null) {
				mOperation = ERROR;
			} else if (result.compareTo(AUTHENTICATED) != 0) {
				mOperation = FAILURE;
			} else {
				AzureTokenDTO token = new AzureTokenDTO((JSONObject)mClient.getResponse());
				setAzureToken(token);
				//ModelFactory.getCIService().setAuthenticationToken(mLoginInDTO.token);
				//ModelFactory.getPRVService().setAuthenticationToken(mLoginInDTO.token);
				//ModelFactory.getPhotoService().setAuthenticationToken(mLoginInDTO.token);
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
		mClient = new AzureAuthClient(this);

		switch (mOperation) {
		case AZURE_LOGIN:
			mClient.setLoginRequest(mLoginInDTO);
			//mClient.setEnvelope(SoapEnvelopeGenerator.getSoapEnvelope(mLoginInDTO));
			mClient.execute();
			break;
		case AZURE_REFRESH:
			mClient.setRefreshRequest(mTokenDTO);
			mClient.execute();
			break;
		}
		return NetworkListener.SUCCESS;
	}

	@Override
	public void prepareLogin(AzureLoginDTO param) {
		mOperation = AZURE_LOGIN;
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

	@Override
	public AzureTokenDTO getAzureToken() {
		return mTokenDTO;
	}

	@Override
	public void setAzureToken(AzureTokenDTO param) {
		mTokenDTO = param;
	}

	@Override
	public String getAuthorizationHeader() {
		return "Bearer " + mTokenDTO.getAccessToken();
	}

	@Override
	public String getURL() {
		if (mIsUAT) {
			return AzureAuthClient.URL_MM_TEST;
		} else {
			return AzureAuthClient.URL_MM;
		}
	}

	@Override
	public void setIsUAT(boolean uat) {
		mIsUAT = uat;
	}
}
