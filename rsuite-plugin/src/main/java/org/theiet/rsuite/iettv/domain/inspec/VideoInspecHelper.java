package org.theiet.rsuite.iettv.domain.inspec;

import static com.reallysi.rsuite.api.ObjectType.*;

import java.io.*;
import java.util.zip.ZipEntry;

import org.theiet.rsuite.datatype.deliveryUser.*;
import org.theiet.rsuite.iettv.domain.datatype.VideoRecord;

import com.reallysi.rsuite.api.*;
import com.reallysi.rsuite.api.extensions.ExecutionContext;
import com.rsicms.rsuite.helpers.download.*;
import com.rsicms.rsuite.helpers.utils.ZipFilter;

public class VideoInspecHelper {

    private static final String USER_ID_IET_TV_INSPEC = "IetTvInspecUser";

    public static void sendToInspec(ExecutionContext context, File file) throws RSuiteException {

        DeliveryUser deliveryUser =
                DeliveryUserFactory.createDeliveryUser(context, USER_ID_IET_TV_INSPEC);

        try {
            FileInputStream inspecPackageInputStream = new FileInputStream(file);
            deliveryUser.deliverToMainDestination(inspecPackageInputStream, file.getName());
        } catch (IOException e) {
            throw new RSuiteException("Unable to send inspec file " + file.getAbsolutePath());
        }
    }

    public static void extractInspecPackage(ExecutionContext context, VideoRecord videoRecord,
            OutputStream outStream) throws RSuiteException {

        ZipHelperConfiguration configuration = new ZipHelperConfiguration();
        configuration.setZipFilter(createZipFilter());
        try {
            ZipHelper.zipContentAssemblyContents(context, videoRecord.getVideoRecordContainer(), outStream,
                    configuration);
            outStream.flush();
            outStream.close();

        } catch (IOException e) {
            throw new RSuiteException("Unable to extract inspec package for video  "
                    + videoRecord.getVideoId());
        }
    }

    private static ZipFilter createZipFilter() {
        return new ZipFilter() {

            @Override
            public boolean unzipZipEntry(ZipEntry arg0, ContentAssembly item,
                    ExecutionContext context) {
                return false;
            }

            @Override
            public boolean archiveContentAssemblyItem(ContentAssemblyItem item,
                    ExecutionContext context) {

                if (isManagedObject(item)) {
                    return true;
                }
                return false;
            }

            private boolean isManagedObject(ContentAssemblyItem item) {
                ObjectType objectType = item.getObjectType();
                return objectType == MANAGED_OBJECT_REF || objectType == MANAGED_OBJECT_NONXML
                        || objectType == MANAGED_OBJECT;
            }
        };
    }
}
