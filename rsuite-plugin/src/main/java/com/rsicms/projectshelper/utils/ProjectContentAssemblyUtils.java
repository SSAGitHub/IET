package com.rsicms.projectshelper.utils;

import java.io.*;
import java.text.MessageFormat;
import java.util.*;
import java.util.zip.*;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;

import com.reallysi.rsuite.api.*;
import com.reallysi.rsuite.api.control.*;
import com.reallysi.rsuite.api.extensions.ExecutionContext;
import com.reallysi.rsuite.service.*;
import com.rsicms.projectshelper.datatype.RSuiteCaType;
import com.rsicms.projectshelper.utils.visitor.ListReferencedContentContainerVisitor;
import com.rsicms.rsuite.helpers.utils.*;

public class ProjectContentAssemblyUtils {

    private ProjectContentAssemblyUtils(){
    }
    
	public static String getPaterntId (ExecutionContext context, User user, String moId) throws RSuiteException {
		ContentAssemblyService caSvc = context.getContentAssemblyService();
		ManagedObjectService moSvc = context.getManagedObjectService();
		DependencyTracker tracker = moSvc.getDependencyTracker();
		List<ReferenceInfo> refList = tracker.listDirectReferences(user, moId);
		
		if (refList.size() == 0){
			throw new RSuiteException("Unable to localize parent. There are no references for " + moId);
		}
		
		ReferenceInfo ref = refList.get(0);
		String browseUri = ref.getParentBrowseUri();
		String[] parents = browseUri.split("\\/");
		if (parents.length == 1)
			return null;
		String refCaId = parents[parents.length - 1].split(":")[1];		
		return ((ContentAssemblyReference)caSvc.getContentAssemblyItem(user, refCaId)).getTargetId();
	}
	
	public static ContentAssemblyItem getAncestorCAbyTypes(ExecutionContext context, String caId, Set<String> caTypes) throws RSuiteException {
		User user = context.getAuthorizationService().getSystemUser();
		ContentAssemblyItem ancestorCA = getCAItem(context, user, caId);
		
		if ((ancestorCA == null) || (!caTypes.contains(ancestorCA.getType()))) {
			String parentId = getPaterntId(context, user, caId);
			if (parentId != null) {				
				ancestorCA = getAncestorCAbyTypes(context, parentId, caTypes);
			} else {
				return null;
			}
		}
		
		return ancestorCA;
	}

	public static String getLMDFromCATypeAncestor(ExecutionContext context, String childMoId, String caType, String lmdName) throws RSuiteException {
		ManagedObject mo = context.getManagedObjectService().getManagedObject(context.getAuthorizationService().getSystemUser(), childMoId);
		String moId = childMoId;
		
		if (mo.getObjectType() == ObjectType.MANAGED_OBJECT_REF) {
			moId = mo.getTargetId();
		}

		String bookPrefix = ProjectContentAssemblyUtils.getAncestorCAbyType(context, moId, caType).getLayeredMetadataValue(lmdName);

		return bookPrefix;
	}

	   public static ContentAssemblyItem getAncestorCAbyType (ExecutionContext context, String caId, RSuiteCaType caType) throws RSuiteException {
	        Set<String> caTypes = new HashSet<String>();
	        caTypes.add(caType.getTypeName());
	        return getAncestorCAbyTypes(context, caId, caTypes);
	    }
	
	public static ContentAssemblyItem getAncestorCAbyType (ExecutionContext context, String caId, String caType) throws RSuiteException {
		Set<String> caTypes = new HashSet<String>();
		caTypes.add(caType);
		return getAncestorCAbyTypes(context, caId, caTypes);
	}
	
	public static ContentAssemblyItem getCAItem (ExecutionContext context, User user, String caId) throws RSuiteException {
		ManagedObjectService moServ = context.getManagedObjectService();
		ContentAssemblyService caServ = context.getContentAssemblyService();
		ManagedObject managedObject = moServ.getManagedObject(user, caId);
		ContentAssemblyItem caItem = null;
						
		if (managedObject.getObjectType() == ObjectType.CONTENT_ASSEMBLY ||
		    managedObject.getObjectType() == ObjectType.CONTENT_ASSEMBLY_REF) {
			caItem = getCAFromMO(context, user, caId);
		} else  if (managedObject.getObjectType() == ObjectType.CONTENT_ASSEMBLY_NODE) {
			caItem = caServ.getCANode(user, managedObject.getId());
		}

		return caItem;
	}
	
