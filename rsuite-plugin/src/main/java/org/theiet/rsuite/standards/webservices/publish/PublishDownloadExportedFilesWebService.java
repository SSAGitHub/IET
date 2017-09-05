package org.theiet.rsuite.standards.webservices.publish;

import java.io.File;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.theiet.rsuite.standards.StandardsBooksConstans;

import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.User;
import com.reallysi.rsuite.api.remoteapi.CallArgumentList;
import com.reallysi.rsuite.api.remoteapi.DefaultRemoteApiHandler;
import com.reallysi.rsuite.api.remoteapi.RemoteApiExecutionContext;
import com.reallysi.rsuite.api.remoteapi.RemoteApiResult;
import com.reallysi.rsuite.api.remoteapi.result.ByteSequenceResult;
import com.reallysi.rsuite.api.remoteapi.result.MessageDialogResult;
import com.reallysi.rsuite.api.remoteapi.result.MessageType;
import com.rsicms.projectshelper.publish.workflow.PublishWorkflowUtils;
import com.rsicms.projectshelper.webservice.ProjectWebServiceUtils;

public class PublishDownloadExportedFilesWebService extends
		DefaultRemoteApiHandler implements StandardsBooksConstans {

	private static Log log = LogFactory
			.getLog(PublishDownloadExportedFilesWebService.class);

	@Override
	public RemoteApiResult execute(RemoteApiExecutionContext context,
			CallArgumentList args) throws RSuiteException {

		User user = context.getSession().getUser();
		String processId = args.getFirstString("pid");

		try {
			File exportedFile = PublishWorkflowUtils
					.getExportedFilesForWorkflow(context, user, processId);

			ByteSequenceResult fileResult = ProjectWebServiceUtils.createWebserviceResultObject(exportedFile);

			return fileResult;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return new MessageDialogResult(MessageType.ERROR,
					"Download exported files", e.getMessage());
		}

	}

}
