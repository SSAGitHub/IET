package org.theiet.rsuite.journals.webservice.journal;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.theiet.rsuite.journals.JournalConstants;
import org.theiet.rsuite.journals.domain.issues.datatype.Issue;
import org.theiet.rsuite.journals.domain.journal.Journal;
import org.theiet.rsuite.journals.domain.journal.JournalAvailableArticles;

import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.User;
import com.reallysi.rsuite.api.remoteapi.CallArgumentList;
import com.reallysi.rsuite.api.remoteapi.DefaultRemoteApiHandler;
import com.reallysi.rsuite.api.remoteapi.RemoteApiDefinition;
import com.reallysi.rsuite.api.remoteapi.RemoteApiExecutionContext;
import com.reallysi.rsuite.api.remoteapi.RemoteApiResult;
import com.reallysi.rsuite.api.remoteapi.result.MessageDialogResult;
import com.reallysi.rsuite.api.remoteapi.result.MessageType;
import com.rsicms.projectshelper.webservice.result.JSONRemoteApiResult;

/**
 * Custom RSuite web service to sent email
 * 
 */
public class JournalAvailableArticlesWebService extends DefaultRemoteApiHandler implements
		JournalConstants {

	private static final String WEB_SERVICE_LABEL = "Issue Available Articles";

	private static Log log = LogFactory.getLog(JournalAvailableArticlesWebService.class);

	/**
	 * Create an MO and save all the search results as a list in the MO
	 */
	@Override
	public RemoteApiResult execute(RemoteApiExecutionContext context,
			CallArgumentList args) throws RSuiteException {

		try {
			User user = context.getSession().getUser();

			Issue issue = new Issue(context, args, user);
			Journal journal = issue.getJournal();
			JournalAvailableArticles availableArticles = journal.getAvailableArticles();
			
			return new JSONRemoteApiResult(availableArticles.serializeToJSON());
			
		} catch (Exception e) {
			log.error("Unable to create issue", e);
			return new MessageDialogResult(MessageType.ERROR,
					WEB_SERVICE_LABEL, "Error: " + e.getMessage());
		}

	}

	@Override
	public void initialize(RemoteApiDefinition arg0) {
	}

}
