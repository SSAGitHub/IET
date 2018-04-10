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
import org.theiet.rsuite.journals.domain.article.Article;
import org.theiet.rsuite.journals.domain.article.delivery.digitallibrary.ArticleDigitalLibrary;
import org.theiet.rsuite.journals.domain.article.delivery.digitallibrary.ArticleDigitalLibraryNameUtils;
import org.theiet.rsuite.journals.domain.article.manuscript.ManifestType;
import org.theiet.rsuite.journals.domain.article.manuscript.ManuscriptPackage;
import org.theiet.rsuite.journals.domain.journal.Journal;
import org.theiet.rsuite.mocks.api.MockUtils;
import org.w3c.dom.Document;
import org.xmlunit.builder.Input;
import org.xmlunit.matchers.EvaluateXPathMatcher;
import org.xmlunit.util.Convert;

import com.reallysi.rsuite.api.RSuiteException;
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
		builderFactory.setFeature("http://xml.org/sax/features/validation", false);
		builderFactory.setFeature(
		       "http://apache.org/xml/features/nonvalidating/load-dtd-grammar",
		       false);
		builderFactory.setFeature(
		       "http://apache.org/xml/features/nonvalidating/load-external-dtd",
		       false);
	}

	@Test
	public void create_initial_jats_for_article_without_prefix_manuscript_s1() throws RSuiteException, IOException {

		ArticleDigitalLibrary digitalLibrary = mock(ArticleDigitalLibrary.class);
		XmlApiManager xmlApiManager = MockUtils.createXmlApiManagerMock();

		PublishOnAcceptance publish = new PublishOnAcceptance(xmlApiManager, digitalLibrary, ManifestType.S1);

		ManuscriptPackage manuscriptPackage = stubManuscriptPackage("cps-2017-0066-metadata.xml");

		Journal journal = mock(Journal.class);
		when(journal.getJournalCode()).thenReturn("CPS");

		Article article = mock(Article.class);
		when(article.getJournal()).thenReturn(journal);
		when(article.getShortArticleId()).thenReturn("CPS-2017-0066");

		String digitalLibraryArticleId = ArticleDigitalLibraryNameUtils.createDigitalLibraryBaseName(journal, article.getShortArticleId());
		File articleFile = publish.createInitialArticle(journal, digitalLibraryArticleId, manuscriptPackage, 4);
		
		Source articleSource = Input.fromFile(articleFile).build();
		Document xmlToTest = Convert.toDocument(articleSource, builderFactory);
	    
		assertThat(articleFile.getName(), is("CPS.2017.0066.xml"));
	    assertThat(xmlToTest, EvaluateXPathMatcher.hasXPath("/article/front/journal-meta/journal-id/text()", equalTo("CPS")));

	}

	private ManuscriptPackage stubManuscriptPackage(String testFileName) throws IOException {
		File packageFolder = prepareManuscriptPackgeFolder(testFileName);
		File manuscriptFile = new File(packageFolder, testFileName);

		ManuscriptPackage manuscriptPackage = mock(ManuscriptPackage.class);
		when(manuscriptPackage.getPackageFolder()).thenReturn(packageFolder);
		when(manuscriptPackage.getMetadataFile()).thenReturn(manuscriptFile);
		return manuscriptPackage;
	}

	private File prepareManuscriptPackgeFolder(String testFileName) throws IOException {
		File manuscriptFile = TestHelper.getTestFile(getClass(), testFileName);
		File dtdFile = TestHelper.getTestFile(getClass(), "s1.dtd");
		File packageFolder = testFolder.newFolder("packageFolder");
		FileUtils.copyFileToDirectory(manuscriptFile, packageFolder);
		FileUtils.copyFileToDirectory(dtdFile, packageFolder);
		return packageFolder;
	}

}
