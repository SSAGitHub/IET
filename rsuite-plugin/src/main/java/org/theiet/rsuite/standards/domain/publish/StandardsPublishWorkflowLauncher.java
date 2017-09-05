package org.theiet.rsuite.standards.domain.publish;

import java.util.Map;

import org.theiet.rsuite.datamodel.types.IetWorkflows;
import org.theiet.rsuite.standards.domain.publish.export.StandardsDitaContentExporterConfiguration;
import org.theiet.rsuite.standards.domain.publish.generators.StandardsOutputGeneratorConfiguration;
import org.theiet.rsuite.standards.domain.publish.notification.StandardsSendNotificationConfiguration;
import org.theiet.rsuite.standards.domain.publish.upload.StandardsGeneratedOutputUploaderConfiguration;

import com.reallysi.rsuite.api.*;
import com.reallysi.rsuite.api.extensions.ExecutionContext;
import com.rsicms.projectshelper.publish.workflow.launcher.PublishWorkflowLauncher;
import com.rsicms.projectshelper.workflow.RSuiteWorkflowVariable;

public final class StandardsPublishWorkflowLauncher {

    private StandardsPublishWorkflowLauncher(){};
    
    public static void launchPublishWorkflow(ExecutionContext context, User user, ManagedObject contextMo, Map<RSuiteWorkflowVariable, Object> variables) throws RSuiteException{
        
        PublishWorkflowLauncher.Builder builder = new PublishWorkflowLauncher.Builder(context, user);
        builder.addWorkflowVariables(variables);
        builder.workflowName(IetWorkflows.IET_PUBLISH_WORKFLOW.getWorkflowName());
        builder.addContextMo(contextMo);
        builder.exportConfiguration(StandardsDitaContentExporterConfiguration.class);
        builder.outputGeneratorConfiguration(StandardsOutputGeneratorConfiguration.class);
        builder.generatedOutputUploaderConfiguration(StandardsGeneratedOutputUploaderConfiguration.class);
        builder.sendNotificationConfiguration(StandardsSendNotificationConfiguration.class);
        PublishWorkflowLauncher workflowLauncher = builder.build();
        workflowLauncher.launchPublishWorkflow();        
    }
}
