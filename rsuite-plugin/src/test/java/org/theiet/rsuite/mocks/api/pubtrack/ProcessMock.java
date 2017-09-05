package org.theiet.rsuite.mocks.api.pubtrack;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.reallysi.rsuite.api.pubtrack.Process;
import com.reallysi.rsuite.api.pubtrack.ProcessMetaDataItem;

public class ProcessMock extends Process {

	public ProcessMock(Map<String, String> metadata) {
		
		Set<ProcessMetaDataItem> metadataSet = new HashSet<ProcessMetaDataItem>();
		
		
		for (String key : metadata.keySet()){
			ProcessMetaDataItem item = new ProcessMetaDataItem();
			item.setName(key);
			item.setValue(metadata.get(key));
			metadataSet.add(item);
		}
		
		setMetaData(metadataSet);
	}
	
	@Override
	public Long getId() {		
		return new Long(-1);
	}
}
