package com.rsicms.projectshelper.publish.workflow.actionhandlers;

import static com.rsicms.projectshelper.publish.workflow.actionhandlers.ConfigurationFactory.createOutputGeneratorConfiguration;
import static com.rsicms.projectshelper.publish.workflow.constant.PublishWorkflowVariables.EXPORTED_FILES;
import static com.rsicms.projectshelper.publish.workflow.constant.PublishWorkflowVariables.MO_OUTPUTS;
import static com.rsicms.projectshelper.publish.workflow.constant.PublishWorkflowVariables.OUTPUT_GENERATOR_CONFIGURATION_CLASS;
import static com.rsicms.projectshelper.publish.workflow.constant.PublishWorkflowVariables.TEMP_FOLDER;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.logging.Log;

import com.reallysi.rsuite.api.workflow.WorkflowExecutionContext;
import com.rsicms.projectshelper.publish.generators.OutputGenerator;
import com.rsicms.projectshelper.publish.workflow.configuration.OutputGeneratorConfiguration;

public class GenerateOutputActionHandler extends PublishWorkflowActionHandler {

    private static final long serialVersionUID = 8224727220520504208L;

    @SuppressWarnings("unchecked")
    @Override
    public void executeHandler(WorkflowExecutionContext context) throws Exception {
    	
        Log wfLog = context.getWorkflowLog();
        

        Map<String, File> exportedMOs = (Map<String, File>) context
                .getVariableAsObject(EXPORTED_FILES.getVariableName());

        Map<String, File> moOutputs = new HashMap<String, File>();

        File workflowTempFolder = (File) context
                .getVariableAsObject(TEMP_FOLDER.getVariableName());

        String outputGeneratorConfigurationClass = context.getVariable(OUTPUT_GENERATOR_CONFIGURATION_CLASS.getVariableName());
        OutputGeneratorConfiguration outputGeneratorConfiguration = createOutputGeneratorConfiguration(outputGeneratorConfigurationClass);

        OutputGenerator outputGenerator = outputGeneratorConfiguration.getOutputGenerator(context, getSystemUser());
        outputGenerator.initialize(context, context.getWorkflowLog(), getWorkflowVariables(context));

        for (Entry<String, File> exportedMo : exportedMOs.entrySet()) {
            String moId = exportedMo.getKey();
            File moFile = exportedMo.getValue();

            File outputDir = new File(workflowTempFolder, "output/" + moId);
            outputDir.mkdirs();
            
            File outputFile = outputGenerator.generateOutput(workflowTempFolder, moId,
                    moFile, outputDir);

            moOutputs.put(moId, outputFile);
            wfLog.info("Generated output for mo: " + moId + " output file " + outputFile.getAbsolutePath());
        }

        context.setVariable(MO_OUTPUTS.getVariableName(), moOutputs);

        wfLog.info(exportedMOs);
        wfLog.info(workflowTempFolder);
        
    }

	@Override
	String getWorkflowTaskName() {
		return "Generate output";
	}

   
}
