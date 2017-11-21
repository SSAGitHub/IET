package com.rsicms.external.mathml.image.jeuclid;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.xml.parsers.ParserConfigurationException;

import net.sourceforge.jeuclid.MathMLParserSupport;
import net.sourceforge.jeuclid.context.LayoutContextImpl;
import net.sourceforge.jeuclid.context.Parameter;
import net.sourceforge.jeuclid.converter.Converter;
import net.sourceforge.jeuclid.converter.ConverterRegistry;

import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.commons.io.FilenameUtils;
import org.apache.fop.svg.AbstractFOPTranscoder;
import org.apache.fop.svg.PDFTranscoder;
import org.theiet.rsuite.standards.domain.publish.mathml.mathmlToImage.MathMlToImageConventer;
import org.theiet.rsuite.standards.domain.publish.mathml.mathmlToImage.MathMlToImageConventerParameters;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import com.reallysi.rsuite.api.RSuiteException;

public class JEuclidMathMlEngine implements MathMlToImageConventer {

	@Override
	public void generateImageFromMathMLNotation (String mathMlContent, File outputImageFile, MathMlToImageConventerParameters jeuclidParams) throws RSuiteException {
		try {						
			String outputFileName = outputImageFile.getName();
			String outFileType = ConverterRegistry.getInstance().getMimeTypeForSuffix(FilenameUtils.getExtension(outputFileName));
			
			if ("application/pdf".equals(outFileType)){
				File tempFile = new File(outputImageFile.getParentFile(), FilenameUtils.getBaseName(outputFileName) + ".svg");
				generateImageFromMathMLNotation(mathMlContent, tempFile, jeuclidParams);
				convertSVGToPDF(tempFile, outputImageFile);
				tempFile.delete();
				
			}else{
				generateImageFromMathMl(mathMlContent, outputImageFile, outFileType, jeuclidParams);
			}
			
		} catch (IOException ex) {
			throw org.theiet.rsuite.utils.ExceptionUtils.createRsuiteException(generateErrorMessage(mathMlContent, outputImageFile) ,ex);
		} catch (SAXException ex) {
			throw org.theiet.rsuite.utils.ExceptionUtils.createRsuiteException(generateErrorMessage(mathMlContent, outputImageFile) ,ex);
		} catch (ParserConfigurationException ex) {
			throw org.theiet.rsuite.utils.ExceptionUtils.createRsuiteException(generateErrorMessage(mathMlContent, outputImageFile) ,ex);
		} catch (TranscoderException ex) {
			throw org.theiet.rsuite.utils.ExceptionUtils.createRsuiteException(generateErrorMessage(mathMlContent, outputImageFile) ,ex);
		}
	}

	@Override
	public MathMlToImageConventerParameters createMathMlConversionParameters(
			String mathmlFont, float fontSize) {
		JEuclidMathMlEngineParameters jeuclidParams =
				new JEuclidMathMlEngineParameters(mathmlFont, fontSize);
		return jeuclidParams;
	}

	private String generateErrorMessage(String mathMlContent,
			File outputImageFile) {
		return "Unable to generate image " + outputImageFile.getAbsolutePath() + " for: " + mathMlContent;
	}

	private void generateImageFromMathMl(String mmlFile, File outputImageFile,
			String outFileType, MathMlToImageConventerParameters jeuclidParams) throws SAXException,
			ParserConfigurationException, IOException {
		LayoutContextImpl localMutableLayoutContext = new LayoutContextImpl(LayoutContextImpl.getDefaultLayoutContext());
		localMutableLayoutContext.setParameter(Parameter.MATHSIZE, jeuclidParams.getMathSize());
		localMutableLayoutContext.setParameter(Parameter.FONTS_SERIF, jeuclidParams.getFont());
		Document doc = MathMLParserSupport.parseString(mmlFile);				
		Converter.getInstance().convert(doc, outputImageFile, outFileType, localMutableLayoutContext);
	}
	
	private void convertSVGToPDF(File svgFile, File pdfFile) throws IOException, TranscoderException {
		AbstractFOPTranscoder transcoder = new PDFTranscoder();
    	
    	TranscoderInput input = new TranscoderInput(new FileInputStream(svgFile));
        OutputStream os = new FileOutputStream(pdfFile);
        TranscoderOutput output = new TranscoderOutput(os);
        
        transcoder.transcode(input, output);
        os.flush();
        os.close();
		
	}

}
