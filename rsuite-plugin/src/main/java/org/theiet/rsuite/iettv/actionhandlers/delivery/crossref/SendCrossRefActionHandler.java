package org.theiet.rsuite.iettv.actionhandlers.delivery.crossref;


import java.io.File;

import org.apache.commons.logging.Log;
import org.theiet.rsuite.iettv.domain.delivery.crossref.CrossRefDocumentSender;

import com.reallysi.rsuite.api.workflow.WorkflowExecutionContext;
import com.rsicms.projectshelper.workflow.actionhandlers.AbstractNonLeavingActionHandler;

public class SendCrossRefActionHandler extends AbstractNonLeavingActionHandler {
	
    
	private static final long serialVersionUID = -8779399353708073693L;

	@Override
	public void executeTask(WorkflowExecutionContext executionContext)
			throws Exception {

	    Log workflowLog = executionContext.getWorkflowLog();
        workflowLog.info("Sending CrossRef Document ");

        
        String crossRefPath = executionContext.getVariable(IetTvCrossRefWorflowVariables.CROSS_REF_PATH.getVariableName());
        File crossRefFile= new File(crossRefPath);
        
        CrossRefDocumentSender.sendCrossRef(executionContext, executionContext.getWorkflowLog(), crossRefFile);
        
        workflowLog.info("Sending CrossRef Document has been sent");
	}

}