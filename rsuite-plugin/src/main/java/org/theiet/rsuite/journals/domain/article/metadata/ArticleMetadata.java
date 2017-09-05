package org.theiet.rsuite.journals.domain.article.metadata;

import static org.theiet.rsuite.journals.domain.article.metadata.ArticleMetadataFields.*;

import org.theiet.rsuite.utils.StringUtils;

import com.reallysi.rsuite.api.ContentAssembly;
import com.reallysi.rsuite.api.RSuiteException;

public class ArticleMetadata {

	private ContentAssembly articleCa;

	public ArticleMetadata(ContentAssembly articleCa) {
		this.articleCa = articleCa;
	}


	public String getArticleTitle() throws RSuiteException {
		return getLmd(LMD_FIELD_ARTICLE_TITLE);
	}
	
	public String getOnlinePublishDate() throws RSuiteException {
		return getLmd(LMD_FIELD_ONLINE_PUBLISHED_DATE);
	}
	
	public String getDoi() throws RSuiteException {
		return getLmd(LMD_FIELD_DOI);
	}
	
	private String getLmd(String lmdName) throws RSuiteException {
		return articleCa.getLayeredMetadataValue(lmdName);
	}

	public String getAuthorEmail() throws RSuiteException {
		return getLmd(LMD_FIELD_AUTHOR_EMAIL);		
	}


	public boolean isSpecialIssue() throws RSuiteException {
		return getBooleanFromYesNoValue(getLmd(LMD_FIELD_IS_SPECIAL_ISSUE));	
	}
	
	private boolean getBooleanFromYesNoValue(String yesNoValue){
		return "yes".equalsIgnoreCase(yesNoValue);
	}
	
	public String getArticleType() throws RSuiteException{
		return getLmd(LMD_FIELD_ARTICLE_TYPE);
	}
	
	public String getTypesetPages() throws RSuiteException{
		return getLmd(LMD_FIELD_TYPESET_PAGES);
	}
	
	public String getSpecialIssueTitle() throws RSuiteException{
		return getLmd(LMD_FIELD_SPECIAL_ISSUE_TITLE);
	}
	
	public String getSpecialIssue() throws RSuiteException{
		return getLmd(LMD_FIELD_IS_SPECIAL_ISSUE);
	}
	
	public String getOpenAcess() throws RSuiteException{
		return getLmd(LMD_FIELD_OPEN_ACCESS);
	}
	
	public String getInspecRequired() throws RSuiteException{
		return getLmd(LMD_FIELD_INSPEC_REQUIRED);
	}
	
	public boolean isInspecRequired() throws RSuiteException{
		String lmdInspectRequired =  getInspecRequired();
		return convertYesNoToBoolean(lmdInspectRequired);
	}

	private boolean convertYesNoToBoolean(String lmdInspectRequired) {
		if (StringUtils.isBlank(lmdInspectRequired) || LMD_VALUE_NO.equals(lmdInspectRequired)) {
				return false;
		}
		
		return true;
	}

	public boolean hasSupplementaryMaterial() throws RSuiteException {
		return convertYesNoToBoolean(getLmd(LMD_FIELD_SUPPLEMENTARY_MATERIAL));
	}

	public boolean isAvailable() throws RSuiteException {
		return convertYesNoToBoolean(getLmd(LMD_FIELD_ARTICLE_AVAILABLE));
		
	}
	
	public String getPrePublishDate() throws RSuiteException {
		return getLmd(LMD_FIELD_PRE_PUBLISHED_DATE);
	}
	
	
	public String getExportDate() throws RSuiteException {
		return getLmd(LMD_FIELD_EXPORT_DATE);
	}
}
