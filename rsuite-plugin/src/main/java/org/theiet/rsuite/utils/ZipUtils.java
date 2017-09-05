package org.theiet.rsuite.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.tools.zip.MultithreadedUnzippingController;

public class ZipUtils {

	/**
	 * Archives all provided files and folder
	 * @param files array of files object (files and folders)
	 * @param baseFolder base folder.
	 * @param outPutFile output zip file
	 * @throws ZipUtilsException if something goes wrong.
	 */
	@SuppressWarnings("unchecked")
	public static void zipFiles(File[] files, String baseFolder, File outPutFile)
			throws RSuiteException {

		try {
			OutputStream outStream = new FileOutputStream(outPutFile);
			ZipOutputStream zipOut = new ZipOutputStream(outStream);
			baseFolder = FilenameUtils.separatorsToUnix(baseFolder);
			for (File file : files) {
				if (file.isFile()) {
					addFileToZip(file, baseFolder, zipOut);
				} else {
					Collection<File> folderFiles = FileUtils.listFiles(file,
							null, true);
					for (File folderFile : folderFiles) {
						addFileToZip(folderFile, baseFolder, zipOut);
					}
				}

			}

			zipOut.flush();
			outStream.flush();
			zipOut.close();
			outStream.close();
		} catch (IOException e) {
			ExceptionUtils.throwRsuiteException(e);
		}
	}

	private static void addFileToZip(File file, String baseFolder,
			ZipOutputStream zipOut) throws IOException {

		String filePath = FilenameUtils
				.separatorsToUnix(file.getAbsolutePath());
		FileInputStream fis = new FileInputStream(filePath);

		filePath = filePath.replace(baseFolder, "");
		if (filePath.startsWith("/")){
			filePath = filePath.substring(1);
		}
		
		ZipEntry entry = new ZipEntry(filePath);

		zipOut.putNextEntry(entry);

		byte[] buf = new byte[1024];
		int len;

		while ((len = fis.read(buf)) > 0) {
			zipOut.write(buf, 0, len);
		}
		
		fis.close();
	}

	/**
	 * Unzips the specified Zip into the specified folder, returning the folder
	 * that contains the unzipped files.
	 * 
	 * @param zipFile
	 * @param outputFolder
	 * @return Folder containing the unzipped files.
	 * @throws IOException
	 * @throws RSuiteException
	 */
	public static File unzip(File zipFile, File outputFolder)
			throws IOException, RSuiteException {
		MultithreadedUnzippingController controller = new MultithreadedUnzippingController();
	
		controller.setMaxThreads(10);
		File unzipFolder = controller.unzip(zipFile, outputFolder, false);
		if (unzipFolder == null || unzipFolder.list() == null
				|| unzipFolder.list().length == 0) {
			throw new RSuiteException(
					"No files in zip or failed to create result folder during unzip.");
		}
		return unzipFolder;
	}
}
