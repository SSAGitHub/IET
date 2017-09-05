package org.theiet.rsuite.onix.domain;

import java.io.FileInputStream;

import org.theiet.rsuite.FTPConnectionInfoFactory;
import org.theiet.rsuite.onix.datatype.OnixRequestDTO;

import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.extensions.ExecutionContext;
import com.rsicms.rsuite.helpers.utils.net.ftp.FTPConnectionInfo;
import com.rsicms.rsuite.helpers.utils.net.ftp.FTPUtils;

public final class OnixFtpSender {

	private OnixFtpSender() {
	};

	public static void sendOnixRequestPackage(ExecutionContext context, OnixRequestDTO onixRequestDTO)
			throws RSuiteException {

		try {
			FTPConnectionInfo connectionInfo = FTPConnectionInfoFactory.getFTPConnection(context,
					onixRequestDTO.getOnixExternalUser());
			String ftpFolder = FTPConnectionInfoFactory.getTargetFolder(context, onixRequestDTO.getOnixExternalUser(),
					onixRequestDTO.getTargetFolderProperty());
			FileInputStream resultFileIS = new FileInputStream(onixRequestDTO.getFile());

			FTPUtils.uploadStream(connectionInfo, resultFileIS, onixRequestDTO.getTargetFileName(), ftpFolder);
		} catch (Exception ex) {
			throw new RSuiteException(RSuiteException.ERROR_NOT_DEFINED, ex.getMessage(), ex);
		}
	}

}
