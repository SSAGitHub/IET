package org.theiet.rsuite.onix.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.theiet.rsuite.utils.XpathUtils;

import com.reallysi.rsuite.api.ContentAssembly;
import com.reallysi.rsuite.api.ManagedObject;
import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.ReferenceInfo;
import com.reallysi.rsuite.api.User;
import com.reallysi.rsuite.api.control.DependencyTracker;
import com.reallysi.rsuite.api.extensions.ExecutionContext;
import com.reallysi.rsuite.service.ContentAssemblyService;
import com.reallysi.rsuite.service.SearchService;

public class OnixConfiguration {

	private ExecutionContext context;

	private User user;

	private Map<String, OnixRecipientConfiguration> recipientsMap = new HashMap<String, OnixRecipientConfiguration>();

	private OnixDefaultConfiguration defaultConfiguration;

	private List<String> onixConfigurationIDs;

	private Set<String> supportedBookCaTypes;

	public OnixConfiguration(ExecutionContext context) throws RSuiteException {
		this.context = context;
		user = context.getAuthorizationService().getSystemUser();

		defaultConfiguration = OnixDefaultConfiguration.getInstance(context);

		setUpOnixConfigurationIDs();

		for (ContentAssembly recipientConfigurationCa : getRecipients()) {

			OnixRecipientConfiguration recipientConfiguration = new OnixRecipientConfiguration(
					context, recipientConfigurationCa, defaultConfiguration);
			recipientsMap.put(recipientConfigurationCa.getDisplayName(),
					recipientConfiguration);
		}
		
		supportedBookCaTypes = new HashSet<String>();
		supportedBookCaTypes.add("book");
		supportedBookCaTypes.add("standardsBookEdition");
	}

	public OnixRecipientConfiguration getRecipientConfiguration(
			String recipientName) {
		return recipientsMap.get(recipientName);
	}

	public OnixDefaultConfiguration getDefaultConfiguration() {
		return defaultConfiguration;
	}

	public Set<String> getRecipientNames() {
		return recipientsMap.keySet();
	}

	private List<ContentAssembly> getRecipients() throws RSuiteException {

		List<ContentAssembly> recipientList = new ArrayList<ContentAssembly>();
		ContentAssemblyService caSvc = context.getContentAssemblyService();
		SearchService searchSvc = context.getSearchService();
		String query = "/rs_ca_map/rs_ca[rmd:get-type(.) = ('onixRecipient')]";

		query = XpathUtils.resolveRSuiteFunctionsInXPath(query);
		List<ManagedObject> results = searchSvc.executeXPathSearch(user, query,
				0, 20000);

		for (ManagedObject mo : results) {
			ContentAssembly recipientCa = caSvc.getContentAssembly(user,
					mo.getId());

			recipientList.add(recipientCa);

		}

		return recipientList;
	}

	public boolean isInOnixConfiguration(String browserUri) {
		for (String onixConfigurationCaId : onixConfigurationIDs) {
			if (browserUri.contains(":" + onixConfigurationCaId + "/")) {
				return true;
			}
		}

		return false;
	}

	private void setUpOnixConfigurationIDs() throws RSuiteException {

		SearchService searchSvc = context.getSearchService();
		String query = "/rs_ca_map/rs_ca[rmd:get-type(.) = ('onixConfigurations')]";

		query = XpathUtils.resolveRSuiteFunctionsInXPath(query);

		List<ManagedObject> results = searchSvc.executeXPathSearch(user, query,
				0, 20000);

		DependencyTracker dt = context.getManagedObjectService()
				.getDependencyTracker();

		onixConfigurationIDs = new ArrayList<String>();
		for (ManagedObject mo : results) {
			onixConfigurationIDs.add(mo.getId());
			List<ReferenceInfo> rtr = dt.listDirectReferences(user, mo.getId());
			for (ReferenceInfo ri : rtr) {
				onixConfigurationIDs.add(ri.getId());
			}
		}

		Collections.reverse(onixConfigurationIDs);

	}
	
	public Set<String> getSupportedBookTypes(){
		
		return supportedBookCaTypes;
	}
}
