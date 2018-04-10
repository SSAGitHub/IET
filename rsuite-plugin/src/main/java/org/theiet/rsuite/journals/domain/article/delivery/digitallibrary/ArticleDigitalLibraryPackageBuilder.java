package org.theiet.rsuite.journals.domain.article.delivery.digitallibrary;

import static org.theiet.rsuite.journals.domain.article.delivery.digitallibrary.ArticleDigitalLibraryNameUtils.*;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.*;
import org.theiet.rsuite.journals.domain.article.Article;
import org.theiet.rsuite.journals.domain.article.ArticleTypesetterContainer;
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
		

		File imageConversionFolder = ProjectFileUtils.getTmpSubDir(context, "imgConvertion", new Date(), true);
		
		try(ZipOutputStream zipStream = new ZipOutputStream(articleDLArchiveStream)) {
			addFinalPdfToArchive(context, user, article, zipStream,
					typesetterContainer.getFinalPdfMO());

			addArticleXMLToArchive(context, user, article, zipStream,
					typesetterContainer.getArticleXMLMO());

			addImagesToArchive(context, zipStream,
					typesetterContainer.getImagesCA(), imageConversionFolder);

		} catch (RSuiteException | IOException e) {
			throw new RSuiteException(0, "Problem with creating archive for "
					+ article, e);
		}finally {
			FileUtils.deleteQuietly(imageConversionFolder);
		}

		

		return articleDLArchiveStream;
	}

	private static void addImagesToArchive(ExecutionContext context,
			ZipOutputStream zipStream, ContentAssembly imagesCA, File imageConversionFolder)
			throws RSuiteException {

		if (imagesCA == null) {
			return;
		}
		
		ImageMagickUtils imageMagickUtils = ImageMagickUtils.getInstance(context);

		for (ContentAssemblyItem child : imagesCA.getChildrenObjects()) {
			if (isImageMoReference(child)) {
				ManagedObject imageMo = MoUtils.getRealMo(context,
						child.getId());
				String fileExtension = FilenameUtils.getExtension(imageMo.getDisplayName());
				if (!fileExtension.equalsIgnoreCase("gif")) {
					addFileToZip(reduceImgDPI(imageMagickUtils, imageConversionFolder, imageMo), imageMo.getDisplayName(), zipStream);
				} else {
					addMoToZip(imageMo, imageMo.getDisplayName(), zipStream);
				}				
			}
		}
	}

	private static File reduceImgDPI(ImageMagickUtils imageMagickUtils, File imageConversionFolder,
			ManagedObject imageMo) throws RSuiteException {
		
		
		File imgFile = writeImageMOtoDrive(imageConversionFolder, imageMo);
		
		
		File outputImgFolder = new File(imageConversionFolder, "output");
		outputImgFolder.mkdirs();
		File outputImg = new File(outputImgFolder, imageMo.getDisplayName());
		
		List<String> parameters = new ArrayList<String>();
		parameters.add("-strip");
		parameters.add("-resample");
		parameters.add("300");

		
		imageMagickUtils.convertImage(imgFile, parameters, outputImg, null);

		return outputImg;
	}

	private static File writeImageMOtoDrive(File imageConversionFolder, ManagedObject imageMo)
			throws RSuiteException {
		File imgFile = new File(imageConversionFolder, imageMo.getDisplayName());
		try {
			FileUtils.writeByteArrayToFile(imgFile, IOUtils.toByteArray(imageMo.getInputStream()));
			return imgFile;	
		}catch (IOException e) {
			throw new RSuiteException(RSuiteException.ERROR_INTERNAL_ERROR, "Unable to write: " + imgFile, e);
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
		try (FileInputStream fis = new FileInputStream(file)){
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

}
