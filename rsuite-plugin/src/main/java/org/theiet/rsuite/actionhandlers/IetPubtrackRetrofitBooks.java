package org.theiet.rsuite.actionhandlers;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.theiet.rsuite.IetConstants;
import org.theiet.rsuite.books.BooksConstans;
import org.theiet.rsuite.utils.PubtrackLogger;

import com.reallysi.rsuite.api.ContentAssembly;
import com.reallysi.rsuite.api.ManagedObject;
import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.User;
import com.reallysi.rsuite.api.pubtrack.Process;
import com.reallysi.rsuite.api.pubtrack.ProcessMetaDataItem;
import com.reallysi.rsuite.api.workflow.AbstractBaseActionHandler;
import com.reallysi.rsuite.api.workflow.WorkflowExecutionContext;
import com.reallysi.rsuite.service.ContentAssemblyService;
import com.reallysi.rsuite.service.PubtrackManager;
import com.reallysi.rsuite.service.SearchService;

public class IetPubtrackRetrofitBooks extends AbstractBaseActionHandler implements BooksConstans {

	private static final long serialVersionUID = 1L;
	private static final String BOOK_QUERY = "/rs_ca_map/rs_ca[rmd:get-type(.) = 'book']";

	@SuppressWarnings("unchecked")
	@Override
	public void execute(WorkflowExecutionContext context)
			throws Exception {
		Log log = context.getWorkflowLog();
		PubtrackManager ptMgr = context.getPubtrackManager();
		ContentAssemblyService caSvc = context.getContentAssemblyService();
		User user = getSystemUser();
		SearchService srchSvc = context.getSearchService();
		log.info("execute: execute query " + BOOK_QUERY);
		List<ManagedObject> caSet = srchSvc.executeXPathSearch(user,
				BOOK_QUERY, 1, 0);
		int nBooks = caSet.size();
		log.info("getIssueCaId: query returned " + nBooks);
		for (int i=0; i<nBooks; i++) {
			String bookCaId = caSet.get(i).getId();
			try {
				//caSvc.removeContentAssembly(user, bookCaId);
				
				ContentAssembly bookCa = caSvc.getContentAssembly(user, bookCaId);
				Date dtBookCreated = bookCa.getDtCreated();
				String bookCode = bookCa.getLayeredMetadataValue(BooksConstans.LMD_FIELD_BOOK_PRODUCT_CODE);
				log.info("execute: process book " + bookCode + " " + bookCaId);
				String externalId = "IET_BOOK_" + bookCode;
				List<Process> processList = ptMgr.getProcessByExternalId(user, externalId);
				int n = processList.size();
//				Book was previously created but no pubtrack process started.
//				Create new process
				if (n == 0) {
					Process p = PubtrackLogger.createProcess(user, context, log, "BOOK", bookCode, bookCaId);
					log.info("\tcreated new pubtrack process " + p.getId());
					p.setDtStarted(dtBookCreated);
				}
//				Process already exists, because workflow was started.
//				Capture and existing pubtrack metadata, except for the 3 items
//				that will be created. PUBTRACK_WORKFLOW_STARTED should not exist,
//				but best to check anyway.
				else if (n == 1) {
					HashMap<String, String> metaMap = new HashMap<String, String>();
					Process p = processList.get(0);
					Set<ProcessMetaDataItem> metaSet = p.getMetaData();
					for (ProcessMetaDataItem item : metaSet) {
						String name = item.getName();
						if (!name.equals("OBJECT_ID") && 
							!name.equals("PRODUCT_ID") &&
							!name.equals("PRODUCT")) {
							metaMap.put(name, item.getValue());
						}
					}
//					Date that pubtrack process was started not becomes the date
//					of the workflow started event, so add this back into the set
					Date dtStarted = p.getDtStarted();
					String dateString = IetConstants.UK_DATE_FORMAT_SHORT.format(dtStarted);
					if (!metaMap.containsKey(PUBTRACK_PREPARE_BOOK_WORKFLOW_STARTED)) {
						log.info("Add " + PUBTRACK_PREPARE_BOOK_WORKFLOW_STARTED + " " + dateString);
						metaMap.put(PUBTRACK_PREPARE_BOOK_WORKFLOW_STARTED, dateString);
					}
//					Then remove old process and start a new one using the create date
//					of the CA as the start date of the process
					
					Process newProcess = PubtrackLogger.createProcess(user, context, log, WF_VAR_BOOK, bookCode, bookCaId, metaMap);
					log.info("\tcreated new pubtrack process " + newProcess.getId());
					newProcess.setDtStarted(dtBookCreated);
					log.info("createProcess: new process id is " + newProcess.getId());
				}
				
			}
			catch (RSuiteException e) {
				log.error("execute: error with bookCaId" + bookCaId + " was " + e.getMessage());
			}
		}
	}

}
