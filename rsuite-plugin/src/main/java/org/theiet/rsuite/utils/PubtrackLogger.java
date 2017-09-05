package org.theiet.rsuite.utils;

import java.text.DateFormat;
import java.util.*;

import org.apache.commons.logging.Log;

import com.reallysi.rsuite.api.*;
import com.reallysi.rsuite.api.extensions.ExecutionContext;
import com.reallysi.rsuite.api.pubtrack.*;
import com.reallysi.rsuite.api.pubtrack.Process;
import com.reallysi.rsuite.service.PubtrackManager;
import com.rsicms.projectshelper.datatype.RSuitePubtrackEvent;

public final class PubtrackLogger {
	
    private PubtrackLogger(){};
    
	/**
	 * <p>Creates new pubtrack process for IET product.<p>
	 * <p>Forms a pubtrack external id from the supplied article id and
	 * creates new pubtrack process</p>
	 * <p>If a process with the same external id was created previously, it
	 * is removed.</p>
	 * @param user
	 * @param context
	 * @param log
	 * @param product (e.g., "ARTICLE" or "ISSUE" or "BOOK")
	 * @param id (e.g., "ELL-2012-14-12" for an issue)
	 * @param objectId caId for book or article or . . .
	 * @param optionalKeys map of additional name/value pairs for pubtrack log
	 * @throws RSuiteException
	 */
	public static Process createProcess(User user, 
			ExecutionContext context, 
			Log log,
			String productName,
			String id, 
			String objectId, 
			Map<String, String> optionalKeys) throws RSuiteException {
		String externalId = getPubtrackExternalId(productName, id);
		log.info("createProcess: create process " + externalId);
		PubtrackManager ptMgr = context.getPubtrackManager();
		List<Process> processList = ptMgr.getProcessByExternalId(user, externalId);
		for (Process p: processList) {
			String pId = String.valueOf(p.getId());
			ptMgr.removeProcess(user, pId);
			log.info("createProcess: found and removed process " + pId);
		}
		HashMap<String, String> metaMap = new HashMap<String, String>();
		metaMap.put("PRODUCT", productName);
		metaMap.put("PRODUCT_ID", id);
		metaMap.put("OBJECT_ID", objectId);
		if (optionalKeys != null) {
			for (String name: optionalKeys.keySet()) {
				metaMap.put(name, optionalKeys.get(name));
			}
		}
		Process p = ptMgr.startProcess(user,
                externalId,
                "IET_" + productName,
                Calendar.getInstance().getTime(), null, metaMap);
		log.info("createProcess: new process id is " + p.getId());
		return p;
	}

	/**
	 * <p>Creates new pubtrack process for IET product.<p>
	 * <p>Forms a pubtrack external id from the supplied article id and
	 * creates new pubtrack process</p>
	 * <p>If a process with the same external id was created previously, it
	 * is removed.</p>
	 * @param user
	 * @param context
	 * @param log
	 * @param product (e.g., "ARTICLE" or "ISSUE" or "BOOK")
	 * @param id (e.g., "ELL-2012-14-12" for an issue)
	 * @param objectId caId for book or article or . . .
	 * @throws RSuiteException 
	 */
	public static Process createProcess(User user, 
			ExecutionContext context, 
			Log log,
			String productName,
			String id, 
			String objectId) throws RSuiteException {
		return createProcess(user, context, log, productName, id, objectId, null);
	}
	
	/**
	 * <p>Adds metadata (name, value) to an existing Pubtrack process by external id 
	 * based on article id. Throws exception if process is not found, or is not unique.
	 * The value for this purpose is the string value of the current date</p>
	 *
	 * @param user
	 * @param context
	 * @param log
	 * @param productName
	 * @param id
	 * @param eventName the event name, e.g. "AUTHOR_COMMENTS_RECEIVED"
	 * @throws RSuiteException
	 */
	@SuppressWarnings("unchecked")
	public static void logToProcess(User user, 
			ExecutionContext context, 
			Log log,
			String productName,
			String id,
			String eventName) throws RSuiteException {
		
		PubtrackManager ptMgr = context.getPubtrackManager();
		
		Process p = getPubtrackProcess(user, productName, id, ptMgr);
		
		Set<ProcessMetaDataItem> metaSet = p.getMetaData();
		ProcessMetaDataItem item = new ProcessMetaDataItem();
		item.setProcess(p);
		item.setName(eventName);
		item.setValue(getDateString());
		metaSet.add(item);
		p.setMetaData(metaSet);
		ptMgr.updateProcess(user, p);
	}
	
	public static void logToProcess(ExecutionContext context, User user, 
            String productName,
            String id,
            RSuitePubtrackEvent event) throws RSuiteException {
        
	    logToProcess(user, context, null, productName, id, event.getEventName());
    }

	public static String getPubtrackExternalId(String productName, String id) {
		return "IET_" + productName + "_" + id;
	}
	
	/**
	 * Complete pubtrack process
	 *
	 * @param user
	 * @param context
	 * @param log
	 * @param productName
	 * @param id
	 * @throws RSuiteException
	 */
	public static void completeProcess(User user, 
			ExecutionContext context, 
			String productName,
			String id) throws RSuiteException {

		PubtrackManager ptMgr = context.getPubtrackManager();
		
		getPubtrackProcess(user, productName, id, ptMgr);

		String externalIds = getPubtrackExternalId(productName, id);
		
		ptMgr.completeProcess(user, externalIds, Calendar.getInstance().getTime());

	}

	public static Process getPubtrackProcess(User user, String productName,
			String id, PubtrackManager ptMgr) throws RSuiteException {
		String externalId = getPubtrackExternalId(productName, id);
		List<Process> processList = ptMgr.getProcessByExternalId(user, externalId);
		
		int n = processList.size();
		
		if (n == 0) {
			throw new RSuiteException("No process found for external id " + externalId);
		}
		else if (n > 1) {
			throw new RSuiteException(String.valueOf(n) + " processes found for external id " + externalId);
		}
		
		return processList.get(0);
	}
	
	private static String getDateString() {
		DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT, java.util.Locale.UK);
		return df.format(Calendar.getInstance().getTime());
	}

	/**
	 * Adds name/value metaitem to pubtrack process.
	 *
	 * @param user
	 * @param context
	 * @param log
	 * @param productName
	 * @param id
	 * @param metaName
	 * @param metaValue
	 * @throws RSuiteException
	 */
	@SuppressWarnings("unchecked")
	public static void addMetaItemToProcess(User user, 
			ExecutionContext context, 
			Log log,
			String productName,
			String id,
			String metaName,
			String metaValue) throws RSuiteException {
		String externalId = getPubtrackExternalId(productName, id);
		log.info("logToProcess: retrieve process " + externalId);
		PubtrackManager ptMgr = context.getPubtrackManager();
		List<Process> processList = ptMgr.getProcessByExternalId(user, externalId);
		int n = processList.size();
		if (n == 0) {
			throw new RSuiteException("No process found for external id " + externalId);
		}
		else if (n > 1) {
			throw new RSuiteException(String.valueOf(n) + " processes found for external id " + externalId);
		}
		else {
			Process p = processList.get(0);
			Set<ProcessMetaDataItem> metaSet = p.getMetaData();
			ProcessMetaDataItem item = new ProcessMetaDataItem();
			item.setProcess(p);
			item.setName(metaName);
			item.setValue(metaValue);
			metaSet.add(item);
			p.setMetaData(metaSet);
			ptMgr.updateProcess(user, p);
			log.info("logToProcess: add event " + metaValue);
		}
	}

}
