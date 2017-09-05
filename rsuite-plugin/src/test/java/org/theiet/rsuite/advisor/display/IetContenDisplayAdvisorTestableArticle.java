package org.theiet.rsuite.advisor.display;

import org.mockito.Mockito;
import org.theiet.rsuite.advisors.display.IetContentDisplayAdvisor;
import org.theiet.rsuite.journals.domain.article.Article;
import org.theiet.rsuite.journals.domain.article.metadata.ArticleMetadata;

import com.reallysi.rsuite.api.ManagedObject;
import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.User;
import com.reallysi.rsuite.api.content.ContentAdvisorContext;

public class IetContenDisplayAdvisorTestableArticle extends
		IetContentDisplayAdvisor {

	@Override
	protected Article getArticle(ContentAdvisorContext context,
			ManagedObject mo, User user) throws RSuiteException {

		ArticleMetadata articleMetadataStub = Mockito.mock(ArticleMetadata.class);
		Mockito.when(articleMetadataStub.isAvailable()).thenReturn(true);
		Article article = Mockito.mock(Article.class);
		Mockito.when(article.getArticleMetadata()).thenReturn(articleMetadataStub);
		
		return article;
	}
	
}
