package org.theiet.rsuite.iettv.domain.validation;

import java.io.File;
import java.util.*;

import org.xml.sax.*;

import com.reallysi.rsuite.api.RSuiteException;

class IetTvValidationErrorHandler implements ErrorHandler {

    private List<SAXParseException> errors = new ArrayList<SAXParseException>();

    private File fileToValidate;
    
    public IetTvValidationErrorHandler(File fileToValidate) {
        this.fileToValidate = fileToValidate;
    }

    @Override
    public void warning(SAXParseException exception) throws SAXException {}

    @Override
    public void error(SAXParseException exception) throws SAXException {
        errors.add(exception);

    }

    @Override
    public void fatalError(SAXParseException exception) throws SAXException {
        errors.add(exception);

    }

    public void checkForErrors() throws RSuiteException {
        if (errors.isEmpty()){
            return;
        }
        
        StringBuilder errorMessage = serializeErrorsList();
        throw new RSuiteException(errorMessage.toString());
    }

    private StringBuilder serializeErrorsList() {
        StringBuilder errorMessage = new StringBuilder();
        errorMessage.append("INVALID FILE: ").append(fileToValidate.getAbsolutePath());
        errorMessage.append("\n");
        
        for (SAXParseException error : errors) {

            errorMessage.append("line: ").append(error.getLineNumber());
            errorMessage.append("; ").append(error.getMessage());
            errorMessage.append("\r\n");

        }
        return errorMessage;
    }
}
