package com.contactpoint.model.service.impl;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.Vector;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;
import org.ksoap2.serialization.SoapObject;

import android.content.Context;
import android.location.Location;

import com.contactpoint.ci_prv_mm.R;
import com.contactpoint.controller.AsyncSoapTaskCompleteListener;
import com.contactpoint.controller.NetworkListener;
import com.contactpoint.model.ModelFactory;
import com.contactpoint.model.DTO.LocationInDTO;
import com.contactpoint.model.DTO.azure.AzureTokenDTO;
import com.contactpoint.model.DTO.prv.BookPRVInDTO;
import com.contactpoint.model.DTO.prv.DeletePRVInDTO;
import com.contactpoint.model.DTO.prv.DownloadPRVInDTO;
import com.contactpoint.model.DTO.prv.IncompletePRVInDTO;
import com.contactpoint.model.DTO.prv.ItemInDTO;
import com.contactpoint.model.DTO.prv.RoomOutDTO;
import com.contactpoint.model.DTO.prv.SearchPRVInDTO;
import com.contactpoint.model.DTO.prv.SummaryOutDTO;
import com.contactpoint.model.DTO.prv.UploadPRVInDTO;
import com.contactpoint.model.DTO.prv.validator.UploadPRVInDTOValidator;
import com.contactpoint.model.client.PRVForm;
import com.contactpoint.model.client.authentication.AuthenticationToken;
import com.contactpoint.model.client.prv.BookingReasons;
import com.contactpoint.model.client.prv.Client;
import com.contactpoint.model.client.prv.Item;
import com.contactpoint.model.client.prv.Items;
import com.contactpoint.model.client.prv.PRVDownload;
import com.contactpoint.model.client.prv.PRVQuestions;
import com.contactpoint.model.client.prv.PRVResult;
import com.contactpoint.model.client.prv.PRVUpload;
import com.contactpoint.model.client.prv.QuestionAnswer;
import com.contactpoint.model.client.prv.ReferenceData;
import com.contactpoint.model.client.prv.ReferenceIDDropDown;
import com.contactpoint.model.client.prv.Room;
import com.contactpoint.model.client.prv.RoomItem;
import com.contactpoint.model.client.prv.VectorPRVDownload;
import com.contactpoint.model.client.prv.VectorPRVResult;
import com.contactpoint.model.service.AzureAuthenticationService;
import com.contactpoint.model.service.PRVService;
import com.contactpoint.model.util.AzureAuthClient;
import com.contactpoint.model.util.InternalIO;
import com.contactpoint.model.util.MyLocation;
import com.contactpoint.model.util.MyLocation.LocationResult;
import com.contactpoint.model.util.PRVFormComparator;
import com.contactpoint.model.util.SoapClient;
import com.contactpoint.model.util.SoapEnvelopeGenerator;
import com.contactpoint.view.fragment.WorkListFragment;
import com.google.gson.reflect.TypeToken;

public class PRVServiceImpl implements PRVService, AsyncSoapTaskCompleteListener<String> {

	private class SingletonHolder {
		SearchPRVInDTO mSearchPRVInDTO;
		DownloadPRVInDTO mDownloadPRVInDTO;
		DeletePRVInDTO mDeletePRVInDTO;
		BookPRVInDTO mBookPRVInDTO;
		IncompletePRVInDTO mIncompletePRVInDTO;
		SummaryOutDTO mSummaryOutDTO;

		Vector<PRVForm> mFormInDTO;
		Vector<PRVForm> mUploadQueue;
		Vector<PRVResult> mDownloadQueue;
		VectorPRVResult mPRVResults;
		PRVForm mCurrentForm;	// target for delete and booking
		RoomOutDTO mCurrentRoom;
		UploadPRVInDTO mPRVUpload;
		ReferenceData mRefOutDTO;
		String mLastUpdated;
		String mLastChecked;
		MyLocation myLocation;
		
		// Move Maestro variables
		String mMMLastUpdated;
		String mMMLastChecked;
		ReferenceData mMMRefOutDTO;
	}

	private AuthenticationToken mAuthenticationToken;
	private SoapClient mClient;
	private AzureAuthClient mAzureClient;
	//private SoapClientMM mMMClient;
	private NetworkListener mListener;
	private int mOperation;
	private PRVFormComparator mPRVFormComparator;
	private SingletonHolder mSingletonHolder;
	
	private final static double ROUND = 1000.0;		// 3 digits
	private final static String CARTON_STANDARD_NAME = "STANDARD CARTONS";
	private final static String CARTON_PICTURE_NAME = "PICTURE CARTONS";
	private final static String CARTON_BOOK_NAME = "BOOK CARTONS";
	
	public PRVServiceImpl() {
		mSingletonHolder = new SingletonHolder();
		mPRVFormComparator = new PRVFormComparator();
	}

	@Override
	public void initPRVInDTO() {
		if (getCurrentRoomList().size() == 0) {
			mSingletonHolder.mCurrentRoom = null;				
		} else {
			mSingletonHolder.mCurrentRoom = getCurrentRoomList().lastElement();
		}
	}

	@Override 
	public Vector<ItemInDTO> getCurrentItemList() {
		return mSingletonHolder.mCurrentRoom.getCurrentItemList();
	}

	@Override
	public void initRoomSummaryInDTO(Room room) {
		// give number to room name
		int index = 1;
		String currentRoomName = "";
		if (room.Name.length() == 0) {
			room.Name = "Custom";
		}
				
		for (int i = getCurrentRoomList().size() - 1; i >= 0; i--) {
			currentRoomName = getCurrentRoomList().get(i).Name;
			if (currentRoomName.contains(room.Name)) {
				index = Integer.parseInt(currentRoomName.substring(
						currentRoomName.lastIndexOf(" ") + 1)) + 1;
				break;
			}
		}

		mSingletonHolder.mCurrentRoom = new RoomOutDTO();
		mSingletonHolder.mCurrentRoom.ID = room.ID;
		mSingletonHolder.mCurrentRoom.Name = room.Name + " " + index;
		mSingletonHolder.mCurrentRoom.setCurrentItemList(new Vector<ItemInDTO>());
		mSingletonHolder.mCurrentRoom.setCartonList(new Vector<ItemInDTO>());
		mSingletonHolder.mCurrentRoom.setCoverList(new Vector<ItemInDTO>());
		mSingletonHolder.mCurrentRoom.setCrateList(new Vector<ItemInDTO>());
		
		parseAddOn();

		getCurrentRoomList().add(mSingletonHolder.mCurrentRoom);
	}

	@Override
	public void addItem(ItemInDTO i) {
		mSingletonHolder.mCurrentRoom.getCurrentItemList().add(0, i);
	}

	@Override
	public void removeItem(ItemInDTO i) {
		mSingletonHolder.mCurrentRoom.getCurrentItemList().remove(i);
	}
	
	@Override
	public void addCrate(ItemInDTO i) {
		mSingletonHolder.mCurrentRoom.getCrateList().add(i);
	}

	@Override
	public void removeCrate(ItemInDTO i) {
		mSingletonHolder.mCurrentRoom.getCrateList().remove(i);
	}
	
	@Override
	public double calculateVolume(String l, String w, String d) {
		int m = 100;	// cm to m
		if (l.length() == 0) l = "0";
		if (w.length() == 0) w = "0";
		if (d.length() == 0) d = "0";
		
		double vol = Double.parseDouble(l) / m * Double.parseDouble(w) / m * Double.parseDouble(d) / m;
		return Math.round(vol * ROUND) / ROUND;	// 3 digits
	}

