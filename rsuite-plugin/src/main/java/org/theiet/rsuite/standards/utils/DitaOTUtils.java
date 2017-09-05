package org.theiet.rsuite.standards.utils;

import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.extensions.ExecutionContext;
import com.reallysi.rsuite.api.xml.DitaOpenToolkit;

public class DitaOTUtils {


	public static DitaOpenToolkit getToolkit(ExecutionContext context, String otName)
			throws RSuiteException {
	
		DitaOpenToolkit toolkit = context.getXmlApiManager()
				.getDitaOpenToolkitManager().getToolkit(otName);
		if (toolkit == null) {
			throw new RSuiteException(
	
			"No DITA Open Toolkit named \"" + otName
					+ "\" provided by Open Toolkit Manager. Cannot continue.");
		}
		return toolkit;
	}

}
