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
package com.contactpoint.model.client.ci;

import java.io.Serializable;
import java.util.Hashtable;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;

public class Contact implements KvmSerializable, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -185800625339130370L;
	public String NAMESPACE = "http://TMSConsultant.Model/2013/ProcessCI";
	public String phoneHome;
	public String phoneBusiness;
	public String phoneMobile;
	public String fax;
	public String email;

	public Contact(){}

	public Contact(SoapObject soapObject){

		if (soapObject.hasProperty("PhoneHome"))
		{
			Object obj = soapObject.getProperty("PhoneHome");
			if (obj != null && obj.getClass().equals(SoapPrimitive.class)){
				SoapPrimitive j17 =(SoapPrimitive) soapObject.getProperty("PhoneHome");
				phoneHome = j17.toString();
			}
		}
		if (soapObject.hasProperty("PhoneBusiness"))
		{
			Object obj = soapObject.getProperty("PhoneBusiness");
			if (obj != null && obj.getClass().equals(SoapPrimitive.class)){
				SoapPrimitive j18 =(SoapPrimitive) soapObject.getProperty("PhoneBusiness");
				phoneBusiness = j18.toString();
			}
		}
		if (soapObject.hasProperty("PhoneMobile"))
		{
			Object obj = soapObject.getProperty("PhoneMobile");
			if (obj != null && obj.getClass().equals(SoapPrimitive.class)){
				SoapPrimitive j19 =(SoapPrimitive) soapObject.getProperty("PhoneMobile");
				phoneMobile = j19.toString();
			}
		}
		if (soapObject.hasProperty("Fax"))
		{
			Object obj = soapObject.getProperty("Fax");
			if (obj != null && obj.getClass().equals(SoapPrimitive.class)){
				SoapPrimitive j20 =(SoapPrimitive) soapObject.getProperty("Fax");
				fax = j20.toString();
			}
		}
		if (soapObject.hasProperty("Email"))
		{
			Object obj = soapObject.getProperty("Email");
			if (obj != null && obj.getClass().equals(SoapPrimitive.class)){
				SoapPrimitive j21 =(SoapPrimitive) soapObject.getProperty("Email");
				email = j21.toString();
			}
		}
	}
	@Override
	public Object getProperty(int arg0) {
		switch(arg0){
		case 0:
			return phoneHome;
		case 1:
			return phoneBusiness;
		case 2:
			return phoneMobile;
		case 3:
			return fax;
		case 4:
			return email;
		}
		return null;
	}
	@Override
	public int getPropertyCount() {
		return 5;
	}
	@Override
	public void getPropertyInfo(int index, @SuppressWarnings("rawtypes") Hashtable arg1, PropertyInfo info) {
		switch(index){
		case 0:
			info.type = PropertyInfo.STRING_CLASS;
			info.name = "PhoneHome";
			break;
		case 1:
			info.type = PropertyInfo.STRING_CLASS;
			info.name = "PhoneBusiness";
			break;
		case 2:
			info.type = PropertyInfo.STRING_CLASS;
			info.name = "PhoneMobile";
			break;
		case 3:
			info.type = PropertyInfo.STRING_CLASS;
			info.name = "Fax";
			break;
		case 4:
			info.type = PropertyInfo.STRING_CLASS;
			info.name = "Email";
			break;
		}
	}
	@Override
	public void setProperty(int index, Object value) {
		switch(index){
		case 0:
			phoneHome = value.toString() ;
			break;
		case 1:
			phoneBusiness = value.toString() ;
			break;
		case 2:
			phoneMobile = value.toString() ;
			break;
		case 3:
			fax = value.toString() ;
			break;
		case 4:
			email = value.toString() ;
			break;
		}
	}
}
