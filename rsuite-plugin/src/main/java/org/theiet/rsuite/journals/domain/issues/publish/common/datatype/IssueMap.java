package org.theiet.rsuite.journals.domain.issues.publish.common.datatype;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.reallysi.rsuite.api.RSuiteException;

public class IssueMap {

	private IssueMap() {
	}

	private static final String FILE_NAME_ISSUE_MAP_XML = "issue_map.xml";

	public static String getIssueMapFileName() {
		return FILE_NAME_ISSUE_MAP_XML;
	}

	public static void createIssueMap(List<IssueArticleReference> articles, File exportFolder)
			throws RSuiteException {

		try {

			String issueMap = convertArticleListToMap(articles);
			File issueMapFile = new File(exportFolder, "issue_map.xml");

			FileUtils.writeStringToFile(issueMapFile, issueMap, "utf-8");

		} catch (IOException e) {
			throw new RSuiteException(0, "Unable to create issue map", e);
		}

	}

	private static String convertArticleListToMap(List<IssueArticleReference> articles) {
		StringBuilder issueMap = new StringBuilder("<issuemap>");
		issueMap.append("<prelimpageref href=\"").append("instruct_page.xml").append("\" />");
		for (IssueArticleReference articleReference : articles) {
			issueMap.append("<articleref href=\"").append(articleReference.getArticleReference())
					.append("\" specialIssue=\"" + articleReference.isSpecialIssue() + "\" />");
		}
		issueMap.append("</issuemap>");
		return issueMap.toString();
	}

}
