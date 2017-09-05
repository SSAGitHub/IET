package org.theiet.rsuite.standards.datatype;

import java.util.List;

import org.theiet.rsuite.standards.StandardsBooksConstans;

import com.reallysi.rsuite.api.DataTypeOptionValue;
import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.forms.DataTypeProviderOptionValuesContext;
import com.reallysi.rsuite.api.forms.DefaultDataTypeOptionValuesProviderHandler;

public class StandardsBookICMLTransformationProvider extends DefaultDataTypeOptionValuesProviderHandler implements StandardsBooksConstans  {

	@Override
	public void provideOptionValues(
			DataTypeProviderOptionValuesContext context,
			List<DataTypeOptionValue> optionValues) throws RSuiteException {
		optionValues.clear();
		optionValues.add(new DataTypeOptionValue(LMD_VALUE_ICML_TRANSFORMATION_WRITING_REGULATION, OUTPUT_TRANSFORMATION_WIRING_REGULATIONS));
		optionValues.add(new DataTypeOptionValue(LMD_VALUE_ICML_TRANSFORMATION_GUIDANCE_NOTES, OUTPUT_TRANSFORMATION_GUIDANCE_NOTES));
		optionValues.add(new DataTypeOptionValue(LMD_VALUE_ICML_TRANSFORMATION_ONSITE_GUIDE, OUTPUT_TRANSFORMATION_ONSITE_GUIDE));
	}

}
