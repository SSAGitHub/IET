package org.theiet.rsuite.datatype.deliveryUser;

import static org.theiet.rsuite.datatype.deliveryUser.DeliveryUserUtils.*;

import java.io.*;
import java.util.Map;

import org.apache.commons.io.*;
import org.apache.commons.lang.StringUtils;

import com.reallysi.rsuite.api.RSuiteException;

class LocalDeliveryUser implements DeliveryUser {

    private static final String USER_PROP_DELIVERY_PATH = "deliveryPath";

    private Map<String, String> userProperties;

    private String mainTargetPath;

    private String userId;

    public LocalDeliveryUser(String userId, Map<String, String> userProperties) throws RSuiteException {

        this.userProperties = userProperties;
        this.userId = userId;

        mainTargetPath = getPropertyValue(userProperties, USER_PROP_DELIVERY_PATH);
        
        if (StringUtils.isBlank(mainTargetPath)){
        	throw new RSuiteException(String.format("Property %s is not defined for user %s", USER_PROP_DELIVERY_PATH, userId));
        }
        
    }

    @Override
    public void deliverToMainDestination(InputStream inputStream, String fileName)
            throws RSuiteException {
        storeIntoFile(inputStream, fileName, mainTargetPath);

    }

    private void storeIntoFile(InputStream inputStream, String fileName, String targetPath)
            throws RSuiteException {
        File targetFile = new File(targetPath, fileName);
        targetFile.getParentFile().mkdirs();
        
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(targetFile);
            IOUtils.copy(inputStream, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (IOException e) {
            throw new RSuiteException(0, "Unable to deliver " + fileName + " delivery user "
                    + userId, e);
        }
    }

    @Override
    public void deliverToDestination(InputStream inputStream, String fileName, String path)
            throws RSuiteException {
        storeIntoFile(inputStream, fileName, mainTargetPath);

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
    public String userId() {
        return userId;
    }

	@Override
	public String getLocationInfo() {
		return "Local drive " + mainTargetPath;
	}

}
