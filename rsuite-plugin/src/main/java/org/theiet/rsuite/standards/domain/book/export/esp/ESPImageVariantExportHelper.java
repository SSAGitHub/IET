package org.theiet.rsuite.standards.domain.book.export.esp;

import static org.theiet.rsuite.standards.datatype.StandardsNonXmlVariants.MAIN_WEB;
import static org.theiet.rsuite.standards.datatype.StandardsNonXmlVariants.THUMBNAIL1;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;

import com.reallysi.rsuite.api.ManagedObject;
import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.VariantDescriptor;
import com.rsicms.projectshelper.message.ProcessingMessageHandler;

public class ESPImageVariantExportHelper {

	static final String IMAGES_FOLDER_PATH = "images/";
	
	private File exportFolder;
	
	private ProcessingMessageHandler messageHandler;
	
	public ESPImageVariantExportHelper(File exportFolder,
			ProcessingMessageHandler messageHandler) {
		this.exportFolder = exportFolder;
		this.messageHandler = messageHandler;
	}

	public void exportVariants(ManagedObject mo, String exportUri)
			throws RSuiteException {

		exportVariant(mo, MAIN_WEB.getVariantName(),
				MAIN_WEB.getEspExportFolderName(), exportUri);
		exportVariant(mo, THUMBNAIL1.getVariantName(),
				THUMBNAIL1.getEspExportFolderName(), exportUri);

	}

	public void exportVariant(ManagedObject mo, String variantName,
			String variantFolderName, String exportUri) throws RSuiteException {
		VariantDescriptor variantDescriptor = mo.getVariant(variantName);

		if (variantDescriptor == null) {
			String moDetail = getMoDetailInformation(mo, exportUri);
			messageHandler.warning("Missing '" + variantName + "' variant for mo " + moDetail ,
					mo.getId());
		} else {
			exportImageVariantFile(mo, variantFolderName, variantDescriptor,
					exportUri);
		}

	}

	public String getMoDetailInformation(ManagedObject mo, String exportUri) {
		return FilenameUtils.getBaseName(exportUri) + " [" + mo.getId() + "]";
	}

	public void exportImageVariantFile(ManagedObject mo,
			String variantFolderName, VariantDescriptor variantDescriptor,
			String exportUri) throws RSuiteException {

		File outputFile = getFileForImageVariant(mo, variantFolderName,
				variantDescriptor, exportUri);

		try {
			FileUtils.writeByteArrayToFile(outputFile,
					variantDescriptor.getContent());
		} catch (IOException e) {
			throw new RSuiteException(0, e.getLocalizedMessage(), e);
		}
	}

	protected File getFileForImageVariant(ManagedObject mo, String variantFolderName,
			VariantDescriptor variantDescriptor, String exportUri) {
		String variantFileName = variantDescriptor.getExternalAssetPath();
		if (StringUtils.isBlank(variantFileName)) {
			String defaultFileName = FilenameUtils.getName(exportUri);
			String moDetail = getMoDetailInformation(mo, exportUri);
			messageHandler.warning("Missing file name for variant "
					+ variantDescriptor.getVariantName()
					+ " export with default name " + defaultFileName + " mo " + moDetail);
			variantFileName = defaultFileName;
		}else{
			variantFileName = FilenameUtils.getBaseName(exportUri) + "." + FilenameUtils.getExtension(variantFileName).toLowerCase();
		}

		File outputFile = new File(exportFolder, IMAGES_FOLDER_PATH
				+ variantFolderName + "/" + variantFileName);
		return outputFile;
	}
}
