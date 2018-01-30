package org.theiet.rsuite.journals.actionhandlers;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.theiet.rsuite.journals.JournalConstants;
import org.theiet.rsuite.journals.domain.article.Article;
import org.theiet.rsuite.journals.domain.article.create.ArticleContainerCreateor;
import org.theiet.rsuite.journals.domain.article.manuscript.*;
import org.theiet.rsuite.journals.domain.article.manuscript.acceptance.PublishOnAcceptance;
import org.theiet.rsuite.journals.domain.article.metadata.ArticleMetadataUpdater;
import org.theiet.rsuite.journals.domain.article.pubtrack.ArticlePubtrackManager;
import org.theiet.rsuite.journals.domain.journal.Journal;
import org.theiet.rsuite.journals.domain.journal.JournalsFinder;
import org.theiet.rsuite.journals.utils.JournalUtils;
import org.theiet.rsuite.utils.ExceptionUtils;
import org.theiet.rsuite.utils.IetUtils;

import com.reallysi.rsuite.api.*;
import com.reallysi.rsuite.api.workflow.WorkflowExecutionContext;
import com.reallysi.rsuite.service.XmlApiManager;
import com.rsicms.projectshelper.workflow.actionhandlers.AbstractActionHandler;
import com.rsicms.rsuite.helpers.upload.*;


public class ManuscriptCentralIngestion extends AbstractActionHandler implements JournalConstants {

	private static final long serialVersionUID = 1L;


	@Override
	public void executeTask (WorkflowExecutionContext context) throws Exception {
		Log log = context.getWorkflowLog();
		User user = getSystemUser();
		
		String pId = context.getProcessInstanceId();
				
		File rootFolder = context.getFileWorkflowObject().getFile().listFiles()[0];
		
		log.info("execute: Processing folder:  " + rootFolder.getAbsolutePath());
		
		validateWorkflowTempFolderStructure(context, rootFolder);
		ManuscriptPackage manuscriptPackage = new ManuscriptPackage(rootFolder);
		
		File manifestFile = manuscriptPackage.getMetadataFile();
		
		ManifestDocument manifestDocument = obtainManifestDocument(context,
				manifestFile);
		
		String articleId = obtainArticleIdFromManifest(context,
				manifestDocument);
		
		Journal journal = obtainJournal(context, user, articleId);
		Article article = createArticle(context, user, articleId, journal);
		
		log.info("execute: Created article ca " + article.getArticleCaId() + " underneath articles ca " + journal.getArticles().getArticlesCAId());
		

		setUpWorkflowVariables(context, pId, journal, article);
		setUpArticleLmd(journal.getJournalCode(), article, manifestDocument);		
		renameManifestFile(log, rootFolder, manifestFile);				
		loadManifestPackageFiles(context, user, log, article, rootFolder);
		
		
		ArticlePubtrackManager.createArticleProcess(context, user, log, article);
		log.info("Deliver to Digital Library");
		PublishOnAcceptance publishOnAcceptance = new PublishOnAcceptance(context, user);
		publishOnAcceptance.publishOnAcceptance(article, manuscriptPackage);
	}

	private String obtainArticleIdFromManifest(
			WorkflowExecutionContext context, ManifestDocument manifestDocument)
			throws Exception {
		String articleId = manifestDocument.getArticleId();
		
		validateArticleId(context, articleId);
		return articleId;
	}

	private ManifestDocument obtainManifestDocument(
			WorkflowExecutionContext context, File manifestFile)
			throws RSuiteException {
		ManifestFileParser fileParser = new ManifestFileParser();
		return fileParser.parseManifestDocument(context.getXmlApiManager(), manifestFile);
	}

	private Journal obtainJournal(WorkflowExecutionContext context, User user,
			String articleId) throws RSuiteException {
		String journal_code = articleId.substring(0, 3);
		
		Journal journal = JournalsFinder.findJournal(context, user, journal_code);
		return journal;
	}

	private Article createArticle(WorkflowExecutionContext context, User user,
			String articleId, Journal journal) throws RSuiteException {
		
		String normalizedArticleId = JournalUtils.normalizeArticleId(articleId);
		ArticleContainerCreateor articleCreator = new ArticleContainerCreateor(context, user);
		Article article = articleCreator.createArticle(journal, normalizedArticleId);
		return article;
	}

	private void setUpWorkflowVariables(WorkflowExecutionContext context,
			String pId, Journal journal, Article article)
			throws RSuiteException {
		context.setVariable(WF_VAR_ARTICLE_ID, article.getArticleId());
		context.setVariable(WF_VAR_PRODUCT_ID, article.getArticleId()); // for inbox display
		context.setVariable("pId", pId);
		context.setVariable(JournalConstants.WF_VAR_PRODUCT, JournalConstants.WF_VAR_ARTICLE);
		context.setVariable(WF_VAR_JOURNAL_ID, journal.getJournalCode());
		context.setVariable(WF_VAR_JOURNAL_CA_ID, journal.getJournalCa().getId());
		context.setVariable(WF_VAR_RSUITE_CONTENTS, article.getArticleCaId());
	}

	private void validateWorkflowTempFolderStructure(WorkflowExecutionContext context, File rootFolder)
			throws RSuiteException {
		String zipFileName = context.getVariable("rsuiteSourceFilePath");
		File tempDir = new File(rootFolder.getParentFile().getParentFile().getParent(), "temp");
		
		File submittedZipFile = new File(tempDir, zipFileName);
		if (!submittedZipFile.exists()) {
			throw new RSuiteException("File " + submittedZipFile.getAbsolutePath() + " not found");			
		}
	}

