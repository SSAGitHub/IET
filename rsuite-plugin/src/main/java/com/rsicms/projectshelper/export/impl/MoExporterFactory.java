package com.rsicms.projectshelper.export.impl;

import java.util.ArrayList;

import com.reallysi.rsuite.api.*;
import com.reallysi.rsuite.api.extensions.ExecutionContext;
import com.rsicms.projectshelper.export.*;
import com.rsicms.projectshelper.export.impl.configuration.DefaultMoExporterConfigurationImpl;
import com.rsicms.projectshelper.export.impl.refence.MoExporterImpl;

public final class MoExporterFactory {

    private MoExporterFactory(){};
    
    public static MoExporter createMoExporter(ExecutionContext context, User user, MoExportConfiguration moExporterConfiguration){
        return new MoExporterImpl(context, user, moExporterConfiguration);
    }
    
    public static MoExporter createSimpleMoExporter(ExecutionContext context, User user) throws RSuiteException{
        return new MoExporterImpl(context, user, new DefaultMoExporterConfigurationImpl(new ArrayList<String>()));
    }
}
