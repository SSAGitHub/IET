package org.theiet.rsuite.advisors;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.reallysi.rsuite.api.Alias;
import com.reallysi.rsuite.api.ManagedObject;
import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.control.AliasContainer;
import com.reallysi.rsuite.api.control.DefaultManagedObjectAdvisor;
import com.reallysi.rsuite.api.control.ManagedObjectAdvisor;
import com.reallysi.rsuite.api.control.ManagedObjectAdvisorContext;
import com.reallysi.rsuite.api.extensions.ExecutionContext;

/**
 * Sets the filename of managed objects as an alias of type "filename". For
 * XML managed objects this should be on insert. For non-XML managed objects
 * this should be on insert and update.
 *
 */
public class AliasSettingManagedObjectAdvisor extends
		DefaultManagedObjectAdvisor implements ManagedObjectAdvisor {
	
	private static Log log = LogFactory.getLog(AliasSettingManagedObjectAdvisor.class);

	public static final String ALIAS_TYPE_FILENAME = "filename";
	public static final String ALIAS_TYPE_BASENAME = "basename";

	public void adviseDuringInsert(ExecutionContext context,  
			ManagedObjectAdvisorContext insertContext)
    		throws RSuiteException {
		String moID = insertContext.getId();
        if ( !insertContext.isRootObjectOfOperation()) {
            // do not set alias if submo.
            return;
        }

		String externalFilename = insertContext.getExternalFileName();
		if (externalFilename != null) {
			String filename = FilenameUtils.getName( externalFilename);
			String basename = FilenameUtils.getBaseName(externalFilename);
			AliasContainer aliasContainer = insertContext.getAliasContainer();
			aliasContainer.addAlias( filename, ALIAS_TYPE_FILENAME);
			aliasContainer.addAlias(basename, ALIAS_TYPE_BASENAME);
		}
	}

	/**
	 * Set an alias of type "filename" if not already set.
	 */
	public void adviseDuringUpdate(
			ExecutionContext context,
            ManagedObjectAdvisorContext updateContext)
            throws RSuiteException {

		String moID = updateContext.getId();
        if ( !updateContext.isRootObjectOfOperation()) {
            // do not set alias if submo.
            return;
        }

		String externalFilename = updateContext.getExternalFileName();
		if (externalFilename != null) {
			addFileNameAliasIfNotExist(context, updateContext, moID,
					externalFilename);

		}
	}

	public void addFileNameAliasIfNotExist(ExecutionContext context,
			ManagedObjectAdvisorContext updateContext, String moID,
			String externalFilename) {
		// Have to copy existing aliases to the alias container
		// or they won't be set on update.
		AliasContainer aliasContainer = updateContext.getAliasContainer();
		String filename = FilenameUtils.getName( externalFilename);
		String basename = FilenameUtils.getBaseName(externalFilename);

		log.info("adviseDuringUpdate(): updateContext.getId()=" + moID);
		try {
			ManagedObject mo = context.getManagedObjectService().
			   getManagedObject(updateContext.getUser(), moID);						
			
			Alias[] aliases = mo.getAliases();
			// Copy existing aliases into the alias container.
			for (Alias alias : aliases) {

				aliasContainer.addAlias(alias.getText(), alias.getType());
				
				if (ALIAS_TYPE_FILENAME.equalsIgnoreCase(alias.getType())){
					return;
				}
			}
			aliasContainer.addAlias(filename, ALIAS_TYPE_FILENAME);
			aliasContainer.addAlias(basename, ALIAS_TYPE_BASENAME);
		} catch (Exception e) {
			log.error("Unexpected exception setting aliases: " + e.getClass().getSimpleName() + " - " + e.getMessage(), e);
		}
	}
	
}
