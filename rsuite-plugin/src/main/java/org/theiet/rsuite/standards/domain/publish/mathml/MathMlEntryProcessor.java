package org.theiet.rsuite.standards.domain.publish.mathml;

import java.io.File;

import com.reallysi.rsuite.api.RSuiteException;

public interface MathMlEntryProcessor {

	String convertMathMlToImage(File contextFile, String mathMlXML) throws RSuiteException;
}
