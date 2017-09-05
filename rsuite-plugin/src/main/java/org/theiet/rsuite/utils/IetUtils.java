package org.theiet.rsuite.utils;

import java.io.File;
import java.util.*;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.logging.*;

import com.reallysi.rsuite.api.*;
import com.reallysi.rsuite.api.extensions.ExecutionContext;
import com.reallysi.rsuite.api.pubtrack.*;
import com.reallysi.rsuite.api.pubtrack.Process;
import com.reallysi.rsuite.api.workflow.*;
import com.reallysi.rsuite.service.ManagedObjectService;
import com.rsicms.rsuite.helpers.messages.ProcessFailureMessage;
import com.rsicms.rsuite.helpers.upload.*;

// TODO: Auto-generated Javadoc
/**
 * The Class IetUtils.
 */
public class IetUtils {

	/** The log. */
	private static Log log = LogFactory.getLog(IetUtils.class);

	public static String getDomainUrl (ExecutionContext context) {
		return "http://" + context.getRSuiteServerConfiguration().getHostName() + 
		":" + context.getRSuiteServerConfiguration().getPort();
	}
	
	/**
	 * Parse out the product ID from a filename, such as an incoming Zip file.
	 * @param fileName The filename to get the product ID from.
	 * @return The product ID or null if it can't be determined.
	 */
	public static String getProductIdFromFilename(String fileName) {
		// Product ID needs to be the last underscore-separated
		// token in the filename:
		
		String namePart = FilenameUtils.getBaseName(fileName);
		
		String[] tokens = namePart.split("_");
		if (tokens == null || tokens.length == 0) {
			log.warn("No '_' characters in filename \"" + fileName + "\", can't find a product ID.");
		}
		
		
		return tokens[tokens.length - 1];
	}

	/**
	 * Construct book specific process external id.
	 *
	 * @param context the context
	 * @param piInfo the pi info
	 * @param variables the variables
	 * @return the string
	 */
	public static String constructBookSpecificProcessExternalId(
			ExecutionContext context,
			ProcessInstanceSummaryInfo piInfo,
			Map<String, Object> variables) {
		String pubCode = null;
		if (variables != null) {
			pubCode = (String)variables.get("productCode");
		}
		return constructBookSpecificProcessExternalId(piInfo, pubCode);
	}

	/**
	 * Construct book specific process external id.
	 *
	 * @param piInfo the pi info
	 * @param pubCode the pub code
	 * @return the string
	 */
	public static String constructBookSpecificProcessExternalId(
			ProcessInstanceSummaryInfo piInfo, 
			String pubCode) {
		String externalId = "workflow_" +
		  (pubCode == null || "".equals(pubCode.trim()) ? "" : pubCode + "_") +
		  piInfo.getProcessInstanceId();
		return externalId;
	}

	/**
	 * If a metadata item with same name exists, update its value, otherwise
	 * add a new item.
	 *
	 * @param metadata the metadata
	 * @param item the item
	 */
	public static void addOrReplacePubtrackProcessMetadataItem(
			Set<ProcessMetaDataItem> metadata,
			ProcessMetaDataItem item) {
		ProcessMetaDataItem setMe = null;
		for (ProcessMetaDataItem cand : metadata) {
			if (cand.getName().equals(item.getName())) {
				setMe = cand;
				break;
			}
		}
		if (setMe != null) {
			setMe.setValue(item.getValue());
		} else {
			metadata.add(item);
		}
		
	}

	/**
	 * Adds the or set pubtrack process metadata item.
	 *
	 * @param name the name
	 * @param value the value
	 * @param metadata the metadata
	 * @param proc the proc
	 */
	public static void addOrSetPubtrackProcessMetadataItem(
			String name,
			String value,
			Set<ProcessMetaDataItem> metadata, Process proc) {
		ProcessMetaDataItem item;
		item = new ProcessMetaDataItem();
		item.setName(name);
		item.setValue(value);
		item.setProcess(proc);
		addOrReplacePubtrackProcessMetadataItem(metadata, item);
	}
	
	/**
	 * Gets the file load result from the load options.
	 *
	 * @param log the log
	 * @param loadOpts the load opts
	 * @return result
	 */
	public static boolean getFileLoadResult(Log log, RSuiteFileLoadOptions loadOpts) {
		RSuiteFileLoadResult loadResult = loadOpts.getLoadResult();
		if (loadResult.hasErrors()) {
			logLoadErrorResult(log, loadResult);
		}
		return loadResult.hasErrors();
	}

