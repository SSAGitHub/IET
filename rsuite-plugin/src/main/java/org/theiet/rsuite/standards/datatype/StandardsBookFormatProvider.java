package org.theiet.rsuite.standards.datatype;

import com.reallysi.rsuite.api.DataTypeOptionValue;
import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.forms.DataTypeProviderOptionValuesContext;
import com.reallysi.rsuite.api.forms.DefaultDataTypeOptionValuesProviderHandler;


/**
 * Abstract class for support user-list dynamic data types.
 */
public class StandardsBookFormatProvider extends
        DefaultDataTypeOptionValuesProviderHandler {

    private static final String CD = "CD";
	private static final String WIRO = "Wiro";
	private static final String LOOSE_LEAF = "Loose Leaf";
	private static final String PAPER_BACK = "Paper Back";
	private static final String HARD_BACK = "Hard Back";

	@Override
    public void provideOptionValues(
            DataTypeProviderOptionValuesContext context,
            java.util.List<DataTypeOptionValue> optionValues
     ) throws RSuiteException {
      
		optionValues.clear();

    	
		optionValues.add(new DataTypeOptionValue(HARD_BACK, HARD_BACK));
		optionValues.add(new DataTypeOptionValue(PAPER_BACK, PAPER_BACK));
		optionValues.add(new DataTypeOptionValue(LOOSE_LEAF, LOOSE_LEAF));
		optionValues.add(new DataTypeOptionValue(WIRO, WIRO));
		optionValues.add(new DataTypeOptionValue(CD, CD));
      
    }
    
   
}
