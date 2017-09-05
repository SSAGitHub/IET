package org.theiet.rsuite.journals.domain.issues.datatype;

import java.util.Comparator;

import org.theiet.rsuite.journals.domain.article.Article;

import com.reallysi.rsuite.api.RSuiteException;

public class IsssueArticlesComparator implements Comparator<Article> {

	@Override
	public int compare(Article article1, Article article2) {
		if (article1 == null && article2 == null) {
			return 0;
		} else if (article1 == null) {
			return -1;
		} else if (article2 == null) {
			return 1;
		}

		try {
			return article1.getArticleMetadata().getExportDate()
					.compareTo(article2.getArticleMetadata().getExportDate());
		} catch (RSuiteException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

}
