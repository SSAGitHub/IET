package com.rsicms.projectshelper.utils.browse;

import java.util.*;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;

import com.reallysi.rsuite.api.*;
import com.reallysi.rsuite.api.control.ContentAssemblyItemFilter;
import com.reallysi.rsuite.api.extensions.ExecutionContext;
import com.reallysi.rsuite.api.vfs.BrowsePath;
import com.reallysi.rsuite.api.vfs.BrowseTreeNode;
import com.reallysi.rsuite.service.ContentAssemblyService;
import com.reallysi.rsuite.service.ManagedObjectService;
import com.rsicms.projectshelper.datatype.RSuiteCaType;
import com.rsicms.projectshelper.utils.browse.filters.*;
import com.rsicms.rsuite.helpers.utils.MoUtils;

public final class ProjectBrowserUtils {

    private ProjectBrowserUtils(){};
    
	public static String getParentIdFromPath(String path){
		String[] pathParts = path.split("/");
		if (pathParts.length > 1){
			String pathPart = pathParts[pathParts.length -2];
			return pathPart.split(":")[1];
		}
		
		return null;
	}

	
	/**
	 * Finds CA ancestor with given caType
	 * 
	 * @param context
	 *            the execution context
	 * @param path
	 *            the BrowserPath
	 * @param caType
	 *            caType
	 * @return Content Assembly if found otherwise null.
	 * @throws RSuiteException
	 */
	public static ContentAssemblyItem getAncestorCAbyType(
			ExecutionContext context, String path, Set<String> caType)
			throws RSuiteException {

		User user = context.getAuthorizationService().getSystemUser();
		ContentAssemblyService caService = context.getContentAssemblyService();

		String[] nodes = path.split("/");
		

		for (int i = nodes.length - 1; i >= 0; i--) {
			
			String node = nodes[i];

			ContentAssemblyItem caItem = getCaItem(user, caService, node);

			if (caItem != null && caType.contains(caItem.getType())) {
					return caItem;
			}
		}

		return null;
	}

	private static ContentAssemblyItem getCaItem(User user,
			ContentAssemblyService caService, String node)
			throws RSuiteException {
		String[] nodeInfo = node.split(":");

		String noideId = nodeInfo[1];

		ObjectType nodeType = getObjectTypeFromString(nodeInfo[0]);

		ContentAssemblyItem caItem = null;
		if (nodeType == ObjectType.CONTENT_ASSEMBLY_REF) {

			ContentAssemblyItem caRef = caService.getContentAssemblyItem(
					user, noideId);
			String caId = ((ContentAssemblyReference) caRef).getTargetId();
			caItem = caService.getContentAssembly(user, caId);

		} else if (nodeType == ObjectType.CONTENT_ASSEMBLY) {
			caItem = caService.getContentAssembly(user, noideId);
		} else if (nodeType == ObjectType.CONTENT_ASSEMBLY_NODE) {
			caItem = caService.getCANode(user, noideId);
		}
		return caItem;
	}

	
	   public static ContentAssembly getAncestorCAbyType(
	            ExecutionContext context, User user, String path, RSuiteCaType caType)
	            throws RSuiteException {
	       
	       ContentAssemblyItem ancestor = getAncestorCAbyType(context, path, caType.getTypeName());
	       
	       if (ancestor != null){
	           return context.getContentAssemblyService().getContentAssembly(user, ancestor.getId());
	       }
	       
	       return null; 
	   }
	
	/**
	 * Finds CA ancestor with given caType
	 * 
	 * @param context
	 *            the execution context
	 * @param path
	 *            the BrowserPath
	 * @param caType
	 *            caType
	 * @return Content Assembly if found otherwise null.
	 * @throws RSuiteException
	 */
	public static ContentAssemblyItem getAncestorCAbyType(
			ExecutionContext context, String path, String caType)
			throws RSuiteException {

		Set<String> types = new HashSet<String>();
		types.add(caType);
		return getAncestorCAbyType(context, path, types);
	}

	public static String getBrowserUri (ExecutionContext context, String moId) throws RSuiteException {
		User systemUser = context.getAuthorizationService().getSystemUser();
		ManagedObjectService moServ = context.getManagedObjectService();
		List<ReferenceInfo> refernces = moServ.getDependencyTracker()
				.listAllReferences(systemUser, moId);
		
		if (refernces.size() > 0) {
			ReferenceInfo refernceInfo = refernces.get(0);
			return refernceInfo.getBrowseUri();
		}
		
		return null;
	}
	
