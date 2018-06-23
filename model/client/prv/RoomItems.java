package com.contactpoint.model.client.prv;

import java.util.Hashtable;
import java.util.Vector;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;

public class RoomItems extends Vector<RoomItem> implements KvmSerializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3607244943774259898L;
	public static final String NAMESPACE = "http://TMSConsultant.Model/2013/TMSConsultant";
	public final static String MM_NAMESPACE = "http://schemas.datacontract.org/2004/07/TollTransitions.API.DataContracts";
	public boolean isMM;

	public RoomItems(){}

	public RoomItems(SoapObject soapObject)
	{
		if (soapObject != null){
			int size = soapObject.getPropertyCount();
			for (int i0=0;i0<size;i0++){
				Object obj = soapObject.getProperty(i0);
				if (obj.getClass().equals(SoapObject.class)){
					SoapObject j0 =(SoapObject) soapObject.getProperty(i0);
					RoomItem j1= new RoomItem(j0);
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
		arg2.name = "RoomItem";
		arg2.namespace = isMM ? MM_NAMESPACE : NAMESPACE;
		arg2.type = RoomItem.class;
	}

	@Override
	public void setProperty(int arg0, Object arg1) {
		this.add((RoomItem)arg1);
	}

}
