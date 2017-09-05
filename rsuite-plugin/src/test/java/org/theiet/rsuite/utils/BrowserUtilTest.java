package org.theiet.rsuite.utils;

import org.junit.*;

import com.rsicms.projectshelper.utils.browse.ProjectBrowserUtils;

public class BrowserUtilTest {

	@Test
	public void testGetParentIdFromPath() throws Exception {

		testPath("/", null);

		testPath("test", null);

		testPath("/caref:1111/caref:1532/caref:1538", "1532");

	}

	private void testPath(String path, String expected) {
		String parentId = ProjectBrowserUtils.getParentIdFromPath(path);
		print(expected, parentId);
		Assert.assertEquals(expected, parentId);
	}

	public void print(String expected, String newXpath) {

		System.out.println("==========");
		System.out.println(expected);
		System.out.println(newXpath);

	}

}
