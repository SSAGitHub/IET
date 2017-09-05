package org.theiet.rsuite.iettv.actionhandlers.inspec;

import java.io.*;

import org.apache.commons.logging.Log;
import org.theiet.rsuite.iettv.domain.datatype.VideoRecord;
import org.theiet.rsuite.iettv.domain.inspec.VideoInspecHelper;

import com.reallysi.rsuite.api.*;
import com.reallysi.rsuite.api.workflow.WorkflowExecutionContext;
import com.rsicms.projectshelper.workflow.actionhandlers.AbstractNonLeavingActionHandler;

public class DeliverVideoRecordPackageForInspecActionHandler extends AbstractNonLeavingActionHandler {

    private static final long serialVersionUID = -8622663468143367335L;

    @Override
	public void executeTask(WorkflowExecutionContext executionContext)
			throws Exception {

        Log workflowLog = executionContext.getWorkflowLog();
        workflowLog.info("Deliver package for inspec");

        String videoRecordContainerId = getRSuiteId();
        User user = getSystemUser();
        
        VideoRecord videoRecord = new VideoRecord(executionContext, user, videoRecordContainerId);
        
        File temporaryFolder = getTempFolder();
        File inspecPackageFile = new File(temporaryFolder, videoRecord.createVideoInspecPackageFileName());
        extractInspecPackage(executionContext, videoRecord, inspecPackageFile);
        
        VideoInspecHelper.sendToInspec(executionContext, inspecPackageFile);
        
	}

    private void extractInspecPackage(WorkflowExecutionContext executionContext,
            VideoRecord videoRecord, File inspecPackageFile) throws FileNotFoundException,
            RSuiteException {
        FileOutputStream inspecPackageFileOutStream = new FileOutputStream(inspecPackageFile);        
        VideoInspecHelper.extractInspecPackage(executionContext, videoRecord, inspecPackageFileOutStream);
        
    }

   

}