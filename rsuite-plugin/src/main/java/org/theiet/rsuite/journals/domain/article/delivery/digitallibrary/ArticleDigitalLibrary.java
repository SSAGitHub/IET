package org.theiet.rsuite.journals.domain.article.delivery.digitallibrary;

import java.io.*;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.theiet.rsuite.datamodel.ExternalCompanyUser;
import org.theiet.rsuite.datatype.deliveryUser.DeliveryUser;
import org.theiet.rsuite.datatype.deliveryUser.DeliveryUserFactory;
import org.theiet.rsuite.domain.date.IetDate;
import org.theiet.rsuite.domain.user.UserUtils;
import org.theiet.rsuite.journals.domain.article.Article;
import org.theiet.rsuite.journals.domain.article.metadata.ArticleMetadata;
import org.theiet.rsuite.journals.domain.article.metadata.ArticleMetadataUpdater;
import org.theiet.rsuite.journals.domain.article.pubtrack.ArticlePubtrackManager;
import org.theiet.rsuite.journals.domain.article.update.ArticleXMLUpdater;

import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.User;
import com.reallysi.rsuite.api.extensions.ExecutionContext;

public class ArticleDigitalLibrary {

	private Log logger = LogFactory.getLog(getClass());

	private ExecutionContext context;

	private User user;

	private static final String DIGITAL_LIBRARY_ARTICLE_USER = "DigitalLibraryArticle";
	private static final String PROP_DIGITAL_LIBRARY_FTP_FOLDER_ARTICLE = "ftp.folder.article";

	public ArticleDigitalLibrary(ExecutionContext context, User user, Log logger) {
		this.context = context;
		this.user = user;
		this.logger = logger;
	}
	
	public void deliverArticle(Article article) throws RSuiteException {
		deliverArticle(article, true);
	}
	
	public void deliverArticle(Article article, 
			boolean completePubtrack) throws RSuiteException {
		
		setArticleLmd(article);
		ByteArrayOutputStream articleArchive = ArticleDigitalLibraryPackageBuilder.createArticleDigitalLibraryArchive(context, user, article);
		sentArticleArchiveToDigitalLibrary(article, articleArchive);
		makeArticleAvailableForIssueAssigment(article);
		logEventToPubtrack(user, context, completePubtrack, article);
	}
	
	public void deliverArticleOnPublishAcceptance(Article article, File pdfFile, File articleFile) throws RSuiteException {
		ByteArrayOutputStream articleArchive = ArticleDigitalLibraryPackageBuilder.createPublishOnAcceptanceLibraryArchive(context, article, pdfFile, articleFile);
		sentArticleArchiveToDigitalLibrary(article, articleArchive);		
		
		setPrePublishDate(article);
		ArticlePubtrackManager.logInitialSendToDigitalLibrary(context, user, article);
	}

	private void setPrePublishDate(Article article) throws RSuiteException {
		ArticleMetadataUpdater articleMetadataUpdater = article.createArticleMetadataUpdater();
		articleMetadataUpdater.setPrePublishedDate(IetDate.getCurrentDate());
		articleMetadataUpdater.updateMetadata();
	}

	private void sentArticleArchiveToDigitalLibrary(Article article,
			ByteArrayOutputStream articleArchive) throws RSuiteException {
		byte[] bytes = articleArchive.toByteArray();
		ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
		
		ExternalCompanyUser digitalLibraryUser = UserUtils.getExtarnalUser(
				context, DIGITAL_LIBRARY_ARTICLE_USER);
		DeliveryUser deliveryUser = DeliveryUserFactory.createDeliveryUser(
				context, digitalLibraryUser.getUserId());
		
		String ftpFileName = ArticleDigitalLibraryPackageBuilder.createDigitalLibraryFinalFileName(context, article.getArticleId(), "zip");

		String targetFolder = deliveryUser
				.getProperty(PROP_DIGITAL_LIBRARY_FTP_FOLDER_ARTICLE);
		deliveryUser.deliverToDestination(bais, ftpFileName, targetFolder);
		logger.info("deliverArticle: transfer complete" + article);
	}

	private void setArticleLmd(Article article) throws RSuiteException {
		ArticleMetadataUpdater articleMetadataUpdater = article.createArticleMetadataUpdater();
		
		ArticleMetadata articleMetadata = article.getArticleMetadata();
		String onlineDate = articleMetadata.getOnlinePublishDate();
		
		if (StringUtils.isBlank(onlineDate)){
			onlineDate = IetDate.getCurrentDate();
			articleMetadataUpdater.setOnlinePublishedDate(onlineDate);
		}
		
		String prePublishDate = articleMetadata.getPrePublishDate();

		ArticleXMLUpdater articleUpdater = new ArticleXMLUpdater(context, user, article.getXMLMo());
		articleUpdater.addPublishDates(prePublishDate, IetDate.parseDate(onlineDate));
		
		articleMetadataUpdater.updateMetadata();
	}
	
	private void makeArticleAvailableForIssueAssigment(Article article) throws RSuiteException {
		ArticleMetadataUpdater articleMetadataUpdater = article.createArticleMetadataUpdater();
		articleMetadataUpdater.makeArticleAvailable();
		articleMetadataUpdater.updateMetadata();
	}

	private void logEventToPubtrack(User user, ExecutionContext context,
			boolean completePubtrack, Article article) throws RSuiteException {
		ArticlePubtrackManager.logSendToDigitalLibrary(context, user, article);
		
		if (completePubtrack){
			ArticlePubtrackManager.completeArticleProcess(context, user, article);
		}
	}


}
