package org.theiet.rsuite.journals.actionhandlers;

import static com.rsicms.projectshelper.utils.ProjectContentAssemblyUtils.*;

import java.io.File;
import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.theiet.rsuite.FTPConnectionInfoFactory;
import org.theiet.rsuite.books.utils.BookUtils;
import org.theiet.rsuite.datamodel.ExternalCompanyUser;
import org.theiet.rsuite.journals.JournalConstants;
import org.theiet.rsuite.utils.ExceptionUtils;

import com.reallysi.rsuite.api.*;
import com.reallysi.rsuite.api.workflow.WorkflowExecutionContext;
import com.reallysi.rsuite.service.ManagedObjectService;
import com.rsicms.projectshelper.workflow.actionhandlers.AbstractActionHandler;
import com.rsicms.rsuite.helpers.utils.net.ftp.*;

public class FtpPackageToTypesetter extends AbstractActionHandler implements JournalConstants {

	private static final long serialVersionUID = 1L;

	@Override
	public void executeTask (WorkflowExecutionContext context) throws Exception {
		Log log = context.getWorkflowLog();
		User user = getSystemUser();
		String pId = context.getProcessInstanceId();
		String zipFileName = context.getVariable("rsuiteSourceFilePath");
		File wfFile = context.getFileWorkflowObject().getFile();
		File tmpDir = new File(wfFile.getParentFile().getParentFile(), "temp");
		File zipFile = new File(tmpDir, zipFileName);
		if (!zipFile.exists()) {
			String failDetail = "zipFile " + zipFile.getAbsolutePath()
			+ " not found";
			context.setVariable(JournalConstants.WF_VAR_ERROR_MSG, ExceptionUtils.constructWfReportLink(failDetail, pId));
			throw new Exception(failDetail);
		}
		
		String journalCaId = context.getVariable(WF_VAR_JOURNAL_CA_ID);
		
		ManagedObjectService moSvc = context.getManagedObjectService();
		try {
		ContentAssembly journalCa = getCAFromMO(context, getSystemUser(), journalCaId);
		ExternalCompanyUser typesetterUser = BookUtils.getTypeSetterUser(context, journalCa);
		
		
		FTPConnectionInfo connectionInfo = FTPConnectionInfoFactory.getFTPConnection(context, typesetterUser.getUserId());

		log.info("execute: sending" + connectionInfo + "\n" + zipFile.getAbsolutePath());
		
		String ftpFolder = FTPConnectionInfoFactory.getTargetFolder(context,
				typesetterUser.getUserId(),
				PROP_TYPESETTER_FTP_MANUSCRIPT_FOLDER);
		
		FTPUtils.uploadFile(connectionInfo, zipFile, ftpFolder);
		ArrayList<MetaDataItem>lmdList =  new ArrayList<MetaDataItem>();
		lmdList.add(new MetaDataItem(LMD_FIELD_AWAITING_TYPESETTER_UPDATES, LMD_VALUE_YES));
		lmdList.add(new MetaDataItem(LMD_FIELD_TYPESETTER_UPDATE_TYPE, LMD_VALUE_INITIAL));
		String articleCaId = context.getVariable(WF_VAR_RSUITE_CONTENTS);
		moSvc.setMetaDataEntries(user, articleCaId, lmdList);
		log.info("execute: Transfer OK, set typesetter flag on articleCaId " + articleCaId);
		}
		catch (RSuiteException e) {
			ExceptionUtils.throwWorfklowException(context, e, "RSuite Exception was " + e.getMessage());
		}
		catch (FTPUtilsException e) {
			ExceptionUtils.throwWorfklowException(context, e, "FTP Exception was " + e.getMessage());
		}

	}

}
