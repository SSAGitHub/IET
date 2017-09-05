package org.theiet.rsuite.iettv.actionhandlers.delivery.crossref;


import java.io.File;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.theiet.rsuite.iettv.domain.datatype.VideoRecord;
import org.theiet.rsuite.iettv.domain.delivery.crossref.CrossRefDocumentCreator;

import com.reallysi.rsuite.api.User;
import com.reallysi.rsuite.api.workflow.WorkflowExecutionContext;
import com.rsicms.projectshelper.workflow.actionhandlers.AbstractNonLeavingActionHandler;

public class CreateCrossRefActionHandler extends AbstractNonLeavingActionHandler {
	
    private static final String FILENAME_EXTENSION_XML = "xml";

	private static final String SUFFIX_REPLACEMENT = "_crossRef" + FILENAME_EXTENSION_XML;

	private static final String EXTENSION = "." + FILENAME_EXTENSION_XML;
    
    private static final long serialVersionUID = 1850267183084785401L;

    @Override
	public void executeTask(WorkflowExecutionContext executionContext)
			throws Exception {

	    Log workflowLog = executionContext.getWorkflowLog();
        workflowLog.info("Creating CrossRef document ");

        String videoRecordContainerId = getRSuiteId();
        User user = getSystemUser();
        
        VideoRecord videoRecord = new VideoRecord(executionContext, user, videoRecordContainerId);
        
        String crossRefDocument = CrossRefDocumentCreator.createCrossRefDocument(executionContext, user, videoRecord);
        
        File temporaryFolder = getTempFolder();
        String crossRefFileName = videoRecord.createVideoFileName(FILENAME_EXTENSION_XML);
        crossRefFileName = crossRefFileName.replace(EXTENSION, SUFFIX_REPLACEMENT);
        File crossRefFile = new File(temporaryFolder, crossRefFileName);
        FileUtils.writeStringToFile(crossRefFile, crossRefDocument, "utf-8");        
        
        executionContext.setVariable(IetTvCrossRefWorflowVariables.CROSS_REF_PATH.getVariableName(), crossRefFile.getAbsolutePath());
	}

 

}