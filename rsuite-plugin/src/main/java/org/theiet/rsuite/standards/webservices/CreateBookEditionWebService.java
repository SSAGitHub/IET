/**
 * Copyright (c) 2012 Really Strategies, Inc.
 */
package org.theiet.rsuite.standards.webservices;

import java.util.*;

import org.theiet.rsuite.datamodel.bookpublication.CallArgumentsBookPublicationMapper;
import org.theiet.rsuite.standards.StandardsBooksConstans;
import org.theiet.rsuite.standards.domain.book.*;

import com.reallysi.rsuite.api.*;
import com.reallysi.rsuite.api.remoteapi.*;
import com.rsicms.projectshelper.lmd.MetadataUtils;
import com.rsicms.projectshelper.webservice.ProjectRemoteApiHandler;

/**
 * Sets LMD provided through a utility form.
 */
public class CreateBookEditionWebService extends ProjectRemoteApiHandler implements
		StandardsBooksConstans {

	private static final String PARAM_LMD_EDITION_NUMBER = "editionNumber";

	@Override
	protected String getDialogTitle() {
		return "Create New Book edition";
	}

	@Override
	protected String exectuteAction(RemoteApiExecutionContext context,
			CallArgumentList args) throws RSuiteException {

		String bookCaId = getMoSourceId(args); //  args.getFirstValue(PARAM_RSUITE_ID);
		User user = getUser(context);

		List<MetaDataItem> initialMetadataList = MetadataUtils
				.getLmdListFromWsParameters(args);

		CallArgumentsBookPublicationMapper callArgumentMapper = new CallArgumentsBookPublicationMapper();

		Map<String, String> variables = callArgumentMapper
				.converFormCallArgumentsToMap(args, initialMetadataList);

		initialMetadataList.add(new MetaDataItem(LMD_FIELD_BOOK_TITLE,
				callArgumentMapper.generateFullTitle(variables)));

		String editionName = createEditionName(args);
		
		StandardsBook book = new StandardsBook(context, bookCaId);
		book.createBookEdtion(user, editionName, initialMetadataList, variables);

		return "A new book edition has been created";

	}

	private String createEditionName(CallArgumentList args) {
		int editionNumber = Integer.parseInt(args.getFirstValue(PARAM_LMD_EDITION_NUMBER));
		String editionName = StandardsBookUtils.convertEditonNumberToOrdinal(editionNumber);
		return editionName;
	}

	@Override
	protected boolean refreshParent() {
		return true;
	}

}
