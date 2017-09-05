package com.rsicms.projectshelper.export.impl.refence;

import static com.rsicms.projectshelper.export.impl.MoExportHelper.*;
import static com.rsicms.projectshelper.utils.ProjectMoUtils.*;

import java.io.*;
import java.util.*;
import java.util.concurrent.*;

import com.reallysi.rsuite.api.*;
import com.reallysi.rsuite.api.extensions.ExecutionContext;
import com.rsicms.projectshelper.datatype.ManagedObjectWrapper;
import com.rsicms.projectshelper.export.*;
import com.rsicms.projectshelper.export.impl.MoExportHandlerContextImpl;
import com.rsicms.projectshelper.export.impl.cache.*;
import com.rsicms.projectshelper.export.impl.exportercontext.MoExportContext;
import com.rsicms.projectshelper.export.impl.subexporter.*;
import com.rsicms.projectshelper.export.impl.utils.MoExporterPathUtils;
import com.rsicms.projectshelper.utils.ProjectMoUtils;

public class MoExporterImpl implements MoExporter {

    private ConcurrentLinkedQueue<ManagedObjectWrapper> mosToProcess;

    private Set<String> mosProcessed;

    private Map<String, String> exportPathCache;

    private ReferenceTargetValueCache referenceTargetValuesCache;

    private ExecutionContext context;

    private User user;

    private MoExportContext exportContext;

    private MoExportConfiguration moExporterConfiguration;

    private MoSubExporter assebmlySubExporter;

    private MoSubExporter nonXMLSubExporter;
    
    private MoSubExporter xmlSubExporter;

    public MoExporterImpl(ExecutionContext context, User user,
            MoExportConfiguration moExporterConfiguration) {

        this.moExporterConfiguration = moExporterConfiguration;
        this.context = context;
        this.user = user;

        intializeFields();
    }

    private void intializeFields() {
        exportContext = new MoExportContext(moExporterConfiguration);
        mosToProcess = new ConcurrentLinkedQueue<ManagedObjectWrapper>();
        mosProcessed = new ConcurrentSkipListSet<String>();
        exportPathCache = new HashMap<String, String>();
        referenceTargetValuesCache = new ReferenceTargetValueCache();        
    }

    @Override
    public MoExportResult exportMo(ManagedObject mo, File outputFolder) throws RSuiteException {
        return exportMo(null, mo, outputFolder);
    }
    
    @Override
    public MoExportResult exportMo(MoExportContainerContext exportContainerContext,
            List<ManagedObject> moToExportList, File outputFolder) throws RSuiteException {
        
        intializeBeforeExport(exportContainerContext, moToExportList, outputFolder);
        
        List<ManagedObject> exportedMainMoList = new ArrayList<>();
        
        for (ManagedObject mo : moToExportList){
            ManagedObject mainMoToExport =
                    getMoToExportBasedOnTheContext(context, user, exportContainerContext, mo);
            
            MoExportHandler exportHandler = exportContext.getExportHandler();
            if (exportHandler.exportMo(mainMoToExport)){
            	mosToProcess.add(new ManagedObjectWrapper(mainMoToExport));
            	exportedMainMoList.add(mainMoToExport);
            }
        }

        ManagedObjectWrapper moWrapperToProcess = null;
        while ((moWrapperToProcess = mosToProcess.poll()) != null) {

            ManagedObject moToProcess = moWrapperToProcess.getMo();

            if (mosProcessed.contains(moToProcess.getId())) {
                continue;
            }

            processMo(moToProcess);

            mosProcessed.add(moToProcess.getId());

        }

        exportContext.getExportHandler().afterExport(outputFolder);
        return createExportResult(exportedMainMoList, outputFolder);
    }

    private MoExportResult createExportResult(List<ManagedObject> moToExportList,
            File outputFolder) {
        
        MoExportResult moExportResult = new MoExportResult(exportContext.getMessageHandler());
        for (ManagedObject mo : moToExportList){
            File exportedFile = new File(outputFolder, exportPathCache.get(mo.getId()));

            moExportResult.addExportMapping(mo, exportedFile.getAbsolutePath());
        }
        return moExportResult;
    }

