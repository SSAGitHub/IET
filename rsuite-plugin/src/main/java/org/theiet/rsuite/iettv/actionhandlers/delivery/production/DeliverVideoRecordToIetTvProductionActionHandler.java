package org.theiet.rsuite.iettv.actionhandlers.delivery.production;


import java.io.*;

import org.apache.commons.logging.Log;
import org.theiet.rsuite.iettv.domain.datatype.VideoRecord;
import org.theiet.rsuite.iettv.domain.delivery.production.ProductionDeliveryHelper;

import com.reallysi.rsuite.api.*;
import com.reallysi.rsuite.api.workflow.WorkflowExecutionContext;
import com.rsicms.projectshelper.workflow.actionhandlers.AbstractNonLeavingActionHandler;

public class DeliverVideoRecordToIetTvProductionActionHandler extends AbstractNonLeavingActionHandler {
	
    private static final String FILENAME_EXTENSION_ZIP = "zip";
    
    private static final long serialVersionUID = 1850267183084785401L;

    @Override
	public void executeTask(WorkflowExecutionContext executionContext)
			throws Exception {

	    Log workflowLog = executionContext.getWorkflowLog();
        workflowLog.info("Delivering package to IET.tv production ");

        String videoRecordContainerId = getRSuiteId();
        User user = getSystemUser();
        
        VideoRecord videoRecord = new VideoRecord(executionContext, user, videoRecordContainerId);
        
        File temporaryFolder = getTempFolder();
        File productionPackageFile = new File(temporaryFolder, videoRecord.createVideoFileName(FILENAME_EXTENSION_ZIP));
        extractProductionPackage(executionContext, videoRecord, productionPackageFile);
        
        ProductionDeliveryHelper.sendToProduction(executionContext, productionPackageFile);
	}

    private void extractProductionPackage(WorkflowExecutionContext executionContext,
            VideoRecord videoRecord, File productionPackageFile) throws RSuiteException, FileNotFoundException {
        
        FileOutputStream inspecPackageFileOutStream = new FileOutputStream(productionPackageFile);
        ProductionDeliveryHelper.extractProductionPackage(executionContext, videoRecord, inspecPackageFileOutStream);
        
    }

}