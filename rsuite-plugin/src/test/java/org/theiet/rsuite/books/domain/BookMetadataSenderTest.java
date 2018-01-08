package org.theiet.rsuite.books.domain;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.URIResolver;
import javax.xml.transform.sax.SAXSource;

import net.sf.saxon.TransformerFactoryImpl;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.theiet.rsuite.books.BooksConstans;
import org.theiet.rsuite.datamodel.IetBookPublication;
import org.theiet.rsuite.mocks.api.service.ContentAssemblyServiceMock;
import org.theiet.rsuite.mocks.api.service.ManagedObjectServiceMock;
import org.xml.sax.InputSource;

import com.reallysi.rsuite.api.ConfigurationProperties;
import com.reallysi.rsuite.api.ContentAssembly;
import com.reallysi.rsuite.api.MetaDataItem;
import com.reallysi.rsuite.api.User;
import com.reallysi.rsuite.api.extensions.ExecutionContext;
import com.reallysi.rsuite.api.system.MailMessageBean;
import com.reallysi.rsuite.service.AuthorizationService;
import com.reallysi.rsuite.service.ContentAssemblyService;
import com.reallysi.rsuite.service.MailService;
import com.reallysi.rsuite.service.XmlApiManager;
import com.rsicms.projectshelper.utils.ProjectPluginProperties;

//THIS test is not completed yet
public class BookMetadataSenderTest implements BooksConstans {

	@Before
	public void before() throws IOException{
		final File f = new File(ProjectPluginProperties.class.getProtectionDomain().getCodeSource().getLocation().getPath());
		FileUtils.copyDirectoryToDirectory( new File("src/main/resources/WebContent"), f);
		ProjectPluginProperties.reloadProperties();
	}

	@Test	
	public void test() throws Exception {
        
		
		createTransformer(new URI("rsuite:/res/plugin/iet/xslt/books/SendBookMetadata.xsl"));
		
	
		ExecutionContext context = Mockito.mock(ExecutionContext.class);

		ConfigurationProperties configurationMock = Mockito.mock(ConfigurationProperties.class);
		
		AuthorizationService authorizationSvcMock = Mockito.mock(AuthorizationService.class);
		
		MailService mailSvc = Mockito.mock(MailService.class);
		
		User sampleUser = Mockito.mock(User.class);
		
		ContentAssemblyService caSvcMock = new ContentAssemblyServiceMock();
		
		ManagedObjectServiceMock moSvc = new ManagedObjectServiceMock();
		
		XmlApiManager xmlApiMgr = Mockito.mock(XmlApiManager.class);
		
		
		Mockito.when(context.getXmlApiManager()).thenReturn(xmlApiMgr);
		
		when(xmlApiMgr.getTransformer(any(URI.class))).then(new Answer<Transformer>() {

			@Override
			public Transformer answer(InvocationOnMock invocation)
					throws Throwable {

				 Object[] args = invocation.getArguments();
				return createTransformer((URI)args[0]);
			}
			
		});
		
		
		Mockito.when(context.getContentAssemblyService()).thenReturn(caSvcMock);
		Mockito.when(context.getConfigurationProperties()).thenReturn(configurationMock);
		Mockito.when(context.getAuthorizationService()).thenReturn(authorizationSvcMock);
		Mockito.when(context.getMailService()).thenReturn(mailSvc);
		Mockito.when(context.getManagedObjectService()).thenReturn(moSvc);
		
		Mockito.when(authorizationSvcMock.findUser(null)).thenReturn(sampleUser);
		
		Mockito.when(sampleUser.getEmail()).thenReturn("llacki@gmail.com");
	
		final List<MailMessageBean> mailToVerifyList = new ArrayList<MailMessageBean>();
		
		Mockito.doAnswer(new Answer<Void>() {

			@Override
			public Void answer(InvocationOnMock invocation) throws Throwable {
				mailToVerifyList.add((MailMessageBean)invocation.getArguments()[0]);
				return null;
			}
		}).when(mailSvc).send(any(MailMessageBean.class));
		
		ContentAssembly ca = caSvcMock.createContentAssembly(null, "111", "Sample book edition", null);
		
		IetBookPublication publication = new IetBookPublication(ca);
		
		context.getManagedObjectService().setMetaDataEntry(null, ca.getId(), new MetaDataItem(LMD_FIELD_CONTRACTED_TS_DELIVERY_DATE, "2012-02-08"));
		context.getManagedObjectService().setMetaDataEntry(null, ca.getId(), new MetaDataItem("e_product_code", "1234"));
		
		
		String additionalText = "Sample text";
		
		BookMetadaSender sender = new BookMetadaSender();
		sender.sendBookMetadata(context, publication, additionalText);
		
		MailMessageBean result = mailToVerifyList.get(0);
		
	}
	
	

	private Transformer createTransformer(URI xsltUri) throws Exception{
		
		TransformerFactory tFactory = TransformerFactoryImpl.newInstance();

		URIResolver resolver = new URIResolver() {
			
			@Override
			public Source resolve(String href, String base) throws TransformerException {
				if (href.startsWith("rsuite:/")){
				href = href.replace("rsuite:/res/plugin/iet", "src/main/resources/WebContent");
					return new SAXSource(new InputSource(href));
				}
				return null;
			}
		};
		
		 Source baseSource = resolver.resolve(xsltUri.toString(), null);
		

	    Transformer transformer =  tFactory.newTransformer(baseSource);
	   return transformer;
	}
}
