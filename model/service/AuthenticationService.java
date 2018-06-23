package com.contactpoint.model.service;

import com.contactpoint.controller.NetworkListener;
import com.contactpoint.model.client.authentication.RequestLogin;

public interface AuthenticationService {

	public final static int LOGIN = 0;
	public final static int ERROR = -3;
	public final static int FAILURE = -4;
    //public final static String PATH = "/demo/TMSConsultant/PrecompiledWeb/TMSConsultantWCF.HostV3/Authentication.svc";
    //public final static String PATH = "/demo/TMSConsultant/PrecompiledWeb/TMSConsultantWCF.Host/Authentication.svc";
    public final static String PATH = "/demo/TMSConsultant/PrecompiledWeb/TollAppMM/Authentication.svc";
    public final static String AUTHENTICATED = "Authenticated";
    public final static String DOMAIN = "@tollgroup.com";
    
	public void setNetworkListener(NetworkListener listener);
	public int executeSoapRequest();
	public void prepareLogin(RequestLogin param);
	
	public String getResponseMessage();
	public String getClientMessage();	
}