	@Override
	public boolean refDataIsUpToDate() {
		return mSingletonHolder.mRefOutDTO != null && 
				mSingletonHolder.mRefOutDTO.lastUpdated.
				compareTo(mSingletonHolder.mLastUpdated) == 0;
	}
	
	@Override
	public boolean mmRefDataIsUpToDate() {
		return mSingletonHolder.mMMRefOutDTO != null && 
				mSingletonHolder.mMMRefOutDTO.lastUpdated.
				compareTo(mSingletonHolder.mMMLastUpdated) == 0;
	}

	@Override
	public boolean refDataNeedRefresh() {
		return mSingletonHolder.mLastChecked == null || 
				mSingletonHolder.mLastChecked.compareTo(
				ModelFactory.getUtilService().getCurrentDateWithFormat(PRVService.DATE_FORMAT)) != 0;
	}
	
	@Override
	public boolean mmRefDataNeedRefresh() {
		return mSingletonHolder.mMMLastChecked == null || 
				mSingletonHolder.mMMLastChecked.compareTo(
				ModelFactory.getUtilService().getCurrentDateWithFormat(PRVService.DATE_FORMAT)) != 0;
	}

	@Override
	public void setNetworkListener(NetworkListener listener) { mListener = listener; }

	@Override
	public String getClientMessage() 	{ return mClient.getMessage(); }

	@Override
	public String getResponseMessage() 	{ return mClient.getResponse().toString(); }

	@Override
	public Vector<PRVForm> getUploadQueue() {
		return mSingletonHolder.mUploadQueue;
	}

	@Override
	public Vector<PRVResult> getDownloadQueue() {
		return mSingletonHolder.mDownloadQueue;
	}

	@Override
	public SearchPRVInDTO getSearchPRVInDTO() {
		return mSingletonHolder.mSearchPRVInDTO;
	}

	@Override
	public DownloadPRVInDTO getDownloadPRVInDTO() {
		return mSingletonHolder.mDownloadPRVInDTO;
	}

	@Override
	public BookPRVInDTO getBookPRVInDTO() {
		return mSingletonHolder.mBookPRVInDTO;
	}

	@Override
	public IncompletePRVInDTO getIncompletePRVInDTO() {
		return mSingletonHolder.mIncompletePRVInDTO;
	}

	@Override
	public ReferenceData getReferenceDataOutDTO(boolean isMM) {
		return isMM ? mSingletonHolder.mMMRefOutDTO : mSingletonHolder.mRefOutDTO;
	}
	/*
	@Override
	public ReferenceData getMMReferenceDataOutDTO() {
		return mSingletonHolder.mMMRefOutDTO;
	}*/

	@Override
	public void onPreExecuteSoap() {
		if (mOperation == PRV_REF_DOWNLOAD) {
			mListener.showLoadingDialog(NetworkListener.UPDATING_MSG);
		} else {
			mListener.showLoadingDialog(NetworkListener.LOADING_MSG);
		}
	}

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
		
		//mMMClient = null;
		//mMMClient = new SoapClientMM(this);

		switch (mOperation) {
		case PRV_SEARCH:
			mAuthenticationToken.setIsMM(false);
			mClient.setEnvelope(SoapEnvelopeGenerator.getSearchPRVEnvelope(
					mSingletonHolder.mSearchPRVInDTO, mAuthenticationToken));
			mClient.execute(SearchPRVInDTO.ACTION, PATH);
			break;
		case PRV_MM_SEARCH:
			mAuthenticationToken.setIsMM(true);
			mClient.setEnvelope(SoapEnvelopeGenerator.getSearchPRVEnvelope(
					mSingletonHolder.mSearchPRVInDTO, mAuthenticationToken));
			mClient.execute(SearchPRVInDTO.MM_ACTION, PATH_MM, ModelFactory.getAzureService().getURL());
			break;
		case PRV_DOWNLOAD:
		case PRV_REDOWNLOAD:
			mClient.setEnvelope(SoapEnvelopeGenerator.getDownloadPRVEnvelope(
					mSingletonHolder.mDownloadPRVInDTO, mAuthenticationToken));
			if (mAuthenticationToken.isMM()) {
				mClient.execute(DownloadPRVInDTO.MM_ACTION, PATH_MM, ModelFactory.getAzureService().getURL());
			} else {
				mClient.execute(DownloadPRVInDTO.ACTION, PATH);
			}
			break;
		case PRV_BOOKING:
			mClient.setEnvelope(SoapEnvelopeGenerator.getBookPRVEnvelope(
					mSingletonHolder.mBookPRVInDTO, mAuthenticationToken));
			if (mAuthenticationToken.isMM()) {
				mClient.execute(BookPRVInDTO.MM_ACTION, PATH_MM, ModelFactory.getAzureService().getURL());
			} else {
				mClient.execute(BookPRVInDTO.ACTION, PATH);
			}
			break;
		case PRV_DELETE:
			mClient.setEnvelope(SoapEnvelopeGenerator.getDeletePRVEnvelope(
					mSingletonHolder.mDeletePRVInDTO, mAuthenticationToken));
			if (mAuthenticationToken.isMM()) {
				mClient.execute(DeletePRVInDTO.MM_ACTION, PATH_MM, ModelFactory.getAzureService().getURL());
			} else {
				mClient.execute(DeletePRVInDTO.ACTION, PATH);
			}
			break;
		case PRV_CANCELLED:
		case PRV_INCOMPLETE:
		case PRV_RELEASED:
			mClient.setEnvelope(SoapEnvelopeGenerator.getIncompletePRVEnvelope(
					mSingletonHolder.mIncompletePRVInDTO, mAuthenticationToken));
			if (mAuthenticationToken.isMM()) {
				mClient.execute(IncompletePRVInDTO.MM_ACTION, PATH_MM, ModelFactory.getAzureService().getURL());
			} else {
				mClient.execute(IncompletePRVInDTO.ACTION, PATH);
			}
			break;
		case PRV_UPLOAD:
			mClient.setEnvelope(SoapEnvelopeGenerator.getUploadPRVEnvelope(
					mSingletonHolder.mPRVUpload, mAuthenticationToken));
			if (mAuthenticationToken.isMM()) {
				mClient.execute(UploadPRVInDTO.MM_ACTION, PATH_MM, ModelFactory.getAzureService().getURL());
			} else {
				mClient.execute(UploadPRVInDTO.ACTION, PATH);
			}
			break;
		case PRV_REF_CHECK:
			mAuthenticationToken.setIsMM(false);
			mClient.setEnvelope(SoapEnvelopeGenerator.getReferenceEnvelope(mAuthenticationToken));
			mClient.execute("ReferenceDataCheckDate", PATH);
			break;
		case PRV_MM_REF_CHECK:
			mAuthenticationToken.setIsMM(true);
			mClient.setEnvelope(SoapEnvelopeGenerator.getMMReferenceCheckDateEnvelope(mAuthenticationToken));
			mClient.execute(MM_ACTION_PREFIX + "ReferenceDataCheckDate", PATH_MM, ModelFactory.getAzureService().getURL());
			break;
		case PRV_REF_DOWNLOAD:
			mAuthenticationToken.setIsMM(false);
			// TMS Ref CheckDate & Download uses the same Soap Envelope
			mClient.setEnvelope(SoapEnvelopeGenerator.getReferenceEnvelope(mAuthenticationToken));
			mClient.execute("ReferenceDataDownload", PATH);
			break;
		case PRV_MM_REF_DOWNLOAD:
			mAuthenticationToken.setIsMM(true);
			mClient.setEnvelope(SoapEnvelopeGenerator.getMMReferenceDownloadEnvelope(mAuthenticationToken));
			mClient.execute(MM_ACTION_PREFIX + "ReferenceDataDownload", PATH_MM, ModelFactory.getAzureService().getURL());
			break;
		case PRV_ENVIRONMENT:
			mAuthenticationToken.setIsMM(true); // always true
			mClient.setEnvelope(SoapEnvelopeGenerator.getEnvironmentEnvelope(mAuthenticationToken));
			mClient.execute(MM_ACTION_PREFIX + "GetEnvironment", PATH_MM, ModelFactory.getAzureService().getURL());
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
			mOperation = PRVService.ERROR;
		}

