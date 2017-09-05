package org.theiet.rsuite.standards.domain.publish.upload;

import static com.rsicms.projectshelper.publish.storage.datatype.OutputCaTypes.*;
import static org.theiet.rsuite.standards.datatype.StandardsCaTypes.*;

import java.io.File;
import java.util.*;
import java.util.Map.Entry;

import org.apache.commons.logging.Log;
import org.theiet.rsuite.standards.domain.book.StandardsBookEdition;

import com.reallysi.rsuite.api.*;
import com.reallysi.rsuite.api.extensions.ExecutionContext;
import com.rsicms.projectshelper.datatype.RSuiteCaType;
import com.rsicms.projectshelper.publish.datatype.OutputGenerationResult;
import com.rsicms.projectshelper.publish.datatype.UploadGeneratedOutputsResult;
import com.rsicms.projectshelper.publish.storage.upload.BaseGeneratedOutputUploader;
import com.rsicms.projectshelper.utils.browse.ProjectBrowserUtils;

public class StandardsGeneratedOutputUploader extends BaseGeneratedOutputUploader {

    private StandardsBookEdition bookEdition;

    private ExecutionContext context;

    public StandardsGeneratedOutputUploader(ExecutionContext context, Log logger, String workflowProcessId, StandardsBookEdition bookEdition) {
        super(context, logger, workflowProcessId);
        this.context = context;
        this.bookEdition = bookEdition;
    }

    @Override
    public UploadGeneratedOutputsResult uploadGeneratedOutputs(OutputGenerationResult outputGenerationResult)
            throws RSuiteException {

        renameOutputFiles(outputGenerationResult.getMoOutPuts());
        return super.uploadGeneratedOutputs(outputGenerationResult);
    }

 
    @Override
    protected RSuiteCaType getHistoryCaType() {
        return STANDARDS_PLUBISH_HISTORY;
    }
    
    private void renameOutputFiles(Map<String, File> moOutPuts) throws RSuiteException {

        String isbn = bookEdition.getLmd().getISBN();
        String isbnPrefix = isbn == null ? "" : isbn + "_";

        for (Entry<String, File> entry : moOutPuts.entrySet()) {
            File file = entry.getValue();
            File newFile = new File(file.getParentFile(), isbnPrefix + file.getName());
            file.renameTo(newFile);
            moOutPuts.put(entry.getKey(), newFile);
        }
    }

    @Override
    protected ContentAssembly getOutputsCa() throws RSuiteException {
        ContentAssembly outputs =
                ProjectBrowserUtils.getChildCaByType(context, bookEdition.getBookPublicationCa(), OUTPUTS);
        return outputs;
    }

    @Override
    protected String getOutputCAName(ManagedObject mo) throws RSuiteException {
        String displayName = mo.getDisplayName();
        return displayName.replaceAll("[^0-9\\p{L}\\s]+", "");
    }



}
