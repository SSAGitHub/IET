package org.theiet.rsuite.utils;

import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.User;
import com.reallysi.rsuite.api.event.events.ContentAssemblyCreatedEventData;
import com.reallysi.rsuite.api.event.events.ContentAssemblyObjectAttachedEventData;
import com.reallysi.rsuite.api.event.events.ObjectInsertedEventData;
import com.reallysi.rsuite.api.event.events.ObjectMetaDataUpdatedEventData;
import com.reallysi.rsuite.service.ManagedObjectService;

public class EventHandlerUtils {

	/**
	 * Returns container Id from the userData object.
	 * 
	 * @param userData
	 * @param systemUser
	 * @param moServ
	 * @return containerId
	 * @throws RSuiteException
	 */
	public static String getContainerIdFromEventData(Object userData,
			User systemUser, ManagedObjectService moServ)
			throws RSuiteException {
		String containerId = null;

		if (userData instanceof ObjectInsertedEventData) {
			// This happens when eventType is equal to "object.mo.inserted"
			ObjectInsertedEventData oiEventData = (ObjectInsertedEventData) userData;
			containerId = moServ.getContentDisplayObject(systemUser,
					oiEventData.getManagedObject().getId()).getId();
		} else if (userData instanceof ContentAssemblyObjectAttachedEventData) {
			// This happens when eventType is equal to "object.contentassembly.objectattached"
			ContentAssemblyObjectAttachedEventData caoaEventData = (ContentAssemblyObjectAttachedEventData) userData;
			containerId = caoaEventData.getReference().getId();
		} else if (userData instanceof ContentAssemblyCreatedEventData) {
			// This happens when eventType is equal to "object.contentassembly.created"
			ContentAssemblyCreatedEventData caCreatedEventData = (ContentAssemblyCreatedEventData) userData;
			containerId = caCreatedEventData.getContentAssembly().getId();
		} else if (userData instanceof ObjectMetaDataUpdatedEventData) {
			// This happens when eventType is equal to "object.metadata.updated"
			ObjectMetaDataUpdatedEventData omuEventData = (ObjectMetaDataUpdatedEventData) userData;
			containerId = moServ.getContentDisplayObject(systemUser,
					omuEventData.getManagedObject().getId()).getId();
		}

		return containerId;
	}

}
