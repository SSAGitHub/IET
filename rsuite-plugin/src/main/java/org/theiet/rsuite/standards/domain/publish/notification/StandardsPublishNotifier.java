package org.theiet.rsuite.standards.domain.publish.notification;

import java.util.Map;

import org.theiet.rsuite.domain.mail.IetMailUtils;

import com.reallysi.rsuite.api.*;
import com.reallysi.rsuite.api.workflow.WorkflowExecutionContext;
import com.rsicms.projectshelper.publish.datatype.OutputGenerationResult;
import com.rsicms.projectshelper.publish.datatype.UploadGeneratedOutputsResult;
import com.rsicms.projectshelper.publish.notification.EmailGeneratedOutputNotifier;

public class StandardsPublishNotifier extends EmailGeneratedOutputNotifier {

    private String PROP_STANDARDS_BOOKS_SEND_PUBLISHING_MAIL_TITLE = "iet.standards.books.send.publishing.mail.title";
    private String PROP_STANDARDS_BOOKS_SEND_PUBLISHING_MAIL_BODY = "iet.standards.books.send.publishing.mail.body";
    
    @Override
    public String getEmailFrom(WorkflowExecutionContext context, User user) throws RSuiteException {
        return IetMailUtils.obtainEmailFrom();
    }

    @Override
    public Map<String, String> getEmailVariables(WorkflowExecutionContext context, User user,  OutputGenerationResult generationResult, UploadGeneratedOutputsResult uploadResult)
            throws RSuiteException {
        return StandardsMailUtils.setUpVariablesMap(context, user);
    }

    @Override
    public String getEmailSubjectProperty() {
        return PROP_STANDARDS_BOOKS_SEND_PUBLISHING_MAIL_TITLE;
    }

    @Override
    public String getMessageTemplateProperty() {
        return PROP_STANDARDS_BOOKS_SEND_PUBLISHING_MAIL_BODY;
    }

}
