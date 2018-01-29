package org.theiet.rsuite.journals.domain.journal;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.theiet.rsuite.journals.domain.article.Article;
import org.theiet.rsuite.journals.domain.article.metadata.ArticleMetadata;

import com.reallysi.rsuite.api.RSuiteException;

public class JournalAvailableArticlesTest {

	@Test
	public void test_serialization_to_json() throws RSuiteException, JSONException {
		
		JournalAvailableArticles availableArticles = new JournalAvailableArticles(getTestArticles());
		
		String jsonString = availableArticles.serializeToJSON();
		
		Assert.assertTrue(jsonString.contains("COM 1234"));
	}

	private List<Article> getTestArticles() throws RSuiteException {
		List<Article> artilces = new ArrayList<Article>();
		artilces.add(createArticleStub("COM 1234", null));
		artilces.add(createArticleStub("COM 22234", "specialTitle"));
		
		
		return artilces;
	}

	private Article createArticleStub(String articleId, String specialTitle) throws RSuiteException {
		Article articleStub = Mockito.mock(Article.class);
		Mockito.when(articleStub.getArticleId()).thenReturn(articleId);
	
		

		ArticleMetadata articleMetdataStub = Mockito.mock(ArticleMetadata.class);
		Mockito.when(articleMetdataStub.getArticleType()).thenReturn("Resarch Journal");
		Mockito.when(articleMetdataStub.getTypesetPages()).thenReturn("6");
		
		
		Mockito.when(articleMetdataStub.getSpecialIssueTitle()).thenReturn(specialTitle);
		
		
		Mockito.when(articleStub.getArticleMetadata()).thenReturn(articleMetdataStub);
		
		return articleStub;
	}



}
