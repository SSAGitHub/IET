package org.theiet.rsuite.utils;

import java.io.ByteArrayInputStream;

import org.theiet.rsuite.FTPConnectionInfoFactory;
import org.theiet.rsuite.books.BooksConstans;
import org.theiet.rsuite.books.datatype.BookRequestDTO;

import com.reallysi.rsuite.api.ContentAssembly;
import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.User;
import com.reallysi.rsuite.api.extensions.ExecutionContext;
import com.rsicms.projectshelper.utils.ProjectContentAssemblyUtils;
import com.rsicms.rsuite.helpers.utils.net.ftp.FTPConnectionInfo;
import com.rsicms.rsuite.helpers.utils.net.ftp.FTPUtils;

public final class ExternalRequestUtils implements BooksConstans {

    private ExternalRequestUtils(){};

	public static void sendBookRequestPackage(ExecutionContext context,
			BookRequestDTO bookRequest, String caIdToSend,
			String externalUserId) throws Exception {

		User user = context.getAuthorizationService().getSystemUser();

		ContentAssembly assemblyToSend = context.getContentAssemblyService()
				.getContentAssembly(user, caIdToSend);

		if (assemblyToSend == null) {
			throw new RSuiteException(
					"Unable to obtain the context content assambly id: "
							+ caIdToSend);
		}

		FTPConnectionInfo connectionInfo = FTPConnectionInfoFactory
				.getFTPConnection(context, externalUserId);

		ByteArrayInputStream inStream = new ByteArrayInputStream(
				ProjectContentAssemblyUtils.zipContentAssembly(context, assemblyToSend, true).toByteArray());

		String fileName = bookRequest.getTargetFileName();

		String ftpFolder = FTPConnectionInfoFactory.getTargetFolder(context,
				externalUserId, bookRequest.getTargetFolderProperty());
		
		bookRequest.addEmailVariable(EMAIL_VAR_FTP_TARGET_FOLDER, ftpFolder);
		

		FTPUtils.uploadStream(connectionInfo, inStream, fileName, ftpFolder);
	}
}

	
