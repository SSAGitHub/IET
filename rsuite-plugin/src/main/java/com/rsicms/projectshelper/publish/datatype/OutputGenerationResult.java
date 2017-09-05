package com.rsicms.projectshelper.publish.datatype;

import static com.rsicms.projectshelper.publish.workflow.constant.PublishWorkflowVariables.EXPORT_ERROR_FLAG;
import static com.rsicms.projectshelper.publish.workflow.constant.PublishWorkflowVariables.EXPORT_WARNING_FLAG;
import static com.rsicms.projectshelper.publish.workflow.constant.PublishWorkflowVariables.MO_OUTPUTS;
import static com.rsicms.projectshelper.workflow.WorkflowVariables.RSUITE_PATH;
import static com.rsicms.projectshelper.workflow.WorkflowVariables.RSUITE_SESSION_ID;

import java.io.File;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.reallysi.rsuite.api.Session;
import com.reallysi.rsuite.api.User;
import com.reallysi.rsuite.api.workflow.WorkflowExecutionContext;
import com.reallysi.rsuite.service.SessionService;

public class OutputGenerationResult {

    private Map<String, File> moOutputFileMap;
    private String rsuitePath;
    private boolean hasExportError;
    private boolean hasExportWarning;
    private User user;



    private OutputGenerationResult(Map<String, File> moOutputFileMap, String rsuitePath,
            boolean exportError, boolean exportWarning, User user) {
        this.moOutputFileMap = moOutputFileMap;
        this.rsuitePath = rsuitePath;
        this.hasExportError = exportError;
        this.hasExportWarning = exportWarning;
        this.user = user;
    }

    public static class Builder {

        private Map<String, File> moOutputFileMap;

        private String rsuitePath;

        private boolean exportError;

        private boolean exportWarning;

        private User user;

        public Builder moOutputFileMap(Map<String, File> moOutputFileMap) {
            this.moOutputFileMap = moOutputFileMap;
            return this;
        }

        public Builder rsuitePath(String rsuitePath) {
            this.rsuitePath = rsuitePath;
            return this;
        }

        public Builder exportError(boolean exportError) {
            this.exportError = exportError;
            return this;
        }

        public Builder exportWarning(boolean exportWarning) {
            this.exportWarning = exportWarning;
            return this;
        }

        public Builder user(User user) {
            this.user = user;
            return this;
        }

        public OutputGenerationResult build() {
            return new OutputGenerationResult(moOutputFileMap, rsuitePath, exportError,
                    exportWarning, user);
        }

    }

    public Map<String, File> getMoOutPuts() {
        return moOutputFileMap;
    }

    public String getRsuitePath() {
        return rsuitePath;
    }

    public boolean hasExportError() {
        return hasExportError;
    }

    public boolean hasExportWarning() {
        return hasExportWarning;
    }

    public String getUser() {
        String userName = user.getFullName();
        
        if (StringUtils.isBlank(userName) || "none".equalsIgnoreCase(userName)){
            return user.getUserId();
        }
        
        return user.getFullName();
    }

    @SuppressWarnings("unchecked")
	public static OutputGenerationResult createFromWorkflowContext(WorkflowExecutionContext context){
            OutputGenerationResult.Builder resultBuilder = new Builder();
            
            Map<String, Object> workflowVariables = context.getWorkflowVariables();
            
            String rsuitePath = (String)workflowVariables.get(RSUITE_PATH);
            Boolean exportError = (Boolean)workflowVariables.get(EXPORT_ERROR_FLAG.getVariableName());
            Boolean exportWarning = (Boolean)workflowVariables.get(EXPORT_WARNING_FLAG.getVariableName());
            User user = getContextUser(context, workflowVariables);
            Map<String, File> moOutPuts = (Map<String, File>) workflowVariables.get(MO_OUTPUTS.getVariableName());
            
            resultBuilder.moOutputFileMap(moOutPuts);
            resultBuilder.rsuitePath(rsuitePath);
            resultBuilder.exportError(exportError.booleanValue());
            resultBuilder.exportWarning(exportWarning.booleanValue());
            resultBuilder.user(user);
            
            return resultBuilder.build();
        }
    
    private static User getContextUser(WorkflowExecutionContext context,
            Map<String, Object> workflowVariables) {
        SessionService sessionService = context.getSessionService();
        String rsuiteSessionId = (String)workflowVariables.get(RSUITE_SESSION_ID.getVariableName());
        
        Session session = sessionService.getSession(rsuiteSessionId);
        if (session != null){
            return sessionService.getSession(rsuiteSessionId).getUser();
        }
        
        return context.getAuthorizationService().getSystemUser();
    }
}
