package org.theiet.rsuite.journals.domain.issues.publish.proof;

import static org.theiet.rsuite.journals.domain.issues.publish.common.datatype.IssuePublishWorkflowVariables.ISSUE_CA_ID;

import java.util.HashMap;
import java.util.Map;

import org.theiet.rsuite.datamodel.types.IetWorkflows;
import org.theiet.rsuite.journals.domain.issues.datatype.Issue;
import org.theiet.rsuite.journals.domain.issues.publish.proof.export.IssueContentExporterConfiguration;
import org.theiet.rsuite.journals.domain.issues.publish.proof.generation.IssueOutputGeneratorConfiguration;
import org.theiet.rsuite.journals.domain.issues.publish.proof.notification.IssueProofSendNotificationConfiguration;
import org.theiet.rsuite.journals.domain.issues.publish.proof.upload.IssueGeneratedOutputUploaderConfiguration;

import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.User;
import com.reallysi.rsuite.api.extensions.ExecutionContext;
import com.reallysi.rsuite.api.remoteapi.CallArgumentList;
import com.reallysi.rsuite.api.remoteapi.RemoteApiExecutionContext;
import com.reallysi.rsuite.api.workflow.ProcessInstanceSummaryInfo;
import com.rsicms.projectshelper.publish.workflow.launcher.PublishWorkflowLauncher;
import com.rsicms.projectshelper.workflow.RSuiteWorkflowVariable;
import com.rsicms.projectshelper.workflow.WorkflowUtils;

public final class IssueProofPdfWorkflowLauncher {

    private IssueProofPdfWorkflowLauncher(){}

    public static ProcessInstanceSummaryInfo launchProofPdfWorkflowForIssue(RemoteApiExecutionContext context,
			CallArgumentList args, Issue issue) throws RSuiteException{
    	
    	User user = context.getSession().getUser();
    	
        Map<RSuiteWorkflowVariable, Object> workflowVariables = WorkflowUtils.createCommonWorkflowVariables(context, args);       
        return launchProofIssueWorkflow(context, user, issue, workflowVariables);
    }
    
    public static ProcessInstanceSummaryInfo launchProofPdfWorkflowForIssue(ExecutionContext context, User user, Issue issue) throws RSuiteException{
        Map<RSuiteWorkflowVariable, Object> workflowVariables = new HashMap<RSuiteWorkflowVariable, Object>();
        return launchProofIssueWorkflow(context, user, issue, workflowVariables);
    }
    
    
    private static ProcessInstanceSummaryInfo launchProofIssueWorkflow(ExecutionContext context, User user, Issue issue, Map<RSuiteWorkflowVariable, Object> workflowVariables) throws RSuiteException{
        
    	workflowVariables.put(ISSUE_CA_ID, issue.getIssueCa().getId());    	
    	
        PublishWorkflowLauncher.Builder builder = new PublishWorkflowLauncher.Builder(context, user);
        builder.addWorkflowVariables(workflowVariables);
        builder.workflowName(IetWorkflows.IET_PUBLISH_WORKFLOW.getWorkflowName());
        builder.addContextMo(issue.getIssueMo());
        builder.exportConfiguration(IssueContentExporterConfiguration.class);
        builder.outputGeneratorConfiguration(IssueOutputGeneratorConfiguration.class);
        builder.generatedOutputUploaderConfiguration(IssueGeneratedOutputUploaderConfiguration.class);
        builder.sendNotificationConfiguration(IssueProofSendNotificationConfiguration.class);
        PublishWorkflowLauncher workflowLauncher = builder.build();
        return workflowLauncher.launchPublishWorkflow();
    }
    
    
}
