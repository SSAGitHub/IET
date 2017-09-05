package org.theiet.rsuite.standards.domain.book.export.esp;

import java.util.*;

import javax.xml.transform.*;

import net.sf.saxon.om.NodeInfo;

import org.apache.commons.io.FilenameUtils;

import com.reallysi.rsuite.api.*;
import com.reallysi.rsuite.api.extensions.ExecutionContext;
import com.rsicms.projectshelper.contenttypes.jatsv1.JatsUtils;
import com.rsicms.projectshelper.export.*;
import com.rsicms.projectshelper.export.impl.handlers.DefaultMoExportHandler;
import com.rsicms.projectshelper.utils.ProjectTransformationUtils;

public class ESPJatsMoExportHandler extends DefaultMoExportHandler {

    private ESPMoExportHandlerHelper moExportHelper;

    private List<MoExportAdditionalTransformation> additionalTransformationList;
    
    private static final String XSLT_URI_REMOVE_DTD_DECLARATION = "rsuite:/res/plugin/iet/xslt/jats/remove-dtd-declaration.xsl";
    
    @Override
    public void initialize(MoExportHandlerContext moExportHandlerContext) throws RSuiteException {
        super.initialize(moExportHandlerContext);
        moExportHelper = new ESPMoExportHandlerHelper(moExportHandlerContext);
        additionalTransformationList = createAdditionalTransformationForDitaFiles(moExportHandlerContext.getContext());
    }
    
    protected List<MoExportAdditionalTransformation> createAdditionalTransformationForDitaFiles(ExecutionContext context) throws RSuiteException {

        TransformerFactory transformerFactory =context.getXmlApiManager().getTransformerFactory();

        Templates ditaTemplate =
                ProjectTransformationUtils.createTransformTemplate(context, transformerFactory,
                        XSLT_URI_REMOVE_DTD_DECLARATION);

        List<MoExportAdditionalTransformation> additionalTransformationList =
                new ArrayList<MoExportAdditionalTransformation>();
        additionalTransformationList.add(new MoExportAdditionalTransformation(ditaTemplate, null));

        return additionalTransformationList;
    }

    @Override
    public boolean processRefenceToMo(String contextMoId, NodeInfo referenceAttibute,
            ManagedObject referencedMo) {
        return true;
    }

    @Override
    public MoExportCrossReferenceValidator createMoExportCrossRefernceValidator() {
        return null;
    }

    @Override
    public String getMoExportPath(ManagedObject mo, String exportUri) throws RSuiteException {
                
        String exportPath = super.getMoExportPath(mo, exportUri);
        
        if (JatsUtils.isJatsArticle(mo)){
            exportPath = FilenameUtils.getName(exportPath);
        }
        
        exportPath = moExportHelper.createMoExportPath(mo, exportUri, exportPath);

        return exportPath;
    }
    
    

    @Override
    public List<MoExportAdditionalTransformation> getTransformationToPerformBeforePersistExportedMo(
            ManagedObject mo) throws RSuiteException {
        return additionalTransformationList;
    }
    
}
