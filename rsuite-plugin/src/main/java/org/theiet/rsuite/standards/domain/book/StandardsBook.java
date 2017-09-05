package org.theiet.rsuite.standards.domain.book;

import java.util.*;

import org.theiet.rsuite.datamodel.IetBookPublication;
import org.theiet.rsuite.standards.StandardsBooksConstans;

import com.reallysi.rsuite.api.*;
import com.reallysi.rsuite.api.extensions.ExecutionContext;
import com.rsicms.projectshelper.utils.browse.ProjectBrowserUtils;

/**
 * Represents Standards Book Edition
 * @author lukasz
 *
 */
public class StandardsBook extends IetBookPublication implements StandardsBooksConstans {

	private ExecutionContext context;

	public StandardsBook(ExecutionContext context,
			ContentAssembly bookEditionCa) {
		super(bookEditionCa);
		initialize(context);
	}

	public StandardsBook(ExecutionContext context, String bookCaId)
			throws RSuiteException {
		super(context, bookCaId);
		initialize(context);
	}

	private void initialize(ExecutionContext context) {
		this.context = context;
	}

	/**
	 * Obtains all book edition ca for given book
	 * @return list of book edition ca
	 * @throws RSuiteException
	 */
	public List<ContentAssembly> getBookEditions() throws RSuiteException {
		ContentAssembly bookCa = getBookPublicationCa();

		return ProjectBrowserUtils.getChildrenCaByType(context, bookCa, CA_TYPE_STANDARDS_BOOK_EDITION);

	}

	public StandardsBookEdition createBookEdtion(User user, String editionName, List<MetaDataItem> metadataList, Map<String, String> variables) throws RSuiteException {
		
		ContentAssembly bookEditionCa = StandardsBookUtils.createStandarsBookEdition(context, user, editionName, getRsuiteId(), metadataList, variables);
		
		return new StandardsBookEdition(context, bookEditionCa);
	}
	
	public ContentAssembly getImagesCA() throws RSuiteException {
		ContentAssembly bookCA = getBookPublicationCa();
		ContentAssembly imagesCA = ProjectBrowserUtils.getChildCaByType(context, bookCA, CA_TYPE_STANDARDS_IMAGES);
		
		if (imagesCA == null){
			throw new RSuiteException("No images container found for book " + bookCA.getDisplayName() + " " + bookCA.getId());
		}
		
		return imagesCA; 
		
	}
}
