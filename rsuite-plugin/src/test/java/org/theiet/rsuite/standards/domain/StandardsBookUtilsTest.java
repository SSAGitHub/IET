package org.theiet.rsuite.standards.domain;

import junit.framework.Assert;

import org.junit.Test;
import org.mockito.Mockito;
import org.theiet.rsuite.mocks.api.service.ContentAssemblyServiceMock;
import org.theiet.rsuite.mocks.api.service.ManagedObjectServiceMock;
import org.theiet.rsuite.standards.StandardsBooksConstans;
import org.theiet.rsuite.standards.domain.book.StandardsBookUtils;

import com.reallysi.rsuite.api.ContentAssembly;
import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.extensions.ExecutionContext;
import com.reallysi.rsuite.service.AuthorizationService;
import com.reallysi.rsuite.service.ContentAssemblyService;
import com.reallysi.rsuite.service.ManagedObjectService;

import static org.junit.Assert.*;

public class StandardsBookUtilsTest implements StandardsBooksConstans {


	@Test
	public void testCreatingNewBook() throws RSuiteException{
		
		ExecutionContext context = Mockito.mock(ExecutionContext.class);
		
		AuthorizationService authSvcMock = Mockito.mock(AuthorizationService.class);
		
		ContentAssemblyService caSvcMock = new ContentAssemblyServiceMock();
		ManagedObjectService moSvcMock = new ManagedObjectServiceMock();
		
		Mockito.when(context.getContentAssemblyService()).thenReturn(caSvcMock);
		Mockito.when(context.getManagedObjectService()).thenReturn(moSvcMock);
		Mockito.when(context.getAuthorizationService()).thenReturn(authSvcMock);
		
		String bookTitle = "Sample title";
		String shortTitle = "Short title";
		
		ContentAssembly bookCa = StandardsBookUtils.createStandardBookCa(context, "1111", bookTitle, shortTitle);
		
		assertEquals(StandardsBooksConstans.CA_TYPE_STANDARDS_BOOK, bookCa.getType());
		assertEquals(bookTitle, bookCa.getDisplayName());
		
		assertEquals(bookTitle, bookCa.getLayeredMetadataValue(LMD_FIELD_BOOK_TITLE));
		assertEquals(shortTitle, bookCa.getLayeredMetadataValue(LMD_FIELD_BOOK_TITLE_SHORT));
		
	}
	

	@Test
	public void convertEditonNumberToOrdinal_1_1st(){
		String ordinal = StandardsBookUtils.convertEditonNumberToOrdinal(1);
		Assert.assertEquals("1st", ordinal);
	}
	
	@Test
	public void convertEditonNumberToOrdinal_91_91st(){
		String ordinal = StandardsBookUtils.convertEditonNumberToOrdinal(91);
		Assert.assertEquals("91st", ordinal);
	}
	
	@Test
	public void convertEditonNumberToOrdinal_22_22nd(){
		String ordinal = StandardsBookUtils.convertEditonNumberToOrdinal(22);
		Assert.assertEquals("22nd", ordinal);
	}
	
	@Test
	public void convertEditonNumberToOrdinal_23_23rd(){
		String ordinal = StandardsBookUtils.convertEditonNumberToOrdinal(23);
		Assert.assertEquals("23rd", ordinal);
	}
	
	@Test
	public void convertEditonNumberToOrdinal_11_11th(){
		String ordinal = StandardsBookUtils.convertEditonNumberToOrdinal(11);
		Assert.assertEquals("11th", ordinal);
	}
	
	@Test
	public void convertEditonNumberToOrdinal_212_212th(){
		String ordinal = StandardsBookUtils.convertEditonNumberToOrdinal(212);
		Assert.assertEquals("212th", ordinal);
	}
}
