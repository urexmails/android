package com.contactpoint.model.client.prv;

import java.io.Serializable;
import java.util.Hashtable;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;

public class RoomItem implements KvmSerializable, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2245757216603053235L;
	public static final String NAMESPACE = "http://TMSConsultant.Model/2013/TMSConsultant";
	public final static String MM_NAMESPACE = "http://schemas.datacontract.org/2004/07/TollTransitions.API.DataContracts";

	public int ItemID;
	public int RoomID;
	public int Quantity;
	public boolean isMM;
	
	public RoomItem() {}
	public RoomItem(SoapObject soapObject) {
		if (soapObject.hasProperty("ItemID"))
		{
			Object obj = soapObject.getProperty("ItemID");
			if (obj != null && obj.getClass().equals(SoapPrimitive.class)){
				SoapPrimitive j1 =(SoapPrimitive) soapObject.getProperty("ItemID");
				ItemID = Integer.parseInt(j1.toString());
			}
		}
		if (soapObject.hasProperty("RoomID"))
		{
			Object obj = soapObject.getProperty("RoomID");
			if (obj != null && obj.getClass().equals(SoapPrimitive.class)){
				SoapPrimitive j2 =(SoapPrimitive) soapObject.getProperty("RoomID");
				RoomID = Integer.parseInt(j2.toString());
			}
		}
		if (soapObject.hasProperty("Quantity"))
		{
			Object obj = soapObject.getProperty("Quantity");
			if (obj != null && obj.getClass().equals(SoapPrimitive.class)){
				SoapPrimitive j3 =(SoapPrimitive) soapObject.getProperty("Quantity");
				Quantity = Integer.parseInt(j3.toString());
			}
		}
	}
	@Override
	public Object getProperty(int index) {
		switch (index) {
		case 0: return ItemID;
		case 1: return RoomID;
		case 2: return Quantity;
		}
		return null;
	}

	@Override
	public int getPropertyCount() {
		return 3;
	}

	@Override
	public void getPropertyInfo(int index, @SuppressWarnings("rawtypes") Hashtable arg1, PropertyInfo info) {
		info.namespace = isMM ? MM_NAMESPACE : NAMESPACE;
		switch(index){
		case 0:
			info.type = PropertyInfo.INTEGER_CLASS;
			info.name = "ItemID";
			break;
		case 1:
			info.type = PropertyInfo.INTEGER_CLASS;
			info.name = "RoomID";
			break;
		case 2:
			info.type = PropertyInfo.INTEGER_CLASS;
			info.name = "Quantity";
			break;			
		}
	}

	@Override
	public void setProperty(int index, Object value) {
		switch(index)  {
		case 0: ItemID = Integer.parseInt(value.toString()); break;
		case 1: RoomID = Integer.parseInt(value.toString()); break;
		case 2: Quantity = Integer.parseInt(value.toString()); break;
		}
	}

}
