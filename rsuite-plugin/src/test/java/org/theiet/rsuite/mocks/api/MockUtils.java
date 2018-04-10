package org.theiet.rsuite.mocks.api;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.net.URI;
import java.util.List;

import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.URIResolver;
import javax.xml.transform.sax.SAXSource;
import javax.xml.xpath.XPathFactory;

import net.sf.saxon.TransformerFactoryImpl;

import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.theiet.rsuite.mocks.api.service.ContentAssemblyServiceMock;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;

import com.reallysi.rsuite.api.ManagedObject;
import com.reallysi.rsuite.api.MetaDataItem;
import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.Session;
import com.reallysi.rsuite.api.User;
import com.reallysi.rsuite.api.control.DependencyTracker;
import com.reallysi.rsuite.api.extensions.ExecutionContext;
import com.reallysi.rsuite.api.report.ReportGenerationContext;
import com.reallysi.rsuite.service.AuthorizationService;
import com.reallysi.rsuite.service.ContentAssemblyService;
import com.reallysi.rsuite.service.ManagedObjectService;
import com.reallysi.rsuite.service.PubtrackManager;
import com.reallysi.rsuite.service.SearchService;
import com.reallysi.rsuite.service.XmlApiManager;

public class MockUtils {

	public static ReportGenerationContext createReportBaseContext()
			throws RSuiteException {
		ReportGenerationContext context = Mockito
				.mock(ReportGenerationContext.class);

		enrichExecutionContextMock(context);

		User user = Mockito.mock(User.class);
		Session sessionMock = Mockito.mock(Session.class);
		Mockito.when(sessionMock.getUser()).thenReturn(user);
		Mockito.when(context.getSession()).thenReturn(sessionMock);

		return context;
	}

	public static ExecutionContext createExecutionContextWithStatefulCaSvcStub()
			throws RSuiteException {
		ExecutionContext context = Mockito.mock(ExecutionContext.class);

		enrichExecutionContextMock(context);

		return context;
	}
	
	public static ExecutionContext createExecutionContext()
			throws RSuiteException {
		ExecutionContext context = Mockito.mock(ExecutionContext.class);

		enrichExecutionContextMock(context, false);

		return context;
	}
	
	public static void enrichExecutionContextMock(ExecutionContext contextMock)
			throws RSuiteException {
		
		enrichExecutionContextMock(contextMock, true);
	}

	public static void enrichExecutionContextMock(ExecutionContext contextMock, boolean statefullCaSvcStub)
			throws RSuiteException {

		AuthorizationService authorizationSvcMock = Mockito
				.mock(AuthorizationService.class);
		Mockito.when(contextMock.getAuthorizationService()).thenReturn(
				authorizationSvcMock);

		User systemUser = Mockito.mock(User.class);
		Mockito.when(authorizationSvcMock.getSystemUser()).thenReturn(
				systemUser);

		PubtrackManager pubtackMgr = Mockito.mock(PubtrackManager.class);
		Mockito.when(contextMock.getPubtrackManager()).thenReturn(pubtackMgr);

		ContentAssemblyService caSvcMock = Mockito.mock(ContentAssemblyService.class);
		if (statefullCaSvcStub){
			caSvcMock = new ContentAssemblyServiceMock();
		}
		
		Mockito.when(contextMock.getContentAssemblyService()).thenReturn(
				caSvcMock);

		SearchService searchSvc = Mockito.mock(SearchService.class);
		Mockito.when(contextMock.getSearchService()).thenReturn(searchSvc);

		

		ManagedObjectService moSvcMock = Mockito
				.mock(ManagedObjectService.class);
		Mockito.when(
				moSvcMock.setMetaDataEntries(Mockito.any(User.class),
						Mockito.anyString(), Mockito.anyList())).thenAnswer(
				new Answer<ManagedObject>() {
					public ManagedObject answer(InvocationOnMock invocation)
							throws Throwable {

						Object[] args = invocation.getArguments();

						String moid = (String) args[1];
						List<MetaDataItem> entries = (List<MetaDataItem>) args[2];

						ContentAssemblyMock ca = ContentAssemblyServiceMock.mocksCaMap
								.get(moid);
						if (ca != null) {
							ca.addMetadataItem(entries);
						}

						return null;
					};
				});
		
		DependencyTracker dependencyTracker = Mockito.mock(DependencyTracker.class);
		Mockito.when(moSvcMock.getDependencyTracker()).thenReturn(dependencyTracker);

		Mockito.when(contextMock.getManagedObjectService()).thenReturn(
				moSvcMock);

		
		
		XmlApiManager xmlApiMgr = createXmlApiManagerMock();
		Mockito.when(contextMock.getXmlApiManager()).thenReturn(xmlApiMgr);
	}

	public static XmlApiManager createXmlApiManagerMock() throws RSuiteException {
		XmlApiManager xmlApiMgr = Mockito.mock(XmlApiManager.class);
		
		when(xmlApiMgr.getTransformer(any(URI.class))).then(
				new Answer<Transformer>() {

					@Override
					public Transformer answer(InvocationOnMock invocation)
							throws Throwable {

						Object[] args = invocation.getArguments();
						return createTransformer((URI) args[0]);
					}

				});

		XPathFactory xpathFactory = new net.sf.saxon.xpath.XPathFactoryImpl();
		when(xmlApiMgr.getXPathFactory()).thenReturn(xpathFactory);
		return xmlApiMgr;
	}

	private static Transformer createTransformer(URI xsltUri) throws Exception {

		TransformerFactory tFactory = TransformerFactoryImpl.newInstance();

		URIResolver resolver = new URIResolver() {

			@Override
			public Source resolve(String href, String base)
					throws TransformerException {
				if (href.startsWith("rsuite:/")) {
					href = href.replace("rsuite:/res/plugin/iet", "src/main/resources/WebContent");
					return new SAXSource(new InputSource(href));
				}
				return null;
			}
		};

		Source baseSource = resolver.resolve(xsltUri.toString(), null);

		Transformer transformer = tFactory.newTransformer(baseSource);
		return transformer;
	}
}
