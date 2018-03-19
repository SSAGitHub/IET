package org.theiet.rsuite.journals.domain.article.manuscript.acceptance;

import org.theiet.rsuite.domain.classloader.CustomClassLoader;

import com.reallysi.rsuite.api.RSuiteException;

public class JournalCustomLibraryFactory {

	private IScholarOnePdfTransformer scholarOnePdfTransfomer;

	private static JournalCustomLibraryFactory factory;

	private static String CLASS_NAME = "org.theiet.rsuite.journals.domain.article.manuscript.acceptance.ScholarOnePdfTransformer";

	JournalCustomLibraryFactory(IScholarOnePdfTransformer instance) {
		this.scholarOnePdfTransfomer = instance;
	}

	public static IScholarOnePdfTransformer getInstance() throws RSuiteException {

		if (factory == null) {
			throw new RSuiteException("Factory hasn't neen initialized");
		}

		IScholarOnePdfTransformer instance = factory.getScholarOnePdfTransformer();

		if (instance != null) {
			return instance;
		}

		throw new RSuiteException("Scholar one pdf transfomer is not instantiated");
	}

	private IScholarOnePdfTransformer getScholarOnePdfTransformer() {
		return scholarOnePdfTransfomer;
	}

	public static synchronized void reloadFactory(CustomClassLoader customLibraryClassLoader) throws RSuiteException {

		IScholarOnePdfTransformer scholarOnePdfTransformer = (IScholarOnePdfTransformer) customLibraryClassLoader
				.instantiateClass(CLASS_NAME);
		factory = new JournalCustomLibraryFactory(scholarOnePdfTransformer);
	}

}
