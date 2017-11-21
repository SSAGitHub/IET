/**
 * Script to import a directory tree into RSuite, recreating the
 * directory structure in RSuite using folders and content assembly nodes.
 * 
 */

import com.reallysi.rsuite.client.api.*
import com.reallysi.rsuite.remote.api.*

//def projectDir = new File(scriptFile.absolutePath).parentFile.parentFile.parentFile
def projectDir = new File("setup")
def sampleDataDir = new File(projectDir, 'sample_data');
localResources = new File(sampleDataDir, 'onix');
xmlExtensions = [".xml", ".nxml", ".dita", ".ditamap"];

def getLocalResourceAsFile (resource) {
	localResourceAsFile = new File(localResources.getAbsolutePath() + "\\" + resource);

	return localResourceAsFile;
}

def validateResource (file) {
	if (!file.exists()) {
		println "Resource \"" + file.getAbsolutePath() + "\" does not exist";
		return;
	}
}

def fileIsXml(file) {
	def isXml = false;
	xmlExtensions.each {
		if (file.getName().endsWith(it))
			isXml = true;
	}
	return isXml;
}

def createContainerIfNotExist(parentPath, parentId, containerName, type, lmdMap){
	
	def containerPath = parentPath + containerName;
	
	def folderId = null;
	
	infos = rsuite.findObjectForPath(containerPath);
	
	if (infos == null){
   
		def folderOptions = new Options();
		
		folderOptions.add("type", type);
		
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

def getNodeType(file) {
	
	def nodeType = "ca";
	if (!file.isDirectory()) {
		if (fileIsXml(file)) {
			nodeType = "mo";
		} else {
			nodeType = "mononxml";
		}
	}
		
	println "nodeType=${nodeType}";
	return nodeType;
}

/**
 * revisionType - should be 2 for Major, or 1 for Minor
 */
def addMetaData (moid, lmdName, lmdValue, versionNote, revisionType) {
	rsuite.checkOut(moid);
	rsuite.setMetaData(moid, lmdValue, lmdName);
	rsuite.checkIn(moid, versionNote, revisionType);	
}

def addMetaData (moid, lmdName, lmdValue) {
	addMetaData(moid, lmdValue, lmdName, null, 1);
}

def addMetaData(moid, lmdList) {
	def lmdSet= lmdList.entrySet()
	lmdSet.each {
		addMetaData (moid, it.key, it.value);
	}
}

def loadFile(file, lmdList, parentMoid, isXml) {
	print "Loading file: ${file.getName()}..."

	try {
		if (isXml) {
			result = rsuite.loadXmlFromFile(file, true, true);
		} else {
			result = rsuite.loadNonXmlFromFile(file, true, true);
		}
		def moid = result.getEntryValue("moid")
		println "load as moid [${moid}]"

		rsuite.addResourceToAssembly(parentMoid, moid)
		
		addMetaData(moid, lmdList);
	} catch (Exception e) {
		println "Exception loading file: ${e.getMessage()}";
	}
}

def createBrowseTreeNode (file, lmdList, parentMoId) {
	validateResource(file);
	
	println "Creating browse tree node from file ${file}...";
	def nodeType = getNodeType(file);
	
	switch (nodeType) {
		case "ca":
		// TODO Create mechanism to upload ca
			break;
		case "mo":
			loadFile(file, lmdList, parentMoId, true);
			break;
		case "mononxml":
			loadFile(file, lmdList, parentMoId, false);
			break;
	}
	
}

def defaultTemapleExist(onixMoId, lmdList) {
	defaultTemapleFound = false;
	onixChildrenInfo = rsuite.listContentAssemblyChildrenInfos(onixMoId);
	onixChildrenInfo.each {
		def onixChildInfo = rsuite.getInfo(it.refId);

		if (onixChildInfo.objectType == "mo") {
			def onixChildLmd = rsuite.getMetaData(it.refId);
			onixChildLmd.each {
				lmdName = it.name;
				lmdValue = it.value;
				lmdList.each {
					if (lmdName == it.key && lmdValue == it.value) {
						defaultTemapleFound = true;
					}
				}
				
			}			
		}
	}

	return defaultTemapleFound;
}

def importDefaultOnixTemplate (onixMoId) {
	def lmdList = ["onix_configuration_type" : "template"];
	
	if (!defaultTemapleExist(onixMoId, lmdList)) {
		def localResourceAsFile = getLocalResourceAsFile("theiet-onix-template.xml");
		createBrowseTreeNode(localResourceAsFile, lmdList, onixMoId);
	} else {
		println "[INFO] Onix template will  not be added. There is a default onix template already."
	}
}

rsuite.login();

println "[INFO] Importing content to onix";

def onixMoId = createContainerIfNotExist("/", "/",  "Onix", "onixConfigurations", null);
importDefaultOnixTemplate(onixMoId)


rsuite.logout();