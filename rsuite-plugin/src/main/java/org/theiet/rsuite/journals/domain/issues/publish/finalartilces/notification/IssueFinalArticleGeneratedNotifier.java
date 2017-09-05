package org.theiet.rsuite.journals.domain.issues.publish.finalartilces.notification;

import java.util.Map;

import org.theiet.rsuite.domain.mail.IetMailUtils;
import org.theiet.rsuite.journals.domain.issues.datatype.Issue;
import org.theiet.rsuite.journals.domain.issues.notification.typesetter.TypesetterNotification;
import org.theiet.rsuite.journals.domain.issues.publish.common.IssuePublishWorkflowHelper;
import org.theiet.rsuite.journals.domain.issues.publish.common.notification.IssuePublishNotifiationHelper;

import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.User;
import com.reallysi.rsuite.api.workflow.WorkflowExecutionContext;
import com.rsicms.projectshelper.publish.datatype.OutputGenerationResult;
import com.rsicms.projectshelper.publish.datatype.UploadGeneratedOutputsResult;
import com.rsicms.projectshelper.publish.notification.EmailGeneratedOutputNotifier;

public class IssueFinalArticleGeneratedNotifier extends EmailGeneratedOutputNotifier {

	private final static String PROP_ISSUE_FINAL_ARTICLE_SEND_PUBLISHING_MAIL_TITLE = "iet.journals.issue.final.article.publish.notification.mail.title";
	
	private final static String PROP_ISSUE_FINAL_ARTICLE_SEND_PUBLISHING_MAIL_BODY = "iet.journals.issue.final.article.publish.notification.mail.body";

	@Override
	public void sendNotification(WorkflowExecutionContext context,
			OutputGenerationResult generationResult,
			UploadGeneratedOutputsResult uploadResult) throws RSuiteException {	
		super.sendNotification(context, generationResult, uploadResult);
		
		User user = context.getAuthorizationService().getSystemUser();
		
		Issue issue = IssuePublishWorkflowHelper.getIssueFromWorkflowContext(context);
		TypesetterNotification.sendPassForPressNotification(context, user, issue);
	}
	
	@Override
	protected String getEmailFrom(WorkflowExecutionContext context, User user)
			throws RSuiteException {
		return IetMailUtils.obtainEmailFrom();
	}

	@Override
	protected Map<String, String> getEmailVariables(
			WorkflowExecutionContext context, User user, OutputGenerationResult generationResult, UploadGeneratedOutputsResult uploadResult) throws RSuiteException {
		return IssuePublishNotifiationHelper.getEmailVariables(context, user, generationResult, uploadResult);
	}
	

	@Override
	protected String getEmailSubjectProperty() {
		return PROP_ISSUE_FINAL_ARTICLE_SEND_PUBLISHING_MAIL_TITLE;
	}

	@Override
	protected String getMessageTemplateProperty() {
		return PROP_ISSUE_FINAL_ARTICLE_SEND_PUBLISHING_MAIL_BODY;
	}

}
