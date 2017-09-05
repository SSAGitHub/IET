package org.theiet.rsuite.onix.datatype;

import org.theiet.rsuite.onix.OnixConstants;

import com.reallysi.rsuite.api.DataTypeOptionValue;
import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.forms.DataTypeProviderOptionValuesContext;
import com.reallysi.rsuite.api.forms.DefaultDataTypeOptionValuesProviderHandler;

public class OnixProductFormatProvider extends
		DefaultDataTypeOptionValuesProviderHandler implements OnixConstants {


	@Override
	public void provideOptionValues(
			DataTypeProviderOptionValuesContext context,
			java.util.List<DataTypeOptionValue> optionValues)
			throws RSuiteException {

		optionValues.clear();

		optionValues.add(new DataTypeOptionValue(LMD_VALUE_PRINT, LMD_VALUE_PRINT));
		optionValues.add(new DataTypeOptionValue(LMD_VALUE_DIGITAL, LMD_VALUE_DIGITAL));
	}

}