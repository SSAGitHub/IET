package org.theiet.rsuite.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

//TODO to remove
public class DateUtils {

	public static final SimpleDateFormat DATE_FORMAT_yyyy_MM_dd_h_m_s_ampm = new SimpleDateFormat("yyyy-MM-dd h:m:s a");

	/**
	 * A date and time format that looks like "2014-08-21 232215"
	 */
	public static final SimpleDateFormat DATE_FORMAT_YYYYMMDDHHMMSS = new SimpleDateFormat("yyyyMMddHHmmss");

	public static String getDate (SimpleDateFormat dateFormat, Date date) {
		return dateFormat.format(date);
	}

}
