package com.rsicms.projectshelper.workflow;

import static com.rsicms.projectshelper.workflow.WorkflowVariables.RSUITE_CONTENTS;
import static com.rsicms.projectshelper.workflow.WorkflowVariables.RSUITE_PATH;
import static com.rsicms.projectshelper.workflow.WorkflowVariables.RSUITE_SESSION_ID;
import static com.rsicms.projectshelper.workflow.WorkflowVariables.RSUITE_USER_EMAIL_ADDRESS;
import static com.rsicms.projectshelper.workflow.WorkflowVariables.RSUITE_USER_FULL_NAME;
import static com.rsicms.projectshelper.workflow.WorkflowVariables.RSUITE_USER_ID;
import static com.rsicms.projectshelper.workflow.WorkflowVariables.SESSION_KEY;

import java.io.File;
import java.io.IOException;
import java.text.Normalizer;
import java.util.*;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

import com.reallysi.rsuite.api.*;
import com.reallysi.rsuite.api.extensions.ExecutionContext;
import com.reallysi.rsuite.api.remoteapi.CallArgumentList;
import com.reallysi.rsuite.api.remoteapi.RemoteApiExecutionContext;
import com.reallysi.rsuite.api.workflow.*;
import com.reallysi.rsuite.api.workflow.WorkflowConstants;
import com.reallysi.rsuite.service.ManagedObjectService;
import com.reallysi.rsuite.service.ProcessInstanceService;

public final class WorkflowLauncher {

    private static final String PROPERTY_RSUITE_WORKFLOW_BASEWORKFOLDER = "rsuite.workflow.baseworkfolder";

    private WorkflowLauncher() {}

    public static ProcessInstanceSummaryInfo startWorkflow(ExecutionContext context, User user,
            String rsuiteId, String processDefinitionName) throws RSuiteException {
        Map<String, Object> workflowVars = new HashMap<String, Object>();
        return startWorkflow(context, user, rsuiteId, processDefinitionName, workflowVars);
    }

    public static ProcessInstanceSummaryInfo startWorkflow(ExecutionContext context, User user,
            String rsuiteId, String processDefinitionName, Map<String, Object> workflowVars)
            throws RSuiteException {

        workflowVars.put(RSUITE_CONTENTS.getVariableName(), rsuiteId);

        return context.getProcessInstanceService().createAndStart(user, processDefinitionName,
                workflowVars);
    }


    public static void startWorkflowForMoFromWS(RemoteApiExecutionContext context,
            CallArgumentList args, String workflowName, Map<RSuiteWorkflowVariable, Object> additionalVariables)
            throws RSuiteException {
        Session session = context.getSession();
        User user = session.getUser();

        String rsuiteId = args.getFirstValue("sourceId");

        if (StringUtils.isBlank(rsuiteId)) {
            rsuiteId = args.getFirstValue("rsuiteId");
        }

        ManagedObjectService moSvc = context.getManagedObjectService();
        ManagedObject mo = moSvc.getManagedObject(user, rsuiteId);

        Map<RSuiteWorkflowVariable, Object> variables = new HashMap<RSuiteWorkflowVariable, Object>();
        String rsuitePath = args.getFirstString("rsuiteBrowseUri");
        if (StringUtils.isNotBlank(rsuitePath) && !variables.containsKey(RSUITE_PATH)) {
            variables.put(RSUITE_PATH, rsuitePath);
        }

        if (additionalVariables != null) {
            variables.putAll(additionalVariables);
        }

        startWorkflowWithContext(context, user, mo, workflowName, variables);
    }

    public static ProcessInstanceSummaryInfo startWorkflowForMoInHotfolder(
            ExecutionContext context, String processDefinitionName, ManagedObject mo,
            Map<RSuiteWorkflowVariable, Object> variables) throws IOException, RSuiteException {

        Map<RSuiteWorkflowVariable, Object> workflowVariables = initializeVariables(context, variables, mo);

        WorkflowJobContext jobContext = createJobContext(context, mo);

        File workfolder = createWorkFolder(jobContext);

        File workflowFile = createWorkflowFile(mo, jobContext, workfolder);

        WorkflowStartContext startContext =
                createStartContext(processDefinitionName, workflowVariables, jobContext, workfolder);

        addWorkflowFileToStartContext(workflowFile, startContext);


        startContext.addVariable(WorkflowConstants.VAR_SOURCEFILE_PATH, workflowFile.getName());

        removeMoFromHotFolder(context, mo);

        return createAndStartProcess(context, startContext);
    }

    private static void removeMoFromHotFolder(ExecutionContext context, ManagedObject mo)
            throws RSuiteException {
        context.getManagedObjectService().checkOut(
                context.getAuthorizationService().getSystemUser(), mo.getId());
        context.getManagedObjectService().remove(context.getAuthorizationService().getSystemUser(),
                mo.getId());
    }

    private static void addWorkflowFileToStartContext(File workflowFile,
            WorkflowStartContext startContext) {
        // zip file business object is creating and be put into process context.
        FileWorkflowObject bo = new FileWorkflowObject();
        bo.setFile(workflowFile);

        startContext.addContextVariable(bo);
    }

    private static File createWorkflowFile(ManagedObject mo, WorkflowJobContext jobContext,
            File workfolder) throws RSuiteException, IOException {
        File workflowFile = new File(workfolder.getPath(), mo.getDisplayName());

        FileUtils.writeByteArrayToFile(workflowFile, IOUtils.toByteArray(mo.getInputStream()));

        File tempWorkflowFile = new File(jobContext.getTempFolderPath(), workflowFile.getName());
        FileUtils.copyFile(workflowFile, tempWorkflowFile);

        jobContext.setSourceFilePath(tempWorkflowFile.getAbsolutePath());
        return workflowFile;
    }

