package org.theiet.rsuite.iettv.domain.factories.parsers;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

import java.io.File;

import org.junit.Test;
import org.theiet.rsuite.iettv.domain.datatype.VideoChannel;
import org.theiet.rsuite.iettv.domain.datatype.VideoRecordMetadata;
import org.theiet.rsuite.iettv.domain.factories.parsers.VideoRecordMetadataXMLParser;

public class VideoRecordMetadataXMLParserTest {

    
    private static VideoRecordMetadata videoRecordMetadata;
    
    
    private VideoRecordMetadata getVideoRecordMetadata(){
        if (videoRecordMetadata == null){
            Class<?> clazz = this.getClass();
            File videoRecordXML = getTestFile(clazz, "videoMetadata.xml");           
            videoRecordMetadata = VideoRecordMetadataXMLParser.praseVideoRecordMetadata(videoRecordXML);  
        }
        
        return videoRecordMetadata;
    }

    private File getTestFile(Class<?> clazz, String fileName) {
        String packageName = clazz.getPackage().getName();
        packageName = packageName.replace(".", "/");
       
        return new File("src/test/resources/" + packageName + "/" + fileName);
    }
    
    @Test
    public void test_praseVideoRecordMetadata_title() {        
        VideoRecordMetadata videoRecordMetadata = getVideoRecordMetadata();        
        assertEquals("Coupling spin waves to circuit through PEEC", videoRecordMetadata.getTitle());       
    }
    
    @Test
    public void test_praseVideoRecordMetadata_videoId() {        
        VideoRecordMetadata videoRecordMetadata = getVideoRecordMetadata();        
        assertEquals("0b95f4d9-3e6b-4eed-836d-008d8c55f7ce", videoRecordMetadata.getVideoId());       
    }
    
    @Test
    public void test_praseVideoRecordMetadata_mainChannel() {        
        VideoRecordMetadata videoRecordMetadata = getVideoRecordMetadata();        
        assertEquals(new VideoChannel("Communications", "Sample category", true), videoRecordMetadata.getMainChannel());       
    }
    
    @Test
    public void test_praseVideoRecordMetadata_channels() {        
        VideoRecordMetadata videoRecordMetadata = getVideoRecordMetadata();  
        
        VideoChannel channel1 = new VideoChannel("Communications", "Sample category", true);
        VideoChannel channel2 = new VideoChannel("IT", "", false);
        
        assertThat(videoRecordMetadata.getChannels(), containsInAnyOrder(channel1, channel2));
        assertEquals("Sample category", videoRecordMetadata.getChannels().get(0).getCategory());
    }
    
    @Test
    public void test_praseVideoRecordMetadata_year() {        
        VideoRecordMetadata videoRecordMetadata = getVideoRecordMetadata();        
        assertEquals("2015", videoRecordMetadata.getYear());       
    }
    
    @Test
    public void test_praseVideoRecordMetadata_withdrawn() {        
        VideoRecordMetadata videoRecordMetadata = getVideoRecordMetadata();        
        assertEquals(false, videoRecordMetadata.isWithdrawn());       
    }
    
    @Test
    public void test_praseVideoNumber() {        
        VideoRecordMetadata videoRecordMetadata = getVideoRecordMetadata();        
        assertThat(videoRecordMetadata.getVideoNumber(), is("111"));    
    }
    
    
    @Test
    public void test_parseAccessionNumber(){
    	File videoRecordXML = getTestFile(getClass(), "videoMetadata2.xml");           
        VideoRecordMetadata videoRecordMetadata = VideoRecordMetadataXMLParser.praseVideoRecordMetadata(videoRecordXML);
        
        assertThat(videoRecordMetadata.getAccessionNumber(), is("11704305")); 
    }

}
