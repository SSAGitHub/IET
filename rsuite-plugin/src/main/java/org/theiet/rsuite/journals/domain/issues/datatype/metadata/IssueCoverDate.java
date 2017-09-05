package org.theiet.rsuite.journals.domain.issues.datatype.metadata;

import java.text.*;
import java.util.*;

import org.apache.commons.lang.StringUtils;

import com.reallysi.rsuite.api.RSuiteException;

public class IssueCoverDate {

	private String coverDateValue;

	private IssueCoverDateFormat dateFormat;

	private Calendar coverDate;

	private DateFormat coverDateFormat = new SimpleDateFormat("d MMMM yyyy",
			Locale.UK);

	private DateFormat coverDateFormat2 = new SimpleDateFormat("MMMM yyyy",
			Locale.UK);

	public IssueCoverDate(String coverDateValue) throws RSuiteException {
		if (StringUtils.isNotBlank(coverDateValue)){
			this.coverDateValue = coverDateValue.trim();
			parseCoverDate(coverDateValue);	
		}
	}

	private void parseCoverDate(String coverDateValue) throws RSuiteException {

		String normalizedCoverDate = normalizeCoverDate(coverDateValue);

		try {

			if (coverDateValue.matches("^[0-9]+.*")) {
				coverDate = dateToCalendar(coverDateFormat
						.parse(normalizedCoverDate));
				dateFormat = IssueCoverDateFormat.FULL_DATE_FORMAT;
			} else {
				coverDate = dateToCalendar(coverDateFormat2
						.parse(normalizedCoverDate));
				dateFormat = IssueCoverDateFormat.MONTH_YEAR_FORMAT;
			}

		} catch (ParseException e) {
			throw new RSuiteException(0, "Unable to parse cover date "
					+ normalizedCoverDate, e);
		}

	}

	public String getAbbreviatedDate(){
		return coverDateValue;
	}
	
	private static Calendar dateToCalendar(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal;
	}

	public String getCoverDateValue() {
		return coverDateValue;
	}

	public IssueCoverDateFormat getDateFormat() {
		return dateFormat;
	}

	public Calendar getCoverDate() {
		return coverDate;
	}

	private static String normalizeCoverDate(String coverDate) {
		return coverDate.replaceAll("^([0-9]+)[a-z]+(.*)", "$1$2");
	}

}
