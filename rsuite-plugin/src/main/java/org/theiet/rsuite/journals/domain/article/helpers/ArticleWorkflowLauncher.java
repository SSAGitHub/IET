package org.theiet.rsuite.journals.domain.article.helpers;

import static com.rsicms.projectshelper.workflow.WorkflowVariables.*;
import static org.theiet.rsuite.journals.domain.constants.JournalWorkflowVariables.*;

import java.util.*;

import org.theiet.rsuite.journals.domain.article.Article;

import com.reallysi.rsuite.api.*;
import com.reallysi.rsuite.api.extensions.ExecutionContext;
import com.reallysi.rsuite.service.ProcessInstanceService;
import com.rsicms.projectshelper.workflow.RSuiteWorkflowVariable;

public final class ArticleWorkflowLauncher {

    private static final String WF_PREPARE_ARTICLE = "IET Prepare Article";
    
    private ArticleWorkflowLauncher(){};
    
    public static void startPrepareArticle(ExecutionContext context, User user, Article article) throws RSuiteException{
        
        ProcessInstanceService piSvc = context.getProcessInstanceService();
        
        Map<String, Object> wfVarMap = new HashMap<String, Object>();
        addVariable(wfVarMap, WF_VAR_ARTICLE_ID, article.getArticleId());
        addVariable(wfVarMap, WF_VAR_PRODUCT_ID, article.getArticleId());
        addVariable(wfVarMap, RSUITE_CONTENTS, article.getArticleCaId());
        addVariable(wfVarMap, WF_VAR_JOURNAL_ID, article.getJounralCode());
        
        piSvc.createAndStart(user, WF_PREPARE_ARTICLE, wfVarMap);
    }
    
    private static void addVariable(Map<String, Object> wfVarMap, RSuiteWorkflowVariable variable, String value){
        wfVarMap.put(variable.getVariableName(), value);
    }
}
