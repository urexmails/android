package com.contactpoint.model.client.prv;

import java.io.Serializable;
import java.util.Hashtable;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;

public class PRVDownload implements KvmSerializable, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2439512637227299493L;
	public String moveNumber;
    public Name memberName;
    public Address memberAddress;
    public Contact memberContact;
    public String moveDate;
    public String region;
    public String zone;
    public String volume;
    public String poNumber;
    public String caseNumber;
    public String supplierCode;
    public String supplierName;
    public String employeeCode;
    public String sensitive;
    public String PRVStartDate;
    public String PRVEndDate;
    public String bookedDateTime;
    public String originalBookedDateTime;
    public String clientCode;
    public String PrvBookingDetailsId;
    public String CaseManagerName;
    public String CaseManagerNumber;
    public ServiceDetails serviceDetails;
    public CartonKits cartonKits;
    public String OrderLineId;
    public String StartNote;
    public String EndNote;
    public String ServiceType;
    public String GroupParentOrderLineId;

    public PRVDownload() {}
    
    public PRVDownload(SoapObject soapObject){

		if (soapObject.hasProperty("MoveNumber"))
		{
			Object obj = soapObject.getProperty("MoveNumber");
			if (obj != null && obj.getClass().equals(SoapPrimitive.class)){
				SoapPrimitive j22 =(SoapPrimitive) obj;
				moveNumber = j22.toString();
			}
		}
		if (soapObject.hasProperty("MemberName"))
		{
			SoapObject j23 = (SoapObject)soapObject.getProperty("MemberName");
			memberName =  new Name (j23);

		}
		if (soapObject.hasProperty("MemberAddress"))
		{
			SoapObject j24 = (SoapObject)soapObject.getProperty("MemberAddress");
			memberAddress =  new Address (j24);

		}
		if (soapObject.hasProperty("MemberContact"))
		{
			SoapObject j25 = (SoapObject)soapObject.getProperty("MemberContact");
			memberContact =  new Contact (j25);

		}
		if (soapObject.hasProperty("MoveDate"))
		{
			Object obj = soapObject.getProperty("MoveDate");
			if (obj != null && obj.getClass().equals(SoapPrimitive.class)){
				SoapPrimitive j26 =(SoapPrimitive) obj;
				moveDate = j26.toString();
			}
		}
		if (soapObject.hasProperty("Region"))
		{
			Object obj = soapObject.getProperty("Region");
			if (obj != null && obj.getClass().equals(SoapPrimitive.class)){
				SoapPrimitive j27 =(SoapPrimitive) obj;
				region = j27.toString();
			}
		}
		if (soapObject.hasProperty("Zone"))
		{
			Object obj = soapObject.getProperty("Zone");
			if (obj != null && obj.getClass().equals(SoapPrimitive.class)){
				SoapPrimitive j28 =(SoapPrimitive) obj;
				zone = j28.toString();
			}
		}
		if (soapObject.hasProperty("Volume"))
		{
			Object obj = soapObject.getProperty("Volume");
			if (obj != null && obj.getClass().equals(SoapPrimitive.class)){
				SoapPrimitive j29 =(SoapPrimitive) obj;
				volume = j29.toString();
			}
		}
		if (soapObject.hasProperty("PoNumber"))
		{
			Object obj = soapObject.getProperty("PoNumber");
			if (obj != null && obj.getClass().equals(SoapPrimitive.class)){
				SoapPrimitive j30 =(SoapPrimitive) obj;
				poNumber = j30.toString();
			}
		}
		if (soapObject.hasProperty("CaseNumber"))
		{
			Object obj = soapObject.getProperty("CaseNumber");
			if (obj != null && obj.getClass().equals(SoapPrimitive.class)){
				SoapPrimitive j32 =(SoapPrimitive) obj;
				caseNumber = j32.toString();
			}
		}
		if (soapObject.hasProperty("SupplierCode"))
		{
			Object obj = soapObject.getProperty("SupplierCode");
			if (obj != null && obj.getClass().equals(SoapPrimitive.class)){
				SoapPrimitive j33 =(SoapPrimitive) obj;
				supplierCode = j33.toString();
			}
		}
		if (soapObject.hasProperty("SupplierName"))
		{
			Object obj = soapObject.getProperty("SupplierName");
			if (obj != null && obj.getClass().equals(SoapPrimitive.class)){
				SoapPrimitive j34 =(SoapPrimitive) obj;
				supplierName = j34.toString();
			}
		}
		if (soapObject.hasProperty("EmployeeCode"))
		{
			Object obj = soapObject.getProperty("EmployeeCode");
			if (obj != null && obj.getClass().equals(SoapPrimitive.class)){
				SoapPrimitive j35 =(SoapPrimitive) obj;
				employeeCode = j35.toString();
			}
		}
		if (soapObject.hasProperty("Sensitive"))
		{
			Object obj = soapObject.getProperty("Sensitive");
			if (obj != null && obj.getClass().equals(SoapPrimitive.class)){
				SoapPrimitive j36 =(SoapPrimitive) obj;
				sensitive = j36.toString();
			}
		}
		if (soapObject.hasProperty("PRVStartDate"))
		{
			Object obj = soapObject.getProperty("PRVStartDate");
			if (obj != null && obj.getClass().equals(SoapPrimitive.class)){
				SoapPrimitive j37 =(SoapPrimitive) obj;
				PRVStartDate = j37.toString();
			}
		}
		if (soapObject.hasProperty("PRVEndDate"))
		{
			Object obj = soapObject.getProperty("PRVEndDate");
			if (obj != null && obj.getClass().equals(SoapPrimitive.class)){
				SoapPrimitive j38 =(SoapPrimitive) obj;
				PRVEndDate = j38.toString();
			}
		}
		if (soapObject.hasProperty("BookedDateTime"))
		{
			Object obj = soapObject.getProperty("BookedDateTime");
			if (obj != null && obj.getClass().equals(SoapPrimitive.class)){
				SoapPrimitive j39 =(SoapPrimitive) obj;
				bookedDateTime = j39.toString();
			}
		}
		if (soapObject.hasProperty("OriginalBookedDateTime"))
		{
			Object obj = soapObject.getProperty("OriginalBookedDateTime");
			if (obj != null && obj.getClass().equals(SoapPrimitive.class)){
				SoapPrimitive j40 =(SoapPrimitive) obj;
				originalBookedDateTime = j40.toString();
			}
		}
		if (soapObject.hasProperty("ClientCode"))
		{
			Object obj = soapObject.getProperty("ClientCode");
			if (obj != null && obj.getClass().equals(SoapPrimitive.class)){
				SoapPrimitive j41 =(SoapPrimitive) obj;
				clientCode = j41.toString();
			}
		}
		if (soapObject.hasProperty("PrvBookingDetailsId"))
		{
			Object obj = soapObject.getProperty("PrvBookingDetailsId");
			if (obj != null && obj.getClass().equals(SoapPrimitive.class)){
				SoapPrimitive j42 =(SoapPrimitive) obj;
				PrvBookingDetailsId = j42.toString();
			}
		}
		if (soapObject.hasProperty("CaseManagerName"))
		{
			Object obj = soapObject.getProperty("CaseManagerName");
			if (obj != null && obj.getClass().equals(SoapPrimitive.class)){
				SoapPrimitive j43 =(SoapPrimitive) obj;
				CaseManagerName = j43.toString();
			}
		}
		if (soapObject.hasProperty("CaseManagerNumber"))
		{
			Object obj = soapObject.getProperty("CaseManagerNumber");
			if (obj != null && obj.getClass().equals(SoapPrimitive.class)){
				SoapPrimitive j44 =(SoapPrimitive) obj;
				CaseManagerNumber = j44.toString();
			}
		}
		if (soapObject.hasProperty("ServiceDetails"))
		{
			SoapObject j45 = (SoapObject)soapObject.getProperty("ServiceDetails");
			serviceDetails =  new ServiceDetails (j45);
		}
		if (soapObject.hasProperty("CartonKit"))
		{
			SoapObject j46 = (SoapObject)soapObject.getProperty("CartonKit");
			cartonKits =  new CartonKits (j46);
		}
		if (soapObject.hasProperty("OrderLineId"))
		{
			Object obj = soapObject.getProperty("OrderLineId");
			if (obj != null && obj.getClass().equals(SoapPrimitive.class)){
				SoapPrimitive j47 =(SoapPrimitive) obj;
				OrderLineId = j47.toString();
			}
		}
		if (soapObject.hasProperty("StartNote"))
		{
			Object obj = soapObject.getProperty("StartNote");
			if (obj != null && obj.getClass().equals(SoapPrimitive.class)){
				SoapPrimitive j48 =(SoapPrimitive) obj;
				StartNote = j48.toString();
			}
		}
		if (soapObject.hasProperty("EndNote"))
		{
			Object obj = soapObject.getProperty("EndNote");
			if (obj != null && obj.getClass().equals(SoapPrimitive.class)){
				SoapPrimitive j49 =(SoapPrimitive) obj;
				EndNote = j49.toString();
			}
		}
		if (soapObject.hasProperty("ServiceType"))
		{
			Object obj = soapObject.getProperty("ServiceType");
			if (obj != null && obj.getClass().equals(SoapPrimitive.class)){
				SoapPrimitive j50 =(SoapPrimitive) obj;
				ServiceType = j50.toString();
			}
		}
		if (soapObject.hasProperty("GroupParentOrderLineId"))
		{
			Object obj = soapObject.getProperty("GroupParentOrderLineId");
			if (obj != null && obj.getClass().equals(SoapPrimitive.class)){
				SoapPrimitive j50 =(SoapPrimitive) obj;
				GroupParentOrderLineId = j50.toString();
			}
		}
		
	}
	@Override
	public Object getProperty(int index) {
		switch(index)  {
        case 0: return moveNumber;
        case 1: return memberName;
        case 2: return memberAddress;
        case 3: return memberContact;
        case 4: return moveDate;
        case 5: return region;
        case 6: return zone;
        case 7: return volume;
        case 8: return poNumber;
        case 9: return caseNumber;
        case 10: return supplierCode;
        case 11: return supplierName;
        case 12: return employeeCode;
        case 13: return sensitive;
        case 14: return PRVStartDate;
        case 15: return PRVEndDate;
        case 16: return bookedDateTime;
        case 17: return originalBookedDateTime;
        case 18: return clientCode;
        case 19: return PrvBookingDetailsId;
        case 20: return CaseManagerName;
        case 21: return CaseManagerNumber;
        case 22: return cartonKits;
        case 23: return serviceDetails;
        case 24: return OrderLineId;
        case 25: return StartNote;
        case 26: return EndNote;
        case 27: return ServiceType;
        case 28: return GroupParentOrderLineId;
        }
        return null;
	}

	@Override
	public int getPropertyCount() {
		return 29;
	}

	@Override
	public void getPropertyInfo(int index, @SuppressWarnings("rawtypes") Hashtable arg1, PropertyInfo info) {
		switch(index)  {
        case 0:
            info.name = "MoveNumber";
            info.type = PropertyInfo.STRING_CLASS; break;
        case 1:
            info.name = "MemberName";
            info.type = Name.class; break;
        case 2:
            info.name = "MemberAddress";
            info.type = Address.class; break;
        case 3:
            info.name = "MemberContact";
            info.type = Contact.class; break;
        case 4:
            info.name = "MoveDate";
            info.type = PropertyInfo.STRING_CLASS; break;
        case 5:
            info.name = "Region";
            info.type = PropertyInfo.STRING_CLASS; break;
        case 6:
            info.name = "Zone";
            info.type = PropertyInfo.STRING_CLASS; break;
        case 7:
            info.name = "Volume";
            info.type = PropertyInfo.STRING_CLASS; break;
        case 8:
            info.name = "PoNumber";
            info.type = PropertyInfo.STRING_CLASS; break;
        case 9:
            info.name = "CaseNumber";
            info.type = PropertyInfo.STRING_CLASS; break;
        case 10:
            info.name = "SupplierCode";
            info.type = PropertyInfo.STRING_CLASS; break;
        case 11:
            info.name = "SupplierName";
            info.type = PropertyInfo.STRING_CLASS; break;
        case 12:
            info.name = "EmployeeCode";
            info.type = PropertyInfo.STRING_CLASS; break;
        case 13:
            info.name = "Sensitive";
            info.type = PropertyInfo.STRING_CLASS; break;
        case 14:
            info.name = "PRVStartDate";
            info.type = PropertyInfo.STRING_CLASS; break;
        case 15:
            info.name = "PRVEndDate";
            info.type = PropertyInfo.STRING_CLASS; break;
        case 16:
            info.name = "BookedDateTime";
            info.type = PropertyInfo.STRING_CLASS; break;
        case 17:
            info.name = "OriginalBookedDateTime";
            info.type = PropertyInfo.STRING_CLASS; break;
        case 18:
            info.name = "ClientCode";
            info.type = PropertyInfo.STRING_CLASS; break;
        case 19:
        	info.name = "PrvBookingDetailsId";
        	info.type = PropertyInfo.STRING_CLASS; break;
        case 20: 
        	info.name = "CaseManagerName";
        	info.type = PropertyInfo.STRING_CLASS; break;
        case 21:
        	info.name = "CaseManagerNumber";
        	info.type = PropertyInfo.STRING_CLASS; break;
        case 22:
            info.name = "CartonKits";
            info.type = CartonKit.class; break;
        case 23:
            info.name = "ServiceDetails";
            info.type = ServiceDetail.class; break;
        case 24:
        	info.name = "OrderLineId";
        	info.type = PropertyInfo.STRING_CLASS; break;        	
        case 25:
        	info.name = "StartNote";
        	info.type = PropertyInfo.STRING_CLASS; break;        	
        case 26:
        	info.name = "EndNote";
        	info.type = PropertyInfo.STRING_CLASS; break;
        case 27:
        	info.name = "ServiceType";
        	info.type = PropertyInfo.STRING_CLASS; break;   	
        case 28:
        	info.name = "GroupParentOrderLineId";
        	info.type = PropertyInfo.STRING_CLASS; break;   	
        }
	}

	@Override
	public void setProperty(int index, Object value) {
		switch(index)  {
        case 0: moveNumber 				= value.toString(); break;
        case 1: memberName 				= (Name) value; break;
        case 2: memberAddress 			= (Address) value; break;
        case 3: memberContact 			= (Contact) value; break;
        case 4: moveDate 				= value.toString(); break;
        case 5: region 					= value.toString(); break;
        case 6: zone 					= value.toString(); break;
        case 7: volume 					= value.toString(); break;
        case 8: poNumber 				= value.toString(); break;
        case 9: caseNumber 				= value.toString(); break;
        case 10: supplierCode 			= value.toString(); break;
        case 11: supplierName 			= value.toString(); break;
        case 12: employeeCode 			= value.toString(); break;
        case 13: sensitive 				= value.toString(); break;
        case 14: PRVStartDate 			= value.toString(); break;
        case 15: PRVEndDate 			= value.toString(); break;
        case 16: bookedDateTime 		= value.toString(); break;
        case 17: originalBookedDateTime = value.toString(); break;
        case 18: clientCode 			= value.toString(); break;
        case 19: PrvBookingDetailsId	= value.toString(); break;
        case 20: CaseManagerName 		= value.toString(); break;
        case 21: CaseManagerNumber		= value.toString(); break;
        case 22: cartonKits				= (CartonKits) value; break;
        case 23: serviceDetails			= (ServiceDetails) value; break;
        case 24: OrderLineId			= value.toString(); break;
        case 25: StartNote		 		= value.toString(); break;
        case 26: EndNote				= value.toString(); break;
        case 27: ServiceType			= value.toString(); break;
        case 28: GroupParentOrderLineId = value.toString(); break;
        }
	}
}
