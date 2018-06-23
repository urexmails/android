package com.contactpoint.model.client.prv;

import java.io.Serializable;
import java.util.Hashtable;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

public class PRVUpload implements Serializable, KvmSerializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4428121742918812910L;
	
	public static final String NAMESPACE = "http://TMSConsultant.Model/2013/TMSConsultant";
	public final static String MM_NAMESPACE = "http://schemas.datacontract.org/2004/07/TollTransitions.API.DataContracts";
	
	public VectorPRVQuestionAnswer questionAnswer;
	public RoomItems roomItems;
	public Rooms rooms;
	public Items items;
	private boolean isMM;
	
	public PRVUpload() {
		questionAnswer = new VectorPRVQuestionAnswer();
		roomItems = new RoomItems();
		rooms = new Rooms();
		items = new Items();
	}
	
	public boolean isMM() { return isMM; }
	
	public void setIsMM(boolean isMM) {
		this.isMM = isMM;
		questionAnswer.isMM = isMM;
		roomItems.isMM = isMM;
		rooms.isMM = isMM;
		items.isMM = isMM;
	}
	
	@Override
	public Object getProperty(int arg0) {
		switch(arg0){
		case 0:
			return questionAnswer;
		case 1:
			return roomItems;
		case 2:
			return rooms;
		case 3:
			return items;
		}
		return null;
	}

	@Override
	public int getPropertyCount() {
		return 4;
	}

	@Override
	public void getPropertyInfo(int index, @SuppressWarnings("rawtypes") Hashtable arg1, PropertyInfo info) {
		switch(index){
		case 0:
			info.type = PropertyInfo.VECTOR_CLASS;
			info.name = "PRVQuestionAnswer";
			info.namespace = isMM ? QuestionAnswer.MM_NAMESPACE : QuestionAnswer.NAMESPACE;
			break;
		case 1:
			info.type = PropertyInfo.VECTOR_CLASS;
			info.name = "RoomItems";
			info.namespace = isMM ? RoomItems.MM_NAMESPACE : RoomItems.NAMESPACE;
			break;
		case 2:
			info.type = PropertyInfo.VECTOR_CLASS;
			info.name = "Rooms";
			info.namespace = isMM ? Rooms.MM_NAMESPACE : Rooms.NAMESPACE;
			break;
		case 3:
			info.type = PropertyInfo.VECTOR_CLASS;
			info.name = "Items";
			info.namespace = isMM ? Items.MM_NAMESPACE : Items.NAMESPACE;
			break;
		}
	}

	@Override
	public void setProperty(int index, Object value) {
		switch(index){
		case 0:
			questionAnswer = (VectorPRVQuestionAnswer)value;
			break;
		case 1:
			roomItems = (RoomItems)value;
			break;
		case 2:
			rooms = (Rooms)value;
			break;
		case 3:
			items = (Items)value;
			break;
		}
	}

}
