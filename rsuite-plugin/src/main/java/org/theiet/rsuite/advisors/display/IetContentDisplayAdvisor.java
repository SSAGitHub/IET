package org.theiet.rsuite.advisors.display;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.theiet.rsuite.IetIconsConstans;
import org.theiet.rsuite.datamodel.BrowserTreeStatusIcon;
import org.theiet.rsuite.iettv.constants.IetTvLmdFields;
import org.theiet.rsuite.journals.JournalConstants;
import org.theiet.rsuite.journals.domain.article.Article;
import org.theiet.rsuite.journals.domain.article.ArticleFactory;
import org.theiet.rsuite.journals.domain.article.metadata.ArticleMetadata;
import org.theiet.rsuite.onix.OnixConstants;
import org.theiet.rsuite.standards.StandardsBooksConstans;

import com.reallysi.rsuite.api.ManagedObject;
import com.reallysi.rsuite.api.ObjectType;
import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.User;
import com.reallysi.rsuite.api.content.ContentAdvisorContext;
import com.reallysi.rsuite.api.content.ContentDisplayAdvisor;
import com.reallysi.rsuite.api.content.ContentDisplayObject;
import com.rsicms.projectshelper.utils.ProjectContentAssemblyUtils;

/**
 * Updates the browse tree as appropriate.
 * 
 */