		switch (mOperation) {
		case PRV_SEARCH:
			if (mSingletonHolder.mPRVResults != null) {
				mSingletonHolder.mPRVResults.removeAllElements();
			} else {
				mSingletonHolder.mPRVResults = new VectorPRVResult();
			}
			
			VectorPRVResult prvResults = new VectorPRVResult(mClient.getResponseAsObject());
			for (PRVResult prvResult : prvResults) {
				prvResult.isMM = false; // This comes from TMS
				mSingletonHolder.mPRVResults.add(prvResult);
			}
			//mSingletonHolder.mPRVResults = new VectorPRVResult(mClient.getResponseAsObject());
			break;
		case PRV_MM_SEARCH:
			if (mSingletonHolder.mPRVResults == null) {
				mSingletonHolder.mPRVResults = new VectorPRVResult();
			} 
			
			// If TMS is not enabled, clear previous results here
			if (!AzureAuthClient.ENABLE_TMS) {
				mSingletonHolder.mPRVResults.removeAllElements();
			}
			
			prvResults = new VectorPRVResult(mClient.getResponseAsObject());
			for (PRVResult prvResult : prvResults) {
				prvResult.isMM = true; // This comes from MoveMaestro
				mSingletonHolder.mPRVResults.add(prvResult);
			}
			break;
		case PRV_DOWNLOAD:
			PRVResult target = mSingletonHolder.mDownloadQueue.remove(0);
			mSingletonHolder.mPRVResults.remove(target);
			VectorPRVDownload downloaded = new VectorPRVDownload(mClient.getResponseAsObject());
			PRVForm form = new PRVForm();
			form.setIsMM(target.isMM);
			form.setDownload(downloaded);
			
			mSingletonHolder.mFormInDTO.add(form);
			target = null;
			break;
		case PRV_BOOKING:
			// ignore in case of booking contact 
			if (mSingletonHolder.mBookPRVInDTO.getBookedDate() != null && mSingletonHolder.mBookPRVInDTO.getBookedDate().length() != 0) {
				mSingletonHolder.mCurrentForm.getDownload().get(0).bookedDateTime = mSingletonHolder.mBookPRVInDTO.getBookedDate();
			}
			break;
		case PRV_DELETE:
			if (result.compareTo(DeletePRVInDTO.DELETE_SUCCESSFUL) == 0) {
				mSingletonHolder.mFormInDTO.remove(mSingletonHolder.mCurrentForm);
				mSingletonHolder.mCurrentForm = null;
				mOperation = WorkListFragment.PRV_TARGET_DELETE;
			} else {
				mClient.setMessage(result);
				mOperation = PRVService.ERROR;
			}
			break;
		case PRV_CANCELLED:
		case PRV_INCOMPLETE:
			if (result.compareTo(IncompletePRVInDTO.INCOMPLETE_SUCCESSFUL) == 0) {
				PRVDownload download = mSingletonHolder.mCurrentForm.getDownload().firstElement();
				String poNumber = getCombinedPONumber(download);
				ModelFactory.getPhotoService().removePhoto(mListener.getContext(), poNumber);
				mSingletonHolder.mFormInDTO.remove(mSingletonHolder.mCurrentForm);
				mSingletonHolder.mCurrentForm = null;
			} else {
				mClient.setMessage(result);
				mOperation = PRVService.ERROR;
			}
			break;
		case PRV_RELEASED:
			if (result.compareTo(IncompletePRVInDTO.INCOMPLETE_SUCCESSFUL) == 0) {
				preparePRVDownload(mSingletonHolder.mCurrentForm);
				executeSoapRequest();
			} else {
				mClient.setMessage(result);
				mOperation = PRVService.ERROR;
			}
			return;	// stop function here
		case PRV_UPLOAD:
			if (result.compareTo(UploadPRVInDTO.UPLOAD_SUCCESSFUL) == 0) {
				if (mSingletonHolder.mCurrentForm.getDownload().size() != 1 && !mSingletonHolder.mCurrentForm.uploadedOnce()) {
					mSingletonHolder.mCurrentForm.decrementCounter();
					mSingletonHolder.mCurrentForm.setUploadedOnce(true);
				} else {
					form = mSingletonHolder.mUploadQueue.remove(0);
					if (!result.contains("Failed")) {		// upload success
						mSingletonHolder.mFormInDTO.remove(form);	
						form = null;
					} else {								// upload failed
						mSingletonHolder.mCurrentForm.setUploadedOnce(false);
					}
				}
			} else {
				mClient.setMessage(result);
				mOperation = PRVService.ERROR;
			}
			break;
		case PRV_REF_CHECK:
			mSingletonHolder.mLastUpdated = result;
			mSingletonHolder.mLastChecked = ModelFactory.getUtilService().getCurrentDateWithFormat(PRVService.DATE_FORMAT);
			break;
		case PRV_REF_DOWNLOAD:
			mSingletonHolder.mRefOutDTO = new ReferenceData((Vector<SoapObject>)mClient.getResponse());
			mSingletonHolder.mRefOutDTO.lastUpdated = mSingletonHolder.mLastUpdated;
			break;
		case PRV_REDOWNLOAD:
			downloaded = new VectorPRVDownload(mClient.getResponseAsObject());
			if (downloaded.size() == 0) {
				mOperation = WorkListFragment.PRV_REDOWNLOAD_CANCELLED;
			} else {
				mSingletonHolder.mCurrentForm.setDownload(downloaded);
			}
			break;
		case PRV_ENVIRONMENT:
			SoapObject response = mClient.getResponseAsObject();
			if (response.hasProperty("Result")) {
				boolean isUAT = Boolean.parseBoolean(response.getProperty("Result").toString());
				ModelFactory.getAzureService().setIsUAT(isUAT);
			}
			break;
		case PRV_MM_REF_CHECK:
			mSingletonHolder.mMMLastUpdated = result;
			mSingletonHolder.mMMLastChecked = ModelFactory.getUtilService().getCurrentDateWithFormat(PRVService.DATE_FORMAT);
			break;
		case PRV_MM_REF_DOWNLOAD:
			mSingletonHolder.mMMRefOutDTO = new ReferenceData((Vector<SoapObject>)mClient.getResponse());
			mSingletonHolder.mMMRefOutDTO.lastUpdated = mSingletonHolder.mMMLastUpdated;
			break;		
		case ERROR:
			break;
		}
		mListener.callback(mOperation);
	}

	@Override
	public void preparePRVSearch() {
		mOperation = PRVService.PRV_SEARCH;
	}

	@Override
	public void preparePRVMMSearch() {
		mOperation = PRVService.PRV_MM_SEARCH;
	}
	
	@Override
	public void preparePRVDownload() {
		mOperation = PRVService.PRV_DOWNLOAD;
		PRVResult target = mSingletonHolder.mDownloadQueue.get(0);
		mSingletonHolder.mDownloadPRVInDTO.setPONumber(target.pONumber);
		mSingletonHolder.mDownloadPRVInDTO.setOrderLineId(target.orderLineId);
		mSingletonHolder.mDownloadPRVInDTO.setBookingDetailsID(null);
		mSingletonHolder.mDownloadPRVInDTO.setGroupParentOrderLineId("");
		mSingletonHolder.mDownloadPRVInDTO.Tracker = ModelFactory.getUtilService().getCurrentDateWithFormat(DATE_TIME_FORMAT);
		mAuthenticationToken.setIsMM(target.isMM);
	}
	
	@Override
	public void preparePRVDownload(PRVForm target) {
		mOperation = PRVService.PRV_REDOWNLOAD;
		PRVDownload download = target.getDownload().get(0);
		mSingletonHolder.mDownloadPRVInDTO.setPONumber(getCombinedPONumber(download));
		mSingletonHolder.mDownloadPRVInDTO.setOrderLineId(download.OrderLineId);
		// Advise from Seng via email 16/08/2016:
		// When you are re-downloading the PRV record from TMSR 
		// you need to remove the bookingdetailID reference within the request. 
		if (target.isMM()) {
			mSingletonHolder.mDownloadPRVInDTO.setBookingDetailsID(download.PrvBookingDetailsId);
		} else {
			mSingletonHolder.mDownloadPRVInDTO.setBookingDetailsID(null);
		}
		mSingletonHolder.mDownloadPRVInDTO.setGroupParentOrderLineId(download.GroupParentOrderLineId);
		mSingletonHolder.mDownloadPRVInDTO.Tracker = ModelFactory.getUtilService().getCurrentDateWithFormat(DATE_TIME_FORMAT);
		mAuthenticationToken.setIsMM(target.isMM());
	}

	@Override
	public void preparePRVBooking() {
		mOperation = PRVService.PRV_BOOKING;
		PRVDownload download = mSingletonHolder.mCurrentForm.getDownload().get(0);
		mSingletonHolder.mBookPRVInDTO.setBookingDetailsID(download.PrvBookingDetailsId);
		mSingletonHolder.mBookPRVInDTO.setPONumber(ModelFactory.getPRVService().getCombinedPONumber(download));
		mAuthenticationToken.setIsMM(mSingletonHolder.mCurrentForm.isMM());
	}

	@Override
	public void preparePRVDelete() {
		mOperation = PRVService.PRV_DELETE;
		PRVDownload download = mSingletonHolder.mCurrentForm.getDownload().get(0);
		mSingletonHolder.mDeletePRVInDTO.setBookingDetailsID(download.PrvBookingDetailsId);
		mSingletonHolder.mDeletePRVInDTO.setPONumber(ModelFactory.getPRVService().getCombinedPONumber(download));
		mSingletonHolder.mDeletePRVInDTO.Tracker = ModelFactory.getUtilService().getCurrentDateWithFormat(DATE_TIME_FORMAT);
		mAuthenticationToken.setIsMM(mSingletonHolder.mCurrentForm.isMM());
	}

	@Override
	public void preparePRVIncomplete() {
		mOperation = PRVService.PRV_INCOMPLETE;
		PRVDownload download = mSingletonHolder.mCurrentForm.getDownload().get(0);
		mSingletonHolder.mIncompletePRVInDTO.setPONumber(ModelFactory.getPRVService().getCombinedPONumber(download));
		mSingletonHolder.mIncompletePRVInDTO.Tracker = ModelFactory.getUtilService().getCurrentDateWithFormat(DATE_TIME_FORMAT);
		mAuthenticationToken.setIsMM(mSingletonHolder.mCurrentForm.isMM());
	}

	@Override
	public void preparePRVUpload() {
		mOperation = PRVService.PRV_UPLOAD;
		mSingletonHolder.mPRVUpload = new UploadPRVInDTO();
		PRVUpload prvUpload = new PRVUpload();
		RoomItem ri = null;
		QuestionAnswer qa = null;
		mSingletonHolder.mCurrentForm = mSingletonHolder.mUploadQueue.get(0);
		mSingletonHolder.mCurrentForm.getQuestionAnswer().put(UploadPRVInDTO.P_GEOCODE_UPLOAD, LocationInDTO.LastLocation);
		boolean isMM = mSingletonHolder.mCurrentForm.isMM();
		prvUpload.setIsMM(isMM);
		
		if (!mSingletonHolder.mCurrentForm.uploadedOnce()) {	// First upload (D2D)
			mSingletonHolder.mCurrentForm.setD2D(true);
			mSingletonHolder.mCurrentForm.getQuestionAnswer().put(
					UploadPRVInDTO.P_BOOKING_DETAILS_ID, 
					mSingletonHolder.mCurrentForm.getDownload().get(0).PrvBookingDetailsId);			
			mSingletonHolder.mPRVUpload.setPONumber(mSingletonHolder.mCurrentForm.getDownload().get(0).
					poNumber);
		} else {											// Second upload (D2S)
			mSingletonHolder.mCurrentForm.setD2D(false);
			mSingletonHolder.mCurrentForm.getQuestionAnswer().put(
					UploadPRVInDTO.P_BOOKING_DETAILS_ID, 
					mSingletonHolder.mCurrentForm.getDownload().get(1).PrvBookingDetailsId);						
			mSingletonHolder.mPRVUpload.setPONumber(mSingletonHolder.mCurrentForm.getDownload().get(1).
					poNumber);
		}
		
		refreshSummaryOutDTO();
		
		mSingletonHolder.mCurrentForm.getQuestionAnswer().put(
				UploadPRVInDTO.P_UPLOAD_RETRY_COUNT, 
				mSingletonHolder.mCurrentForm.incrementCounter() + "");
		mSingletonHolder.mCurrentForm.getQuestionAnswer().put(
				UploadPRVInDTO.P_TOTAL_VOLUME, 
				ModelFactory.getUtilService().printFormat(
						mSingletonHolder.mSummaryOutDTO.getTotalCubic()));
		mSingletonHolder.mCurrentForm.getQuestionAnswer().put(
				UploadPRVInDTO.P_UPLOAD_DATE, 
				ModelFactory.getUtilService().getCurrentDateWithFormat(DATE_TIME_FORMAT));
		
		for (RoomOutDTO room : getCurrentRoomList()) {	
			room.isMM = isMM;
			prvUpload.rooms.add(room);
			for (ItemInDTO item : room.getCurrentItemList()) {
				item.isMM = isMM;
				
				ri = new RoomItem();
				ri.ItemID = item.ID;
				ri.RoomID = room.ID;
				ri.Quantity = item.getQty();
				ri.isMM = isMM;
				prvUpload.items.put(item.ID, item);
				prvUpload.roomItems.add(ri);
			}
			for (ItemInDTO item : room.getCartonList()) {
				if (item.getQty() == 0) continue;
				item.isMM = isMM;
				
				ri = new RoomItem();
				ri.ItemID = item.ID;
				ri.RoomID = room.ID;
				ri.Quantity = item.getQty();
				ri.isMM = isMM;
				prvUpload.items.put(item.ID, item);
				prvUpload.roomItems.add(ri);
			}
			for (ItemInDTO item : room.getCoverList()) {
				if (item.getQty() == 0) continue;
				item.isMM = isMM;
				
				ri = new RoomItem();
				ri.ItemID = item.ID;
				ri.RoomID = room.ID;
				ri.Quantity = item.getQty();
				ri.isMM = isMM;
				prvUpload.items.put(item.ID, item);
				prvUpload.roomItems.add(ri);
			}
			for (ItemInDTO item : room.getCrateList()) {
				if (item.getQty() == 0) continue;
				item.isMM = isMM;
				
				ri = new RoomItem();
				ri.ItemID = item.ID;
				ri.RoomID = room.ID;
				ri.Quantity = item.getQty();
				ri.isMM = isMM;
				prvUpload.items.put(item.ID, item);
				prvUpload.roomItems.add(ri);
			}
		}
				
		Iterator<Entry<String, String>> it = mSingletonHolder.mCurrentForm.
				getQuestionAnswer().entrySet().iterator();
		Map.Entry<String, String> pairs;
		while (it.hasNext()) {
			pairs = it.next();
			qa = new QuestionAnswer();
			qa.questionCode = pairs.getKey();
			qa.answer = pairs.getValue();
			qa.isMM = isMM;
			prvUpload.questionAnswer.add(qa);
			//it.remove();
		}
		
		
		mSingletonHolder.mPRVUpload.setPRVUpload(prvUpload);
		mAuthenticationToken.setIsMM(isMM);
	}

	@Override
	public void prepareRefDataCheckDate() {
		mOperation = PRVService.PRV_REF_CHECK;
	}
	
	@Override
	public void prepareMMRefDataCheckDate() {
		mOperation = PRVService.PRV_MM_REF_CHECK;
	}

	@Override
	public void prepareRefDataDownload() {
		mOperation = PRVService.PRV_REF_DOWNLOAD;
	}
	
	@Override
	public void prepareMMRefDataDownload() {
		mOperation = PRVService.PRV_MM_REF_DOWNLOAD;
	}
	
	@Override
	public void preparePRVCancelled() {
		mOperation = PRVService.PRV_CANCELLED;
		mSingletonHolder.mIncompletePRVInDTO.setPONumber(
				mSingletonHolder.mDownloadPRVInDTO.getPONumber());
		mSingletonHolder.mIncompletePRVInDTO.setReason(AUTO_CANCEL_REASON);
		mSingletonHolder.mIncompletePRVInDTO.Tracker = ModelFactory.getUtilService().getCurrentDateWithFormat(DATE_TIME_FORMAT);
		mAuthenticationToken.setIsMM(mSingletonHolder.mCurrentForm.isMM());
	}
	
	@Override
	public void preparePRVRelease() {
		mOperation = PRVService.PRV_RELEASED;
		PRVDownload download = mSingletonHolder.mCurrentForm.getDownload().firstElement();
		mSingletonHolder.mIncompletePRVInDTO.setPONumber(getCombinedPONumber(download));
		mSingletonHolder.mIncompletePRVInDTO.setReason("");
		mSingletonHolder.mIncompletePRVInDTO.Tracker = ModelFactory.getUtilService().getCurrentDateWithFormat(DATE_TIME_FORMAT);
		mAuthenticationToken.setIsMM(mSingletonHolder.mCurrentForm.isMM());
	}
	
	@Override
	public void prepareGetEnvironment() {
		mOperation = PRVService.PRV_ENVIRONMENT;
	}

	@Override
	public void saveRefData(Context context) {
		InternalIO.saveToInternalStorage(context, InternalIO.REF_FORM, 
				mSingletonHolder.mRefOutDTO);
	}
	
	@Override
	public void saveMMRefData(Context context) {
		InternalIO.saveToInternalStorage(context, InternalIO.REF_MM_FORM, 
				mSingletonHolder.mMMRefOutDTO);
	}

	@Override
	public void saveFormData(Context context) {
		InternalIO.saveToInternalStorage(context, InternalIO.PRV_FORM, 
				mSingletonHolder.mFormInDTO);
	}

	@Override
	public void loadFormData(Context context) {
		/*Object obj = InternalIO.loadFromInternalStorage(context, InternalIO.PRV_FORM);
		if (obj != null) {
			mSingletonHolder.mFormInDTO = (Vector<PRVForm>)obj;
		}

		Object obj2 = InternalIO.loadFromInternalStorage(context, InternalIO.REF_FORM);
		if (obj2 != null) {
			mSingletonHolder.mRefOutDTO = (ReferenceData) obj2;
		}*/

		Vector<PRVForm> temp = new Vector<PRVForm>();
		Type collectionType = new TypeToken<Vector<PRVForm>>(){}.getType();
		temp = InternalIO.loadFromInternalStorage(context, InternalIO.PRV_FORM, collectionType);
		if (temp != null) {
			mSingletonHolder.mFormInDTO = temp;
		} else {
			mSingletonHolder.mFormInDTO = new Vector<PRVForm>();
		}
		mSingletonHolder.mRefOutDTO = InternalIO.loadFromInternalStorage(context, InternalIO.REF_FORM, ReferenceData.class);
		mSingletonHolder.mMMRefOutDTO = InternalIO.loadFromInternalStorage(context, InternalIO.REF_MM_FORM, ReferenceData.class);
		
		mSingletonHolder.mSearchPRVInDTO = new SearchPRVInDTO();
		mSingletonHolder.mDownloadPRVInDTO = new DownloadPRVInDTO();
		mSingletonHolder.mDeletePRVInDTO = new DeletePRVInDTO();
		mSingletonHolder.mBookPRVInDTO = new BookPRVInDTO();
		mSingletonHolder.mIncompletePRVInDTO = new IncompletePRVInDTO();
		mSingletonHolder.mSummaryOutDTO = new SummaryOutDTO();

		mSingletonHolder.mUploadQueue = new Vector<PRVForm>();
		mSingletonHolder.mDownloadQueue = new Vector<PRVResult>();
	}


	/*@Override
	public AuthenticationToken getAuthenticationToken() {
		return mAuthenticationToken;
	}*/

	@Override
	public void setAuthenticationToken(AuthenticationToken param) {
		mAuthenticationToken = param;
	}

	@Override
	public VectorPRVResult getPRVResults() {
		return mSingletonHolder.mPRVResults;
	}

	@Override
	public Vector<PRVForm> getPRVForms() {
		return mSingletonHolder.mFormInDTO;
	}

	@Override
	public PRVForm getCurrentForm() {
		return mSingletonHolder.mCurrentForm;
	}

	@Override
	public RoomOutDTO getCurrentRoom() {
		return mSingletonHolder.mCurrentRoom;
	}

	@Override
	public void setCurrentForm(PRVForm target) {
		mSingletonHolder.mCurrentForm = target;
	}
	
	@Override
	public void setCurrentRoom(int position) {
		mSingletonHolder.mCurrentRoom = getCurrentRoomList().get(position);
	}

	private void parseAddOn() {
		// move carton, cover, and crate to separate list
		ItemInDTO item = null, standard = null, picture = null, book = null;
		Items items = mSingletonHolder.mCurrentForm.isMM() ? 
				mSingletonHolder.mMMRefOutDTO.items : mSingletonHolder.mRefOutDTO.items;
		
		for (Item i : items.values()) {
			if (i.ItemType.compareTo(ItemInDTO.STANDARD) == 0) continue;

			item = new ItemInDTO();
			item.ID = i.ID;
			item.ItemType = i.ItemType;
			item.Name = i.Name;
			item.Length = i.Length;
			item.Width = i.Width;
			item.Depth = i.Depth;				
			item.CubicMeterage = i.CubicMeterage;
			item.IsCustom = i.IsCustom;
			item.MaxNumber = i.MaxNumber;
			item.setQty(0);

			if (i.ItemType.compareTo(ItemInDTO.CARTON) == 0) {	
				// standard cartons, picture cartons, and book cartons are added last to ensure they have higher priority
				if (item.Name.toUpperCase().compareTo(CARTON_STANDARD_NAME) == 0) {	// STANDARD CARTONS
					standard = item;
				} else if (item.Name.toUpperCase().compareTo(CARTON_PICTURE_NAME) == 0) {	// PICTURE CARTONS
					picture = item;
				} else if (item.Name.toUpperCase().compareTo(CARTON_BOOK_NAME) == 0) {	// BOOK CARTONS
					book = item;
				} else {
					mSingletonHolder.mCurrentRoom.getCartonList().add(item);					
				}
			} else if (i.ItemType.compareTo(ItemInDTO.COVER) == 0) {				
				mSingletonHolder.mCurrentRoom.getCoverList().add(item);
			} else if (i.ItemType.compareTo(ItemInDTO.CRATE) == 0) {				
				mSingletonHolder.mCurrentRoom.getCrateList().add(item);
			} 
		}

		// sort cartons based on priority
		mSingletonHolder.mCurrentRoom.getCartonList().add(0, standard);
		mSingletonHolder.mCurrentRoom.getCartonList().add(1, book);
		mSingletonHolder.mCurrentRoom.getCartonList().add(2, picture);
	}
	
	@Override
	public void refreshSummaryOutDTO() {
		mSingletonHolder.mSummaryOutDTO.setTotalRoom(getCurrentRoomList().size());
		
		mSingletonHolder.mSummaryOutDTO.getCartonList().clear();
		mSingletonHolder.mSummaryOutDTO.getCoverList().clear();
		mSingletonHolder.mSummaryOutDTO.getCrateList().clear();
		mSingletonHolder.mSummaryOutDTO.setTotalCartons(0);
		mSingletonHolder.mSummaryOutDTO.setTotalCovers(0);
		mSingletonHolder.mSummaryOutDTO.setTotalCrates(0);
		
		double cubic = 0;
		for (RoomOutDTO out : getCurrentRoomList()) {
			cubic += getRoomRunningTotal(out);
			refreshSummaryTotalCartons(out);
			refreshSummaryTotalCovers(out);
			refreshSummaryTotalCrates(out);
		}
		mSingletonHolder.mSummaryOutDTO.setTotalCubic(cubic);
	}
	
	@Override
	public SummaryOutDTO getSummaryOutDTO() {
		return mSingletonHolder.mSummaryOutDTO;
	}

	@Override
	public <K, V> Vector<NameValuePair> mapToNameValuePair(Map<K, V> map) {
		Vector<NameValuePair> vec = new Vector<NameValuePair>();
		BasicNameValuePair nvp;
		for (Map.Entry<K, V> entry : map.entrySet()) {
			nvp = new BasicNameValuePair(entry.getKey().toString(), entry.getValue().toString());
			vec.add(nvp);
		}
		return vec;
	}

	@Override
	public void putQuestionAnswer(Context context, String questionCode, int id) {
		switch (id) {
		case R.id.prv_radio_yes:
			mSingletonHolder.mCurrentForm.getQuestionAnswer().put(questionCode, YES);
			break;
		case R.id.prv_radio_no:
			mSingletonHolder.mCurrentForm.getQuestionAnswer().put(questionCode, NO);
			break;
		}		
	}
	
	@Override
	public void putQuestionAnswer(Context context, String questionCode, boolean val) {
		if (val) {
			mSingletonHolder.mCurrentForm.getQuestionAnswer().put(questionCode, YES);
		} else {
			mSingletonHolder.mCurrentForm.getQuestionAnswer().put(questionCode, NO);
		}
	}
	
	@Override
	public void putQuestionAnswer(Context context, String questionCode, String ans) {
		mSingletonHolder.mCurrentForm.getQuestionAnswer().put(questionCode, ans);
	}
	
	private LocationResult locationResult = new LocationResult() {

		@Override
		public void gotLocation(Location location) {
			ModelFactory.getUtilService().updateLocationInDTO(location);
			mSingletonHolder.mCurrentForm.getQuestionAnswer().put(
					UploadPRVInDTO.P_GPS_COORDINATES, LocationInDTO.LastLocation);
		}
	};
	
	@Override
	public void startTimeStamp(Context context) {
		if (!UploadPRVInDTOValidator.hasTimeStamp(mSingletonHolder.mCurrentForm
				.getQuestionAnswer())) {
			
			mSingletonHolder.mCurrentForm.getQuestionAnswer().put(
					UploadPRVInDTO.P_TRANSITIONS_START_TIMESTAMP, 
					ModelFactory.getUtilService().getCurrentDateWithFormat(
							PRVService.DATE_TIME_FORMAT));
			mSingletonHolder.mCurrentForm.getQuestionAnswer().put(
					UploadPRVInDTO.P_APP_VERSION, 
					context.getResources().getString(R.string.app_version));
			
			mSingletonHolder.myLocation = new MyLocation();
			mSingletonHolder.myLocation.getLocation(context, locationResult);
		}
	}
	
	@Override
	public void sortForms(int criteria) {
		if (mPRVFormComparator.getCompareCriteria() == criteria) {
			Collections.reverse(mSingletonHolder.mFormInDTO);
		} else {
			mPRVFormComparator.setCompareCriteria(criteria);
			Collections.sort(mSingletonHolder.mFormInDTO, mPRVFormComparator);
		}
	}

	@Override
	public double getRoomRunningTotal(RoomOutDTO room) {
		return getRoomItemVolume(room) + getRoomAddOnVolume(room);
	}
	
	@Override
	public double getRoomItemVolume(RoomOutDTO room) {
		double d = 0;
		for (ItemInDTO i : room.getCurrentItemList()) {
			d += Double.parseDouble(i.CubicMeterage) * i.getQty();
		}
		return Math.round(d * ROUND) / ROUND;
	}
	
	@Override
	public double getRoomAddOnVolume(RoomOutDTO room) {
		double d = 0;
		for (ItemInDTO i : room.getCartonList()) {
			d += Double.parseDouble(i.CubicMeterage) * i.getQty();
		}
		for (ItemInDTO i : room.getCoverList()) {
			d += Double.parseDouble(i.CubicMeterage) * i.getQty();
		}
		for (ItemInDTO i : room.getCrateList()) {
			d += Double.parseDouble(i.CubicMeterage) * i.getQty();
		}
		return Math.round(d * ROUND) / ROUND;
	}

	private void refreshSummaryTotalCartons(RoomOutDTO room) {
		TreeMap<String, Integer> map = mSingletonHolder.mSummaryOutDTO.getCartonList();
		int total = mSingletonHolder.mSummaryOutDTO.getTotalCartons();
		for (ItemInDTO i : room.getCartonList()) {
			if (!map.containsKey(i.Name)) {
				map.put(i.Name, i.getQty());				
			} else {
				map.put(i.Name, i.getQty() + map.get(i.Name));
			}
			total += i.getQty();
		}
		mSingletonHolder.mSummaryOutDTO.setTotalCartons(total);
	}

	private void refreshSummaryTotalCovers(RoomOutDTO room) {
		TreeMap<String, Integer> map = mSingletonHolder.mSummaryOutDTO.getCoverList();
		int total = mSingletonHolder.mSummaryOutDTO.getTotalCovers();
		for (ItemInDTO i : room.getCoverList()) {
			if (!map.containsKey(i.Name)) {
				map.put(i.Name, i.getQty());				
			} else {
				map.put(i.Name, i.getQty() + map.get(i.Name));
			}
			total += i.getQty();
		}
		mSingletonHolder.mSummaryOutDTO.setTotalCovers(total);
	}

	private void refreshSummaryTotalCrates(RoomOutDTO room) {
		TreeMap<String, Integer> map = mSingletonHolder.mSummaryOutDTO.getCrateList();
		int total = mSingletonHolder.mSummaryOutDTO.getTotalCrates();
		for (ItemInDTO i : room.getCrateList()) {
			if (!map.containsKey(i.Name)) {
				map.put(i.Name, i.getQty());				
			} else {
				map.put(i.Name, i.getQty() + map.get(i.Name));
			}
			total += i.getQty();
		}
		mSingletonHolder.mSummaryOutDTO.setTotalCrates(total);
	}
	
	@Override
	public Item[] getStandardItems() {
		Vector <Item> v = new Vector<Item>();
		boolean isMM = mSingletonHolder.mCurrentForm.isMM();
		for (Item i : getReferenceDataOutDTO(isMM).items.values()) {
			if (i.ItemType.compareTo(ItemInDTO.STANDARD) != 0)	continue;
			v.add(i);
		}
		Item [] items = new Item[v.size()];
		return v.toArray(items);
	}
	
	@Override
	public boolean completeDateDiffers() {		
		return !mSingletonHolder.mCurrentForm.getDownload().get(0).bookedDateTime.contains(
				ModelFactory.getUtilService().getCurrentDateWithFormat(PRVService.DATE_FORMAT)) &&
				(!mSingletonHolder.mCurrentForm.getQuestionAnswer().containsKey(UploadPRVInDTO.P_DATE_DIFFERS) ||
				!mSingletonHolder.mCurrentForm.getQuestionAnswer().containsKey(UploadPRVInDTO.P_DD_COMMENTS));
	}

	@Override
	public void populateUploadQueue(Context context) {
		mSingletonHolder.mUploadQueue.clear();
		for (PRVForm target : mSingletonHolder.mFormInDTO) {
			if (target.isReady()) {
				mSingletonHolder.mUploadQueue.add(target);
			}
		}
	}
	
	@Override
	public String getCombinedPONumber(PRVDownload form) {
		//return form.poNumber.replace("A", "");
		return form.poNumber.substring(0, form.poNumber.length()-1);
	}
	
	@Override
	public Vector<RoomOutDTO> getCurrentRoomList() {
		if (mSingletonHolder.mCurrentForm.isD2D()) {
			return mSingletonHolder.mCurrentForm.getD2DRoomList();
		} else {
			return mSingletonHolder.mCurrentForm.getD2SRoomList();
		}
	}
	
	@Override
	public void setServiceModeD2D(boolean param) {
		mSingletonHolder.mCurrentForm.setD2D(param);
	}
	
	@Override
	public boolean validateItemCollection() {
		
		if (mSingletonHolder.mCurrentForm.getDownload().size() == 1) {
			mSingletonHolder.mCurrentForm.setItemCollectionCompleted(mSingletonHolder.mSummaryOutDTO.getTotalCubic() != 0);
		} else {
			boolean isComplete = mSingletonHolder.mSummaryOutDTO.getTotalCubic() != 0;
			boolean which = mSingletonHolder.mCurrentForm.isD2D();
			
			mSingletonHolder.mCurrentForm.setD2D(!which);
			refreshSummaryOutDTO();
			
			isComplete = isComplete && mSingletonHolder.mSummaryOutDTO.getTotalCubic() != 0;
			mSingletonHolder.mCurrentForm.setItemCollectionCompleted(isComplete);
			
			mSingletonHolder.mCurrentForm.setD2D(which);
			refreshSummaryOutDTO();
		}
		return mSingletonHolder.mCurrentForm.itemCollectionCompleted();
	}
	/*
	private String getCurrentTime() {
		Date date = Calendar.getInstance().getTime();
		DateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		return df.format(date);
	}*/

	@Override
	public void onFinalised() {
		mSingletonHolder.mCurrentForm.getQuestionAnswer().put(UploadPRVInDTO.P_FINALISED_DATE, 
				ModelFactory.getUtilService().getCurrentDateWithFormat(DATE_TIME_FORMAT));
		mSingletonHolder.mCurrentForm.getQuestionAnswer().put(UploadPRVInDTO.P_GEO_CODE_FINALISED, 
				LocationInDTO.LastLocation);
	}
	
	@Override
	public ArrayList<ReferenceIDDropDown> getBookingReasonCodes(String type) {
		ArrayList<ReferenceIDDropDown> array = new ArrayList<ReferenceIDDropDown>();
		
		BookingReasons refData = mSingletonHolder.mCurrentForm.isMM() ? 
				mSingletonHolder.mMMRefOutDTO.bookingReasons : mSingletonHolder.mRefOutDTO.bookingReasons;
		
		for (ReferenceIDDropDown br : refData) {
			if (br.Type.compareTo(type) == 0) {
				array.add(br);
			}
		}
		
		return array;
	}
	
	@Override
	public boolean validateLimit(ItemInDTO item, int val, int sign) {
		int max;
		
		if (item.ID < 0) return true;
		
		if (mSingletonHolder.mCurrentForm.isD2D()) {
			max = mSingletonHolder.mCurrentForm.getD2DLimiter().get(item.ID).MaxNumber;
		} else {
			max = mSingletonHolder.mCurrentForm.getD2SLimiter().get(item.ID).MaxNumber;
		}
		
		//System.out.println(max);
		return max == Item.DEFAULT_MAX || sign == PRVService.MINUS || (sign == PRVService.PLUS && max >= val);
	}
	
	@Override
	public void updateLimit(ItemInDTO item, int val, int sign) {
		Item i;
		if (mSingletonHolder.mCurrentForm.isD2D()) {
			i = mSingletonHolder.mCurrentForm.getD2DLimiter().get(item.ID);
		} else {
			i = mSingletonHolder.mCurrentForm.getD2SLimiter().get(item.ID);
		}
		if (i == null || i.MaxNumber == Item.DEFAULT_MAX) return;	// if item is not limited
		if (sign == PRVService.PLUS) i.MaxNumber -= val;
		else if (sign == PRVService.MINUS) i.MaxNumber += val;
		
		// Hyap: I got a feeling this system.out.println wasn't supposed to be deleted... 
		// but I can't remember the reason...
		if (mSingletonHolder.mCurrentForm.isD2D()) {
			System.out.println(i.MaxNumber + " " + mSingletonHolder.mCurrentForm.getD2DLimiter().get(item.ID).MaxNumber);
		} else {
			System.out.println(i.MaxNumber + " " + mSingletonHolder.mCurrentForm.getD2SLimiter().get(item.ID).MaxNumber);
		}
	}
	
	@Override
	public ArrayList<ReferenceIDDropDown> getPRVQuestions(String type) {
		ArrayList<ReferenceIDDropDown> array = new ArrayList<ReferenceIDDropDown>();
		
		PRVQuestions refData = mSingletonHolder.mCurrentForm.isMM() ? 
				mSingletonHolder.mMMRefOutDTO.pRVQuestions : mSingletonHolder.mRefOutDTO.pRVQuestions;
		
		for (ReferenceIDDropDown br : refData) {
			if (br.Type.compareTo(type) == 0 && br.Description.compareTo("Other") != 0) {
				array.add(br);
			}
		}
		
		return array;
	}
	
	@Override
	public ArrayList<ReferenceIDDropDown> getRefDataRegion() {
		ArrayList<ReferenceIDDropDown> array = new ArrayList<ReferenceIDDropDown>();
		
		// add everything from MoveMaestro
		array.addAll(mSingletonHolder.mMMRefOutDTO.regions);
		
		// add from TMS anything that doesn't have the exact Type and Description
		if (AzureAuthClient.ENABLE_TMS) {
			for (ReferenceIDDropDown tms : mSingletonHolder.mRefOutDTO.regions) {
				boolean alreadyAdded = false;
				
				for (ReferenceIDDropDown mm : mSingletonHolder.mMMRefOutDTO.regions) {
					if (mm.Type.compareTo(tms.Type) == 0 &&
							mm.Description.compareTo(tms.Description) == 0) 
					{
						alreadyAdded = true;
						break;
					}
				}
				
				if (!alreadyAdded) {
					array.add(tms);
				}
			}
		}
		return array;
	}
	

	@Override
	public ArrayList<ReferenceIDDropDown> getRefDataZone() {
		ArrayList<ReferenceIDDropDown> array = new ArrayList<ReferenceIDDropDown>();
		
		// add everything from MoveMaestro
		array.addAll(mSingletonHolder.mMMRefOutDTO.zones);
		
		// add from TMS anything that doesn't have the exact Type and Description
		if (AzureAuthClient.ENABLE_TMS) {
			for (ReferenceIDDropDown tms : mSingletonHolder.mRefOutDTO.zones) {
				boolean alreadyAdded = false;
				
				for (ReferenceIDDropDown mm : mSingletonHolder.mMMRefOutDTO.zones) {
					if (mm.Type.compareTo(tms.Type) == 0 &&
							mm.Description.compareTo(tms.Description) == 0) 
					{
						alreadyAdded = true;
						break;
					}
				}
				
				if (!alreadyAdded) {
					array.add(tms);
				}
			}
		}
		return array;
	}

	@Override
	public ArrayList<ReferenceIDDropDown> getRefDataBusinessGroup() {
		ArrayList<ReferenceIDDropDown> array = new ArrayList<ReferenceIDDropDown>();
		
		// add everything from MoveMaestro
		array.addAll(mSingletonHolder.mMMRefOutDTO.businessGroups);
		
		// add from TMS anything that doesn't have the exact Type and Description
		if (AzureAuthClient.ENABLE_TMS) {
			for (ReferenceIDDropDown tms : mSingletonHolder.mRefOutDTO.businessGroups) {
				boolean alreadyAdded = false;
				
				for (ReferenceIDDropDown mm : mSingletonHolder.mMMRefOutDTO.businessGroups) {
					if (mm.Type.compareTo(tms.Type) == 0 &&
							mm.Description.compareTo(tms.Description) == 0) 
					{
						alreadyAdded = true;
						break;
					}
				}
				
				if (!alreadyAdded) {
					array.add(tms);
				}
			}
		}
		return array;
	}

	@Override
	public ArrayList<ReferenceIDDropDown> getRefDataVolume() {
		ArrayList<ReferenceIDDropDown> array = new ArrayList<ReferenceIDDropDown>();
		
		// add everything from MoveMaestro
		array.addAll(mSingletonHolder.mMMRefOutDTO.volumes);
		
		// add from TMS anything that doesn't have the exact Type and Description
		if (AzureAuthClient.ENABLE_TMS) {
			for (ReferenceIDDropDown tms : mSingletonHolder.mRefOutDTO.volumes) {
				boolean alreadyAdded = false;
				
				for (ReferenceIDDropDown mm : mSingletonHolder.mMMRefOutDTO.volumes) {
					if (mm.Type.compareTo(tms.Type) == 0 &&
							mm.Description.compareTo(tms.Description) == 0) 
					{
						alreadyAdded = true;
						break;
					}
				}
				
				if (!alreadyAdded) {
					array.add(tms);
				}
			}
		}
		return array;
	}
	
	@Override
	public ArrayList<Client> getRefDataClient() {
		ArrayList<Client> array = new ArrayList<Client>();
		
		// add everything from MoveMaestro
		array.addAll(mSingletonHolder.mMMRefOutDTO.clients);
		
		// add from TMS anything that doesn't have the exact Type and Description
		if (AzureAuthClient.ENABLE_TMS) {
			for (Client tms : mSingletonHolder.mRefOutDTO.clients) {
				boolean alreadyAdded = false;
				
				for (Client mm : mSingletonHolder.mMMRefOutDTO.clients) {
					if (mm.ClientCode.compareTo(tms.ClientCode) == 0 &&
							mm.Name.compareTo(tms.Name) == 0) 
					{
						alreadyAdded = true;
						break;
					}
				}
				
				if (!alreadyAdded) {
					array.add(tms);
				}
			}
		}
		return array;
	}

	@Override
	public ArrayList<ReferenceIDDropDown> getRefDataProvider() {
		ArrayList<ReferenceIDDropDown> array = new ArrayList<ReferenceIDDropDown>();
		
		// add everything from MoveMaestro
		array.addAll(mSingletonHolder.mMMRefOutDTO.providerCodes);
		
		// add from TMS anything that doesn't have the exact Type and Description
		if (AzureAuthClient.ENABLE_TMS) {
			for (ReferenceIDDropDown tms : mSingletonHolder.mRefOutDTO.providerCodes) {
				boolean alreadyAdded = false;
				
				for (ReferenceIDDropDown mm : mSingletonHolder.mMMRefOutDTO.providerCodes) {
					if (mm.Type.compareTo(tms.Type) == 0 &&
							mm.Description.compareTo(tms.Description) == 0) 
					{
						alreadyAdded = true;
						break;
					}
				}
				
				if (!alreadyAdded) {
					array.add(tms);
				}
			}
		}
		return array;
	}
	
	@Override
	public void deallocateMemory() {
		mSingletonHolder.mFormInDTO = null;
		mSingletonHolder.mUploadQueue = null;
		mSingletonHolder.mDownloadQueue = null;
		mSingletonHolder.mPRVResults = null;
		mSingletonHolder.mCurrentForm = null;	// target for delete and booking
		mSingletonHolder.mCurrentRoom = null;
		mSingletonHolder.mPRVUpload = null;
		mSingletonHolder.mRefOutDTO = null;
		mSingletonHolder.myLocation = null;
		
		mSingletonHolder.mSearchPRVInDTO = null;
		mSingletonHolder.mDownloadPRVInDTO = null;
		mSingletonHolder.mDeletePRVInDTO = null;
		mSingletonHolder.mBookPRVInDTO = null;
		mSingletonHolder.mIncompletePRVInDTO = null;
		mSingletonHolder.mSummaryOutDTO = null;
		
		mSingletonHolder.mMMRefOutDTO = null;
	}
}
