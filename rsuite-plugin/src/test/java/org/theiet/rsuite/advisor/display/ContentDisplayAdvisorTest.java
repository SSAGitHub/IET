package org.theiet.rsuite.advisor.display;

import static org.mockito.Mockito.CALLS_REAL_METHODS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.theiet.rsuite.advisors.display.IetContentDisplayAdvisor;
import org.theiet.rsuite.books.BooksConstans;
import org.theiet.rsuite.journals.JournalConstants;
import org.theiet.rsuite.mocks.api.MockUtils;
import org.theiet.rsuite.mocks.api.content.ContentDisplayObjectMock;
import org.theiet.rsuite.standards.StandardsBooksConstans;

import com.reallysi.rsuite.api.ContentAssembly;
import com.reallysi.rsuite.api.ManagedObject;
import com.reallysi.rsuite.api.ObjectType;
import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.User;
import com.reallysi.rsuite.api.content.ContentAdvisorContext;
import com.reallysi.rsuite.api.content.ContentDisplayObject;
import com.reallysi.rsuite.service.ManagedObjectService;
import com.rsicms.projectshelper.utils.ProjectContentAssemblyUtils;

public class ContentDisplayAdvisorTest implements StandardsBooksConstans,
		JournalConstants {

	@Test
	public void testCancelledStandardsBookStatusIcon() throws RSuiteException {

		try {

			IetContentDisplayAdvisor advisor = new IetContentDisplayAdvisor();
			// given
			ContentAdvisorContext context = mock(ContentAdvisorContext.class);
			MockUtils.enrichExecutionContextMock(context);

			ManagedObject sampleMo = createMoMock(context);

			ContentDisplayObject item = getContenDisplayMock(sampleMo);

			when(
					sampleMo.getLayeredMetadataValue(StandardsBooksConstans.LMD_FIELD_BOOK_STATUS))
					.thenReturn(StandardsBooksConstans.LMD_VALUE_REJECTED);

			// when
			advisor.adjustContentItem(context, item);

			// then
			Assert.assertTrue(item.getLabel()
					.contains("/iet/images/reject.png"));

		} catch (RSuiteException e) {
			Assert.fail();
		}

	}

	@Test
	public void testLicenseIcon() throws RSuiteException {
		// copyright.jpg

		// given
		try {

			IetContentDisplayAdvisor advisor = new IetContentDisplayAdvisor();
			// given
			ContentAdvisorContext context = mock(ContentAdvisorContext.class);
			MockUtils.enrichExecutionContextMock(context);

			ManagedObject sampleMo = createMoMock(context);

			ContentDisplayObject item = getContenDisplayMock(sampleMo);

			when(sampleMo.getLayeredMetadataValue(LMD_FIELD_LICENCE_TYPE))
					.thenReturn("notIet");

			// when
			advisor.adjustContentItem(context, item);

			// then
			Assert.assertTrue(item.getLabel().contains(
					"/iet/images/copyright.jpg"));

		} catch (RSuiteException e) {
			Assert.fail();
		}

	}

	@Test
	public void testArichivedEdition() throws RSuiteException {

		// given
		ContentAdvisorContext context = mock(ContentAdvisorContext.class);
		MockUtils.enrichExecutionContextMock(context);

		ManagedObject sampleMo = createMoMock(context);

		when(sampleMo.getLayeredMetadataValue(LMD_FIELD_BOOK_STATUS))
				.thenReturn(LMD_VALUE_ARCHIVED);

		ContentDisplayObject item = runContentDisplayAdvisor(context, sampleMo);

		// then
		Assert.assertTrue(item.getLabel().contains("/iet/images/pin.png"));

	}
	
	@Test
	public void display_efirst_icon_when_article_available() throws RSuiteException {

		// given
		ContentAdvisorContext context = mock(ContentAdvisorContext.class);
		MockUtils.enrichExecutionContextMock(context);

		ManagedObject sampleMo = createMoMock(context, "article");


		ContentDisplayObject item = getContenDisplayMock(sampleMo);
		// when
		IetContentDisplayAdvisor advisor = new IetContenDisplayAdvisorTestableArticle();
		advisor.adjustContentItem(context, item);
				

		// then
		Assert.assertTrue(item.getLabel().contains("/iet/images/efirst"));

	}
	
	private ContentDisplayObject runContentDisplayAdvisor(
			ContentAdvisorContext context, ManagedObject sampleMo)
			throws RSuiteException {
		ContentDisplayObject item = getContenDisplayMock(sampleMo);
		// when
		IetContentDisplayAdvisor advisor = new IetContentDisplayAdvisor();
		advisor.adjustContentItem(context, item);
		return item;
	}

	private ManagedObject createMoMock(ContentAdvisorContext context, String type) throws RSuiteException{
		ManagedObject sampleMo = type == null ? getMoMock(context) : getMoMock(context, type);

		ManagedObjectService moSvcMock = Mockito
				.mock(ManagedObjectService.class);
		Mockito.when(context.getManagedObjectService()).thenReturn(moSvcMock);

		when(
				moSvcMock.getManagedObject(Mockito.any(User.class),
						Mockito.anyString())).thenReturn(sampleMo);
		return sampleMo;	
	}
	
	private ManagedObject createMoMock(ContentAdvisorContext context)
			throws RSuiteException {
		return createMoMock(context, null);
	}

	@Test
	public void testPublishDateMark() throws RSuiteException {

		try {

			IetContentDisplayAdvisor advisor = new IetContentDisplayAdvisor();
			// given
			ContentAdvisorContext context = mock(ContentAdvisorContext.class);

			MockUtils.enrichExecutionContextMock(context);

			ManagedObject sampleMo = createMoMock(context);

			ContentDisplayObject item = getContenDisplayMock(sampleMo);

			when(
					sampleMo.getLayeredMetadataValue(LMD_FIELD_PRINT_PUBLISHED_DATE))
					.thenReturn("2012-02-01");

			// when
			advisor.adjustContentItem(context, item);

			// then
			Assert.assertTrue(item.getLabel().contains(
					"<span style='color: green;'>"));

			Assert.assertTrue(item.getLabel().contains("&#x2713;"));

		} catch (RSuiteException e) {
			Assert.fail();
		}

	}

	private ContentDisplayObject getContenDisplayMock(ManagedObject sampleMo)
			throws RSuiteException {
		ContentDisplayObject item = mock(ContentDisplayObjectMock.class);
		when(item.getLabel()).then(CALLS_REAL_METHODS);
		Mockito.doCallRealMethod().when(item)
				.setLabel(Mockito.any(String.class));
		when(item.getManagedObject()).thenReturn(sampleMo);
		return item;
	}

	private ManagedObject getMoMock(ContentAdvisorContext context, String type)
			throws RSuiteException {
		ContentAssembly bookCa = ProjectContentAssemblyUtils
				.createContentAssembly(context, "111", "Sample container",
						type);

		ManagedObject sampleMo = mock(ManagedObject.class);
		when(sampleMo.getDisplayName()).thenReturn("Sample container");
		when(sampleMo.getObjectType()).thenReturn(ObjectType.CONTENT_ASSEMBLY);
		when(sampleMo.getId()).thenReturn(bookCa.getId());
		return sampleMo;
	}
	
	private ManagedObject getMoMock(ContentAdvisorContext context)
			throws RSuiteException {
		return getMoMock(context, BooksConstans.CA_TYPE_BOOK);
	}

}
