package org.theiet.rsuite.iettv.actionhandlers.inspec;

import org.apache.commons.logging.Log;
import org.theiet.rsuite.iettv.domain.datatype.VideoRecord;
import org.theiet.rsuite.iettv.domain.publish.VideoRecordPdfGenerator;

import com.reallysi.rsuite.api.User;
import com.reallysi.rsuite.api.workflow.WorkflowExecutionContext;
import com.rsicms.projectshelper.workflow.actionhandlers.AbstractNonLeavingActionHandler;

public class GenerateVideoRecordPdfActionHandler extends AbstractNonLeavingActionHandler {


    private static final long serialVersionUID = -8622663468143367335L;

    @Override
	public void executeTask(WorkflowExecutionContext executionContext)
			throws Exception {

        Log workflowLog = executionContext.getWorkflowLog();
        workflowLog.info("Generating PDF for video record");

        String videoRecordContainerId = getRSuiteId();
        User user = getSystemUser();
        
        VideoRecord videoRecord = new VideoRecord(executionContext, user, videoRecordContainerId);
        VideoRecordPdfGenerator.generatePdf(executionContext, user, videoRecord, getTempFolder());
	}

   

}