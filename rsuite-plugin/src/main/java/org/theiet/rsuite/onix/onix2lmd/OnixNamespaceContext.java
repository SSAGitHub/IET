package org.theiet.rsuite.onix.onix2lmd;

import java.util.Iterator;

import javax.xml.namespace.NamespaceContext;

public class OnixNamespaceContext implements NamespaceContext {

	@Override
	public Iterator getPrefixes(String namespaceURI) {
		return null;
	}

	@Override
	public String getPrefix(String namespaceURI) {
		return null;
	}

	@Override
	public String getNamespaceURI(String prefix) {
		if ("onix".equals(prefix)) {
			return "http://ns.editeur.org/onix/3.0/reference";
		}

		return null;
	}

}
