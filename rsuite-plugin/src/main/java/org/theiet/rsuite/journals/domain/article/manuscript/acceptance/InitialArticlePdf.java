package org.theiet.rsuite.journals.domain.article.manuscript.acceptance;

import java.io.File;

public class InitialArticlePdf {

	private File articlePDF;

	private int numberOfPages;

	public InitialArticlePdf(File articlePDF, int numberOfPages) {
		super();
		this.articlePDF = articlePDF;
		this.numberOfPages = numberOfPages;
	}

	public File getArticlePDF() {
		return articlePDF;
	}

	public int getNumberOfPages() {
		return numberOfPages;
	}

}
