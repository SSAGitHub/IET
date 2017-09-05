package org.theiet.rsuite.standards.datatype;

import org.theiet.rsuite.standards.StandardsBooksConstans;

public enum StandardsIcmlXsltProvider {

	REG_2_ICML_WREG(StandardsBooksConstans.XSLT_URI_REG_2_ICML_WREG, StandardsBooksConstans.OUTPUT_TRANSFORMATION_WIRING_REGULATIONS),
	REG_2_ICML_OSG(StandardsBooksConstans.XSLT_URI_REG_2_ICML_OSG, StandardsBooksConstans.OUTPUT_TRANSFORMATION_ONSITE_GUIDE),
	REG_2_ICML_GN(StandardsBooksConstans.XSLT_URI_REG_2_ICML_GN, StandardsBooksConstans.OUTPUT_TRANSFORMATION_GUIDANCE_NOTES);

	private String xslURI;
	private String tranformationContentName;

	private StandardsIcmlXsltProvider (String xslURI, String tranformationContentName) {
		this.xslURI = xslURI;
		this.tranformationContentName = tranformationContentName;
	}

	public static StandardsIcmlXsltProvider getStandardsIcmlXsltProvider(String icmlTransformation) {
		StandardsIcmlXsltProvider icmlXsltProvider = null; 
		if (icmlTransformation != null && icmlTransformation.startsWith(StandardsBooksConstans.LMD_VALUE_ICML_TRANSFORMATION_WRITING_REGULATION)) {
			icmlXsltProvider = REG_2_ICML_WREG; 
		} else if (icmlTransformation != null && icmlTransformation.startsWith(StandardsBooksConstans.LMD_VALUE_ICML_TRANSFORMATION_GUIDANCE_NOTES)) {
			icmlXsltProvider = REG_2_ICML_GN;
		} else if (icmlTransformation != null && icmlTransformation.startsWith(StandardsBooksConstans.LMD_VALUE_ICML_TRANSFORMATION_ONSITE_GUIDE)) {
			icmlXsltProvider = REG_2_ICML_OSG;
		} else {// Guidance Notes will be the default transformation
			icmlXsltProvider = REG_2_ICML_GN;
		}

		return icmlXsltProvider;
	}
	
	public String getXslURI () {
		return xslURI;
	}

	public String getTranformationContentName () {
		return tranformationContentName;
	}

}
