package org.theiet.rsuite.journals.domain.issues.publish.proof.generation;


import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.User;
import com.reallysi.rsuite.api.workflow.WorkflowExecutionContext;
import com.rsicms.projectshelper.publish.generators.OutputGenerator;
import com.rsicms.projectshelper.publish.workflow.configuration.OutputGeneratorConfiguration;

public class IssueOutputGeneratorConfiguration implements OutputGeneratorConfiguration {

    @Override
    public OutputGenerator getOutputGenerator(WorkflowExecutionContext context, User user)
            throws RSuiteException {
        return new IssuePdfOutputGenerator();
    }

}
