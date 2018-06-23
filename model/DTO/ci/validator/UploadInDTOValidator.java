package com.contactpoint.model.DTO.ci.validator;

import java.util.HashMap;

import com.contactpoint.model.DTO.ci.UploadInDTO;

public class UploadInDTOValidator extends Validator {

	//public final static int DEFAULT_SIG_LENGTH = 1136;
	public static boolean validateServiceDetail(HashMap<String, String> param) {
		
		if (param.get(UploadInDTO.P_Q1_TIME_ATTENDING) == null) {
			return false;
		}
		
		if (param.get(UploadInDTO.P_Q1_SERVICE_ORDERED) == null) {
			return false;
		}
		
		if (param.get(UploadInDTO.P_Q2_PROVIDER_CONFIRM) == null) {
			return false;
		}
				
		if (param.get(UploadInDTO.P_Q2_PROVIDER_CONFIRM).compareTo(UploadInDTO.YES) == 0 &&
				param.get(UploadInDTO.P_Q2C_TIME_MET) == null) {
			return false;
		}
				
		return true;
	}
	
	public static boolean validateProviderService1(HashMap<String, String> param) {
		if (param.get(UploadInDTO.P_Q3C_NO_OF_CREW) == null) {
			return false;
		}
		
		if (param.get(UploadInDTO.P_Q3C_PROVIDER_LOGO_DISPLAYED) == null) {
			return false;
		}
		
		if (param.get(UploadInDTO.P_Q3C_1_PRESENTATION_CREW) == null) {
			return false;
		}
		
		if (param.get(UploadInDTO.P_Q3C_2_CUST_SERVICE_SKILL) == null) {
			return false;
		}
		
		if (param.get(UploadInDTO.P_Q3C_3_COND_OF_VEHICLE) == null) {
			return false;
		}
		
		if (param.get(UploadInDTO.P_Q3C_4_COND_OF_PACKAGING) == null) {
			return false;
		}
		
		if (param.get(UploadInDTO.P_Q3C_5_STANDING_CONTAINER) == null) {
			return false;
		}
		/*
		if (param.get(UploadInDTO.P_Q3C_6_REMLST_ON_TIME) == null) {
			return false;
		}
		
		if (param.get(UploadInDTO.P_Q3C_7_REMLST_PROFESSIONAL) == null) {
			return false;
		}
		
		if (param.get(UploadInDTO.P_Q3C_8_REM_CONDUCTED_TIMELY) == null) {
			return false;
		}*/
		
		return true;
	}
	
	public static boolean validateProviderService2(HashMap<String, String> param) {
		/*
		if (param.get(UploadInDTO.P_Q3C_9_SPEC_PACKAGING) == null) {
			return false;
		}
		
		if (param.get(UploadInDTO.P_Q3C_9_SPEC_PACKAGING).compareTo(UploadInDTO.YES) == 0 &&
			param.get(UploadInDTO.P_Q3C_9A_SPEC_MATLS_PROVIDED)== null) {
			return false;
		}
		
		if (param.get(UploadInDTO.P_Q3C_9_SPEC_PACKAGING).compareTo(UploadInDTO.YES) == 0 &&
			param.get(UploadInDTO.P_Q3C_9B_PLASMA_REAR_PROJ) == null &&
			param.get(UploadInDTO.P_Q3C_9B_BIKE) == null &&
			param.get(UploadInDTO.P_Q3C_9B_PAINTING) == null &&
			param.get(UploadInDTO.P_Q3C_9B_OTHER) == null) {
			return false;
		}
		
		if (param.get(UploadInDTO.P_Q3C_10_REMOVAL_AS_REQD) == null) {
			return false;
		}
		
		if (param.get(UploadInDTO.P_Q3C_11_ICR) == null) {
			return false;
		}
		
		if (param.get(UploadInDTO.P_Q3C_11_ICR).compareTo(UploadInDTO.YES) == 0 &&
			param.get(UploadInDTO.P_Q3C_11A_ICR_CORRECT) == null) {
			return false;
		}
		
		if (param.get(UploadInDTO.P_Q3C_12_REMLST_VEH_POS) == null) {
			return false;
		}
		
		if (param.get(UploadInDTO.P_Q3C_12_REMLST_VEH_POS).compareTo(UploadInDTO.NO) == 0 &&
				param.get(UploadInDTO.P_Q3C_12A_TRUCK_IN_DRIVEWAY) == null &&
				param.get(UploadInDTO.P_Q3C_12A_TRUCK_DBL_PARKED) == null &&
				param.get(UploadInDTO.P_Q3C_12A_TRUCK_ON_VERGE) == null) {
			return false;
		}*/
				
		return true;
	}
	
	public static boolean validateProviderService3(HashMap<String, String> param) {
		/*
		if (param.get(UploadInDTO.P_Q3C_13_REMLST_FE_RESP) == null) {
			return false;
		}
		
		if (param.get(UploadInDTO.P_Q3C_14_REMLST_TREATING_RESID) == null) {
			return false;
		}
		
		if (param.get(UploadInDTO.P_Q3C_15_PROVIDER_OHS_COMP) == null) {
			return false;
		}
		
		if (param.get(UploadInDTO.P_Q3C_15_PROVIDER_OHS_COMP).compareTo(UploadInDTO.NO) == 0 &&
				param.get(UploadInDTO.P_Q3C_15A_NO_SAFETY_GEAR) == null &&
				param.get(UploadInDTO.P_Q3C_15A_TRUCK_LOAD) == null &&
				param.get(UploadInDTO.P_Q3C_15A_UNSAFE_LIFT_CARRY) == null &&
				param.get(UploadInDTO.P_Q3C_15A_PACKING) == null &&
				param.get(UploadInDTO.P_Q3C_15A_TRUCK_LOCATION) == null) {
			return false;
		}*/
				
		return true;
	}
	
