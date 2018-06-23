package com.contactpoint.model.util;

import java.util.Comparator;

import com.contactpoint.model.client.CIForm;

public class CIFormComparator implements Comparator<CIForm> {
	
	public static final int NAME = 0;		// changes in tag should change those in work_list_tbl_header
	public static final int MOVE_DATE = 1;
	public static final int SUBURB = 2;
	public static final int ZONE = 3;
	public static final int VOLUME = 4;
	public static final int STATUS = 5;
	
	private int mCompareCriteria;
	
	public CIFormComparator() {
		mCompareCriteria = -1;
	}

	@Override
	public int compare(CIForm lhs, CIForm rhs) {
		switch (mCompareCriteria) {
		case NAME:
			return lhs.getDownload().memberName.firstName
					.compareTo(rhs.getDownload().memberName.firstName);
		case MOVE_DATE:
			return lhs.getDownload().moveDate
					.compareTo(rhs.getDownload().moveDate);
		case SUBURB:
			return lhs.getDownload().memberAddress.suburb
					.compareTo(rhs.getDownload().memberAddress.suburb);
		case ZONE:
			return lhs.getDownload().zone
					.compareTo(rhs.getDownload().zone);
		case VOLUME:
			return lhs.getDownload().volume
					.compareTo(rhs.getDownload().volume);
		case STATUS:
			if ((lhs.getUpload().isValid() && rhs.getUpload().isValid()) ||
					(!lhs.getUpload().isValid() && !rhs.getUpload().isValid())) {
				return 0;
			} else if (lhs.getUpload().isValid()) {
				return 1;
			} else if (rhs.getUpload().isValid()) {
				return -1;
			}
		}
		return 0;
	}
			
	public int getCompareCriteria () { return mCompareCriteria; }
	public void setCompareCriteria (int criteria) { mCompareCriteria = criteria; }
	
}