	public static ContentAssembly getCAFromMO (ExecutionContext context, User user, String moId) throws RSuiteException {
		ManagedObjectService moServ = context.getManagedObjectService();
		ContentAssemblyService caServ = context.getContentAssemblyService();
		ManagedObject managedObject = moServ.getManagedObject(user, moId);
		ContentAssembly ca = null;
						
		if (managedObject.getObjectType() == ObjectType.CONTENT_ASSEMBLY) {
			ca = caServ.getContentAssembly(user, managedObject.getId());	
		} else  if (managedObject.getObjectType() == ObjectType.CONTENT_ASSEMBLY_REF) {
			ca = caServ.getContentAssembly(user, managedObject.getTargetId());
		}

		return ca;
	}
	
	public static String getParentRef (ExecutionContext context, String containerId) throws RSuiteException {
		int last = 2;
		return getCaRefId(context, containerId, last);
	}
	
	public static String getSelfRefId (ExecutionContext context, String containerId) throws RSuiteException {
		int last = 1;
		return getCaRefId(context, containerId, last);
	}
	
	private static String getCaRefId (ExecutionContext context, String containerId, int position) throws RSuiteException {
		User systemUser = context.getAuthorizationService().getSystemUser();
		ManagedObjectService moServ = context.getManagedObjectService();

		List<ReferenceInfo> refernces = moServ.getDependencyTracker()
				.listAllReferences(systemUser, containerId);

		String ref = null;
		for (ReferenceInfo refernceInfo : refernces) {
			String browserUri = refernceInfo.getBrowseUri();
			String[] uriParts = browserUri.split("/");

			if (uriParts.length > 1) {	
				String parentPart = uriParts[uriParts.length-position];
				ref = parentPart.split(":")[1];
			}
		}

		return ref;
	}
	
	public static String getCaType (ExecutionContext context, String caId) throws RSuiteException {
		User user = context.getAuthorizationService().getSystemUser();
		ContentAssemblyItem caItem = getCAItem(context, user, caId);
		
		return caItem.getType();
	}
	
	public static String getCAURI (ExecutionContext context, String caId) throws RSuiteException {
		String caURI = null;
		User user = context.getAuthorizationService().getSystemUser();
		ManagedObjectService moSvc = context.getManagedObjectService();
		DependencyTracker tracker = moSvc.getDependencyTracker();		
		List<ReferenceInfo> refList = tracker.listDirectReferences(user, caId);
		if (refList.size() > 0) {
			ReferenceInfo ref = refList.get(0);
			caURI = ref.getParentBrowseUri();
		}
		
		return caURI;
	}
	
	public static ByteArrayOutputStream zipContentAssembly (ExecutionContext context, ContentAssembly ca, boolean removeRSuiteAttributes) throws RSuiteException {
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		ZipOutputStream zip = new ZipOutputStream(outStream);
		try {
			List<? extends ContentAssemblyItem> children = ca.getChildrenObjects();
			for (ContentAssemblyItem caItem : children){
				visitCAItem(caItem, context, "", zip, null, true);
			}
		} catch (IOException ex) {
			throw new RSuiteException(0, "Unable to archive CA" + ca.getId() + ". " + ex.getLocalizedMessage(), ex);
		} finally {
			ZipUtil.closeZipStream(zip);
		}
		
		return outStream;
	}
	
	private static void visitCAItem(ContentAssemblyItem caItem,
			ExecutionContext context, String path,
			ZipOutputStream zip, ZipFilter filter,
			boolean removeRSuiteAttributes) throws RSuiteException,
			IOException {
		
		if (caItem.getObjectType() == ObjectType.CONTENT_ASSEMBLY_REF){
			caItem = getCAItem(context, context.getAuthorizationService().getSystemUser(), caItem.getId());
		}
		
		if ((filter != null)
				&& (!(filter.archiveContentAssemblyItem(caItem,
						context)))) {
			return;
		}

		if (RSuiteUtils.ifContentAssemblyItemIsMo(caItem)) {
			User user = context.getAuthorizationService()
					.getSystemUser();
			ManagedObject mo = context.getManagedObjectService()
					.getManagedObject(user, caItem.getId());
			addMoToZip(mo, context, path, zip, removeRSuiteAttributes);
		} else {
			path = path + caItem.getDisplayName() + "/";
			List<? extends ContentAssemblyItem> children = caItem.getChildrenObjects();
			addDirectoryToZip(path, zip, children.size() == 0);

			for (ContentAssemblyItem childObject : children)
				visitCAItem(childObject, context, path, zip,
						filter, removeRSuiteAttributes);
		}
	}
	
