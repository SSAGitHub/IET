package org.theiet.rsuite.standards.eventhandlers;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.theiet.rsuite.standards.StandardsBooksConstans;
import org.theiet.rsuite.utils.WorkflowUtils;

import com.reallysi.rsuite.api.ContentAssemblyNodeContainer;
import com.reallysi.rsuite.api.ManagedObject;
import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.User;
import com.reallysi.rsuite.api.UserType;
import com.reallysi.rsuite.api.event.DefaultEventHandler;
import com.reallysi.rsuite.api.event.Event;
import com.reallysi.rsuite.api.event.events.ContentAssemblyObjectAttachedEventData;
import com.reallysi.rsuite.api.extensions.ExecutionContext;
import com.reallysi.rsuite.api.workflow.ProcessDefinitionInfo;
import com.rsicms.rsuite.helpers.utils.RSuiteUtils;

/**
 * Monitors the object-attached event and checks the container being attached to
 * to see if it's configured as a hot container. If the attached MO is not
 * already in a workflow (_pid has a value), then starts the specified process
 * instance on the MO.
 * 
 */
public class HotContainerManagerEventHandler extends DefaultEventHandler
		implements StandardsBooksConstans {

	private static Log log = LogFactory
			.getLog(HotContainerManagerEventHandler.class);

	// workflow.process.aborted
	public void handleEvent(ExecutionContext context, Event event,
			Object eventData) {

		try {
			ContentAssemblyObjectAttachedEventData data = (ContentAssemblyObjectAttachedEventData) event
					.getUserData();
			ContentAssemblyNodeContainer assembly = data.getParent();
			String isEnabled = assembly
					.getLayeredMetadataValue(LMD_FIELD_IS_HOT_CONTAINER_ENABLED);
			if (LMD_VALUE_YES.equalsIgnoreCase(isEnabled)) {
				startWorkflowForAttachedObject(context, assembly, data);
			}
		} catch (Exception e) {
			log.error(
					e.getClass().getSimpleName() + " handling event: "
							+ e.getMessage(), e);
		}

	}

	/**
	 * Attempt to start the workflow on the attached MO, if it's not already in
	 * a workflow.
	 * 
	 * @param context
	 *            {@link ExecutionContext}
	 * @param assembly
	 *            The content assembly that was attached to.
	 * @param data
	 *            The object-attached event data.
	 * @throws RSuiteException
	 */
	protected void startWorkflowForAttachedObject(ExecutionContext context,
			ContentAssemblyNodeContainer assembly,
			ContentAssemblyObjectAttachedEventData data) throws RSuiteException {
		ManagedObject mo = data.getChild();
		String pid = mo
				.getLayeredMetadataValue(StandardsBooksConstans.LMD_FIELD_PID);
		if (pid != null && !"".equals(pid.trim())) {
			log.info("Managed object " + RSuiteUtils.formatMoId(mo)
					+ " is already associated with workflow process \"" + pid
					+ "\"");
			return;
		}

		// Not in a workflow as far as we know, try to start one.
		String processDefName = assembly
				.getLayeredMetadataValue(StandardsBooksConstans.LMD_FIELD_HOT_CONTAINER_PROCESS_DEFINITION);
		log.info("startWorkflowForAttachedObject(): Attempting to start workflow \""
				+ processDefName
				+ "\" for MO "
				+ RSuiteUtils.formatMoId(mo)
				+ "...");
		if (processDefName != null && !"".equals(processDefName.trim())) {
			ProcessDefinitionInfo pdInfo = context
					.getProcessDefinitionService().getProcessDefinitionByName(
							processDefName, -1);
			if (pdInfo == null) {
				log.error("startWorkflowForAttachedObject(): Could not find processing definition for name \""
						+ processDefName + "\"");
			}
			// OK, have a process definition, should be able to start it.
			log.info("startWorkflowForAttachedObject(): Starting workflow process...");
			Map<String, Object> variables = new HashMap<String, Object>();
			variables.put("rsuite contents", mo.getId());
			variables.put("rsuiteUserId", getUserId(data));
			variables.put("rsuiteUserFullName", data.getUser().getFullName());
			if (data.getUser().getEmail() != null) {
				variables.put("rsuiteUserEmailAddress", data.getUser()
						.getEmail());
			}
			variables.put("containerAttachedTo", assembly.getId());
			String fullFileName = mo.getDisplayName();
			String baseFileName = FilenameUtils.getBaseName(fullFileName);
			String extension = FilenameUtils.getExtension(fullFileName);
			variables.put("fullFileName", fullFileName);
			variables.put("baseFileName", baseFileName);
			variables.put("extension", extension);

			try {
				WorkflowUtils.startWorkflowForMoInHotfolder(context,
						processDefName, mo, variables);
			} catch (Exception e) {
				log.error("Unable to start workflow", e);
			}

		}

	}

    private String getUserId(ContentAssemblyObjectAttachedEventData data) {
        
        User user = data.getUser();
        String userId = user.getUserId();              
        
        if (user.getUserType() == UserType.ACTIVE_DIRECTORY && userId != null){
            userId = userId.toLowerCase();
        }
        
        return userId;
    }
}
