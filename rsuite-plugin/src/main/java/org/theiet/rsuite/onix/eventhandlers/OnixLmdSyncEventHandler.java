package org.theiet.rsuite.onix.eventhandlers;

import org.theiet.rsuite.onix.OnixConstants;
import org.theiet.rsuite.onix.onix2lmd.Onix2LmdSynchronizer;
import org.theiet.rsuite.onix.onix2lmd.adjuster.IetOnix2LmdValueAdjuster;

import com.reallysi.rsuite.api.ManagedObject;
import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.event.DefaultEventHandler;
import com.reallysi.rsuite.api.event.Event;
import com.reallysi.rsuite.api.event.events.ObjectCheckedInEventData;
import com.reallysi.rsuite.api.extensions.ExecutionContext;

/**
  */
public class OnixLmdSyncEventHandler extends DefaultEventHandler implements
		OnixConstants {

	/**
	 * Handles workflow and task events.
	 * 
	 * @throws RSuiteException
	 */
	public void handleEvent(ExecutionContext context, Event event,
			Object additionalEventData) throws RSuiteException {

		if (event.getUserData() instanceof ObjectCheckedInEventData) {
			ObjectCheckedInEventData checkInData = (ObjectCheckedInEventData) event
					.getUserData();
			ManagedObject mo = checkInData.getManagedObject();
			if (!mo.isAssemblyNode() && !mo.isNonXml()
					&& ELEMENT_NAME_ONIX_MESSAGE.equals(mo.getLocalName())) {
				Onix2LmdSynchronizer synchronizer = new Onix2LmdSynchronizer(new IetOnix2LmdValueAdjuster());
				synchronizer.synchronizeLmdWithOnix(context, mo);
			}
		}

	}
}
