package com.rsicms.rsuite.oxygen.iet.applet.extension.framework.dita;

import com.rsicms.rsuite.oxygen.iet.applet.extension.framework.dita.IetDITAUniqueAttributesRecognizer;

import ro.sync.ecss.extensions.api.AuthorExtensionStateListener;
import ro.sync.ecss.extensions.api.UniqueAttributesRecognizer;
import ro.sync.ecss.extensions.api.content.ClipboardFragmentProcessor;
import ro.sync.ecss.extensions.dita.DITAExtensionsBundle;
import ro.sync.ecss.extensions.dita.id.DITAUniqueAttributesRecognizer;

public class IetDITAExtensionsBundle extends DITAExtensionsBundle {

	private DITAUniqueAttributesRecognizer attributesRecognizer = new IetDITAUniqueAttributesRecognizer();
	
	@Override
	public UniqueAttributesRecognizer getUniqueAttributesIdentifier() {
		return attributesRecognizer;
	}
	
	@Override
	public AuthorExtensionStateListener createAuthorExtensionStateListener() {
		return attributesRecognizer;
	}
	
	@Override
	public ClipboardFragmentProcessor getClipboardFragmentProcessor() {
		return attributesRecognizer;
	}
}
