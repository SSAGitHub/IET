package com.rsicms.projectshelper.publish.generators.pdf;

import java.io.*;

import javax.xml.transform.*;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.io.IOUtils;

import com.reallysi.rsuite.api.*;
import com.reallysi.rsuite.api.extensions.ExecutionContext;
import com.reallysi.rsuite.api.xml.FopHelper;
import com.reallysi.rsuite.service.XmlApiManager;
import com.rsicms.rsuite.helpers.utils.TransformationUtils;


public class PDFGenerator {

    public static String generatePDF(ExecutionContext context, ManagedObject mo, String xsltUri,
            OutputStream pdfOutputStream) throws RSuiteException {
        String fo = transformXMLToFO(context, mo, xsltUri);

        try {
            generatePDFFromFo(context.getXmlApiManager(), IOUtils.toInputStream(fo),
                    pdfOutputStream);
        } catch (TransformerConfigurationException e) {
            throw new RSuiteException(0, "Unable to generated PDF for mo " + mo.getId(), e);
        }
        
        return fo;
    }

    private static String transformXMLToFO(ExecutionContext context, ManagedObject mo, String xsltUri)
            throws RSuiteException {
        return TransformationUtils.transformDocument(context, mo, xsltUri);
    }

    private static void generatePDFFromFo(XmlApiManager xmlApiManager, InputStream foInputStream,
            OutputStream pdfOutputStream) throws TransformerConfigurationException, RSuiteException {

        FopHelper fopHelper = xmlApiManager.getFopHelper();
        Transformer transformer = xmlApiManager.getTransformerFactory().newTransformer();
        StreamSource source = new StreamSource(foInputStream);


        fopHelper.transformToPDF(source, transformer, pdfOutputStream);
    }
}
