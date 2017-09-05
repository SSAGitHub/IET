package org.theiet.rsuite.standards.actionhandlers.esp;

import static org.theiet.rsuite.standards.StandardsBooksConstans.*;
import static org.theiet.rsuite.standards.constans.PublishWorkflowContans.*;

import java.io.*;

import org.apache.commons.io.FileUtils;
import org.theiet.rsuite.standards.domain.book.export.esp.ESPDelivery;

import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.workflow.*;
import com.rsicms.projectshelper.workflow.actionhandlers.AbstractActionHandler;
import com.rsicms.rsuite.helpers.utils.ZipUtil;

public class ESPDeliveryActionHandler extends AbstractActionHandler {

	private static final long serialVersionUID = 1L;

	@Override
	public void executeTask(WorkflowExecutionContext executionContext)
			throws Exception {

		WorkflowJobContext workflowJobContext = executionContext
				.getWorkflowJobContext();

		String bookEditionProductCode = executionContext
				.getVariable(WF_VAR_BOOK_EDITION_E_PRODUCT_CODE);

		File exportDir = new File(
				executionContext.getVariable(WF_VAR_EXPORT_FOLDER));

		File resultFile = new File(workflowJobContext.getTempFolderPath(),
				bookEditionProductCode + ".zip");

		try {
			ZipUtil.zipFolder(exportDir.getAbsolutePath(),
					resultFile.getAbsolutePath());

			ESPDelivery.deliver(context, resultFile);
		} catch (IOException e) {
			throw new RSuiteException(0,
					"Unable to deliver to ESB book edition with product code "
							+ bookEditionProductCode, e);
		}

		FileUtils.deleteQuietly(exportDir);

	}

}