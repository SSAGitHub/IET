package com.rsicms.projectshelper.utils;

import static org.junit.Assert.*;

import java.io.File;

import org.apache.commons.io.FilenameUtils;
import org.junit.Test;

public class ProjectFileUtilsTest {

    @Test
    public void test_relative_path() {
        
        String contextPath = "Typescript/esp001_samplesmall-pub1.xml";
        String exportPath = "images/InlineReaderImage/wreg_fig557.2_am3_1.png";
        
        String relativePath = ProjectFileUtils.computeRelativePath(contextPath, exportPath);
        
        String expected = "../images/InlineReaderImage/wreg_fig557.2_am3_1.png";
        assertEquals(expected, FilenameUtils.separatorsToUnix(relativePath));
    }

}
