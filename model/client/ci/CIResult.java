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

import java.util.Hashtable;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;

public class CIResult implements KvmSerializable {

	public String NAMESPACE ="http://TMSConsultant.Model/2013/ProcessCI";
	public String moveNumber;
	public boolean moveNumberSpecified;
	public Name name;
	public String moveDate;
	public String region;
	public String zone;
	public String volume;
	public Address address;
	public String businessGroup;
	public String cIType;
	public String cIStatus;
	public String serviceCentre;
	public String kPIRequirement;
	public String orderLineId;
	public String poNumber;
	public String sensitive;
	public boolean isMM;

	public CIResult(){}

	public CIResult(SoapObject soapObject){

		if (soapObject.hasProperty("MoveNumber"))
		{
			Object obj = soapObject.getProperty("MoveNumber");
			if (obj != null && obj.getClass().equals(SoapPrimitive.class)){
				SoapPrimitive j47 =(SoapPrimitive) soapObject.getProperty("MoveNumber");
				moveNumber = j47.toString();
			}
		}
		if (soapObject.hasProperty("MoveNumberSpecified"))
		{
			Object obj = soapObject.getProperty("MoveNumberSpecified");
			if (obj != null && obj.getClass().equals(SoapPrimitive.class)){
				SoapPrimitive j48 =(SoapPrimitive) soapObject.getProperty("MoveNumberSpecified");
				moveNumberSpecified = Boolean.parseBoolean(j48.toString());
			}
		}
		if (soapObject.hasProperty("Name"))
		{
			SoapObject j49 = (SoapObject)soapObject.getProperty("Name");
			name =  new Name (j49);

		}
		if (soapObject.hasProperty("MoveDate"))
		{
			Object obj = soapObject.getProperty("MoveDate");
			if (obj != null && obj.getClass().equals(SoapPrimitive.class)){
				SoapPrimitive j50 =(SoapPrimitive) soapObject.getProperty("MoveDate");
				moveDate = j50.toString();
			}
		}
		if (soapObject.hasProperty("Region"))
		{
			Object obj = soapObject.getProperty("Region");
			if (obj != null && obj.getClass().equals(SoapPrimitive.class)){
				SoapPrimitive j51 =(SoapPrimitive) soapObject.getProperty("Region");
				region = j51.toString();
			}
		}
		if (soapObject.hasProperty("Zone"))
		{
			Object obj = soapObject.getProperty("Zone");
			if (obj != null && obj.getClass().equals(SoapPrimitive.class)){
				SoapPrimitive j52 =(SoapPrimitive) soapObject.getProperty("Zone");
				zone = j52.toString();
			}
		}
		if (soapObject.hasProperty("Volume"))
		{
			Object obj = soapObject.getProperty("Volume");
			if (obj != null && obj.getClass().equals(SoapPrimitive.class)){
				SoapPrimitive j53 =(SoapPrimitive) soapObject.getProperty("Volume");
				volume = j53.toString();
			}
		}
		if (soapObject.hasProperty("Address"))
		{
			SoapObject j54 = (SoapObject)soapObject.getProperty("Address");
			address =  new Address (j54);

		}
		if (soapObject.hasProperty("BusinessGroup"))
		{
			Object obj = soapObject.getProperty("BusinessGroup");
			if (obj != null && obj.getClass().equals(SoapPrimitive.class)){
				SoapPrimitive j55 =(SoapPrimitive) soapObject.getProperty("BusinessGroup");
				businessGroup = j55.toString();
			}
		}
		if (soapObject.hasProperty("CIType"))
		{
			Object obj = soapObject.getProperty("CIType");
			if (obj != null && obj.getClass().equals(SoapPrimitive.class)){
				SoapPrimitive j56 =(SoapPrimitive) soapObject.getProperty("CIType");
				cIType = j56.toString();
			}
		}
		if (soapObject.hasProperty("CIStatus"))
		{
			Object obj = soapObject.getProperty("CIStatus");
			if (obj != null && obj.getClass().equals(SoapPrimitive.class)){
				SoapPrimitive j57 =(SoapPrimitive) soapObject.getProperty("CIStatus");
				cIStatus = j57.toString();
			}
		}
		if (soapObject.hasProperty("ServiceCentre"))
		{
			Object obj = soapObject.getProperty("ServiceCentre");
			if (obj != null && obj.getClass().equals(SoapPrimitive.class)){
				SoapPrimitive j58 =(SoapPrimitive) soapObject.getProperty("ServiceCentre");
				serviceCentre = j58.toString();
			}
		}
		if (soapObject.hasProperty("KPIRequirement"))
		{
			Object obj = soapObject.getProperty("KPIRequirement");
			if (obj != null && obj.getClass().equals(SoapPrimitive.class)){
				SoapPrimitive j59 =(SoapPrimitive) soapObject.getProperty("KPIRequirement");
				kPIRequirement = j59.toString();
			}
		}
		if (soapObject.hasProperty("OrderLineId"))
		{
			Object obj = soapObject.getProperty("OrderLineId");
			if (obj != null && obj.getClass().equals(SoapPrimitive.class)){
				SoapPrimitive j60 =(SoapPrimitive) soapObject.getProperty("OrderLineId");
				orderLineId = j60.toString();
			}
		}
		if (soapObject.hasProperty("PoNumber"))
		{
			Object obj = soapObject.getProperty("PoNumber");
			if (obj != null && obj.getClass().equals(SoapPrimitive.class)){
				SoapPrimitive j61 =(SoapPrimitive) soapObject.getProperty("PoNumber");
				poNumber = j61.toString();
			}
		}
		if (soapObject.hasProperty("Sensitive"))
		{
			Object obj = soapObject.getProperty("Sensitive");
			if (obj != null && obj.getClass().equals(SoapPrimitive.class)){
				SoapPrimitive j62 =(SoapPrimitive) soapObject.getProperty("Sensitive");
				sensitive = j62.toString();
			}
		}
	}
	@Override
	public Object getProperty(int arg0) {
		switch(arg0){
		case 0:
			return moveNumber;
		case 1:
			return moveNumberSpecified;
		case 2:
			return name;
		case 3:
			return moveDate;
		case 4:
			return region;
		case 5:
			return zone;
		case 6:
			return volume;
		case 7:
			return address;
		case 8:
			return businessGroup;
		case 9:
			return cIType;
		case 10:
			return cIStatus;
		case 11:
			return serviceCentre;
		case 12:
			return kPIRequirement;
		case 13:
			return orderLineId;
		case 14:
			return poNumber;
		case 15:
			return sensitive;
		}
		return null;
	}
	@Override
	public int getPropertyCount() {
		return 16;
	}
	@Override
	public void getPropertyInfo(int index, @SuppressWarnings("rawtypes") Hashtable arg1, PropertyInfo info) {
		switch(index){
		case 0:
			info.type = PropertyInfo.STRING_CLASS;
			info.name = "MoveNumber";
			break;
		case 1:
			info.type = PropertyInfo.BOOLEAN_CLASS;
			info.name = "MoveNumberSpecified";
			break;
		case 2:
			info.type = Name.class;
			info.name = "Name";
			break;
		case 3:
			info.type = PropertyInfo.STRING_CLASS;
			info.name = "MoveDate";
			break;
		case 4:
			info.type = PropertyInfo.STRING_CLASS;
			info.name = "Region";
			break;
		case 5:
			info.type = PropertyInfo.STRING_CLASS;
			info.name = "Zone";
			break;
		case 6:
			info.type = PropertyInfo.STRING_CLASS;
			info.name = "Volume";
			break;
		case 7:
			info.type = Address.class;
			info.name = "Address";
			break;
		case 8:
			info.type = PropertyInfo.STRING_CLASS;
			info.name = "BusinessGroup";
			break;
		case 9:
			info.type = PropertyInfo.STRING_CLASS;
			info.name = "CIType";
			break;
		case 10:
			info.type = PropertyInfo.STRING_CLASS;
			info.name = "CIStatus";
			break;
		case 11:
			info.type = PropertyInfo.STRING_CLASS;
			info.name = "ServiceCentre";
			break;
		case 12:
			info.type = PropertyInfo.STRING_CLASS;
			info.name = "KPIRequirement";
			break;
		case 13:
			info.type = PropertyInfo.STRING_CLASS;
			info.name = "OrderLineId";
			break;
		case 14:
			info.type = PropertyInfo.STRING_CLASS;
			info.name = "PoNumber";
			break;
		case 15:
			info.type = PropertyInfo.STRING_CLASS;
			info.name = "Sensitive";
			break;
		}
	}
	@Override
	public void setProperty(int index, Object value) {
		switch(index){
		case 0:
			moveNumber = value.toString() ;
			break;
		case 1:
			moveNumberSpecified = Boolean.parseBoolean(value.toString()) ;
			break;
		case 2:
			name = (Name)value;
			break;
		case 3:
			moveDate = value.toString() ;
			break;
		case 4:
			region = value.toString() ;
			break;
		case 5:
			zone = value.toString() ;
			break;
		case 6:
			volume = value.toString() ;
			break;
		case 7:
			address = (Address)value;
			break;
		case 8:
			businessGroup = value.toString() ;
			break;
		case 9:
			cIType = value.toString() ;
			break;
		case 10:
			cIStatus = value.toString() ;
			break;
		case 11:
			serviceCentre = value.toString() ;
			break;
		case 12:
			kPIRequirement = value.toString() ;
			break;
		case 13:
			orderLineId = value.toString() ;
			break;
		case 14:
			poNumber = value.toString() ;
			break;
		case 15:
			sensitive = value.toString() ;
			break;
		}
	}
}
