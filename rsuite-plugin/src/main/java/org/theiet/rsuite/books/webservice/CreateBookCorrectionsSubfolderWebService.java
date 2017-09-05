/**
 * Copyright (c) 2012 Really Strategies, Inc.
 */
package org.theiet.rsuite.books.webservice;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.theiet.rsuite.books.BooksConstans;

import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.remoteapi.CallArgumentList;
import com.reallysi.rsuite.api.remoteapi.RemoteApiExecutionContext;
import com.reallysi.rsuite.api.remoteapi.RemoteApiResult;
import com.reallysi.rsuite.api.remoteapi.result.MessageDialogResult;
import com.reallysi.rsuite.api.remoteapi.result.MessageType;
import com.rsicms.projectshelper.utils.ProjectContentAssemblyUtils;
import com.rsicms.rsuite.helpers.webservice.RemoteApiHandlerBase;

/**
 * Sets LMD provided through a utility form.
 */
public class CreateBookCorrectionsSubfolderWebService extends
		RemoteApiHandlerBase implements BooksConstans {

	/** Web service parameters */
	private static final String PARAM_NAME = "name";

	/**
	 * 
	 */
	private static Log log = LogFactory
			.getLog(CreateBookCorrectionsSubfolderWebService.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.reallysi.rsuite.api.remoteapi.RemoteApiHandler#execute(com.reallysi
	 * .rsuite.api.remoteapi.RemoteApiExecutionContext,
	 * com.reallysi.rsuite.api.remoteapi.CallArgumentList)
	 */
	public RemoteApiResult execute(RemoteApiExecutionContext context,
			CallArgumentList args) throws RSuiteException {

		log.info("execute(): args=" + args.getValuesMap());

		String contextNodeId = args.getFirstValue(PARAM_RSUITE_ID);
		String folderName = args.getFirstValue(PARAM_NAME);

		try {

			if (StringUtils.isEmpty(folderName)) {
				throw new Exception("A new folder name cannot be empty");
			}

			ProjectContentAssemblyUtils.createContentAssembly(context, contextNodeId, folderName,
					CA_TYPE_BOOK_CORRECTIONS);

		} catch (Exception e) {
			return new MessageDialogResult(MessageType.ERROR,
					"Create Book Corrections", e.getMessage());
		}

		String msg = "A new corrrections folder has been created";
		return new MessageDialogResult("Create Book Corrections",
				msg.toString(), "500");
	}

}
