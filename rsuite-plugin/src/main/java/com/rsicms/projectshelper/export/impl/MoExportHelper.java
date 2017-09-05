package com.rsicms.projectshelper.export.impl;

import org.apache.commons.lang.StringUtils;

import com.reallysi.rsuite.api.*;
import com.reallysi.rsuite.api.extensions.ExecutionContext;
import com.reallysi.rsuite.service.ManagedObjectService;
import com.rsicms.projectshelper.export.MoExportContainerContext;

public final class MoExportHelper {

    private MoExportHelper() {};


    public static ManagedObject getMoToExportBasedOnTheContext(ExecutionContext context, User user,
            MoExportContainerContext exportContainerContext, ManagedObject moToExport)
            throws RSuiteException {

        if (exportContainerContext != null) {
            String versionToExport = exportContainerContext.getContextMoVersion(moToExport);

            if (StringUtils.isNotBlank(versionToExport)) {
                ManagedObjectService moService = context.getManagedObjectService();
                moToExport =
                        moService.getManagedObject(user, new VersionSpecifier(moToExport.getId(),
                                versionToExport));
            }
        }
        return moToExport;
    }

}
