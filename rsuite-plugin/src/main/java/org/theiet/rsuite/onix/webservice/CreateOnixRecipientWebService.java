package org.theiet.rsuite.onix.webservice;

import java.util.*;

import org.theiet.rsuite.onix.OnixConstants;
import org.theiet.rsuite.utils.ContentDisplayUtils;

import com.reallysi.rsuite.api.*;
import com.reallysi.rsuite.api.extensions.ExecutionContext;
import com.reallysi.rsuite.api.remoteapi.*;
import com.reallysi.rsuite.api.remoteapi.result.*;
import com.rsicms.projectshelper.utils.ProjectContentAssemblyUtils;
import com.rsicms.projectshelper.utils.browse.ProjectBrowserUtils;

public class CreateOnixRecipientWebService extends DefaultRemoteApiHandler implements OnixConstants {

	private static final String PARAM_ONIX_RECIPIENT = LMD_FIELD_ONIX_RECIPIENT;
	private static final String PARAM_PRODUCT_FORMAT = LMD_FIELD_PRODUCT_FORMAT;
	private static final String PARAM_ONIX_BOOK_VENDOR = LMD_FIELD_ONIX_BOOK_VENDOR;
	
	@Override
	public RemoteApiResult execute(RemoteApiExecutionContext context,
			CallArgumentList args) throws RSuiteException {

		String onixConfigCaId = args.getFirstValue(PARAM_RSUITE_ID);
		String recipientName = args.getFirstValue(PARAM_ONIX_RECIPIENT);
		
		try {
			
			if (isRecipientNameCurrentlyUsed(context, onixConfigCaId, recipientName)) {
				throw new RSuiteException("Recipient with given name already exist");
			}
			
			createRecipientCa(context, args, onixConfigCaId, recipientName);
			
		} catch (RSuiteException ex) {
			return new MessageDialogResult(MessageType.ERROR,
					"Create New Onix Recipient", ex.getMessage());
		}
		
		String msg = "A new Onix Recipient has been created";
		return ContentDisplayUtils.getResultWithLabelRefreshing(
				MessageType.SUCCESS, "Create New Onix Recipient", msg, "500", onixConfigCaId);
	}
	
	private ContentAssembly createRecipientCa(
			RemoteApiExecutionContext context,
			CallArgumentList args, String onixConfigCaId,
			String recipientName) throws RSuiteException {
		User user = context.getAuthorizationService().getSystemUser();
		String productFormat = args.getFirstValue(PARAM_PRODUCT_FORMAT);
		String bookVendor = args.getFirstValue(PARAM_ONIX_BOOK_VENDOR);
		
		List<MetaDataItem> metadataList = new ArrayList<MetaDataItem>();
		
		
		metadataList.add(new MetaDataItem(LMD_FIELD_PRODUCT_FORMAT, productFormat));
		metadataList.add(new MetaDataItem(LMD_FIELD_ONIX_BOOK_VENDOR, bookVendor));
		metadataList.add(new MetaDataItem(LMD_FIELD_ONIX_MESSAGE_NUMBER, "1"));
		
		ContentAssembly recipientCA = ProjectContentAssemblyUtils.createContentAssembly(context, onixConfigCaId,
				recipientName, CA_TYPE_ONIX_RECIPIENT);
		
		context.getManagedObjectService().setMetaDataEntries(user, recipientCA.getId(),
				metadataList);

		ProjectContentAssemblyUtils.createContentAssembly(context, recipientCA.getId(),
				CA_NAME_OUTPUT, CA_TYPE_ONIX_OUTPUT);
		
		return recipientCA;

	}
	
	private boolean isRecipientNameCurrentlyUsed (ExecutionContext context, String onixConfigCaId, 
			String recipientName) throws RSuiteException {
		if (recipientName == null) {
			return true;
		}
		
		User user = context.getAuthorizationService().getSystemUser();
		
		ContentAssemblyItem onixConfigCaItem = ProjectContentAssemblyUtils.getCAItem(context, user, onixConfigCaId);
		List<ContentAssembly> onixRecipients = ProjectBrowserUtils.getChildrenCaByType(context, onixConfigCaItem, CA_TYPE_ONIX_RECIPIENT);

		for (ContentAssembly onixRecipient : onixRecipients) {
			String onixRecipietName = onixRecipient.getDisplayName();
			if (onixRecipietName != null && onixRecipietName.equalsIgnoreCase(recipientName)) {
				return true;
			}
		}

		return false;
	}
	
}
