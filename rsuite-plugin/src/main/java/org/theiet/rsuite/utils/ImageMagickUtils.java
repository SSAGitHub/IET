package org.theiet.rsuite.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.extensions.ExecutionContext;
import com.reallysi.rsuite.service.ManagedObjectService;
import com.rsicms.projectshelper.system.*;

public final class ImageMagickUtils {

	private static final String COMMAND_IMAGEMAGICK_CONVERT = "convert";

	private static ImageMagickUtils instance;

	private File imageMagickBinFolder;

	private ImageMagickUtils(File imageMagickHomeFolder) throws RSuiteException {

		if (imageMagickHomeFolder == null) {
			throw new RSuiteException(
					"Image magick home folder is not configured");
		}

		String path = "\"" + imageMagickHomeFolder.getAbsolutePath() + "\"";

		if (!imageMagickHomeFolder.exists()) {
			throw new RSuiteException("ImageMagick home directory " + path
					+ " does not exist.");
		}
		if (!imageMagickHomeFolder.canRead()) {
			throw new RSuiteException("ImageMagick home directory " + path
					+ " cannot be read.");
		}

		if (!imageMagickHomeFolder.isDirectory()) {
			throw new RSuiteException("ImageMagick home directory specified "
					+ path + " exists but is not a directory.");
		}

		imageMagickBinFolder = new File(imageMagickHomeFolder, "bin");
		
		if (!imageMagickBinFolder.exists()){
			imageMagickBinFolder = imageMagickHomeFolder;
		}

	}

	public void convertImage(File inputFile, List<String> parameters,
			File outputFile, String imageFrame) throws RSuiteException {
		List<String> commandList = new ArrayList<String>();
		commandList.add(imageMagickBinFolder + "/" + COMMAND_IMAGEMAGICK_CONVERT);

		String inputPath = inputFile.getAbsolutePath();

		if (StringUtils.isNotBlank(imageFrame)) {
			inputPath += "["  + imageFrame +"]";
		}

		commandList.add(inputPath);

		commandList.addAll(parameters);
		commandList.add(outputFile.getAbsolutePath());

		CommandExecutionResult result = SystemUtils.executeOScommand(
				commandList);

		StringBuilder error = result.getError();

		if (error != null && StringUtils.isNotBlank(error.toString())) {
			throw new RSuiteException(error.toString());
		}

		if (!outputFile.exists()) {
			throw new RSuiteException("The output file "
					+ outputFile.getAbsolutePath() + " hasn't been created");
		}
	}

	public static ImageMagickUtils getInstance(File imageMagickHomeFolder)
			throws RSuiteException {

		if (instance == null) {
			instance = new ImageMagickUtils(imageMagickHomeFolder);
		}

		return instance;
	}
	
	public static ImageMagickUtils getInstance(ExecutionContext context)
			throws RSuiteException {
		
		if (instance == null) {
			ManagedObjectService managedObjectService = context.getManagedObjectService();
			File imageMagickHomeFolder = managedObjectService
					.getImageMagickConfiguration().getHomeDir();
			instance = new ImageMagickUtils(imageMagickHomeFolder);
		}

		return instance;
	}
	
	

}
