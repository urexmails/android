package com.contactpoint.model.util;

import java.text.ParseException;
import java.util.Comparator;

import com.contactpoint.model.ModelFactory;
import com.contactpoint.model.client.PRVForm;
import com.contactpoint.model.service.UtilService;

public class PRVFormComparator implements Comparator<PRVForm> {

	public static final int ORDER = 0;		// changes in tag should change those in work_list_tbl_header
	public static final int NAME = 1;
	public static final int PO_NUM = 2;
	public static final int SUBURB = 3;
	public static final int START = 4;
	public static final int END = 5;
	public static final int BOOK = 6;

	private int mCompareCriteria;

	public PRVFormComparator() {
		mCompareCriteria = -1;
	}

	@Override
	public int compare(PRVForm lhs, PRVForm rhs) {
		switch (mCompareCriteria) {
		case ORDER:
			if (lhs.getOrder() == 0) return 1;
			else if (rhs.getOrder() == 0) return -1;
			return lhs.getOrder() - rhs.getOrder();
		case NAME:
			return lhs.getDownload().get(0).memberName.firstName
					.compareTo(rhs.getDownload().get(0).memberName.firstName);
		case PO_NUM:
			return lhs.getDownload().get(0).poNumber
					.compareTo(rhs.getDownload().get(0).poNumber);
		case SUBURB:
			return lhs.getDownload().get(0).memberAddress.suburb
					.compareTo(rhs.getDownload().get(0).memberAddress.suburb);
		case START:
			try {
				return ModelFactory.getUtilService().getDateFromString(lhs.getDownload().get(0).PRVStartDate, UtilService.DATE_FORMAT).compareTo(
						ModelFactory.getUtilService().getDateFromString(rhs.getDownload().get(0).PRVStartDate, UtilService.DATE_FORMAT));
			} catch (ParseException e) {
				return 0;
			}
		case END:
			try {
				return ModelFactory.getUtilService().getDateFromString(lhs.getDownload().get(0).PRVEndDate, UtilService.DATE_FORMAT).compareTo(
						ModelFactory.getUtilService().getDateFromString(rhs.getDownload().get(0).PRVEndDate, UtilService.DATE_FORMAT));
			} catch (ParseException e) {
				return 0;
			}
		case BOOK:
			try {
				return ModelFactory.getUtilService().getDateFromString(lhs.getDownload().get(0).bookedDateTime, UtilService.DATE_TIME_FORMAT).compareTo(
						ModelFactory.getUtilService().getDateFromString(rhs.getDownload().get(0).bookedDateTime, UtilService.DATE_TIME_FORMAT));
			} catch (ParseException e) {
				return 0;
			}
		}
		return 0;
	}

	public int getCompareCriteria () { return mCompareCriteria; }
	public void setCompareCriteria (int criteria) { mCompareCriteria = criteria; }

}
