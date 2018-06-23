package com.contactpoint.model.client.prv;

import java.io.Serializable;
import java.util.Hashtable;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;

public class Client implements KvmSerializable, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7378529050376886518L;
	public static final String NAMESPACE = "http://TMSConsultant.Model/2013/TMSConsultant";
	public String ClientCode;
	public String Name;
	public int ClientID;
	
	public Client() {}
	public Client(SoapObject soapObject){

		if (soapObject.hasProperty("ClientCode"))
		{
			Object obj = soapObject.getProperty("ClientCode");
			if (obj.getClass().equals(SoapPrimitive.class)){
				SoapPrimitive j41 =(SoapPrimitive) soapObject.getProperty("ClientCode");
				ClientCode = j41.toString();
			}
		}
		if (soapObject.hasProperty("Name"))
		{
			Object obj = soapObject.getProperty("Name");
			if (obj.getClass().equals(SoapPrimitive.class)){
				SoapPrimitive j42 =(SoapPrimitive) soapObject.getProperty("Name");
				Name = j42.toString();
			}
		}
		if (soapObject.hasProperty("ClientID"))
		{
			Object obj = soapObject.getProperty("ClientID");
			if (obj.getClass().equals(SoapPrimitive.class)){
				SoapPrimitive j43 =(SoapPrimitive) soapObject.getProperty("ClientID");
				ClientID = Integer.parseInt(j43.toString());
			}
		}
	}
	@Override
	public Object getProperty(int index) {
		switch(index){
		case 0:
			return ClientCode;
		case 1:
			return Name;
		case 2:
			return ClientID;
		}
		return null;
	}

	@Override
	public int getPropertyCount() {
		return 3;
	}

	@Override
	public void getPropertyInfo(int index, @SuppressWarnings("rawtypes") Hashtable arg1, PropertyInfo info) {
		switch(index){
		case 0:
			info.type = PropertyInfo.STRING_CLASS;
			info.name = "ClientCode";
			break;
		case 1:
			info.type = PropertyInfo.STRING_CLASS;
			info.name = "Name";
			break;
		case 2:
			info.type = PropertyInfo.STRING_CLASS;
			info.name = "ClientID";
			break;
		}
	}

	@Override
	public void setProperty(int index, Object value) {
		switch(index){
		case 0:
			ClientCode = value.toString() ;
			break;
		case 1:
			Name = value.toString() ;
			break;
		case 2:
			ClientID = Integer.parseInt(value.toString()) ;
			break;
		}
	}
	
	@Override
	public String toString() {
		return ClientCode;
	}

}
