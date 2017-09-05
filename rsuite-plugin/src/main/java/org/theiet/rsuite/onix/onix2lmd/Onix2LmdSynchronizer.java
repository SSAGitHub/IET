package org.theiet.rsuite.onix.onix2lmd;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.transform.sax.SAXSource;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import net.sf.saxon.om.NodeInfo;
import net.sf.saxon.trans.XPathException;
import net.sf.saxon.xpath.XPathEvaluator;

import org.apache.commons.lang.StringUtils;
import org.theiet.rsuite.books.BooksConstans;
import org.theiet.rsuite.onix.OnixConstants;
import org.theiet.rsuite.standards.StandardsBooksConstans;
import org.theiet.rsuite.utils.ExceptionUtils;
import org.xml.sax.InputSource;

import com.reallysi.rsuite.api.ContentAssemblyItem;
import com.reallysi.rsuite.api.ManagedObject;
import com.reallysi.rsuite.api.MetaDataItem;
import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.User;
import com.reallysi.rsuite.api.control.ObjectMetaDataSetOptions;
import com.reallysi.rsuite.api.extensions.ExecutionContext;
import com.reallysi.rsuite.service.ManagedObjectService;
import com.rsicms.projectshelper.utils.ProjectContentAssemblyUtils;

@SuppressWarnings({"unchecked" })
public class Onix2LmdSynchronizer implements OnixConstants {

	private ObjectMetaDataSetOptions metadataOptions;

	private SimpleDateFormat dateFormatter = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:sss");

	private Onix2LmdValueAdjuster metadatValueAdjuster;
	
	public Onix2LmdSynchronizer(Onix2LmdValueAdjuster metadatValueAdjuster) {
		this();
		this.metadatValueAdjuster = metadatValueAdjuster;
	}

	public Onix2LmdSynchronizer() {
		metadataOptions = new ObjectMetaDataSetOptions();
		metadataOptions.setAddNewItem(false);
	}

	public void synchronizeLmdWithOnix(ExecutionContext context,
			ManagedObject onixMo) throws RSuiteException {

		User user = context.getAuthorizationService().getSystemUser();
		ManagedObjectService moSvc = context.getManagedObjectService();
		ManagedObject publicationMo = null;
		boolean succeed = false;

		try {

			publicationMo = getRelatedPublicationMoWithOnix(context, user,
					onixMo);

			if (publicationMo != null) {

				Onix2LmdMapping onix2lmdMapping = Onix2LmdMapping
						.getInstance();

				removeMultivalueLmd(user, moSvc, publicationMo, onix2lmdMapping);

				addLmdFromDocument(context, user, moSvc, onixMo,
						publicationMo.getId(), onix2lmdMapping);

				succeed = true;
			}

		} catch (XPathExpressionException e) {
			ExceptionUtils.throwRsuiteException(e);
		} catch (XPathException e) {
			ExceptionUtils.throwRsuiteException(e);
		} finally {
			setSyncLmdForPublication(user, moSvc, publicationMo, succeed);
		}

	}

	private void setSyncLmdForPublication(User user,
			ManagedObjectService moSvc, ManagedObject publicationMo,
			boolean succeed) throws RSuiteException {

		if (publicationMo == null) {
			return;
		}

		MetaDataItem failMarkLMD = new MetaDataItem(
				LMD_FIELD_ONIX_LAST_SYNC_FAILED, "true");

		if (succeed) {
			failMarkLMD = new MetaDataItem(LMD_FIELD_ONIX_LAST_SYNC_FAILED, "");
		}

		moSvc.addMetaDataEntry(user, publicationMo.getId(), failMarkLMD,
				metadataOptions);

		MetaDataItem syncTimestamp = new MetaDataItem(
				LMD_FIELD_ONIX_LAST_SYNC_TIMESTAMP,
				dateFormatter.format(new Date()));
		moSvc.addMetaDataEntry(user, publicationMo.getId(), syncTimestamp,
				metadataOptions);

	}

	private ManagedObject getRelatedPublicationMoWithOnix(
			ExecutionContext context, User user, ManagedObject onixMo)
			throws RSuiteException {
		ManagedObjectService moSvc = context.getManagedObjectService();

		String publicationId = setPublicationLmdIfPartOfPublication(context,
				onixMo, user, moSvc);

		ManagedObject publicationMo = null;

		if (publicationId != null) {

			publicationMo = moSvc.getManagedObject(user, publicationId);
		}

		return publicationMo;
	}

	private void addLmdFromDocument(ExecutionContext context, User user,
			ManagedObjectService moSvc, ManagedObject onixMo,
			String publicationMoId, Onix2LmdMapping onix2lmdMapping)
			throws XPathExpressionException, RSuiteException, XPathException {

		XPath xpath = createXpathObject(context);

		NodeInfo doc2 = createDocumentObject(onixMo, xpath);

		for (Onix2LmdMappingItem mappingItem : onix2lmdMapping
				.getMappingItems()) {

			List<MetaDataItem> metadataToAdd = getMetadaItem(mappingItem,
					xpath, doc2);

			if (metadatValueAdjuster != null){
				metadataToAdd = metadatValueAdjuster.adjustMetadata(metadataToAdd, publicationMoId);
			}
			
			for (MetaDataItem metadItem : metadataToAdd) {
				moSvc.addMetaDataEntry(user, publicationMoId, metadItem,
						metadataOptions);
			}
		}
	}

	

