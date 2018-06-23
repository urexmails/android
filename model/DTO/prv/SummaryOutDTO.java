package com.contactpoint.model.DTO.prv;

import java.util.TreeMap;

public class SummaryOutDTO {

	public int getTotalRoom() { return mTotalRoom; }
	public double getTotalCubic() { return mTotalCubic; }
	public TreeMap<String, Integer> getCartonList() { return mCartonList; }
	public TreeMap<String, Integer> getCoverList() { return mCoverList; }
	public TreeMap<String, Integer> getCrateList() { return mCrateList; }
	public int getTotalCartons() { return mTotalCartons; }
	public int getTotalCovers() { return mTotalCovers; }
	public int getTotalCrates() { return mTotalCrates; }
	
	public void setTotalRoom(int mTotalRoom) { this.mTotalRoom = mTotalRoom; }
	public void setTotalCubic(double mTotalCubic) { this.mTotalCubic = mTotalCubic; }
	public void setTotalCartons(int mTotalCartons) { this.mTotalCartons = mTotalCartons; }
	public void setTotalCovers(int mTotalCovers) { this.mTotalCovers = mTotalCovers; }
	public void setTotalCrates(int mTotalCrates) { this.mTotalCrates = mTotalCrates; }
	
	private int mTotalRoom;
	private double mTotalCubic;
	private int mTotalCartons;
	private int mTotalCovers;
	private int mTotalCrates;
	
	private TreeMap<String, Integer> mCartonList = new TreeMap<String, Integer>();
	private TreeMap<String, Integer> mCoverList = new TreeMap<String, Integer>();
	private TreeMap<String, Integer> mCrateList = new TreeMap<String, Integer>();
}
