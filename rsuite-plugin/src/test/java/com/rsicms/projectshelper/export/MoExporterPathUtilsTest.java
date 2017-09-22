package com.rsicms.projectshelper.export;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;

import com.reallysi.rsuite.api.Alias;
import com.reallysi.rsuite.api.ManagedObject;
import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.VariantDescriptor;
import com.rsicms.projectshelper.export.impl.exportercontext.MoExportContext;
import com.rsicms.projectshelper.export.impl.utils.MoExporterPathUtils;

public class MoExporterPathUtilsTest {

	@Test
	public void test_getDefaultExportPath_filenameAlias() throws RSuiteException {
		ManagedObject moToExport = mock(ManagedObject.class);
		MoExportContext exportContext = mock(MoExportContext.class);
		
		Alias[] aliases = new Alias[2];
		aliases[0] = new Alias("chapter41", "basename");
		aliases[1] = new Alias("chapter41.dita", "filename");
		
		when(moToExport.getAliases()).thenReturn(aliases);
		
		String defaultExportPath = MoExporterPathUtils.getDefaultExportPath(moToExport, exportContext);
		assertEquals("chapter41.dita", defaultExportPath);
	}
	
	
	@Test
	public void test_getDefaultExportPath_basenamAlias() throws RSuiteException {
		ManagedObject moToExport = mock(ManagedObject.class);
		MoExportContext exportContext = mock(MoExportContext.class);
		
		Alias[] aliases = new Alias[1];
		aliases[0] = new Alias("chapter41", "basename");
		
		when(moToExport.getAliases()).thenReturn(aliases);
		when(moToExport.getDisplayName()).thenReturn("chapter41");
		
		
		String defaultExportPath = MoExporterPathUtils.getDefaultExportPath(moToExport, exportContext);
		assertEquals("chapter41", defaultExportPath);
	}

	@Test
	public void test_getDefaultExportPath_noAlias() throws RSuiteException {
		ManagedObject moToExport = mock(ManagedObject.class);
		MoExportContext exportContext = mock(MoExportContext.class);
		
		when(moToExport.getDisplayName()).thenReturn("chapter41");
		
		String defaultExportPath = MoExporterPathUtils.getDefaultExportPath(moToExport, exportContext);
		assertEquals("chapter41", defaultExportPath);
	}
	
	@Test
	public void test_getDefaultExportPath_noAlias_spaces() throws RSuiteException {
		ManagedObject moToExport = mock(ManagedObject.class);
		MoExportContext exportContext = mock(MoExportContext.class);
		
		when(moToExport.getDisplayName()).thenReturn("chapter 41");
		
		String defaultExportPath = MoExporterPathUtils.getDefaultExportPath(moToExport,  exportContext);
		
		assertEquals("chapter_41", defaultExportPath);
	}
	
	@Test
	public void test_getDefaultExportPath_imageVariant() throws RSuiteException {
		
		String variantName = "test";
		
		MoExportContext exportContextStub = mock(MoExportContext.class);
		MoExportConfiguration exportConfigurationStub = mock(MoExportConfiguration.class);
		
		when(exportContextStub.getExportConfiguration()).thenReturn(exportConfigurationStub);
		
		when(exportConfigurationStub.getExportNonXmlVariant()).thenReturn(variantName);
		
		
		ManagedObject moStub = mock(ManagedObject.class);
		
		VariantDescriptor variantDescriptorStub = mock(VariantDescriptor.class);
		when(moStub.getVariant(eq(variantName))).thenReturn(variantDescriptorStub);
		when(moStub.isNonXml()).thenReturn(true);
		when(moStub.getDisplayName()).thenReturn("image.png");
		when(variantDescriptorStub.getExternalAssetPath()).thenReturn("/test/test/045435.eps");
		
		String newExportPath = MoExporterPathUtils.getDefaultExportPath(moStub, exportContextStub);
		
		assertEquals("image.eps", newExportPath);
	}
}
