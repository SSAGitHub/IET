package org.theiet.rsuite.datamodel.bookpublication;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.mockito.Mockito;
import org.theiet.rsuite.books.BooksConstans;
import org.theiet.rsuite.onix.OnixConstants;

import com.reallysi.rsuite.api.MetaDataItem;
import com.reallysi.rsuite.api.remoteapi.CallArgumentList;

public class CallArgumentsBookPublicationMapperTest implements BooksConstans, OnixConstants {


	@Test
	public void convertFormCallArgumetnsToMap_providedMetadataList_lmdPresentInMap() {

		String testValue = "testValue";
		String testName = "testLmdName";
		
		CallArgumentsBookPublicationMapper mapper = new CallArgumentsBookPublicationMapper();
		
		CallArgumentList args = Mockito.mock(CallArgumentList.class);
		
		List<MetaDataItem> metadaList = new ArrayList<MetaDataItem>();
		
		metadaList.add(new MetaDataItem(testName, testValue));
		
		Map<String, String> map = mapper.converFormCallArgumentsToMap(args, metadaList);

		assertEquals(testValue, map.get(testName));
	}
	
	@Test
	public void convertFormCallArgumetnsToMap_processIsbnParams_isbnNumbersOnly() {

		String testValue = "432-234 234-3224";
		
		String expectedValue = "4322342343224";
		
		CallArgumentsBookPublicationMapper mapper = new CallArgumentsBookPublicationMapper();
		
		CallArgumentList args = Mockito.mock(CallArgumentList.class);
		
		List<MetaDataItem> metadaList = new ArrayList<MetaDataItem>();
		
		metadaList.add(new MetaDataItem(LMD_FIELD_ISBN, testValue));
		metadaList.add(new MetaDataItem(LMD_FIELD_E_ISBN, testValue));
		
		Map<String, String> map = mapper.converFormCallArgumentsToMap(args, metadaList);

		assertEquals(expectedValue, map.get(LMD_FIELD_ISBN));
		assertEquals(expectedValue, map.get(LMD_FIELD_E_ISBN));
	}


	@Test
	public void convertFormCallArgumetnsToMap_bookTitleParam_bookTitlePresent() {

		String paramName = "bookTitle";
		testParam(paramName);
	
	}
	
	@Test
	public void convertFormCallArgumetnsToMap_subTitleParam_subTitlePresent() {

		String paramName = ONIX_VAR_BOOK_SUBTITLE;
		testParam(paramName);
	
	}
	
	@Test
	public void convertFormCallArgumetnsToMap_editionNumberParam_editionNumberPresent() {

		String paramName = ONIX_VAR_EDITION_NUMBER;
		testParam(paramName);
	
	}

	private void testParam(String paramName) {
		CallArgumentsBookPublicationMapper mapper = new CallArgumentsBookPublicationMapper();
		
		CallArgumentList args = Mockito.mock(CallArgumentList.class);
		Mockito.when(args.getFirstValue(Mockito.eq(paramName))).thenReturn("title");
		List<MetaDataItem> metadaList = new ArrayList<MetaDataItem>();
		
		
		Map<String, String> map = mapper.converFormCallArgumentsToMap(args, metadaList);
		
		assertEquals("title", map.get(paramName));
	}

	@Test
	public void generateFullTitle_titleParamOnly_titleOnly() {

		
		Map<String, String> argumentsMap = new HashMap<String, String>();
		argumentsMap.put("bookTitle", "title");		
				
		CallArgumentsBookPublicationMapper mapper = new CallArgumentsBookPublicationMapper();
		String fullTitle = mapper.generateFullTitle(argumentsMap);
		
		assertEquals("title", fullTitle);
	
	}
	
	@Test
	public void generateFullTitle_titleAndSubtitleParam_concantenatedTitle() {

		
		Map<String, String> argumentsMap = new HashMap<String, String>();
		argumentsMap.put("bookTitle", "title");		
		argumentsMap.put(ONIX_VAR_BOOK_SUBTITLE, "Subtitle");
				
		CallArgumentsBookPublicationMapper mapper = new CallArgumentsBookPublicationMapper();
		String fullTitle = mapper.generateFullTitle(argumentsMap);
		
		assertEquals("title Subtitle", fullTitle);
	
	}
	
	@Test
	public void generateFullTitle_titleAndSubtitleAndEditionNumberParam_concantenatedTitle() {

		
		Map<String, String> argumentsMap = new HashMap<String, String>();
		argumentsMap.put("bookTitle", "title");		
		argumentsMap.put(ONIX_VAR_BOOK_SUBTITLE, "Subtitle");
		argumentsMap.put(ONIX_VAR_EDITION_NUMBER, "2");
				
		CallArgumentsBookPublicationMapper mapper = new CallArgumentsBookPublicationMapper();
		String fullTitle = mapper.generateFullTitle(argumentsMap);
		
		assertEquals("title Subtitle 2", fullTitle);
	
	}
}
