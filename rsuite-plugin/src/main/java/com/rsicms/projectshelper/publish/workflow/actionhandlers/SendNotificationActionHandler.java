package com.rsicms.projectshelper.publish.workflow.actionhandlers;

import static com.rsicms.projectshelper.publish.workflow.actionhandlers.ConfigurationFactory.createSendNotificationConfiguration;
import static com.rsicms.projectshelper.publish.workflow.constant.PublishWorkflowVariables.SEND_NOTIFICATION_CONFIGURATION_CLASS;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;

import com.reallysi.rsuite.api.workflow.WorkflowExecutionContext;
import com.rsicms.projectshelper.publish.datatype.BaseUploadGeneratedOutputsResult;
import com.rsicms.projectshelper.publish.datatype.OutputGenerationResult;
import com.rsicms.projectshelper.publish.datatype.UploadGeneratedOutputsResult;
import com.rsicms.projectshelper.publish.notification.GeneratedOutputNotifier;
import com.rsicms.projectshelper.publish.workflow.configuration.SendNotificationConfiguration;

public class SendNotificationActionHandler extends PublishWorkflowActionHandler{

	private static final long serialVersionUID = -1365577614556941434L;

	@Override
	public void executeHandler(WorkflowExecutionContext context)
			throws Exception {
		Log wfLog = context.getWorkflowLog();
		
		String notificationEmailConfigurationClass = context.getVariable(SEND_NOTIFICATION_CONFIGURATION_CLASS.getVariableName());
		
		if (StringUtils.isEmpty(notificationEmailConfigurationClass)){
		    wfLog.info("Skipping notification. No notifier configuration");
		    return;
		}
		
		SendNotificationConfiguration notificationConfiguration = createSendNotificationConfiguration(notificationEmailConfigurationClass);
		GeneratedOutputNotifier notifier = notificationConfiguration.createNotifier();
		OutputGenerationResult generationResult =  OutputGenerationResult.createFromWorkflowContext(context);
		UploadGeneratedOutputsResult uploadResult = new BaseUploadGeneratedOutputsResult(context);
		notifier.sendNotification(context, generationResult, uploadResult);
	}

	@Override
	String getWorkflowTaskName() {
		return "Send notification";
	}

}