	private List<MetaDataItem> getMetadaItem(Onix2LmdMappingItem mappingItem,
			XPath xpath, NodeInfo doc2) throws XPathExpressionException {
		List<String> xpathList = mappingItem.getXpaths();
		List<MetaDataItem> metadataToAdd = new ArrayList<MetaDataItem>();

		for (int i = 0; i < xpathList.size(); i++) {
			String mappingXpath = xpathList.get(i);
			XPathExpression xpathExpression = xpath.compile(mappingXpath);
			boolean isLastXpath = i + 1 == xpathList.size();

			QName returnType = XPathConstants.STRING;

			if (mappingItem.isMultivalue()) {
				returnType = XPathConstants.NODESET;
			}

			Object result = xpathExpression.evaluate(doc2, returnType);

			if (result instanceof List) {
				processListResult(mappingItem, metadataToAdd, isLastXpath,
						result);

			} else if (result != null && StringUtils.isNotBlank(result.toString()) || isLastXpath) {
				metadataToAdd
						.add(createMetadata(mappingItem, result.toString()));
			}

			if (!metadataToAdd.isEmpty()) {
				break;
			}
		}

		return metadataToAdd;
	}

	private void processListResult(Onix2LmdMappingItem mappingItem,
			List<MetaDataItem> metadataToAdd, boolean isLastXpath, Object result) {
		List<String> resultList = (List<String>) result;

		if (!resultList.isEmpty() || isLastXpath) {

			for (int z = 0; z < resultList.size(); z++) {

				String resultItem = resultList.get(z);

				if (StringUtils.isBlank(resultItem)) {
					continue;
				}

				metadataToAdd.add(createMetadata(mappingItem, resultItem));
			}

		}
	}

	private MetaDataItem createMetadata(Onix2LmdMappingItem mappingItem,
			String resultItem) {
		

		if (resultItem != null && resultItem.startsWith("${") && resultItem.endsWith("}")){
			resultItem = "";
		}
			
		MetaDataItem metadata = new MetaDataItem(mappingItem.getLmdName(),
				normalizeValue(resultItem));
		return metadata;
	}

	private String normalizeValue(String value) {
		return value.replaceAll("\\s+", " ");
	}

	private NodeInfo createDocumentObject(ManagedObject mo, XPath xpath)
			throws RSuiteException, XPathException {
		InputSource is = new InputSource(mo.getInputStream());
		SAXSource ss = new SAXSource(is);

		return ((XPathEvaluator) xpath).setSource(ss);
	}

	private XPath createXpathObject(ExecutionContext context) {
		XPathFactory factory = context.getXmlApiManager().getXPathFactory();
		XPath xpath = factory.newXPath();

		xpath.setNamespaceContext(createNamespaceContext());
		return xpath;
	}

	public OnixNamespaceContext createNamespaceContext() {
		return new OnixNamespaceContext();
	}

	private void removeMultivalueLmd(User user, ManagedObjectService moSvc,
			ManagedObject publicationMo, Onix2LmdMapping onix2lmdMapping)
			throws RSuiteException {
		List<MetaDataItem> metadataToRemove = new ArrayList<MetaDataItem>();

		for (MetaDataItem metadata : publicationMo.getMetaDataItems()) {
			if (onix2lmdMapping.getMultivalueItems().contains(
					metadata.getName())) {
				metadataToRemove.add(metadata);
			}
		}

		for (MetaDataItem metadata : metadataToRemove) {
			moSvc.removeMetaDataEntry(user, publicationMo.getId(), metadata);
		}
	}

	private String setPublicationLmdIfPartOfPublication(
			ExecutionContext context, ManagedObject mo, User user,
			ManagedObjectService moSvc) throws RSuiteException {
		String publicationId = mo
				.getLayeredMetadataValue(LMD_FIELD_ONIX_PUBLICATION_RSUITE_ID);

		if (publicationId != null) {
			return publicationId;
		}

		ContentAssemblyItem publicationCa = getRelatedPublication(context, mo);

		if (publicationCa != null) {
			publicationId = publicationCa.getId();
			moSvc.setMetaDataEntry(user, mo.getId(), new MetaDataItem(
					LMD_FIELD_ONIX_PUBLICATION_RSUITE_ID, publicationId));
		}

		return publicationId;
	}

	private ContentAssemblyItem getRelatedPublication(ExecutionContext context,
			ManagedObject mo) throws RSuiteException {
		Set<String> booksCaTypes = new HashSet<String>();
		booksCaTypes.add(BooksConstans.CA_TYPE_BOOK);
		booksCaTypes.add(StandardsBooksConstans.CA_TYPE_STANDARDS_BOOK_EDITION);

		return ProjectContentAssemblyUtils.getAncestorCAbyTypes(context, mo.getId(),
				booksCaTypes);
	}

}
