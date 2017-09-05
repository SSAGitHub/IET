package com.rsicms.projectshelper.export.impl.validation;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class DitaLinkTargetParser implements LinkTargetParser {

	public Set<String> parseTargetValues(Element element) {
		Set<String> targetValues = new HashSet<String>();
		traverseDocumentTree(element, "", targetValues);
		return targetValues;
	}

	private void traverseDocumentTree(Element element, String topicId,
			Set<String> targetValues) {

		NodeList childNodes = element.getChildNodes();
		String elementId = getTargetValue(element);

		if (StringUtils.isNotBlank(elementId) && isTopicElement(element)) {
			topicId = elementId;
			targetValues.add(topicId);
		} else if (StringUtils.isNotBlank(elementId)) {
			String targetValue = topicId + "/" + elementId;
			targetValues.add(targetValue);
		}

		for (int i = 0; i < childNodes.getLength(); i++) {
			Node item = childNodes.item(i);
			if (item instanceof Element) {
				traverseDocumentTree((Element) item, topicId, targetValues);
			}
		}

	}

	protected String getTargetValue(Element element) {
		return element.getAttribute("id");
	}

	private static boolean isTopicElement(Element element) {
		String ditaClass = element.getAttribute("class");

		if (ditaClass != null && ditaClass.contains("topic/topic")) {
			return true;
		}

		return false;
	}

}
