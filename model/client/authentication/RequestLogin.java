//------------------------------------------------------------------------------
// <wsdl2code-generated>
//    This code was generated by http://www.wsdl2code.com version Beta 1.2
//
//    Please dont change this code, regeneration will override your changes
//</wsdl2code-generated>
//
//------------------------------------------------------------------------------
//
//This source code was auto-generated by Wsdl2Code Beta Version
//
package com.contactpoint.model.client.authentication;

import java.util.Hashtable;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;

public class RequestLogin implements KvmSerializable {

	public String NAMESPACE = "http://TMSConsultant.Model/2013/TMSAuthentication";
	public Login loginCredentials;
	public AuthenticationToken token;

	public RequestLogin(){}

	public RequestLogin(SoapObject soapObject){

		if (soapObject.hasProperty("LoginCredentials"))
		{
			SoapObject j8 = (SoapObject)soapObject.getProperty("LoginCredentials");
			loginCredentials =  new Login (j8);

		}
		if (soapObject.hasProperty("Token"))
		{
			SoapObject j9 = (SoapObject)soapObject.getProperty("Token");
			token =  new AuthenticationToken (j9);

		}
	}
	@Override
	public Object getProperty(int arg0) {
		switch(arg0){
		case 0:
			return loginCredentials;
		case 1:
			return token;
		}
		return null;
	}
	@Override
	public int getPropertyCount() {
		return 2;
	}
	@Override
	public void getPropertyInfo(int index, @SuppressWarnings("rawtypes") Hashtable arg1, PropertyInfo info) {
		switch(index){
		case 0:
			info.type = Login.class;
			info.name = "LoginCredentials";
			break;
		case 1:
			info.type = AuthenticationToken.class;
			info.name = "Token";
			break;
		}
	}
	@Override
	public void setProperty(int index, Object value) {
		switch(index){
		case 0:
			loginCredentials = (Login)value;
			break;
		case 1:
			token = (AuthenticationToken)value;
			break;
		}
	}
}