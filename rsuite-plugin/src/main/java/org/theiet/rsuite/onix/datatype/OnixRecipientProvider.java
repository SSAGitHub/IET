package org.theiet.rsuite.onix.datatype;

import java.util.List;

import org.theiet.rsuite.onix.OnixConstants;
import org.theiet.rsuite.utils.XpathUtils;

import com.reallysi.rsuite.api.*;
import com.reallysi.rsuite.api.extensions.ExecutionContext;
import com.reallysi.rsuite.api.forms.*;
import com.reallysi.rsuite.service.SearchService;
import com.rsicms.projectshelper.utils.ProjectContentAssemblyUtils;
import com.rsicms.projectshelper.utils.browse.ProjectBrowserUtils;

public class OnixRecipientProvider extends
		DefaultDataTypeOptionValuesProviderHandler implements OnixConstants {

	@Override
	public void provideOptionValues(
			DataTypeProviderOptionValuesContext context,
			java.util.List<DataTypeOptionValue> optionValues)
			throws RSuiteException {

		User user = context.getAuthorizationService().getSystemUser();
		optionValues.clear();

		List<ManagedObject> onixConfigurationsMOSet = getOnixConfigurationsMO(context);
		for (ManagedObject onixConfigurationsMO : onixConfigurationsMOSet) {
			ContentAssemblyItem onixConfigurationsCAItem = ProjectContentAssemblyUtils.
					getCAItem(context, user, onixConfigurationsMO.getId());
			List<ContentAssembly> recipientsCA = ProjectBrowserUtils.getChildrenCaByType(context, onixConfigurationsCAItem,
					CA_TYPE_ONIX_RECIPIENT);
			for (ContentAssembly recipientCA : recipientsCA) {				
				String recipientName = recipientCA.getDisplayName();
				if (recipientName != null) {
					optionValues.add(new DataTypeOptionValue(recipientName.trim(), recipientName));
				}				
			}			
		}
	}
	
	private List<ManagedObject> getOnixConfigurationsMO (ExecutionContext context) throws RSuiteException {
		SearchService searchScv = context.getSearchService();
		User user = context.getAuthorizationService().getSystemUser();
		
		String onixConfigQuery = "/rs_ca_map/rs_ca[rmd:get-type(.) = '" + CA_TYPE_ONIX_CONFIGURATIONS + "']";
		onixConfigQuery = XpathUtils.resolveRSuiteFunctionsInXPath(onixConfigQuery);
		
		List<ManagedObject> onixConfigurationsCASet = searchScv.executeXPathSearch(user, onixConfigQuery, 1, 0);
		
		return onixConfigurationsCASet;
	}

}