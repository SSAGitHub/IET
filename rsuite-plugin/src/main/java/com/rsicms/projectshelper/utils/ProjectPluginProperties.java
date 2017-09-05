package com.rsicms.projectshelper.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.reallysi.rsuite.api.RSuiteException;

public final class ProjectPluginProperties {

	private static Log log = LogFactory.getLog(ProjectPluginProperties.class);

	private static Properties prop;

	private ProjectPluginProperties() {
	}
	
	public static void reloadProperties() {
		prop = new Properties();
		try {
			prop.load(ProjectPluginProperties.class
					.getResourceAsStream("/plugin.properties"));
		} catch (IOException e) {			
			log.error("Unable to load properties", e);
		}

	}

	public static String getProperty(String name, String defaultValue) {
		return prop.getProperty(name, defaultValue);
	}

	public static String getPropertyTargetResource(String name,
			String defaultValue) throws RSuiteException {

		try {
			return getPropertyTargetResource(name, defaultValue, false);
		} catch (IOException e) {
			throw new RSuiteException(0, "Unable to get plugin property " + name, e);
		}
	}

	public static String getPropertyTargetResource(String name,
			String defaultValue, boolean failIfNotFound) throws IOException {

		String targetResource = getProperty(name, "");

		InputStream is = ProjectPluginProperties.class.getResourceAsStream(targetResource);

		if (is != null) {
			return IOUtils.toString(is, "UTF-8");
		}

		if (is == null && failIfNotFound) {
			throw new IOException("Unable to find resoure: " + targetResource
					+ " for property: " + name);
		}

		return defaultValue;
	}
}
