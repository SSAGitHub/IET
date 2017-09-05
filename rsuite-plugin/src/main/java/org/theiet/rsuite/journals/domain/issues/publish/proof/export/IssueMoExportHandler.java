package org.theiet.rsuite.journals.domain.issues.publish.proof.export;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.transform.Templates;
import javax.xml.transform.TransformerFactory;

import org.apache.commons.io.FilenameUtils;
import org.theiet.rsuite.journals.domain.article.Article;
import org.theiet.rsuite.journals.domain.article.ArticleFactory;
import org.theiet.rsuite.journals.domain.article.metadata.ArticleMetadata;
import org.theiet.rsuite.journals.domain.issues.publish.common.datatype.IssueArticleReference;
import org.theiet.rsuite.journals.domain.issues.publish.common.datatype.IssueMap;
import org.theiet.rsuite.journals.domain.issues.publish.finalartilces.export.IssueFinalArticlesMoExportHandler;

import com.reallysi.rsuite.api.ManagedObject;
import com.reallysi.rsuite.api.MetaDataItem;
import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.User;
import com.reallysi.rsuite.api.extensions.ExecutionContext;
import com.rsicms.projectshelper.export.MoExportAdditionalTransformation;
import com.rsicms.projectshelper.export.MoExportHandlerContext;
import com.rsicms.projectshelper.utils.ProjectTransformationUtils;

public class IssueMoExportHandler extends IssueFinalArticlesMoExportHandler {

	private static final String XSLT_URI_ID_UNIQUE = "rsuite:/res/plugin/iet/xslt/jats-fo-xsl/article-globally-unique-ids.xsl";

	private ExecutionContext context;

	private File exportFolder;

	private List<IssueArticleReference> articles = new ArrayList<IssueArticleReference>();

	private Map<String, Article> articleXml2Article = new HashMap<>();
	
	private List<MoExportAdditionalTransformation> additionalDitaTransformationList;
	
	private User user;

	public IssueMoExportHandler() {
	}

	@Override
	public void initialize(MoExportHandlerContext moExportHandlerContext)
			throws RSuiteException {
		super.initialize(moExportHandlerContext);
		context = moExportHandlerContext.getContext();
		user = moExportHandlerContext.getUser();
		exportFolder = moExportHandlerContext.getExportFolder();

		TransformerFactory transformerFactory = context.getXmlApiManager()
				.getTransformerFactory();
		additionalDitaTransformationList = createAdditionalTransformationForDitaFiles(transformerFactory);

	}

	@Override
	public boolean exportMo(ManagedObject mo) {
		return checkIfSkipExport(mo, true);
	}


	@Override
	public String getMoExportPath(ManagedObject mo, String exportUri)
			throws RSuiteException {
		String exportPath = super.getMoExportPath(mo, exportUri);

		if (isArticleMo(mo)) {
			
			String specialPage = isLegalOrInstructPage(mo);
			
			if ("legal_page".equals(specialPage) || "instruct_page".equals(specialPage)){
				exportPath = specialPage + ".xml";
			}else{
				Article article = ArticleFactory.getAtricleBaseOnArticleXMLMoId(context, user, mo.getId());
				ArticleMetadata articleMetadata = article.getArticleMetadata();
				
				exportPath = FilenameUtils.getName(exportPath);
				articles.add(new IssueArticleReference(exportPath, articleMetadata.isSpecialIssue()));
				
				articleXml2Article.put(mo.getId(), article);
			}
		}
		
		return exportPath;
	}

	@Override
	public boolean embedLmd() {
		return true;
	}

	@Override
	public void afterExport(File outputFolder) throws RSuiteException {
		super.afterExport(outputFolder);
		IssueMap.createIssueMap(articles, exportFolder);
		convertImagesToGrayScale(exportFolder);
	}
		

	private void convertImagesToGrayScale(File exportFolder) throws RSuiteException {
		ImageToGrayscaleConventer imageConventer = new ImageToGrayscaleConventer(context);
		imageConventer.convertImagesToGrayScale(new File(exportFolder, "images"));
	}

	@Override
	public List<MoExportAdditionalTransformation> getTransformationToPerformBeforePersistExportedMo(
			ManagedObject mo) throws RSuiteException {

		return additionalDitaTransformationList;
	}

	private List<MoExportAdditionalTransformation> createAdditionalTransformationForDitaFiles(
			TransformerFactory transformerFactory) throws RSuiteException {

		String xsltURI = XSLT_URI_ID_UNIQUE;

		Templates ditaTemplate = ProjectTransformationUtils
				.createTransformTemplate(context, transformerFactory, xsltURI);

		List<MoExportAdditionalTransformation> additionalTransformationList = new ArrayList<MoExportAdditionalTransformation>();
		additionalTransformationList.add(new MoExportAdditionalTransformation(
				ditaTemplate, null));

		return additionalTransformationList;
	}
	
	@Override
	public List<MetaDataItem> getAdditionalMetaData(ManagedObject moToExport) throws RSuiteException {
		if (articleXml2Article.containsKey(moToExport.getId())){
			Article article = articleXml2Article.get(moToExport.getId());	
			return article.getArticleCA().getMetaDataItems();
		}
	
		return super.getAdditionalMetaData(moToExport);
	}
}
