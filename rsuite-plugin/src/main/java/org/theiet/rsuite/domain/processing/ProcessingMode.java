package org.theiet.rsuite.domain.processing;

import com.rsicms.projectshelper.utils.ProjectPluginProperties;

public class ProcessingMode {

    public static boolean isDevelopmentMode(){        
        String developmentMode = ProjectPluginProperties.getProperty("development.mode", "false");
        return Boolean.parseBoolean(developmentMode);
    }
}
