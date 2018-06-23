package com.contactpoint.model.DTO.ci.validator;

import com.contactpoint.model.DTO.ci.SearchCIInDTO;

public class SearchCIInDTOValidator {
	
	public final static int EXIT_SUCCESS = 0;
	public final static int USERNAME_EMPTY = 1;
	public final static int PASSWORD_EMPTY = 2;
	
	public static int validate(SearchCIInDTO param) {
		return EXIT_SUCCESS;
	}
}
