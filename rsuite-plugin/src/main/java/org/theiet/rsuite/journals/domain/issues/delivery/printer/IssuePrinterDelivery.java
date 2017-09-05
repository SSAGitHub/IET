package org.theiet.rsuite.journals.domain.issues.delivery.printer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.commons.io.FilenameUtils;
import org.theiet.rsuite.datatype.deliveryUser.DeliveryUser;
import org.theiet.rsuite.journals.domain.issues.datatype.Issue;
import org.theiet.rsuite.journals.domain.journal.Journal;

import com.reallysi.rsuite.api.ContentAssemblyItem;
import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.User;
import com.reallysi.rsuite.api.control.ContentAssemblyItemFilter;
import com.reallysi.rsuite.api.extensions.ExecutionContext;
import com.rsicms.rsuite.helpers.download.ZipHelper;
import com.rsicms.rsuite.helpers.download.ZipHelperConfiguration;

public class IssuePrinterDelivery {

	private ExecutionContext context;

	public IssuePrinterDelivery(ExecutionContext context) {
		this.context = context;
	}

	public void deliverIssuePdf(Issue issue) throws RSuiteException {

		Journal journal = issue.getJournal();
		DeliveryUser printer = journal.getPrinter();

		if (printer != null) {

			ByteArrayOutputStream zipStream = archiveIssuePdfFiles(issue);

			String issueZipName = issue.getIssueCode() + ".zip";

			printer.deliverToMainDestination(
					new ByteArrayInputStream(zipStream.toByteArray()),
					issueZipName);
		}

	}

	private ByteArrayOutputStream archiveIssuePdfFiles(Issue issue)
			throws RSuiteException {

		ByteArrayOutputStream zipStream = new ByteArrayOutputStream();
		ZipHelperConfiguration zipConfiguration = new ZipHelperConfiguration();
		zipConfiguration.setCaItemFilter(createCaItemFilter());
		try {
			ZipHelper.zipContentAssemblyContents(context,
					issue.getTypesetterCA(), zipStream, zipConfiguration);
		} catch (IOException e) {
			throw new RSuiteException(0, "Unable to archive pdf files for "
					+ issue, e);
		}

		return zipStream;
	}

	private ContentAssemblyItemFilter createCaItemFilter() {
		return new ContentAssemblyItemFilter() {

			@Override
			public boolean include(User user, ContentAssemblyItem item)
					throws RSuiteException {

				if (isPdfFile(item)) {
					return true;
				}

				return false;
			}

			private boolean isPdfFile(ContentAssemblyItem item) {
				String displayName = item.getDisplayName();
				String extension = FilenameUtils.getExtension(displayName);
				if ("pdf".equalsIgnoreCase(extension)) {
					return true;
				}
				return false;
			}
		};
	}

}
