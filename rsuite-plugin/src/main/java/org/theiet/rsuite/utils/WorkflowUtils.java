package org.theiet.rsuite.utils;

import java.io.*;
import java.util.*;

import org.apache.commons.io.*;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.jbpm.*;
import org.jbpm.context.exe.VariableInstance;
import org.theiet.rsuite.IetConstants;

import com.reallysi.rsuite.api.*;
import com.reallysi.rsuite.api.extensions.ExecutionContext;
import com.reallysi.rsuite.api.remoteapi.*;
import com.reallysi.rsuite.api.workflow.*;
import com.reallysi.rsuite.service.*;

public final class WorkflowUtils implements IetConstants{

	private WorkflowUtils() {
	}
	
	public static void addCommentToWorkflow(ExecutionContext context,
			String processId, String commentText) throws RSuiteException {

		User user = context.getAuthorizationService().getSystemUser();
		ProcessInstanceService processService = context
				.getProcessInstanceService();

		CommentStream commentStream = processService.getCommentStream(
				processId, "rsuite.commentStream.global");
		if (commentStream == null) {
			context.getProcessInstanceService().createCommentStream(processId,
					"rsuite.commentStream.global", "Comments");
		}

		context.getProcessInstanceService().addCommentToStream(user, processId,
				"rsuite.commentStream.global", commentText);

	}

	public static ProcessInstanceSummaryInfo startWorkflow(ExecutionContext context,
			User user, String rsuiteId, String processDefinitionName)
			throws RSuiteException {
		Map<String, Object> workflowVars = new HashMap<String, Object>();		
		return startWorkflow(context, user, rsuiteId, processDefinitionName, workflowVars);
	}

	public static ProcessInstanceSummaryInfo startWorkflow(ExecutionContext context,
			User user, String rsuiteId, String processDefinitionName, Map<String, Object> workflowVars)
			throws RSuiteException {		
		
		workflowVars.put(WF_VAR_RSUITE_CONTENTS, rsuiteId);
		
		return context.getProcessInstanceService().createAndStart(user,
				processDefinitionName, workflowVars);
	}

	   public static ProcessInstanceSummaryInfo startWorkflowWithContext(
	            ExecutionContext context, User user, ManagedObject mo, String processDefinitionName
	            ) throws RSuiteException {
	    
	       return startWorkflowWithContext(context, user, mo, processDefinitionName, null);
	   }
	
	public static ProcessInstanceSummaryInfo startWorkflowWithContext(
            ExecutionContext context, User user, ManagedObject mo, String processDefinitionName,
            Map<String, Object> variables) throws RSuiteException {

        ProcessInstanceService processInstanceService = context
                .getProcessInstanceService();

        String basePath = context.getConfigurationProperties().getProperty(
                "rsuite.workflow.baseworkfolder", "");

        WorkflowJobContext jobContext = new WorkflowJobContext(basePath);

        String module = createModuleName(mo);        
        jobContext.setModule(module);
        jobContext.setHotFolderPath(null);

        File workfolder = new File(jobContext.getWorkFolderPath());

        if (!workfolder.exists()) {
            workfolder.mkdirs();
            File tempfolder = new File (workfolder.getParentFile(), "temp");
            tempfolder.mkdirs();
        }

        WorkflowStartContext startContext = new WorkflowStartContext();
        startContext.setName(processDefinitionName);

        if (variables != null) {
            for (String name : variables.keySet()){
                startContext.addVariable(name, variables.get(name));
            }
        }

        startContext.addContextVariable(jobContext);
        // startContext.addVariable

        startContext.addVariable("rsuite contents",
                mo.getId());
        startContext.addVariable(WorkflowConstants.VAR_WORKINGFOLDER_PATH,
                workfolder.getAbsolutePath());

        startContext.addVariable("rsuiteUserFullName", user.getFullName());
        startContext.addVariable(WF_VAR_RSUITE_USER_ID, user.getUserId());
        startContext.addVariable("rsuiteUserEmailAddress", user.getEmail());
        

        return processInstanceService.createAndStart(null, startContext);
	    
	}

    protected static String createModuleName(ManagedObject mo) throws RSuiteException {
        String module = mo.getDisplayName();
        module = module.replaceAll("[\\W]+", "_");
        return module;
    }
	
	public static ProcessInstanceSummaryInfo startWorkflowWithContextFromWS(
			RemoteApiExecutionContext context, String processDefinitionName,
			ManagedObject mo, Map<String, Object> variables) throws RSuiteException {

	    if (variables == null){
	        variables = new HashMap<String, Object>();
	    }
	    
	    Session session = context.getSession();
        User user = session.getUser();
	    
	    variables.put("rsuiteSessionId", session.getKey());
	    variables.put("skey", session.getKey());

		return startWorkflowWithContext(context, user, mo, processDefinitionName, variables);
	}

