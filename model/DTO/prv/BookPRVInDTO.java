package com.contactpoint.model.DTO.prv;

public class BookPRVInDTO {

	public String getBookedDate() 				{ return BookedDate; }
	public String getBookingDetailsID() 		{ return BookingDetailsID; }
	public String getContactFailureCode()		{ return ContactFailureCode; }
	public String getContactFailureDate()		{ return ContactFailureDate; }
	public String getContactFailureDetails()	{ return ContactFailureDetails; }
	public String getPONumber() 				{ return PONumber; }
	public String getPastEndDateReasonCode()	{ return PastEndDateReasonCode; }
	public String getPastEndDateReasonDetails()	{ return PastEndDateReasonDetails; }
	public String getReBookingReasonCode()		{ return ReBookingReasonCode; }
	public String getReBookingReasonDetails()	{ return ReBookingReasonDetails; }
	
	public void setBookedDate(String bookedDate) 			  		{ BookedDate = bookedDate; }
	public void setBookingDetailsID(String bookingDetailsID)  		{ BookingDetailsID = bookingDetailsID; }
	public void setContactFailureCode(String reasonCode) 	  		{ ContactFailureCode = reasonCode; }
	public void setContactFailureDate(String date) 	  				{ ContactFailureDate = date; }
	public void setContactFailureDetails(String reasonDetails)		{ ContactFailureDetails = reasonDetails; }
	public void setPONumber(String pONumber) 				  		{ PONumber = pONumber; }
	public void setPastEndDateReasonCode(String reasonCode) 	  	{ PastEndDateReasonCode = reasonCode; }
	public void setPastEndDateReasonDetails(String reasonDetails)	{ PastEndDateReasonDetails = reasonDetails; }
	public void setReBookingReasonCode(String reasonCode) 	  		{ ReBookingReasonCode = reasonCode; }
	public void setReBookingReasonDetails(String reasonDetails)		{ ReBookingReasonDetails = reasonDetails; }
	
	private String BookedDate;
	private String BookingDetailsID;
	private String ContactFailureCode;
	private String ContactFailureDate;
	private String ContactFailureDetails;
	private String PONumber;
	private String PastEndDateReasonCode;
	private String PastEndDateReasonDetails;
	private String ReBookingReasonCode;
	private String ReBookingReasonDetails;
	
	public final static String NAMESPACE = "http://TMSConsultant.Model/2013/ProcessPRV";
	public final static String ACTION 	 = "BookPRV";
	
	public final static String MM_NAMESPACE = "http://tempuri.org/";
	public final static String MM_ACTION 	 = "http://tempuri.org/IPRVService/BookPRV";
	
}
