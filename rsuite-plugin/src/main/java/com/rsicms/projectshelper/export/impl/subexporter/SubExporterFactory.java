package com.rsicms.projectshelper.export.impl.subexporter;

import java.io.File;

import com.reallysi.rsuite.api.User;
import com.reallysi.rsuite.api.extensions.ExecutionContext;
import com.rsicms.projectshelper.export.impl.exportercontext.MoExportContext;

public final class SubExporterFactory {

    private SubExporterFactory(){};
    
    public static MoSubExporter createAssemblyMoSubExporter(ExecutionContext context, User user, MoExportContext exportContext, File outputFolder){
        return new AssemblyMoSubExporter(context, user, exportContext, outputFolder);
    }
    
    public static MoSubExporter createNonXMLMoSubExporter(MoExportContext exportContext, File outputFolder){        
        return new NonXMLMoSubExporter(exportContext, outputFolder);
    }    

    public static MoSubExporter createXMLSubExporter(ExecutionContext context, User user, MoExportContext exportContext, File outputFolder){        
        return new XMLMoSubExporter(context, user, exportContext, outputFolder);
    }
}
