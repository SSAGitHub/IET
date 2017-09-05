package org.theiet.rsuite.standards.domain.publish.notification;

import static com.rsicms.projectshelper.publish.workflow.constant.PublishWorkflowVariables.MO_FILE_PREFIX;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.theiet.rsuite.domain.mail.IetMailUtils;
import org.theiet.rsuite.journals.JournalConstants;
import org.theiet.rsuite.utils.StringUtils;

import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.User;
import com.reallysi.rsuite.api.workflow.WorkflowExecutionContext;
import com.reallysi.rsuite.service.ManagedObjectService;
import com.rsicms.projectshelper.utils.ProjectMoUtils;
import com.rsicms.projectshelper.workflow.WorkflowVariables;

final class StandardsMailUtils implements  JournalConstants{

    private StandardsMailUtils() {
    }
    
    private final static String EMAIL_VAR_STANDARDS_MAIL_FROM = "mail_from";
    private final static String EMAIL_VAR_STANDARDS_DISPLAY_NAME_FILE_LINK_LIST = "display_name_file_link_list";
    
	public static Map<String, String> setUpVariablesMap(WorkflowExecutionContext context, User user)
			throws RSuiteException {

		Map<String, String> variables = new HashMap<String, String>();
		Map<String, Object> contextInstanceVars = context.getContextInstance().getVariables();

		String mailFrom = IetMailUtils.obtainEmailFrom();
		String linksList = listLinks(context, user, contextInstanceVars); 

		variables.put(EMAIL_VAR_STANDARDS_MAIL_FROM, mailFrom);
		variables.put(EMAIL_VAR_STANDARDS_DISPLAY_NAME_FILE_LINK_LIST, linksList);
				
		return variables;
	}


	private static String listLinks (WorkflowExecutionContext context, User user, Map<String, Object> contextInstanceVars)
			throws RSuiteException {
		
		ManagedObjectService moServ = context.getManagedObjectService();
		List<String> linksList = new ArrayList<String>();
		String separator = StringUtils.NEW_LINE_SEPARATOR;		
		for (Entry<String, Object> keyValueEntry : contextInstanceVars.entrySet()) {
			String key = keyValueEntry.getKey();
			String moFileId = null;
			String displayName = null;
			String link = null;
			if (key.contains(MO_FILE_PREFIX.getVariableName())) {
				moFileId = (String)keyValueEntry.getValue();
				displayName = moServ.getContentDisplayObject(user, moFileId).getDisplayName();
				link = buildFileLink(context, user, moFileId);
				linksList.add(displayName + " - " + link);				
			}
		}

		return StringUtils.join(separator, linksList);
	}
	
	
	
	public static String buildFileLink (WorkflowExecutionContext context, User user, String moFileId) throws RSuiteException {	
		ManagedObjectService moServ = context.getManagedObjectService();
		String rsuiteURL = (String)context.getContextInstance().getVariables().get(WorkflowVariables.RSUITE_URL.getVariableName());
		return ProjectMoUtils.buildFileLinkToMo(moServ, user, rsuiteURL, moFileId);
	}

}
