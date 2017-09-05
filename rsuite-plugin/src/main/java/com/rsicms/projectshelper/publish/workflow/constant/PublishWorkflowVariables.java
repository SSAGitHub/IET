package com.rsicms.projectshelper.publish.workflow.constant;

import com.rsicms.projectshelper.workflow.RSuiteWorkflowVariable;

public enum PublishWorkflowVariables implements RSuiteWorkflowVariable {


    EXPORT_FOLDER("exportFolder"), 
    CONTENT_EXPORTER_CONFIGURATION_CLASS("ContentExporterConfigurationClass"),
    OUTPUT_GENERATOR_CONFIGURATION_CLASS("OutputGeneratorConfigurationClass"),
    GENERATED_OUTPUT_UPLOADER_CONFIGURATION_CLASS("GeneratedOutputUploaderConfigurationClass"),
    SEND_NOTIFICATION_CONFIGURATION_CLASS("SendNotificationConfigurationClass"),
    NON_XML_MO_VARIANT("nonXMLMoVariant"),
    EXPORTED_FILES("exportedFiles"),
    EXPORT_ERROR_FLAG("exportErrorFlag"),
    EXPORT_WARNING_FLAG("exportWarningFlag"),
    MO_OUTPUTS("moOutputs"),
    TEMP_FOLDER("tempFolder"),
    MO_FILE_PREFIX("moFile_"),
    DOWNLOADS_FOLDER("downloadsFolder"),
    ADDITIONAL_UPLOADED_MO_IDS("additionalUploadedMoIds");

    private String variableName;

    private PublishWorkflowVariables(String variableName) {
        this.variableName = variableName;
    }

    public String getVariableName() {
        return variableName;
    }

}
