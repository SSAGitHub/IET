package org.theiet.rsuite.standards.domain.publish.generators;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.theiet.rsuite.standards.domain.publish.datatype.FolderFilesProcessorHandler;
import org.theiet.rsuite.utils.ExceptionUtils;
import org.theiet.rsuite.utils.RSuiteFileUtils;

import com.reallysi.rsuite.api.RSuiteException;

public class FolderFilesProcessor {

	private FolderFilesProcessor(){
	}
	
	public static void processFolder(File inputFolder, FolderFilesProcessorHandler processorHandler) throws RSuiteException {

		String tempFolderName = "temp_output_" + UUID.randomUUID().toString();

		File tempOutputFolder = new File(inputFolder.getParentFile(),
				tempFolderName);

		try {
			processFilesFromFolder(processorHandler, inputFolder, tempOutputFolder);

			copyOutputFilesToBaseFolder(inputFolder, tempOutputFolder);

		} catch (IOException e) {
			throw ExceptionUtils.createRsuiteException(e);
		} finally {
			cleanupTempOutputFolder(tempOutputFolder);
		}
	}


	protected static File createOutputFile(File outputFolder, String basePath,
			File contextFile) {
		String inputFolderPath = contextFile.getAbsolutePath();
		String relativePath = inputFolderPath.replace(basePath, "");
		File outputFile = new File(outputFolder, relativePath);
		outputFile.getParentFile().mkdirs();
		return outputFile;
	}

	@SuppressWarnings("unchecked")
	protected static void copyOutputFilesToBaseFolder(File inputFolder,
			File tempOutputFolder) throws IOException {
		String basePath = tempOutputFolder.getAbsolutePath();
		Iterator<File> files = FileUtils.iterateFiles(tempOutputFolder, null,
				true);
		while (files.hasNext()) {
			File file = (File) files.next();

			String inputFolderPath = file.getAbsolutePath();
			String relativePath = inputFolderPath.replace(basePath, "");
			File outputFile = new File(inputFolder, relativePath);
			
			FileUtils.copyFile(file, outputFile);
		}
	}

	private static void cleanupTempOutputFolder(File tempOutputFolder) {
		if (tempOutputFolder.exists()) {
			FileUtils.deleteQuietly(tempOutputFolder);
		}
	}
	
	private static void processFilesFromFolder(FolderFilesProcessorHandler processorHandler, File inputFolder, File outputFolder) throws RSuiteException {

		String basePath = inputFolder.getAbsolutePath();

		
		File contextFile = null;

		Iterator<File> files = FileUtils.iterateFiles(inputFolder, null, true);

		while (files.hasNext()) {
			contextFile = (File) files.next();

			File outputFile = createOutputFile(outputFolder, basePath,
					contextFile);

			if (RSuiteFileUtils.isXmlFile(contextFile)) {
				processorHandler.
				processXMLFile(contextFile, outputFile);				
			}else{
				processorHandler.processNonXMLFile(contextFile, outputFile);
			}

		}

	}
}
