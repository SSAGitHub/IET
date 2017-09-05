package com.rsicms.projectshelper.validation.resolvers;

import java.io.IOException;
import java.net.URL;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.SchemaInfo;
import com.reallysi.rsuite.api.extensions.ExecutionContext;
import com.reallysi.rsuite.service.SchemaService;

public class RSuiteEntityResolver implements EntityResolver {

	private Log log = LogFactory.getLog(getClass());

	private SchemaService schemaInfoCollection;

	/**
	 * 
	 * @param schemaInfoCollection
	 */
	public RSuiteEntityResolver(ExecutionContext context) {
		schemaInfoCollection = context.getSchemaService();
	}

	public InputSource resolveEntity(String publicId, String systemId)
			throws SAXException, IOException {

		if (systemId != null) {
			if (systemId.indexOf("/rsuite/schemas") > -1)
				return null;
		}

		try {
			return getEntityInputSource(publicId, systemId);
		} catch (RSuiteException e) {
			log.error(e.getMessage(), e);
		}

		return null;
	}

	public InputSource getEntityInputSource(String publicId, String systemId)
			throws RSuiteException, IOException {

		if (systemId != null && systemId.startsWith("file://")) {
			systemId = systemId.substring("file://".length());
			systemId = systemId.replace("%20", " ");
		}

		SchemaInfo info = schemaInfoCollection.findMatchingSchemaInfo("DTD",
				publicId, systemId);

		if (info != null) {
			String schemaContent = schemaInfoCollection.getSchemaContent(info
					.getSchemaId());
			return new InputSource(IOUtils.toInputStream(schemaContent, "utf-8"));	
		}

		return null;
	}

}
