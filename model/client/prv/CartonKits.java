package com.contactpoint.model.client.prv;

import java.io.Serializable;
import java.util.Hashtable;
import java.util.Vector;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;

public class CartonKits extends Vector<CartonKit> implements KvmSerializable,
		Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -249211135436452445L;
	public static final String NAMESPACE = "http://TMSConsultant.Model/2013/TMSConsultant";

	public CartonKits(){}

	public CartonKits(SoapObject soapObject)
	{
		if (soapObject != null){
			int size = soapObject.getPropertyCount();
			for (int i0=0;i0<size;i0++){
				Object obj = soapObject.getProperty(i0);
				if (obj.getClass().equals(SoapObject.class)){
					SoapObject j0 =(SoapObject) soapObject.getProperty(i0);
					CartonKit j1= new CartonKit(j0);
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
		arg2.name = "CartonKit";
		arg2.type = CartonKit.class;
	}

	@Override
	public void setProperty(int arg0, Object arg1) {
		this.add((CartonKit)arg1);
	}

}
