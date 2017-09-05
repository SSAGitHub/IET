package org.theiet.rsuite.iettv.domain.delivery.crossref;

import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.theiet.rsuite.iettv.domain.datatype.IetTvDomain;
import org.theiet.rsuite.iettv.domain.datatype.VideoRecord;
import org.theiet.rsuite.iettv.domain.delivery.crossref.sequence.CrossRefSequencer;
import org.theiet.rsuite.iettv.domain.search.IetTvFinder;

import com.reallysi.rsuite.api.ManagedObject;
import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.User;
import com.reallysi.rsuite.api.extensions.ExecutionContext;
import com.rsicms.projectshelper.utils.ProjectTransformationUtils;

public class CrossRefDocumentCreator {

	private static final String XSLT_URI_IET_TV_VIDEO_TO_CROSSREF = "rsuite:/res/plugin/iet/xslt/iet-tv/iet-tv-video-record-2-crossref.xsl";
	
	private CrossRefDocumentCreator(){
	}

	public static String createCrossRefDocument(ExecutionContext context, User user,
			VideoRecord videoRecord) throws RSuiteException {
		
		ManagedObject videoMetadataMo = videoRecord.getVideoMetadataMo();

		ByteArrayOutputStream crossRefOutputStream = new ByteArrayOutputStream();
		OutputStreamWriter writer = new OutputStreamWriter(crossRefOutputStream);

		Map<String, String> parameters = createParameters(context, user, videoRecord);

		ProjectTransformationUtils.transformMo(context, videoMetadataMo,
				XSLT_URI_IET_TV_VIDEO_TO_CROSSREF, writer, parameters);

		return createStringFromOutputStream(crossRefOutputStream);
	}

	private static String createStringFromOutputStream(
			ByteArrayOutputStream crossRefOutputStream) throws RSuiteException {
		try {
			return new String(crossRefOutputStream.toByteArray(), "utf-8");
		} catch (UnsupportedEncodingException e) {
			throw new RSuiteException(0, e.getMessage(), e);
		}
	}

	private static Map<String, String> createParameters(ExecutionContext context, User user, VideoRecord videoRecord) throws RSuiteException {
		
		IetTvDomain ietTvDomain = IetTvFinder.findIetTvDomain(context, user);
		
		Map<String, String> parameters = new HashMap<String, String>();
		addParameterIfNotEmpty(parameters, "registrant", ietTvDomain.getRegistrantName());
		addParameterIfNotEmpty(parameters, "depositor_name", ietTvDomain.getPortalAdministrator());
		addParameterIfNotEmpty(parameters, "email_address", ietTvDomain.getEmail());
		addParameterIfNotEmpty(parameters, "parent_doi", ietTvDomain.getParentDoi());
		addParameterIfNotEmpty(parameters, "sqeunce_number", CrossRefSequencer.getNextFormattedSequenceNumber(context));
		
		
		return parameters;
	}
	
	private static void addParameterIfNotEmpty(Map<String, String> parameters, String parameterName, String parameterValue){
		if (StringUtils.isNotBlank(parameterValue)){
			parameters.put(parameterName, parameterValue);
		}
	}
}
