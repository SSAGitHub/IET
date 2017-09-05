package com.rsicms.projectshelper.export.impl.subexporter;

import static com.rsicms.projectshelper.export.impl.subexporter.SubExporterHelper.*;

import java.io.File;
import java.util.*;

import com.reallysi.rsuite.api.*;
import com.reallysi.rsuite.api.extensions.ExecutionContext;
import com.reallysi.rsuite.service.*;
import com.rsicms.projectshelper.datatype.ManagedObjectWrapper;
import com.rsicms.projectshelper.export.impl.cache.MoExportCache;
import com.rsicms.projectshelper.export.impl.exportercontext.MoExportContext;
import com.rsicms.projectshelper.export.impl.utils.MoExporterPathUtils;

public class AssemblyMoSubExporter implements MoSubExporter {

    private File outputFolder;

    private ContentAssemblyService caService;

    private ManagedObjectService moService;

    private User user;

    private MoExportContext exportContext;
    
    public AssemblyMoSubExporter(ExecutionContext context, User user, MoExportContext exportContext, File outputFolder){
        this.outputFolder = outputFolder;
        caService = context.getContentAssemblyService();
        moService = context.getManagedObjectService();
        this.exportContext = exportContext;
        this.user = user;
    }
    
    @Override
    public SubExporterResult exportMo(MoExportCache moExportCache, ManagedObject moToExport,
            String exportPath) throws RSuiteException {
        createFolderForContainer(exportPath);
        return getChildMosToExport(moToExport, exportPath);
    }

    private SubExporterResult getChildMosToExport(ManagedObject mo, String exportPath) throws RSuiteException {
        ContentAssemblyNodeContainer nodeContainer =
                caService.getContentAssemblyNodeContainer(user, getRealMoId(mo));
        Set<ManagedObjectWrapper> moToExportSet = new LinkedHashSet<ManagedObjectWrapper>();
        Map<String, String> exportPathMap = new HashMap<String, String>();
        
        List<? extends ContentAssemblyItem> childrenObjects = nodeContainer.getChildrenObjects();
        for (ContentAssemblyItem caItem : childrenObjects) {
            
            String moId =  caItem.getId();
            
            if (caItem instanceof ObjectReference){
                ObjectReference moRef = (ObjectReference)caItem;
                moId = moRef.getTargetId();
            }
            
            ManagedObject childMo = moService.getManagedObject(user, moId);
            
            MoExporterPathUtils.getExportPathForMo(exportContext, childMo, exportPath + "/", exportPathMap);
            
            moToExportSet.add(new ManagedObjectWrapper(childMo));
            
        }

        return new SubExporterResult(moToExportSet, exportPathMap);
    }

    private void createFolderForContainer(String exportPath) {
        File folder = new File(outputFolder, exportPath);
        folder.mkdirs();
    }


}
