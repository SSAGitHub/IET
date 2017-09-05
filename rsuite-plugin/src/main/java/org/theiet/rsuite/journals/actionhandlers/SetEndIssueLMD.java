package org.theiet.rsuite.journals.actionhandlers;

import static com.rsicms.projectshelper.utils.ProjectContentAssemblyUtils.*;

import java.util.*;

import org.apache.commons.logging.Log;
import org.theiet.rsuite.IetConstants;
import org.theiet.rsuite.journals.JournalConstants;

import com.reallysi.rsuite.api.*;
import com.reallysi.rsuite.api.workflow.*;
import com.reallysi.rsuite.service.ManagedObjectService;
import com.rsicms.projectshelper.utils.browse.ProjectBrowserUtils;

public class SetEndIssueLMD extends AbstractBaseActionHandler {

	private static final long serialVersionUID = 1L;

	@Override
	public void execute(WorkflowExecutionContext context) throws Exception {
		Log log = context.getWorkflowLog();
		User user = getSystemUser();
		
		ManagedObjectService moSvc = context.getManagedObjectService();
		String issueCaId = context
				.getVariable(JournalConstants.WF_VAR_RSUITE_CONTENTS);
		ContentAssembly issueCa = getCAFromMO(context, user, issueCaId);
		Date today = Calendar.getInstance().getTime();
		String dateString = IetConstants.UK_DATE_FORMAT.format(today);
		log.info("execute: set date string " + dateString + " on issue CA "
				+ issueCaId);
		moSvc.setMetaDataEntry(user, issueCaId, new MetaDataItem(
				JournalConstants.LMD_FIELD_PRINT_PUBLISHED_DATE, dateString));
		ContentAssembly articlesCa = ProjectBrowserUtils.getChildCaByType(context,
				issueCa, JournalConstants.CA_TYPE_ISSUE_ARTICLES);
		List<ContentAssembly> articleCaList = ProjectBrowserUtils.getChildrenCaByType(
				context, articlesCa, JournalConstants.CA_TYPE_ARTICLE);
		for (ContentAssembly articleCa : articleCaList) {
			String articleCaId = articleCa.getId();
			log.info("execute: set date string " + dateString
					+ " on article CA " + articleCaId);
			moSvc.setMetaDataEntry(user, articleCaId,
					new MetaDataItem(
							JournalConstants.LMD_FIELD_PRINT_PUBLISHED_DATE,
							dateString));
		}

	}

}
