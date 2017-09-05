package org.theiet.rsuite.journals.domain.issues.datatype.metadata;

import static org.theiet.rsuite.journals.domain.issues.datatype.metadata.IssueMetadataFields.*;

import java.util.ArrayList;
import java.util.List;

import com.reallysi.rsuite.api.MetaDataItem;
import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.User;
import com.reallysi.rsuite.service.ManagedObjectService;

public class IssueMetadataUpdater {

	
	private ManagedObjectService moService;
	
	private User user;
	
	private String issueCaId;
	
	private String advertPages;
	
	private String lastNumberedPage;
	
	private String totalPagination;
	
	private String insideFrontCover;
	
	private String insideBackCover;
	
	private String prelims;
	
	private String onlinePublishDate;
	
	
	public IssueMetadataUpdater(User user, ManagedObjectService moService,
			String issueCaId) {
		super();
		this.user = user;
		this.moService = moService;
		this.issueCaId = issueCaId;
	}

	public void setAdvertPages(int advertPages) throws RSuiteException {
		this.advertPages = String.valueOf(advertPages); 		
	}
	
	public void setLastNumberedPage(int lastNumberedPage) throws RSuiteException {
		this.lastNumberedPage = String.valueOf(lastNumberedPage);
	}
	
	public void setTotalPagination(int totalPagination) throws RSuiteException {
		this.totalPagination = String.valueOf(totalPagination);
	}
	
	public void setInsideFrontCoverLegal(){
		insideFrontCover = "Legal";
	}
	
	public void setInsideBacktCoverInstructions(){
		insideBackCover = "Instructions";
	}
	
	public void setPrelims(int prelims){
		this.prelims = String.valueOf(prelims);
	}
	
	public void setOnlinePublishDate(String onlinePublishDate){
		this.onlinePublishDate = onlinePublishDate;
	}
	
	public void updateMetadata() throws RSuiteException{
		
		List<MetaDataItem> items = createMetadataItemsList();
		moService.setMetaDataEntries(user, issueCaId, items);
	}

	private List<MetaDataItem> createMetadataItemsList() {
		List<MetaDataItem> items = new ArrayList<>();
		
		addMetaDataItem(items, LMD_FIELD_ADVERT_PAGES, advertPages);
		addMetaDataItem(items, LMD_FIELD_LAST_NUMBERED_PAGE, lastNumberedPage);
		addMetaDataItem(items, LMD_FIELD_TOTAL_PAGINATION, totalPagination);
		addMetaDataItem(items, LMD_FIELD_INSIDE_FRONT_COVER, insideFrontCover);
		addMetaDataItem(items, LMD_FIELD_INSIDE_BACK_COVER, insideBackCover);
		addMetaDataItem(items, LMD_FIELD_PRELIMS, prelims);
		addMetaDataItem(items, LMD_FIELD_ONLINE_PUBLISHED_DATE, onlinePublishDate);
		return items;
	}
	
	private void addMetaDataItem(List<MetaDataItem> items, String name,
			String value) {
		if (value != null){
			items.add(new MetaDataItem(name, value));
		}
	}
	
}
