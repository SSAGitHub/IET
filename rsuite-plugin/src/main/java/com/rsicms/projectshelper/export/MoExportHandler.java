package com.rsicms.projectshelper.export;


import java.io.*;
import java.util.List;

import net.sf.saxon.om.NodeInfo;

import com.reallysi.rsuite.api.*;

public interface MoExportHandler {

	void initialize(MoExportHandlerContext exportHandlerContext) throws RSuiteException;
	
	String getMoExportPath(ManagedObject referencedMo, String exportUri) throws RSuiteException;
	
	String getLinkValue(ManagedObject referencedMo, String linkValue) throws RSuiteException;
	
	InputStream getMangedObjectContent(ManagedObject mo, InputStream is) throws RSuiteException;
	
	List<MoExportAdditionalTransformation> getTransformationToPerformBeforePersistExportedMo(ManagedObject mo) throws RSuiteException;
	
	boolean exportMo(ManagedObject mo);
	
	boolean processRefenceToMo(String contextMoId, NodeInfo referenceAttibute,  ManagedObject referencedMo);
	
	LinkValueToMoMapper createLinkValueToMoMapper();
	
	String getDefaultValueForMissingLink(String linkValue);
	
	MoExportCrossReferenceValidator createMoExportCrossRefernceValidator();
	
	void afterExport(File exportFolder) throws RSuiteException;

	boolean embedLmd();

	List<MetaDataItem> getAdditionalMetaData(ManagedObject moToExport) throws RSuiteException;
}
