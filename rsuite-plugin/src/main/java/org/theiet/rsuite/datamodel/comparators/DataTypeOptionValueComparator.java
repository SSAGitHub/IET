package org.theiet.rsuite.datamodel.comparators;

import java.util.Comparator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.theiet.rsuite.books.BooksConstans;
import org.theiet.rsuite.journals.JournalConstants;

import com.reallysi.rsuite.api.ContentAssembly;
import com.reallysi.rsuite.api.DataTypeOptionValue;
import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.User;
import com.reallysi.rsuite.api.extensions.ExecutionContext;
import com.reallysi.rsuite.service.ContentAssemblyService;

public class DataTypeOptionValueComparator implements Comparator<DataTypeOptionValue>, BooksConstans, JournalConstants {

	private static Log log = LogFactory.getLog(DataTypeOptionValueComparator.class);
	private ExecutionContext context;
	private CompareDataTypeBy compareDataTypeBy;
	
	public enum CompareDataTypeBy {
		DATA_TYPE_OPTION_VALUE_LABEL,
		DATA_TYPE_OPTION_VALUE_EXPORT_DATE_METADATA 
	}
	
	public DataTypeOptionValueComparator() {
		compareDataTypeBy = CompareDataTypeBy.DATA_TYPE_OPTION_VALUE_LABEL; 
	}
	
	public DataTypeOptionValueComparator(ExecutionContext context, CompareDataTypeBy compareDataTypeBy) {
		this.context = context;
		this.compareDataTypeBy = compareDataTypeBy; 
	}
	
	@Override
	public int compare(DataTypeOptionValue o1, DataTypeOptionValue o2) {
		String comparisonVar1 = "";
		String comparisonVar2 = "";
		switch (compareDataTypeBy) {
			case DATA_TYPE_OPTION_VALUE_LABEL:
				comparisonVar1 = o1.getLabel();
				comparisonVar2 = o2.getLabel();
				break;	
			case DATA_TYPE_OPTION_VALUE_EXPORT_DATE_METADATA:
				comparisonVar1 = getComparisonMetadataValue(o1.getValue().replaceAll("[\\D]", ""), compareDataTypeBy);
				comparisonVar2 = getComparisonMetadataValue(o2.getValue().replaceAll("[\\D]", ""), compareDataTypeBy);
				break;
		}
		
		if (comparisonVar1 == null && comparisonVar2 == null){
			return 0;
		}
		
		if (comparisonVar1 != null && comparisonVar2 == null){
			return 1;
		}
		
		if (comparisonVar1 == null && comparisonVar2 != null){
			return -1;
		}
		
		return comparisonVar1.compareTo(comparisonVar2);
	}
	
	private String getComparisonMetadataValue (String containerId, CompareDataTypeBy compareDataTypeBy) {
		try {
			ContentAssemblyService caServ = context.getContentAssemblyService();
			User user = context.getAuthorizationService().getSystemUser();		
			ContentAssembly ca = caServ.getContentAssembly(user, containerId);
			String metadataName = null;
			if (compareDataTypeBy == CompareDataTypeBy.DATA_TYPE_OPTION_VALUE_EXPORT_DATE_METADATA) {
				metadataName = LMD_FIELD_EXPORT_DATE;
			}
			return ca.getLayeredMetadataValue(metadataName);
		} catch (RSuiteException ex) {
			log.info("Error getting metadata for DataTypeOptionValue comparison", ex);
		}	
		
		return null;
	}

}
