package org.theiet.rsuite.iettv.domain.delivery.production;

import java.io.*;

import org.theiet.rsuite.datatype.deliveryUser.*;
import org.theiet.rsuite.iettv.constants.IetTvCaType;
import org.theiet.rsuite.iettv.domain.datatype.VideoRecord;

import com.reallysi.rsuite.api.*;
import com.reallysi.rsuite.api.control.ContentAssemblyItemFilter;
import com.reallysi.rsuite.api.extensions.ExecutionContext;
import com.reallysi.rsuite.service.ManagedObjectService;
import com.rsicms.rsuite.helpers.download.*;

public class ProductionDeliveryHelper {

    private static final String ELEMENT_NAME_VIDEO = "Video";
    private static final String USER_ID_IET_TV_PRODUCTION = "IetTvProductionUser";

    public static void sendToProduction(ExecutionContext context, File file) throws RSuiteException {

        DeliveryUser deliveryUser =
                DeliveryUserFactory.createDeliveryUser(context, USER_ID_IET_TV_PRODUCTION);

        try {
            FileInputStream inspecPackageInputStream = new FileInputStream(file);
            deliveryUser.deliverToMainDestination(inspecPackageInputStream, file.getName());
        } catch (IOException e) {
            throw new RSuiteException("Unable to send inspec file " + file.getAbsolutePath());
        }
    }

    public static void extractProductionPackage(ExecutionContext context, VideoRecord videoRecord,
            OutputStream outStream) throws RSuiteException {

        ZipHelperConfiguration configuration = new ZipHelperConfiguration();
        configuration.setObjectNameHandler(createObjectNameHandler(videoRecord));
        configuration.setCaItemFilter(createCaItemFilter(context, videoRecord));

        try {
            ZipHelper.zipContentAssembly(context, videoRecord.getVideoRecordContainer(), outStream,
                    configuration);
            outStream.flush();
            outStream.close();

        } catch (IOException e) {
            throw new RSuiteException("Unable to extract inspec package for video  "
                    + videoRecord.getVideoId());
        }
    }

    private static ContentAssemblyItemFilter createCaItemFilter(final ExecutionContext context,
            final VideoRecord videoRecord) {


        return new ContentAssemblyItemFilter() {

            private ManagedObjectService moService = context.getManagedObjectService();

            @Override
            public boolean include(User user, ContentAssemblyItem caItem) throws RSuiteException {

                if (caItem.getObjectType() == ObjectType.MANAGED_OBJECT_REF) {
                    ManagedObjectReference moRef = (ManagedObjectReference) caItem;
                    ManagedObject managedObject =
                            moService.getManagedObject(user, moRef.getTargetId());

                    if (isVideo(managedObject)) {
                        return true;
                    }
                }

                return false;
            }

            private boolean isVideo(ManagedObject managedObject) throws RSuiteException {
                return !managedObject.isNonXml()
                        && ELEMENT_NAME_VIDEO.equals(managedObject.getElement()
                                .getLocalName());
            }
        };
    }

    private static RSuiteObjectNameHandler createObjectNameHandler(final VideoRecord videoRecord) {

        return new RSuiteObjectNameHandler() {

            @Override
            public String getName(ExecutionContext arg0, ContentAssemblyNodeContainer container,
                    String defaultName) throws RSuiteException {

                if (IetTvCaType.VIDEO_RECORD.getTypeName().equals(container.getType())) {
                    return videoRecord.createVideoFileName("");
                }

                return defaultName;
            }

            @Override
            public String getName(ExecutionContext arg0, ManagedObject mo, String defaultName)
                    throws RSuiteException {

                Alias[] aliases = mo.getAliases("filename");
                if (aliases.length > 0) {
                    return aliases[0].getText();
                }

                return defaultName;
            }
        };
    }


}
