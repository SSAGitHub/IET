package org.theiet.rsuite.journals.domain.issues.publish.proof.export;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.theiet.rsuite.utils.ImageMagickUtils;

import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.extensions.ExecutionContext;

public class ImageToGrayscaleConventer {

	private List<String> imageMagicParameters;
	
	private ImageMagickUtils imageMagickUtils;
	
	{
		createImageMagickParameters();
	}

	private void createImageMagickParameters() {
		imageMagicParameters = new ArrayList<>();
		imageMagicParameters.add("-colorspace");
		imageMagicParameters.add("gray");
	}
	
	public ImageToGrayscaleConventer(ExecutionContext context) throws RSuiteException{
		this.imageMagickUtils = ImageMagickUtils.getInstance(context);
	}
	
	public ImageToGrayscaleConventer(ImageMagickUtils imageMagickUtils){
		this.imageMagickUtils = imageMagickUtils;
	}
	
	public void convertImagesToGrayScale(File imagesFolder) throws RSuiteException{
		
		File tempFolder = createTempFolder(imagesFolder);
		try{
			moveImageFilesToTempFolder(imagesFolder, tempFolder);	
			convertImages(imagesFolder, tempFolder);
		}catch (IOException e){
			throw new RSuiteException(0, "Unable to convert images to grayscale", e);
		}finally{
			removeTempFolder(tempFolder);
		}
		
		
	}

	private void convertImages(File imagesFolder, File tempFolder) throws RSuiteException {
		imagesFolder.mkdirs();
		
		for (File imageFile : tempFolder.listFiles()){
			File outputFile = new File(imagesFolder, imageFile.getName());
			imageMagickUtils.convertImage(imageFile, imageMagicParameters, outputFile, "");
		}
	}
	
	private void removeTempFolder(File tempFolder) {
		FileUtils.deleteQuietly(tempFolder);
	}


	private void moveImageFilesToTempFolder(File imagesFolder, File tempFolder)
			throws IOException {
		FileUtils.moveDirectory(imagesFolder, tempFolder);
	}

	private File createTempFolder(File imagesFolder) {		
		return new File(imagesFolder.getParentFile(), "temp_images");
	}
}
