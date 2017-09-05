package org.theiet.rsuite.journals.actionhandlers;

import static com.rsicms.projectshelper.utils.ProjectContentAssemblyUtils.*;

import java.io.*;
import java.util.*;
import java.util.zip.ZipOutputStream;

import org.apache.commons.logging.Log;
import org.theiet.rsuite.*;
import org.theiet.rsuite.books.utils.BookUtils;
import org.theiet.rsuite.journals.JournalConstants;
import org.theiet.rsuite.journals.domain.article.delivery.digitallibrary.ArticleDigitalLibraryPackageBuilder;
import org.theiet.rsuite.journals.utils.*;
import org.theiet.rsuite.utils.*;

import com.reallysi.rsuite.api.*;
import com.reallysi.rsuite.api.workflow.*;
import com.reallysi.rsuite.service.ManagedObjectService;
import com.rsicms.projectshelper.utils.browse.ProjectBrowserUtils;
import com.rsicms.rsuite.helpers.utils.net.ftp.*;

public class SendToInspecFromIssue extends AbstractBaseActionHandler {

	private static final long serialVersionUID = 1L;
	@Override
	public void execute(WorkflowExecutionContext context) throws Exception {
		
		Log log = context.getWorkflowLog();
		User user = getSystemUser();
		
		ManagedObjectService moSvc = context.getManagedObjectService();
		String issueCaId = context.getVariable(JournalConstants.WF_VAR_RSUITE_CONTENTS);
		ContentAssembly issueCa =  getCAFromMO(context, user, issueCaId);
		
		Date today = Calendar.getInstance().getTime();
		String dateString = IetConstants.UK_DATE_FORMAT.format(today);
		log.info("execute: set date string " + dateString + " on issue CA " + issueCaId);
		moSvc.setMetaDataEntry(user, issueCaId, new MetaDataItem(JournalConstants.LMD_FIELD_PRINT_PUBLISHED_DATE, dateString));
		ContentAssembly articlesCa = ProjectBrowserUtils.getChildCaByType(context, issueCa, JournalConstants.CA_TYPE_ISSUE_ARTICLES);
		List<ContentAssembly> articleCaList = ProjectBrowserUtils.getChildrenCaByType(context, articlesCa, JournalConstants.CA_TYPE_ARTICLE);
		
		String inspecClassifier = "";
		try {
			String journalCode = context.getVariable(JournalConstants.WF_VAR_JOURNAL_ID);
			String journalCaId = JournalUtils.getJournalCaId(log, user, context, context.getSearchService(), journalCode);
			ContentAssembly journalCA = getCAFromMO(context, getSystemUser(), journalCaId);
			inspecClassifier = BookUtils.getUserIdFormLmd(journalCA, JournalConstants.LMD_FIELD_INSPEC_CLASSIFIER);
		}
		catch (RSuiteException e) {
			ExceptionUtils.throwWorfklowException(context, "Unable to get journal or inspec classifier");
		}

		Map<String, InputStream>inspecStreams = new HashMap<String, InputStream>();

		for (ContentAssembly articleCa : articleCaList) {
			String articleCaId = articleCa.getId();
			log.info("execute: set date string " + dateString + " on article CA " + articleCaId);
			moSvc.setMetaDataEntry(user, articleCaId, new MetaDataItem(JournalConstants.LMD_FIELD_PRINT_PUBLISHED_DATE, dateString));
			packageArticleForInspecFromIssue(context, user, moSvc, log, articleCa, inspecStreams);
		}
		
		log.info("execute: get connection");
		FTPConnectionInfo connectionInfo = FTPConnectionInfoFactory
		.getFTPConnection(context, inspecClassifier);
		String exportPath = FTPConnectionInfoFactory.getTargetFolder(context, 
				inspecClassifier, 
				JournalConstants.PROP_INSPEC_FTP_FOLDER) + "/IET Published Journals";
		
		for (Map.Entry<String, InputStream> entry : inspecStreams.entrySet()) {
			String articleId = entry.getKey();
			InputStream inStream = entry.getValue();
			log.info("execute: upload " + articleId);
			FTPUtils.uploadStream(connectionInfo, inStream, articleId + ".zip", exportPath);
		}
		
	}
		
	private void packageArticleForInspecFromIssue(WorkflowExecutionContext context,
			User user,
			ManagedObjectService moSvc,
			Log log,
			ContentAssembly articleCa,
			Map<String, InputStream> inspecStreams) throws RSuiteException {
		String lmdInspectRequired =  articleCa.getLayeredMetadataValue(JournalConstants.LMD_FIELD_INSPEC_REQUIRED);
		if (!StringUtils.isBlank(lmdInspectRequired)) {
			if (IetConstants.LMD_VALUE_NO.equals(lmdInspectRequired)) {
				log.info("packageArticleForInspecFromIssue: Inspec classification not required, skipping");
				return;
			}
		}
		
		String articleId = articleCa.getDisplayName();
		log.info("packageArticleForInspecFromIssue: process article " + articleId);
		ContentAssembly typesetterCa = ProjectBrowserUtils.getChildCaByType(context, articleCa, IetConstants.CA_TYPE_TYPESETTER);
		if (typesetterCa == null) {
			throw new RSuiteException("Unable to find typesetter Ca for article " + articleId);
		}
		
		ManagedObject finalPDFMo = null;
		ManagedObject xmlMo = null;
		List<? extends ContentAssemblyItem> childList = typesetterCa.getChildrenObjects();
		for (ContentAssemblyItem child : childList) {
			if (child instanceof ManagedObjectReference) {
				String moId = ((ManagedObjectReference)child).getTargetId();
				log.info("doDigitalLibraryZip: check moId " + moId);
				ManagedObject mo = moSvc.getManagedObject(user, moId);
				if (mo.isNonXml()) {
					String displayName = mo.getDisplayName();
					if (displayName.toLowerCase().endsWith("final.pdf")) {
						if (finalPDFMo != null) {
							throw new RSuiteException("More than one final PDF found");
						}
						else {
							finalPDFMo = mo;
						}
					}
				}
				else {
					String localName = mo.getLocalName();					
					if ("article".equals(localName)) {
						if (xmlMo != null) {
							throw new RSuiteException("More than one XML article found");
						}
						else {
							xmlMo = mo;
						}
					}
				}
			}
		}
		
		//	Stat article
		if (finalPDFMo == null) throw new RSuiteException("No final PDF found");
		if (xmlMo == null) throw new RSuiteException("No XML article found");
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			ZipOutputStream zos = new ZipOutputStream(baos);

			//	PDF
			String finalPDFFileName = articleId + ".pdf";
			log.info("packageArticleForInspecFromIssue: add " + finalPDFFileName + " to zip");
			ArticleDigitalLibraryPackageBuilder.addMoToZip(finalPDFMo, finalPDFFileName, zos);
			
			// XML
			String xmlFileName = articleId + ".xml";
			log.info("packageArticleForInspecFromIssue: add " + xmlFileName + " to zip");
			ArticleDigitalLibraryPackageBuilder.addMoToZip(xmlMo, xmlFileName, zos);
			zos.close();
			InputStream bais = new ByteArrayInputStream(baos.toByteArray());
			inspecStreams.put(articleId, bais);
			bais.close();		
		}
		catch (IOException e) {
			throw new RSuiteException(0, "Failure in packaging " + e.getMessage(), e);
		}

	}

}
