package org.theiet.rsuite.onix.domain;

import java.io.*;

import org.theiet.rsuite.onix.OnixConstants;

import com.reallysi.rsuite.api.*;
import com.reallysi.rsuite.api.extensions.ExecutionContext;
import com.reallysi.rsuite.service.ContentAssemblyService;
import com.rsicms.projectshelper.upload.UploadUtils;
import com.rsicms.projectshelper.utils.browse.ProjectBrowserUtils;

public class OnixUploader implements OnixConstants{

	public static void uploadOnixResultFile(ExecutionContext context,
			String recipientCaId, File resultFile)
			throws RSuiteException, IOException {
		User user = context.getAuthorizationService().getSystemUser();
		ContentAssemblyService caSvc = context.getContentAssemblyService();
		
		ContentAssembly recipentCa = caSvc.getContentAssembly(user, recipientCaId);
		
		ContentAssembly outputCa = ProjectBrowserUtils.getChildCaByType(context, recipentCa, CA_TYPE_ONIX_OUTPUT);
		
		if (outputCa == null){
			throw new RSuiteException("There is no ouput container for " + recipentCa.getDisplayName() + " [ " + recipientCaId + "]");
		}
	
		
		File wfFolder = resultFile.getParentFile();
		File fileToUpload = new File(wfFolder, recipentCa.getDisplayName() + ONIX_FILE_SUFFIX);
		
				
		OnixRSuiteSchemaIdReverter schemaReverter = new OnixRSuiteSchemaIdReverter();
		schemaReverter.revertRSuiteOnixSchema(resultFile, fileToUpload);
		
		UploadUtils.uploadOutputFile(context, outputCa, fileToUpload);
	}

}
