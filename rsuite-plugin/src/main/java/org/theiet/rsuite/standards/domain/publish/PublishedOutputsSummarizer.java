package org.theiet.rsuite.standards.domain.publish;

import java.io.File;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.theiet.rsuite.IetConstants;
import org.theiet.rsuite.standards.constans.PublishWorkflowContans;

import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.User;
import com.reallysi.rsuite.api.extensions.ExecutionContext;
import com.reallysi.rsuite.service.SessionService;
import com.reallysi.tools.dita.PluginVersionConstants;

public class PublishedOutputsSummarizer implements IetConstants, PublishWorkflowContans {

	private ExecutionContext context;
	private Map<String, Object> workflowVariables;
	private Log log;
	private Map<String, File> moOutPuts;
	private String rsuitePath;
	private boolean exportError;
	private boolean exportWarning;
	private User user;

	public PublishedOutputsSummarizer(ExecutionContext context, Map<String, Object> map,
			Log log) throws RSuiteException {
		this.context = context;
		this.workflowVariables = map;
		this.log = log;

		initVars();
	}

	@SuppressWarnings("unchecked")
	private void initVars() throws RSuiteException {
		SessionService sessionService = context.getSessionService();
		moOutPuts = (Map<String, File>) workflowVariables.get(WF_VAR_MO_OUTPUTS);

		log.info("Uploading output result "
				+ PluginVersionConstants.getVersion() + "."
				+ " Outputs to upload: " + moOutPuts.size());

		rsuitePath = (String)workflowVariables.get(WF_VAR_RSUITE_PATH);
		exportError = (Boolean)workflowVariables.get(WF_VAR_EXPORT_ERROR_FLAG);
		exportWarning = (Boolean)workflowVariables.get(WF_VAR_EXPORT_WARNING_FLAG);
		String rsuiteSessionId = (String)workflowVariables.get("rsuiteSessionId");
		user = sessionService.getSession(rsuiteSessionId).getUser();
	}

	public Map<String, File> getMoOutPuts() {
		return moOutPuts;
	}

	public String getRsuitePath() {
		return rsuitePath;
	}

	public String isExportError() {
		return exportError ? LMD_VALUE_YES : LMD_VALUE_NO;
	}

	public String isExportWarning() {
		return exportWarning ? LMD_VALUE_YES : LMD_VALUE_NO;
	}

	public String getUser() {
		return user.getFullName();
	}

}
