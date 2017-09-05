package org.theiet.rsuite.iettv.domain.factories.parsers;

import java.io.*;

import javax.xml.stream.*;

import org.theiet.rsuite.iettv.domain.datatype.*;
import org.theiet.rsuite.iettv.domain.datatype.VideoRecordMetadata.Builder;

import com.reallysi.rsuite.api.RSuiteException;

public final class VideoRecordMetadataXMLParser {

    
	private static final String ELEMENT_NAME_ACCESSION_NUMBER = "AccessionNumber";
	private static final String ATTRIBUTE_NAME_ID = "ID";
    private static final String ELEMENT_NAME_VIDEO_CREATE_DATE = "Date";

    private VideoRecordMetadataXMLParser(){
        
    }
    
    private static final String ELEMENT_NAME_CHANNEL_CATEGORY = "Category";
    private static final String ELEMENT_NAME_CHANNEL_NAME = "ChannelName";
    private static final String ELEMENT_NAME_CHANNEL = "Channel";
    private static final String ELEMENT_NAME_CHANNEL_MAPPINGS = "ChannelMapping";
    private static final String ELEMENT_NAME_TITLE = "Title";
    private static final String ELEMENT_NAME_VIDEO_CREATE_INFO = "CreationInfo";
    private static final String ELEMENT_NAME_BASIC_INFO = "BasicInfo";
    private static final String ELEMENT_NAME_VIDEO = "Video";
    private static final String ELEMENT_VIDEO_STATUS = "VideoStatus";
    private static final String ELEMENT_VIDEO_NUMBER = "VideoNumber";
    
    private static XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();

    public static VideoRecordMetadata praseVideoRecordMetadata(File videoRecordXML) {

        VideoRecordMetadata.Builder metadataBuilder = new Builder();
        try {
            FileInputStream videoRecordStream = new FileInputStream(videoRecordXML);

            XMLStreamReader xmlStreamReader =
                    xmlInputFactory.createXMLStreamReader(videoRecordStream);

            while (xmlStreamReader.hasNext()) {
                xmlStreamReader.next();

                if (xmlStreamReader.isStartElement()) {
                    String localName = xmlStreamReader.getLocalName();

                    if (ELEMENT_NAME_VIDEO.equals(localName)) {
                        parseVideoId(metadataBuilder, xmlStreamReader);
                    }else if (ELEMENT_VIDEO_NUMBER.equals(localName)) {
                        metadataBuilder.videoNumber(xmlStreamReader.getElementText());
                    }else if (ELEMENT_VIDEO_STATUS.equals(localName)) {
                        parseVideoStatus(metadataBuilder, xmlStreamReader);
                    } else if (ELEMENT_NAME_BASIC_INFO.equals(localName)) {
                        parseBasicInfo(xmlStreamReader, metadataBuilder);
                    }else if (ELEMENT_NAME_VIDEO_CREATE_INFO.equals(localName)){
                        parseVideoCreateDate(metadataBuilder, xmlStreamReader);
                    }else if (ELEMENT_NAME_ACCESSION_NUMBER.equals(localName)){
                    	metadataBuilder.accessionNumber(xmlStreamReader.getElementText());
                    }
                }

            }
        } catch (XMLStreamException e) {
            new RSuiteException(0, "Unable to parse file " + videoRecordXML.getAbsolutePath(), e);
        } catch (IOException e) {
            new RSuiteException(0, "Unable to parse file " + videoRecordXML.getAbsolutePath(), e);
        }


        return metadataBuilder.build();
    }

    private static void parseVideoId(VideoRecordMetadata.Builder metadataBuilder,
            XMLStreamReader xmlStreamReader) throws XMLStreamException {
        metadataBuilder.videoId(xmlStreamReader.getAttributeValue("", ATTRIBUTE_NAME_ID));
    }

