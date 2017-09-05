/**
 * Script to import a directory tree into RSuite, recreating the
 * directory structure in RSuite using folders and content assembly nodes.
 * 
 */

    import com.reallysi.rsuite.client.api.*
    import com.reallysi.rsuite.remote.api.*

def projectDir = new File(scriptFile.absolutePath).parentFile.parentFile.parentFile
def sampleDataDir = new File(projectDir, 'sample_data');
def initialBrowseTreeDir = new File(sampleDataDir, 'initial_browse_tree');

ignoredDirectoriesRegex = ~/(^\.svn)|(^CVS)/;
ignoredFilesRegex = ~/^_rsuiteNodeType|^\.|^__MAC|^~/;

xmlExtensions = [".xml", ".nxml", ".dita", ".ditamap"]; // Modify this list as needed.

/**
 * Returns true if the file is an XML file, otherwise false;
 * 
 * 
 */
def fileIsXml(file) {
	 def isXml = false;
	 xmlExtensions.each {
		 if (file.getName().endsWith(it))
		 	isXml = true;
	 }
	 return isXml;
}

def getDirectoryNodeType(dirFile) {
  def nodeType = "root"; // Assume initial folder is root, meaning it should not itself be reflected in the browse tree.
  
  def nodeTypeFile = getRSuiteNodeTypeFile(dirFile);
  
  matcher = ( nodeTypeFile.name =~ /_rsuiteNodeType_([^\.]+)/);
  nodeType = matcher[0][1];
  
  return nodeType;
}

def getRSuiteNodeTypeFile(File dirFile) {

  def resultFile = null;

  def foundNodeType = false;

  dirFile.eachFile {  file ->
       
       if ( file.name =~ /_rsuiteNodeType_([^\.]+)/) {
          if (foundNodeType) {
             throw new RuntimeException("ERROR: Found multiple _rsuiteNodeType files in directory ${file.name}. You can have at most one such file.");
          }
          resultFile = file;
          foundNodeType = true;
      }
  }
  
  return resultFile;
}

def setPermissions(id, dirFile) {
	// println "\n + [DEBUG] setPermissions(): Starting, dirFile=${dirFile}";
	File dataFile = getRSuiteNodeTypeFile(dirFile);
	if (dataFile == null) {
		println "\n + [INFO] setPermissions(): No directory configuration file for directory ${dirFile.getAbsolutePath()}";
		return;
	}
	if (dataFile.length() == 0) {
		return;
	}
	def props = null;
	try {
		props = new ConfigSlurper().parse(dataFile.toURL());
	} catch (e) {
		println "\n + [INFO] Failed to construct properties from data file ${dirFile.name}/${dataFile.name}: ${e}";
		return;
	}
	if (props == null) {
		println "\n - [WARN] setPermissions(): No properties in directory configuration file ${dirFile.getName()}";
		return;
	}
	def acl = props.ACL;
	if (acl == null) {
		println "\n - [WARN] setPermissions(): no ACL property in directory configuration file ${dirFile.getName()}";
		return;
	}
	def roles = acl.roles;
	if (roles == null) {
		println "\n - [WARN] setPermissions(): no ACL.roles property in directory configuration file ${dirFile.getName()}";
		return;
	}
	roles.each { roleName, permissions ->
		println "\n + [INFO] Setting permissions for role ${roleName} to \"${permissions}\"...";
		def effectiveRoleName = ("any" == roleName ? "*" : roleName);
		rsuite.setACE(id, effectiveRoleName, permissions);
		
	}
		
}

def setLayeredMetadata(id, dirFile) {
	File dataFile = getRSuiteNodeTypeFile(dirFile);
	if (dataFile == null) {
		println "\n + [INFO] setPermissions(): No directory configuration file for directory ${dirFile.getAbsolutePath()}";
		return;
	}
	
	
	
	def props = null;
	try {
		if (dataFile.length() > 0) {
			props = new ConfigSlurper().parse(dataFile.toURL());
		}
	} catch (e) {
		println "\n + [INFO] Failed to construct properties from data file ${dirFile.name}/${dataFile.name}: ${e}";
		return;
	}

	if (props != null) {
		if (props.lmd != null) {
			println "\n + [INFO] Setting LMD for MO ${id}...";
			props.lmd.each { key, value ->
				println " + [INFO] Setting LMD '${key}' to '${value}'";
				rsuite.addMetaData(id, key, value);
				
			}
			println " + [INFO] LMD set";
		}
	}
		
}

def createRqlContentAssemblyNode(parentId, nodeDisplayName, query) {    
    println "\n + [INFO] createRqlContentAssemblyNode(): Creating query node \"" + nodeDisplayName + "\"...";
    
    RSuiteMap result = rsuite.
    createDynamicContentAssemblyNode(parentId, nodeDisplayName, query);
}
 

