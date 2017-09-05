package org.theiet.rsuite.iettv.actionhandlers.inspec;

import static org.theiet.rsuite.iettv.actionhandlers.IetTvWorkflowVariables.*;

import org.theiet.rsuite.iettv.domain.datatype.VideoRecord;
import org.theiet.rsuite.iettv.domain.search.IetTvFinder;

import com.reallysi.rsuite.api.workflow.WorkflowExecutionContext;
import com.rsicms.projectshelper.workflow.actionhandlers.AbstractNonLeavingActionHandler;

public class MergeVideoInspecActionHandler extends AbstractNonLeavingActionHandler {

	private static final long serialVersionUID = 1L;

	@Override
	public void executeTask(WorkflowExecutionContext executionContext)
			throws Exception {
	    
	    String videoId = executionContext.getVariable(VIDEO_ID.getVariableName());
	    VideoRecord videoRecord = IetTvFinder.findExistingVideoRecordByVideoId(executionContext, getSystemUser(), videoId);
	    videoRecord.mergeInspecData();
	    videoRecord.startProductionDeliveryWorkflow();
	}

}