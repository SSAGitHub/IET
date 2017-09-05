package com.rsicms.projectshelper.export.impl.validation;

import static org.junit.Assert.assertArrayEquals;

import java.io.IOException;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class DitaLinkTargetParserTest {

	@Test
	public void test_parseTargetValues() throws Exception {
		Document document = createSampleDitaDocument();
		
		DitaLinkTargetParser linkTargetParser = new DitaLinkTargetParser();
		Set<String> targetValues = linkTargetParser.parseTargetValues(document.getDocumentElement());
		
		String[] expected = {"topic1/para1", "topic1" };
		String[] actual = targetValues.toArray(new String[targetValues.size()]);
		assertArrayEquals(expected, 	 actual);
	}

	public static Document createSampleDitaDocument()
			throws Exception {
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
		String xml = "<topic class=\" topic/topic \" id=\"topic1\"><p id=\"para1\"/></topic>";
		
		Document document = documentBuilder.parse(IOUtils.toInputStream(xml));
		return document;
	}

}
