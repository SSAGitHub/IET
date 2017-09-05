package org.theiet.rsuite.standards.datatype;

import org.apache.commons.lang.StringUtils;

public enum StandardsPdfTranstypeProvider {

	REG_2_PDF_WREG("pdf-reg", "Wiring Regulations"),
	// REG_2_PDF_OSG(null, "Onsite Guide"),
	REG_2_PDF_GN("pdf-GN", "Guidance Notes");

	private final String transtype;
	private final String tranformationContentName;

	private StandardsPdfTranstypeProvider(String transtype, String tranformationContentName) {
		this.transtype = transtype;
		this.tranformationContentName = tranformationContentName;
	}

	public static StandardsPdfTranstypeProvider getStandardsPdfTranstypeProvider(String pdfTransformation) {
		
		if (StringUtils.isBlank(pdfTransformation)){
			return REG_2_PDF_WREG;
		}
		
		switch (pdfTransformation) {
		case "pdf-GN":
			return REG_2_PDF_GN;
		default:
			return REG_2_PDF_WREG;
		}
	}

	public String getTranstype() {
		return transtype;
	}

	public String getTranformationContentName() {
		return tranformationContentName;
	}

}
