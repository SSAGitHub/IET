package org.theiet.rsuite.journals.domain.issues.notification.typesetter;

import java.util.List;

import org.theiet.rsuite.journals.domain.article.Article;
import org.theiet.rsuite.journals.domain.article.ArticleContentDetail;
import org.theiet.rsuite.journals.domain.issues.datatype.Issue;
import org.theiet.rsuite.journals.domain.issues.datatype.IssueArticles;
import org.theiet.rsuite.journals.domain.issues.datatype.metadata.IssueMetadata;

import com.reallysi.rsuite.api.RSuiteException;
import com.rsicms.projectshelper.utils.string.csv.CSVBuilder;

public class TypesetterCsv {
	
	private TypesetterCsv() {
	}

	public static String createPassForPressCsv(Issue issue) throws RSuiteException {
		IssueMetadata issueMetadata = issue.getIssueMetadata();
		StringBuilder csvContent = new StringBuilder(issueMetadata.getLmdInCSV());
		csvContent.append("\n");
		csvContent.append(createArticleDetailsCSV(issue));
		return csvContent.toString();
	}

	private static StringBuilder createArticleDetailsCSV(Issue issue) throws RSuiteException {

		IssueArticles issueArticles = issue.getIssueArticles();
		List<Article> articles = issueArticles.getArticles();

		CSVBuilder articlesCSV = new CSVBuilder();

		for (Article article : articles) {
			ArticleContentDetail contentDetail = article.getArticleContentDetail();

			articlesCSV.addValue(article.getArticleId());
			articlesCSV.addValueSeparator();
			
			String pageRange = contentDetail.getFirstPage() + "-" + contentDetail.getLastPage();
			articlesCSV.addValue(pageRange);
			articlesCSV.addNewLine();			
		}
		
		return articlesCSV.getCSVContent();
	}

}
