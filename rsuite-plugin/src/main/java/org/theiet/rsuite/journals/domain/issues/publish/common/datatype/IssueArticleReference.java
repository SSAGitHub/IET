package org.theiet.rsuite.journals.domain.issues.publish.common.datatype;

public class IssueArticleReference {

	private String articleReference;
	
	private boolean isSpecialIssue;

	public IssueArticleReference(String articleReference, boolean isSpecialIssue) {
		this.articleReference = articleReference;
		this.isSpecialIssue = isSpecialIssue;
	}

	public String getArticleReference() {
		return articleReference;
	}

	public boolean isSpecialIssue() {
		return isSpecialIssue;
	}
		
}
