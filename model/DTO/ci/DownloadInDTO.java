package com.contactpoint.model.DTO.ci;



public class DownloadInDTO {

	public String getCIType() 					{ return CIType; }
	public String getGroupParentOrderLineId() 	{ return GroupParentOrderLineId; }
	public String getOrderLineId() 				{ return OrderLineId; }
	public String getPONumber() 				{ return PONumber; }
	
	public void setCIType(String cIType) 								 { CIType = cIType; }
	public void setGroupParentOrderLineId(String groupParentOrderLineId) { GroupParentOrderLineId = groupParentOrderLineId; }
	public void setOrderLineId(String orderLineId) 						 { OrderLineId = orderLineId; }
	public void setPONumber(String pONumber) 							 { PONumber = pONumber; }
	
	private String CIType;
	private String GroupParentOrderLineId;
	private String OrderLineId;
	private String PONumber;
	
	public String Tracker;

	public final String NAMESPACE = "http://TMSConsultant.Model/2013/ProcessCI";
	public final String ACTION = "http://TMSConsultant.Model/2013/ProcessCI/DownloadCI";
	
	public final String MM_NAMESPACE = "http://tempuri.org/";
	public final String MM_ACTION = "http://tempuri.org/ICIService/DownloadCI";
	
}
