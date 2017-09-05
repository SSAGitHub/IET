package org.theiet.rsuite.standards.domain.image;

import static org.junit.Assert.*;

import org.junit.Test;

import com.reallysi.rsuite.api.RSuiteException;

public class ImageFileNormalizerTest {

	@Test
	public void test_createNormalizedFileName_fileWithNoPrefix() throws RSuiteException{
		String normamlizedFileName = ImageFileNormalizer.createNormalizedFileName("wre", "testImage.eps");		
		assertEquals("wre_testImage.eps", normamlizedFileName);		
	}
	
	@Test
	public void test_createNormalizedFileName_fileWithPrefix() throws RSuiteException{
		String normamlizedFileName = ImageFileNormalizer.createNormalizedFileName("wre", "wre_testImage.eps");		
		assertEquals("wre_testImage.eps", normamlizedFileName);		
	}
	
	
	@Test
	public void test_createNormalizedFileName_fileCapitalizeExtension() throws RSuiteException{
		String normamlizedFileName = ImageFileNormalizer.createNormalizedFileName("wre", "wre_testImage.EPS");		
		assertEquals("wre_testImage.eps", normamlizedFileName);		
	}
	
	@Test
	public void test_createNormalizedFileName_fileWithUnderscore() throws RSuiteException{
		String normamlizedFileName = ImageFileNormalizer.createNormalizedFileName("wre", "test_testImage.EPS");
		assertEquals("wre_test_testImage.eps", normamlizedFileName);
	}
	
	
	
}
