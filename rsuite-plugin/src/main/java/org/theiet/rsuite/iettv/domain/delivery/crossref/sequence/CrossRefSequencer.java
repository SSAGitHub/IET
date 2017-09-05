package org.theiet.rsuite.iettv.domain.delivery.crossref.sequence;

import java.util.HashMap;

import org.theiet.rsuite.utils.XqueryUtils;

import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.extensions.ExecutionContext;
import com.reallysi.rsuite.api.repository.ComposedXQuery;

public class CrossRefSequencer {

	private CrossRefSequencer(){
	}
	
	public static int getSequenceNumber(ExecutionContext context) throws RSuiteException{
		ComposedXQuery xqueryObject = XqueryUtils.getXquery("iet_tv_cross_ref_sequence.xqy", new HashMap<String, String>());
		return Integer.valueOf(context.getRepositoryService().queryAsString(xqueryObject.getText()));
	}
	
	public static String getNextFormattedSequenceNumber(ExecutionContext context) throws RSuiteException {		
		return String.format("%03d", getSequenceNumber(context));
	}
	
}
