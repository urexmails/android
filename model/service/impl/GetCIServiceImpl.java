package com.contactpoint.model.service.impl;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import org.json.JSONObject;
import org.kobjects.base64.Base64;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.location.Location;

import com.contactpoint.controller.AsyncSoapTaskCompleteListener;
import com.contactpoint.controller.NetworkListener;
import com.contactpoint.model.ModelFactory;
import com.contactpoint.model.DTO.LocationInDTO;
import com.contactpoint.model.DTO.azure.AzureTokenDTO;
import com.contactpoint.model.DTO.ci.DeleteInDTO;
import com.contactpoint.model.DTO.ci.DownloadInDTO;
import com.contactpoint.model.DTO.ci.SearchCIInDTO;
import com.contactpoint.model.DTO.ci.UploadInDTO;
import com.contactpoint.model.client.CIForm;
import com.contactpoint.model.client.authentication.AuthenticationToken;
import com.contactpoint.model.client.ci.CIDownload;
import com.contactpoint.model.client.ci.CIResult;
import com.contactpoint.model.client.ci.VectorCIResult;
import com.contactpoint.model.service.AzureAuthenticationService;
import com.contactpoint.model.service.GetCIService;
//import com.contactpoint.model.service.PRVService;
import com.contactpoint.model.util.AzureAuthClient;
import com.contactpoint.model.util.CIFormComparator;
import com.contactpoint.model.util.InternalIO;
import com.contactpoint.model.util.MyLocation;
import com.contactpoint.model.util.MyLocation.LocationResult;
import com.contactpoint.model.util.SoapClient;
import com.contactpoint.model.util.SoapEnvelopeGenerator;
import com.contactpoint.view.fragment.SearchCIResultFragment;
import com.contactpoint.view.fragment.WorkListFragment;
import com.google.gson.reflect.TypeToken;

public class GetCIServiceImpl implements GetCIService, AsyncSoapTaskCompleteListener<String> {
	
	private static class SingletonHolder {
		static Vector<CIForm> mUpliftInDTO;
		static Vector<CIForm> mDeliveryInDTO;
		static Vector<CIForm> mDeleteQueue;
		static Vector<CIForm> mUploadQueue;
		static Vector<CIResult> mDownloadQueue;
		static Vector<CIResult> mUpliftResult;
		static Vector<CIResult> mDeliveryResult;
		
		static SearchCIInDTO mSearchInDTO;
		static DeleteInDTO mDeleteInDTO;
		static CIForm mCurrentForm;
		static DownloadInDTO mDownloadInDTO;

	}
	private SoapClient mClient;
	private AzureAuthClient mAzureClient;
	private NetworkListener mListener;
	private int mOperation;
		
	private AuthenticationToken mAuthenticationToken;
	
	private MyLocation myLocation;
	private CIFormComparator mUpliftFormComparator;
	private CIFormComparator mDeliveryFormComparator;
	
	@Override
	public void setNetworkListener(NetworkListener listener) { mListener = listener; }

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
		case SEARCH:
			mAuthenticationToken.setIsMM(false);
			mClient.setEnvelope(SoapEnvelopeGenerator.getSearchCISoapEnvelope(
					SingletonHolder.mSearchInDTO, mAuthenticationToken));
			mClient.execute(SingletonHolder.mSearchInDTO.ACTION, PATH);
			break;
		case MM_SEARCH:
			mAuthenticationToken.setIsMM(true);
			mClient.setEnvelope(SoapEnvelopeGenerator.getSearchCISoapEnvelope(
					SingletonHolder.mSearchInDTO, mAuthenticationToken));
			mClient.execute(SingletonHolder.mSearchInDTO.MM_ACTION, PATH_MM, ModelFactory.getAzureService().getURL());
			break;
		case DOWNLOAD:
			mClient.setEnvelope(SoapEnvelopeGenerator.getDownloadCIEnvelope(
					SingletonHolder.mDownloadInDTO, mAuthenticationToken));
			if (mAuthenticationToken.isMM()) {
				mClient.execute(SingletonHolder.mDownloadInDTO.MM_ACTION, PATH_MM, ModelFactory.getAzureService().getURL());
			} else {
				mClient.execute(SingletonHolder.mDownloadInDTO.ACTION, PATH);
			}
			break;
		case DELETE:
			mClient.setEnvelope(SoapEnvelopeGenerator.getDeleteCIEnvelope(
					SingletonHolder.mDeleteInDTO, mAuthenticationToken));
			if (mAuthenticationToken.isMM()) {
				mClient.execute(SingletonHolder.mDeleteInDTO.MM_ACTION, PATH_MM, ModelFactory.getAzureService().getURL());
			} else {
				mClient.execute(SingletonHolder.mDeleteInDTO.ACTION, PATH);
			}
			break;
		case UPLOAD:
			// resize signature to 100 x 100
			for (Map.Entry<String, String> entry : SingletonHolder.mCurrentForm.getUpload().getSignatureContract().entrySet()) {
				SingletonHolder.mCurrentForm.getUpload().getSignatureContract().put(entry.getKey(), getResizedSignature(entry.getValue(), 100, 100));
			}
			
