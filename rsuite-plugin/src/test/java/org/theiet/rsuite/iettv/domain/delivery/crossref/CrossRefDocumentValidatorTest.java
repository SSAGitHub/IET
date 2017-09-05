package org.theiet.rsuite.iettv.domain.delivery.crossref;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.reallysi.rsuite.api.RSuiteException;
import com.rsicms.test.TestHelper;

public class CrossRefDocumentValidatorTest {

	private static EntityResolver entityResolver;

	@BeforeClass
	public static void intialize() {
		entityResolver = createTestEntityResolver();
	}

	@Test
	public void pass_validation_for_valid_document() throws IOException,
			RSuiteException {
		

		String document = TestHelper.getTestResourceAsString(getClass(), "cross_ref_valid.xml");
		
		CrossRefDocumentValidationResult result = CrossRefDocumentValidator.validateCrossRefDocument(document,
				entityResolver);		
		
		System.out.println(result.getValidationErrorMessages());
		Assert.assertTrue(result.isValid());
	}

	@Test
	public void fail_validation_for_invalid_document() throws IOException,
			RSuiteException {
		String document = TestHelper.getTestResourceAsString(getClass(), "cross_ref_invalid.xml");

		CrossRefDocumentValidationResult result = CrossRefDocumentValidator.validateCrossRefDocument(document,
				entityResolver);	
		Assert.assertFalse(result.isValid());			

	}

	private static EntityResolver createTestEntityResolver() {
		return new EntityResolver() {

			@Override
			public InputSource resolveEntity(String publicId, String systemId)
					throws SAXException, IOException {

				String basename = FilenameUtils.getName(systemId);
				File file = new File("src/main/resources/WebContent/doctypes/crossref", basename);

				InputSource inputSource = new InputSource(new FileReader(file));
				return inputSource;
			}

		};
	}

}
