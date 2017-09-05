package org.theiet.maintenance.webservice;

import static org.theiet.rsuite.journals.JournalConstants.*;

import java.util.List;

import org.theiet.rsuite.utils.*;

import com.reallysi.rsuite.api.*;
import com.reallysi.rsuite.api.control.*;
import com.reallysi.rsuite.api.remoteapi.*;
import com.reallysi.rsuite.service.*;
import com.rsicms.projectshelper.utils.browse.ProjectBrowserUtils;
import com.rsicms.rsuite.helpers.webservice.XmlRemoteApiResult;

/**
 * Custom RSuite web service to save search results to user's clipboard
 * 
 */
public class Iet724 extends DefaultRemoteApiHandler {

	private static final String AVAILABLE = "Available";

	/**
	 * Create an MO and save all the search results as a list in the MO
	 */
	@Override
	public RemoteApiResult execute(RemoteApiExecutionContext context,
			CallArgumentList args) throws RSuiteException {

		Session sess = context.getSession();
		User user = sess.getUser();

		String result = "<result>";

		String journalQuery = "/rs_ca_map/rs_ca[rmd:get-type(.) = 'journal']";
		journalQuery = XpathUtils.resolveRSuiteFunctionsInXPath(journalQuery);
		SearchService srchSvc = context.getSearchService();

		int maxResult = 200;

		List<ManagedObject> journalList = SearchUtils.executeXpathSearch(user,
				journalQuery, srchSvc, maxResult);

		ContentAssemblyService caSvc = context.getContentAssemblyService();

		for (ManagedObject journalMo : journalList) {
			ContentAssembly journalCa = caSvc.getContentAssembly(user,
					journalMo.getId());

			ContentAssembly availableCa = ProjectBrowserUtils.getChildCaByDisplayName(
					context, journalCa, AVAILABLE);

			String availableRefId = availableRefId(journalCa);

			if (availableRefId != null) {
				caSvc.detach(user, journalCa.getId(), availableRefId,
						new ObjectDetachOptions());
			}

			if (availableCa != null) {
				caSvc.removeContentAssembly(user, availableCa.getId());
			}

			String xpath = createXpath(journalCa);
			caSvc.createSmartFolderFromXpathSearch(user, xpath,
					journalCa.getId(), AVAILABLE,
					new ContentAssemblyCreateOptions());

			availableRefId = availableRefId(caSvc.getContentAssembly(user,
					journalMo.getId()));

			ObjectReferenceMoveOptions objectReferenceMoveOptions = new ObjectReferenceMoveOptions();
			objectReferenceMoveOptions.setPosition(1);
			caSvc.moveReference(user, availableRefId, journalCa.getId(),
					objectReferenceMoveOptions);

			result += "<fixedJournal>"
					+ journalCa.getDisplayName().replace("&", "")
					+ "</fixedJournal>";
		}

		return new XmlRemoteApiResult(result += "</result>");

	}

	public String createXpath(ContentAssembly journalCa) throws RSuiteException {
		String xpath = "/rs_ca_map/rs_ca[rmd:get-type(.) = 'article' and rmd:get-lmd-value(., 'journal_code') = '"
				+ journalCa.getLayeredMetadataValue(LMD_FIELD_JOURNAL_CODE)
				+ "' and rmd:get-lmd-value(., 'available') = 'yes']";
		return XpathUtils.resolveRSuiteFunctionsInXPath(xpath);
	}

	public String availableRefId(ContentAssembly journalCa)
			throws RSuiteException {
		String availableRefId = null;

		List<? extends ContentAssemblyItem> childrenObjects = journalCa
				.getChildrenObjects();
		for (ContentAssemblyItem contentAssemblyItem : childrenObjects) {
			String displayName = contentAssemblyItem.getDisplayName();
			if (AVAILABLE.equals(displayName)) {
				availableRefId = contentAssemblyItem.getId();
			}
		}
		return availableRefId;
	}

}
