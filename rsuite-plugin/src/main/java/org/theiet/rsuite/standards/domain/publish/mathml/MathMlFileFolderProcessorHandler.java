package org.theiet.rsuite.standards.domain.publish.mathml;

import java.io.File;

import org.theiet.rsuite.standards.domain.publish.datatype.FolderFilesProcessorHandler;

import com.reallysi.rsuite.api.RSuiteException;

public class MathMlFileFolderProcessorHandler implements
		FolderFilesProcessorHandler {

	private MathMlConventer mathMlDitaConventer;

	public MathMlFileFolderProcessorHandler(File imagesFolder, MathMlEntryProcessorParameters mathMlEntryProcessorParameters) {
		MathMlEntryProcessorImpl mathMlImageGenerator = 
				new MathMlEntryProcessorImpl(imagesFolder, mathMlEntryProcessorParameters);
		mathMlDitaConventer = new MathMlConventer(mathMlImageGenerator);
	}

	@Override
	public void processXMLFile(File contextFile, File outputFile)
			throws RSuiteException {
		mathMlDitaConventer.convertMathMlInXMLContent(contextFile, outputFile);

	}

	@Override
	public void processNonXMLFile(File contextFile, File outputFile) {
	}

}
