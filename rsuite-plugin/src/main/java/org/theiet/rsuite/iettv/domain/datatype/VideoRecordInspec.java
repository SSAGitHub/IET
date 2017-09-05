package org.theiet.rsuite.iettv.domain.datatype;

import org.w3c.dom.*;

import com.reallysi.rsuite.api.*;
import com.reallysi.rsuite.api.control.*;
import com.reallysi.rsuite.api.extensions.ExecutionContext;
import com.reallysi.rsuite.service.ManagedObjectService;
import com.rsicms.projectshelper.utils.browse.ProjectBrowserHelper;

class VideoRecordInspec {

    private static final String ATTRIBUTE_NO_NAMESPACE_SCHEMA_LOCATION = "noNamespaceSchemaLocation";

    private static final String NAMESPACE_SCHEMA_INSTANCE = "http://www.w3.org/2001/XMLSchema-instance";

    private static final String ATTRIBUTE_NAME_RSUITE_ID = "rsuiteId";
                                                             
    private static final String NAMESPACE_RSUITE_METADATA = "http://www.rsuitecms.com/rsuite/ns/metadata";

    private static final String ELEMENT_NAME_INSPEC_VIDEO = "VideoInspec";
    
    private static final String ELEMENT_NAME_INSPEC_IETTV = "IETTV-Inspec";

    private ProjectBrowserHelper browserHelper;
    
    VideoRecordInspec(ProjectBrowserHelper browserHelper) {
		super();
		this.browserHelper = browserHelper;
	}

	public boolean mergeVideoInspec(ManagedObjectService moService, User user, ContentAssembly videoRecordContainer,
            ManagedObject videoMetadataMo) throws RSuiteException {
        ManagedObject videoInspecMo = getVideoInspecMo(videoRecordContainer);
        
        if (videoInspecMo == null){
            return false;
        }
        
        Element mergedVideoElement = mergeVideoInspecElement(videoMetadataMo.getElement(), videoInspecMo.getElement());
        
        updateVideoMetadaMo(moService, user, videoMetadataMo, mergedVideoElement);
        
        return true;
    }

    protected boolean hasVideoInspec(ExecutionContext context, User user, ContentAssembly videoRecordContainer) throws RSuiteException{
        
        ManagedObject videoInspecMo = getVideoInspecMo(videoRecordContainer);
        if (videoInspecMo == null){
            return false;
        }
        
        return true;
    }

    private ManagedObject getVideoInspecMo(
            ContentAssembly videoRecordContainer) throws RSuiteException {
        ManagedObject videoInspecMo =
                browserHelper.getChildMoByNodeName(videoRecordContainer,
                        ELEMENT_NAME_INSPEC_VIDEO, ELEMENT_NAME_INSPEC_IETTV);

        return videoInspecMo;
    }

    protected Element mergeVideoInspecElement(Element videoElement, Element videoInspecElement) {
        Element existingVideoInspecElement = getExistingVideoInspecElement(videoElement);

        Element newVideoInspecElement = importInspecElement(videoElement, videoInspecElement);
        newVideoInspecElement.removeAttributeNS(NAMESPACE_RSUITE_METADATA, ATTRIBUTE_NAME_RSUITE_ID);
        newVideoInspecElement.removeAttributeNS(NAMESPACE_SCHEMA_INSTANCE, ATTRIBUTE_NO_NAMESPACE_SCHEMA_LOCATION);
        
        if (existingVideoInspecElement != null) {
            restoreRSuiteId(newVideoInspecElement, existingVideoInspecElement);
            videoElement.replaceChild(newVideoInspecElement, existingVideoInspecElement);
        } else {
            videoElement.appendChild(newVideoInspecElement);
        }
        
        return videoElement;
    }


    private void restoreRSuiteId(Element newVideoInspecElement, Element existingVideoInspecElement) {
        Attr rsuiteItAtt = existingVideoInspecElement.getAttributeNodeNS(NAMESPACE_RSUITE_METADATA, ATTRIBUTE_NAME_RSUITE_ID);
        Attr newRsuiteItAtt = (Attr)rsuiteItAtt.cloneNode(true);
        newVideoInspecElement.setAttributeNodeNS(newRsuiteItAtt);
    }


    private Element getExistingVideoInspecElement(Element videoElement) {
        NodeList childNodes = videoElement.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node childNode = childNodes.item(i);

            if (isInspecElement(childNode)) {
                return (Element)childNode;
            }
        }
        return null;
    }


    private boolean isInspecElement(Node childNode) {
        return childNode instanceof Element && isInspecElementName( ((Element)childNode));
    }
    
    private boolean isInspecElementName(Element element){
    	String name = element.getLocalName();
    	return (ELEMENT_NAME_INSPEC_VIDEO.equals(name) || ELEMENT_NAME_INSPEC_IETTV.equals(name));
    }


    private Element importInspecElement(Element videoElement, Element videoInspecElement) {
        Document videoDocument = videoElement.getOwnerDocument();
        Node newVideoInspecElement = videoDocument.importNode(videoInspecElement, true);
        return (Element)newVideoInspecElement;
    }
    
    private void updateVideoMetadaMo(ManagedObjectService managedObjectService, User user,
            ManagedObject videoMetadataMo, Element mergedVideoElement) throws RSuiteException,
            ValidationException {

        String videoMoId = videoMetadataMo.getId();
        managedObjectService.checkOut(user, videoMoId);
        
        ObjectSource objectSource = new XmlObjectSource(mergedVideoElement);
        managedObjectService.update(user, videoMetadataMo.getId(), objectSource, null);
        
        ObjectCheckInOptions checkInOptions = new ObjectCheckInOptions();
        checkInOptions.setVersionNote("Merged inspec metadata");
        checkInOptions.setVersionType(VersionType.MAJOR);
        
        managedObjectService.checkIn(user, videoMoId, checkInOptions);
    }

}
