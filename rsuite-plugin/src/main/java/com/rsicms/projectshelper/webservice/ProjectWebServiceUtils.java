package com.rsicms.projectshelper.webservice;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.io.IOUtils;
import org.theiet.rsuite.IetConstants;

import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.remoteapi.CallArgumentList;
import com.reallysi.rsuite.api.remoteapi.result.ByteSequenceResult;

public class ProjectWebServiceUtils {

	private ProjectWebServiceUtils(){
	}
	
	public static String extractDomainURLFromArgumentList (CallArgumentList args)
			throws RSuiteException {
		
		String domainURL = null;
		String identifier = args.getValues(IetConstants.ARGS_REQUEST_IDENTIFIER_KEY);		
		try {
			URL url = new URL(identifier);
			
			
			domainURL = url.getProtocol()  + "://" + url.getHost() + getPortPart(url);
		} catch (MalformedURLException ex) {
			throw new RSuiteException(RSuiteException.ERROR_NOT_DEFINED, ex.getMessage(), ex);			
		}

		return domainURL;
	}
	
	private static String getPortPart(URL url){
		int portNumber = url.getPort();
		return   portNumber == -1 ? "" : ":" + portNumber;
	}
	
	public static ByteSequenceResult createWebserviceResultObject(File exportedFile)
            throws IOException {
        FileInputStream exprtedFilesArchiveIS = new FileInputStream(
                exportedFile);

        ByteSequenceResult fileResult = new ByteSequenceResult(
                IOUtils.toByteArray(exprtedFilesArchiveIS));
        fileResult.setSuggestedFileName(exportedFile.getName());
        fileResult.setContentType("application/octet-stream");
        return fileResult;
    }
}
