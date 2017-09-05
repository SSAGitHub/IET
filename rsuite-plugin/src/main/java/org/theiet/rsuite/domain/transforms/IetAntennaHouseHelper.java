package org.theiet.rsuite.domain.transforms;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;

import com.reallysi.rsuite.api.RSuiteException;

public class IetAntennaHouseHelper {

	private static final String ANTENNA_HOUSE_CONF_XML = "/WebContent/conf/antennaHouse/ah-conf.xml";

	public static String getAntenaHouseConfiguration() throws RSuiteException {
		String configuration = null;

		InputStream is = IetAntennaHouseHelper.class
				.getResourceAsStream(ANTENNA_HOUSE_CONF_XML);

		if (is != null) {
			try {
				configuration = IOUtils.toString(is);
			} catch (IOException e) {
				throw new RSuiteException(0,
						"Unable to read the configuration file "
								+ ANTENNA_HOUSE_CONF_XML, e);
			}
		}

		return configuration;
	}
}
