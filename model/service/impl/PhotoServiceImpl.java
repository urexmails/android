package com.contactpoint.model.service.impl;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.Vector;

import org.json.JSONObject;

import android.content.Context;

import com.contactpoint.controller.AsyncSoapTaskCompleteListener;
import com.contactpoint.controller.NetworkListener;
import com.contactpoint.controller.PhotoController;
import com.contactpoint.model.ModelFactory;
import com.contactpoint.model.DTO.PhotoInDTO;
import com.contactpoint.model.DTO.azure.AzureTokenDTO;
import com.contactpoint.model.client.authentication.AuthenticationToken;
import com.contactpoint.model.client.prv.PhotoConfig;
import com.contactpoint.model.client.prv.ReferenceIDDropDown;
import com.contactpoint.model.service.AzureAuthenticationService;
import com.contactpoint.model.service.PhotoService;
import com.contactpoint.model.util.AzureAuthClient;
import com.contactpoint.model.util.InternalIO;
import com.contactpoint.model.util.SoapClient;
import com.contactpoint.model.util.SoapEnvelopeGenerator;
import com.google.gson.reflect.TypeToken;

public class PhotoServiceImpl implements PhotoService, AsyncSoapTaskCompleteListener<String> {

	private class SingletonHolder {
		Vector<PhotoInDTO> mPhotoQueue;
	}

	private AuthenticationToken mAuthenticationToken;
	private SoapClient mClient;
	private AzureAuthClient mAzureClient;
	private NetworkListener mListener;
	private int mOperation;
	private SingletonHolder mSingletonHolder;
	
	private final String CI_LIMIT_TYPE = "CI_MAX_PHOTO_COUNT";
	private final String PRV_LIMIT_TYPE = "PRV_MAX_PHOTO_COUNT";
	
	private int mTotalPhotoToBeUploaded;
	private int mTotalPhotoUploaded;
	private PhotoInDTO mNextUpload;
	
	private final String UPLOAD_MESSAGE = "Successfully uploaded %d out of %d photos";
	
	public PhotoServiceImpl()  {
		mSingletonHolder = new SingletonHolder();
	}
	
	@Override
	public void onTaskComplete(String result) {
		//mListener.showResult(result);
		
		if (mAzureClient != null) {
			// if we are refreshing successfully, execute the actual Soap request
			if (result.compareTo(AzureAuthenticationService.AUTHENTICATED) == 0) {
				AzureTokenDTO token = new AzureTokenDTO((JSONObject)mAzureClient.getResponse());
				ModelFactory.getAzureService().setAzureToken(token);

				mAzureClient = null;
				this.executeSoapRequest();
				return;
			} 
			// on unsuccessful refresh, move the error message from Azure client to the generic SoapClient
			else {
				mClient.setMessage(mAzureClient.getMessage());
			}
		}

		if (mClient.getMessage() != null) {
			mOperation = ERROR;
		}
		
		switch (mOperation) {
		case PHOTO_UPLOAD:
			//PhotoInDTO photo = getFirstToBeUploaded();
			mSingletonHolder.mPhotoQueue.remove(mNextUpload);
			PhotoController.deletePicture(mListener.getContext(), mNextUpload.getPhoto());
			//System.out.println("Uploaded: " + mNextUpload.getPhoto());
			mTotalPhotoUploaded++;
			break;
		case ERROR:
			//photo = getFirstToBeUploaded();
			mNextUpload.setFailUploaded(true);
			mOperation = PHOTO_UPLOAD;
			break;
		}
		mListener.callback(mOperation);
	}

	@Override
	public void onPreExecuteSoap() {
		mListener.showLoadingDialog(NetworkListener.LOADING_MSG);
	}

	@Override
	public void setNetworkListener(NetworkListener listener) { mListener = listener; }

	@Override
	public String getClientMessage() 	{ return mClient.getMessage(); }

	@Override
	public String getResponseMessage() 	{ return mClient.getResponse().toString(); }

	@Override
	public int executeSoapRequest() {
		if (!mListener.isOnline()) {
			return NetworkListener.ERR_OFFLINE;
		}

		// check if Azure token has expired
		AzureTokenDTO azureToken = ModelFactory.getAzureService().getAzureToken();
		Date now = new Date();
		if (azureToken.getExpiresOn() * 1000 < now.getTime()) {
			// if expired, intercept the business workflow to refresh the token
			mAzureClient = new AzureAuthClient(this);
			mAzureClient.setRefreshRequest(azureToken);
			mAzureClient.execute();
			return NetworkListener.SUCCESS;
		}
				
		mClient = null;
		mClient = new SoapClient(this);
		
		switch (mOperation) {
		case PHOTO_UPLOAD:
			//PhotoInDTO photo = getFirstToBeUploaded();
			mClient.setEnvelope(SoapEnvelopeGenerator.getPhotoEnvelope(mNextUpload, mAuthenticationToken));
			if (mAuthenticationToken.isMM()) {
				mClient.execute(PhotoInDTO.MM_ACTION, PATH_MM, ModelFactory.getAzureService().getURL());
			} else {
				mClient.execute(PhotoInDTO.ACTION, PATH);
			}
			break;
		}
		return NetworkListener.SUCCESS;
	}
	
