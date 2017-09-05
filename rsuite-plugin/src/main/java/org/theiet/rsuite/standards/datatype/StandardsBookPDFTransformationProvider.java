package org.theiet.rsuite.standards.datatype;

import java.util.List;

import org.theiet.rsuite.standards.StandardsBooksConstans;

import com.reallysi.rsuite.api.DataTypeOptionValue;
import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.forms.DataTypeProviderOptionValuesContext;
import com.reallysi.rsuite.api.forms.DefaultDataTypeOptionValuesProviderHandler;

public class StandardsBookPDFTransformationProvider extends DefaultDataTypeOptionValuesProviderHandler
		implements StandardsBooksConstans {

	@Override
	public void provideOptionValues(DataTypeProviderOptionValuesContext context, List<DataTypeOptionValue> optionValues)
			throws RSuiteException {
		optionValues.clear();
		optionValues.add(createDataOption(StandardsPdfTranstypeProvider.REG_2_PDF_WREG));
		optionValues.add(createDataOption(StandardsPdfTranstypeProvider.REG_2_PDF_GN));
	}

	private DataTypeOptionValue createDataOption(StandardsPdfTranstypeProvider pdfTranstype) {
		return new DataTypeOptionValue(pdfTranstype.getTranstype(), pdfTranstype.getTranformationContentName());
	}
}
