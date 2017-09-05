package com.rsicms.projectshelper.export.impl.context;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;

public class MoExportContextXqueryBuilder {

	public static String createContextXquery(String caId) throws IOException {
		String queryTemplate = IOUtils.toString(
				getMoExportContextQueryTemplate(), "utf-8");
		return queryTemplate.replace("$caId$", caId);
	}

	private static InputStream getMoExportContextQueryTemplate() {
		return MoExportContextXqueryBuilder.class
				.getResourceAsStream("/com/rsicms/projectshelper/resources/export/moExportContextQuery.xqy");
	}
}
