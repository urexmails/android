package com.contactpoint.model.DTO.prv;

public class IncompletePRVInDTO {

	public String getPONumber() { return PONumber; }
	public String getReason() 	{ return Reason; }
	
	public void setPONumber(String pONumber) 	{ PONumber = pONumber; }
	public void setReason(String reason) 		{ Reason = reason; }
	
	private String PONumber;
	private String Reason;
	
	public String Tracker;
	
	public final static String NAMESPACE = "http://TMSConsultant.Model/2013/ProcessPRV";
	public final static String ACTION 	 = "IncompletePRV";
	
	public final static String MM_NAMESPACE = "http://tempuri.org/";
	public final static String MM_ACTION 	= "http://tempuri.org/IPRVService/IncompletePRV";
	
	public final static String INCOMPLETE_SUCCESSFUL = "PRV Incomplete Successful";
}
