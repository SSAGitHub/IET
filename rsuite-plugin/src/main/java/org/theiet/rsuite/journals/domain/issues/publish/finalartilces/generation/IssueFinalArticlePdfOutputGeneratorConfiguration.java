package org.theiet.rsuite.journals.domain.issues.publish.finalartilces.generation;


import com.reallysi.rsuite.api.*;
import com.reallysi.rsuite.api.workflow.WorkflowExecutionContext;
import com.rsicms.projectshelper.publish.generators.OutputGenerator;
import com.rsicms.projectshelper.publish.workflow.configuration.OutputGeneratorConfiguration;

public class IssueFinalArticlePdfOutputGeneratorConfiguration implements OutputGeneratorConfiguration {

    @Override
    public OutputGenerator getOutputGenerator(WorkflowExecutionContext context, User user)
            throws RSuiteException {
        return new IssueFinalArticlePdfOutputGenerator();
    }

}
