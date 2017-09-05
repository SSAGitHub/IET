/**
 * Copyright (c) 2012 Really Strategies, Inc.
 */
package org.theiet.rsuite.standards.webservices;

import org.apache.commons.logging.*;
import org.theiet.rsuite.standards.StandardsBooksConstans;
import org.theiet.rsuite.standards.domain.book.StandardsBookUtils;

import com.reallysi.rsuite.api.*;
import com.reallysi.rsuite.api.remoteapi.*;
import com.rsicms.projectshelper.webservice.ProjectRemoteApiHandler;

/**
 * Sets LMD provided through a utility form.
 */
public class CreateBookWebService extends ProjectRemoteApiHandler
		implements StandardsBooksConstans {

	
	/** Logger. **/
	private static Log log = LogFactory
			.getLog(CreateBookWebService.class);

	@Override
	protected String getDialogTitle() {
		return "Create Book";
	}

	@Override
	protected String exectuteAction(RemoteApiExecutionContext context,
			CallArgumentList args) throws RSuiteException {
		log.info("execute(): args=" + args.getValuesMap());

		String booksGroupCaId = args.getFirstValue(PARAM_RSUITE_ID);
		
		String bookName = args.getFirstValue("bookName");
		String shortTitle = args.getFirstValue("book_title_short");
		
		ContentAssembly standardsBook = StandardsBookUtils.createStandardBookCa(context, booksGroupCaId, bookName,
				shortTitle);

		StandardsBookUtils.createStandardBookCaStructure(context, standardsBook.getId());		

		return "A new book has been created";
	}


	
}
