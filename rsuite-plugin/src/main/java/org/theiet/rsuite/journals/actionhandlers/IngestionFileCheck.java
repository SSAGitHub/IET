package org.theiet.rsuite.journals.actionhandlers;

import org.theiet.rsuite.journals.JournalConstants;
import org.theiet.rsuite.journals.utils.JournalWorkflowUtils;

import com.reallysi.rsuite.api.ManagedObject;
import com.reallysi.rsuite.api.workflow.AbstractBaseNonLeavingActionHandler;
import com.reallysi.rsuite.api.workflow.WorkflowExecutionContext;
import com.reallysi.rsuite.service.ManagedObjectService;

public class IngestionFileCheck extends AbstractBaseNonLeavingActionHandler
		implements JournalConstants {

	private static final long serialVersionUID = 1L;

	@Override
	public void execute(WorkflowExecutionContext executionContext)
			throws Exception {
		String articleId = executionContext.getVariable(WF_VAR_PRODUCT_ID);
		String product = executionContext.getVariable(WF_VAR_PRODUCT);

		try {
			if (articleId != null && WF_VAR_ARTICLE.equals(product)) {

				String pId = executionContext.getProcessInstanceId();
				String articleCaId = JournalWorkflowUtils.getArticleCaId(
						executionContext, pId, articleId);
				ManagedObjectService moSvc = executionContext.getManagedObjectService();
				ManagedObject mo = moSvc.getManagedObject(getSystemUser(),
						articleCaId);
				String status = mo.getLayeredMetadataValue(LMD_FIELD_STATUS);

				if (LMD_VALUE_WITHDRAWN.equals(status)) {
					executionContext.setVariable("skip", "true");
					executionContext.getWorkflowLog().info(
							"Skipping file for withdrawn article " + articleId);
				}
			}
		} catch (Exception e) {
			executionContext.getContextInstance().deleteVariable(
					WF_VAR_ERROR_MSG);
			executionContext.getWorkflowLog().error(
					"Unable to check article status", e);
		}

	}
}
