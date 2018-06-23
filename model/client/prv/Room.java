package com.contactpoint.model.client.prv;

import java.io.Serializable;
import java.util.Hashtable;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;

public class Room implements KvmSerializable, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6631594501188505519L;
	public static final String NAMESPACE = "http://TMSConsultant.Model/2013/TMSConsultant";
	public final static String MM_NAMESPACE = "http://schemas.datacontract.org/2004/07/TollTransitions.API.DataContracts";

	public int ID;
	public String Name;
	public boolean isMM;
	
	public Room() {}
	public Room(SoapObject soapObject) {
		if (soapObject.hasProperty("ID"))
		{
			Object obj = soapObject.getProperty("ID");
			if (obj != null && obj.getClass().equals(SoapPrimitive.class)){
				SoapPrimitive j1 =(SoapPrimitive) soapObject.getProperty("ID");
				ID = Integer.parseInt(j1.toString());
			}
		}
		if (soapObject.hasProperty("Name"))
		{
			Object obj = soapObject.getProperty("Name");
			if (obj != null && obj.getClass().equals(SoapPrimitive.class)){
				SoapPrimitive j2 =(SoapPrimitive) soapObject.getProperty("Name");
				Name = j2.toString();
			}
		}
	}
	@Override
	public Object getProperty(int index) {
		switch (index) {
		case 0: return ID;
		case 1: return Name;
		}
		return null;
	}

	@Override
	public int getPropertyCount() {
		return 2;
	}

	@Override
	public void getPropertyInfo(int index, @SuppressWarnings("rawtypes") Hashtable arg1, PropertyInfo info) {
		info.namespace = isMM ? MM_NAMESPACE : NAMESPACE;
		switch(index){
		case 0:
			info.type = PropertyInfo.INTEGER_CLASS;
			info.name = "ID";
			break;
		case 1:
			info.type = PropertyInfo.STRING_CLASS;
			info.name = "Name";
			break;
		}
	}

	@Override
	public void setProperty(int index, Object value) {
		switch(index)  {
		case 0: ID = Integer.parseInt(value.toString()); break;
		case 1: Name = value.toString(); break;
		}
	}

}
