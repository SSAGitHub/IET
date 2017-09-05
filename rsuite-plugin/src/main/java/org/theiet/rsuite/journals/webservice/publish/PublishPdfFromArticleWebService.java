package org.theiet.rsuite.journals.webservice.publish;

import static com.rsicms.projectshelper.workflow.WorkflowVariables.*;
import static org.theiet.rsuite.journals.domain.article.publish.datatype.ArticlePublishType.*;
import static org.theiet.rsuite.journals.domain.constants.JournalsCaTypes.*;

import java.util.*;

import org.apache.commons.lang.StringUtils;
import org.theiet.rsuite.journals.domain.article.Article;
import org.theiet.rsuite.journals.domain.article.publish.ArticlePublishPdfWorkflowLauncher;
import org.theiet.rsuite.journals.domain.article.publish.datatype.ArticlePublishType;

import com.reallysi.rsuite.api.*;
import com.reallysi.rsuite.api.remoteapi.*;
import com.rsicms.projectshelper.utils.browse.ProjectBrowserUtils;
import com.rsicms.projectshelper.webservice.ProjectRemoteApiHandler;
import com.rsicms.projectshelper.workflow.RSuiteWorkflowVariable;

public class PublishPdfFromArticleWebService extends ProjectRemoteApiHandler {

    private static final String PARAM_TYPE = "type";
    private static final String WEB_SERVICE_LABEL = "Start Publish Workflow";

    @Override
    protected String getDialogTitle() {
        return WEB_SERVICE_LABEL;
    }

    @Override
    protected String exectuteAction(RemoteApiExecutionContext context, CallArgumentList args)
            throws RSuiteException {


        ArticlePublishType publishType = getPublishTypeFromWsParameters(args);

        Map<RSuiteWorkflowVariable, Object> variables =
                new HashMap<RSuiteWorkflowVariable, Object>();
        setUpCommonVariables(context, args, variables);


        User user = context.getSession().getUser();

        String rsuitePath = (String) variables.get(RSUITE_PATH);
        ContentAssembly articleCa =
                ProjectBrowserUtils.getAncestorCAbyType(context, user, rsuitePath, ARTICLE);

        Article article = new Article(context, user, articleCa);
        
        if (publishType == PROOF) {
            ArticlePublishPdfWorkflowLauncher.launchProofPublishPdfWorkflowForArticle(context,
                    user, article);
        } else if (publishType == FINAL) {
            ArticlePublishPdfWorkflowLauncher.launchFinalPublishPdfWorkflowForArticle(context,
                    user, article);
        }

        return "Publish process has been started.";
    }

    private ArticlePublishType getPublishTypeFromWsParameters(CallArgumentList args)
            throws RSuiteException {
        String publishTypeParameter = args.getFirstString(PARAM_TYPE);

        if (StringUtils.isEmpty(publishTypeParameter)) {
            throw new RSuiteException("Missing argument " + PARAM_TYPE);
        }

        publishTypeParameter = publishTypeParameter.toUpperCase();

        ArticlePublishType publishType = ArticlePublishType.valueOf(publishTypeParameter);
        return publishType;
    }

    private void setUpCommonVariables(RemoteApiExecutionContext context, CallArgumentList args,
            Map<RSuiteWorkflowVariable, Object> variables) throws RSuiteException {
        setUpRSuitePathIfNotPresentInWSArguments(context, args, variables);
    }

    private void setUpRSuitePathIfNotPresentInWSArguments(RemoteApiExecutionContext context,
            CallArgumentList args, Map<RSuiteWorkflowVariable, Object> variables)
            throws RSuiteException {
        String rsuitePath = args.getFirstString("rsuiteBrowseUri");
        if (rsuitePath == null) {
            rsuitePath = ProjectBrowserUtils.getBrowserUri(context, args.getFirstValue("sourceId"));
        }

        variables.put(RSUITE_PATH, rsuitePath);
    }

}
