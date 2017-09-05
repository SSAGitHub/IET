package org.theiet.rsuite.standards.actionhandlers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;

import javax.xml.transform.Transformer;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.logging.Log;
import org.w3c.dom.Document;

import com.reallysi.rsuite.api.ContentAssembly;
import com.reallysi.rsuite.api.ContentAssemblyItem;
import com.reallysi.rsuite.api.ContentAssemblyNodeContainer;
import com.reallysi.rsuite.api.ManagedObject;
import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.workflow.FileWorkflowObject;
import com.reallysi.rsuite.api.workflow.MoListWorkflowObject;
import com.reallysi.rsuite.api.workflow.MoWorkflowObject;
import com.reallysi.rsuite.api.workflow.WorkflowExecutionContext;
import com.reallysi.rsuite.api.xml.DitaOpenToolkit;
import com.reallysi.rsuite.workflow.actions.RSuiteDitaSupportActionHandlerBase;
import com.reallysi.tools.dita.BosConstructionOptions;
import com.reallysi.tools.dita.BosMember;
import com.reallysi.tools.dita.BosMemberValidationException;
import com.reallysi.tools.dita.BosVisitor;
import com.reallysi.tools.dita.BoundedObjectSet;
import com.reallysi.tools.dita.BrowseTreeConstructingBosVisitor;
import com.reallysi.tools.dita.DitaMapImportOptions;
import com.reallysi.tools.dita.DitaUtil;
import com.reallysi.tools.dita.DomException;
import com.reallysi.tools.dita.DomUtil;
import com.reallysi.tools.dita.PluginVersionConstants;
import com.reallysi.tools.dita.RSuiteDitaHelper;

/**
 * Takes one or more DITA map files and imports each map and its dependencies to
 * RSuite.
 */
