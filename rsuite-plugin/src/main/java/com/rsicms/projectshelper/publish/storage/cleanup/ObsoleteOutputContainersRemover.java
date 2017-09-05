package com.rsicms.projectshelper.publish.storage.cleanup;

import static com.rsicms.projectshelper.publish.storage.datatype.OutputCaTypes.*;
import static com.rsicms.projectshelper.publish.storage.datatype.OutputLmd.*;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.impl.LogFactoryImpl;

import com.reallysi.rsuite.api.*;
import com.reallysi.rsuite.api.control.ObjectDestroyOptions;
import com.reallysi.rsuite.api.extensions.ExecutionContext;
import com.reallysi.rsuite.service.*;

public class ObsoleteOutputContainersRemover {

	Log log = LogFactoryImpl.getLog(ObsoleteOutputContainersRemover.class);

	private ContentAssemblyService caSvc;
	private ManagedObjectService moSvc;
	private User user;

	public ObsoleteOutputContainersRemover(ExecutionContext context, User user) {
		this.caSvc = context.getContentAssemblyService();
		this.moSvc = context.getManagedObjectService();
		this.user = user;
	}

	public void removeDuplicates(String outputContainerId)
			throws RSuiteException {
		ContentAssembly outputsContainer = caSvc.getContentAssembly(user,
				outputContainerId);
		
		validateContainer(outputContainerId, outputsContainer);

		List<? extends ContentAssemblyItem> publicationResults = outputsContainer
				.getChildrenObjects();

		for (ContentAssemblyItem publicationResult : publicationResults) {
			if (publicationResult instanceof ContentAssemblyReference) {
				ContentAssemblyReference publicationResultMoRef = (ContentAssemblyReference) publicationResult;
				ContentAssembly outputContainer = caSvc.getContentAssembly(
						user, publicationResultMoRef.getTargetId());
				processOutputContainer(outputContainer);
			}

		}
	}

	public void validateContainer(String outputContainerId,
			ContentAssembly outputsContainer) throws RSuiteException {
		
		String containerType = outputsContainer.getType();
		
		if (OUTPUTS.isSameType(containerType)){
			throw new RSuiteException("This method is dedicated only for outputs continer. " + outputContainerId + " has type " + containerType);
		}
	}

	public void processOutputContainer(ContentAssembly outputContainer)
			throws RSuiteException {
		String sourceMoId = outputContainer
				.getLayeredMetadataValue(SOURCE_MO_ID.getLmdName());

		ManagedObject managedObject = getSourceManagedObject(sourceMoId);

		if (isObsoleteOutputContainer(managedObject)) {
			purgeAndRemoveContainer(outputContainer);
		}

	}

	public ManagedObject getSourceManagedObject(String sourceMoId)
			throws RSuiteException {
		
		ManagedObject mo = null;
		
		try{
			mo = moSvc.getManagedObject(user, sourceMoId);
		}catch (RSuiteException e){
			//fails is there is no mo
		}
		
		return mo;
	}

	public boolean isObsoleteOutputContainer(ManagedObject managedObject) {
		return managedObject ==null || managedObject.isOriginalMO();
	}

	private void purgeAndRemoveContainer(ContentAssembly containerToRemove)
			throws RSuiteException {

		List<? extends ContentAssemblyItem> childs = containerToRemove
				.getChildrenObjects();
		
		for (ContentAssemblyItem child : childs) {
			removeContentAssemblyItem(child);

		}
		
		caSvc.removeContentAssembly(user, containerToRemove.getId());
	}

	public void removeContentAssemblyItem(ContentAssemblyItem child)
			throws RSuiteException {
		
		if (child instanceof ContentAssemblyReference) {
			ContentAssemblyReference caReference = (ContentAssemblyReference) child;
			ContentAssembly container = caSvc.getContentAssembly(
					user, caReference.getTargetId());
			purgeAndRemoveContainer(container);
		}else if (child instanceof ManagedObjectReference){
			ManagedObjectReference moReference = (ManagedObjectReference) child;
			String moId = moReference.getTargetId();
			moSvc.checkOut(user, moId);
			moSvc.destroy(user, moId, new ObjectDestroyOptions());
		}
	}

}