    private static void parseVideoStatus(Builder metadataBuilder,
			XMLStreamReader xmlStreamReader) throws XMLStreamException {
    	metadataBuilder.videoStatus(VideoStatus.valueOf(xmlStreamReader.getElementText().toUpperCase())); 
	}
 
    private static void parseVideoCreateDate(VideoRecordMetadata.Builder metadataBuilder,
            XMLStreamReader xmlStreamReader) throws XMLStreamException {
        
        while (!(xmlStreamReader.isEndElement() && ELEMENT_NAME_VIDEO_CREATE_INFO.equals(xmlStreamReader
                .getLocalName()))) {
            xmlStreamReader.next();

            if (xmlStreamReader.isStartElement()) {
                String localName = xmlStreamReader.getLocalName();

                if (ELEMENT_NAME_VIDEO_CREATE_DATE.equals(localName)) {
                    String creationDate = xmlStreamReader.getElementText();
                    String creationYear = creationDate.substring(0, creationDate.indexOf('-'));
                    metadataBuilder.year(creationYear);
                }
            }
        }
        
        
    }

    private static void parseBasicInfo(XMLStreamReader xmlStreamReader,
            VideoRecordMetadata.Builder metadataBuilder) throws XMLStreamException {


        while (!(xmlStreamReader.isEndElement() && ELEMENT_NAME_BASIC_INFO.equals(xmlStreamReader
                .getLocalName()))) {
            xmlStreamReader.next();

            if (xmlStreamReader.isStartElement()) {
                String localName = xmlStreamReader.getLocalName();

                if (ELEMENT_NAME_TITLE.equals(localName)) {
                    metadataBuilder.title(xmlStreamReader.getElementText());
                } else if (ELEMENT_NAME_CHANNEL_MAPPINGS.equals(localName)) {
                    parseChannels(xmlStreamReader, metadataBuilder);
                }
            }
        }
    }

    private static void parseChannels(XMLStreamReader xmlStreamReader,
            VideoRecordMetadata.Builder metadataBuilder) throws XMLStreamException {

        while (!(xmlStreamReader.isEndElement() && ELEMENT_NAME_CHANNEL_MAPPINGS.equals(xmlStreamReader
                .getLocalName()))) {
        	
        	
            xmlStreamReader.next();

            if (xmlStreamReader.isStartElement()) {
                String localName = xmlStreamReader.getLocalName();

                if (ELEMENT_NAME_CHANNEL.equals(localName)) {
                	
                	VideoChannel videoChannel = parseVideoChannel(xmlStreamReader);
                	metadataBuilder.channel(videoChannel);
                }
            }

        }
    }
    
    private static VideoChannel parseVideoChannel(XMLStreamReader xmlStreamReader) throws XMLStreamException{
    	
    	VideoChannel.Builder channelBuilder = new VideoChannel.Builder();
    	
    	  while (!(xmlStreamReader.isEndElement() && ELEMENT_NAME_CHANNEL.equals(xmlStreamReader
                  .getLocalName()))) {
    		  
    		  if (xmlStreamReader.isStartElement()) {
    			  String localName = xmlStreamReader.getLocalName();
    			  
    			  if (ELEMENT_NAME_CHANNEL.equals(localName)) {
                      String defaultChannel = xmlStreamReader.getAttributeValue(null, "default");
                      channelBuilder.mainChannel(Boolean.parseBoolean(defaultChannel));
                  }

                  if (ELEMENT_NAME_CHANNEL_NAME.equals(localName)) {
                      String channelName = xmlStreamReader.getElementText();
                      channelBuilder.name(channelName);
                  }
                  
                  if (ELEMENT_NAME_CHANNEL_CATEGORY.equals(localName)) {
                      String category = xmlStreamReader.getElementText();
                      channelBuilder.category(category);
                  }
    		  }
    		  
              xmlStreamReader.next();
    	  }
    	  
    	  
    	  return channelBuilder.build();
    }
}
