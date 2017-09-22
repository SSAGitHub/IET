package org.theiet.rsuite.utils;

import static org.junit.Assert.assertEquals;

import java.io.File;

import org.junit.Test;

public class RSuiteFileUtilsTest {

	@SuppressWarnings("deprecation")
	@Test
	public void testCommonFolders() {

		File file1 = new File("/opt/test/test2/temp/test/file.txt");
		File file2 = new File("/opt/test/test2/input/nextFolder/test/file.txt");
		
		File commonFolder = RSuiteFileUtils.getCommonFolder(file1, file2);
		assertEquals(new File("/opt/test/test2").getAbsolutePath(),
				commonFolder.getAbsolutePath());
	}

	@Test
	public void testNonCommonFolders() {

		File file1 = new File("/home/test/test2/temp/test/file.txt");
		File file2 = new File("/opt/test/test2/input/nextFolder/test/file.txt");

		File commonFolder = RSuiteFileUtils.getCommonFolder(file1, file2);
		assertEquals("", commonFolder.toString().
				substring(commonFolder.toString().lastIndexOf(":") + 1));
	}

}
