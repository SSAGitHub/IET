package com.rsicms.projectshelper.publish.generators;

import java.io.File;
import java.util.Map;

import org.apache.commons.logging.Log;

import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.extensions.ExecutionContext;

public interface OutputGenerator {

	File generateOutput(File tempFolder, String moId, File inputFile, File outputFolder)
            throws RSuiteException;
	
	void initialize(ExecutionContext context, Log logger, Map<String, String> variables) throws RSuiteException;
	
	void afterGenerateOutput(File tempFolder, String moId, File inputFile, File outputFolder)
            throws RSuiteException;
	
	void beforeGenerateOutput(File tempFolder, String moId, File inputFile, File outputFolder)
            throws RSuiteException;
	}