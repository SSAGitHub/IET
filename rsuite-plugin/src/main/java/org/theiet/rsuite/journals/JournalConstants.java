package org.theiet.rsuite.journals;

import org.theiet.rsuite.IetConstants;

public interface JournalConstants extends IetConstants {
	
	String CA_TYPE_WITHDRAWN_ARTICLES = "withdrawnArticles";
	
	String CA_TYPE_ARTICLE = "article";
	
	String CA_TYPE_ARTICLES = "articles";
	
	String CA_TYPE_JOURNAL_ARCHIVE = "journal_archive";
	
	String CA_TYPE_JOURNAL_ARCHIVE_YEAR = "journal_archive_year";
	
	String CA_TYPE_JOURNAL_ARCHIVE_MONTH = "journal_archive_month";
	
	
	String CA_TYPE_JOURNAL = "journal";
	String CA_AUTHOR_CORRECTIONS = "authorCorrections";
	
	String CA_TYPE_YEAR = "year";
	
	String CA_TYPE_VOLUME = "volume";
	
	String CA_TYPE_ISSUE = "issue";
	
	String CA_TYPE_ISSUES = "issues";
	
	String CA_TYPE_ISSUE_ARTICLES = "issueArticles";
	
	String CA_NAME_DOCUMENTATION = "Documentation";
	
	String CA_NAME_ARTICLES = "Articles";
	
	String CA_NAME_AVAILABLE = "Available";
	
	String CA_NAME_ISSUES = "Issues";
	
	String LMD_VALUE_WITHDRAWN = "withdrawn";
	
	String LMD_FIELD_STATUS = "status";
	
	String LMD_FIELD_JOURNAL_CODE = "journal_code";
	
	String LMD_FIELD_ISSUE_CODE = "issue_code";
	
	String LMD_FIELD_JOURNAL_EMAIL = "journal_email";
	
	String LMD_FIELD_AUTHOR_EMAIL = "author_email";
	
	String LMD_FIELD_AUTHOR_SALUTATION = "author_salutation";
	
	String LMD_FIELD_ARTICLE_TITLE = "article_title";
	
	String LMD_FIELD_ARTICLE_TYPE = "article_type";
	
	String LMD_FIELD_SPECIAL_ISSUE_TITLE = "special_issue_title";
	
	String LMD_FIELD_SUBMITTED_DATE = "submitted_date";
	
	String LMD_FIELD_DECISION_DATE = "decision_date";
	
	String LMD_FIELD_EXPORT_DATE = "export_date";
	
	String LMD_FIELD_CATEGORY = "category";
	
	String LMD_FIELD_SUPPLEMENTARY_MATERIAL = "supplementary_material";
	
	String LMD_FIELD_TYPESET_PAGES = "typeset_pages";
	
	String LMD_FIELD_ONLINE_PUBLISHED_DATE = "online_published_date";

	String LMD_FIELD_PRINT_JOURNAL_ISSUE = "print_journal_issue";
	
	String LMD_FIELD_ARTICLE_AVAILABLE = "available";
	
	String LMD_FIELD_ARTICLE_ASSIGNED = "assigned";
	
	String LMD_FIELD_AWAITING_AUTHOR_COMMENTS = "awaiting_author_comments";
	
	String LMD_FIELD_INSPEC_CLASSIFIER = "inspec_classifier";
	
	String LMD_FIELD_INSPEC_REQUIRED = "inspec_required";

	String LMD_FIELD_OPEN_ACCESS = "open_access";
	
	String LMD_FIELD_CLASSIFICATIONS = "classifications";

	String LMD_FIELD_CATEGORY_CODE = "category_code";
	
	String LMD_FIELD_SUBMISSION_TYPE = "submission_type";
	
//	Note UK spelling
	String LMD_FIELD_LICENCE_TYPE = "licence_type";
	
	String LMD_FIELD_AUTHOR_INSTITUTION = "author_institution";
	
	String LMD_FIELD_AUTHOR_COUNTRY = "author_country";
	
	String LMD_FIELD_ARTICLE_NOTES = "article_notes";
	
	String LMD_FIELD_DISPLAY_ARTICLE_NOTES_ICON = "display_article_notes_icon";
	
	String LMD_FIELD_JOURNAL_WORFLOW_TYPE = "journal_worflow_type";
	
	String LMD_FIELD_ADD_PREFIX_DIGITAL_LIBRARY_DELIVERY = "add_prefix_digital_library_delivery";

	String PROP_PDF_PROOF_MAIL_TITLE = "iet.journals.pdf.proof.mail.title";
	
	String PROP_PDF_ROOF_MAIL_BODY = "iet.journals.pdf.proof.mail.body";
	
	String PROP_TYPESETTER_FINAL_REQUEST_MAIL_BODY = "iet.journals.typesetter.final.request.mail.body";
	
	String PROP_TYPESETTER_FINAL_REQUEST_MAIL_TITLE = "iet.journals.typesetter.final.request.mail.title";
	
	String PROP_ISSUE_TYPESETTER_REQUEST_MAIL_BODY = "iet.journals.issue.typesetter.request.mail.body";
	
	String PROP_ISSUE_TYPESETTER_REQUEST_MAIL_TITLE = "iet.journals.issue.typesetter.request.mail.title";
	
	String PROP_ISSUE_TYPESETTER_FINAL_REQUEST_MAIL_BODY = "iet.journals.issue.typesetter.final.request.mail.body";
	
	String PROP_ISSUE_TYPESETTER_FINAL_REQUEST_MAIL_TITLE = "iet.journals.issue.typesetter.final.request.mail.title";
	
