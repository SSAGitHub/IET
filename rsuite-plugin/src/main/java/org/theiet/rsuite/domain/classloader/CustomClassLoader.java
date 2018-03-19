package org.theiet.rsuite.domain.classloader;

import static org.theiet.rsuite.utils.ExceptionUtils.createRsuiteException;

import java.io.File;
import java.lang.reflect.Constructor;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.apache.commons.io.FilenameUtils;

import com.reallysi.rsuite.api.RSuiteException;
import com.rsicms.projectshelper.classloader.ParentLastURLClassLoader;

public class CustomClassLoader {

	private ClassLoader classLoader;

	public CustomClassLoader(ClassLoader parentClassLoader, File libraryFolder) throws RSuiteException {
		this.classLoader = createClassLoader(parentClassLoader, libraryFolder);
	}

	private static ClassLoader createClassLoader(ClassLoader parentClassLoader, File libraryFolder)
			throws RSuiteException {
		try {
			ParentLastURLClassLoader loader = new ParentLastURLClassLoader(parentClassLoader, new ArrayList<URL>());

			for (File file : libraryFolder.listFiles()) {

				if (isJarFile(file)) {

					loader.addJarToClasspath(file.getAbsolutePath());
				}
			}
			return loader;
		} catch (MalformedURLException e) {
			throw createRsuiteException(e);
		}
	}

	private static boolean isJarFile(File file) {
		if (file.isFile() && "jar".equalsIgnoreCase(FilenameUtils.getExtension(file.getName()))) {
			return true;
		}
		return false;
	}

	public Object instantiateClass(String className) throws RSuiteException {
		try {
			Class<?> clazz = classLoader.loadClass(className);
			Constructor<?> constructor = clazz.getConstructor();
			return constructor.newInstance();
		} catch (Exception e) {
			throw createRsuiteException(e);
		}
	}
}
