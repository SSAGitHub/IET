package org.theiet.rsuite.standards.actionhandlers.esp;

import static com.rsicms.projectshelper.workflow.WorkflowConstants.*;
import static org.theiet.rsuite.standards.constans.PublishWorkflowContans.*;

import java.io.File;

import org.apache.commons.logging.Log;
import org.theiet.rsuite.standards.StandardsBooksConstans;
import org.theiet.rsuite.standards.domain.book.StandardsBookEdition;
import org.theiet.rsuite.standards.domain.book.export.esp.ESPContentExporter;
import org.theiet.rsuite.standards.domain.book.index.*;
import org.theiet.rsuite.utils.ActionHandlerUtils;

import com.reallysi.rsuite.api.*;
import com.reallysi.rsuite.api.workflow.WorkflowExecutionContext;
import com.rsicms.projectshelper.export.MoExportResult;
import com.rsicms.projectshelper.message.ProcessingMessageHandler;
import com.rsicms.projectshelper.publish.workflow.PublishWorkflowUtils;
import com.rsicms.projectshelper.workflow.actionhandlers.AbstractNonLeavingActionHandler;

public class ESPExportBookEditionForDeliveryActionHandler extends
		AbstractNonLeavingActionHandler implements StandardsBooksConstans {

	private static final long serialVersionUID = 1L;

	@Override
	public void executeTask(WorkflowExecutionContext context) throws Exception {

		String publicationCaId = context.getVariable(WF_VAR_RSUITE_CONTENTS);

		File tempFolder = getTempFolder(context);

		StandardsBookEdition bookEdition = new StandardsBookEdition(context,
				publicationCaId);

		File exportDir = setUpExportFolder(context, tempFolder, bookEdition);
		String userId = context.getVariable(WF_VAR_RSUITE_USER_ID);
		User  user = context.getAuthorizationService().findUser(userId);
		
		ESPContentExporter espExporter = new ESPContentExporter(context,
				user, bookEdition, exportDir);

		MoExportResult exportResult = espExporter.export();

		context.getWorkflowLog().info("Exporting files to " + exportDir);
		
		processExportResults(context, exportResult);

	}

	public void processExportResults(WorkflowExecutionContext context,
	        MoExportResult exportResult) throws RSuiteException {
		ProcessingMessageHandler messageHandler = exportResult
				.getMessageHandler();
		
		if (messageHandler.hasErrors() || messageHandler.hasWarnings()){			
			logExportProcessingMessages(context, messageHandler);						
			context.setVariable(EXCEPTION_OCCUR, "true");
			ActionHandlerUtils.createFailDetailInformation(context);
		}
	}

	public void logExportProcessingMessages(WorkflowExecutionContext context,
			ProcessingMessageHandler messageHandler) {
		Log workflowLog = context.getWorkflowLog();
		String pId = context.getProcessInstanceId();
		workflowLog.info(PublishWorkflowUtils.constructDownloadExpotedFilesLink(pId));
		
		workflowLog.info("----- START EXPORT LOG -----");
		messageHandler.writeMessagesToLogger(workflowLog);
		workflowLog.info("----- END EXPORT LOG -----");
	}

	public File setUpExportFolder(WorkflowExecutionContext context,
			File tempFolder, StandardsBookEdition bookEdition)
			throws RSuiteException {
		String bookEditionEProductCode = bookEdition.getBookPublicationCa()
				.getLayeredMetadataValue(LMD_FIELD_BOOK_E_PRODUCT_CODE);

		File exportDir = new File(tempFolder, bookEditionEProductCode);

		context.setVariable(WF_VAR_EXPORT_FOLDER, exportDir.getAbsolutePath());							
		context.setVariable(WF_VAR_BOOK_EDITION_E_PRODUCT_CODE, bookEditionEProductCode);
		return exportDir;
	}

	private File getTempFolder(WorkflowExecutionContext context) {
		String tempFolderPath = context.getWorkflowJobContext()
				.getTempFolderPath();
		File tempFolder = new File(tempFolderPath);
		return tempFolder;
	}

}