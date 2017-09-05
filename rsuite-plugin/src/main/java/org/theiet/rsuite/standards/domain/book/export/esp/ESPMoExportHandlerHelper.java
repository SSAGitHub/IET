package org.theiet.rsuite.standards.domain.book.export.esp;

import static org.theiet.rsuite.standards.StandardsBooksConstans.VAR_BOOK_EDITION_ID;
import static org.theiet.rsuite.standards.domain.book.export.esp.ESPImageVariantExportHelper.IMAGES_FOLDER_PATH;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.theiet.rsuite.standards.domain.book.StandardsBookEdition;

import com.reallysi.rsuite.api.ManagedObject;
import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.extensions.ExecutionContext;
import com.rsicms.projectshelper.export.MoExportContainerContext;
import com.rsicms.projectshelper.export.MoExportHandlerContext;

public class ESPMoExportHandlerHelper {

    private ESPMoToEProductCodeMapper moToEProductCodeMapper;
    
    private ESPImageVariantExportHelper imageVariantExportHelper;
    
    public ESPMoExportHandlerHelper(MoExportHandlerContext moExportHandlerContext) throws RSuiteException {
        moToEProductCodeMapper = createMoToEProductCodeMapper(moExportHandlerContext);
        
        imageVariantExportHelper =
                new ESPImageVariantExportHelper(moExportHandlerContext.getExportFolder(),
                        moExportHandlerContext.getMessageHandler());
    }
    
    
    private ESPMoToEProductCodeMapper createMoToEProductCodeMapper(MoExportHandlerContext moExportHandlerContext)
            throws RSuiteException {
        ExecutionContext context = moExportHandlerContext.getContext();
        MoExportContainerContext exportContainerContext = moExportHandlerContext
                .getExportContainerContext();

        StandardsBookEdition bookEdition = getBookEdition(context, exportContainerContext);

        return new ESPMoToEProductCodeMapper(context,
                moExportHandlerContext.getUser(), exportContainerContext,
                bookEdition);
    }
    
    protected static StandardsBookEdition getBookEdition(ExecutionContext context,
            MoExportContainerContext exportContainerContext)
            throws RSuiteException {
        String mainBookEditionId = exportContainerContext
                .getAdditionalContextInfo(VAR_BOOK_EDITION_ID);
        return new StandardsBookEdition(context, mainBookEditionId);
    }
    
    protected String createMoExportPath(ManagedObject mo, String exportUri, String exportPath)
            throws RSuiteException {
        if (mo.isNonXml()) {

            imageVariantExportHelper.exportVariants(mo, exportUri);
            return IMAGES_FOLDER_PATH + "InlineReaderImage/" + FilenameUtils.getName(exportPath);
        } else {
            exportPath = createExportPathForXMLMo(mo, exportPath);
        }
        return exportPath;
    }
    
    protected String createExportPathForXMLMo(ManagedObject mo, String exportPath)
            throws RSuiteException {
        String eproductCode = moToEProductCodeMapper
                .getEProductCodeForMo(mo);
        exportPath = addProductPrefix(exportPath, eproductCode);
        return exportPath;
    }

    private String addProductPrefix(String exportPath, String eproductCode) {
        String path = FilenameUtils.getPath(exportPath);
        String name = FilenameUtils.getName(exportPath);

        String eproductCodePrefix = "";

        if (StringUtils.isNotBlank(eproductCode)) {
            eproductCodePrefix = eproductCode + "_";
        }

        return path + eproductCodePrefix + name;
    }
}
