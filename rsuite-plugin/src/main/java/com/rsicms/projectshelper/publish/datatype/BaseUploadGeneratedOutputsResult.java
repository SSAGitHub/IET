package com.rsicms.projectshelper.publish.datatype;

import static com.rsicms.projectshelper.publish.workflow.constant.PublishWorkflowVariables.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.reallysi.rsuite.api.ManagedObject;
import com.reallysi.rsuite.api.workflow.WorkflowExecutionContext;

public class BaseUploadGeneratedOutputsResult implements
		UploadGeneratedOutputsResult {

	private static final String MO_ID_SEPARATOR = ",";

	private Map<String, String> sourceMoIdMap2outputMoId;

	private static final List<String> ADDITIONAL_UPLOADED_MO_IDS_LIST = new ArrayList<>();

	private List<String> additionalUploadedMoIds;

	public BaseUploadGeneratedOutputsResult(WorkflowExecutionContext context) {

		Map<String, Object> workflowVariables = context.getWorkflowVariables();

		sourceMoIdMap2outputMoId = parseSourceMoIdToOutputMoId(workflowVariables);
		additionalUploadedMoIds = parseAdditionalUploadedMoIds(workflowVariables);
	}

	private Map<String, String> parseSourceMoIdToOutputMoId(
			Map<String, Object> workflowVariables) {
		Map<String, String> sourceMoIdMap2outputMoId = new HashMap<String, String>();

		for (Entry<String, Object> entry : workflowVariables.entrySet()) {
			String key = entry.getKey();
			if (key.contains(MO_FILE_PREFIX.getVariableName())) {
				String sourceMoId = key.replace(
						MO_FILE_PREFIX.getVariableName(), "");
				String outputMoId = (String) entry.getValue();
				sourceMoIdMap2outputMoId.put(sourceMoId, outputMoId);
			}
		}
		return sourceMoIdMap2outputMoId;
	}

	private List<String> parseAdditionalUploadedMoIds(
			Map<String, Object> workflowVariables) {
		String additionalMoIds = (String) workflowVariables
				.get(ADDITIONAL_UPLOADED_MO_IDS.getVariableName());
		List<String> additionalUploadedMoIds = new ArrayList<>();
		if (StringUtils.isNotEmpty(additionalMoIds)) {
			String[] moIds = additionalMoIds.split(MO_ID_SEPARATOR);
			for (String moId : moIds) {
				if (StringUtils.isNotBlank(moId)) {
					additionalUploadedMoIds.add(moId.trim());
				}
			}
		}
		return additionalUploadedMoIds;
	}

	public BaseUploadGeneratedOutputsResult(
			Map<String, String> sourceMoIdMap2outputMoId) {
		this.sourceMoIdMap2outputMoId = sourceMoIdMap2outputMoId;
		additionalUploadedMoIds = ADDITIONAL_UPLOADED_MO_IDS_LIST;
	}

	public BaseUploadGeneratedOutputsResult(
			Map<String, String> sourceMoIdMap2outputMoId,
			List<ManagedObject> additionalMOs) {

		this.sourceMoIdMap2outputMoId = sourceMoIdMap2outputMoId;

		additionalUploadedMoIds = new ArrayList<>();
		for (ManagedObject uploadedMo : additionalMOs) {
			additionalUploadedMoIds.add(uploadedMo.getId());
		}

	}
	
	public void addAdditionalUploadedMoIds(List<ManagedObject> additionalMOs){
		if (additionalUploadedMoIds == null){
			additionalUploadedMoIds = new ArrayList<>();
		}
		
		for (ManagedObject uploadedMo : additionalMOs) {
			additionalUploadedMoIds.add(uploadedMo.getId());
		}
	}

	@Override
	public String getOutputMoIdForMo(String xmlMoId) {
		return sourceMoIdMap2outputMoId.get(xmlMoId);
	}

	@Override
	public Set<String> getXmlMoIds() {
		return sourceMoIdMap2outputMoId.keySet();
	}

	@Override
	public List<String> getAdditionalUploadedMoIds() {
		return additionalUploadedMoIds;
	}

	@Override
	public String getSerializedAdditionalMoIds() {
		return StringUtils.join(additionalUploadedMoIds, MO_ID_SEPARATOR);
	}
}
