package org.theiet.rsuite.journals.domain.article;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.theiet.rsuite.journals.domain.article.ArticleContentDetail;
import org.w3c.dom.Document;

import com.rsicms.test.TestHelper;
import com.rsicms.test.XPathEvalutorForTest;

public class ArticleContentDetailTest {

	@Test
	public void should_retrieve_first_and_last_page() throws Exception {
		XPathEvalutorForTest xpathEvaluator = new XPathEvalutorForTest();
		Document articleDocument = TestHelper.getTestDocument(getClass(), "article-content-details.xml");

		ArticleContentDetail contentDetails = new ArticleContentDetail(xpathEvaluator,
				articleDocument.getDocumentElement());
		
		assertEquals("1487", contentDetails.getFirstPage());
		assertEquals("1495", contentDetails.getLastPage());
		
	}

}
