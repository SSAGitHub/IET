package org.theiet.rsuite.onix.domain;

import java.util.HashSet;
import java.util.Set;


public enum ProductFormat {

	PRINT("print"), DIGITAL("digital");

	private String value;

	private ProductFormat(String value) {
		this.value = value;
	}


	public static ProductFormat fromString(String stringValue) {

		for (ProductFormat outputFormat : ProductFormat.values()) {
			if (outputFormat.getValue().equalsIgnoreCase(stringValue)) {
				return outputFormat;
			}
		}

		throw new IllegalArgumentException("Invalid output format: "
				+ stringValue);
	}

	public String getValue() {
		return value;
	}
	
	public static Set<ProductFormat> getAllProductFormats(){
		Set<ProductFormat> allFormats = new HashSet<ProductFormat>();
		for (ProductFormat format : ProductFormat.values()){
			allFormats.add(format);
		}
		return allFormats;
	}

}
