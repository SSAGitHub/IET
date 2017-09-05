package com.rsicms.projectshelper.publish.generators.pdf.engines;

import java.io.*;
import java.util.*;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;

import com.reallysi.rsuite.api.RSuiteException;
import com.rsicms.projectshelper.system.SystemUtils;

public class LocalAntennaHousePdfEngine implements AntennaHousePdfEngine {

    private File antennaHouseHomeFolder;

    private String antennaHouseConfiguration;
    
    private String antennaHouseCommand;

    LocalAntennaHousePdfEngine(String antennaHouseHomeDir, String antennaHouseCommand, String antennaHouseConfiguration)
            throws RSuiteException {
        this.antennaHouseHomeFolder = new File(antennaHouseHomeDir);
        if (!antennaHouseHomeFolder.exists()) {
            throw new RSuiteException("Folder " + antennaHouseHomeDir + "doesn't exist");
        }

        this.antennaHouseConfiguration = antennaHouseConfiguration;
        this.antennaHouseCommand = antennaHouseCommand;
    }


    @Override
    public void generatePDFFromFo(InputStream foInputStream, OutputStream pdfOutputStream)
            throws RSuiteException {
        throw new RSuiteException("This method is not implemented ");

    }

    @Override
    public void generatePDFFromFo(Log logger, File foInputFile, File pdfOutputFile)
            throws RSuiteException {

        List<String> execCmd = createBaseCommand(foInputFile, pdfOutputFile);

        addConfigurationParameter(execCmd, foInputFile);

        SystemUtils.executeOScommand(logger, execCmd, antennaHouseHomeFolder);

        if (!pdfOutputFile.exists()) {
            throw new RSuiteException("Unable to generate pdf file for " + foInputFile);
        }

    }

    private void addConfigurationParameter(List<String> execCmd, File foInputFile)
            throws RSuiteException {

        if (StringUtils.isNotBlank(antennaHouseConfiguration)) {
            File configurationFile = createConfigurationFile(foInputFile);
            execCmd.add("-i");
            execCmd.add(configurationFile.getAbsolutePath());
        }

    }

    private File createConfigurationFile(File foInputFile) throws RSuiteException {

        File configurationFile = new File(foInputFile.getParentFile(), createConfigurationFile());

        try {

            FileUtils.writeStringToFile(configurationFile, antennaHouseConfiguration, "utf8");
        } catch (IOException e) {
            throw new RSuiteException(0, "Unable to create configuration file "
                    + configurationFile.getAbsolutePath(), e);
        }

        return configurationFile;
    }


    @Override
    public void generateAreaTreeFileFromFo(Log logger, File foInputFile, File pdfOutputFile)
            throws RSuiteException {

        List<String> execCmd = createBaseCommand(foInputFile, pdfOutputFile);
        execCmd.add("-p");
        execCmd.add("@AreaTree");
        
        addConfigurationParameter(execCmd, foInputFile);

        SystemUtils.executeOScommand(logger, execCmd, antennaHouseHomeFolder);

        if (!pdfOutputFile.exists()) {
            throw new RSuiteException("Unable to generate pdf file for " + foInputFile);
        }

    }



    private List<String> createBaseCommand(File foInputFile, File pdfOutputFile) {
        List<String> execCmd = new ArrayList<String>();
        execCmd.add(antennaHouseCommand);
        execCmd.add("-x");
        execCmd.add("3");
        execCmd.add("-d");
        execCmd.add(foInputFile.getAbsolutePath());
        execCmd.add("-o");
        execCmd.add(pdfOutputFile.getAbsolutePath());
        return execCmd;
    }

    private String createConfigurationFile() {
        return "ah_config_" + UUID.randomUUID().toString() + ".xml";
    }

}
