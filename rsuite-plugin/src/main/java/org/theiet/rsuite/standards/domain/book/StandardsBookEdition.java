package org.theiet.rsuite.standards.domain.book;

import java.util.*;

import org.theiet.rsuite.datamodel.IetBookPublication;
import org.theiet.rsuite.standards.StandardsBooksConstans;
import org.theiet.rsuite.standards.domain.book.helper.*;
import org.theiet.rsuite.utils.SearchUtils;

import com.reallysi.rsuite.api.*;
import com.reallysi.rsuite.api.extensions.ExecutionContext;
import com.reallysi.rsuite.service.ManagedObjectService;
import com.rsicms.projectshelper.contenttypes.jatsv1.JatsUtils;
import com.rsicms.projectshelper.lmd.MetadataUtils;
import com.rsicms.projectshelper.utils.browse.*;
import com.rsicms.projectshelper.utils.browse.filters.ChildMoFilter;

/**
 * Represents Standards Book Edition
 * 
 * @author lukasz
 * 
 */
public class StandardsBookEdition extends IetBookPublication implements
		StandardsBooksConstans {

	private ExecutionContext context;

	private StandardsBookEditionType type;
	
	private StandardsBookEditionLmd lmd;
	
	public StandardsBookEdition(ExecutionContext context,
			ContentAssembly bookEditionCa) {
		super(bookEditionCa);
		initialize(context);
		lmd = new StandardsBookEditionLmd(bookEditionCa);
	}

	public StandardsBookEdition(ExecutionContext context, String bookEditonCaId)
			throws RSuiteException {
		super(context, bookEditonCaId);
		initialize(context);
	}

	private void initialize(ExecutionContext context) {
		this.context = context;
	}

	/**
	 * Returns Typescript Content Assembly for given book edition
	 * 
	 * @return Typescript Content Assembly
	 * @throws RSuiteException
	 */
	public ContentAssembly getTypesriptCA() throws RSuiteException {
		ContentAssembly bookEditionCa = getBookPublicationCa();

		ContentAssembly editorial = ProjectBrowserUtils.getChildCaByDisplayName(
				context, bookEditionCa, "Editorial");
		return ProjectBrowserUtils.getChildCaByType(context, editorial,
				CA_TYPE_STANDARDS_TYPESCRIPT);

	}

	public List<ManagedObject> getMainXMLMo() throws RSuiteException {

		ContentAssembly typescriptCA = getTypesriptCA();
		ChildMoFilter childMoFilter = new StandardsBoookMainMoFilter(context);
		
		List<ManagedObject> bookEditionMainXMLMos = ProjectBrowserUtils.getChildMos(context, typescriptCA, childMoFilter);

		if (bookEditionMainXMLMos.size() == 0) {
			throw new RSuiteException(
					"Unable to find main XML mo for book edition " + getRsuiteId());
		}
		
		return bookEditionMainXMLMos;
	}
	
	public StandardsBookEditionType getBookEditionType() throws RSuiteException{
	    
	    if (type == null){
	        computeBookEditionType();
	    }
	    
	    return type;
	}

    private void computeBookEditionType() throws RSuiteException {
        ContentAssembly typescriptCA = getTypesriptCA();
        ChildMoFilter childMoFilter = new StandardsBoookMainMoFilter(context);
        ManagedObject mainXMLMo = ProjectBrowserUtils.getChildMo(context, typescriptCA, childMoFilter);
        
        if (mainXMLMo == null){
            throw new RSuiteException("Unable to get type for boook edition " + getRsuiteId());
        }
        
        if (JatsUtils.isJatsArticle(mainXMLMo)){
            type = StandardsBookEditionType.JATS;
        }else{
            type = StandardsBookEditionType.DITA;
        }
    }

	public void archiveEdition() throws RSuiteException {

		User systemUser = context.getAuthorizationService().getSystemUser();
		ContentAssembly typeScript = getTypesriptCA();

		BookEditionContentArchiver archiver = new BookEditionContentArchiver(
				context, systemUser);
		archiver.archive(typeScript);

		markEditionAsArchived(systemUser);

	}

	private void markEditionAsArchived(User systemUser) throws RSuiteException {
		ManagedObjectService moSvc = context.getManagedObjectService();

		String presentTimestamp = StandardsBookUtils.getPresentTimestamp();

		List<MetaDataItem> metadataList = new ArrayList<MetaDataItem>();

		metadataList.add(new MetaDataItem(LMD_FIELD_BOOK_STATUS,
				LMD_VALUE_ARCHIVED));
		metadataList.add(new MetaDataItem(LMD_FIELD_ARCHIVE_TIMESTAMP,
				presentTimestamp));

		moSvc.setMetaDataEntries(systemUser, getBookPublicationCa().getId(),
				metadataList);

	}

	public void copyContentFromAnotherEdition(StandardsBookEdition editionFrom)
			throws RSuiteException {
		ContentAssembly targetCa = getTypesriptCA();
		ContentAssembly srcCa = editionFrom.getTypesriptCA();

		User root = context.getAuthorizationService().getSystemUser();

		BookEditionContentCopier copier = new BookEditionContentCopier(context,
				root, targetCa);
		copier.copyContent(srcCa);
	}
	
	public List<String> getDirectBookEditionDependency(User user) throws RSuiteException{
		
		ContentAssembly bookEditionCa = getBookPublicationCa();
		List<MetaDataItem> metaDataItems = bookEditionCa.getMetaDataItems();
				
		List<String> productCodes = MetadataUtils.getLmdValues(metaDataItems, LMD_FIELD_PUBLICATION_DEPENDENCY);
		
		List<ManagedObject> dependencyMOs = SearchUtils.findCaBasedOnLmd(context.getSearchService(), user, LMD_FIELD_BOOK_E_PRODUCT_CODE, productCodes);
		
		List<String> dependencyIds = new ArrayList<String>();
		
		for (ManagedObject dependencyMO : dependencyMOs){
			dependencyIds.add(dependencyMO.getId());
		}
		
		return dependencyIds;
	}
	
	public String getLMD(String lmdName) throws RSuiteException{
		return getBookPublicationCa().getLayeredMetadataValue(lmdName);
	}

    public StandardsBookEditionLmd getLmd() {
        return lmd;
    }
	
	
}
