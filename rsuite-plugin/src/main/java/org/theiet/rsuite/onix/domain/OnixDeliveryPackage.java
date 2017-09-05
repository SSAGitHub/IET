package org.theiet.rsuite.onix.domain;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.VersionEntry;
import com.rsicms.projectshelper.zip.ZipHelper;

public class OnixDeliveryPackage {

	public static String getDeliveryOnixMessageFileName(VersionEntry moVerionEntry) throws RSuiteException {
		Date committedDate = moVerionEntry.getDtCommitted();
		return getDeliveryOnixFileName(committedDate, "xml");
	}

	private static String getDeliveryOnixFileName(Date date, String extension) throws RSuiteException {
		SimpleDateFormat dateFormatter = new SimpleDateFormat("YYYYMMdd");
		return String.format("iet_%s_onix." + extension, dateFormatter.format(date));
	}

	public File createDeliveryOnixZipPackage(File onixMessageFile, File parentFolder) throws RSuiteException {

		Date currentDate = new Date();
		String zipPackageFileName = getDeliveryOnixFileName(currentDate, "zip");

		File outputZipFile = new File(parentFolder, zipPackageFileName);
		
		
		try (FileInputStream onixFileInputStream = new FileInputStream(onixMessageFile)) {
			ZipHelper zipHelper = new ZipHelper(outputZipFile);
			
			InputStream formattedOnix = prepareOnixMessageBeforeSend( onixFileInputStream);
			String onixMessageFileName = getDeliveryOnixFileName(currentDate, "xml");
			
			zipHelper.addZipEntry(onixMessageFileName, formattedOnix);
			zipHelper.closeArchive();
		} catch (IOException e) {
			throw new RSuiteException(0, e.getMessage(), e);
		}

		return outputZipFile;
	}
	
	private InputStream prepareOnixMessageBeforeSend(FileInputStream resultFileIS) throws RSuiteException {
		ByteArrayOutputStream onixOS = new ByteArrayOutputStream();
		
		OnixFormatter onixFormatter = new OnixFormatter();
		onixFormatter.removeRSuiteDataFromOnixMessage(resultFileIS, onixOS);
		InputStream formattedOnixMessage = new ByteArrayInputStream(onixOS.toByteArray());
		return formattedOnixMessage;
	}
}
