package com.rsicms.projectshelper.export.impl;

import static org.junit.Assert.*;

import org.junit.Test;

public class RSuiteFileNameAliasLinkValueToMoMapperTest {

	@Test
	public void test_aliasFromLinkValue() {							
		String linkValue = "/rsuite/rest/v2/content/element/alias/CHAPTER 54 EARTHING ARRANGEMENTS AND PROTECTIVE CONDUCTORS#r54";
		String alias = RSuiteFileNameAliasLinkValueToMoMapper.getAliasFromLinkValue(linkValue);
		assertEquals("CHAPTER 54 EARTHING ARRANGEMENTS AND PROTECTIVE CONDUCTORS", alias);
	}

}
