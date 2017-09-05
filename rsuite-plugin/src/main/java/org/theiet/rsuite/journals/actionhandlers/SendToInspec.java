package org.theiet.rsuite.journals.actionhandlers;

import static com.rsicms.projectshelper.utils.ProjectContentAssemblyUtils.*;

import java.io.*;
import java.util.List;

import org.apache.commons.logging.Log;
import org.theiet.rsuite.*;
import org.theiet.rsuite.books.utils.BookUtils;
import org.theiet.rsuite.journals.JournalConstants;
import org.theiet.rsuite.journals.domain.article.ArticleContentAssemblyItemFilter;
import org.theiet.rsuite.journals.domain.issues.IETObjectNameInspecHandler;
import org.theiet.rsuite.journals.utils.JournalUtils;
import org.theiet.rsuite.utils.*;

import com.reallysi.rsuite.api.*;
import com.reallysi.rsuite.api.control.ContentAssemblyItemFilter;
import com.reallysi.rsuite.api.workflow.WorkflowExecutionContext;
import com.rsicms.projectshelper.workflow.actionhandlers.AbstractActionHandler;
import com.rsicms.rsuite.helpers.download.*;
import com.rsicms.rsuite.helpers.utils.net.ftp.*;

public class SendToInspec extends AbstractActionHandler implements
		JournalConstants {

	private static final long serialVersionUID = 1L;

	@Override
	public void executeTask (WorkflowExecutionContext context) throws Exception {

		Log log = context.getWorkflowLog();
		User user = getSystemUser();

		String articleCaId = context.getVariable(WF_VAR_RSUITE_CONTENTS);

		ContentAssembly articleCa = getCAFromMO(context, user,
				articleCaId);
		
		if (articleCa == null) {
			ExceptionUtils.throwWorfklowException(context, "Unable to find article content assembly");
		}
		
//		HG: Add logic to bypass inspect if not required
		String lmdInspectRequired =  articleCa.getLayeredMetadataValue(LMD_FIELD_INSPEC_REQUIRED);
		if (!StringUtils.isBlank(lmdInspectRequired) && IetConstants.LMD_VALUE_NO.equals(lmdInspectRequired)) {
				log.info("execute: Inspec classification not required, skipping");
				return;
		}
		
		log.info("trying to sent to INSPEC classifcation...");


		
		String articleId = articleCa.getDisplayName();

		List<? extends ContentAssemblyItem> childObjects = articleCa
				.getChildrenObjects();

		String typeSetterCaId = null;

		for (ContentAssemblyItem caItem : childObjects) {
			if (IetConstants.CA_NAME_TYPESETTER.equalsIgnoreCase(caItem.getDisplayName())
					&& (caItem.getObjectType() == ObjectType.CONTENT_ASSEMBLY || caItem
							.getObjectType() == ObjectType.CONTENT_ASSEMBLY_REF)) {

				if (caItem.getObjectType() == ObjectType.CONTENT_ASSEMBLY_REF) {
					ContentAssemblyReference caRef = (ContentAssemblyReference) caItem;
					typeSetterCaId = caRef.getTargetId();
				} else {
					typeSetterCaId = caItem.getId();
				}

			}
		}

		if (typeSetterCaId == null) {
			ExceptionUtils.throwWorfklowException(context, "Unable to find a  content assembly with name "
					+ IetConstants.CA_NAME_TYPESETTER + " in the article CA");
		}

		ContentAssembly typeSetterCa = getCAFromMO(context, user,
				typeSetterCaId);

		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		RSuiteObjectNameHandler nameHandler = new IETObjectNameInspecHandler(articleId);
		ContentAssemblyItemFilter caItemFilter = new ArticleContentAssemblyItemFilter(context);

		ZipHelperConfiguration zipConfiguration = new ZipHelperConfiguration();
		zipConfiguration.setObjectNameHandler(nameHandler);
		zipConfiguration.setCaItemFilter(caItemFilter);

		ZipHelper.zipContentAssemblyContents(context, typeSetterCa, outStream,zipConfiguration);

		String zipFileName = articleCa.getDisplayName() + ".zip";
		
		String journalCode = context.getVariable(WF_VAR_JOURNAL_ID);

		String journalCaId = JournalUtils.getJournalCaId(log, user, context, context.getSearchService(), journalCode);
		
		ContentAssembly journalCA = getCAFromMO(context, getSystemUser(), journalCaId);
		
		String inspecClassifier = BookUtils.getUserIdFormLmd(journalCA, LMD_FIELD_INSPEC_CLASSIFIER);
		try {
		FTPConnectionInfo connectionInfo = FTPConnectionInfoFactory
				.getFTPConnection(context, inspecClassifier);

		ByteArrayInputStream inStream = new ByteArrayInputStream(
				outStream.toByteArray());

		
		String exportPath = FTPConnectionInfoFactory.getTargetFolder(context, inspecClassifier, PROP_INSPEC_FTP_FOLDER);
		
		FTPUtils.uploadStream(connectionInfo, inStream, zipFileName, exportPath);
		}
		catch (FTPUtilsException e) {
			ExceptionUtils.throwWorfklowException(context, e, "FTP Exception was " + e.getMessage());			
		}
		PubtrackLogger.logToProcess(user, context, log, "ARTICLE", articleId, JournalConstants.PUBTRACK_INSPEC_CLASSIFICATION_REQUESTED);

		log.info("Sent successfully to INSPEC classifcation...");
	}

}
