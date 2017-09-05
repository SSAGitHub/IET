/**
 * 
 */
package org.theiet.rsuite.utils;

import java.io.File;

import org.theiet.rsuite.datamodel.PublicationMetadata;

import com.reallysi.rsuite.api.ContentAssemblyNodeContainer;
import com.reallysi.rsuite.api.control.ObjectInsertOptions;
import com.reallysi.rsuite.api.control.ObjectUpdateOptions;


/**
 * Manages the setting of effective filenames for incoming files.
 *
 */
public class IetFilenameSetter  {

    

    /**
     * Sets the effective filename to use for the imported or updated object.
     * 
     */
    public static void setFilename( 
    		ContentAssemblyNodeContainer ca, 
    		PublicationMetadata pubMeta,
    		File file, 
    		ObjectUpdateOptions options) {


        String filename = constructEffectiveFilename(pubMeta, file);

        pubMeta.setDisplayName(filename);
        if (options != null)
        {
            options.setDisplayName(filename);
        }
    }

	public static void setFilename(
			ContentAssemblyNodeContainer ca,
			PublicationMetadata pubMeta, 
			File file, 
			ObjectInsertOptions options) {
        String filename = constructEffectiveFilename(pubMeta, file);

        pubMeta.setDisplayName(filename);
        if (options != null)
        {
            options.setDisplayName(filename);
        }
		
	}
	
	/**
	 * Constructs the effective (in-RSuite) filename for a given
	 * file based on the publication metadata for the publication
	 * it belongs to or is being imported in the context of.
	 * @param pubMeta
	 * @param file File to be imported
	 * @return Effective filename.
	 */
	public static String constructEffectiveFilename(
			PublicationMetadata pubMeta, File file) {
		String filename;
		// This is a placeholder for more complete
		// business rules for setting filenames.
    	String jobNumber = null;
        if (jobNumber != null && 
        	!"".equals(jobNumber) && 
        	!file.getName().startsWith(jobNumber)) {
            filename = jobNumber;
            filename += "-" + file.getName();
        }
        else {
            filename = file.getName();
        }
		return filename;
	}

    
}	
