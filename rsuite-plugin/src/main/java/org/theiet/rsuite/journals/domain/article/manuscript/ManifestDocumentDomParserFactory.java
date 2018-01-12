package org.theiet.rsuite.journals.domain.article.manuscript;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.xml.XPathEvaluator;

public class ManifestDocumentDomParserFactory {

	private ManifestDocumentDomParserFactory() {
	}

	public static ManifestDocumentDomParser createManifestDomParser(XPathEvaluator xpathEvaluator, Document manifestDOM) throws RSuiteException {
		Element documentElement = manifestDOM.getDocumentElement();
		String localName = documentElement.getLocalName();

		ManifestXPath manifestXpath = new ManifestXPath(xpathEvaluator, manifestDOM);
		ManifestDocumentDomParser manifestDomParser = null;

		if ("article_set".equals(localName)) {
			manifestDomParser = new ManifestDocumentS1DomParser(manifestXpath);
		} else if ("article".equals(localName)) {
			manifestDomParser = new ManifestDocumentPeerReviewDomParser(manifestXpath);
		} else {
			throw new RSuiteException("Unsupported manifest file");
		}
		return manifestDomParser;
	}
}
