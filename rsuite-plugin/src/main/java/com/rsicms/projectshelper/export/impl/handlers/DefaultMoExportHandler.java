package com.rsicms.projectshelper.export.impl.handlers;

import java.io.*;
import java.util.List;

import net.sf.saxon.om.NodeInfo;

import com.reallysi.rsuite.api.ManagedObject;
import com.reallysi.rsuite.api.MetaDataItem;
import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.User;
import com.reallysi.rsuite.api.extensions.ExecutionContext;
import com.rsicms.projectshelper.export.*;
import com.rsicms.projectshelper.export.impl.RSuiteFileNameAliasLinkValueToMoMapper;
import com.rsicms.projectshelper.export.impl.validation.DefaultMoExportCrossReferenceValidator;

public class DefaultMoExportHandler implements MoExportHandler {

	private ExecutionContext context;

	private User user;

	@Override
	public void initialize(MoExportHandlerContext moExportHandlerContext) throws RSuiteException{
		this.context = moExportHandlerContext.getContext();
		this.user = moExportHandlerContext.getUser();
	}

	@Override
	public String getMoExportPath(ManagedObject mo, String exportUri)
			throws RSuiteException {
		return exportUri;
	}

	@Override
	public InputStream getMangedObjectContent(ManagedObject mo, InputStream is)
			throws RSuiteException {
		return is;
	}

	@Override
	public boolean exportMo(ManagedObject mo) {
		return true;
	}

	@Override
	public boolean processRefenceToMo(String contextMoId,
			NodeInfo referenceAttibute, ManagedObject referencedMo) {
		return true;
	}

	@Override
	public LinkValueToMoMapper createLinkValueToMoMapper() {
		return new RSuiteFileNameAliasLinkValueToMoMapper(user,
				context.getManagedObjectService());
	}

	@Override
	public String getDefaultValueForMissingLink(String linkValue) {
		return linkValue;
	}

	@Override
	public MoExportCrossReferenceValidator createMoExportCrossRefernceValidator() {
		return new DefaultMoExportCrossReferenceValidator();
	}

	@Override
	public String getLinkValue(ManagedObject referencedMo, String linkValue)
			throws RSuiteException {
		return linkValue;
	}

	@Override
	public List<MoExportAdditionalTransformation> getTransformationToPerformBeforePersistExportedMo(
			ManagedObject mo) throws RSuiteException {
		return null;
	}

	@Override
	public void afterExport(File outputFolder) throws RSuiteException{
	}

	@Override
	public boolean embedLmd() {
		return false;
	}

	@Override
	public List<MetaDataItem> getAdditionalMetaData(ManagedObject moToExport) throws RSuiteException {
		return null;
	}
}
