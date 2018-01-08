package org.theiet.rsuite.onix.onix2lmd;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.theiet.rsuite.books.BooksConstans;
import org.theiet.rsuite.mocks.api.MockUtils;
import org.theiet.rsuite.onix.onix2lmd.adjuster.IetOnix2LmdValueAdjuster;
import org.theiet.rsuite.standards.StandardsBooksConstans;

import com.reallysi.rsuite.api.ContentAssembly;
import com.reallysi.rsuite.api.ContentAssemblyReference;
import com.reallysi.rsuite.api.ManagedObject;
import com.reallysi.rsuite.api.MetaDataItem;
import com.reallysi.rsuite.api.ObjectType;
import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.ReferenceInfo;
import com.reallysi.rsuite.api.User;
import com.reallysi.rsuite.api.control.DependencyTracker;
import com.reallysi.rsuite.api.control.ObjectMetaDataSetOptions;
import com.reallysi.rsuite.api.extensions.ExecutionContext;
import com.reallysi.rsuite.service.ContentAssemblyService;


@RunWith(PowerMockRunner.class)
@PrepareForTest({ Onix2LmdMapping.class })
public class Onix2LmdSynchronizerTest implements StandardsBooksConstans, BooksConstans {

	private static final String TEST_ONIX_BASE_PATH = "src/test/resources/org/theiet/rsuite/onix/onix2lmd/";
	
	private static final String TEST_ONIX_XML_CORPORATE = TEST_ONIX_BASE_PATH + "onixCorporate.xml";
	
	private static final String TEST_ONIX_XML_BOOK_NOT_PUBLISHED = TEST_ONIX_BASE_PATH + "onixBookNotPublished.xml";
	
	private static final String TEST_ONIX_XML_BOOK_PUBLISHED = TEST_ONIX_BASE_PATH + "onixBookPublished.xml";
	
	private Map<String, String> addedLmdMapCorporate;
	
	private Map<String, String> addedLmdMapBookNotPublished;
	
	private Map<String, String> addedLmdMapBookPublished;
	
	public Onix2LmdSynchronizerTest() throws Exception{
		addedLmdMapCorporate = runSynchronizationWithCorporate(TEST_ONIX_XML_CORPORATE);
		addedLmdMapBookNotPublished = runSynchronizationWithCorporate(TEST_ONIX_XML_BOOK_NOT_PUBLISHED);
		addedLmdMapBookPublished = runSynchronizationWithCorporate(TEST_ONIX_XML_BOOK_PUBLISHED);
	}
	
	@BeforeClass
	  public static void setUpClass() {
	    System.setProperty("javax.xml.xpath.XPathFactory:http://java.sun.com/jaxp/xpath/dom",
	        "com.sun.org.apache.xpath.internal.jaxp.XPathFactoryImpl");
	  }
	
	@Test
	public void onix2lmd_corporateAuthorName_addedCorporateLmd() {
		assertEquals("Test name", addedLmdMapCorporate.get(LMD_FIELD_CORPORATE_NAME));
	}
	
	@Test
	public void onix2lmd_title_adjustedTitleSubtitleEnditionToFullTitile() {		
		assertEquals("Sample: subtitle, 2nd Edition", addedLmdMapCorporate.get(LMD_FIELD_BOOK_TITLE));
	}
	
	@Test
	public void onix2lmd_book_not_publishedBook_setReforecast() {
		assertEquals("2014-06-11", addedLmdMapBookNotPublished.get(LMD_FIELD_REFORECAST_PUB_DATE));
	}
	
	@Test
	public void onix2lmd_book_not_publishedBook_setReforecast_noActualPubDate() {
		assertTrue( (addedLmdMapBookNotPublished.get(LMD_FIELD_REFORECAST_PUB_DATE) != null && addedLmdMapBookNotPublished.get(LMD_FIELD_ACTUAL_PUB_DATE) == null) );
	}
	
	@Test
	public void onix2lmd_book_publishedBook_setActualPubDate_noReforecast() {		
		assertTrue( (addedLmdMapBookPublished.get(LMD_FIELD_REFORECAST_PUB_DATE) == null && addedLmdMapBookPublished.get(LMD_FIELD_ACTUAL_PUB_DATE) != null) );
	}
	
	@Test
	public void onix2lmd_book_publishedBook_setActualPubDate() {		
		assertEquals("2014-05-11", addedLmdMapBookPublished.get(LMD_FIELD_ACTUAL_PUB_DATE));
	}