	public static ObjectType getObjectTypeFromString(String type)
			throws RSuiteException {

		if ("ca".equals(type)) {
			return ObjectType.CONTENT_ASSEMBLY;
		} else if ("caref".equalsIgnoreCase(type)) {
			return ObjectType.CONTENT_ASSEMBLY_REF;
		} else if ("moref".equalsIgnoreCase(type)) {
			return ObjectType.MANAGED_OBJECT_REF;
		} else if ("mo".equalsIgnoreCase(type)) {
			return ObjectType.MANAGED_OBJECT;
		} else if ("canode".equalsIgnoreCase(type)) {
			return ObjectType.CONTENT_ASSEMBLY_NODE;
		}

		throw new RSuiteException("Unable to parse type " + type);
	}
	
	/**
	 * Finds CA ancestor with given caType
	 * 
	 * @param context
	 *            the execution context
	 * @param path
	 *            the BrowserPath
	 * @param caType
	 *            caType
	 * @return Content Assembly if found otherwise null.
	 * @throws RSuiteException
	 */
	public static ContentAssemblyItem getAncestorCAbyType(
			ExecutionContext context, BrowsePath path, String caType)
			throws RSuiteException {

		User user = context.getAuthorizationService().getSystemUser();
		ContentAssemblyService caService = context.getContentAssemblyService();

		List<BrowseTreeNode> nodes = path.getNodes();

		for (int i = nodes.size() - 1; i >= 0; i--) {
			ContentAssemblyItem caItem = null;
			BrowseTreeNode node = nodes.get(i);

			if (node.getType() == ObjectType.CONTENT_ASSEMBLY_REF) {

				ContentAssemblyItem caRef = caService.getContentAssemblyItem(
						user, node.getNodeId());
				String caId = ((ContentAssemblyReference) caRef).getTargetId();
				caItem = caService.getContentAssembly(user, caId);

			} else if (node.getType() == ObjectType.CONTENT_ASSEMBLY) {
				caItem = caService.getContentAssembly(user, node.getNodeId());
			} else if (node.getType() == ObjectType.CONTENT_ASSEMBLY_NODE) {
				caItem = caService.getCANode(user, node.getNodeId());
			}

			if (caItem != null) {
				if (StringUtils.equals(caType, caItem.getType())) {
					return caItem;
				}
			}
		}

		return null;
	}

	/**
     * Finds content assembly with given type
     * 
     * @param context
     *            the execution context
     * @param contextCa
     *            the context CA
     * @param caType
     *            caType
     * @return First found Content Assembly otherwise null.
     * @throws RSuiteException
     */
    public static ContentAssembly getChildCaByType(ExecutionContext context,
            ContentAssemblyItem contextCa, RSuiteCaType caType) throws RSuiteException {

        return getChildCaByType(context, contextCa, caType.getTypeName());
    }
	
	/**
	 * Finds content assembly with given type
	 * 
	 * @param context
	 *            the execution context
	 * @param contextCa
	 *            the context CA
	 * @param caType
	 *            caType
	 * @return First found Content Assembly otherwise null.
	 * @throws RSuiteException
	 */
	public static ContentAssembly getChildCaByType(ExecutionContext context,
			ContentAssemblyItem contextCa, String caType) throws RSuiteException {

		List<ContentAssembly> caList = getChildrenCaByType(context, contextCa,
				caType);
		return getFirstResult(caList);
	}

	/**
	 * Gets first content assembly from the list
	 * 
	 * @param resultList
	 *            the CA list
	 * @return first content assembly from the list or null if list is empty
	 */
	private static ContentAssembly getFirstResult(
			List<ContentAssembly> resultList) {
		if (resultList.size() > 0){
			//get first
			return resultList.get(0);
		}
		
		return null;
	}
	
	public static List<ContentAssembly> getChildrenCaByType(
			ExecutionContext context, ContentAssemblyItem contextCa,
			RSuiteCaType caType) throws RSuiteException {
		return getChildrenCaByType(context, contextCa, caType.getTypeName());
	}
	
