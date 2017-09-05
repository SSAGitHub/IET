package com.rsicms.test;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;

import org.w3c.dom.Document;

import com.rsicms.projectshelper.dom.DocumentBuilderPool;

public class TestHelper {

	private static DocumentBuilderPool documentBuilderPool = DocumentBuilderPool.getInstance();
	
	private TestHelper(){
	}
	
    public static File getTestFile(Class<?> clazz, String fileName) {
        String packageName = clazz.getPackage().getName();
        packageName = packageName.replace(".", "/");
       
        return new File("src/test/resources/" + packageName + "/" + fileName);
    }
    
    public static Document getTestDocument(Class<?> clazz, String fileName)
			throws Exception {
	
    	
    	DocumentBuilder documentBuilder = documentBuilderPool.acquireDocumentBuilder();
    	try{
    		File testFile = getTestFile(clazz, fileName);
    		return documentBuilder.parse(testFile);
    	}finally{
    		documentBuilderPool.releaseDocumentBuilder(documentBuilder);
    	}
	}
}
