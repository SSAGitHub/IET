package com.rsicms.projectshelper.export.impl.configuration;

import java.util.List;
import java.util.Map;

import com.reallysi.rsuite.api.RSuiteException;
import com.rsicms.projectshelper.export.MoExportConfiguration;
import com.rsicms.projectshelper.export.MoExportHandler;
import com.rsicms.projectshelper.export.ProcessRefenceTrasnformationGenerator;
import com.rsicms.projectshelper.export.impl.handlers.DefaultMoExportHandler;

public class DefaultMoExporterConfigurationImpl implements
		MoExportConfiguration {

	private String refenceTransformationDTD;

	private String refenceTransformationXSD;
	
	private String variantName;
	
	private List<String> referenceXpaths;
	
	private Map<String, String> namespaceDeclarations;

	public DefaultMoExporterConfigurationImpl(List<String> referenceXpaths)
			throws RSuiteException {
	    this.referenceXpaths = referenceXpaths;
		ProcessRefenceTrasnformationGenerator transformationGenerator = new ProcessRefenceTrasnformationGenerator();
		setupRefencesTransformers(transformationGenerator);
		
	}
	
	public DefaultMoExporterConfigurationImpl(Map<String, String> namespaceDeclarations, List<String> referenceXpaths)
            throws RSuiteException {
        this.referenceXpaths = referenceXpaths;
        this.namespaceDeclarations = namespaceDeclarations;
        ProcessRefenceTrasnformationGenerator transformationGenerator = new ProcessRefenceTrasnformationGenerator();
        setupRefencesTransformers(transformationGenerator);
    }

	private void setupRefencesTransformers(ProcessRefenceTrasnformationGenerator transformationGenerator)
			throws RSuiteException {
		setUpRefenceTransformerDTD(transformationGenerator);
		setUpRefenceTransformerXSD(transformationGenerator);
	}

	private void setUpRefenceTransformerDTD(
			ProcessRefenceTrasnformationGenerator transformationGenerator
			) throws RSuiteException {
		refenceTransformationDTD = transformationGenerator
				.generateProcessRefenceTransformationDTD(namespaceDeclarations, referenceXpaths);
	}

	private void setUpRefenceTransformerXSD(
			ProcessRefenceTrasnformationGenerator transformationGenerator) throws RSuiteException {
		refenceTransformationXSD = transformationGenerator
				.generateProcessRefenceTransformationXSD(namespaceDeclarations, referenceXpaths);
	}

	public String getRefenceTransformationDTD() {
		return refenceTransformationDTD;
	}

	public String getRefenceTransformationXSD() {
		return refenceTransformationXSD;
	}

	@Override
	public MoExportHandler createMoExportHandler() {
		return new DefaultMoExportHandler();
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
