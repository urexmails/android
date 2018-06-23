package com.contactpoint.model.service;

import java.util.Vector;

import android.content.Context;

import com.contactpoint.controller.NetworkListener;
import com.contactpoint.model.DTO.ci.DeleteInDTO;
import com.contactpoint.model.DTO.ci.DownloadInDTO;
import com.contactpoint.model.DTO.ci.SearchCIInDTO;
import com.contactpoint.model.DTO.ci.UploadInDTO;
import com.contactpoint.model.client.CIForm;
import com.contactpoint.model.client.authentication.AuthenticationToken;
import com.contactpoint.model.client.ci.CIResult;

public interface GetCIService {

	public final static int SEARCH = 0;
	public final static int DOWNLOAD = 1;
	public final static int DELETE = 2;
	public final static int UPLOAD = 3;
	public final static int ERROR = -1;
	public final static int RESULT_EMPTY = -3;
	public final static int MM_SEARCH = 200;
	public final static int MM_DOWNLOAD = 201;
	public final static int MM_DELETE = 202;
	public final static int MM_UPLOAD = 203;
	
	//public final static String PATH = "/demo/TMSConsultant/PrecompiledWeb/TMSConsultantWCF.HostV3/ProcessCI.svc";
	//public final static String PATH = "/demo/TMSConsultant/PrecompiledWeb/TMSConsultantWCF.Host/ProcessCI.svc";
	public final String PATH = "/demo/TMSConsultant/PrecompiledWeb/TollAppMM/ProcessCI.svc";
    public final String PATH_MM = "/tabletapi/CIService.svc";
	public static final String DATE_FORMAT = "dd/MM/yyyy hh:mm:ss a";
	
	public void setNetworkListener(NetworkListener listener);
	public int executeSoapRequest();
	public String getClientMessage();
	public String getResponseMessage();
	
	public Vector<CIResult> getDeliveryResults();
	public Vector<CIResult> getUpliftResults();
	public Vector<CIForm> getDeliveryForms();
	public Vector<CIForm> getUpliftForms();
	public Vector<CIForm> getDeleteQueue();
	public Vector<CIForm> getUploadQueue();
	public Vector<CIResult> getDownloadQueue();
	
	public AuthenticationToken getAuthenticationToken();
	public void setAuthenticationToken(AuthenticationToken param);
	public void prepareSearch();
	public void prepareDownload();
	public void prepareUpload();
	public void prepareDelete();

	public void prepareMMSearch();
	
	public void populateUploadQueue();
	public void populateDeleteQueue();
	
	public SearchCIInDTO getSearchInDTO();
	public DownloadInDTO getDownloadInDTO();
	public DeleteInDTO getDeleteInDTO();
	public UploadInDTO getUploadInDTO();
	public CIForm getCurrentForm();
	public void setCurrentForm(CIForm form);
	
	public void saveFormData(Context context);
	public void loadFormData(Context context);
	
	public void findMyLocation(Context context);
	
	public void sortForms(int which, int criteria);
	
	public void onFinalised();
	
	public void deallocateMemory();
}
