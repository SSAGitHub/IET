package org.theiet.rsuite.standards.domain.publish.datatype;

import java.io.File;

import com.reallysi.rsuite.api.RSuiteException;

public interface FolderFilesProcessorHandler {

	void processXMLFile(File inputFile, File outputFile) throws RSuiteException;

	void processNonXMLFile(File inputFile, File outputFile) throws RSuiteException;

}
