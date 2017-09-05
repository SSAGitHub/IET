package org.theiet.rsuite.utils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.reallysi.rsuite.api.extensions.ExecutionContext;
import com.reallysi.rsuite.api.workflow.WorkflowExecutionContext;

public class LoggingUtils {

	public static Log getProperDomainLogger (ExecutionContext context, Class<?> loggerClass) {
		Log log = null;

		if (context instanceof WorkflowExecutionContext) {
			log = ((WorkflowExecutionContext)context).getWorkflowLog();
		} else {
			log = LogFactory.getLog(loggerClass);
		}

		return log;
	}
	
}
