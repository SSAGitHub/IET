package org.theiet.rsuite.mocks.api;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.theiet.rsuite.mocks.api.service.ContentAssemblyServiceMock;
import org.w3c.dom.Element;

import com.reallysi.rsuite.api.ContentAssembly;
import com.reallysi.rsuite.api.ContentAssemblyItem;
import com.reallysi.rsuite.api.ContentAssemblyNode;
import com.reallysi.rsuite.api.ManagedObject;
import com.reallysi.rsuite.api.MetaDataItem;
import com.reallysi.rsuite.api.ObjectType;
import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.security.ACL;

public class ContentAssemblyMock implements ContentAssembly {

	private String type;
	
	private String displayName;
	
	private List<MetaDataItem> metdataItems = new ArrayList<MetaDataItem>();
	
	private String id;
	
	private ContentAssemblyServiceMock caSvc;
	
	private List<ContentAssemblyItem> additionalChilds = new ArrayList<ContentAssemblyItem>();
	
	public ContentAssemblyMock(List<MetaDataItem> metdataItems){
		this.metdataItems = metdataItems;
	}
	
	public ContentAssemblyMock(ContentAssemblyServiceMock caSvc, String id, String type, String displayName) {		
		this.type = type;
		this.displayName = displayName;
		this.id = id;
		this.caSvc = caSvc;
	}

	@Override
	public Element getElement() throws RSuiteException {
		// TODO Auto-generated method stub
		return null;
	}

	public List<ContentAssemblyItem> getAdditionalChilds() {
		return additionalChilds;
	}

	@Override
	public boolean isDynamic() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean hasChild(ManagedObject mo) throws RSuiteException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public String getDisplayName() {
		return displayName;
	}

	@Override
	public String getType() {
		return type;
	}

	@Override
	public ObjectType getObjectType() {
		return ObjectType.CONTENT_ASSEMBLY;
	}

	@Override
	public ACL getACL() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Date getDtCreated() throws RSuiteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<? extends ContentAssemblyItem> getChildrenObjects()
			throws RSuiteException {
		
		List<String> childsIds = caSvc.childMap.get(id);
		
		
		if (childsIds != null){
			List<ContentAssemblyItem> childs = new ArrayList<ContentAssemblyItem>(); 
			for (String childId : childsIds){
				ContentAssembly assembly = caSvc.getContentAssembly(null, childId);
				if (assembly != null){
					childs.add(assembly);	
				}else{
					for (ContentAssemblyItem item: additionalChilds){
						childs.add(item);
					}
					
				}
				
			}
			
			return childs;
		}
		
		return new ArrayList<ContentAssemblyItem>();
	}

	@Override
	public List<ContentAssemblyNode> getContentAssemblyNodeChildren()
			throws RSuiteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ContentAssemblyItem findDescendantOrSelf(String id)
			throws RSuiteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getIdListDescendantsOrSelf() throws RSuiteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<MetaDataItem> getMetaDataItems() throws RSuiteException {
		return metdataItems;
	}

	@Override
	public String getLayeredMetadataValue(String name) throws RSuiteException {
		
		for (MetaDataItem item : metdataItems){
			if (item.getName().equals(name)){
				return item.getValue();
			}
		}
		
		return null;
	}

	@Override
	public void setUri(String uri) {
		// TODO Auto-generated method stub

	}

	public void addMetadataItem(MetaDataItem item){
		metdataItems.add(item);
	}

	public void addMetadataItem(List<MetaDataItem> items){
		metdataItems.addAll(items);
	}

	@Override
	public Date getDtModified() throws RSuiteException {
		// TODO Auto-generated method stub
		return null;
	}

}
