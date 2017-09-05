package org.theiet.rsuite.standards.domain.publish;

import java.io.File;
import java.util.*;
import java.util.Map.Entry;

import org.theiet.rsuite.standards.StandardsBooksConstans;
import org.theiet.rsuite.utils.DateUtils;

import com.reallysi.rsuite.api.*;
import com.reallysi.rsuite.api.control.ObjectCheckInOptions;
import com.reallysi.rsuite.api.extensions.ExecutionContext;
import com.reallysi.rsuite.service.ManagedObjectService;
import com.rsicms.projectshelper.upload.UploadUtils;
import com.rsicms.projectshelper.utils.*;
import com.rsicms.projectshelper.utils.browse.ProjectBrowserUtils;
import com.rsicms.projectshelper.workflow.WorkflowUtils;

public class PublishOutputUploader implements StandardsBooksConstans {

	private ExecutionContext context;

	private User user;
	
	private ManagedObjectService moSvc;

	private String workflowProcessId;

	public PublishOutputUploader(ExecutionContext context, String workflowProcessId) {
		this.context = context;
		this.workflowProcessId = workflowProcessId;
		user = context.getAuthorizationService().getSystemUser();
		moSvc = context.getManagedObjectService();
	}

	public Map<String, String> uploadPublishedOutputs(PublishedOutputsSummarizer publishedOutputsSummarizer) throws RSuiteException {
		Map<String, String> sourceMoIdMap2outputMoId = new HashMap<String, String>();
		ContentAssembly editionCa = getOutpusEditionCA(context, publishedOutputsSummarizer.getRsuitePath());
		ContentAssembly outputsCa = getOutputsCA(editionCa);

		String isbn = editionCa.getLayeredMetadataValue(LMD_FIELD_ISBN);
		isbn = isbn == null ? "" : isbn + "_";

		for (Entry<String, File> entry : publishedOutputsSummarizer.getMoOutPuts().entrySet()) {
			ContentAssembly outputCa = prepareOutputCAforNewFileUpload(outputsCa, isbn, entry);
			ManagedObject mo = uploadOutputForMo(outputCa, isbn, entry, publishedOutputsSummarizer);
			sourceMoIdMap2outputMoId.put(entry.getKey(), mo.getId());
			setLmdForSourceMo(entry.getKey());
		}

		return sourceMoIdMap2outputMoId;
	}

	private ContentAssembly prepareOutputCAforNewFileUpload(ContentAssembly outputsCa, String isbn, Entry<String, File> entry) 
			throws RSuiteException {
		String moId = entry.getKey();

		File file = entry.getValue();

		ContentAssembly outputCa = getOutputCA(outputsCa, moId);		

		ContentAssembly historyCa = getHistoryCA(outputCa);
		moveOldPublicationToHistoryCA(historyCa, new File(file.getParentFile(), isbn + file.getName()).getName(), outputCa.getId());
		
		return outputCa;
	}

	private void moveOldPublicationToHistoryCA(ContentAssembly publishHistory, String moAlias, String outputCaId) throws RSuiteException {
		List<ManagedObject> moList = context.getManagedObjectService().getObjectsByAlias(user, moAlias);
		if (moList != null) {
			for (ManagedObject managedObject : moList) {
				String moParentId = ProjectContentAssemblyUtils.getPaterntId(context, user, managedObject.getId());
				if (outputCaId.equals(moParentId)) {
					ProjectContentAssemblyUtils.moveRefToFistPossition(context, managedObject.getId(), publishHistory.getId());
				}
			}
		}
	}

	private ContentAssembly getHistoryCA(ContentAssembly outputCa) throws RSuiteException {
		ContentAssembly publishHistory = ProjectBrowserUtils.getChildCaByType(context, outputCa, CA_TYPE_STANDARDS_PLUBISH_HISTORY);
		
		if (publishHistory == null) {
			publishHistory = ProjectContentAssemblyUtils.createContentAssembly(context,
					outputCa.getId(), CA_NAME_STANDARDS_PLUBISH_HISTORY, CA_TYPE_STANDARDS_PLUBISH_HISTORY);
		}

		return publishHistory;
	}

	private ManagedObject uploadOutputForMo(ContentAssembly outputCa, String isbn,
			Entry<String, File> entry, PublishedOutputsSummarizer publishedOutputsSummarizer) throws RSuiteException {
		File file = entry.getValue();

		File newFile = new File(file.getParentFile(), isbn + file.getName());

		if (file.renameTo(newFile)) {
			file = newFile;
		}
		
		ManagedObject publicationMo = UploadUtils.uploadOutputFile(context, outputCa, file, false);

		List<MetaDataItem> metadataEntries = generateLayerMetaData(publicationMo, outputCa, publishedOutputsSummarizer);

		loadMetadata(publicationMo, metadataEntries);

		return publicationMo;
	}

