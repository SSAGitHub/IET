package org.theiet.rsuite.actionhandlers;

import java.io.File;
import java.util.List;

import org.apache.commons.logging.Log;
import org.theiet.rsuite.IetConstants;
import org.theiet.rsuite.books.BooksConstans;
import org.theiet.rsuite.utils.IetUtils;

import com.reallysi.rsuite.api.ContentAssembly;
import com.reallysi.rsuite.api.ManagedObject;
import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.workflow.AbstractBaseActionHandler;
import com.reallysi.rsuite.api.workflow.MoListWorkflowObject;
import com.reallysi.rsuite.api.workflow.ProcessInstanceInfo;
import com.reallysi.rsuite.api.workflow.TaskInfo;
import com.reallysi.rsuite.api.workflow.TokenInfo;
import com.reallysi.rsuite.api.workflow.WorkflowExecutionContext;
import com.rsicms.rsuite.helpers.upload.RSuiteFileLoadHelper;
import com.rsicms.rsuite.helpers.upload.RSuiteFileLoadOptions;
import com.rsicms.rsuite.helpers.upload.RSuiteFileLoadResult;

public class ImportBookFiles extends AbstractBaseActionHandler {

	/**
	 * Specifies the name of the task node to signal when the import is complete.
	 * If specified, can also specify the taskTransition parameter to 
	 * set the name of the transition to signal. Note that this is the name
	 * of the jBPM <i>node</i>, not the task contained by the node. The 
	 * process will complete all the tasks contained by the node.
	 */
	public static final String PARAM_TASK_NODE_TO_SIGNAL = "taskNodeToSignal";

	public static final String PARAM_TASK_TO_COMPLETE = "taskToComplete";
	/**
	 * If specified, names the transition of the task named by the {@link PARAM_TASK_NODE_TO_SIGNAL}
	 * parameter. If unspecified, then the normal jBPM default transition rules will be used,
	 * namely first or only transition out of the task (in the order the transitions occur
	 * in the XML process definition document).
	 */
	public static final String PARAM_TASK_TRANSITION = "taskTransition";
	/**
	 * 
	 */
	private static final long serialVersionUID = 6311669272674947729L;

