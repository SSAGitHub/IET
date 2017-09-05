package org.theiet.rsuite.standards.domain.book.export.esp;

import static org.theiet.rsuite.standards.StandardsBooksConstans.USER_LOCAL_ESP_BOOK;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.extensions.ExecutionContext;
import com.reallysi.rsuite.api.system.UserPropertiesCatalog;

public class ESPDelivery {

	
	public static void deliver(ExecutionContext context, File bookEditionArchive) throws RSuiteException, IOException {

		
		String userID = USER_LOCAL_ESP_BOOK;
		
		UserPropertiesCatalog userPropertiesCatalog = context.getUserService()
				.getUserPropertiesCatalog();
		
		String deliveryPath = getDeliveryPath(userPropertiesCatalog, context.getRSuiteServerConfiguration().getTmpDir(), userID);
		File deliveryFolder = new File(deliveryPath);
		deliveryFolder.mkdirs();
		
		FileUtils.copyFileToDirectory(bookEditionArchive, deliveryFolder);
		
	}

	protected static String getDeliveryPath(UserPropertiesCatalog userPropertiesCatalog, File rsuiteTempDir, String espUserId) throws RSuiteException {
		
		Map<String, String> userProperties = userPropertiesCatalog.getProperties(espUserId);

		String deliveryPathProperty = "deliveryPath";
		String deliveryPath = userProperties.get(deliveryPathProperty);

		if (StringUtils.isBlank(deliveryPath)) {
			throw new RSuiteException("Missing property "
					+ deliveryPathProperty + " for user " + espUserId);
		}

		if (deliveryPath.contains("$rsuiteTempFolder")) {
			String rsuiteTempPath = rsuiteTempDir.getAbsolutePath();
			deliveryPath = deliveryPath.replace("$rsuiteTempFolder",
					rsuiteTempPath);
		}
		
		return deliveryPath;
	}
}
