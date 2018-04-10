package org.theiet.rsuite.journals.domain.article.manuscript.acceptance;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.theiet.rsuite.journals.domain.article.Article;
import org.theiet.rsuite.journals.domain.article.delivery.digitallibrary.*;
import org.theiet.rsuite.journals.domain.article.manuscript.ManifestType;
import org.theiet.rsuite.journals.domain.article.manuscript.ManuscriptPackage;
import org.theiet.rsuite.journals.domain.journal.Journal;

import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.User;
import com.reallysi.rsuite.api.extensions.ExecutionContext;
import com.rsicms.projectshelper.utils.ProjectTransformationUtils;

public class PublishOnAcceptance {

	private static final String XSLT_URI_MANUSCRIPT_METADATA_TO_ARTICLE = "rsuite:/res/plugin/iet/xslt/journals/manuscript/manuscript-metadata-to-jats-article.xsl";

	private static final String XSLT_URI_MANUSCRIPT_RV_METADATA_TO_ARTICLE = "rsuite:/res/plugin/iet/xslt/journals/manuscript/manuscript-rv-metadata-to-jats-article.xsl";

	private ExecutionContext context;

	private User user;

	private Log logger;

	private String xsltTransformPath;

	public PublishOnAcceptance(ExecutionContext context, User user, Log logger, ManifestType manifestType) {
		this.context = context;
		this.user = user;
		this.logger = logger;
		xsltTransformPath = manifestType == ManifestType.S1 ? XSLT_URI_MANUSCRIPT_METADATA_TO_ARTICLE
				: XSLT_URI_MANUSCRIPT_RV_METADATA_TO_ARTICLE;
	}

	public void publishOnAcceptance(Article article, ManuscriptPackage manuscriptPackage) throws RSuiteException {
		InitialArticlePdf initialPdf = createInitialPDF(article, manuscriptPackage);
		File initialArticleXmlFile = createInitialArticle(article, manuscriptPackage, initialPdf.getNumberOfPages());
		sendToDigitalLibrary(article, initialPdf.getArticlePDF(), initialArticleXmlFile);
	}

	private void sendToDigitalLibrary(Article article, File pdfFile, File articleFile) throws RSuiteException {
		ArticleDigitalLibrary digitalLibrary = new ArticleDigitalLibrary(context, user, logger);
		digitalLibrary.deliverArticleOnPublishAcceptance(article, pdfFile, articleFile);

	}

	private File createInitialArticle(Article article, ManuscriptPackage manuscriptPackage, int numberOfPagesInPdf)
			throws RSuiteException {
		Journal journal = article.getJournal();

		Map<String, String> parameters = new HashMap<>();
		parameters.put("page-count", String.valueOf(numberOfPagesInPdf));
		parameters.put("journal-abbrv-title", journal.getAbbreviatedTitle());
		parameters.put("journal-id", ArticleDigitalLibraryNameUtils.getFixedJournalName(journal));
		parameters.put("article-id-publisher", ArticleDigitalLibraryNameUtils.createDigitalLibraryBaseName(journal, article.getShortArticleId()));
		

		String articleCode = article.getShortArticleId().replace("-", "");
		File articleFile = new File(manuscriptPackage.getPackageFolder(), articleCode + ".xml");
		ProjectTransformationUtils.transformDocument(context, manuscriptPackage.getMetadataFile(), xsltTransformPath,
				articleFile, parameters);
		return articleFile;
	}

	private InitialArticlePdf createInitialPDF(Article article, ManuscriptPackage manuscriptPackge)
			throws RSuiteException {
		File outputPdfFile = new File(manuscriptPackge.getPackageFolder(), article.getShortArticleId() + "-PROOF.pdf");
		File scholarOnePdf = manuscriptPackge.getScholarOnePdf();
		int numberOfPages = JournalCustomLibraryFactory.getInstance().createPdfForPublishOnAcceptance(scholarOnePdf,
				outputPdfFile);
		return new InitialArticlePdf(outputPdfFile, numberOfPages);
	}
}
