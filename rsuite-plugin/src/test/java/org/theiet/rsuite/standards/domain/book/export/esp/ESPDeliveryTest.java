package org.theiet.rsuite.standards.domain.book.export.esp;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;
import org.junit.Test;

import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.system.UserPropertiesCatalog;

public class ESPDeliveryTest {

	@Test
	public void test_getDeliveryPath_missingProperty_exception() {

		String espUserId = "Test";
		UserPropertiesCatalog userPropertiesCatalog = mock(UserPropertiesCatalog.class);
		when(userPropertiesCatalog.getProperties(espUserId)).thenReturn(
				new HashMap<String, String>());

		try {
			ESPDelivery.getDeliveryPath(userPropertiesCatalog, null, espUserId);
			fail("Exception should be thrown");
		} catch (RSuiteException e) {

		}

	}

	@Test
	public void test_getDeliveryPath_propertyWithTempFolder_expandedTempFolderProperty()
			throws RSuiteException, URISyntaxException {

		String espUserId = "Test";
		UserPropertiesCatalog userPropertiesCatalog = mock(UserPropertiesCatalog.class);

		Map<String, String> userProperties = new HashMap<String, String>();
		userProperties.put("deliveryPath", "$rsuiteTempFolder/espDelivery");

		when(userPropertiesCatalog.getProperties(espUserId)).thenReturn(
				userProperties);

		String deliveryPath = ESPDelivery.getDeliveryPath(
				userPropertiesCatalog, new File("/tmp"), espUserId);
		deliveryPath = FilenameUtils.separatorsToUnix(deliveryPath);

		assertEquals("/tmp/espDelivery", deliveryPath);

	}

}
