package com.contactpoint.model.DTO.ci;

import java.io.Serializable;
import java.util.HashMap;

public class UploadInDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5753062458186320244L;
	public HashMap<String, String> getQuestionAnswers()   { return mQuestionAnswers; }
	public HashMap<String, String> getSignatureContract() { return mSignatureContract; }
	public boolean isValid() 							  { return mIsValid; }
	public boolean hasSignature()						  { return mHasSignatures; }
	
	public void setQuestionAnswers(HashMap<String, String> mQuestionAnswers) 	 { this.mQuestionAnswers = mQuestionAnswers; }
	public void setSignatureContract(HashMap<String, String> mSignatureContract) { this.mSignatureContract = mSignatureContract; }
	public void setIsValid(boolean mIsValid) 									 { this.mIsValid = mIsValid; }
	public void setHasSignatures(boolean mHasSignatures) 						 { this.mHasSignatures = mHasSignatures; }
	
	protected HashMap<String, String> mQuestionAnswers;
	protected HashMap<String, String> mSignatureContract;
	private boolean mIsValid;
	private boolean mHasSignatures;
	
	public final static String NAMESPACE = "http://TMSConsultant.Model/2013/ProcessCI";
	public final static String ACTION = "http://TMSConsultant.Model/2013/ProcessCI/UploadCI";
	
	public final static String MM_NAMESPACE = "http://tempuri.org/";
	public final static String MM_ACTION = "http://tempuri.org/ICIService/UploadCI";
	
	public final static String YES 			= "1";
	public final static String NO 			= "0";
	public final static String EXCELLENT 	= "5";
	public final static String GOOD 		= "4";
	public final static String AVERAGE 		= "3";
	public final static String POOR 		= "2";
	public final static String VERY_POOR 	= "1";	
	
	public final static String UPLOAD_SUCCESSFUL = "Upload Successful";
