package org.theiet.rsuite.journals.domain.article.delivery.digitallibrary;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.*;
import org.apache.commons.lang.StringUtils;
import org.theiet.rsuite.journals.domain.article.Article;
import org.theiet.rsuite.journals.domain.article.ArticleTypesetterContainer;
import org.theiet.rsuite.journals.domain.journal.Journal;
import org.theiet.rsuite.utils.ImageMagickUtils;

import com.reallysi.rsuite.api.ContentAssembly;
import com.reallysi.rsuite.api.ContentAssemblyItem;
import com.reallysi.rsuite.api.ManagedObject;
import com.reallysi.rsuite.api.ManagedObjectReference;
import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.User;
import com.reallysi.rsuite.api.extensions.ExecutionContext;
import com.rsicms.projectshelper.utils.ProjectFileUtils;
import com.rsicms.projectshelper.zip.ZipHelper;
import com.rsicms.rsuite.helpers.utils.MoUtils;
import com.rsicms.rsuite.helpers.utils.RsuiteXMLUtils;

public class ArticleDigitalLibraryPackageBuilder {

	private ArticleDigitalLibraryPackageBuilder() {
	}

	private static final int JOURNAL_CODE_LENGTH = 3;

	public static ByteArrayOutputStream createArticleDigitalLibraryArchive(
			ExecutionContext context, User user, Article article)
			throws RSuiteException {

		ArticleTypesetterContainer typesetterContainer = article
				.getTypesetterContainer();
		validateArticle(article, typesetterContainer);

		return buildArticleArchive(context, user, article, typesetterContainer);
	}
	
	public static ByteArrayOutputStream createPublishOnAcceptanceLibraryArchive(ExecutionContext context, Article article, File pdfFile, File articleFile) throws RSuiteException{
		ByteArrayOutputStream articleDLArchiveStream = new ByteArrayOutputStream();
		ZipHelper zipHelper = new ZipHelper(articleDLArchiveStream);

		try (InputStream pdfStream = new FileInputStream(pdfFile);
			 InputStream articleStream = new FileInputStream(articleFile)) {
			String finalPDFFileName = createDigitalLibraryFinalFileName(context,
					article.getArticleId(), "pdf");
			
			zipHelper.addZipEntry(finalPDFFileName, pdfStream);
			
			String xmlFileName = createDigitalLibraryFinalFileName(context,
					article.getArticleId(), "xml");
			zipHelper.addZipEntry(xmlFileName, articleStream);
			
		} catch (RSuiteException  | IOException e) {
			throw new RSuiteException(0, "Problem with creating archive for "
					+ article, e);
		}finally {
			zipHelper.closeArchive();
		}

		return articleDLArchiveStream;
	}

	

	private static void validateArticle(Article article,
			ArticleTypesetterContainer typesetterContainer)
			throws RSuiteException {

		if (typesetterContainer.getFinalPdfMO() == null) {
			throw new RSuiteException("No final PDF found for " + article);
		}

		if (typesetterContainer.getArticleXMLMO() == null) {
			throw new RSuiteException("No XML article found for " + article);
		}

	}

	private static ByteArrayOutputStream buildArticleArchive(
			ExecutionContext context, User user, Article article,
			ArticleTypesetterContainer typesetterContainer)
			throws RSuiteException {
		ByteArrayOutputStream articleDLArchiveStream = new ByteArrayOutputStream();
		ZipOutputStream zipStream = new ZipOutputStream(articleDLArchiveStream);

		try {
			addFinalPdfToArchive(context, user, article, zipStream,
					typesetterContainer.getFinalPdfMO());

			addArticleXMLToArchive(context, user, article, zipStream,
					typesetterContainer.getArticleXMLMO());

			addImagesToArchive(context, zipStream,
					typesetterContainer.getImagesCA());

		} catch (RSuiteException e) {
			throw new RSuiteException(0, "Problem with creating archive for "
					+ article, e);
		}

		IOUtils.closeQuietly(zipStream);

		return articleDLArchiveStream;
	}

	private static void addImagesToArchive(ExecutionContext context,
			ZipOutputStream zipStream, ContentAssembly imagesCA)
			throws RSuiteException {

		if (imagesCA == null) {
			return;
		}

		for (ContentAssemblyItem child : imagesCA.getChildrenObjects()) {
			if (isImageMoReference(child)) {
				ManagedObject imageMo = MoUtils.getRealMo(context,
						child.getId());
				String fileExtension = FilenameUtils.getExtension(imageMo.getDisplayName());
				if (!fileExtension.equalsIgnoreCase("gif")) {
					addFileToZip(reduceImgDPI(context, imageMo), imageMo.getDisplayName(), zipStream);
				} else {
					addMoToZip(imageMo, imageMo.getDisplayName(), zipStream);
				}				
			}
		}
	}