	/**
	 * Finds child CA by caType
	 * 
	 * @param context
	 *            the execution context
	 * @param contextCa
	 *            the context CA
	 * @param caType
	 *            caType
	 * @return Content Assembly if found otherwise null.
	 * @throws RSuiteException
	 */
	public static List<ContentAssembly> getChildrenCaByType(
			ExecutionContext context, ContentAssemblyItem contextCa,
			final String caType) throws RSuiteException {

		ChildFilter filter = new ChildFilter() {			
			@Override
			public boolean accept(ContentAssembly ca) {
				if (StringUtils.equals(caType, ca.getType())) {
					return true;
				}
				return false;
			}
		};
		
		return  getChildrenByFilter(context, contextCa, filter, false);		
	}
	
	/**
	 * Finds child CA by caType
	 * 
	 * @param context
	 *            the execution context
	 * @param contextCa
	 *            the context CA
	 * @param caType
	 *            caType
	 * @return Content Assembly if found otherwise null.
	 * @throws RSuiteException
	 */
	public static List<ContentAssembly> getChildrenCaLMD(
			ExecutionContext context, ContentAssembly contextCa,
			final String lmdName, final String lmdValue) throws RSuiteException {

		ChildFilter filter = new ChildFilter() {			
			@Override
			public boolean accept(ContentAssembly ca) throws RSuiteException {
				
				if (StringUtils.equals(lmdValue,
						ca.getLayeredMetadataValue(lmdName))) {
					return true;
				}
				return false;
			}
		};
		
		return  getChildrenByFilter(context, contextCa, filter, false);		
	}
	
	/**
	 * Finds child MO by local name
	 * 
	 * @param context
	 *            the execution context
	 * @param contextCa
	 *            the context CA
	 * @param caType
	 *            caType
	 * @return Content Assembly if found otherwise null.
	 * @throws RSuiteException
	 */
	public static ContentAssembly getChildCaByDisplayName(
			ExecutionContext context, ContentAssembly contextCa,
			final String displaName) throws RSuiteException {

		ChildFilter filter = new ChildFilter() {			
			@Override
			public boolean accept(ContentAssembly ca) {
				if (ca.getDisplayName().equals(displaName)) {
					return true;
				}
				return false;
			}
		};
		
		return getFirstResult(getChildrenByFilter(context, contextCa, filter,
				true));
	}
	
	/**
	 * Gets content assembly
	 * 
	 * @param context
	 *            The execution service
	 * @param user
	 *            The user object
	 * @param id
	 *            CA or CAref id
	 * @return Content Assembly or null if CA with given id doesn't exist
	 * @throws RSuiteException
	 */
	public static ContentAssembly getContentAssembly(ExecutionContext context,
			User user, ContentAssemblyItem caItem) throws RSuiteException {
		
		if (caItem == null){
			return null;
		}
		
		if (caItem instanceof ContentAssembly){
			return (ContentAssembly)caItem;
		}
		
		return getContentAssembly(context, user, caItem.getId());
	}
	
	public static ContentAssembly getContentAssembly(ExecutionContext context,
			 String id) throws RSuiteException {
		User user = context.getAuthorizationService().getSystemUser();
		return getContentAssembly(context, user, id);
	}
	
	/**
	 * Gets content assembly
	 * 
	 * @param context
	 *            The execution service
	 * @param user
	 *            The user object
	 * @param id
	 *            CA or CAref id
	 * @return Content Assembly or null if CA with given id doesn't exist
	 * @throws RSuiteException
	 */
	public static ContentAssembly getContentAssembly(ExecutionContext context,
			User user, String id) throws RSuiteException {
		
		ManagedObjectService moService = context.getManagedObjectService();	
		ManagedObject mo = moService.getManagedObject(user, id);
		
		
		if (mo == null){
			throw new RSuiteException("Object with id " + id + " doesn't exist");
		}
		
		
		ContentAssemblyService caService = context.getContentAssemblyService();
		
		if (mo.getObjectType() == ObjectType.CONTENT_ASSEMBLY){
			return caService.getContentAssembly(user, id);
		}
		
		if (mo.getObjectType() == ObjectType.CONTENT_ASSEMBLY_REF){
			return caService.getContentAssembly(user, mo.getTargetId());
		}
		
		throw new RSuiteException("There is no content assembly with id "  + id);
	}
		
