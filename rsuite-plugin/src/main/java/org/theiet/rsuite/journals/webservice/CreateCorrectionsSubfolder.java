package org.theiet.rsuite.journals.webservice;

import org.theiet.rsuite.IetConstants;
import org.theiet.rsuite.journals.JournalConstants;

import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.User;
import com.reallysi.rsuite.api.control.ContentAssemblyCreateOptions;
import com.reallysi.rsuite.api.remoteapi.CallArgumentList;
import com.reallysi.rsuite.api.remoteapi.DefaultRemoteApiHandler;
import com.reallysi.rsuite.api.remoteapi.RemoteApiExecutionContext;
import com.reallysi.rsuite.api.remoteapi.result.MessageDialogResult;
import com.reallysi.rsuite.api.remoteapi.result.MessageType;
import com.reallysi.rsuite.service.ContentAssemblyService;

public class CreateCorrectionsSubfolder extends DefaultRemoteApiHandler {
	
	private static final String PARAM_NAME = "name";
	private String DIALOG_TITLE = "Create Corrections Subfolder";
	
	public MessageDialogResult execute (
			RemoteApiExecutionContext context,
			CallArgumentList args) {
		User user = context.getSession().getUser();
		ContentAssemblyService caSvc = context.getContentAssemblyService();
		ContentAssemblyCreateOptions opts = new ContentAssemblyCreateOptions();
		opts.setType(JournalConstants.CA_AUTHOR_CORRECTIONS);
		String caId = args.getFirstValue(IetConstants.PARAM_RSUITE_ID);
		String folderName = args.getFirstValue(PARAM_NAME);
		try {
			caSvc.createContentAssembly(user, caId, folderName, opts);
			return new MessageDialogResult(MessageType.SUCCESS, DIALOG_TITLE , "Container created. Please refresh view.");
		} catch (RSuiteException e) {
			return new MessageDialogResult(MessageType.ERROR, DIALOG_TITLE, "Server exception was " + e.getMessage());
		}
	}

}