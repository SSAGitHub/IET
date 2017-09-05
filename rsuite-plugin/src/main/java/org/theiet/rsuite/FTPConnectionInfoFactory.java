package org.theiet.rsuite;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.theiet.rsuite.journals.JournalConstants;

import com.reallysi.rsuite.api.*;
import com.reallysi.rsuite.api.extensions.ExecutionContext;
import com.rsicms.rsuite.helpers.utils.net.ftp.FTPConnectionInfo;

public class FTPConnectionInfoFactory implements JournalConstants {

	public static FTPConnectionInfo getInspecFTP(ExecutionContext context) {

		return createConnectionInfoObject(context, CFG_FTP_INSPEC_HOST,
				CFG_FTP_INSPEC_USER, CFG_FTP_INSPEC_PASSWORD);
	}

	private static void validateProperty(String propertyName,
			String propertyValue) {
		if (StringUtils.isBlank(propertyValue)) {
			throw new RuntimeException("Property " + propertyName
					+ " is not set");
		}
	}
	
	public static FTPConnectionInfo getFTPConnection(ExecutionContext context, String userId) throws RSuiteException {

		
		Map<String, String> userProperties = context.getUserService().getUserPropertiesCatalog().getProperties(userId);
		
		if (userProperties == null){
			throw new RSuiteException("Properties for user hasn't been found " + userId);
		}

		String ftpHost = userProperties.get(USER_PROP_FTP_HOST);
		String ftpUser = userProperties.get(USER_PROP_USER);
		String ftpPassword = userProperties.get(USER_PROP_PASSWORD);
		
		String ftpPort = userProperties.get(USER_PROP_FTP_PORT);
		
		if (StringUtils.isNumeric(ftpPort)){
			return new FTPConnectionInfo(ftpHost, Integer.parseInt(ftpPort), ftpUser, ftpPassword);
		}
		
		return new FTPConnectionInfo(ftpHost, ftpUser, ftpPassword);
	}

	private static FTPConnectionInfo createConnectionInfoObject(
			ExecutionContext context, String host, String user, String password) {
		ConfigurationProperties props = context.getConfigurationProperties();

		String ftpHost = props.getProperty(host, null);
		validateProperty(host, ftpHost);

		String ftpUser = props.getProperty(user, null);
		validateProperty(user, ftpUser);

		String ftpPassword = props.getProperty(password, null);
		validateProperty(password, ftpPassword);

		return new FTPConnectionInfo(ftpHost, ftpUser, ftpPassword);
	}
	
	public static String getTargetFolder(ExecutionContext context, String userId, String property) throws RSuiteException{
		String targetFolder = null;
		
		Map<String, String> userProperties = context.getUserService().getUserPropertiesCatalog().getProperties(userId);
		if (userProperties != null){
			targetFolder = userProperties.get(property);
		}
		
		return targetFolder == null ? "" : targetFolder;
	}
}
