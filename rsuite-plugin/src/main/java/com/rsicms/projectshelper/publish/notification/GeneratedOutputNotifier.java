package com.rsicms.projectshelper.publish.notification;

import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.workflow.WorkflowExecutionContext;
import com.rsicms.projectshelper.publish.datatype.OutputGenerationResult;
import com.rsicms.projectshelper.publish.datatype.UploadGeneratedOutputsResult;

public interface GeneratedOutputNotifier {

    void sendNotification(WorkflowExecutionContext context, OutputGenerationResult generationResult, UploadGeneratedOutputsResult uploadResult) throws RSuiteException;
}
