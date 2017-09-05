package org.theiet.rsuite.mocks.api.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.theiet.rsuite.mocks.api.ContentAssemblyMock;

import com.reallysi.rsuite.api.ContentAssembly;
import com.reallysi.rsuite.api.ContentAssemblyItem;
import com.reallysi.rsuite.api.ContentAssemblyNode;
import com.reallysi.rsuite.api.ContentAssemblyNodeContainer;
import com.reallysi.rsuite.api.ContentAssemblySummaryInfo;
import com.reallysi.rsuite.api.ContentAssemblyTypeDefinitionCatalog;
import com.reallysi.rsuite.api.ManagedObject;
import com.reallysi.rsuite.api.ManagedObjectReference;
import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.User;
import com.reallysi.rsuite.api.VersionSpecifier;
import com.reallysi.rsuite.api.content.ContentDisplayObject;
import com.reallysi.rsuite.api.control.ContentAssemblyBuilder;
import com.reallysi.rsuite.api.control.ContentAssemblyCreateOptions;
import com.reallysi.rsuite.api.control.ContentAssemblyItemFilter;
import com.reallysi.rsuite.api.control.ContentAssemblyUpdateOptions;
import com.reallysi.rsuite.api.control.ContentAssemblyUpdater;
import com.reallysi.rsuite.api.control.InsertXmlOperationType;
import com.reallysi.rsuite.api.control.ObjectAttachOptions;
import com.reallysi.rsuite.api.control.ObjectCopyContentAssemblyOptions;
import com.reallysi.rsuite.api.control.ObjectCopyOptions;
import com.reallysi.rsuite.api.control.ObjectDetachOptions;
import com.reallysi.rsuite.api.control.ObjectFreezeOptions;
import com.reallysi.rsuite.api.control.ObjectMoveContentAssemblyOptions;
import com.reallysi.rsuite.api.control.ObjectReferenceMoveOptions;
import com.reallysi.rsuite.api.control.ObjectUnfreezeOptions;
import com.reallysi.rsuite.api.search.Search;
import com.reallysi.rsuite.service.ContentAssemblyService;

public class ContentAssemblyServiceMock implements ContentAssemblyService {

	public static Map<String, ContentAssemblyMock> mocksCaMap = new HashMap<String, ContentAssemblyMock>();

	public static Map<String, List<String>> childMap = new HashMap<String, List<String>>();

	@Override
	public ContentAssemblyBuilder constructContentAssemblyBuilder(User user,
			String parenId, String name, ContentAssemblyCreateOptions options)
			throws RSuiteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ContentAssemblyUpdater constructContentAssemblyUpdater(User user,
			String id, ContentAssemblyUpdateOptions options)
			throws RSuiteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isContentAssemblyByID(String ID) throws RSuiteException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isReferenceFromContentAssembly(String id) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public ContentAssemblyNodeContainer createCANode(User user,
			String parentid, String name, ContentAssemblyCreateOptions options)
			throws RSuiteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ContentAssemblyNodeContainer createCANodesForPath(User user,
			ContentAssembly ca, String path) throws RSuiteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ContentAssemblyNodeContainer createCANodesForPath(User user,
			ContentAssembly ca, String[] pathComponents) throws RSuiteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ContentAssembly getRootFolder(User user) throws RSuiteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ContentAssemblyNode getCANode(User user, String canodeId)
			throws RSuiteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ContentAssembly renameContentAssembly(User user, String caid,
			String name) throws RSuiteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ContentAssemblyItem renameCANode(User user, String canodeId,
			String name) throws RSuiteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ContentAssemblyNodeContainer copyInto(User user, String sourceCaId,
			String targetCaId, ObjectCopyContentAssemblyOptions options)
			throws RSuiteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ContentAssembly moveCANode(User user, String caNodeId,
			String newParentId, ObjectMoveContentAssemblyOptions moveOptions)
			throws RSuiteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ContentAssemblyItem moveToFolder(User user, String caId,
			String targetFolderId, ObjectMoveContentAssemblyOptions moveOptions)
			throws RSuiteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ContentAssembly moveReference(User user, String moId,
			String newParentId, int position) throws RSuiteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ContentAssembly moveReference(User user, String moId,
			String newParentId, ObjectReferenceMoveOptions moveOptions)
			throws RSuiteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ContentAssembly createContentAssembly(User user, String parentId,
			String name, boolean silentIfExists) throws RSuiteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ContentAssembly createContentAssembly(User user, String parentId,
			String name, ContentAssemblyCreateOptions options)
			throws RSuiteException {

		String id = UUID.randomUUID().toString();
		String type = "ca";
		if (options != null && options.getType() != null){
			type = options.getType();
		}
		
		ContentAssemblyMock ca = new ContentAssemblyMock(this, id, type, name);
		
		mocksCaMap.put(id, ca);
		
		if (parentId != null){
			List<String> childIds =  childMap.get(parentId);
			if (childIds == null){
				childIds = new ArrayList<String>();
				childMap.put(parentId, childIds);
			}
			
			childIds.add(id);
		}
		
		return ca;
	}

