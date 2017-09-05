package org.theiet.rsuite.webservice;

import java.io.File;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.theiet.rsuite.onix.OnixConstants;

import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.User;
import com.reallysi.rsuite.api.remoteapi.CallArgumentList;
import com.reallysi.rsuite.api.remoteapi.DefaultRemoteApiHandler;
import com.reallysi.rsuite.api.remoteapi.RemoteApiExecutionContext;
import com.reallysi.rsuite.api.remoteapi.RemoteApiResult;
import com.reallysi.rsuite.api.remoteapi.result.ByteSequenceResult;
import com.reallysi.rsuite.api.remoteapi.result.MessageDialogResult;
import com.reallysi.rsuite.api.remoteapi.result.MessageType;
import com.reallysi.rsuite.api.workflow.ProcessInstanceInfo;
import com.reallysi.rsuite.api.workflow.VariableInfo;
import com.reallysi.rsuite.service.ProcessInstanceService;

public class ErrorResourceDownloaderWebService extends DefaultRemoteApiHandler implements OnixConstants {

	private static Log log = LogFactory
			.getLog(ErrorResourceDownloaderWebService.class); 
	
	private static final String remoteApiDefinitionId = "iet.remote.error.resource.downloader";
	
	
	public RemoteApiResult execute(RemoteApiExecutionContext context,
			CallArgumentList args) throws RSuiteException {
		RemoteApiResult result = null;

		User user = context.getAuthorizationService().getSystemUser();
		String pid= args.getFirstValue("pid");
		
		ProcessInstanceService piSvc = context.getProcessInstanceService();
		ProcessInstanceInfo processInstance = piSvc.getProcessInstance(user, pid);
	
		List<VariableInfo> variables = processInstance.getVariables(); 

		String filePath = null;
		for (VariableInfo variable : variables){
			if (WF_VAR_RESULT_FILE.equals(variable.getName())){
				filePath = variable.getValue().toString();
				break;
			}
		}
		
		if (filePath == null){
			throw new RSuiteException("There is no result file for the workflow");
		}
		
		File file = new File(filePath);
		try {
			ByteSequenceResult fileResult = new ByteSequenceResult(FileUtils.readFileToByteArray(file));
			result = fileResult;
			fileResult.setSuggestedFileName(file.getName());
			fileResult.setContentType("application/zip");
		} catch (Exception ex) {
			log.error(ex.getLocalizedMessage(), ex);
			return new MessageDialogResult(MessageType.ERROR,
					"Resource Downloader", "Server returned: " + ex.getMessage());

		}
		
		return result;
	}
	
	public static String getRemoteApiDefinitionId () {
		return remoteApiDefinitionId;
	}

}
