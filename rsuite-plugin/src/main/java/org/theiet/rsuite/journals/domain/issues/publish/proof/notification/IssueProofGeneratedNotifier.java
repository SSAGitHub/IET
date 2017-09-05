package org.theiet.rsuite.journals.domain.issues.publish.proof.notification;

import java.util.Map;

import org.theiet.rsuite.domain.mail.IetMailUtils;
import org.theiet.rsuite.journals.domain.issues.publish.common.notification.IssuePublishNotifiationHelper;

import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.User;
import com.reallysi.rsuite.api.workflow.WorkflowExecutionContext;
import com.rsicms.projectshelper.publish.datatype.OutputGenerationResult;
import com.rsicms.projectshelper.publish.datatype.UploadGeneratedOutputsResult;
import com.rsicms.projectshelper.publish.notification.EmailGeneratedOutputNotifier;

public class IssueProofGeneratedNotifier extends EmailGeneratedOutputNotifier {

	private final static String PROP_ISSUE_PROOF_SEND_PUBLISHING_MAIL_TITLE = "iet.journals.issue.proof.publish.notification.mail.title";
	
	private final static String PROP_ISSUE_PROOF_SEND_PUBLISHING_MAIL_BODY = "iet.journals.issue.proof.publish.notification.mail.body";

	@Override
	protected String getEmailSubjectProperty() {
		return PROP_ISSUE_PROOF_SEND_PUBLISHING_MAIL_TITLE;
	}

	@Override
	protected String getMessageTemplateProperty() {
		return PROP_ISSUE_PROOF_SEND_PUBLISHING_MAIL_BODY;
	}

	@Override
	protected String getEmailFrom(WorkflowExecutionContext context, User user)
			throws RSuiteException {
		return IetMailUtils.obtainEmailFrom();
	}

	@Override
	protected Map<String, String> getEmailVariables(
			WorkflowExecutionContext context, User user,
			OutputGenerationResult generationResult,
			UploadGeneratedOutputsResult uploadResult) throws RSuiteException {
		return IssuePublishNotifiationHelper.getEmailVariables(context, user, generationResult, uploadResult);
	}

}
