package org.theiet.rsuite.eventhandlers;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.theiet.rsuite.advisors.display.ContentDisplayCASorterConfig;
import org.theiet.rsuite.advisors.display.ContentDisplayCASorterConfigurationFactory;
import org.theiet.rsuite.advisors.display.ContentDisplaySorter;
import org.theiet.rsuite.books.BooksConstans;
import org.theiet.rsuite.journals.JournalConstants;

import com.reallysi.rsuite.api.ContentAssembly;
import com.reallysi.rsuite.api.ContentAssemblyNodeContainer;
import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.event.DefaultEventHandler;
import com.reallysi.rsuite.api.event.Event;
import com.reallysi.rsuite.api.event.events.ContentAssemblyCreatedEventData;
import com.reallysi.rsuite.api.extensions.ExecutionContext;

public class ContentDisplayHandler extends DefaultEventHandler implements BooksConstans, JournalConstants {
	
	private static final String EVENT_TYPE_OBJECT_CONTENT_ASSEMBLY_CREATED = "object.contentassembly.created";
	
	private Log logger = LogFactory.getLog(getClass());
	
	@Override
	public void handleEvent(ExecutionContext context, Event event, Object eventData) {
		
		try {
			if (event.getType().equals(EVENT_TYPE_OBJECT_CONTENT_ASSEMBLY_CREATED)) {
				ContentAssemblyCreatedEventData createdEventData = obtainContextContentAssembly(context, event);
				ContentAssembly contextCa = createdEventData.getParent(); obtainContextContentAssembly(context, event);
				ContentAssembly createdCa = createdEventData.getContentAssembly();
				
				if (contextCa != null && contextCaShouldBeSorted(contextCa)){
					ContentDisplayCASorterConfig caSorterConfig = ContentDisplayCASorterConfigurationFactory.getSortingConfiguration(contextCa);						
					ContentDisplaySorter contentDisplaySorter = new ContentDisplaySorter(context, contextCa, createdCa);
					contentDisplaySorter.sort(caSorterConfig);
				}
			}			
		} catch (RSuiteException ex) {
			logger.error(ex.getMessage(), ex);
		}		
	}

	private boolean contextCaShouldBeSorted(
			ContentAssemblyNodeContainer contextCa) {
		
		if (CA_TYPE_BOOKS_CONTENT_ASSEMBLY.equalsIgnoreCase(contextCa.getType())){
			return true;
		}
		
		return false;
	}

	public ContentAssemblyCreatedEventData obtainContextContentAssembly(ExecutionContext context, Event event) throws RSuiteException {
		
		Object userData = event.getUserData();
		
		if (userData instanceof  ContentAssemblyCreatedEventData) {
			return (ContentAssemblyCreatedEventData) userData;
						

		}
		
		return null;
	}
	
	
	
}
