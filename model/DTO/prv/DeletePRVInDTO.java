package com.contactpoint.model.DTO.prv;


public class DeletePRVInDTO {

	public String getBookingDetailsID() { return BookingDetailsID; }
	public String getPONumber() 		{ return PONumber; }
	
	public void setBookingDetailsID(String bookingDetailsID) { BookingDetailsID = bookingDetailsID; }
	public void setPONumber(String pONumber) 				 { PONumber = pONumber; }
	
	private String BookingDetailsID;
	private String PONumber;
	
	public String Tracker;
	
	public final static String NAMESPACE = "http://TMSConsultant.Model/2013/ProcessPRV";
	public final static String ACTION 	 = "DeletePRV";

	public final static String MM_NAMESPACE = "http://tempuri.org/";
	public final static String MM_CONTRACT_NAMESPACE = "http://schemas.datacontract.org/2004/07/TollTransitions.API.DataContracts";
	public final static String MM_ACTION 	 = "http://tempuri.org/IPRVService/DeletePRV";
	
	public final static String DELETE_SUCCESSFUL = "Delete Successful";
	public final static String DELETE_FAILED = "Delete Failed";

}
