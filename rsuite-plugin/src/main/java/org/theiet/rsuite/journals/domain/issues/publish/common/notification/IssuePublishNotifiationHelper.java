package org.theiet.rsuite.journals.domain.issues.publish.common.notification;

import java.util.HashMap;
import java.util.Map;

import org.theiet.rsuite.journals.domain.issues.datatype.Issue;
import org.theiet.rsuite.journals.domain.issues.publish.common.IssuePublishWorkflowHelper;

import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.User;
import com.reallysi.rsuite.api.workflow.WorkflowExecutionContext;
import com.rsicms.projectshelper.publish.datatype.OutputGenerationResult;
import com.rsicms.projectshelper.publish.datatype.UploadGeneratedOutputsResult;
import com.rsicms.projectshelper.publish.notification.EmailGeneratedOutputNotifier;

public class IssuePublishNotifiationHelper {

	private IssuePublishNotifiationHelper(){
	}
	
	private final static String EMAIL_VAR_STANDARDS_DISPLAY_NAME_FILE_LINK_LIST = "display_name_file_link_list";
	
	private final static String EMAIL_VAR_ISSUE_CODE = "issue_code";
	
	
	public static Map<String, String> getEmailVariables(
			WorkflowExecutionContext context, User user, OutputGenerationResult generationResult, UploadGeneratedOutputsResult uploadResult) throws RSuiteException {
		
		Map<String, String> variables = new HashMap<String, String>();

		String linksList = EmailGeneratedOutputNotifier.createLinkToUploadMoList(context, user, uploadResult); 

		variables.put(EMAIL_VAR_STANDARDS_DISPLAY_NAME_FILE_LINK_LIST, linksList);
		
		Issue issue = IssuePublishWorkflowHelper.getIssueFromWorkflowContext(context);
		variables.put(EMAIL_VAR_ISSUE_CODE, issue.getIssueCode());		
		
		return variables;
	}
}