	private static void addMoToZip(ManagedObject mo,
			ExecutionContext context, String path,
			ZipOutputStream zip, boolean removeRSuiteAttributes)
			throws RSuiteException, IOException {
		User user = context.getAuthorizationService().getSystemUser();
		ManagedObjectService moService = context
				.getManagedObjectService();

		String id = mo.getId();
		if (!(mo.isOriginalMO())) {
			id = mo.getTargetId();
			mo = moService.getManagedObject(user, id);
		}

		InputStream inStream = context.getManagedObjectService()
				.getBytes(user, id);

		if ((removeRSuiteAttributes) && (!(mo.isNonXml()))) {
			inStream = RsuiteXMLUtils
					.removeAttributesFromRSuiteNamespace(inStream);
		}

		path = path + mo.getDisplayName();
		addStreamToZip(path, inStream, zip);
	}	

	private static void addStreamToZip(String path, InputStream stream,
			ZipOutputStream zip) throws IOException {

		byte[] buf = new byte[1024];
		int len;

		zip.putNextEntry(new ZipEntry(path));

		while ((len = stream.read(buf)) >= 0) {
			zip.write(buf, 0, len);
		}
		stream.close();
		zip.closeEntry();

	}
	
	private static void addDirectoryToZip(String path, ZipOutputStream zip,
			boolean closeEntry) throws IOException {
		ZipEntry entry = new ZipEntry(path);
		zip.putNextEntry(entry);

		if (closeEntry)
			zip.closeEntry();
	}

	 public static ManagedObject copyAndAttach(ExecutionContext context,
			    User user,
			    VersionSpecifier moSpecifier,
			    String caId,
			    ObjectCopyOptions copyOptions,
			    ObjectAttachOptions attachOptions)
			      throws RSuiteException
			  {
			    // Verify EDIT permission
			    if (!context.getSecurityService().hasEditPermission(user, caId))
			      throw new RSuiteException(
			        context.getMessageResources().getMessageText("ca.nocreatepermissionincaid", caId));
			    
			    ManagedObjectService moSvc = context.getManagedObjectService();
			    ContentAssemblyService caSvc = context.getContentAssemblyService();
			    
			    if (copyOptions == null)
			      copyOptions = new ObjectCopyOptions();
			    copyOptions.setContentAssembly(caSvc.getContentAssembly(user, caId));
			    
			    
			    ManagedObject newMo 
			      = moSvc.copy(user, moSpecifier, copyOptions);
			    
			    return caSvc.attach(user, caId, newMo, null, attachOptions);
			  }

	public static void moveRefToFistPossition(ExecutionContext context, String moId, String parentId) throws RSuiteException {
		ContentAssemblyService caSvc = context.getContentAssemblyService();
		User user = context.getAuthorizationService().getSystemUser();

		ObjectReferenceMoveOptions refMoveOptions = new ObjectReferenceMoveOptions();
		int fistChildNodePosition = 0;
		refMoveOptions.setPosition(fistChildNodePosition);
		caSvc.moveReference(user, ProjectContentAssemblyUtils.getSelfRefId(context, moId), parentId, refMoveOptions);		
	}

    public static ContentAssembly createContentAssembly(ExecutionContext context, String parentId, String displayName, String type) throws RSuiteException{
    	User user = context.getAuthorizationService().getSystemUser();
    	ContentAssemblyService caService = context.getContentAssemblyService();
    	ContentAssemblyCreateOptions options = new ContentAssemblyCreateOptions();
    	if (StringUtils.isNotBlank(type)){
    		options.setType(type);
    	}
    	
    	String noramlizedName = normalizeContentAssemblyName(displayName);
    	return caService.createContentAssembly(user, parentId, noramlizedName ,options);
    }

    private static String normalizeContentAssemblyName(String displayName){
    	String normalizedName = displayName.replace('#', '-');
    	normalizedName = normalizedName.replace('/', '-');
    	normalizedName = normalizedName.replace('\\', '-');
    	return normalizedName;
    }
    
    public static ContentAssembly createContentAssembly(ExecutionContext context, String parentId, String displayName, RSuiteCaType type) throws RSuiteException{
        return createContentAssembly(context, parentId, displayName, type.getTypeName());
    }

