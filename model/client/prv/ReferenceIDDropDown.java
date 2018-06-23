package com.contactpoint.model.client.prv;

import java.io.Serializable;
import java.util.Hashtable;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;

public class ReferenceIDDropDown implements KvmSerializable, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7487585722886862319L;
	public int ID;
	public String Description;
	public String Type;
	
	public static final String AFTER_END_DATE 	= "AfterEndDate";
	public static final String REBOOK 			= "Rebooking";
	public static final String CONTACT_FAILURE 	= "ContactFailure";
	public static final String RESIDENTIAL		= "ResidentialAccess";
	public static final String CHECKLIST		= "Checklist";

	public ReferenceIDDropDown () {}
	public ReferenceIDDropDown (SoapObject soapObject) {
		if (soapObject.hasProperty("ID"))
		{
			Object obj = soapObject.getProperty("ID");
			if (obj != null && obj.getClass().equals(SoapPrimitive.class)){
				SoapPrimitive j1 =(SoapPrimitive) soapObject.getProperty("ID");
				ID = Integer.parseInt(j1.toString());
			}
		}
		if (soapObject.hasProperty("Description"))
		{
			Object obj = soapObject.getProperty("Description");
			if (obj != null && obj.getClass().equals(SoapPrimitive.class)){
				SoapPrimitive j3 =(SoapPrimitive) soapObject.getProperty("Description");
				Description = j3.toString();
			}
		}
		if (soapObject.hasProperty("Type"))
		{
			Object obj = soapObject.getProperty("Type");
			if (obj != null && obj.getClass().equals(SoapPrimitive.class)){
				SoapPrimitive j2 =(SoapPrimitive) soapObject.getProperty("Type");
				Type = j2.toString();
			}
		}
	}
	@Override
	public Object getProperty(int index) {
		switch (index) {
		case 0: return ID;
		case 1: return Description;
		case 2: return Type;
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
			info.type = PropertyInfo.INTEGER_CLASS;
			info.name = "ID";
			break;
		case 1:
			info.type = PropertyInfo.STRING_CLASS;
			info.name = "Description";
			break;
		case 2:
			info.type = PropertyInfo.STRING_CLASS;
			info.name = "Type";
			break;
		}
	}

	@Override
	public void setProperty(int index, Object value) {
		switch(index)  {
		case 0: ID 			= Integer.parseInt(value.toString()); break;
		case 1: Description = value.toString(); break;
		case 2: Type 		= value.toString(); break;
		}
	}
	
	@Override
	public String toString() {
		return Description;
	}

}
