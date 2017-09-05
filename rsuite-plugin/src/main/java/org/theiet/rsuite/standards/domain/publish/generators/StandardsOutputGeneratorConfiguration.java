package org.theiet.rsuite.standards.domain.publish.generators;

import static org.theiet.rsuite.standards.domain.publish.datatype.StandardsPublishWorkflowVariables.*;

import com.reallysi.rsuite.api.*;
import com.reallysi.rsuite.api.workflow.WorkflowExecutionContext;
import com.rsicms.projectshelper.publish.generators.OutputGenerator;
import com.rsicms.projectshelper.publish.workflow.configuration.OutputGeneratorConfiguration;

public class StandardsOutputGeneratorConfiguration implements OutputGeneratorConfiguration {

    @Override
    public OutputGenerator getOutputGenerator(WorkflowExecutionContext context, User user)
            throws RSuiteException {
        
        String generatorId = context.getVariable(GENERATOR_ID.getVariableName());
        
        OutputGeneratorType generatorType = OutputGeneratorType.valueOf(generatorId);
        
        return OutputGeneratorFactory.getOutputGenerator(generatorType);
    }

}