	@Override
	public void removeContentAssembly(User user, String id)
			throws RSuiteException {
		mocksCaMap.remove(id);
		
	}

	@Override
	public ManagedObject moveUpReference(User user, String moid, String parentid)
			throws RSuiteException {
		return null;
	}

	@Override
	public ManagedObject moveDownReference(User user, String moid,
			String parentid) throws RSuiteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ManagedObject attach(User user, String contentAssemblyId,
			ManagedObject mo, ObjectAttachOptions options)
			throws RSuiteException {

		 List<String> list = childMap.get(contentAssemblyId);
		 if (list == null){
			 list = new ArrayList<String>();
			 childMap.put(contentAssemblyId, list);
		 }
		 
		 list.add(mo.getId());
		
		return mo;
	}

	@Override
	public ManagedObject attach(User user, String contentAssemblyId,
			String moId, ObjectAttachOptions options) throws RSuiteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ManagedObject attach(User user, String contentAssemblyId,
			ManagedObject mo, String revision, ObjectAttachOptions options)
			throws RSuiteException {

		ContentAssembly ca = mocksCaMap.get(contentAssemblyId);
		return null;
	}

	@Override
	public ManagedObject attach(User user, String contentAssemblyId,
			ManagedObject mo, InsertXmlOperationType action,
			String insertionPointMOId, String revision,
			ObjectAttachOptions options) throws RSuiteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ManagedObject attach(User user, String contentAssemblyId,
			String moId, InsertXmlOperationType action,
			String insertionPointMOId, String revision,
			ObjectAttachOptions options) throws RSuiteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ContentAssembly attach(User user, String contentAssemblyId,
			List<ManagedObject> managedObjects, ObjectAttachOptions options)
			throws RSuiteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ManagedObject attach(User user, String contentAssemblyId,
			ContentAssembly caToAttach, ObjectAttachOptions options)
			throws RSuiteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ManagedObject freeze(User user, String id,
			ObjectFreezeOptions options) throws RSuiteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ManagedObject unfreeze(User user, String id,
			ObjectUnfreezeOptions options) throws RSuiteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ContentAssemblyNodeContainer moveTo(User user, String newParentCaId,
			String caId) throws RSuiteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ContentAssemblyItem moveTo(User user, String newParentCaId,
			String caId, ObjectMoveContentAssemblyOptions moveOptions)
			throws RSuiteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ManagedObject copyAndAttach(User user, String moId, String caId,
			ObjectAttachOptions attachOptions) throws RSuiteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ManagedObject copyTemplateAndAttach(User user, String templateId,
			String caId, ObjectCopyOptions copyOptions,
			ObjectAttachOptions attachOptions) throws RSuiteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ManagedObject copyAndAttach(User user, String moId, String caId,
			ObjectCopyOptions copyOptions, ObjectAttachOptions attachOptions)
			throws RSuiteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ManagedObject copyAndAttach(User user, VersionSpecifier moSpecifier,
			String caId, ObjectCopyOptions copyOptions,
			ObjectAttachOptions attachOptions) throws RSuiteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ContentAssemblyNodeContainer createDynamicCANodeFromXPath(User user,
			String xpath, String parentId, String name) throws RSuiteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ContentAssemblyNodeContainer createDynamicCANodeFromXPath(User user,
			String xpath, String parentId, String name,
			ContentAssemblyCreateOptions options) throws RSuiteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ContentAssemblyNodeContainer createDynamicCANodeFromSearch(
			User user, String parentId, String name, Search search,
			ContentAssemblyCreateOptions options) throws RSuiteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ContentAssemblyNodeContainer createSmartFolderFromFacetedSearch(
			User arg0, String arg1, String arg2, String arg3,
			ContentAssemblyCreateOptions arg4) throws RSuiteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ContentAssemblyNodeContainer createSmartFolderFromFullTextSearch(
			User arg0, String arg1, String arg2, String arg3,
			ContentAssemblyCreateOptions arg4) throws RSuiteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ContentAssemblyNodeContainer createSmartFolderFromSearch(User arg0,
			String arg1, String arg2, Search arg3,
			ContentAssemblyCreateOptions arg4) throws RSuiteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ContentAssemblyNodeContainer createSmartFolderFromXpathSearch(
			User arg0, String arg1, String arg2, String arg3,
			ContentAssemblyCreateOptions arg4) throws RSuiteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteCANode(User user, String canodeId) throws RSuiteException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ContentAssembly detach(User user, String contentAssemblyId,
			List<ManagedObject> managedObjects, ObjectDetachOptions options)
			throws RSuiteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ContentAssembly detach(User user, String contentAssemblyId,
			String referrerId, ObjectDetachOptions options)
			throws RSuiteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ContentAssembly detachAll(User user, String contentAssemblyId,
			ObjectDetachOptions options) throws RSuiteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ContentAssembly detachManagedObjects(User arg0, String arg1,
			List<ManagedObject> arg2, ObjectDetachOptions arg3)
			throws RSuiteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ContentAssemblySummaryInfo> findAllContentAssemblies(User user)
			throws RSuiteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ContentAssemblyNodeContainer findCANodeForPath(User user,
			ContentAssembly ca, String[] pathComponents) throws RSuiteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ContentAssembly> listContentAssembliesByName(User user,
			String parentId, String caName) throws RSuiteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ManagedObjectReference> listReferencesToManagedObjectInContentAssembly(
			User user, ContentAssembly ca, ManagedObject mo)
			throws RSuiteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ContentAssemblyItemFilter getAllContentAssemblyItemFilter() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ContentAssemblyItemFilter getAllManagedObjectReferenceFilter() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ContentAssemblyItemFilter getAllManagedObjectReferenceToTargetFilter(
			String targetId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<? extends ContentAssemblyItem> getChildObjectsFiltered(
			User user, ContentAssemblyNodeContainer caNodeContainer,
			ContentAssemblyItemFilter filter) throws RSuiteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ContentAssemblyItem findObjectForPath(User user, String path)
			throws RSuiteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ContentDisplayObject> getChildrenDisplayObjectsForSearch(
			User arg0, ContentAssemblyNodeContainer arg1)
			throws RSuiteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ManagedObject> getChildrenObjectsForSearch(User user,
			ContentAssemblyNodeContainer ca) throws RSuiteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ContentAssembly getContentAssemblyById(User user,
			String contentAssemblyId) throws RSuiteException {
		
		return mocksCaMap.get(contentAssemblyId);
	}

	@Override
	public ContentAssembly getContentAssembly(User user, String caId)
			throws RSuiteException {
		
		return mocksCaMap.get(caId);
	}

	@Override
	public ContentAssemblyItem getContentAssemblyItem(User user, String id)
			throws RSuiteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ContentAssembly getContentAssemblyInFolder(User user,
			String folderPath, String name) throws RSuiteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ContentAssemblyNodeContainer getContentAssemblyNodeContainer(
			User arg0, String arg1) throws RSuiteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ContentAssemblyTypeDefinitionCatalog getContentAssemblyTypeDefinitionCatalog()
			throws RSuiteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ContentAssemblyItemFilter getGraphicsContentAssemblyItemFilter(
			Set<String> includedFileExtensions) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ContentAssemblyItemFilter getGraphicsContentAssemblyItemFilter() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<? extends ContentAssemblyItem> getDescendantObjectsFiltered(
			User user, ContentAssemblyNodeContainer caNodeContainer,
			ContentAssemblyItemFilter filter) throws RSuiteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ContentAssembly changeContentAssemblyType(User arg0, String arg1,
			String arg2) throws RSuiteException {
		// TODO Auto-generated method stub
		return null;
	}


}
