package com.rsicms.projectshelper.publish.generators.pdf.engines;

import java.io.*;

import javax.xml.transform.*;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.logging.Log;

import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.xml.FopHelper;
import com.reallysi.rsuite.service.XmlApiManager;

public class FopPdfEngine implements PdfFromattingEngine {

    private XmlApiManager xmlApiManager;

    public FopPdfEngine(XmlApiManager xmlApiManager) {
        this.xmlApiManager = xmlApiManager;
    }

    @Override
    public void generatePDFFromFo(InputStream foInputStream, OutputStream pdfOutputStream)
            throws RSuiteException {

        try {
            FopHelper fopHelper = xmlApiManager.getFopHelper();
            Transformer transformer = xmlApiManager.getTransformerFactory().newTransformer();
            StreamSource source = new StreamSource(foInputStream);

            fopHelper.transformToPDF(source, transformer, pdfOutputStream);

        } catch (TransformerException e) {
            throw new RSuiteException(0, "Unable to generated pdf from fo file", e);
        }
    }

    @Override
    public void generatePDFFromFo(Log logger, File foInputFile, File pdfOutputFile) throws RSuiteException {
        try {
            FileInputStream foInputStream = new FileInputStream(foInputFile);
            FileOutputStream pdfOutputStream = new FileOutputStream(pdfOutputFile);
            generatePDFFromFo(foInputStream, pdfOutputStream);
        } catch (IOException e) {
            throw new RSuiteException(0, "Unable to generated pdf from fo file", e);
        }

    }

}
