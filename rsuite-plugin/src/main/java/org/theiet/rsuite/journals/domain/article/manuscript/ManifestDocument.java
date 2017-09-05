package org.theiet.rsuite.journals.domain.article.manuscript;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.lang.StringUtils;
import org.theiet.rsuite.journals.domain.article.datype.ArticleAuthor;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.xml.XPathEvaluator;
import com.reallysi.rsuite.service.XmlApiManager;

public class ManifestDocument {

	private Document manifestDoc;
	
	private XPathEvaluator xPathEval;
	
	private String articleId;
	
	private boolean supplementaryMaterials;
	
	private String articleTitle;

	private String manuscriptType;
	
	private ArticleAuthor author;
	
	private boolean isSpecialIssue;
	
	private String specialIssueTitle;

	private boolean openAcessCheckList;

	private String category;

	private List<String> classifications;

	private String submittedDate;

	private String decisionDate;

	private String exportDate;

	private String licenseType;

	private String submissionType;

	private String categoryCodes;
	
	public ManifestDocument(XmlApiManager xmlApiMgr, File manifestFile) throws RSuiteException {
		manifestDoc = parseManifestFile(xmlApiMgr, manifestFile);
		
		xPathEval = xmlApiMgr.getXPathEvaluator();
		parseFields();
	}
	
	private void parseFields() throws RSuiteException{
		articleId = getValueFromManifestDocument("/article_set/article/@ms_no");
		
		articleTitle = getValueFromManifestDocument("/article_set/article/article_title");
		
		parseArticleAuthor();
		
		manuscriptType = getValueFromManifestDocument(createConfigurableDataXpath("Manuscript Type"));
		
		category = getValueFromManifestDocument(createConfigurableDataXpath("EL Category"));
		
		parseSpecialIssue();
		
		openAcessCheckList = getBooleanValueFromManifestDocument(createConfigurableDataXpath("Open Access Checklist"));
		
		classifications = getMultiValueFromManifestDocument(xPathEval, manifestDoc, createConfigurableDataXpath("Categories"));
		if (classifications != null){
			categoryCodes = StringUtils.join(classifications, ", ");
		}
		
		
		submittedDate = getValueFromManifestDocument(createDateXpath("/article_set/article/history/ms_id[./rev_id = '0']/submitted_date"));
		decisionDate = getValueFromManifestDocument(createDateXpath("/article_set/article/history/ms_id[1]/decision_date"));
		
		parseExportDate();
		
		licenseType = getValueFromManifestDocument(createConfigurableDataXpath("Licence Type"));
		
		
		supplementaryMaterials = getBooleanValueFromManifestDocument(createConfigurableDataXpath("Are you attaching supplementary material?"));
		
		submissionType = getValueFromManifestDocument(createConfigurableDataXpath("Is this paper a transfer from another journal or direct submission?"));
	}

	private void parseExportDate() throws RSuiteException {
		exportDate = getValueFromManifestDocument("/article_set/article/@export_date"); 

		int spaceIndex = exportDate.indexOf(' ');
		if (spaceIndex > -1){
			exportDate = exportDate.substring(0, spaceIndex);
		}
		
	}

	private void parseSpecialIssue() throws RSuiteException {
		String specialIssueFlag = getValueFromManifestDocument(createConfigurableDataXpath("Is this paper for a Special Issue?"));
		isSpecialIssue = convertYesNoToBoolean(specialIssueFlag);
				
		if (isSpecialIssue){
			specialIssueTitle =  getValueFromManifestDocument(createConfigurableDataXpath("Special Issue Title"));
		}
	}

	private boolean convertYesNoToBoolean(String value) {
		String normalizedValue = StringUtils.isBlank(value) ? "no" : value.toLowerCase();
		if ("yes".equalsIgnoreCase(normalizedValue)){
			return true;
		}
		
		return false;
	}

	private void parseArticleAuthor() throws RSuiteException {
		Node authorNode = getNodeFromManifestDocument(xPathEval, manifestDoc, "/article_set/article/author_list/author[@corr='true']");
		
		String author_email = getValueFromManifestDocument(xPathEval, authorNode, "email[@addr_type='primary']");
		String author_salutation = getValueFromManifestDocument(xPathEval, authorNode, "salutation");
		String author_surname = getValueFromManifestDocument(xPathEval, authorNode, "last_name");
		String author_first = getValueFromManifestDocument(xPathEval, authorNode, "first_name");
		
		author = new ArticleAuthor(author_salutation, author_first, author_surname, author_email);
	}

