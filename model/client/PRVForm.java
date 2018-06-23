package com.contactpoint.model.client;

import java.io.Serializable;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;

import com.contactpoint.model.ModelFactory;
import com.contactpoint.model.DTO.prv.ItemInDTO;
import com.contactpoint.model.DTO.prv.RoomOutDTO;
import com.contactpoint.model.client.prv.Item;
import com.contactpoint.model.client.prv.Items;
import com.contactpoint.model.client.prv.PRVDownload;
import com.contactpoint.model.client.prv.ServiceDetail;
import com.contactpoint.model.client.prv.VectorPRVDownload;
import com.contactpoint.model.service.UtilService;

public class PRVForm implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6870237288784367537L;
	public static int newRoomId = 0;
    public static int newItemId = 0;
    
    public static final String PP_DATE_IDENTIFIER = "Prepack";
	private final String UNBOOKED_DATE = "01/01/0001 12:00:00 AM";

    private String mPPDate;
	private VectorPRVDownload mPRVDownload;
	private Vector<RoomOutDTO> mD2DRoomOutDTO = new Vector<RoomOutDTO>();
	private Vector<RoomOutDTO> mD2SRoomOutDTO;
	private Items mD2DLimiter = null;
	private Items mD2SLimiter = null;
	private HashMap<String, String> mQuestionAnswer = new HashMap<String, String>();
	private int mOrder = 0;
	private boolean mIsReady = false;
	private boolean mIsD2D = true;
	private boolean mItemCollectionCompleted = false;
	private boolean mChecklistCompleted = false;
	private boolean mUploadedOnce = false;					// parameter for D2D and D2S to determine if one has been uploaded
	private int mUploadCounter = -1;
    private boolean isMM;

	public int getOrder()					 { return mOrder; }
	public boolean isReady()				 { return mIsReady; }
	public boolean isD2D()					 { return mIsD2D; }
	public Items getD2DLimiter()			 { return mD2DLimiter; }
	public Items getD2SLimiter()			 { return mD2SLimiter; }
	public boolean itemCollectionCompleted() { return mItemCollectionCompleted; }
	public boolean checklistCompleted()		 { return mChecklistCompleted; }
	public boolean uploadedOnce()			 { return mUploadedOnce; }
	public String getPPDate()				 { return mPPDate; }
	public boolean isMM()					 { return isMM; }
	
	public void setOrder(int param)						  { mOrder = param; }
	public void setReady(boolean param)					  { mIsReady = param; }
	public void setD2D(boolean param)					  { mIsD2D = param; }
	public void setItemCollectionCompleted(boolean param) { mItemCollectionCompleted = param; }
	public void setChecklistCompleted(boolean param)	  { mChecklistCompleted = param; }
	public void setUploadedOnce(boolean param)			  { mUploadedOnce = param; }
	public void setPPDate(String param)					  { mPPDate = param; }
	public void setIsMM(boolean param)					  { isMM = param; }

	public VectorPRVDownload getDownload()			   { return mPRVDownload; }
	public Vector<RoomOutDTO> getD2DRoomList()		   { return mD2DRoomOutDTO; }
	public Vector<RoomOutDTO> getD2SRoomList()		   { return mD2SRoomOutDTO; }
	public HashMap<String, String> getQuestionAnswer() { return mQuestionAnswer; }

	public void setDownload(VectorPRVDownload param) { 
		// when calling this method, ensure isMM variable has been setup properly
		// otherwise we will get the wrong reference data
		
		mPRVDownload = param; 
		
		// initialise limiter
		if (mD2DLimiter == null) {
			mD2DLimiter = new Items();
			for (Item i : ModelFactory.getPRVService().getReferenceDataOutDTO(isMM).items.values()) {
				Item item = new ItemInDTO();
				item.ID = i.ID;
				item.MaxNumber = i.MaxNumber;
				item.isMM = isMM;
				mD2DLimiter.put(item.ID, item);
			}
		}
		//mD2DLimiter.putAll(ModelFactory.getPRVService().getReferenceDataOutDTO().items);
		if (mPRVDownload.size() > 0 && mD2SRoomOutDTO == null) {
			mD2SRoomOutDTO = new Vector<RoomOutDTO>();
			if (mD2SLimiter == null) {
				mD2SLimiter = new Items();
				//mD2SLimiter.putAll(mD2DLimiter);
				for (Item i : ModelFactory.getPRVService().getReferenceDataOutDTO(isMM).items.values()) {
					Item item = new ItemInDTO();
					item.ID = i.ID;
					item.MaxNumber = i.MaxNumber;
					item.isMM = isMM;
					mD2SLimiter.put(item.ID, item);
				}
			}
		}
		mPPDate = "";
		parsePPDate();
	}
	
	public int incrementCounter() {
		return ++mUploadCounter;
	}
	
	public void decrementCounter() {
		mUploadCounter--;
	}
	
	private void parsePPDate() {
		// retrieve PPDate from PRVDownload
		for (PRVDownload download : mPRVDownload) {
			for (ServiceDetail detail : download.serviceDetails) {
				// Service with name = PP_DATE_IDENTIFIER
				if (detail.ServiceName.contains(PP_DATE_IDENTIFIER)) {
					if (mPPDate == null || mPPDate.length() == 0) {
						mPPDate = detail.ServiceDate;
					} else {
						try {
							Date first = ModelFactory.getUtilService().getDateFromString(mPPDate, UtilService.DATE_FORMAT);
							Date second = ModelFactory.getUtilService().getDateFromString(detail.ServiceDate, UtilService.DATE_FORMAT);
							if (first.compareTo(second) < 0) {
								mPPDate = detail.ServiceDate;
							}
						} catch (ParseException e) {
							e.printStackTrace();
							System.out.println("Error format");
						}
					}
				}
			}
		}
		
	}
	
	public boolean isBooked() {
		return mPRVDownload.get(0).bookedDateTime != null && 
				mPRVDownload.get(0).bookedDateTime.compareTo(UNBOOKED_DATE) != 0;
	}
}
