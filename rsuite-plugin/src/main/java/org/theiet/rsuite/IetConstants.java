package org.theiet.rsuite;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;


/**
 * Constants specific to the IET system.
 *
 */
public interface IetConstants {


	String UTF_8 = "utf-8";
	
	String ALIAS_TYPE_FILENAME = "filename";
	
	String ARGS_REQUEST_IDENTIFIER_KEY = "_request:identifier";
	
	String ARGS_RSUITE_BROWSE_URI = "rsuiteBrowseUri";
	
	String REST_V1_URL_ROOT = "/rsuite/rest/v1";
	
	String REST_V2_URL_ROOT = "/rsuite/rest/v2";
	
	String REST_API_REFERENCE = "/api";

	String PROJECT_NAME_LMD = "project";
	
	String LMD_VALUE_YES = "yes";
	
	String LMD_VALUE_NO = "no";
	
	String LMD_VALUE_TRUE = "true";
	
	String LMD_VALUE_IET = "IET";
	
	String LMD_FIELD_SOURCE_MO_ID = "sourceMoId";
	
	String LMD_VALUE_INITIAL = "initial";
	
	String LMD_VALUE_UPDATE = "update";
	
	String LMD_VALUE_FINAL = "final";
	
	
	String LMD_FIELD_APPROVAL_STATUS = "approval_status";	
	String LMD_FIELD_AUTHOR_FIRST_NAME = "author_first_name";
	String LMD_FIELD_AUTHOR_SURNAME = "author_surname";
	String LMD_FIELD_ADDTIONAL_AUTHORS = "additional_authors";
	String LMD_FIELD_TYPESETTER_USER = "typesetter";
	String LMD_FIELD_PRINTER_USER = "printer";
	
	String LMD_FIELD_PRINT_PUBLISHED_DATE = "print_published_date";
	
	String LMD_FIELD_TYPESETTER_UPDATE_TYPE = "typesetter_update_type";
	
	
	String LMD_FIELD_CONTENT_ASSEMBLY_TYPE = "caType";
	String LMD_FIELD_DOI = "doi";
	String LMD_FIELD_IET_TAXONOMY = "iet_taxonomy";
	String LMD_FIELD_INSPEC_CODE = "inspec_code";
	String LMD_FIELD_INSPEC_TEXT = "inspec_text";
	String LMD_FIELD_ISBN = "isbn";
	String LMD_FIELD_ISSN = "issn";
	String LMD_FIELD_MD5_HASH = "_md5Hash";
	String LMD_FIELD_PID = "_pid"; // The PID of the workflow process associated with the object
	String LMD_FIELD_TITLE = "pubtitle";
	String LMD_FIELD_WORKFLOW_STATUS = "workflow_status";
	String LMD_FIELD_XML_ID = "xml_id";
	String LMD_FIELD_PRODUCTION_CONTROLLER_USER = "production_controller";
	String LMD_FIELD_EDITORIAL_ASSISTANT = "editorial_assistant";
	String LMD_FIELD_AWAITING_TYPESETTER_UPDATES = "awaiting_typesetter_updates";
	String LMD_FIELD_CA_MODOFIED = "ca_modofied";
	String LMD_FIELD_USER_CREATOR = "user_creator";
	String LMD_FIELD_DATE_CREATED = "date_created";

	String LMD_FIELD_NAME_USER_CREATOR = "User creator";
	String LMD_FIELD_NAME_DATE_CREATED = "Date created";

	String USER_PROP_CONTACT_FIRST_NAME = "contact.first.name";

	String BOOK_TYPESETTER_EXPORT_PATH_PROPERTY = "iet.workflow.book.typesetter.export";
	
	String BOOK_INSPEC_EXPORT_PATH_PROPERTY = "iet.workflow.book.inspec.export";
	
	String BOOK_FINAL_EXPORT_PATH_PROPERTY = "iet.workflow.book.final.export";
	
	
	String PARAM_RSUITE_ID = "rsuiteId";
	
