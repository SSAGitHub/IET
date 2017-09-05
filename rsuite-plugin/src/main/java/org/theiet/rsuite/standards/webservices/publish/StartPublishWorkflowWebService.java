package org.theiet.rsuite.standards.webservices.publish;

import java.util.HashMap;
import java.util.Map;

import org.theiet.rsuite.datamodel.types.IetWorkflows;
import org.theiet.rsuite.standards.domain.book.StandardsBookEdition;
import org.theiet.rsuite.standards.domain.book.StandardsBookTypes;
import org.theiet.rsuite.standards.domain.publish.StandardsPublishWorkflowLauncher;

import com.reallysi.rsuite.api.*;
import com.reallysi.rsuite.api.extensions.ExecutionContext;
import com.reallysi.rsuite.api.remoteapi.CallArgumentList;
import com.reallysi.rsuite.api.remoteapi.RemoteApiExecutionContext;
import com.rsicms.projectshelper.utils.browse.ProjectBrowserUtils;
import com.rsicms.projectshelper.webservice.ProjectRemoteApiHandler;
import com.rsicms.projectshelper.workflow.RSuiteWorkflowVariable;
import com.rsicms.projectshelper.workflow.WorkflowUtils;


public abstract class StartPublishWorkflowWebService extends ProjectRemoteApiHandler {

    private static final String WEB_SERVICE_LABEL = "Start Publish Workflow";

    @Override
    protected String exectuteAction(RemoteApiExecutionContext context, CallArgumentList args)
            throws RSuiteException {
        
    	String moId = getRsuiteId(args);
    	String rsuitePath = getRSuitePath(args);
    	
    	if (rsuitePath == null) {
            rsuitePath = ProjectBrowserUtils.getBrowserUri(context, args.getFirstValue("sourceId"));
        }
    	
    	StandardsBookEdition bookEdition = obtainBookEdition(context, moId, rsuitePath);
    	
    	Map<RSuiteWorkflowVariable, Object> additionalVariables = getAdditionalVarialbes(context, bookEdition, moId, args);
    	
        Map<RSuiteWorkflowVariable, Object> variables =
                additionalVariables == null ? new HashMap<RSuiteWorkflowVariable, Object>()
                        : additionalVariables;
                
        variables.putAll(WorkflowUtils.createCommonWorkflowVariables(context, args));
                
        User user = context.getSession().getUser();
        ManagedObject contextMo = context.getManagedObjectService().getManagedObject(user, getMoSourceId(args));        
        StandardsPublishWorkflowLauncher.launchPublishWorkflow(context, user, contextMo, variables);

        return "Publish process has been started.";
    }

    protected abstract Map<RSuiteWorkflowVariable, Object> getAdditionalVarialbes(
    		ExecutionContext context, StandardsBookEdition bookEdition, String moId, CallArgumentList args) throws RSuiteException;


    protected String getWorkflowName() {
        return IetWorkflows.IET_PUBLISH_WORKFLOW.getWorkflowName();
    }

    @Override
    protected String getDialogTitle() {
        return WEB_SERVICE_LABEL;
    }
    
    private StandardsBookEdition obtainBookEdition(RemoteApiExecutionContext context, String moId, String rsuitePath)
			throws RSuiteException {
		User user = getUser(context);
		
		ContentAssembly bookEditionCa = ProjectBrowserUtils.getAncestorCAbyType(context, user, rsuitePath, StandardsBookTypes.CA_TYPE_STANDARDS_BOOK_EDITION);
		if (bookEditionCa == null){
			throw new RSuiteException("Unable to localize book edition for mo " +  moId);
		}
		
		StandardsBookEdition standardsBookEdition = new StandardsBookEdition(context, bookEditionCa);
		return standardsBookEdition;
	}


}
