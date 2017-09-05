package com.rsicms.projectshelper.dom;

import java.util.concurrent.*;

import javax.xml.parsers.*;

public class DocumentBuilderPool {

	private static int POOL_SIZE = 20;

	private BlockingQueue<DocumentBuilder> documentBuilderPool = new LinkedBlockingQueue<>(POOL_SIZE);

	private static DocumentBuilderPool instance = new DocumentBuilderPool();

	public DocumentBuilderPool() {

		DocumentBuilderFactory documentBuilderFactory = createDocumentBuilderFactory();

		for (int i = 0; i < POOL_SIZE; i++) {
			DocumentBuilder documentBuilder = createDocumentBuilder(documentBuilderFactory);
			documentBuilderPool.add(documentBuilder);
		}

	}

	public DocumentBuilder acquireDocumentBuilder() {
		try {
			return documentBuilderPool.poll(10, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			return createAdditionalDocumentBuilder();
		}
	}

	public void releaseDocumentBuilder(DocumentBuilder documentBuilder) {
		documentBuilderPool.offer(documentBuilder);
	}

	private DocumentBuilder createAdditionalDocumentBuilder() {
		DocumentBuilderFactory documentBuilderFactory = createDocumentBuilderFactory();
		
		return createDocumentBuilder(documentBuilderFactory);

	}

	private DocumentBuilderFactory createDocumentBuilderFactory() {
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		documentBuilderFactory.setNamespaceAware(true);
		return documentBuilderFactory;
	}

	private DocumentBuilder createDocumentBuilder(DocumentBuilderFactory documentBuilderFactory) {
		try {
			return documentBuilderFactory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			throw new RuntimeException("Unable to create document builder pool", e);
		}
	}

	public static DocumentBuilderPool getInstance() {
		return instance;
	}
}
