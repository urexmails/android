package com.contactpoint.model.DTO.prv;

import com.contactpoint.model.client.prv.Item;

public class ItemInDTO extends Item {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7139402077044522918L;
	public static final String IS_CUSTOM = "1";
	public static final String NOT_CUSTOM = "0";
	public static final String STANDARD = "STANDARD";
	public static final String CARTON = "CARTONS";
	public static final String COVER = "COVERS";
	public static final String CRATE = "CRATES";
	
	public int getQty()			{ return mQty; }
	public void setQty(int qty)	{ mQty = qty; }
	
	private int mQty;
}