    @Override
    public MoExportResult exportMo(MoExportContainerContext exportContainerContext,
            ManagedObject mo, File outputFolder) throws RSuiteException {

        List<ManagedObject> moToExportList = new ArrayList<ManagedObject>();
        moToExportList.add(mo);
        
        return exportMo(exportContainerContext, moToExportList, outputFolder);
    }

    protected void intializeBeforeExport(MoExportContainerContext exportContainerContext,
            List<ManagedObject> topExportedMo, File outputFolder) throws RSuiteException {

        intializeFields();
        exportContext.setExportContainerContext(exportContainerContext);        
        MoExportHandler exportHandler = exportContext.getExportHandler();
        MoExportHandlerContext handlerContext =
                new MoExportHandlerContextImpl(context, user, topExportedMo, outputFolder,
                        exportContext);
        exportHandler.initialize(handlerContext);
        initializeSubExporters(outputFolder);
    }

    private void initializeSubExporters(File outputFolder) {
        assebmlySubExporter = SubExporterFactory.createAssemblyMoSubExporter(context, user, exportContext, outputFolder);
        nonXMLSubExporter = SubExporterFactory.createNonXMLMoSubExporter(exportContext, outputFolder);
        xmlSubExporter = SubExporterFactory.createXMLSubExporter(context, user, exportContext, outputFolder);
    }

    private Map<String, String> createWorkingExportPathCache() {
        Map<String, String> workingExportPathCache = new HashMap<String, String>();
        workingExportPathCache.putAll(exportPathCache);
        return workingExportPathCache;
    }

    private ReferenceTargetValueCache createWorkingReferenceTargetValuesCache() {
        ReferenceTargetValueCache workingExportPathCache = new ReferenceTargetValueCache();
        workingExportPathCache.addCache(referenceTargetValuesCache);
        return workingExportPathCache;
    }

    private void processMo(ManagedObject moToExport) throws RSuiteException {

        MoExportHandler exportHandler = exportContext.getExportHandler();

        try {
            if (exportHandler.exportMo(moToExport)) {

                ProcessResult result = exportMo(moToExport);

                mosToProcess.addAll(result.getMoToProcess());
                exportPathCache.putAll(result.getExportPathMap());
                referenceTargetValuesCache.addCache(result.getReferenceTargetValuesCache());
            }
        } catch (IOException | RSuiteException e) {
            throw new RSuiteException(0, "Problem with exporting mo: " + moToExport.getId(), e);
        }
    }

    private ProcessResult exportMo(ManagedObject moToExport) throws RSuiteException, IOException {

        MoExportCache moExportCache =
                new MoExportCache(createWorkingExportPathCache(),
                        createWorkingReferenceTargetValuesCache());

        Map<String, String> exportPathMap = moExportCache.getExportPathCache();
        String exportPath =
                MoExporterPathUtils.getExportPathForMo(exportContext, moToExport, exportPathMap);

        exportContext.getMessageHandler().info("Exporting mo id " + getIdWithRevision(moToExport));

        SubExporterResult subExportResult = null;

        if (ProjectMoUtils.isContainerMo(moToExport)) {
            subExportResult = exportAssemblyMo(moExportCache, moToExport, exportPath);
        } else if (ProjectMoUtils.isXMLMo(moToExport)) {
            subExportResult = exportXMLMo(moExportCache, moToExport, exportPath);
        } else if (moToExport.isNonXml()) {
            subExportResult = exportNonXmlMo(moExportCache, moToExport, exportPath);
        } else {
            throw new RSuiteException("Unsupported managed object type");
        }

        moExportCache.addExportPaths(subExportResult.getExportPathMap());

        return new ProcessResult(subExportResult.getMoToProcess(), moExportCache);
    }


    protected SubExporterResult exportXMLMo(MoExportCache moExportCache, ManagedObject moToExport,
            String exportPath) throws RSuiteException {
        return xmlSubExporter.exportMo(moExportCache, moToExport, exportPath);
    }


    protected SubExporterResult exportAssemblyMo(MoExportCache moExportCache,
            ManagedObject moToExport, String exportPath) throws RSuiteException {
        return assebmlySubExporter.exportMo(moExportCache, moToExport, exportPath);
    }

    protected SubExporterResult exportNonXmlMo(MoExportCache moExportCache,
            ManagedObject moToExport, String exportPath) throws RSuiteException {
        return nonXMLSubExporter.exportMo(moExportCache, moToExport, exportPath);
    }
  
}
