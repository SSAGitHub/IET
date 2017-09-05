package com.rsicms.projectshelper.export.impl.subexporter;

import java.io.File;
import java.io.StringReader;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.transform.*;
import javax.xml.transform.sax.*;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.reallysi.rsuite.api.*;
import com.rsicms.projectshelper.export.*;
import com.rsicms.projectshelper.export.impl.refence.ReferenceProcessor;
import com.rsicms.projectshelper.lmd.MetadataUtils;

public class MoExportTransformationHelper {

    private Log logger = LogFactory.getLog(this.getClass());
		
	private MoExportHandler moExportHandler;

	private SAXTransformerFactory transformationFactory;

	private MoExportConfiguration moExporterConfiguration;

	public MoExportTransformationHelper(
			SAXTransformerFactory transformationFactory,
			MoExportHandler moExportHandler,
			MoExportConfiguration moExporterConfiguration) {
		super();
		this.transformationFactory = transformationFactory;
		this.moExportHandler = moExportHandler;
		this.moExporterConfiguration = moExporterConfiguration;
	}

	public TransformerHandler setupMainTransfomerHandler(
			ManagedObject moToExport, ReferenceProcessor referenceProcessor,
			File outputFile) throws TransformerConfigurationException,
			RSuiteException {
		transformationFactory.setErrorListener(new ErrorListener() {
			
			@Override
			public void warning(TransformerException exception)
					throws TransformerException {
				logger.error(exception.getMessage(), exception);
			}
			
			@Override
			public void fatalError(TransformerException exception)
					throws TransformerException {
			    logger.error(exception.getMessage(), exception);
				
			}
			
			@Override
			public void error(TransformerException exception)
					throws TransformerException {
			    logger.error(exception.getMessage(), exception);				
			}
		});
		
		Templates templates1 = transformationFactory
				
				.newTemplates(new StreamSource(new StringReader(getReferenceTranformation(moToExport))));
		
		TransformerHandler mainTransfomerHandler = transformationFactory
				.newTransformerHandler(templates1);
		

		Transformer transformer = mainTransfomerHandler.getTransformer();
		
		setupTransformerParmaters(moToExport, referenceProcessor,
				transformer);

		TransformerHandler lastTransformerHandler = setUpAdditionalTransformerHandlers(
				moToExport, mainTransfomerHandler);

		lastTransformerHandler.setResult(new StreamResult(outputFile));

		return mainTransfomerHandler;
	}

	private String getReferenceTranformation(ManagedObject moToExport)
			throws RSuiteException {

		if (useDTDSchemaType(moToExport)) {
			return moExporterConfiguration.getRefenceTransformationDTD();
		} else {
			return moExporterConfiguration.getRefenceTransformationXSD();
		}

	}

	private boolean useDTDSchemaType(ManagedObject moToExport) {
		SchemaInfo schemaInfo = moToExport.getSchemaInfo();
		SchemaType schemaType = schemaInfo.getType();
		
		return SchemaType.DTD == schemaType;
	}

	private void setupTransformerParmaters(ManagedObject moToExport,
			ReferenceProcessor referenceProcessor, Transformer transformer)
			throws RSuiteException {
		setupBaseTransformerParamters(moToExport, transformer);
		
		transformer.setParameter("rsuite.exporter", referenceProcessor);

		
		if (moExportHandler.embedLmd()){
			List<MetaDataItem> metaDataItems = moToExport.getMetaDataItems();
			List<MetaDataItem> additionalMetaData = moExportHandler.getAdditionalMetaData(moToExport);
			
			if (additionalMetaData != null){
				metaDataItems.addAll(additionalMetaData);
			}
			
			transformer.setParameter("rsuite.lmd.document", MetadataUtils.convertLmdToXmlDocument(metaDataItems));
		}
	}

	private void setupBaseTransformerParamters(ManagedObject moToExport, Transformer transformer)
			throws RSuiteException {
		transformer.setParameter("rsuite.moId", moToExport.getId());
		transformer.setParameter("rsuite.public.id",
				moToExport.getPublicIdProperty());
		transformer.setParameter("rsuite.system.id",
				moToExport.getSystemIdProperty());
	}

	private TransformerHandler setUpAdditionalTransformerHandlers(
			ManagedObject moToExport, TransformerHandler mainTransformerHandler)
			throws RSuiteException, TransformerConfigurationException {

		TransformerHandler lastTransformerHandler = mainTransformerHandler;

		TransformerHandler formerTransformerHandler = mainTransformerHandler;

		List<MoExportAdditionalTransformation> additionalTrasnformations = moExportHandler
				.getTransformationToPerformBeforePersistExportedMo(moToExport);

		if (additionalTrasnformations != null) {

			for (MoExportAdditionalTransformation transformation : additionalTrasnformations) {

				TransformerHandler additionalTransformerHandler = transformationFactory
						.newTransformerHandler(transformation.getTemplate());

				Transformer transformer = additionalTransformerHandler.getTransformer();
				
				setupBaseTransformerParamters(moToExport, transformer);
				setCustomTransformationParameters(transformation,
						additionalTransformerHandler);

				formerTransformerHandler.setResult(new SAXResult(
						additionalTransformerHandler));

				formerTransformerHandler = additionalTransformerHandler;

				lastTransformerHandler = additionalTransformerHandler;
			}
		}
		return lastTransformerHandler;
	}

	private void setCustomTransformationParameters(
			MoExportAdditionalTransformation transformation,
			TransformerHandler additionalTransformerHandler) {
		Map<String, Object> paramters = transformation.getParameters();

		if (paramters != null) {
			Transformer transformer = additionalTransformerHandler
					.getTransformer();
			for (Entry<String, Object> parameterEntry : paramters.entrySet()) {
				transformer.setParameter(parameterEntry.getKey(),
						parameterEntry.getValue());
			}
		}
		
		
	}
}
