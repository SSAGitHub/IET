package org.theiet.rsuite.books.webservice;

import java.util.*;

import org.apache.commons.logging.*;
import org.theiet.rsuite.IetConstants;
import org.theiet.rsuite.books.BooksConstans;

import com.reallysi.rsuite.api.*;
import com.reallysi.rsuite.api.control.ObjectAttachOptions;
import com.reallysi.rsuite.api.remoteapi.*;
import com.reallysi.rsuite.api.remoteapi.result.*;
import com.reallysi.rsuite.service.*;
import com.rsicms.projectshelper.utils.ProjectContentAssemblyUtils;
import com.rsicms.projectshelper.utils.browse.ProjectBrowserUtils;

public class CreateReferenceInPrintFilesAssemblyCa extends DefaultRemoteApiHandler 
implements BooksConstans {
	
	private static final String DIALOG_TITLE = "Designate printer file";
	
	public MessageDialogResult execute (
			RemoteApiExecutionContext context,
			CallArgumentList args) {
		Log log = LogFactory.getLog(CreateReferenceInPrintFilesAssemblyCa.class);
		ManagedObjectService moSvc = context.getManagedObjectService();
		ContentAssemblyService caSvc = context.getContentAssemblyService();
		User user = context.getSession().getUser();
		String finalFilesCaId = args.getFirstValue(IetConstants.PARAM_RSUITE_ID);
		try {
			if (finalFilesCaId == null) {
				return new MessageDialogResult(MessageType.ERROR, 
						DIALOG_TITLE, "Unable to get contextRsuiteId");
			}
			ContentAssembly finalFilesCa = caSvc.getContentAssembly(user, finalFilesCaId);

			ContentAssembly bookCa = (ContentAssembly) ProjectContentAssemblyUtils.getAncestorCAbyType(context, finalFilesCaId, CA_TYPE_BOOK);
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
			
			List<String> printFileList = ProjectBrowserUtils.getMoIdListFromCa(log, user, moSvc, printFileCa);
			List<? extends ContentAssemblyItem> childList = finalFilesCa.getChildrenObjects();
			ArrayList<ManagedObject>moList = new ArrayList<ManagedObject>();
			log.info("execute: Got child list for final files ca " + finalFilesCaId + " size " + childList.size());
			int count = 0;
			for (ContentAssemblyItem child : childList) {
				if (child instanceof ManagedObjectReference) {
					ManagedObjectReference moRef = (ManagedObjectReference)child;
					String moId = moRef.getTargetId();
					if (!printFileList.contains(moId)) {
						moList.add(moSvc.getManagedObject(user, moId));
						log.info("execute: add " + moId);
						count++;
					}
					
				}
			}

			String printFileCaId = printFileCa.getId();
			log.info("execute: copy to print file ca " + printFileCaId);
			ObjectAttachOptions attachOpts = new ObjectAttachOptions();
			caSvc.attach(user, printFileCaId, moList, attachOpts);
			return new MessageDialogResult(DIALOG_TITLE, String.valueOf(count) + " object(s) copied");
		}
		catch (RSuiteException e) {
			return new MessageDialogResult(MessageType.ERROR,
					DIALOG_TITLE,
					"Server returned " + e.getMessage());
		}
	}

}
