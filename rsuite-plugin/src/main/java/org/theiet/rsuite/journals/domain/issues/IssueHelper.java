package org.theiet.rsuite.journals.domain.issues;

import java.util.HashMap;
import java.util.Map;

import org.theiet.rsuite.journals.JournalConstants;
import org.theiet.rsuite.journals.domain.issues.datatype.Issue;
import org.theiet.rsuite.journals.utils.JournalMailUtils;

import com.reallysi.rsuite.api.ContentAssemblyItem;
import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.User;
import com.reallysi.rsuite.api.extensions.ExecutionContext;

public class IssueHelper implements JournalConstants {

	private IssueHelper(){
	}
	
	public static Map<String, String> setUpVariablesMap(
			ExecutionContext context, User user,
			ContentAssemblyItem journalCa, Issue issue) throws RSuiteException {

		Map<String, String> variables = new HashMap<String, String>();
		variables.put(
				JournalMailUtils.EMAIL_VAR_JOURNAL_NAME,
				journalCa.getDisplayName());
		variables
				.put(JournalMailUtils.EMAIL_VAR_EDITORIAL_ASSISTANT_NAME,
						user.getFullName());

		StringBuilder issueDetails = new StringBuilder(issue.getYear());
		issueDetails.append("/").append(issue.getVolume());
		issueDetails.append("/").append(issue.getIssueNumber());

		variables.put("IssueDetails", issueDetails.toString());

		return variables;
	}

}
