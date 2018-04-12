package org.theiet.rsuite.journals.domain.article.manuscript.acceptance;

import java.io.File;

import com.reallysi.rsuite.api.RSuiteException;

public interface IScholarOnePdfTransformer {

	public int createPdfForPublishOnAcceptance(File scholarOnePdf, File outputPdfFile) throws RSuiteException;

}
