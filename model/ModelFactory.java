package com.contactpoint.model;

import com.contactpoint.model.service.AuthenticationService;
import com.contactpoint.model.service.AzureAuthenticationService;
import com.contactpoint.model.service.GetCIService;
import com.contactpoint.model.service.PRVService;
import com.contactpoint.model.service.PhotoService;
import com.contactpoint.model.service.UtilService;
import com.contactpoint.model.service.impl.AuthenticationServiceImpl;
import com.contactpoint.model.service.impl.AzureAuthenticationServiceImpl;
import com.contactpoint.model.service.impl.GetCIServiceImpl;
import com.contactpoint.model.service.impl.PRVServiceImpl;
import com.contactpoint.model.service.impl.PhotoServiceImpl;
import com.contactpoint.model.service.impl.UtilServiceImpl;

public class ModelFactory {
	private static AuthenticationServiceImpl authImpl = new AuthenticationServiceImpl();
	private static GetCIServiceImpl ciImpl = new GetCIServiceImpl();
	private static PRVServiceImpl prvImpl = new PRVServiceImpl();
	private static UtilServiceImpl utilImpl = new UtilServiceImpl();
	private static PhotoServiceImpl photoImpl = new PhotoServiceImpl();
	private static AzureAuthenticationService azureImpl = new AzureAuthenticationServiceImpl();
	
	public static AuthenticationService getAuthenticationService() 	{ return authImpl; }
	public static GetCIService getCIService() 						{ return ciImpl; }
	public static PRVService getPRVService() 						{ return prvImpl; }
	public static UtilService getUtilService()						{ return utilImpl; }
	public static PhotoService getPhotoService()					{ return photoImpl; }
	public static AzureAuthenticationService getAzureService()		{ return azureImpl; }
}
