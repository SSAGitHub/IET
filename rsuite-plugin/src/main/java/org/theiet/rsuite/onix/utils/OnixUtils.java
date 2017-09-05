package org.theiet.rsuite.onix.utils;

import org.theiet.rsuite.onix.domain.OnixConfiguration;
import org.theiet.rsuite.onix.domain.OnixProcessor;
import org.theiet.rsuite.onix.domain.OnixRecipientConfiguration;

import com.reallysi.rsuite.api.extensions.ExecutionContext;

public class OnixUtils {

	public static OnixProcessor createProcessor(
			ExecutionContext context,
			OnixConfiguration onixConfiguration, String recipientName) {
		OnixRecipientConfiguration recipientConfiguration = onixConfiguration
					.getRecipientConfiguration(recipientName);
		
		return new OnixProcessor(context, recipientConfiguration);
	}
	
}
