package org.theiet.rsuite.datatype.deliveryUser;

import static org.theiet.rsuite.datatype.deliveryUser.DeliveryUserUtils.getContactFirstNameValue;

import java.io.InputStream;
import java.util.Map;

import com.reallysi.rsuite.api.RSuiteException;

public abstract class BaseDeliveryUser implements DeliveryUser {

	private Map<String, String> userProperties;

	private String userId;

	public BaseDeliveryUser(String userId, Map<String, String> userProperties) {
		super();
		this.userProperties = userProperties;
		this.userId = userId;
	}

	@Override
	public void deliverToMainDestination(InputStream inputStream, String fileName) throws RSuiteException {
	}

	@Override
	public void deliverToDestination(InputStream inputStream, String fileName, String path) throws RSuiteException {
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

}
