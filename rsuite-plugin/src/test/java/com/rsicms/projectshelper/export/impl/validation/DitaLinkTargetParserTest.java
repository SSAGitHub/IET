package com.rsicms.projectshelper.export.impl.validation;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.w3c.dom.Document;

public class DitaLinkTargetParserTest {

	
	
	@Test
	public void test_parseTargetValues() throws Exception {
		Document document = createSampleDitaDocument();
		
		DitaLinkTargetParser linkTargetParser = new DitaLinkTargetParser();
		Set<String> targetValues = linkTargetParser.parseTargetValues(document.getDocumentElement());
		
		assertThat(targetValues, containsInAnyOrder("topic1/para1", "topic1" ));
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
