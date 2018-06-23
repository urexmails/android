package com.contactpoint.model.service.impl;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.location.Location;

import com.contactpoint.model.DTO.LocationInDTO;
import com.contactpoint.model.service.UtilService;

public class UtilServiceImpl implements UtilService {

	@Override
	public boolean isSensitive(String sensitive) {
		return sensitive.compareTo("1") == 0;
	}

	@Override
	public boolean isUplift(String type) {
		return type.toUpperCase().compareTo(UPLIFT) == 0;
	}

	@Override
	public boolean isDelivery(String type) {
		return type.toUpperCase().compareTo(DELIVERY) == 0;
	}

	@Override
	public String printFormat(double digit) {
		return String.format(FORMAT, digit);
	}

	@Override
	public String getCurrentDateWithFormat(String format) {
		DateFormat df = new SimpleDateFormat(format);
		return df.format(new Date());
	}
	
	@Override
	public Date getDateFromString(String date, String format) throws ParseException {
		SimpleDateFormat df = new SimpleDateFormat(format);
		return df.parse(date);
	}
	
	@Override
	public boolean isNumeric(String number) {
		try {
			Integer.parseInt(number);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	@Override
	public void updateLocationInDTO(Location param) {
		String strloc = "0,0";
		if (param != null) {
			strloc  = param.getLatitude() + "," + param.getLongitude();
		}

		LocationInDTO.LastLocation = strloc;
		LocationInDTO.LastUpdated = System.currentTimeMillis();
	}

	@Override
	public String removeBase64Indicator(String base64String) {
		// Hyap #9559 - remove base64 indicator to bypass Toll WAF filter
		// base64 image sample: 'data:image/jpeg;base64,{actual image}';
		String[] image = base64String.split(",");
		if (image.length == 2) {
			base64String = image[1];
		}
		return base64String;
	}
	
}