			mClient.setEnvelope(SoapEnvelopeGenerator.getUploadCIEnvelope(
					SingletonHolder.mCurrentForm, mAuthenticationToken));
			if (mAuthenticationToken.isMM()) {
				mClient.execute(UploadInDTO.MM_ACTION, PATH_MM, ModelFactory.getAzureService().getURL());
			} else {
				mClient.execute(UploadInDTO.ACTION, PATH);
			}
			break;
		}
		return NetworkListener.SUCCESS;
	}
	
	@Override
	public void onTaskComplete(String result) {
		//mListener.showResult(result);
		
		// check if we had to refresh the azure token
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
		case SEARCH:
			
			SingletonHolder.mUpliftResult.removeAllElements();
			SingletonHolder.mDeliveryResult.removeAllElements();
			
			VectorCIResult vector = new VectorCIResult(mClient.getResponseAsObject());
			//if (vector.size() == 0) {
			//	mOperation = RESULT_EMPTY;
			//	break;
			//}
			
			for (CIResult ciResult : vector) {
				ciResult.isMM = false;	// this CI comes from TMSR
				if (ModelFactory.getUtilService().isUplift(ciResult.cIType)) {
					SingletonHolder.mUpliftResult.add(ciResult);
				} else if (ModelFactory.getUtilService().isDelivery(ciResult.cIType)) {
					SingletonHolder.mDeliveryResult.add(ciResult);
				}
			}
			break;
		case MM_SEARCH:
			
			// If TMS is not enabled, clear previous results here
			if (!AzureAuthClient.ENABLE_TMS) {
				SingletonHolder.mUpliftResult.removeAllElements();
				SingletonHolder.mDeliveryResult.removeAllElements();
			}
			
			vector = new VectorCIResult(mClient.getResponseAsObject());
			for (CIResult ciResult : vector) {
				ciResult.isMM = true;	// this CI comes from MoveMaestro
				if (ModelFactory.getUtilService().isUplift(ciResult.cIType)) {
					SingletonHolder.mUpliftResult.add(ciResult);
				} else if (ModelFactory.getUtilService().isDelivery(ciResult.cIType)) {
					SingletonHolder.mDeliveryResult.add(ciResult);
				}
			}
			
			if (SingletonHolder.mUpliftResult.size() == 0 && SingletonHolder.mDeliveryResult.size() == 0) {
				mOperation = RESULT_EMPTY;
			}
			break;
		case DOWNLOAD:
			// initialize CIDownload and UploadInDTO
			CIDownload val = new CIDownload(mClient.getResponseAsObject());
			UploadInDTO in = new UploadInDTO();
			in.setQuestionAnswers(new HashMap<String, String>());
			in.setSignatureContract(new HashMap<String, String>());
			
			CIForm form = new CIForm();
			form.setDownload(val);
			form.setUpload(in);
			form.setIsMM(mAuthenticationToken.isMM());
			CIResult removed = SingletonHolder.mDownloadQueue.remove(0);
			if (form.isUplift()) {
				SingletonHolder.mUpliftInDTO.add(form);	
				SingletonHolder.mUpliftResult.remove(removed);
				mOperation = SearchCIResultFragment.UPLIFT_DOWNLOAD;
			} else if (form.isDelivery()) {
				SingletonHolder.mDeliveryInDTO.add(form);				
				SingletonHolder.mDeliveryResult.remove(removed);
				mOperation = SearchCIResultFragment.DELIVERY_DOWNLOAD;
			}
			removed = null;
			
			break;
		case DELETE:
			CIForm target = null;
			if (result.compareTo(DeleteInDTO.DELETE_SUCCESSFUL) == 0) {
				target = SingletonHolder.mDeleteQueue.remove(0);
				ModelFactory.getPhotoService().removePhoto(mListener.getContext(), target.getDownload().poNumber);
				if (target.isUplift()) {
					SingletonHolder.mUpliftInDTO.remove(target);
					mOperation = WorkListFragment.UPLIFT_TARGET_DELETE;
				} else if (target.isDelivery()) {
					SingletonHolder.mDeliveryInDTO.remove(target);
					mOperation = WorkListFragment.DELIVERY_TARGET_DELETE;
				}
				target = null;
			} else {
				mClient.setMessage(result);
				mOperation = GetCIService.ERROR;
			}
			break;
		case UPLOAD:
			if (result.compareTo(UploadInDTO.UPLOAD_SUCCESSFUL) == 0) {
				target = SingletonHolder.mUploadQueue.remove(0);
				if (target.isUplift()) {
					SingletonHolder.mUpliftInDTO.remove(target);
					mOperation = WorkListFragment.UPLIFT_TARGET_UPLOAD;
				} else if (target.isDelivery()) {
					SingletonHolder.mDeliveryInDTO.remove(target);
					mOperation = WorkListFragment.DELIVERY_TARGET_UPLOAD;
				}
				target = null;
			} else {
				mClient.setMessage(result);
				mOperation = GetCIService.ERROR;
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
	public String getClientMessage() 			 { return mClient.getMessage(); }

	@Override
	public Vector<CIResult> getDeliveryResults() { return SingletonHolder.mDeliveryResult; }
	
	@Override
	public Vector<CIResult> getUpliftResults()	 { return SingletonHolder.mUpliftResult; }

	@Override
	public Vector<CIForm> getDeliveryForms()	 { return SingletonHolder.mDeliveryInDTO; }
	
	@Override
	public Vector<CIForm> getUpliftForms()		 { return SingletonHolder.mUpliftInDTO; }
	
	@Override
	public Vector<CIForm> getDeleteQueue() 		 { return SingletonHolder.mDeleteQueue; }
	
	@Override
	public Vector<CIForm> getUploadQueue() 		 { return SingletonHolder.mUploadQueue; }
	
	@Override
	public Vector<CIResult> getDownloadQueue() 	 { return SingletonHolder.mDownloadQueue; }
	
	@Override
	public CIForm getCurrentForm()				 { return SingletonHolder.mCurrentForm; }

	@Override
	public void saveFormData(Context context) {
		InternalIO.saveToInternalStorage(context, InternalIO.DELIVERY_FORM, 
				SingletonHolder.mDeliveryInDTO);
		InternalIO.saveToInternalStorage(context, InternalIO.UPLIFT_FORM, 
				SingletonHolder.mUpliftInDTO);
	}
	
	@Override
	public void loadFormData(Context context) {
		/*
		Object obj = InternalIO.loadFromInternalStorage(context, InternalIO.UPLIFT_FORM);
		if (obj != null) {
			SingletonHolder.mUpliftInDTO = (Vector<CIForm>)obj;
		}
		
		obj = InternalIO.loadFromInternalStorage(context, InternalIO.DELIVERY_FORM);
		if (obj != null) {
			SingletonHolder.mDeliveryInDTO = (Vector<CIForm>)obj;
		}*/
		Vector<CIForm> temp = new Vector<CIForm>();
		Type collectionType = new TypeToken<Vector<CIForm>>(){}.getType();
		temp = InternalIO.loadFromInternalStorage(context, InternalIO.UPLIFT_FORM, collectionType);
		if (temp != null) {
			SingletonHolder.mUpliftInDTO = temp;
		} else {
			SingletonHolder.mUpliftInDTO = new Vector<CIForm>();
		}
		temp = InternalIO.loadFromInternalStorage(context, InternalIO.DELIVERY_FORM, collectionType);
		if (temp != null) {
			SingletonHolder.mDeliveryInDTO = temp;
		} else {
			SingletonHolder.mDeliveryInDTO = new Vector<CIForm>();
		}
		
		if (mUpliftFormComparator == null) {
			mUpliftFormComparator = new CIFormComparator();
		}
		
		if (mDeliveryFormComparator == null) {
			mDeliveryFormComparator = new CIFormComparator();
		}
		
		SingletonHolder.mDeleteQueue = new Vector<CIForm>();
		SingletonHolder.mUploadQueue = new Vector<CIForm>();
		SingletonHolder.mDownloadQueue = new Vector<CIResult>();
		SingletonHolder.mUpliftResult = new Vector<CIResult> ();
		SingletonHolder.mDeliveryResult = new Vector<CIResult> ();
		
		SingletonHolder.mSearchInDTO = new SearchCIInDTO();
		SingletonHolder.mDeleteInDTO = new DeleteInDTO();
		SingletonHolder.mDownloadInDTO = new DownloadInDTO();
	}
	
	@Override
	public AuthenticationToken getAuthenticationToken() {
		return mAuthenticationToken;
	}
	
	@Override
	public void setAuthenticationToken(AuthenticationToken param) {
		mAuthenticationToken = param;
	}

	@Override
	public void prepareSearch() {
		mOperation = GetCIService.SEARCH;
	}
	
	@Override
	public void prepareMMSearch() {
		mOperation = GetCIService.MM_SEARCH;
	}
	
	@Override
	public void prepareDownload() {
		mOperation = GetCIService.DOWNLOAD;
		
		CIResult target = SingletonHolder.mDownloadQueue.get(0);
		if (target.poNumber != null && target.poNumber.length() != 0) {
			SingletonHolder.mDownloadInDTO.setCIType(target.cIType);
			SingletonHolder.mDownloadInDTO.setPONumber(target.poNumber);
		} else {
			SingletonHolder.mDownloadInDTO.setCIType(target.cIType);
			SingletonHolder.mDownloadInDTO.setOrderLineId(target.orderLineId);
		}
		
		mAuthenticationToken.setIsMM(target.isMM);
		SingletonHolder.mDownloadInDTO.Tracker = ModelFactory.getUtilService().getCurrentDateWithFormat(DATE_FORMAT);
	}

	@Override
	public void prepareUpload() {		
		mOperation = GetCIService.UPLOAD;
		setCurrentForm(SingletonHolder.mUploadQueue.get(0));
		
		// parameter for upload
		SingletonHolder.mCurrentForm.getUpload().getQuestionAnswers().put(UploadInDTO.P_GEOCODE_FRM_UPLOAD, LocationInDTO.LastLocation);		
		
		//myLocation = new MyLocation();
		//myLocation.getLocation(context, uploadedCILocationResult);

		SingletonHolder.mCurrentForm.getUpload().getQuestionAnswers().put(
				UploadInDTO.P_UPLOAD_DATE, 
				ModelFactory.getUtilService().getCurrentDateWithFormat(DATE_FORMAT));
		SingletonHolder.mCurrentForm.getUpload().getQuestionAnswers().put(
				UploadInDTO.P_UPLOAD_RETRY_COUNT, 
				SingletonHolder.mCurrentForm.incrementCounter() + "");
		
		mAuthenticationToken.setIsMM(SingletonHolder.mCurrentForm.isMM());
	}

	@Override
	public void prepareDelete() {
		mOperation = GetCIService.DELETE;
		CIForm target = SingletonHolder.mDeleteQueue.get(0);
		String poNumber = target.getDownload().poNumber;
		SingletonHolder.mDeleteInDTO.setPONumber(poNumber);
		SingletonHolder.mDeleteInDTO.Tracker = ModelFactory.getUtilService().getCurrentDateWithFormat(DATE_FORMAT);
		
		mAuthenticationToken.setIsMM(target.isMM());
	}
	
	
	@Override
	public void populateUploadQueue() {
		SingletonHolder.mUploadQueue.clear();
		for (CIForm target : SingletonHolder.mUpliftInDTO) {
			if (target.getUpload().isValid()) {
				SingletonHolder.mUploadQueue.add(target);	
			}
		}
		for (CIForm target : SingletonHolder.mDeliveryInDTO) {
			if (target.getUpload().isValid()) {
				SingletonHolder.mUploadQueue.add(target);	
			}
		}
	}
	
	@Override
	public void populateDeleteQueue() {
		SingletonHolder.mDeleteQueue.addAll(SingletonHolder.mDeliveryInDTO);
		SingletonHolder.mDeleteQueue.addAll(SingletonHolder.mUpliftInDTO);
	}

	@Override
	public String getResponseMessage() { return mClient.getResponse().toString(); }
	
	@Override
	public SearchCIInDTO getSearchInDTO() {
		return SingletonHolder.mSearchInDTO;
	}
	
	@Override
	public DownloadInDTO getDownloadInDTO() {
		return SingletonHolder.mDownloadInDTO;
	}
	
	@Override
	public DeleteInDTO getDeleteInDTO() {
		return SingletonHolder.mDeleteInDTO;
	}

	@Override
	public UploadInDTO getUploadInDTO() {
		return SingletonHolder.mCurrentForm.getUpload();
	}
	
	@Override
	public void setCurrentForm(CIForm form) {
		for (CIForm target : SingletonHolder.mUpliftInDTO) {
			if (target.getDownload().poNumber.compareTo(form.getDownload().poNumber) == 0) {
				SingletonHolder.mCurrentForm = target;
				return;
			}
		}
		for (CIForm target : SingletonHolder.mDeliveryInDTO) {
			if (target.getDownload().poNumber.compareTo(form.getDownload().poNumber) == 0) {
				SingletonHolder.mCurrentForm = target;
				return;
			}
		}	
	}

	@Override
	public void findMyLocation(Context context) {
		myLocation = new MyLocation();
		myLocation.getLocation(context, locationResult);
	}
	
	@Override
	public void onFinalised() {
		SingletonHolder.mCurrentForm.getUpload().getQuestionAnswers().put(UploadInDTO.P_FINALISED_DATE, 
				ModelFactory.getUtilService().getCurrentDateWithFormat(DATE_FORMAT));
		SingletonHolder.mCurrentForm.getUpload().getQuestionAnswers().put(
				UploadInDTO.P_GEO_CODE_FINALISED, LocationInDTO.LastLocation);
	}
	
	private LocationResult locationResult = new LocationResult() {

		@Override
		public void gotLocation(Location location) {
			String strloc = "0,0";
			if (location != null) {
				strloc  = location.getLatitude() + "," + location.getLongitude();
			}
	
			SingletonHolder.mCurrentForm.getUpload().getQuestionAnswers().put(
					UploadInDTO.P_GPS_SUBMIT_LOCATION, strloc);
		}
	};
	
	@Override
	public void sortForms(int which, int criteria) {
		switch (which) {
		case 0:
			if (mUpliftFormComparator.getCompareCriteria() == criteria) {
				Collections.reverse(SingletonHolder.mUpliftInDTO);
			} else {
				mUpliftFormComparator.setCompareCriteria(criteria);
				Collections.sort(SingletonHolder.mUpliftInDTO, mUpliftFormComparator);
			}
			break;
		case 1:
			if (mDeliveryFormComparator.getCompareCriteria() == criteria) {
				Collections.reverse(SingletonHolder.mDeliveryInDTO);
			} else {
				mDeliveryFormComparator.setCompareCriteria(criteria);
				Collections.sort(SingletonHolder.mDeliveryInDTO, mDeliveryFormComparator);
			}
			break;
		case 2:
			break;
		}
	}
	
	//http://thinkandroid.wordpress.com/2009/12/25/resizing-a-bitmap/
	private String getResizedSignature(String base64Signature, int newHeight, int newWidth) {
		// decode signature
		byte[] imageAsBytes = Base64.decode(base64Signature);
		Bitmap bm = BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
		
		// get size
		int width = bm.getWidth();
		int height = bm.getHeight();
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;

		// create a matrix for the manipulation
		Matrix matrix = new Matrix();
		// resize the bit map
		matrix.postScale(scaleWidth, scaleHeight);
		// recreate the new Bitmap
		Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);

		// encode signature
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
		byte[] byteArray = stream.toByteArray();
		return Base64.encode(byteArray);
	}
	
	@Override
	public void deallocateMemory() {
		SingletonHolder.mUpliftInDTO = null;
		SingletonHolder.mDeliveryInDTO = null;
		SingletonHolder.mDeleteQueue = null;
		SingletonHolder.mUploadQueue = null;
		SingletonHolder.mDownloadQueue = null;
		SingletonHolder.mUpliftResult = null;
		SingletonHolder.mDeliveryResult = null;
		
		SingletonHolder.mSearchInDTO = null;
		SingletonHolder.mDeleteInDTO = null;
		SingletonHolder.mCurrentForm = null;
		SingletonHolder.mDownloadInDTO = null;
	}
}