    public static ContentAssembly createContentAssembly(ExecutionContext context, String parentId, String displayName) throws RSuiteException{
        return createContentAssembly(context, parentId, displayName, "");
    }

    /**
	 * DANGER: This method permanently deletes the given container and EVERYTHING it references, even if also referenced by other containers.
	 * <p>
	 * This implementation minimizes the number of database writes by deleting containers before MOs.
	 * <p>
	 * There is no code within that explicitly deals with CANodes as those are deleted with the CAs.
	 * <p>
	 * Error handling could be added in an attempt to continue despite having an issue with one object.
	 * 
	 * @param context
	 * @param user The user to operate as.
	 * @param container The container to permanently deleted, as well as all content it references.
	 * @throws RSuiteException Thrown if unable to complete the operation successfully.  A possible outcome
	 * 		is that some of the objects were destroyed, but not all.
	 */
	public static void deleteContainerAndReferencedContent(
			ExecutionContext context,
			User user,
			ContentAssemblyNodeContainer container,
			Log log) 
	throws RSuiteException {
		
		log.info("MarkStartOfOperation");
		
		// Visit the container
		ListReferencedContentContainerVisitor visitor = 
			new ListReferencedContentContainerVisitor(
				context,
				user);
		visitor.visitContentAssemblyNodeContainer(container);
		
		ManagedObjectService moService = context.getManagedObjectService();
		ObjectDestroyOptions options = new ObjectDestroyOptions();

		// 1st: delete the starting container.
		if (visitor.getStartingContainer() != null) {
			String message = "User with ID '{0}' is deleting '{1}' (ID: {2})";
			log.info(MessageFormat.format(message, 
					user.getUserId(), 
					visitor.getStartingContainer().getDisplayName(), 
					visitor.getStartingContainer().getId()));

			deleteContainer(context, user, visitor.getStartingContainer(), log);
		}
		
		/*
		 * 2nd: delete the CAs, in the list's order.  Those that were directly referenced
		 * by the starting container are no longer referenced by it.  The idea is that
		 * those sooner in the list may reference those later in the list (and not the 
		 * other way around).
		 */
		if (visitor.getReferencedContentAssemblies() != null) {
			for (ContentAssembly ca : visitor.getReferencedContentAssemblies()) {
				String message = "User with ID '{0}' is deleting '{1}' (ID: {2})";
				log.info(MessageFormat.format(message, 
						user.getUserId(), 
						ca.getDisplayName(), 
						ca.getId()));

				deleteContainer(context, user, ca, log);
			}
		}
		
		/*
		 * 3rd: delete the MOs, which are no longer referenced by the containers
		 * deleted above.
		 */
		if (visitor.getReferencedManagedObjects() != null) {
			for (ManagedObject mo : visitor.getReferencedManagedObjects()) {
				String message = "User with ID '{0}' is deleting '{1}' (ID: {2})";
				log.info(MessageFormat.format(message, 
						user.getUserId(), 
						mo.getDisplayName(), 
						mo.getId()));

				try {
					moService.checkOut(user, mo.getId());
					moService.destroy(user, mo.getId(), options);
				} catch (RSuiteException e) {
					log.warn(e);
				}
			}
		}

		for (ContentAssembly contentAssembly: visitor.getReferencedContentAssemblies()) {
			log.info("ContentAssembly deleted: " + contentAssembly.getDisplayName() + "[ID: " + contentAssembly.getId() + "]");
		}

		for (ManagedObject managedObject: visitor.getReferencedManagedObjects()) {
			log.info("ManagedObject deleted: " + managedObject.getDisplayName() + "[ID: " + managedObject.getId() + "]");
		}

		log.info("MarkEndOfOperation");
	}

	/**
	 * Delete a content assembly node container, regardless of it being a CA or CANode.
	 * <p>
	 * This method does not delete any content the container references.
	 * 
	 * @param context
	 * @param user
	 * @param container
	 * @param log
	 * @throws RSuiteException
	 */
	public static void deleteContainer(
			ExecutionContext context,
			User user,
			ContentAssemblyNodeContainer container,
			Log log) throws RSuiteException {
		if (container instanceof ContentAssembly) {
			context.getContentAssemblyService().removeContentAssembly(user, container.getId());
		} else {
			context.getContentAssemblyService().deleteCANode(user, container.getId());
		}
	}
}
