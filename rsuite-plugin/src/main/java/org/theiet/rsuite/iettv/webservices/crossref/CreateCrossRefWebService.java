package org.theiet.rsuite.iettv.webservices.crossref;

import org.theiet.rsuite.iettv.domain.datatype.VideoRecord;
import org.theiet.rsuite.iettv.domain.delivery.crossref.CrossRefDocumentCreator;

import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.User;
import com.reallysi.rsuite.api.remoteapi.CallArgumentList;
import com.reallysi.rsuite.api.remoteapi.DefaultRemoteApiHandler;
import com.reallysi.rsuite.api.remoteapi.RemoteApiExecutionContext;
import com.reallysi.rsuite.api.remoteapi.RemoteApiResult;
import com.reallysi.rsuite.api.remoteapi.result.ByteSequenceResult;
import com.rsicms.projectshelper.webservice.ProjectRemoteApiHandler;

public class CreateCrossRefWebService extends DefaultRemoteApiHandler {

	@Override
	public RemoteApiResult execute(RemoteApiExecutionContext context,
			CallArgumentList args) throws RSuiteException {

		String videoContainerId = ProjectRemoteApiHandler.getMoId(args);
		User user = context.getSession().getUser();
		
		VideoRecord videoRecord = new VideoRecord(context, user, videoContainerId);
		videoRecord.startInspecDeliveryWorkflow();
		
		String crossRefXML = CrossRefDocumentCreator.createCrossRefDocument(context, user, videoRecord);
				
		ByteSequenceResult result = new  ByteSequenceResult(crossRefXML.getBytes());
		result.setSuggestedFileName(videoRecord.getVideoId() + "_cross_ref.xml");
		
		return result;
	}
}