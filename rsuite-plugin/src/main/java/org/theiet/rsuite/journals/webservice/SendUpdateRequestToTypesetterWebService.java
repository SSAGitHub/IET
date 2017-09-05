package org.theiet.rsuite.journals.webservice;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.theiet.rsuite.FTPConnectionInfoFactory;
import org.theiet.rsuite.datamodel.ExternalCompanyUser;
import org.theiet.rsuite.journals.JournalConstants;
import org.theiet.rsuite.journals.utils.JournalUtils;
import org.theiet.rsuite.utils.ContentDisplayUtils;
import org.theiet.rsuite.utils.PubtrackLogger;

import com.reallysi.rsuite.api.ContentAssembly;
import com.reallysi.rsuite.api.ContentAssemblyItem;
import com.reallysi.rsuite.api.ContentAssemblyReference;
import com.reallysi.rsuite.api.ManagedObject;
import com.reallysi.rsuite.api.MetaDataItem;
import com.reallysi.rsuite.api.ObjectType;
import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.User;
import com.reallysi.rsuite.api.remoteapi.CallArgumentList;
import com.reallysi.rsuite.api.remoteapi.DefaultRemoteApiHandler;
import com.reallysi.rsuite.api.remoteapi.RemoteApiDefinition;
import com.reallysi.rsuite.api.remoteapi.RemoteApiExecutionContext;
import com.reallysi.rsuite.api.remoteapi.RemoteApiResult;
import com.reallysi.rsuite.api.remoteapi.result.MessageDialogResult;
import com.reallysi.rsuite.api.remoteapi.result.MessageType;
import com.reallysi.rsuite.api.remoteapi.result.UserInterfaceAction;
import com.reallysi.rsuite.service.ContentAssemblyService;
import com.reallysi.rsuite.service.ManagedObjectService;
import com.rsicms.projectshelper.utils.ProjectContentAssemblyUtils;
import com.rsicms.rsuite.helpers.utils.net.ftp.FTPConnectionInfo;
import com.rsicms.rsuite.helpers.utils.net.ftp.FTPUtils;

/**
 * Custom RSuite web service to sent email
 * 
 */
public class SendUpdateRequestToTypesetterWebService extends
		DefaultRemoteApiHandler implements JournalConstants {

	private static final String WEB_SERVICE_LABEL = "Sent update request";

	private static Log log = LogFactory
			.getLog(SendUpdateRequestToTypesetterWebService.class);

	/**
	 * Create an MO and save all the search results as a list in the MO
	 */
	@Override
	public RemoteApiResult execute(RemoteApiExecutionContext context,
			CallArgumentList args) throws RSuiteException {

		try {
			String contextRsuiteId = args.getFirstValue(PARAM_RSUITE_ID);
			User user = context.getSession().getUser();
			ManagedObjectService moSvc = context.getManagedObjectService();

			ContentAssemblyItem articleCa = ProjectContentAssemblyUtils.getAncestorCAbyType(
					context, contextRsuiteId, CA_TYPE_ARTICLE);

			ContentAssembly authorsCorrections = obtainCAtoSend(context, args, user);		

			ExternalCompanyUser typesetterUser = JournalUtils.getTypessterUser(context, contextRsuiteId);
			
			FTPConnectionInfo connectionInfo = FTPConnectionInfoFactory
					.getFTPConnection(context, typesetterUser.getUserId());

			ByteArrayInputStream inStream = new ByteArrayInputStream(
					ProjectContentAssemblyUtils.zipContentAssembly(context, authorsCorrections, true).toByteArray());

			String fileName = FILENAME_PREFIX_ARTICLE + articleCa.getDisplayName() + ".zip";

			String ftpFolder = FTPConnectionInfoFactory.getTargetFolder(context,
					typesetterUser.getUserId(), PROP_TYPESETTER_FTP_ARTICLE_UPDATE_FOLDER);
			
			
			FTPUtils.uploadStream(connectionInfo, inStream, fileName, ftpFolder);

			String articleId = articleCa.getDisplayName();
			String articleCaId = articleCa.getId();			
			
			ArrayList<MetaDataItem> items = new ArrayList<MetaDataItem>();

			items.add(new MetaDataItem(LMD_FIELD_AWAITING_TYPESETTER_UPDATES, LMD_VALUE_YES));
			items.add(new MetaDataItem(LMD_FIELD_TYPESETTER_UPDATE_TYPE, LMD_VALUE_UPDATE));
			moSvc.setMetaDataEntries(context.getSession().getUser(), articleCaId, items);
			PubtrackLogger.logToProcess(user, context, log, "ARTICLE", articleId, PUBTRACK_REQUESTED_TYPESETTER_UPDATE);
			
			return ContentDisplayUtils.getResultWithLabelRefreshing(
					MessageType.SUCCESS, WEB_SERVICE_LABEL, "<html><body><div><p>Update request has been sent to the typesetter.</p></div></body></html>", "500", articleCaId);
			
		} catch (Exception e) {
			log.error("Unable to send email for review", e);
			return new MessageDialogResult(MessageType.ERROR,
					WEB_SERVICE_LABEL, "Error: " + e.getMessage());
		}

	}

	private ContentAssembly obtainCAtoSend(RemoteApiExecutionContext context, CallArgumentList args,
			User user) throws RSuiteException {

		ContentAssemblyService caService = context.getContentAssemblyService();

		ManagedObject node = args.getFirstManagedObject(user);		
		
		if (node.getObjectType() == ObjectType.CONTENT_ASSEMBLY_REF) {

			ContentAssemblyItem caRef = caService.getContentAssemblyItem(user,
					node.getId());
			String caId = ((ContentAssemblyReference) caRef).getTargetId();
			return caService.getContentAssembly(user, caId);

		} else if (node.getObjectType() == ObjectType.CONTENT_ASSEMBLY) {
			return caService.getContentAssembly(user, node.getId());
		}

		// fail if CA was not found
		throw new RSuiteException("Unable to obtain the content assambly");
	}

	@Override
	public void initialize(RemoteApiDefinition arg0) {
	}

}
