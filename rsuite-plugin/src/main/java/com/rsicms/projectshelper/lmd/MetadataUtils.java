package com.rsicms.projectshelper.lmd;

import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.reallysi.rsuite.api.MetaDataItem;
import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.User;
import com.reallysi.rsuite.api.control.ObjectCheckInOptions;
import com.reallysi.rsuite.api.extensions.ExecutionContext;
import com.reallysi.rsuite.api.remoteapi.CallArgument;
import com.reallysi.rsuite.api.remoteapi.CallArgumentList;
import com.reallysi.rsuite.service.ManagedObjectService;

public final class MetadataUtils {

	private static final DocumentBuilderFactory DOCUMENT_BUILDER_FACTORY = DocumentBuilderFactory.newInstance();
	
    private MetadataUtils(){};
    
	public static List<MetaDataItem> getLmdListFromWsParameters(CallArgumentList args) {
		List<MetaDataItem> metadataList = new ArrayList<MetaDataItem>();
	
		List<CallArgument> argumentList = args.getAll();
		for (CallArgument argument : argumentList) {
			String name = argument.getName();
			if (name.startsWith("lmd_")) {
				String lmdName = name.replaceFirst("lmd_", "");
				metadataList.add(new MetaDataItem(lmdName, argument.getValue()));
			}
		}
		return metadataList;
	}

	public static void setMetadata(ExecutionContext context, User user, String moId, String lmdName, String lmdValue) throws RSuiteException {

		ManagedObjectService moSvc = context.getManagedObjectService();

		moSvc.checkOut(user, moId);

		MetaDataItem metadataItem = new MetaDataItem(lmdName, lmdValue);

		moSvc.setMetaDataEntry(user, moId, metadataItem);

		moSvc.checkIn(user, moId, new ObjectCheckInOptions());
	}

	public static List<String> getLmdValues(List<MetaDataItem> metaDataItems,
			String metadataName) {
		List<String> values = new ArrayList<String>();
		
		for (MetaDataItem metadataItem : metaDataItems){
			if (metadataName.equals(metadataItem.getName())){
				values.add(metadataItem.getValue());
			}						
		}
		
		return values;
	}

	public static Document convertLmdToXmlDocument(List<MetaDataItem> metaDataItems) throws RSuiteException{
		try{
			DocumentBuilder documentBuilder = DOCUMENT_BUILDER_FACTORY.newDocumentBuilder();
			Document lmdDocument = documentBuilder.newDocument();
			Element rsuiteLmdElement = lmdDocument.createElement("rsuite_lmd");
			lmdDocument.appendChild(rsuiteLmdElement);
			
			for (MetaDataItem item : metaDataItems){
				Element lmdElement = lmdDocument.createElement("lmd");
				String lmdName = item.getName().replace("\\s+", "_");
				lmdElement.setAttribute("name", lmdName);
				lmdElement.setAttribute("value", item.getValue());
				rsuiteLmdElement.appendChild(lmdElement);
			}
			return lmdDocument;
		}catch (ParserConfigurationException e){
			throw new RSuiteException("Unable to convert lmd to a document");
		}
		
	}
}
