import com.reallysi.rsuite.client.api.*
import com.reallysi.rsuite.remote.api.*

rsuite.login();

def booksMoId = createContainerIfNotExist("/", "/",  "Practitioner", "standards", null);

def importMoId = createContainerIfNotExist("/", "/",  "Import", "");
createContainerIfNotExist("/Import/", importMoId, "Imported Maps", "");

createContainerIfNotExist("/", "/",  "DITA Hotfolder", "", [_is_hot_container_enabled : "yes",
  _hot_container_process_definition : "IET Import DITA Map"]);

createContainerIfNotExist("/Practitioner/", booksMoId,  "Image Hotfolder", "hotfolder", [_is_hot_container_enabled : "yes",
	_hot_container_process_definition : "IET Standards Image Ingestion"]);
 
def createContainerIfNotExist(parentPath, parentId, containerName, type){
	createContainerIfNotExist(parentPath, parentId, containerName, type, null);
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

rsuite.logout();