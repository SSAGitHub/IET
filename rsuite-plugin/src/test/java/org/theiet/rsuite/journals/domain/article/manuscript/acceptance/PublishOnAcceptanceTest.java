package org.theiet.rsuite.journals.domain.article.manuscript.acceptance;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Source;

import org.apache.commons.io.FileUtils;
import org.junit.*;
import org.junit.rules.TemporaryFolder;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.theiet.rsuite.journals.domain.article.Article;
import org.theiet.rsuite.journals.domain.article.delivery.digitallibrary.ArticleDigitalLibrary;
import org.theiet.rsuite.journals.domain.article.manuscript.ManifestType;
import org.theiet.rsuite.journals.domain.article.manuscript.ManuscriptPackage;
import org.theiet.rsuite.journals.domain.journal.Journal;
import org.theiet.rsuite.mocks.api.MockUtils;
import org.w3c.dom.Document;
import org.xmlunit.builder.Input;
import org.xmlunit.matchers.EvaluateXPathMatcher;
import org.xmlunit.util.Convert;

import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.extensions.ExecutionContext;
import com.reallysi.rsuite.service.XmlApiManager;
import com.rsicms.test.TestHelper;

public class PublishOnAcceptanceTest {

	@Rule
	public TemporaryFolder testFolder = new TemporaryFolder();

	private static DocumentBuilderFactory builderFactory;

	@BeforeClass
	public static void prepareTest() throws ParserConfigurationException {
		builderFactory = DocumentBuilderFactory.newInstance();
		builderFactory.setValidating(false);
		builderFactory.setFeature("http://xml.org/sax/features/validation",
				false);
		builderFactory
				.setFeature(
						"http://apache.org/xml/features/nonvalidating/load-dtd-grammar",
						false);
		builderFactory
				.setFeature(
						"http://apache.org/xml/features/nonvalidating/load-external-dtd",
						false);
		System.setProperty(
				"javax.xml.xpath.XPathFactory:http://java.sun.com/jaxp/xpath/dom",
				"com.sun.org.apache.xpath.internal.jaxp.XPathFactoryImpl");
	}

	@Test
	public void create_initial_jats_for_article_without_prefix_manuscript_s1()
			throws RSuiteException, IOException {

		ArticleDigitalLibrary digitalLibraryMock = mock(ArticleDigitalLibrary.class);

		XmlApiManager xmlApiManager = MockUtils.createXmlApiManagerMock();

		PublishOnAcceptance publish = new PublishOnAcceptanceTestable(
				xmlApiManager, digitalLibraryMock, ManifestType.S1);

		ManuscriptPackage manuscriptPackage = stubManuscriptPackage("cps-2017-0066-metadata.xml");

		Journal journal = getJournalMock(false, "");

		Article article = getArticleMock(journal);

		publish.publishOnAcceptance(article, manuscriptPackage);

		ArgumentCaptor<File> articleXMLCapture = ArgumentCaptor
				.forClass(File.class);
		ArgumentCaptor<File> articlePDFCapture = ArgumentCaptor
				.forClass(File.class);

		verifyAll(digitalLibraryMock, articleXMLCapture, articlePDFCapture);

		File articleXML = articleXMLCapture.getValue();

		Source articleSource = Input.fromFile(articleXML).build();
		Document xmlToTest = Convert.toDocument(articleSource, builderFactory);
		assertAll(articlePDFCapture, articleXML, xmlToTest,
				"CPS.2017.0066.xml", "CPS", "CPS.2017.0066-PROOF.pdf");

	}

	@Test
	public void create_initial_jats_for_article_with_default_prefix_manuscript_s1()
			throws RSuiteException, IOException {

		ArticleDigitalLibrary digitalLibraryMock = mock(ArticleDigitalLibrary.class);

		XmlApiManager xmlApiManager = MockUtils.createXmlApiManagerMock();

		PublishOnAcceptance publish = new PublishOnAcceptanceTestable(
				xmlApiManager, digitalLibraryMock, ManifestType.S1);

		ManuscriptPackage manuscriptPackage = stubManuscriptPackage("cps-2017-0066-metadata.xml");

		Journal journal = getJournalMock(true, "");

		Article article = getArticleMock(journal);

		publish.publishOnAcceptance(article, manuscriptPackage);

		ArgumentCaptor<File> articleXMLCapture = ArgumentCaptor
				.forClass(File.class);
		ArgumentCaptor<File> articlePDFCapture = ArgumentCaptor
				.forClass(File.class);

		verifyAll(digitalLibraryMock, articleXMLCapture, articlePDFCapture);

		File articleXML = articleXMLCapture.getValue();

		Source articleSource = Input.fromFile(articleXML).build();
		Document xmlToTest = Convert.toDocument(articleSource, builderFactory);

		assertAll(articlePDFCapture, articleXML, xmlToTest,
				"IET-CPS.2017.0066.xml", "IET-CPS",
				"IET-CPS.2017.0066-PROOF.pdf");

	}

