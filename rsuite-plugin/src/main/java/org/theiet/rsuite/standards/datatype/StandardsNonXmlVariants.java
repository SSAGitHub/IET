package org.theiet.rsuite.standards.datatype;


public enum StandardsNonXmlVariants {

	MASTER("master"), MAIN_WEB("mainWeb", "FullViewImage"), THUMBNAIL1("thumbnail1", "ThumbnailImage");
	
	private String variantName;
	
	private String espExportFolderName;

	private StandardsNonXmlVariants(String variantName) {
		this.variantName = variantName;
	}
	
	private StandardsNonXmlVariants(String variantName, String espExportFolderName) {
		this(variantName);
		this.espExportFolderName = espExportFolderName;
	}
	
	public String getVariantName() {
		return variantName;
	}
	
	public String getEspExportFolderName() {
		return espExportFolderName;
	}

	@Override
	public String toString() {
		return variantName;
	}
}
