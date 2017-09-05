package org.theiet.rsuite.journals.actionhandlers;

import java.util.List;

import org.apache.commons.logging.Log;
import org.theiet.rsuite.journals.domain.article.Article;
import org.theiet.rsuite.journals.domain.article.metadata.ArticleMetadata;
import org.theiet.rsuite.journals.domain.issues.datatype.Issue;
import org.theiet.rsuite.journals.domain.issues.datatype.IssueArticles;
import org.theiet.rsuite.journals.transforms.InspecClassificationMerge;
import org.theiet.rsuite.utils.ExceptionUtils;

import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.User;
import com.reallysi.rsuite.api.workflow.AbstractBaseActionHandler;
import com.reallysi.rsuite.api.workflow.WorkflowExecutionContext;
import com.rsicms.projectshelper.workflow.WorkflowVariables;

public class InspecMergeFromIssue extends AbstractBaseActionHandler {

	private static final long serialVersionUID = 1L;

	@Override
	public void execute(WorkflowExecutionContext context) throws Exception {
		Log log = context.getWorkflowLog();
		User user = getSystemUser();
		String issueCaId = context.getVariable(WorkflowVariables.RSUITE_CONTENTS.getVariableName());
		
		Issue issue = new Issue(context, user, issueCaId);
				
		log.info("execute: begin Inspec merge for issue caId " + issueCaId);
		
		IssueArticles issueArticles = issue.getIssueArticles();
				
		if (issueArticles.getArtilcesCA() == null) {
			ExceptionUtils.throwWorfklowException(context, "No Article CA in issue");
		}
		
		
		List<Article> articleList = issueArticles.getArticles();
		boolean hasError = false;
		for (Article article : articleList) {
			
			ArticleMetadata articleMetadata = article.getArticleMetadata();
			log.info("execute: process article " + article);
			
			if (!articleMetadata.isInspecRequired()) {
				log.info("execute: Inspec classification not required, skipping");
			}
			else {
				try {
					InspecClassificationMerge.mergeInspecClassification(context, article);
					log.info("execute: Inspec OK");
				}
				catch (RSuiteException e) {
					hasError = true;
					log.info("execute: error was " + e.getMessage());
				}
			}

		}
		if (hasError) {
			ExceptionUtils.throwWorfklowException(context, "One or more article failed");
		}
		
	}

}