package com.contactpoint.model.DTO.prv;

import java.util.Vector;

import com.contactpoint.model.client.prv.Room;

public class RoomOutDTO extends Room {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8270710013151167519L;
	
	public Vector<ItemInDTO> getCurrentItemList() { return currentItemList; }
	public Vector<ItemInDTO> getCartonList() { return cartonList; }
	public Vector<ItemInDTO> getCoverList() { return coverList; }
	public Vector<ItemInDTO> getCrateList() { return crateList; }
	
	public void setCurrentItemList(Vector<ItemInDTO> currentItemList) { this.currentItemList = currentItemList; }
	public void setCartonList(Vector<ItemInDTO> cartonList) { this.cartonList = cartonList; }
	public void setCoverList(Vector<ItemInDTO> coverList) { this.coverList = coverList; }
	public void setCrateList(Vector<ItemInDTO> crateList) { this.crateList = crateList; }

	private Vector<ItemInDTO> currentItemList;
	private Vector<ItemInDTO> cartonList;
	private Vector<ItemInDTO> coverList;
	private Vector<ItemInDTO> crateList;
	
}
