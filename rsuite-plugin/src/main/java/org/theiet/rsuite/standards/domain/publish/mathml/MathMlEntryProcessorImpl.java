package org.theiet.rsuite.standards.domain.publish.mathml;

import static org.theiet.rsuite.standards.domain.publish.mathml.mathmlToImage.MathMlToImageConventerType.JEUCLID;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.theiet.rsuite.standards.StandardsBooksConstans;
import org.theiet.rsuite.standards.domain.publish.mathml.mathmlToImage.MathMlToImageConventer;
import org.theiet.rsuite.standards.domain.publish.mathml.mathmlToImage.MathMlToImageConventerFactory;
import org.theiet.rsuite.standards.domain.publish.mathml.mathmlToImage.MathMlToImageConventerParameters;

import com.reallysi.rsuite.api.RSuiteException;
import com.rsicms.projectshelper.utils.ProjectFileUtils;

public class MathMlEntryProcessorImpl implements MathMlEntryProcessor, StandardsBooksConstans {
	private int imageCounter = 0;
	
	private File imagesFolder;
	
	private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddhhmmss");

	private MathMlEntryProcessorParameters mathMlEntryProcessorParameters;

	public MathMlEntryProcessorImpl(File imagesFolder, MathMlEntryProcessorParameters mathMlEntryProcessorParameters) {
		this.imagesFolder = imagesFolder;
		this.mathMlEntryProcessorParameters = mathMlEntryProcessorParameters;
	}

	@Override
	public String convertMathMlToImage(File contextFile, String mathMlXML) throws RSuiteException{		
		File imageFile = createImageFileObject();		
		generateImage(mathMlXML, imageFile);
		return ProjectFileUtils.computeRelativePath(contextFile.getAbsolutePath(), imageFile.getAbsolutePath());
	}

	private File createImageFileObject() {
		String suffix = dateFormat.format(new Date());
		String imageFileName = "equation_" + imageCounter + "_" + suffix + ".pdf";
		File imageFile  = new File(imagesFolder, imageFileName);
		imageCounter++;
		return imageFile;
	}
	
	private void generateImage(String mathMlXML, File imageFile) throws RSuiteException {
		MathMlToImageConventer mathMlEngine = MathMlToImageConventerFactory.getConventer(JEUCLID);
		MathMlToImageConventerParameters mathMlEngineParams = 
				mathMlEngine.createMathMlConversionParameters(mathMlEntryProcessorParameters.getFontName(), mathMlEntryProcessorParameters.getFontSize());		
		mathMlEngine.generateImageFromMathMLNotation(mathMlXML, imageFile, mathMlEngineParams);
	}
	
}