    private static WorkflowStartContext createStartContext(String processDefinitionName,
            Map<RSuiteWorkflowVariable, Object> workflowVariables, WorkflowJobContext jobContext, File workfolder) {
        WorkflowStartContext startContext = new WorkflowStartContext();
        startContext.setName(processDefinitionName);

        for (RSuiteWorkflowVariable name : workflowVariables.keySet()) {
            startContext.addVariable(name.getVariableName(), workflowVariables.get(name));
        }

        startContext.addContextVariable(jobContext);
        startContext.addVariable(WorkflowConstants.VAR_WORKINGFOLDER_PATH,
                workfolder.getAbsolutePath());
        return startContext;
    }

    private static File createWorkFolder(WorkflowJobContext jobContext) {
        File workfolder = new File(jobContext.getWorkFolderPath());

        if (!workfolder.exists()) {
            workfolder.mkdirs();
            File tempfolder = new File(workfolder.getParentFile(), "temp");
            tempfolder.mkdirs();
        }
        return workfolder;
    }

    private static Map<RSuiteWorkflowVariable, Object> initializeVariables(ExecutionContext context, Map<RSuiteWorkflowVariable, Object> variables,
            ManagedObject mo) {

        Map<RSuiteWorkflowVariable, Object> workflowVariables = new HashMap<RSuiteWorkflowVariable, Object>();

        if (variables != null) {
            workflowVariables.putAll(variables);
        }
        
        if (context instanceof RemoteApiExecutionContext){
            RemoteApiExecutionContext wsContext = (RemoteApiExecutionContext)context;
            
            Session session = wsContext.getSession();
            workflowVariables.put(RSUITE_SESSION_ID, session.getKey());
            workflowVariables.put(SESSION_KEY, session.getKey());
            
        }
        
//		if (mo != null){
//			workflowVariables.put(MO_LIST_WORKFLOW_OBJECT, mo.getId());
//		}
        
        return workflowVariables;
    }

    public static ProcessInstanceSummaryInfo startWorkflowWithContext(ExecutionContext context,
            User user, ManagedObject mo, String processDefinitionName) throws RSuiteException {

        return startWorkflowWithContext(context, user, mo, processDefinitionName, null);
    }
    
    public static ProcessInstanceSummaryInfo startWorkflowWithContext(ExecutionContext context,
            User user, List<ManagedObject> moList, String processDefinitionName, Map<RSuiteWorkflowVariable, Object> variables)
            throws RSuiteException {

    	if (moList.size() < 1){
    		throw new RSuiteException("The managed object list cannot be empty");
    	}
    	
        Map<RSuiteWorkflowVariable, Object> workflowVariables = initializeVariables(context, variables, null);

        addUserVariables(user, workflowVariables);
        
        MoListWorkflowObject moListWorkflowObject = convertToMoListWorkflowObject(moList);
        
        workflowVariables.put(RSUITE_CONTENTS, moListWorkflowObject.toString());

        WorkflowJobContext jobContext = createJobContext(context, moList.get(0));

        File workfolder = createWorkFolder(jobContext);

        WorkflowStartContext startContext =
                createStartContext(processDefinitionName, workflowVariables, jobContext, workfolder);

        
        return createAndStartProcess(context, startContext);
    }
    

    private static MoListWorkflowObject convertToMoListWorkflowObject(
			List<ManagedObject> moList) {
		List<MoWorkflowObject> moWorkflowList= new ArrayList<>();
		for (ManagedObject mo : moList){
			moWorkflowList.add(new MoWorkflowObject(mo.getId()));
		}
		
		return  new MoListWorkflowObject(moWorkflowList);
	}

	public static ProcessInstanceSummaryInfo startWorkflowWithContext(ExecutionContext context,
            User user, ManagedObject mo, String processDefinitionName, Map<RSuiteWorkflowVariable, Object> variables)
            throws RSuiteException {

        Map<RSuiteWorkflowVariable, Object> workflowVariables = initializeVariables(context, variables, mo);

        addUserVariables(user, workflowVariables);
        workflowVariables.put(RSUITE_CONTENTS, mo.getId());

        WorkflowJobContext jobContext = createJobContext(context, mo);

        File workfolder = createWorkFolder(jobContext);

        WorkflowStartContext startContext =
                createStartContext(processDefinitionName, workflowVariables, jobContext, workfolder);

        
        return createAndStartProcess(context, startContext);
    }

    private static void addUserVariables(User user, Map<RSuiteWorkflowVariable, Object> variables) {
        variables.put(RSUITE_USER_ID, user.getUserId());
        variables.put(RSUITE_USER_FULL_NAME, user.getFullName());
        variables.put(RSUITE_USER_EMAIL_ADDRESS, user.getEmail());
    }

    private static ProcessInstanceSummaryInfo createAndStartProcess(ExecutionContext context,
            WorkflowStartContext startContext) throws RSuiteException {
        ProcessInstanceService processInstanceService = context.getProcessInstanceService();
        return processInstanceService.createAndStart(null, startContext);
    }

    private static WorkflowJobContext createJobContext(ExecutionContext context, ManagedObject mo)
            throws RSuiteException {
        String basePath =
                context.getConfigurationProperties().getProperty(PROPERTY_RSUITE_WORKFLOW_BASEWORKFOLDER,
                        "");

        WorkflowJobContext jobContext = new WorkflowJobContext(basePath);

        jobContext.setModule(removeAllNonAsciCharacters(mo.getDisplayName()));
        jobContext.setHotFolderPath(null);
        return jobContext;
    }

    private static String removeAllNonAsciCharacters(String value){
    	String subjectString = Normalizer.normalize(value, Normalizer.Form.NFD);
		return subjectString.replaceAll("[^\\x00-\\x7F]", "");
    }


}
