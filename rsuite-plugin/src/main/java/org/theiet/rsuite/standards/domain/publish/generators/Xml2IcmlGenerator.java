package org.theiet.rsuite.standards.domain.publish.generators;

import static org.theiet.rsuite.standards.domain.publish.datatype.StandardsPublishWorkflowVariables.ICML_XSLT_URI;
import static org.theiet.rsuite.standards.domain.publish.datatype.StandardsPublishWorkflowVariables.MATHML_FONT;
import static org.theiet.rsuite.standards.domain.publish.datatype.StandardsPublishWorkflowVariables.MATHML_SIZE;

import java.io.*;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.theiet.rsuite.standards.StandardsBooksConstans;
import org.theiet.rsuite.standards.domain.publish.mathml.MathMlEntryProcessorParameters;
import org.theiet.rsuite.standards.domain.publish.mathml.MathMlFileFolderProcessorHandler;
import org.theiet.rsuite.standards.utils.OutputUtils;
import org.theiet.rsuite.utils.*;

import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.extensions.ExecutionContext;
import com.rsicms.projectshelper.publish.generators.OutputGenerator;

public class Xml2IcmlGenerator implements OutputGenerator,
		 StandardsBooksConstans {

	private ExecutionContext context;
	private Map<String, String> variables;
	private Log log = LogFactory.getLog(this.getClass());	
	
	@Override
	public void initialize(ExecutionContext context, Log logger,
			Map<String, String> variables) throws RSuiteException {
		this.context = context;
		this.variables = variables;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.theiet.rsuite.standards.domain.publish.OutputGenerator#generateOutPut
	 * (org.apache.commons.logging.Log, java.io.File, java.io.File,
	 * java.io.File)
	 */
	@Override
	public File generateOutput(File tempFolder, String moId, File inputFile,
			File outputDir) throws RSuiteException {

		String xslURI = variables.get(ICML_XSLT_URI.getVariableName());

		File outputFile = null;
				
		convertMathMlToImages(tempFolder);
		
		Xml2IndesingHelper.generateIndesingFromXml(context, log, inputFile,
				outputDir, xslURI, tempFolder);

		String resultFileExtension = "icml";
		File[] files = getResultFiles(outputDir, resultFileExtension);

		if (files == null || files.length == 0) {
			throw new RSuiteException("No output genereted in "
					+ outputDir.getAbsolutePath());
		}

		File mediaOutputFolder = new File(outputDir, "media");
		mediaOutputFolder.mkdirs();
		copyMediaFilesToOutputFolder(inputFile, mediaOutputFolder);

		outputFile = OutputUtils.archiveOutputFolder(tempFolder,
				FilenameUtils.getBaseName(inputFile.getName()) + "_ICML", outputDir);

		return outputFile;
	}

	private void convertMathMlToImages(File tempFolder) throws RSuiteException {
		File imagesFolder = new File(tempFolder, "export/images");
		imagesFolder.mkdirs();
		MathMlFileFolderProcessorHandler processorHandler =		        
				new MathMlFileFolderProcessorHandler(imagesFolder, createMathMlParamatersFromVariables());
		FolderFilesProcessor.processFolder(tempFolder, processorHandler);
	}
	
	private MathMlEntryProcessorParameters createMathMlParamatersFromVariables(){
	    float fontSize = Float.parseFloat(variables.get(MATHML_SIZE.getVariableName()));
        String tmpFontName = variables.get(MATHML_FONT.getVariableName());
        return new MathMlEntryProcessorParameters(tmpFontName, fontSize);
	}

	@SuppressWarnings("unchecked")
	private void copyMediaFilesToOutputFolder(File inputFile, File outputDir)
			throws RSuiteException {

		try {

			File exportFolder = getExportFolderFromInputFile(inputFile);

			Iterator<File> exportedFilesIt = FileUtils.iterateFiles(
					exportFolder, null, true);
			while (exportedFilesIt.hasNext()) {
				File file = (File) exportedFilesIt.next();
				if (RSuiteFileUtils.isNonXmlFile(file)) {
					FileUtils.copyFileToDirectory(file, outputDir, true);
				}
			}
		} catch (IOException e) {
			ExceptionUtils.throwRsuiteException(e);
		}
	}


	private File getExportFolderFromInputFile(File inputFile) {
		File exportFolder = inputFile.getParentFile();

		while (!"export".equalsIgnoreCase(exportFolder.getName())
				&& exportFolder != null) {
			exportFolder = exportFolder.getParentFile();
		}

		if (exportFolder == null) {
			exportFolder = inputFile.getParentFile();
		}

		return exportFolder;
	}

	private File[] getResultFiles(File outputDir,
			final String resultFileExtension) {
		File[] files = outputDir.listFiles(new FilenameFilter() {

			@Override
			public boolean accept(File dir, String name) {
				String extension = FilenameUtils.getExtension(name);
				if (StringUtils.isNotEmpty(extension)
						&& extension.toLowerCase()
								.endsWith(resultFileExtension)) {
					return true;
				}
				return false;
			}
		});
		return files;
	}

	@Override
	public void afterGenerateOutput(File tempFolder, String moId,
			File inputFile, File outputFolder) throws RSuiteException {
	}

	@Override
	public void beforeGenerateOutput(File tempFolder, String moId,
			File inputFile, File outputFolder) throws RSuiteException {
	}

}
