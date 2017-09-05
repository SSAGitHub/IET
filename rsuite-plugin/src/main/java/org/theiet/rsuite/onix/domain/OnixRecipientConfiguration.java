package org.theiet.rsuite.onix.domain;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.theiet.rsuite.onix.OnixConstants;

import com.reallysi.rsuite.api.ContentAssembly;
import com.reallysi.rsuite.api.ContentAssemblyItem;
import com.reallysi.rsuite.api.ManagedObject;
import com.reallysi.rsuite.api.ManagedObjectReference;
import com.reallysi.rsuite.api.MetaDataItem;
import com.reallysi.rsuite.api.ObjectType;
import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.User;
import com.reallysi.rsuite.api.extensions.ExecutionContext;
import com.reallysi.rsuite.service.ManagedObjectService;

public class OnixRecipientConfiguration  implements OnixConstants{

	private static final String LMD_FIELD_PRODUCT_FORMAT = "product_format";

	private ContentAssembly recipientConfigurationCa;

	private ExecutionContext context;
	
	private OnixDefaultConfiguration defaultConfiguration;
	
	private Map<String, String> configurationUris = new HashMap<String, String>();
	
	private String recipientName;
	
	private Set<ProductFormat> productFormats = new HashSet<ProductFormat>();

	public OnixRecipientConfiguration(ExecutionContext context,
			ContentAssembly recipientConfigurationCa, OnixDefaultConfiguration defaultConfiguration) throws RSuiteException {
		super();
		this.recipientConfigurationCa = recipientConfigurationCa;
		this.context = context;
		this.defaultConfiguration = defaultConfiguration;
		this.recipientName = recipientConfigurationCa.getDisplayName();
		
		productFormats = obtainRecipientProductFormats(recipientConfigurationCa);
		
		if (productFormats.isEmpty()){
			productFormats.addAll(ProductFormat.getAllProductFormats());
		}
	}

	private Set<ProductFormat> obtainRecipientProductFormats(
			ContentAssembly recipientConfigurationCa) throws RSuiteException {
		List<MetaDataItem> metadataItems = recipientConfigurationCa.getMetaDataItems();
		Set<ProductFormat> productFormats = new HashSet<ProductFormat>();
		
		try{
			for (MetaDataItem metaItem : metadataItems){
				
				if (LMD_FIELD_PRODUCT_FORMAT.equals(metaItem.getName()) && StringUtils.isNotEmpty(metaItem.getValue())){
					productFormats.add(ProductFormat.fromString(metaItem.getValue()));
				}
			}
		}catch (IllegalArgumentException e){
			String exceptionContext = recipientConfigurationCa.getDisplayName() + "[" + recipientConfigurationCa.getId() + "] : ";
			throw new RSuiteException(0, exceptionContext +  e.getMessage(), e);
		}
			
		return productFormats;
	}

	public String getFilteringConfigurationUri(String outputType)
			throws RSuiteException {
		
		if (configurationUris.containsKey(outputType)){
			return configurationUris.get(outputType);
		}
		
		String configurationUri = getChildConfigurationUri(context, recipientConfigurationCa,
				"onix_configuration_type", outputType);
		
		if (configurationUri == null){
			configurationUri = defaultConfiguration.getDefaultFilteringConfigurationUri(outputType);
		}
		
		if (configurationUri == null){
			throw new RSuiteException("Unable to localize filtering configuration for type " + outputType);
		}
		
		 configurationUris.put(outputType, configurationUri);
		
		return configurationUri;
	}

	public static String getChildConfigurationUri(ExecutionContext context,
			ContentAssembly contextCa, String lmdName, String lmdValue)
			throws RSuiteException {
		ManagedObjectService moSvc = context.getManagedObjectService();
		User user = context.getAuthorizationService().getSystemUser();

		List<? extends ContentAssemblyItem> children = contextCa
				.getChildrenObjects();

		for (ContentAssemblyItem child : children) {

			ManagedObject mo;

			if (child.getObjectType() == ObjectType.MANAGED_OBJECT_REF) {

				ManagedObjectReference moref = (ManagedObjectReference) child;
				mo = moSvc.getManagedObject(user, moref.getTargetId());
			} else if (child.getObjectType() == ObjectType.MANAGED_OBJECT_REF) {
				mo = (ManagedObject) child;
			} else {
				continue;
			}

			List<MetaDataItem> metadataItems = mo.getMetaDataItems();
			for (MetaDataItem metadata : metadataItems) {
				if (metadata.getName().equals(lmdName)
						&& metadata.getValue().equals(lmdValue)) {
					return "rsuite:/res/content/" + mo.getId();
				}
			}
		}

		return null;
	}

	public ContentAssembly getRecipientConfigurationCa() {
		return recipientConfigurationCa;
	}

	public String getRecipientTemplate() throws RSuiteException {
		
		String outputType = "template";
		
		if (configurationUris.containsKey(outputType)){
			return configurationUris.get(outputType);
		}
		
		String configurationUri = getChildConfigurationUri(context, recipientConfigurationCa,
				"onix_configuration_type", outputType);
		
		configurationUris.put(outputType, configurationUri);
		
		return configurationUri;
	}


	public Set<ProductFormat> getProductFormats() {
		return productFormats;
	}

	public String getRecipientName() {
		return recipientName;
	}

	public OnixDefaultConfiguration getDefaultConfiguration() {
		return defaultConfiguration;
	}
	
	
	public String getMessageNumber() throws RSuiteException{
		String messageNumber = recipientConfigurationCa.getLayeredMetadataValue(LMD_FIELD_ONIX_MESSAGE_NUMBER);
		return messageNumber == null ? "" : messageNumber;
	}
}
