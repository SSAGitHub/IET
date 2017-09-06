package com.rsicms.rsuite.oxygen.iet.applet.extension;

import java.io.IOException;
import java.net.*;

import org.apache.commons.io.FilenameUtils;

import com.rsicms.rsuite.editors.oxygen.applet.common.cms.rmsuite.RSuiteURI;

public class IetStreamImageURLHandler extends URLStreamHandler {

    private RSuiteURI uri;
    
    public IetStreamImageURLHandler(RSuiteURI uri) {
        this.uri = uri;
    }

    @Override
    protected URLConnection openConnection(URL url) throws IOException {
        String path = url.getPath();
        
        if (!path.startsWith("/rsuite/rest/v2/content/binary/alias")){
            path = "/rsuite/rest/v2/content/binary/alias/" + path;
            path = FilenameUtils.normalize(path);
        }
        
        URL rsuilteURL = new URL(uri.getImagePreviewUriByAlias(path));
        
        return rsuilteURL.openConnection();
    }
    
    

}
