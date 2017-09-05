package org.theiet.rsuite.eventhandlers;

import static org.theiet.rsuite.IetConstants.*;

import java.util.List;

import org.apache.commons.logging.*;
import org.theiet.rsuite.domain.permissions.PermissionsUtils;
import org.theiet.rsuite.utils.EventHandlerUtils;

import com.reallysi.rsuite.api.*;
import com.reallysi.rsuite.api.event.*;
import com.reallysi.rsuite.api.extensions.ExecutionContext;
import com.reallysi.rsuite.service.*;
import com.rsicms.projectshelper.utils.browse.ProjectBrowserUtils;

public class ContainerPermissionManagerEventHandler extends DefaultEventHandler {

    private static Log log = LogFactory.getLog(ContainerPermissionManagerEventHandler.class);

    @Override
    public void handleEvent(ExecutionContext context, Event event, Object eventData) {
        SecurityService secSvc = context.getSecurityService();
        ManagedObjectService moSvc = context.getManagedObjectService();
        User systemUser = context.getAuthorizationService().getSystemUser();
        String containerId = null;
        String eventType = event.getType();

        if (eventType.equals("object.contentassembly.created")
                || eventType.equals("object.contentassembly.objectattached")

        ) {

            try {
                Object userData = event.getUserData();
                containerId =
                        EventHandlerUtils.getContainerIdFromEventData(userData, systemUser, moSvc);


                if (belongsToIetTvDomain(context, containerId)) {
                    PermissionsUtils.setIetTvACL(moSvc, secSvc, systemUser, containerId, log);
                } else if (belongsToStandardsDomain(context, containerId)) {
                    PermissionsUtils.setStandarsContainerACL(moSvc, secSvc, systemUser,
                            containerId, log);
                }


            } catch (RSuiteException ex) {
                log.error("Unable to set permissions for container: " + containerId, ex);
            }
        }
    }


    private boolean belongsToStandardsDomain(ExecutionContext context, String containerId)
            throws RSuiteException {
        return isDescendandOfHomeContainerType(context, containerId, CA_TYPE_STANDARDS_DOMAIN);
    }


    private boolean belongsToIetTvDomain(ExecutionContext context, String containerId)
            throws RSuiteException {
        return isDescendandOfHomeContainerType(context, containerId, CA_TYPE_IET_TV_DOMAIN);
    }


    private boolean isDescendandOfHomeContainerType(ExecutionContext context, String containerId,
            String homeContainerType) throws RSuiteException {
        User systemUser = context.getAuthorizationService().getSystemUser();
        ManagedObjectService moServ = context.getManagedObjectService();

        List<ReferenceInfo> refernces =
                moServ.getDependencyTracker().listAllReferences(systemUser, containerId);

        for (ReferenceInfo refernceInfo : refernces) {
            String browserUri = refernceInfo.getBrowseUri();
            String[] uriParts = browserUri.split("/");

            if (uriParts.length > 1) {
                String domainPart = uriParts[1];
                String caId = domainPart.split(":")[1];
                ContentAssembly ca = ProjectBrowserUtils.getContentAssembly(context, caId);

                if (ca != null && homeContainerType.equals(ca.getType())) {
                    return true;
                }
            }
        }

        return false;
    }



}