	String PROP_JOURNALS_SEND_EMAIL_AUTHOR_FROM_EA_MAIL_BODY = "iet.journals.send.email.author.from.ea.mail.body";
	
	String PROP_JOURNALS_SEND_EMAIL_AUTHOR_FROM_EA_MAIL_TITLE = "iet.journals.send.email.author.from.ea.mail.title";

	String PROP_JOURNALS_ARTICLE_WITHDRAWN_NOTIFICATION_MAIL_BODY = "iet.journals.article.withdrawn.notification.mail.body";
	
	String PROP_JOURNALS_ARTICLE_WITHDRAWN_NOTIFICATION_MAIL_TITLE = "iet.journals.article.withdrawn.notification.mail.title";

	String WF_PREPARE_ISSUE = "IET Prepare Issue";
	
	String WF_VAR_ARTICLE_ID = "articleId";
	
	String WF_VAR_JOURNAL_CA_ID = "journalCaId";
	
	String WF_VAR_PRODUCT_ID = "id";

	String WF_VAR_ARTICLE = "ARTICLE";
	
	String WF_VAR_ISSUE = "ISSUE";
	
	String WF_VAR_YEAR = "year";
	
	String WF_PD_TYPESETTER_INGESTION = "IET Typesetter Ingestion";
	
	String PUBTRACK_PRODUCT_ARTICLE = "ARTICLE";
	
	String PUBTRACK_REQUESTED_AUTHOR_COMMENTS = "REQUESTED_AUTHOR_COMMENTS";
	
	String PUBTRACK_REQUESTED_TYPESETTER_UPDATE = "REQUESTED_TYPESETTER_UPDATE";
	
	String PUBTRACK_REQUESTED_TYPESETTER_INITIAL = "REQUESTED_TYPESETTER_INITIAL";
	
	String PUBTRACK_REQUESTED_TYPESETTER_FINAL_FILES = "REQUESTED_TYPESETTER_FINAL_FILES";
	
	String PUBTRACK_AUTHOR_COMMENTS_RECEIVED = "AUTHOR_COMMENTS_RECEIVED";
	
	String PUBTRACK_TYPESETTER_INITIAL_RECEIVED = "TYPESETTER_INITIAL_RECEIVED";

	String PUBTRACK_JOURNAL_CODE = "JOURNAL_CODE";
	
	String PUBTRACK_ARTICLE_RELEASED = "ARTICLE_RELEASED";

	String PUBTRACK_INSPEC_CLASSIFICATION_RECEIVED = "INSPEC_CLASSIFICATION_RECEIVED";

	String PUBTRACK_INSPEC_CLASSIFICATION_REQUESTED = "INSPEC_CLASSIFICATION_REQUESTED";

	String PUBTRACK_SENT_TO_DIGITAL_LIBRARY = "DELIVERED_TO_DIGITAL_LIBRARY";
	
	String PUBTRACK_WORKFLOW_STARTED = "PUBTRACK_WORKFLOW_STARTED";
	
	String PUBTRACK_ARTICLE_WITHDRAWN = "ARTICLE_WITHDRAWN";

	String ROLE_TYPESETTER = "JournalTypesetter";
	
	String ROLE_INSPEC_CLASSIFIER = "JournalInspecClassifier";
	
	String ROLE_PRODUCTION_CONTROLLER = "JournalProductionController";
	
	String ROLE_EDITORIAL_ASSISTANT = "JournalEditorialAssistant";

	String CODE_SEPARATOR = "-";
		
	String CFG_FTP_INSPEC_HOST="iet.inspec.ftp.host";
	
	String CFG_FTP_INSPEC_USER="iet.inspec.ftp.user";
	
	String CFG_FTP_INSPEC_PASSWORD="iet.inspec.ftp.password";
	
	String PROP_INSPEC_FTP_FOLDER = "iet.inspec.ftp.folder";
	
	String PROP_TYPESETTER_FTP_MANUSCRIPT_FOLDER = "iet.typesetter.ftp.manuscriptFolder";
	
	String PROP_TYPESETTER_FTP_ARTICLE_UPDATE_FOLDER = "iet.typesetter.ftp.articleFolder";
	
	String PROP_TYPESETTER_FTP_ISSUE_FOLDER = "iet.typesetter.ftp.issueFolder";
	
	String FILENAME_PREFIX_ARTICLE ="ARTICLE-";
	
	String FILENAME_PREFIX_ISSUE ="ISSUE-";
	
	String EMAIL_VAR_CORRESPONDING_AUTHOR_FULL_NAME = "CorrespondingAuthorFullName";
	
	String EMAIL_VAR_CORRESPONDING_AUTHOR_FIRST_NAME = "CorrespondingAuthorFirstname";
	
	String EMAIL_VAR_CORRESPONDING_AUTHOR_SURNAME = "CorrespondingAuthorSurname";
	
	String EMAIL_VAR_EDITORIAL_ASSISTANT_NAME = "EditorialAssistantName";
	
	String EMAIL_VAR_MANUSCRIPT_ID = "ManuscriptID";
	
	String EMAIL_VAR_PAPER_TITLE = "PaperTitle";
	
	String EMAIL_VAR_CORRESPONDING_AUTHOR_SALUTATION = "CorrespondingAuthorSalutation";
	
	String EMAIL_VAR_JOURNAL_NAME = "JournalName";
	
	String EMAIL_VAR_ATTACHED_FILE = "attachedFile";
	
	String DT_NAME_ARCHIVED_JOURNAL = "archivedJournalDataType";
}
