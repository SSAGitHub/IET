package org.theiet.rsuite.domain.download;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.theiet.rsuite.onix.domain.OnixDeliveryPackage;
import org.theiet.rsuite.onix.domain.OnixFormatter;
import org.w3c.dom.Element;

import com.reallysi.rsuite.api.*;
import com.reallysi.rsuite.api.content.ContentDisplayObject;
import com.reallysi.rsuite.api.content.ContentObjectPath;
import com.reallysi.rsuite.api.extensions.ExecutionContext;
import com.reallysi.rsuite.api.remoteapi.RemoteApiResult;
import com.reallysi.rsuite.api.remoteapi.result.ByteSequenceResult;
import com.rsicms.projectshelper.utils.ProjectContentAssemblyUtils;

public class DownloadWebserviceHelper {

	private ExecutionContext context;

	private User user;
	
	public DownloadWebserviceHelper(ExecutionContext context, User user) {
		this.context = context;
		this.user = user;
	}

	public RemoteApiResult createWebServiceReuslt(ContentObjectPath objectPath, ManagedObject mo, String variant)
			throws RSuiteException {
		try {
			
			if (variant != null){
				VariantDescriptor variantDescriptor = mo.getVariant(variant);
				return createResultForVariant(mo.getDisplayName(), variantDescriptor);
			}
			
			Element moElement = mo.getElement();
			String fileName = getDefaultFileName(mo, moElement);

			if (isOutputOnixMessage(context, user, moElement, objectPath)) {
				VersionEntry currentVersionEntry = mo.getVersionHistory().getCurrentVersionEntry();
				String onixName = OnixDeliveryPackage.getDeliveryOnixMessageFileName(currentVersionEntry);

				return createResultObjectForOutputOnix(mo, onixName);
			}

			ByteSequenceResult result = new ByteSequenceResult(IOUtils.toByteArray(mo.getInputStream()));
			result.setSuggestedFileName(fileName);
			return result;
		} catch (IOException e) {
			throw new RSuiteException(0, e.getLocalizedMessage(), e);
		}
	}

	private RemoteApiResult createResultForVariant(String moDisplayName, VariantDescriptor variantDescriptor) throws RSuiteException {
		ByteSequenceResult result = new ByteSequenceResult(variantDescriptor.getContent());
		result.setSuggestedFileName(FilenameUtils.getBaseName(moDisplayName) + "." + variantDescriptor.getRecommendedFileExtension());
		return result;
	}

	private String getDefaultFileName(ManagedObject mo, Element moElement) throws RSuiteException {
		String fileName = mo.getDisplayName();

		if (moElement != null && !mo.isNonXml() && !mo.isAssemblyNode()) {

			if (StringUtils.isBlank(fileName)) {
				fileName = moElement.getLocalName();
			}

			if (!fileName.toLowerCase().endsWith(".xml")) {

				fileName += ".xml";
			}
		}
		return fileName;
	}

	private RemoteApiResult createResultObjectForOutputOnix(ManagedObject mo, String onixName) throws RSuiteException {
		OnixFormatter onixFormatter = new OnixFormatter();
		ByteArrayOutputStream formattedOnix = new ByteArrayOutputStream();
		onixFormatter.removeRSuiteDataFromOnixMessage(mo.getInputStream(), formattedOnix);

		ByteSequenceResult result = new ByteSequenceResult(formattedOnix.toByteArray());
		result.setSuggestedFileName(onixName);
		return result;
	}

	private boolean isOutputOnixMessage(ExecutionContext context, User user, Element moElement,
			ContentObjectPath objectPath) throws RSuiteException {

		if (objectPath.getSize() > 1 && moElement != null && "ONIXMessage".equals(moElement.getLocalName())) {
			List<ContentDisplayObject> pathObjects = objectPath.getPathObjects();
			ContentDisplayObject parentDisplayObject = pathObjects.get(pathObjects.size() - 2);
			ManagedObject parentMo = parentDisplayObject.getManagedObject();

			ContentAssembly parentCA = ProjectContentAssemblyUtils.getCAFromMO(context, user, parentMo.getId());
			if ("onixOutput".equals(parentCA.getType())) {
				return true;
			}
		}

		return false;
	}


}