	String UI_PARAM_RSUITE_REFRESH_MANAGED_OBJECTS = "rsuite:refreshManagedObjects";
	
	String UI_PROPERTY_OBJECTS = "objects";
	
	String UI_PROPERTY_CHILDREN = "children";
	
	//TODO use IetDate class
	SimpleDateFormat UK_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
	
	SimpleDateFormat UK_DATE_FORMAT_LONG = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	SimpleDateFormat UK_DATE_FORMAT_TIMESTAMP = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
	
	
	DateFormat UK_DATE_FORMAT_SHORT = DateFormat.getDateInstance(DateFormat.SHORT, Locale.UK);

	String CA_TYPE_FINAL_FILES = "finalFiles";
	String CA_TYPE_TYPESETTER = "typesetter";
	
	String CA_TYPE_FOLDER = "ca";
	
	String CA_NAME_FINAL_FILES = "Final Files";
	String CA_NAME_TYPESETTER = "Typesetter";
	
	String CA_TYPE_STANDARDS_DOMAIN = "standards";
	
	String CA_TYPE_IET_TV_DOMAIN = "iettv";

	String RSUITE_SESSION_KEY = "RSUITE-SESSION-KEY";
	
	
	String REPORT_JOURNAL_CODE = "JOURNAL_CODE";

	String REPORT_IET_ISSUE = "IET_ISSUE";

	String REPORT_IET_ARTICLE = "IET_ARTICLE";
	
	String REPORT_YEAR = "YEAR";

	String REPORT_VOLUME_NUMBER = "VOLUME_NUMBER";

	String REPORT_ISSUE_NUMBER = "ISSUE_NUMBER";
	
	String WF_NAME_IET_STANDARDS_WORKFLOW = "IET Standards Workflow";
	String WF_DATE_STRING = "dateString";
	String WF_VAR_RSUITE_CONTENTS = "rsuite contents";
	String WF_VAR_DOMAIN_URL = "domainURL";
	String WF_VAR_RSUITE_USER_ID = "rsuiteUserId";
	String WF_VAR_RSUITE_PATH = "rsuitePath";
	
	String WF_VAR_ERROR_MSG = "failDetail";
	
	String WF_VAR_SUBMISSION_TYPE = "typesetter_update_type";
	String WF_VAR_JOURNAL_ID = "journalId";
	String WF_VAR_PRODUCT = "product";
	
	String CFG_FTP_TYPESETTER_HOST="iet.typesetter.ftp.host";
	String CFG_FTP_TYPESETTER_USER="iet.typesetter.ftp.user";
	String CFG_FTP_TYPESETTER_PASSWORD="iet.typesetter.ftp.password";
		
	String USER_PROP_FTP_HOST="ftp.host";
	String USER_PROP_USER="ftp.user";
	String USER_PROP_PASSWORD="ftp.password";
	String USER_PROP_FTP_PORT="ftp.port";
	
	String EMAIL_VAR_ADDITIONAL_TEXT = "additionalText";
	
	String MIME_APPLICATION_XML = "application/xml";

	String MIME_APPLICATION_ZIP = "application/zip";
	
	String ATTRIBUTE_NAME_NO_NAMESPACE_SCHEMA_LOCATION = "noNamespaceSchemaLocation";

	String PUBTRACK_TYPESETTER_INITIAL_RECEIVED = "TYPESETTER_INITIAL_RECEIVED";
	String PUBTRACK_TYPESETTER_UPDATE_RECEIVED = "TYPESETTER_UPDATE_RECEIVED";
	String PUBTRACK_TYPESETTER_FINAL_RECEIVED = "TYPESETTER_FINAL_RECEIVED";
	
	String ROLE_STANDARDS_BOOK_AUTHORIZED_USER = "StandardsBookAuthorizedUser";
	String ROLE_IET_TV_AUTHORIZED_USER = "IetTvAuthorizedUser";
	
}
