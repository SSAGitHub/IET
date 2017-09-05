package org.theiet.maintenance.webservice;

import org.theiet.rsuite.journals.JournalCode;
import org.theiet.rsuite.journals.JournalConstants;
import org.theiet.rsuite.journals.domain.journal.Journal;

import com.reallysi.rsuite.api.ContentAssembly;
import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.remoteapi.CallArgumentList;
import com.reallysi.rsuite.api.remoteapi.DefaultRemoteApiHandler;
import com.reallysi.rsuite.api.remoteapi.RemoteApiExecutionContext;
import com.reallysi.rsuite.api.remoteapi.RemoteApiResult;
import com.rsicms.projectshelper.utils.ProjectContentAssemblyUtils;
import com.rsicms.rsuite.helpers.webservice.XmlRemoteApiResult;

/**
 * Custom RSuite web service to save search results to user's clipboard
 * 
 */
public class Iet493 extends DefaultRemoteApiHandler {

	/**
	 * Create an MO and save all the search results as a list in the MO
	 */
	@Override
	public RemoteApiResult execute(RemoteApiExecutionContext context,
			CallArgumentList args) throws RSuiteException {

		Journal journal = new Journal(context, "JOE");
		ContentAssembly archiveCa = journal.getArchiveCa();
		ContentAssembly journalCa = journal.getJournalCa();
		
		if (archiveCa == null){
			archiveCa = ProjectContentAssemblyUtils.createContentAssembly(context, journalCa.getId(), "Archive", JournalConstants.CA_TYPE_JOURNAL_ARCHIVE);
		}
		
		journal.archiveAvailableArticles(archiveCa);
		
		return new XmlRemoteApiResult("<result>Archive container for JOE has been created</result>");

	}

}
