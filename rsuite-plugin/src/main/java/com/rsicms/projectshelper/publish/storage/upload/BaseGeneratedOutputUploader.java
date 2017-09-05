package com.rsicms.projectshelper.publish.storage.upload;

import static com.rsicms.projectshelper.lmd.value.LmdUtils.createMetadataItem;
import static com.rsicms.projectshelper.publish.storage.datatype.OutputCaTypes.OUTPUT_EVENT;
import static com.rsicms.projectshelper.publish.storage.datatype.OutputLmd.HAS_OUTPUT;
import static com.rsicms.projectshelper.publish.storage.datatype.OutputLmd.SOURCE_MO_ID;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.logging.Log;

import com.reallysi.rsuite.api.Alias;
import com.reallysi.rsuite.api.ContentAssembly;
import com.reallysi.rsuite.api.ManagedObject;
import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.User;
import com.reallysi.rsuite.api.extensions.ExecutionContext;
import com.reallysi.rsuite.service.ManagedObjectService;
import com.rsicms.projectshelper.datatype.RSuiteCaType;
import com.rsicms.projectshelper.lmd.value.YesNoLmdValue;
import com.rsicms.projectshelper.publish.datatype.BaseUploadGeneratedOutputsResult;
import com.rsicms.projectshelper.publish.datatype.OutputGenerationResult;
import com.rsicms.projectshelper.publish.datatype.UploadGeneratedOutputsResult;
import com.rsicms.projectshelper.publish.storage.datatype.OutputCaTypes;
import com.rsicms.projectshelper.upload.UploadUtils;
import com.rsicms.projectshelper.utils.ProjectContentAssemblyUtils;
import com.rsicms.projectshelper.utils.browse.ProjectBrowserUtils;

public abstract class BaseGeneratedOutputUploader implements GeneratedOutputUploader {

    private static final String CA_NAME_HISTORY = "History";

    private ExecutionContext context;

    private User user;

    private ManagedObjectService moSvc;

    private String workflowProcessId;
    
    private Log logger;
        
    
    public BaseGeneratedOutputUploader(ExecutionContext context, Log logger, String workflowProcessId) {
        this.context = context;
        this.workflowProcessId = workflowProcessId;
        user = context.getAuthorizationService().getSystemUser();
        moSvc = context.getManagedObjectService();
        this.logger = logger;
    }
    
    @Override    
    public UploadGeneratedOutputsResult uploadGeneratedOutputs(
            OutputGenerationResult outputGenerationResult) throws RSuiteException {
        Map<String, String> sourceMoIdMap2outputMoId = new HashMap<String, String>();

        ContentAssembly outputsCa = getOutputsCa();
        for (Entry<String, File> entry : outputGenerationResult.getMoOutPuts().entrySet()) {
            ContentAssembly outputCa =
                    prepareOutputCAforNewFileUpload(outputsCa, entry.getKey(), entry.getValue());
            ManagedObject mo = uploadOutputForMo(outputCa, entry, outputGenerationResult);
            sourceMoIdMap2outputMoId.put(entry.getKey(), mo.getId());
            setLmdForSourceMo(entry.getKey());
        }

        return new BaseUploadGeneratedOutputsResult(sourceMoIdMap2outputMoId);
    }

    protected abstract ContentAssembly getOutputsCa() throws RSuiteException;

    private ContentAssembly prepareOutputCAforNewFileUpload(ContentAssembly outputsCa, String moId,
            File outputFile) throws RSuiteException {

        ContentAssembly outputCa = getOutputCA(outputsCa, moId);
        String outputFileName = outputFile.getName();

        ContentAssembly historyCa = getHistoryCA(outputCa);
        moveOldPublicationToHistoryCA(historyCa, outputFileName, outputCa);

        return outputCa;
    }

    private void moveOldPublicationToHistoryCA(ContentAssembly publishHistory, String moAlias,
            ContentAssembly outputCa) throws RSuiteException {

        List<ManagedObject> moList =
                context.getManagedObjectService().getObjectsByAlias(user, moAlias);
        if (moList != null) {
            for (ManagedObject managedObject : moList) {
                String moParentId =
                        ProjectContentAssemblyUtils.getPaterntId(context, user,
                                managedObject.getId());
                if (outputCa.getId().equals(moParentId)) {
                    ProjectContentAssemblyUtils.moveRefToFistPossition(context,
                            managedObject.getId(), publishHistory.getId());
                }
            }
        }
    }

