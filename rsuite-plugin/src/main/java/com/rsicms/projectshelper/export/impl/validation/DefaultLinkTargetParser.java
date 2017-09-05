package com.rsicms.projectshelper.export.impl.validation;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class DefaultLinkTargetParser implements LinkTargetParser {

	/* (non-Javadoc)
	 * @see com.rsicms.projectshelper.export.impl.validation.LinkTargetParser#parseTargetValues(org.w3c.dom.Element)
	 */
	@Override
	public Set<String> parseTargetValues(Element element) {
		Set<String> targetValues = new HashSet<String>();
		traverseDocumentTree(element, targetValues);
		return targetValues;
	}

	private void traverseDocumentTree(Element element,
			Set<String> targetValues) {

		NodeList childNodes = element.getChildNodes();
		String elementId = getTargetValue(element);

		if (StringUtils.isNotBlank(elementId)) {
			String targetValue = elementId;
			targetValues.add(targetValue);
		}

		for (int i = 0; i < childNodes.getLength(); i++) {
			Node item = childNodes.item(i);
			if (item instanceof Element) {
				traverseDocumentTree((Element) item, targetValues);
			}
		}

	}

	protected String getTargetValue(Element element) {
		return element.getAttribute("id");
	}

}
