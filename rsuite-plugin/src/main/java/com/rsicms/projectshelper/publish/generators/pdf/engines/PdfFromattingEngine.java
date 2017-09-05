package com.rsicms.projectshelper.publish.generators.pdf.engines;

import java.io.*;

import org.apache.commons.logging.Log;

import com.reallysi.rsuite.api.RSuiteException;

public interface PdfFromattingEngine {

    void generatePDFFromFo(InputStream foInputStream, OutputStream pdfOutputStream)
            throws RSuiteException;
    
    void generatePDFFromFo(Log logger, File foInputFile, File pdfOutputFile)
            throws RSuiteException;
}
