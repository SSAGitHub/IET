package org.theiet.rsuite.journals.domain.issues.publish.common;

import static org.theiet.rsuite.journals.domain.issues.publish.common.datatype.IssuePublishWorkflowVariables.ISSUE_CA_ID;

import java.util.Map;

import org.theiet.rsuite.journals.domain.issues.datatype.Issue;

import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.extensions.ExecutionContext;
import com.reallysi.rsuite.api.workflow.WorkflowExecutionContext;

public class IssuePublishWorkflowHelper {

	private IssuePublishWorkflowHelper(){
	}
	
	public static Issue getIssueFromWorkflowContext(ExecutionContext context, 
			Map<String, String> variables) throws RSuiteException {
		
		String issueCaId = (String) variables
				.get(ISSUE_CA_ID.getVariableName());
		
		return new Issue(context, context.getAuthorizationService()
				.getSystemUser(), issueCaId);

	}
	
	public static Issue getIssueFromWorkflowContext(WorkflowExecutionContext context) throws RSuiteException {
		
		Map<String, Object> variables = context.getWorkflowVariables();
		String issueCaId = (String) variables
				.get(ISSUE_CA_ID.getVariableName());
		
		return new Issue(context, context.getAuthorizationService()
				.getSystemUser(), issueCaId);

	}
}
