package com.rsicms.projectshelper.utils.string.csv;

public class CSVBuilder {

	private static final String VALUE_SEPARATOR = ";";

	private static final String QUOT = "\"";

	private static final String LINE_SEPARATOR = "\n";

	private StringBuilder csvContent = new StringBuilder();

	public void addValueSeparator() {
		csvContent.append(VALUE_SEPARATOR);
	}

	public void addValue(String value) {
		String csvValue = value == null ? "" : value;
		csvValue = csvValue.replace(QUOT, "\"\"");
		csvContent.append(QUOT).append(csvValue).append(QUOT);
	}

	public void addNewLine() {
		csvContent.append(LINE_SEPARATOR);
	}

	public StringBuilder getCSVContent() {
		return csvContent;
	}

	@Override
	public String toString() {
		return csvContent.toString();
	}
}
