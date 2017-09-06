/**
 * Script to import a directory tree into RSuite, recreating the
 * directory structure in RSuite using folders and content assembly nodes.
 * 
 */

import com.reallysi.rsuite.client.api.*
import com.reallysi.rsuite.remote.api.*

def projectDir = new File(scriptFile.absolutePath).parentFile.parentFile.parentFile
def sampleDataDir = new File(projectDir, 'sample_data');
initialBrowseTreeDir = new File(sampleDataDir, 'Practitioner');
initalContentExist = false;

ignoredDirectoriesRegex = ~/(^\.svn)|(^CVS)/;
ignoredFilesRegex = ~/^_rsuiteNodeType|^\.|^__MAC|^~/;

xmlExtensions = [
	".xml",
	".nxml",
	".dita",
	".ditamap"
]; // Modify this list as needed.

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

/**
 * Gets the assembly node type: folder, ca, canode, or any user-defined type.
 * For any type other than canode, ca CA is created with the specified type.
 * For canode, a canode is created.
 * @param dirFile
 * @return The CA type.
 */
def getDirectoryNodeType(dirFile) {
	def nodeType = "ca"; // Assume initial folder is root, meaning it should not itself be reflected in the browse tree.
	if (dirFile.getName() == 'initial_browse_tree') {
		nodeType = 'root';
	}

	if (dirFile.name =~ /_rsuiteNodeType_([^\.]+)/) {
		matcher = ( dirFile.name =~ /_rsuiteNodeType_([^\.]+)/);
		nodeType = matcher[0][1];
	}
		
	println "nodeType=${nodeType}";
	return nodeType;
}

