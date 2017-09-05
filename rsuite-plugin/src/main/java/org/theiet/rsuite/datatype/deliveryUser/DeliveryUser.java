package org.theiet.rsuite.datatype.deliveryUser;

import java.io.InputStream;

import com.reallysi.rsuite.api.RSuiteException;

public interface DeliveryUser {

    void deliverToMainDestination(InputStream inputStream, String fileName) throws RSuiteException;
    
    void deliverToDestination(InputStream inputStream, String fileName, String path) throws RSuiteException;
    
    String getProperty(String propertyName) throws RSuiteException;
    
    String getContactFirstName();

    String userId();

}
