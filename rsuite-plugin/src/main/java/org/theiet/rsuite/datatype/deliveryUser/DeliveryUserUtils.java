package org.theiet.rsuite.datatype.deliveryUser;

import java.util.Map;


class DeliveryUserUtils {

    public static String getContactFirstNameValue(Map<String, String> userProperties){
        return getPropertyValue(userProperties, "contact.first.name");
    }
    
    static String getPropertyValue(Map<String, String> userProperties, String propertyName) {
        String propertyValue = null;
        
        
        if (userProperties != null){
            propertyValue = userProperties.get(propertyName);
        }
        
        return propertyValue == null ? "" : propertyValue;
    }
}
