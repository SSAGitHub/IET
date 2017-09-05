package org.theiet.rsuite.iettv.domain.publish;

import java.io.*;

import org.apache.commons.io.FileUtils;
import org.theiet.rsuite.iettv.domain.datatype.VideoRecord;

import com.reallysi.rsuite.api.*;
import com.reallysi.rsuite.api.extensions.ExecutionContext;
import com.rsicms.projectshelper.publish.generators.pdf.PDFGenerator;
import com.rsicms.projectshelper.upload.UploadHelper;

public class VideoRecordPdfGenerator {

    private static final String XSLT_URI_IET_TV_VIDEO_TO_FO =
            "rsuite:/res/plugin/iet/xslt/iet-tv/iet-tv-video-record-2-fo.xsl";
    
    public static void generatePdf(ExecutionContext context, User user, VideoRecord videoRecord,
            File temporaryFolder) throws RSuiteException {

        ContentAssembly videoRecordContainer = videoRecord.getVideoRecordContainer();

        try {

            File pdfFile = new File(temporaryFolder, videoRecord.createVideoFileName("pdf"));
            pdfFile.createNewFile();
            File foFile = new File(temporaryFolder, videoRecord.createVideoFileName("fo"));

            FileOutputStream pdfOutputStream = new FileOutputStream(pdfFile);
            String foContent =
                    PDFGenerator.generatePDF(context, videoRecord.getVideoMetadataMo(),
                            XSLT_URI_IET_TV_VIDEO_TO_FO, pdfOutputStream);
            FileUtils.writeStringToFile(foFile, foContent, "utf-8");
            UploadHelper.upsertFileToContainer(context, user, pdfFile, videoRecordContainer);
        } catch (IOException e) {
            throw new RSuiteException(0, "Unable to generate PDF for "
                    + videoRecordContainer.getId(), e);
        } catch (RSuiteException e) {
            throw new RSuiteException(0, "Unable to generate PDF for "
                    + videoRecordContainer.getId(), e);
        }

    }
}
