package org.theiet.rsuite.iettv.domain.delivery.crossref;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.logging.Log;
import org.theiet.rsuite.datatype.deliveryUser.DeliveryUser;

import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.extensions.ExecutionContext;

public class CrossRefDocumentSender {

	private static final String USER_ID_IET_TV_CROSS_REF_USER_ID = "IetTvCrossRefUser";

	private CrossRefDocumentSender() {
	}

	public static void sendCrossRef(ExecutionContext context, File crossRefFile)
			throws RSuiteException {
		sendCrossRef(context, null, crossRefFile);	
	}
	
	public static void sendCrossRef(ExecutionContext context, Log logger, File crossRefFile)
			throws RSuiteException {

		DeliveryUser deliveryUser = CrossRefDeliveryUserFactory
				.createDeliveryUser(context, logger, USER_ID_IET_TV_CROSS_REF_USER_ID);
		try (InputStream crossRefStream = new FileInputStream(crossRefFile)) {
			deliveryUser.deliverToMainDestination(crossRefStream,
					crossRefFile.getName());
		} catch (FileNotFoundException e) {
			throw new RSuiteException(0, e.getMessage()
					+ crossRefFile.getName(), e);
		} catch (IOException e) {
			throw new RSuiteException(0, e.getMessage()
					+ crossRefFile.getName(), e);
		}

	}
}
