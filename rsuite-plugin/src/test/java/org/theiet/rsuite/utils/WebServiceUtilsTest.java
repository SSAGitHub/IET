package org.theiet.rsuite.utils;

import static org.junit.Assert.*;

import java.net.MalformedURLException;

import org.junit.Test;
import org.mockito.Mockito;
import org.theiet.rsuite.IetConstants;

import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.remoteapi.CallArgumentList;
import com.rsicms.projectshelper.webservice.ProjectWebServiceUtils;

public class WebServiceUtilsTest implements IetConstants {

	@Test
	public void testUrlDomainWithPort() throws RSuiteException {

		String urlToTest = "http://localhost:8080/rsuite/rest/v1/api/iet.test?skey=2116041799&_=7jSEOuJ1&rsuiteId=173";
		CallArgumentList args = getCallArgumentListMock(urlToTest);

		String domainUrl = ProjectWebServiceUtils
				.extractDomainURLFromArgumentList(args);

		assertEquals("http://localhost:8080", domainUrl);

	}
	
	@Test
	public void testUrlDomainWithoutPort() throws RSuiteException {

		String urlToTest = "http://rsuite_esteban1.tstus2.rsuitecms.com/rsuite-cms";
		CallArgumentList args = getCallArgumentListMock(urlToTest);

		String domainUrl = ProjectWebServiceUtils
				.extractDomainURLFromArgumentList(args);

		assertEquals("http://rsuite_esteban1.tstus2.rsuitecms.com", domainUrl);

	}

	@Test
	public void testInvalidUrlDomain() {

		String urlToTest = "xxx:ds/localhost:8080/rsuite/rest/v1/api/iet.test?skey=2116041799&_=7jSEOuJ1&rsuiteId=173"; 
		CallArgumentList args = getCallArgumentListMock(urlToTest);

		try{
			ProjectWebServiceUtils
				.extractDomainURLFromArgumentList(args);
		fail("Exception should be thrown");
		}catch(RSuiteException e){
			assertTrue(e.getCause() instanceof MalformedURLException);
		}

	}
	
	private CallArgumentList getCallArgumentListMock(String urlToTest) {
		CallArgumentList args = Mockito.mock(CallArgumentList.class);

		Mockito.when(args.getValues(ARGS_REQUEST_IDENTIFIER_KEY))
				.thenReturn(
						urlToTest);
		return args;
	}

}
