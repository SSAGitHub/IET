package com.rsicms.projectshelper.upload;

import static com.rsicms.rsuite.helpers.upload.RSuiteFileLoadHelper.*;

import java.io.File;
import java.util.*;

import com.reallysi.rsuite.api.*;
import com.reallysi.rsuite.api.control.*;
import com.reallysi.rsuite.api.extensions.ExecutionContext;
import com.reallysi.rsuite.service.*;
import com.rsicms.rsuite.helpers.upload.*;

public final class UploadHelper {

    private UploadHelper(){};

    public static void synchronizeContentAssemblyWithFolder(ExecutionContext context, User user,
            ContentAssemblyNodeContainer caToSynchronize, File folder) throws RSuiteException {

        purgeContentAssemblyNodeContainer(context, user, caToSynchronize);

        RSuiteFileLoadOptions loadOptions = new RSuiteFileLoadOptions(user);
        loadOptions.setDuplicateFilenamePolicy(DuplicateFilenamePolicy.UPDATE);
        loadDirectoryContentsToCaNodeContainer(context, folder, caToSynchronize, loadOptions);
        RSuiteFileLoadOptionsUtils.checkForErrors(loadOptions,
                "Unable to synchronize " + folder.getAbsolutePath());
    }


    private static void purgeAndDestroyContentAssemblyNodeContainer(ExecutionContext context,
            User user, ContentAssemblyNodeContainer caNode) throws RSuiteException {
        purgeContentAssemblyNodeContainer(context, user, caNode);
        destroyContentAssemblyNodeContainer(context, user, caNode);
    }

    private static void purgeContentAssemblyNodeContainer(ExecutionContext context, User user,
            ContentAssemblyNodeContainer caNode) throws RSuiteException {
        List<? extends ContentAssemblyItem> childrenObjects = caNode.getChildrenObjects();
        Set<String> moIdsToDestroy = new HashSet<String>();

        for (ContentAssemblyItem item : childrenObjects) {
            if (item instanceof ManagedObjectReference) {
                ManagedObjectReference moReference = (ManagedObjectReference) item;
                moIdsToDestroy.add(moReference.getTargetId());
            } else if (item instanceof ManagedObject) {
                moIdsToDestroy.add(item.getId());
            } else if (item instanceof ContentAssemblyReference) {
                String caId = ((ContentAssemblyReference) item).getTargetId();
                ContentAssemblyService caSvc = context.getContentAssemblyService();
                ContentAssembly contentAssembly = caSvc.getContentAssembly(user, caId);
                purgeAndDestroyContentAssemblyNodeContainer(context, user, contentAssembly);
            } else if (item instanceof ContentAssemblyNodeContainer) {
                ContentAssemblyNodeContainer container = (ContentAssemblyNodeContainer) item;
                purgeAndDestroyContentAssemblyNodeContainer(context, user, container);
            }
        }

        destroyMos(context, user, moIdsToDestroy);
    }


    private static void destroyMos(ExecutionContext context, User user, Set<String> moIdsToDestroy)
            throws RSuiteException {
        ManagedObjectService moSvc = context.getManagedObjectService();
        ObjectDestroyOptions destroyOptions = new ObjectDestroyOptions();
        for (String moId : moIdsToDestroy) {

            if (moSvc.isCheckedOut(user, moId)) {
                moSvc.undoCheckout(user, moId);
            }
            moSvc.checkOut(user, moId);
            moSvc.destroy(user, moId, destroyOptions);
        }
    }


    private static void destroyContentAssemblyNodeContainer(ExecutionContext context, User user,
            ContentAssemblyNodeContainer caNode) throws RSuiteException {

        if (caNode instanceof ContentAssembly) {
            context.getContentAssemblyService().removeContentAssembly(user, caNode.getId());
        } else if (caNode instanceof ContentAssemblyNode) {
            context.getContentAssemblyService().deleteCANode(user, caNode.getId());
        } else {
            throw new RSuiteException("Unexpected CA node container type "
                    + caNode.getClass().getSimpleName());
        }
    }

    public static void upsertFileToContainer(ExecutionContext context, User user,
            File fileToUpload, ContentAssemblyNodeContainer parentContainer) throws RSuiteException {

        ManagedObjectService moSvc = context.getManagedObjectService();
        ManagedObject mo = moSvc.getObjectByAlias(user, fileToUpload.getName());

        RSuiteFileLoadOptions fileLoadOptions = createFileLoadOptions(user);
        if (mo != null) {
            updateMoFromFile(context, fileToUpload, parentContainer, mo, fileLoadOptions);
        } else {
            mo = loadFileAsNewMo(context, fileToUpload, parentContainer, fileLoadOptions);
        }
        RSuiteFileLoadOptionsUtils.checkForErrors(fileLoadOptions);
    }


    private static RSuiteFileLoadOptions createFileLoadOptions(User user) {
        RSuiteFileLoadOptions rsuiteFileLoadOptions = new RSuiteFileLoadOptions(user);
        rsuiteFileLoadOptions.setDuplicateFilenamePolicy(DuplicateFilenamePolicy.UPDATE);
        return rsuiteFileLoadOptions;
    }
}