	public static ProcessInstanceSummaryInfo startWorkflowForMoInHotfolder(
			ExecutionContext context, String processDefinitionName,
			ManagedObject mo, Map<String, Object> variables) throws IOException, RSuiteException {

		ProcessInstanceService processInstanceService = context
				.getProcessInstanceService();

		String basePath = context.getConfigurationProperties().getProperty(
				"rsuite.workflow.baseworkfolder", "");

		WorkflowJobContext jobContext = new WorkflowJobContext(basePath);

		jobContext.setModule(mo.getDisplayName());
		jobContext.setHotFolderPath(null);

		File workfolder = new File(jobContext.getWorkFolderPath());

		if (!workfolder.exists()) {
			workfolder.mkdirs();
		}

		File workflowFile = new File(workfolder.getPath(), mo.getDisplayName());

		FileUtils.writeByteArrayToFile(workflowFile,
				IOUtils.toByteArray(mo.getInputStream()));

		File tempWorkflowFile = new File(jobContext.getTempFolderPath(),
				workflowFile.getName());
		FileUtils.copyFile(workflowFile, tempWorkflowFile);

		jobContext.setSourceFilePath(tempWorkflowFile.getAbsolutePath());

		WorkflowStartContext startContext = new WorkflowStartContext();
		startContext.setName(processDefinitionName);

		if (variables != null) {
			for (String name : variables.keySet())
				startContext.addVariable(name, variables.get(name));
		}

		// zip file business object is creating and be put into process context.
		FileWorkflowObject bo = new FileWorkflowObject();

		bo.setFile(workflowFile);

		startContext.addContextVariable(bo);

		startContext.addContextVariable(jobContext);

		startContext.addVariable(WorkflowConstants.VAR_SOURCEFILE_PATH,
				workflowFile.getName());
		startContext.addVariable(WorkflowConstants.VAR_WORKINGFOLDER_PATH,
				workfolder.getAbsolutePath());

		context.getManagedObjectService().checkOut(
				context.getAuthorizationService().getSystemUser(), mo.getId());
		context.getManagedObjectService().remove(
				context.getAuthorizationService().getSystemUser(), mo.getId());


		return processInstanceService.createAndStart(null, startContext);
	}

//	public static String constructWfReportLink(String msg, String pId) {
//		StringBuffer sb = new StringBuffer(msg).append(" see ")
//				.append("<a href=\"")
//				.append(constructWfReportURL(pId))
//				.append("\" target=\"_blank\"").append(">workflow log</a>");
//		return sb.toString();
//	}
//
//	public static String constructWfReportURL(String pId) {
//		StringBuffer sb = new StringBuffer(REST_V1_URL_ROOT)
//				.append("/workflow/process/log/")
//				.append(pId)
//				.append("?skey=RSUITE-SESSION-KEY");
//
//		return sb.toString();
//	}
	
	
	public static File writeWorflowTempFile (WorkflowExecutionContext context, String fileName, byte[] fileContent) throws RSuiteException {
		File tempDir = com.rsicms.projectshelper.workflow.WorkflowUtils.createWorkflowTempFolder(context);
		File mergedOnixFile = new File(tempDir.getAbsolutePath() + "/" + fileName);
		try {			
			FileOutputStream fos = new FileOutputStream(mergedOnixFile);
			IOUtils.write(fileContent, fos);
			fos.flush();
			fos.close();
		} catch (Exception ex) {
			throw new RSuiteException(RSuiteException.ERROR_NOT_DEFINED, ex.getMessage(), ex);
		}
		
		return mergedOnixFile;
	}
	
	public static File writeWorflowTempFile (WorkflowExecutionContext context, String fileName, String fileContent) throws RSuiteException {
		try {
			return writeWorflowTempFile(context, fileName, fileContent.getBytes(UTF_8));
		} catch (UnsupportedEncodingException ex) {
			throw new RSuiteException(RSuiteException.ERROR_NOT_DEFINED, ex.getMessage(), ex);			
		}
	}
	
	

	public static void startWorkflow(ExecutionContext context, User user,  String workflowName, String rsuiteId, String rsuitePath, Map<String, Object> additionalVariables) throws RSuiteException{
	 
	    ManagedObjectService moSvc = context.getManagedObjectService();
        ManagedObject mo = moSvc.getManagedObject(user, rsuiteId);
       
       Map<String, Object> variables = new HashMap<String, Object>();
       
       if (StringUtils.isNotBlank(rsuitePath)){
           variables.put("rsuitePath", rsuitePath);  
       }
       
       variables.put("com.reallysi.rsuite.api.workflow.MoListWorkflowObject ", rsuiteId);
       
       if (additionalVariables != null){
           variables.putAll(additionalVariables);
       }
	    
       startWorkflowWithContext(context, user, mo, workflowName, variables);
	}

