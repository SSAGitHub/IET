package org.theiet.rsuite.standards.domain.publish.mathml.mathmlToImage;

import static org.theiet.rsuite.standards.domain.publish.mathml.mathmlToImage.MathMlToImageConventerType.JEUCLID;
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

public class MathMlToImageConventerFactory {

	private Map<MathMlToImageConventerType, MathMlToImageConventer> conventerInstances;

	private static MathMlToImageConventerFactory instance;

	private MathMlToImageConventerFactory(
			Map<MathMlToImageConventerType, MathMlToImageConventer> conventerInstances) {
		this.conventerInstances = conventerInstances;
	}

	public static MathMlToImageConventer getConventer(
			MathMlToImageConventerType conventerType) throws RSuiteException {
		
		if (instance == null){
			throw new RSuiteException("Factory hasn't neen initialized");	
		}
		
		
		Map<MathMlToImageConventerType, MathMlToImageConventer> cache = instance
				.getConventerCache();

		if (cache.containsKey(conventerType)) {
			return cache.get(conventerType);
		}

		throw new RSuiteException("Convert " + conventerType + "is not loaded");
	}

	private Map<MathMlToImageConventerType, MathMlToImageConventer> getConventerCache() {
		return conventerInstances;
	}

	public static synchronized void reloadFactory(
			ClassLoader parentClassLoader, File libraryFolder)
			throws RSuiteException {

		ClassLoader classLoader = createClassLoader(parentClassLoader,
				libraryFolder);

		Map<MathMlToImageConventerType, MathMlToImageConventer> conventerInstances = new HashMap<MathMlToImageConventerType, MathMlToImageConventer>();

		addConventer(classLoader, conventerInstances, JEUCLID,
				"com.rsicms.external.mathml.image.jeuclid.JEuclidMathMlEngine");

		instance = new MathMlToImageConventerFactory(conventerInstances);
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

	private static MathMlToImageConventer instantiateConverter(
			ClassLoader classLoader, String className) throws RSuiteException {

		try {
			Class<?> clazz = classLoader.loadClass(className);

			Constructor<?> constructor = clazz.getConstructor();

			return (MathMlToImageConventer) constructor.newInstance();

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

	private static void addConventer(
			ClassLoader classLoader,
			Map<MathMlToImageConventerType, MathMlToImageConventer> conventerInstances,
			MathMlToImageConventerType type, String className)
			throws RSuiteException {
		MathMlToImageConventer converter = instantiateConverter(classLoader,
				className);
		conventerInstances.put(type, converter);
	}

}
