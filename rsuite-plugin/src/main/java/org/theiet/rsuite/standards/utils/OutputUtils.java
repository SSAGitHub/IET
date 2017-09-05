package org.theiet.rsuite.standards.utils;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.theiet.rsuite.utils.ExceptionUtils;
import org.theiet.rsuite.utils.ZipUtils;

import com.reallysi.rsuite.api.RSuiteException;


public class OutputUtils {

	public static File archiveOutputFolder(File tempFolder, String archiveName,
			File outputDir) throws RSuiteException {
		File zipOutput = new File(tempFolder,
				FilenameUtils.getBaseName(archiveName) + ".zip");

		
		try {			
			ZipUtils.zipFiles(outputDir.listFiles(), outputDir.getAbsolutePath(), zipOutput);
			FileUtils.moveFileToDirectory(zipOutput, outputDir, true);
		} catch (IOException e) {
			ExceptionUtils.throwRsuiteException(e);
		}
		return new File(outputDir, zipOutput.getName());
	}
}
