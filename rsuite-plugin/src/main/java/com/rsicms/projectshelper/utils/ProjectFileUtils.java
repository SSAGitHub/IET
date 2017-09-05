package com.rsicms.projectshelper.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.StringTokenizer;

import org.apache.commons.lang.StringUtils;
import org.theiet.rsuite.utils.DateUtils;

import com.reallysi.rsuite.api.extensions.ExecutionContext;

public class ProjectFileUtils {

  public static  String relativePath(File from, File to) {
    return relativePath(from, to, File.separatorChar);
  }

  public static String relativePath(File from, File to, char separatorChar) {
    String fromPath = from.getAbsolutePath();
    String toPath = to.getAbsolutePath();
    boolean isDirectory = from.isDirectory();
    return relativePath(fromPath, toPath, isDirectory, separatorChar);
  }

  static public String relativePath(String fromPath, String toPath,
      boolean fromIsDirectory, char separatorChar) {
	  
	  if (fromPath.equals(toPath)){
		  return "";
	  }
	  
    ArrayList<String> fromElements = splitPath(fromPath);
    ArrayList<String> toElements = splitPath(toPath);
    while (!fromElements.isEmpty() && !toElements.isEmpty()) {
      if (!(fromElements.get(0).equals(toElements.get(0)))) {
        break;
      }
      fromElements.remove(0);
      toElements.remove(0);
    }

    StringBuffer result = new StringBuffer();
    for (int i = 0; i < fromElements.size() - (fromIsDirectory ? 0 : 1); i++) {
      result.append("..");
      result.append(separatorChar);
    }
    for (String s : toElements) {
      result.append(s);
      result.append(separatorChar);
    }
    return result.substring(0, result.length() - 1);
  }

  private static ArrayList<String> splitPath(String path) {
    ArrayList<String> pathElements = new ArrayList<String>();
    for (StringTokenizer st = new StringTokenizer(path, File.separator); st
        .hasMoreTokens();) {
      String token = st.nextToken();
      if (token.equals(".")) {
        // do nothing
      } else if (token.equals("..")) {
        if (!pathElements.isEmpty()) {
          pathElements.remove(pathElements.size() - 1);
        }
      } else {
        pathElements.add(token);
      }
    }
    return pathElements;
  }

  public static String computeRelativePath(String contextPath,
			String exportPath) {
		File from = new File(contextPath);
		File to = new File(exportPath);
		return relativePath(from, to, File.separatorChar);
  }

	/**
	 * Get or create a directory within RSuite's temp directory.
	 * 
	 * @param context
	 * @return A sub-directory of RSuite's temp dir.
	 */
	public static File getTmpSubDir(ExecutionContext context) {
		return getTmpSubDir(context, null, new Date(), true);
	}
	
	/**
	 * Get or create a directory within RSuite's temp directory using the given directory name prefix and date.
	 * 
	 * @param context
	 * @param dirNamePrefix
	 * @param date
	 * @param create Submit true to instruct this method to attempt to create when it doesn't exist.
	 * @return A sub-directory of RSuite's temp dir which incorporates the given prefix and date in the directory name.
	 */
	public static File getTmpSubDir(
			ExecutionContext context,
			String dirNamePrefix,
			Date date,
			boolean create) {

		String datedPath = DateUtils.DATE_FORMAT_YYYYMMDDHHMMSS.format(date);

		File tmpDir = new File(
			context.getRSuiteServerConfiguration().getTmpDir(), 
			StringUtils.isNotBlank(dirNamePrefix) 
				? dirNamePrefix.concat(datedPath)
				: datedPath);
		
		if (create && !tmpDir.exists()){
			tmpDir.mkdirs();
		}
		
		return tmpDir;
	}

}