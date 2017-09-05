package org.theiet.rsuite.onix.domain;

import java.util.*;
import java.util.Map.Entry;

import org.theiet.rsuite.onix.OnixConstants;

import com.reallysi.rsuite.api.*;
import com.reallysi.rsuite.api.extensions.ExecutionContext;
import com.reallysi.rsuite.service.*;
import com.rsicms.projectshelper.utils.browse.ProjectBrowserUtils;

public class OnixDelivery implements OnixConstants {

	
	
	public static Map<String, Map<String, String>> getRecipientOnixUnitsToDelivery(
			ExecutionContext context, User user,
			OnixConfiguration onixConfiguration) throws RSuiteException {
		
		ManagedObjectService moSvc = context.getManagedObjectService();
		
		List<ManagedObject> results = retrieveAllOnixMOs(context, user);

		Map<String, Map<String, String>> recipietnsToDelivery = new HashMap<String, Map<String, String>>();

		for (ManagedObject onixMO : results) {
			List<ReferenceInfo> directRefernceList = moSvc
					.getDependencyTracker().listDirectReferences(user,
							onixMO.getId());

			for (ReferenceInfo node : directRefernceList) {
				String browserUri = node.getBrowseUri();
				if (onixConfiguration.isInOnixConfiguration(browserUri)) {
					continue;
				}

				ContentAssemblyItem bookCaItem = ProjectBrowserUtils
						.getAncestorCAbyType(context, browserUri,
								onixConfiguration.getSupportedBookTypes());

				if (bookCaItem == null) {
					continue;
				}

				OnixUnit onixUnit = new OnixUnit(onixMO, bookCaItem);

				if (!onixUnit.isReady()) {
					continue;
				}

				Set<String> bookRecipients = onixUnit.getRecipients();
				if (bookRecipients.isEmpty()) {
					bookRecipients = onixConfiguration.getRecipientNames();
				}

				addOnixUnitToRecipient(recipietnsToDelivery, bookRecipients,
						onixUnit);
			}
		}
		return recipietnsToDelivery;
	}

	public static List<ManagedObject> retrieveAllOnixMOs(
			ExecutionContext context, User user) throws RSuiteException {
		SearchService searchSvc = context.getSearchService();
		
		

		String query = "/*:" + ELEMENT_NAME_ONIX_MESSAGE
				+ "[namespace-uri() = '" + NAMESPACE_ONIX + "']";
		List<ManagedObject> results = searchSvc.executeXPathSearch(user, query,
				0, 20000);
		return results;
	}

	private static void addOnixUnitToRecipient(
			Map<String, Map<String, String>> recipientsToDelivery,
			Set<String> bookRecipients, OnixUnit onixUnit) {
		for (String recipientName : bookRecipients) {
			boolean recipientAreadyAdded = false;
			for (Entry<String, Map<String, String>> recipientToDeliver : recipientsToDelivery
					.entrySet()) {
				if (recipientToDeliver.getKey().equals(recipientName)) {
					String OnixMOId = onixUnit.getOnixMO().getId();
					String bookCaItemId = onixUnit.getBookCAitem().getId();
					recipientToDeliver.getValue().put(OnixMOId, bookCaItemId);
					recipientAreadyAdded = true;
				}
			}

			if (!recipientAreadyAdded) {
				Map<String, String> onixUnits = new HashMap<String, String>();
				String OnixMOId = onixUnit.getOnixMO().getId();
				String bookCaItemId = onixUnit.getBookCAitem().getId();
				onixUnits.put(OnixMOId, bookCaItemId);
				recipientsToDelivery.put(recipientName, onixUnits);
			}
		}
	}
}
