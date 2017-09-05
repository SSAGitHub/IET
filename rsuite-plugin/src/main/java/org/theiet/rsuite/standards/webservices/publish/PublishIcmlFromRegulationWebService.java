package org.theiet.rsuite.standards.webservices.publish;


import static com.rsicms.projectshelper.publish.workflow.constant.PublishWorkflowVariables.NON_XML_MO_VARIANT;
import static org.theiet.rsuite.standards.datatype.StandardsNonXmlVariants.MASTER;
import static org.theiet.rsuite.standards.domain.publish.datatype.StandardsPublishWorkflowVariables.GENERATE_CHANGE_TRACKING;
import static org.theiet.rsuite.standards.domain.publish.datatype.StandardsPublishWorkflowVariables.GENERATOR_ID;
import static org.theiet.rsuite.standards.domain.publish.datatype.StandardsPublishWorkflowVariables.ICML_XSLT_URI;
import static org.theiet.rsuite.standards.domain.publish.datatype.StandardsPublishWorkflowVariables.MATHML_FONT;
import static org.theiet.rsuite.standards.domain.publish.datatype.StandardsPublishWorkflowVariables.MATHML_SIZE;
import static org.theiet.rsuite.standards.domain.publish.generators.OutputGeneratorType.XML_2_ICML;

import java.util.HashMap;
import java.util.Map;

import org.theiet.rsuite.standards.StandardsBooksConstans;
import org.theiet.rsuite.standards.constans.PublishWorkflowContans;
import org.theiet.rsuite.standards.domain.book.StandardsBookEdition;
import org.theiet.rsuite.standards.domain.book.StandardsBookUtils;

import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.extensions.ExecutionContext;
import com.reallysi.rsuite.api.remoteapi.CallArgumentList;
import com.rsicms.projectshelper.workflow.RSuiteWorkflowVariable;

public class PublishIcmlFromRegulationWebService extends
		StartPublishWorkflowWebService implements PublishWorkflowContans,
		 StandardsBooksConstans {

	@Override
	protected Map<RSuiteWorkflowVariable, Object> getAdditionalVarialbes(ExecutionContext context, StandardsBookEdition bookEdition, String moId, CallArgumentList args) throws RSuiteException {
		Map<RSuiteWorkflowVariable, Object> variables = new HashMap<RSuiteWorkflowVariable, Object>();
		variables.put(GENERATOR_ID, XML_2_ICML.toString());
		variables.put(NON_XML_MO_VARIANT, MASTER.getVariantName());
		variables.put(ICML_XSLT_URI, bookEdition.getLmd().getIcmlXslt());
		variables.put(MATHML_FONT, StandardsBookUtils.getStandardsBookLMD(context, moId, LMD_FIELD_MATHML_FONT));
		variables.put(MATHML_SIZE, WF_VAR_VALUE_DEFAULT_MATHML_SIZE);
		variables.put(GENERATE_CHANGE_TRACKING, String.valueOf(true));

		return variables;
	}

}
