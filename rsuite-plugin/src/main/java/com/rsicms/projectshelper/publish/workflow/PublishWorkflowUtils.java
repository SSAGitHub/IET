package com.rsicms.projectshelper.publish.workflow;

import java.io.*;
import java.net.URLEncoder;

import org.apache.commons.lang.StringUtils;
import org.theiet.rsuite.utils.WorkflowUtils;

import com.reallysi.rsuite.api.*;
import com.reallysi.rsuite.api.remoteapi.RemoteApiExecutionContext;
import com.reallysi.rsuite.api.workflow.ProcessInstanceInfo;
import com.reallysi.rsuite.service.ProcessInstanceService;
import com.rsicms.projectshelper.publish.workflow.constant.PublishWorkflowVariables;
import com.rsicms.rsuite.helpers.utils.ZipUtil;

public class PublishWorkflowUtils {

    private PublishWorkflowUtils() {}

    public static File getExportedFilesForWorkflow(RemoteApiExecutionContext context, User user,
            String processId) throws RSuiteException, IOException {

        ProcessInstanceService processInstanceService = context.getProcessInstanceService();

        ProcessInstanceInfo processInstance =
                processInstanceService.getProcessInstance(user, processId);

        if (processInstance == null) {
            throw new RSuiteException("There is no workflow with id " + processId);
        }
        String variableToFind = PublishWorkflowVariables.EXPORT_FOLDER.getVariableName();

        String exportFolderPath =
                WorkflowUtils.getWorkflowVariable(processInstance, variableToFind).toString();

        String errorDetail = "";

        if (StringUtils.isNotBlank(exportFolderPath)) {
            File exportFolder = new File(exportFolderPath);

            File exportedFile =
                    new File(exportFolder.getParentFile(), "exportedFiles_pid_" + processId
                            + ".zip");

            if (!exportedFile.exists()) {
                ZipUtil.zipFolder(exportFolder.getAbsolutePath(), exportedFile.getAbsolutePath());

                return exportedFile;
            } else {
                errorDetail += "File " + exportedFile.getAbsolutePath() + " doesn't exist";
            }
        } else {
            errorDetail += "Empty value for " + variableToFind;
        }

        throw new RSuiteException("There is no export folder for workflow with id " + processId
                + " " + errorDetail);

    }
    
    
    public static File getWorkflowFile(RemoteApiExecutionContext context, User user,
            String processId, String fileName) throws RSuiteException, IOException {

        ProcessInstanceService processInstanceService = context.getProcessInstanceService();

        ProcessInstanceInfo processInstance =
                processInstanceService.getProcessInstance(user, processId);

        if (processInstance == null) {
            throw new RSuiteException("There is no workflow with id " + processId);
        }
        String variableToFind = PublishWorkflowVariables.DOWNLOADS_FOLDER.getVariableName();

        String exportFolderPath =
                WorkflowUtils.getWorkflowVariable(processInstance, variableToFind).toString();

        String errorDetail = "";

        if (StringUtils.isNotBlank(exportFolderPath)) {
            File exportFolder = new File(exportFolderPath);

            File fileToDownload =
                    new File(exportFolder, fileName);

            if (fileToDownload.exists()) {
                return fileToDownload;
            } else {
                errorDetail += "File " + fileToDownload.getAbsolutePath() + " doesn't exist";
            }
        } else {
            errorDetail += "Empty value for " + variableToFind;
        }

        throw new RSuiteException("There is no export folder for workflow with id " + processId
                + " " + errorDetail);

    }
    

    public static String constructDownloadExpotedFilesLink(String pId) {
        StringBuffer sb =
                new StringBuffer("<a href=\"").append("/rsuite/rest/v1/api")
                        .append("/iet.standards.publish.download.exported.files?pid=").append(pId)
                        .append("&skey=RSUITE-SESSION-KEY\"").append(" target=\"_blank\"")
                        .append(">Link to exported files</a>");

        return sb.toString();
    };

    public static String constructDownloadFilesLink(String pId, String fileName, String linkText) throws RSuiteException {
        StringBuffer sb =
                new StringBuffer("<a href=\"").append("/rsuite/rest/v1/api")
                        .append("/projects.helper.publish.download.files?pid=").append(pId).append("&fileName=")
                        .append(createEncodedFileName(fileName))
                        .append("&skey=RSUITE-SESSION-KEY\"").append(" target=\"_blank\"")
                        .append(">" + linkText + "</a>");

        return sb.toString();
    }

    private static String createEncodedFileName(String fileName) throws RSuiteException {
        try {
            return URLEncoder.encode(fileName, "utf-8");
        } catch (UnsupportedEncodingException e) {
            throw new RSuiteException(0, e.getLocalizedMessage(), e);
        }
    };
}
