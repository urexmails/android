package com.contactpoint.model.service;

import android.content.Context;

import com.contactpoint.controller.NetworkListener;
import com.contactpoint.model.DTO.PhotoInDTO;
import com.contactpoint.model.client.authentication.AuthenticationToken;

public interface PhotoService {

	public final int PHOTO_UPLOAD = 400;
	public final int ERROR = -400;
    //public final String PATH = "/demo/TMSConsultant/PrecompiledWeb/TMSConsultantWCF.Host/ProcessPRV.svc";
    public final String PATH = "/demo/TMSConsultant/PrecompiledWeb/TollAppMM/ProcessPRV.svc";
    public final String PATH_MM = "/tabletapi/PRVService.svc";
    
	public void setNetworkListener(NetworkListener listener);
	public int executeSoapRequest();
	public String getClientMessage();
	public String getResponseMessage();
	public void setAuthenticationToken(AuthenticationToken param);
	
	public void addPhotoInDTO(PhotoInDTO inDTO);
	public void removePhoto(Context context, String poNumber);
	public void clearPhoto(Context context);
	public boolean hasPhotoToBeUploaded();
	public int getPhotoLimit(String appType);
	public int getTotalPhoto(String poNumber);
	public void finalisePhoto(String poNumber);
	public void initialisePhotoUpload();
	
	public void saveFormData(Context context);
	public void loadFormData(Context context);
	
	public void preparePhotoUpload();
	public String getFinalMessage();
}