public class IetContentDisplayAdvisor implements ContentDisplayAdvisor,
		StandardsBooksConstans, JournalConstants, IetIconsConstans, OnixConstants {


	public enum DisplayCaCondition {
		EQUALS, NOT_BLANK, NOT_EQUALS
	}

	private static Map<String, BrowserTreeStatusIcon> typesetterImageMap = new HashMap<String, BrowserTreeStatusIcon>();
	
	private static Map<String, BrowserTreeStatusIcon> booksIconImageMap = new HashMap<String, BrowserTreeStatusIcon>();
	static {
		initializeTypesetterImageMap();
		
		initializeBookIconMap();
	}

	private static void initializeBookIconMap() {
		booksIconImageMap.put(LMD_VALUE_WORKFLOW_STARTED, new BrowserTreeStatusIcon(ICON_RED_FLAG,
				"Handed over to production"));
		booksIconImageMap.put(LMD_VALUE_UPDATE, new BrowserTreeStatusIcon(
					ICON_YELLOW_FLAG, "Update typesetter request"));
		booksIconImageMap.put(LMD_VALUE_FINAL, new BrowserTreeStatusIcon(ICON_BLUE_FLAG_PROOF_PNG,
					"Final typesetter request"));
		booksIconImageMap.put(LMD_VALUE_FINAL + LMD_VALUE_PROOF_SUFFIX , new BrowserTreeStatusIcon(ICON_FINAL_FLAG,					
					"Print file received"));
	}

	private static void initializeTypesetterImageMap() {
		typesetterImageMap.put(LMD_VALUE_INITIAL, new BrowserTreeStatusIcon(
				ICON_RED_FLAG, "Initial typesetter request"));
		typesetterImageMap.put(LMD_VALUE_UPDATE, new BrowserTreeStatusIcon(
				ICON_YELLOW_FLAG, "Update typesetter request"));
		typesetterImageMap.put(LMD_VALUE_FINAL, new BrowserTreeStatusIcon(ICON_FINAL_FLAG,
				"Final typesetter request"));		
	}
	
	

	@Override
	public void adjustContentItem(ContentAdvisorContext context,
			ContentDisplayObject item) throws RSuiteException {
		ManagedObject mo = getRealMo(context, item.getManagedObject());

		ObjectType objType = mo.getObjectType();
		StringBuffer label = createLabel(mo);
		StringBuffer title = createTitle(mo);

		ContentDisplayCaLabelAdjuster caAdjuster = new ContentDisplayCaLabelAdjuster(
				mo, label, title);
		
		if (objType == ObjectType.CONTENT_ASSEMBLY) {			
			updateCALabel(context, mo, caAdjuster);
		} else if (objType == ObjectType.MANAGED_OBJECT || objType == ObjectType.MANAGED_OBJECT_NONXML) {
			updateMOLabel(mo, caAdjuster);
		}

		if (caAdjuster.isAdjustedLabel()) {
			item.setLabel(caAdjuster.getNewLabel());
		}

	}

	private void updateMOLabel(ManagedObject mo, ContentDisplayCaLabelAdjuster caAdjuster) throws RSuiteException {
		caAdjuster.addLabelIcon(LMD_FIELD_PUBLISH_WORKFLOW_LOG, null,
				DisplayCaCondition.NOT_BLANK,
				new BrowserTreeStatusIcon("workflowLog.png", "Workflow Log", mo.getLayeredMetadataValue(LMD_FIELD_PUBLISH_WORKFLOW_LOG)));

		caAdjuster.addLabelIcon(LMD_FIELD_STANDARDS_IS_REMOVABLE_PUBLICATION_RESULT,
				LMD_VALUE_NO, DisplayCaCondition.EQUALS,
				new BrowserTreeStatusIcon("publicationResultNotRemovable.png", "Publication result not removable"));

		caAdjuster.addLabelIcon(LMD_FIELD_STANDARDS_PUBLICATION_WITH_WARNINGS,
				LMD_VALUE_YES, DisplayCaCondition.EQUALS,
				new BrowserTreeStatusIcon("warning.png", "Publication with warnings"));

		caAdjuster.addLabelIcon(LMD_FIELD_STANDARDS_PUBLICATION_WITH_ERRORS,
				LMD_VALUE_YES, DisplayCaCondition.EQUALS,
				new BrowserTreeStatusIcon("error.png", "Publication with errors"));

		caAdjuster.addLabelTitle(LMD_FIELD_USER_CREATOR, LMD_FIELD_NAME_USER_CREATOR, null,
				DisplayCaCondition.NOT_BLANK,
				mo.getLayeredMetadataValue(LMD_FIELD_USER_CREATOR));

		caAdjuster.addLabelTitle(LMD_FIELD_DATE_CREATED, LMD_FIELD_NAME_DATE_CREATED, null,
				DisplayCaCondition.NOT_BLANK,
				mo.getLayeredMetadataValue(LMD_FIELD_DATE_CREATED));
	}

	private void updateCALabel(ContentAdvisorContext context, ManagedObject mo, ContentDisplayCaLabelAdjuster caAdjuster) throws RSuiteException {
		String updateType = mo
				.getLayeredMetadataValue(JournalConstants.LMD_FIELD_TYPESETTER_UPDATE_TYPE);
		
		if (isAcademicBook(context, mo)){

			caAdjuster.addLabelIcon(LMD_FIELD_AWAITING_TYPESETTER_UPDATES,
					LMD_VALUE_YES, DisplayCaCondition.EQUALS,
					getIcon(booksIconImageMap, updateType,  getDefaultIcon(ICON_PRINTER, "Typesetting")));
			
			if (updateType != null){
				caAdjuster.addLabelIcon(LMD_FIELD_AWAITING_TYPESETTER_UPDATES,
						null, DisplayCaCondition.EQUALS,
						getIcon(booksIconImageMap, updateType,  getDefaultIcon(ICON_BLUE_FLAG_PROOF_PNG, "IET reviwing proofs")));
			}
		} else { 
			caAdjuster.addLabelIcon(LMD_FIELD_AWAITING_TYPESETTER_UPDATES,
					LMD_VALUE_YES, DisplayCaCondition.EQUALS,
					getTypesetterIcon(updateType));
		}
		
		User user = context.getAuthorizationService().getSystemUser();
		Article article = getArticle(context, mo, user);
		
		if (article != null){
			
			ArticleMetadata articleMetadata = article.getArticleMetadata();
			
			caAdjuster.addLabelIcon(articleMetadata.isAvailable(), 
					getIconWithEmptyStatus("efirst.jpg"));
			
			caAdjuster.addLabelIcon(article.getArticleMetadata().hasSupplementaryMaterial(),
					new BrowserTreeStatusIcon("supplementaryMaterialSymbol.png", "Supplementary Metarials Attached"));
			
			caAdjuster.addLabelIcon(articleMetadata.isSpecialIssue(),
					new BrowserTreeStatusIcon("special_issue.png", "Special issue: " + article.getArticleMetadata().getSpecialIssueTitle()));
		}
		
		caAdjuster.addLabelIcon(LMD_FIELD_AWAITING_AUTHOR_COMMENTS,
				LMD_VALUE_YES, DisplayCaCondition.EQUALS,
				getIconWithEmptyStatus("author.gif"));

		caAdjuster.addLabelIcon(LMD_FIELD_LICENCE_TYPE, LMD_VALUE_IET,
				DisplayCaCondition.NOT_EQUALS,
				getIconWithEmptyStatus("copyright.jpg"));

		caAdjuster.addLabelIcon(LMD_FIELD_DISPLAY_ARTICLE_NOTES_ICON, LMD_VALUE_YES,
				DisplayCaCondition.EQUALS,
				getIconWithEmptyStatus("notes.png"));
		
		
		caAdjuster.addLabelIcon(LMD_FIELD_ONIX_LAST_SYNC_FAILED, LMD_VALUE_TRUE,
				DisplayCaCondition.EQUALS,
				new BrowserTreeStatusIcon("sync_failed.png", "Last onix synchronization failed"));
		
		caAdjuster.addLabelIcon(StandardsBooksConstans.LMD_FIELD_BOOK_STATUS, StandardsBooksConstans.LMD_VALUE_REJECTED,
				DisplayCaCondition.EQUALS,
				new BrowserTreeStatusIcon("reject.png", "Rejected Book editon"));

		caAdjuster.addLabelIcon(StandardsBooksConstans.LMD_FIELD_BOOK_STATUS, StandardsBooksConstans.LMD_VALUE_ARCHIVED,
				DisplayCaCondition.EQUALS,
				new BrowserTreeStatusIcon("pin.png", "Archvied Book edition"));
		
		caAdjuster.adjustLabel(LMD_FIELD_PRINT_PUBLISHED_DATE, null,
				DisplayCaCondition.NOT_BLANK,
				"<span style='color: green;'> &#x2713;</span>");

		caAdjuster.addLabelIcon(IetTvLmdFields.IS_WITHDRAWN.getLmdName(), LMD_VALUE_TRUE,
				DisplayCaCondition.EQUALS,
				new BrowserTreeStatusIcon("withdrawn.png", "Withdrawn video"));
	}

	protected Article getArticle(ContentAdvisorContext context,
			ManagedObject mo, User user) throws RSuiteException {
		return ArticleFactory.getIfArticleContainer(context, user, mo);
	}

	private boolean isAcademicBook(ContentAdvisorContext context,
			ManagedObject mo) throws RSuiteException {
		return CA_TYPE_BOOK.equals(ProjectContentAssemblyUtils.getCaType(context, mo.getId()));
	}	

	private BrowserTreeStatusIcon getIconWithEmptyStatus(String iconFileName) {
		return new BrowserTreeStatusIcon(iconFileName);
	}

	@Override
	public void adjustNodeCollectionList(ContentAdvisorContext context,
			List<ContentDisplayObject> collection) throws RSuiteException {

		/* Following code has been commented out because it will most likely be deprecated or removed in future RSuite versions.
		 * 
		 * ContentDisplayCaSorter cdCaSorter = new ContentDisplayCaSorter(context, collection);
		 * cdCaSorter.sort(ObjectType.CONTENT_ASSEMBLY_REF, CA_TYPE_BOOK, SortCaBy.PRODUCT_CODE);
		 * cdCaSorter.sort(ObjectType.CONTENT_ASSEMBLY, CA_TYPE_ARTICLE, SortCaBy.EXPORT_DATE);
		 * 
		 * */		
	}

	private StringBuffer createLabel(ManagedObject mo) throws RSuiteException {

		String displayName;
		String id;
		String CA_ID = "CA_ID";
		String htmlFormatId = " <span class=\"tag-bracket\">[</span>"
				+ "<span class=\"tag-name\">" + CA_ID + "</span>"
				+ "<span class=\"tag-bracket\">]</span>";
		if (mo == null) {
			displayName = "<span style=\"color: red\">" + "Missing mo "
					+ "</span>";
			id = "<span style=\"color: red\">" + "Missing refenced mo "
					+ "</span>";
		} else {
			displayName = mo.getDisplayName();
			id = mo.getId();
		}
		
		if (displayName == null){
			displayName = "";
		}

		StringBuffer label = new StringBuffer(displayName);
		label.append(htmlFormatId.replace(CA_ID, id));
		
		return label;
	}

	private StringBuffer createTitle(ManagedObject mo) throws RSuiteException {
		StringBuffer title = new StringBuffer(mo.getDisplayName());
		String formatedMoId = " [" + mo.getId() + "]";
		title.append(formatedMoId);

		return title;
	}

	private BrowserTreeStatusIcon getTypesetterIcon(String updateType) {
		return getIcon(typesetterImageMap, updateType, getDefaultIcon(ICON_PRINTER));
	}
	
	
	private BrowserTreeStatusIcon getIcon( Map<String, BrowserTreeStatusIcon> iconMap, String updateType, BrowserTreeStatusIcon defaultIcon) {
		 
		return iconMap.containsKey(updateType) ? iconMap
				.get(updateType) : defaultIcon;
	}
	
	private BrowserTreeStatusIcon getDefaultIcon(String defaultIconFileName){
		return getDefaultIcon(defaultIconFileName, null);
	}
	
	private BrowserTreeStatusIcon getDefaultIcon(String defaultIconFileName, String title){
		
		title = title == null ? "" : title;
		
		return new BrowserTreeStatusIcon(defaultIconFileName, title);
	}

	private ManagedObject getRealMo(ContentAdvisorContext context,
			ManagedObject mo) throws RSuiteException {
		String targetId = mo.getTargetId();
		ManagedObject realMo = mo;
		if (targetId != null) {
			ManagedObject referencedMo = context.getManagedObjectService()
					.getManagedObject(context.getUser(), targetId);
			realMo = (referencedMo == null ? mo : referencedMo);
		}

		return realMo;
	}
}
