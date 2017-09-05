package org.theiet.rsuite.iettv.domain.factories;

import static org.junit.Assert.*;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;

import org.theiet.rsuite.iettv.domain.datatype.VideoRecordMetadata;
import org.theiet.rsuite.iettv.domain.factories.ChannelYearFactory;

import com.reallysi.rsuite.api.ContentAssembly;
import com.reallysi.rsuite.api.extensions.ExecutionContext;

class TestChannelYearFactoryThread extends Thread {

    private ExecutionContext context;
    private VideoRecordMetadata videoMetadata;
    private CyclicBarrier gate;
    private CountDownLatch latch;

    private boolean succedded = false;

    TestChannelYearFactoryThread(ExecutionContext context, VideoRecordMetadata videoMetadata, CyclicBarrier gate,
            CountDownLatch latch) {
        super();
        this.context = context;
        this.videoMetadata = videoMetadata;
        this.gate = gate;
        this.latch = latch;
    }



    public void run() {
        try {
            gate.await();
            ContentAssembly yearContainer =
                    ChannelYearFactory.getOrCreateChannelYearContainer(context, videoMetadata);
            if ("2014".equals(yearContainer.getDisplayName())){
                succedded = true;
            }

            latch.countDown();
        } catch (Exception e) {
            e.printStackTrace();
            fail("Unable to get year container");
        }
    }



    public boolean isSuccedded() {
        return succedded;
    }
    
}
