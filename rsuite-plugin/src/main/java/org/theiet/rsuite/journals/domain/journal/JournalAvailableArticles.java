package org.theiet.rsuite.journals.domain.journal;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.theiet.rsuite.journals.domain.article.Article;
import org.theiet.rsuite.journals.domain.article.metadata.ArticleMetadata;

import com.reallysi.rsuite.api.RSuiteException;

public class JournalAvailableArticles {

	private List<Article> articles;

	public JournalAvailableArticles(List<Article> articles) {
		super();
		this.articles = articles;
	}

	public List<Article> getArticles() {
		return articles;
	}

	public String serializeToJSON() throws RSuiteException, JSONException {
		JSONArray array = new JSONArray();

		for (Article article : articles) {
			array.put(toJSONObject(article));
		}

		return array.toString(3);
	}

	private JSONObject toJSONObject(Article article) throws RSuiteException {
		try {
			ArticleMetadata articleMetadata = article.getArticleMetadata();
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("articleId", article.getArticleId());
			jsonObject.put("moId", article.getArticleCaId());
			jsonObject.put("articleType", articleMetadata.getArticleType());
			jsonObject.put("typesetPages", articleMetadata.getTypesetPages());
			jsonObject.put("specialTitle",
					articleMetadata.getSpecialIssueTitle());
			jsonObject.put("specialIssue",
					articleMetadata.isSpecialIssue());

			return jsonObject;
		} catch (JSONException e) {
			throw new RSuiteException(0, "Unable to serialize article "
					+ article, e);
		}
	}
}