	@Override
	public void execute(WorkflowExecutionContext context) throws Exception {
		Log wfLog = context.getWorkflowLog();
		wfLog.info("START import");
		
		String taskNodeToSignal = 
				resolveVariablesAndExpressions(
						context, 
						getParameterWithDefault(PARAM_TASK_NODE_TO_SIGNAL, null));

		String taskTransition = 
				resolveVariablesAndExpressions(
						context, 
						getParameterWithDefault(PARAM_TASK_TRANSITION, null));

		String taskToComplete = 
				resolveVariablesAndExpressions(
						context, 
						getParameterWithDefault(PARAM_TASK_TO_COMPLETE, null));
		
		File fileToImport = context.getFileWorkflowObject().getFile();

		
		String fileName = fileToImport.getName();
		
		// Get the product ID from the incoming Zip filename. From this
		// we can find the CA for the book that has that product ID
		
		String productId = IetUtils.getProductIdFromFilename(fileName);
		if (productId == null || "".equals(productId)) {
			logError(
					context, 
					wfLog, 
					"Failed to determine product ID from Zip filename \"" + fileName + "\". Cannot continue.");
			return;
		}
		
		wfLog.info("Got product ID \"" + productId + "\" from the incoming Zip file.");
		// Find the Content assembly of type "book" with the specified product ID:
	
		ContentAssembly bookCa = null;
		
		wfLog.info("Looking for corresponding book content assembly item...");
		
//		String xpath = "//(rs_canode|rs_ca)[rmd:get-type(.) = '" + IetConstants.CA_TYPE_BOOK + "']" +
//				"[rmd:get-lmd-value(., " + IetConstants.LMD_FIELD_PRODUCT_CODE + ") = " + productId + "]";
		
		String rql = "select * from RSUITE:CONTENT-ASSEMBLY,RSUITE:CANODE where " +
				"lmd::" + IetConstants.LMD_FIELD_CONTENT_ASSEMBLY_TYPE + "='book' and " +
				"lmd::" + BooksConstans.LMD_FIELD_BOOK_PRODUCT_CODE + "='" + productId + "'";
		
		wfLog.info("RQL query=" + rql);
//		wfLog.info("XPath query=" + xpath);
//		List<ManagedObject> mos = context.getSearchService()
//				.executeXPathSearch(
//						getSystemUser(), 
//						xpath,
//						1, 
//						100);
		List<ManagedObject> mos = null;
/*
 * Above line and this block temporary fix for 3.7. Harv Greenberg		
		List<ManagedObject> mos = context.getSearchService()
				.executeRqlSearch(
						getSystemUser(), 
						rql,
						1, 
						100);
*/
		wfLog.info("mos.size()=" + mos.size());
		// RQL query always returns first node as an empty MO object.
		if (mos.size() > 1) {
			if (mos.size() > 2) {
				wfLog.warn("Found " + mos.size() + " content assembly items with CA type " +
						"of 'book' and product ID of \"" + productId + "\". " +
						"There should be at most 1");
			}
			ManagedObject caMo = mos.get(1);
			String moId = caMo.getId();
			if (moId == null || "".equals(moId)) {
				logError(
						context, 
						wfLog, 
						"Failed to find a book content assembly for product ID \"" + productId + "\". Cannot continue;");			
				return;
				
			}
			if (!caMo.isAssemblyNode()) {
				logError(
						context, 
						wfLog, 
						"Managed object [" + caMo.getId() + "] should be a content assembly node but it's not. Cannot continue.");
				return;

			}
			bookCa = context.getContentAssemblyService()
					   .getContentAssemblyById(getSystemUser(), 
							                   moId);
		}
		if (bookCa == null) {
			logError(
					context, 
					wfLog, 
					"Failed to find a book content assembly for product ID \"" + productId + "\". Cannot continue;");			
			return;
		}
		
		wfLog.info("Found book content assembly [" + bookCa.getId() + "] " +
				bookCa.getDisplayName() + " for product ID \"" + productId + "\"");
		// Get the process ID from the LMD on the book content assembly
		String parentPID = bookCa.getLayeredMetadataValue(IetConstants.LMD_FIELD_PID);
		
		// Make the book CA the context item for the workflow.
		MoListWorkflowObject moList = new MoListWorkflowObject();
		moList.addMoIfNotPresent(bookCa.getId());
		context.setMoListWorkflowObject(moList);
		
		wfLog.info("Loading Zip contents...");
		RSuiteFileLoadOptions options = 
				new RSuiteFileLoadOptions(getSystemUser());

		try {
			RSuiteFileLoadResult results = RSuiteFileLoadHelper.loadZipContentsToCaNodeContainer(
					context, 
					fileToImport, 
					bookCa, 
					options);
			if (results.hasErrors()) {
				logError(
						context, 
						wfLog, 
						"Errors loading Zip file contents. Not signaling book workflow.");
				// FIXME: This is a quick hack. Really need a separate method on the helper
				// to output the result messages to a log.
				logError(context, wfLog, RSuiteFileLoadHelper.formatLoadResultMessagesAsHtml(options));
			} else {
				if (taskNodeToSignal != null && !"".equals(taskNodeToSignal.trim())) {

					signalBookWorkflow(
							context, 
							parentPID, 
							taskNodeToSignal, 
							taskToComplete,
							taskTransition);
				}

			}
		} catch (Exception e) {
			logError(
					context, 
					wfLog, 
					"Unhandled exception doing zip load: " + e.getClass().getSimpleName() + " - " + e.getMessage());						
		}
		
		wfLog.info("Zip load complete.");
		
		wfLog.info("Done.");
	}

