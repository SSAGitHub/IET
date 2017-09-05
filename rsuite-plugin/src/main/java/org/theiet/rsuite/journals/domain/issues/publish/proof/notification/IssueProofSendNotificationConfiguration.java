package org.theiet.rsuite.journals.domain.issues.publish.proof.notification;

import com.rsicms.projectshelper.publish.notification.GeneratedOutputNotifier;
import com.rsicms.projectshelper.publish.workflow.configuration.SendNotificationConfiguration;

public class IssueProofSendNotificationConfiguration implements
		SendNotificationConfiguration {

	@Override
	public GeneratedOutputNotifier createNotifier() {
		return new IssueProofGeneratedNotifier();
	}

}
