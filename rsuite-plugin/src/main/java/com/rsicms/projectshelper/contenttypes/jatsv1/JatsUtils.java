package com.rsicms.projectshelper.contenttypes.jatsv1;

import com.reallysi.rsuite.api.ManagedObject;
import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.SchemaInfo;

public class JatsUtils {

    private static final String ELEMENT_NAME_ARTICLE = "article";

    private static final String PUBLIC_ID_JATS =
            "-//NLM//DTD JATS (Z39.96) Journal Publishing DTD v1.0 20120330//EN";

    public static boolean isJatsArticle(ManagedObject mo)
            throws RSuiteException {

        if (mo.isNonXml() || mo.isAssemblyNode()) {
            return false;
        }

        return useJatsPublishDtd(mo)
                && ELEMENT_NAME_ARTICLE.equals(getDocumentElementName(mo));
    }

    public static boolean useJatsPublishDtd(ManagedObject mo) {
        SchemaInfo schemaInfo = mo.getSchemaInfo();
        String publicId = schemaInfo.getPublicId();

        return PUBLIC_ID_JATS.equals(publicId);
    }

    private static String getDocumentElementName(ManagedObject mo)
            throws RSuiteException {       
        return mo.getElement().getLocalName();
    }
}
