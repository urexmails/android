package com.contactpoint.model.DTO.prv.validator;

import java.util.HashMap;

import android.content.Context;

import com.contactpoint.model.ModelFactory;
import com.contactpoint.model.DTO.ci.validator.Validator;
import com.contactpoint.model.DTO.prv.UploadPRVInDTO;
import com.contactpoint.model.client.prv.ReferenceIDDropDown;

public class UploadPRVInDTOValidator extends Validator {
	
	public static boolean validateCheckList(Context context, HashMap<String, String> param) {
		for (int i = 0; i < ModelFactory.getPRVService().getPRVQuestions(ReferenceIDDropDown.CHECKLIST)
				.size(); i++) {
			if (!param.containsKey(UploadPRVInDTO.P_PRV_ + (i + 1))) return false;			
		}
		return true;
	}
	
	public static boolean hasTimeStamp(HashMap<String, String> param) {
		return param.containsKey(UploadPRVInDTO.P_TRANSITIONS_START_TIMESTAMP);
	}
	
}
