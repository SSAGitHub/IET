package com.rsicms.projectshelper.export.impl.configuration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.reallysi.rsuite.api.RSuiteException;
import com.rsicms.projectshelper.export.MoExportConfiguration;
import com.rsicms.projectshelper.export.MoExportHandler;
import com.rsicms.projectshelper.export.impl.handlers.*;

public class JatsMoExporterConfigurationImpl implements MoExportConfiguration {

	private MoExportConfiguration moExporterConfiguration;

	private String variantName;

	public JatsMoExporterConfigurationImpl() throws RSuiteException {

	    Map<String, String> namespaceDeclarations = new HashMap<String, String>();
	    namespaceDeclarations.put("xlink", "http://www.w3.org/1999/xlink");
	    
		List<String> referenceXpaths = new ArrayList<String>();
		referenceXpaths.add("graphic/@xlink:href");
		referenceXpaths.add("inline-graphic/@xlink:href");
		referenceXpaths.add("media/@xlink:href");
		referenceXpaths.add("related-object/@xlink:href");

		moExporterConfiguration = new DefaultMoExporterConfigurationImpl(namespaceDeclarations,
				referenceXpaths);
	}

	public String getRefenceTransformationDTD() {
		return moExporterConfiguration.getRefenceTransformationDTD();
	}

	public String getRefenceTransformationXSD() {
		return moExporterConfiguration.getRefenceTransformationXSD();
	}

	@Override
	public MoExportHandler createMoExportHandler() {
		return new JatsMoExportHandler();
	}

	@Override
	public String getExportNonXmlVariant() {
		return variantName;
	}

	@Override
	public void setExportNonXmlVariant(String variantName) {
		this.variantName = variantName;

	}

}
