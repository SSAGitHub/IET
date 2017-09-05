package org.theiet.rsuite.advisors.managedobject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Element;

import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.control.AliasContainer;
import com.reallysi.rsuite.api.control.DefaultManagedObjectAdvisor;
import com.reallysi.rsuite.api.control.ManagedObjectAdvisor;
import com.reallysi.rsuite.api.control.ManagedObjectAdvisorContext;
import com.reallysi.rsuite.api.extensions.ExecutionContext;

/**
 * Sets the filename of managed objects as an alias of type "filename". For
 * XML managed objects this should be on insert. For non-XML managed objects
 * this should be on insert and update. Also sets the alias "basename" to the
 * base file name. Note that the base filename need not be unique as different
 * objects of different types may have the same base name. This provides a bit
 * of indirection between references to logical objects by base name and 
 * the specific MO or variant to use for a given resolution instance.
 *
 */
//TODO seems to be not used so marked as deprecated. check user and remove if not used
@Deprecated 
public class AliasSettingManagedObjectAdvisor extends
		DefaultManagedObjectAdvisor implements ManagedObjectAdvisor {

	public static final String ALIAS_TYPE_FILENAME = "filename";
	public static final String ALIAS_TYPE_BASENAME = "basename";
	
	private static Log log = LogFactory.getLog(AliasSettingManagedObjectAdvisor.class);

	public void adviseDuringInsert(ExecutionContext context,  
			ManagedObjectAdvisorContext insertContext)
    		throws RSuiteException {
		Element elem = insertContext.getElement();
		if (elem != null) {
			addAlias(insertContext, elem);
		}
	}

	public void addAlias(ManagedObjectAdvisorContext advisorContext, Element elem) {
		String alias = null;
		// WEK: This is sample code. Update to reflect IET requirements.
		if ("KeywordItem".equals(elem.getLocalName())) {
			alias = elem.getAttribute("Key");
			
		} else if ("book-part".equals(elem.getLocalName())) {
			alias = elem.getAttribute("id");
		}
		if (alias != null) {
			AliasContainer aliases = advisorContext.getAliasContainer();
			log.info("addAlias(): Adding alias \"" + alias + "\" to KeywordItem element.");
			aliases.addAlias(alias);
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
			addAlias(updateContext, elem);			
		}
		
	}
	
	
}
