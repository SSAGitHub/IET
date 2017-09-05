package com.rsicms.projectshelper.publish.workflow.actionhandlers;

import static com.rsicms.projectshelper.publish.workflow.actionhandlers.ConfigurationFactory.createGeneratedOutputUploaderConfiguration;
import static com.rsicms.projectshelper.publish.workflow.constant.PublishWorkflowVariables.ADDITIONAL_UPLOADED_MO_IDS;
import static com.rsicms.projectshelper.publish.workflow.constant.PublishWorkflowVariables.GENERATED_OUTPUT_UPLOADER_CONFIGURATION_CLASS;
import static com.rsicms.projectshelper.publish.workflow.constant.PublishWorkflowVariables.MO_FILE_PREFIX;

import java.util.Map;

import org.jbpm.context.exe.ContextInstance;

import com.reallysi.rsuite.api.workflow.WorkflowExecutionContext;
import com.rsicms.projectshelper.publish.datatype.OutputGenerationResult;
import com.rsicms.projectshelper.publish.datatype.UploadGeneratedOutputsResult;
import com.rsicms.projectshelper.publish.storage.upload.GeneratedOutputUploader;
import com.rsicms.projectshelper.publish.workflow.configuration.GeneratedOutputUploaderConfiguration;

public class UploadGeneratedOutputActionHandler extends PublishWorkflowActionHandler {

    private static final long serialVersionUID = 8224727220520504208L;

    @SuppressWarnings("unchecked")
    @Override
    public void executeHandler(WorkflowExecutionContext context) throws Exception {

        OutputGenerationResult generationResult =  OutputGenerationResult.createFromWorkflowContext(context);
        
        String uploaderConfigurationClass = context.getVariable(GENERATED_OUTPUT_UPLOADER_CONFIGURATION_CLASS.getVariableName());
        
        GeneratedOutputUploaderConfiguration uploaderConfiguration = createGeneratedOutputUploaderConfiguration(uploaderConfigurationClass);
        GeneratedOutputUploader uploader = uploaderConfiguration.getGeneratedOutputUploader(context, getSystemUser());

        uploader.beforeUpload(generationResult);
        UploadGeneratedOutputsResult uploadResult = uploader.uploadGeneratedOutputs(generationResult);
        uploader.afterUpload(uploadResult, generationResult);
        
        ContextInstance contextInstance = context.getContextInstance();
        Map<String, Object> variables = contextInstance.getVariables();
        for (String xmlMoId : uploadResult.getXmlMoIds()) {
            variables.put(MO_FILE_PREFIX.getVariableName() + xmlMoId, uploadResult.getOutputMoIdForMo(xmlMoId));
        }
        
        variables.put(ADDITIONAL_UPLOADED_MO_IDS.getVariableName(), uploadResult.getSerializedAdditionalMoIds());
        
        contextInstance.addVariables(variables);

    }

	@Override
	String getWorkflowTaskName() {
		return "Upload generated output";
	}
}
