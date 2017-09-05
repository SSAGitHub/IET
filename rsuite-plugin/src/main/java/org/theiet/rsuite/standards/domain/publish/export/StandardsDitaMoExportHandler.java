package org.theiet.rsuite.standards.domain.publish.export;


import java.util.ArrayList;
import java.util.List;

import javax.xml.transform.Templates;
import javax.xml.transform.TransformerFactory;

import com.reallysi.rsuite.api.ManagedObject;
import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.extensions.ExecutionContext;
import com.rsicms.projectshelper.export.MoExportAdditionalTransformation;
import com.rsicms.projectshelper.export.MoExportHandlerContext;
import com.rsicms.projectshelper.export.impl.handlers.DitaMoExportHandler;
import com.rsicms.projectshelper.utils.ProjectTransformationUtils;

public class StandardsDitaMoExportHandler extends DitaMoExportHandler {

	private String XSLT_URI_O2_PIS_TO_DITA = "rsuite:/res/plugin/iet/xslt/o2pi2dita-cm/O2_PIs_2_DITA-CM.xsl";
	
	private Templates transformTemplate;
	
	private List<MoExportAdditionalTransformation> additionalTransformation = new ArrayList<>();

	boolean changeTracking = false;
	
	public StandardsDitaMoExportHandler(String generateChangeTrackingVariable) {

    	if ("true".equalsIgnoreCase(generateChangeTrackingVariable)){
    		changeTracking = Boolean.parseBoolean(generateChangeTrackingVariable);
    	}
	}

	@Override
	public void initialize(MoExportHandlerContext moExportHandlerContext) throws RSuiteException {
		super.initialize(moExportHandlerContext);
		ExecutionContext context  = moExportHandlerContext.getContext();
		TransformerFactory transformerFactory = context.getXmlApiManager().getTransformerFactory();
		transformTemplate = ProjectTransformationUtils.createTransformTemplate(moExportHandlerContext.getContext(), transformerFactory, XSLT_URI_O2_PIS_TO_DITA);
	}

	@Override
	public List<MoExportAdditionalTransformation> getTransformationToPerformBeforePersistExportedMo(ManagedObject mo)
			throws RSuiteException {
	
		if (changeTracking){
			additionalTransformation.clear();
			additionalTransformation.add(new MoExportAdditionalTransformation(transformTemplate));
		}
		
		return additionalTransformation; 
	}

	protected Templates createDitaChangeTrackingTemplate(ExecutionContext context,
			TransformerFactory transformerFactory) throws RSuiteException {

		String xsltURI = XSLT_URI_O2_PIS_TO_DITA;

		Templates ditaTemplate = ProjectTransformationUtils.createTransformTemplate(context, transformerFactory,
				xsltURI);

		return ditaTemplate;
	}
}
