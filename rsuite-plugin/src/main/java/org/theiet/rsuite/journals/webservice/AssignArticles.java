package org.theiet.rsuite.journals.webservice;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.theiet.rsuite.journals.domain.article.Article;
import org.theiet.rsuite.journals.domain.article.metadata.ArticleMetadataUpdater;
import org.theiet.rsuite.journals.domain.issues.datatype.Issue;
import org.theiet.rsuite.journals.domain.issues.datatype.IssueArticles;
import org.theiet.rsuite.journals.domain.journal.Journal;
import org.theiet.rsuite.journals.domain.journal.JournalArticles;
import org.theiet.rsuite.utils.ContentDisplayUtils;
import org.theiet.rsuite.utils.StringUtils;

import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.User;
import com.reallysi.rsuite.api.remoteapi.*;
import com.reallysi.rsuite.api.remoteapi.result.MessageDialogResult;
import com.reallysi.rsuite.api.remoteapi.result.MessageType;

public class AssignArticles extends DefaultRemoteApiHandler {
	
	private static final String DIALOG_TITLE = "Assign Articles to Issue";

	@Override

	public MessageDialogResult execute (
			RemoteApiExecutionContext context,
			CallArgumentList args) throws RSuiteException {
		Log log = LogFactory.getLog(AssignArticles.class);
		User user = context.getSession().getUser();

		try {			
			Issue issue = new Issue(context, args, user);
			
			String issuePublishedDate = issue.getIssueMetadata().getPrintPublishedDate();
			if (!StringUtils.isBlank(issuePublishedDate)) {
				return new MessageDialogResult(MessageType.ERROR, DIALOG_TITLE, 
						"Issue has already been published");
			}
			String issueCode = issue.getIssueCode();
			if (StringUtils.isBlank(issueCode)) {
				return new MessageDialogResult(MessageType.ERROR, DIALOG_TITLE, 
						"No issue code on LMD");
			}
			
			Journal journal = issue.getJournal();
			JournalArticles articles = journal.getArticles();
			String[] articleIds =  args.getFirstString("articleToAssing", "").split(",");
			List<String> articlesToAssign = Arrays.asList(articleIds);
			
			IssueArticles issueArticles = issue.getIssueArticles();
			
			for (String articleToAssignId : articlesToAssign){
				Article article = new Article(context, user, articleToAssignId);
				
				articles.detachArticle(article);				
				issueArticles.attachArticle(article);
				
				updateArticleMetadata(article, issueCode);
			}
			
			return ContentDisplayUtils.getResultWithLabelRefreshing(
					MessageType.SUCCESS, DIALOG_TITLE, "Articles moved", "500", journal.getJournalCa().getId());
		}
		catch (RSuiteException e) {
			log.error(e.getMessage(), e);
			return new MessageDialogResult(MessageType.ERROR, DIALOG_TITLE, 
					"Server returned " + e.getMessage());
		}
	}

	private void updateArticleMetadata(Article article, String issueCode)
			throws RSuiteException {
		ArticleMetadataUpdater articleMetadataUpdater = article.createArticleMetadataUpdater();
		articleMetadataUpdater.setAssigned(true);
		articleMetadataUpdater.setIssueCode(issueCode);
		articleMetadataUpdater.makeArticleNotAvailable();
		articleMetadataUpdater.updateMetadata();
	}
	
}