    private ContentAssembly getHistoryCA(ContentAssembly outputCa) throws RSuiteException {
        ContentAssembly publishHistory =
                ProjectBrowserUtils.getChildCaByType(context, outputCa, getHistoryCaType());

        if (publishHistory == null) {
            publishHistory =
                    ProjectContentAssemblyUtils.createContentAssembly(context, outputCa.getId(),
                            CA_NAME_HISTORY, getHistoryCaType());
        }

        return publishHistory;
    }

    private ManagedObject uploadOutputForMo(ContentAssembly outputCa, Entry<String, File> entry,
            OutputGenerationResult publishedOutputsSummarizer) throws RSuiteException {
        File file = entry.getValue();
        
        ManagedObject publicationMo = UploadUtils.uploadOutputFile(context, outputCa, file, false);

        GeneratedOutputMetadata outputMetadata = new GeneratedOutputMetadata(context, user);
        
        outputMetadata.setVersionableOutputMetadata(workflowProcessId, publicationMo, publishedOutputsSummarizer);
        
        ProjectContentAssemblyUtils.moveRefToFistPossition(context, publicationMo.getId(),
                outputCa.getId());

        return publicationMo;
    }

    private void setLmdForSourceMo(String moId) throws RSuiteException {

        ManagedObject mo = moSvc.getManagedObject(user, moId);
        if (mo.getTargetId() != null) {
            mo = moSvc.getManagedObject(user, mo.getTargetId());
        }


        moSvc.setMetaDataEntry(user, mo.getId(),
                createMetadataItem(HAS_OUTPUT, YesNoLmdValue.YES.getValue()));
    }

    public ContentAssembly getOutputCA(ContentAssembly outputs, final String moId)
            throws RSuiteException {

        String syncObject = "rsuite_" + moId;

        ManagedObjectService moSvc = context.getManagedObjectService();
        String outputContainerAlias = "output" + moId;

        ManagedObject outputMo = moSvc.getObjectByAlias(user, outputContainerAlias);

        ContentAssembly outputCa = null;
        if (outputMo != null) {
            outputCa =
                    context.getContentAssemblyService().getContentAssembly(user, outputMo.getId());
        } else {
            synchronized (syncObject.intern()) {
                outputCa = createOutputContainer(outputs, moId, moSvc, outputContainerAlias);
            }
        }

        return outputCa;
    }

    private ContentAssembly createOutputContainer(ContentAssembly outputs, final String moId,
            ManagedObjectService moSvc, String outputContainerAlias) throws RSuiteException {

        ManagedObject outputMo = moSvc.getObjectByAlias(user, outputContainerAlias);

        if (outputMo != null) {
            return context.getContentAssemblyService().getContentAssembly(user, outputMo.getId());
        }

        ManagedObject mo = moSvc.getManagedObject(user, moId);
        String outputCAName = getOutputCAName(mo);

        ContentAssembly ca =
                ProjectContentAssemblyUtils.createContentAssembly(context, outputs.getId(),
                        outputCAName, OUTPUT_EVENT);

        moSvc.setMetaDataEntry(user, ca.getId(), createMetadataItem(SOURCE_MO_ID, moId));

        moSvc.setAlias(user, ca.getId(), new Alias(outputContainerAlias));

        return ca;
    }

    protected abstract String getOutputCAName(ManagedObject mo) throws RSuiteException;

    protected RSuiteCaType getHistoryCaType() {
        return OutputCaTypes.OUTPUT_HISTORY;
    }    

    @Override
    public void beforeUpload(OutputGenerationResult ouputGenerationResult)
    		throws RSuiteException {
    }

    @Override
    public void afterUpload(UploadGeneratedOutputsResult uploadResult,
    		OutputGenerationResult ouputGenerationResult)
    		throws RSuiteException {
    }
}
