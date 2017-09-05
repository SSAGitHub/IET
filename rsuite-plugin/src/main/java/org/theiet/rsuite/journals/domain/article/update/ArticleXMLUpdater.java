package org.theiet.rsuite.journals.domain.article.update;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Calendar;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.*;

import org.apache.commons.lang.StringUtils;
import org.theiet.rsuite.domain.date.IetDate;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import com.reallysi.rsuite.api.*;
import com.reallysi.rsuite.api.control.ObjectUpdateOptions;
import com.reallysi.rsuite.api.control.XmlObjectSource;
import com.reallysi.rsuite.api.extensions.ExecutionContext;
import com.reallysi.rsuite.service.ManagedObjectService;
import com.reallysi.rsuite.service.XmlApiManager;

public class ArticleXMLUpdater {

	private ManagedObject articleXMLMO;
	
	private ExecutionContext context;
	
	private User user;

	public ArticleXMLUpdater(ExecutionContext context, User user, ManagedObject articleXMLMO) {
		super();
		this.articleXMLMO = articleXMLMO;
		this.context = context;
		this.user = user;
	}

	public void addPublishDates(String prePublishDate, Calendar onlinePublishDate) throws RSuiteException{
		XmlApiManager xmlManager = context.getXmlApiManager();
			
			try {
				XPathFactory xpathFactory = xmlManager.getXPathFactory();
				XPath xpath = xpathFactory.newXPath();
				
				Element root = articleXMLMO.getElement();
				
				boolean  modified = addEPubPublishDate(xpath, root, onlinePublishDate);
				modified |=  addPrePublishDate(xpath, root, prePublishDate);
				
				if (modified){
					updateMo(root, "Added publish dates");
				}
				
			} catch (XPathExpressionException | RSuiteException e) {
				throw new RSuiteException(0, "Unable to add epub date for " + articleXMLMO.getId(), e);
			}
	}


	private boolean addPrePublishDate(XPath xpath, Element root, String prePublishDate) throws RSuiteException, XPathExpressionException {
		String dateType = "epreprint";
		if (StringUtils.isNotBlank(prePublishDate) && pubDateElementNotExist(xpath, root, dateType)){
			Calendar date = IetDate.parseDate(prePublishDate);
			Element prePublishElement =  createPublishDateElement(root, date, dateType);
			addPubDateElement(root, prePublishElement, xpath);
			return true;
		}
		
		return false;
	}
	
	private boolean addEPubPublishDate(XPath xpath, Element root, Calendar epubDate) throws XPathExpressionException, RSuiteException{
		String dateType = "epub";
		if (pubDateElementNotExist(xpath, root, dateType)){
			Element publishDateElement =  createPublishDateElement(root, epubDate, dateType);
			addPubDateElement(root, publishDateElement, xpath);
			return true;
		}
		
		return false;
	}

	private void addPubDateElement(Element articleElement, Element pubDateElement, XPath xpath) throws XPathExpressionException, RSuiteException {
		Document document = articleElement.getOwnerDocument();
		
		
		Node firstPubDateElement = getNodeFromXPath(xpath, articleElement, "/*/front/article-meta/pub-date[1]");
		
		if (firstPubDateElement == null){
			throw new RSuiteException("Unable to localize the 'pub-date' element ");
		}
		
		
		Node epubToAdd = document.importNode(pubDateElement, true);
		Node articleMeta = firstPubDateElement.getParentNode();
		
		articleMeta.insertBefore(epubToAdd, firstPubDateElement);
	}


	private Element createPublishDateElement(Element root,
			Calendar onlinePublishDate, String dateType) throws RSuiteException{
		
		StringBuilder epubElementXML = new StringBuilder();
		epubElementXML.append("<pub-date pub-type='" + dateType +"'>");
		epubElementXML.append(createValueElement("day",  onlinePublishDate.get(Calendar.DAY_OF_MONTH)));
		epubElementXML.append(createValueElement("month",  onlinePublishDate.get(Calendar.MONTH) + 1));
		epubElementXML.append(createValueElement("year",  onlinePublishDate.get(Calendar.YEAR)));
		epubElementXML.append("</pub-date>");
		
		return convertXMLStringToNode(context, epubElementXML);
	}


	private Element convertXMLStringToNode(ExecutionContext context, StringBuilder epubElementXML
			) throws RSuiteException {
		
		XmlApiManager xmlManager = context.getXmlApiManager();
		DocumentBuilderFactory documentBuilderFactory = xmlManager.getDocumentBuilderFactory();
		
		try{
			Element node =  documentBuilderFactory
				    .newDocumentBuilder()
				    .parse(new ByteArrayInputStream(epubElementXML.toString().getBytes()))
				    .getDocumentElement();
			return node;	
		}catch (SAXException | IOException | ParserConfigurationException e){
			throw new RSuiteException(0, "Unable to create node for " + epubElementXML.toString(),e );
		}
		
		
	}
	
	private String createValueElement(String elementName, int value){
		return createValueElement(elementName, String.valueOf(value));
	}
	
	private String createValueElement(String elementName, String value){
		StringBuilder elementXml = new StringBuilder("<");
		elementXml.append(elementName).append(">");
		elementXml.append(value);
		elementXml.append("</").append(elementName).append(">");
		return elementXml.toString(); 
	}


	private boolean pubDateElementNotExist(XPath xpath, Element root, String dateType)
			throws XPathExpressionException {
		Node epuDateElement = getNodeFromXPath(xpath, root, "/*/front/article-meta/pub-date[@pub-type='" + dateType + "']");
		
		return epuDateElement == null ? true : false;
	}


	private Node getNodeFromXPath(XPath xpath, Element root, String expression)
			throws XPathExpressionException {
		XPathExpression epubExpression = xpath
				.compile(expression);

		return (Node)epubExpression.evaluate(root, XPathConstants.NODE);
	}
	
	private void updateMo(Element newMoRoot, String comment) throws RSuiteException{
			ManagedObjectService moService = context.getManagedObjectService();
		 moService.checkOut(user, articleXMLMO.getId());
		 moService.update(user, articleXMLMO.getId(), new XmlObjectSource(newMoRoot), new ObjectUpdateOptions());
		 moService.checkIn(user, articleXMLMO.getId(), VersionType.MINOR, comment, false);
	}
	
}
