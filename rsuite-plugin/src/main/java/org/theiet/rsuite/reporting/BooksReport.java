package org.theiet.rsuite.reporting;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.theiet.rsuite.IetConstants;
import org.theiet.rsuite.books.BooksConstans;
import org.theiet.rsuite.utils.StaxUtils;
import org.theiet.rsuite.utils.WorkflowUtils;
import org.theiet.rsuite.utils.XpathUtils;

import com.reallysi.rsuite.api.ContentAssembly;
import com.reallysi.rsuite.api.ContentAssemblyItem;
import com.reallysi.rsuite.api.ContentAssemblyReference;
import com.reallysi.rsuite.api.ManagedObject;
import com.reallysi.rsuite.api.MetaDataItem;
import com.reallysi.rsuite.api.ObjectType;
import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.User;
import com.reallysi.rsuite.api.extensions.ExecutionContext;
import com.reallysi.rsuite.api.pubtrack.Process;
import com.reallysi.rsuite.api.pubtrack.ProcessMetaDataItem;
import com.reallysi.rsuite.api.remoteapi.CallArgumentList;
import com.reallysi.rsuite.api.report.DefaultReportGenerator;
import com.reallysi.rsuite.api.report.HTMLReport;
import com.reallysi.rsuite.api.report.Report;
import com.reallysi.rsuite.api.report.ReportGenerationContext;
import com.reallysi.rsuite.service.ContentAssemblyService;
import com.reallysi.rsuite.service.PubtrackManager;
import com.reallysi.rsuite.service.SearchService;
import com.reallysi.rsuite.service.XmlApiManager;
import com.rsicms.projectshelper.utils.ProjectContentAssemblyUtils;

