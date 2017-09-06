import com.reallysi.rsuite.client.api.*
import com.reallysi.rsuite.remote.api.*




rsuite.login();

def channels = ["Communications", "Control", "Electronics", "IT", "Management", "Manufacturing", "Power", "Transport"];



folderId = createContainerIfNotExist("/", "IET.tv", "iettv");

channels.each() {channel -> createContainerIfNotExist("/IET.tv", channel, "iettv_channel") }; 

def createContainerIfNotExist(parentPath, containerName, containerType){
    def containerPath = parentPath + containerName;
    def folderId = null;
    infos = rsuite.findObjectForPath(containerPath);
    
    if (parentPath != "/"){
        pranetInfos = rsuite.findObjectForPath(parentPath);
        parentId = pranetInfos["moid"]
    }else{
        parentId = parentPath;
    }
    
        
    if (infos == null){
                  
        def folderOptions = new Options();
        folderOptions.add("type", containerType);
        
        result = rsuite.createContentAssembly(parentId, containerName, folderOptions);        
        folderId = result["moid"];
        rsuite.setACE(folderId, "any", "");
        rsuite.setACE(folderId, "RSuiteAdministrator", "list,view,edit,copy,reuse,admin,delete");
        rsuite.setACE(folderId, "IetTvAuthorizedUser", "list,view,edit,copy,reuse");
        
        println "[INFO] Created " + containerType + ": " + containerName + " at " + containerPath + " (" + folderId + ") new";                
    }else{
        if (infos.entryList != null){
            for (entry in infos.entryList){
                if (entry.key.equals("moid")){
                    folderId = entry.value
                    break
                }
            }
        }
        println "[INFO] " + containerType + ": " + containerName + " at " + containerPath +  " (" + folderId + ") already exist";
    }
   
    return folderId;
}

rsuite.logout();