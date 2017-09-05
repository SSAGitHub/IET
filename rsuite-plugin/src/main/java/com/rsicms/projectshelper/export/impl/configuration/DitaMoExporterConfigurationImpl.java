package com.rsicms.projectshelper.export.impl.configuration;

import java.util.ArrayList;
import java.util.List;

import com.reallysi.rsuite.api.RSuiteException;
import com.rsicms.projectshelper.export.MoExportConfiguration;
import com.rsicms.projectshelper.export.MoExportHandler;
import com.rsicms.projectshelper.export.impl.handlers.DitaMoExportHandler;

public class DitaMoExporterConfigurationImpl implements MoExportConfiguration {

	private MoExportConfiguration moExporterConfiguration;

	private String variantName;

	public DitaMoExporterConfigurationImpl() throws RSuiteException {

		List<String> referenceXpaths = new ArrayList<String>();
		referenceXpaths.add("@href[not(parent::*/@scope = 'external')]");

		moExporterConfiguration = new DefaultMoExporterConfigurationImpl(
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
		return new DitaMoExportHandler();
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
