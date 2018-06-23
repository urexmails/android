package com.contactpoint.model.client.prv;

import java.io.Serializable;
import java.util.Hashtable;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;

public class CartonKit implements KvmSerializable, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7487585722886862319L;
	public String Quantity;
	public String Type;
	public String DeliveryDate;

	public CartonKit () {}
	public CartonKit (SoapObject soapObject) {
		if (soapObject.hasProperty("Quantity"))
		{
			Object obj = soapObject.getProperty("Quantity");
			if (obj != null && obj.getClass().equals(SoapPrimitive.class)){
				SoapPrimitive j1 =(SoapPrimitive) soapObject.getProperty("Quantity");
				Quantity = j1.toString();
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
		if (soapObject.hasProperty("DeliveryDate"))
		{
			Object obj = soapObject.getProperty("DeliveryDate");
			if (obj != null && obj.getClass().equals(SoapPrimitive.class)){
				SoapPrimitive j3 =(SoapPrimitive) soapObject.getProperty("DeliveryDate");
				DeliveryDate = j3.toString();
			}
		}
	}
	@Override
	public Object getProperty(int index) {
		switch (index) {
		case 0: return Quantity;
		case 1: return Type;
		case 2: return DeliveryDate;
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
			info.name = "Quantity";
			break;
		case 1:
			info.type = PropertyInfo.STRING_CLASS;
			info.name = "Type";
			break;
		case 2:
			info.type = PropertyInfo.STRING_CLASS;
			info.name = "DeliveryDate";
			break;
		}
	}

	@Override
	public void setProperty(int index, Object value) {
		switch(index)  {
		case 0: Quantity 		= value.toString(); break;
		case 1: Type 			= value.toString(); break;
		case 2: DeliveryDate 	= value.toString(); break;
		}
	}

}
