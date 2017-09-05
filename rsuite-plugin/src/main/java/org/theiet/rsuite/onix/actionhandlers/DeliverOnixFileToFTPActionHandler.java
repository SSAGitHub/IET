package org.theiet.rsuite.onix.actionhandlers;

import java.io.File;

import org.apache.commons.logging.Log;
import org.theiet.rsuite.onix.OnixConstants;
import org.theiet.rsuite.onix.datatype.OnixRequestDTO;
import org.theiet.rsuite.onix.domain.OnixDeliveryPackage;
import org.theiet.rsuite.onix.domain.OnixFtpSender;

import com.reallysi.rsuite.api.*;
import com.reallysi.rsuite.api.control.ObjectMetaDataSetOptions;
import com.reallysi.rsuite.api.workflow.WorkflowExecutionContext;
import com.reallysi.rsuite.service.ContentAssemblyService;
import com.rsicms.projectshelper.workflow.actionhandlers.AbstractNonLeavingActionHandler;

public class DeliverOnixFileToFTPActionHandler extends AbstractNonLeavingActionHandler implements OnixConstants {

	private static final long serialVersionUID = 1L;

	@Override
	public void executeTask(WorkflowExecutionContext context) throws Exception {

		Log wlog = context.getWorkflowLog();

		wlog.info("Trying to deliver ONIX pacakge to FTP...");

		User user = context.getAuthorizationService().getSystemUser();
		ContentAssemblyService caSvc = context.getContentAssemblyService();

		String recipientCaId = context.getVariable(WF_VAR_RSUITE_CONTENTS);
		boolean isValidOnixFile = (Boolean) context.getVariableAsObject(WF_VAR_VALID_ONIX_FILE);
		File validatedOnixFile = (File) context.getVariableAsObject(WF_VAR_RESULT_FILE);
		ContentAssembly recipentCa = caSvc.getContentAssembly(user, recipientCaId);
		String onixExternalUser = recipentCa.getLayeredMetadataValue(LMD_FIELD_ONIX_BOOK_VENDOR);

		OnixDeliveryPackage deliveryPackage = new OnixDeliveryPackage();
		File onixDeliveryPackage = deliveryPackage.createDeliveryOnixZipPackage(validatedOnixFile,
				validatedOnixFile.getParentFile());

		OnixRequestDTO onixRequestDTO = getOnixRequestDTO(onixDeliveryPackage, onixExternalUser, isValidOnixFile);

		OnixFtpSender.sendOnixRequestPackage(context, onixRequestDTO);

		incrementMessageNumber(recipentCa);
	}

	public void incrementMessageNumber(ContentAssembly recipentCa) throws RSuiteException {
		String messageNumerLmd = recipentCa.getLayeredMetadataValue(LMD_FIELD_ONIX_MESSAGE_NUMBER);

		int messageNumber = 0;
		if (messageNumerLmd != null) {
			try {
				messageNumber = Integer.parseInt(messageNumerLmd);
			} catch (NumberFormatException e) {

			}
		}

		messageNumber++;

		ObjectMetaDataSetOptions metadataOptions = new ObjectMetaDataSetOptions();
		metadataOptions.setAddNewItem(false);

		MetaDataItem metaDataItem = new MetaDataItem(LMD_FIELD_ONIX_MESSAGE_NUMBER, String.valueOf(messageNumber));
		context.getManagedObjectService().addMetaDataEntry(getSystemUser(), recipentCa.getId(), metaDataItem,
				metadataOptions);
	}

	private OnixRequestDTO getOnixRequestDTO(File resultFile, String onixExternalUser, boolean isValidOnixFile) {
		OnixRequestDTO onixRequestDTO = new OnixRequestDTO();
		onixRequestDTO.setFile(resultFile);
		onixRequestDTO.setOnixExternalUser(onixExternalUser);
		onixRequestDTO.setTargetFileName(resultFile.getName());

		if (isValidOnixFile) {
			onixRequestDTO.setTargetFolderProperty(CFG_ONIX_VALIDATION_SUCCEEDED);
		} else {
			onixRequestDTO.setTargetFolderProperty(CFG_ONIX_VALIDATION_FAILED);
		}

		return onixRequestDTO;
	}

}