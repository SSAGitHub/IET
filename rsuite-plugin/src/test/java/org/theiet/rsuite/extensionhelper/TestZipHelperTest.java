package org.theiet.rsuite.extensionhelper;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import junit.framework.Assert;

import org.junit.Test;
import org.mockito.Mockito;
import org.theiet.rsuite.books.BooksConstans;
import org.theiet.rsuite.mocks.api.service.ContentAssemblyServiceMock;
import org.theiet.rsuite.mocks.api.service.ManagedObjectServiceMock;

import com.reallysi.rsuite.api.ConfigurationProperties;
import com.reallysi.rsuite.api.ContentAssembly;
import com.reallysi.rsuite.api.User;
import com.reallysi.rsuite.api.extensions.ExecutionContext;
import com.reallysi.rsuite.service.AuthorizationService;
import com.reallysi.rsuite.service.ContentAssemblyService;
import com.reallysi.rsuite.service.MailService;
import com.rsicms.rsuite.helpers.download.ZipHelper;
import com.rsicms.rsuite.helpers.download.ZipHelperConfiguration;

//THIS test is not completed yet
public class TestZipHelperTest implements BooksConstans {

	@Test	
	public void test() throws Exception {
        
		
		ExecutionContext context = Mockito.mock(ExecutionContext.class);

		ConfigurationProperties configurationMock = Mockito.mock(ConfigurationProperties.class);
		
		AuthorizationService authorizationSvcMock = Mockito.mock(AuthorizationService.class);
		
		MailService mailSvc = Mockito.mock(MailService.class);
		
		User sampleUser = Mockito.mock(User.class);
		
		ContentAssemblyService caSvcMock = new ContentAssemblyServiceMock();
		
		ManagedObjectServiceMock moSvc = new ManagedObjectServiceMock();
		
		
		Mockito.when(context.getContentAssemblyService()).thenReturn(caSvcMock);
		Mockito.when(context.getConfigurationProperties()).thenReturn(configurationMock);
		Mockito.when(context.getAuthorizationService()).thenReturn(authorizationSvcMock);
		Mockito.when(context.getMailService()).thenReturn(mailSvc);
		Mockito.when(context.getManagedObjectService()).thenReturn(moSvc);
		
		Mockito.when(authorizationSvcMock.findUser(null)).thenReturn(sampleUser);
		
		
		
		
		ContentAssembly ca = caSvcMock.createContentAssembly(null, "111", "Sample book edition", null);
		
		caSvcMock.createContentAssembly(null, ca.getId(), "test", null);
		caSvcMock.createContentAssembly(null, ca.getId(), "test", null);
		
		ByteArrayOutputStream fos = new ByteArrayOutputStream();
		
		ZipHelper.zipContentAssemblyContents(context, ca, fos, new ZipHelperConfiguration());
		
		ByteArrayOutputStream fos2 = new ByteArrayOutputStream();
		ZipHelper.zipContentAssemblyContents(context, ca, fos2, new ZipHelperConfiguration());

		checkZipFile(fos);
		checkZipFile(fos2);
         
         
         
	}

	public void checkZipFile(ByteArrayOutputStream fos2) throws IOException {
		ByteArrayInputStream input = new ByteArrayInputStream(fos2.toByteArray());
		 
         ZipInputStream zip = new ZipInputStream(input);
         ZipEntry entry = zip.getNextEntry();
         
         Assert.assertEquals("test/", entry.getName());
         entry = zip.getNextEntry();
         
         Assert.assertEquals("test(1)/", entry.getName());
	}
	
}
