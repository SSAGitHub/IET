package org.theiet.rsuite.journals.domain.journal;

import static org.theiet.rsuite.IetConstants.LMD_FIELD_EDITORIAL_ASSISTANT;
import static org.theiet.rsuite.IetConstants.LMD_FIELD_PRODUCTION_CONTROLLER_USER;
import static org.theiet.rsuite.IetConstants.LMD_FIELD_TYPESETTER_USER;
import static org.theiet.rsuite.journals.JournalConstants.CA_NAME_ARTICLES;
import static org.theiet.rsuite.journals.JournalConstants.CA_NAME_AVAILABLE;
import static org.theiet.rsuite.journals.JournalConstants.CA_NAME_ISSUES;
import static org.theiet.rsuite.journals.JournalConstants.CA_TYPE_ARTICLES;
import static org.theiet.rsuite.journals.JournalConstants.CA_TYPE_ISSUES;
import static org.theiet.rsuite.journals.JournalConstants.CA_TYPE_JOURNAL;
import static org.theiet.rsuite.journals.JournalConstants.LMD_FIELD_ADD_PREFIX_DIGITAL_LIBRARY_DELIVERY;
import static org.theiet.rsuite.journals.JournalConstants.LMD_FIELD_INSPEC_CLASSIFIER;
import static org.theiet.rsuite.journals.JournalConstants.LMD_FIELD_JOURNAL_CODE;
import static org.theiet.rsuite.journals.JournalConstants.LMD_FIELD_JOURNAL_EMAIL;
import static org.theiet.rsuite.journals.JournalConstants.LMD_FIELD_JOURNAL_WORFLOW_TYPE;

import java.util.ArrayList;
import java.util.List;

import org.theiet.rsuite.journals.JournalConstants;
import org.theiet.rsuite.journals.datatype.JournalWorkflowType;
import org.theiet.rsuite.journals.utils.JournalUtils;
import org.theiet.rsuite.utils.XpathUtils;

import com.reallysi.rsuite.api.ContentAssembly;
import com.reallysi.rsuite.api.ManagedObject;
import com.reallysi.rsuite.api.MetaDataItem;
import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.User;
import com.reallysi.rsuite.api.extensions.ExecutionContext;
import com.reallysi.rsuite.service.ContentAssemblyService;
import com.rsicms.projectshelper.utils.ProjectContentAssemblyUtils;

public class JournalCreator {

	/**
	 * Create journal content assembly
	 * 
	 * @param context
	 *            The web service context
	 * @param args
	 *            The web service argument list
	 * @param productCode
	 *            the product code
	 * @param booksCaId
	 *            books container CA id
	 * @return created journal content assembly
	 * @throws RSuiteException
	 */
	public static ContentAssembly createJournalCa(ExecutionContext context,
			User user, JournalCreateDTO params, String journalsCaId)
			throws Exception {

		String journalCode = params.getJournalCode();

		checkIfJournalAlreadyExistr(context, user, journalCode);

		ContentAssembly journalCa = ProjectContentAssemblyUtils.createContentAssembly(context,
				journalsCaId, params.getJournalName(), CA_TYPE_JOURNAL);

		List<MetaDataItem> metadataList = createMetadataList(params,
				journalCode);
		context.getManagedObjectService().setMetaDataEntries(user,
				journalCa.getId(), metadataList);

		Journal journal = new Journal(context, journalCode, journalCa.getId());

		createJournalStructure(context, user, journal);

		return journalCa;
	}

	public static void checkIfJournalAlreadyExistr(ExecutionContext context,
			User user, String journalCode) throws RSuiteException, Exception {
		List<ManagedObject> searchResult = JournalUtils.searchJournalWithCode(
				user, context.getSearchService(), journalCode);

		if (searchResult.size() > 0) {
			throw new Exception("Journal with code " + journalCode
					+ " already exist");
		}
	}

	public static List<MetaDataItem> createMetadataList(
			JournalCreateDTO params, String journalCode) {
		List<MetaDataItem> metadataList = new ArrayList<MetaDataItem>();

		metadataList.add(new MetaDataItem(LMD_FIELD_JOURNAL_CODE, journalCode));
		metadataList.add(new MetaDataItem(LMD_FIELD_JOURNAL_EMAIL, params
				.getJournalEmail()));
		metadataList.add(new MetaDataItem(LMD_FIELD_EDITORIAL_ASSISTANT, params
				.getEditorialAssistant()));
		metadataList.add(new MetaDataItem(LMD_FIELD_PRODUCTION_CONTROLLER_USER,
				params.getProducationController()));
		metadataList.add(new MetaDataItem(LMD_FIELD_TYPESETTER_USER, params
				.getTypesetter()));
		metadataList.add(new MetaDataItem(LMD_FIELD_INSPEC_CLASSIFIER, params
				.getInpsecClassifier()));

		metadataList.add(new MetaDataItem(LMD_FIELD_JOURNAL_WORFLOW_TYPE,
				params.getWorkflowType()));

		metadataList.add(new MetaDataItem(
				LMD_FIELD_ADD_PREFIX_DIGITAL_LIBRARY_DELIVERY, params
						.getAddPrefixForDigitalLibraryDelivery()));
		return metadataList;
	}

	/**
	 * Creates journal content assembly structure
	 * 
	 * @param context
	 *            execution context
	 * @param journalCa
	 *            journal content assembly
	 * @param params
	 * @throws RSuiteException
	 */
	private static void createJournalStructure(ExecutionContext context,
			User user, Journal journal) throws RSuiteException {

		String journalCode = journal.getJournalCode();
		ContentAssembly journalCa = journal.getJournalCa();
		ContentAssemblyService caService = context.getContentAssemblyService();

		String journalCaId = journalCa.getId();

		ProjectContentAssemblyUtils.createContentAssembly(context, journalCaId, CA_NAME_ARTICLES,
				CA_TYPE_ARTICLES);

		String xpath = "/rs_ca_map/rs_ca[rmd:get-type(.) = 'article' and rmd:get-lmd-value(., 'journal_code') = '"
				+ journalCode
				+ "' and rmd:get-lmd-value(., 'available') = 'yes']";

		xpath = XpathUtils.resolveRSuiteFunctionsInXPath(xpath);
		caService.createDynamicCANodeFromXPath(user, xpath, journalCaId,
				CA_NAME_AVAILABLE);

		ProjectContentAssemblyUtils.createContentAssembly(context, journalCaId, CA_NAME_ISSUES,
				CA_TYPE_ISSUES);

		if (journal.getWorflowType() == JournalWorkflowType.ARCHIVE) {
			ProjectContentAssemblyUtils.createContentAssembly(context, journalCa.getId(),
					"Archive", JournalConstants.CA_TYPE_JOURNAL_ARCHIVE);
		}

	}

}
