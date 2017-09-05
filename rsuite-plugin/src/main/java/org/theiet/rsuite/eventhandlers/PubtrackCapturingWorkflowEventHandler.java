package org.theiet.rsuite.eventhandlers;

import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.theiet.rsuite.IetConstants;
import org.theiet.rsuite.books.BooksConstans;
import org.theiet.rsuite.utils.IetUtils;

import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.User;
import com.reallysi.rsuite.api.event.DefaultEventHandler;
import com.reallysi.rsuite.api.event.Event;
import com.reallysi.rsuite.api.event.events.TaskEventData;
import com.reallysi.rsuite.api.event.events.WorkflowEventData;
import com.reallysi.rsuite.api.extensions.ExecutionContext;
import com.reallysi.rsuite.api.pubtrack.Process;
import com.reallysi.rsuite.api.pubtrack.ProcessMetaDataItem;
import com.reallysi.rsuite.api.workflow.ProcessInstanceSummaryInfo;
import com.reallysi.rsuite.service.PubtrackManager;

/**
 * Saves relevant details about workflow to Pubtrack, reflecting IET's specific
 * workflow and tracking details. The Pubtrack data is intended to be reported
 * using the generic RSuite simple workflow reporter plugin.
 * <p>
 * NOTE: Using this because the 3.6.3 Pubtrack data collection facility doesn't
 * quite do what we need, in particular, it does provide a way to update the
 * workflow status metadata value on the Pubtrack process.
 *
 */
public class PubtrackCapturingWorkflowEventHandler extends DefaultEventHandler {
	
	public static Log log = LogFactory.getLog(PubtrackCapturingWorkflowEventHandler.class);

	/**
	 * Handles workflow and task events.
	 */
	public void handleEvent(
			ExecutionContext context, 
			Event event, 
			Object additionalEventData) {
		
		User user = context.getAuthorizationService().getSystemUser();
		if (event.getUserData() instanceof TaskEventData) {
			handleTaskEvent(context, event, user);
		} else if (event.getUserData() instanceof WorkflowEventData) {
			handleWorkflowEvent(context, event, user);
		} else {
			log.warn("Unhandled event type \"" + event.getType() + "\". Expected workflow or task event");
		}
	}

	private void handleWorkflowEvent(
			ExecutionContext context, 
			Event event,
			User user) {
		
		WorkflowEventData eventData = (WorkflowEventData)event.getUserData();
		PubtrackManager ptMgr = context.getPubtrackManager();
	
		Process proc = getProcess(context, user, eventData);
		if (proc == null) return;

		if (event.getType().equals("workflow.process.completed")) {
			try {
				ptMgr.completeProcess(
						user, 
						proc.getExternalId(), 
						Calendar.getInstance().getTime());
			} catch (RSuiteException e) {
				log.error("Unexpected exception completing Pubtrack process \"" + proc.getExternalId() + "\": " +
						"" + e.getMessage(), e);
			}
		} else if (event.getType().equals("workflow.process.aborted")) {
			try {
				Set<ProcessMetaDataItem> metadata = new HashSet<ProcessMetaDataItem>();
				IetUtils.addOrSetPubtrackProcessMetadataItem(
						IetConstants.LMD_FIELD_WORKFLOW_STATUS,
						"Workflow killed",
						metadata,
						proc);

				ptMgr.completeProcess(
						user, 
						proc.getExternalId(), 
						Calendar.getInstance().getTime());
			} catch (RSuiteException e) {
				log.error("Unexpected exception completing Pubtrack process \"" + proc.getExternalId() + "\": " +
						"" + e.getMessage(), e);
			}
		} else {
			log.warn("Unhandled event type \"" + event.getType() + "\". " +
					"Expected workflow.process.completed or workflow.process.aborted event");
			
		}
	}

