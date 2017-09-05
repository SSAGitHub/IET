package org.theiet.rsuite.books.domain;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.theiet.rsuite.books.BooksConstans;
import org.theiet.rsuite.datamodel.IetBookPublication;
import org.theiet.rsuite.domain.mail.IetMailUtils;
import org.theiet.rsuite.standards.StandardsBooksConstans;
import org.theiet.rsuite.utils.ExceptionUtils;
import org.theiet.rsuite.utils.StaxUtils;
import org.theiet.rsuite.utils.StringUtils;

import com.reallysi.rsuite.api.ConfigurationProperties;
import com.reallysi.rsuite.api.ContentAssembly;
import com.reallysi.rsuite.api.MetaDataItem;
import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.User;
import com.reallysi.rsuite.api.extensions.ExecutionContext;
import com.reallysi.rsuite.service.XmlApiManager;
import com.rsicms.projectshelper.net.mail.MailMessage;
import com.rsicms.projectshelper.net.mail.MailUtils;
import com.rsicms.projectshelper.utils.ProjectUserUtils;

public class BookMetadaSender implements StandardsBooksConstans {

	private static final int SIX_MONTHS = 6;

	private Log log = LogFactory.getLog(BookMetadaSender.class);

	private static Map<String, String> LMD_LABELS_MAP = new HashMap<String, String>();

	private static final String EMAIL_VAR_AUTHOR_FIRST_NAME = LMD_FIELD_AUTHOR_FIRST_NAME;
	private static final String EMAIL_VAR_BOOK_TITLE = LMD_FIELD_BOOK_TITLE;
	private static final String EMAIL_VAR_MAIL_FROM = "mail_from";
	private static final String EMAIL_VAR_AUTHOR_SURNAME = LMD_FIELD_AUTHOR_SURNAME;
	private static final String EMAIL_VAR_BOOK_PRODUCT_CODE = LMD_FIELD_BOOK_PRODUCT_CODE;

	private static final String XSLT_URI = "rsuite:/res/plugin/iet/xslt/books/SendBookMetadata.xsl";

	private static final DateTimeFormatter DATE_TIME_FORMATTER = ISODateTimeFormat
			.date();

	private static final SimpleDateFormat REPORT_DATE_STRING_FORMAT = new SimpleDateFormat(
			"yyyy-MM-dd");

	private static String[] XML_LMD_FIELDS = { LMD_FIELD_BOOK_TITLE,
			LMD_FIELD_BOOK_PRODUCT_CODE, "e_product_code", "work_order",
			"price_pound", "price_dollar", "isbn", "e_isbn",
			LMD_FIELD_CONTRACTED_TS_DELIVERY_DATE, "target_pub_date", "format",
			"extent", "initial_reorder_level", "replacement", };

	static {
		initializeLmdLabelsMap();
	}

	public void sendBookMetadata(ExecutionContext context,
			IetBookPublication publication, String additionalText)
			throws RSuiteException {
		String emailTo = getEmailTo(context, publication.getBookPublicationCa());

		Map<String, String> lmdMap = getLmdFromPublicationCa(context,
				publication.getBookPublicationCa());

		byte[] lmdXml = convertLmdMapToXml(lmdMap);

		byte[] lmdHtml = transformLmdXmlToHtml(context, lmdMap, lmdXml);

		sendEmailWithMetadata(context, emailTo, lmdMap, lmdHtml, additionalText);

		setSentFlagForPublication(context, publication.getRsuiteId());
	}

	private void setSentFlagForPublication(ExecutionContext context,
			String publicationCaId) throws RSuiteException {

		User user = context.getAuthorizationService().getSystemUser();

		List<MetaDataItem> metadataList = new ArrayList<MetaDataItem>();

		metadataList.add(new MetaDataItem(LMD_FIELD_BOOK_METADATA_SENT,
				LMD_VALUE_YES));
		context.getManagedObjectService().setMetaDataEntries(user,
				publicationCaId, metadataList);
	}

