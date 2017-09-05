package org.theiet.rsuite.standards;

import org.theiet.rsuite.books.BooksConstans;

public interface StandardsBooksConstans extends BooksConstans {

	String DISPLAY_NAME_STANDARDS_CONTENT = "content";

	String FORM_PARAM_SOURCE_BOOK_EDITION = "sourceBookEdition";
	
	String LMD_FIELD_PUBLICATION_DEPENDENCY = "publication_dependency";
	
	String LMD_FIELD_IET_OWNER = "iet_owner";
	String LMD_FIELD_IET_AUTHOR = "iet_author";
	


	String LMD_FIELD_CORPORATE_NAME = "corporate_contributor";
	
	String LMD_FIELD_ARCHIVE_TIMESTAMP = "archiveTimestamp";
	String LMD_FIELD_CREATE_TIMESTAMP = "createTimestamp";

	String LMD_FIELD_STANDARDS_TEMPLATE_ID = "templateId";
	String LMD_FIELD_STANDARDS_EMAIL = "standards_email";

	String LMD_FIELD_BOOK_STATUS = "book_status";
	
	String LMD_FIELD_PID = "_pid";

	String LMD_FIELD_IS_HOT_CONTAINER_ENABLED = "_is_hot_container_enabled";

	String LMD_FIELD_STANDARDS_IS_REMOVABLE_PUBLICATION_RESULT = "is_removable_publication_result";

	String LMD_FIELD_STANDARDS_PUBLICATION_WITH_WARNINGS = "publication_with_warnings";

	String LMD_FIELD_STANDARDS_PUBLICATION_WITH_ERRORS = "publication_with_errors";

	String LMD_FIELD_HOT_CONTAINER_PROCESS_DEFINITION = "_hot_container_process_definition";
	
	String LMD_FIELD_STANDARDS_BOOK_PREFIX = "standards_book_prefix";
	
	String LMD_FIELD_PUBLISH_WORKFLOW_LOG = "publish_workflow_log";
	
	String LMD_FIELD_MATHML_FONT = "mathml_font";
	
	String LMD_FIELD_MATHML_SIZE = "mathml_size";

	String LMD_VALUE_ICML_TRANSFORMATION_WRITING_REGULATION = "wreg";
	String LMD_VALUE_ICML_TRANSFORMATION_ONSITE_GUIDE = "osg";
	String LMD_VALUE_ICML_TRANSFORMATION_GUIDANCE_NOTES = "gn";
	
	String LMD_VALUE_STANDARD_TEMPLATES = "StandardTemplates";
	String LMD_VALUE_ARCHIVED = "archived";
	
	String LMD_VALUE_INITIALIZED = "initialized";
	
	String LMD_VALUE_IN_WORFKLOW = "in_workflow";

	String CA_TYPE_STANDARDS_PRINT_FILES = "printFiles";
	String CA_TYPE_STANDARDS_TYPESCRIPT = "standardsTypescript";

	String CA_TYPE_STANDARDS_BOOK = "standardsBook";
	String CA_TYPE_STANDARDS_BOOK_EDITION = "standardsBookEdition";
	String CA_TYPE_STANDARDS_BOOK_CORRECTIONS = "bookCorrections";
	String CA_TYPE_STANDARDS_PRINTER_INSTRUCTIONS = "printerInstructions";
	String CA_TYPE_STANDARDS_TEMPLATE = "template";
	
	String CA_TYPE_STANDARDS_IMAGES = "standardsImages";
	
	String CA_TYPE_STANDARDS_PLUBISH_HISTORY = "standardsPublishHistory";

	String CA_NAME_STANDARDS_IMAGES = "Images";
	String CA_NAME_STANDARDS_CORRECTIONS = "Corrections";
	String CA_NAME_STANDARDS_COVER = "Cover";
	String CA_NAME_STANDARDS_PRINT_FILES = "Print Files";
	String CA_NAME_STANDARDS_TYPESCRIPT = "Typescript";
	String CA_NAME_STANDARDS_PRODUCTION_FILES = "Production Files";
	String CA_NAME_STANDARDS_PRODUCTION_DOCUMENTATION = "Production Documentation";
	String CA_NAME_STANDARDS_EDITORIAL = "Editorial";
	String CA_NAME_STANDARDS_CONTRACTS = "Contracts and permissions";
	String CA_NAME_STANDARDS_PRODUCT_INFORMATION = "Product information";
	String CA_NAME_STANDARDS_PRINTER_INSTRUCTIONS = "Printer Instructions";
	String CA_NAME_STANDARDS_COMMENTS = "Comments";
	String CA_NAME_STANDARDS_NEXT_EDITION_COMMENTS = "Next edition comments";
	String CA_NAME_STANDARDS_PLUBISH_HISTORY = "History";
	String CA_NAME_FINAL_FILES = "Files";

