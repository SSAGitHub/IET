package org.theiet.rsuite.onix.domain;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.reallysi.rsuite.api.ContentAssemblyItem;
import com.reallysi.rsuite.api.ManagedObject;
import com.reallysi.rsuite.api.MetaDataItem;
import com.reallysi.rsuite.api.RSuiteException;

public class OnixUnit {

	private ManagedObject onixMO;
	
	private ContentAssemblyItem bookCAitem;
	
	private Set<ProductFormat> productFormats = new HashSet<ProductFormat>();
	
	private Set<String> recipients = new HashSet<String>();
	
	private boolean isReady = false;
	
	public OnixUnit(ManagedObject onixMO, ContentAssemblyItem bookCAitem) throws RSuiteException {
		this.onixMO = onixMO;
		this.bookCAitem = bookCAitem;
		List<MetaDataItem> metadataItems = bookCAitem.getMetaDataItems();
		
		for (MetaDataItem metaItem : metadataItems){
			
			if (metaItem.getName().equals("onix_recipient")){
				recipients.add(metaItem.getValue());
			}
			
			String value = metaItem.getValue();
			
			if (metaItem.getName().equals("product_format") && StringUtils.isNotEmpty(value)){
				productFormats.add(ProductFormat.fromString(value));
			}
			
			if (metaItem.getName().equals("onix_ready")){
				value = metaItem.getValue();
				if ("yes".equalsIgnoreCase(value)){
					value = Boolean.TRUE.toString();
				}
				
				isReady = Boolean.parseBoolean(value);
			}
		}
		
		if (productFormats.isEmpty()){
			productFormats.addAll(ProductFormat.getAllProductFormats());
		}
		
	}

	public ManagedObject getOnixMO() {
		return onixMO;
	}

	public ContentAssemblyItem getBookCAitem() {
		return bookCAitem;
	}
	
	public Set<ProductFormat> getProductFormats() {
		return productFormats;
	}

	public Set<String> getRecipients() {
		return recipients;
	}

	public boolean isReady() {
		return isReady;
	}	
}
