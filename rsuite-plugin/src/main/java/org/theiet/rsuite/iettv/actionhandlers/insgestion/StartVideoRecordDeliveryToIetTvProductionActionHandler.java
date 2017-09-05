package org.theiet.rsuite.iettv.actionhandlers.insgestion;

import static org.theiet.rsuite.iettv.actionhandlers.IetTvWorkflowVariables.*;

import org.apache.commons.logging.Log;
import org.theiet.rsuite.iettv.domain.datatype.VideoRecord;
import org.theiet.rsuite.iettv.domain.search.IetTvFinder;

import com.reallysi.rsuite.api.workflow.WorkflowExecutionContext;
import com.rsicms.projectshelper.workflow.actionhandlers.AbstractActionHandler;

public class StartVideoRecordDeliveryToIetTvProductionActionHandler extends AbstractActionHandler {

	private static final long serialVersionUID = 1L;

	@Override
	public void executeTask(WorkflowExecutionContext executionContext)
			throws Exception {
	    
	    String videoId = executionContext.getVariable(VIDEO_ID.getVariableName());
	    VideoRecord videoRecord = IetTvFinder.findExistingVideoRecordByVideoId(executionContext, getSystemUser(), videoId);
	    if (videoRecord.hasInspecData()){
	        videoRecord.startProductionDeliveryWorkflow();   
	    }else{
	        Log workflowLog = executionContext.getWorkflowLog();
	        workflowLog.info("Inspec data not present. Skip send to the production system");
	    }
	}

}