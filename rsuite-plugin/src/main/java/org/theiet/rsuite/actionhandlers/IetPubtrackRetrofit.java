package org.theiet.rsuite.actionhandlers;

import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.theiet.rsuite.journals.JournalConstants;
import org.theiet.rsuite.utils.StringUtils;

import com.reallysi.rsuite.api.User;
import com.reallysi.rsuite.api.pubtrack.Process;
import com.reallysi.rsuite.api.pubtrack.ProcessMetaDataItem;
import com.reallysi.rsuite.api.workflow.AbstractBaseActionHandler;
import com.reallysi.rsuite.api.workflow.WorkflowExecutionContext;
import com.reallysi.rsuite.service.PubtrackManager;

public class IetPubtrackRetrofit extends AbstractBaseActionHandler {

	private static final long serialVersionUID = 1L;
	private static final Pattern JOURNAL_CODE_PATTERN = Pattern.compile("[A-Z]{3}");

	@SuppressWarnings("unchecked")
	@Override
	public void execute(WorkflowExecutionContext context) throws Exception {
		Log log = context.getWorkflowLog();
		PubtrackManager ptMgr = context.getPubtrackManager();
		String query = "from Process p where p.name='IET_ARTICLE' and p.dtCompleted is null";
		User user = getSystemUser();
		@SuppressWarnings("unused")
		List<Process> processList = ptMgr.query(user, query);
		log.info("Process list is size " + processList.size());
		for (Process p : processList) {
			String journalCode = new String();
			Set<ProcessMetaDataItem> metaSet = p.getMetaData();
			log.info("execute: Check process id " + p.getId() + " " + p.getExternalId());
			for (ProcessMetaDataItem item : metaSet) {
				if(item.getName().equals("PRODUCT_ID")) {
					try {
						journalCode = item.getValue().substring(0, 3);
						if (JOURNAL_CODE_PATTERN.matcher(journalCode).matches()) {
							log.info("execute: journalCode is " + journalCode);
						}
						else {
							log.info("execute: Invalid code");
						}
					}
					catch (StringIndexOutOfBoundsException e) {
						log.info("execute: Invalid code");
					}
				}
			}
			if (!StringUtils.isBlank(journalCode)) {
				ProcessMetaDataItem item = new ProcessMetaDataItem();
				item.setName(JournalConstants.PUBTRACK_JOURNAL_CODE);
				item.setValue(journalCode);
				item.setProcess(p);
				metaSet.add(item);
				p.setMetaData(metaSet);
				ptMgr.updateProcess(user, p);
				log.info("execute: process updated");
			}
		}
	}

}
