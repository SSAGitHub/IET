package com.rsicms.projectshelper.publish.generators.pdf.engines;

import java.io.*;

import org.apache.commons.io.*;
import org.apache.commons.logging.Log;

import com.reallysi.rsuite.api.RSuiteException;
import com.rsicms.projectshelper.net.http.MultipartRequest;

public class RemoteAntennaHousePdfEngine implements AntennaHousePdfEngine {

    private String remoteAntennaHouseUrl;

    private String antennaHouseConfiguration;

    RemoteAntennaHousePdfEngine(String remoteAntennaHouseUrl, String antennaHouseConfiguration)
            throws RSuiteException {
        this.remoteAntennaHouseUrl = remoteAntennaHouseUrl;
        this.antennaHouseConfiguration = antennaHouseConfiguration;
    }


    @Override
    public void generatePDFFromFo(InputStream foInputStream, OutputStream pdfOutputStream)
            throws RSuiteException {
        throw new RSuiteException("This method is not implemented ");

    }

    @Override
    public void generatePDFFromFo(Log logger, File foInputFile, File pdfOutputFile)
            throws RSuiteException {

        sendRequestToAntennaHouseServlet("pdf", foInputFile, pdfOutputFile);

        if (!pdfOutputFile.exists()) {
            throw new RSuiteException("Unable to generate pdf file for " + foInputFile);
        }

    }

    public void generateAreaTreeFileFromFo(Log logger, File foInputFile, File areaTreeFile)
            throws RSuiteException {
        sendRequestToAntennaHouseServlet("area", foInputFile, areaTreeFile);
    }


    private void sendRequestToAntennaHouseServlet(String operation, File foInputFile,
            File outputFile) throws RSuiteException {

        FileInputStream foInputStream = null;

        try {
            foInputStream = new FileInputStream(foInputFile);
            String uri =
                    remoteAntennaHouseUrl + "/AntennaHouse/AntennaHouseServlet?operation="
                            + operation;
            MultipartRequest request = new MultipartRequest(uri, "utf-8", 400000);

            if (antennaHouseConfiguration != null) {
                request.addFormField("configuration", antennaHouseConfiguration);
            }

            request.addFilePart(foInputFile.getName(), foInputFile.getName(), foInputStream);
            InputStream is = request.completeRequest();

            FileUtils.writeByteArrayToFile(outputFile, IOUtils.toByteArray(is));
            request.disconnect();
        } catch (IOException e) {
            throw new RSuiteException(RSuiteException.ERROR_INTERNAL_ERROR,
                    e.getLocalizedMessage(), e);
        } finally {
            IOUtils.closeQuietly(foInputStream);
        }
    }

}
