package org.theiet.rsuite.iettv.actionhandlers.inspec;

import static org.theiet.rsuite.iettv.actionhandlers.IetTvWorkflowVariables.*;

import java.io.File;

import org.apache.commons.io.FilenameUtils;
import org.theiet.rsuite.iettv.domain.datatype.VideoRecord;
import org.theiet.rsuite.iettv.domain.inspec.VideoInspecLoader;
import org.theiet.rsuite.iettv.domain.search.IetTvFinder;
import org.theiet.rsuite.utils.ActionHandlerUtils;

import com.reallysi.rsuite.api.ContentAssembly;
import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.workflow.*;
import com.rsicms.projectshelper.workflow.WorkflowVariables;
import com.rsicms.projectshelper.workflow.actionhandlers.AbstractNonLeavingActionHandler;

public class LoadVideoInspecActionHandler extends AbstractNonLeavingActionHandler {
    
    private static final long serialVersionUID = 1L;

    @Override
    public void executeTask(WorkflowExecutionContext executionContext) throws Exception {

        WorkflowJobContext workflowJobContext = executionContext.getWorkflowJobContext();
        setUpWorkflowVariables(executionContext);
        
        String packageName = getWorkflowIngestionPackageName(workflowJobContext);

        File packageFolder = ActionHandlerUtils.unzipIngestionPackageAndCreatPackgeTempFolder(workflowJobContext);

        VideoInspecLoader inspecLoader =
                new VideoInspecLoader(executionContext, getSystemUser(), packageName);
        inspecLoader.loadVideoInspec(packageFolder);
        
        VideoRecord videoRecord = inspecLoader.getVideoRecord();
        executionContext.setVariable(WorkflowVariables.RSUITE_CONTENTS.getVariableName(), videoRecord.getVideoRecordContainer().getId());
        
        executionContext.setVariable(VIDEO_ID.getVariableName(), videoRecord.getVideoId());
    }

    private void setUpWorkflowVariables(WorkflowExecutionContext executionContext) throws RSuiteException {
        ContentAssembly ietTvDomainCa = IetTvFinder.findMainDomainContainer(executionContext, getSystemUser());
        
        if (ietTvDomainCa == null){
            throw new RSuiteException("Iet tv domain does not exist");
        }
        executionContext.setVariable(WorkflowVariables.RSUITE_CONTENTS.getVariableName(), ietTvDomainCa.getId());        
    }
    
    private String getWorkflowIngestionPackageName(WorkflowJobContext workflowJobContext)
            throws RSuiteException {
        String sourceFilePath = workflowJobContext.getSourceFilePath();

        return FilenameUtils.getName(sourceFilePath);

    }

}
