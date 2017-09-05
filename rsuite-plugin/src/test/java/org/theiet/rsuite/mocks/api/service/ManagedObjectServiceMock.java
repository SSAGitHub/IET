package org.theiet.rsuite.mocks.api.service;

import java.io.File;
import java.io.InputStream;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.mockito.Mockito;
import org.theiet.rsuite.mocks.api.ContentAssemblyMock;
import org.w3c.dom.Element;

import com.reallysi.rsuite.api.Alias;
import com.reallysi.rsuite.api.CheckOutInfo;
import com.reallysi.rsuite.api.ManagedObject;
import com.reallysi.rsuite.api.ManagedObjectSummaryInfo;
import com.reallysi.rsuite.api.ManagedObjectTree;
import com.reallysi.rsuite.api.MetaDataItem;
import com.reallysi.rsuite.api.ObjectReference;
import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.User;
import com.reallysi.rsuite.api.ValidationException;
import com.reallysi.rsuite.api.VariantDescriptor;
import com.reallysi.rsuite.api.VersionHistory;
import com.reallysi.rsuite.api.VersionSpecifier;
import com.reallysi.rsuite.api.VersionType;
import com.reallysi.rsuite.api.browse.BrowseInfo;
import com.reallysi.rsuite.api.content.ContentDisplayObject;
import com.reallysi.rsuite.api.content.SchemaContentStats;
import com.reallysi.rsuite.api.control.DependencyTracker;
import com.reallysi.rsuite.api.control.ImageConverter;
import com.reallysi.rsuite.api.control.ImageMagickConfiguration;
import com.reallysi.rsuite.api.control.ManagedObjectLoader;
import com.reallysi.rsuite.api.control.MetaDataChangeSet;
import com.reallysi.rsuite.api.control.ObjectCheckInOptions;
import com.reallysi.rsuite.api.control.ObjectCheckOutOptions;
import com.reallysi.rsuite.api.control.ObjectCopyOptions;
import com.reallysi.rsuite.api.control.ObjectDestroyOptions;
import com.reallysi.rsuite.api.control.ObjectInsertIntoOptions;
import com.reallysi.rsuite.api.control.ObjectInsertOptions;
import com.reallysi.rsuite.api.control.ObjectMetaDataModifyOptions;
import com.reallysi.rsuite.api.control.ObjectMetaDataSetOptions;
import com.reallysi.rsuite.api.control.ObjectPurgeOptions;
import com.reallysi.rsuite.api.control.ObjectRefreshOptions;
import com.reallysi.rsuite.api.control.ObjectRollbackOptions;
import com.reallysi.rsuite.api.control.ObjectSource;
import com.reallysi.rsuite.api.control.ObjectUpdateOptions;
import com.reallysi.rsuite.api.control.PreviewHelper;
import com.reallysi.rsuite.api.control.PurgePolicy;
import com.reallysi.rsuite.api.control.VariantCreationConfiguration;
import com.reallysi.rsuite.api.control.XmlObjectSource;
import com.reallysi.rsuite.api.editor.XopusIntegrationAdvisor;
import com.reallysi.rsuite.api.system.StorageBucketCatalog;
import com.reallysi.rsuite.api.tools.AliasHelper;
import com.reallysi.rsuite.service.ManagedObjectService;

public class ManagedObjectServiceMock implements ManagedObjectService {

	@Override
	public VariantCreationConfiguration getVariantCreationConfiguration() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PreviewHelper getPreviewHelper() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AliasHelper getAliasHelper() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public XopusIntegrationAdvisor getXopusIntegrationAdvisor(User user,
			String name, ManagedObject mo) throws RSuiteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public StorageBucketCatalog getStorageBucketCatalog()
			throws RSuiteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ManagedObject getTargetManagedObject(User user, ObjectReference ref)
			throws RSuiteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public VersionHistory getVersionHistory(User user, String moid)
			throws RSuiteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getParentManagedObjectId(User user, String childMOId)
			throws RSuiteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getParentManagedObjectId(User user, ManagedObject childMO)
			throws RSuiteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getRootManagedObjectId(User user, String moid)
			throws RSuiteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ManagedObjectSummaryInfo> findAllRootManagedObjects(User user,
			String namespaceUri, String localName) throws RSuiteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addAlias(User user, String moid, String alias)
			throws RSuiteException {
		// TODO Auto-generated method stub

	}

