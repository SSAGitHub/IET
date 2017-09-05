package com.rsicms.projectshelper.publish.workflow.generators;

import java.io.*;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;

import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.extensions.ExecutionContext;
import com.reallysi.rsuite.api.workflow.WorkflowExecutionContext;
import com.rsicms.projectshelper.publish.generators.pdf.AntennaHouseAreaTreePdfOutputGenerator;
import com.rsicms.projectshelper.publish.workflow.PublishWorkflowUtils;

public abstract class WorkflowAntennaHouseAreaTreePdfOutputGenerator extends
        AntennaHouseAreaTreePdfOutputGenerator {

    private WorkflowExecutionContext context;

    private Log logger;

    @Override
    public void initialize(ExecutionContext context, Log logger, Map<String, String> variables)
            throws RSuiteException {
        super.initialize(context, logger, variables);


        if (context instanceof WorkflowExecutionContext) {
            this.context = (WorkflowExecutionContext) context;
            this.logger = this.context.getWorkflowLog();
        } else {
            throw new RSuiteException(
                    "This generator is dedicated only for workflow excuction context");
        }

    }

    @Override
    public File generateOutput(File workflowFolder, String moId, File inputFile, File outputFolder)
            throws RSuiteException {

        try {
            File outputFile = super.generateOutput(workflowFolder, moId, inputFile, outputFolder);
            return outputFile;
        } catch (RSuiteException e) {
            throw e;
        } finally {
            createDownloadLinks(moId, workflowFolder, workflowFolder);
        }
    }

    protected void createDownloadLinks(String moId, File workflowFolder, File tempFolder) throws RSuiteException {

        File initialFo = getInitialFoFile(tempFolder);
        createDownloadLinkForAFile(moId, workflowFolder, initialFo, "Link to the initial fo file");

        File areaTreeFile = getAreaTreeFile(tempFolder);
        createDownloadLinkForAFile(moId, workflowFolder, areaTreeFile, "Link to the area tree file");

        File finalFo = getFoFile(tempFolder);
        createDownloadLinkForAFile(moId, workflowFolder, finalFo, "Link to the final fo file");
    }

    protected void createDownloadLinkForAFile(String moId, File workflowFolder, File fileToDwonload,
            String linkText) throws RSuiteException {

        File publishFolder = new File(workflowFolder, "downloads/" + moId);

        try {
            if (fileToDwonload.exists()) {
                FileUtils.moveFileToDirectory(fileToDwonload, publishFolder, true);
                String link =
                        PublishWorkflowUtils.constructDownloadFilesLink(
                                context.getProcessInstanceId(), moId + "/" + fileToDwonload.getName(), linkText);
                logger.info(link);
            }

        } catch (IOException e) {
            throw new RSuiteException(0, "Unable to set up downloads", e);
        }
    }

}
