package org.theiet.rsuite.standards.domain.publish.mathml.mathmlToImage;

import static org.theiet.rsuite.standards.domain.publish.mathml.mathmlToImage.MathMlToImageConventerType.JEUCLID;

import java.util.HashMap;
import java.util.Map;

import org.theiet.rsuite.domain.classloader.CustomLibraryClassLoader;

import com.reallysi.rsuite.api.RSuiteException;

public class MathMlToImageConventerFactory {

	private static final String JEUCLID_MATHML_ENGINE_CLASS_NAME = "com.rsicms.external.mathml.image.jeuclid.JEuclidMathMlEngine";

	private Map<MathMlToImageConventerType, MathMlToImageConventer> conventerInstances;

	private static MathMlToImageConventerFactory instance;

	private MathMlToImageConventerFactory(Map<MathMlToImageConventerType, MathMlToImageConventer> conventerInstances) {
		this.conventerInstances = conventerInstances;
	}

	public static MathMlToImageConventer getConventer(MathMlToImageConventerType conventerType) throws RSuiteException {

		if (instance == null) {
			throw new RSuiteException("Factory hasn't neen initialized");
		}

		Map<MathMlToImageConventerType, MathMlToImageConventer> cache = instance.getConventerCache();

		if (cache.containsKey(conventerType)) {
			return cache.get(conventerType);
		}

		throw new RSuiteException("Convert " + conventerType + "is not loaded");
	}

	private Map<MathMlToImageConventerType, MathMlToImageConventer> getConventerCache() {
		return conventerInstances;
	}

	public static synchronized void reloadFactory(CustomLibraryClassLoader customLibraryClassLoader)
			throws RSuiteException {

		Map<MathMlToImageConventerType, MathMlToImageConventer> conventerInstances = new HashMap<MathMlToImageConventerType, MathMlToImageConventer>();

		addConventer(customLibraryClassLoader, conventerInstances, JEUCLID, JEUCLID_MATHML_ENGINE_CLASS_NAME);

		instance = new MathMlToImageConventerFactory(conventerInstances);
	}

	private static void addConventer(CustomLibraryClassLoader customLibraryClassLoader,
			Map<MathMlToImageConventerType, MathMlToImageConventer> conventerInstances, MathMlToImageConventerType type,
			String className) throws RSuiteException {
		MathMlToImageConventer converter = (MathMlToImageConventer) customLibraryClassLoader
				.instantiateClass(className);
		conventerInstances.put(type, converter);
	}

}
