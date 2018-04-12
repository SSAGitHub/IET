package org.theiet.rsuite.journals.domain.article.manuscript.acceptance;

import static org.mockito.Mockito.*;

import java.io.File;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.theiet.rsuite.journals.domain.article.delivery.digitallibrary.ArticleDigitalLibrary;
import org.theiet.rsuite.journals.domain.article.manuscript.ManifestType;

import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.service.XmlApiManager;

public class PublishOnAcceptanceTestable extends PublishOnAcceptance {

	IScholarOnePdfTransformer scholarOnePdfTransformer;

	public PublishOnAcceptanceTestable(XmlApiManager xmlApiManager, ArticleDigitalLibrary digitalLibrary,
			ManifestType manifestType) throws RSuiteException {
		super(xmlApiManager, digitalLibrary, manifestType);

		Answer<Integer> answer = new Answer<Integer>() {
			public Integer answer(InvocationOnMock invocation) throws Throwable {
				File file = (File) invocation.getArguments()[1];
				file.createNewFile();
				return 4;
			}
		};

		scholarOnePdfTransformer = mock(IScholarOnePdfTransformer.class);

		doAnswer(answer)
				.when(scholarOnePdfTransformer).createPdfForPublishOnAcceptance(any(File.class), any(File.class));

	}

	@Override
	protected IScholarOnePdfTransformer createJournalCustomLibraryFactory() throws RSuiteException {
		return scholarOnePdfTransformer;
	}
}