	private void loadMetadata(ManagedObject publicationMo, List<MetaDataItem> metadataEntries) throws RSuiteException {
		moSvc.checkOut(user, publicationMo.getId());
		moSvc.setMetaDataEntries(user, publicationMo.getId(), metadataEntries);
		moSvc.checkIn(user, publicationMo.getId(), new ObjectCheckInOptions());
	}

	private List<MetaDataItem> generateLayerMetaData (ManagedObject publicationMo, ContentAssembly outputCa,
			PublishedOutputsSummarizer publishedOutputsSummarizer) throws RSuiteException {
		List<MetaDataItem> metadataEntries = new ArrayList<MetaDataItem>();
		ProjectContentAssemblyUtils.moveRefToFistPossition(context, publicationMo.getId(), outputCa.getId());
		String workflowUrl = WorkflowUtils.constructWfReportURL(workflowProcessId).replaceAll("RSUITE-SESSION-KEY", ProjectExecutionContextUtils.getSkey(context));

		metadataEntries.add(new MetaDataItem(LMD_FIELD_STANDARDS_PUBLICATION_WITH_ERRORS, publishedOutputsSummarizer.isExportError()));
		metadataEntries.add(new MetaDataItem(LMD_FIELD_STANDARDS_PUBLICATION_WITH_WARNINGS, publishedOutputsSummarizer.isExportWarning()));
		metadataEntries.add(new MetaDataItem(LMD_FIELD_STANDARDS_IS_REMOVABLE_PUBLICATION_RESULT, LMD_VALUE_YES));
		metadataEntries.add(new MetaDataItem(LMD_FIELD_USER_CREATOR, publishedOutputsSummarizer.getUser()));
		metadataEntries.add(new MetaDataItem(LMD_FIELD_DATE_CREATED, DateUtils.getDate(DateUtils.DATE_FORMAT_yyyy_MM_dd_h_m_s_ampm, publicationMo.getDtCreated())));
		metadataEntries.add(new MetaDataItem(LMD_FIELD_PUBLISH_WORKFLOW_LOG, workflowUrl));

		return metadataEntries;
	}

	private void setLmdForSourceMo(String moId) throws RSuiteException{
		
		ManagedObject mo = moSvc.getManagedObject(user, moId);
		if (mo.getTargetId() != null){
			mo = moSvc.getManagedObject(user, mo.getTargetId());
		}
		
		
		moSvc.setMetaDataEntry(user, mo.getId(), new MetaDataItem("has_output", LMD_VALUE_YES));
	}

	public ContentAssembly getOutputCA(ContentAssembly outputs,
			final String moId) throws RSuiteException {

		String syncObject = "rsuite_" + moId;

		synchronized (syncObject.intern()) {

			ManagedObjectService moSvc = context.getManagedObjectService();
			String outputContainerAlias = "output" + moId;
						
			ManagedObject outputMo = moSvc.getObjectByAlias(user, outputContainerAlias);
			if (outputMo != null){
			    return context.getContentAssemblyService().getContentAssembly(user, outputMo.getId());   
			}else{
			    return createOutputContainer(outputs, moId, moSvc, outputContainerAlias);  
			}    
		}
	}

    private ContentAssembly createOutputContainer(ContentAssembly outputs, final String moId,
            ManagedObjectService moSvc, String outputContainerAlias) throws RSuiteException {
        ManagedObject mo = moSvc.getManagedObject(user, moId);
        String outputCAName = getOutputCAName(mo);
        
        ContentAssembly ca = ProjectContentAssemblyUtils.createContentAssembly(context,
                outputs.getId(), outputCAName, CA_TYPE_OUTPUT_EVENT);
        moSvc.setMetaDataEntry(user, ca.getId(), new MetaDataItem(
                LMD_FIELD_SOURCE_MO_ID, moId));

        moSvc.setAlias(user, ca.getId(), new Alias(outputContainerAlias));

        return ca;
    }

	private String getOutputCAName(ManagedObject mo) throws RSuiteException {
		String displayName = mo.getDisplayName();
		return displayName.replaceAll("[^0-9\\p{L}\\s]+", "");
	}

	private ContentAssembly getOutpusEditionCA(ExecutionContext context,
			String contextPath) throws RSuiteException {

		ContentAssemblyItem caItem = ProjectBrowserUtils.getAncestorCAbyType(context,
				contextPath, CA_TYPE_STANDARDS_BOOK_EDITION);

		return (ContentAssembly) caItem;
	}

	private ContentAssembly getOutputsCA(ContentAssembly editionCa)
			throws RSuiteException {

		ContentAssembly outputs = ProjectBrowserUtils.getChildCaByType(context,
				editionCa, CA_TYPE_OUTPUTS);
		return outputs;
	}

}