	private String getEmailTo(ExecutionContext context, ContentAssembly publicationCA) throws RSuiteException {
		ConfigurationProperties configurationProperties = context
				.getConfigurationProperties();
		
		String bookType = publicationCA.getType();
		
		String metadataUserId = configurationProperties.getProperty(
				PROP_BOOKS_METADATA_DISTRIBUTION_USER,
				USER_METADATA_DISTRIBUTION_DEFAULT);
		
		if (CA_TYPE_STANDARDS_BOOK_EDITION.equalsIgnoreCase(bookType)){
			metadataUserId = configurationProperties.getProperty(
					PROP_STANDARDS_BOOKS_METADATA_DISTRIBUTION_USER,
					USER_METADATA_DISTRIBUTION_STANDARDS_DEFAULT);
		}
		
		
		String emailTo = ProjectUserUtils.getUserEmail(context, metadataUserId);
		log.info("ExtractAndSendBookMetadata: Send metadata to " + emailTo);

		if (StringUtils.isBlank(emailTo)) {
			throw new RSuiteException("Email must not be blank");
		}
		return emailTo;
	}

	private void sendEmailWithMetadata(ExecutionContext context,
			String emailTo, Map<String, String> lmdMap, byte[] lmdHtml,
			String additionalText) throws RSuiteException {

		String mailFrom = IetMailUtils.obtainEmailFrom();
		Map<String, String> eMailVars = new HashMap<String, String>();
		eMailVars.put(EMAIL_VAR_AUTHOR_SURNAME,
				getLmdValueFromMap(LMD_FIELD_AUTHOR_SURNAME, lmdMap));
		eMailVars.put(EMAIL_VAR_AUTHOR_FIRST_NAME,
				getLmdValueFromMap(LMD_FIELD_AUTHOR_FIRST_NAME, lmdMap));
		eMailVars.put(EMAIL_VAR_BOOK_PRODUCT_CODE,
				getLmdValueFromMap(LMD_FIELD_BOOK_PRODUCT_CODE, lmdMap));
		eMailVars.put(EMAIL_VAR_BOOK_TITLE,
				getLmdValueFromMap(LMD_FIELD_BOOK_TITLE, lmdMap));
		eMailVars.put(EMAIL_VAR_MAIL_FROM, mailFrom);

		eMailVars.put(EMAIL_VAR_ADDITIONAL_TEXT, additionalText);

		String mailSubject = MailUtils.obtainEmailSubject(context,
				PROP_BOOKS_SEND_METADATA_MAIL_TITLE);
		mailSubject = mailSubject.replace("${product_code}",
				getLmdValueFromMap(LMD_FIELD_BOOK_PRODUCT_CODE, lmdMap));
		MailMessage message = new MailMessage(mailFrom, emailTo,
				mailSubject);
		message.setMessageTemplateProperty(PROP_BOOKS_SEND_METADATA_MAIL_BODY);
		message.setVariables(eMailVars);
		message.addAttachment(
				lmdMap.get(BooksConstans.LMD_FIELD_BOOK_PRODUCT_CODE)
						+ ".html", lmdHtml);
		MailUtils.sentEmail(context, message);
	}

	private byte[] transformLmdXmlToHtml(ExecutionContext context,
			Map<String, String> lmdMap, byte[] xmlBytes)
			throws RSuiteException {

		byte[] htmlBytes = null;

		try {

			String date = REPORT_DATE_STRING_FORMAT.format(Calendar
					.getInstance().getTime());
			XmlApiManager xmlApiMgr = context.getXmlApiManager();
			String productCode = lmdMap
					.get(BooksConstans.LMD_FIELD_BOOK_PRODUCT_CODE);
			Transformer trans = xmlApiMgr.getTransformer(new URI(XSLT_URI));
			trans.setParameter("title", productCode);
			trans.setParameter("date", date);
			ByteArrayInputStream xmlByteInputStream = new ByteArrayInputStream(
					xmlBytes);
			StreamSource xmlSource = new StreamSource(xmlByteInputStream);
			ByteArrayOutputStream htmlOutputStream = new ByteArrayOutputStream();

			StreamResult htmlResult = new StreamResult(htmlOutputStream);
			trans.transform(xmlSource, htmlResult);
			htmlBytes = htmlOutputStream.toByteArray();

		} catch (TransformerException e) {
			ExceptionUtils.throwRsuiteException(e);
		} catch (URISyntaxException e) {
			ExceptionUtils.throwRsuiteException(e);
		}

		return htmlBytes;
	}

