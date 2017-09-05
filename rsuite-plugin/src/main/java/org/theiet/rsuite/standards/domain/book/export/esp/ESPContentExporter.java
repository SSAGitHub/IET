package org.theiet.rsuite.standards.domain.book.export.esp;

import static org.theiet.rsuite.books.BooksConstans.*;
import static org.theiet.rsuite.standards.domain.book.export.BookEditionExportHelper.*;

import java.io.*;
import java.util.*;

import org.theiet.rsuite.standards.domain.book.*;

import com.reallysi.rsuite.api.*;
import com.reallysi.rsuite.api.extensions.ExecutionContext;
import com.rsicms.projectshelper.export.*;
import com.rsicms.projectshelper.export.impl.refence.MoExporterImpl;

public class ESPContentExporter {

    private List<ManagedObject> bookEditionMainXMLMo;

    private ExecutionContext context;

    private User user;

    private File exportDir;

    private StandardsBookEdition bookEdition;

    public ESPContentExporter(ExecutionContext context, User user,
            StandardsBookEdition bookEdition, File exportDir) throws RSuiteException {

        this.bookEditionMainXMLMo = bookEdition.getMainXMLMo();
        this.bookEdition = bookEdition;
        this.context = context;
        this.user = user;
        this.exportDir = exportDir;
        this.exportDir.mkdirs();
    }

    public static FileInputStream createFileInputStream(String pathname)
            throws FileNotFoundException {
        return new FileInputStream(new File(pathname));
    }

    public MoExportResult export() throws RSuiteException {

        MoExporterImpl moExporter = createMoExporter(context, user);

        MoExportContainerContext moExportContainerContext =
                createMoExportContainerContext(context, user, bookEdition);

        MoExportResult exportResult =
                moExporter.exportMo(moExportContainerContext, bookEditionMainXMLMo, exportDir);

        exportManifestFile();

        return exportResult;

    }

    private void exportManifestFile() throws RSuiteException {
        Properties properties = new Properties();
        properties.setProperty("title", bookEdition.getLMD(LMD_FIELD_BOOK_TITLE));
        properties.setProperty("eproduct", bookEdition.getLMD(LMD_FIELD_BOOK_E_PRODUCT_CODE));

        File manifestFile = new File(exportDir, "manifest");

        try {

            properties.store(new FileOutputStream(manifestFile), null);
        } catch (IOException e) {
            throw new RSuiteException(0, "Unable to store manifest file "
                    + manifestFile.getAbsolutePath() + " for book edition "
                    + bookEdition.getRsuiteId(), e);
        }


    }

    protected MoExporterImpl createMoExporter(ExecutionContext context, User user)
            throws RSuiteException {

        MoExportConfiguration moExporterConfiguration = new ESPDitaMoExporterConfiguration();

        if (StandardsBookEditionType.JATS == bookEdition.getBookEditionType()) {
            moExporterConfiguration = new ESPJatsMoExporterConfiguration();
        }


        MoExporterImpl moExporter = new MoExporterImpl(context, user, moExporterConfiguration);
        return moExporter;
    }

}
