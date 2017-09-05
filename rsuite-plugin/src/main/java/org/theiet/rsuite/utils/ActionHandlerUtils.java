package org.theiet.rsuite.utils;

import java.io.*;

import org.apache.commons.io.FilenameUtils;
import org.jbpm.graph.exe.Token;

import com.rsicms.projectshelper.workflow.WorkflowUtils;
import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.workflow.*;

public class ActionHandlerUtils {

    public static void handleException(WorkflowExecutionContext executionContext, Exception e)
            throws Exception {
        createFailDetailInformation(executionContext);
        throw e;
    }

    public static void createFailDetailInformation(WorkflowExecutionContext executionContext)
            throws RSuiteException {

        StringBuilder sb = new StringBuilder();
        sb.append("Task ").append(getCurrentTaskName(executionContext)).append(" has failed: ");

        String pId = executionContext.getProcessInstanceId();
        executionContext.setVariable("failDetail",
                WorkflowUtils.constructWfReportLink(sb.toString(), pId));
    }

    public static String getCurrentTaskName(WorkflowExecutionContext executionContext) {
        String taskName = "";
        try {
            Token token = executionContext.getCurrentToken();
            taskName = token.getNode().getName();
        } catch (Exception e) {

        }

        return taskName;
    }

    public static File unzipIngestionPackageAndCreatPackgeTempFolder(
            WorkflowJobContext workflowJobContext) throws IOException, RSuiteException {
        return unzipIngestionPackage(workflowJobContext, true);
    }

    public static File unzipIngestionPackage(WorkflowJobContext workflowJobContext)
            throws IOException, RSuiteException {
        return unzipIngestionPackage(workflowJobContext, false);
    }

    public static File unzipIngestionPackage(WorkflowJobContext workflowJobContext,
            boolean createPackageSubfolder) throws IOException, RSuiteException {
        String sourceFilePath = workflowJobContext.getSourceFilePath();

        File zipFile = new File(sourceFilePath);
        File packageFolder =
                new File(workflowJobContext.getTempFolderPath(), "extracted");
                        
        if (createPackageSubfolder){
            packageFolder =
                    new File(workflowJobContext.getTempFolderPath(),
                            FilenameUtils.getBaseName(sourceFilePath));    
        }
        
        packageFolder.mkdirs();
        ZipUtils.unzip(zipFile, packageFolder);
        return packageFolder;
    }
}
