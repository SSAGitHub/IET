package org.theiet.rsuite.journals.domain.issues.notification.typesetter;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.theiet.rsuite.journals.domain.article.Article;
import org.theiet.rsuite.journals.domain.article.ArticleContentDetail;
import org.theiet.rsuite.journals.domain.issues.datatype.Issue;
import org.theiet.rsuite.journals.domain.issues.datatype.IssueArticles;
import org.theiet.rsuite.journals.domain.issues.datatype.metadata.IssueMetadata;

import com.reallysi.rsuite.api.RSuiteException;

public class TypesetterCsvTest {

	@Test
	public void should_return_csv_with_article_details() throws RSuiteException {

		Issue issueStub = createIssueStub();

		String csv = TypesetterCsv.createPassForPressCsv(issueStub);

		String[] lines = csv.split("\n");
		assertEquals("issueDetails", lines[0]);
		assertEquals("\"INS2\";\"10-20\"", lines[1]);
		assertEquals("\"INS3\";\"30-40\"", lines[2]);
	}

	private Issue createIssueStub() throws RSuiteException {
		IssueMetadata issueMetadata = mock(IssueMetadata.class);
		when(issueMetadata.getLmdInCSV()).thenReturn("issueDetails");

		Issue issueStub = mock(Issue.class);
		when(issueStub.getIssueMetadata()).thenReturn(issueMetadata);

		IssueArticles issueArticles = createIssueArticles();
		when(issueStub.getIssueArticles()).thenReturn(issueArticles);
		return issueStub;
	}

	private IssueArticles createIssueArticles() throws RSuiteException {
		List<Article> articleList = createArticleList();

		IssueArticles issueArticles = mock(IssueArticles.class);
		when(issueArticles.getArticles()).thenReturn(articleList);
		return issueArticles;
	}

	private List<Article> createArticleList() throws RSuiteException {
		List<Article> articleList = new ArrayList<>();
		articleList.add(createArticleStub("INS2", "10", "20"));
		articleList.add(createArticleStub("INS3", "30", "40"));
		return articleList;
	}

	private Article createArticleStub(String articleId, String fpage, String lpage) throws RSuiteException {
		ArticleContentDetail contentDetail = createContentDetailStub(fpage, lpage);

		Article articleStub = mock(Article.class);
		when(articleStub.getArticleContentDetail()).thenReturn(contentDetail);
		when(articleStub.getArticleId()).thenReturn(articleId);
		return articleStub;
	}

	private ArticleContentDetail createContentDetailStub(String fpage, String lpage) throws RSuiteException {
		ArticleContentDetail contentDetail = mock(ArticleContentDetail.class);
		when(contentDetail.getFirstPage()).thenReturn(fpage);
		when(contentDetail.getLastPage()).thenReturn(lpage);
		return contentDetail;
	}

}
