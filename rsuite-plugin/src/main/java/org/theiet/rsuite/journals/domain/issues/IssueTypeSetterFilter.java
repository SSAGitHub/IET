package org.theiet.rsuite.journals.domain.issues;

import java.util.HashSet;
import java.util.Set;

import org.theiet.rsuite.IetConstants;
import org.theiet.rsuite.journals.JournalConstants;

import com.reallysi.rsuite.api.ContentAssembly;
import com.reallysi.rsuite.api.ContentAssemblyItem;
import com.reallysi.rsuite.api.ContentAssemblyReference;
import com.reallysi.rsuite.api.ManagedObject;
import com.reallysi.rsuite.api.ManagedObjectReference;
import com.reallysi.rsuite.api.ObjectType;
import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.User;
import com.reallysi.rsuite.api.control.ContentAssemblyItemFilter;
import com.reallysi.rsuite.api.extensions.ExecutionContext;
import com.reallysi.rsuite.service.ContentAssemblyService;
import com.reallysi.rsuite.service.ManagedObjectService;

public class IssueTypeSetterFilter implements ContentAssemblyItemFilter, JournalConstants {

	
	private ContentAssemblyService caService;
	
	private ManagedObjectService moService;
	
	private Set<String> allowedCATypes = new HashSet<String>();
	
	public IssueTypeSetterFilter(ExecutionContext context) {
		super();
		caService = context.getContentAssemblyService();
		moService = context.getManagedObjectService();
		allowedCATypes.add(CA_TYPE_ARTICLE);
		allowedCATypes.add(IetConstants.CA_TYPE_TYPESETTER);
	}



	@Override
	public boolean include(User user, ContentAssemblyItem item)
			throws RSuiteException {
		
		ContentAssembly ca = null;
		ManagedObject mo = null;
		
		if (item.getObjectType() == ObjectType.CONTENT_ASSEMBLY_REF){
			ContentAssemblyReference caRef = (ContentAssemblyReference)item;
			ca = caService.getContentAssembly(user, caRef.getTargetId());
		}else if(item.getObjectType() == ObjectType.CONTENT_ASSEMBLY){
			ca = (ContentAssembly)item;
		}else if (item.getObjectType() == ObjectType.MANAGED_OBJECT_REF){
			ManagedObjectReference moRef = (ManagedObjectReference)item;
			mo = moService.getManagedObject(user, moRef.getTargetId());
		}else if (item.getObjectType() == ObjectType.MANAGED_OBJECT){
			mo = (ManagedObject)item;
		}
		
		if (ca != null && allowedCATypes.contains(ca.getType())){
			return true;
		}
		
		if (mo != null && "article".equals(mo.getLocalName())){
			return true;
		}
		
		
		return false;
	}

}
