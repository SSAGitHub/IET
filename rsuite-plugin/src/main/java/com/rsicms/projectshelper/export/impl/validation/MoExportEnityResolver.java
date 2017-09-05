package com.rsicms.projectshelper.export.impl.validation;

import java.io.IOException;

import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class MoExportEnityResolver implements EntityResolver {

	private EntityResolver rsuiteAwareEnityResolver;
	
	public MoExportEnityResolver(EntityResolver rsuiteAwareEnityResolver) {
		this.rsuiteAwareEnityResolver = rsuiteAwareEnityResolver;
	}

	@Override
	public InputSource resolveEntity(String publicId, String systemId)
			throws SAXException, IOException {
		
		return rsuiteAwareEnityResolver.resolveEntity(publicId, systemId);
	}

}
