package org.theiet.rsuite.journals.domain.article.manuscript.acceptance;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.theiet.rsuite.journals.domain.article.Article;
import org.theiet.rsuite.journals.domain.article.delivery.digitallibrary.*;
import org.theiet.rsuite.journals.domain.article.manuscript.ManifestType;
import org.theiet.rsuite.journals.domain.article.manuscript.ManuscriptPackage;
import org.theiet.rsuite.journals.domain.journal.Journal;

import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.service.XmlApiManager;
import com.rsicms.projectshelper.utils.ProjectTransformationUtils;

public class PublishOnAcceptance {

	private static final String XSLT_URI_MANUSCRIPT_METADATA_TO_ARTICLE = "rsuite:/res/plugin/iet/xslt/journals/manuscript/manuscript-metadata-to-jats-article.xsl";

	private static final String XSLT_URI_MANUSCRIPT_RV_METADATA_TO_ARTICLE = "rsuite:/res/plugin/iet/xslt/journals/manuscript/manuscript-rv-metadata-to-jats-article.xsl";

	private XmlApiManager xmlApiManager;

	private String xsltTransformPath;
	
	private ArticleDigitalLibrary digitalLibrary;
	
	public PublishOnAcceptance(XmlApiManager xmlApiManager, ArticleDigitalLibrary digitalLibrary, ManifestType manifestType) {
		this.digitalLibrary = digitalLibrary;
		this.xmlApiManager = xmlApiManager;
		xsltTransformPath = manifestType == ManifestType.S1 ? XSLT_URI_MANUSCRIPT_METADATA_TO_ARTICLE
				: XSLT_URI_MANUSCRIPT_RV_METADATA_TO_ARTICLE;
	}


	public void publishOnAcceptance(Article article, ManuscriptPackage manuscriptPackage) throws RSuiteException {
		
		Journal journal = article.getJournal();
		String digitalLibraryArticleId = ArticleDigitalLibraryNameUtils.createDigitalLibraryBaseName(journal, article.getShortArticleId());
		
		InitialArticlePdf initialPdf = createInitialPDF(digitalLibraryArticleId, manuscriptPackage);
		File initialArticleXmlFile = createInitialArticle(journal, digitalLibraryArticleId, manuscriptPackage, initialPdf.getNumberOfPages());
		sendToDigitalLibrary(article, initialPdf.getArticlePDF(), initialArticleXmlFile);
	}

	private void sendToDigitalLibrary(Article article, File pdfFile, File articleFile) throws RSuiteException {
		digitalLibrary.deliverArticleOnPublishAcceptance(article, pdfFile, articleFile);

	}

	public File createInitialArticle(Journal journal, String digitalLibraryArticleId, ManuscriptPackage manuscriptPackage, int numberOfPagesInPdf)
			throws RSuiteException {
		
		
		Map<String, String> parameters = new HashMap<>();
		parameters.put("page-count", String.valueOf(numberOfPagesInPdf));
		parameters.put("journal-abbrv-title", journal.getAbbreviatedTitle());
		parameters.put("journal-id", ArticleDigitalLibraryNameUtils.getFixedJournalName(journal));
		parameters.put("article-id-publisher", digitalLibraryArticleId);
		

		File articleFile = new File(manuscriptPackage.getPackageFolder(), digitalLibraryArticleId + ".xml");
		ProjectTransformationUtils.transformDocument(xmlApiManager, manuscriptPackage.getMetadataFile(), xsltTransformPath,
				articleFile, parameters);
		return articleFile;
	}

	private InitialArticlePdf createInitialPDF(String digitalLibraryArticleId, ManuscriptPackage manuscriptPackge)
			throws RSuiteException {
		File outputPdfFile = new File(manuscriptPackge.getPackageFolder(), digitalLibraryArticleId + "-PROOF.pdf");
		File scholarOnePdf = manuscriptPackge.getScholarOnePdf();
		int numberOfPages = JournalCustomLibraryFactory.getInstance().createPdfForPublishOnAcceptance(scholarOnePdf,
				outputPdfFile);
		return new InitialArticlePdf(outputPdfFile, numberOfPages);
	}
}
