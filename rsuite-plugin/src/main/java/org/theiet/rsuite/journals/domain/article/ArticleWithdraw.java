package org.theiet.rsuite.journals.domain.article;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.theiet.rsuite.journals.JournalConstants;
import org.theiet.rsuite.journals.utils.JournalMailUtils;
import org.theiet.rsuite.utils.PubtrackLogger;
import org.theiet.rsuite.utils.SearchUtils;
import org.theiet.rsuite.utils.WorkflowUtils;

import com.reallysi.rsuite.api.ContentAssembly;
import com.reallysi.rsuite.api.ManagedObject;
import com.reallysi.rsuite.api.MetaDataItem;
import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.User;
import com.reallysi.rsuite.api.control.ObjectAttachOptions;
import com.reallysi.rsuite.api.control.ObjectDetachOptions;
import com.reallysi.rsuite.api.extensions.ExecutionContext;
import com.reallysi.rsuite.api.pubtrack.Process;
import com.reallysi.rsuite.service.ContentAssemblyService;
import com.reallysi.rsuite.service.ProcessInstanceService;
import com.reallysi.rsuite.service.PubtrackManager;
import com.rsicms.projectshelper.net.mail.MailMessage;
import com.rsicms.projectshelper.net.mail.MailUtils;
import com.rsicms.projectshelper.utils.ProjectContentAssemblyUtils;
import com.rsicms.projectshelper.utils.browse.ProjectBrowserUtils;

public class ArticleWithdraw implements JournalConstants {

	private static Log log = LogFactory.getLog(ArticleWithdraw.class);

	private ExecutionContext context;

	private ContentAssemblyService caSvc;

	private User user;

	private String articleCaId;

	private String articleCaRefId;

	private ContentAssembly articleCa;

	private ContentAssembly journalCa;
	
	private ContentAssembly monthCa;

	public ArticleWithdraw(ExecutionContext context, String articleCaId,
			String articleCaRefId) throws RSuiteException {
		this.context = context;

		user = context.getAuthorizationService().getSystemUser();
		caSvc = context.getContentAssemblyService();
		this.articleCaId = articleCaId;
		this.articleCaRefId = articleCaRefId;
	}

	public void withdrawArticle() throws RSuiteException {

		articleCa = caSvc.getContentAssembly(user, articleCaId);

		checkIfAlreadyWithdrawn();

		journalCa = getJournalCaForArticle();

		killWorkflowRelatedWithArticle();
		moveArticleToWithdrawContainer();
		updateAricleMetada();
		modifyPubtrackProcess();
		sendNotyficationToTypesetter();
	}

	private void checkIfAlreadyWithdrawn() throws RSuiteException {
		String status = articleCa.getLayeredMetadataValue(LMD_FIELD_STATUS);

		if (LMD_VALUE_WITHDRAWN.equals(status)) {
			throw new RSuiteException("Article " + articleCaId
					+ " has been already withdrawn");
		}
	}

	private void sendNotyficationToTypesetter() throws RSuiteException {

		Map<String, String> variables = JournalMailUtils.setUpVariablesMap(
				context, user, journalCa, articleCa);

		String mailSubject = MailUtils.obtainEmailSubject(context,
				PROP_JOURNALS_ARTICLE_WITHDRAWN_NOTIFICATION_MAIL_TITLE);

		String emailFrom = JournalMailUtils.obtainEmailFrom(context, journalCa);
		

		String typesetterEmail = JournalMailUtils.getTypesetterEmail(
				context, journalCa);

		MailMessage message = new MailMessage(emailFrom, typesetterEmail,
				mailSubject);
		message.setMessageTemplateProperty(PROP_JOURNALS_ARTICLE_WITHDRAWN_NOTIFICATION_MAIL_BODY);
		message.setVariables(variables);
		
		MailUtils.sentEmail(context, message);
	}

	private void modifyPubtrackProcess() throws RSuiteException {

		String articleId = articleCa.getId();
		
		PubtrackManager ptMgr = context.getPubtrackManager();
		
		String externalId = PubtrackLogger.getPubtrackExternalId(PUBTRACK_PRODUCT_ARTICLE, articleId);
		List<Process> processList = ptMgr.getProcessByExternalId(user, externalId);

		if (processList.size() > 0){
			PubtrackLogger.logToProcess(user, context, log,
					PUBTRACK_PRODUCT_ARTICLE, articleId,
					JournalConstants.PUBTRACK_ARTICLE_WITHDRAWN);
			PubtrackLogger.completeProcess(user, context, 
					PUBTRACK_PRODUCT_ARTICLE, articleId);
		}
	}

