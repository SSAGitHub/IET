package org.theiet.rsuite.journals.domain.issues.publish.finalartilces.notification;

import com.rsicms.projectshelper.publish.notification.GeneratedOutputNotifier;
import com.rsicms.projectshelper.publish.workflow.configuration.SendNotificationConfiguration;

public class IssueFinalArticleSendNotificationConfiguration implements
		SendNotificationConfiguration {

	@Override
	public GeneratedOutputNotifier createNotifier() {
		return new IssueFinalArticleGeneratedNotifier();
	}

}
