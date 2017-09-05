package com.rsicms.projectshelper.publish.storage.upload;

import com.reallysi.rsuite.api.RSuiteException;
import com.rsicms.projectshelper.publish.datatype.OutputGenerationResult;
import com.rsicms.projectshelper.publish.datatype.UploadGeneratedOutputsResult;

public interface GeneratedOutputUploader {

    public UploadGeneratedOutputsResult uploadGeneratedOutputs(OutputGenerationResult outputGenerationResult) throws RSuiteException;
    
    public void beforeUpload(OutputGenerationResult outputGenerationResult) throws RSuiteException;
    
    public void afterUpload(UploadGeneratedOutputsResult uploadResult, OutputGenerationResult outputGenerationResult) throws RSuiteException;
    
}
