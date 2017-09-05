package com.rsicms.projectshelper.publish.storage.upload;

import static com.rsicms.projectshelper.lmd.value.LmdUtils.addMetadata;
import static com.rsicms.projectshelper.publish.storage.datatype.OutputLmd.DATE_CREATED;
import static com.rsicms.projectshelper.publish.storage.datatype.OutputLmd.IS_REMOVABLE_PUBLICATION_RESULT;
import static com.rsicms.projectshelper.publish.storage.datatype.OutputLmd.PUBLICATION_WITH_ERRORS;
import static com.rsicms.projectshelper.publish.storage.datatype.OutputLmd.PUBLICATION_WITH_WARNINGS;
import static com.rsicms.projectshelper.publish.storage.datatype.OutputLmd.PUBLISH_WORKFLOW_LOG;
import static com.rsicms.projectshelper.publish.storage.datatype.OutputLmd.USER_CREATOR;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.reallysi.rsuite.api.ManagedObject;
import com.reallysi.rsuite.api.MetaDataItem;
import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.User;
import com.reallysi.rsuite.api.control.ObjectCheckInOptions;
import com.reallysi.rsuite.api.extensions.ExecutionContext;
import com.reallysi.rsuite.service.ManagedObjectService;
import com.rsicms.projectshelper.publish.datatype.OutputGenerationResult;
import com.rsicms.projectshelper.utils.ProjectExecutionContextUtils;
import com.rsicms.projectshelper.workflow.WorkflowUtils;

public class GeneratedOutputMetadata {

	private ExecutionContext context;
	
	private User user;

	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(
			"yyyy-MM-dd hh:mm:ss a");

	public GeneratedOutputMetadata(ExecutionContext context, User user) {
		this.context = context;
		this.user = user;
	}

	public void setVersionableOutputMetadata(String workflowProcessId,
			ManagedObject publicationMo,
			OutputGenerationResult publishedOutputsSummarizer)
			throws RSuiteException {
		
		List<MetaDataItem> metadataEntries = generateLayerMetaData(workflowProcessId, publicationMo, publishedOutputsSummarizer);
		
		ManagedObjectService moSvc = context.getManagedObjectService();
		moSvc.checkOut(user, publicationMo.getId());
		moSvc.setMetaDataEntries(user, publicationMo.getId(), metadataEntries);
		moSvc.checkIn(user, publicationMo.getId(), new ObjectCheckInOptions());
	}
	
	public void setOutputMetadata(String workflowProcessId,
			ManagedObject publicationMo,
			OutputGenerationResult publishedOutputsSummarizer)
			throws RSuiteException {		
		List<MetaDataItem> metadataEntries = generateLayerMetaData(workflowProcessId, publicationMo, publishedOutputsSummarizer);
		ManagedObjectService moSvc = context.getManagedObjectService();
		moSvc.setMetaDataEntries(user, publicationMo.getId(), metadataEntries);
	}
	
	

	public List<MetaDataItem> generateLayerMetaData(String workflowProcessId,
			ManagedObject publicationMo,
			OutputGenerationResult publishedOutputsSummarizer)
			throws RSuiteException {
		List<MetaDataItem> metadataEntries = new ArrayList<MetaDataItem>();
		String workflowUrl = WorkflowUtils.constructWfReportURL(
				workflowProcessId).replaceAll("RSUITE-SESSION-KEY",
				ProjectExecutionContextUtils.getSkey(context));

		addMetadata(metadataEntries, PUBLICATION_WITH_ERRORS,
				publishedOutputsSummarizer.hasExportError());
		addMetadata(metadataEntries, PUBLICATION_WITH_WARNINGS,
				publishedOutputsSummarizer.hasExportWarning());
		addMetadata(metadataEntries, IS_REMOVABLE_PUBLICATION_RESULT, true);
		addMetadata(metadataEntries, USER_CREATOR,
				publishedOutputsSummarizer.getUser());
		addMetadata(metadataEntries, DATE_CREATED,
				DATE_FORMAT.format(new Date()));
		addMetadata(metadataEntries, PUBLISH_WORKFLOW_LOG, workflowUrl);

		return metadataEntries;
	}
}
