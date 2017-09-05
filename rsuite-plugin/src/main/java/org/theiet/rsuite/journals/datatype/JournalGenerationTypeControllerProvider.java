package org.theiet.rsuite.journals.datatype;

import static org.theiet.rsuite.journals.datatype.JournalGenerationType.*;

import com.reallysi.rsuite.api.DataTypeOptionValue;
import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.forms.DataTypeProviderOptionValuesContext;
import com.reallysi.rsuite.api.forms.DefaultDataTypeOptionValuesProviderHandler;

/**
 * Abstract class for support user-list dynamic data types.
 */
public class JournalGenerationTypeControllerProvider extends
		DefaultDataTypeOptionValuesProviderHandler {

	@Override
	public void provideOptionValues(
			DataTypeProviderOptionValuesContext context,
			java.util.List<DataTypeOptionValue> optionValues)
			throws RSuiteException {

		optionValues.clear();

		optionValues.add(new DataTypeOptionValue(TYPESETTER.toString(), TYPESETTER
				.getDescription()));
		optionValues.add(new DataTypeOptionValue(AUTOMATED.toString(), AUTOMATED
				.getDescription()));
	}

}