	public Map<String, String> runSynchronizationWithCorporate(String onixFilePath) throws RSuiteException,
			IOException {
		ExecutionContext context = MockUtils.createExecutionContext();

		Map<String, String> addedLmdMapCorporate = setUpMockForAddLmdMethod(context);

		ManagedObject onixMo = createStubs(context, onixFilePath);

		Onix2LmdSynchronizer onixSynchronizer = new Onix2LmdSynchronizer(new IetOnix2LmdValueAdjuster());

		onixSynchronizer.synchronizeLmdWithOnix(context, onixMo);
		
		return addedLmdMapCorporate;
	}

	public Map<String,String> setUpMockForAddLmdMethod(ExecutionContext context)
			throws RSuiteException {
		final Map<String, String> addedLmdSet = new HashMap<String, String>();
		
		when(
				context.getManagedObjectService().addMetaDataEntry(
						anyUser(), anyString(), any(MetaDataItem.class),
						any(ObjectMetaDataSetOptions.class))).thenAnswer(
				new Answer<String>() {

					@Override
					public String answer(InvocationOnMock invocation)
							throws Throwable {

						MetaDataItem metaDataItem = (MetaDataItem) invocation.getArguments()[2];
						addedLmdSet.put(metaDataItem.getName(), metaDataItem.getValue());
						return null;
					}
					
				});
		return addedLmdSet;
	}

	private ManagedObject createStubs(ExecutionContext context, String onixFilePath)
			throws RSuiteException, IOException {
		String onixMoId = "onixMoId";
		String bookEditionId = "21213";

		ManagedObject onixMo = createOnixMoStub(context, onixMoId, onixFilePath);

		createBookEditionMoStub(context, bookEditionId);

		createBookEditionCaStub(context, bookEditionId);

		createDependecyTrackerStub(context, onixMoId);

		stupOnix2LmdMapping();

		return onixMo;
	}

	public void stupOnix2LmdMapping() throws RSuiteException {
		mockStatic(Onix2LmdMapping.class);
		when(Onix2LmdMapping.getInstance()).thenReturn(
				new Onix2LmdMappingStub());
	}

	private ManagedObject createOnixMoStub(ExecutionContext context,
			String onixMoId, String onixFilePath) throws RSuiteException, IOException {
		ManagedObject onixMo = mock(ManagedObject.class);

		when(onixMo.getId()).thenReturn(onixMoId);
		
		File sampleFile = new File(onixFilePath);
				
		when(onixMo.getInputStream()).thenReturn(new FileInputStream(sampleFile));
		

		when(
				context.getManagedObjectService().getManagedObject(anyUser(),
						eq(onixMoId))).thenReturn(onixMo);
		return onixMo;
	}

	private void createDependecyTrackerStub(ExecutionContext context,
			String onixMoId) throws RSuiteException {
		ReferenceInfo ref = mock(ReferenceInfo.class);
		when(ref.getParentBrowseUri()).thenReturn(
				"/caref:1116/caref:11005/caref:11011/caref:21169/caref:21214");

		DependencyTracker tracker = context.getManagedObjectService()
				.getDependencyTracker();

		List<ReferenceInfo> refList = new ArrayList<ReferenceInfo>();
		refList.add(ref);
		when(tracker.listDirectReferences(anyUser(), eq(onixMoId))).thenReturn(
				refList);
	}

	private void createProductionDocCaItemStub(String bookEditionId,
			ContentAssemblyService contentAssemblyService)
			throws RSuiteException {
		ContentAssemblyReference productionDocCaItem = mock(ContentAssemblyReference.class);
		when(productionDocCaItem.getTargetId()).thenReturn(bookEditionId);

		when(
				contentAssemblyService.getContentAssemblyItem(anyUser(),
						eq("21214"))).thenReturn(productionDocCaItem);
	}

	private void createBookEditionCaStub(ExecutionContext context,
			String bookEditionId) throws RSuiteException {
		ContentAssembly bookEditionCa = mock(ContentAssembly.class);
		when(bookEditionCa.getType())
				.thenReturn(CA_TYPE_STANDARDS_BOOK_EDITION);
		when(bookEditionCa.getId()).thenReturn(bookEditionId);

		ContentAssemblyService contentAssemblyService = context
				.getContentAssemblyService();
		when(
				contentAssemblyService.getContentAssembly(anyUser(),
						eq(bookEditionId))).thenReturn(bookEditionCa);

		createProductionDocCaItemStub(bookEditionId, contentAssemblyService);

	}

	private void createBookEditionMoStub(ExecutionContext context,
			String bookEditionId) throws RSuiteException {
		ManagedObject bookEditionMo = mock(ManagedObject.class);

		when(bookEditionMo.getObjectType()).thenReturn(
				ObjectType.CONTENT_ASSEMBLY);
		when(bookEditionMo.getId()).thenReturn(bookEditionId);

		when(
				context.getManagedObjectService().getManagedObject(anyUser(),
						eq(bookEditionId))).thenReturn(bookEditionMo);
	}

	private User anyUser() {
		return any(User.class);
	}

}