	public static void startWorkflowForMoFromWS(RemoteApiExecutionContext context, CallArgumentList args, String workflowName, Map<String, Object> additionalVariables) throws RSuiteException{
		   Session sess = context.getSession();
	       User user = sess.getUser();
	       
	       String rsuiteId = args.getFirstValue("sourceId");

	       if (StringUtils.isBlank(rsuiteId)){
	    	   rsuiteId = args.getFirstValue("rsuiteId");
	       }
	       
			ManagedObjectService moSvc = context.getManagedObjectService();
			ManagedObject mo = moSvc.getManagedObject(user, rsuiteId);
	       
	       Map<String, Object> variables = new HashMap<String, Object>();
	       String rsuitePath = args.getFirstString("rsuiteBrowseUri");
	       if (StringUtils.isNotBlank(rsuitePath)){
	    	   variables.put("rsuitePath", rsuitePath);  
	       }
	       
	       variables.put("com.reallysi.rsuite.api.workflow.MoListWorkflowObject	", rsuiteId);
	       
	       if (additionalVariables != null){
	    	   variables.putAll(additionalVariables);
	       }
	       
	       startWorkflowWithContextFromWS(context, workflowName, mo , variables);
	   }
	
	
	/**
	 * Return true if there is one task assigned to rsuiteId and related to
	 * rsuiteTaskName.
	 * 
	 * @param context
	 * @param user
	 * @param rsuiteId
	 * @param rsuiteTaskName
	 * @return boolean
	 * @throws RSuiteException
	 */
	public static boolean isAlreadyInWorkflow(ExecutionContext context,
			User user, String rsuiteId, String rsuiteTaskName)
			throws RSuiteException {

		List<String> taskList = new ArrayList<String>();

		if (rsuiteTaskName != null) {
			taskList.add(rsuiteTaskName);
		}

		return isAlreadyInWorkflow(rsuiteId, taskList);

	}

	@SuppressWarnings("rawtypes")
	public static boolean isAlreadyInWorkflow(String rsuiteId,
			List<String> rsuiteTaskNames) throws RSuiteException {

		JbpmContext jbpmContext = getJbpmContext();
		try {
			Query query = createQuery(jbpmContext, rsuiteTaskNames, rsuiteId);

			Iterator tasks = query.list().iterator();

			if (tasks.hasNext()) {
				return true;
			}

			return false;
		} finally {
			jbpmContext.close();
		}
	}

	@SuppressWarnings("rawtypes")
	public static Set<String> getRSuiteObjectIdsRelatedWithActiveWorkflow(
			List<String> rsuiteTaskNames) throws RSuiteException {
		Set<String> rsuiteIds = new HashSet<String>();

		JbpmContext jbpmContext = getJbpmContext();
		try {

			Query query = createQuery(jbpmContext, rsuiteTaskNames, null);
			Iterator tasks = query.list().iterator();

			while (tasks.hasNext()) {
				VariableInstance vi = (VariableInstance) tasks.next();
				rsuiteIds.add(vi.getValue().toString());
			}

			return rsuiteIds;
		} finally {
			jbpmContext.close();
		}
	}


	/**
	 * Return true if there is one task assigned to rsuiteId and related to
	 * rsuiteTaskName.
	 * 
	 * @param context
	 * @param processingUser
	 * @param rsuiteId
	 * @param rsuiteTaskName
	 * @return boolean
	 * @throws RSuiteException
	 */
	private static Query createQuery(JbpmContext jbpmContext,
			List<String> rsuiteTaskNames, String rsuiteId) {

		StringBuilder taskCondition = new StringBuilder();

		if (rsuiteTaskNames != null) {
			Iterator<String> it = rsuiteTaskNames.iterator();

			if (it.hasNext()) {
				it.next();

				taskCondition.append(" and (ti.name = ? ");

				while (it.hasNext()) {
					it.next();

					taskCondition.append("or ti.name = ? ");
				}

				taskCondition.append(" ) ");
			}
		}

		String sqlQuery = "select vi "
				+ "  from org.jbpm.taskmgmt.exe.TaskInstance as ti, org.jbpm.context.exe.VariableInstance as vi  "
				+ "where ti.token = vi.token and " + " vi.name = 'rsuiteId' "
				+ taskCondition.toString();

		if (rsuiteId != null) {
			sqlQuery += " and vi.value ='" + rsuiteId + "'";
		}

		org.hibernate.Query query = jbpmContext.getSession().createQuery(
				sqlQuery);

		if (rsuiteTaskNames != null) {
			for (int i = 0; i < rsuiteTaskNames.size(); i++) {

				query.setString(i, rsuiteTaskNames.get(i));
			}

		}

		return query;
	}
	