	@Test
	public void create_initial_jats_for_article_with_custom_prefix_manuscript_s1()
			throws RSuiteException, IOException {

		ArticleDigitalLibrary digitalLibraryMock = mock(ArticleDigitalLibrary.class);

		XmlApiManager xmlApiManager = MockUtils.createXmlApiManagerMock();

		PublishOnAcceptance publish = new PublishOnAcceptanceTestable(
				xmlApiManager, digitalLibraryMock, ManifestType.S1);

		ManuscriptPackage manuscriptPackage = stubManuscriptPackage("cps-2017-0066-metadata.xml");

		Journal journal = getJournalMock(true, "PRE");

		Article article = getArticleMock(journal);

		publish.publishOnAcceptance(article, manuscriptPackage);

		ArgumentCaptor<File> articleXMLCapture = ArgumentCaptor
				.forClass(File.class);
		ArgumentCaptor<File> articlePDFCapture = ArgumentCaptor
				.forClass(File.class);

		verifyAll(digitalLibraryMock, articleXMLCapture, articlePDFCapture);

		File articleXML = articleXMLCapture.getValue();

		Source articleSource = Input.fromFile(articleXML).build();
		Document xmlToTest = Convert.toDocument(articleSource, builderFactory);

		assertAll(articlePDFCapture, articleXML, xmlToTest,
				"PRE.2017.0066.xml", "PRE", "PRE.2017.0066-PROOF.pdf");

	}

	private ManuscriptPackage stubManuscriptPackage(String testFileName)
			throws IOException {
		File packageFolder = prepareManuscriptPackgeFolder(testFileName);
		File manuscriptFile = new File(packageFolder, testFileName);

		ManuscriptPackage manuscriptPackage = mock(ManuscriptPackage.class);
		when(manuscriptPackage.getPackageFolder()).thenReturn(packageFolder);
		when(manuscriptPackage.getMetadataFile()).thenReturn(manuscriptFile);
		return manuscriptPackage;
	}

	private File prepareManuscriptPackgeFolder(String testFileName)
			throws IOException {
		File manuscriptFile = TestHelper.getTestFile(getClass(), testFileName);
		File dtdFile = TestHelper.getTestFile(getClass(), "s1.dtd");
		File packageFolder = testFolder.newFolder("packageFolder");
		FileUtils.copyFileToDirectory(manuscriptFile, packageFolder);
		FileUtils.copyFileToDirectory(dtdFile, packageFolder);
		return packageFolder;
	}

	private Article getArticleMock(Journal journal) throws RSuiteException {
		Article article = mock(Article.class);
		when(article.getJournal()).thenReturn(journal);
		when(article.getShortArticleId()).thenReturn("CPS-2017-0066");
		return article;
	}

	private Journal getJournalMock(boolean isPrefixRequied, String customPrefix)
			throws RSuiteException {
		Journal journal = mock(Journal.class);
		when(journal.getJournalCode()).thenReturn("CPS");
		when(journal.requiresPrefixForDigitaLibrary()).thenReturn(
				isPrefixRequied);
		when(journal.getPrefixForDigitaLibrary()).thenReturn(customPrefix);
		return journal;
	}
	
	private void verifyAll(ArticleDigitalLibrary digitalLibraryMock,
			ArgumentCaptor<File> articleXMLCapture,
			ArgumentCaptor<File> articlePDFCapture) throws RSuiteException {
		verify(digitalLibraryMock).deliverArticleOnPublishAcceptance(
				Mockito.any(Article.class), articlePDFCapture.capture(),
				articleXMLCapture.capture());
	}

	private void assertAll(ArgumentCaptor<File> articlePDFCapture,
			File articleXML, Document xmlToTest, String expectedArticleXMLName,
			String expectedXPath, String expectedArticlePDFName) {
		assertThat(articleXML.getName(), is(expectedArticleXMLName));
		assertThat(xmlToTest, EvaluateXPathMatcher.hasXPath(
				"/article/front/journal-meta/journal-id/text()",
				equalTo(expectedXPath)));

		File articlePDF = articlePDFCapture.getValue();
		assertThat(articlePDF.getName(), is(expectedArticlePDFName));
	}

}
