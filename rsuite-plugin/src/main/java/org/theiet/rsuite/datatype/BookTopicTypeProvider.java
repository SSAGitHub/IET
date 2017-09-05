package org.theiet.rsuite.datatype;

import java.util.List;

import com.reallysi.rsuite.api.DataTypeOptionValue;
import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.forms.DataTypeProviderOptionValuesContext;
import com.reallysi.rsuite.api.forms.DefaultDataTypeOptionValuesProviderHandler;

public class BookTopicTypeProvider extends DefaultDataTypeOptionValuesProviderHandler {

	private static final String CIRCUITS_DEVICES_MATERIALS = "Circuits, Devices, Materials (CS, ED, EM, EP)";
	private static final String COMPUTING = "Computing (CM, PC)";
	private static final String CONTROL_ROBOTICS = "Control &amp; Robotics (CE)";
	private static final String ELECTROMAGNETICS_RADAR = "Electromagnetics &amp; Radar (EL, EW, RA)";
	private static final String MANAGEMENT = "Management (MT)";
	private static final String MANUFACTURING = "Manufacturing (ME)";
	private static final String POWER_ENERGY = "Power &amp; Energy (EN, PO, RN)";
	private static final String TELECOMMUNICATIONS = "Telecommunications (BT, TE)";
	private static final String HISTORY_OF_TECHNOLOGY ="History of Technology (HT)";
	
	@Override
	public void provideOptionValues(
			DataTypeProviderOptionValuesContext context,
			List<DataTypeOptionValue> optionValues) throws RSuiteException {
		optionValues.clear();
		optionValues.add(new DataTypeOptionValue(CIRCUITS_DEVICES_MATERIALS, CIRCUITS_DEVICES_MATERIALS));
		optionValues.add(new DataTypeOptionValue(COMPUTING, COMPUTING));
		optionValues.add(new DataTypeOptionValue(CONTROL_ROBOTICS, CONTROL_ROBOTICS));
		optionValues.add(new DataTypeOptionValue(ELECTROMAGNETICS_RADAR, ELECTROMAGNETICS_RADAR));
		optionValues.add(new DataTypeOptionValue(MANAGEMENT, MANAGEMENT));
		optionValues.add(new DataTypeOptionValue(MANUFACTURING, MANUFACTURING));
		optionValues.add(new DataTypeOptionValue(POWER_ENERGY, POWER_ENERGY));
		optionValues.add(new DataTypeOptionValue(TELECOMMUNICATIONS, TELECOMMUNICATIONS));
		optionValues.add(new DataTypeOptionValue(HISTORY_OF_TECHNOLOGY, HISTORY_OF_TECHNOLOGY));
	}
	
}
