package org.theiet.rsuite.journals.domain.article.manuscript;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.xml.XPathEvaluator;
import com.reallysi.rsuite.service.XmlApiManager;

public class ManifestFileParser {

	public ManifestDocument parseManifestDocument(XmlApiManager xmlApiMgr, File manifestFile) throws RSuiteException {
		Document manifestDOM = parseManifestFile(xmlApiMgr, manifestFile);
		ManifestDocumentDomParser manifestDomParser = createManifestDomParser(xmlApiMgr.getXPathEvaluator(), manifestDOM);
		return manifestDomParser.parseManifestDOM();
	}

	protected ManifestDocumentDomParser createManifestDomParser(XPathEvaluator xPathEvaluator, Document manifestDOM) throws RSuiteException {
		return ManifestDocumentDomParserFactory.createManifestDomParser(xPathEvaluator, manifestDOM);
	}

	private Document parseManifestFile(XmlApiManager xmlApiMgr, File manifestFile) throws RSuiteException {
		try {
			return xmlApiMgr.getW3CDomFromFile(manifestFile, false);
		} catch (ParserConfigurationException | IOException | SAXException e) {
			throw new RSuiteException(0, "Unable to parse " + manifestFile.getAbsolutePath(), e);
		}
	}

}
