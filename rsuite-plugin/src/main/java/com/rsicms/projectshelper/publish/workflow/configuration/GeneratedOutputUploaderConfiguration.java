package com.rsicms.projectshelper.publish.workflow.configuration;

import com.reallysi.rsuite.api.*;
import com.reallysi.rsuite.api.workflow.WorkflowExecutionContext;
import com.rsicms.projectshelper.publish.storage.upload.GeneratedOutputUploader;

public interface GeneratedOutputUploaderConfiguration {

    GeneratedOutputUploader getGeneratedOutputUploader(WorkflowExecutionContext context, User user) throws RSuiteException;
}
