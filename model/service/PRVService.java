package com.contactpoint.model.service;

import java.util.ArrayList;
import java.util.Map;
import java.util.Vector;

import org.apache.http.NameValuePair;

import android.content.Context;

import com.contactpoint.controller.NetworkListener;
import com.contactpoint.model.DTO.prv.BookPRVInDTO;
import com.contactpoint.model.DTO.prv.DownloadPRVInDTO;
import com.contactpoint.model.DTO.prv.IncompletePRVInDTO;
import com.contactpoint.model.DTO.prv.ItemInDTO;
import com.contactpoint.model.DTO.prv.RoomOutDTO;
import com.contactpoint.model.DTO.prv.SearchPRVInDTO;
import com.contactpoint.model.DTO.prv.SummaryOutDTO;
import com.contactpoint.model.client.PRVForm;
import com.contactpoint.model.client.authentication.AuthenticationToken;
import com.contactpoint.model.client.prv.Client;
import com.contactpoint.model.client.prv.Item;
import com.contactpoint.model.client.prv.PRVDownload;
import com.contactpoint.model.client.prv.PRVResult;
import com.contactpoint.model.client.prv.ReferenceData;
import com.contactpoint.model.client.prv.ReferenceIDDropDown;
import com.contactpoint.model.client.prv.Room;
import com.contactpoint.model.client.prv.VectorPRVResult;

public interface PRVService {

	public final int PRV_SEARCH = 4;
	public final int PRV_DOWNLOAD = 5;
	public final int PRV_BOOKING = 6;
	public final int PRV_DELETE = 7;
	public final int PRV_INCOMPLETE = 8;
	public final int PRV_UPLOAD = 9;
	public final int PRV_REF_CHECK = 10;
	public final int PRV_REF_DOWNLOAD = 11;
	public final int PRV_REDOWNLOAD = 12;
	public final int PRV_CANCELLED = 13;
	public final int PRV_ENVIRONMENT = 14;
	public final int PRV_MM_REF_CHECK = 15;
	public final int PRV_MM_REF_DOWNLOAD = 16;
	public final int PRV_MM_SEARCH = 17;
	public final int PRV_RELEASED = 18;
	
	public final int ERROR = -2;
	public final int RESULT_EMPTY = -4;
    //public final static String PATH = "/demo/TMSConsultant/PrecompiledWeb/TMSConsultantWCF.Host/Processprv.svc";
	//public final static String PATH = "/demo/TMSConsultant/PrecompiledWeb/TMSConsultantWCF.HostV3/ProcessPRV.svc";
    //public final String PATH = "/demo/TMSConsultant/PrecompiledWeb/TMSConsultantWCF.Host/ProcessPRV.svc";
    public final String PATH = "/demo/TMSConsultant/PrecompiledWeb/TollAppMM/ProcessPRV.svc";
    public final String PATH_MM = "/tabletapi/PRVService.svc";
    public final String MM_ACTION_PREFIX = "http://tempuri.org/IPRVService/";
    public final String YES 		= "1";
	public final String NO 			= "0";
	
	public final int PLUS  = 0;
	public final int MINUS = 1;
	
	public final String AUTO_CANCEL_REASON = "PRV Cancelled";
	public final String DATE_TIME_FORMAT   = "dd/MM/yyyy hh:mm:ss a";
	public final String DATE_FORMAT 	   = "dd/MM/yyyy";

//	public final static String EXCELLENT 	= "5";
//	public final static String GOOD 		= "4";
//	public final static String AVERAGE 		= "3";
//	public final static String POOR 		= "2";
//	public final static String VERY_POOR 	= "1";	
	
    /* Wireframe methods */
    public void initPRVInDTO();
    public void initRoomSummaryInDTO(Room room);
    public Vector<ItemInDTO> getCurrentItemList();
    public void addItem(ItemInDTO i);
    public void removeItem(ItemInDTO i);
    public void addCrate(ItemInDTO i);
    public void removeCrate(ItemInDTO i);
    public double calculateVolume(String l, String w, String d);
    
