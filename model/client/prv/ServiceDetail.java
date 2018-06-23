package com.contactpoint.model.client.prv;

import java.io.Serializable;
import java.util.Hashtable;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;

public class ServiceDetail implements KvmSerializable, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6914429913145746015L;
	public String LineNo;
	public String ComponentLineNo;
	public String ServiceName;
	public String ServiceDate;
	public String ServiceAddress;

	public ServiceDetail() {}
	
	public ServiceDetail(SoapObject soapObject) {
		if (soapObject.hasProperty("LineNo"))
		{
			Object obj = soapObject.getProperty("LineNo");
			if (obj != null && obj.getClass().equals(SoapPrimitive.class)){
				SoapPrimitive j1 =(SoapPrimitive) soapObject.getProperty("LineNo");
				LineNo = j1.toString();
			}
		}
		if (soapObject.hasProperty("ComponentLineNo"))
		{
			Object obj = soapObject.getProperty("ComponentLineNo");
			if (obj != null && obj.getClass().equals(SoapPrimitive.class)){
				SoapPrimitive j2 =(SoapPrimitive) soapObject.getProperty("ComponentLineNo");
				ComponentLineNo = j2.toString();
			}
		}
		if (soapObject.hasProperty("ServiceName"))
		{
			Object obj = soapObject.getProperty("ServiceName");
			if (obj != null && obj.getClass().equals(SoapPrimitive.class)){
				SoapPrimitive j3 =(SoapPrimitive) soapObject.getProperty("ServiceName");
				ServiceName = j3.toString();
			}
		}
		if (soapObject.hasProperty("ServiceDate"))
		{
			Object obj = soapObject.getProperty("ServiceDate");
			if (obj != null && obj.getClass().equals(SoapPrimitive.class)){
				SoapPrimitive j4 =(SoapPrimitive) soapObject.getProperty("ServiceDate");
				ServiceDate = j4.toString();
			}
		}
		if (soapObject.hasProperty("ServiceAddress"))
		{
			Object obj = soapObject.getProperty("ServiceAddress");
			if (obj != null && obj.getClass().equals(SoapPrimitive.class)){
				SoapPrimitive j5 =(SoapPrimitive) soapObject.getProperty("ServiceAddress");
				ServiceAddress = j5.toString();
			}
		}
	}
	@Override
	public Object getProperty(int arg0) {
		switch(arg0){
		case 0: return LineNo;
		case 1: return ComponentLineNo;
		case 2: return ServiceName;
		case 3: return ServiceDate;
		case 4: return ServiceAddress;
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
			info.name = "LineNo";
			break;
		case 1:
			info.type = PropertyInfo.STRING_CLASS;
			info.name = "ComponentLineNo";
			break;
		case 2:
			info.type = PropertyInfo.STRING_CLASS;
			info.name = "ServiceName";
			break;
		case 3:
			info.type = PropertyInfo.STRING_CLASS;
			info.name = "ServiceDate";
			break;
		case 4:
			info.type = PropertyInfo.STRING_CLASS;
			info.name = "ServiceAddress";
			break;
		}
	}

	@Override
	public void setProperty(int index, Object value) {
		switch(index){
		case 0: LineNo 			= value.toString();	break;
		case 1: ComponentLineNo = value.toString();	break;
		case 2: ServiceName 	= value.toString();	break;
		case 3: ServiceDate 	= value.toString();	break;
		case 4: ServiceAddress 	= value.toString();	break;
		}
	}

}