	private byte[] convertLmdMapToXml(Map<String, String> lmdMap)
			throws RSuiteException {

		byte[] xmlBytes = null;
		try {
			ByteArrayOutputStream xmlOutStream = new ByteArrayOutputStream();
			OutputStreamWriter xmlOutStreamWriter = new OutputStreamWriter(
					xmlOutStream);
			XMLStreamWriter xmlWriter = StaxUtils
					.setUpWriter(xmlOutStreamWriter);
			xmlWriter.writeStartDocument("UTF-8", "1.0");
			xmlWriter.writeStartElement("meta-data");
			for (String name : XML_LMD_FIELDS) {
				String value = getLmdValueFromMap(name, lmdMap);
				xmlWriter.writeStartElement("field");
				xmlWriter.writeAttribute("name", LMD_LABELS_MAP.get(name));
				if (name.equals("price_dollar")){
					xmlWriter.writeAttribute("currency", "dollar");
				}
					
				if (name.equals("price_pound")){
					xmlWriter.writeAttribute("currency", "pound");
				}
					
				xmlWriter.writeCharacters(value);
				xmlWriter.writeEndElement();
			}
			xmlWriter.writeEndElement();
			xmlWriter.writeEndDocument();
			xmlOutStreamWriter.close();
			xmlBytes = xmlOutStream.toByteArray();

		} catch (FactoryConfigurationError e) {
			ExceptionUtils.throwRsuiteException(e);
		} catch (XMLStreamException e) {
			ExceptionUtils.throwRsuiteException(e);

		} catch (IOException e) {
			ExceptionUtils.throwRsuiteException(e);
		}

		return xmlBytes;
	}

	private Map<String, String> getLmdFromPublicationCa(
			ExecutionContext context, ContentAssembly ca) throws RSuiteException {

		List<MetaDataItem> lmdList = ca.getMetaDataItems();
		HashMap<String, String> lmdMap = new HashMap<String, String>();
		for (MetaDataItem item : lmdList) {
			String name = item.getName();
			String value = item.getValue();

			if (LMD_FIELD_CONTRACTED_TS_DELIVERY_DATE.equalsIgnoreCase(name)) {
				addTargetPublicationDate(lmdMap, value);
			}

			lmdMap.put(name, value);
		}
		return lmdMap;
	}

	private void addTargetPublicationDate(Map<String, String> lmdMap,
			String contractedDeliveryDate) throws RSuiteException {

		String targetPuDate = "Not available";

		try {
			if (!StringUtils.isBlank(contractedDeliveryDate)) {
				DateTime tsDateTime = DATE_TIME_FORMATTER
						.parseDateTime(contractedDeliveryDate);
				DateTime targetPubDate = tsDateTime.plusMonths(SIX_MONTHS);
				targetPuDate = StringUtils
						.getUKDateFromDateTime(targetPubDate);
			}

			lmdMap.put("target_pub_date", targetPuDate);
		} catch (IllegalArgumentException e) {
			ExceptionUtils.throwRsuiteException(
					"Unable to parse contracted_ts_delivery_date "
							+ contractedDeliveryDate, e);
		}
	}

	private String getLmdValueFromMap(String name, Map<String, String> lmdMap) {
		return lmdMap.containsKey(name) ? lmdMap.get(name) : " ";
	}

	private static void initializeLmdLabelsMap() {
		LMD_LABELS_MAP.put(EMAIL_VAR_BOOK_TITLE, "Book Title");
		LMD_LABELS_MAP.put(LMD_FIELD_BOOK_PRODUCT_CODE, "Product Code");
		LMD_LABELS_MAP.put("e_product_code", "eProduct Code");
		LMD_LABELS_MAP.put("work_order", "Work Order");
		LMD_LABELS_MAP.put("price_pound", "Price in Pounds");
		LMD_LABELS_MAP.put("price_dollar", "Price in Dollars");
		LMD_LABELS_MAP.put("isbn", "ISBN");
		LMD_LABELS_MAP.put("e_isbn", "eISBN");
		LMD_LABELS_MAP.put(LMD_FIELD_CONTRACTED_TS_DELIVERY_DATE,
				"Contracted TS Delivery Date");
		LMD_LABELS_MAP.put("target_pub_date", "Target Pub Date");
		LMD_LABELS_MAP.put("format", "Format");
		LMD_LABELS_MAP.put("extent", "Extent");
		LMD_LABELS_MAP.put("initial_reorder_level", "Initial Reorder Level");
		LMD_LABELS_MAP.put("replacement", "Replacement");
	}
}
