package org.theiet.rsuite.iettv.domain.ingestion;

import org.apache.commons.lang.StringUtils;
import org.theiet.rsuite.iettv.domain.datatype.VideoChannel;
import org.theiet.rsuite.iettv.domain.datatype.VideoRecordMetadata;

public class VideoInspecChecker {

	private VideoInspecChecker() {
	}

	public static boolean inspecRequried(VideoRecordMetadata videoMetadata) {

		if (hasNoAccessionNumber(videoMetadata) && containsTechnologyChannel(videoMetadata)) {
			return true;
		}

		return false;
	}

	private static boolean hasNoAccessionNumber(VideoRecordMetadata videoMetadata) {
		return StringUtils.isBlank(videoMetadata.getAccessionNumber());
	}

	private static boolean containsTechnologyChannel(VideoRecordMetadata videoMetadata) {
		for (VideoChannel videoChannel : videoMetadata.getChannels()) {
			if ("Technology".equals(videoChannel.getCategory())) {
				return true;
			}
		}
		return false;
	}
}
