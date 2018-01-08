package org.theiet.rsuite.journals.webservice;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.theiet.rsuite.IetConstants;
import org.theiet.rsuite.journals.JournalConstants;
import org.theiet.rsuite.utils.StringUtils;

import com.reallysi.rsuite.api.ContentAssembly;
import com.reallysi.rsuite.api.MetaDataItem;
import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.User;
import com.reallysi.rsuite.api.remoteapi.CallArgumentList;
import com.reallysi.rsuite.api.remoteapi.DefaultRemoteApiHandler;
import com.reallysi.rsuite.api.remoteapi.RemoteApiExecutionContext;
import com.reallysi.rsuite.api.remoteapi.result.MessageDialogResult;
import com.reallysi.rsuite.api.remoteapi.result.MessageType;
import com.reallysi.rsuite.api.workflow.ProcessInstanceInfo;
import com.reallysi.rsuite.api.workflow.TaskInfo;
import com.reallysi.rsuite.api.workflow.TaskSummary;
import com.reallysi.rsuite.api.workflow.VariableInfo;
import com.reallysi.rsuite.service.AuthorizationService;
import com.reallysi.rsuite.service.ContentAssemblyService;
import com.reallysi.rsuite.service.ManagedObjectService;
import com.reallysi.rsuite.service.ProcessInstanceService;
import com.reallysi.rsuite.service.TaskService;

public class ReassignEa extends DefaultRemoteApiHandler {
	
	private static final String DIALOG_TITLE = "Reassign Editorial Assistant";
	private static final String NEW_EA_USER_ID = "eauserid";
	private static Log log = LogFactory.getLog(ReassignEa.class);
	
	@SuppressWarnings("unchecked")
	public MessageDialogResult execute (
			RemoteApiExecutionContext context,
			CallArgumentList args) {
		User user = context.getSession().getUser();
		ContentAssemblyService caSvc = context.getContentAssemblyService();
		ManagedObjectService moSvc = context.getManagedObjectService();
		AuthorizationService authSvc = context.getAuthorizationService();
		TaskService taskSvc = context.getTaskService();
		ProcessInstanceService piSvc = context.getProcessInstanceService();
		String journalCaId = args.getFirstValue(IetConstants.PARAM_RSUITE_ID);
		String newEaUserId = args.getFirstValue(NEW_EA_USER_ID);
		try {
			ContentAssembly journalCa = caSvc.getContentAssembly(user, journalCaId);
			String currentEaUserId = journalCa.getLayeredMetadataValue(JournalConstants.LMD_FIELD_EDITORIAL_ASSISTANT);
			String journalCode = journalCa.getLayeredMetadataValue(JournalConstants.LMD_FIELD_JOURNAL_CODE);
			log.info("execute: reassigning EA for journal " + journalCode);
			if (StringUtils.isBlank(currentEaUserId)) {
				return new MessageDialogResult(MessageType.WARNING, DIALOG_TITLE, "No editorial assistant assigned to this journal");
			}
			if (StringUtils.isBlank(journalCode)) {
				return new MessageDialogResult(MessageType.WARNING, DIALOG_TITLE, "No journal code set");
			}
			
			User currentEaUser = authSvc.findUser(currentEaUserId);
			if (currentEaUser == null) {
				return new MessageDialogResult(MessageType.ERROR, DIALOG_TITLE, "Unable to find user " + currentEaUserId);
			}
			if  (newEaUserId.equals(currentEaUserId)) {
				return new MessageDialogResult(MessageType.WARNING, DIALOG_TITLE, "New EA is same as current");
			}
			
			User newEaUser = authSvc.findUser(newEaUserId);
			if (newEaUser == null) {
				return new MessageDialogResult(MessageType.ERROR, DIALOG_TITLE, "Unable to find user " + newEaUserId);
			}
			int taskCount = 0;
			int reassignCount = 0;
			List<TaskSummary> openTasks = taskSvc.getToDoList(user, currentEaUserId);
			for (TaskSummary taskSummary : openTasks) {
				taskCount++;
				String taskId = taskSummary.getTaskId();
				log.info("execute: check taskId " + taskId);
				TaskInfo taskInfo = taskSvc.getTaskInstance(user, taskId);
				String pId = taskInfo.getProcessInstanceId();
				ProcessInstanceInfo piInfo = piSvc.getProcessInstance(user, pId);
				List <VariableInfo> vars = piInfo.getVariables();
				String journalId = new String();
				for (VariableInfo var : vars) {
					String name = var.getName();
					log.info(name + ":" + var.getValue());
					if (name.equals(JournalConstants.WF_VAR_JOURNAL_ID)) {
						journalId = var.getValue().toString();
					}
				}
				if (!StringUtils.isBlank(journalId)) {
					if (journalId.equals(journalCode)) {
					log.info("execute: reassign taskId " + taskId);
					taskSvc.assignUserToTask(user, newEaUserId, taskId);
					reassignCount++;
					}
				}
			}
			moSvc.setMetaDataEntry(user, journalCaId, new MetaDataItem(JournalConstants.LMD_FIELD_EDITORIAL_ASSISTANT, newEaUserId));
			return new MessageDialogResult(MessageType.SUCCESS, DIALOG_TITLE, "Reassigned " +
					String.valueOf(reassignCount) + " task(s) out of " + taskCount);
		} catch (RSuiteException e) {
			return new MessageDialogResult(MessageType.ERROR, DIALOG_TITLE, "Server error was " + e.getMessage());
		}
	}
	
}