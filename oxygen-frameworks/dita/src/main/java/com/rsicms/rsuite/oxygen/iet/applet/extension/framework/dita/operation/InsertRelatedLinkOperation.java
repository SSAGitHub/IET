package com.rsicms.rsuite.oxygen.iet.applet.extension.framework.dita.operation;

import ro.sync.ecss.extensions.api.AuthorAccess;
import ro.sync.ecss.extensions.api.AuthorOperationException;
import ro.sync.ecss.extensions.api.node.AuthorNode;

import com.rsicms.rsuite.editors.oxygen.applet.common.api.lookup.ISelectedReferenceNode;
import com.rsicms.rsuite.editors.oxygen.applet.common.references.InsertReferenceElement;
import com.rsicms.rsuite.editors.oxygen.applet.common.references.InsertReferenceHandlerDefault;
import com.rsicms.rsuite.editors.oxygen.applet.common.references.LookupMethod;
import com.rsicms.rsuite.editors.oxygen.applet.extension.operations.InsertReferenceOperation;

public class InsertRelatedLinkOperation extends InsertReferenceOperation {

	private static final String ELEMENT_NAME = "link";
	
	@Override
	protected InsertReferenceElement createInsertReferenceElement(AuthorAccess authorAccess) {
		
		
		InsertReferenceElement relatedLinkElement = new InsertReferenceElement(ELEMENT_NAME, "href");
		relatedLinkElement.setLookup(LookupMethod.BROWSE);
		
		relatedLinkElement.setReferenceHandler(new InsertReferenceHandlerDefault() {
			
			@Override
			public String getXmlFragment(ISelectedReferenceNode selectedNode, String xmlFragment) {				
				xmlFragment = xmlFragment.replace("/>", " class=\"- topic/link \"  />");
				return xmlFragment;
			}
			
			@Override
			public void beforeInsertFragment(AuthorAccess paramAuthorAccess,
					InsertReferenceElement refenceElement,
					ISelectedReferenceNode selectedNode)
					throws AuthorOperationException {
				prepareForRelLinkInsertion(paramAuthorAccess);
			}
		});
		
		relatedLinkElement.setValidateInsertContext(false);
		
		return relatedLinkElement;
	}

	
	
	@Override
	public String getDescription() {
		return "Infert Reletated Link";
	}
	
	  private static void prepareForRelLinkInsertion(AuthorAccess paramAuthorAccess) throws AuthorOperationException
	  {
	    String xpath1 = "boolean(self::*[contains(@class,\" topic/related-links \")\nor contains(@class,\" topic/linklist \")\nor contains(@class,\" topic/linkpool \")\n])";
	    
	      Object[] arrayOfObject = paramAuthorAccess.getDocumentController().evaluateXPath(xpath1, true, true, true);
	      if ((arrayOfObject == null) || (arrayOfObject.length != 1) || (!Boolean.TRUE.equals(arrayOfObject[0])))
	      {
	        String xpath2 = "ancestor-or-self::*[contains(@class,\" topic/topic \")]/*[contains(@class,\" topic/related-links \")]";
	        AuthorNode[] arrayOfAuthorNode = paramAuthorAccess.getDocumentController().findNodesByXPath(xpath2, true, true, true);
	        if ((arrayOfAuthorNode != null) && (arrayOfAuthorNode.length > 0))
	        {
	          paramAuthorAccess.getEditorAccess().setCaretPosition(arrayOfAuthorNode[0].getEndOffset());
	        }
	        else
	        {
	          String xpath3 = "ancestor-or-self::*[contains(@class,\" topic/topic \")]";
	          arrayOfAuthorNode = paramAuthorAccess.getDocumentController().findNodesByXPath(xpath3, true, true, true);
	          if ((arrayOfAuthorNode != null) && (arrayOfAuthorNode.length > 0))
	            paramAuthorAccess.getDocumentController().insertXMLFragment("<related-links/>", arrayOfAuthorNode[0].getEndOffset());
	        }
	      }
	   
	   
	  }
}