	private String createConfigurableDataXpath(String fieldName){											  
		StringBuilder sb = new StringBuilder("/article_set/article/configurable_data_fields/custom_fields[@cd_code='");
				sb.append(fieldName).append("']//@cd_value");
		return sb.toString();		
	}
	
	private String createDateXpath(String baseQuery){
		String dateXpath = "/concat(year,'-', month, '-', day)";
		return baseQuery + dateXpath;
	}

	public String getArticleTitle() {
		return articleTitle;
	}

	public String getArticleId() {
		return articleId;
	}
	
	public boolean getSupplementaryMaterial(){
		return supplementaryMaterials; 
	}
	

	public String getManuscriptType() {
		return manuscriptType;
	}
	
	public ArticleAuthor getArticleAuthor() {
		return author;
	}
	
	
	public boolean isSpecialIssue() {
		return isSpecialIssue;
	}

	private Document parseManifestFile(XmlApiManager xmlApiMgr, File manifestFile)
			throws RSuiteException {
		try{
			return xmlApiMgr.getW3CDomFromFile(manifestFile, false);			
		}catch (ParserConfigurationException | IOException | SAXException e ){
			throw new RSuiteException(0, "Unable to parse " + manifestFile.getAbsolutePath(), e);
		}
	}
	
	private boolean getBooleanValueFromManifestDocument(String xpath) throws RSuiteException{
		String value = getValueFromManifestDocument(xPathEval, manifestDoc, xpath);
		return convertYesNoToBoolean(value);
	}
	
	private String getValueFromManifestDocument(String xpath) throws RSuiteException{
		return getValueFromManifestDocument(xPathEval, manifestDoc, xpath);
	}
	
	private String getValueFromManifestDocument(
			XPathEvaluator xPathEval,
			Node contextNode,
			String xPath) throws RSuiteException {
		return getValueFromManifestDocument(xPathEval, contextNode, xPath, false);
	}
	
	private List<String> getMultiValueFromManifestDocument(XPathEvaluator xPathEval,
			Node contextNode,
			String xPath) throws RSuiteException{
		
		String values[] = xPathEval.executeXPathToStringArray(xPath, contextNode);
		if (values != null && values.length > 0){
			return Arrays.asList(values);
		}
		
		return null;
	}
	
	private String getValueFromManifestDocument(
			XPathEvaluator xPathEval,
			Node contextNode,
			String xPath, boolean returnArray) throws RSuiteException {
		
		String value = null;
		if (returnArray) {
			value = "";
			String values[] = xPathEval.executeXPathToStringArray(xPath, contextNode);
			for (int i = 0; i < values.length; i++) {
				value += (i == 0 ? "" : " ") + values[i];
			}
			value = value.replaceAll(" ", ", ");
		} else {
			value = xPathEval.executeXPathToString(xPath, contextNode);
		}
		
		
		if (StringUtils.isBlank(value)){
			value = null;
		}
		
		return value;
	}
	
	public static Node getNodeFromManifestDocument(
			XPathEvaluator xPathEval,
			Node contextNode,
			String xPath) throws RSuiteException {
		
		
		Node value = xPathEval.executeXPathToNode(xPath, contextNode);
		
		if (value == null){
			throw new RSuiteException("Unable to locate " + xPath + " in the manifest file");
		}
		
		return value;
	}

	public String getSpecialIssueTitle() {
		return specialIssueTitle;
	}

	public boolean isOpenAccessCheckList() {
		return openAcessCheckList;
	}

	public String getCategory() {
		return category;
	}

	public List<String> getClassifications() {
		return classifications;
	}

	public String getSubmittedDate() {
		return fixIfEmptyDate(submittedDate);
	}

	public String getDecisionDate() {
		return fixIfEmptyDate(decisionDate);
	}


	private String fixIfEmptyDate(String date) {
		if ("--".equals(date)){
			date = "";
		}
		return date;
	}

	public String getExportDate() {
		return exportDate;
	}

	public String getLicenseType() {
		return licenseType;
	}

	public String getSubmissionType() {
		return submissionType;
	}

	public String getCategoryCodes() {
		return categoryCodes;
	}
}	
