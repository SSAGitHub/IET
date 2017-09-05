package org.theiet.rsuite.datatype.deliveryUser;

import static org.theiet.rsuite.datatype.deliveryUser.DeliveryUserUtils.*;

import java.io.InputStream;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.net.io.CopyStreamException;

import com.reallysi.rsuite.api.RSuiteException;
import com.rsicms.rsuite.helpers.utils.net.ftp.*;

class FtpDeliveryUser implements DeliveryUser {

    private static final String USER_PROP_FTP_FOLDER_MAIN = "ftp.folder.main";

    private static final String USER_PROP_FTP_HOST = "ftp.host";

    private static final String USER_PROP_USER = "ftp.user";

    private static final String USER_PROP_PASSWORD = "ftp.password";

    private static final String USER_PROP_FTP_PORT = "ftp.port";

    private FTPConnectionInfo connectionInfo;

    private Map<String, String> userProperties;

    private String mainTargetPath;

    private String userId;

    public FtpDeliveryUser(String userId, Map<String, String> userProperties) {

        createFtpConnectionInfo(userProperties);

        this.userProperties = userProperties;
        this.userId = userId;

        mainTargetPath = getPropertyValue(userProperties, USER_PROP_FTP_FOLDER_MAIN);
    }

    private void createFtpConnectionInfo(Map<String, String> userProperties) {
        String ftpHost = userProperties.get(USER_PROP_FTP_HOST);
        String ftpUser = userProperties.get(USER_PROP_USER);
        String ftpPassword = userProperties.get(USER_PROP_PASSWORD);

        String ftpPort = userProperties.get(USER_PROP_FTP_PORT);

        if (StringUtils.isNumeric(ftpPort)) {
            connectionInfo =
                    new FTPConnectionInfo(ftpHost, Integer.parseInt(ftpPort), ftpUser, ftpPassword);
        }else{
            connectionInfo = new FTPConnectionInfo(ftpHost, ftpUser, ftpPassword);   
        }        
    }

    @Override
    public void deliverToMainDestination(InputStream inputStream, String fileName)
            throws RSuiteException {

        String targetPath = mainTargetPath;
        uploadToFtp(inputStream, fileName, targetPath);

    }

    private void uploadToFtp(InputStream inputStream, String fileName, String targetPath)
            throws RSuiteException {
        try {
            FTPUtils.uploadStream(connectionInfo, inputStream, fileName, targetPath);
        } catch (FTPUtilsException e) {
        	Exception causeException = getCauseException(e);
            throw new RSuiteException(0, "Unable to deliver " + fileName + " delivery user " + userId, causeException);
        }
    }

	private Exception getCauseException(Exception e) {
		
		if (e.getCause() instanceof CopyStreamException){
			CopyStreamException streamException = (CopyStreamException)e.getCause();
			return streamException.getIOException();
		}
		return e;
	}



    @Override
    public String getProperty(String propertyName) throws RSuiteException {
        return userProperties.get(propertyName);
    }

    @Override
    public String getContactFirstName() {
        return getContactFirstNameValue(userProperties);
    }

    @Override
    public void deliverToDestination(InputStream inputStream, String fileName, String path)
            throws RSuiteException {
        uploadToFtp(inputStream, fileName, path);

    }

    @Override
    public String userId() {
        return userId;
    }

}
