package com.rsicms.projectshelper.utils;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class ProjectDomUtils {

	private ProjectDomUtils(){
	}
	
	public static Element getNextElement(Node node){
		
		Node sibling = null;
		
		while ((sibling = node.getNextSibling()) != null) {
			if (sibling.getNodeType() == Node.ELEMENT_NODE){
				return (Element)sibling;
			}
			
			node = sibling;
			
		}
		
		return null;
	}
}
