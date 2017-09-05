package org.theiet.rsuite.utils;

import org.apache.commons.logging.Log;

import com.reallysi.rsuite.api.remoteapi.RemoteApiResult;
import com.reallysi.rsuite.api.remoteapi.result.MessageDialogResult;
import com.reallysi.rsuite.api.remoteapi.result.MessageType;

public class WebServiceUtils {

	public static MessageDialogResult createResultDialog(String label, String message) {
		return new MessageDialogResult(
				MessageType.SUCCESS,
				label,
				"<html><body><div><p>" + message +"</p></div></body></html>",
				"500");
	}

	public static RemoteApiResult handleException(Exception e, Log log, String label) {
		log.error("Unable to send email for review", e);
		return new MessageDialogResult(MessageType.ERROR,
				label, "Error: " + e.getMessage());
	}
	
}
