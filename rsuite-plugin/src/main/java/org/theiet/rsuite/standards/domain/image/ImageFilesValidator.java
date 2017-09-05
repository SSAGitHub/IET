package org.theiet.rsuite.standards.domain.image;

import static org.theiet.rsuite.standards.domain.image.ImageFileHelper.ifMasterFileExists;
import static org.theiet.rsuite.standards.domain.image.ImageFileHelper.ifPsdFileExists;
import static org.theiet.rsuite.standards.domain.image.ImageFileHelper.isMasterFile;
import static org.theiet.rsuite.standards.domain.image.ImageFileHelper.isPsdFile;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FilenameUtils;

import com.reallysi.rsuite.api.RSuiteException;

final class ImageFilesValidator {
	
	private ImageFilesValidator() {
	}

	protected static void validateInputImages(File imagesBaseFolder)
			throws RSuiteException {

		Set<String> validatedFiles = new HashSet<String>();
		List<String> invalidFiles = new ArrayList<String>();

		File currentFolder = imagesBaseFolder;

		validateImageFiles(imagesBaseFolder, currentFolder, validatedFiles,
				invalidFiles);

		if (invalidFiles.size() > 0) {
			StringBuilder errorMessage = new StringBuilder(
					"Unable to ingest following files: ");
			for (String invalidFile : invalidFiles) {
				errorMessage.append("\n").append(invalidFile);
			}

			throw new RSuiteException(errorMessage.toString());
		}

	}

	protected static void validateImageFiles(File imagesBaseFolder,
			File currentFolder, Set<String> validatedFiles,
			List<String> invalidFiles) throws RSuiteException {
		File[] files = currentFolder.listFiles();

		for (File file : files) {

			if (file.isDirectory()) {
				validateImageFiles(imagesBaseFolder, file, validatedFiles,
						invalidFiles);
			} else {
				validateImageFile(imagesBaseFolder, file, validatedFiles,
						invalidFiles);
			}
		}
	}

	private static void validateImageFile(File imagesBaseFolder, File file,
			Set<String> validatedFiles, List<String> invalidFiles) {
		String fileName = file.getName();
		String baseName = FilenameUtils.getBaseName(fileName);

		if (validatedFiles.contains(baseName)) {
			return;
		}

		if ((isMasterFile(fileName) && ifPsdFileExists(file))
				|| (isPsdFile(fileName) && ifMasterFileExists(file))) {
			validatedFiles.add(baseName);
		} else {

			String baseImagePath = imagesBaseFolder.getAbsolutePath();
			String fileImagePath = file.getAbsolutePath();

			invalidFiles.add(fileImagePath.replace(baseImagePath, ""));
		}

	}

}
