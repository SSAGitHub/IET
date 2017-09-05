package org.theiet.rsuite.journals.domain.issues.publish.proof.upload;

import static org.theiet.rsuite.journals.domain.issues.publish.common.datatype.IssuePublishWorkflowVariables.ISSUE_CA_ID;

import org.theiet.rsuite.journals.domain.issues.datatype.Issue;

import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.User;
import com.reallysi.rsuite.api.workflow.WorkflowExecutionContext;
import com.rsicms.projectshelper.publish.storage.upload.GeneratedOutputUploader;
import com.rsicms.projectshelper.publish.workflow.configuration.GeneratedOutputUploaderConfiguration;

public class IssueGeneratedOutputUploaderConfiguration implements
		GeneratedOutputUploaderConfiguration {

	@Override
	public GeneratedOutputUploader getGeneratedOutputUploader(
			WorkflowExecutionContext context, User user) throws RSuiteException {

		String workflowProcessId = context.getProcessInstanceId();

		String issueCaId = context.getVariable(ISSUE_CA_ID.getVariableName());

		Issue issue = new Issue(context, user, issueCaId);

		return new IssueGeneratedOutputUploader(context, user, context.getWorkflowLog(),
				workflowProcessId, issue);
	}

}
