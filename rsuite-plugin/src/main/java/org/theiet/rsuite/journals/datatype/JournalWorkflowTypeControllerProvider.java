package org.theiet.rsuite.journals.datatype;

import static org.theiet.rsuite.journals.datatype.JournalWorkflowType.ARCHIVE;
import static org.theiet.rsuite.journals.datatype.JournalWorkflowType.ISSUE;

import com.reallysi.rsuite.api.DataTypeOptionValue;
import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.forms.DataTypeProviderOptionValuesContext;
import com.reallysi.rsuite.api.forms.DefaultDataTypeOptionValuesProviderHandler;

/**
 * Abstract class for support user-list dynamic data types.
 */
public class JournalWorkflowTypeControllerProvider extends
		DefaultDataTypeOptionValuesProviderHandler {

	@Override
	public void provideOptionValues(
			DataTypeProviderOptionValuesContext context,
			java.util.List<DataTypeOptionValue> optionValues)
			throws RSuiteException {

		optionValues.clear();

		optionValues.add(new DataTypeOptionValue(ISSUE.toString(), ISSUE
				.getDescription()));
		optionValues.add(new DataTypeOptionValue(ARCHIVE.toString(), ARCHIVE
				.getDescription()));
	}

}
