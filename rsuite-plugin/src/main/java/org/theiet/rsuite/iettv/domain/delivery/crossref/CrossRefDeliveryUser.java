package org.theiet.rsuite.iettv.domain.delivery.crossref;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.theiet.rsuite.datatype.deliveryUser.BaseDeliveryUser;

import com.reallysi.rsuite.api.RSuiteException;
import com.rsicms.projectshelper.net.http.MultipartRequest;

public class CrossRefDeliveryUser extends BaseDeliveryUser{

    private Log logger;
	
	public CrossRefDeliveryUser(Log logger, String userId, Map<String, String> userProperties) {
		super(userId, userProperties);
		this.logger = logger;				
	}

	@Override
	public void deliverToMainDestination(InputStream inputStream,
			String fileName) throws RSuiteException {
		
		String url = getProperty("http.url");
        String user = getProperty("http.user");
        String password = getProperty("http.password");
        
        try {
			MultipartRequest request = new MultipartRequest(url, "utf-8", 400000);
			request.addFilePart("fname", fileName, inputStream);
			request.addFormField("login_id", user);
			request.addFormField("login_passwd", password);
			request.addFormField("operation", "doMDUpload");
			request.addFormField("type", "metadata");			
											   

			InputStream responseStream = request.completeRequest();
			if (logger != null){
				logger.info("Response from CrossRef" );
				logger.info(IOUtils.toString(responseStream));	
			}			
			request.disconnect();
			
		} catch (IOException e) {
			throw new RSuiteException(0, "Unable to create request for " + url, e);
		}
		
	}

	@Override
	public void deliverToDestination(InputStream inputStream, String fileName,
			String path) throws RSuiteException {		
		
	}

}