public class MapImporterActionHandler extends
		RSuiteDitaSupportActionHandlerBase {

	private static final String VALIDATION_XSLT_URI = "rsuite:/res/plugin/rsuite-dita-support/validation-report/validation-report.xsl";

	/**
	 * 
	 */
	public static final String MAP_CA_NODE_NAME_PARAM = "mapCaNodeName";

	/**
	 * Path of the root map to process. Use if map roots are not in
	 * FileWorkflowObject.
	 */
	public static final String ROOT_MAP_PATH_PARAM = "rootMapPath";

	/**
	 * Filename of the map. Will be looked for in the working directory.
	 */
	public static final String MAP_FILENAME_PARAM = "mapFileName";

	/**
	 * The absolute path of the browse tree folder into which the map is
	 * imported. e.g. "/maps/mystuff"
	 */
	public static final String PARENT_BROWSE_TREE_FOLDER_PARAM = "parentBrowseTreeFolder";

	/**
	 * The path of the content assembly or node within the parent folder into
	 * which the map is imported, e.g. "books/reference" to create CA "books",
	 * CA Node "reference".
	 */
	public static final String PARENT_CA_NODE_PARAM = "parentCaNode";

	/**
	 * Optional. URI of the graphic to use for missing graphics.
	 */
	public static final String MISSING_GRAPHIC_URI_PARAM = "missingGraphicUri";

	/**
	 * (Optional) Name of topic container name to use.
	 * <p>
	 * If not specified, "<tt>content</tt>" will be used.
	 * </p>
	 */
	public static final String TOPIC_CONTAINER_NAME_PARAM = "topicContainerName";

	/**
	 * (Optional) Name of non-xml container name to use.
	 * <p>
	 * If not specified, "<tt>media</tt>" will be used.
	 * </p>
	 */
	public static final String NONXML_CONTAINER_NAME_PARAM = "nonXmlContainerName";

	/**
	 * (Optional) name of the variable to hold the MO ID of the imported root
	 * map. Default is "mapMoId".
	 */
	public static final String MAP_MO_ID_VAR_NAME_PARAM = "mapMoIdVarName";

	/**
	 * (Optional) name of the variable to hold the content assembly ID of the CA
	 * that is the direct parent of the imported map.
	 */
	public static final String MAP_CA_ID_VAR_NAME_PARAM = "mapCaIdVarName";

	/**
	 * Specifies the message to use for any commits to the repository. Default
	 * value is "Automatic update".
	 */
	public static final String COMMIT_MESSAGE_PARAM = "commitMessage";

	private static final long serialVersionUID = 1L;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.reallysi.rsuite.api.workflow.AbstractBaseNonLeavingActionHandler#
	 * execute(com.reallysi.rsuite.api.workflow.WorkflowExecutionContext)
	 */
	@Override
	public void execute(WorkflowExecutionContext context) throws Exception {

		ArrayList<MoWorkflowObject> importedMaps = new ArrayList<MoWorkflowObject>();
		BosMember rootMember = null; // Will hold the BOS member for the root
		// map

		Log wfLog = context.getWorkflowLog();
		wfLog.info("Using rsuite-dita-support version "
				+ PluginVersionConstants.getVersion() + ".");

		this.checkParamsNotEmptyOrNull(PARENT_BROWSE_TREE_FOLDER_PARAM,
				PARENT_CA_NODE_PARAM);

		String mapCaNodeNameParam = resolveVariablesAndExpressions(context,
				getParameter(MAP_CA_NODE_NAME_PARAM));

		File mapFile = findDitaMapFile(context, wfLog);

		StringBuilder validationReport = new StringBuilder(
				"Map import validation report.\n\n").append(
				" + [INFO] Root map document: " + mapFile.getAbsolutePath())
				.append("\n\n");

		BosConstructionOptions domOptions = createBOSoptions(context,
				validationReport);

		try {

			Document doc = getDomFromMapFile(wfLog, mapFile, domOptions);

			String mapCaNodeName = FilenameUtils.getBaseName(mapFile.getName());
			if (mapCaNodeNameParam != null && !"".equals(mapCaNodeNameParam)) {
				mapCaNodeName = mapCaNodeNameParam;
			}
			
			ContentAssembly rootCA = getRootImportContainer(context,
					mapCaNodeName, mapFile);

			DitaMapImportOptions importOptions = createImportOptions(context,
					mapCaNodeName, rootCA);

			BoundedObjectSet bos = importMap(context, doc, domOptions,
					importOptions);

			if (bos.hasInvalidMembers()) {
				throw new RSuiteException("Map has invalid members.");
			}

			BosVisitor visitor = new BrowseTreeConstructingBosVisitor(context,
					getSystemUser(), importOptions, wfLog);
			// The MO in this context is the CA into which the map is loaded.
			importOptions.setCreateCaNodeForRootMap(false); // Create root map
															// in specified CA
															// node (e.g.,
															// "xml").
			ManagedObject ca = RSuiteDitaHelper.loadMapToBrowseTree(context,
					bos, importOptions, visitor);

			wfLog.info("Map imported");

			rootMember = bos.getRoot();
			if (rootMember == null) {
				throw new RSuiteException(
						"Failed to get root member for BOS following apparently-successful BOS import. This should not happen.");

			}

			importedMaps.add(new MoWorkflowObject(ca));
		} catch (Exception e) {
			String errorMessage = "Failed File: [" + mapFile.getName()
					+ "]\r\n" + e + "\r\n\r\n";

			context.setVariable(EXCEPTION_MESSAGE_VAR, errorMessage);
			createWorkflowComment(context, "RSuite",
					DEFAULT_WORKFLOW_COMMENT_STREAM_NAME, errorMessage);

			wfLog.error(e.getLocalizedMessage(), e);

			captureValidationReport(context, mapFile, validationReport);			
		}

		if (!isPreserveWorkflowContext(context))
			context.setMoListWorkflowObject(new MoListWorkflowObject(
					importedMaps));

		wfLog.info("Maps imported successfully");

		setUpWorkflowVariables(context, importedMaps, rootMember);

		wfLog.info("Done");
	}

	private Document getDomFromMapFile(Log wfLog, File mapFile,
			BosConstructionOptions domOptions) throws DomException,
			FileNotFoundException, BosMemberValidationException,
			RSuiteException {
		wfLog.info("Parsing map file \"" + mapFile.getAbsolutePath()
				+ "\"...");
		Document doc = DomUtil.getDomForDocument(mapFile, domOptions, true);

		if (!DitaUtil.isDitaMap(doc.getDocumentElement())) {
			throw new RSuiteException("File " + mapFile.getName()
					+ " does not appear to be a DITA map, skipping.");
		}
		return doc;
	}

	private ContentAssembly getRootImportContainer(
			WorkflowExecutionContext context, String mapCaNodeName, File mapFile)
			throws RSuiteException {
		
		FileWorkflowObject fileWF = context.getFileWorkflowObject();
		
		String fileWFPath = fileWF.getFile().getAbsolutePath();
		String mapPath = mapFile.getAbsolutePath();
		
		String mapContextPath = mapPath.replace(fileWFPath, "");
		
		
		ContentAssemblyItem parentObject = context
				.getContentAssemblyService().findObjectForPath(
						getSystemUser(), "/Import/Imported Maps");

		ContentAssembly rootCA = context.getContentAssemblyService()
				.createContentAssembly(getSystemUser(),
						parentObject.getId(), mapCaNodeName, true);
		return rootCA;
	}
	
	private void setUpWorkflowVariables(WorkflowExecutionContext context,
			ArrayList<MoWorkflowObject> importedMaps, BosMember rootMember)
			throws RSuiteException {
		if (importedMaps.size() > 0) {
			String mapCaIdVarName = resolveVariablesAndExpressions(
					context,
					getParameterWithDefault(MAP_CA_ID_VAR_NAME_PARAM, "mapCaId"));
			context.setVariable(mapCaIdVarName, importedMaps.get(0).getMoid());
		}
		if (rootMember != null) {
			String mapMoIdVarName = resolveVariablesAndExpressions(
					context,
					getParameterWithDefault(MAP_MO_ID_VAR_NAME_PARAM, "mapMoId"));
			context.setVariable(mapMoIdVarName, rootMember.getManagedObject()
					.getId());
		}
	}

	private DitaMapImportOptions createImportOptions(
			WorkflowExecutionContext context, String mapCaNodeName,
			ContentAssembly rootCA) {
		DitaMapImportOptions importOptions = new DitaMapImportOptions();

		importOptions.setRootFolder(resolveVariablesAndExpressions(context,
				getParameter(PARENT_BROWSE_TREE_FOLDER_PARAM)));

		importOptions
				.setRootContentAssemblyPath(resolveVariablesAndExpressions(
						context, getParameter(PARENT_CA_NODE_PARAM))
						+ ("".equals(mapCaNodeName) ? "" : "/" + mapCaNodeName));

		importOptions.setMissingGraphicUri(resolveVariablesAndExpressions(
				context,
				getParameterWithDefault(MISSING_GRAPHIC_URI_PARAM,
						"/rsuite-dita-support/images/missingGraphic")));
		importOptions
				.setTopicContainerName(resolveVariablesAndExpressions(
						context,
						getParameterWithDefault(TOPIC_CONTAINER_NAME_PARAM,
								"content")));
		importOptions.setNonXmlContainerName(resolveVariablesAndExpressions(
				context,
				getParameterWithDefault(NONXML_CONTAINER_NAME_PARAM, "media")));
		importOptions.setUser(getSystemUser());
		importOptions.setRootCa((ContentAssemblyNodeContainer) rootCA);
		return importOptions;
	}

	private BosConstructionOptions createBOSoptions(
			WorkflowExecutionContext context, StringBuilder validationReport)
			throws RSuiteException, URISyntaxException, MalformedURLException {

		Log wfLog = context.getWorkflowLog();
		String[] catalogs = getXMLcatalogs(context);

		BosConstructionOptions domOptions = new BosConstructionOptions(wfLog,
				validationReport, catalogs);

		Transformer validationReportTransform = context.getXmlApiManager()
				.getTransformer(new URI(VALIDATION_XSLT_URI));
		domOptions.setReportSerializationTransform(validationReportTransform);
		return domOptions;
	}

	/**
	 * Get array of XML catalogs
	 * 
	 * @param context
	 * @param wfLog
	 * @return Get array of XML catalogs
	 * @throws RSuiteException
	 * @throws MalformedURLException
	 */
	private String[] getXMLcatalogs(WorkflowExecutionContext context)
			throws RSuiteException, MalformedURLException {

		Log wfLog = context.getWorkflowLog();

		// We need the Open Toolkit because we use its catalog to parse the
		// incoming files.
		String otName = resolveVariablesAndExpressions(context,
				getParameterWithDefault(OPEN_TOOLKIT_NAME_PARAM, "default"));
		wfLog.info("Using Open Toolkit named \"" + otName + "\"");
		DitaOpenToolkit toolkit = context.getXmlApiManager()
				.getDitaOpenToolkitManager().getToolkit(otName);
		if (toolkit == null) {
			String msg = "No DITA Open Toolkit named \"" + otName
					+ "\" provided by Open Toolkit Manager. Cannot continue.";
			reportAndThrowRSuiteException(context, msg);
		}

		wfLog.info("Got an Open Toolkit, located at \"" + toolkit.getPath()
				+ "\"");

		String[] catalogs = new String[1];
		String catalogPath = toolkit.getCatalogPath();
		File catalogFile = new File(catalogPath);
		if (!catalogFile.exists()) {
			String msg = "Cannot find catalog file "
					+ catalogFile.getAbsolutePath();
			reportAndThrowRSuiteException(context, msg);
		}
		String catalogUrl = catalogFile.toURI().toURL().toExternalForm();

		wfLog.info("Using Toolkit catalog \"" + catalogUrl + "\"");
		catalogs[0] = catalogUrl;
		return catalogs;
	}

	public static String getStackTrace(Throwable aThrowable) {
		final Writer result = new StringWriter();
		final PrintWriter printWriter = new PrintWriter(result);
		aThrowable.printStackTrace(printWriter);
		return result.toString();
	}

	/**
	 * If the input is in the file workflow object, it very likely came from a
	 * zip file, and the root map could be below the root path, so look for it,
	 * otherwise, just do the import.
	 * 
	 * @param context
	 * @param wfLog
	 * @return
	 * @throws RSuiteException
	 */
	@SuppressWarnings("unchecked")
	protected File findDitaMapFile(WorkflowExecutionContext context, Log wfLog)
			throws RSuiteException {

		File mapFile = null;
		FileWorkflowObject fileWFO = getAndCheckWorkflowFileObject(context, wfLog);

		File workflowDir = fileWFO.getFile();
		String[] extensions = { "ditamap" };

		Collection<File> ditaMapsCollection = FileUtils.listFiles(workflowDir,
				extensions, true);

		if (ditaMapsCollection.size() == 0) {
			reportAndThrowRSuiteException(context, "No ditamap file found");
		}

		// Return first
		for (File file : ditaMapsCollection) {
			mapFile = file;
			break;
		}

		return mapFile;
	}

	private FileWorkflowObject getAndCheckWorkflowFileObject(
			WorkflowExecutionContext context, Log wfLog) throws RSuiteException {
		FileWorkflowObject fileWFO = context.getFileWorkflowObject();
		if (fileWFO == null) {
			String msg = "No file in the workflow context. Nothing to do";
			context.setVariable(EXCEPTION_MESSAGE_VAR, msg);
			createWorkflowComment(context, "RSUite",
					DEFAULT_WORKFLOW_COMMENT_STREAM_NAME, "Error: " + msg);
			wfLog.warn(msg);
			reportAndThrowRSuiteException(context, msg);
		}
		return fileWFO;
	}

	/**
	 * @param context
	 * @param domOptions
	 * @param bos
	 * @param outputPath
	 * @param mo
	 */
	private BoundedObjectSet importMap(WorkflowExecutionContext wfContext,
			Document mapDoc, BosConstructionOptions domOptions,
			DitaMapImportOptions importOptions) throws RSuiteException {
		Log wfLog = wfContext.getWorkflowLog();

		BoundedObjectSet bos = RSuiteDitaHelper.importMap(context, wfLog,
				getSystemUser(), mapDoc, domOptions, importOptions);
		return bos;
	}

	public void setRootMapPath(String rootMapPath) {
		this.setParameter(ROOT_MAP_PATH_PARAM, rootMapPath);
	}

	public void setMapFileName(String mapFileName) {
		this.setParameter(MAP_FILENAME_PARAM, mapFileName);
	}

	public void setParentBrowseTreeFolder(String parentBrowseTreeFolder) {
		this.setParameter(PARENT_BROWSE_TREE_FOLDER_PARAM,
				parentBrowseTreeFolder);
	}

	public void setParentCaNode(String parentCaNode) {
		this.setParameter(PARENT_CA_NODE_PARAM, parentCaNode);
	}

	public void setMapCaNodeName(String mapCaNodeName) {
		this.setParameter(MAP_CA_NODE_NAME_PARAM, mapCaNodeName);
	}

	public void setValidationReportIdVarName(String validationReportIdVarName) {
		setParameter(VALIDATION_REPORT_ID_VAR_NAME_PARAM,
				validationReportIdVarName);
	}

	public void setValidationReportFileNameVarName(
			String validationReportFileNameVarName) {
		setParameter(VALIDATION_REPORT_FILENAME_VAR_NAME_PARAM,
				validationReportFileNameVarName);
	}

	public void setMissingGraphicUri(String missingGraphicUri) {
		setParameter(MISSING_GRAPHIC_URI_PARAM, missingGraphicUri);
	}

	public void setTopicContainerName(String name) {
		setParameter(TOPIC_CONTAINER_NAME_PARAM, name);
	}

	public void setNonXmlContainerName(String name) {
		setParameter(NONXML_CONTAINER_NAME_PARAM, name);
	}

	public void setMapMoIdVarName(String mapMoIdVarName) {
		setParameter(MAP_MO_ID_VAR_NAME_PARAM, mapMoIdVarName);
	}

	public void setMapCaIdVarName(String mapCaIdVarName) {
		setParameter(MAP_CA_ID_VAR_NAME_PARAM, mapCaIdVarName);
	}

	public void setCommitMessage(String commitMessage) {
		setParameter(COMMIT_MESSAGE_PARAM, commitMessage);
	}

}
