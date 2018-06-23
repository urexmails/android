package com.contactpoint.model.DTO.prv;

import com.contactpoint.model.client.prv.ReferenceIDDropDown;

public class SearchPRVInDTO {

	

	public String getBusinessGroup() 			 { return BusinessGroup; }
	public String getClientCode()	 			 { return ClientCode; }
	public String getFromDate() 	 			 { return FromDate; }
	public String getOrderNumber() 	 			 { return OrderNumber; }
	public String getPONumber() 	 			 { return PONumber; }
	public ReferenceIDDropDown getProviderCode() { return ProviderCode; }
	public String getRegion() 		 			 { return Region; }
	public String getToDate() 		 			 { return ToDate; }
	public String getZone() 		 			 { return Zone; }
	
	public void setBusinessGroup(String businessGroup) 			  { BusinessGroup = businessGroup; }
	public void setClientCode(String clientCode) 				  { ClientCode = clientCode; }
	public void setFromDate(String fromDate) 					  { FromDate = fromDate; }
	public void setOrderNumber(String orderNumber) 				  { OrderNumber = orderNumber; }
	public void setPONumber(String pONumber) 					  { PONumber = pONumber; }
	public void setProviderCode(ReferenceIDDropDown providerCode) { ProviderCode = providerCode; }
	public void setRegion(String region) 						  { Region = region; }
	public void setToDate(String toDate) 						  { ToDate = toDate; }
	public void setZone(String zone) 							  { Zone = zone; }
	
	private String BusinessGroup;
	private String ClientCode;
	private String FromDate;
	private String OrderNumber;
	private String PONumber;
	private ReferenceIDDropDown ProviderCode;
	private String Region;
	private String ToDate;
	private String Zone;
	
	public final static String NAMESPACE = "http://TMSConsultant.Model/2013/ProcessPRV";
	public final static String ACTION 	 = "SearchPRV";
	public final static String MM_NAMESPACE = "http://tempuri.org/";
	public final static String MM_ACTION 	 = "http://tempuri.org/IPRVService/SearchPRV";
	public final static int PROV_CODE_DEFAULT_ID = 0;

}
