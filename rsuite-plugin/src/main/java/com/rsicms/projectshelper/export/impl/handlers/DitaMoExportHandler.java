package com.rsicms.projectshelper.export.impl.handlers;

import static com.rsicms.projectshelper.utils.ProjectMoUtils.isXMLMo;

import java.io.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sf.saxon.om.NodeInfo;

import org.apache.commons.io.FilenameUtils;

import com.reallysi.rsuite.api.ManagedObject;
import com.reallysi.rsuite.api.MetaDataItem;
import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.User;
import com.reallysi.rsuite.api.extensions.ExecutionContext;
import com.reallysi.tools.dita.DitaUtil;
import com.rsicms.projectshelper.export.*;
import com.rsicms.projectshelper.export.impl.RSuiteFileNameAliasLinkValueToMoMapper;
import com.rsicms.projectshelper.export.impl.validation.DitaMoExportCrossReferenceValidator;
import com.rsicms.projectshelper.message.ProcessingMessageHandler;

public class DitaMoExportHandler implements MoExportHandler {

	private ExecutionContext context;

	private User user;

	private int exportCount = 0;

	private Set<String> directReferencesFromTopExportedMo = new HashSet<String>();

	private Set<String> conrefs = new HashSet<String>();

	private Set<String> topExportedMoId = new HashSet<String>();

	private ProcessingMessageHandler messageHandler;

	@Override
	public void initialize(MoExportHandlerContext moExportHandlerContext)
			throws RSuiteException {
		exportCount = 0;
		this.context = moExportHandlerContext.getContext();
		this.user = moExportHandlerContext.getUser();
		this.messageHandler = moExportHandlerContext.getMessageHandler();
		setTopExportedMoId(moExportHandlerContext.getTopExportedMos());

	}

	/**
	 * @param topExportedMo
	 * @throws RSuiteException
	 */
	private void setTopExportedMoId(List<ManagedObject> topExportedMos)
			throws RSuiteException {
	    
	    for (ManagedObject topExportedMo : topExportedMos){
	        topExportedMoId.add(topExportedMo.getId());

	        if (topExportedMo.getTargetId() != null) {
	            topExportedMoId.add(topExportedMo.getTargetId());
	        }
	    }
	}

	@Override
	public String getMoExportPath(ManagedObject mo, String exportUri)
			throws RSuiteException {

		String fileName = FilenameUtils.getName(exportUri);
		
		if (isMainExportMo()) {
			exportCount++;
			return fileName;
		}
		
		if (mo.isNonXml()) {
			exportUri = "images/" + fileName;
		}

		if (isXMLMo(mo) && DitaUtil.isDitaTopic(mo.getElement())) {
			exportUri = "topics/" + fileName;
		}

		return exportUri;
	}

	private boolean isMainExportMo() {
		return exportCount == 0;
	}

	@Override
	public InputStream getMangedObjectContent(ManagedObject mo, InputStream is)
			throws RSuiteException {
		return is;
	}


	@Override
	public boolean exportMo(ManagedObject mo) {

		try {

			if (mo.isNonXml() || mo.isAssemblyNode()) {
				return true;
			}

			if (topExportedMoId.contains(mo.getId())
					|| directReferencesFromTopExportedMo.contains(mo.getId())
					|| conrefs.contains(mo.getId())) {
				return true;
			}

		} catch (RSuiteException e) {
			messageHandler.error(e.getLocalizedMessage(), e);
		}

		return false;
	}

	@Override
	public boolean processRefenceToMo(String contextMoId,
			NodeInfo referenceAttibute, ManagedObject referencedMo) {

		String attibuteName = referenceAttibute.getLocalPart();
		if (topExportedMoId.contains(contextMoId)) {
			directReferencesFromTopExportedMo.add(referencedMo.getId());
		}

		if ("conref".equals(attibuteName)) {
			conrefs.add(referencedMo.getId());
		}

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
		return new DitaMoExportCrossReferenceValidator();
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
    public void afterExport(File outputFolder) throws RSuiteException {
    }

	@Override
	public boolean embedLmd() {
		return false;
	}

	@Override
	public List<MetaDataItem> getAdditionalMetaData(ManagedObject moToExport) {
		return null;
	}

}