	/**
	 * Finds child CA by type and display name
	 * 
	 * @param context
	 *            execution context
	 * @param contextCa
	 *            the context content CA
	 * @param caType
	 *            the ca type to find
	 * @param displayName
	 *            the display name to find
	 * @return Content Assembly if found otherwise null.
	 * @throws RSuiteException
	 */
	public static ContentAssembly getChildCaByNameAndType(
			ExecutionContext context, ContentAssembly contextCa,
			final String caType, final String displayName)
			throws RSuiteException {

		ChildFilter filter = new ChildFilter() {			
			@Override
			public boolean accept(ContentAssembly ca) {
				if (StringUtils.equals(caType, ca.getType())
						&& ca.getDisplayName().equals(displayName)) {
					return true;
				}
				return false;
			}
		};
		
		List<ContentAssembly> resultList = getChildrenByFilter(context,
				contextCa, filter, true);
		return getFirstResult(resultList);
	}
	
	/**
	 * Finds child CA by type and display name
	 * 
	 * @param context
	 *            execution context
	 * @param contextCa
	 *            the context content CA
	 * @param caType
	 *            the ca type to find
	 * @param displayName
	 *            the display name to find
	 * @return Content Assembly if found otherwise null.
	 * @throws RSuiteException
	 */
	public static List<ContentAssembly> getChildrenByFilter(
			ExecutionContext context, ContentAssemblyItem contextCa,
			ChildFilter filter, boolean firstOnly) throws RSuiteException {

		User user = context.getAuthorizationService().getSystemUser();
		ContentAssemblyService caService = context.getContentAssemblyService();

		List<? extends ContentAssemblyItem> nodes = contextCa
				.getChildrenObjects();
		

		List<ContentAssembly> resultList = new ArrayList<ContentAssembly>();

		for (int i = 0; i < nodes.size(); i++) {

			ContentAssembly ca = null;

			ContentAssemblyItem caItem = nodes.get(i);

			if (caItem.getObjectType() == ObjectType.CONTENT_ASSEMBLY_REF) {

				String caId = ((ContentAssemblyReference) caItem).getTargetId();
				ca = caService.getContentAssembly(user, caId);

			} else if (caItem.getObjectType() == ObjectType.CONTENT_ASSEMBLY) {
				ca = caService.getContentAssembly(user, caItem.getId());
			}

			if (ca != null && filter.accept(ca)) {
				resultList.add(ca);
				
				if (firstOnly){
					break;
				}
			}
		}

		return resultList;
	}
	
	/**
	 * Finds child MO by node name
	 * 
	 * @param context
	 *            the execution context
	 * @param path
	 *            the BrowserPath
	 * @param nodeName
	 *            caType
	 * @return Content Assembly if found otherwise null.
	 * @throws RSuiteException
	 */
	public static ManagedObject getChildMoByNodeName(ExecutionContext context,
			ContentAssembly contextCa, final String... nodeName) throws RSuiteException {

	    ChildMoFilter nodeNameFilter = new NodeNameChildMoFilter(nodeName);
		
		return getChildMo(context, contextCa, nodeNameFilter);
	}
	
	public static ManagedObject getChildMoLmd(ExecutionContext context,
			ContentAssembly contextCa, final String lmdName, final String lmdValue) throws RSuiteException {
		
		ChildMoFilter lmdFilter = new ChildMoFilter() {
			
			@Override
			public boolean accept(ManagedObject mo) throws RSuiteException {
				if (StringUtils.equals(lmdValue,
						mo.getLayeredMetadataValue(lmdName))) {
					return true;
				}
				return false;
			}
		};
	
		return getChildMo(context, contextCa, lmdFilter);
	}
	
	public static ManagedObject getChildMo(ExecutionContext context,
			ContentAssembly contextCa, ChildMoFilter childMoFilter) throws RSuiteException {

		 List<ManagedObject> childMos = getChildMos(context, contextCa, childMoFilter, true, true);
		 
		 if(childMos.size() > 0){
		     return childMos.get(0);
		 }
		
		return null;
	}
	
   public static ManagedObject getSourceChildMo(ExecutionContext context,
	            ContentAssembly contextCa, ChildMoFilter childMoFilter) throws RSuiteException {

	         List<ManagedObject> childMos = getChildMos(context, contextCa, childMoFilter, true, false);
	         
	         if(childMos.size() > 0){
	             return childMos.get(0);
	         }
	        
	        return null;
	    }

	public static List<ManagedObject> getChildMos(ExecutionContext context,
	            ContentAssembly contextCa, ChildMoFilter childMoFilter) throws RSuiteException {
	       return getChildMos(context, contextCa, childMoFilter, false, true);
	}
	
