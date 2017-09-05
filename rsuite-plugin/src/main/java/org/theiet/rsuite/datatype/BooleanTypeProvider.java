package org.theiet.rsuite.datatype;

import org.theiet.rsuite.journals.JournalConstants;

import com.reallysi.rsuite.api.DataTypeOptionValue;
import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.forms.DataTypeProviderOptionValuesContext;
import com.reallysi.rsuite.api.forms.DefaultDataTypeOptionValuesProviderHandler;


/**
 * Abstract class for support user-list dynamic data types.
 */
public class BooleanTypeProvider extends
        DefaultDataTypeOptionValuesProviderHandler implements JournalConstants {

    @Override
    public void provideOptionValues(
            DataTypeProviderOptionValuesContext context,
            java.util.List<DataTypeOptionValue> optionValues
     ) throws RSuiteException {
      
    optionValues.clear();
    	
      optionValues.add(new DataTypeOptionValue(LMD_VALUE_YES, "Yes"));
      optionValues.add(new DataTypeOptionValue(LMD_VALUE_NO, "No"));
      

      
    }
    
   
}
