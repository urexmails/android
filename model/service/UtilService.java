package com.contactpoint.model.service;

import java.text.ParseException;
import java.util.Date;

import android.location.Location;

public interface UtilService {
	public static final String UPLIFT = "UPLIFT";
	public static final String DELIVERY = "DELIVERY";
	public static final String FORMAT = "%.3f";
	public static final String DATE_FORMAT = "d/M/yyyy";
	public static final String DATE_TIME_FORMAT = "d/M/yyyy H:m:s";
	
	public boolean isSensitive(String sensitive);
	public boolean isUplift(String type);
	public boolean isDelivery(String type);
	public String printFormat(double digit);
	public String getCurrentDateWithFormat(String format);
	public Date getDateFromString(String date, String format) throws ParseException;
	public boolean isNumeric(String number);
	
	public void updateLocationInDTO(Location param);
	
	public String removeBase64Indicator(String base64String);
}
