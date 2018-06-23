package com.contactpoint.model.service;

import com.contactpoint.controller.NetworkListener;
import com.contactpoint.model.DTO.azure.AzureLoginDTO;
import com.contactpoint.model.DTO.azure.AzureTokenDTO;

public interface AzureAuthenticationService {

	public final static int AZURE_LOGIN = 100;
	public final static int AZURE_REFRESH = 200;
	public final static int ERROR = -300;
	public final static int FAILURE = -400;
    //public final static String PATH = "/demo/TMSConsultant/PrecompiledWeb/TMSConsultantWCF.HostV3/Authentication.svc";
    //public final static String PATH = "/demo/TMSConsultant/PrecompiledWeb/TMSConsultantWCF.Host/Authentication.svc";
    public final static String AUTHENTICATED = "Authenticated";
    
	public void setNetworkListener(NetworkListener listener);
	public int executeSoapRequest();
	public void prepareLogin(AzureLoginDTO param);
	
	public AzureTokenDTO getAzureToken();
	public void setAzureToken(AzureTokenDTO param);
	public String getURL();
	public void setIsUAT(boolean uat);
	
	public String getAuthorizationHeader();
	
	public String getResponseMessage();
	public String getClientMessage();
}
