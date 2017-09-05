package com.rsicms.projectshelper.classloader;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.List;

public class ParentLastURLClassLoader extends ClassLoader {

	private ChildURLClassLoader childClassLoader;

	synchronized public void addJarToClasspath(URL urlPath)
			throws MalformedURLException {
		childClassLoader.addURL(urlPath);
	}

	public void addJarToClasspath(String jarName) throws MalformedURLException {
		File filePath = new File(jarName);
		URI uriPath = filePath.toURI();
		addJarToClasspath(uriPath.toURL());
	}

	/**
	 * This class allows me to call findClass on a classloader
	 */
	private static class FindClassClassLoader extends ClassLoader {
		public FindClassClassLoader(ClassLoader parent) {
			super(parent);
		}

		@Override
		public Class<?> findClass(String name) throws ClassNotFoundException {
			return super.findClass(name);
		}
	}

	/**
	 * This class delegates (child then parent) for the findClass method for a
	 * URLClassLoader. We need this because findClass is protected in
	 * URLClassLoader
	 */
	private static class ChildURLClassLoader extends URLClassLoader {
		private FindClassClassLoader realParent;

		public ChildURLClassLoader(URL[] urls, FindClassClassLoader realParent) {
			super(urls, null);

			this.realParent = realParent;
		}

		public void addURL(URL url) {
			super.addURL(url);
		}

		@Override
		public Class<?> findClass(String name) throws ClassNotFoundException {
			try {
				return super.findClass(name);
			} catch (ClassNotFoundException e) {
				return realParent.loadClass(name);
			}
		}

		@Override
		public Class<?> loadClass(String name) throws ClassNotFoundException {
			try {

				return super.loadClass(name);
			} catch (ClassNotFoundException e) {
				return realParent.loadClass(name);
			}

		}

		@Override
		protected synchronized Class<?> loadClass(String name, boolean resolve)
				throws ClassNotFoundException {
			try {
				return super.loadClass(name, resolve);
			} catch (ClassNotFoundException e) {
				return realParent.loadClass(name);
			}
		}

	}

	public ParentLastURLClassLoader(ClassLoader parentClassLoader,
			List<URL> classpath) {
		super(parentClassLoader);

		URL[] urls = classpath.toArray(new URL[classpath.size()]);

		childClassLoader = new ChildURLClassLoader(urls,
				new FindClassClassLoader(this.getParent()));
	}

	@Override
	public Class<?> loadClass(String name) throws ClassNotFoundException {
		try {
			return childClassLoader.loadClass(name);
		} catch (ClassNotFoundException e) {
			return super.loadClass(name);
		}
	}

	@Override
	protected synchronized Class<?> loadClass(String name, boolean resolve)
			throws ClassNotFoundException {
		try {
			return childClassLoader.loadClass(name, resolve);
		} catch (ClassNotFoundException e) {
			return super.loadClass(name, resolve);
		}
	}

	@Override
	protected Class<?> findClass(String name) throws ClassNotFoundException {
		try {
			return childClassLoader.findClass(name);
		} catch (ClassNotFoundException e) {
			return super.findClass(name);
		}

	}

	@Override
	public Enumeration<URL> getResources(String name) throws IOException {
		Enumeration<URL> resources = childClassLoader.getResources(name);
		if (resources != null) {
			return resources;
		}

		return super.getResources(name);
	}

	@Override
	public URL getResource(String name) {
		URL resource = childClassLoader.getResource(name);
		if (resource != null) {
			return resource;
		}

		return super.getResource(name);
	}

	@Override
	public InputStream getResourceAsStream(String name) {
		InputStream resourceAsStream = childClassLoader
				.getResourceAsStream(name);

		if (resourceAsStream != null) {
			return resourceAsStream;
		}
		return super.getResourceAsStream(name);
	}

}