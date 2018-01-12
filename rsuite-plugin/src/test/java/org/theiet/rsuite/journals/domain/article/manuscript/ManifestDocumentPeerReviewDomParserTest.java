package org.theiet.rsuite.journals.domain.article.manuscript;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;

import org.junit.BeforeClass;
import org.junit.Test;
import org.theiet.rsuite.journals.domain.article.manuscript.*;
import org.w3c.dom.Document;

import com.rsicms.test.TestHelper;
import com.rsicms.test.XPathEvalutorForTest;

public class ManifestDocumentPeerReviewDomParserTest {

//	@BeforeClass
//	  public static void setUpClass() {
//	    System.setProperty("javax.xml.xpath.XPathFactory:http://java.sun.com/jaxp/xpath/dom",
//	        "com.sun.org.apache.xpath.internal.jaxp.XPathFactoryImpl");
//	  }
	
	@Test
	public void parse_basic_article_information() throws Exception {
		
		XPathEvalutorForTest xpathEvaluator = new XPathEvalutorForTest();
		
		Document manifestDoc = TestHelper.getTestDocument(getClass(), "COM-2017-0090.xml");
		ManifestXPath manifestXPath = new ManifestXPath(xpathEvaluator, manifestDoc);
		ManifestDocumentPeerReviewDomParser parser = new ManifestDocumentPeerReviewDomParser(manifestXPath);
		ManifestDocument manifestDocument = parser.parseManifestDOM();
		
		
		assertThat(manifestDocument.getArticleId(), is("COM-2017-0090"));
		
	}

}