	public void setNetworkListener(NetworkListener listener);
	public int executeSoapRequest();
	public String getClientMessage();
	public String getResponseMessage();
	
	public SearchPRVInDTO getSearchPRVInDTO();
	public DownloadPRVInDTO getDownloadPRVInDTO();
	public BookPRVInDTO getBookPRVInDTO();
	public IncompletePRVInDTO getIncompletePRVInDTO();
	
	public VectorPRVResult getPRVResults();
	public Vector<PRVForm> getPRVForms();
	public Vector<PRVForm> getUploadQueue();
	public Vector<PRVResult> getDownloadQueue();
	public ReferenceData getReferenceDataOutDTO(boolean isMM);
	public boolean refDataIsUpToDate();
	public boolean refDataNeedRefresh();
	
	//public AuthenticationToken getAuthenticationToken();
	public void setAuthenticationToken(AuthenticationToken param);
	
	public void preparePRVSearch();
	public void preparePRVMMSearch();
	public void preparePRVDownload();
	public void preparePRVDownload(PRVForm target);
	public void preparePRVBooking();
	public void preparePRVDelete();
	public void preparePRVIncomplete();
	public void preparePRVUpload();
	public void preparePRVCancelled();
	public void preparePRVRelease();
	
	public void prepareRefDataCheckDate();
	public void prepareRefDataDownload();

	// Move Maestro Methods
	public void prepareGetEnvironment();
	public void prepareMMRefDataCheckDate();
	public void prepareMMRefDataDownload();
	//public ReferenceData getMMReferenceDataOutDTO();
	public boolean mmRefDataIsUpToDate();
	public boolean mmRefDataNeedRefresh();
	public void saveMMRefData(Context context);
	
	public void saveRefData(Context context);
	public void saveFormData(Context context);
	public void loadFormData(Context context);
	
	public PRVForm getCurrentForm();
	public RoomOutDTO getCurrentRoom();
	public void setCurrentForm(PRVForm form);
	public void setCurrentRoom(int position);
	
	public void refreshSummaryOutDTO();
	public <K, V> Vector<NameValuePair> mapToNameValuePair(Map<K, V> map);
	public SummaryOutDTO getSummaryOutDTO();
	public void putQuestionAnswer(Context context, String questionCode, int id);
	public void putQuestionAnswer(Context context, String questionCode, boolean val);
	public void putQuestionAnswer(Context context, String questionCode, String ans);
	public void startTimeStamp(Context context);
	
	public double getRoomRunningTotal(RoomOutDTO room);
	public double getRoomItemVolume(RoomOutDTO room);
	public double getRoomAddOnVolume(RoomOutDTO room);
	
	public Item[] getStandardItems();
	
	public void sortForms(int criteria);
	
	public boolean completeDateDiffers();
	
	public void populateUploadQueue(Context context);
	
	public String getCombinedPONumber(PRVDownload form);
	
	public Vector<RoomOutDTO> getCurrentRoomList();
	public void setServiceModeD2D(boolean param);
	public boolean validateItemCollection();
	
	public void onFinalised();
	
	public ArrayList<ReferenceIDDropDown> getBookingReasonCodes(String type);
	
	public boolean validateLimit(ItemInDTO item, int val, int sign);
	public void updateLimit(ItemInDTO item, int val, int sign);
	
	public ArrayList<ReferenceIDDropDown> getPRVQuestions(String type);
	
	public ArrayList<ReferenceIDDropDown> getRefDataRegion();
	public ArrayList<ReferenceIDDropDown> getRefDataZone();
	public ArrayList<ReferenceIDDropDown> getRefDataBusinessGroup();
	public ArrayList<ReferenceIDDropDown> getRefDataVolume();
	public ArrayList<Client> getRefDataClient();
	public ArrayList<ReferenceIDDropDown> getRefDataProvider();
	public void deallocateMemory();

}
