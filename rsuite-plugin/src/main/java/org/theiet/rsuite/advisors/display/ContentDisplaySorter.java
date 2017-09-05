package org.theiet.rsuite.advisors.display;

import java.util.List;

import com.reallysi.rsuite.api.*;
import com.reallysi.rsuite.api.content.ContentDisplayObject;
import com.reallysi.rsuite.api.control.ObjectReferenceMoveOptions;
import com.reallysi.rsuite.api.extensions.ExecutionContext;
import com.reallysi.rsuite.service.*;
import com.rsicms.projectshelper.utils.browse.ProjectBrowserUtils;

public class ContentDisplaySorter {
	
	private ExecutionContext context;
	private ContentAssembly createdCA;
	private ContentAssembly parentCA;
	
	public ContentDisplaySorter(ExecutionContext context, ContentAssembly parentCA, 
			ContentAssembly createdCA) {
		this.context = context;
		this.createdCA = createdCA;
		this.parentCA = parentCA;
	}

	public void sort(ContentDisplayCASorterConfig caSorterConfig) throws RSuiteException {
		
		synchronized (parentCA.getId().intern()) {
			List<ContentDisplayObject> collection = getCASiblings(context, createdCA, parentCA);
			int newCAPosition = getPositionFromSortedCollection(context, collection, createdCA, caSorterConfig);
			relocateCA(context, createdCA, parentCA, newCAPosition);
		}
	}

	public static void relocateCA(ExecutionContext context, ContentAssembly ca, ContentAssembly parentCA, int newPosition) throws RSuiteException {
		ContentAssemblyService caServ = context.getContentAssemblyService();
		User user = context.getAuthorizationService().getSystemUser();

		ObjectReferenceMoveOptions paramObjectReferenceMoveOptions = new ObjectReferenceMoveOptions();
		paramObjectReferenceMoveOptions.setPosition(newPosition);	
		String URI[] = ProjectBrowserUtils.getBrowserUri(context, ca.getId()).split("\\/");
		String moId = URI[URI.length-1].split(":")[1];
		
		caServ.moveReference(user, moId, parentCA.getId(), paramObjectReferenceMoveOptions);
		
	}

	private int getPositionFromSortedCollection(ExecutionContext context, List<ContentDisplayObject> collection, ContentAssembly ca,
			ContentDisplayCASorterConfig contentDisplayCASorterConfig) throws RSuiteException {
		ContentDisplayCaSorter cdCaSorter = new ContentDisplayCaSorter(collection);
		cdCaSorter.sort(contentDisplayCASorterConfig.getSortCaBy());
		int newCAPosition = getCAPosition(context, collection, ca.getId());
		
		return newCAPosition;
	}
	
	private List<ContentDisplayObject> getCASiblings(ExecutionContext context, ContentAssembly ca, ContentAssembly parentCA) throws RSuiteException {
		ManagedObjectService moServ = context.getManagedObjectService();
		User user = context.getAuthorizationService().getSystemUser();
		String uri = moServ.getDependencyTracker().listDirectReferences(context.getAuthorizationService().getSystemUser(),parentCA.getId()).get(0).getBrowseUri();
		List<ContentDisplayObject> collection = moServ.getContainerDisplayObjects(user, parentCA.getId(), uri);
		
		return collection;
	}
	
	

	private int getCAPosition(ExecutionContext context, List<ContentDisplayObject> collection, String caId) throws RSuiteException {
		for (int i = 0; i < collection.size(); i++) {
			String contentId = collection.get(i).getManagedObject().getTargetId();
			if (contentId.equals(caId)) {
				return i;
			}
		}
		
		return -1;
	}
	
}
