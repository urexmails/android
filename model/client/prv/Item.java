package com.contactpoint.model.client.prv;

import java.io.Serializable;
import java.util.Hashtable;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;

public class Item implements KvmSerializable, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3497714018521997929L;
	public static final String NAMESPACE = "http://TMSConsultant.Model/2013/TMSConsultant";
	public final static String MM_NAMESPACE = "http://schemas.datacontract.org/2004/07/TollTransitions.API.DataContracts";
	
	public static final int DEFAULT_MAX = -1;
	public int ID;
	public String Name;
	public String CubicMeterage;
	public String ItemType;
	public String Length;
	public String Width;
	public String Depth;
	public String IsCustom;
	public int MaxNumber = DEFAULT_MAX;
	public boolean isMM;
	
	public Item() {}
	public Item(SoapObject soapObject) {
		if (soapObject.hasProperty("ID"))
		{
			Object obj = soapObject.getProperty("ID");
			if (obj != null && obj.getClass().equals(SoapPrimitive.class)){
				SoapPrimitive j26 =(SoapPrimitive) soapObject.getProperty("ID");
				ID = Integer.parseInt(j26.toString());
			}
		}
		if (soapObject.hasProperty("Name"))
		{
			Object obj = soapObject.getProperty("Name");
			if (obj != null && obj.getClass().equals(SoapPrimitive.class)){
				SoapPrimitive j27 =(SoapPrimitive) soapObject.getProperty("Name");
				Name = j27.toString();
			}
		}
		if (soapObject.hasProperty("CubicMeterage"))
		{
			Object obj = soapObject.getProperty("CubicMeterage");
			if (obj != null && obj.getClass().equals(SoapPrimitive.class)){
				SoapPrimitive j28 =(SoapPrimitive) soapObject.getProperty("CubicMeterage");
				CubicMeterage = j28.toString();
			}
		}
		if (soapObject.hasProperty("ItemType"))
		{
			Object obj = soapObject.getProperty("ItemType");
			if (obj != null && obj.getClass().equals(SoapPrimitive.class)){
				SoapPrimitive j29 =(SoapPrimitive) soapObject.getProperty("ItemType");
				ItemType = j29.toString();
			}
		}
		if (soapObject.hasProperty("Length"))
		{
			Object obj = soapObject.getProperty("Length");
			if (obj != null && obj.getClass().equals(SoapPrimitive.class)){
				SoapPrimitive j30 =(SoapPrimitive) soapObject.getProperty("Length");
				Length = j30.toString();
			}
		}
		if (soapObject.hasProperty("Width"))
		{
			Object obj = soapObject.getProperty("Width");
			if (obj != null && obj.getClass().equals(SoapPrimitive.class)){
				SoapPrimitive j32 =(SoapPrimitive) soapObject.getProperty("Width");
				Width = j32.toString();
			}
		}
		if (soapObject.hasProperty("Depth"))
		{
			Object obj = soapObject.getProperty("Depth");
			if (obj != null && obj.getClass().equals(SoapPrimitive.class)){
				SoapPrimitive j33 =(SoapPrimitive) soapObject.getProperty("Depth");
				Depth = j33.toString();
			}
		}
		if (soapObject.hasProperty("IsCustom"))
		{
			Object obj = soapObject.getProperty("IsCustom");
			if (obj != null && obj.getClass().equals(SoapPrimitive.class)){
				SoapPrimitive j34 =(SoapPrimitive) soapObject.getProperty("IsCustom");
				IsCustom = j34.toString();
			}
		}
		if (soapObject.hasProperty("MaxNumber"))
		{
			Object obj = soapObject.getProperty("MaxNumber");
			if (obj != null && obj.getClass().equals(SoapPrimitive.class)){
				SoapPrimitive j34 =(SoapPrimitive) soapObject.getProperty("MaxNumber");
				MaxNumber = Integer.parseInt(j34.toString());
			}
		}
		
	}
	@Override
	public Object getProperty(int index) {
		switch(index)  {
        case 0: return ID;
        case 1: return Name;
        case 2: return CubicMeterage;
        case 3: return ItemType;
        case 4: return Length;
        case 5: return Width;
        case 6: return Depth;
        case 7: return IsCustom;
        case 8: return MaxNumber;
		}
		return null;
	}

	@Override
	public int getPropertyCount() {
		return 9;
	}

	@Override
	public void getPropertyInfo(int index, @SuppressWarnings("rawtypes") Hashtable arg1, PropertyInfo info) {
		info.namespace = isMM ? MM_NAMESPACE : NAMESPACE;
		switch(index)  {
        case 0: 
        	info.name = "ID";
        	info.type = PropertyInfo.INTEGER_CLASS;
        	break;
        case 1: 
        	info.name = "Name";
        	info.type = PropertyInfo.STRING_CLASS;
        	break;
        case 2: 
        	info.name = "CubicMeterage";
        	info.type = PropertyInfo.STRING_CLASS;
        	break;
        case 3: 
        	info.name = "ItemType";
        	info.type = PropertyInfo.STRING_CLASS;
        	break;
        case 4: 
        	info.name = "Length";
        	info.type = PropertyInfo.STRING_CLASS;
        	break;
        case 5: 
        	info.name = "Width";
        	info.type = PropertyInfo.STRING_CLASS;
        	break;
        case 6: 
        	info.name = "Depth";
        	info.type = PropertyInfo.STRING_CLASS;
        	break;
        case 7: 
        	info.name = "IsCustom";
        	info.type = PropertyInfo.STRING_CLASS;
        	break;
        case 8: 
        	info.name = "MaxNumber";
        	info.type = PropertyInfo.INTEGER_CLASS;
        	break;
		}
	}

	@Override
	public void setProperty(int index, Object value) {
		switch(index)  {
        case 0: ID 				= Integer.parseInt(value.toString()); break;
        case 1: Name 			= value.toString();	break;
        case 2: CubicMeterage 	= value.toString();	break;
        case 3: ItemType 		= value.toString();	break;
        case 4: Length 			= value.toString();	break;
        case 5: Width 			= value.toString();	break;
        case 6: Depth 			= value.toString();	break;
        case 7: IsCustom 		= value.toString();	break;
        case 8: MaxNumber 		= Integer.parseInt(value.toString()); break;
		}
	}
	
	@Override
	public String toString() {
		return Name;
	}

}
