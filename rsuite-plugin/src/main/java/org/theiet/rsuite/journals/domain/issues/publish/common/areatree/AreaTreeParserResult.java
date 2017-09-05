package org.theiet.rsuite.journals.domain.issues.publish.common.areatree;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

class AreaTreeParserResult implements IssueProofInformation {

	private static final int PAGE_DIVISION_NUMER = 4;

	private int initialTotalPageNumber = 0;

	private String currentStartArticle = null;

	private String currentEndArticle = null;

	private int currentPageNumber = 0;

	private int startTOCPageNumber = 0;

	private int endTOCPageNumber = 0;

	private int startInstructPageNumber = 0;

	private int endInstructPageNumber = 0;

	private Map<String, IssueArticleInfo> articleInfoMap = new LinkedHashMap<String, IssueArticleInfo>();

	public int getTotalPageNumber() {

		int finalTotalPagNumber = initialTotalPageNumber;

		if (shouldExtractInstructPage()) {
			finalTotalPagNumber = finalTotalPagNumber - getInstructPageNumber();
		}
		return finalTotalPagNumber + getAdvertPagesToAdd();
	}

	void incrementPageNumbers() {
		initialTotalPageNumber++;
	}

	void endPDFPage() {

		if (currentStartArticle != null) {
			IssueArticleInfo articleAreaTreeInfo = new IssueArticleInfo(
					currentStartArticle, currentPageNumber);
			articleInfoMap.put(currentStartArticle, articleAreaTreeInfo);
		}

		if (currentEndArticle != null) {
			articleInfoMap.get(currentEndArticle)
					.setLastPage(currentPageNumber);
		}

		cleanUpCurrentData();
	}

	void startArticle(String articleDoi) {
		currentStartArticle = articleDoi;
	}

	void endArticle(String articleDoi) {
		currentEndArticle = articleDoi;
	}

	void startTOC() {
		startTOCPageNumber = initialTotalPageNumber;
	}

	void endTOC() {
		endTOCPageNumber = initialTotalPageNumber;
	}

	void currentPage(String pageNumber) {
		currentPageNumber = Integer.parseInt(pageNumber);
	}

	private void cleanUpCurrentData() {
		currentStartArticle = null;
		currentEndArticle = null;
		currentPageNumber = 0;
	}

	private int getPagesToAdd() {

		int finalTotalPageNumber = initialTotalPageNumber;

		if (shouldExtractInstructPage()) {
			finalTotalPageNumber = finalTotalPageNumber - getInstructPageNumber();
		}

		int modulo = finalTotalPageNumber % PAGE_DIVISION_NUMER;

		if (modulo > 0) {
			return PAGE_DIVISION_NUMER - modulo;
		}

		return 0;
	}

	@Override
	public int getAdvertPagesToAdd() {
		return getPagesToAdd();
	}

	@Override
	public IssueArticlesInformation getArticlesInformation() {
		return new IssueArticlesInformation(articleInfoMap);
	}

	@Override
	public int getTocPageNumber() {
		return endTOCPageNumber - startTOCPageNumber + 1;
	}

	public void startInstructPage() {
		startInstructPageNumber = initialTotalPageNumber;

	}

	public void endInstructPage() {
		endInstructPageNumber = initialTotalPageNumber;
	}

	@Override
	public int getInstructPageNumber() {
		return endInstructPageNumber - startInstructPageNumber + 1;
	}

	@Override
	public boolean shouldExtractInstructPage() {

		int tocPageNumber = getTocPageNumber();

		if (tocPageNumber > 1) {
			return true;
		}

		return false;
	}

	@Override
	public int getPrelimsPageNumber() {

		if (shouldExtractInstructPage()) {
			return getTocPageNumber();
		}

		return getTocPageNumber() + getInstructPageNumber();
	}

	@Override
	public int getLastNumberedPage() {
		
		IssueArticlesInformation articlesInformation = getArticlesInformation();

		List<IssueArticleInfo> articlesInfo = articlesInformation
				.getArticlesInfo();

		if (articlesInfo.size() > 0) {
			IssueArticleInfo articleAreaTreeInfo = articlesInfo
					.get(articlesInfo.size() - 1);
			return articleAreaTreeInfo.getLastPage();
		}
		
		return 0;
	}
}