/*	
	P_PIMP_CCV_ID
	P_GROUP_PARENT_ORDER_LINE_ID
	P_ORDER_NUMBER
	P_FIRST_NAME
	P_SURNAME
	P_ADDRESS1
	P_ADDRESS2
	P_CITY
	P_POSTCODE
	P_PHONE
	P_PHONE_MOBILE
	P_AGREED_UOM_QTY
	P_REGION_DESCRIPTION
	P_SYSUSER_FIRST_NAME
	P_SYSUSER_SURNAME
	P_SUPPLIER_CODE
	P_FILE_NOTE_ID
	P_SUBMITTED_DATE
	P_LAST_SUBMITTED_DATE
	P_IS_DELETED
	P_IS_PROCESSED
	P_IS_CANCELLED
	P_USER_NAME
	P_USER_NAME_DB
	P_MEMBER_TITLE
	P_MEMBER_PMKEY
	P_REPRESENT_SERVICE_CENTRE_DES
	P_IS_MEMBER_SIGNATURE
	P_IS_MEMBERS_AGENT_SIGNATURE
	P_IS_DHRM_SIGNATURE
	P_IS_TT_REPRESENTATIVE_SIGNATU
	P_FORM_TYPE
	P_CANCELLATION_DATE
	P_CI_ORDER_LINE_ID
	P_IS_OL_CANCELLED
	P_CCV_FORM_ID 
	P_Q1_SERVICE_TYPE
	P_Q2P_7_PROV_MADE_AWARE_CHANGE
*/
	public static final String P_Q1_TIME_ATTENDING = "P_Q1_TIME_ATTENDING";
	public static final String P_Q1_SERVICE_ORDERED = "P_Q1_SERVICE_ORDERED";
	public static final String P_Q2_PROVIDER_CONFIRM = "P_Q2_PROVIDER_CONFIRM";
	public static final String P_Q2C_TIME_MET = "P_Q2C_TIME_MET";
	
	public static final String P_Q3C_NO_OF_CREW = "P_Q3C_NO_OF_CREW";
	public static final String P_Q3C_PROVIDER_LOGO_DISPLAYED = "P_Q3C_PROVIDER_LOGO_DISPLAYED";
	public static final String P_Q3C_1_PRESENTATION_CREW = "P_Q3C_1_PRESENTATION_CREW";
	public static final String P_Q3C_2_CUST_SERVICE_SKILL = "P_Q3C_2_CUST_SERVICE_SKILL";
	public static final String P_Q3C_3_COND_OF_VEHICLE = "P_Q3C_3_COND_OF_VEHICLE";
	public static final String P_Q3C_4_COND_OF_PACKAGING = "P_Q3C_4_COND_OF_PACKAGING";
	public static final String P_Q3C_5_STANDING_CONTAINER = "P_Q3C_5_STANDING_CONTAINER";
	public static final String P_Q3C_6_REMLST_ON_TIME = "P_Q3C_6_REMLST_ON_TIME";
	public static final String P_Q3C_7_REMLST_PROFESSIONAL = "P_Q3C_7_REMLST_PROFESSIONAL";
	public static final String P_Q3C_8_REM_CONDUCTED_TIMELY = "P_Q3C_8_REM_CONDUCTED_TIMELY";
	
	public static final String P_Q3C_9_SPEC_PACKAGING = "P_Q3C_9_SPEC_PACKAGING";
	public static final String P_Q3C_9A_SPEC_MATLS_PROVIDED = "P_Q3C_9A_SPEC_MATLS_PROVIDED";
	public static final String P_Q3C_9B_PLASMA_REAR_PROJ = "P_Q3C_9B_PLASMA_REAR_PROJ";
	public static final String P_Q3C_9B_BIKE = "P_Q3C_9B_BIKE";
	public static final String P_Q3C_9B_PAINTING = "P_Q3C_9B_PAINTING";
	public static final String P_Q3C_9B_OTHER = "P_Q3C_9B_OTHER";
	public static final String P_Q3C_9B_ALL = "P_Q3C_9B_ALL";
	public static final String P_Q3C_10_REMOVAL_AS_REQD = "P_Q3C_10_REMOVAL_AS_REQD";
	public static final String P_Q3C_11_ICR = "P_Q3C_11_ICR";
	public static final String P_Q3C_11A_ICR_CORRECT = "P_Q3C_11A_ICR_CORRECT";
	public static final String P_Q3C_12_REMLST_VEH_POS = "P_Q3C_12_REMLST_VEH_POS";
	public static final String P_Q3C_12A_TRUCK_IN_DRIVEWAY = "P_Q3C_12A_TRUCK_IN_DRIVEWAY";
	public static final String P_Q3C_12A_TRUCK_DBL_PARKED = "P_Q3C_12A_TRUCK_DBL_PARKED";
	public static final String P_Q3C_12A_TRUCK_ON_VERGE = "P_Q3C_12A_TRUCK_ON_VERGE";
	
	public static final String P_Q3C_13_REMLST_FE_RESP = "P_Q3C_13_REMLST_FE_RESP";
	public static final String P_Q3C_14_REMLST_TREATING_RESID = "P_Q3C_14_REMLST_TREATING_RESID";
	public static final String P_Q3C_15_PROVIDER_OHS_COMP = "P_Q3C_15_PROVIDER_OHS_COMP";
	public static final String P_Q3C_15A_NO_SAFETY_GEAR = "P_Q3C_15A_NO_SAFETY_GEAR";
	public static final String P_Q3C_15A_TRUCK_LOAD = "P_Q3C_15A_TRUCK_LOAD";
	public static final String P_Q3C_15A_UNSAFE_LIFT_CARRY = "P_Q3C_15A_UNSAFE_LIFT_CARRY";
	public static final String P_Q3C_15A_PACKING = "P_Q3C_15A_PACKING";
	public static final String P_Q3C_15A_TRUCK_LOCATION = "P_Q3C_15A_TRUCK_LOCATION";
	public static final String P_Q3C_15A_ALL = "P_Q3C_15A_ALL";
	
	public static final String P_Q4U_8_RATING = "P_Q4U_8_RATING";
	public static final String P_Q4U_7_REMLST_COMMS = "P_Q4U_7_REMLST_COMMS";
	public static final String P_Q4U_7_OVERALL_IMPRESSION = "P_Q4U_7_OVERALL_IMPRESSION";
	public static final String P_Q4U_7_NO_OF_CREW_INSUFF = "P_Q4U_7_NO_OF_CREW_INSUFF";
	public static final String P_Q4U_7_TREAT_SPEC_ITEMS = "P_Q4U_7_TREAT_SPEC_ITEMS";
	public static final String P_Q4U_7_ICR_COMPLETED = "P_Q4U_7_ICR_COMPLETED";
	
	public static final String P_Q6D_4_RATING = "P_Q6D_4_RATING";
	public static final String P_Q6D_4_REMLST_COMMS = "P_Q6D_4_REMLST_COMMS";
	public static final String P_Q6D_4_OVERALL_IMPRESSION = "P_Q6D_4_OVERALL_IMPRESSION";
	public static final String P_Q6D_4_NO_CREW_INSUFF = "P_Q6D_4_NO_CREW_INSUFF";
	public static final String P_Q6D_4_TREAT_ITEMS = "P_Q6D_4_TREAT_ITEMS";
	public static final String P_Q6D_4_ICR_COMPLETED = "P_Q6D_4_ICR_COMPLETED";
	
	public static final String P_Q8_1_RATING = "P_Q8_1_RATING";
	public static final String P_Q8_2_TIME_ON_SITE = "P_Q8_2_TIME_ON_SITE";

	public static final String P_Q5U_VOLUME = "P_Q5U_VOLUME";
	public static final String P_Q7D_VOLUME = "P_Q7D_VOLUME";
	public static final String P_Q9_COMM = "P_Q9_COMMENTS";

	public static final String P_Q6D_1_MEM_ADVISED_PAID = "P_Q6D_1_MEM_ADVISED_PAID";
	public static final String P_Q6D_2_MEM_TOLL_WARR = "P_Q6D_2_MEM_TOLL_WARR";
	public static final String P_Q6D_3_DELIVERY_COMPLETED = "P_Q6D_3_DELIVERY_COMPLETED";
	public static final String P_Q6D_4_MEM_REM_DEBRIS = "P_Q6D_4_MEM_REM_DEBRIS";
	public static final String P_Q7D_1_REM_UNPACK = "P_Q7D_1_REM_UNPACK";
	public static final String P_Q7D_2_PACK_MATL = "P_Q7D_2_PACK_MATL";
	public static final String P_Q7D_3_ANYTHING_OFF_LOAD = "P_Q7D_3_ANYTHING_OFF_LOAD";
	public static final String P_Q7D_3A_VOLUME = "P_Q7D_3A_VOLUME";
	
	public static final String P_Q4U_1_CARTON_KIT_REQD = "P_Q4U_1_CARTON_KIT_REQD";
	public static final String P_Q4U_1A_REVD_IN_TIMELY_MANNER = "P_Q4U_1A_REVD_IN_TIMELY_MANNER";
	public static final String P_Q4U_2_MEM_REVD_GUIDE = "P_Q4U_2_MEM_REVD_GUIDE";
	public static final String P_Q4U_3_MEM_AWARE_OF_ITEMS = "P_Q4U_3_MEM_AWARE_OF_ITEMS";
	public static final String P_Q4U_4_MEM_AWARE_ICR = "P_Q4U_4_MEM_AWARE_ICR";
	public static final String P_Q4U_5_MEM_AWARE_OF_WARR = "P_Q4U_5_MEM_AWARE_OF_WARR";
	public static final String P_Q4U_6_MEM_AWARE_OF_CHANGES = "P_Q4U_6_MEM_AWARE_OF_CHANGES";
	public static final String P_Q4_7_CASE_MAN_AWARE_OF_CHANG = "P_Q4_7_CASE_MAN_AWARE_OF_CHANG";
	public static final String P_Q4_8_CASE_MANAGER_APPROVAL = "P_Q4_8_CASE_MANAGER_APPROVAL";
	public static final String P_Q4U_7_PROV_CHANGES_MEM_INV = "P_Q4U_7_PROV_CHANGES_MEM_INV";
	
	public static final String Member = "Member";
	public static final String Agent = "Agent";
	public static final String DHRM = "DHRM";
	public static final String TT = "TT";
	
	public static final String P_TRANSITIONS_START_TIMESTAMP = "P_TRANSITIONS_START_TIMESTAMP";
	public static final String P_GPS_SUBMIT_LOCATION = "P_GPS_SUBMIT_LOCATION";
	public static final String P_APP_VERSION = "P_APP_VERSION";
	public static final String P_FINALISED_DATE = "P_FINALISED_DATE";
	public static final String P_GEO_CODE_FINALISED = "P_GEO_CODE_FINALISED";
	public static final String P_UPLOAD_DATE = "P_UPLOAD_DATE";
	public static final String P_GEOCODE_FRM_UPLOAD = "P_GEOCODE_FRM_UPLOAD";
	public static final String P_UPLOAD_RETRY_COUNT = "P_UPLOAD_RETRY_COUNT";
}
