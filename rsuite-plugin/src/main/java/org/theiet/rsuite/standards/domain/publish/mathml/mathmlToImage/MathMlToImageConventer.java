package org.theiet.rsuite.standards.domain.publish.mathml.mathmlToImage;

import java.io.File;

import com.reallysi.rsuite.api.RSuiteException;

public interface MathMlToImageConventer {

	
	public void generateImageFromMathMLNotation (String mathMlContent, File outputImageFile, MathMlToImageConventerParameters mathMlToImageConventerParameters) throws RSuiteException;

	public MathMlToImageConventerParameters createMathMlConversionParameters(String mathmlFont, float mathmlFontSize);

}
