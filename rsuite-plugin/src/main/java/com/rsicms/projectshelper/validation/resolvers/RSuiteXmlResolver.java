package com.rsicms.projectshelper.validation.resolvers;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.HashSet;
import java.util.Set;

import javax.xml.stream.XMLResolver;
import javax.xml.stream.XMLStreamException;

import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;

public class RSuiteXmlResolver implements XMLResolver {

	private EntityResolver resuiteAwareEntityResolver;

	private Set<String> schemaSet = new HashSet<String>();

	public RSuiteXmlResolver(EntityResolver mEntityResolver) {
		this.resuiteAwareEntityResolver = mEntityResolver;
	}

	@Override
	public Object resolveEntity(String publicID, String systemID,
			String baseURI, String namespace) throws XMLStreamException {
		if (resuiteAwareEntityResolver != null) {
			try {
				InputSource isrc = resuiteAwareEntityResolver.resolveEntity(
						publicID, systemID);

				schemaSet.add(systemID);
				if (isrc != null) {
					InputStream in = isrc.getByteStream();
					if (in != null) {
						return in;
					}
					Reader r = isrc.getCharacterStream();
					if (r != null) {
						return r;
					}
				}

			} catch (IOException ex) {
				throw new XMLStreamException(ex);
			} catch (Exception ex) {
				throw new XMLStreamException(ex.getMessage(), ex);
			}
		}
		return null;
	}

	public Set<String> getSchemaSet() {
		return schemaSet;
	}

}
