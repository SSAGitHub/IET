package org.theiet.rsuite.domain.classloader;

import java.io.File;

import com.reallysi.rsuite.api.RSuiteException;

public class CustomLibraryClassLoader extends CustomClassLoader {

	public CustomLibraryClassLoader(ClassLoader parentClassLoader, File rsuiteHomeFolder) throws RSuiteException {
		super(parentClassLoader, new File(rsuiteHomeFolder, "customLibrary"));
	}
}
