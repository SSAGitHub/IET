package org.theiet.rsuite.journals.domain.article.metadata;

import static org.theiet.rsuite.journals.domain.article.metadata.ArticleMetadataFields.*;


import java.util.ArrayList;
import java.util.List;

import org.theiet.rsuite.journals.domain.article.Article;
import org.theiet.rsuite.journals.domain.article.datype.ArticleAuthor;

import com.reallysi.rsuite.api.MetaDataItem;
import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.User;
import com.reallysi.rsuite.service.ManagedObjectService;

public class ArticleMetadataUpdater {

	private ManagedObjectService moService;

	private User user;

	private Article article;
	
	private String onlinePublishedDate;
	
	private String articleAvailable;

	private String doi;
	
	private String authorInstitution;
	
	private String authorCountry;
	
	private String typesetPages;
	
	private boolean assigned;

	private String issueCode;

	private String articleTitle;

	private String articleType;
	
	private ArticleAuthor author;
	
	private String specialIssue;

	private String specialIssueTitle;

	private String openAccessCheckList;

	private String category;

	private List<String> classifications;

	private String submittedDate;

	private String decisionDate;

	private String exportDate;

	private String inspecRequired;

	private String licenseType;

	private String articleNotes;

	private String submissionType;

	private String articleNotesIcon;

	private String categoryCodes;

	private String journalCode;

	private String supplementaryMaterial;
	
	private String prePublishedDate;
	
	public ArticleMetadataUpdater(User user, ManagedObjectService moService,
			Article article) {
		this.user = user;
		this.moService = moService;
		this.article = article;
	}
	
	public void updateMetadata() throws RSuiteException {
		List<MetaDataItem> items = createMetadataItemsList();
		moService.setMetaDataEntries(user, article.getArticleCaId(), items);
		article.refreshArticleCa();
	}

	private List<MetaDataItem> createMetadataItemsList() {
		List<MetaDataItem> items = new ArrayList<>();

		addMetaDataItem(items, LMD_FIELD_ONLINE_PUBLISHED_DATE, onlinePublishedDate);
		addMetaDataItem(items, LMD_FIELD_PRE_PUBLISHED_DATE, prePublishedDate);
		addMetaDataItem(items, LMD_FIELD_ARTICLE_AVAILABLE, articleAvailable);
		addMetaDataItem(items, LMD_FIELD_DOI, doi);
		addMetaDataItem(items, LMD_FIELD_AUTHOR_INSTITUTION, authorInstitution);
		addMetaDataItem(items, LMD_FIELD_AUTHOR_COUNTRY, authorCountry);
		addMetaDataItem(items, LMD_FIELD_TYPESET_PAGES, typesetPages);
		addMetaDataItem(items, LMD_FIELD_ARTICLE_ASSIGNED, convertToYesNo(assigned));
		addMetaDataItem(items, LMD_FIELD_ISSUE_CODE, issueCode);
		addMetaDataItem(items, LMD_FIELD_ARTICLE_TITLE, articleTitle);
		addMetaDataItem(items, LMD_FIELD_ARTICLE_TYPE, articleType);
		
		addAuthorMetadata(items);
		
		addMetaDataItem(items, LMD_FIELD_IS_SPECIAL_ISSUE, specialIssue);
		addMetaDataItem(items, LMD_FIELD_SPECIAL_ISSUE_TITLE, specialIssueTitle);
		addMetaDataItem(items, LMD_FIELD_OPEN_ACCESS, openAccessCheckList);
		addMetaDataItem(items, LMD_FIELD_CATEGORY, category);
		
		addClassifications(items);
		
		addMetaDataItem(items, LMD_FIELD_SUBMITTED_DATE, submittedDate);
		addMetaDataItem(items, LMD_FIELD_DECISION_DATE, decisionDate);
		addMetaDataItem(items, LMD_FIELD_EXPORT_DATE, exportDate);
		addMetaDataItem(items, LMD_FIELD_INSPEC_REQUIRED, inspecRequired);
		addMetaDataItem(items, LMD_FIELD_LICENCE_TYPE, licenseType);
		addMetaDataItem(items, LMD_FIELD_ARTICLE_NOTES, articleNotes);
		addMetaDataItem(items, LMD_FIELD_SUBMISSION_TYPE, submissionType);
		addMetaDataItem(items, LMD_FIELD_DISPLAY_ARTICLE_NOTES_ICON, articleNotesIcon);
		addMetaDataItem(items, LMD_FIELD_CATEGORY_CODE, categoryCodes);
		addMetaDataItem(items, LMD_FIELD_JOURNAL_CODE, journalCode);
		addMetaDataItem(items, LMD_FIELD_SUPPLEMENTARY_MATERIAL, supplementaryMaterial);
		
		return items;
	}

