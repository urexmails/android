package com.contactpoint.model.DTO.ci.validator;

public abstract class Validator {

	protected boolean hasValidLength(String target, int minLength, int maxLength) {
		return target.length() >= minLength && target.length() <= maxLength; 
	}
	
	protected static boolean isNotEmpty(String target) {
		return target != null && target.length() != 0;
	}
}
