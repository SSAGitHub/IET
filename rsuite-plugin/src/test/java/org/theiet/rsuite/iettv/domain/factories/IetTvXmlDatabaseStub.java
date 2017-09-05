package org.theiet.rsuite.iettv.domain.factories;

import java.util.HashMap;
import java.util.Map;

import com.reallysi.rsuite.api.ContentAssembly;

public class IetTvXmlDatabaseStub {

    private Map<String, ContentAssembly> caMap = new HashMap<String, ContentAssembly>();
    
    public void addCa(ContentAssembly ca){
        
        if (caMap.containsKey(ca.getId())){
            throw new RuntimeException("The CA should be created only once");
        }
        
        caMap.put(ca.getId(), ca);
    }
    
    public ContentAssembly getCa(String id){
        return caMap.get(id);
    }
}
