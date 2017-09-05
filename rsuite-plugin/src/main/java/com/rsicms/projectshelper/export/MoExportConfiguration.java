package com.rsicms.projectshelper.export;


public interface MoExportConfiguration {

	String getRefenceTransformationDTD();

	String getRefenceTransformationXSD();
	
	MoExportHandler createMoExportHandler();
	
	String getExportNonXmlVariant();
	
	void setExportNonXmlVariant(String variantName);
}
