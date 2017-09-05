/**
 * Copyright (c) 2012 Really Strategies, Inc.
 */
package org.theiet.rsuite.journals.webservice;

import static org.theiet.rsuite.journals.domain.journal.JournalCreator.createJournalCa;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.theiet.rsuite.journals.JournalConstants;
import org.theiet.rsuite.journals.domain.journal.JournalCreateDTO;

import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.remoteapi.CallArgumentList;
import com.reallysi.rsuite.api.remoteapi.RemoteApiExecutionContext;
import com.reallysi.rsuite.api.remoteapi.RemoteApiResult;
import com.reallysi.rsuite.api.remoteapi.result.MessageDialogResult;
import com.reallysi.rsuite.api.remoteapi.result.MessageType;
import com.rsicms.rsuite.helpers.webservice.RemoteApiHandlerBase;

/**
 * Sets LMD provided through a utility form.
 */
public class CreateNewJournalWebService extends RemoteApiHandlerBase implements
		JournalConstants {

	private static final String WS_TITLE = "Create New Journal";

	private static Log log = LogFactory
			.getLog(CreateNewJournalWebService.class);

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

		String journalsCaId = args.getFirstValue("rsuiteId");

		try {

			JournalCreateDTO params = new JournalCreateDTO(args);

			createJournalCa(context, getUser(context), params, journalsCaId);

		} catch (Exception e) {
			log.error("Unable to create journal", e);
			return new MessageDialogResult(MessageType.ERROR, WS_TITLE,
					e.getMessage());
		}

		String msg = "A new journal has been created";
		return new MessageDialogResult(WS_TITLE, msg.toString(), "500");
	}

}
