package com.rsicms.projectshelper.publish.notification;

import static com.rsicms.projectshelper.publish.workflow.constant.PublishWorkflowVariables.SEND_NOTIFICATION_CONFIGURATION_CLASS;
import static com.rsicms.projectshelper.workflow.WorkflowVariables.RSUITE_USER_ID;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;

import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.User;
import com.reallysi.rsuite.api.workflow.WorkflowExecutionContext;
import com.reallysi.rsuite.service.ManagedObjectService;
import com.rsicms.projectshelper.net.mail.MailMessage;
import com.rsicms.projectshelper.net.mail.MailUtils;
import com.rsicms.projectshelper.publish.datatype.OutputGenerationResult;
import com.rsicms.projectshelper.publish.datatype.UploadGeneratedOutputsResult;
import com.rsicms.projectshelper.utils.ProjectMoUtils;
import com.rsicms.projectshelper.utils.ProjectUserUtils;
import com.rsicms.projectshelper.workflow.WorkflowVariables;

public abstract class EmailGeneratedOutputNotifier implements GeneratedOutputNotifier {

	private static final String NEW_LINE = " \r\n";
	
    @Override
    public void sendNotification(WorkflowExecutionContext context, OutputGenerationResult generationResult, UploadGeneratedOutputsResult uploadResult) throws RSuiteException {

        Log wfLog = context.getWorkflowLog();
        User user = context.getAuthorizationService().getSystemUser();

        wfLog.info("Loading parameters for publishing email notification.");

        String userId = context.getVariable(RSUITE_USER_ID.getVariableName());
        String recipientEmailAddress = ProjectUserUtils.getUserEmail(context, userId);

        if (StringUtils.isBlank(recipientEmailAddress)) {
            wfLog.info("Skipping email notification. No email address for user " + userId);
            return;
        }

        String notificationEmailConfigurationClass =
                context.getVariable(SEND_NOTIFICATION_CONFIGURATION_CLASS.getVariableName());

        if (StringUtils.isEmpty(notificationEmailConfigurationClass)) {
            wfLog.info("Skipping email notification. No email configuration");
            return;
        }


        String emailFrom = getEmailFrom(context, user);

        String mailSubject = "";

        mailSubject = MailUtils.obtainEmailSubject(context, getEmailSubjectProperty());

        wfLog.info("Loading variables for publishing email notification.");
        Map<String, String> variables = getEmailVariables(context, user, generationResult, uploadResult);

        wfLog.info("Preparing message for publishing email notification.");
        MailMessage message = new MailMessage(emailFrom, recipientEmailAddress, mailSubject);
        message.setMessageTemplateProperty(getMessageTemplateProperty());
        message.setVariables(variables);

        wfLog.info("Sending publishing email notification.");
        MailUtils.sentEmail(context, message);

        wfLog.info("Publishing notification was successfully sent.");

    }
    
    public static String createLinkToUploadMoList (WorkflowExecutionContext context, User user, UploadGeneratedOutputsResult uploadResult)
			throws RSuiteException {

		ManagedObjectService moServ = context.getManagedObjectService();

		Set<String> sourceMoIds = uploadResult.getXmlMoIds();
		
		List<String> linksList = new ArrayList<String>();
		
		for (String sourceMoId : sourceMoIds){
			String moFileId = uploadResult.getOutputMoIdForMo(sourceMoId);
			String fullLink = generateLink(context, user, moServ, moFileId); 
			linksList.add(fullLink);
		}
		
		for (String additionalMoId : uploadResult.getAdditionalUploadedMoIds()){
			String fullLink = generateLink(context, user, moServ, additionalMoId); 
			linksList.add(fullLink);
		}

		return StringUtils.join(linksList, NEW_LINE);
	}

    private static String generateLink(WorkflowExecutionContext context,
			User user, ManagedObjectService moServ, String moFileId)
			throws RSuiteException {
		String  displayName = moServ.getContentDisplayObject(user, moFileId).getDisplayName();
		String link = buildFileLink(context, user, moFileId);
		String fullLink = displayName + " - " + link;
		return fullLink;
	}

    private static String buildFileLink (WorkflowExecutionContext context, User user, String moFileId) throws RSuiteException {	
		ManagedObjectService moServ = context.getManagedObjectService();
		String rsuiteURL = (String)context.getContextInstance().getVariables().get(WorkflowVariables.RSUITE_URL.getVariableName());
		return ProjectMoUtils.buildFileLinkToMo(moServ, user, rsuiteURL, moFileId);
	}

    protected abstract String getEmailFrom(WorkflowExecutionContext context, User user)
            throws RSuiteException;

    protected abstract Map<String, String> getEmailVariables(WorkflowExecutionContext context,
            User user, OutputGenerationResult generationResult, UploadGeneratedOutputsResult uploadResult) throws RSuiteException;

    protected abstract String getEmailSubjectProperty();

    protected abstract String getMessageTemplateProperty();
}
