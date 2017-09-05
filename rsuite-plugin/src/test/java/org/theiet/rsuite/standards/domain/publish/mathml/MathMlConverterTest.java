package org.theiet.rsuite.standards.domain.publish.mathml;

import static org.junit.Assert.*;

import java.io.*;

import javax.xml.stream.XMLStreamException;

import org.apache.commons.io.*;
import org.junit.Test;
import org.theiet.rsuite.standards.StandardsBooksConstans;
import org.theiet.rsuite.standards.constans.PublishWorkflowContans;

import com.reallysi.rsuite.api.RSuiteException;

public class MathMlConverterTest implements StandardsBooksConstans, PublishWorkflowContans {

	private static String basePath = getBasePath();

	@Test
	public void test_convertMathMlInXMLContent_mathMLReplacement() throws Exception {
		
		MathMlEntryProcessor imageGeneratorStub = new MathMlImageGeneratorTestable(null, getMathMlEntryProcessorParameters());		
		MathMlConventer mathMlConventer = new MathMlConventer(imageGeneratorStub);
		
		String outputXML = convertTestFile(mathMlConventer, "test_mathml_convert.xml");

		assertEquals(
				"<?xml version=\"1.0\" encoding=\"UTF-8\"?><body><image href=\"equation_1.pdf\" class=\"- topic/image \"></image></body>",
				outputXML);
	}
	
	@Test
	public void test_convertMathMlInXMLContent_testForeginNamespaceElements() throws Exception {
		
		MathMlEntryProcessor imageGenerator = new MathMlImageGeneratorTestable(null, getMathMlEntryProcessorParameters());		
		MathMlConventer mathMlConventer = new MathMlConventer(imageGenerator);
		
		String inputXML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><body><r:test xmlns:r=\"http://sample.com\"><r:test2></r:test2></r:test></body>";
		
		String outputXML = convertTestConent(mathMlConventer, inputXML);
		
		assertEquals(
				inputXML,
				outputXML);
	}
	
	@Test
	public void test_convertMathMlInXMLContent_testExtractedMathml() throws Exception {
		
		MathMlImageGeneratorTestable imageGeneratorMock = new MathMlImageGeneratorTestable(null, getMathMlEntryProcessorParameters());		
		MathMlConventer mathMlConventer = new MathMlConventer(imageGeneratorMock);
		
		String inputXML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><body><d4p_MathML><m:math xmlns:m=\"http://www.w3.org/1998/Math/MathML\"><m:mrow></m:mrow></m:math></d4p_MathML></body>";
		
		convertTestConent(mathMlConventer, inputXML);

		assertEquals(
				"<m:math xmlns:m=\"http://www.w3.org/1998/Math/MathML\"><m:mrow></m:mrow></m:math>",
				imageGeneratorMock.getMathMlXML());
	}
	
	@Test
	public void test_extractMathMl_blockMathML_() throws Exception {
		
		MathMlImageGeneratorTestable imageGeneratorMock = new MathMlImageGeneratorTestable(null, getMathMlEntryProcessorParameters());		
		MathMlConventer mathMlConventer = new MathMlConventer(imageGeneratorMock);
		
		String testFileName = "test_mathml_extract.xml";
		
		convertTestFile(mathMlConventer, testFileName);

		String extractedMathMl = imageGeneratorMock.getMathMlXML();
		extractedMathMl = normalizeXMLContent(extractedMathMl);

		String resultFileName = "test_mathml_extract_result.xml";
		        
		
		String expectedXML = getResultFileConent(resultFileName);

		assertEquals(
				expectedXML,
				extractedMathMl);
	}

    private String getResultFileConent(String resultFileName) throws IOException {
        File resultFile = getTestFile(resultFileName);
		String expectedXML = FileUtils.readFileToString(resultFile);
        return normalizeXMLContent(expectedXML);
    }
	
	@Test
    public void test_convertMathMlInXMLContent_InlineMathMl() throws Exception {
        
        MathMlImageGeneratorTestable imageGeneratorMock = new MathMlImageGeneratorTestable(null, getMathMlEntryProcessorParameters());       
        MathMlConventer mathMlConventer = new MathMlConventer(imageGeneratorMock);

        String testFileName = "test_mathml_inline.xml";
        
        String convertedXML = convertTestFile(mathMlConventer, testFileName);

        String expectedXML = getResultFileConent("test_mathml_inline_convert_result.xml");

      
        assertEquals(
                expectedXML,
                convertedXML);
    }
	
	@Test
    public void test_convertMathMlInXMLContent_twoEquationsInOneD4pDisplayEquation() throws Exception {
        
        MathMlImageGeneratorTestable imageGeneratorMock = new MathMlImageGeneratorTestable(null, getMathMlEntryProcessorParameters());       
        MathMlConventer mathMlConventer = new MathMlConventer(imageGeneratorMock);

        String testFileName = "test_mathml_two_equations.xml";
        
        String convertedXML = convertTestFile(mathMlConventer, testFileName);

        String expectedXML = getResultFileConent("test_mathml_two_equations_convert_result.xml");

      
        assertEquals(
                expectedXML,
                convertedXML);
    }

		private MathMlEntryProcessorParameters getMathMlEntryProcessorParameters() {
    	
		MathMlEntryProcessorParameters mathMlEntryProcessorParameters = new MathMlEntryProcessorParameters("arial", 25);

		return mathMlEntryProcessorParameters;		
	}

    private String convertTestFile(MathMlConventer mathMlConventer, String testFileName)
            throws XMLStreamException, RSuiteException, IOException {
        File testFile = getTestFile(testFileName);
        String testContent = FileUtils.readFileToString(testFile);
        return convertTestConent(mathMlConventer, testContent);
    }
    
    private String convertTestConent(MathMlConventer mathMlConventer, String testContent)
            throws XMLStreamException, RSuiteException, IOException {
        InputStream testFileInputStream = new ByteArrayInputStream(testContent.getBytes("utf-8"));        
        ByteArrayOutputStream outputXMLStream = new ByteArrayOutputStream();

        mathMlConventer.convertMathMlInXMLContent(testFileInputStream,
                outputXMLStream);
        
        String convertedFile = new String(outputXMLStream.toByteArray()); 
        return normalizeXMLContent(convertedFile);
    }
	
    private String normalizeXMLContent(String xmlContent){
        return xmlContent.replaceAll(">[\\s]+<", "><");
    }

	private File getTestFile(String fileName) {
		return new File(basePath, fileName);

	}

	private static String getBasePath() {
		String className = MathMlConverterTest.class.getName();
		String classPath = className.replace(".", "/");
		String packagePath = FilenameUtils.getPathNoEndSeparator(classPath);
		return "src/test/resources/" + packagePath;
	}
	
	private class MathMlImageGeneratorTestable extends MathMlEntryProcessorImpl{
	
		private String mathMlXML;
		
		private int counter = 0;
		
		public MathMlImageGeneratorTestable(File imagesFolder, MathMlEntryProcessorParameters mathMlEntryProcessorParameters) {
			super(imagesFolder, mathMlEntryProcessorParameters);
		}

		@Override
		public String convertMathMlToImage(File contextFile, String mathMlXML) {
			this.mathMlXML = mathMlXML;
			return "equation_" + ++counter +".pdf";
		}

		public String getMathMlXML() {
			return mathMlXML;
		}				
		
	}
}