def getRSuiteNodeTypeFile(File dirFile) {

	def resultFile = null;

	def foundNodeType = false;

	dirFile.listFiles().toList().sort{ it.name }.each  {  file ->

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
		println "\n + [INFO] setPermissions(): Failed to construct properties from data file ${dirFile.name}/${dataFile.name}: ${e}";
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
		println "\n + [INFO] setLayeredMetadata(): No directory configuration file for directory ${dirFile.getAbsolutePath()}";
		return;
	}



	def props = null;
	try {
		if (dataFile.length() > 0) {
			props = new ConfigSlurper().parse(dataFile.toURL());
		}
	} catch (e) {
		println "\n + [WARN] setLayeredMetadata(): Failed to construct properties from data file ${dirFile.name}/${dataFile.name}: ${e}";
		return;
	}

	println " + [DEBUG] setLayeredMetadata(): props=${props}";

	if (props != null) {
		if (props.lmd != null) {
			println "\n + [INFO] Setting LMD for MO ${id}...";
			props.lmd.each { key, value ->
				println " + [INFO] Setting LMD '${key}' to '${value}'";
				try {
					rsuite.addMetaData(id, key, value);
				} catch (e) {
					println "\n + [WARN] setLayeredMetadata(): Failed to add metadata ${key}: ${e}";
				}
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
		println "\n + [warn] createDynamicAssemblyNodeFromDirectory(): Failed to construct properties from data file ${dirFile.name}/${dataFile.name}: ${e}";
		return;
	}
	def query = props.query;
	if ("" == query) {
		throw new RuntimeException("No 'query' property in RSuite node type file ${file.absolutePath}. Properties are: ${props}");
	}

	rsuite.createDynamicContentAssemblyNode(parentMoid, dirFile.name, query);
}

def createContainerIfNotExist(parentPath, parentId, containerName, type, lmdMap, silentIfExists){
	
	def containerPath = parentPath + containerName;
	
	def folderId = null;
	
	infos = rsuite.findObjectForPath(containerPath);
	
	if (infos == null){
       
		def folderOptions = new Options();
		
		folderOptions.add("type", type);
		
		if (silentIfExists != null) {
			folderOptions.add("silentIfExists", silentIfExists);
		}
		
		result = rsuite.createContentAssembly(parentId, containerName, folderOptions);
		folderId = result["moid"];
		println "[INFO] Created container " + containerName + " " + folderId;
       
	}else{
		if (infos.entryList != null){
			for (entry in infos.entryList){
				
				if (entry.key.equals("moid")){
					folderId = entry.value
					break
				}
			}
		}
		
		println "[INFO] Container " + containerPath +  "  already exist";
	}
	
	if (folderId != null && lmdMap != null){
		lmdMap.each { key, value ->
			println " + [INFO] Setting LMD '${key}' to '${value}'";
			try {
			  rsuite.setMetaData(folderId, key, value);
			 } catch (e) {
			  println "\n + [WARN] setLayeredMetadata(): Failed to add metadata ${key}: ${e}";
			}
		}
	}
       
	return folderId;
}

/**
 * Create nodes from directory.
 */
def createBrowseTreeNode(dirFile, parentMoid, level) {
	println "Creating browse tree node from file ${dirFile}...";
	def caType = getDirectoryNodeType(dirFile);
	
	println  "caType: ${caType}";
	println  "dirFile: ${dirFile.getAbsolutePath()}";
	
	def moidCA = null;
	def nodeType = "ca"; // Default

	switch (caType) {
		case "root":
			break;

		case "dynamicCa":
			createDynamicAssemblyNodeFromDirectory(dirFile, parentMoid, level);
			return;

		case "canode":
			nodeType = "canode";
		case "template":
			nodeType = "template";
			parentName = dirFile.parentFile.name;
			def containerName = dirFile.absolutePath.replace(initialBrowseTreeDir.absolutePath,"").replaceAll(/_rsuiteNodeType_([^\.]+)/,"").replace("\\", "").replace("/", "");
			moidCA = createContainerIfNotExist("/" + parentName + "/", parentMoid, containerName, caType, ["templateId" : "StandardTemplates"], null);
			break;
		case "ca":
		default:
			def props = null;
			def dataFile = getRSuiteNodeTypeFile(dirFile);
			try {

				if (dataFile != null) {
					props = new ConfigSlurper().parse(dataFile.toURL());
					if (props.nodeProps != null) {
						props.nodeProps.each { key, value ->
							if (key == "nodeType") {
								nodeType = value;
							}

						}
					}

				}
			} catch (e) {
				println "\n + [WARN] createBrowseTreeNode(): Failed to construct properties from data file ${dirFile.name}/${dataFile.name}: ${e}";
			}

			println " + [INFO] Constructing new node of type ${nodeType}... for parent id ${parentMoid}";

			def caOptions = new Options();
			caOptions.add( "silentIfExists", "true");
			caOptions.add( "type", caType);
			if (nodeType == "ca") {
				moidCA = createContainerIfNotExist(parentMoid, parentMoid, dirFile.name, caType, null, "true");
//				def result = rsuite.createContentAssembly( parentMoid, dirFile.name, caOptions);
//				moidCA = result[ "moid"];
			} else {
				def result = rsuite.createContentAssemblyNode( parentMoid, dirFile.name, caOptions);
				moidCA = result[ "moid"];
			}

			setPermissions(moidCA, dirFile);
			println " + [DEBUG] Calling setLayeredMetadata()..."
			setLayeredMetadata(moidCA, dirFile);

			println "\n + [INFO] Content assembly/assembly node created as moid [${moidCA}]";
	}

	processFilesInAssemblyDir(dirFile, moidCA, level++);
}

def processFilesInAssemblyDir(dirFile, parentMoid, level) {
	  println " + [DEBUG] processFilesInAssemblyDir(): Starting, parentMoid='${parentMoid}'...";
	  
	dirFile.listFiles().toList().sort(){a,b -> if ( a.isDirectory() && !b.isDirectory()) {
			return -1}else if ( !a.isDirectory() && b.isDirectory()){ return 1} else  {return a.name.compareTo(b.name)} }.each {  file ->
		
		if (file.isDirectory()) {
			if (!(file.name =~ ignoredDirectoriesRegex)) {
				
				def rsuitePath = file.absolutePath.replace(basePath,"").replaceAll(/_rsuiteNodeType_([^\.]+)/,"");				
				infos = rsuite.findObjectForPath(rsuitePath);
				
				if (infos == null){
					createBrowseTreeNode(file, parentMoid, level++);
				}else{
					println "Container ${rsuitePath} alrady exist skipping processing for the folder"
					initalContentExist = true;
				}
				
			}
		} else {
			if (!(file.name =~ ignoredFilesRegex)) {
				loadFile(file, parentMoid, level);
			} else {
				 println " + [DEBUG] processFilesInAssemblyDir(): skipping file";
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

copy = { File src,File dest->
	
	   def input = src.newDataInputStream()
	   def output = dest.newDataOutputStream()
	
	   output << input
	
	   input.close()
	   output.close()
   }


rsuite.login()

def dirToImport = initialBrowseTreeDir;
basePath = dirToImport.parentFile.absolutePath
def targetFolder = "/";

if (!dirToImport.exists()) {
	println "Folder \"" + dirToImport.getAbsolutePath() + "\" does not exist";
	return;
}

println "Importing ${dirToImport.name} to RSuite folder \"${targetFolder}\"...";

createBrowseTreeNode( dirToImport, targetFolder, 0);

println "Content exist ${initalContentExist}"
println "Insgesting sample sample maps to hotfloder ${hotfoldersBasePath}";


if (!initalContentExist){
def hotfoldersDir = new File(hotfoldersBasePath.trim())
def ingestionDir = new File(sampleDataDir, "ingestion")

ingestionDir.eachFile  {  hotfolder ->
	hotfolder.eachFile { file ->
		
		File dir = new File(hotfoldersDir, hotfolder.getName());
		
		copy(file,new File(dir, file.getName()))
		println "Copied ${file.getName()} ---> ${dir.getAbsolutePath()} "			
	}
}
}


rsuite.logout();