/**
 * Given an initial directory, use it to construct a new browse
 * tree under the specified parent folder. A folder of "/" indicates
 * the RSuite browse tree root.
 */
def createBrowseTreeFromFolder(dirFile, parentFolder, level) {
   // println " + [DEBUG] createBrowseTreeFromFolder(): Starting...";
   
   println " + [INFO] Creating browse tree from directory ${dirFile.name}...";

  def nodeType = getDirectoryNodeType(dirFile);
  // println " + [DEBUG] createBrowseTreeFromFolder(): Node type is '${nodeType}'";

  switch (nodeType) {
    case "ca":
       createCAHierarchyFromPath(dirFile, parentFolder, null, level++);
       break;
    case "dynamicCa":       
       throw new RuntimeException("Directory ${dirFile.name} is marked as a dynamnic CA. " +
                                  "You cannot have a dynamic node at the root level. It must be within a content assembly"); 
    case "folder":
      createFolderFromDirectory(dirFile, parentFolder, level);
      break;   
    case "root":      
    default:
      processFilesInFolderDir(dirFile, parentFolder, level++);
  }  
  
   println " + [INFO] Done";
}

def processFilesInFolderDir(dirFile, parentFolder, level) {
  // println " + [DEBUG] processFilesInFolderDir(): Starting, parentFolder='${parentFolder}'...";
  dirFile.eachFile {  file ->
       if (file.isDirectory()) {
          if (!(file.name =~ ignoredDirectoriesRegex)) {
            createBrowseTreeNodeFromFolderChild(file, parentFolder, level);
          }
       } else {
         if (!(file.name =~ ignoredFilesRegex)) {
           println " - [WARNING] Skipping file ${file.name} with a parent directory of node type folder";
         }
       } 
  }
}


def createFolderFromDirectory(dirFile, parentFolder, level) {
  // println " + [DEBUG] createFolderFromDirectory(): Starting...";
   print " + [INFO] ";(0..(level*2)).each { print " "} 
   print "Creating folder: ${dirFile.getName()}..."

  folder = rsuite.createFolder(parentFolder, dirFile.name);
  setPermissions(folder["name"],dirFile);
  
  // print "Done.\n";
  // println " + [DEBUG] createFolderFromDirectory(): folder=${folder}";
  processFilesInFolderDir(dirFile, folder["path"], level++);
}

/**
 * Create nodes from children of folder-type directories. Folders cannot
 * contain content MOs directly, on content assemblies and other folders.
 */
def createBrowseTreeNodeFromFolderChild(dirFile, parentFolder, level) {
  def nodeType = getDirectoryNodeType(dirFile);
  // println " + [DEBUG] createBrowseTreeNodeFromFolderChild(): Node type is '${nodeType}'";

  switch (nodeType) {
    case "ca":
       createContentAssemblyFromDirectory(dirFile, parentFolder, level);
       break;
    case "dynamicCa":       
       throw new RuntimeException("Directory ${dirFile.name} is marked as a dynamnic CA. " +
                                  "You cannot have a dynamic node within a folder. It must be within a content assembly"); 
    case "folder":
      createFolderFromDirectory(dirFile, parentFolder, level);
      break;   
    case "root":
    default:
      processFilesInAssemblyDir(dirFile, moid, level);
  }  
    
}  

def createDynamicAssemblyNodeFromDirectory(dirFile, parentMoid, level) {
  // println " + [DEBUG] createDynamicAssemblyNodeFromDirectory(): Starting...'";
  print " + [INFO] "; (0..(level*2)).each { print " "} 
  
  println "Creating dynamic assembly node ${dirFile.name}...";

  File dataFile = getRSuiteNodeTypeFile(dirFile);
  if (dataFile == null) {
	println "\n + [WARN] createDynamicAssemblyNodeFromDirectory(): No directory configuration file found for dirFile ${dirFile.getAbsolutePath()}";
	return;
  }
	if (dataFile.length() == 0) {
		println "\n + [WARN] createDynamicAssemblyNodeFromDirectory(): Directory configuration file is empty}";
		return;
	}
	def props = null;
	  try {
	  props = new ConfigSlurper().parse(dataFile.toURL());
  } catch (e) {
	println "\n + [INFO] Failed to construct properties from data file ${dirFile.name}/${dataFile.name}: ${e}";
	return;
  }
  def query = props.query;
  if ("" == query) {
     throw new RuntimeException("No 'query' property in RSuite node type file ${file.absolutePath}. Properties are: ${props}");
  }
  
  rsuite.createDynamicContentAssemblyNode(parentMoid, dirFile.name, query);
}

/**
 * Create nodes from children of assembly-type directories. Assemblies cannot
 * contain folders but can contain CAs, dynamic CAs, and files of any type.
 */