	private void handleTaskEvent(
			ExecutionContext context, 
			Event event,
			User user) {

		TaskEventData eventData = (TaskEventData)event.getUserData();
		PubtrackManager ptMgr = context.getPubtrackManager();
		
	
		Process proc = getProcess(context, user, eventData);
		if (proc == null) return;
		
		if (event.getType().equals("task.assigned")) {
			try {
				ptMgr.startTask(
						user, 
						proc, 
						eventData.getTaskId(), 
						eventData.getName(), 
						Calendar.getInstance().getTime(), 
						eventData.getActorId());
			} catch (RSuiteException e) {
				log.error("Unexpected exception starting Pubtrack task: " + e.getMessage(), e);
			}
		}
		if (event.getType().equals("task.completed")) {
			try {
				ptMgr.completeTask(
						user, 
						eventData.getTaskId(), 
						Calendar.getInstance().getTime());
			} catch (RSuiteException e) {
				log.error("Unexpected exception starting Pubtrack task: " + e.getMessage(), e);
			}
		}
	}
	
	private Process getProcess(
			ExecutionContext context, 
			User user,
			WorkflowEventData eventData) {
		ProcessInstanceSummaryInfo piInfo = eventData.getProcessInstanceSummaryInfo();
		Map<String, Object> variables = eventData.getVariables();
		return getOrCreateProcess(
				context,
				user,
				piInfo,
				variables
				);
	}



	private Process getProcess(
			ExecutionContext context, 
			User user, 
			TaskEventData eventData) {
		
		ProcessInstanceSummaryInfo piInfo = eventData.getProcessInstanceSummaryInfo();
		Map<String, Object> variables = eventData.getVariables();
		return getOrCreateProcess(
				context,
				user,
				piInfo,
				variables
				);
	}

	@SuppressWarnings("unchecked")
	private Process getOrCreateProcess(
			ExecutionContext context,
			User user, 
			ProcessInstanceSummaryInfo piInfo,
			Map<String, Object> variables) {
		Process proc = null;
		PubtrackManager ptMgr = context.getPubtrackManager();
		
		String processExternalId = IetUtils.constructBookSpecificProcessExternalId(context, piInfo, variables);
		String processDefinitionName = piInfo.getProcessDefinitionName();
		String processDefinitionVersion = piInfo.getProcessDefinitionVersion();
		String productCode = (String)variables.get("productCode");
		String workflowStatus = (String)variables.get("workflowStatus");
	    Set<ProcessMetaDataItem> metadata = new HashSet<ProcessMetaDataItem>();

		try {
			
					

			List<Process> procs = ptMgr.getProcessByExternalId(
					user,
					processExternalId);
			if (procs.size() > 0) {
				proc = procs.get(0);
				if (procs.size() > 1) {
					log.warn("Found " + procs.size() + " Pubtrack processes with the external ID \"" + processExternalId + "\". " +
							"Expected at most one. Using first returned (process ID " + proc.getId() + ")");
				}
				metadata = proc.getMetaData();
			}
		} catch (RSuiteException e) {
			log.error("Unexpected exception getting Pubtrack Process: " + e.getMessage(), e);
			return null;
		}

		if (proc == null) {
			try {
				proc = ptMgr.startProcess(
						user, 
						processExternalId, 
						processDefinitionName, 
						Calendar.getInstance().getTime());
			} catch (RSuiteException e) {
				log.error("Unexpected exception starting Pubtrack Process: " + e.getMessage(), e);
				return null;
			}
			metadata = new HashSet<ProcessMetaDataItem>();

			IetUtils.addOrSetPubtrackProcessMetadataItem(
					"process-id",
					piInfo.getProcessInstanceId(),
					metadata,
					proc);
			
			IetUtils.addOrSetPubtrackProcessMetadataItem(
					"process-name",
					processDefinitionName,
					metadata,
					proc);
			
			IetUtils.addOrSetPubtrackProcessMetadataItem(
					"process-version",
					processDefinitionVersion,
					metadata,
					proc);
		}

		
		
		
		
		if (productCode != null) {
			IetUtils.addOrSetPubtrackProcessMetadataItem(
					BooksConstans.LMD_FIELD_BOOK_PRODUCT_CODE,
					productCode,
					metadata,
					proc);
		}
		
		if (workflowStatus == null) workflowStatus = "No workflow status set in workflow";

		IetUtils.addOrSetPubtrackProcessMetadataItem(
				IetConstants.LMD_FIELD_WORKFLOW_STATUS,
				workflowStatus,
				metadata,
				proc);
		
		proc.setMetaData(metadata);
		
		try {
			ptMgr.updateProcess(user, proc);
		} catch (RSuiteException e) {
			log.error("Unexpected exception updating Pubtrack Process: " + e.getMessage(), e);
		}
		return proc;
	}

}
