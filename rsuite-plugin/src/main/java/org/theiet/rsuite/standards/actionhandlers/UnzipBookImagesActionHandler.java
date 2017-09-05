package org.theiet.rsuite.standards.actionhandlers;

import java.io.File;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.theiet.rsuite.standards.StandardsBooksConstans;
import org.theiet.rsuite.standards.domain.image.ImageFilesIngestionUnzipHelper;

import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.workflow.*;
import com.rsicms.projectshelper.workflow.actionhandlers.AbstractNonLeavingActionHandler;

public class UnzipBookImagesActionHandler extends AbstractNonLeavingActionHandler implements StandardsBooksConstans {

	private static final long serialVersionUID = 1L;

	@Override
	public void executeTask(WorkflowExecutionContext context)
			throws Exception {
		Log wLog = context.getWorkflowLog();
		
		wLog.info("Getting images zip file");
		FileWorkflowObject fileWorkflowObject = context.getFileWorkflowObject();

		File bookImgPkg =  fileWorkflowObject.getFile();
		wLog.info("Zip file ingested: " + bookImgPkg.getAbsolutePath());
		
		context.setVariable(WF_VAR_REMOVABLE_DIRECTORY, getIngestionWorkspaceFolder());
		
		ImageFilesIngestionUnzipHelper imgFileIngestionUnzip = new ImageFilesIngestionUnzipHelper(context);
		Map<String, String> workflowVars = imgFileIngestionUnzip.unzipImagesPackage(bookImgPkg);
				
		setWorkflowVariables(context, workflowVars);
	}

	/**
	 * @param context
	 * @param workflowVars
	 * @throws RSuiteException
	 */
	protected void setWorkflowVariables(WorkflowExecutionContext context,
			Map<String, String> workflowVars) throws RSuiteException {
		context.setVariable(WF_VAR_IMAGES_DIRECTORY, workflowVars.get(ImageFilesIngestionUnzipHelper.OUTPUT_DIRECTORY));		
		context.setVariable(WF_VAR_BOOK_CA_ID, workflowVars.get(ImageFilesIngestionUnzipHelper.BOOK_CA_ID));
		context.setVariable(WF_VAR_BOOK_PREFIX, workflowVars.get(ImageFilesIngestionUnzipHelper.BOOK_PREFIX));
	}

}
