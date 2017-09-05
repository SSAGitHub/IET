package org.theiet.rsuite.journals.actionhandlers;

import static com.rsicms.projectshelper.utils.ProjectContentAssemblyUtils.*;

import java.io.*;
import java.util.List;

import javax.xml.transform.*;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.*;

import org.apache.commons.io.FilenameUtils;
import org.theiet.rsuite.IetConstants;
import org.theiet.rsuite.journals.JournalConstants;
import org.theiet.rsuite.journals.utils.JournalWorkflowUtils;
import org.theiet.rsuite.utils.*;
import org.xml.sax.*;
import org.xml.sax.helpers.XMLReaderFactory;

import com.reallysi.rsuite.api.*;
import com.reallysi.rsuite.api.workflow.WorkflowExecutionContext;
import com.reallysi.rsuite.service.XmlApiManager;
import com.rsicms.projectshelper.workflow.actionhandlers.AbstractActionHandler;
import com.rsicms.rsuite.helpers.upload.*;

public class InspecIngestion extends AbstractActionHandler implements
		JournalConstants {

	private static final String EXTRACT_ERR_MSG = "Unable to extract inspec classication metada";
	private static final long serialVersionUID = 1L;

	@Override
	public void executeTask (WorkflowExecutionContext context) throws Exception {

		String pId = context.getProcessInstanceId();
		context.setVariable("pId", pId);
		User user = getSystemUser();
		
		String zipFileName = context.getVariable("rsuiteSourceFilePath");
		String articleId = FilenameUtils.getBaseName(zipFileName);

		String articleCaId = JournalWorkflowUtils.getArticleCaId(context, pId,
				articleId);
		ContentAssembly articleCa = getCAFromMO(context, user, articleCaId);
		String journalCode = articleCa.getLayeredMetadataValue(LMD_FIELD_JOURNAL_CODE);
		context.setVariable(WF_VAR_JOURNAL_ID, journalCode);
		context.setVariable(WF_VAR_PRODUCT, WF_VAR_ARTICLE);

		File rootFolder = context.getFileWorkflowObject().getFile();

		File[] files = rootFolder.listFiles();
		if (files.length != 1) {
			ExceptionUtils.throwWorfklowException(context,
					"The package file should include only a one XML file");
		}

		File xmlFile = files[0];

		File temp = new File(rootFolder, "temp");
		temp.mkdirs();
		File inspecFile = new File(temp, articleCaId + "_inspec.xml");

		extractInspecFromDocument(context, xmlFile, inspecFile);

		

		String typesetterCaId = obtainTypesetterCaId(context, user, articleCaId);

		if (typesetterCaId == null) {
			ExceptionUtils
					.throwWorfklowException(context,
							"Unable to locate typesetter content assembly in the article CA");
		}

		RSuiteFileLoadOptions options = new RSuiteFileLoadOptions(
				getSystemUser());
		options.setDuplicateFilenamePolicy(DuplicateFilenamePolicy.UPDATE);

		ContentAssembly typesetterCA = getCAFromMO(context, getSystemUser(), typesetterCaId);
		
		
		ManagedObject inspecMo = obtainInspecMo(context, typesetterCA);
		
		if (inspecMo != null){
			RSuiteFileLoadHelper.updateMoFromFile(context, inspecFile, typesetterCA, inspecMo, options);
		}else{
			RSuiteFileLoadHelper.loadFileAsNewMo(context, inspecFile, typesetterCA, options);
		}

		boolean hasLoadErrors = IetUtils.getFileLoadResult(
				context.getWorkflowLog(), options);
		if (hasLoadErrors) {

			ExceptionUtils.throwWorfklowException(context,
					"Load had errors");
		}
		else {
			PubtrackLogger.logToProcess(user, 
				context, 
				context.getWorkflowLog(), 
				"ARTICLE",
				articleId,
				PUBTRACK_INSPEC_CLASSIFICATION_RECEIVED);
		}

	}

	private ManagedObject obtainInspecMo(WorkflowExecutionContext context,
			ContentAssembly typesetterCA) throws RSuiteException {
		ManagedObject inspecMo = null;
		
		List<? extends ContentAssemblyItem> children = typesetterCA.getChildrenObjects();
		for (ContentAssemblyItem caItem : children){
			
			//caItem.getLayeredMetadataValue(name)
			
			ManagedObject mo = null;
			
			if (caItem.getObjectType() == ObjectType.MANAGED_OBJECT_REF){
				mo = context.getManagedObjectService().getManagedObject(getSystemUser(), ((ManagedObjectReference)caItem).getTargetId());
				
			}else if (caItem.getObjectType() == ObjectType.MANAGED_OBJECT){
				mo = (ManagedObject) caItem;
			}
			
			if (mo != null && "article-meta".equals(mo.getLocalName())){
				inspecMo =mo;
			}
			
			
		}
		return inspecMo;
	}

	/**
	 * Obtains a type setter content assembly id
	 * 
	 * @param context
	 *            execution context
	 * @param user
	 *            the user
	 * @param articleCaId
	 *            the article CA id
	 * @return
	 * @throws RSuiteException
	 */
	private String obtainTypesetterCaId(WorkflowExecutionContext context,
			User user, String articleCaId) throws RSuiteException {
		
		ContentAssembly articleCa = getCAFromMO(context, user,
				articleCaId);

		List<? extends ContentAssemblyItem> children = articleCa
				.getChildrenObjects();

		String typesetterCaId = null;

		for (ContentAssemblyItem caItem : children) {

			String type = caItem.getType();
			String id = caItem.getId();

			if (caItem.getObjectType() == ObjectType.CONTENT_ASSEMBLY_REF) {
				ContentAssembly ca = getCAFromMO(context, user,
						((ContentAssemblyReference) caItem).getTargetId());
				type = ca.getType();
				id = ca.getId();
			}

			if (IetConstants.CA_TYPE_TYPESETTER.equals(type)) {
				typesetterCaId = id;
				break;
			}
		}
		return typesetterCaId;
	}

	/**
	 * Extract inspec classification metadata
	 * 
	 * @param context
	 *            - the execution context
	 * @param xmlFile
	 *            - NLM document
	 * @return
	 * @throws RSuiteException
	 *             if extracting process fails.
	 */
	private void extractInspecFromDocument(WorkflowExecutionContext context,
			File xmlFile, File inspecFile) throws Exception {

		if (!inspecFile.exists()) {
			inspecFile.createNewFile();
		}

		FileOutputStream baos = new FileOutputStream(inspecFile);

		try {
			ClassLoader classLoader = this.getClass().getClassLoader();
			InputStream is = classLoader
					.getResourceAsStream("WebContent/xslt/inspec/extractInspecClassificationMetadata.xsl");

			TransformerFactory factory = context.getXmlApiManager()
					.getTransformerFactory();

			Source source = new StreamSource(is);

			Transformer transformer = factory.newTransformer(source);

			XmlApiManager xmlApi = context.getXmlApiManager();

			XMLReader reader = XMLReaderFactory.createXMLReader();

			reader.setEntityResolver(xmlApi.getRSuiteAwareEntityResolver());

			SAXSource xmlSource = new SAXSource(reader, new InputSource(
					new FileReader(xmlFile)));

			Result result = new StreamResult(baos);

			transformer.transform(xmlSource, result);
		} catch (SAXException e) {
			ExceptionUtils.throwWorfklowException(context, e,
					EXTRACT_ERR_MSG);
		} catch (TransformerConfigurationException e) {
			ExceptionUtils.throwWorfklowException(context, e,
					EXTRACT_ERR_MSG);
		} catch (FileNotFoundException e) {
			ExceptionUtils.throwWorfklowException(context, e,
					EXTRACT_ERR_MSG);
		} catch (TransformerException e) {
			ExceptionUtils.throwWorfklowException(context, e,
					EXTRACT_ERR_MSG);
		} finally {
			try {
				baos.close();
			} catch (Exception e) {
			}
		}

	}

}
