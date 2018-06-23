package com.contactpoint.model.client.prv;

import java.io.Serializable;
import java.util.Hashtable;
import java.util.Vector;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;

public class BusinessGroups extends Vector<ReferenceIDDropDown> implements KvmSerializable,
		Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -249211135436452445L;
	public static final String NAMESPACE = "http://TMSConsultant.Model/2013/TMSConsultant";

	public BusinessGroups(){}

	public BusinessGroups(SoapObject soapObject)
	{
		if (soapObject != null){
			int size = soapObject.getPropertyCount();
			for (int i0=0;i0<size;i0++){
				Object obj = soapObject.getProperty(i0);
				if (obj.getClass().equals(SoapObject.class)){
					SoapObject j0 =(SoapObject) soapObject.getProperty(i0);
					ReferenceIDDropDown j1= new ReferenceIDDropDown(j0);
					add(j1);
				}
			}
		}
	}
	@Override
	public Object getProperty(int arg0) {
		return this.get(arg0);
	}

	@Override
	public int getPropertyCount() {
		return this.size();
	}

	@Override
	public void getPropertyInfo(int arg0, @SuppressWarnings("rawtypes") Hashtable arg1, PropertyInfo arg2) {
		arg2.name = "ReferenceIDDropDown";
		arg2.type = ReferenceIDDropDown.class;
	}

	@Override
	public void setProperty(int arg0, Object arg1) {
		this.add((ReferenceIDDropDown)arg1);
	}

}
