package org.theiet.rsuite.books.webservice;

import java.util.List;

import org.apache.commons.logging.*;
import org.theiet.rsuite.IetConstants;
import org.theiet.rsuite.books.BooksConstans;
import org.theiet.rsuite.utils.ContentDisplayUtils;

import com.reallysi.rsuite.api.*;
import com.reallysi.rsuite.api.control.*;
import com.reallysi.rsuite.api.remoteapi.*;
import com.reallysi.rsuite.api.remoteapi.result.*;
import com.reallysi.rsuite.service.*;
import com.rsicms.projectshelper.utils.ProjectContentAssemblyUtils;
import com.rsicms.projectshelper.utils.browse.ProjectBrowserUtils;

public class CreateReferenceInPrintFilesAssembly extends DefaultRemoteApiHandler 
	implements BooksConstans {
	
	private static final String DIALOG_TITLE = "Designate printer file";

	public MessageDialogResult execute (
			RemoteApiExecutionContext context,
			CallArgumentList args) {
		Log log = LogFactory.getLog(CreateReferenceInPrintFilesAssembly.class);
		User user = context.getSession().getUser();
		ContentAssemblyService caSvc = context.getContentAssemblyService();
		ManagedObjectService moSvc = context.getManagedObjectService();
		String contextRsuiteId = args.getFirstValue(PARAM_RSUITE_ID);
		if (contextRsuiteId == null) {
			return new MessageDialogResult(MessageType.ERROR, 
					DIALOG_TITLE, "Unable to get contextRsuiteId");
		}
		String moId = args.getFirstValue(IetConstants.PARAM_RSUITE_ID);
		log.info("execute: moId is " + moId);
		try {
			log.info("execute: get book ancestor");
			ContentAssembly bookCa = (ContentAssembly) ProjectContentAssemblyUtils.getAncestorCAbyType(context, contextRsuiteId, CA_TYPE_BOOK);
			ContentAssembly productionFilesCa = ProjectBrowserUtils.getChildCaByDisplayName(context, bookCa, BooksConstans.CA_NAME_PRODUCTION_FILES);
			if (productionFilesCa == null) {
				return new MessageDialogResult(MessageType.ERROR, 
						DIALOG_TITLE, "Unable to locate assembly " + 
						BooksConstans.CA_NAME_PRODUCTION_FILES);
			}
			ContentAssembly printFileCa = ProjectBrowserUtils.getChildCaByNameAndType(context, productionFilesCa, BooksConstans.CA_TYPE_PRINT_FILES, BooksConstans.CA_NAME_PRINT_FILES);
			if (printFileCa == null) {
				return new MessageDialogResult(MessageType.ERROR, 
						DIALOG_TITLE, "Unable to locate assembly " + 
						BooksConstans.CA_NAME_PRINT_FILES);
			}
			String printFileCaId = printFileCa.getId();
			List<String> moIdList = ProjectBrowserUtils.getMoIdListFromCa(log, user, moSvc, printFileCa);
			if (!moIdList.contains(moId)) {
				ObjectAttachOptions attachOpts = new ObjectAttachOptions();
//				caSvc.attach(user, printFileCaId, moId, attachOpts );
				ObjectCopyOptions copyOptions = new ObjectCopyOptions();
				copyOptions.setContentAssembly(caSvc.getContentAssembly(user, printFileCaId));
				ManagedObject newMo = moSvc.copy(user, new VersionSpecifier(moId), copyOptions);				
				caSvc.attach(user, printFileCaId, newMo, null, attachOpts);
				
				return ContentDisplayUtils.getResultWithLabelRefreshing(
						MessageType.SUCCESS, DIALOG_TITLE, "Object copied", "500", printFileCa.getId());
			}
			else {
				return ContentDisplayUtils.getResultWithLabelRefreshing(
						MessageType.INFO, DIALOG_TITLE, "Object already in print files", "500", printFileCa.getId());
			}
			
		}
		catch (RSuiteException e) {
			return new MessageDialogResult(MessageType.ERROR,
					DIALOG_TITLE,
					"Server returned " + e.getMessage());
		}
		
	}

}
