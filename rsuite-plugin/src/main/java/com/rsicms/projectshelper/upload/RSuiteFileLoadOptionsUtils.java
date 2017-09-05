package com.rsicms.projectshelper.upload;

import java.util.List;

import com.reallysi.rsuite.api.RSuiteException;
import com.rsicms.rsuite.helpers.messages.ProcessFailureMessage;
import com.rsicms.rsuite.helpers.upload.*;

public class RSuiteFileLoadOptionsUtils {

    public static void checkForErrors(RSuiteFileLoadOptions loadOptions) throws RSuiteException{
        checkForErrors(loadOptions, "");
    }
    
    public static void checkForErrors(RSuiteFileLoadOptions loadOptions, String contextErrorMessage) throws RSuiteException{
        RSuiteFileLoadResult loadResult = loadOptions.getLoadResult();
        
        if (loadResult == null){
            return;
        }
        
        if (loadResult.hasErrors()){
            String errorMessage = serializeErrorMessages(loadResult);
            throw new RSuiteException(contextErrorMessage + " " + errorMessage);
        }
    }

    private static String serializeErrorMessages(RSuiteFileLoadResult loadResult) {
        StringBuilder errorMessage = new StringBuilder();
        
        List<List<ProcessFailureMessage>> failuresByLabel = loadResult.getFailuresByLabel();
        
        for (List<ProcessFailureMessage> messsages : failuresByLabel){
            for (ProcessFailureMessage message : messsages){
                errorMessage.append(message.getMessageText()).append("\n");
            }
        }
        
        return errorMessage.toString();
    }
}
