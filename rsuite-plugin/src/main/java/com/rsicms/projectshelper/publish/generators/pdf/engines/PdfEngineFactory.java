package com.rsicms.projectshelper.publish.generators.pdf.engines;

import com.reallysi.rsuite.api.*;
import com.reallysi.rsuite.api.extensions.ExecutionContext;

public final class PdfEngineFactory {

    private PdfEngineFactory(){};
    
    public static PdfFromattingEngine createFopEngine(ExecutionContext context){
      return  new FopPdfEngine(context.getXmlApiManager());
      
    }
    
    public static AntennaHousePdfEngine createAntennaHouseEngine(ExecutionContext context) throws RSuiteException{
        return createAntennaHouseEngine(context, null); 
    }
    
    public static AntennaHousePdfEngine createAntennaHouseEngine(ExecutionContext context, String antennaHouseConfiguration) throws RSuiteException{
        
        ConfigurationProperties configurationProperties = context.getConfigurationProperties();
        String antennaHouseHomeBinDir = configurationProperties.getProperty("rsuite.integration.antennahouse.home.bin", "");
        String antennaHouseCommand = configurationProperties.getProperty("rsuite.integration.antennahouse.command", "AHFCmd");
        
        String antennaHouseRemoteUrl = context.getConfigurationProperties().getProperty("rsuite.antenna.house.remote.url", null);
        
        if (antennaHouseRemoteUrl != null){
            return new RemoteAntennaHousePdfEngine(antennaHouseRemoteUrl, antennaHouseConfiguration);
        }
        
        
        return new LocalAntennaHousePdfEngine(antennaHouseHomeBinDir, antennaHouseCommand, antennaHouseConfiguration);
    }
}
