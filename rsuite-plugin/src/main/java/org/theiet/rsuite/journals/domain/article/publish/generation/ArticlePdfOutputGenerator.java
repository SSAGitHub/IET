package org.theiet.rsuite.journals.domain.article.publish.generation;

import static org.theiet.rsuite.journals.domain.article.publish.datatype.ArticlePublishWorkflowVariables.PUBLISH_TYPE;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.theiet.rsuite.domain.transforms.IetAntennaHouseHelper;
import org.theiet.rsuite.journals.domain.article.Article;

import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.extensions.ExecutionContext;
import com.rsicms.projectshelper.publish.generators.pdf.engines.AntennaHousePdfEngine;
import com.rsicms.projectshelper.publish.workflow.generators.WorkflowAntennaHouseAreaTreePdfOutputGenerator;

public class ArticlePdfOutputGenerator extends WorkflowAntennaHouseAreaTreePdfOutputGenerator {

    private static final String XSLT_URI =
            "rsuite:/res/plugin/iet/xslt/jats-fo-xsl/article-fo-1-0.xsl";

    private static final String XSLT_PARAM_SECTION = "section";
    
    private static final String PASS_ALL = "all";
    
    private String publishType;

    private Article article;
    
    private Log logger = LogFactory.getLog(getClass());
    
    private Map<String, String> parameters;
    
    public ArticlePdfOutputGenerator(){
    }
    
    public ArticlePdfOutputGenerator(Article article) {
		this.article = article;
	}

	@Override
    public void initialize(ExecutionContext context, Log logger, Map<String, String> variables)
            throws RSuiteException {
        super.initialize(context, logger, variables);

        publishType = variables.get(PUBLISH_TYPE.getVariableName());
    }

    @Override
    protected String getXml2FoXsltUri() {
        return XSLT_URI;
    }
    
    @Override
    protected Map<String, String> getXml2FoXslParameters() {
        return parameters;
    }

	private Map<String, String> initializeXsltParameters() {
		Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("type", publishType.toLowerCase());
        parameters.put(XSLT_PARAM_SECTION, "body");
		return parameters;
	}


    @Override
    public String getOutputFileName(String moId, String defaultName) throws RSuiteException {
        return  article.getShortArticleId() + "-" + publishType + ".pdf";
    }
    @Override
    protected String getAntennaHouseConfigurationConfiguration() throws RSuiteException {
        return IetAntennaHouseHelper.getAntenaHouseConfiguration();
    }
    
    @Override
	public File generateOutput(File workflowFolder, String moId,
			File inputFile, File outputFolder) throws RSuiteException {


    	parameters = initializeXsltParameters();
		AntennaHousePdfEngine formattingEngine = createPdfEngine();
		File outputFile = new File(outputFolder, getOutputFileName(moId, ""));
		
		File exportFolder = inputFile.getParentFile();
		try {

			File fakeAreaFile = getAreaTreeFile(exportFolder);
			createFakeAreaFile(fakeAreaFile);

			File foFile = createFoUsingAreaFile(exportFolder, inputFile,
					fakeAreaFile);
			
			File areaFile = getAreaTreeFile(exportFolder);
			formattingEngine.generateAreaTreeFileFromFo(logger, foFile,
					areaFile);

			File initialFo = getInitialFoFile(exportFolder);
			FileUtils.moveFile(foFile, initialFo);
			
			foFile = createFoUsingAreaFile(exportFolder, inputFile, areaFile);

			formattingEngine.generateAreaTreeFileFromFo(logger, foFile,
					areaFile);
			
			parameters.put(XSLT_PARAM_SECTION, PASS_ALL);
			
			foFile = createFoUsingAreaFile(exportFolder, inputFile, areaFile);
//			formattingEngine.generateAreaTreeFileFromFo(logger, foFile,
//					areaFile);
//			
//			parameters.remove(XSLT_PARAM_SECTION);
//			foFile = createFoUsingAreaFile(exportFolder, inputFile, areaFile);
			
			
			formattingEngine.generatePDFFromFo(logger, foFile, outputFile);
			
			
		} catch (RSuiteException e) {
			throw new RSuiteException(0,
					createGenerateOutputErrorMessage(inputFile), e);
		} catch (IOException e) {
			throw new RSuiteException(0,
					createGenerateOutputErrorMessage(inputFile), e);
		}finally{
			createDownloadLinks(moId, workflowFolder, exportFolder);
		}

		return outputFile;
	}


}
