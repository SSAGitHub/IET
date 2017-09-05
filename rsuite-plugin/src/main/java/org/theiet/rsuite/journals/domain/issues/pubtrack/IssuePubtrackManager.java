package org.theiet.rsuite.journals.domain.issues.pubtrack;

import org.theiet.rsuite.journals.domain.constants.JournalsEvents;
import org.theiet.rsuite.journals.domain.issues.datatype.Issue;
import org.theiet.rsuite.journals.domain.issues.datatype.metadata.IssueMetadata;
import org.theiet.rsuite.utils.PubtrackLogger;

import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.User;
import com.reallysi.rsuite.api.extensions.ExecutionContext;

public class IssuePubtrackManager {

	private static final String PUBTRACK_PRODUCT_NAME = "ISSUE";
	
	private IssuePubtrackManager(){
	}
	
	public static void completeIssueProcess(ExecutionContext context, User user, Issue issue) throws RSuiteException{
		IssueMetadata issueMetadata = issue.getIssueMetadata();
		String issueCode = issueMetadata.getIssueCode();
		PubtrackLogger.completeProcess(user, context, PUBTRACK_PRODUCT_NAME, issueCode);
		
	}
	
	public static void logSendToDigitalLibrary(ExecutionContext context, User user, Issue issue) throws RSuiteException{
		IssueMetadata issueMetadata = issue.getIssueMetadata();
		String issueCode = issueMetadata.getIssueCode();
		PubtrackLogger.logToProcess(context, user,  PUBTRACK_PRODUCT_NAME, issueCode, JournalsEvents.DELIVERED_TO_DIGITAL_LIBRARY);
	}
}
