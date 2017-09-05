package org.theiet.rsuite.journals.domain.issues.datatype.metadata;

import static org.junit.Assert.*;

import java.util.Calendar;

import org.junit.Test;

import com.reallysi.rsuite.api.RSuiteException;

public class IssueCoverDateTest {

	@Test
	public void check_date_format_for_full_date_format2() throws RSuiteException {
		IssueCoverDate coverDate = new IssueCoverDate("19th September 2015");
		assertTrue(IssueCoverDateFormat.FULL_DATE_FORMAT == coverDate.getDateFormat());
		assertEquals("19th September 2015", coverDate.getAbbreviatedDate());
	}
	
	@Test
	public void check_date_format_for_full_date_format() throws RSuiteException {
		IssueCoverDate coverDate = new IssueCoverDate("2nd February 2015");
		assertTrue(IssueCoverDateFormat.FULL_DATE_FORMAT == coverDate.getDateFormat());
	}

	@Test
	public void check_date_format_for_month_date_format() throws RSuiteException {
		IssueCoverDate coverDate = new IssueCoverDate("February 2015");
		assertTrue(IssueCoverDateFormat.MONTH_YEAR_FORMAT == coverDate.getDateFormat());
	}
	
	@Test
	public void check_month_number_and_year_number_for_month_date_format() throws RSuiteException {
		IssueCoverDate coverDate = new IssueCoverDate("February 2015");
		Calendar date = coverDate.getCoverDate();
		assertTrue(date.get(Calendar.MONTH) == 1);
		assertTrue(date.get(Calendar.YEAR) == 2015);
		
	}
}
