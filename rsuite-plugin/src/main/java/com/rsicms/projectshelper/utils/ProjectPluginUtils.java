package com.rsicms.projectshelper.utils;

import java.io.InputStream;
import java.net.URI;

import javax.xml.transform.Source;
import javax.xml.transform.URIResolver;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamSource;

import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.extensions.ExecutionContext;

public class ProjectPluginUtils {

	public  static InputStream getPluginResource(ExecutionContext context, URI resourceURI)
			throws RSuiteException {

		URIResolver resolver = context.getXmlApiManager()
				.getRSuiteAwareURIResolver();
		try {
			Source baseSource = resolver.resolve(resourceURI.toString(), null);

			if (baseSource instanceof StreamSource) {
				return ((StreamSource) baseSource).getInputStream();
			}else if (baseSource instanceof SAXSource){
				return ((SAXSource)baseSource).getInputSource().getByteStream();
			}
		} catch (Exception e) {
			throw new RSuiteException(0, createErrorMessage(resourceURI), e);
		}

		throw new RSuiteException(createErrorMessage(resourceURI));
	}

	private static String createErrorMessage(URI resourceURI) {
		String errorMessage = "Unable to obtain resource for " + resourceURI;
		return errorMessage;
	}

}
