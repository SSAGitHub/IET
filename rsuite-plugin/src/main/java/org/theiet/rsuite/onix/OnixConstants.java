package org.theiet.rsuite.onix;

import org.theiet.rsuite.IetConstants;

public interface OnixConstants extends IetConstants {

	String NAMESPACE_ONIX = "http://ns.editeur.org/onix/3.0/reference";
	
	String ONIX_FILE_SUFFIX = "_onix.xml";
	
	String ONIX_VAR_EDITION_NUMBER = "editionNumber";

	String ONIX_VAR_BOOK_SUBTITLE = "bookSubtitle";
	
	String LMD_VALUE_TEMPLATE = "template";
	String LMD_VALUE_PRINT = "print";
	String LMD_VALUE_DIGITAL = "digital";
	
	String LMD_FIELD_ONIX_LAST_SYNC_TIMESTAMP = "onix_last_sync_timestamp";

	String LMD_FIELD_ONIX_LAST_SYNC_FAILED = "onix_last_sync_failed";
	
	String LMD_FIELD_ONIX_MESSAGE_NUMBER = "onix_message_number";
	

	String ELEMENT_NAME_ONIX_MESSAGE = "ONIXMessage";
	
	String LMD_FIELD_ONIX_CONFIGURATION_TYPE = "onix_configuration_type";
	String LMD_FIELD_PRODUCT_FORMAT = "product_format";
	String LMD_FIELD_ONIX_RECIPIENT = "onix_recipient";
	String LMD_FIELD_ONIX_BOOK_VENDOR = "onix_book_vendor";
	String LMD_FIELD_ONIX_PUBLICATION_RSUITE_ID = "onix_publication_rsuite_id";

	String CA_TYPE_ONIX_RECIPIENT = "onixRecipient";
	String CA_TYPE_ONIX_CONFIGURATIONS = "onixConfigurations";
	String CA_TYPE_ONIX_BOOK_CONFIGURATION = "onixBookConfiguration";
	String CA_TYPE_ONIX_OUTPUT = "onixOutput";
	

	String CA_NAME_ONIX_RECIPIENT = "Onix Recipient";
	String CA_NAME_ONIX_CONFIGURATIONS = "Onix Configurations";
	String CA_NAME_ONIX_BOOK_CONFIGURATION = "Onix Book Configuration";
	String CA_NAME_OUTPUT = "Output";
	
	
	String ONIX_SCHEMAS_LOCATION_PLUGIN_PATH = "/WebContent/doctypes/onix/";
	
	String ONIX_SCHEMA_FILE_NAME = "ONIX_BookProduct_3.0_reference.xsd";
	
	String ONIX_RSUITE_SCHEMA_FILE_NAME = "ONIX_BookProduct_3.0_reference_IET.xsd";
	
	
	String ONIX_SUGGESTED_FILE_NAME = "onix.xml";
	
	String ONIX_ERROR_FILE_NAME = "errors.txt";
	
	String ONIX_ARCHIVE_RESULT_FILE_NAME = "onix_result.zip";
	
	String WF_PD_ONIX_DELIVERY = "IET Onix Delivery";

	String WF_VAR_RECIPIENT_NAME = "recipientName";
	String WF_VAR_ONIX_PROCESSOR = "onixProcessor";
	String WF_VAR_MERGED_FILE = "mergedFile";
	String WF_VAR_RESULT_FILE = "resultFile";
	String WF_VAR_ERROR_RESOURCE_REFERENCE = "errorResourceReference";
	String WF_VAR_VALID_ONIX_FILE = "validOnixFile";
	String WF_VAR_ONIX_UNITS = "onixUnits";
	
	String ROLE_ONIX_BOOK_VENDOR = "OnixBookVendor";
	
	String CFG_ONIX_VALIDATION_SUCCEEDED = "ftp.folder.onix.validation.succeeded";
	String CFG_ONIX_VALIDATION_FAILED = "ftp.folder.onix.validation.failed";
	
	String ONIX_CONFIGURATIONS_QUERY = 
			"/rs_ca_map/rs_ca[rmd:get-type(.) = '" + OnixConstants.CA_TYPE_ONIX_CONFIGURATIONS + "']";

}
