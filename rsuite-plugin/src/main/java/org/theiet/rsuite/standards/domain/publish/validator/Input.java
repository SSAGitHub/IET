package org.theiet.rsuite.standards.domain.publish.validator;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

import org.w3c.dom.ls.LSInput;

public class Input implements LSInput {

	private String baseURI;
	private InputStream byteStream;
	private boolean certifiedText;
	private Reader characterStream;
	private String encoding;
	private String publicId;
	private String stringData; 
	private String systemId;
	private BufferedInputStream inputStream;

	public Input(String publicId, String sysId, InputStream input) {
		this.publicId = publicId;
		this.systemId = sysId;
		this.inputStream = new BufferedInputStream(input);
	}
	
	public BufferedInputStream getInputStream() {
		return inputStream;
	}

	public void setInputStream(BufferedInputStream inputStream) {
		this.inputStream = inputStream;
	}
	
	@Override
	public String getPublicId() {
		return publicId;
	}

	@Override
	public void setPublicId(String publicId) {
		this.publicId = publicId;
	}

	@Override
	public String getBaseURI() {
		return baseURI;
	}

	@Override
	public InputStream getByteStream() {
		return byteStream;
	}

	@Override
	public boolean getCertifiedText() {
		return certifiedText;
	}

	@Override
	public Reader getCharacterStream() {
		return characterStream;
	}

	@Override
	public String getEncoding() {
		return encoding;
	}

	@Override
	public String getStringData() {
		synchronized (inputStream) {
			try {
				byte[] input = new byte[inputStream.available()];
				inputStream.read(input);
				String contents = new String(input);
				return contents;
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("Exception " + e);
				return null;
			}
		}
	}

	@Override
	public void setBaseURI(String baseURI) {
		this.baseURI = baseURI;
	}

	@Override
	public void setByteStream(InputStream byteStream) {
		this.byteStream = byteStream;
	}

	@Override
	public void setCertifiedText(boolean certifiedText) {
		this.certifiedText = certifiedText;
	}

	@Override
	public void setCharacterStream(Reader characterStream) {
		this.characterStream = characterStream;
	}

	@Override
	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	@Override
	public void setStringData(String stringData) {
		this.stringData = stringData;
	}

	@Override
	public String getSystemId() {
		return systemId;
	}

	@Override
	public void setSystemId(String systemId) {
		this.systemId = systemId;
	}

}
