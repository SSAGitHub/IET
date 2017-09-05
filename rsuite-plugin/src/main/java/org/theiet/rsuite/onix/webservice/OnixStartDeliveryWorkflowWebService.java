package org.theiet.rsuite.onix.webservice;

import java.util.*;
import java.util.Map.Entry;

import org.apache.commons.logging.*;
import org.theiet.rsuite.onix.OnixConstants;
import org.theiet.rsuite.onix.domain.*;
import org.theiet.rsuite.utils.*;

import com.reallysi.rsuite.api.*;
import com.reallysi.rsuite.api.remoteapi.*;
import com.reallysi.rsuite.api.remoteapi.result.*;
import com.reallysi.rsuite.service.ContentAssemblyService;
import com.rsicms.projectshelper.utils.browse.ProjectBrowserUtils;

public class OnixStartDeliveryWorkflowWebService extends DefaultRemoteApiHandler implements OnixConstants {

	
	private static Log log = LogFactory
			.getLog(OnixStartDeliveryWorkflowWebService.class);

	@Override
	public RemoteApiResult execute(RemoteApiExecutionContext context,
			CallArgumentList args) throws RSuiteException {

		int numberOfStartedWF = 0;
		try {
			numberOfStartedWF = startOnixDeliveryWorkflows(context, args);

		} catch (Exception ex) {
			log.error(ex.getLocalizedMessage(), ex);
			return new MessageDialogResult(MessageType.ERROR,
					getWebserviceLabel(), "Server returned: " + ex.getMessage());

		}
		
		return getRemoteApiResult(getSuccessMessage(numberOfStartedWF));
	}
	


	protected int startOnixDeliveryWorkflows(
			RemoteApiExecutionContext context, CallArgumentList args)
			throws Exception {

		User user = context.getSession().getUser();

		OnixConfiguration onixConfiguration = new OnixConfiguration(context);
		
		Map<String, Map<String, String>> recipietnsToDelivery = OnixDelivery.getRecipientOnixUnitsToDelivery(
				context, user, onixConfiguration);		
		
		
		String specificRecipient = getSpecificRecipient(context, args, user);
		
		int numberOfStartedWf = startDeliveryWorkflows(context, args, user,
				onixConfiguration, recipietnsToDelivery, specificRecipient);
	
		return numberOfStartedWf;
	}



	public int startDeliveryWorkflows(RemoteApiExecutionContext context,
			CallArgumentList args, User user,
			OnixConfiguration onixConfiguration,
			Map<String, Map<String, String>> recipietnsToDelivery,
			String specificRecipient)
			throws RSuiteException {
		ContentAssembly onixConfigCa = onixConfiguration.getDefaultConfiguration().getOnixConfigurationsCa();
		
		int numberOfStartedWf = 0;
		
		for (Entry<String, Map<String, String>> recipientToDelivery : recipietnsToDelivery.entrySet()) {
			String recipientName = recipientToDelivery.getKey();
			
			if (specificRecipient != null && !specificRecipient.equals(recipientName)){
				continue;
			}
			
			Map<String, String> onixUnits = recipientToDelivery.getValue();
			ContentAssembly recipientCa = ProjectBrowserUtils.getChildCaByNameAndType(context, onixConfigCa, CA_TYPE_ONIX_RECIPIENT, recipientName);
			Map <String, Object> workflowVars =  createWorkflowVars(recipientName, onixUnits);
			WorkflowUtils.startWorkflow(context, user, recipientCa.getId(), WF_PD_ONIX_DELIVERY, workflowVars);
			numberOfStartedWf++;
		}
		return numberOfStartedWf;
	}

	public String getSpecificRecipient(RemoteApiExecutionContext context,
			CallArgumentList args, User user) throws RSuiteException {
		String specificRecipient = null;
		String oneVendorMode = args.getFirstString("oneVendorMode");
		
		if ("true".equals(oneVendorMode)){
			String rsuiteId = args.getFirstString("rsuiteId");
			ContentAssemblyService caSvc = context.getContentAssemblyService();
			ContentAssembly ca = caSvc.getContentAssembly(user, rsuiteId);
			specificRecipient = ca.getDisplayName();
		}
		return specificRecipient;
	}
	
	private Map<String, Object> createWorkflowVars(String recipientName, Map<String, String> onixUnits) {
		Map<String, Object> workflowVars = new HashMap<String, Object>();
		workflowVars.put(WF_VAR_RECIPIENT_NAME, recipientName);
		workflowVars.put(WF_VAR_ONIX_UNITS, onixUnits);
		return workflowVars;
	}
	
	private RemoteApiResult getRemoteApiResult (String successMessage) {
		return ContentDisplayUtils.getResultWithLabelRefreshing(
				MessageType.SUCCESS, getWebserviceLabel(), successMessage, dialogWidth(), "");
	}

	private String getWebserviceLabel () {
		return "Onix Delivery Workflow";
	}

	private String getSuccessMessage (int numberOfWorflows) {
		return  " Onix delivery worflow(s) have been started for " + numberOfWorflows + " vendor(s)";
	}

	private String dialogWidth () {
		return "500";
	}

}