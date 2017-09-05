package com.rsicms.projectshelper.publish.storage.upload;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;

import com.reallysi.rsuite.api.ContentAssembly;
import com.reallysi.rsuite.api.ManagedObject;
import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.User;
import com.reallysi.rsuite.api.extensions.ExecutionContext;
import com.rsicms.projectshelper.publish.datatype.OutputGenerationResult;
import com.rsicms.projectshelper.upload.UploadUtils;

public class SingleMoUploaderHelper {

	private GeneratedOutputMetadata generatedMetadata;
	
	private String workflowProcessId;
	
	private ExecutionContext context;
	
	private OutputGenerationResult outputGenerationResult;

	private Map<String, String> sourceMoIdMap2outputMoId = new HashMap<String, String>();
	
	private Log logger;

	public SingleMoUploaderHelper(ExecutionContext context, User user, Log logger,
			String workflowProcessId,
			OutputGenerationResult outputGenerationResult) {
		super();
		this.context = context;
		this.workflowProcessId = workflowProcessId;
		this.outputGenerationResult = outputGenerationResult;
		this.logger = logger;
		generatedMetadata = new GeneratedOutputMetadata(context, user);
	}


	public ManagedObject uploadGenratedFile(String sourceMOid, File generatedFile, ContentAssembly outputCA) throws RSuiteException{
		
		ManagedObject uploadedMO = uploadGenratedFile(generatedFile, outputCA);
		sourceMoIdMap2outputMoId.put(sourceMOid, uploadedMO.getId());
		return uploadedMO;
	}


	public Map<String, String> getSourceMoIdMap2outputMoId() {
		return sourceMoIdMap2outputMoId;
	}


	public ManagedObject uploadGenratedFile(File generatedFile, ContentAssembly outputCA) throws RSuiteException {
		ManagedObject uploadedMO = UploadUtils.uploadOutputFile(context,
				outputCA, generatedFile);


		
		generatedMetadata.setOutputMetadata(workflowProcessId, uploadedMO,
				outputGenerationResult);
		
		logger.info("Uploaded " + generatedFile + " to mo "
				+ uploadedMO.getId());
		
		return uploadedMO;
		
	}
	
		
}