	private static List<ManagedObject> getChildMos(ExecutionContext context,
            ContentAssembly contextCa, ChildMoFilter childMoFilter, boolean returnFirstOnly, boolean realMo) throws RSuiteException {

        User user = context.getAuthorizationService().getSystemUser();
        ManagedObjectService moService = context.getManagedObjectService();

        List<? extends ContentAssemblyItem> nodes = contextCa
                .getChildrenObjects();
        
        List<ManagedObject> childMos = new ArrayList<ManagedObject>();
        
        for (int i = nodes.size() - 1; i >= 0; i--) {
            ContentAssemblyItem caItem = nodes.get(i);
            ManagedObject mo = null;
            
            
            if (caItem.getObjectType() == ObjectType.MANAGED_OBJECT_REF) {

                String caId = ((ManagedObjectReference) caItem).getId();
                if (realMo){
                    caId = ((ManagedObjectReference) caItem).getTargetId();
                    
                }
                                
                mo = moService.getManagedObject(user, caId);

            } else if (caItem.getObjectType() == ObjectType.MANAGED_OBJECT) {
                mo = moService.getManagedObject(user, caItem.getId());
                
            }
            
            if (mo == null){
                continue;
            }
            
            ManagedObject moToFilter = mo;
            
            if (StringUtils.isNotBlank(mo.getTargetId())){
                moToFilter = MoUtils.getRealMo(context, mo.getId());
            }

            if (childMoFilter.accept(moToFilter)) {
                childMos.add(mo);
                
                if (returnFirstOnly){
                    break;
                }
            }
        }

        return childMos;
    }
	
	/**
	 * Build a map of refIds indexed by caId.
	 *
	 * @param log
	 * @param user
	 * @param caSvc
	 * @param ca
	 *            Content Assembly for child list
	 * @return the refIdMap
	 * @throws RSuiteException
	 */
	public static Map<String, String> getCaRefIdMap(User user,
			ContentAssemblyService caSvc, ContentAssembly ca)
			throws RSuiteException {
		Map<String, String> refIdMap = new HashMap<String, String>();		
		List<? extends ContentAssemblyItem> childList = ca.getChildrenObjects();
		for (ContentAssemblyItem item: childList) {
			if (item.getObjectType() == ObjectType.CONTENT_ASSEMBLY_REF) {
				String refId = item.getId();
				ContentAssemblyReference caRef = (ContentAssemblyReference)item;
				String caId = caRef.getTargetId();
				refIdMap.put(caId, refId);
			}
		}
		return refIdMap;
		
	}
	
	/**
	 * Gets first reference (content assembly item for given ca in given contanier - context ca;
	 * @param context
	 * @param contextCa
	 * @param ca
	 * @return
	 * @throws RSuiteException
	 */
	public static ContentAssemblyItem getFristCaRef(ExecutionContext context, ContentAssembly contextCa, final ContentAssembly ca) throws RSuiteException{
		User user = context.getAuthorizationService().getSystemUser();
		
		List<? extends ContentAssemblyItem> children = context.getContentAssemblyService().getChildObjectsFiltered(user, contextCa, new ContentAssemblyItemFilter() {
			
			@Override
			public boolean include(User user, ContentAssemblyItem item)
					throws RSuiteException {
		
				if (item.getObjectType() == ObjectType.CONTENT_ASSEMBLY_REF) {
					ContentAssemblyReference caRef = (ContentAssemblyReference)item;
					 if (ca.getId().equals(caRef.getTargetId()) || ca.getId().equals(caRef.getId())){
						 return true;
					 }
				}
				
				return false;
			}
		});
		
		if (children.size() == 0){
			throw new RSuiteException("No reference found for " + ca.getId() + " in " + contextCa.getId());
		}
		
		
		return children.get(0);
	}
	
	public static List<String> getMoIdListFromCa(Log log, User user,
			ManagedObjectService moSvc, ContentAssembly ca)
			throws RSuiteException {
			List<String>moIdList = new ArrayList<String>();
			List<? extends ContentAssemblyItem> childList = ca.getChildrenObjects();
			for (ContentAssemblyItem child : childList) {
				if (child instanceof ManagedObjectReference) {
					ManagedObjectReference moRef = (ManagedObjectReference)child;
					String moId = moRef.getTargetId();
					moIdList.add(moId);
				}
			}
			return moIdList;
		}

	public static ContentAssembly getChildCaByNameAndType(ExecutionContext context,
            ContentAssembly contextCa, RSuiteCaType type, String name) throws RSuiteException {
        return getChildCaByNameAndType(context, contextCa, type.getTypeName(), name);
    }
}
