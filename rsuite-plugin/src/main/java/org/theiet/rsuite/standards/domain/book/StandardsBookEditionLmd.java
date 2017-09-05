package org.theiet.rsuite.standards.domain.book;

import org.theiet.rsuite.standards.datatype.StandardsIcmlXsltProvider;
import org.theiet.rsuite.standards.datatype.StandardsPdfTranstypeProvider;

import com.reallysi.rsuite.api.*;

public class StandardsBookEditionLmd {

    private String LMD_FIELD_ISBN = "isbn";
    
    private static final String LMD_FIELD_PDF_TRANSFORMATION = "pdf_transformation";
    
    private static final String LMD_FIELD_ICML_TRANSFORMATION = "icml_transformation";
    
    private ContentAssembly bookEditionCa;

    StandardsBookEditionLmd(ContentAssembly bookEditionCa) {
        this.bookEditionCa = bookEditionCa;
    }
    
    public String getISBN() throws RSuiteException{
        return bookEditionCa.getLayeredMetadataValue(LMD_FIELD_ISBN);
    }
    
    public String getPdfTranstype() throws RSuiteException{
    	String pdfTranstypeLmd = bookEditionCa.getLayeredMetadataValue(LMD_FIELD_PDF_TRANSFORMATION);
    	
    	StandardsPdfTranstypeProvider pdfTranstype = StandardsPdfTranstypeProvider.getStandardsPdfTranstypeProvider(pdfTranstypeLmd);
    	return pdfTranstype.getTranstype();
    }
    
    public String getIcmlXslt() throws RSuiteException{
    	String icmlTranstypeLmd = bookEditionCa.getLayeredMetadataValue(LMD_FIELD_ICML_TRANSFORMATION);
    	
    	StandardsIcmlXsltProvider pdfTranstype = StandardsIcmlXsltProvider.getStandardsIcmlXsltProvider(icmlTranstypeLmd);
    	return pdfTranstype.getXslURI();
    }
}
