package org.theiet.rsuite.reporting;

import java.io.*;
import java.net.*;
import java.text.*;
import java.util.*;

import javax.xml.stream.*;
import javax.xml.transform.*;
import javax.xml.transform.stream.*;

import org.apache.commons.lang.StringUtils;
import org.joda.time.*;
import org.theiet.rsuite.journals.JournalConstants;

import com.reallysi.rsuite.api.*;
import com.reallysi.rsuite.api.report.ReportGenerationContext;
import com.reallysi.rsuite.service.XmlApiManager;

final class ArticleReportHelper implements JournalConstants {

    private static final SimpleDateFormat REPORT_DATE_STRING_FORMAT = new SimpleDateFormat(
            "yyyy-MM-dd");
    
    static final int TWELVE_MONTHS = 12;
    
    private static final double AVG_DAYS_IN_MONTH = 30.0;
    
    static final DecimalFormat MONTH_FORMAT = new DecimalFormat("#0.0");
    
    static final double DAYS_TO_MONTHS = 1.0 / AVG_DAYS_IN_MONTH;
    
    static class SummaryInfo {

        List<Integer> daysSubmitToEFirstPublication = new ArrayList<Integer>();

        List<Integer> daysAcceptToEFirstPublication = new ArrayList<Integer>();

        List<Integer> daysSubmitToAccept = new ArrayList<Integer>();
        
        int pagesTypesetter = 0;

        int totalArticles = 0;

        int articlesWithin12Months = 0;

        public int getTotalArticles() {
            return totalArticles;
        }

    }
    
    static void collectSummaryInfo(SummaryInfo summaryInfo,
            Map<String, String> articleDetails) {

        summaryInfo.totalArticles++;

        collectDaysIfAvailable(summaryInfo.daysSubmitToAccept, articleDetails,
                "submit-to-acceptance_days");

        Integer value = collectDaysIfAvailable(
                summaryInfo.daysSubmitToEFirstPublication, articleDetails,
                "submit-to-e-first-publication_days");
        
        Double months = (value == null) ? null : value.intValue() * DAYS_TO_MONTHS;
        if (months != null && months < TWELVE_MONTHS) {
            summaryInfo.articlesWithin12Months++;
        }

        collectDaysIfAvailable(summaryInfo.daysAcceptToEFirstPublication,
                articleDetails, "accept-to-e-first-publication_days");

        collectPagesIfAvailable(articleDetails, summaryInfo, "typesetPages");
    }

    static void collectPagesIfAvailable (Map<String, String> articleDetails,
    		SummaryInfo summaryInfo, String pageField) {
    	String typesetPages = articleDetails.get(pageField);

        if (StringUtils.isNumeric(typesetPages)) {
        	int pages = Integer.valueOf(typesetPages);
        	summaryInfo.pagesTypesetter = summaryInfo.pagesTypesetter + pages; 
        }
    }

    static Integer collectDaysIfAvailable(List<Integer> summaryList,
            Map<String, String> articleDetails, String dayField) {
        String days = articleDetails.get(dayField);
        Integer value = null;
        String monthValue = "Unavailable";
        if (StringUtils.isNumeric(days)) {
            value = Integer.valueOf(days);
            summaryList.add(value);

            double months = value.intValue() * DAYS_TO_MONTHS;
            monthValue = MONTH_FORMAT.format(months);

        }
        
        articleDetails.put(dayField.replace("_days", ""), monthValue);

        return value;
    }

    static void serializeArticleDetails(XMLStreamWriter xmlWriter,
            Map<String, String> articleDetails, String[] REPORT_ENTRIES, String[] REPORT_ENTRY_CLASS) throws XMLStreamException {

        String articleId = articleDetails.get("articleId");

        xmlWriter.writeStartElement("article");
        xmlWriter.writeAttribute("id", articleId);
        xmlWriter.writeAttribute("class", "ca");
        for (int i = 0; i < REPORT_ENTRIES.length; i++) {
            xmlWriter.writeStartElement("meta-item");
            xmlWriter.writeAttribute("item", REPORT_ENTRIES[i]);
            xmlWriter.writeAttribute("class", REPORT_ENTRY_CLASS[i]);

            String reportEntry = articleDetails.get(REPORT_ENTRIES[i]);
            xmlWriter.writeCharacters(reportEntry == null ? "" : reportEntry);
            xmlWriter.writeEndElement();
        }
        xmlWriter.writeEndElement();
    }

