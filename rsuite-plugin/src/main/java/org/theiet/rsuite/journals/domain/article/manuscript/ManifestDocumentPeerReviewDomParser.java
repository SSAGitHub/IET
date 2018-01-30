package org.theiet.rsuite.journals.domain.article.manuscript;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;
import org.theiet.rsuite.journals.domain.article.datype.ArticleAuthor;
import org.theiet.rsuite.journals.domain.article.manuscript.ManifestDocument.Builder;
import org.w3c.dom.Node;

import com.reallysi.rsuite.api.RSuiteException;

public class ManifestDocumentPeerReviewDomParser implements ManifestDocumentDomParser {

	private ManifestXPath manifestXPath;

	public ManifestDocumentPeerReviewDomParser(ManifestXPath manifestXPath) {
		this.manifestXPath = manifestXPath;
	}

	@Override
	public ManifestDocument parseManifestDOM() throws RSuiteException {
		ManifestDocument.Builder manifestBuilder = new ManifestDocument.Builder();

		String articleId = manifestXPath
				.getValueFromManifestDocument("/article/article-meta/article-id[@pub-id-type = \"manuscript\"]");
		manifestBuilder.articleId(articleId);

		String articleTitle = manifestXPath
				.getValueFromManifestDocument("/article/article-meta/title-group/article-title");
		manifestBuilder.articleTitle(articleTitle);

		parseArticleAuthor(manifestBuilder);

		String manuscriptType = manifestXPath.getValueFromManifestDocument("/article/@article-type");
		
		manuscriptType = mapArticleTypeManuscriptCentral(manuscriptType);
			
		
		
		manifestBuilder.manuscriptType(manuscriptType);

		String category = manifestXPath
				.getValueFromManifestDocument(createConfigurableDataXpath("Category Selections"));
		manifestBuilder.category(category);

		parseSpecialIssue(manifestBuilder);

		boolean openAcessCheckList = manifestXPath.getBooleanValueFromManifestDocument(
				createConfigurableDataXpath("Did the author select an open access licence type?"));
		manifestBuilder.openAcessCheckList(openAcessCheckList);

		List<String> classifications = manifestXPath
				.getMultiValueFromManifestDocument(createConfigurableDataXpath("Categories"));
		manifestBuilder.classifications(classifications);

		if (classifications != null) {
			String categoryCodes = StringUtils.join(classifications, ", ");
			manifestBuilder.categoryCodes(categoryCodes);
		}

		String submittedDate = manifestXPath.getValueFromManifestDocument(
				createDateXpath("/article/article-meta/history/date[@date-type = \"received\"]"));
		manifestBuilder.submittedDate(submittedDate);

		String decisionDate = manifestXPath.getValueFromManifestDocument(
				createDateXpath("/article/article-meta/history/date[@date-type = \"accepted\"]"));
		manifestBuilder.decisionDate(decisionDate);

		parseExportDate(manifestBuilder);

		String licenseType = manifestXPath
				.getValueFromManifestDocument("/article/article-meta/permissions/license/license-p");
		manifestBuilder.licenseType(licenseType);

		boolean supplementaryMaterials = manifestXPath.getBooleanValueFromManifestDocument(
				createConfigurableDataXpath("Are you including supplementary material with your submission?"));
		manifestBuilder.supplementaryMaterials(supplementaryMaterials);

		String submissionType = manifestXPath.getValueFromManifestDocument(
				createConfigurableDataXpath("Was this a direct submission or a transfer?"));
		manifestBuilder.submissionType(submissionType);

		return manifestBuilder.build();
	}

	private String mapArticleTypeManuscriptCentral(String manuscriptType) {
		String mapping = manuscriptType;
		if ("research-article".equals(manuscriptType)) {
			mapping = "Research Paper";
		}else if ("news-item".equals(manuscriptType)) {
			mapping = "News Items";
		}else {
			mapping = manuscriptType.replace("-", " ");
			mapping = WordUtils.capitalizeFully(mapping);
		}
		return mapping;
	}

	private void parseExportDate(Builder manifestBuilder) throws RSuiteException {
		String exportDate = manifestXPath.getValueFromManifestDocument("/article_set/article/@export_date");
//TODO
//		int spaceIndex = exportDate.indexOf(' ');
//		if (spaceIndex > -1) {
//			exportDate = exportDate.substring(0, spaceIndex);
//		}
//
		manifestBuilder.exportDate(exportDate);
	}

	private void parseSpecialIssue(Builder manifestBuilder) throws RSuiteException {
		String specialIssueFlag = manifestXPath
				.getValueFromManifestDocument(createConfigurableDataXpath("Is this paper for a Special Issue?"));
		boolean isSpecialIssue = manifestXPath.convertYesNoToBoolean(specialIssueFlag);
		manifestBuilder.isSpecialIssue(isSpecialIssue);

		if (isSpecialIssue) {
			String specialIssueTitle = manifestXPath.getValueFromManifestDocument(
					createConfigurableDataXpath("Please enter the title of the Special Issue:"));
			manifestBuilder.specialIssueTitle(specialIssueTitle);
		}
	}

	private void parseArticleAuthor(Builder manifestBuilder) throws RSuiteException {

		Node authorNode = manifestXPath.getNodeFromManifestDocument(
				"/article/article-meta/contrib-group/contrib[@contrib-type=\"author\" and @corresp=\"yes\"]");

		String author_email = manifestXPath.getValueFromManifestDocument(authorNode, "email[1]");
		String author_salutation = manifestXPath.getValueFromManifestDocument(authorNode, "name/salutation");
		String author_surname = manifestXPath.getValueFromManifestDocument(authorNode, "name/surname");
		String author_first = manifestXPath.getValueFromManifestDocument(authorNode, "name/given-name");

		ArticleAuthor author = new ArticleAuthor(author_salutation, author_first, author_surname, author_email);
		manifestBuilder.author(author);
	}

	private String createConfigurableDataXpath(String fieldName) {
		StringBuilder sb = new StringBuilder("/article/article-meta/configurable_data_fields/custom_fields[@cd_name='");
		sb.append(fieldName).append("']//@cd_value");
		return sb.toString();
	}

	private String createDateXpath(String baseQuery) {
		String dateXpath = "/concat(year,'-', month, '-', day)";
		return baseQuery + dateXpath;
	}

}
