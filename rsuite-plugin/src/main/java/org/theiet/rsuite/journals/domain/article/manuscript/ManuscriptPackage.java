package org.theiet.rsuite.journals.domain.article.manuscript;

import java.io.File;

import com.reallysi.rsuite.api.RSuiteException;

public class ManuscriptPackage {

	private File metadataFile;

	private File scholarOnePdf;

	private File packageFolder;

	public File getMetadataFile() {
		return metadataFile;
	}

	public File getScholarOnePdf() {
		return scholarOnePdf;
	}

	public File getPackageFolder() {
		return packageFolder;
	}

	public ManuscriptPackage(File packageFolder) throws RSuiteException {
		this.packageFolder = packageFolder;
		this.metadataFile = findManuscriptMetadataFile(packageFolder);
		this.scholarOnePdf = findScholarOnePdf(packageFolder);
	}

	private File findScholarOnePdf(File packageFolder) throws RSuiteException {
		File pdfFolder = new File(packageFolder, "pdf");
		for (File file : pdfFolder.listFiles()) {
			String filename = file.getName();
			if (filename.toLowerCase().endsWith("pdf")) {
				return file;
			}
		}

		throw new RSuiteException("Unable to find the scholar one pdf file");
	}

	private File findManuscriptMetadataFile(File packageFolder) throws RSuiteException {
		for (File file : packageFolder.listFiles()) {
			String filename = file.getName();
			if (filename.toLowerCase().endsWith("xml")) {
				return file;
			}
		}

		throw new RSuiteException("Unable to find the metadata file");
	}

}