	private void addClassifications(List<MetaDataItem> items) {
		if (classifications != null){
			for (String classification : classifications){
				addMetaDataItem(items, LMD_FIELD_CLASSIFICATIONS, classification);	
			}			
		}		
	}

	private void addAuthorMetadata(List<MetaDataItem> items) {
		if (author != null){
			addMetaDataItem(items, LMD_FIELD_AUTHOR_SALUTATION, author.getSalutation());
			addMetaDataItem(items, LMD_FIELD_AUTHOR_FIRST_NAME, author.getFirstname());
			addMetaDataItem(items, LMD_FIELD_AUTHOR_SURNAME, author.getSurname());
			addMetaDataItem(items, LMD_FIELD_AUTHOR_EMAIL, author.getEmail());
		}		
	}

	private String convertToYesNo(boolean value){
		return value ? LMD_VALUE_YES : LMD_VALUE_NO;
	}
	
	public void makeArticleAvailable(){
		articleAvailable = LMD_VALUE_YES;
	}
	
	public void makeArticleNotAvailable(){
		articleAvailable = LMD_VALUE_NO;
	}
	
	public void setOnlinePublishedDate(String onlinePublishedDate) {
		this.onlinePublishedDate = onlinePublishedDate;
	}
	
	public void setAuthor(ArticleAuthor author){
		this.author = author;
	}

	private void addMetaDataItem(List<MetaDataItem> items, String name,
			String value) {
		if (value != null) {
			items.add(new MetaDataItem(name, value));
		}
	}

	public void setDoi(String doi) {
		this.doi = doi;
		
	}

	public void setAuthorInstitution(String authorInstitution) {
		this.authorInstitution = authorInstitution;
		
	}

	public void sertAuthorCountry(String authorCountry) {
		this.authorCountry = authorCountry;
	}
	
	public void setTypesetPages(String typesetPages) {
		this.typesetPages = typesetPages;
	}

	public void setAssigned(boolean value) {
		assigned = value;		
	}

	public void setIssueCode(String issueCode) {
		this.issueCode = issueCode;
		
	}

	public void setArticleTitle(String articleTitle) {
		this.articleTitle = articleTitle;
		
	}

	public void setArticleType(String articleType) {
		this.articleType = articleType;
		
	}

	public void isSpecialIssue(boolean specialIssue) {
		this.specialIssue = convertToYesNo(specialIssue);
		
	}

	public void setSpecialIssueTitle(String specialIssueTitle) {
		this.specialIssueTitle = specialIssueTitle;
		
	}

	public void setOpenAccessCheckList(boolean openAccessCheckList) {
		this.openAccessCheckList = convertToYesNo(openAccessCheckList);
		
	}

	public void setCategory(String category) {
		this.category = category;
		
	}

	public void setClassifications(List<String> classifications) {
		this.classifications = classifications;
		
	}

	public void setSubmittedDate(String submittedDate) {
		this.submittedDate = submittedDate;
		
	}

	public void setDecisionDate(String decisionDate) {
		this.decisionDate = decisionDate;
	}

	public void setExportDate(String exportDate) {
		this.exportDate = exportDate;
		
	}

	public void setInspecRequired(boolean value) {
		inspecRequired = convertToYesNo(value);
		
	}

	public void setLicenseType(String licenseType) {
		this.licenseType = licenseType;
		
	}

	public void setArticleNotes(String articleNotes){
		this.articleNotes = articleNotes;
		
	}

	public void setSubmissionType(String submissionType) {
		this.submissionType = submissionType; 
		
	}

	public void setArticleNotesIcon(boolean value) {
		articleNotesIcon = convertToYesNo(value);
		
	}

	public void setCategoryCode(String categoryCodes) {
		this.categoryCodes = categoryCodes;
		
	}

	public void setJournalCode(String jounralCode) {
		this.journalCode = jounralCode;
		
	}

	public void setSupplementaryMaterial(boolean supplementaryMaterial) {
		this.supplementaryMaterial = convertToYesNo(supplementaryMaterial);
		
	}

	public void setPrePublishedDate(String prePublishedDate) {
		this.prePublishedDate = prePublishedDate;
	}
	
}
