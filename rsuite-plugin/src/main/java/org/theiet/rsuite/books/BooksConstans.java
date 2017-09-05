package org.theiet.rsuite.books;

import org.theiet.rsuite.IetConstants;

public interface BooksConstans extends IetConstants {

	String ROLE_BOOK_TYPESETTER = "BookTypesetter";
	
	String TYPESETTER_REQUEST_PRINTER = "printer";
	
	String LMD_VALUE_PROOF_SUFFIX = "_PROOF";
	String LMD_VALUE_WORKFLOW_STARTED = "book_workflow_started";
	
	String LMD_FIELD_BOOK_TITLE_SHORT = "book_title_short";
	String LMD_FIELD_BOOK_TITLE = "book_title";
	String LMD_FIELD_BOOK_SERIES_NAME = "book_series_name";
	String LMD_FIELD_E_ISBN = "e_isbn";
	String LMD_FIELD_CONTRACT_SIGNED_DATE = "contract_signed_date";
	String LMD_FIELD_BOOK_E_PRODUCT_CODE = "e_product_code";	
	String LMD_FIELD_BOOK_PRODUCT_CODE = "product_code";
	String LMD_FIELD_BOOK_METADATA_SENT = "book_metadata_sent";
	String LMD_FIELD_CONTRACTED_TS_DELIVERY_DATE = "contracted_ts_delivery_date";
	
	String LMD_FIELD_REFORECAST_PUB_DATE = "reforecast_pub_date";
	
	String LMD_FIELD_ACTUAL_PUB_DATE = "actual_pub_date";

	String CA_TYPE_PRINT_FILES = "printFiles";
	String CA_TYPE_TYPESCRIPT = "typescript";
	String CA_TYPE_BOOK = "book";
	String CA_TYPE_BOOKS_CONTENT_ASSEMBLY = "booksCA";
	String CA_TYPE_BOOK_CORRECTIONS = "bookCorrections";
	String CA_TYPE_PRINTER_INSTRUCTIONS = "printerInstructions";
	
	
	String CA_NAME_CORRECTIONS = "Corrections";	
	String CA_NAME_COVER = "Cover";
	String CA_NAME_PRINT_FILES = "Print Files";
	
	String CA_NAME_IMAGES = "Images";
	String CA_NAME_TYPESCRIPT = "Typescript";
	String CA_NAME_PRODUCTION_FILES = "Production Files";
	String CA_NAME_PRODUCTION_DOCUMENTATION = "Production Documentation";
	String CA_NAME_EDITORIAL = "Editorial";
	String CA_NAME_CONTRACTS = "Contracts";
	String CA_NAME_PRODUCT_INFORMATION = "Product information";
	String CA_NAME_PRINTER_INSTRUCTIONS = "Printer Instructions";
		
	String ROLE_BOOK_PRODUCTION_CONTROLLER = "BookProductionController";	
	String ROLE_BOOK_EDITORIAL_ASSISTANT = "BookEditorialAssistant";
	String ROLE_BOOK_PRINTER = "BookPrinter";
	
	String CFG_BOOK_REQUEST_TARGET_FOLDER = "ftp.folder.book";
	String CFG_BOOK_UPDATE_REQUEST_TARGET_FOLDER = "ftp.folder.book.update";

	String FILENAME_PREFIX_BOOK ="BOOK-";

	String WF_NAME_IET_PREPARE_BOOK = "IET Prepare Book";
	
	String WF_VAR_BOOK = "BOOK";
	String WF_PD_PREPARE_BOOK = "IET Prepare Book";
	String WF_BOOK_PRODUCT_CODE = "id";

	String WF_TASK_PREPARE_BOOK = "Prepare book";
	String WF_TASK_REVIEW_PROOFS = "Review proofs";
	String WF_TASK_UPDATE_FILES = "Update book files";
	
	String PROP_BOOKS_TYPESETTER_REQUEST_MAIL_TITLE = "iet.books.typesetter.request.mail.title";
	String PROP_BOOKS_TYPESETTER_REQUEST_MAIL_BODY = "iet.books.typesetter.request.mail.body";
	String PROP_BOOKS_TYPESETTER_CORRECTIONS_MAIL_TITLE = "iet.books.typesetter.corrections.mail.title";
	String PROP_BOOKS_TYPESETTER_CORRECTIONS_MAIL_BODY = "iet.books.typesetter.corrections.mail.body";
	String PROP_BOOKS_TYPESETTER_FINAL_REQUEST_MAIL_TITLE = "iet.books.typesetter.final.request.mail.title";
	String PROP_BOOKS_TYPESETTER_FINAL_REQUEST_MAIL_BODY = "iet.books.typesetter.final.request.mail.body";	
	String PROP_BOOKS_PRINTER_REQUEST_MAIL_TITLE = "iet.books.printer.request.mail.title";
	String PROP_BOOKS_PRINTER_REQUEST_MAIL_BODY = "iet.books.printer.request.mail.body";
	String PROP_BOOKS_SEND_METADATA_MAIL_TITLE = "iet.books.send.metadata.mail.title";
	String PROP_BOOKS_SEND_METADATA_MAIL_BODY = "iet.books.send.metadata.mail.body";
	
	String PROP_BOOKS_METADATA_DISTRIBUTION_USER = "iet.books.metadata.distribution.user";
	
	String CONTAINER_BOOK_CONTRACTED = "Contracted";
	String CONTAINER_BOOK_IN_PRODUCTION = "In Production";
	String CONTAINER_BOOK_PUBLISHED = "Published";
	
	String EMAIL_VAR_CONTACT_NAME = "contactFirstName";
	String EMAIL_VAR_COMPANY_NAME = "companyName";
	String EMAIL_VAR_DEADLINE = "deadline";
	String EMAIL_VAR_PRODUCTION_CONTROLLER = "productionController";
	String EMAIL_VAR_SERIES_NAME = "seriesName";
	String EMAIL_VAR_AUTHOR_FULL_NAME = "authorFullName";
	String EMAIL_VAR_BOOK_TITLE = "bookTitle";
	String EMAIL_VAR_PRODUCT_CODE = "productCode";
	String EMAIL_VAR_FTP_FILE_NAME = "ftpFileName";
	String EMAIL_VAR_FTP_TARGET_FOLDER = "ftpTargetFolder";

	String USER_METADATA_DISTRIBUTION_DEFAULT = "MetaDataDistributionUser";



	String PUBTRACK_PREPARE_BOOK_WORKFLOW_STARTED = "PUBTRACK_PREPARE_BOOK_WORKFLOW_STARTED";
}
