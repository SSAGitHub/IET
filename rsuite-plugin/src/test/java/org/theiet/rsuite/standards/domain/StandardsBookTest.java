package org.theiet.rsuite.standards.domain;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.theiet.rsuite.mocks.api.ContentAssemblyMock;
import org.theiet.rsuite.mocks.api.MockUtils;
import org.theiet.rsuite.onix.OnixConstants;
import org.theiet.rsuite.standards.StandardsBooksConstans;
import org.theiet.rsuite.standards.domain.book.StandardsBook;
import org.theiet.rsuite.standards.domain.book.StandardsBookEdition;

import com.reallysi.rsuite.api.ContentAssembly;
import com.reallysi.rsuite.api.ContentAssemblyItem;
import com.reallysi.rsuite.api.ManagedObject;
import com.reallysi.rsuite.api.MetaDataItem;
import com.reallysi.rsuite.api.ObjectType;
import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.User;
import com.reallysi.rsuite.api.control.ContentAssemblyCreateOptions;
import com.reallysi.rsuite.api.control.ObjectAttachOptions;
import com.reallysi.rsuite.api.extensions.ExecutionContext;
import com.reallysi.rsuite.service.ContentAssemblyService;
import com.reallysi.rsuite.service.SearchService;

public class StandardsBookTest implements OnixConstants, StandardsBooksConstans {

	@Test
	public void createBookEdition_newBookEdition_testCAStructure()
			throws Exception {

		StandardsBookEdition bookEdition = createSampleBookEdition();

		List<String> directChildNames = createExptectedChildNames();

		int index = 0;
		for (ContentAssemblyItem item : bookEdition.getBookPublicationCa()
				.getChildrenObjects()) {
			String expectedName = directChildNames.get(index);
			Assert.assertEquals(expectedName, item.getDisplayName());
			index++;
		}

	}

	private StandardsBookEdition createSampleBookEdition()
			throws RSuiteException, FileNotFoundException {
		ExecutionContext context = MockUtils
				.createExecutionContextWithStatefulCaSvcStub();
		ContentAssembly bookCa = Mockito.mock(ContentAssembly.class);

		ContentAssembly configurationsCA = setUpConfigurationsCaStub(context);

		ManagedObject templateOnixMo = setUpOnixTemplateMoStub(context, "0001");

		context.getContentAssemblyService().attach(null,
				configurationsCA.getId(), templateOnixMo, null);

		stubSearchService(context, configurationsCA.getId());

		StandardsBookEdition bookEdition = createBookEdition(context, bookCa);

		return bookEdition;
	}

	private ContentAssembly setUpConfigurationsCaStub(ExecutionContext context)
			throws RSuiteException {
		ContentAssemblyItem templateOnixCaItem = mock(ContentAssemblyItem.class);
		when(templateOnixCaItem.getId()).thenReturn("0001");
		when(templateOnixCaItem.getObjectType()).thenReturn(
				ObjectType.MANAGED_OBJECT);

		ContentAssemblyCreateOptions options = new ContentAssemblyCreateOptions();
		options.setType(CA_TYPE_ONIX_CONFIGURATIONS);
		ContentAssembly configurationsCA = context.getContentAssemblyService()
				.createContentAssembly(null, null, "Configurations", options);
		ContentAssemblyMock caMock = (ContentAssemblyMock) configurationsCA;
		caMock.getAdditionalChilds().add(templateOnixCaItem);
		return configurationsCA;
	}

	private void stubSearchService(ExecutionContext context,
			String configurationCaId) throws RSuiteException {

		List<ManagedObject> results = new ArrayList<ManagedObject>();
		ManagedObject templateMoMock = Mockito.mock(ManagedObject.class);
		Mockito.when(templateMoMock.getId()).thenReturn(configurationCaId);
		results.add(templateMoMock);

		SearchService seachSvc = context.getSearchService();
		Mockito.when(
				seachSvc.executeXPathSearch(any(User.class), anyString(),
						anyInt(), anyInt())).thenReturn(results);
	}

	private List<String> createExptectedChildNames() {
		List<String> directChildNames = new ArrayList<String>();

		directChildNames.add("Editorial");
		directChildNames.add("Production Documentation");
		directChildNames.add("Production Files");
		directChildNames.add("Outputs");
		return directChildNames;
	}

	@Test
	public void createBookEdition_createNewBookEdition_onixAttachedToProductionDocumentCa()
			throws Exception {

		ExecutionContext context = MockUtils.createExecutionContext();

		String productionFilesCaId = "productionFilesCA";

		ContentAssembly bookCa = createBookCaStub(context, productionFilesCaId);

		setUpStubs(context);

		createBookEdition(context, bookCa);

		verify(context.getContentAssemblyService(), times(1)).attach(
				any(User.class), eq(productionFilesCaId),
				any(ManagedObject.class), anyString(),
				any(ObjectAttachOptions.class));

	}

