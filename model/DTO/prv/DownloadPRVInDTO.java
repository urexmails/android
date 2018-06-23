package com.contactpoint.model.DTO.prv;


public class DownloadPRVInDTO {

	public String getBookingDetailsID()			{ return BookingDetailsID; }
	public String getGroupParentOrderLineId() 	{ return GroupParentOrderLineId; }
	public String getOrderLineId() 				{ return OrderLineId; }
	public String getPONumber() 				{ return PONumber; }

	public void setBookingDetailsID(String bookingDetailsID)			 { BookingDetailsID = bookingDetailsID; }
	public void setGroupParentOrderLineId(String groupParentOrderLineId) { GroupParentOrderLineId = groupParentOrderLineId; }
	public void setOrderLineId(String orderLineId) 						 { OrderLineId = orderLineId; }
	public void setPONumber(String pONumber) 							 { PONumber = pONumber; }
	
	private String BookingDetailsID;
	private String GroupParentOrderLineId;
	private String OrderLineId;
	private String PONumber;
	
	public String Tracker;
	public final static String NAMESPACE = "http://TMSConsultant.Model/2013/ProcessPRV";
	public final static String ACTION 	 = "SelectAndDownloadPRV";

	public final static String MM_NAMESPACE = "http://tempuri.org/";
	public final static String MM_ACTION 	 = "http://tempuri.org/IPRVService/SelectAndDownloadPRV";
}
