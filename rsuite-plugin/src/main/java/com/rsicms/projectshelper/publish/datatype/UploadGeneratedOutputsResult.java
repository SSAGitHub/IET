package com.rsicms.projectshelper.publish.datatype;

import java.util.List;
import java.util.Set;

public interface UploadGeneratedOutputsResult {

	public String getOutputMoIdForMo(String xmlMoId);
	
	public Set<String> getXmlMoIds();
	
	public List<String> getAdditionalUploadedMoIds();

	String getSerializedAdditionalMoIds();
}
