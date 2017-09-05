package org.theiet.rsuite.journals.domain.issues.publish.common.areatree;

public class IssueArticleInfo {

	private String articleDOI;
	
	private int firstPage;
	
	private int lastPage;
	
	IssueArticleInfo(String articleDOI, int firstPage){
		this.articleDOI = articleDOI;
		this.firstPage = firstPage;
	}
	
	void setLastPage(int lastPage){
		this.lastPage = lastPage;
	}

	public String getArticleDOI() {
		return articleDOI;
	}

	public void setArticleDOI(String articleDOI) {
		this.articleDOI = articleDOI;
	}

	public int getFirstPage() {
		return firstPage;
	}

	public void setFirstPage(int firstPage) {
		this.firstPage = firstPage;
	}

	public int getLastPage() {
		return lastPage;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("doi: ");
		sb.append(articleDOI);
		sb.append(" pages: ").append(firstPage).append("-").append(lastPage);
		return sb.toString();
	}
}
