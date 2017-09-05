package org.theiet.rsuite.onix.actionhandlers;

import java.io.File;

import org.theiet.rsuite.onix.OnixConstants;
import org.theiet.rsuite.standards.domain.publish.validator.*;
import org.theiet.rsuite.utils.*;
import org.theiet.rsuite.webservice.ErrorResourceDownloaderWebService;

import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.extensions.ExecutionContext;
import com.reallysi.rsuite.api.workflow.WorkflowExecutionContext;
import com.rsicms.projectshelper.utils.ProjectExecutionContextUtils;
import com.rsicms.projectshelper.workflow.actionhandlers.AbstractNonLeavingActionHandler;

public class StartOnixFileValidationActionHandler extends AbstractNonLeavingActionHandler implements OnixConstants {

	private static final long serialVersionUID = 1L;

	private static final String WS_RESOURCE_DOWNLOADER_REFERENCE = "/" + ErrorResourceDownloaderWebService.getRemoteApiDefinitionId();

	@Override
	public void executeTask(WorkflowExecutionContext context)
			throws Exception {
		
		File mergedOnixFile = (File)context.getVariableAsObject(WF_VAR_MERGED_FILE);
		String recipientName = context.getVariable(WF_VAR_RECIPIENT_NAME);
		String mergedOnixContet = FileUtils.loadFile(mergedOnixFile);
		OnixBookSchemaValidator onixBookSChemaValidator = new OnixBookSchemaValidator(mergedOnixContet, recipientName);
		OnixValidationResult onixValidationResult = onixBookSChemaValidator.validate();
		File resultFile = WorkflowUtils.writeWorflowTempFile(context, onixValidationResult.getFileName(), onixValidationResult.getContent());

		if (!onixValidationResult.isValidOnixFile()) {
			String errorResourceReference = createErrorResourceReference(context, context.getProcessInstanceId());
			context.setVariable(WF_VAR_ERROR_RESOURCE_REFERENCE, errorResourceReference);
		}
		context.setVariable(WF_VAR_RESULT_FILE, resultFile);
		context.setVariable(WF_VAR_VALID_ONIX_FILE, Boolean.valueOf(onixValidationResult.isValidOnixFile()));
		
	}
	
	
	private String createErrorResourceReference (ExecutionContext context, String pid) throws RSuiteException {
		String skey = ProjectExecutionContextUtils.getSkey(context);
		String errorResourceReference = "<a href='" + REST_V1_URL_ROOT + REST_API_REFERENCE + WS_RESOURCE_DOWNLOADER_REFERENCE + 
							"?skey=" + skey + 
							"&pid=" + pid + 
					         "' target='_blank'>here</a>";
		
		return errorResourceReference;
	}

}