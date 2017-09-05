package org.theiet.rsuite.journals.transforms;

import java.net.URI;
import java.net.URISyntaxException;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;

import com.reallysi.rsuite.api.ManagedObject;
import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.transformation.ManagedObjectTransformer;
import com.reallysi.rsuite.api.transformation.TransformationContext;

public class HTMLPreviewTransformer implements ManagedObjectTransformer {
	
	private ManagedObject mo;
	private TransformationContext context;
	private static Log log = LogFactory.getLog(HTMLPreviewTransformer.class);

	public HTMLPreviewTransformer(TransformationContext context,
			ManagedObject mo) {
		this.mo = mo;
		this.context = context;
	}

	@Override
	public String getMimeType() {
		return "text/html";
	}

	@Override
	public String getName() {
		return null;
	}

	@Override
	public String getSuggestedFileName() {
		return null;
	}

	@Override
	public void transform(StreamSource source, StreamResult result)
			throws RSuiteException {		
		try {
			log.info("enter transform");
			String xsltPath = "rsuite:/res/plugin/iet/xslt/jpub3-preview/jpub3-rsuite-preview-shell.xsl";
			Transformer transformer = context.getXmlApiManager().getTransformer(new URI(xsltPath));
			Document doc = mo.getElement().getOwnerDocument();
			DOMSource moSource = new DOMSource(doc);
			moSource.setSystemId(doc.getDocumentURI());
			transformer.transform(moSource, result);
		} catch (URISyntaxException e) {
			log.error(e.getMessage(), e);
		} catch (TransformerException e) {
			log.error(e.getMessage(), e);
		}		
	}

}
