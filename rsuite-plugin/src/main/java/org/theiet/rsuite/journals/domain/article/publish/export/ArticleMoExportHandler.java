package org.theiet.rsuite.journals.domain.article.publish.export;

import java.io.File;

import com.reallysi.rsuite.api.*;
import com.reallysi.rsuite.api.extensions.ExecutionContext;
import com.rsicms.projectshelper.export.*;
import com.rsicms.projectshelper.export.impl.MoExporterFactory;
import com.rsicms.projectshelper.export.impl.handlers.JatsMoExportHandler;

public class ArticleMoExportHandler extends JatsMoExportHandler {

    private ExecutionContext context;
    
    private User user;
    
    @Override
    public void initialize(MoExportHandlerContext moExportHandlerContext) throws RSuiteException {
        super.initialize(moExportHandlerContext);
        context = moExportHandlerContext.getContext();
        user = moExportHandlerContext.getUser();
    }
    
    @Override
    public void afterExport(File outputFolder) throws RSuiteException {
        super.afterExport(outputFolder);        
        exportStaticContent(outputFolder);
    }

    private void exportStaticContent(File outputFolder) throws RSuiteException {
        ManagedObject staticContainer = findArticlePdfStaticContainer();
        MoExporter defaultMoExporter = MoExporterFactory.createSimpleMoExporter(context, user);
        defaultMoExporter.exportMo(staticContainer, outputFolder);
    }

    private ManagedObject findArticlePdfStaticContainer() throws RSuiteException {

        String path = "/Journals/Admin/Publish/Pdf/static";
        ContentAssemblyItem item = context.getContentAssemblyService().findObjectForPath(user, path);
        if (item == null){
            throw new RSuiteException("Unable to find container " + path);            
        }
                
        return context.getManagedObjectService().getManagedObject(user, item.getId());
    }
}
