package org.theiet.rsuite.standards.domain.publish.upload;

import static com.rsicms.projectshelper.workflow.WorkflowVariables.*;
import static org.theiet.rsuite.standards.datatype.StandardsCaTypes.*;

import org.theiet.rsuite.standards.domain.book.StandardsBookEdition;

import com.reallysi.rsuite.api.*;
import com.reallysi.rsuite.api.extensions.ExecutionContext;
import com.reallysi.rsuite.api.workflow.WorkflowExecutionContext;
import com.rsicms.projectshelper.publish.storage.upload.GeneratedOutputUploader;
import com.rsicms.projectshelper.publish.workflow.configuration.GeneratedOutputUploaderConfiguration;
import com.rsicms.projectshelper.utils.browse.ProjectBrowserUtils;

public class StandardsGeneratedOutputUploaderConfiguration implements
        GeneratedOutputUploaderConfiguration {

    @Override
    public GeneratedOutputUploader getGeneratedOutputUploader(WorkflowExecutionContext context,
            User user) throws RSuiteException {
 
        String workflowProcessId = context.getProcessInstanceId();
        
        String rsuitePath = (String)context.getWorkflowVariables().get(RSUITE_PATH.getVariableName());
        
        StandardsBookEdition bookEdition = getBookEdition(context, user, rsuitePath);
        
        return new StandardsGeneratedOutputUploader(context, context.getWorkflowLog(), workflowProcessId, bookEdition);
    }
    
    private StandardsBookEdition getBookEdition(ExecutionContext context, User user, String rsuitePath)
            throws RSuiteException {
        ContentAssembly bookEditionCa =
                getOutpusEditionCA(context, user, rsuitePath);
        return new StandardsBookEdition(context, bookEditionCa);
    }
    
    private ContentAssembly getOutpusEditionCA(ExecutionContext context, User user, String rsuitePath)
            throws RSuiteException {
        return ProjectBrowserUtils.getAncestorCAbyType(context, user, rsuitePath, STANDARDS_BOOK_EDITION);
    }

}
