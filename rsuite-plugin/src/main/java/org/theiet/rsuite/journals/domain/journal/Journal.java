package org.theiet.rsuite.journals.domain.journal;

import static org.theiet.rsuite.journals.domain.constants.JournalsCaTypes.JOURNAL_ARCHIVE;
import static org.theiet.rsuite.journals.domain.constants.JournalsCaTypes.JOURNAL_ARCHIVE_MONTH;
import static org.theiet.rsuite.journals.domain.constants.JournalsCaTypes.JOURNAL_ARCHIVE_YEAR;

import java.util.*;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.theiet.rsuite.datatype.deliveryUser.DeliveryUser;
import org.theiet.rsuite.datatype.deliveryUser.DeliveryUserFactory;
import org.theiet.rsuite.domain.date.IetDate;
import org.theiet.rsuite.journals.JournalConstants;
import org.theiet.rsuite.journals.datatype.JournalGenerationType;
import org.theiet.rsuite.journals.datatype.JournalWorkflowType;
import org.theiet.rsuite.journals.domain.article.Article;
import org.theiet.rsuite.journals.domain.issues.datatype.IsssueArticlesComparator;
import org.theiet.rsuite.journals.utils.JournalUtils;
import org.theiet.rsuite.utils.IetUtils;
import org.theiet.rsuite.utils.XpathUtils;

import com.reallysi.rsuite.api.ContentAssembly;
import com.reallysi.rsuite.api.ContentAssemblyItem;
import com.reallysi.rsuite.api.ManagedObject;
import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.User;
import com.reallysi.rsuite.api.control.ObjectAttachOptions;
import com.reallysi.rsuite.api.control.ObjectDetachOptions;
import com.reallysi.rsuite.api.extensions.ExecutionContext;
import com.reallysi.rsuite.service.ContentAssemblyService;
import com.reallysi.rsuite.service.SearchService;
import com.rsicms.projectshelper.utils.ProjectContentAssemblyUtils;
import com.rsicms.projectshelper.utils.ProjectUserUtils;
import com.rsicms.projectshelper.utils.browse.ProjectBrowserUtils;

public class Journal {

    private static final String LMD_FIELD_JOURNAL_CODE = "journal_code";
    
    private static final String LMD_FIELD_ADD_PREFIX_DIGITAL_LIBRARY_DELIVERY = "add_prefix_digital_library_delivery";
    
    private static final String LMD_FIELD_JOURNAL_WORFLOW_TYPE = "journal_worflow_type";
    
    private static final String LMD_FIELD_JOURNAL_GENERATION_TYPE = "journal_generation_type";
    
    private static final String LMD_FIELD_JOURNAL_PRINTER_USER = "journal_issue_printer";
    
    private static final String LMD_FIELD_ONLINE_PUBLISHED_DATE = "online_published_date";
    
    private static final String LMD_FIELD_ARTICLE_AVAILABLE = "available";
    
    private static final String LMD_FIELD_TYPESETTER_USER = "typesetter";
    
    private static final String LMD_FIELD_PRODUCTION_CONTROLLER_USER = "production_controller";
    
    private static final String LMD_VALUE_YES = "yes";
    
	private Log log = LogFactory.getLog(getClass());

	private String journalCode;

	private ContentAssembly journalCa;

	private ExecutionContext context;

	private ContentAssemblyService caService;

	private User user;

	public Journal(ExecutionContext context, ContentAssembly journalCa)
            throws RSuiteException {
        this.context = context;
        this.caService = context.getContentAssemblyService();
        this.journalCa = journalCa;
        this.journalCode = journalCa.getLayeredMetadataValue(LMD_FIELD_JOURNAL_CODE);
        initializeFields(context, journalCode);
    }
	
	public Journal(ExecutionContext context, String journalCode, String journalCaId)
			throws RSuiteException {
		initializeFields(context, journalCode);
		journalCa = caService.getContentAssembly(user, journalCaId);
	}
	
	public Journal(ExecutionContext context, String journalCode)
			throws RSuiteException {
		
		initializeFields(context, journalCode);

		SearchService srchSvc = context.getSearchService();

		String journalCaId = JournalUtils.getJournalCaId(log, user, context,
				srchSvc, journalCode);

		
		journalCa = caService.getContentAssembly(user, journalCaId);

	}

