package com.rsicms.projectshelper.publish.workflow.actionhandlers;

import static com.rsicms.projectshelper.publish.workflow.actionhandlers.ConfigurationFactory.createContentExporterConfiguration;
import static com.rsicms.projectshelper.publish.workflow.constant.PublishWorkflowVariables.CONTENT_EXPORTER_CONFIGURATION_CLASS;
import static com.rsicms.projectshelper.publish.workflow.constant.PublishWorkflowVariables.DOWNLOADS_FOLDER;
import static com.rsicms.projectshelper.publish.workflow.constant.PublishWorkflowVariables.EXPORTED_FILES;
import static com.rsicms.projectshelper.publish.workflow.constant.PublishWorkflowVariables.EXPORT_ERROR_FLAG;
import static com.rsicms.projectshelper.publish.workflow.constant.PublishWorkflowVariables.EXPORT_FOLDER;
import static com.rsicms.projectshelper.publish.workflow.constant.PublishWorkflowVariables.EXPORT_WARNING_FLAG;
import static com.rsicms.projectshelper.publish.workflow.constant.PublishWorkflowVariables.NON_XML_MO_VARIANT;
import static com.rsicms.projectshelper.publish.workflow.constant.PublishWorkflowVariables.TEMP_FOLDER;
import static com.rsicms.projectshelper.workflow.WorkflowVariables.RSUITE_USER_ID;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;

import com.reallysi.rsuite.api.ManagedObject;
import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.User;
import com.reallysi.rsuite.api.workflow.MoListWorkflowObject;
import com.reallysi.rsuite.api.workflow.MoWorkflowObject;
import com.reallysi.rsuite.api.workflow.WorkflowExecutionContext;
import com.reallysi.rsuite.service.ManagedObjectService;
import com.rsicms.projectshelper.export.MoExportConfiguration;
import com.rsicms.projectshelper.export.MoExportContainerContext;
import com.rsicms.projectshelper.export.MoExportResult;
import com.rsicms.projectshelper.export.MoExporter;
import com.rsicms.projectshelper.export.impl.MoExporterFactory;
import com.rsicms.projectshelper.message.ProcessingMessageHandler;
import com.rsicms.projectshelper.publish.workflow.PublishWorkflowUtils;
import com.rsicms.projectshelper.publish.workflow.configuration.ContentExporterConfiguration;
import com.rsicms.projectshelper.utils.ProjectUserUtils;
import com.rsicms.projectshelper.workflow.WorkflowUtils;

public class ExportContentActionHandler extends PublishWorkflowActionHandler {

    private static final long serialVersionUID = 8224727220520504208L;

    
    @Override
    public void executeHandler(WorkflowExecutionContext context) throws Exception {
        
        Log wfLog = context.getWorkflowLog();
        
        File exportDir = createBaseFolders(context);

        wfLog.info("----Exporting content to " + exportDir.getAbsolutePath());
        
        MoListWorkflowObject mos = context.getMoListWorkflowObject();

        if (mos == null) {
            wfLog.warn("No managed objects in the workflow context. Nothing to do");
            return;
        }

        String pId = context.getProcessInstanceId();
        wfLog.info(PublishWorkflowUtils.constructDownloadExpotedFilesLink(pId));

        wfLog.info("---- START EXPORT LOG");
        exportWorkflowMos(context, exportDir);
        wfLog.info("----- END EXPORT LOG");

        wfLog.info("Content exported successfully");
        wfLog.info("Done");
        
        
    }

    public File createBaseFolders(WorkflowExecutionContext context) throws RSuiteException {
        File tempDir =
                WorkflowUtils.createWorkflowTempFolder(context);
        File exportDir = new File(tempDir, "export");
        File downloadsDir = new File(tempDir, "downloads");

        context.setVariable(EXPORT_FOLDER.getVariableName(), exportDir.getAbsolutePath());
        context.setVariable(TEMP_FOLDER.getVariableName(), tempDir);        
        context.setVariable(DOWNLOADS_FOLDER.getVariableName(), downloadsDir.getAbsolutePath());

        return exportDir;
    }

