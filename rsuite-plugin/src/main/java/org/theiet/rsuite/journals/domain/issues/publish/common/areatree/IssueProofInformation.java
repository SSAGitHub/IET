package org.theiet.rsuite.journals.domain.issues.publish.common.areatree;


public interface IssueProofInformation {

	public int getTotalPageNumber();

	public int getAdvertPagesToAdd();
	
	public IssueArticlesInformation getArticlesInformation();

	public int getTocPageNumber();
	
	public int getInstructPageNumber();
	
	public boolean shouldExtractInstructPage();
	
	public int getPrelimsPageNumber();
	
	public int getLastNumberedPage();
}
