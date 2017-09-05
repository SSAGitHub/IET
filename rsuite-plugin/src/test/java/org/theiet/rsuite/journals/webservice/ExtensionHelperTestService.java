package org.theiet.rsuite.journals.webservice;

import java.io.File;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.theiet.rsuite.journals.JournalConstants;

import com.reallysi.rsuite.api.ContentAssembly;
import com.reallysi.rsuite.api.ManagedObject;
import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.User;
import com.reallysi.rsuite.api.remoteapi.CallArgument;
import com.reallysi.rsuite.api.remoteapi.CallArgumentList;
import com.reallysi.rsuite.api.remoteapi.DefaultRemoteApiHandler;
import com.reallysi.rsuite.api.remoteapi.RemoteApiExecutionContext;
import com.reallysi.rsuite.api.remoteapi.result.MessageDialogResult;
import com.reallysi.rsuite.service.ContentAssemblyService;
import com.rsicms.rsuite.helpers.messages.ProcessFailureMessage;
import com.rsicms.rsuite.helpers.upload.DuplicateFilenamePolicy;
import com.rsicms.rsuite.helpers.upload.RSuiteFileLoadHelper;
import com.rsicms.rsuite.helpers.upload.RSuiteFileLoadOptions;
import com.rsicms.rsuite.helpers.upload.RSuiteFileLoadResult;

public class ExtensionHelperTestService extends DefaultRemoteApiHandler implements JournalConstants {
	public MessageDialogResult execute (
			RemoteApiExecutionContext context,
			CallArgumentList args) {
		String caId = args.getFirstValue("rsuiteId");
		Log log = LogFactory.getLog(ExtensionHelperTestService.class);
		log.info("caId is " + caId);
		CallArgument fileArg = args.getFirst("fileName");
		File file = null;
		User user = context.getSession().getUser();
		RSuiteFileLoadOptions loadOpts = new RSuiteFileLoadOptions(user);
		try {
			ContentAssemblyService caSvc = context.getContentAssemblyService();
			loadOpts.setDuplicateFilenamePolicy(DuplicateFilenamePolicy.UPDATE);
			ContentAssembly ca = caSvc.getContentAssembly(user, caId);
			RSuiteFileLoadHelper.evaluteCandidateFile(context, file, loadOpts);
			ManagedObject mo = RSuiteFileLoadHelper.loadFileToCa(context, file, ca, loadOpts);
			RSuiteFileLoadResult loadResult = loadOpts.getLoadResult();
			if (loadResult.hasErrors()) {
				List<List<ProcessFailureMessage>> failList = loadResult.getFailuresByType();
				for (List<ProcessFailureMessage> typelist: failList) {
					for (ProcessFailureMessage message: typelist) {
						log.info("Failure is " + message.getMessageType() + ":" + message.getMessageText());
					}
				}
			}
		}
		catch (RSuiteException e) {
			log.info("Trapped error" + e.getMessage());
		}
		log.equals("Success");
		return new MessageDialogResult("OK", "OK");
	}
}
