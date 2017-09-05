package org.theiet.rsuite.domain.date;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.reallysi.rsuite.api.RSuiteException;

public class IetDate {

	private IetDate() {
	}

	private static final String UK_DATE_FORMAT = "yyyy-MM-dd";

	public static String getCurrentDate() {
		Date time = Calendar.getInstance().getTime();
		SimpleDateFormat ukDateFormat = new SimpleDateFormat(UK_DATE_FORMAT);
		return ukDateFormat.format(time);
	}

	public static Calendar parseDate(String date) throws RSuiteException {
		SimpleDateFormat ukDateFormat = new SimpleDateFormat(UK_DATE_FORMAT);
		try {
			Calendar instance = Calendar.getInstance();
			instance.setTime(ukDateFormat.parse(date));
			return instance;
		} catch (ParseException e) {
			throw new RSuiteException(0, "Unable to parse date" + date, e);
		}
	}
}
