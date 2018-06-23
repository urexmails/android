package com.contactpoint.model.DTO.prv;

import com.contactpoint.model.client.prv.PRVUpload;

public class UploadPRVInDTO {
	public PRVUpload getPRVUpload() 		{ return PRVUpload; }
	public String getPONumber() 			{ return PONumber; }
//	public boolean isValid()				{ return isValid; }
	
	public void setPRVUpload(PRVUpload pRVUpload) 	{ PRVUpload = pRVUpload; }
	public void setPONumber(String pONumber) 		{ PONumber = pONumber; }
//	public void setValid(boolean param)				{ isValid = param; }
	
	private PRVUpload PRVUpload;
	private String PONumber;
//	private boolean isValid;
	
	public final static String NAMESPACE = "http://TMSConsultant.Model/2013/ProcessPRV";
	public final static String ACTION 	 = "UploadPRV";
	
	public final static String MM_NAMESPACE = "http://tempuri.org/";
	public final static String MM_ACTION 	= "http://tempuri.org/IPRVService/UploadPRV";
	
	public final static String UPLOAD_SUCCESSFUL = "Upload Successful";
	
	public static final String P_ACCESS_ = "P_ACCESS_";
	public static final String U_ = "U_";
	public static final String D_ = "D_";
	public static final String [] ACCESS = {
		"GROUND_FLOOR", "PARKING_RESTRICT", "2_STOREY",
		"LIFT_ACCESS", "NO_PARK_DRIVE", "HIGH_RISE",
		"FERRY", "NO_PARK_STREET", "STEEP_STAIRS",
		"POWER_LINES", "REAR_ACCESS", "STEEP_DRIVE",
		"NO_DRIVEWAY", "20_METRE_TRAMP", "STAIR_ACCESS"
	};
	
	public static final String [] MM_ACCESS = {
		"20_METRE_TRAMP", "2_STOREY", "FERRY", 
		"GROUND_FLOOR", "HIGH_RISE", "LIFT_ACCESS", 
		"POWER_LINES", "NO_DRIVEWAY", "NO_PARK_DRIVE", 
		"NO_PARK_STREET", "PARKING_RESTRICT", "REAR_ACCESS", 
		"STAIR_ACCESS", "STEEP_DRIVE", "STEEP_STAIRS"
	};
	
	public final static String P_PRV_ = "P_PRV_";
	//public final static String P_UPLIFT_NOTES = "P_UPLIFT_NOTES";
	//public final static String P_DELIVERY_NOTES = "P_DELIVERY_NOTES";
	public final static String P_GENERAL_COMMENTS = "P_GENERAL_COMMENTS";
	public final static String P_INTERNAL_COMMENTS = "P_INTERNAL_COMMENTS";
	public final static String P_GPS_COORDINATES = "P_GPS_COORDINATES";
	public final static String P_DATE_DIFFERS = "P_DATE_DIFFERS";
	public final static String P_TRANSITIONS_START_TIMESTAMP = "P_TRANSITIONS_START_TIMESTAMP";
	
	public final static String P_DD_IS_TRANSF_RESCHEDULE = "P_DD_IS_TRANSF_RESCHEDULE";
	public final static String P_DD_TRANSF_UNAVAILABLE = "P_DD_TRANSF_UNAVAILABLE";
	public final static String P_DD_SHORT_NOTICE = "P_DD_SHORT_NOTICE";
	//public final static String P_DD_TOLL_RESCHEDULE = "P_DD_TOLL_RESCHEDULE";
	public final static String P_DD_PRS_AFTER_SERVICE_END = "P_DD_PRS_AFTER_SERVICE_END";
	public final static String P_DD_INCORRECT_BOOKING = "P_DD_INCORRECT_BOOKING";
	public final static String P_DD_COMMENTS = "P_DD_COMMENTS";
	public final static String P_BOOKING_DETAILS_ID = "P_BOOKING_DETAILS_ID";
	public final static String P_TOTAL_VOLUME = "P_TOTAL_VOLUME";
	public final static String P_DD_OTHER = "P_DD_OTHER";
	
	public final static String P_TO_WORKLIST_DATE = "P_TO_WORKLIST_DATE";
	public final static String P_DELETED_DATE = "P_DELETED_DATE";
	public final static String P_INCOMPLETE_DATE = "P_INCOMPLETE_DATE";
	public final static String P_APP_VERSION = "P_APP_VERSION";
	public final static String P_FINALISED_DATE = "P_FINALISED_DATE";
	public final static String P_GEO_CODE_FINALISED = "P_GEO_CODE_FINALISED";
	public final static String P_UPLOAD_DATE = "P_UPLOAD_DATE";
	public final static String P_GEOCODE_UPLOAD = "P_GEOCODE_UPLOAD";
	public final static String P_UPLOAD_RETRY_COUNT = "P_UPLOAD_RETRY_COUNT";
	
		
	/*
	 * P_PURCHASE_ORDER_NUMBER,
P_ORDER_NUMBER,
P_AH_PURCHASE_ORDER_NO
P_CASE_NUMBER,
P_PHONE_AH,
P_PhONE_BH
P_PHONE_MOBILE,
P_EMAIL,
P_START_ADDRESS1,
P_START_ADDRESS2,
P_START_CITY,
P_START_STATE,
P_START_POSTCODE,
P_END_ADDRESS1,
P_END_ADDRESS2,
P_END_CITY,
P_END_STATE,
P_END_POSTCODE,
P_ISPROCESSED,
P_AH_PURCHASE_ORDER_ID,
P_USER_NAME,
P_CARTONS_STD,
P_CARTONS_BOOK,
P_CARTONS_PORTA_ROBE,
P_CARTON_PICTURE,
P_CARTON_BIKE,
P_CARTON_PLASMA,
P_COVERS_SETTEE,
P_COVERS_ARMCHAIR,
P_COVERS_DINING,
P_COVERS_QUEEN,
P_COVERS_KING,
P_COVERS_DOUBLE,
P_COVERS_SINGLE
P_CUSTOMER_FIRSTNAME
P_CUSTOMER_LASTNAME
P_CUSTOMER_TITLE
P_SERVICE_START_DATE
P_SERVICE_END_DATE
P_REMOVAL_SERVICE_TYPE
P_PREPACK
P_UPLIFT
P_DELIVERY


	 */
}