public class BooksReport extends DefaultReportGenerator implements
		BooksConstans {

	
	private static final String WORKFLOW_START_DATE = "workflow_start_date";

	private static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat(
			"yyyy-MM-dd");

	private Log log = LogFactory.getLog(this.getClass());

	private final static String[] LMD_FIELDS = {
			"product_code",
			"book_title",
			"author", // computed
			"isbn", "e_isbn", "e_product_code", "extent", "trim_size",
			"format", "price_pound", "price_dollar", "initial_print_run",
			"work_order",
			"commissioning_editor",
			"book_series_name",
			"status", // computed
			"stage_due_complete", "proposal_presentation_date",
			"contract_signed_date",
			"contracted_ts_delivery_date",
			LMD_FIELD_REFORECAST_PUB_DATE, "actual_ts_delivery_date",
			LMD_FIELD_ACTUAL_PUB_DATE, 
			"date_created", // computed
			WORKFLOW_START_DATE, // computed
			"pub_date_catalogue" };

	private static final String XSLT_URI = "rsuite:/res/plugin/iet/xslt/books/BooksReport.xsl";

	@Override
	public Report generateReport(ReportGenerationContext context,
			String reportId, CallArgumentList args) throws RSuiteException {

		User user = context.getSession().getUser();

		ContentAssemblyService caSvc = context.getContentAssemblyService();
		XmlApiManager xmlApiMgr = context.getXmlApiManager();
		try {

			ByteArrayOutputStream xmlOutStream = new ByteArrayOutputStream();
			OutputStreamWriter xmlStreamWriter = new OutputStreamWriter(
					xmlOutStream, "utf-8");

			XMLStreamWriter xmlWriter = StaxUtils.setUpWriter(xmlStreamWriter);
			xmlWriter.writeStartDocument("UTF-8", "1.0");
			xmlWriter.writeStartElement("books");

			String startDate = args.getFirstValue("startDate");
			String endDate = args.getFirstValue("endDate");

			if (!StringUtils.isBlank(startDate)) {
				xmlWriter.writeAttribute("startDate",
						IetConstants.UK_DATE_FORMAT_SHORT.format(DATE_FORMATTER
								.parse(startDate)));
			}
			if (!StringUtils.isBlank(endDate)) {
				xmlWriter.writeAttribute("endDate",
						IetConstants.UK_DATE_FORMAT_SHORT.format(DATE_FORMATTER
								.parse(endDate)));
			}

			List<Process> booksProcessList = getBooksProcessList(context, user);

			Set<String> reletedRSuiteIdsWithActiveWorkflow = getBooksIdsInActiveWorkflow();

			for (Process process : booksProcessList) {
				Map<String, String> processInfoMap = collectMetadaFromProcess(process);

				// Get book ca
				String bookCaId = processInfoMap.get("OBJECT_ID");

				if (StringUtils.isBlank(bookCaId)) {
					throw new RSuiteException(
							"No OBJECT_ID for pubtrack process "
									+ process.getExternalId() + " ("
									+ process.getId() + ")");
				}

				ContentAssembly bookCa = caSvc.getContentAssembly(user,
						bookCaId);

				if (bookCa != null) {

					processBookCa(context, xmlWriter, bookCa, processInfoMap,
							reletedRSuiteIdsWithActiveWorkflow);
				}

			}

			xmlWriter.writeEndElement(); // close books
			xmlWriter.writeEndDocument();
			xmlStreamWriter.flush();
			xmlStreamWriter.close();
			byte[] xmlSourceBytes = xmlOutStream.toByteArray();

			return transformReportXML2HTMLReport(context, xmlApiMgr,
					xmlSourceBytes);

		} catch (Exception e) {
			return handleException(log, e);
		}
	}

	private Set<String> getBooksIdsInActiveWorkflow() throws RSuiteException {
		return WorkflowUtils.getRelatedRSuiteIdsWithActiveWorkflow(WF_NAME_IET_PREPARE_BOOK);
	}

	@SuppressWarnings("unchecked")
	private List<Process> getBooksProcessList(ExecutionContext context,
			User user) throws RSuiteException {

		PubtrackManager pubtrackManager = context.getPubtrackManager();

		String HQL = getHQL(context, user, pubtrackManager);
		log.info("generateReport: HQL is\n" + HQL);
		List<Process> booksProcessList = pubtrackManager.query(user, HQL);
		return booksProcessList;
	}

	private Report transformReportXML2HTMLReport(
			ReportGenerationContext context, XmlApiManager xmlApiMgr,
			byte[] xmlSourceBytes) throws URISyntaxException, RSuiteException,
			TransformerException {
		ByteArrayInputStream bais = new ByteArrayInputStream(xmlSourceBytes);
		StreamSource xmlSource = new StreamSource(bais);
		StringWriter stringWriter = new StringWriter();
		StreamResult result = new StreamResult(stringWriter);
		String skey = context.getSession().getKey();
		java.net.URI xsltUri = new java.net.URI(XSLT_URI);
		Transformer trans = xmlApiMgr.getTransformer(xsltUri);
		trans.setParameter("skey", skey);
		trans.transform(xmlSource, result);

		return new HTMLReport(stringWriter.toString());
	}

	private void processBookCa(ExecutionContext context,
			XMLStreamWriter xmlWriter, ContentAssembly bookCa,
			Map<String, String> processInfoMap,
			Set<String> reletedRSuiteIdsWithActiveWorkflow)
			throws XMLStreamException, RSuiteException, ParseException {

		String product_id = processInfoMap.get("PRODUCT_ID");

		addCaLmdToProcessInfoMap(bookCa, processInfoMap);

		String date_created = DATE_FORMATTER.format(bookCa
				.getDtCreated());

		processInfoMap.put("date_created", date_created);

		String stageDueComplete = getNormalizedValue(processInfoMap,
				"stage_due_complete");

		String rowFormat = getRowFormat(context, bookCa, processInfoMap,
				reletedRSuiteIdsWithActiveWorkflow);
		
		// Apply business rules for specific cases
		String author_surname = getNormalizedValue(processInfoMap,
				"author_surname");
		String author_first_name = getNormalizedValue(processInfoMap,
				"author_first_name");
		processInfoMap.put("author", author_surname + ", "
				+ author_first_name);

		String status = getBookStatus(context, bookCa, processInfoMap, reletedRSuiteIdsWithActiveWorkflow);
		processInfoMap.put("status", status);
		
		xmlWriter.writeStartElement("book");
		xmlWriter.writeAttribute("product_id", product_id);

		xmlWriter.writeAttribute("stage_due_complete", stageDueComplete);
		xmlWriter.writeAttribute("row_format", rowFormat);

		serializeProcessInfoMap(xmlWriter, processInfoMap);

		xmlWriter.writeEndElement(); // close book

	}

	private void serializeProcessInfoMap(XMLStreamWriter xmlWriter,
			Map<String, String> processInfoMap) throws XMLStreamException {
		for (int i = 1; i < LMD_FIELDS.length; i++) { // Skip LMD_FIELDS[0]
			xmlWriter.writeStartElement("meta-item");
			xmlWriter.writeAttribute("item", LMD_FIELDS[i]);
			if (LMD_FIELDS[i].equals("price_dollar")) {
				xmlWriter.writeAttribute("currency", "dollar");
			}
			if (LMD_FIELDS[i].equals("price_pound")) {
				xmlWriter.writeAttribute("currency", "pound");
			}
			xmlWriter.writeCharacters(getNormalizedValue(processInfoMap,
					LMD_FIELDS[i]));
			xmlWriter.writeEndElement();
		}
	}

	private void addCaLmdToProcessInfoMap(ContentAssembly bookCa,
			Map<String, String> processInforMap) throws RSuiteException {
		// Add all LMD to the hash
		List<MetaDataItem> lmdList = bookCa.getMetaDataItems();
		for (MetaDataItem lmdItem : lmdList) {
			String name = lmdItem.getName();
			String value = lmdItem.getValue();
			processInforMap.put(name, value);
		}
	}

	private Map<String, String> collectMetadaFromProcess(Process process)
			throws ParseException {
		Map<String, String> pubtrackMetadata = new HashMap<String, String>();

		// Store all pubtrack metadata items in a hash
		Set<ProcessMetaDataItem> metaSet = process.getMetaData();

		for (ProcessMetaDataItem pubTrackItem : metaSet) {
			String name = pubTrackItem.getName();
			String value = pubTrackItem.getValue();

			pubtrackMetadata.put(name, value);
		}

		String date_workflow_started = DATE_FORMATTER
				.format(process.getDtStarted());
		pubtrackMetadata.put(WORKFLOW_START_DATE, date_workflow_started);

		String contracted_ts_delivery_date = pubtrackMetadata
				.get("contracted_ts_delivery_date");

		if (StringUtils.isBlank(contracted_ts_delivery_date)) {
			pubtrackMetadata.put("contracted_ts_delivery_date", " ");
		}

		return pubtrackMetadata;
	}

	private String getHQL(ExecutionContext context, User user,
			PubtrackManager ptMgr) throws RSuiteException {

		ContentAssemblyService caSvc = context.getContentAssemblyService();

		List<ManagedObject> bookContainersMO = getBooksContainers(context,
				user, log);

		StringBuffer bookIdList = new StringBuffer();
		for (ManagedObject bookContainerMO : bookContainersMO) {
			ContentAssembly booksCa = caSvc.getContentAssembly(user,
					bookContainerMO.getId());
			List<? extends ContentAssemblyItem> bookCaItems = booksCa
					.getChildrenObjects();
			for (ContentAssemblyItem bookCaItem : bookCaItems) {
				bookIdList.append((bookIdList.length() > 0 ? ", " : "") + "'"
						+ ((ContentAssemblyReference) bookCaItem).getTargetId()
						+ "'");
			}
		}

		StringBuilder hqlBuffer = new StringBuilder(
				"from Process p where p.name='IET_BOOK'");
		hqlBuffer.append(" and p.metaData.name='OBJECT_ID'");
		hqlBuffer.append(" and p.metaData.value in (" + bookIdList.toString()
				+ ")");

		return hqlBuffer.toString();
	}

	private List<ManagedObject> getBooksContainers(ExecutionContext context,
			User user, Log log) throws RSuiteException {
		SearchService srchSvc = context.getSearchService();
		String booksCaQuery = "/rs_ca_map/rs_ca[rmd:get-type(.) = '"
				+ CA_TYPE_BOOKS_CONTENT_ASSEMBLY
				+ "' and (rmd:get-display-name(.) = '"
				+ CONTAINER_BOOK_IN_PRODUCTION
				+ "' or rmd:get-display-name(.) = '"
				+ CONTAINER_BOOK_CONTRACTED + "')]";
		booksCaQuery = XpathUtils.resolveRSuiteFunctionsInXPath(booksCaQuery);
		log.info("adjustFormInstance: query for available templates\n"
				+ booksCaQuery);

		return srchSvc.executeXPathSearch(user, booksCaQuery, 0, 2);
	}
	
	private String getBookStatus (ExecutionContext context,
			ContentAssembly bookCa, Map<String, String> processInfoMap,
			Set<String> relatedRSuiteIdsWithActiveWorkflow) throws RSuiteException {
		String status = "";
		
		boolean hasManuscript = isManuscriptLoaded(context, bookCa);
		
		boolean hasNoActiveWorkflow = !relatedRSuiteIdsWithActiveWorkflow.contains(bookCa.getId());
		
		boolean isPublished = !getNormalizedValue(processInfoMap,
				"print_published_date").trim().equals("");
		boolean isInProduction = isBookWithinContainer(context, bookCa,
				CONTAINER_BOOK_IN_PRODUCTION);
		boolean isContracted = isBookWithinContainer(context, bookCa,
				CONTAINER_BOOK_CONTRACTED);
		
		String awaitingTypesetterUpdates = processInfoMap.get(LMD_FIELD_AWAITING_TYPESETTER_UPDATES);
		
		String typeSetterUpdateType = processInfoMap.get(LMD_FIELD_TYPESETTER_UPDATE_TYPE);
		
		String printer = processInfoMap.get(LMD_FIELD_PRINTER_USER);
		
		if (!hasManuscript && isContracted && hasNoActiveWorkflow) {
			status = "Contracted";
		} else if (hasManuscript && !isPublished && hasNoActiveWorkflow) {
			status = "QA";
		} else if (!hasNoActiveWorkflow &&
			    (awaitingTypesetterUpdates == null) &&
			    StringUtils.equals(typeSetterUpdateType, LMD_VALUE_WORKFLOW_STARTED)) {
			status = "Handed over to production";
		} else if (!hasNoActiveWorkflow &&
			    StringUtils.equals(awaitingTypesetterUpdates, LMD_VALUE_YES) &&
			    (typeSetterUpdateType == null)) {
			status = "Typesetting";
		} else if (!hasNoActiveWorkflow &&
			    (awaitingTypesetterUpdates == null) &&
			    StringUtils.equals(typeSetterUpdateType, LMD_VALUE_INITIAL + LMD_VALUE_PROOF_SUFFIX)) {
			status = "IET reviewing initial proofs";
		} else if (!hasNoActiveWorkflow  &&
			    StringUtils.equals(awaitingTypesetterUpdates, LMD_VALUE_YES) &&
			    StringUtils.equals(typeSetterUpdateType, LMD_VALUE_UPDATE)) {
			status = "Typesetter applying corrections";
		} else if (!hasNoActiveWorkflow &&
			    (awaitingTypesetterUpdates == null) &&
			    StringUtils.equals(typeSetterUpdateType, LMD_VALUE_UPDATE + LMD_VALUE_PROOF_SUFFIX)) {
			status = "IET reviewing updated proofs";
		} else if (!hasNoActiveWorkflow &&
			    StringUtils.equals(awaitingTypesetterUpdates, LMD_VALUE_YES) &&
			    StringUtils.equals(typeSetterUpdateType, LMD_VALUE_FINAL)) {
			status = "Waiting for print files";
		} else if (!hasNoActiveWorkflow &&
			    (awaitingTypesetterUpdates == null) &&
			    StringUtils.equals(typeSetterUpdateType, LMD_VALUE_FINAL + LMD_VALUE_PROOF_SUFFIX)) {
			status = "Print files received";
		} else if (!hasNoActiveWorkflow &&
			    (awaitingTypesetterUpdates == null) &&
			    (typeSetterUpdateType == null) &&
			    (!isPublished) &&
			    (!StringUtils.isBlank(printer))) {
			status = "Printing";
		} else if (isPublished) {
			status = "Published";
		}

		return status;
	}

	private String getRowFormat(ExecutionContext context,
			ContentAssembly bookCa, Map<String, String> processInfoMap,
			Set<String> relatedRSuiteIdsWithActiveWorkflow)
			throws RSuiteException {
		String rowformat = " ";

		boolean hasManuscript = isManuscriptLoaded(context, bookCa);
		
		boolean hasNoActiveWorkflow = !relatedRSuiteIdsWithActiveWorkflow.contains(bookCa.getId());
		

		boolean isPublished = !getNormalizedValue(processInfoMap,
				"print_published_date").trim().equals("");
		boolean isInProduction = isBookWithinContainer(context, bookCa,
				CONTAINER_BOOK_IN_PRODUCTION);
		boolean isContracted = isBookWithinContainer(context, bookCa,
				CONTAINER_BOOK_CONTRACTED);

		if (isPublished) {
			rowformat = "PUBLISHED";
		} else if (isInProduction || !hasNoActiveWorkflow) {
			rowformat = "INPRODUCTION";
		} else if (!hasManuscript && isContracted && hasNoActiveWorkflow) {
			rowformat = "CONTRACTED";
		} else if (hasManuscript && hasNoActiveWorkflow) {
				rowformat = "MANUSCRIPTRECEIVED";			 	
		} else {
			rowformat = "DEFAULT";
		}

		return rowformat;
	}


	private boolean isManuscriptLoaded(ExecutionContext context,
			ContentAssembly bookCa) throws RSuiteException {
		ContentAssembly productionFilesCAItem = getChildrenByDisplayedName(
				context, bookCa.getChildrenObjects(),
				BooksConstans.CA_NAME_PRODUCTION_FILES);
		if (productionFilesCAItem != null) {
			ContentAssembly typescriptCAItem = getChildrenByDisplayedName(
					context, productionFilesCAItem.getChildrenObjects(),
					BooksConstans.CA_NAME_TYPESCRIPT);
			if (typescriptCAItem != null) {
				List<? extends ContentAssemblyItem> typescriptChildrens = typescriptCAItem
						.getChildrenObjects();
				for (ContentAssemblyItem typescriptChildren : typescriptChildrens) {
					if ((typescriptChildren.getDisplayName().toLowerCase()
							.lastIndexOf(".doc") > -1)
							|| (typescriptChildren.getDisplayName()
									.toLowerCase().lastIndexOf(".docx") > -1)) {
						return true;
					}
				}
			}
		}

		return false;
	}


	private ContentAssembly getChildrenByDisplayedName(
			ExecutionContext context,
			List<? extends ContentAssemblyItem> childrenObjects,
			String displayName) throws RSuiteException {
		ContentAssemblyService caServ = context.getContentAssemblyService();
		User user = context.getAuthorizationService().getSystemUser();
		ContentAssembly childrenCA = null;
		for (ContentAssemblyItem childrenObject : childrenObjects) {
			if (childrenObject.getDisplayName().equalsIgnoreCase(displayName)) {
				if (childrenObject.getObjectType() == ObjectType.CONTENT_ASSEMBLY) {
					childrenCA = caServ.getContentAssembly(user,
							childrenObject.getId());
				} else if (childrenObject.getObjectType() == ObjectType.CONTENT_ASSEMBLY_REF) {
					childrenCA = caServ.getContentAssembly(user,
							((ContentAssemblyReference) childrenObject)
									.getTargetId());
				}

				return childrenCA;
			}
		}

		return null;
	}

	private boolean isBookWithinContainer(ExecutionContext context,
			ContentAssembly bookCa, String containerName)
			throws RSuiteException {
		ContentAssemblyItem ancestorCAItem = ProjectContentAssemblyUtils
				.getAncestorCAbyType(context, bookCa.getId(), "booksCA");
		return ancestorCAItem.getDisplayName().equals(containerName);
	}

	private Report handleException(Log log, Exception e) {
		log.error("Unable to generate HTML output", e);
		return new HTMLReport("Server returned error " + e.getMessage());
	}

	private String getNormalizedValue(Map<String, String> metaHash, String name) {
		String value = metaHash.get(name);
		if (StringUtils.isBlank(value)) {
			return " ";
		} else {
			return value;
		}
	}

}
