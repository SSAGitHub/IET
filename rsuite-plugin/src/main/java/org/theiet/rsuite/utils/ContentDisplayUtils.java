package org.theiet.rsuite.utils;

import org.theiet.rsuite.IetConstants;

import com.reallysi.rsuite.api.remoteapi.result.MessageDialogResult;
import com.reallysi.rsuite.api.remoteapi.result.MessageType;
import com.reallysi.rsuite.api.remoteapi.result.UserInterfaceAction;

public class ContentDisplayUtils {

	public static MessageDialogResult getResultWithLabelRefreshing (
			MessageType messageType, String dialogTitle, String message, String dialogWith, String moId) {
		MessageDialogResult result = new MessageDialogResult(messageType, dialogTitle, message, dialogWith);
		UserInterfaceAction uia = new UserInterfaceAction(IetConstants.UI_PARAM_RSUITE_REFRESH_MANAGED_OBJECTS);
		if (moId != null){
			uia.addProperty(IetConstants.UI_PROPERTY_OBJECTS, moId);
		}
		
		uia.addProperty(IetConstants.UI_PROPERTY_CHILDREN, true);		
		result.addAction(uia);			
		return result;		
	}
	
}
