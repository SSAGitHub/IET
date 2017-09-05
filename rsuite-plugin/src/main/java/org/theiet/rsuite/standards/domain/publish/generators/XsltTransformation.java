package org.theiet.rsuite.standards.domain.publish.generators;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Iterator;
import java.util.UUID;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.URIResolver;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.theiet.rsuite.utils.ExceptionUtils;
import org.theiet.rsuite.utils.RSuiteFileUtils;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.extensions.ExecutionContext;
import com.reallysi.rsuite.service.XmlApiManager;

//TODO use FolderFilesProcessor
public class XsltTransformation {

	private ExecutionContext context;

	private String xsltUri;
	
	private URIResolver uriResolver;
	
	private Log log = LogFactory.getLog(getClass());
	
	public XsltTransformation(ExecutionContext context, String xsltUri) {
		this.context = context;
		this.xsltUri = xsltUri;
	}
	
	public XsltTransformation(ExecutionContext context, URIResolver uriResolver, String xsltUri) {
		this(context, xsltUri);
		this.uriResolver = uriResolver;
	}

	public void transformFolder(File inputFolder) throws RSuiteException {
		String tempFolderName = "temp_output_" + UUID.randomUUID().toString();
		File tempOutputFolder = new File(inputFolder.getParentFile(),
				tempFolderName);

		try {
			transformFolder(inputFolder, tempOutputFolder, true);
			
			copyOutputFilesToBaseFolder(inputFolder, tempOutputFolder);
			
		} catch (IOException e) {
			ExceptionUtils.throwRsuiteException(e);
		} finally {
			cleanupTempOutputFolder(tempOutputFolder);
		}
	}

	protected void copyOutputFilesToBaseFolder(File inputFolder,
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

	private void cleanupTempOutputFolder(File tempOutputFolder) {
		if (tempOutputFolder.exists()) {
			FileUtils.deleteQuietly(tempOutputFolder);
		}
	}

	public void transformFolder(File inputFolder, File outputFolder)
			throws RSuiteException {
		transformFolder(inputFolder, outputFolder, true);
	}

	public void transformFolder(File inputFolder, File outputFolder,
			boolean copyNonXml) throws RSuiteException {

		File contextFile = null;
		
		try {
			XmlApiManager xmlApiManager = context.getXmlApiManager();
			Transformer transformer = xmlApiManager.getTransformer(new URI(
					xsltUri));
			if (uriResolver != null){
				transformer.setURIResolver(uriResolver);	
			}
			
			String basePath = inputFolder.getAbsolutePath();

			Iterator<File> files = FileUtils.iterateFiles(inputFolder, null,
					true);
			
			while (files.hasNext()) {
				contextFile = (File) files.next();

				File outputFile = createOutputFile(outputFolder, contextFile,
						basePath);

				if (RSuiteFileUtils.isXmlFile(contextFile)) {
					InputSource inSource = new InputSource(new FileInputStream(contextFile));

		           XMLReader reader = context.getXmlApiManager().getLoggingXMLReader(log, true);
			            
					
					Source source = new SAXSource(reader, inSource);
					Result result = new StreamResult(outputFile);

					transformer.transform(source, result);
				} else if (copyNonXml) {
					copyNonXmlFile(contextFile, outputFile);
				}

			}

		} catch (URISyntaxException e) {
			ExceptionUtils.throwRsuiteException(generateContextMesssage(contextFile), e);
		} catch (IOException e) {
			ExceptionUtils.throwRsuiteException(generateContextMesssage(contextFile), e);
		} catch (TransformerException e) {
			ExceptionUtils.throwRsuiteException(generateContextMesssage(contextFile), e);
		}
	}

	protected File createOutputFile(File outputFolder, File contextFile,
			String basePath) {
		String inputFolderPath = contextFile.getAbsolutePath();
		String relativePath = inputFolderPath.replace(basePath, "");
		File outputFile = new File(outputFolder, relativePath);
		outputFile.getParentFile().mkdirs();
		return outputFile;
	}
	
	private String generateContextMesssage(File contextFile){
		if (contextFile != null){
			return "Failed transformation for file " + contextFile.getAbsolutePath();
		}
		
		return "";
	}

	private void copyNonXmlFile(File file, File outputFile) throws IOException {
		FileUtils.copyFile(file, outputFile);

	}

}
