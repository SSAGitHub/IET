package org.theiet.rsuite.standards.domain.image;

import static org.theiet.rsuite.standards.domain.image.ImageVariant.*;
import static org.theiet.rsuite.standards.StandardsBooksConstans.*;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.io.FilenameUtils;

import com.reallysi.rsuite.api.RSuiteException;

public final class ImageFileHelper {

	private static final Set<String> MASTER_FILE_EXTENSIONS = new HashSet<String>();

	static {
		initializeMasterFileExtensions();
	}
	
	private ImageFileHelper(){
	}

	private static void initializeMasterFileExtensions() {
		MASTER_FILE_EXTENSIONS.add(FILE_EXTENSION_EPS);
		MASTER_FILE_EXTENSIONS.add(FILE_EXTENSION_TIFF);
	}
	
	
	public static boolean isMasterFile(String fileName) {

		String fileExtension = FilenameUtils.getExtension(fileName);

		if (MASTER_FILE_EXTENSIONS.contains(fileExtension)) {
			return true;
		}

		return false;
	}

	public static boolean isPsdFile(String fileName) {

		String fileExtension = FilenameUtils.getExtension(fileName);

		if (FILE_EXTENSION_PSD.equalsIgnoreCase(fileExtension)) {
			return true;
		}

		return false;
	}
	
	public static boolean isThumbnail2VariantFile(String filename){
		
		String extension = FilenameUtils.getExtension(filename);
		String suffix = getImageFileSuffix(filename);

		if (FILE_EXTENSION_PNG.equals(extension) && THUMBNAIL2.equals(suffix)){
			return true;
		}
		
		return false;
	}


	/**
	 * @param baseName
	 * @return
	 */
	protected static String getImageFileSuffix(String filename) {
		
		String baseName = FilenameUtils.getBaseName(filename);
		
		String suffix = "";
		
		int index = baseName.lastIndexOf(PREFIX_SEPARATOR);
		
		if (index > -1){
			suffix = baseName.substring(index + 1);
		}
		return suffix;
	}
	
	public static String getBaseImageFileName(String filename){
		
		String suffix = getImageFileSuffix(filename);
		String baseName = FilenameUtils.getBaseName(filename);
		
		if (MAIN_WEB.equals(suffix) ||  THUMBNAIL1.equals(suffix) || THUMBNAIL2.equals(suffix)){
		
			return baseName.replace("_" + suffix, "");
		}
		
		return baseName;
	}
	
	public static boolean ifMasterFileExists(File psdFile) {

		for (String masterFileExtension : MASTER_FILE_EXTENSIONS) {
			File masterFile = new File(psdFile.getParentFile(),
					FilenameUtils.getBaseName(psdFile.getName())
							+ SEPARATOR_FILE_EXTENSION + masterFileExtension);

			if (masterFile.exists()) {
				return true;
			}
		}

		return false;
	}

	public static boolean ifPsdFileExists(File masterFile) {
		File psdFile = new File(masterFile.getParentFile(),
				FilenameUtils.getBaseName(masterFile.getName())
						+ SEPARATOR_FILE_EXTENSION + FILE_EXTENSION_PSD);
		return psdFile.exists();
	}


	public static String getMimeTypeFromImageFileName(String fileName){
		String extension = FilenameUtils.getExtension(fileName);
		return "image/" + extension;
	}
	
	public static String getMasterVariantFileName(String baseImageFileName){
		return baseImageFileName + SEPARATOR_FILE_EXTENSION + FILE_EXTENSION_EPS;
	}
	
	public static String getThumbnail1VariantFileName(String baseImageFileName){
		return baseImageFileName + PREFIX_SEPARATOR + THUMBNAIL1.getVariantName() + SEPARATOR_FILE_EXTENSION + FILE_EXTENSION_PNG;
	}
	
	public static String getMainWebVariantFileName(String baseImageFileName){
		return baseImageFileName + PREFIX_SEPARATOR + MAIN_WEB.getVariantName() + SEPARATOR_FILE_EXTENSION + FILE_EXTENSION_PNG;
	}
	
	public static String getOriginalVariantFileName(File imageFolder, String baseImageFileName) throws RSuiteException {
        File imageFile = new File(imageFolder, baseImageFileName + SEPARATOR_FILE_EXTENSION + FILE_EXTENSION_PSD);
        
        if (imageFile.exists()){
            return imageFile.getName();
        }
        
        imageFile = new File(imageFolder, baseImageFileName + SEPARATOR_FILE_EXTENSION + FILE_EXTENSION_TIFF);
        
        if (!imageFile.exists()){
            throw new RSuiteException("Unable to localize the original image for " + baseImageFileName);
        }
        
        return imageFile.getName();
    }
	
	public static String getImageFilePrefix(String filename) {

		if (filename.contains(PREFIX_SEPARATOR)) {
			return filename.substring(0, filename.indexOf(PREFIX_SEPARATOR));
		}

		return "";
	}
}
