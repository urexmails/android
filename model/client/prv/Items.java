package com.contactpoint.model.client.prv;

import java.util.Hashtable;
import java.util.TreeMap;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;

public class Items extends TreeMap<Integer, Item> implements KvmSerializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2805251171822433824L;
	public static final String NAMESPACE = "http://TMSConsultant.Model/2013/TMSConsultant";
	public final static String MM_NAMESPACE = "http://schemas.datacontract.org/2004/07/TollTransitions.API.DataContracts";

	public boolean isMM;
	
	public Items(){}

	public Items(SoapObject soapObject)
	{
		if (soapObject != null){
			int size = soapObject.getPropertyCount();
			for (int i0=0;i0<size;i0++){
				Object obj = soapObject.getProperty(i0);
				if (obj.getClass().equals(SoapObject.class)){
					SoapObject j0 =(SoapObject) soapObject.getProperty(i0);
					Item j1= new Item(j0);
					put(j1.ID, j1);
//					add(j1);
				}
			}
		}
	}
	@Override
	public Object getProperty(int arg0) {
		return this.values().toArray()[arg0];
//		return this.get(arg0);
	}

	@Override
	public int getPropertyCount() {
		return this.size();
	}

	@Override
	public void getPropertyInfo(int arg0, @SuppressWarnings("rawtypes") Hashtable arg1, PropertyInfo arg2) {
		arg2.name = "Item";
		arg2.namespace = isMM ? MM_NAMESPACE : NAMESPACE;
		arg2.type = Item.class;
	}

	@Override
	public void setProperty(int arg0, Object arg1) {
		Item object = (Item)arg1;
		this.put(object.ID, object);
//		this.add((Item)arg1);
	}

}
