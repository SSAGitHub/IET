package com.rsicms.projectshelper.publish.storage.cleanup;

import static com.rsicms.projectshelper.lmd.value.YesNoLmdValue.*;
import static com.rsicms.projectshelper.publish.storage.datatype.OutputLmd.*;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.impl.LogFactoryImpl;

import com.reallysi.rsuite.api.*;
import com.reallysi.rsuite.api.extensions.ExecutionContext;
import com.reallysi.rsuite.service.*;

public class ClearPublicationHistory {

	Log log = LogFactoryImpl.getLog(ClearPublicationHistory.class);

	private ContentAssemblyService caSvc;
	private ManagedObjectService moSvc;
	private User user;

	public ClearPublicationHistory(ExecutionContext context, User user) {
		this.caSvc = context.getContentAssemblyService();
		this.moSvc = context.getManagedObjectService();
		this.user = user;
	}

	public void startHistoryCleanup (String historyCaId) throws RSuiteException {		
		ContentAssembly historyCa = caSvc.getContentAssembly(user, historyCaId);

		List<? extends ContentAssemblyItem> publicationResults = historyCa.getChildrenObjects();

		for (ContentAssemblyItem publicationResult : publicationResults) {

			ManagedObjectReference publicationResultMoRef = (ManagedObjectReference) publicationResult;

			destroyIfRemovable(publicationResultMoRef.getTargetId());

		}
	}

	private void destroyIfRemovable(String publicationResultId) throws RSuiteException {
		ManagedObject publicationResultMo = moSvc.getManagedObject(user, publicationResultId);

		if (isRemovablePublication(publicationResultMo)) {

			moSvc.checkOut(user, publicationResultMo.getId());

			log.info("Deleting publication result: " + publicationResultMo.getDisplayName() + "[" + publicationResultMo.getId() + "]");

			moSvc.destroy(user, publicationResultMo.getId(), null);

		}
	}

	private boolean isRemovablePublication(ManagedObject publicationResultMo) throws RSuiteException {
		String isRemovablePublicationResult = publicationResultMo.getLayeredMetadataValue(IS_REMOVABLE_PUBLICATION_RESULT.getLmdName());

		if (StringUtils.isNotBlank(isRemovablePublicationResult) && isRemovablePublicationResult.equals(NO.getValue())) {
			return false;
		}

		return true;
	}
}