	private void initializeFields(ExecutionContext context, String journalCode) {
		this.journalCode = journalCode;
		this.context = context;
		caService = context.getContentAssemblyService();
		this.user = context.getAuthorizationService().getSystemUser();
	}

	public void archiveAvailableArticles(ContentAssembly journalArchvie)
			throws RSuiteException {
		ContentAssemblyService caSvc = context.getContentAssemblyService();

		ObjectDetachOptions detachOpts = new ObjectDetachOptions();

		ContentAssembly articlePoolCa = ProjectBrowserUtils.getChildCaByType(context,
				journalCa, JournalConstants.CA_TYPE_ARTICLES);
		String articlePoolCaId = articlePoolCa.getId();

		Map<String, String> refIdMap = ProjectBrowserUtils.getCaRefIdMap(user, caSvc,
				articlePoolCa);

		if (journalArchvie != null) {
			List<ManagedObject> availableArticles = getAvailableArticles(
					context, user, journalCode);
			for (ManagedObject availableArticle : availableArticles) {

				ContentAssembly articleCa = caSvc.getContentAssembly(user,
						availableArticle.getId());
				String articleCaId = articleCa.getId();

				archiveArticle(journalArchvie, articleCa);

				caSvc.detach(user, articlePoolCaId, refIdMap.get(articleCaId),
						detachOpts);

			}
		}
	}

	public void archiveArticle(ContentAssembly articleToArchive)
			throws RSuiteException {
		ContentAssembly archiveCa = getArchiveCa();
		if (archiveCa == null) {
			throw new RSuiteException(
					"Archive container doesn't exist for journal "
							+ journalCode + " archiving: "
							+ articleToArchive.getId());
		}

		archiveArticle(archiveCa, articleToArchive);
		detachArticleFromArticlesPool(articleToArchive);
	}

	private void archiveArticle(ContentAssembly archiveCa,
			ContentAssembly articleToArchive) throws RSuiteException {

		String publishDateValue = articleToArchive
				.getLayeredMetadataValue(LMD_FIELD_ONLINE_PUBLISHED_DATE);

		
		Calendar publishDate = IetDate.parseDate(publishDateValue);
		ContentAssembly monthCa = getArchiveMonthCa(archiveCa, publishDate);
		caService.attach(user, monthCa.getId(), articleToArchive,
				new ObjectAttachOptions());
		IetUtils.removeMetaDataFieldFromCa(log, user,
				context.getManagedObjectService(), articleToArchive,
				LMD_FIELD_ARTICLE_AVAILABLE);
	}

	public void detachArticleFromArticlesPool(final ContentAssembly article)
			throws RSuiteException {
		
		ContentAssembly articlePool = ProjectBrowserUtils.getChildCaByType(context, journalCa,
				JournalConstants.CA_TYPE_ARTICLES);
		
		ContentAssemblyItem caRef = ProjectBrowserUtils.getFristCaRef(context,
				articlePool, article);
		caService.detach(user, articlePool.getId(), caRef.getId(),
				new ObjectDetachOptions());
	}

	private ContentAssembly getArchiveMonthCa(ContentAssembly journalArchvie,
			Calendar publishDate) throws RSuiteException {

		String year = String.valueOf(publishDate.get(Calendar.YEAR));
		String month = String.valueOf((publishDate.get(Calendar.MONTH) + 1));

		ContentAssembly monthCa = null;

		synchronized (journalCode.intern()) {
			ContentAssembly yearCa = ProjectBrowserUtils.getChildCaByNameAndType(
					context, journalArchvie, JOURNAL_ARCHIVE_YEAR, year);
			if (yearCa == null) {
				yearCa = ProjectContentAssemblyUtils.createContentAssembly(context,
						journalArchvie.getId(), year, JOURNAL_ARCHIVE_YEAR);
			}

			monthCa = ProjectBrowserUtils.getChildCaByNameAndType(context,
					yearCa, JOURNAL_ARCHIVE_MONTH, month);
			if (monthCa == null) {
				monthCa = ProjectContentAssemblyUtils.createContentAssembly(context,
						yearCa.getId(), month, JOURNAL_ARCHIVE_MONTH);
			}
		}

		return monthCa;

	}

