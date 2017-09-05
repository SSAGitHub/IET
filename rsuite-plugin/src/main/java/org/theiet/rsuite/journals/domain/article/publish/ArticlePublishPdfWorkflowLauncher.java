package org.theiet.rsuite.journals.domain.article.publish;

import static org.theiet.rsuite.journals.domain.article.publish.datatype.ArticlePublishType.*;
import static org.theiet.rsuite.journals.domain.article.publish.datatype.ArticlePublishWorkflowVariables.*;

import java.util.*;

import org.theiet.rsuite.datamodel.types.IetWorkflows;
import org.theiet.rsuite.journals.domain.article.Article;
import org.theiet.rsuite.journals.domain.article.publish.datatype.ArticlePublishType;
import org.theiet.rsuite.journals.domain.article.publish.export.ArticleContentExporterConfiguration;
import org.theiet.rsuite.journals.domain.article.publish.generation.ArticleOutputGeneratorConfiguration;
import org.theiet.rsuite.journals.domain.article.publish.upload.ArticleGeneratedOutputUploaderConfiguration;

import com.reallysi.rsuite.api.*;
import com.reallysi.rsuite.api.extensions.ExecutionContext;
import com.reallysi.rsuite.api.workflow.ProcessInstanceSummaryInfo;
import com.rsicms.projectshelper.publish.workflow.launcher.PublishWorkflowLauncher;
import com.rsicms.projectshelper.workflow.RSuiteWorkflowVariable;

public final class ArticlePublishPdfWorkflowLauncher {

    private ArticlePublishPdfWorkflowLauncher(){}
    
    public static ProcessInstanceSummaryInfo launchProofPublishPdfWorkflowForArticle(ExecutionContext context, User user, Article article) throws RSuiteException{
        Map<RSuiteWorkflowVariable, Object> workflowVariables = createInitialVariables(PROOF);
        return launchPublishPdfWorkflowForArticle(context, user, article, workflowVariables);
    }
    
    public static ProcessInstanceSummaryInfo launchFinalPublishPdfWorkflowForArticle(ExecutionContext context, User user, Article article) throws RSuiteException{
        Map<RSuiteWorkflowVariable, Object> workflowVariables = createInitialVariables(FINAL);
        return launchPublishPdfWorkflowForArticle(context, user, article, workflowVariables);
    }
    
    private static Map<RSuiteWorkflowVariable, Object> createInitialVariables(ArticlePublishType publishType){
        Map<RSuiteWorkflowVariable, Object> workflowVariables = new HashMap<RSuiteWorkflowVariable, Object>();
        workflowVariables.put(PUBLISH_TYPE, publishType.toString());
        
        return workflowVariables;
    }
    
    private static ProcessInstanceSummaryInfo launchPublishPdfWorkflowForArticle(ExecutionContext context, User user, Article article, Map<RSuiteWorkflowVariable, Object> workflowVariables) throws RSuiteException{

        workflowVariables.put(ARTICLE_CA_ID, article.getArticleCaId());
    
        PublishWorkflowLauncher.Builder builder = new PublishWorkflowLauncher.Builder(context, user);
        builder.addWorkflowVariables(workflowVariables);
        builder.workflowName(IetWorkflows.IET_PUBLISH_WORKFLOW.getWorkflowName());
        builder.addContextMo(article.getXMLMo());
        builder.exportConfiguration(ArticleContentExporterConfiguration.class);
        builder.outputGeneratorConfiguration(ArticleOutputGeneratorConfiguration.class);
        builder.generatedOutputUploaderConfiguration(ArticleGeneratedOutputUploaderConfiguration.class);
        PublishWorkflowLauncher workflowLauncher = builder.build();
        return workflowLauncher.launchPublishWorkflow();
    }
    
    
}
