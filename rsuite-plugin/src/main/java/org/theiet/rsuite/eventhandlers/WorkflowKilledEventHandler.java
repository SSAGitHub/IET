package org.theiet.rsuite.eventhandlers;

import java.util.Calendar;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.theiet.rsuite.IetConstants;
import org.theiet.rsuite.books.BooksConstans;
import org.theiet.rsuite.utils.IetUtils;

import com.reallysi.rsuite.api.ManagedObject;
import com.reallysi.rsuite.api.MetaDataItem;
import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.event.DefaultEventHandler;
import com.reallysi.rsuite.api.event.Event;
import com.reallysi.rsuite.api.event.events.WorkflowAbortedEventData;
import com.reallysi.rsuite.api.extensions.ExecutionContext;
import com.reallysi.rsuite.api.pubtrack.Process;
import com.reallysi.rsuite.api.pubtrack.ProcessMetaDataItem;
import com.reallysi.rsuite.api.workflow.ProcessInstanceSummaryInfo;

public class WorkflowKilledEventHandler extends DefaultEventHandler {
	
	public static Log log = LogFactory.getLog(WorkflowKilledEventHandler.class);
	
	// workflow.process.aborted
	public void handleEvent(ExecutionContext context, Event event, Object eventData) {
		WorkflowAbortedEventData data = (WorkflowAbortedEventData)event.getUserData();
		String pId = data.getProcessInstanceId();
		// NOTE: For 3.6, RQL cannot search on LMD field names starting with "_".
//		String query = "SELECT * FROM RSUITE:CONTENT-ASSEMBLY,RSUITE:CANODE where " +
//				"lmd::" + IetConstants.LMD_FIELD_PID + "='" + pId + "'";
		
		String query = "SELECT * FROM RSUITE:CONTENT-ASSEMBLY,RSUITE:CANODE";
		List<ManagedObject> moList = null; //= null added for 3.7 hack
		try {
/*
 * Block commented out and = null added above to allow compilation in 3.7 Harv Greenberg
 * 			moList = context.getSearchService()
					.executeRqlSearch(
							context.getAuthorizationService().getSystemUser(), 
							query, 
							0, 
							10
							);
*/
			if (moList.size() > 1) {
				ManagedObject bookCaMo = null;
				for (int i = 0; i < moList.size(); i++) {
					ManagedObject mo = moList.get(i);
					log.debug("Examining CA item [" + mo.getId() + "] " + mo.getDisplayName());
					String temp = mo.getLayeredMetadataValue(IetConstants.LMD_FIELD_PID);
					if (temp == null || "".equals(temp.trim())) continue;
					
					for (MetaDataItem item : mo.getMetaDataItems()) {
						if (IetConstants.LMD_FIELD_PID.equals(item.getName())) {
							bookCaMo = mo;
							if (pId.equals(item.getValue())) {
								log.info("Removing " + IetConstants.LMD_FIELD_PID + " LMD field from CA item [" + mo.getId() + "]");
								context.getManagedObjectService()
								  .removeMetaDataEntry(
										  context.getAuthorizationService().getSystemUser(),
										  mo.getId(), 
										  item
										  );
							}
						}
					}
				}
				
				if (bookCaMo != null) {
					// Remove the workflow status LMD as well, since we're no longer in a workflow.
					for (MetaDataItem item : bookCaMo.getMetaDataItems()) {
						if (IetConstants.LMD_FIELD_WORKFLOW_STATUS.equals(item.getName())) {
							log.info("Removing " + IetConstants.LMD_FIELD_WORKFLOW_STATUS + 
									" LMD field from CA item [" + bookCaMo.getId() + "]");
							context.getManagedObjectService()
							  .removeMetaDataEntry(
									  context.getAuthorizationService().getSystemUser(),
									  bookCaMo.getId(), 
									  item
									  );
							break;
						}
					}
					
					// Now complete the PubTrack process for this workflow and indicate it was killed.
					
					String productCode = bookCaMo.getLayeredMetadataValue(BooksConstans.LMD_FIELD_BOOK_PRODUCT_CODE);
					ProcessInstanceSummaryInfo piInfo = data.getProcessInstanceSummaryInfo();
					if (productCode != null) {
						String processExternalId = 
								IetUtils.constructBookSpecificProcessExternalId(
										piInfo, 
										productCode);
						List<Process> procs = context.getPubtrackManager()
								.getProcessByExternalId(
										data.getUser(), 
										processExternalId);
								
						Process proc = null;
						if (procs.size() > 0) {
							proc = procs.get(0);
							if (procs.size() > 1) {
								log.warn("Found " + procs.size() + " with the external process ID \"" + processExternalId + "\". " +
										"There should be at most 1.");
							}
						}
						@SuppressWarnings("unchecked")
						Set<ProcessMetaDataItem> metadata = proc.getMetaData();
						IetUtils.addOrSetPubtrackProcessMetadataItem(
								IetConstants.LMD_FIELD_WORKFLOW_STATUS, 
								"Process killed", 
								metadata, 
								proc);
						proc.setMetaData(metadata);
						
						try {
							context.getPubtrackManager()
							.updateProcess(data.getUser(), proc);
						} catch (RSuiteException e) {
							log.error("Unexpected exception updating Pubtrack Process: " + e.getMessage(), e);
						}

						context.getPubtrackManager()
						  .completeProcess(
								data.getUser(), 
								proc.getExternalId(), 
								Calendar.getInstance().getTime());
					}
				}
			}
		} catch (RSuiteException e) {
			log.error("Exception removing LMD field \"" + IetConstants.LMD_FIELD_PID + " from content assembly item.", 
					e);
		}
						
	}

}