	public static boolean validateMemberRating(HashMap<String, String> param) {
		if (param.get(UploadInDTO.P_Q4U_8_RATING) == null &&
				param.get(UploadInDTO.P_Q6D_4_RATING) == null) {
			return false;
		}
		/*
		if (param.get(UploadInDTO.P_Q4U_7_REMLST_COMMS) == null &&
				param.get(UploadInDTO.P_Q4U_7_OVERALL_IMPRESSION) == null &&
				param.get(UploadInDTO.P_Q4U_7_NO_OF_CREW_INSUFF) == null &&
				param.get(UploadInDTO.P_Q4U_7_TREAT_SPEC_ITEMS) == null &&
				param.get(UploadInDTO.P_Q4U_7_ICR_COMPLETED) == null) {
			return false;
		}*/
		return true;
	}
	
	public static boolean validateTollRating(HashMap<String, String> param) {
		if (param.get(UploadInDTO.P_Q8_1_RATING) == null) {
			return false;
		}
		
		if (!isNotEmpty(param.get(UploadInDTO.P_Q8_2_TIME_ON_SITE))) {
			return false;
		}
		return true;
	}
	
	public static boolean validateVolume(HashMap<String, String> param) {
		if (!isNotEmpty(param.get(UploadInDTO.P_Q5U_VOLUME)) && 
				!isNotEmpty(param.get(UploadInDTO.P_Q7D_VOLUME))) {
			return false;
		}
		return true;
	}
	
	public static boolean validateDelivery(HashMap<String, String> param) {
		if (param.get(UploadInDTO.P_Q6D_1_MEM_ADVISED_PAID) == null) {
			return false;
		}
		
		if (param.get(UploadInDTO.P_Q6D_2_MEM_TOLL_WARR) == null) {
			return false;
		}
		
		if (param.get(UploadInDTO.P_Q6D_3_DELIVERY_COMPLETED) == null) {
			return false;
		}
		
		if (param.get(UploadInDTO.P_Q6D_4_MEM_REM_DEBRIS) == null) {
			return false;
		}
		
		if (param.get(UploadInDTO.P_Q7D_1_REM_UNPACK) == null) {
			return false;
		}
		
		if (param.get(UploadInDTO.P_Q7D_2_PACK_MATL) == null) {
			return false;
		}
		
		if (param.get(UploadInDTO.P_Q7D_3_ANYTHING_OFF_LOAD) == null) {
			return false;
		}
		
		if (param.get(UploadInDTO.P_Q7D_3_ANYTHING_OFF_LOAD)
				.compareTo(UploadInDTO.YES) == 0) {
			if (!isNotEmpty(param.get(UploadInDTO.P_Q7D_3A_VOLUME))) {
				return false;
			}
			if (param.get(UploadInDTO.P_Q7D_3A_VOLUME) == null) {
				return false;
			}
		}
		
		return true;
	}
	
	public static boolean validateUplift(HashMap<String, String> param) {	
		if (param.get(UploadInDTO.P_Q4U_1_CARTON_KIT_REQD) == null) {
			return false;
		}
		
		if (param.get(UploadInDTO.P_Q4U_1_CARTON_KIT_REQD).compareTo(UploadInDTO.YES) == 0 &&
				param.get(UploadInDTO.P_Q4U_1A_REVD_IN_TIMELY_MANNER) == null) {
			return false;
		}		
		
		if (param.get(UploadInDTO.P_Q4U_2_MEM_REVD_GUIDE) == null) {
			return false;
		}
		
		if (param.get(UploadInDTO.P_Q4U_3_MEM_AWARE_OF_ITEMS) == null) {
			return false;
		}
		
		if (param.get(UploadInDTO.P_Q4U_4_MEM_AWARE_ICR) == null) {
			return false;
		}
		
		if (param.get(UploadInDTO.P_Q4U_5_MEM_AWARE_OF_WARR) == null) {
			return false;
		}
		
		if (param.get(UploadInDTO.P_Q4U_6_MEM_AWARE_OF_CHANGES) == null) {
			return false;
		}
		
		if (param.get(UploadInDTO.P_Q4U_6_MEM_AWARE_OF_CHANGES)
				.compareTo(UploadInDTO.YES) == 0) {
			if (param.get(UploadInDTO.P_Q4_7_CASE_MAN_AWARE_OF_CHANG) == null) {
				return false;
			}
			
			if (param.get(UploadInDTO.P_Q4_8_CASE_MANAGER_APPROVAL) == null) {
				return false;
			}

			if (param.get(UploadInDTO.P_Q4U_7_PROV_CHANGES_MEM_INV) == null) {
				return false;
			}
		}
		
		return true;
	}
	
	public static boolean validateSignature(HashMap<String, String> param) {
		if (param.get(UploadInDTO.TT) == null || 
				/*param.get(UploadInDTO.TT).length() == DEFAULT_SIG_LENGTH*/
				(param.get(UploadInDTO.Member) == null &&
				param.get(UploadInDTO.Agent) == null)) {
			return false;
		}
		return true;
	}
}
