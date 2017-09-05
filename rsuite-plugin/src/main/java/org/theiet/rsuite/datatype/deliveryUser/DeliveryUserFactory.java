package org.theiet.rsuite.datatype.deliveryUser;

import java.util.Map;

import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.extensions.ExecutionContext;

public class DeliveryUserFactory {

    private static final String DELIVER_USER_TYPE_LOCAL = "local";
    private static final String DELIVER_USER_TYPE_FTP = "ftp";
    

    public static DeliveryUser createDeliveryUser(ExecutionContext context, String userId)
            throws RSuiteException {

        Map<String, String> userProperties =
                context.getUserService().getUserPropertiesCatalog().getProperties(userId);

        if (userProperties == null) {
            throw new RSuiteException("Properties for user hasn't been found " + userId);
        }
               

        String deliveryType = userProperties.get("type");

        if (DELIVER_USER_TYPE_FTP.equalsIgnoreCase(deliveryType)) {
            return new FtpDeliveryUser(userId, userProperties);
        } else if (DELIVER_USER_TYPE_LOCAL.equalsIgnoreCase(deliveryType)) {
            return new LocalDeliveryUser(userId, userProperties);
        }

        throw new RSuiteException("Unsupported delivery type user '" + deliveryType + "'");



    }
}
