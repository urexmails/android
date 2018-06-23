package com.contactpoint.model.DTO.ci;



public class SearchCIInDTO {

	public String getBusinessGroup() 					{ return BusinessGroup; }
	public String getFromDate() 						{ return FromDate; }
	public String getRegion() 							{ return Region; }
	public String getToDate() 							{ return ToDate; }
	public String getVolume() 							{ return Volume; }
	public String getZone() 							{ return Zone; }
	
	public void setBusinessGroup(String businessGroup) 							{ BusinessGroup = businessGroup; }
	public void setFromDate(String fromDate) 									{ FromDate = fromDate; }
	public void setRegion(String region) 										{ Region = region; }
	public void setToDate(String toDate) 										{ ToDate = toDate; }
	public void setVolume(String volume) 										{ Volume = volume; }
	public void setZone(String zone) 											{ Zone = zone; }

	private String BusinessGroup;
	private String FromDate;
	private String Region;
	private String ToDate;
	private String Volume;
	private String Zone;
	
	public final String NAMESPACE = "http://TMSConsultant.Model/2013/ProcessCI";
	public final String ACTION 	 = "http://TMSConsultant.Model/2013/ProcessCI/SearchCI";

	public final String MM_NAMESPACE = "http://tempuri.org/";
	public final String MM_ACTION 	 = "http://tempuri.org/ICIService/SearchCI";
}