	private void updateAricleMetada() throws RSuiteException {

		MetaDataItem metaDataItem = new MetaDataItem(LMD_FIELD_STATUS,
				LMD_VALUE_WITHDRAWN);
		context.getManagedObjectService().setMetaDataEntry(user,
				articleCa.getId(), metaDataItem);

	}

	private void killWorkflowRelatedWithArticle() throws RSuiteException {

		User systemUSer = context.getAuthorizationService().getSystemUser();
		ProcessInstanceService piSvc = context.getProcessInstanceService();
		List<String> articleWorkflowIdList = WorkflowUtils
				.getRelatedWorkflowIdsWithRSuiteId(articleCa.getId());
		for (String processInstanceId : articleWorkflowIdList) {
			piSvc.killProcessInstance(systemUSer, processInstanceId);
		}
	}

	private void moveArticleToWithdrawContainer() throws RSuiteException {

		detachArticleFromJournalContainer();
		attachArticleToWithdrawnMonthContainer();
	}

	private void attachArticleToWithdrawnMonthContainer()
			throws RSuiteException {
		ContentAssembly withdrawnArticleCa = getWithdrawnArticles(context,
				user, caSvc);
		ContentAssembly monthCa = getArchiveMonthCa(context, withdrawnArticleCa);
		caSvc.attach(user, monthCa.getId(), articleCa,
				new ObjectAttachOptions());
		this.monthCa = monthCa;
	}

	private void detachArticleFromJournalContainer() throws RSuiteException {

		ContentAssembly articlesCA = ProjectBrowserUtils.getChildCaByType(context,
				journalCa, CA_TYPE_ARTICLES);
		caSvc.detach(user, articlesCA.getId(), articleCaRefId,
				new ObjectDetachOptions());
	}

	private ContentAssembly getJournalCaForArticle() throws RSuiteException {
		String journalCode = articleCa
				.getLayeredMetadataValue(LMD_FIELD_JOURNAL_CODE);

		ManagedObject journalMo = SearchUtils.findMoWitRXSsingleCheck(context,
				user, "/rs_ca_map/rs_ca[rmd:get-type(.) = '" + CA_TYPE_JOURNAL
						+ "']" + "[rmd:get-lmd-value(., '"
						+ LMD_FIELD_JOURNAL_CODE + "') = '" + journalCode
						+ "']", "There should be only one " + journalCode
						+ " journal container.");

		return caSvc.getContentAssembly(user, journalMo.getId());
	}

	private ContentAssembly getWithdrawnArticles(ExecutionContext context,
			User user, ContentAssemblyService caSvc) throws RSuiteException {
		ManagedObject withdrawnArticlesMo = SearchUtils
				.findMoWitRXSsingleCheck(context, user,
						"/rs_ca_map/rs_ca[rmd:get-type(.) = '"
								+ CA_TYPE_WITHDRAWN_ARTICLES + "']",
						"There should be only one withdrawn container.");
		ContentAssembly withdrawnArticleCa = caSvc.getContentAssembly(user,
				withdrawnArticlesMo.getId());
		return withdrawnArticleCa;
	}

	private synchronized ContentAssembly getArchiveMonthCa(
			ExecutionContext context, ContentAssembly withdrawnArticleCa)
			throws RSuiteException {

		Calendar caledar = Calendar.getInstance();
		caledar.setTime(new Date());
		String year = String.valueOf(caledar.get(Calendar.YEAR));
		String month = String.valueOf((caledar.get(Calendar.MONTH) + 1));

		ContentAssembly monthCa = null;

		ContentAssembly yearCa = ProjectBrowserUtils.getChildCaByNameAndType(context,
				withdrawnArticleCa, CA_TYPE_JOURNAL_ARCHIVE_YEAR, year);
		if (yearCa == null) {
			yearCa = ProjectContentAssemblyUtils.createContentAssembly(context,
					withdrawnArticleCa.getId(), year,
					CA_TYPE_JOURNAL_ARCHIVE_YEAR);
		}

		monthCa = ProjectBrowserUtils.getChildCaByNameAndType(context, yearCa,
				CA_TYPE_JOURNAL_ARCHIVE_MONTH, month);
		if (monthCa == null) {
			monthCa = ProjectContentAssemblyUtils.createContentAssembly(context, yearCa.getId(),
					month, CA_TYPE_JOURNAL_ARCHIVE_MONTH);
		}

		return monthCa;

	}

	public ContentAssembly getJournalCa() {
		return journalCa;
	}

	public ContentAssembly getMonthCa() {
		return monthCa;
	}
}
