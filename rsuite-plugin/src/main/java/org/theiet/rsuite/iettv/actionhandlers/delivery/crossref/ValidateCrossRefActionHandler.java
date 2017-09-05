package org.theiet.rsuite.iettv.actionhandlers.delivery.crossref;


import java.io.File;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.theiet.rsuite.iettv.domain.delivery.crossref.CrossRefDocumentValidationResult;
import org.theiet.rsuite.iettv.domain.delivery.crossref.CrossRefDocumentValidator;
import org.theiet.rsuite.utils.ActionHandlerUtils;

import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.workflow.WorkflowExecutionContext;
import com.rsicms.projectshelper.workflow.WorkflowConstants;
import com.rsicms.projectshelper.workflow.actionhandlers.AbstractNonLeavingActionHandler;

public class ValidateCrossRefActionHandler extends AbstractNonLeavingActionHandler {
	
    private static final long serialVersionUID = 1850267183084785401L;

    @Override
	public void executeTask(WorkflowExecutionContext executionContext)
			throws Exception {

	    Log workflowLog = executionContext.getWorkflowLog();
        workflowLog.info("Validating CrossRef Document ");

        String crossRefPath = executionContext.getVariable(IetTvCrossRefWorflowVariables.CROSS_REF_PATH.getVariableName());
        File crossRefFile= new File(crossRefPath);
        String crossRefContent = FileUtils.readFileToString(crossRefFile, "utf-8");
        
         CrossRefDocumentValidationResult result = CrossRefDocumentValidator.validateCrossRefDocument(crossRefContent);
         
         if (result.isValid()){
        	 workflowLog.info("CrossRef Document is valid");

         }else{
        	 workflowLog.error("---Validation errors");
        	 workflowLog.error(result.getValidationErrorMessages());
        	 workflowLog.error("---Validation errors");
        	 
        	 executionContext.setVariable(WorkflowConstants.EXCEPTION_OCCUR, "true");
        	 ActionHandlerUtils.createFailDetailInformation(executionContext);
         }
        
         
        
	}

}