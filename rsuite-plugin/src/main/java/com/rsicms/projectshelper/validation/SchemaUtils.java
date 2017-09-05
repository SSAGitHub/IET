package com.rsicms.projectshelper.validation;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Set;
import java.util.zip.ZipOutputStream;

import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;

import org.xml.sax.EntityResolver;

import com.reallysi.rsuite.api.ManagedObject;
import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.extensions.ExecutionContext;
import com.reallysi.rsuite.api.remoteapi.RemoteApiExecutionContext;
import com.reallysi.rsuite.service.SchemaService;
import com.rsicms.projectshelper.validation.resolvers.RSuiteEntityResolver;
import com.rsicms.projectshelper.validation.resolvers.RSuiteXmlResolver;
import com.rsicms.rsuite.helpers.utils.ZipUtil;

public class SchemaUtils {

	public static ByteArrayOutputStream archiveAllSchemas(
			RemoteApiExecutionContext context) throws RSuiteException {
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		ZipOutputStream zipOutputStream = new ZipOutputStream(outStream);

		String schemasBasePath = "/$SCHEMAS$/";
		String[] uris = context.getRepositoryService()
				.getDocumentsUrisInDirectory(schemasBasePath);

		try {
			for (String uri : uris) {
				String content = context.getRepositoryService().getFileContent(
						uri);
				ZipUtil.addBytesToZip(uri.replace(schemasBasePath, ""),
						content.getBytes(), zipOutputStream);
			}

		} catch (IOException e) {
			throw new RSuiteException(RSuiteException.ERROR_INTERNAL_ERROR,
					"Unable to obtain all schemas", e);
		}

		ZipUtil.closeZipStream(zipOutputStream);
		return outStream;
	}

	public static ByteArrayOutputStream archiveSchemasForGivenMo(
			ExecutionContext context, ManagedObject mo) throws RSuiteException {

		Set<String> list = obtainRelatedSchemaSet(context, mo);
		return zipRelatedSchemas(context, mo, list);
	}

	private static Set<String> obtainRelatedSchemaSet(ExecutionContext context,
			ManagedObject mo) throws FactoryConfigurationError, RSuiteException {

		EntityResolver rsuiteEntityResolver = new RSuiteEntityResolver(
				context);

		XMLInputFactory factory = XMLInputFactory.newFactory();
		RSuiteXmlResolver resolver = new RSuiteXmlResolver(rsuiteEntityResolver);
		factory.setXMLResolver(resolver);

		try {
			XMLEventReader reader = factory.createXMLEventReader(mo
					.getInputStream());
			while (reader.hasNext()) {
				XMLEvent event = reader.nextEvent();
				if (event.isStartElement()) {
					break;
				}
			}
		} catch (XMLStreamException e) {
			handleExcetiption(mo, e);
		}

		return resolver.getSchemaSet();
	}

	private static ByteArrayOutputStream zipRelatedSchemas(
			ExecutionContext context, ManagedObject mo, Set<String> list)
			throws RSuiteException {
		SchemaService schemarService = context.getSchemaService();

		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		ZipOutputStream zipOutputStream = new ZipOutputStream(outStream);

		String schemasBasePath = "/$SCHEMAS$/";
		String rsuiteSchemaPart = "/rsuite/schemas/";

		try {

			for (String uri : list) {

				int index = uri.indexOf(rsuiteSchemaPart);
				if (index > -1) {
					uri = uri.substring(index + rsuiteSchemaPart.length());
				} else {
					uri = schemarService.findSchemaInfoBySuffixMatching(uri)
							.getUri();
					uri = uri.replace(schemasBasePath, "");
				}

				String content = schemarService.getSchemaContent(uri);

				if (content == null) {
					continue;
				}

				ZipUtil.addBytesToZip(uri.replace(schemasBasePath, ""),
						content.getBytes(), zipOutputStream);

			}

		} catch (IOException e) {
			handleExcetiption(mo, e);
		}

		ZipUtil.closeZipStream(zipOutputStream);
		return outStream;
	}

	private static void handleExcetiption(ManagedObject mo, Exception e)
			throws RSuiteException {
		throw new RSuiteException(RSuiteException.ERROR_INTERNAL_ERROR,
				"Unable to obtain schemas for mo " + mo.getId(), e);
	}

}
