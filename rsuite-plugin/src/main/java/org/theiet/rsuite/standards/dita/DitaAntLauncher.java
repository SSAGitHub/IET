package org.theiet.rsuite.standards.dita;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.logging.Log;
import org.theiet.rsuite.standards.utils.AntLauncher;

import com.reallysi.rsuite.api.RSuiteException;
import com.rsicms.projectshelper.system.CommandExecutionResult;

public class DitaAntLauncher implements DitaPdfAntProperties {

	public static String runDita2PDF(Log logger, String toolkitPath,
			File inputFile, File outputDir) throws RSuiteException {
		Properties props = new Properties();
		return runDita2PDF(logger, toolkitPath, inputFile, outputDir, props);
	}

	public static String runDita2PDF(Log logger, String toolkitPath,
			File inputFile, File outputDir, File processingFolder,
			Properties props) throws RSuiteException {

		props.setProperty(DITA_TEMP_DIR,
				new File(processingFolder, "temp").getAbsolutePath());
		props.setProperty(ARGS_LOGDIR,
				new File(processingFolder, "log").getAbsolutePath());

		return runDita2PDF(logger, toolkitPath, inputFile, outputDir, props);
	}

	public static String runDita2PDF(Log logger, String toolkitPath,
			File inputFile, File outputDir, Properties props)
			throws RSuiteException {

		setPropertyIfNotpresent(props, PDF_FORMATTER, "fop");
		setPropertyIfNotpresent(props, TRANSTYPE, "pdf2");
		setPropertyIfNotpresent(props, RETAIN_TOPIC_FO, "yes");
		setPropertyIfNotpresent(props, CLEAN_TEMP, "no");

		String output = runDitaBuild(logger, toolkitPath, inputFile, outputDir,
				props);

		String mapNamePart = FilenameUtils.getBaseName(inputFile.getName());
		String pdfFilename = mapNamePart + ".pdf";

		File pdfFile = new File(outputDir, pdfFilename);
		if (!pdfFile.exists()) {
			tryFixPdfFile(outputDir, mapNamePart, pdfFile, ".dita.pdf");
			tryFixPdfFile(outputDir, mapNamePart, pdfFile, ".xml.pdf");
		}

		return output;
	}

	private static void tryFixPdfFile(File outputDir, String mapNamePart,
			File pdfFile, String extension) throws RSuiteException {
		try {
			String ditaPdfFilename = mapNamePart + extension;
			File ditaPdfFile = new File(outputDir, ditaPdfFilename);

			if (ditaPdfFile.exists()) {
				FileUtils.moveFile(ditaPdfFile, pdfFile);
			}
		} catch (Exception e) {
			throw new RSuiteException(RSuiteException.ERROR_INTERNAL_ERROR,
					"unable to fix pdf file");
		}
	}

	public static String runDitaBuild(Log logger, String toolkitPath,
			File inputFile, File outputDir, Properties props)
			throws RSuiteException {

		String antHome = new File(toolkitPath, "tools/ant/").getAbsolutePath();

		List<String> classPathList = new ArrayList<String>();
		classPathList.add(new File(toolkitPath, "lib").getAbsolutePath());
		classPathList.add(new File(toolkitPath, "lib/saxon").getAbsolutePath());

		File baseFolder = inputFile.getParentFile();

		File buildFile = new File(toolkitPath, "build.xml");

		// The map or topic
		addStandardProperties(toolkitPath, inputFile, outputDir, props,
				baseFolder);

		CommandExecutionResult result = AntLauncher.executeAntBuild(logger,
				antHome, classPathList, baseFolder, buildFile, props);

		if (result.getResultCode() != 0) {
			throw new RSuiteException("Ant process hasn't finished sucessfully");
		}

		return result.getResult().toString();

	}

	private static void addStandardProperties(String toolkitPath,
			File inputFile, File outputDir, Properties props, File baseFolder) {
		setPropertyIfNotpresent(props, ARGS_INPUT, inputFile.getAbsolutePath());

		setPropertyIfNotpresent(props, OUTPUT_DIR, outputDir.getAbsolutePath());
		setPropertyIfNotpresent(props, BASEDIR_DIR,
				baseFolder.getAbsolutePath());

		setPropertyIfNotpresent(props, DITA_DIR, toolkitPath);
		setPropertyIfNotpresent(props, OUTER_CONTROL, "quiet");
		// generate.copy.outer = 2: Generate/copy all files, even those that
		// will
		// end up outside of the output directory.
		// Note that the RSuite exporter should always export the content so
		// that
		// all dependencies are below the root map, so values "1" and "2" have
		// the same effect (all files are processed and copied to the output).
		setPropertyIfNotpresent(props, GENERATE_COPY_OUTER, "2");

	}

	private static void setPropertyIfNotpresent(Properties props, String key,
			String value) {
		String oldValue = props.getProperty(key);
		if (oldValue == null) {
			props.setProperty(key, value);
		}
	}

}