	@Override
	public void setAuthenticationToken(AuthenticationToken param) {
		mAuthenticationToken = param;
	}
	
	@Override
	public void addPhotoInDTO(PhotoInDTO inDTO) {
		// first added, not yet to be uploaded
		inDTO.setToBeUploaded(false);
		inDTO.setFailUploaded(false);
		mSingletonHolder.mPhotoQueue.add(inDTO);
	}
	
	@Override
	public void removePhoto(Context context, String poNumber) {
		Vector<PhotoInDTO> toBeDeletedPhoto = new Vector<PhotoInDTO>();
		for (PhotoInDTO photo : mSingletonHolder.mPhotoQueue) {
			if (photo.getPONumber().compareTo(poNumber) == 0) {
				PhotoController.deletePicture(context, photo.getPhoto());
				toBeDeletedPhoto.add(photo);
			}
		}
		mSingletonHolder.mPhotoQueue.removeAll(toBeDeletedPhoto);
	}
	
	@Override
	public void clearPhoto(Context context) {
		Vector<PhotoInDTO> toBeDeletedPhoto = new Vector<PhotoInDTO>();
		for (PhotoInDTO photo : mSingletonHolder.mPhotoQueue) {
			PhotoController.deletePicture(context, photo.getPhoto());
			toBeDeletedPhoto.add(photo);
		}
		mSingletonHolder.mPhotoQueue.removeAll(toBeDeletedPhoto);
	}
	
	@Override
	public boolean hasPhotoToBeUploaded() {
		for (PhotoInDTO photo : mSingletonHolder.mPhotoQueue) {
			if (photo.toBeUploaded() && !photo.failUploaded()) {
				mNextUpload = photo;
				return true;
			}
		}
		mNextUpload = null;
		return false;
	}
	
	@Override
	public int getTotalPhoto(String poNumber) {
		int total = 0;
		for (PhotoInDTO photo : mSingletonHolder.mPhotoQueue) {
			if (photo.getPONumber().compareTo(poNumber) == 0) {
				total++;
			}
		}
		return total;
	}
	
	@Override
	public int getPhotoLimit(String appType) {
		boolean isCI = appType.compareTo(PhotoController.APP_TYPE_CI) == 0;
		boolean isPRV = appType.compareTo(PhotoController.APP_TYPE_PRV) == 0;
		boolean isMM = true;
		
		PhotoConfig config = null;
		
		if (isCI) {
			isMM = ModelFactory.getCIService().getCurrentForm().isMM();
		} else if (isPRV) {
			isMM = ModelFactory.getPRVService().getCurrentForm().isMM();
		} // else default to isMM = true
		
		config = ModelFactory.getPRVService().getReferenceDataOutDTO(isMM).photoConfig;
		int photoLimit = 0;
		for (ReferenceIDDropDown ref : config) {
			if ((isCI && ref.Type.compareTo(CI_LIMIT_TYPE) == 0) || 
				(isPRV && ref.Type.compareTo(PRV_LIMIT_TYPE) == 0)) {
				photoLimit = Integer.parseInt(ref.Description);
				break;
			}
		}
		return photoLimit;
	}
	
	@Override
	public void finalisePhoto(String poNumber) {
		for (PhotoInDTO photo : mSingletonHolder.mPhotoQueue) {
			if (photo.getPONumber().compareTo(poNumber) == 0) {
				photo.setToBeUploaded(true);
			}
		}
	}
	
	@Override
	public void initialisePhotoUpload() {
		mTotalPhotoToBeUploaded = 0;
		mTotalPhotoUploaded = 0;
		
		for (PhotoInDTO photo : mSingletonHolder.mPhotoQueue) {
			if (photo.toBeUploaded()) {
				photo.setFailUploaded(false);
				mTotalPhotoToBeUploaded++;
			}
		}
	}

	@Override
	public void saveFormData(Context context) {
		InternalIO.saveToInternalStorage(context, InternalIO.PHOTO, 
				mSingletonHolder.mPhotoQueue);
	}

	@Override
	public void loadFormData(Context context) {
		Vector<PhotoInDTO> temp = new Vector<PhotoInDTO>();
		Type collectionType = new TypeToken<Vector<PhotoInDTO>>(){}.getType();
		temp = InternalIO.loadFromInternalStorage(context, InternalIO.PHOTO, collectionType);
		if (temp != null) {
			mSingletonHolder.mPhotoQueue = temp;
		} else {
			mSingletonHolder.mPhotoQueue = new Vector<PhotoInDTO>();
		}
	}

	@Override
	public void preparePhotoUpload() {
		mOperation = PHOTO_UPLOAD;
		mAuthenticationToken.setIsMM(mNextUpload.isMM());
	}
	
	/*
	private PhotoInDTO getFirstToBeUploaded() {
		for (PhotoInDTO photo : mSingletonHolder.mPhotoQueue) {
			if (photo.toBeUploaded() && !photo.failUploaded()) {
				return photo;
			}
		}
		return null;
	}*/

	@Override
	public String getFinalMessage() {
		return String.format(UPLOAD_MESSAGE, mTotalPhotoUploaded, mTotalPhotoToBeUploaded);
	}

}
