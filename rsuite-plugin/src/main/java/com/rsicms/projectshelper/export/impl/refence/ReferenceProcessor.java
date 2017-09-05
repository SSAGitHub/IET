package com.rsicms.projectshelper.export.impl.refence;

import static com.rsicms.projectshelper.utils.ProjectMoUtils.*;
import static com.rsicms.projectshelper.export.impl.MoExportHelper.getMoToExportBasedOnTheContext;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.sf.saxon.om.NodeInfo;

import com.reallysi.rsuite.api.ManagedObject;
import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.User;
import com.reallysi.rsuite.api.extensions.ExecutionContext;
import com.rsicms.projectshelper.datatype.ManagedObjectWrapper;
import com.rsicms.projectshelper.export.LinkValueToMoMapper;
import com.rsicms.projectshelper.export.MoExportCrossReferenceValidator;
import com.rsicms.projectshelper.export.MoExportHandler;
import com.rsicms.projectshelper.export.impl.cache.*;
import com.rsicms.projectshelper.export.impl.exportercontext.MoExportContext;
import com.rsicms.projectshelper.export.impl.utils.MoExporterPathUtils;
import com.rsicms.projectshelper.message.ProcessingMessageHandler;
import com.rsicms.projectshelper.utils.ProjectFileUtils;

public class ReferenceProcessor {

	private ProcessingMessageHandler messageHandler;

	private MoExportHandler exportHandler;

	private LinkValueToMoMapper linkToMoMapper;
	
	private MoExportContext exportContext;

	private Set<ManagedObjectWrapper> moToExport = new HashSet<ManagedObjectWrapper>();

	private Map<String, String> exportPathCache;
	
	private ReferenceTargetValueCache referenceTargetValuesCache;
	
	private MoExportCrossReferenceValidator crossRefenceValidator;
	
	private ExecutionContext context;
	
	private User user;
	
	public ReferenceProcessor(ExecutionContext context, User user, MoExportContext exportContext,
			MoExportCache moExportCache) {

	    this.context = context;
	    this.user = user;
		this.messageHandler = exportContext.getMessageHandler();
		this.exportHandler = exportContext.getExportHandler();
		this.linkToMoMapper = exportHandler.createLinkValueToMoMapper();
		this.exportPathCache = moExportCache.getExportPathCache();
		this.exportContext = exportContext;
		this.referenceTargetValuesCache = moExportCache.getReferenceTargetValuesCache();
		this.crossRefenceValidator = exportHandler.createMoExportCrossRefernceValidator();
	}

	public String handleRefernce(String contextMoId, NodeInfo attribute) {

		String newLinkValue = "";
		String linkValue = "";

		try {

			if (attribute == null) {
				throw new RSuiteException("The refence must be an attribute");
			}

			linkValue = attribute.getStringValue();
			newLinkValue = getDefaultValueForMissingLink(linkValue);

			ManagedObject referncedMo = getReferencedMo(contextMoId, attribute);

			if (referncedMo != null && exportHandler.processRefenceToMo(contextMoId, attribute,
							referncedMo)) {

				moToExport.add(new ManagedObjectWrapper(referncedMo));

				newLinkValue = createRefenceAttributeValue(contextMoId,
						linkValue, referncedMo);
				
				
				if (crossRefenceValidator != null &&  crossRefenceValidator.isCrossReference(newLinkValue)){
					validateCrossReference(contextMoId, linkValue, referncedMo, newLinkValue);
				}
				
				
			}
		} catch (Exception e) {
			String errorMessage = "Problem with processing refrence in mo "
					+ contextMoId + " link " + linkValue + " line "
					+ attribute.getLineNumber();
			messageHandler.error(errorMessage, e);

		}
		return newLinkValue;
	}

	private void validateCrossReference(String contextMoId, String originalLink, ManagedObject referncedMo, String attributeValue
			) throws RSuiteException {
		String referencedMoId = referncedMo.getId();
		Set<String> targetValues = referenceTargetValuesCache.getTargetValuesForMo(referencedMoId);
		
		if (targetValues == null){
			targetValues =  crossRefenceValidator.parseReferenceTargets(referncedMo);
			referenceTargetValuesCache.cacheTargetValuesForMo(referencedMoId, targetValues);
		}
		
		String targetValue = crossRefenceValidator.getTargetValueFromLink(attributeValue);
		
		if (!targetValues.contains(targetValue)){
			messageHandler.error("Broken cross refernce " + originalLink + " context moId: " + contextMoId + " referenced moId:" + getIdWithRevision(referncedMo) + " exported path "  + attributeValue);
		}		
	}

	protected String createRefenceAttributeValue(String contextMoId,
			String linkValue, ManagedObject referncedMo) throws RSuiteException {
		String newAttributeValue;
		String contextPath = exportPathCache.get(contextMoId);

		int hashIndex = linkValue.indexOf("#");

		newAttributeValue = computeExportPath(referncedMo, contextPath);

		if (hashIndex > -1) {
			newAttributeValue += linkValue.substring(hashIndex);
		}
		return exportHandler.getLinkValue(referncedMo, newAttributeValue);
	}

	protected String getDefaultValueForMissingLink(String linkValue) {
		return exportHandler.getDefaultValueForMissingLink(linkValue);
	}

	public ManagedObject getReferencedMo(String contextMoId, NodeInfo attribute)
			throws RSuiteException {
		ManagedObject referncedMo = linkToMoMapper.mapLinkToMo(messageHandler, contextMoId,
				attribute);

		String linkValue = attribute.getStringValue();
		
		if (referncedMo == null) {
			messageHandler.error("There is no mo for " + linkValue
					+ " contex mo id " + contextMoId, contextMoId);
		}else{
		    referncedMo = getMoToExportBasedOnTheContext(context, user, exportContext.getExportContainerContext(), referncedMo);   
		}
				
				
		return referncedMo;
	}

	public String computeExportPath(ManagedObject referencedMo, String contextPath)
			throws RSuiteException {

		String newAttributeValue = "FAILED_TO_COMPUTE_PATH MO " + referencedMo.getId();

		String exportPath = MoExporterPathUtils.getExportPathForMo(exportContext, referencedMo,
				exportPathCache);
		newAttributeValue = ProjectFileUtils.computeRelativePath(contextPath,
				exportPath);

		return newAttributeValue;
	}

	public Set<ManagedObjectWrapper> getMoToExport() {
		return moToExport;
	}

	public Map<String, String> getExportPathMap() {
		return exportPathCache;
	}
}