	private StandardsBookEdition createBookEdition(ExecutionContext context,
			ContentAssembly bookCa) throws RSuiteException {
		String editionName = "Sample edition";

		List<MetaDataItem> metadataList = new ArrayList<MetaDataItem>();
		Map<String, String> variables = new HashMap<String, String>();

		StandardsBook standardsBook = new StandardsBook(context, bookCa);

		return standardsBook.createBookEdtion(null, editionName, metadataList,
				variables);
	}

	private void setUpStubs(ExecutionContext context) throws RSuiteException,
			FileNotFoundException {

		String confiugrationsCaId = "2";
		String onixTemplateMoId = "0001";

		setUpConfiugrationsCaStub(context, confiugrationsCaId, onixTemplateMoId);

		setUpOnixTemplateMoStub(context, onixTemplateMoId);

		stubSearchService(context, confiugrationsCaId);
	}

	private void setUpConfiugrationsCaStub(ExecutionContext context,
			String confiugrationsCaId, String onixTemplateMoId)
			throws RSuiteException {
		ContentAssemblyItem templateOnixCaItem = mock(ContentAssemblyItem.class);

		when(templateOnixCaItem.getId()).thenReturn(onixTemplateMoId);
		when(templateOnixCaItem.getObjectType()).thenReturn(
				ObjectType.MANAGED_OBJECT);

		final List<ContentAssemblyItem> list = new ArrayList<ContentAssemblyItem>();
		list.add(templateOnixCaItem);

		ContentAssembly configurationsCA = mock(ContentAssembly.class);
		mockGetChildrenForCa(configurationsCA, list);

		when(
				context.getContentAssemblyService().getContentAssembly(
						any(User.class), eq(confiugrationsCaId))).thenReturn(
				configurationsCA);

		when(configurationsCA.getId()).thenReturn(confiugrationsCaId);
	}

	private ManagedObject setUpOnixTemplateMoStub(ExecutionContext context,
			String onixTemplateMoId) throws RSuiteException,
			FileNotFoundException {
		ManagedObject templateOnixMo = mock(ManagedObject.class);
		when(templateOnixMo.getId()).thenReturn(onixTemplateMoId);
		when(
				templateOnixMo
						.getLayeredMetadataValue(LMD_FIELD_ONIX_CONFIGURATION_TYPE))
				.thenReturn(LMD_VALUE_TEMPLATE);

		File onixTemplateFile = new File(
				"sample_data/onix/theiet-onix-template.xml");

		when(templateOnixMo.getInputStream()).thenReturn(
				new FileInputStream(onixTemplateFile),
				new FileInputStream(onixTemplateFile));

		when(
				context.getManagedObjectService().getManagedObject(
						any(User.class), anyString())).thenReturn(
				templateOnixMo);

		return templateOnixMo;
	}

	private ContentAssembly createBookCaStub(ExecutionContext context,
			String productionFilesCaId) throws RSuiteException {

		ContentAssemblyService contentAssemblyService = context
				.getContentAssemblyService();

		ContentAssembly bookCa = Mockito.mock(ContentAssembly.class);

		ContentAssembly productionFilesCA = createProductionCaStub(productionFilesCaId);

		final List<ContentAssemblyItem> productionFilesCAList = new ArrayList<ContentAssemblyItem>();
		productionFilesCAList.add(productionFilesCA);

		mockGetChildrenForCa(bookCa, productionFilesCAList);

		when(
				contentAssemblyService.getContentAssembly(any(User.class),
						anyString())).thenReturn(bookCa);
		
		when(
				contentAssemblyService.getContentAssembly(any(User.class),
						eq(productionFilesCaId))).thenReturn(productionFilesCA);

		when(
				contentAssemblyService.createContentAssembly(any(User.class),
						anyString(), anyString(),
						any(ContentAssemblyCreateOptions.class))).thenReturn(
				bookCa);

		
		
		return bookCa;
	}

	private ContentAssembly createProductionCaStub(String productionFilesCaId) {
		ContentAssembly productionFilesCA = mock(ContentAssembly.class);
		when(productionFilesCA.getObjectType()).thenReturn(
				ObjectType.CONTENT_ASSEMBLY);
		when(productionFilesCA.getId()).thenReturn(productionFilesCaId);
		when(productionFilesCA.getDisplayName()).thenReturn(
				CA_NAME_STANDARDS_PRODUCTION_DOCUMENTATION);
		return productionFilesCA;
	}

	private void mockGetChildrenForCa(ContentAssembly caMock,
			final List<ContentAssemblyItem> list) throws RSuiteException {
		when(caMock.getChildrenObjects()).thenAnswer(
				new Answer<List<? extends ContentAssemblyItem>>() {

					@Override
					public List<? extends ContentAssemblyItem> answer(
							InvocationOnMock invocation) throws Throwable {

						return list;
					}
				});
	}
}
