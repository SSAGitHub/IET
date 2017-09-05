package org.theiet.rsuite.utils;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import javax.xml.transform.URIResolver;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;

import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.extensions.ExecutionContext;
import com.reallysi.tools.dita.conversion.InxGenerationOptions;

public class Xml2IndesingHelper {

	private Xml2IndesingHelper() {
	}
	public static File generateIndesingFromXml(ExecutionContext context, Log log,
			File inputFile, File outputFolder, String xslUri, File tempFolder ) throws RSuiteException {

		
		String baseName = FilenameUtils.getBaseName(inputFile.getName());
		
		InxGenerationOptions options = setUpGenerationOptions(context, log,
				xslUri, baseName);

		File outputFile = new File(outputFolder, baseName + "_manifest.xml");
		outputFile.getParentFile().mkdirs();
		

		TransformSupportBean transforBean = new TransformSupportBean(context,
				options.getXsltUriStr());

		Map<String, String> params = setUpParameters(outputFolder, options);

		
 	   URIResolver myResolver = TransformSupportBean.createCustomUriResolver(context, log);

 	   transforBean.setUriResolver(myResolver);
 	   transforBean.applyTransform(baseName, inputFile, outputFile, params, options, log, tempFolder);


		return outputFile;
	}

	public static Map<String, String> setUpParameters(File outputFolder,
			InxGenerationOptions options) {
		Map<String, String> params = new HashMap<String, String>();
		addParameterIfNotEmpty(params, "styleCatalogUri", options.getStyleCatalogUri());
		params.put("outputPath", outputFolder.toURI().toString());
		params.put("outdir", outputFolder.toURI().toString());
		params.put("articleFilenameBase", options.getArticleBaseName());
		return params;
	}
	
	private static void addParameterIfNotEmpty(Map<String, String> parameters, String parameterName, String value){
		if (StringUtils.isNotEmpty(value)){
			parameters.put(parameterName, value);
		}
	}

	private static InxGenerationOptions setUpGenerationOptions(
			ExecutionContext context, Log log, String xslUri, String baseName)
			throws RSuiteException {
		InxGenerationOptions options = new InxGenerationOptions();
		
		options.setIsBuildInDesignDoc(false);
		options.setUser(context.getAuthorizationService().getSystemUser());
		options.setLog(log);
		try {
			options.setXsltUri( new URI(xslUri));
			options.setXsltUriStr(xslUri);
		} catch (URISyntaxException e) {
			ExceptionUtils.throwRsuiteException(e);
		}
		
		options.setArticleBaseName(baseName);
		return options;
	}

}