	private static File reduceImgDPI(ExecutionContext context,
			ManagedObject imageMo) throws RSuiteException {
		File imageConversionFolder = ProjectFileUtils.getTmpSubDir(context, "imgConvertion", new Date(), true);
		
		File imgFile = new File(imageConversionFolder, imageMo.getDisplayName());
		writeFile(imageMo.getInputStream(), imgFile);
		
		File outputImgFolder = new File(imageConversionFolder, "output");
		outputImgFolder.mkdirs();
		File outputImg = new File(outputImgFolder, imageMo.getDisplayName());
		
		List<String> parameters = new ArrayList<String>();
		parameters.add("-strip");
		parameters.add("-resample");
		parameters.add("300");

		ImageMagickUtils imageMagickUtils = ImageMagickUtils.getInstance(context);
		imageMagickUtils.convertImage(imgFile, parameters, outputImg, null);

		//FileUtils.deleteQuietly(imageConversionFolder);
		
		return outputImg;
	}

	private static void writeFile (InputStream inputStream, File outputFile) throws RSuiteException {
		OutputStream outputStream = null;

		try {
			outputStream = new FileOutputStream(outputFile);

			int read = 0;
			byte[] bytes = new byte[1024];

			while ((read = inputStream.read(bytes)) != -1) {
				outputStream.write(bytes, 0, read);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (outputStream != null) {
				try {
					outputStream.close();
				} catch (IOException e) {
					throw new RSuiteException(0, "Problem writing file to disk for file: " + outputFile.getName(), e);
				}

			}
		}
	}
	
	private static boolean isImageMoReference(ContentAssemblyItem imageRef) {
		if (imageRef instanceof ManagedObjectReference
				&& !imageRef.getDisplayName().endsWith(".800") && !imageRef.getDisplayName().endsWith(".db")) {
			return true;
		}
		return false;
	}

	private static void addArticleXMLToArchive(ExecutionContext context,
			User user, Article article, ZipOutputStream zipStream,
			ManagedObject articleXMLMO) throws RSuiteException {
		String xmlFileName = createDigitalLibraryFinalFileName(context,
				article.getArticleId(), "xml");
		addMoToZip(articleXMLMO, xmlFileName, zipStream);

	}

	private static void addFinalPdfToArchive(ExecutionContext context,
			User user, Article article, ZipOutputStream zipStream,
			ManagedObject finalPdfMO) throws RSuiteException {

		String finalPDFFileName = createDigitalLibraryFinalFileName(context,
				article.getArticleId(), "pdf");
		addMoToZip(finalPdfMO, finalPDFFileName, zipStream);

	}

	public static void addFileToZip(File file, String entryName,
			ZipOutputStream zos) throws RSuiteException {
		try {
			FileInputStream fis = new FileInputStream(file);
			addItemToZip(fis, entryName, zos);
		} catch (IOException e) {
			throw new RSuiteException(0, "Unable to create zip entry for file "
					+ file.getName(), e);
		}
	}

	public static void addMoToZip(ManagedObject mo, String entryName,
			ZipOutputStream zos) throws RSuiteException {
		if (mo.isCheckedout()) {
			throw new RSuiteException("Mo " + mo.getId() + " is checked out");
		}

		InputStream is = mo.getInputStream();
		if (!mo.isNonXml()) {
			is = RsuiteXMLUtils.removeAttributesFromRSuiteNamespace(is);
		}

		try {
			addItemToZip(is, entryName, zos);
		} catch (IOException e) {
			throw new RSuiteException(0, "Unable to create zip entry for "
					+ mo.getId(), e);
		}
	}

	public static void addItemToZip(InputStream is, String entryName,
			ZipOutputStream zos) throws IOException, RSuiteException {
		ZipEntry entry = new ZipEntry(entryName);
		zos.putNextEntry(entry);
		IOUtils.copy(is, zos);
		zos.closeEntry();
		is.close();
	}

	public static String createDigitalLibraryFinalFileName(
			ExecutionContext context, String articleId, String ext)
			throws RSuiteException {

		String journalCode = articleId.substring(0, JOURNAL_CODE_LENGTH);

		Journal journal = new Journal(context, journalCode);

		String remainder = articleId.substring(JOURNAL_CODE_LENGTH)
				.replaceFirst(".R\\d+$", "").replaceFirst("SI-", "")
				.replaceAll("-", ".");

		if (journalCode.equals("ELL")) {
			journalCode = "EL";
		}

		if (journal.requiresPrefixForDigitaLibrary()) {
			journalCode = "IET-" + journalCode;
		}
		
		String customPrefix = journal.getPrefixForDigitaLibrary();
		if (StringUtils.isNotBlank(customPrefix)) {
			journalCode = customPrefix;
		}

		return journalCode + remainder + "." + ext;
	}

	public static String getFixedJournalName(Journal journal) throws RSuiteException {
		
		String journalCode = journal.getJournalCode();
		String customPrefix =  journal.getPrefixForDigitaLibrary();
		
		if (journalCode.equals("ELL")) {
			return "EL";
		}
		
		if (!journal.requiresPrefixForDigitaLibrary()) {
			return journalCode;
		}else if (StringUtils.isNotBlank(customPrefix)) {
			return journal.getPrefixForDigitaLibrary();
		}
		else {
			return "IET-" + journalCode;
		}
	}

}
