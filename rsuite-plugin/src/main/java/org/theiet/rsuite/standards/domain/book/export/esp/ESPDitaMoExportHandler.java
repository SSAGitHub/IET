package org.theiet.rsuite.standards.domain.book.export.esp;

import static org.theiet.rsuite.standards.StandardsBooksConstans.XSLT_URI_O2_PIS_TO_DITA;

import java.io.File;
import java.util.*;

import javax.xml.transform.Templates;
import javax.xml.transform.TransformerFactory;

import org.apache.commons.io.FilenameUtils;
import org.theiet.rsuite.standards.domain.book.index.IndexFileToKeywordsConventer;

import com.reallysi.rsuite.api.ManagedObject;
import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.extensions.ExecutionContext;
import com.rsicms.projectshelper.export.*;
import com.rsicms.projectshelper.export.impl.handlers.DitaMoExportHandler;
import com.rsicms.projectshelper.utils.ProjectTransformationUtils;

public class ESPDitaMoExportHandler extends DitaMoExportHandler {

    private ExecutionContext context;

    private List<MoExportAdditionalTransformation> additionalDitaTransformationList;

    private ESPMoExportHandlerHelper moExportHelper;

    @Override
    public void initialize(MoExportHandlerContext moExportHandlerContext) throws RSuiteException {

        super.initialize(moExportHandlerContext);

        context = moExportHandlerContext.getContext();

        TransformerFactory transformerFactory = context.getXmlApiManager().getTransformerFactory();
        additionalDitaTransformationList =
                createAdditionalTransformationForDitaFiles(transformerFactory);

        moExportHelper = new ESPMoExportHandlerHelper(moExportHandlerContext);
    }


    @Override
    public String getMoExportPath(ManagedObject mo, String exportUri) throws RSuiteException {

        String exportPath = super.getMoExportPath(mo, exportUri);

        exportPath =  moExportHelper.createMoExportPath(mo, exportUri, exportPath);

        return exportPath;
    }


    @Override
    public String getLinkValue(ManagedObject referencedMo, String linkValue) throws RSuiteException {

        if (referencedMo.isNonXml()) {
            FilenameUtils.getBaseName(linkValue);
        }

        return linkValue;
    }

    @Override
    public List<MoExportAdditionalTransformation> getTransformationToPerformBeforePersistExportedMo(
            ManagedObject mo) throws RSuiteException {

        return additionalDitaTransformationList;
    }

    protected List<MoExportAdditionalTransformation> createAdditionalTransformationForDitaFiles(
            TransformerFactory transformerFactory) throws RSuiteException {

        String xsltURI = XSLT_URI_O2_PIS_TO_DITA;

        Templates ditaTemplate =
                ProjectTransformationUtils.createTransformTemplate(context, transformerFactory,
                        xsltURI);

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("rsuite.public.id", "");
        
        List<MoExportAdditionalTransformation> additionalTransformationList =
                new ArrayList<MoExportAdditionalTransformation>();
        
        
        additionalTransformationList.add(new MoExportAdditionalTransformation(ditaTemplate, parameters));

        return additionalTransformationList;
    }
    
    @Override
    public void afterExport(File outputFolder) throws RSuiteException {
        IndexFileToKeywordsConventer conventer = new IndexFileToKeywordsConventer();
        conventer.convertIndexFileToKeywords(outputFolder);
    }
}
