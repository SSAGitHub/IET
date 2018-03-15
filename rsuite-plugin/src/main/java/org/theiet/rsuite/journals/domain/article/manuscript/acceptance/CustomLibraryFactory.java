package org.theiet.rsuite.journals.domain.article.manuscript.acceptance;

import static org.theiet.rsuite.utils.ExceptionUtils.createRsuiteException;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;

import com.reallysi.rsuite.api.RSuiteException;
import com.rsicms.projectshelper.classloader.ParentLastURLClassLoader;

public class CustomLibraryFactory {

	private IScholarOnePdfTransformer instance;

	private static CustomLibraryFactory factory;

	private static String CLASS_NAME = "org.theiet.rsuite.journals.domain.article.manuscript.acceptance.ScholarOnePdfTransformer";

	CustomLibraryFactory(IScholarOnePdfTransformer instance) {
		this.instance = instance;
	}

	public static IScholarOnePdfTransformer getInstance() throws RSuiteException {

		if (factory == null) {
			throw new RSuiteException("Factory hasn't neen initialized");
		}

		IScholarOnePdfTransformer cache = factory.getCache();

		if (cache != null) {
			return cache;
		}

		throw new RSuiteException();
	}

	private IScholarOnePdfTransformer getCache() {
		return instance;
	}

	public static synchronized void reloadFactory(
			ClassLoader parentClassLoader, File libraryFolder)
			throws RSuiteException {
		ClassLoader classLoader = createClassLoader(parentClassLoader,
				libraryFolder);

		IScholarOnePdfTransformer scholarOnePdfTransformer = instantiateClass(
				classLoader, CLASS_NAME);

		factory = new CustomLibraryFactory(scholarOnePdfTransformer);
	}

	private static ClassLoader createClassLoader(ClassLoader parentClassLoader,
			File libraryFolder) throws RSuiteException {
		try {
			ParentLastURLClassLoader loader = new ParentLastURLClassLoader(
					parentClassLoader, new ArrayList<URL>());

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
		if (file.isFile()
				&& "jar".equalsIgnoreCase(FilenameUtils.getExtension(file
						.getName()))) {
			return true;
		}
		return false;
	}

	private static IScholarOnePdfTransformer instantiateClass(
			ClassLoader classLoader, String className) throws RSuiteException {
		try {
			Class<?> clazz = classLoader.loadClass(className);
			Constructor<?> constructor = clazz.getConstructor();
			return (IScholarOnePdfTransformer) constructor.newInstance();
		} catch (InstantiationException e) {
			throw createRsuiteException(e);
		} catch (ClassNotFoundException e) {
			throw createRsuiteException(e);
		} catch (SecurityException e) {
			throw createRsuiteException(e);
		} catch (NoSuchMethodException e) {
			throw createRsuiteException(e);
		} catch (IllegalArgumentException e) {
			throw createRsuiteException(e);
		} catch (IllegalAccessException e) {
			throw createRsuiteException(e);
		} catch (InvocationTargetException e) {
			throw createRsuiteException(e);
		}
	}

}
