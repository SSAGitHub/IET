package org.theiet.rsuite.onix.domain;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.InputSource;

import com.reallysi.rsuite.api.ManagedObject;
import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.extensions.ExecutionContext;
import com.reallysi.rsuite.service.XmlApiManager;

public class OnixProcessor {

	private List<OnixUnit> onixProcess = new ArrayList<OnixUnit>();

	private OnixRecipientConfiguration recipientConfiguration;

	private ExecutionContext context;

	private OnixDefaultConfiguration defaultConfiguration;

	private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");

	private Log log = LogFactory.getLog(getClass());
	
	public static String ONIX_SCHEMAS_LOCATION = " rsuite:/res/plugin/iet/doctypes/onix/";

	public OnixProcessor(ExecutionContext context,
			OnixRecipientConfiguration recipientConfiguration) {
		this.recipientConfiguration = recipientConfiguration;
		this.context = context;
		this.defaultConfiguration = recipientConfiguration.getDefaultConfiguration();
	}

	public void addOnixToProcess(OnixUnit onixUnit) {
		onixProcess.add(onixUnit);
	}

	public String createOnixFile() throws RSuiteException {

		try {
			XmlApiManager manager = context.getXmlApiManager();

			StringBuilder mergeVersion = filterAndMergeOnixProducts(manager);

			String finalVersion = getStaticContent(manager, mergeVersion);
			
			return finalVersion;
		} catch (Exception ex) {
			throw new RSuiteException(RSuiteException.ERROR_NOT_DEFINED, ex.getMessage(), ex);
		}

	}
	
	private String getStaticContent(XmlApiManager manager,
			StringBuilder mergeVersion) throws URISyntaxException,
			RSuiteException, TransformerException, IOException {
		
		
		String timeStamp = dateFormat.format(new Date());
		String messageNumber = recipientConfiguration.getMessageNumber();
		
		
		URI fillStaticContentXsl = new URI(
				"rsuite:/res/plugin/iet/xslt/onix/ONIX-Merge-Static-Content.xsl");

		Transformer transformer = manager.getTransformer(fillStaticContentXsl);

		String templateUri = recipientConfiguration.getRecipientTemplate();
		transformer.setParameter("ONIX_recipient_template_uri", templateUri);

		transformer.setParameter("ONIX_default_template_uri",
				defaultConfiguration.getDefaultTemplateUri());
		
		transformer.setParameter("timeStamp",
				timeStamp);

		transformer.setParameter("messageNumber", messageNumber
				);
		

		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		Result result = new StreamResult(baos);
		Source input = new SAXSource(new InputSource(
				IOUtils.toInputStream(mergeVersion.toString(),"utf-8")));

		log .debug("Trasnform: " + mergeVersion.toString());
		transformer.transform(input, result);

		String finalVersion = new String(baos.toByteArray(), "utf-8");
		return finalVersion;
	}

	private StringBuilder filterAndMergeOnixProducts(XmlApiManager manager)
			throws URISyntaxException, RSuiteException, TransformerException,
			UnsupportedEncodingException {
		StringBuilder mergeVersion = new StringBuilder();
		int i = 0;
		URI filteringXsl = new URI(
				"rsuite:/res/plugin/iet/xslt/onix/ONIX-Message-Filter.xsl");
		Transformer transformer = manager.getTransformer(filteringXsl);

		if (onixProcess.size() == 0){
			throw new RSuiteException("There are no onix file for the vendor" );
		}
		
		for (OnixUnit onixUnit : onixProcess) {

			ManagedObject onixMO = onixUnit.getOnixMO();

			for (ProductFormat productFormat : getEffectiveProductFormats(
					recipientConfiguration.getProductFormats(),
					onixUnit.getProductFormats())) {

				String filterUri = recipientConfiguration
						.getFilteringConfigurationUri(productFormat.getValue());
				String onixMessageUri = "rsuite:/res/content/" + onixMO.getId();

				transformer.clearParameters();
				transformer.setParameter("productFormat", productFormat.getValue()		);
				transformer.setParameter("ONIX_source_message_uri",
						onixMessageUri);
				transformer.setParameter("ONIX_configuration_uri", filterUri);
				if (i > 0) {
					transformer.setParameter("productOnly", "true");
				}

				String dummyXml = "<xml/>";

				ByteArrayOutputStream baos = new ByteArrayOutputStream();

				Result result = new StreamResult(baos);
				Source input = new SAXSource(new InputSource(
						IOUtils.toInputStream(dummyXml)));

				transformer.transform(input, result);

				String out = new String(baos.toByteArray(), "utf-8");

				if (i == 0) {
					out = out.replace("</ONIXMessage>", "");
				}

				mergeVersion.append(out);

				i++;

			}
			
		}
		
		mergeVersion.append("</ONIXMessage>");

		return mergeVersion;
	}

	private Set<ProductFormat> getEffectiveProductFormats(
			Set<ProductFormat> recipientProductFormats, Set<ProductFormat> bookProductFormats) {

		if (bookProductFormats.isEmpty()) {
			return recipientProductFormats;
		}

		Set<ProductFormat> tempSetA;
		Set<ProductFormat> tempSetB;
		Set<ProductFormat> resultSet = new HashSet<ProductFormat>();

		if (recipientProductFormats.size() <= bookProductFormats.size()) {
			tempSetA = recipientProductFormats;
			tempSetB = bookProductFormats;
		} else {
			tempSetA = bookProductFormats;
			tempSetB = recipientProductFormats;
		}

		for (ProductFormat e : tempSetA) {
			if (tempSetB.contains(e)) {
				resultSet.add(e);
			}
		}

		return resultSet;
	}
	
	public OnixRecipientConfiguration getRecipientConfiguration() {
		return recipientConfiguration;
	}

}
