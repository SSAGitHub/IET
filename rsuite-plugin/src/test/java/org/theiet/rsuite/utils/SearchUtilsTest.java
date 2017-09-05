package org.theiet.rsuite.utils;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class SearchUtilsTest {

	@Test
	public void test_generateSearchCaBasedOnLmdQuery_noLmdValues() {
		
		String lmdName = "product_code";
		List<String> values = new ArrayList<String>();
		
		String actualQuery = SearchUtils.generateSearchCaBasedOnLmdQuery(lmdName, values);
		assertEquals("/rs_ca_map/rs_ca[rmd:get-lmd-value(., 'product_code') = ()]", actualQuery);
	}
	
	@Test
	public void test_generateSearchCaBasedOnLmdQuery_oneLmdValue() {
		
		String lmdName = "product_code";
		List<String> values = new ArrayList<String>();
		values.add("first");
		
		String actualQuery = SearchUtils.generateSearchCaBasedOnLmdQuery(lmdName, values);
		assertEquals("/rs_ca_map/rs_ca[rmd:get-lmd-value(., 'product_code') = ('first')]", actualQuery);
	}
	
	@Test
	public void test_generateSearchCaBasedOnLmdQuery_twoLmdValues() {
		
		String lmdName = "product_code";
		List<String> values = new ArrayList<String>();
		values.add("first");
		values.add("second");
		
		String actualQuery = SearchUtils.generateSearchCaBasedOnLmdQuery(lmdName, values);
		assertEquals("/rs_ca_map/rs_ca[rmd:get-lmd-value(., 'product_code') = ('first', 'second')]", actualQuery);
	}

}