    public void exportWorkflowMos(WorkflowExecutionContext context, File exportDir)
            throws RSuiteException {

        Boolean exportErrorFlag = Boolean.FALSE;
        Boolean exportWarningFlag = Boolean.FALSE;

        Map<String, File> exportedMOs = new HashMap<String, File>();

        Log wfLog = context.getWorkflowLog();
        User user = getProcessingUser(context);

        MoListWorkflowObject mos = context.getMoListWorkflowObject();
        
        String contentExporterConfigurationClass = context.getVariable(CONTENT_EXPORTER_CONFIGURATION_CLASS.getVariableName());

        ContentExporterConfiguration exporterConfiguration = createContentExporterConfiguration(contentExporterConfigurationClass);

        String variant = context.getVariable(NON_XML_MO_VARIANT.getVariableName());
        MoExporter moExporter = createMoExporter(context, user, exporterConfiguration, variant);

        MoExportContainerContext moExportContainerContext = exporterConfiguration.getMoExportContainerContext(context, user);

        ManagedObjectService moSvc = getManagedObjectService();


        for (MoWorkflowObject moObj : mos.getMoList()) {

            ManagedObject moToExport = getManagedObjectFromWorflow(user, moSvc, moObj);

            MoExportResult exportResult =
                    moExporter.exportMo(moExportContainerContext, moToExport, exportDir);

            ProcessingMessageHandler processingMessageHandler =
                    getProcessingMessageHandler(wfLog, exportResult);

            exportErrorFlag = processingMessageHandler.hasErrors();

            exportWarningFlag = processingMessageHandler.hasWarnings();

            String exportedPathForMo = exportResult.getExportedPathForMo(moToExport.getId());
            if (exportedPathForMo != null){
            	exportedMOs.put(moObj.getMoid(),
                        new File(exportedPathForMo));            	
            }

        }

        context.setVariable(EXPORTED_FILES.getVariableName(), exportedMOs);

        context.setVariable(EXPORT_ERROR_FLAG.getVariableName(), exportErrorFlag);
        context.setVariable(EXPORT_WARNING_FLAG.getVariableName(), exportWarningFlag);
    }

    public User getProcessingUser(WorkflowExecutionContext context) throws RSuiteException {

        String userId = context.getVariable(RSUITE_USER_ID.getVariableName());

        if (StringUtils.isNotBlank(userId) && !getSystemUser().getUserId().equals(userId)) {
            return ProjectUserUtils.getUser(context, userId);
        }

        return getSystemUser();
    }

    private ProcessingMessageHandler getProcessingMessageHandler(Log wfLog,
            MoExportResult exportResult) {

        ProcessingMessageHandler messageHandler = exportResult.getMessageHandler();

        messageHandler.writeMessagesToLogger(wfLog);

        return messageHandler;
    }

    private MoExporter createMoExporter(WorkflowExecutionContext context, User user, ContentExporterConfiguration exporterConfiguration, String nonXMLVariant)
            throws RSuiteException, RSuiteException {

        MoExportConfiguration moExporterConfiguration = exporterConfiguration.getMoExporterConfiguration(context, user);
        moExporterConfiguration.setExportNonXmlVariant(nonXMLVariant);

        return MoExporterFactory.createMoExporter(context, user, moExporterConfiguration);
    }


    protected ManagedObject getManagedObjectFromWorflow(User user, ManagedObjectService moSvc,
            MoWorkflowObject moObj) throws RSuiteException {
        ManagedObject moToExport = moSvc.getManagedObject(user, moObj.getMoid());

        if (moToExport == null) {
            throw new RSuiteException("Unable to find mo with id " + moObj.getMoid());
        }

        if (moToExport.getTargetId() != null) {
            moToExport = moSvc.getManagedObject(user, moToExport.getTargetId());
        }
        return moToExport;
    }

	@Override
	String getWorkflowTaskName() {
		return "Export";
	}

}