	@Override
	public void addAlias(User user, String moid, Alias alias)
			throws RSuiteException {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteAlias(User user, String id, String alias)
			throws RSuiteException {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteAlias(User user, String id, Alias alias)
			throws RSuiteException {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteAllAliases(User user, String id) throws RSuiteException {
		// TODO Auto-generated method stub

	}

	@Override
	public ManagedObjectLoader constructManagedObjectLoader(User user)
			throws RSuiteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void checkOut(User user, String moid) throws RSuiteException {
		// TODO Auto-generated method stub

	}

	@Override
	public void checkOut(User user, String moid, ObjectCheckOutOptions options)
			throws RSuiteException {
		// TODO Auto-generated method stub

	}

	@Override
	public CheckOutInfo getCheckOutInfo(String id) throws RSuiteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void checkIn(User user, String moid, VersionType revisionType,
			String versionNote, boolean verifyIsCheckedOut)
			throws RSuiteException, ValidationException {
		// TODO Auto-generated method stub

	}

	@Override
	public void checkIn(User user, String id, ObjectCheckInOptions options)
			throws RSuiteException, ValidationException {
		// TODO Auto-generated method stub

	}

	@Override
	public ManagedObject deleteVariant(User user, String id,
			String variantName, ObjectUpdateOptions options)
			throws RSuiteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String addMetaDataEntry(User user, String moid,
			MetaDataItem metaDataItem, ObjectMetaDataSetOptions options)
			throws RSuiteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ManagedObject addMetaDataEntries(User user, String moid,
			List<MetaDataItem> entries) throws RSuiteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ManagedObject copy(User user, String id, ObjectCopyOptions options)
			throws RSuiteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ManagedObject copy(User user, VersionSpecifier versionSpecifier,
			ObjectCopyOptions options) throws RSuiteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void destroy(User user, String moid, ObjectDestroyOptions options)
			throws RSuiteException {
		// TODO Auto-generated method stub

	}

	@Override
	public String addDocType(String moid, Element root) throws RSuiteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public InputStream getBytes(User user, String moId) throws RSuiteException {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public ContentDisplayObject getContentDisplayObject(User arg0, String arg1)
			throws RSuiteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ContentDisplayObject getContentDisplayObject(User arg0,
			VersionSpecifier arg1) throws RSuiteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ContentDisplayObject getContentDisplayObject(User arg0, String arg1,
			String arg2) throws RSuiteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ContentDisplayObject getContentDisplayObject(User arg0,
			VersionSpecifier arg1, String arg2) throws RSuiteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ContentDisplayObject getContentDisplayObjectByAlias(User arg0,
			String arg1) throws RSuiteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ContentDisplayObject> getContentDisplayObjectsByAlias(
			User arg0, String arg1) throws RSuiteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ContentDisplayObject> getContentDisplayObjectsByAliasAndType(
			User arg0, String arg1, String arg2) throws RSuiteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ContentDisplayObject> getContentDisplayObjectsByAliasType(
			User arg0, String arg1) throws RSuiteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ImageMagickConfiguration getImageMagickConfiguration() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DependencyTracker getDependencyTracker() {
		DependencyTracker dependencyTracker = Mockito.mock(DependencyTracker.class);
		return dependencyTracker;
	}

	@Override
	public ImageConverter getImageConverter() throws RSuiteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ManagedObject getManagedObject(User user, String id)
			throws RSuiteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ManagedObject getManagedObject(User user,
			VersionSpecifier versionSpecifier) throws RSuiteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ManagedObjectTree getManagedElementTree(User user, String moid,
			Integer depth) throws RSuiteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ManagedObject getObjectByAlias(User user, String alias)
			throws RSuiteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ManagedObject> getObjectsByAlias(User user, String alias)
			throws RSuiteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public File getExternalAsset(User user, VariantDescriptor variant)
			throws RSuiteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SchemaContentStats getCountManagedObjectsForSchema(User user,
			String schemaId) throws RSuiteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ManagedObject> getObjectsByAliasAndType(User arg0, String arg1,
			String arg2) throws RSuiteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ManagedObject> getObjectsByAliasType(User arg0, String arg1)
			throws RSuiteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ManagedObject getParentManagedObject(User user, ManagedObject child)
			throws RSuiteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean hasChildren(User arg0, String arg1) throws RSuiteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean hasObject(User user, String moid) throws RSuiteException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Boolean hasSubcontainers(User arg0, String arg1)
			throws RSuiteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setAlias(User user, String moid, String alias)
			throws RSuiteException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setAlias(User user, String moid, Alias alias)
			throws RSuiteException {
		// TODO Auto-generated method stub

	}

	@Override
	public ManagedObject load(User user, ObjectSource src,
			ObjectInsertOptions options) throws RSuiteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isCheckedOutAuthor(User user, String moID)
			throws RSuiteException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isCheckedOut(User user, String moID) throws RSuiteException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isModified(User user, String id) throws RSuiteException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isCheckedOutButNotByUser(User user, String moID) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<CheckOutInfo> listCheckOutInfo(User principal, String byUserId)
			throws RSuiteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ManagedObject update(User user, String id, ObjectSource src,
			ObjectUpdateOptions options) throws RSuiteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ManagedObject updateVariant(User user, String id,
			String variantName, ObjectSource src, ObjectUpdateOptions options)
			throws RSuiteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setDisplayName(User user, String id, String displayName)
			throws RSuiteException {
		// TODO Auto-generated method stub

	}

	@Override
	public String setMetaDataEntry(User user, String moid,
			MetaDataItem metaDataItem) throws RSuiteException {
		
		ContentAssemblyMock ca =  ContentAssemblyServiceMock.mocksCaMap.get(moid);
		if (ca != null){
			ca.addMetadataItem(metaDataItem);
		}
		
		return null;
	}

	@Override
	public ManagedObject setMetaDataEntries(User user, String moid,
			Collection<MetaDataItem> entries) throws RSuiteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ManagedObject setMetaDataEntries(User user, String moid,
			List<MetaDataItem> entries) throws RSuiteException {
		
		ContentAssemblyMock ca =  ContentAssemblyServiceMock.mocksCaMap.get(moid);
		if (ca != null){
			ca.addMetadataItem(entries);
		}
		
		return null;
	}

	@Override
	public MetaDataItem updateMetaDataEntry(User user, String id,
			MetaDataItem item, ObjectMetaDataModifyOptions options)
			throws RSuiteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void processMetaDataChangeSet(User user, String id,
			MetaDataChangeSet changeSet) throws RSuiteException {
		// TODO Auto-generated method stub

	}

	@Override
	public ManagedObject moveUp(User user, String moid) throws RSuiteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ManagedObject moveDown(User user, String moid)
			throws RSuiteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] insertIntoXML(User user, String id, XmlObjectSource source,
			ObjectInsertIntoOptions options) throws RSuiteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void undoCheckout(User user, String id) throws RSuiteException {
		// TODO Auto-generated method stub

	}

	@Override
	public void undoCheckout(User user, String id, ObjectRollbackOptions options)
			throws RSuiteException {
		// TODO Auto-generated method stub

	}

	@Override
	public void makeCurrent(User user, VersionSpecifier versionSpecifier)
			throws RSuiteException {
		// TODO Auto-generated method stub

	}

	@Override
	public void rollback(User user, VersionSpecifier versionSpecifier,
			ObjectRollbackOptions options) throws RSuiteException {
		// TODO Auto-generated method stub

	}

	@Override
	public void purge(User user, String id, String note,
			PurgePolicy purgePolicy, ObjectPurgeOptions options)
			throws RSuiteException {
		// TODO Auto-generated method stub

	}

	@Override
	public void purge(User user, String id, String note, String[] revisions,
			ObjectPurgeOptions options) throws RSuiteException {
		// TODO Auto-generated method stub

	}

	@Override
	public ManagedObject refresh(User user, String id,
			ObjectRefreshOptions options) throws RSuiteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void refreshSearchView(User arg0, String arg1)
			throws RSuiteException {
		// TODO Auto-generated method stub

	}

	@Override
	public void refreshSearchView(User arg0, List<String> arg1)
			throws RSuiteException {
		// TODO Auto-generated method stub

	}

	@Override
	public void removeMetaDataEntry(User user, String id, MetaDataItem item)
			throws RSuiteException {
		// TODO Auto-generated method stub

	}

	@Override
	public void rollback(User user, String id) throws RSuiteException {
		// TODO Auto-generated method stub

	}

	@Override
	public void rollback(User user, String id, ObjectRollbackOptions options)
			throws RSuiteException {
		// TODO Auto-generated method stub

	}

	@Override
	public void rollback(User user, VersionSpecifier versionSpecifier)
			throws RSuiteException {
		// TODO Auto-generated method stub

	}

	@Override
	public void remove(User user, String moid) throws RSuiteException {
		// TODO Auto-generated method stub

	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	@Override
	public BrowseInfo getChildManagedObjects(User arg0, String arg1, int arg2,
			int arg3) throws RSuiteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BrowseInfo getChildrenDisplayObjects(User arg0, String arg1,
			String arg2, int arg3, int arg4) throws RSuiteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ContentDisplayObject> getContainerDisplayObjects(User arg0,
			String arg1, String arg2) throws RSuiteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, ContentDisplayObject> getContentDisplayObjects(
			User arg0, List<String> arg1) throws RSuiteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<VersionSpecifier, ManagedObject> getManagedObjects(User arg0,
			List<VersionSpecifier> arg1) throws RSuiteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<VersionSpecifier, ManagedObject> getManagedObjects(User arg0,
			List<VersionSpecifier> arg1, boolean arg2, boolean arg3)
			throws RSuiteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean isFirstChild(User arg0, String arg1) throws RSuiteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean isLastChild(User arg0, String arg1) throws RSuiteException {
		// TODO Auto-generated method stub
		return null;
	}

}
