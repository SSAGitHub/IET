package com.rsicms.projectshelper.publish.workflow.configuration;

import com.reallysi.rsuite.api.*;
import com.reallysi.rsuite.api.workflow.WorkflowExecutionContext;
import com.rsicms.projectshelper.publish.generators.OutputGenerator;

public interface OutputGeneratorConfiguration {

    OutputGenerator getOutputGenerator(WorkflowExecutionContext context, User user) throws RSuiteException;
}
