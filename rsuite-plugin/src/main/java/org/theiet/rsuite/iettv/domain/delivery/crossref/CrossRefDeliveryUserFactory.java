package org.theiet.rsuite.iettv.domain.delivery.crossref;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.theiet.rsuite.datatype.deliveryUser.DeliveryUser;
import org.theiet.rsuite.datatype.deliveryUser.DeliveryUserFactory;

import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.extensions.ExecutionContext;

public class CrossRefDeliveryUserFactory {

	private static final String DELIVER_USER_TYPE_CROSS_REF = "crossRef";
	
	   public static DeliveryUser createDeliveryUser(ExecutionContext context, Log logger, String userId)
	            throws RSuiteException {

	        Map<String, String> userProperties =
	                context.getUserService().getUserPropertiesCatalog().getProperties(userId);

	        if (userProperties == null) {
	            throw new RSuiteException("Properties for user hasn't been found " + userId);
	        }
	               

	        String deliveryType = userProperties.get("type");
	        
	        if (DELIVER_USER_TYPE_CROSS_REF.equalsIgnoreCase(deliveryType)) {
	            return new CrossRefDeliveryUser(logger, userId, userProperties);
	        }
	        
	        return DeliveryUserFactory.createDeliveryUser(context, userId);


	    }
	
}