	String ROLE_STANDARDS_BOOK_PRODUCTION_CONTROLLER = "StandardsBookProductionController";
	String ROLE_STANDARDS_BOOK_AUTHOR = "StandardsBookAuthor";
	String ROLE_STANDARDS_BOOK_OWNER = "StandardsBookOwner";
	String ROLE_STANDARDS_BOOK_PRINTER = "StandardsBookPrinter";


	String CA_TYPE_OUTPUT_EVENT = "outputEvent";

	String LMD_VALUE_REJECTED = "rejected";

	String CA_TYPE_OUTPUTS = "outputs";
//TODO to REMOVE
	String PROP_STANDARDS_BOOKS_SEND_PUBLISHING_MAIL_TITLE = "iet.standards.books.send.publishing.mail.title";
	String PROP_STANDARDS_BOOKS_SEND_PUBLISHING_MAIL_BODY = "iet.standards.books.send.publishing.mail.body";


	String WF_VAR_MO_FILE = "moFile_";
	String WF_VAR_AUTHOR = "author";
	String WF_VAR_PRODUCTION_CONTROLLER = "productionController";
	
	String WF_VAR_BOOK_PREFIX = "bookPrefix";
	String WF_VAR_BOOK_CA_ID = "bookCAId";
	String WF_VAR_IMAGES_DIRECTORY = "images_directory";
	
	String WF_VAR_REMOVABLE_DIRECTORY = "removable_directory";
	
	String WF_VAR_BOOK_EDITION_E_PRODUCT_CODE = "bookEditionEProductCode";

	String XSLT_URI_O2_PIS_TO_DITA_OBSOLETE = "rsuite:/res/plugin/iet/xslt/books/O2_PIs_to_DITA.xsl";
	
	String XSLT_URI_O2_PIS_TO_DITA = "rsuite:/res/plugin/iet/xslt/o2pi2dita-cm/O2_PIs_2_DITA-CM.xsl";

	String XSLT_URI_REMOVE_CHANGE_TRACKING = "rsuite:/res/plugin/iet/xslt/books/RemoveChangeTrackingInformation.xsl";

	String XSLT_URI_REG_2_ICML_WREG = "rsuite:/res/plugin/iet/xslt/reg2icml/reg2icml.xsl";
	String XSLT_URI_REG_2_ICML_OSG = "rsuite:/res/plugin/iet/xslt/reg2icml-OSG/reg2icml-OSG.xsl";
	String XSLT_URI_REG_2_ICML_GN = "rsuite:/res/plugin/iet/xslt/reg2icml-GN/reg2icml-GN.xsl";

	String OUTPUT_TRANSFORMATION_WIRING_REGULATIONS = "Wiring Regulations";
	String OUTPUT_TRANSFORMATION_GUIDANCE_NOTES = "Guidance Notes";
	String OUTPUT_TRANSFORMATION_ONSITE_GUIDE = "Onsite Guide";

	String USER_METADATA_DISTRIBUTION_STANDARDS_DEFAULT = "MetaDataDistributionStandardsUser";	
	
	String PROP_STANDARDS_BOOKS_METADATA_DISTRIBUTION_USER = "iet.standards.books.metadata.distribution.user";
	
	String PREFIX_SEPARATOR = "_";

	String SEPARATOR_FILE_EXTENSION = ".";
	
	String FILE_EXTENSION_PNG = "png";

	String FILE_EXTENSION_EPS = "eps";

	String FILE_EXTENSION_TIFF = "tiff";

	String FILE_EXTENSION_PSD = "psd";
	
	String USER_LOCAL_ESP_BOOK = "ESPStandardsBook";
	
	String VAR_BOOK_EDITION_ID = "bookEditionId";

}
