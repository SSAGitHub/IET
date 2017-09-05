package org.theiet.rsuite.journals.xpath;

import org.junit.Assert;
import org.junit.Test;
import org.theiet.rsuite.utils.XpathUtils;

public class XpathUtilsTest {

	@Test
	public void testInspecMerge() throws Exception{
		
		testXpath("/rs_ca_map/rs_ca[rmd:get-type(.) = 'book' and rmd:get-lmd-value(., 'book_code') = 'testing']",
				"/rs_ca_map/rs_ca[./mv:metadata/mv:system/mv:ca-type = 'book' and ./mv:metadata/mv-lmd:layered/mv-lmd:book_code = 'testing']");
		

		testXpath("/rs_ca_map/rs_ca[rmd:get-type(.) = 'article' and rmd:get-display-name(.) = 'ELL-2012-1841']",
				"/rs_ca_map/rs_ca[./mv:metadata/mv:system/mv:ca-type = 'article' and ./mv:metadata/mv:system/mv:display-name = 'ELL-2012-1841']");
		
		
		testXpath("/rs_ca_map/rs_ca[rmd:get-type ( . ) = 'article' and rmd:get-display-name ( .) = 'ELL-2012-1841' ]",
				"/rs_ca_map/rs_ca[./mv:metadata/mv:system/mv:ca-type = 'article' and ./mv:metadata/mv:system/mv:display-name = 'ELL-2012-1841' ]");
		
		
		testXpath("/rs_ca_map/rs_ca[rmd:get-type(.) = 'book' and rmd:get-lmd-value(.,'book_code') = 'testing']",
				"/rs_ca_map/rs_ca[./mv:metadata/mv:system/mv:ca-type = 'book' and ./mv:metadata/mv-lmd:layered/mv-lmd:book_code = 'testing']");
				

		testXpath("/rs_ca_map/rs_ca[rmd:get-type(.) = 'book' and rmd:get-lmd-value(.,'book_code') = 'testing']",
				"/rs_ca_map/rs_ca[./mv:metadata/mv:system/mv:ca-type = 'book' and ./mv:metadata/mv-lmd:layered/mv-lmd:book_code = 'testing']");
		
		testXpath(
		"/rs_ca_map/rs_ca[rmd:get-type(.) = 'article' and rmd:get-lmd-value(., 'journal_code') = 'CODE123' and rmd:get-lmd-value(., 'available') = 'yes']",
		"/rs_ca_map/rs_ca[./mv:metadata/mv:system/mv:ca-type = 'article' and ./mv:metadata/mv-lmd:layered/mv-lmd:journal_code = 'CODE123' and ./mv:metadata/mv-lmd:layered/mv-lmd:available = 'yes']");
		
		testXpath("/rs_ca_map/rs_ca[rmd:get-type(.) = 'article' and not(rmd:get-lmd-value(., 'online_published_date')) and rmd:get-lmd-value(., 'available') = 'yes']",
				"/rs_ca_map/rs_ca[./mv:metadata/mv:system/mv:ca-type = 'article' and not(./mv:metadata/mv-lmd:layered/mv-lmd:online_published_date) and ./mv:metadata/mv-lmd:layered/mv-lmd:available = 'yes']");
		
	}

	private void testXpath(String xpath, String expected){		
		String newXpath = XpathUtils.resolveRSuiteFunctionsInXPath(xpath);
		
		print(expected, newXpath);
		Assert.assertEquals(expected, newXpath);
	}
	
	public void print(String expected, String newXpath) {
		
		System.out.println("==========");
		System.out.println(expected);
		System.out.println(newXpath);
		
	}


}
