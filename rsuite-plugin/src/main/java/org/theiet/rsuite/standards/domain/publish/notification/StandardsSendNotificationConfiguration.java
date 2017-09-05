package org.theiet.rsuite.standards.domain.publish.notification;

import com.rsicms.projectshelper.publish.notification.GeneratedOutputNotifier;
import com.rsicms.projectshelper.publish.workflow.configuration.SendNotificationConfiguration;

public class StandardsSendNotificationConfiguration implements SendNotificationConfiguration {

    @Override
    public GeneratedOutputNotifier createNotifier() {
        return new StandardsPublishNotifier();
    }

}