    public static void logLoadErrorResult(Log log, RSuiteFileLoadResult loadResult) {
        List<List<ProcessFailureMessage>> failList = loadResult.getFailuresByType();
        for (List<ProcessFailureMessage> typelist: failList) {
        	for (ProcessFailureMessage message: typelist) {
        		
        		log.error("getFileLoadResult: failure is " + message.getMessageType() + ":" + message.getMessageText() + " object " + message.getRelatedObjectLabel());
        		log.error(ExceptionUtils.getFullStackTrace(message.getCause()));
        	}
        }
    }
	
    public static void removeMetaDataFieldFromCa(
			User user,
			ManagedObjectService moSvc,
			ContentAssemblyItem ca,
			String name) {
    
    	removeMetaDataFieldFromCa(null, user, moSvc, ca, name);
    }

	/**
	 * Removes meta data field from a ca.
	 *
	 * @param log the log
	 * @param user the user
	 * @param moSvc the mo svc
	 * @param ca the ca
	 * @param name name of LMD field
	 */
	public static void removeMetaDataFieldFromCa(Log log,
			User user,
			ManagedObjectService moSvc,
			ContentAssemblyItem ca,
			String name) {
		try {
			List<MetaDataItem> items = ca.getMetaDataItems();
			for (MetaDataItem item: items) {
				if (item.getName().endsWith(name)) {
					moSvc.removeMetaDataEntry(user, ca.getId(), item);
				}
			}
		} catch (RSuiteException e) {
//			Fail gracefully. May not have had the field set
			if (log != null){
				log.info("removeMetaDataField: removal of item " + name + 
						" from object id " + ca.getId() + " failed");
			}
		}		
	}

	/**
	 * Removes meta data field from a mo.
	 *
	 * @param log the log
	 * @param user the user
	 * @param moSvc the mo svc
	 * @param mo the mo
	 * @param name name of LMD field
	 */
	public static void removeMetaDataFieldFromMo(Log log,
			User user,			
			ManagedObjectService moSvc,
			ManagedObject mo,
			String name) {
		try {
			List<MetaDataItem> items = mo.getMetaDataItems();
			for (MetaDataItem item: items) {
				if (item.getName().endsWith(name)) {
					moSvc.removeMetaDataEntry(user, mo.getId(), item);
					log.info("removeMetaDataField: removed of item " + name + 
							" from object id " + mo.getId());
				}
			}
		} catch (RSuiteException e) {
//			Fail gracefully. May not have had the field set
			log.info("removeMetaDataField: removal of item " + name + 
					" from object id " + mo.getId() + " failed");
		}		
	}
	
	/**
	 * Removes all meta data from a mo.
	 *
	 * @param log the log
	 * @param user the user
	 * @param moSvc the mo svc
	 * @param mo the mo
	 */
	public static void removeAllMetaDataFromMo(Log log,
			User user,			
			ManagedObjectService moSvc,
			ManagedObject mo) {
		try {
			List<MetaDataItem> items = mo.getMetaDataItems();
			for (MetaDataItem item: items) {
					moSvc.removeMetaDataEntry(user, mo.getId(), item);
					log.info("removeMetaDataField: removed of item " + item.getName() + 
							" from object id " + mo.getId());
			}
		} catch (RSuiteException e) {
//			Fail gracefully. May not have had the field set
			log.info("removeMetaDataField: remove all metadata" + 
					" from object id " + mo.getId() + " failed");
		}		
	}

	/**
	 * Load file to ca. Wrapper around RSuiteFileLoadHelper method that
	 * forces a candidate file to be evaluated as to its previous load status. This
	 * allows the duplicate file policy to be honored.
	 *
	 * @param context
	 * @param file
	 * @param ca
	 * @param loadOpts
	 * @return mo
	 * @throws RSuiteException
	 */
	public static ManagedObject loadFileToCa(ExecutionContext context,
			File file, ContentAssembly ca,
			RSuiteFileLoadOptions loadOpts) throws RSuiteException {
		try {
			ManagedObject oldMo = RSuiteFileLoadHelper.getExistingMoForFile(context, file, loadOpts);
			log.info("check for old mo is " + (oldMo != null));
			if (oldMo != null) {
				loadOpts.addFileToMoMapping(file, oldMo);
			}
		} catch (FileUpdateException e) {
			log.info("Exception " + e.getMessage());
		}
		ManagedObject mo = RSuiteFileLoadHelper.loadFileToCa(context, file, ca, loadOpts);
		return mo;
	}
	
}
