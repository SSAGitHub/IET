package com.rsicms.projectshelper.zip;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import com.reallysi.rsuite.api.RSuiteException;

public class ZipHelper {

	private ZipOutputStream zipOut;

	private OutputStream outStream;

	public ZipHelper(File outputFile) throws RSuiteException {
		try {
			outStream = new FileOutputStream(outputFile);
		} catch (FileNotFoundException e) {
			throw new RSuiteException(0, e.getMessage(), e);
		}
		zipOut = new ZipOutputStream(outStream);
	}
	
	public ZipHelper(OutputStream outputStream) throws RSuiteException {
		outStream = outputStream;
		zipOut = new ZipOutputStream(outStream);
	}

	public void addZipEntry(String entryPath, InputStream content) throws RSuiteException {

		String filePath = entryPath;
		if (filePath.startsWith("/")) {
			filePath = filePath.substring(1);
		}

		ZipEntry entry = new ZipEntry(filePath);

		try {
			zipOut.putNextEntry(entry);

			byte[] buf = new byte[1024];
			int len;

			while ((len = content.read(buf)) > 0) {
				zipOut.write(buf, 0, len);
			}

			content.close();
		} catch (IOException e) {
			throw new RSuiteException(0, e.getMessage(), e);
		}
	}

	public void closeArchive() throws RSuiteException {
		try {

			zipOut.flush();
			outStream.flush();
			zipOut.close();
			outStream.close();
		} catch (IOException e) {
			throw new RSuiteException(0, e.getMessage(), e);
		}
	}
}
