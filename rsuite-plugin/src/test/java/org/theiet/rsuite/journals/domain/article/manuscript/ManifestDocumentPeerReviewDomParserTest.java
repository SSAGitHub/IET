package org.theiet.rsuite.journals.domain.article.manuscript;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;

import org.junit.BeforeClass;
import org.junit.Test;
import org.theiet.rsuite.journals.domain.article.datype.ArticleAuthor;
import org.theiet.rsuite.journals.domain.article.manuscript.*;
import org.w3c.dom.Document;

import com.rsicms.test.TestHelper;
import com.rsicms.test.XPathEvalutorForTest;

public class ManifestDocumentPeerReviewDomParserTest {

	
	@Test
	public void parse_basic_article_information() throws Exception {
		
		XPathEvalutorForTest xpathEvaluator = new XPathEvalutorForTest();
		
		Document manifestDoc = TestHelper.getTestDocument(getClass(), "COM-2017-0090.xml");
		ManifestXPath manifestXPath = new ManifestXPath(xpathEvaluator, manifestDoc);
		ManifestDocumentPeerReviewDomParser parser = new ManifestDocumentPeerReviewDomParser(manifestXPath);
		ManifestDocument manifestDocument = parser.parseManifestDOM();
		
		
		assertThat(manifestDocument.getArticleId(), is("COM-2017-0090"));
		assertThat(manifestDocument.getArticleTitle(), is("Test2- RC- May 11th"));
		
		ArticleAuthor articleAuthor = new ArticleAuthor(null, "Lauren (au2)", "Lane", "lane@iet-review.rivervalleytechnologies.com");
		
		assertThat(manifestDocument.getArticleAuthor(), is(articleAuthor));
		
		
		assertThat(manifestDocument.getCategory(), is(nullValue()));
		
		assertThat(manifestDocument.getDecisionDate(), is("2017-11-03"));
		
		assertThat(manifestDocument.getManuscriptType(), is("Additional Item"));
		
	}

}