	public static boolean isAlreadyInWorkflow(String rsuiteId,
			String workflowName) throws RSuiteException {

		JbpmContext jbpmContext = getJbpmContext();
		try {
			return isRsuiteRelatedWithActiveWorkflow(jbpmContext, workflowName,
					rsuiteId);
		} finally {
			jbpmContext.close();
		}
	}
	
	public static List<String> getRelatedWorkflowIdsWithRSuiteId(String rsuiteId) throws RSuiteException {

		JbpmContext jbpmContext = getJbpmContext();
		try {
			return getRelatedActiveWorkflowWithRSuiteId(jbpmContext, rsuiteId);
		} finally {
			jbpmContext.close();
		}
	}

	public static Set<String> getRelatedRSuiteIdsWithActiveWorkflow(
			String workflowName) {

		JbpmContext jbpmContext = getJbpmContext();
		try {
			return getRelatedRSuiteIdsWithActiveWorkflow(jbpmContext,
					workflowName);
		} finally {
			jbpmContext.close();
		}
	}

	@SuppressWarnings("rawtypes")
	private static Set<String> getRelatedRSuiteIdsWithActiveWorkflow(
			JbpmContext jbpmContext, String workflowName) {

		Set<String> resultSet = new HashSet<String>();

		Query query = createActiveWorkflowQuery(jbpmContext, workflowName, null);

		Iterator reslutIterator = query.list().iterator();

		while (reslutIterator.hasNext()) {
			Object[] result = (Object[]) reslutIterator.next();
			resultSet.add(result[0].toString());
		}

		return resultSet;
	}
	
	@SuppressWarnings("rawtypes")
	private static List<String> getRelatedActiveWorkflowWithRSuiteId(
			JbpmContext jbpmContext, String rsuiteId) {

		List<String> resultSet = new ArrayList<String>();

		Query query = createActiveWorkflowQuery(jbpmContext, null, rsuiteId);

		Iterator reslutIterator = query.list().iterator();

		while (reslutIterator.hasNext()) {
			Object[] result = (Object[]) reslutIterator.next();
			resultSet.add(result[1].toString());
		}

		return resultSet;
	}

	@SuppressWarnings("rawtypes")
	private static boolean isRsuiteRelatedWithActiveWorkflow(
			JbpmContext jbpmContext, String workflowName, String rsuiteId) {

		Query query = createActiveWorkflowQuery(jbpmContext, workflowName,
				rsuiteId);

		Iterator reslutIterator = query.list().iterator();

		return reslutIterator.hasNext();
	}

	private static Query createActiveWorkflowQuery(JbpmContext jbpmContext,
			String workflowName, String rsuiteId) {
		String hqlQuery = "select distinct vi.value, pi.id "
				+ "  from org.jbpm.graph.exe.ProcessInstance pi, ";

		if (StringUtils.isNotEmpty(workflowName)) {
			hqlQuery += " org.jbpm.graph.def.ProcessDefinition pd, ";
		}

		hqlQuery += " org.jbpm.context.exe.VariableInstance vi  " + " where "
				+ " vi.processInstance.id = pi.id"

				+ " and pi.start is not null and pi.end is null"
				+ " and vi.name = 'rsuite contents'";

		if (StringUtils.isNotEmpty(workflowName)) {
			hqlQuery += " and  pi.processDefinition.id = pd.id"
					+ " and pd.name = :workflowName";
		}

		if (StringUtils.isNotEmpty(rsuiteId)) {
			hqlQuery += " and vi.value = :rsuiteId";
		}

		Query query = jbpmContext.getSession().createQuery(hqlQuery);

		if (StringUtils.isNotEmpty(rsuiteId)) {
			query.setParameter("rsuiteId", rsuiteId);
		}

		if (StringUtils.isNotEmpty(workflowName)) {
			query.setParameter("workflowName", workflowName);
		}

		return query;
	}
	
	private static JbpmContext getJbpmContext() {
		JbpmContext jbpmContext = JbpmConfiguration.getInstance()
				.createJbpmContext();
		return jbpmContext;
	}

	@SuppressWarnings("unchecked")
	public static Object getWorkflowVariable(ProcessInstanceInfo processInstance,
			String variableToFind) {
		List<VariableInfo> variables = processInstance.getVariables();
		for (VariableInfo variable : variables){
			if (variableToFind.equals(variable.getName())){
				return variable.getValue();
			}
		}
		
		return "";
	}
}
