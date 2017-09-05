package org.theiet.rsuite.standards.domain.publish.export;

import static com.rsicms.projectshelper.workflow.WorkflowVariables.RSUITE_PATH;
import static org.theiet.rsuite.standards.StandardsBooksConstans.CA_TYPE_STANDARDS_BOOK_EDITION;
import static org.theiet.rsuite.standards.domain.book.export.BookEditionExportHelper.createMoExportContainerContext;

import org.apache.commons.lang.StringUtils;
import org.theiet.rsuite.standards.domain.book.StandardsBookEdition;
import org.theiet.rsuite.standards.domain.publish.datatype.StandardsPublishWorkflowVariables;

import com.reallysi.rsuite.api.*;
import com.reallysi.rsuite.api.workflow.WorkflowExecutionContext;
import com.rsicms.projectshelper.export.MoExportConfiguration;
import com.rsicms.projectshelper.export.MoExportContainerContext;
import com.rsicms.projectshelper.publish.workflow.configuration.ContentExporterConfiguration;
import com.rsicms.projectshelper.utils.browse.ProjectBrowserUtils;

public class StandardsDitaContentExporterConfiguration implements ContentExporterConfiguration {

    @Override
    public MoExportConfiguration getMoExporterConfiguration(WorkflowExecutionContext context,
            User user) throws RSuiteException {
    	
    	String generateChangeTrackingVariable = context.getVariable(StandardsPublishWorkflowVariables.GENERATE_CHANGE_TRACKING.getVariableName());
    	StandardsDitaMoExportHandler moExportHandler = new StandardsDitaMoExportHandler(generateChangeTrackingVariable);
    	
        return new StandardsDitaMoExporterConfigurationImpl(moExportHandler);
    }

    @Override
    public MoExportContainerContext getMoExportContainerContext(WorkflowExecutionContext context,
            User user) throws RSuiteException {
            
        String rsuitePath = context.getVariable(RSUITE_PATH.getVariableName());
        
        if (StringUtils.isNotBlank(rsuitePath)){
             ContentAssemblyItem bookEditionCaItem = ProjectBrowserUtils.getAncestorCAbyType(context, rsuitePath, CA_TYPE_STANDARDS_BOOK_EDITION);
             StandardsBookEdition standardsBookEdition = new StandardsBookEdition(context, bookEditionCaItem.getId());                       
             return createMoExportContainerContext(context, user, standardsBookEdition); 
        }
        
        
        return null;
    }

}
