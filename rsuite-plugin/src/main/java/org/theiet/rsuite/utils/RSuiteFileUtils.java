package org.theiet.rsuite.utils;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.io.FilenameUtils;


public final class RSuiteFileUtils {

	private static final Set<String> XML_FILE_EXTENSIONS = new HashSet<String>();
	static {
		XML_FILE_EXTENSIONS.add("xml");
		XML_FILE_EXTENSIONS.add("dita");
		XML_FILE_EXTENSIONS.add("ditamap");
	}

	private RSuiteFileUtils() {
	}
	
	public static File getCommonFolder(File file1, File file2){
		
		String file1path = file1.getAbsolutePath();
		String file2path = file2.getAbsolutePath();
		
		String file1Prefix = FilenameUtils.getPrefix(file1path);
		String file2Prefix = FilenameUtils.getPrefix(file1path);
		
		if (!org.apache.commons.lang.StringUtils.equals(file1Prefix, file2Prefix)) {
			return null;
		}
		
		file1path = getPathWithoutPrefix(file1path);
		file2path = getPathWithoutPrefix(file2path);
		
		
		file1path = FilenameUtils.separatorsToUnix(file1path);
		file2path = FilenameUtils.separatorsToUnix(file2path);
		
		String[] file1PathParts = file1path.split("/");
		String[] file2PathParts = file2path.split("/");
		
		StringBuilder commonPath = new StringBuilder();
		
		for (int i = 0; i < file1PathParts.length; i++){
			String file1PathPart = file1PathParts[i];
			
			//System.out.println(file1PathPart);
			
			if (StringUtils.isBlank(file1PathPart)){
				continue;
			}
			
			if (i < file1PathParts.length && file1PathPart.equals(file2PathParts[i])){
				commonPath.append("/").append(file1PathPart);
			}else{
				break;
			}
		}
		
		if (commonPath.length() > 0){
			
			if (org.apache.commons.lang.StringUtils.isNotBlank(file1Prefix)) {
				commonPath.insert(0, file1Prefix);
			}
			return new File(commonPath.toString());
		}
		
		
		
		return null;
	}
	
	private static String getPathWithoutPrefix(String path) {
		
		String prefix = FilenameUtils.getPrefix(path);
		
		if (org.apache.commons.lang.StringUtils.isNotBlank(prefix)) {
			return path.substring(prefix.length());	
		}
		return path;
	}
	
	public static boolean isXmlFile(File file){
		
		String extension = FilenameUtils.getExtension(file.getName());
		
		if (extension != null && XML_FILE_EXTENSIONS.contains(extension.toLowerCase()) && file.isFile()){
			return true;
		}
		
		return false;
	}
	
	public static  boolean isNonXmlFile(File file){
		return !isXmlFile(file);
	}
}
