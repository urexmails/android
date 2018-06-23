package com.contactpoint.model.DTO.ci;

import com.contactpoint.model.client.authentication.AuthenticationToken;

public class DeleteInDTO {

	public String getPONumber() 				{ return PONumber; }	
	public void setPONumber(String pONumber) 	{ PONumber = pONumber; }
	
	public AuthenticationToken getAuthenticationToken() { return Token; }
	public void setAuthenticationToken(AuthenticationToken param) { Token = param; }
	
	private String PONumber;
	private AuthenticationToken Token;
	public String Tracker;
	
	public final String NAMESPACE = "http://TMSConsultant.Model/2013/ProcessCI";
	public final String ACTION 	 = "http://TMSConsultant.Model/2013/ProcessCI/DeleteCI";

	public final String MM_NAMESPACE = "http://tempuri.org/";
	public final String MM_CONTRACT_NAMESPACE = "http://schemas.datacontract.org/2004/07/TollTransitions.API.DataContracts";
	public final String MM_ACTION 	 = "http://tempuri.org/ICIService/DeleteCI";
	
	public final static String DELETE_SUCCESSFUL = "Delete Successful";
}
