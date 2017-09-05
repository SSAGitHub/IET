package org.theiet.rsuite.journals.datatype;

import org.theiet.rsuite.datatype.ARoleControllerProvider;

/**
 * Abstract class for support user-list dynamic data types.
 */
public class JournalIssuePrinterProvider extends
		ARoleControllerProvider {

	@Override
	protected String getRoleName() {
		return "JournalIssuePrinter";
	}

}
