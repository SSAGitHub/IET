package org.theiet.rsuite.standards.domain.image;

import static org.theiet.rsuite.standards.domain.image.ImageFileHelper.*;
import static org.theiet.rsuite.standards.StandardsBooksConstans.PREFIX_SEPARATOR;
import static org.theiet.rsuite.standards.StandardsBooksConstans.SEPARATOR_FILE_EXTENSION;

import java.io.File;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;

import com.reallysi.rsuite.api.RSuiteException;

final class ImageFileNormalizer {

	private ImageFileNormalizer() {
	}
	
	protected static  void normalizeFileNames(
			File imageFolder, String bookPrefix) throws RSuiteException {

		for (File file : imageFolder.listFiles()) {

			if (file.isDirectory()) {
				normalizeFileNames(file,
						bookPrefix);
			} else {
				normalizeFile(bookPrefix, file);
			}
		}
	}

	protected static void normalizeFile(String bookPrefix, File file)
			throws RSuiteException {
		String filename = file.getName();

		String newFileName = createNormalizedFileName(bookPrefix,
				filename);

		if (!filename.equals(newFileName)) {
			File normalizedFile = new File(file.getParentFile(),
					newFileName);
			file.renameTo(normalizedFile);
		}
	}
	
	protected static String createNormalizedFileName(String bookPrefix, String filename)
			throws RSuiteException {
		
		String filePrefix = getImageFilePrefix(filename);

		String extension = FilenameUtils.getExtension(filename);
		String baseName = FilenameUtils.getBaseName(filename);
		String newFileName = baseName + SEPARATOR_FILE_EXTENSION
				+ extension.toLowerCase();

		if (StringUtils.isEmpty(filePrefix) || !bookPrefix.equals(filePrefix)) {
			newFileName = bookPrefix + PREFIX_SEPARATOR + newFileName;
		}
		return newFileName;
	}
	
}
