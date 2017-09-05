package com.rsicms.projectshelper.export.impl.validation;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import java.io.Serializable;
import java.util.Set;

import org.junit.Test;
import org.mockito.Mockito;
import org.w3c.dom.Document;

import com.reallysi.rsuite.api.ManagedObject;

public class DefaultMoExportCrossReferenceValidatorTest {

	@Test
	public void test_getTargetValueFromLink_localTarget() {
		DefaultMoExportCrossReferenceValidator validator = createCrossReferenceValidator();
		Serializable actual = validator.getTargetValueFromLink("#r2-definitions/Fuse_link");
		
		assertEquals("r2-definitions/Fuse_link", actual);
	}

	@Test
	public void test_getTargetValueFromLink_localEmptyTarget() {
		DefaultMoExportCrossReferenceValidator validator = createCrossReferenceValidator();
		Serializable actual = validator.getTargetValueFromLink("#");
		
		assertEquals("", actual);
	}
	
	@Test
	public void test_getTargetValueFromLink_externalTarget() {
		DefaultMoExportCrossReferenceValidator validator = createCrossReferenceValidator();
		Serializable actual = validator.getTargetValueFromLink("test.dita#r2-definitions/Fuse_link");
		
		assertEquals("r2-definitions/Fuse_link", actual);
	}
	
	@Test
	public void test_parseReferenceTargets() throws Exception {
		DefaultMoExportCrossReferenceValidator validator = createCrossReferenceValidator();
		Document document = DitaLinkTargetParserTest.createSampleDitaDocument();
		
		ManagedObject managedObjectStub = mock(ManagedObject.class);
		when(managedObjectStub.getElement()).thenReturn(document.getDocumentElement());
		
		Set<String> referenceTargets = validator.parseReferenceTargets(managedObjectStub);
		
		String[] expected = {"para1", "topic1" };
		String[] actual = referenceTargets.toArray(new String[referenceTargets.size()]);
		assertArrayEquals(expected, 	 actual);
	}

	public DefaultMoExportCrossReferenceValidator createCrossReferenceValidator() {
		return new DefaultMoExportCrossReferenceValidator();
	}
}