def createBrowseTreeNodeFromAssemblyChild(dirFile, parentMoid, level) {
  def nodeType = getDirectoryNodeType(dirFile);
  // println " + [DEBUG] createBrowseTreeNodeFromAssemblyChild(): Node type is '${nodeType}'";

  switch (nodeType) {
    case "dynamicCa":       
       createDynamicAssemblyNodeFromDirectory(dirFile, parentMoid, level);
       break;
    case "folder":
       throw new RuntimeException("Directory ${dirFile.name} is marked as a folder node type. " +
                                  "You cannot have a folder within a content assembly node."); 
    case "root":
       throw new RuntimeException("Directory ${dirFile.name} is marked as the root folder type " +
                                  "but is not the root directory"); 
    case "ca":
    default:
       // If folder is unmarked, assume it should be a CA node.
       createContentAssemblyNodeFromDirectory(dirFile, parentMoid, level);
       break;
  }  
	println "[6]";
	    
}  

def createContentAssemblyFromDirectory(dirFile, folderPath, level) {
  // println " + [DEBUG] createContentAssemblyFromDirectory(): Starting...";
   print "\n + [INFO] ";(0..(level*2)).each { print " "} 
   print "Creating content assembly: ${dirFile.getName()}..."

  def result = rsuite.createContentAssembly(folderPath, dirFile.name, true);
//println "\n + [DEBUG] result=${result}";
  def moid = result.getEntryValue("moid");
//println " + [DEBUG] moid=${moid}";
// println "\n + [DEBUG] calling setPermissions()...";
  setPermissions(moid,dirFile);
// println "\n + [DEBUG] calling setLayeredMetadata()...";
  setLayeredMetadata(moid, dirFile);  
  // println "Content assembly created as moid [${moid}]";

  processFilesInAssemblyDir(dirFile, moid, level++);
}

def createContentAssemblyNodeFromDirectory(dirFile, parentMoid, level) {
  // println " + [DEBUG] createContentAssemblyNodeFromDirectory(): Starting...";
   print "\n + [INFO] ";(0..(level*2)).each { print " "} 
   print "Creating assembly node: ${dirFile.getName()}..."

  def result = rsuite.createContentAssemblyNode(parentMoid, dirFile.name);
  def moid = result.getEntryValue("moid");
  setPermissions(moid,dirFile);
//println "\n + [DEBUG] calling setLayeredMetadata()...";
  setLayeredMetadata(moid, dirFile);  
  
  println "assembly node created as moid [${moid}]";

  processFilesInAssemblyDir(dirFile, moid, level++);
}

def processFilesInAssemblyDir(dirFile, parentMoid, level) {
  // println " + [DEBUG] processFilesInAssemblyDir(): Starting, parentMoid='${parentMoid}'...";
  
  dirFile.eachFile {  file ->
	
       if (file.isDirectory()) {
          if (!(file.name =~ ignoredDirectoriesRegex)) {
            createBrowseTreeNodeFromAssemblyChild(file, parentMoid, level++);
          }
       } else {
         if (!(file.name =~ ignoredFilesRegex)) {
            loadFile(file, parentMoid, level++);
         } else {
            // println " + [DEBUG] processFilesInAssemblyDir(): skipping file";      
         }
       } 
  }
}

def loadFile(file, parentMoid, level) {
   print "\n + [INFO] ";(0..(level*2)).each { print " "} 
   print "Loading file: ${file.getName()}..."
  
   def isXml = fileIsXml(file);
  
   try {
       if (isXml) {
           // println " + [DEBUG]   File is an XML document.";
           result = rsuite.loadXmlFromFile(file, true, true);
       } else {
           // println " + [DEBUG]   File is not XML.";
           result = rsuite.loadNonXmlFromFile(file, true, true);
       }
     def moid = result.getEntryValue("moid")
     println "load as moid [${moid}]"
  
     rsuite.addResourceToAssembly(parentMoid, moid) 
   } catch (Exception e) {
     println "Exception loading file: ${e.getMessage()}";
   }
}



rsuite.login()

def dirToImport = initialBrowseTreeDir
def targetFolder = "/";

//println "Specify folder to import: ";

//InputStreamReader reader = new InputStreamReader(System.in);
//def dirPath = reader.readLine();

//if (dirPath == null ) return;

//dirToImport = new File(dirPath);

if (!dirToImport.exists()) {
	println "Folder \"" + dirToImport.getAbsolutePath() + "\" does not exist";
	return;
}

// println "Specify RSuite folder to hold imported content [/]:"; 

// reader = new InputStreamReader(System.in);
// def temp = reader.readLine();
// if (temp != null && temp != "")

println "Importing ${dirToImport.name} to RSuite folder \"${targetFolder}\"...";

createBrowseTreeFromFolder(dirToImport, targetFolder, 0);