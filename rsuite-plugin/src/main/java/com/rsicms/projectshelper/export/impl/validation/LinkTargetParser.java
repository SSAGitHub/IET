package com.rsicms.projectshelper.export.impl.validation;

import java.util.Set;

import org.w3c.dom.Element;

public interface LinkTargetParser {

	public abstract Set<String> parseTargetValues(Element element);

}