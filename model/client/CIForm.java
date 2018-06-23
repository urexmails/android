package com.contactpoint.model.client;

import java.io.Serializable;

import com.contactpoint.model.DTO.ci.UploadInDTO;
import com.contactpoint.model.client.ci.CIDownload;

public class CIForm implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3671297453435431816L;
	public static final String UPLIFT = "UPLIFT";
	public static final String DELIVERY = "DELIVERY";
	
	private CIDownload mCIDownload;
	private UploadInDTO mCIUpload;
	private int mUploadCounter = -1;
	private boolean isMM;
	
	public CIDownload getDownload()				{ return mCIDownload; }
	public UploadInDTO getUpload()				{ return mCIUpload; }
	public boolean isMM()						{ return isMM; }

	public void setDownload(CIDownload param)	{ mCIDownload = param; }
	public void setUpload(UploadInDTO param)	{ mCIUpload = param; }
	public void setIsMM(boolean param)			{ isMM = param; }
	
	public boolean isUplift() {
		return mCIDownload.cIType.toUpperCase().compareTo(UPLIFT) == 0;
	}
	
	public boolean isDelivery() {
		return mCIDownload.cIType.toUpperCase().compareTo(DELIVERY) == 0;
	}
	
	public int incrementCounter() {
		return ++mUploadCounter;
	}
	
	public void decrementCounter() {
		mUploadCounter--;
	}
	
}
