package org.theiet.rsuite.utils;

import java.io.File;

import junit.framework.Assert;

import org.junit.Test;

public class RSuiteFileUtilsTest {

	@Test
	public void testCommonFolders() {
		
		File file1 = new File("/opt/test/test2/temp/test/file.txt");
		File file2 = new File("/opt/test/test2/input/nextFolder/test/file.txt");
		
		File commonFolder = RSuiteFileUtils.getCommonFolder(file1, file2);
		Assert.assertEquals("/opt/test/test2", commonFolder.getAbsolutePath()); 
	}
	
	@Test
	public void testNonCommonFolders() {
		
		File file1 = new File("/home/test/test2/temp/test/file.txt");
		File file2 = new File("/opt/test/test2/input/nextFolder/test/file.txt");
		
		File commonFolder = RSuiteFileUtils.getCommonFolder(file1, file2);
		System.out.println(commonFolder);
		Assert.assertNull(commonFolder); 
	}

}
