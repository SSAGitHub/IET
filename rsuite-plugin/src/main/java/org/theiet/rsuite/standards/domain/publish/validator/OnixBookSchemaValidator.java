package org.theiet.rsuite.standards.domain.publish.validator;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.zip.ZipOutputStream;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import org.theiet.rsuite.onix.OnixConstants;
import org.theiet.rsuite.utils.ExceptionUtils;
import org.w3c.dom.ls.LSInput;
import org.w3c.dom.ls.LSResourceResolver;

import com.reallysi.rsuite.api.RSuiteException;
import com.rsicms.rsuite.helpers.utils.ZipUtil;

public class OnixBookSchemaValidator implements OnixConstants {
	
	private String onixXMLFile;

	private InputStream onixXSD;
	
	private String recipientName;
	
	private String timestamp;
	
	private SimpleDateFormat TIMESTAMP_FORMAT = new SimpleDateFormat("yyyyMMdd_HHmmss");
	

	public OnixBookSchemaValidator(String onixXMLFile, String recipientName) {
		this.onixXMLFile = onixXMLFile;
		this.recipientName = recipientName;
		onixXSD = getOnixSchema(ONIX_SCHEMA_FILE_NAME);
	}

	public static InputStream getOnixSchema(String schemaFileName) {
		return OnixBookSchemaValidator.class.getResourceAsStream(ONIX_SCHEMAS_LOCATION_PLUGIN_PATH + schemaFileName);
	}

	public OnixValidationResult validate() throws RSuiteException {
		setTimestamp();
		SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
		factory.setResourceResolver(createResourceResolver());
		OnixBookErrorHandler onixBookErrorHandler = new OnixBookErrorHandler();
		
		OnixValidationResult result = null;
		
		
		try {
			InputStream onixXML = new ByteArrayInputStream(onixXMLFile.getBytes("utf-8"));
			Schema schema = factory.newSchema(new StreamSource(onixXSD));
			Validator validator = schema.newValidator();
			validator.setErrorHandler(onixBookErrorHandler);
			validator.validate(new StreamSource(onixXML));
			

			
			if (onixBookErrorHandler.isHasError()){
				byte[] zipBytes = archiveErrorAndOnixFile(onixBookErrorHandler);
				result = new OnixValidationResult(zipBytes, getFullSuggestedFileNameWithTimestamp(ONIX_ARCHIVE_RESULT_FILE_NAME), MIME_APPLICATION_ZIP, !onixBookErrorHandler.isHasError());
				
			}else{
				result = new OnixValidationResult(onixXMLFile.getBytes(UTF_8), getFullSuggestedFileNameWithTimestamp(ONIX_SUGGESTED_FILE_NAME), MIME_APPLICATION_XML, !onixBookErrorHandler.isHasError());
			}
			
			
		}  catch (Exception e) {
			throw ExceptionUtils.createRsuiteException(e);
		}
		return result;
	}
	
	private void setTimestamp() {
		timestamp = TIMESTAMP_FORMAT.format(new Date()).toString();		
	}

	private String getFullSuggestedFileNameWithTimestamp (String suggestedFileName) {
		String fileExtension = suggestedFileName.substring(suggestedFileName.length()-4, suggestedFileName.length());
		String suggestedFileNameWithTimestamp = suggestedFileName.substring(0, suggestedFileName.length()-4) + "_" + timestamp + fileExtension; 
		return getFullSuggestedFileName (suggestedFileNameWithTimestamp);
	}
	
	private String getFullSuggestedFileName (String suggestedFileName) {
		return recipientName + "_" + suggestedFileName;
	}

	private byte[] archiveErrorAndOnixFile(
			OnixBookErrorHandler onixBookErrorHandler) throws IOException,
			UnsupportedEncodingException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ZipOutputStream zipOutputStream = new ZipOutputStream(baos);

		ZipUtil.addBytesToZip(getFullSuggestedFileName(ONIX_ERROR_FILE_NAME), onixBookErrorHandler.getErrorMessage().getBytes(UTF_8), zipOutputStream);
		ZipUtil.addBytesToZip(getFullSuggestedFileName(ONIX_SUGGESTED_FILE_NAME), onixXMLFile.getBytes(UTF_8), zipOutputStream);
		
		ZipUtil.closeZipStream(zipOutputStream);
		
		byte[] zipBytes = baos.toByteArray();
		return zipBytes;
	}

	private LSResourceResolver createResourceResolver() {
		return new LSResourceResolver() {
			@Override
			public LSInput resolveResource(String type, String namespaceURI,
					String publicId, String systemId, String baseURI) {
				return  new Input(publicId, systemId, OnixBookSchemaValidator.getOnixSchema(systemId));				
			}
		};
	}

}




























































