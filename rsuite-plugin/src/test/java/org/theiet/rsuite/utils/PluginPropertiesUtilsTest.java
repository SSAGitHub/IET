package org.theiet.rsuite.utils;

import java.io.File;
import java.io.IOException;

import junit.framework.Assert;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;

import com.rsicms.projectshelper.utils.ProjectPluginProperties;

public class PluginPropertiesUtilsTest {


	@Before
	public void before() throws IOException{
		final File f = new File(ProjectPluginProperties.class.getProtectionDomain().getCodeSource().getLocation().getPath());
		FileUtils.copyDirectoryToDirectory( new File("src/main/resources/WebContent"), f);
		ProjectPluginProperties.reloadProperties();
	}
	
	@Test
	public void testLoadPropertyDefaultValue(){
		
		
		String prop = ProjectPluginProperties.getProperty("zzz", "default");
		
		Assert.assertEquals("default", prop);
		
				
	}
	
	@Test
	public void testLoadPropertyExistingProprety(){
		String prop = ProjectPluginProperties.getProperty("iet.journals.mail.default.from", null);
		
		Assert.assertEquals("ietdam@theiet.org", prop);
		
				
	}
	
}
