package org.theiet.rsuite.journals.domain.issues.publish.proof.upload;

import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.theiet.rsuite.journals.domain.article.Article;
import org.theiet.rsuite.journals.domain.article.metadata.ArticleMetadata;
import org.theiet.rsuite.journals.domain.issues.datatype.Issue;
import org.theiet.rsuite.journals.domain.issues.datatype.IssueArticles;
import org.theiet.rsuite.journals.domain.issues.datatype.metadata.IssueCoverDate;
import org.theiet.rsuite.journals.domain.issues.datatype.metadata.IssueCoverDateFormat;
import org.theiet.rsuite.journals.domain.issues.datatype.metadata.IssueMetadata;
import org.theiet.rsuite.journals.domain.issues.publish.common.areatree.IssueArticleInfo;
import org.theiet.rsuite.journals.domain.issues.publish.common.areatree.IssueArticlesInformation;

import com.reallysi.rsuite.api.ManagedObject;
import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.User;
import com.reallysi.rsuite.api.control.ObjectCheckInOptions;
import com.reallysi.rsuite.api.control.ObjectSource;
import com.reallysi.rsuite.api.control.ObjectUpdateOptions;
import com.reallysi.rsuite.api.control.XmlObjectSource;
import com.reallysi.rsuite.api.extensions.ExecutionContext;
import com.reallysi.rsuite.service.ManagedObjectService;
import com.rsicms.projectshelper.utils.ProjectTransformationUtils;
public class IssueArtilcesEnrichment {

	private ExecutionContext context;

	private static final String XSLT_URI_ISSUE_ARTICLE_ENRICHMENT = "rsuite:/res/plugin/iet/xslt/journals/issue/issue-article-enrichment.xsl";
	
	private User user;

	public IssueArtilcesEnrichment(ExecutionContext context, User user) {
		this.context = context;
		this.user = user;
	}

	public void enrichIssueArticlesWithIssueData(Issue issue,
			IssueArticlesInformation articlesInformation) throws RSuiteException {
		IssueArticles issueArticles = issue.getIssueArticles();

		for (Article article : issueArticles.getArticles()) {
			ArticleMetadata articleMetadata = article.getArticleMetadata();
			
			IssueArticleInfo articleInfo = articlesInformation
					.getArticleInfo(articleMetadata.getDoi());

			if (articleInfo == null) {
				throw new RSuiteException(
						"There is no issue information for article "
								+ article.getArticleId());
			}

			Map<String, String> parameters = createTransformParameters(issue, article,
					articleInfo);
			updateArticle(article, parameters);
		}
	}

	private void updateArticle(Article article, Map<String, String> parameters)
			throws RSuiteException {
		ManagedObject articleMo = article.getXMLMo();

		ByteArrayOutputStream resultStream = new ByteArrayOutputStream();
		Writer writer = new OutputStreamWriter(resultStream);

		ProjectTransformationUtils.transformMo(context.getXmlApiManager(), articleMo,
				XSLT_URI_ISSUE_ARTICLE_ENRICHMENT, writer, parameters);

		updateArticleMo(articleMo, resultStream.toByteArray());
	}

	private Map<String, String> createTransformParameters(Issue issue, Article article,
			IssueArticleInfo articleInfo) throws RSuiteException {
		Map<String, String> parameters = new HashMap<String, String>();
		
		setUpPublishDateParameters(issue, parameters);
		
		parameters.put("volume", issue.getVolume());
		parameters.put("issue", issue.getIssueNumber());

		boolean specialIssueArticle = article.getArticleMetadata().isSpecialIssue();
		parameters.put("special-issue-article", String.valueOf(specialIssueArticle));
		
		
		IssueMetadata issueMetadata = issue.getIssueMetadata();
		if (issueMetadata.isSpecialIssue() && specialIssueArticle){
			parameters.put("issue-title", issueMetadata.getIssueTitle());
		}
		
		parameters.put("firstPage", String.valueOf(articleInfo.getFirstPage()));
		parameters.put("lastPage", String.valueOf(articleInfo.getLastPage()));
		return parameters;
	}

	private void setUpPublishDateParameters(Issue issue,
			Map<String, String> parameters) throws RSuiteException {
		IssueMetadata issueMetadata = issue.getIssueMetadata();
		IssueCoverDate coverDate = issueMetadata.getCoverDate();
		Calendar date = coverDate.getCoverDate();
		
		parameters.put("year", String.valueOf(date.get(Calendar.YEAR)));
		parameters.put("month", String.valueOf(date.get(Calendar.MONTH) + 1));
		if (coverDate.getDateFormat() == IssueCoverDateFormat.FULL_DATE_FORMAT){
			parameters.put("day", String.valueOf(date.get(Calendar.DAY_OF_MONTH)));
		}
		
	}
	

	private void updateArticleMo(ManagedObject articleMo, byte[] bytes)
			throws RSuiteException {
		String articleMoId = articleMo.getId();

		ManagedObjectService moService = context.getManagedObjectService();
		moService.checkOut(user, articleMo.getId());
		ObjectSource xmlObjectSource = new XmlObjectSource(bytes);
		moService.update(user, articleMoId, xmlObjectSource,
				new ObjectUpdateOptions());
		ObjectCheckInOptions checkInOptions = new ObjectCheckInOptions();
		checkInOptions.setVersionNote("Updated issue information");
		moService.checkIn(user, articleMoId, checkInOptions);
	}
}
