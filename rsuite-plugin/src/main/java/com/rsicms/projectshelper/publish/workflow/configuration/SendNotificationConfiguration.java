package com.rsicms.projectshelper.publish.workflow.configuration;

import com.rsicms.projectshelper.publish.notification.GeneratedOutputNotifier;

public interface SendNotificationConfiguration {

    GeneratedOutputNotifier createNotifier();
}
