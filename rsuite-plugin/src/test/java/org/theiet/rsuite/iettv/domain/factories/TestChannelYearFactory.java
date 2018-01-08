package org.theiet.rsuite.iettv.domain.factories;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.theiet.rsuite.iettv.domain.datatype.VideoChannel;
import org.theiet.rsuite.iettv.domain.datatype.VideoRecordMetadata;
import org.theiet.rsuite.iettv.domain.factories.ChannelYearFactory;
import org.theiet.rsuite.iettv.domain.search.IetTvFinder;
import org.theiet.rsuite.utils.IetUtils;

import com.reallysi.rsuite.api.ContentAssembly;
import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.User;
import com.reallysi.rsuite.api.extensions.ExecutionContext;
import com.reallysi.rsuite.service.AuthorizationService;
import com.reallysi.rsuite.service.ManagedObjectService;
import com.rsicms.projectshelper.datatype.RSuiteCaType;
import com.rsicms.projectshelper.utils.ProjectContentAssemblyUtils;

@RunWith(PowerMockRunner.class)
@PrepareForTest({IetTvFinder.class, IetUtils.class, ProjectContentAssemblyUtils.class})
public class TestChannelYearFactory {

    @Test
    public void test_getOrCreateChannelYearContainer_checkIfCreated() throws RSuiteException {
        PowerMockito.mockStatic(IetTvFinder.class);
        PowerMockito.mockStatic(IetUtils.class);
        PowerMockito.mockStatic(ProjectContentAssemblyUtils.class);

        ExecutionContext context = mock(ExecutionContext.class);
        
        AuthorizationService authorizationServiceStub = mock(AuthorizationService.class);
		when(context.getAuthorizationService()).thenReturn(authorizationServiceStub);
		
		ManagedObjectService moServiceStub = mock(ManagedObjectService.class);
		when(context.getManagedObjectService()).thenReturn(moServiceStub);

        final IetTvXmlDatabaseStub dbStub = new IetTvXmlDatabaseStub();

        stubStaticMethods(dbStub);
        
        VideoRecordMetadata videoMetadata = mock(VideoRecordMetadata.class);
        when(videoMetadata.getYear()).thenReturn("2014");
        when(videoMetadata.getMainChannel()).thenReturn(createVideoChannel());

        ContentAssembly yearContainer =
                ChannelYearFactory.getOrCreateChannelYearContainer(context, videoMetadata);

        assertEquals("2014", yearContainer.getDisplayName());
    }


	private VideoChannel createVideoChannel() {
		return new VideoChannel("IT", "Category", false);
	}


    @Test
    public void test_getOrCreateChannelYearContainer_multipleThreads_checkIfCreated()
            throws Exception {
        PowerMockito.mockStatic(IetTvFinder.class);
        PowerMockito.mockStatic(IetUtils.class);
        PowerMockito.mockStatic(ProjectContentAssemblyUtils.class);

        final ExecutionContext context = mock(ExecutionContext.class);
        
        AuthorizationService authorizationServiceStub = mock(AuthorizationService.class);
		when(context.getAuthorizationService()).thenReturn(authorizationServiceStub);
		
		ManagedObjectService moServiceStub = mock(ManagedObjectService.class);
		when(context.getManagedObjectService()).thenReturn(moServiceStub);

        final IetTvXmlDatabaseStub dbStub = new IetTvXmlDatabaseStub();

        stubStaticMethods(dbStub);

        final VideoRecordMetadata videoMetadata = mock(VideoRecordMetadata.class);
        when(videoMetadata.getYear()).thenReturn("2014");
        when(videoMetadata.getMainChannel()).thenReturn(createVideoChannel());

        List<TestChannelYearFactoryThread> testThreadList = createThreads(context, videoMetadata);
        for (TestChannelYearFactoryThread testThread : testThreadList) {
            assertTrue(testThread.isSuccedded());
        }


    }


    private List<TestChannelYearFactoryThread> createThreads(ExecutionContext context, VideoRecordMetadata videoMetadata)
            throws Exception {
        int numberOfThreads = 4;
        CyclicBarrier gate = new CyclicBarrier(numberOfThreads + 1);
        CountDownLatch latch = new CountDownLatch(4);
        List<TestChannelYearFactoryThread> testThreadList = new ArrayList<TestChannelYearFactoryThread>();

        for (int i = 0; i < numberOfThreads; i++) {
            TestChannelYearFactoryThread testThread = new TestChannelYearFactoryThread(context, videoMetadata, gate, latch);
            testThread.start();
        }

        gate.await();
        latch.await();

        return testThreadList;
    }


    private void stubStaticMethods(final IetTvXmlDatabaseStub dbStub) throws RSuiteException {
        stubIetTvFinderFindChannelYearContainer(dbStub);
        stubIetUtilsCreateContentAssembly(dbStub);
        stubIetTvFinderFindChannelContainer(dbStub);
    }

    private void stubIetTvFinderFindChannelContainer(final IetTvXmlDatabaseStub dbStub) throws RSuiteException {
        PowerMockito.when(IetTvFinder.findChannelContainer(any(ExecutionContext.class), any(User.class), any(VideoChannel.class))).thenAnswer(
                new Answer<ContentAssembly>() {

                    @Override
                    public ContentAssembly answer(InvocationOnMock invocation) throws Throwable {
                        Object[] arguments = invocation.getArguments();
                        VideoChannel channel = (VideoChannel) arguments[2];

                        ContentAssembly channelCa = dbStub.getCa(channel.getName());

                        if (channelCa == null) {
                            channelCa = mock(ContentAssembly.class);
                            when(channelCa.getId()).thenReturn(channel.getName());
                            //dbStub.addCa(channelCa);
                        }

                        return channelCa;
                    }
                });
    }

    private void stubIetUtilsCreateContentAssembly(final IetTvXmlDatabaseStub dbStub)
            throws RSuiteException {
        PowerMockito.when(
                ProjectContentAssemblyUtils.createContentAssembly(any(ExecutionContext.class), anyString(),
                        anyString(), Mockito.any(RSuiteCaType.class))).thenAnswer(new Answer<ContentAssembly>() {

            @Override
            public ContentAssembly answer(InvocationOnMock invocation) throws Throwable {
                Object[] arguments = invocation.getArguments();
                String newCaId = arguments[1] + "_" + arguments[2];
                ContentAssembly yearStub = mock(ContentAssembly.class);
                when(yearStub.getId()).thenReturn(newCaId);
                when(yearStub.getDisplayName()).thenReturn(arguments[2].toString());
                dbStub.addCa(yearStub);

                return yearStub;
            }
        });
    }

    private void stubIetTvFinderFindChannelYearContainer(final IetTvXmlDatabaseStub dbStub) throws RSuiteException {
        PowerMockito.when(
                IetTvFinder.findChannelYearContainer(any(ExecutionContext.class), any(User.class), any(VideoChannel.class), anyString()))
                .thenAnswer(new Answer<ContentAssembly>() {

                    @Override
                    public ContentAssembly answer(InvocationOnMock invocation) throws Throwable {
                        Object[] arguments = invocation.getArguments();
                        VideoChannel channel = (VideoChannel) arguments[2];

                        return dbStub.getCa(channel.getName() + "_" + arguments[3]);
                    }
                });
    }

}
