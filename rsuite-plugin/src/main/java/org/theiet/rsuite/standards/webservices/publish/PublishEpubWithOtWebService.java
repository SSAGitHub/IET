package org.theiet.rsuite.standards.webservices.publish;

import static org.theiet.rsuite.standards.domain.publish.datatype.StandardsPublishWorkflowVariables.*;
import static org.theiet.rsuite.standards.domain.publish.generators.OutputGeneratorType.*;

import java.util.*;

import org.theiet.rsuite.standards.domain.book.StandardsBookEdition;

import com.reallysi.rsuite.api.extensions.ExecutionContext;
import com.reallysi.rsuite.api.remoteapi.CallArgumentList;
import com.rsicms.projectshelper.workflow.RSuiteWorkflowVariable;

/**
 * Custom RSuite web service to save search results to user's clipboard
 * 
 */
public class PublishEpubWithOtWebService extends StartPublishWorkflowWebService
		 {

	@Override
	protected Map<RSuiteWorkflowVariable, Object> getAdditionalVarialbes(ExecutionContext context, StandardsBookEdition bookEdition, String moId, CallArgumentList args) {
		Map<RSuiteWorkflowVariable, Object> variables = new HashMap<RSuiteWorkflowVariable, Object>();
//TODO check dita variables
		variables.put(GENERATOR_ID, DITA_2_PDF.toString());
		variables.put(TRANSTYPE, "epub");
		return variables;
	}

}
