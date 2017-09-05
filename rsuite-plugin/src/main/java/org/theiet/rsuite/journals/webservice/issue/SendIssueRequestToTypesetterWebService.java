package org.theiet.rsuite.journals.webservice.issue;

import java.io.*;
import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.theiet.rsuite.FTPConnectionInfoFactory;
import org.theiet.rsuite.datamodel.ExternalCompanyUser;
import org.theiet.rsuite.journals.JournalConstants;
import org.theiet.rsuite.journals.domain.issues.*;
import org.theiet.rsuite.journals.domain.issues.datatype.Issue;
import org.theiet.rsuite.journals.domain.issues.notification.typesetter.TypesetterNotification;
import org.theiet.rsuite.journals.domain.journal.Journal;
import org.theiet.rsuite.journals.utils.JournalUtils;
import org.theiet.rsuite.utils.*;

import com.reallysi.rsuite.api.*;
import com.reallysi.rsuite.api.remoteapi.*;
import com.reallysi.rsuite.api.remoteapi.result.MessageDialogResult;
import com.reallysi.rsuite.api.remoteapi.result.MessageType;
import com.reallysi.rsuite.service.ManagedObjectService;
import com.rsicms.projectshelper.utils.browse.ProjectBrowserUtils;
import com.rsicms.rsuite.helpers.download.*;
import com.rsicms.rsuite.helpers.utils.net.ftp.*;

/**
 * Custom RSuite web service to sent email
 * 
 */
public class SendIssueRequestToTypesetterWebService extends
		DefaultRemoteApiHandler implements JournalConstants {

	private static final String WEB_SERVICE_LABEL = "Sent update request";

	private static Log log = LogFactory
			.getLog(SendIssueRequestToTypesetterWebService.class);

	/**
	 * Create an MO and save all the search results as a list in the MO
	 */
	@Override
	public RemoteApiResult execute(RemoteApiExecutionContext context,
			CallArgumentList args) throws RSuiteException {
		
		ManagedObjectService moSvc = context.getManagedObjectService();

		try {
			String contextRsuiteId = args.getFirstValue(PARAM_RSUITE_ID);
			User user = context.getSession().getUser();

			Issue issue = new Issue(context, args, user);
			
			Journal journal = issue.getJournal();
			
			if (IssuePublishWorkflowChecker.isAutomatedPublishWorkflowActiveForIssue(issue)){
				return WebServiceUtils.createResultDialog(WEB_SERVICE_LABEL, "This option is not available for " + journal.getJournalCode() + " journal. The automated workflow is active.");
			}

			ContentAssembly issueArticlesCA = ProjectBrowserUtils.getChildCaByType(
					context, issue.getIssueCa(), CA_TYPE_ISSUE_ARTICLES);

			TypesetterNotification.sendRequestNotification(context, user, issue);
			
			ByteArrayInputStream inStream = createZipPackage(context, user,
					issueArticlesCA, issue);

			sendPackageToTypeSetter(context, issue, inStream, contextRsuiteId);
			
			String issueCaId = args.getFirstValue("rsuiteId");
			String submissionType = issue.getIssueCa().getLayeredMetadataValue(LMD_FIELD_TYPESETTER_UPDATE_TYPE);
			String issueCode = issue.getIssueCa().getLayeredMetadataValue(LMD_FIELD_ISSUE_CODE);
			ArrayList<MetaDataItem> items = new ArrayList<MetaDataItem>();
			items.add(new MetaDataItem(LMD_FIELD_AWAITING_TYPESETTER_UPDATES, LMD_VALUE_YES));
			if (StringUtils.isBlank(submissionType)) {
				items.add(new MetaDataItem(LMD_FIELD_TYPESETTER_UPDATE_TYPE, LMD_VALUE_INITIAL));
				PubtrackLogger.logToProcess(user, context, log, "ISSUE", issueCode, PUBTRACK_REQUESTED_TYPESETTER_INITIAL);
			}
			else {
				items.add(new MetaDataItem(LMD_FIELD_TYPESETTER_UPDATE_TYPE, LMD_VALUE_UPDATE));
				PubtrackLogger.logToProcess(user, context, log, "ISSUE", issueCode, PUBTRACK_REQUESTED_TYPESETTER_UPDATE);				
			}
			moSvc.setMetaDataEntries(context.getSession().getUser(), issueCaId, items);

			return new MessageDialogResult(
					MessageType.SUCCESS,
					WEB_SERVICE_LABEL,
					"<html><body><div><p>Request has been sent to the typesetter.</p></div></body></html>",
					"500");
		} catch (Exception e) {
			log.error("Unable to send email for review", e);
			return new MessageDialogResult(MessageType.ERROR,
					WEB_SERVICE_LABEL, "Error: " + e.getMessage());
		}

	}

	/**
	 * Send the zip package to typesetter FTP
	 * 
	 * @param context
	 *            the execution context
	 * @param issue
	 *            the issue object
	 * @param inStream
	 *            The zip package input stream
	 * @throws RSuiteException
	 * @throws FTPUtilsException
	 */
	private void sendPackageToTypeSetter(RemoteApiExecutionContext context,
			Issue issue, ByteArrayInputStream inStream, String contextRsuiteId) throws RSuiteException,
			FTPUtilsException {
		
		ExternalCompanyUser typesetterUser = JournalUtils.getTypessterUser(context, contextRsuiteId);
		
		FTPConnectionInfo connectionInfo = FTPConnectionInfoFactory
				.getFTPConnection(context, typesetterUser.getUserId());
		
		String zipFileName = FILENAME_PREFIX_ISSUE + issue.getIssueCode() + ".zip";

		String ftpFolder = FTPConnectionInfoFactory.getTargetFolder(context,
				typesetterUser.getUserId(), PROP_TYPESETTER_FTP_ISSUE_FOLDER);

		log.info("===============" + connectionInfo.getHost() + " " + zipFileName + " " + ftpFolder);
		
		FTPUtils.uploadStream(connectionInfo, inStream, zipFileName, ftpFolder);
	}

	/**
	 * Zip all NLM documents that belongs to the issue
	 * 
	 * @param context
	 * @param user
	 * @param issueArticlesCA
	 * @param issue
	 * @return
	 * @throws FileNotFoundException
	 * @throws RSuiteException
	 * @throws IOException
	 */
	private ByteArrayInputStream createZipPackage(
			RemoteApiExecutionContext context, User user,
			ContentAssembly issueArticlesCA, Issue issue)
			throws FileNotFoundException, RSuiteException, IOException {
		RSuiteObjectNameHandler nameHandler = new IETObjectNameHandler();
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		
		
		ZipHelperConfiguration zipConfiguration = new ZipHelperConfiguration();
		zipConfiguration.setObjectNameHandler(nameHandler);
		zipConfiguration.setCaItemFilter(new IssueTypeSetterFilter(context));
		ZipHelper.zipContentAssemblyContents(context, issueArticlesCA, outStream,zipConfiguration);

		ByteArrayInputStream inStream = new ByteArrayInputStream(
				outStream.toByteArray());
		return inStream;
	}

	@Override
	public void initialize(RemoteApiDefinition arg0) {
	}

}