	private void signalBookWorkflow(
			WorkflowExecutionContext context,
			String parentPID, 
			String taskNodeToSignal, 
			String taskToComplete, 
			String taskTransition) throws RSuiteException {
		Log wfLog = context.getWorkflowLog();
		
		if (parentPID == null || "".equals(parentPID)) {
			logError(
					context, 
					wfLog, 
					"No parent process ID value on book content assembly. Cannot signal book workflow");
			return;
		}
	
		wfLog.info("Found parent process instance ID \"" + parentPID + "\" for the book.");

		ProcessInstanceInfo parentProcess = context.getProcessInstanceService()
				.getProcessInstance(getSystemUser(), parentPID);
		
		if (parentProcess == null) {
			logError(
					context, 
					wfLog, 
					"Failed to find parent process with ID \"" + parentPID + "\". Cannot continue.");
			return;
		}

		wfLog.info("Found parent process instance with ID \"" + parentPID + "\".");
		
		TokenInfo tokenForTask = null;
		// NOTE: The taskNodeToSignal value is the name of *task node*, not the
		// task, which may have a completely different name.
		for (TokenInfo token : parentProcess.getTokens()) {
			if (token.getNodeName().equals(taskNodeToSignal)) {
				tokenForTask = token;
				break;
			}
		}
		
		if (tokenForTask == null) {
			wfLog.warn("Failed to find a token on the node named \"" + taskNodeToSignal + "\"");
			return;
		}
		
		wfLog.info("Found token pointing to node \"" + taskNodeToSignal + "\"");
		
		// Now we need to get the Task (not task node) associated with the task node
		// we have. RSuite enforces having at most one (or exactly one) Task per task node
		// because of the way the completeTask() method works
			
		TaskInfo theTask = null;
		
		// WEK: This code depends on task names being unique within a process instance
		// as there's no way I can find to go from a task node to its associated task(s).
		// So there is no choice but to look up tasks by name. 
		@SuppressWarnings("rawtypes")
		List tasks = parentProcess.getTasks();
		for (Object taskObj : tasks) {
			TaskInfo taskInfo = (TaskInfo)taskObj;
			if (taskInfo.getName().equals(taskToComplete)) {
				theTask = taskInfo;
				break;
			}
		}
		
		if (theTask == null) {
			wfLog.warn("Failed to find a task named \"" + taskToComplete + "\". Cannot transition book workflow.");
			return;
		}
	    
		
		wfLog.info("Completing task \"" + taskNodeToSignal + "\" on " +
						(taskTransition == null ? 
								"first transition..." : 
								"transition \"" + taskTransition + "\"")
		);
		
		if (theTask.getActorId() == null) {
			context.getTaskService().acceptTask(
					getSystemUser(), 
					String.valueOf(theTask.getTaskInstanceId()), 
					"Accepted by ingestion workflow");
		}
		context.getTaskService()
			.completeTask(
					getSystemUser(), 
					String.valueOf(theTask.getTaskInstanceId()), 
					taskTransition);
		
			
	}

	public void logError(
			WorkflowExecutionContext context, 
			Log wfLog,
			String message) throws RSuiteException {
		wfLog.error(message);
		context.setVariable("EXCEPTION_OCCUR", "true");
		context.setVariable("EXCEPTION_TYPE", "EXCEPTION_TYPE_BUSINESSRULE");
		context.setGlobalVariable("failureDetail", message);	}
	
	public void setTaskNodeToSignal(String taskNodeToSignal) {
		this.setParameter(PARAM_TASK_NODE_TO_SIGNAL, taskNodeToSignal);
	}
	
	public void setTaskToComplete(String taskToComplete) {
		this.setParameter(PARAM_TASK_TO_COMPLETE, taskToComplete);
	}
	
	public void setTaskTransition(String taskTransition) {
		this.setParameter(PARAM_TASK_TRANSITION, taskTransition);
	}
	
}
