package com.contactpoint.model.client.prv;

import java.io.Serializable;
import java.util.Hashtable;
import java.util.Vector;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;

public class ReferenceData implements KvmSerializable, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 539639375583636510L;
	public BookingReasons bookingReasons;
	public BusinessGroups businessGroups;
	public Clients clients;
	public Items items;
	public PRVQuestions pRVQuestions;
	public PhotoConfig photoConfig;
	public ProviderCodes providerCodes;
	public Regions regions;
	public RoomItems roomItems;
	public Rooms rooms;
	public Volumes volumes;
	public Zones zones;
	public String lastUpdated;
	
	public ReferenceData() {}
	public ReferenceData(Vector<SoapObject> soapObject) {
		
    	bookingReasons	= new BookingReasons(soapObject.get(0));
    	businessGroups	= new BusinessGroups(soapObject.get(1));
    	clients			= new Clients(soapObject.get(2));
    	items			= new Items(soapObject.get(3));
    	pRVQuestions	= new PRVQuestions(soapObject.get(4));
    	photoConfig		= new PhotoConfig(soapObject.get(5));
    	providerCodes	= new ProviderCodes(soapObject.get(6));
    	regions			= new Regions(soapObject.get(7));
    	roomItems		= new RoomItems(soapObject.get(8));
    	rooms			= new Rooms(soapObject.get(9));
    	volumes			= new Volumes(soapObject.get(10));
    	zones			= new Zones(soapObject.get(11));

	}
	
	@Override
	public Object getProperty(int index) {
		switch(index)  {
        case 0: return bookingReasons;
        case 1: return businessGroups;
        case 2: return clients;
        case 3: return items;
        case 4: return pRVQuestions;
        case 5: return photoConfig;
        case 6: return providerCodes;
        case 7: return regions;
        case 8: return roomItems;
        case 9: return rooms;
        case 10: return volumes;
        case 11: return zones;
		}
		return null;
	}

	@Override
	public int getPropertyCount() {
		return 12;
	}

	@Override
	public void getPropertyInfo(int index, @SuppressWarnings("rawtypes") Hashtable arg1, PropertyInfo info) {
		switch(index) {
        case 0:
            info.name = "BookingReasons";
            info.type = BookingReasons.class; break;
        case 1:
            info.name = "BusinessGroups";
            info.type = BusinessGroups.class; break;
        case 2:
            info.name = "Clients";
            info.type = Clients.class; break;
		case 3:
            info.name = "Items";
            info.type = Items.class; break;
		case 4:
            info.name = "PRVQuestions";
            info.type = PRVQuestions.class; break;
		case 5:
            info.name = "PhotoConfig";
            info.type = PhotoConfig.class; break;
		case 6:
            info.name = "ProviderCodes";
            info.type = ProviderCodes.class; break;
		case 7:
            info.name = "Regions";
            info.type = Regions.class; break;
        case 8:
            info.name = "RoomItems";
            info.type = RoomItems.class; break;
        case 9:
            info.name = "Rooms";
            info.type = Rooms.class; break;
        case 10:
            info.name = "Volumes";
            info.type = Volumes.class; break;
        case 11:
            info.name = "Zones";
            info.type = Zones.class; break;

		}
	}

	@Override
	public void setProperty(int index, Object value) {
		switch(index)  {
        
        case 0: 	bookingReasons	= (BookingReasons)value; break;
        case 1: 	businessGroups	= (BusinessGroups)value; break;
        case 2: 	clients			= (Clients)value; break;
        case 3: 	items			= (Items)value; break;
        case 4: 	pRVQuestions	= (PRVQuestions)value; break;
        case 5:		photoConfig		= (PhotoConfig)value; break;
        case 6: 	providerCodes	= (ProviderCodes)value; break;
        case 7: 	regions			= (Regions)value; break;
        case 8: 	roomItems		= (RoomItems)value; break;
        case 9: 	rooms			= (Rooms)value; break;
        case 10: 	volumes			= (Volumes)value; break;
        case 11:	zones			= (Zones)value; break;

		}
	}
}
