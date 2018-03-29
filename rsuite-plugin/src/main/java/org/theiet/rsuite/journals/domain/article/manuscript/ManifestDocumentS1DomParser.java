package org.theiet.rsuite.journals.domain.article.manuscript;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.theiet.rsuite.journals.domain.article.datype.ArticleAuthor;
import org.theiet.rsuite.journals.domain.article.manuscript.ManifestDocument.Builder;
import org.w3c.dom.Node;

import com.reallysi.rsuite.api.RSuiteException;

public class ManifestDocumentS1DomParser implements ManifestDocumentDomParser {

	private ManifestXPath manifestXPath;

	public ManifestDocumentS1DomParser(ManifestXPath manifestXPath) {
		this.manifestXPath = manifestXPath;
	}

	@Override
	public ManifestDocument parseManifestDOM() throws RSuiteException {
		ManifestDocument.Builder manifestBuilder = new ManifestDocument.Builder(ManifestType.S1);

		String articleId = manifestXPath.getValueFromManifestDocument("/article_set/article/@ms_no");
		manifestBuilder.articleId(articleId);

		String articleTitle = manifestXPath.getValueFromManifestDocument("/article_set/article/article_title");
		manifestBuilder.articleTitle(articleTitle);

		parseArticleAuthor(manifestBuilder);

		String manuscriptType = manifestXPath
				.getValueFromManifestDocument(createConfigurableDataXpath("Manuscript Type"));
		manifestBuilder.manuscriptType(manuscriptType);

		String category = manifestXPath.getValueFromManifestDocument(createConfigurableDataXpath("EL Category"));
		manifestBuilder.category(category);

		parseSpecialIssue(manifestBuilder);

		boolean openAcessCheckList = manifestXPath
				.getBooleanValueFromManifestDocument(createConfigurableDataXpath("Open Access Checklist"));
		manifestBuilder.openAcessCheckList(openAcessCheckList);

		List<String> classifications = manifestXPath
				.getMultiValueFromManifestDocument(createConfigurableDataXpath("Categories"));
		manifestBuilder.classifications(classifications);

		if (classifications != null) {
			String categoryCodes = StringUtils.join(classifications, ", ");
			manifestBuilder.categoryCodes(categoryCodes);
		}

		String submittedDate = manifestXPath.getValueFromManifestDocument(
				createDateXpath("/article_set/article/history/ms_id[./rev_id = '0']/submitted_date"));
		manifestBuilder.submittedDate(submittedDate);

		String decisionDate = manifestXPath
				.getValueFromManifestDocument(createDateXpath("/article_set/article/history/ms_id[1]/decision_date"));
		manifestBuilder.decisionDate(decisionDate);

		parseExportDate(manifestBuilder);

		String licenseType = manifestXPath.getValueFromManifestDocument(createConfigurableDataXpath("Licence Type"));
		manifestBuilder.licenseType(licenseType);

		boolean supplementaryMaterials = manifestXPath.getBooleanValueFromManifestDocument(
				createConfigurableDataXpath("Are you attaching supplementary material?"));
		manifestBuilder.supplementaryMaterials(supplementaryMaterials);

		String submissionType = manifestXPath.getValueFromManifestDocument(
				createConfigurableDataXpath("Is this paper a transfer from another journal or direct submission?"));
		manifestBuilder.submissionType(submissionType);

		return manifestBuilder.build();
	}

	private void parseExportDate(Builder manifestBuilder) throws RSuiteException {
		String exportDate = manifestXPath.getValueFromManifestDocument("/article_set/article/@export_date");

		int spaceIndex = exportDate.indexOf(' ');
		if (spaceIndex > -1) {
			exportDate = exportDate.substring(0, spaceIndex);
		}

		manifestBuilder.exportDate(exportDate);
	}

	private void parseSpecialIssue(Builder manifestBuilder) throws RSuiteException {
		String specialIssueFlag = manifestXPath
				.getValueFromManifestDocument(createConfigurableDataXpath("Is this paper for a Special Issue?"));
		boolean isSpecialIssue = manifestXPath.convertYesNoToBoolean(specialIssueFlag);
		manifestBuilder.isSpecialIssue(isSpecialIssue);

		if (isSpecialIssue) {
			String specialIssueTitle = manifestXPath
					.getValueFromManifestDocument(createConfigurableDataXpath("Special Issue Title"));
			manifestBuilder.specialIssueTitle(specialIssueTitle);
		}
	}

	private void parseArticleAuthor(Builder manifestBuilder) throws RSuiteException {

		Node authorNode = manifestXPath
				.getNodeFromManifestDocument("/article_set/article/author_list/author[@corr='true']");

		String author_email = manifestXPath.getValueFromManifestDocument(authorNode, "email[@addr_type='primary']");
		String author_salutation = manifestXPath.getValueFromManifestDocument(authorNode, "salutation");
		String author_surname = manifestXPath.getValueFromManifestDocument(authorNode, "last_name");
		String author_first = manifestXPath.getValueFromManifestDocument(authorNode, "first_name");

		ArticleAuthor author = new ArticleAuthor(author_salutation, author_first, author_surname, author_email);
		manifestBuilder.author(author);
	}

	private String createConfigurableDataXpath(String fieldName) {
		StringBuilder sb = new StringBuilder("/article_set/article/configurable_data_fields/custom_fields[@cd_code='");
		sb.append(fieldName).append("']//@cd_value");
		return sb.toString();
	}

	private String createDateXpath(String baseQuery) {
		String dateXpath = "/concat(year,'-', month, '-', day)";
		return baseQuery + dateXpath;
	}

}
