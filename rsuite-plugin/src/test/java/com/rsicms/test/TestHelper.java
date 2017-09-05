package com.rsicms.test;

import java.io.*;

import javax.xml.parsers.DocumentBuilder;

import org.apache.commons.io.IOUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSSerializer;

import com.reallysi.rsuite.api.RSuiteException;
import com.rsicms.projectshelper.dom.DocumentBuilderPool;

public class TestHelper {

	private static DocumentBuilderPool documentBuilderPool = DocumentBuilderPool.getInstance();

	private TestHelper() {
	}

	public static File getTestFile(Class<?> clazz, String fileName) {
		String packageName = clazz.getPackage().getName();
		packageName = packageName.replace(".", "/");

		return new File("src/test/resources/" + packageName + "/" + fileName);
	}

	public static Document getTestDocument(Class<?> clazz, String fileName) throws Exception {

		DocumentBuilder documentBuilder = documentBuilderPool.acquireDocumentBuilder();
		try {
			File testFile = getTestFile(clazz, fileName);
			return documentBuilder.parse(testFile);
		} finally {
			documentBuilderPool.releaseDocumentBuilder(documentBuilder);
		}
	}

	public static String getTestResourceAsString(Class<? extends Object> clazz, String resourceName)
			throws IOException {

		String packageName = clazz.getPackage().getName();
		String path = packageName.replace(".", "/") + "/" + resourceName;

		InputStream testResource = TestHelper.class.getClassLoader().getResourceAsStream(path);
		return IOUtils.toString(testResource);
	}

	public static InputStream getTestResources(Class<? extends Object> clazz, String resourceName) {

		String packageName = clazz.getPackage().getName();
		String path = packageName.replace(".", "/") + "/" + resourceName;

		return TestHelper.class.getClassLoader().getResourceAsStream(path);
	}

	public static void writeTestResourceToAFile(Class<? extends Object> clazz, String resourceName, File file) {

		InputStream inputStream = getTestResources(clazz, resourceName);
		try (FileOutputStream outputStream = new FileOutputStream(file)) {
			int read = 0;
			byte[] bytes = new byte[2048];

			while ((read = inputStream.read(bytes)) != -1) {
				outputStream.write(bytes, 0, read);
			}

		} catch (IOException e) {
			throw new RuntimeException(
					String.format("Unable to write resource %s to file %s", resourceName, file.getPath()), e);
		}
	}

	

	public static String serializeNodeToString(Node node) {
		Document document = node.getOwnerDocument();
		DOMImplementationLS domImplLS = (DOMImplementationLS) document.getImplementation();
		LSSerializer serializer = domImplLS.createLSSerializer();
		return serializer.writeToString(node);
	}

	
}
