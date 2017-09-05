package org.theiet.rsuite.standards.domain.image;

import static org.theiet.rsuite.standards.StandardsBooksConstans.*;
import static org.theiet.rsuite.standards.domain.image.ImageFileHelper.*;
import static org.theiet.rsuite.standards.domain.image.ImageFileNormalizer.*;
import static org.theiet.rsuite.standards.domain.image.ImageFilesValidator.*;
import static org.theiet.rsuite.standards.domain.image.ImageVariant.*;

import java.io.*;
import java.util.*;

import org.apache.commons.logging.Log;

import com.reallysi.rsuite.api.*;
import com.reallysi.rsuite.api.control.*;
import com.reallysi.rsuite.api.extensions.ExecutionContext;
import com.reallysi.rsuite.service.*;

public class ImageFilesIngestionHelper {

	private User user;

	private ManagedObjectService managedObjectService;

	private ContentAssemblyService contentAssemblyService;

	private ImageVariantFileGenerator variantFileGenerator;

	private Log logger;
	
	public ImageFilesIngestionHelper(ExecutionContext context, Log logger)
			throws RSuiteException {

		user = context.getAuthorizationService().getSystemUser();
		managedObjectService = context.getManagedObjectService();
		contentAssemblyService = context.getContentAssemblyService();

		File imageMagickHomeFolder = managedObjectService
				.getImageMagickConfiguration().getHomeDir();

		variantFileGenerator = new ImageVariantFileGenerator(
				imageMagickHomeFolder);
		this.logger = logger;
	}

	public void ingestImages(File imagesBaseFolder,
			ContentAssembly booksImagesCA, String bookPrefix)
			throws RSuiteException {

		normalizeFileNames(imagesBaseFolder, bookPrefix);
		validateInputImages(imagesBaseFolder);

		variantFileGenerator.generateWebVariantsFiles(imagesBaseFolder);

		uploadImagesToRSuite(imagesBaseFolder, booksImagesCA);
	}

	private void uploadImagesToRSuite(File imagesBaseFolder,
			ContentAssembly contextCA) throws RSuiteException {

		for (File file : imagesBaseFolder.listFiles()) {

			if (file.isDirectory()) {
				ContentAssembly newFolder = createRsuiteFolder(file.getName(),
						contextCA);
				uploadImagesToRSuite(file, newFolder);
			} else if (isThumbnail2VariantFile(file.getName())) {
				file = normalizeThumbnail2VariantFile(file);
				uploadImageToCA(file, contextCA);
			}
		}

	}

	private File normalizeThumbnail2VariantFile(File file) {
		String normalizedName = getBaseImageFileName(file.getName()) + SEPARATOR_FILE_EXTENSION + FILE_EXTENSION_PNG;
		File normalizedFile = new File(file.getParentFile(), normalizedName);
		file.renameTo(normalizedFile);
		return normalizedFile;
	}

	private ContentAssembly createRsuiteFolder(String folderName,
			ContentAssembly contextCA) throws RSuiteException {
		ContentAssemblyCreateOptions createOptions = new ContentAssemblyCreateOptions();
		createOptions.setType("folder");
		createOptions.setSilentIfExists(true);

		return contentAssemblyService.createContentAssembly(user,
				contextCA.getId(), folderName, createOptions);
	}

	private void uploadImageToCA(File file, ContentAssembly contextCA)
			throws RSuiteException {

		try {
			
			
			ManagedObject managedObject = managedObjectService.getObjectByAlias(user, file.getName());
			
			if (managedObject != null){
				logger.info("Managed object with given name already exist " + file.getName());
				return;
			}
			
			ObjectInsertOptions options = createInsertOptions(file);
			NonXmlObjectSource nonXMLSource = new NonXmlObjectSource(file);
			ManagedObject mo = managedObjectService.load(user, nonXMLSource, options);
			contentAssemblyService.attach(user, contextCA.getId(), mo, new ObjectAttachOptions());
			logger.info("Uploaded " + file.getName());
		} catch (IOException e) {
			throw new RSuiteException(0, "Unable to upload image "
					+ file.getName(), e);
		} catch (RSuiteException e) {
			throw new RSuiteException(0, "Unable to upload image "
					+ file.getName(), e);
		}

	}

	protected ObjectInsertOptions createInsertOptions(File file) throws RSuiteException {
		String fileName = file.getName();

		List<VariantDescriptor> variants = createVariants(file);

		ObjectInsertOptions options = ObjectInsertOptions
				.constructOptionsForNonXml(fileName,
						ImageFileHelper.getMimeTypeFromImageFileName(fileName),
						null, null);
		options.setVariants(variants);
		return options;
	}


	private List<VariantDescriptor> createVariants(File thumbnail2File) throws RSuiteException {

		List<VariantDescriptor> variants = new ArrayList<VariantDescriptor>();

		String baseImageFileName = getBaseImageFileName(thumbnail2File
				.getName());
		File imageFileFolder = thumbnail2File.getParentFile();

		String thumbnail1FileName = getThumbnail1VariantFileName(baseImageFileName);
		variants.add(createVariantDecriptor(THUMBNAIL1,
				imageFileFolder, thumbnail1FileName));

		String masterFileName = getMasterVariantFileName(baseImageFileName);
		variants.add(createVariantDecriptor(MASTER,
				imageFileFolder, masterFileName));

		String mainWebFileName = getMainWebVariantFileName(baseImageFileName);
		variants.add(createVariantDecriptor(MAIN_WEB,
				imageFileFolder, mainWebFileName));

	     String originalFileName = getOriginalVariantFileName(imageFileFolder, baseImageFileName);
	     variants.add(createVariantDecriptor(ORIGINAL,
	                imageFileFolder, originalFileName));
		
		return variants;
	}

	

    private VariantDescriptor createVariantDecriptor(ImageVariant variant,
			File imageFileFolder, String imageFilename) {

		String mimeType = getMimeTypeFromImageFileName(imageFilename);
		File imageFile = new File(imageFileFolder, imageFilename);

		return VariantDescriptor.constructFromFile(variant.getVariantName(), mimeType,
				imageFile);
	}

}
