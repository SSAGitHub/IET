package com.rsicms.projectshelper.publish.storage.upload;

import java.io.File;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.logging.Log;

import com.reallysi.rsuite.api.ContentAssembly;
import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.User;
import com.reallysi.rsuite.api.extensions.ExecutionContext;
import com.rsicms.projectshelper.publish.datatype.BaseUploadGeneratedOutputsResult;
import com.rsicms.projectshelper.publish.datatype.OutputGenerationResult;
import com.rsicms.projectshelper.publish.datatype.UploadGeneratedOutputsResult;

public abstract class VersionableGeneratedOutputUploader implements GeneratedOutputUploader {

    private ExecutionContext context;
    
    private Log logger;
    
    private String workflowProcessId;
    
    private User user;
    
    public VersionableGeneratedOutputUploader(ExecutionContext context, User user, Log logger, String workflowProcessId) {
        this.context = context;
        this.logger = logger;
        this.workflowProcessId = workflowProcessId;
        this.user = user;
    }
    
	
	@Override
	public BaseUploadGeneratedOutputsResult uploadGeneratedOutputs(
			OutputGenerationResult outputGenerationResult)
			throws RSuiteException {
		
		SingleMoUploaderHelper uploadHelper = new SingleMoUploaderHelper(context, user, logger, workflowProcessId, outputGenerationResult);

		for (Entry<String, File> entry : outputGenerationResult.getMoOutPuts()
				.entrySet()) {
			
			String sourceMOid = entry.getKey();
			File articleFinal = entry.getValue();
			uploadHelper.uploadGenratedFile(sourceMOid, articleFinal, getOutputsCa(sourceMOid));
		}
		
		Map<String, String> sourceMoIdMap2outputMoId  = uploadHelper.getSourceMoIdMap2outputMoId();

		return new BaseUploadGeneratedOutputsResult(sourceMoIdMap2outputMoId);
		
	}

	protected abstract ContentAssembly getOutputsCa(String sourceMOid) throws RSuiteException;


	@Override
	public void beforeUpload(OutputGenerationResult outputGenerationResult)
			throws RSuiteException {
		
	}

	@Override
	public void afterUpload(UploadGeneratedOutputsResult uploadResult,
			OutputGenerationResult outputGenerationResult)
			throws RSuiteException {
		
	}

}
