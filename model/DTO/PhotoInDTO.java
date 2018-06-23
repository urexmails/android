package com.contactpoint.model.DTO;

public class PhotoInDTO {

	public String getAppType() 	  { return AppType; }
	public String getCategory()   { return Category; }
	public String getComment() 	  { return Comment; }
	public String getPONumber()   { return PONumber; }
	public String getPhoto() 	  { return Photo; }
	public boolean toBeUploaded() { return toBeUploaded; }
	public boolean failUploaded() { return failUploaded; }
	public boolean isMM() 		  { return isMM; }

	public void setAppType(String appType) 	 		  { AppType = appType; }
	public void setCategory(String category) 		  { Category = category; }
	public void setComment(String comment) 	 		  { Comment = comment; }
	public void setPONumber(String pONumber) 		  { PONumber = pONumber; }
	public void setPhoto(String photo) 		 		  { Photo = photo; }
	public void setToBeUploaded(boolean toBeUploaded) { this.toBeUploaded = toBeUploaded; }
	public void setFailUploaded(boolean failUploaded) { this.failUploaded = failUploaded; }
	public void setIsMM(boolean isMM) 				  { this.isMM = isMM; }
	
	private String AppType;
	private String Category;
	private String Comment;
	private String PONumber;
	private String Photo;
	private boolean toBeUploaded;
	private boolean failUploaded;
	private boolean isMM;
	
	public static final String NAMESPACE = "http://TMSConsultant.Model/2013/ProcessPRV";
	public static final String ACTION = "UploadPhoto";
	public final static String MM_NAMESPACE = "http://tempuri.org/";
	public final static String MM_ACTION 	= "http://tempuri.org/IPRVService/UploadPhoto";

}
