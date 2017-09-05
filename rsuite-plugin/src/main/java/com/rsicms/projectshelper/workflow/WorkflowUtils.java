package com.rsicms.projectshelper.workflow;

import static com.rsicms.projectshelper.workflow.WorkflowVariables.RSUITE_PATH;
import static com.rsicms.projectshelper.workflow.WorkflowVariables.RSUITE_URL;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.remoteapi.CallArgumentList;
import com.reallysi.rsuite.api.remoteapi.RemoteApiExecutionContext;
import com.reallysi.rsuite.api.workflow.WorkflowExecutionContext;
import com.rsicms.projectshelper.utils.browse.ProjectBrowserUtils;
import com.rsicms.projectshelper.webservice.ProjectWebServiceUtils;

public final class WorkflowUtils {

    private WorkflowUtils(){};
    
    private static final String ROOT_WORKFLOW_TEMP_DIR = "workflow/";
    
    private static final String REST_V1_URL_ROOT = "/rsuite/rest/v1";

    public static File getMainWorkflowFolder(WorkflowExecutionContext context) {

        if (context.getWorkflowJobContext() == null) {
            return null;
        }

        String workFolderPath = context.getWorkflowJobContext().getWorkFolderPath();

        String workflowBasePath =
                context.getConfigurationProperties().getProperty("rsuite.workflow.baseworkfolder",
                        "");

        File workFolder = new File(workFolderPath);

        File workflowFolder = workFolder.getParentFile();

        if (workFolderPath.startsWith(workflowBasePath)) {
            while (workflowFolder.getParentFile() != null
                    && !workflowFolder.getParentFile().getAbsolutePath().equals(workflowBasePath)) {
                workflowFolder = workflowFolder.getParentFile();
            }
        }

        return workflowFolder;
    }

    public static File createWorkflowTempFolder(WorkflowExecutionContext context) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd/");
        File mainTempDir = context.getRSuiteServerConfiguration().getTmpDir();

        String processFolder =
                "def_id_" + context.getProcessInstanceSummaryInfo().getProcessDefinitionId();

        String uuid = UUID.randomUUID().toString();
        File tempDir =
                new File(mainTempDir, ROOT_WORKFLOW_TEMP_DIR + processFolder + "/"
                        + sdf.format(new Date()) + uuid);
        tempDir.mkdirs();
        return tempDir;
    }
    
    public static String constructWfReportLink(String msg, String pId) {
        StringBuffer sb = new StringBuffer(msg).append(" see ")
                .append("<a href=\"")
                .append(constructWfReportURL(pId))
                .append("\" target=\"_blank\"").append(">workflow log</a>");
        return sb.toString();
    }

    public static String constructWfReportURL(String pId) {
        StringBuffer sb = new StringBuffer(REST_V1_URL_ROOT)
                .append("/workflow/process/log/")
                .append(pId)
                .append("?skey=RSUITE-SESSION-KEY");

        return sb.toString();
    }
    
    public static Map<RSuiteWorkflowVariable, Object> createCommonWorkflowVariables(RemoteApiExecutionContext context, CallArgumentList args) throws RSuiteException{
    	Map<RSuiteWorkflowVariable, Object> variables = new HashMap<RSuiteWorkflowVariable, Object>();
    	String domainURL = ProjectWebServiceUtils.extractDomainURLFromArgumentList(args);
    	
        variables.put(RSUITE_URL, domainURL);

        setUpCommonVariables(context, args, variables);
        return variables;
    }
    
    private static void setUpCommonVariables(RemoteApiExecutionContext context, CallArgumentList args,
            Map<RSuiteWorkflowVariable, Object> variables) throws RSuiteException {
        setUpRSuitePathIfNotPresentInWSArguments(context, args, variables);

    }

    private static void setUpRSuitePathIfNotPresentInWSArguments(RemoteApiExecutionContext context,
            CallArgumentList args, Map<RSuiteWorkflowVariable, Object> variables)
            throws RSuiteException {
        String rsuitePath = args.getFirstString("rsuiteBrowseUri");
        if (rsuitePath == null) {
            rsuitePath = ProjectBrowserUtils.getBrowserUri(context, args.getFirstValue("sourceId"));
        }
        variables.put(RSUITE_PATH, rsuitePath);
    }

}
