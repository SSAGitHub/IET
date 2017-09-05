package com.rsicms.projectshelper.export.impl.utils;

import java.io.File;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;

import com.reallysi.rsuite.api.Alias;
import com.reallysi.rsuite.api.ManagedObject;
import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.VariantDescriptor;
import com.rsicms.projectshelper.export.MoExportContainerContext;
import com.rsicms.projectshelper.export.MoExportHandler;
import com.rsicms.projectshelper.export.MoExportConfiguration;
import com.rsicms.projectshelper.export.impl.exportercontext.MoExportContext;
import com.rsicms.projectshelper.utils.ProjectMoUtils;

public class MoExporterPathUtils {

	public static String createExportPathForVariant(
			ManagedObject moToExport, MoExportContext exportContext,
			String path)
			throws RSuiteException {
		
		MoExportConfiguration exporterConfiguration = exportContext.getExportConfiguration();
		String variantName = exporterConfiguration.getExportNonXmlVariant();
	
		VariantDescriptor variant = moToExport.getVariant(variantName);
		if (variant != null) {
			String extension = FilenameUtils.getExtension(variant.getExternalAssetPath());
			File tempFile = new File(new File(path).getParentFile(),
					FilenameUtils.getBaseName(path) + "." + extension);
			path = tempFile.getPath();
		} else {
			exportContext.getMessageHandler().warning(
					"Unable to find variant '" + variantName + "' for mo "
							+ moToExport.getId());
		}
		return path;
	}

	public static String createFilenameFromBasenameAlias(
			ManagedObject moToExport) throws RSuiteException {
		String path = "";
		path = MoExporterPathUtils.getBasenameFromAlias(moToExport);
	
		return path;
	}

	public static String getFilenameFromAlias(ManagedObject moToExport)
			throws RSuiteException {
		return MoExporterPathUtils.getAliasValue(moToExport, "filename");
	}

	public static String getBasenameFromAlias(ManagedObject moToExport)
			throws RSuiteException {
		return MoExporterPathUtils.getAliasValue(moToExport, "basename");
	}

	public static String getAliasValue(ManagedObject moToExport,
			String aliasName) throws RSuiteException {
		Alias[] aliases = moToExport.getAliases();
		String path = "";
	
		if (aliases == null) {
			return path;
		}
	
		for (Alias alias : aliases) {
			if (aliasName.equals(alias.getType())) {
				path = alias.getText();
			}
		}
		return path;
	}

	public static String getExportPathForMo(MoExportContext exportContext,
			ManagedObject moToExport, Map<String, String> exportPathMap)
			throws RSuiteException {
		return getExportPathForMo(exportContext, moToExport, null, exportPathMap);
	}
	
	public static String getExportPathForMo(MoExportContext exportContext,
            ManagedObject moToExport, String parentPath, Map<String, String> exportPathMap)
            throws RSuiteException {
        String exportPath = exportPathMap.get(moToExport.getId());
    
        String newParentPath = parentPath;
        
        if (parentPath == null){
            newParentPath = "";
        }
        
        if (exportPath == null) {
            exportPath = newParentPath + MoExporterPathUtils.getDefaultExportPath(moToExport, exportContext);
    
            MoExportHandler exportHandler = exportContext.getExportHandler();
    
            exportPath = exportHandler.getMoExportPath(moToExport, exportPath);
            exportPathMap.put(moToExport.getId(), exportPath);
        }
        return exportPath;
    }

	public static String getDefaultExportPath(ManagedObject moToExport,
			MoExportContext exportContext) throws RSuiteException {
	
		String path = getFilenameFromAlias(moToExport);
	
		if (StringUtils.isBlank(path) && ProjectMoUtils.isXMLMo(moToExport)) {
			path = createFilenameFromBasenameAlias(moToExport);
		}
	
		if (StringUtils.isBlank(path)) {
			path = moToExport.getDisplayName();
		}
	
		if (ProjectMoUtils.isXMLMo(moToExport)
				&& StringUtils.isBlank(FilenameUtils.getExtension(path))) {
			path += ".xml";
		}
		MoExportConfiguration exporterConfiguration = exportContext
				.getExportConfiguration();
		
		if (moToExport.isNonXml()
				&& StringUtils.isNotBlank(exporterConfiguration
						.getExportNonXmlVariant())) {
	
			path = createExportPathForVariant(moToExport, exportContext, path
					);
	
		}
	
		path = getContextPath(moToExport, exportContext.getExportContainerContext()) + path;
		
		return path.replaceAll("\\s+", "_");
	}

	private static String getContextPath(ManagedObject moToExport,
			MoExportContainerContext exportContainerContext) throws RSuiteException {
		String path = "";
		
		if (exportContainerContext != null){
			path = exportContainerContext.getContextPath(moToExport);
		}
		
		return path == null ? "" : path;
	}

}
