package org.theiet.rsuite.iettv.domain.datatype;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;


import org.junit.Test;
import org.mockito.*;
import org.w3c.dom.*;

import com.reallysi.rsuite.api.*;
import com.reallysi.rsuite.api.control.ObjectUpdateOptions;
import com.reallysi.rsuite.api.control.XmlObjectSource;
import com.reallysi.rsuite.service.ManagedObjectService;
import com.rsicms.projectshelper.utils.browse.ProjectBrowserHelper;
import com.rsicms.test.TestHelper;

public class VideoRecordInspecTest {

	@Test
	public void should_merge_video_inspec_video_record() throws Exception {
		
		ManagedObject inspecMo = createVideoInspecMoStub("videoInspec.xml");
		
		
		ProjectBrowserHelper browserHelperMock = mock(ProjectBrowserHelper.class);
		when(browserHelperMock.getChildMoByNodeName(Mockito.any(ContentAssembly.class),  (String[])anyVararg())).thenReturn(inspecMo);
		
		VideoRecordInspec ietTvInspec = new VideoRecordInspec(browserHelperMock);
		
		ManagedObjectService moService = mock(ManagedObjectService.class);
		ContentAssembly videoRecordContainer =  Mockito.mock(ContentAssembly.class);
		
		ManagedObject videoRecordMo = createVideoMoStub("videoRecord.xml");
	
		boolean result = ietTvInspec.mergeVideoInspec(moService, null, videoRecordContainer, videoRecordMo);
		
		assertThat(result, is(true));
		verify(browserHelperMock).getChildMoByNodeName(Mockito.any(ContentAssembly.class), eq("VideoInspec"), eq("IETTV-Inspec"));
		
	}
	
	@Test
	public void should_merge_iettv_inspec_into_video_record() throws Exception {
		ManagedObject inspecMo = createVideoInspecMoStub("ietTvInspec.xml");
		
		ProjectBrowserHelper browserHelperMock = mock(ProjectBrowserHelper.class);
		when(browserHelperMock.getChildMoByNodeName(Mockito.any(ContentAssembly.class), (String[])anyVararg())).thenReturn(inspecMo);
		
		VideoRecordInspec ietTvInspec = new VideoRecordInspec(browserHelperMock);
		
		ManagedObjectService moService = mock(ManagedObjectService.class);
		ContentAssembly videoRecordContainer =  Mockito.mock(ContentAssembly.class);
		
		ManagedObject videoRecordMo = createVideoMoStub("videoRecord.xml");
		
		
		boolean result = ietTvInspec.mergeVideoInspec(moService, null, videoRecordContainer, videoRecordMo);
		assertThat(result, is(true));

		verify(browserHelperMock).getChildMoByNodeName(Mockito.any(ContentAssembly.class), eq("VideoInspec"), eq("IETTV-Inspec"));
	}
	
	@Test
	public void should_merge_iettv_inspec_into_video_record_by_replacing_old_inspec_element() throws Exception {
		
		ManagedObject inspecMo = createVideoInspecMoStub("ietTvInspec.xml");
		
		ProjectBrowserHelper browserHelperMock = mock(ProjectBrowserHelper.class);
		when(browserHelperMock.getChildMoByNodeName(Mockito.any(ContentAssembly.class), (String[])anyVararg())).thenReturn(inspecMo);
		
		VideoRecordInspec ietTvInspec = new VideoRecordInspec(browserHelperMock);
		
		ManagedObjectService moService = mock(ManagedObjectService.class);
		ContentAssembly videoRecordContainer =  Mockito.mock(ContentAssembly.class);
		
		ManagedObject videoRecordMo = createVideoMoStub("videoRecordWithIetTvInspec.xml");
		
		boolean result = ietTvInspec.mergeVideoInspec(moService, null, videoRecordContainer, videoRecordMo);
		assertThat(result, is(true));
		
		ArgumentCaptor<XmlObjectSource> objectSourceCaptor = ArgumentCaptor.forClass(XmlObjectSource.class);
		verify(moService).update(Mockito.any(User.class), anyString(), objectSourceCaptor.capture(), Mockito.any(ObjectUpdateOptions.class));
		
		XmlObjectSource xmlObjectSource = objectSourceCaptor.getValue();
		Element videoRecord = xmlObjectSource.getElement();
		NodeList inspecElements = videoRecord.getElementsByTagName("IETTV-Inspec");
		
		
		assertThat(inspecElements.getLength(), is(1));

		
	}

	private ManagedObject createVideoMoStub(String filename) throws Exception, RSuiteException {
		ManagedObject videoRecordMo = Mockito.mock(ManagedObject.class) ;
		Document videoRecordDocument = TestHelper.getTestDocument(getClass(), filename);
		when(videoRecordMo.getElement()).thenReturn(videoRecordDocument.getDocumentElement());
		return videoRecordMo;
	}

	private ManagedObject createVideoInspecMoStub(String inspecTestFileName) throws Exception, RSuiteException {
		ManagedObject inspecMo = mock(ManagedObject.class);
		Document videoInspecDocument = TestHelper.getTestDocument(getClass(), inspecTestFileName);
		when(inspecMo.getElement()).thenReturn(videoInspecDocument.getDocumentElement());
		return inspecMo;
	}

}
