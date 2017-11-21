package org.theiet.rsuite.journals.webservice.issue;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.StringContains.containsString;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.theiet.rsuite.journals.domain.issues.IssuePublishWorkflowChecker;
import org.theiet.rsuite.journals.domain.issues.datatype.Issue;
import org.theiet.rsuite.mocks.api.remoteapi.RemoteApiExecutionContextStub;
import org.powermock.modules.junit4.PowerMockRunner;

import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.remoteapi.RemoteApiExecutionContext;
import com.reallysi.rsuite.api.remoteapi.RemoteApiResult;

@RunWith(PowerMockRunner.class)
@PrepareForTest(IssuePublishWorkflowChecker.class)
public class SendIssueFinalRequestToTypesetterWebServiceTest {

	private static RemoteApiExecutionContext executionContextStub;

	@BeforeClass
	public static void preapreTest(){
		executionContextStub = new RemoteApiExecutionContextStub();
	}

	@Test
	public void should_return_not_available_message_for_journal_automated_workflow_and_issue_with_empty_automated_workflow_lmd() throws RSuiteException, IOException {
		String issueAutomatedWorkflowLmdValue = "";
		SendIssueFinalRequestToTypesetterWebService sendRequestWS = new SendIssueFinalRequestToTypesetterWebServiceTestable(issueAutomatedWorkflowLmdValue);		

		PowerMockito.mockStatic(IssuePublishWorkflowChecker.class);
		when(IssuePublishWorkflowChecker.isAutomatedPublishWorkflowActiveForIssue(any(Issue.class))).thenReturn(true);	

		RemoteApiResult apiResult = sendRequestWS.execute(executionContextStub, null);		
		String response = IOUtils.toString(apiResult.getInputStream());
		assertThat(response, response.contains("This option is not available"));

	}

	@Test
	public void should_return_success_message_for_journal_automated_workflow_issue_with_false_automated_workflow_lmd() throws RSuiteException, IOException {
		String issueAutomatedWorkflowLmdValue = "false";
		SendIssueFinalRequestToTypesetterWebService sendRequestWS = new SendIssueFinalRequestToTypesetterWebServiceTestable(issueAutomatedWorkflowLmdValue);		
		RemoteApiResult apiResult = sendRequestWS.execute(executionContextStub, null);

		String response = IOUtils.toString(apiResult.getInputStream());
		assertThat(response, containsString("The final request has been sent to the typesetter"));		
	}

	@Test
	public void should_return_success_message_for_journal_with_no_automated_workflow() throws RSuiteException, IOException {
		String issueAutomatedWorkflowLmdValue = "";
		SendIssueFinalRequestToTypesetterWebService sendRequestWS = new SendIssueFinalRequestToTypesetterWebServiceTestable(false, issueAutomatedWorkflowLmdValue);		
		RemoteApiResult apiResult = sendRequestWS.execute(executionContextStub, null);

		String response = IOUtils.toString(apiResult.getInputStream());
		assertThat(response, containsString("The final request has been sent to the typesetter"));		
	}
}
