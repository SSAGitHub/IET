package org.theiet.rsuite.utils;

import java.io.File;
import java.io.FileInputStream;

import org.apache.commons.io.IOUtils;

import com.reallysi.rsuite.api.RSuiteException;

public class FileUtils {

	public static String loadFile (File filetoLoad) throws RSuiteException {
		try {
			return IOUtils.toString(new FileInputStream(filetoLoad), "utf-8");
		} catch (Exception ex) {
			throw new RSuiteException(RSuiteException.ERROR_NOT_DEFINED, ex.getMessage(), ex);
		}
	}
	
}