	public ContentAssembly getArchiveCa() throws RSuiteException {
		ContentAssembly journalArchvie = ProjectBrowserUtils.getChildCaByType(context,
				journalCa, JOURNAL_ARCHIVE);
		return journalArchvie;
	}
	
	public JournalAvailableArticles getAvailableArticles() throws RSuiteException{
		List<Article> availableArticles = new ArrayList<Article>();
		for (ManagedObject articleMO : getAvailableArticles(context, user, journalCode)){
			availableArticles.add(new Article(context, user, articleMO));
		}
		
		Collections.sort(availableArticles, new IsssueArticlesComparator());
		
		return new JournalAvailableArticles(availableArticles);
	}

	public static List<ManagedObject> getAvailableArticles(
			ExecutionContext context, User user, String journalCode)
			throws RSuiteException {
		SearchService srchSvc = context.getSearchService();
		String articleQuery = "/rs_ca_map/rs_ca[rmd:get-type(.) = 'article' and "
				+ "rmd:get-lmd-value(., 'journal_code') = '"
				+ journalCode
				+ "' and "
				+ "rmd:get-lmd-value(., '"
				+ LMD_FIELD_ARTICLE_AVAILABLE + "') = '" + LMD_VALUE_YES + "']";

		articleQuery = XpathUtils.resolveRSuiteFunctionsInXPath(articleQuery);

		List<ManagedObject> articleSet = srchSvc.executeXPathSearch(user,
				articleQuery, 1, 0);
		return articleSet;
	}


	public ContentAssembly getJournalCa() {
		return journalCa;
	}

	public JournalWorkflowType getWorflowType() throws RSuiteException {
		
		String workflowTypeLmd = journalCa.getLayeredMetadataValue(LMD_FIELD_JOURNAL_WORFLOW_TYPE);
		
		if (StringUtils.isBlank(workflowTypeLmd)){
			return JournalWorkflowType.ISSUE;
		}
		
		return JournalWorkflowType.valueOf(workflowTypeLmd);
	}

	public boolean requiresPrefixForDigitaLibrary() throws RSuiteException {
		
		String addPrefix = journalCa.getLayeredMetadataValue(LMD_FIELD_ADD_PREFIX_DIGITAL_LIBRARY_DELIVERY);
		
		if ("no".equalsIgnoreCase(addPrefix)){
			return false;
		}
		
		return true;
	}

	public String getJournalCode() {
		return journalCode;
	}
	
	public boolean isAutomaticPdfGenerationWorkflow() throws RSuiteException{
		
		String generationType = journalCa.getLayeredMetadataValue(LMD_FIELD_JOURNAL_GENERATION_TYPE);
		
		if (StringUtils.isNotBlank(generationType) && JournalGenerationType.AUTOMATED == JournalGenerationType.valueOf(generationType)){
			return true;
		}
		
		return false;
	}

	public DeliveryUser getPrinter() throws RSuiteException {
		String printerUserId = journalCa.getLayeredMetadataValue(LMD_FIELD_JOURNAL_PRINTER_USER);
		if (printerUserId != null){
			return DeliveryUserFactory.createDeliveryUser(context, printerUserId);	
		}
		
		return null;
	}

	public JournalArticles getArticles() throws RSuiteException {
		ContentAssembly articlesCA = ProjectBrowserUtils.getChildCaByType(context, journalCa, JournalConstants.CA_TYPE_ARTICLES);
		return new JournalArticles(user, context.getContentAssemblyService(), articlesCA);
	}

	public User getTypestter() throws RSuiteException {	
		return getUserWithValidation(LMD_FIELD_TYPESETTER_USER, "Typesetter"); 
	}
	
	public User getProductionController() throws RSuiteException {
		return getUserWithValidation(LMD_FIELD_PRODUCTION_CONTROLLER_USER, "Production controller");
	}
	
	private User getUserWithValidation(String lmdName, String userType) throws RSuiteException{
		String userId = journalCa
				.getLayeredMetadataValue(lmdName);
	
		if (StringUtils.isBlank(userId)) {
			throw new RSuiteException(
					userType + " is not defined for the journal");
		}
		
		return ProjectUserUtils.getUser(context, userId);
	}
	
	public User getJournalProductionUser() throws RSuiteException{
		return  ProjectUserUtils.getUser(context, "JournalProductionUser");
	}

}