    static Map<String, String> getArticleDetails(ContentAssembly articleCa,
            Map<String, String> pubtrackMetadata) throws RSuiteException,
            ParseException {

        Map<String, String> articleDetails = new HashMap<String, String>();

        Map<String, String> articleLmdMap = getLmdMap(articleCa);

        articleDetails.put("articleId", articleCa.getDisplayName());

        String author = getLMD(articleLmdMap, "author_surname") + ", "
                + getLMD(articleLmdMap, "author_first_name");
        articleDetails.put("author", author);

        articleDetails.put("email", getLMD(articleLmdMap, "author_email"));

        String submittedDate = getLMD(articleLmdMap, "submitted_date");
        articleDetails.put("submitted", submittedDate);

        String acceptedDate = getLMD(articleLmdMap, "decision_date");
        articleDetails.put("accepted", acceptedDate);

        String articlePassDate = getLMD(articleLmdMap, "online_published_date");
        articleDetails.put("article-pass-for-press", articlePassDate);
        
        String submissionType = getLMD(articleLmdMap, "submission_type");
        articleDetails.put("submissionType", submissionType);
        
        String categoryCode = getLMD(articleLmdMap, "category_code");
        articleDetails.put("categoryCode", categoryCode);
        
        String licenceType = getLMD(articleLmdMap, "licence_type");
        articleDetails.put("licenceType", licenceType);
        
        String authorInstitution = getLMD(articleLmdMap, "author_institution");
        articleDetails.put("authorInstitution", authorInstitution);
        
        String authorCountry = getLMD(articleLmdMap, "author_country");
        articleDetails.put("authorCountry", authorCountry);

        String typesetPages = getLMD(articleLmdMap, "typeset_pages");
        collectPagesDelta(articleDetails, "typesetPages", typesetPages);

        Date dtSubmit = getDate(submittedDate);
        Date dtAccept = getDate(acceptedDate);
        Date dtArticlePass = getDate(articlePassDate);

        collectDaysDelta(articleDetails, "submit-to-acceptance_days", dtSubmit,
                dtAccept);
        collectDaysDelta(articleDetails, "submit-to-e-first-publication_days",
                dtSubmit, dtArticlePass);

        collectDaysDelta(articleDetails, "accept-to-e-first-publication_days",
                dtAccept, dtArticlePass);

        return articleDetails;
    }

    static Map<String, String> getLmdMap(ContentAssembly articleCa)
            throws RSuiteException {
        List<MetaDataItem> lmdItems = articleCa.getMetaDataItems();

        Map<String, String> lmdHash = new HashMap<String, String>();

        for (MetaDataItem lmdItem : lmdItems) {
            lmdHash.put(lmdItem.getName(), lmdItem.getValue());
        }
        return lmdHash;
    }

    static void collectPagesDelta(Map<String, String> articleDetails,
            String detailFieldName, String typesetterPages) {

        String detailValue = "Unavailable";

        if (StringUtils.isNotBlank(typesetterPages)) {
            detailValue = typesetterPages;
        }

        articleDetails.put(detailFieldName, detailValue);
    }

    static void collectDaysDelta(Map<String, String> articleDetails,
            String detailFieldName, Date startDate, Date endDate) {

        String detailValue = "Unavailable";

        if (!(startDate == null || (endDate == null))) {
            int days = computeDeltaDays(startDate, endDate);
            detailValue = String.valueOf(days);
        }

        articleDetails.put(detailFieldName, detailValue);
    }
    
    static String getLMD(Map<String, String> lmdHash, String name) {
        return lmdHash.containsKey(name) ? lmdHash.get(name) : " ";
    }
    
    static Date getDate(String dateString) throws ParseException {
        if (StringUtils.isBlank(dateString)){
            return null;
        }
        return REPORT_DATE_STRING_FORMAT.parse(dateString);
    }
    
    static int computeDeltaDays(Date d1, Date d2) {
        return Days.daysBetween(new DateTime(d1), new DateTime(d2)).getDays();
    }
    
    static String transformToHTML(ReportGenerationContext context,
            byte[] xmlSourceBytes, String xsltUri) throws RSuiteException, URISyntaxException,
            TransformerException {

        XmlApiManager xmlApiMgr = context.getXmlApiManager();

        StringWriter stringWriter = new StringWriter();

        String skey = context.getSession().getKey();
        Transformer trans = xmlApiMgr.getTransformer(new URI(xsltUri));
        trans.setParameter("skey", skey);

        ByteArrayInputStream bais = new ByteArrayInputStream(xmlSourceBytes);
        StreamSource xmlSource = new StreamSource(bais);

        StreamResult result = new StreamResult(stringWriter);
        trans.transform(xmlSource, result);
        return stringWriter.toString();
    }
}
