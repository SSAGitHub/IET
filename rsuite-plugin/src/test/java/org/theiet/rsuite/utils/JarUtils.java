package org.theiet.rsuite.utils;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

public class JarUtils {

	public static void main(String[] args) throws Exception {

		String expectedPluginId = "dita4publishers";
		String resource = "/toolkit_plugins/net.sourceforge.dita4publishers.common.xslt/xsl/lib/relpath_util.xsl";

		File libFolder = new File("java/lib");
		getResourceFromExternaPlugin(expectedPluginId, resource, libFolder);

	}

	public static byte[] getResourceFromExternaPlugin(String expectedPluginId,
			String resourcePath, File libFolder)
			throws ParserConfigurationException, SAXException, IOException,
			XPathExpressionException {
		Iterator<File> filetIt = FileUtils.iterateFiles(libFolder, null, true);

		while (filetIt.hasNext()) {
			File jarFile = (File) filetIt.next();
			if ("jar".equalsIgnoreCase(FilenameUtils.getExtension(jarFile
					.getName()))) {

				byte[] resourceBytes = null;

				byte[] bytes = extractMyMDBromJAR(jarFile.getAbsolutePath(),
						"rsuite-plugin.xml");

				if (bytes != null) {
					InputStream is = new ByteArrayInputStream(bytes);
					Document doc = getDocumentFromInputStream(is);

					String pluginId = doc.getDocumentElement().getAttribute(
							"id");

					if (expectedPluginId.equalsIgnoreCase(pluginId)) {

						resourceBytes = getResourceFromPluginJar(resourcePath,
								jarFile, doc);
						
						return resourceBytes;
					}
				}
			}

		}
		return null;
	}

	public static byte[] getResourceFromPluginJar(String resourcePath,
			File jarFile, Document doc) throws XPathExpressionException {
		byte[] resourceBytes;
		XPath xPath = XPathFactory.newInstance().newXPath();
		String expression = "//staticWebService";

		Element changeMarkupCount = (Element) xPath.compile(expression)
				.evaluate(doc, XPathConstants.NODE);

		String path = changeMarkupCount.getAttribute("path");
		path += resourcePath;
		path = path.replaceFirst("/", "");

		resourceBytes = extractMyMDBromJAR(jarFile.getAbsolutePath(), path);
		return resourceBytes;
	}

	public static byte[] extractMyMDBromJAR(String home, String source) {

		byte[] bytes = null;

		try {
			JarFile jar = new JarFile(home);
			ZipEntry entry = jar.getEntry(source);

			if (entry == null) {
				return null;
			}

			InputStream in = new BufferedInputStream(jar.getInputStream(entry));
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			byte[] buffer = new byte[2048];
			for (;;) {
				int nBytes = in.read(buffer);
				if (nBytes <= 0)
					break;
				out.write(buffer, 0, nBytes);
			}
			out.flush();
			out.close();
			in.close();

			bytes = out.toByteArray();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return bytes;
	}

	public static Document getDocumentFromInputStream(InputStream is)
			throws ParserConfigurationException, SAXException, IOException {

		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document xmlDocument = dBuilder.parse(is);
		return xmlDocument;
	}
}
