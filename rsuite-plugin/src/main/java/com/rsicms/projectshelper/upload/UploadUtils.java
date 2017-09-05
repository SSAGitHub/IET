package com.rsicms.projectshelper.upload;

import java.io.File;

import com.reallysi.rsuite.api.*;
import com.reallysi.rsuite.api.extensions.ExecutionContext;
import com.rsicms.rsuite.helpers.upload.*;

public class UploadUtils {

	public static ManagedObject uploadOutputFile(ExecutionContext context,
			ContentAssembly outputCa, File outputFile) throws RSuiteException {
		return uploadOutputFile(context, outputCa, outputFile, true);
	}

	public static ManagedObject uploadOutputFile(ExecutionContext context,
			ContentAssembly outputCa, File outputFile, boolean addFileToMoMappingByAlias) throws RSuiteException {
	
		if (!outputFile.exists() || !outputFile.isFile()){
			throw new RSuiteException("File to upload " + outputFile.getAbsolutePath() + " doesn't exist");
		}
		
		if (outputFile.length() == 0){
			throw new RSuiteException("Incorrect file: file size 0 ");
		}
		
		User user = context.getAuthorizationService().getSystemUser();
		RSuiteFileLoadOptions options = new RSuiteFileLoadOptions(user);
	
		ManagedObject mo = context.getManagedObjectService().getObjectByAlias(
				user, outputFile.getName());
		
		if (mo != null && addFileToMoMappingByAlias) {
			options.addFileToMoMapping(outputFile, mo);
		}
		
	
		ManagedObject mofile = RSuiteFileLoadHelper.loadFileToCa(context, outputFile, outputCa,
				options);
		
		if (mofile == null){
			throw new RSuiteException("File " + outputFile.getAbsolutePath() + " has been not uploaded to " + outputCa.getId() + " please check the log.");
		}
		
		return mofile;
	}

}
