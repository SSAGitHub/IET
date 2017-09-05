package com.rsicms.projectshelper.publish.workflow.launcher;

import static com.rsicms.projectshelper.publish.workflow.constant.PublishWorkflowVariables.*;

import java.util.*;

import org.apache.commons.lang.StringUtils;

import com.reallysi.rsuite.api.*;
import com.reallysi.rsuite.api.extensions.ExecutionContext;
import com.reallysi.rsuite.api.workflow.ProcessInstanceSummaryInfo;
import com.rsicms.projectshelper.publish.workflow.configuration.*;
import com.rsicms.projectshelper.workflow.*;

public final class PublishWorkflowLauncher {

    private List<ManagedObject> contextMoList;

    private Map<RSuiteWorkflowVariable, Object> variables =
            new HashMap<RSuiteWorkflowVariable, Object>();

    private String workflowName;

    private ExecutionContext context;

    private User user;

    private PublishWorkflowLauncher(ExecutionContext context, User user, List<ManagedObject> contextMo,
            String workflowName, Map<RSuiteWorkflowVariable, Object> variables) {
        super();
        this.context = context;
        this.user = user;
        this.contextMoList = contextMo;
        this.workflowName = workflowName;
        this.variables = variables;
    }

    public ProcessInstanceSummaryInfo launchPublishWorkflow() throws RSuiteException {
    	
    	if (contextMoList.size() > 1){
    		return WorkflowLauncher
                    .startWorkflowWithContext(context, user, contextMoList, workflowName, variables);
    	}
    	
        return WorkflowLauncher
                .startWorkflowWithContext(context, user, contextMoList.get(0), workflowName, variables);

    }

    public static class Builder {

    	private List<ManagedObject> contextMoList = new ArrayList<>();
    	
        private Map<RSuiteWorkflowVariable, Object> variables =
                new HashMap<RSuiteWorkflowVariable, Object>();

        private String workflowName;

        private ExecutionContext context;

        private User user;

        public Builder(ExecutionContext context, User user) {
            this.context = context;
            this.user = user;
        }

        public Builder addWorkflowVariables(Map<RSuiteWorkflowVariable, Object> variables) {
            this.variables.putAll(variables);
            return this;
        }

        public Builder workflowName(String workflowName) {
            this.workflowName = workflowName;
            return this;
        }
        
        public Builder addContextMo(ManagedObject contextMo) {
            contextMoList.add(contextMo);
            return this;
        }

        public Builder exportConfiguration(Class<? extends ContentExporterConfiguration> clazz) throws RSuiteException {
            variables.put(CONTENT_EXPORTER_CONFIGURATION_CLASS, clazz.getCanonicalName());
            return this;
        }

        public Builder outputGeneratorConfiguration(Class<? extends OutputGeneratorConfiguration> clazz) throws RSuiteException {
            variables.put(OUTPUT_GENERATOR_CONFIGURATION_CLASS, clazz.getCanonicalName());
            return this;
        }

        public Builder generatedOutputUploaderConfiguration(Class<? extends GeneratedOutputUploaderConfiguration> clazz) throws RSuiteException {
            variables.put(GENERATED_OUTPUT_UPLOADER_CONFIGURATION_CLASS, clazz.getCanonicalName());
            return this;
        }

        public Builder sendNotificationConfiguration(Class<? extends SendNotificationConfiguration> clazz) throws RSuiteException {
            variables.put(SEND_NOTIFICATION_CONFIGURATION_CLASS, clazz.getCanonicalName());
            return this;
        }

        public PublishWorkflowLauncher build() throws RSuiteException {
            checkValidatity();
            return new PublishWorkflowLauncher(context, user, contextMoList, workflowName, variables);
        }

        private void checkValidatity() throws RSuiteException {
            if (contextMoList.size() < 1) {
                throw new RSuiteException("Context MO is required");
            }

            if (StringUtils.isEmpty(workflowName)) {
                throw new RSuiteException("Workflow name is required");
            }

            if (!variables.containsKey(OUTPUT_GENERATOR_CONFIGURATION_CLASS)) {
                throw new RSuiteException("Output generator configuration class is required");
            }

            if (!variables.containsKey(GENERATED_OUTPUT_UPLOADER_CONFIGURATION_CLASS)) {
                throw new RSuiteException(
                        "Generated output uploader configuration class is required");
            }
        }

    }



}
