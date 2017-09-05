package org.theiet.rsuite.advisors.managedobject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Element;

import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.control.DefaultManagedObjectAdvisor;
import com.reallysi.rsuite.api.control.ManagedObjectAdvisor;
import com.reallysi.rsuite.api.control.ManagedObjectAdvisorContext;
import com.reallysi.rsuite.api.control.MetaDataContainer;
import com.reallysi.rsuite.api.extensions.ExecutionContext;

/**
 * Sets metadata on load based on simple business rules.
 */
public class SecuritySettingManagedObjectAdvisor extends
		DefaultManagedObjectAdvisor implements ManagedObjectAdvisor {

	private static Log log = LogFactory.getLog(SecuritySettingManagedObjectAdvisor.class);

	public void adviseDuringInsert(ExecutionContext context,  
			ManagedObjectAdvisorContext insertContext)
    		throws RSuiteException {
		Element elem = insertContext.getElement();
		if (elem != null) {
			addMetadata(insertContext, elem);
		}
	}

	public void addMetadata(ManagedObjectAdvisorContext advisorContext, Element elem) {
		MetaDataContainer metadata = advisorContext.getMetaDataContainer();
		// WEK: This is sample code. Adapt to real IET requirements.
		if ("KeywordItem".equals(elem.getLocalName())) {
			
		} else if ("book-part".equals(elem.getLocalName())) {
			String id = elem.getAttribute("id");
			String project = id.split("\\.")[0];
			metadata.addMetaDataItem("workflow_status", "new");
		}
	}

	/**
	 * Set an alias of type "filename" if not already set.
	 */
	public void adviseDuringUpdate(
			ExecutionContext context,
            ManagedObjectAdvisorContext updateContext)
            throws RSuiteException {
		Element elem = updateContext.getElement();
		if (elem != null) {
			addMetadata(updateContext, elem);			
		}
		
	}
	
	
}
