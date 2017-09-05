package org.theiet.rsuite.domain;

import java.io.File;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.theiet.rsuite.IetConstants;
import org.theiet.rsuite.utils.ExceptionUtils;
import org.theiet.rsuite.utils.IetUtils;
import org.theiet.rsuite.utils.PubtrackLogger;

import com.reallysi.rsuite.api.ContentAssembly;
import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.User;
import com.reallysi.rsuite.api.workflow.WorkflowExecutionContext;
import com.reallysi.rsuite.service.ContentAssemblyService;
import com.reallysi.rsuite.service.ManagedObjectService;
import com.rsicms.rsuite.helpers.upload.DuplicateFilenamePolicy;
import com.rsicms.rsuite.helpers.upload.RSuiteFileLoadHelper;
import com.rsicms.rsuite.helpers.upload.RSuiteFileLoadOptions;

public abstract class TypesetterIngestionHelper implements IetConstants {

	private static final String TYPESETTER_UPDATE_INITIAL = "initial";

	private WorkflowExecutionContext context;

	private Log logger;

	private ContentAssemblyService caService;

	private ManagedObjectService moService;

	private User user;

	public TypesetterIngestionHelper(WorkflowExecutionContext context) {
		super();

		this.context = context;

		logger = context.getWorkflowLog();
		caService = context.getContentAssemblyService();
		moService = context.getManagedObjectService();
		user = context.getAuthorizationService().getSystemUser();
	}

	public void loadTypesetterFiles() throws Exception {

		String product = context.getVariable(WF_VAR_PRODUCT);

		String productCode = context.getVariable("id");

		logger.info("execute: look up " + product + " ca for code "
				+ productCode);

		String productAssemblyId = obtainProductCAId(productCode);

		if (StringUtils.isBlank(productAssemblyId)) {
			ExceptionUtils.throwWorfklowException(context,
					"Failed to look up unique object for " + product + " code "
							+ productCode);
		}

		ContentAssembly productCA = caService.getContentAssembly(user,
				productAssemblyId);

		String typesetterSubmission = obtainTypesetterSubmission(productCA);

		ContentAssembly typesetterAssembly = obtainTypesetterCA(productCA);

		if (typesetterAssembly == null) {
			ExceptionUtils.throwWorfklowException(context,
					"Failed to get typesetter assembly for " + productCode
							+ " id: " + productAssemblyId);
		}

		if (StringUtils.isBlank(typesetterSubmission)) {
			typesetterSubmission = TYPESETTER_UPDATE_INITIAL;
		}

		setWorkflowVariables(productCA, typesetterSubmission);

		logTypesetterIngestionEventToPubtrack(product, productCode,
				typesetterSubmission);

		loadFilesToRSuite(typesetterAssembly);

		IetUtils.removeMetaDataFieldFromCa(logger, user, moService, productCA,
				LMD_FIELD_AWAITING_TYPESETTER_UPDATES);
	}

	protected abstract String obtainProductCAId(String productCode)
			throws RSuiteException;

	protected abstract String obtainTypesetterSubmission(
			ContentAssembly productCA) throws RSuiteException;

	protected abstract ContentAssembly obtainTypesetterCA(
			ContentAssembly productCA) throws RSuiteException;

	protected void setWorkflowVariables(ContentAssembly productAssembly,
			String typesetterSubmission) throws RSuiteException {
		context.setVariable(WF_VAR_SUBMISSION_TYPE, typesetterSubmission);
		context.setVariable(WF_VAR_RSUITE_CONTENTS, productAssembly.getId());
		context.setVariable(WF_VAR_JOURNAL_ID, getProductId(productAssembly));
	}

	protected abstract String getProductId(ContentAssembly productCA)
			throws RSuiteException;

	protected void loadFilesToRSuite(ContentAssembly typesetterAssembly)
			throws Exception {

		File typesetterZip = context.getFileWorkflowObject().getFile();

		RSuiteFileLoadOptions loadOpts = new RSuiteFileLoadOptions(user);
		loadOpts.setDuplicateFilenamePolicy(DuplicateFilenamePolicy.UPDATE);
		RSuiteFileLoadHelper.loadZipContentsToCaNodeContainer(context,
				typesetterZip, typesetterAssembly, loadOpts);
		boolean hasLoadErrors = IetUtils.getFileLoadResult(logger, loadOpts);

		if (hasLoadErrors) {
			ExceptionUtils.throwWorfklowException(context,
					"Load had errors");
		}

	}

	protected void logTypesetterIngestionEventToPubtrack(

	String product, String productId, String typesetterSubmission)
			throws RSuiteException {

		if (typesetterSubmission.equals(LMD_VALUE_UPDATE)) {
			PubtrackLogger.logToProcess(user, context, logger, product,
					productId, PUBTRACK_TYPESETTER_UPDATE_RECEIVED);
		} else if (LMD_VALUE_FINAL.equals(typesetterSubmission)) {
			PubtrackLogger.logToProcess(user, context, logger, product,
					productId, PUBTRACK_TYPESETTER_FINAL_RECEIVED);
		} else if (LMD_VALUE_INITIAL.equals(typesetterSubmission)) {
			PubtrackLogger.logToProcess(user, context, logger, product,
					productId, PUBTRACK_TYPESETTER_INITIAL_RECEIVED);
		}
	}

	protected static String getTypesetterUpdateInitial() {
		return TYPESETTER_UPDATE_INITIAL;
	}

	protected WorkflowExecutionContext getContext() {
		return context;
	}

	protected Log getLogger() {
		return logger;
	}

	protected ContentAssemblyService getCaService() {
		return caService;
	}

	protected ManagedObjectService getMoService() {
		return moService;
	}

	protected User getUser() {
		return user;
	}
	
}
