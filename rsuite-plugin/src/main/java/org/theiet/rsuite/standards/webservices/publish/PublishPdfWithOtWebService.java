package org.theiet.rsuite.standards.webservices.publish;

import static com.rsicms.projectshelper.publish.workflow.constant.PublishWorkflowVariables.*;
import static org.theiet.rsuite.standards.datatype.StandardsNonXmlVariants.MAIN_WEB;
import static org.theiet.rsuite.standards.domain.publish.datatype.StandardsPublishWorkflowVariables.GENERATOR_ID;
import static org.theiet.rsuite.standards.domain.publish.datatype.StandardsPublishWorkflowVariables.*;
import static org.theiet.rsuite.standards.domain.publish.generators.OutputGeneratorType.DITA_2_PDF;

import java.util.HashMap;
import java.util.Map;

import org.theiet.rsuite.standards.StandardsBooksConstans;
import org.theiet.rsuite.standards.datatype.StandardsPdfTranstypeProvider;
import org.theiet.rsuite.standards.domain.book.StandardsBookEdition;

import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.extensions.ExecutionContext;
import com.reallysi.rsuite.api.remoteapi.CallArgumentList;
import com.rsicms.projectshelper.workflow.RSuiteWorkflowVariable;


public class PublishPdfWithOtWebService extends StartPublishWorkflowWebService
		implements StandardsBooksConstans {

	@Override
	protected Map<RSuiteWorkflowVariable, Object> getAdditionalVarialbes(ExecutionContext context, StandardsBookEdition bookEdition, String moId, CallArgumentList args) throws RSuiteException {
		
		String transtypeValue = bookEdition.getLmd().getPdfTranstype();
		
		Map<RSuiteWorkflowVariable, Object> variables = new HashMap<RSuiteWorkflowVariable, Object>();
		variables.put(GENERATOR_ID , DITA_2_PDF.toString());
		variables.put(TRANSTYPE, transtypeValue);
		variables.put(NON_XML_MO_VARIANT, MAIN_WEB.getVariantName());

		StandardsPdfTranstypeProvider transtype = StandardsPdfTranstypeProvider.getStandardsPdfTranstypeProvider(transtypeValue);
		
		
		String changeTracking = args.getFirstString("changeTracking");
		if ("true".equalsIgnoreCase(changeTracking)){
			boolean changeTrackingFlag = transtype == StandardsPdfTranstypeProvider.REG_2_PDF_WREG ? false : true;			
			variables.put(GENERATE_CHANGE_TRACKING, String.valueOf(changeTrackingFlag));
		}
		
		return variables;
	}

}