	private void setUpArticleLmd(String jorunalCode, Article article, ManifestDocument manifestDocument)
			throws RSuiteException {
		
		ArticleMetadataUpdater articleMetadataUpdater = article.createArticleMetadataUpdater();
		articleMetadataUpdater.setJournalCode(jorunalCode);
		
		setMetadataFromManifest(manifestDocument, articleMetadataUpdater);
		
		articleMetadataUpdater.updateMetadata();
		
		
	}

	private void loadManifestPackageFiles(WorkflowExecutionContext context,
			User user, Log log, Article article, File rootFolder)
			throws RSuiteException, Exception {
		ContentAssembly packageAssembly = article.getManuscriptCA();
		
	    RSuiteFileLoadOptions loadOpts = new RSuiteFileLoadOptions(user);
	    loadOpts.setDuplicateFilenamePolicy(DuplicateFilenamePolicy.UPDATE);
	    
	    File[] filesToUpload = getManusriptFilesToLoad(rootFolder);
	    
	    for (File packageFile : filesToUpload){
	    	
	    	if (packageFile.isFile() && shouldLoadPackageFile(packageFile)) {
	    		log.info("Load file " + packageFile);
	    		IetUtils.loadFileToCa(context, packageFile, packageAssembly, loadOpts);
			}else if(packageFile.isDirectory()){
				log.info("Load dir " + packageFile.getName());
				RSuiteFileLoadHelper.loadDirectoryToContentAssembly(context, packageFile, packageAssembly, loadOpts);
			}	    	
	    }
	    

		
		boolean hasLoadErrors = IetUtils.getFileLoadResult(log, loadOpts);
		if (hasLoadErrors) {

			ExceptionUtils.throwWorfklowException(context, "Load had errors");					
		}
	}

	private File[] getManusriptFilesToLoad(File rootFolder) {
		
		File[] filesToUpload = rootFolder.listFiles();
		sortFiles(rootFolder);
		return filesToUpload;
	}

	private void sortFiles(File rootFolder) {
		Arrays.sort(rootFolder.listFiles(), new Comparator<File>() {

			@Override
			public int compare(File o1, File o2) {
				if (o1.isFile() && o2.isFile()){
					return 0;	
				}
				
				if (o1.isFile() && o2.isDirectory()){
					return 1;
				}
				
				return -1;
			}
		});
	}

	private void renameManifestFile(Log log, File rootFolder, File manifestFile)
			throws IOException {
		String manifestFileName = manifestFile.getName();
		String manifestTextCopyName = manifestFileName.replace(".xml", ".txt");
		File manifestTextFile = new File(rootFolder,manifestTextCopyName);
		log.info("Copy manifest from " +  manifestFile.getAbsolutePath() + " to " + manifestTextFile.getAbsolutePath());
	    FileUtils.copyFile(manifestFile, manifestTextFile);
	}

	private boolean shouldLoadPackageFile(File packageFile) {
		String ext = FilenameUtils.getExtension(packageFile.getName());
		if ("dtd".equalsIgnoreCase(ext) || "xml".equalsIgnoreCase(ext)){
			return false;
		}
		return true;
	}

	private void validateArticleId(WorkflowExecutionContext context,
			String articleId) throws Exception {
		if (StringUtils.isBlank(articleId)) {
			ExceptionUtils.throwWorfklowException(context, "Unable to get article id ms_no");			
		}
		
		if (articleId.length() < 3) {
			ExceptionUtils.throwWorfklowException(context, "Unable to get journal code from " + articleId);
		}
	}

		
	/**
	 * Extracts article metadata from the manifest document.
	 * @throws RSuiteException
	 */
	private void setMetadataFromManifest(ManifestDocument manifestDocument,  ArticleMetadataUpdater articleMetadataUpdater) throws RSuiteException{
		
		articleMetadataUpdater.setArticleTitle(manifestDocument.getArticleTitle());
		articleMetadataUpdater.setAuthor(manifestDocument.getArticleAuthor());
		
		articleMetadataUpdater.setArticleType(manifestDocument.getManuscriptType());
		articleMetadataUpdater.setCategory(manifestDocument.getCategory());
		
		
		boolean isSpecialIssue = manifestDocument.isSpecialIssue();
		articleMetadataUpdater.isSpecialIssue(isSpecialIssue);
		
		if (isSpecialIssue){
			articleMetadataUpdater.setSpecialIssueTitle(manifestDocument.getSpecialIssueTitle());
		}
		
		articleMetadataUpdater.setOpenAccessCheckList(manifestDocument.isOpenAccessCheckList());
		articleMetadataUpdater.setClassifications(manifestDocument.getClassifications());
		
			
		articleMetadataUpdater.setSubmittedDate(manifestDocument.getSubmittedDate());
		articleMetadataUpdater.setDecisionDate(manifestDocument.getDecisionDate());
		
		articleMetadataUpdater.setExportDate(manifestDocument.getExportDate());
		
		articleMetadataUpdater.setLicenseType(manifestDocument.getLicenseType());

		articleMetadataUpdater.setSubmissionType(manifestDocument.getSubmissionType());
		
		articleMetadataUpdater.setCategoryCode(manifestDocument.getCategoryCodes());
								  
		articleMetadataUpdater.setSupplementaryMaterial(manifestDocument.getSupplementaryMaterial());
		
		articleMetadataUpdater.setArticleNotesIcon(false);
		articleMetadataUpdater.setInspecRequired(true);
	}
}
