package org.theiet.rsuite.standards.domain.image;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.theiet.rsuite.standards.StandardsBooksConstans;
import org.theiet.rsuite.utils.LoggingUtils;
import org.theiet.rsuite.utils.SearchUtils;
import org.theiet.rsuite.utils.XpathUtils;
import org.theiet.rsuite.utils.ZipUtils;

import com.reallysi.rsuite.api.ManagedObject;
import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.User;
import com.reallysi.rsuite.api.extensions.ExecutionContext;
import com.reallysi.rsuite.service.SearchService;

public class ImageFilesIngestionUnzipHelper implements StandardsBooksConstans {

	public static String BOOK_PREFIX = "bookPrefix";
	public static String BOOK_CA_ID = "bookCAId";
	public static String OUTPUT_DIRECTORY = "outputDirectory";
	
	private User user;
	private SearchService srchSvc;
	private Log log;

	public ImageFilesIngestionUnzipHelper (ExecutionContext context) {
		user = context.getAuthorizationService().getSystemUser();
		srchSvc = context.getSearchService();
		log = LoggingUtils.getProperDomainLogger(context, ImageFilesIngestionUnzipHelper.class);
	}

	public Map<String, String> unzipImagesPackage(File bookImgPkg) throws RSuiteException, IOException {
		Map<String, String> resultVar = new HashMap<String, String>();

		String bookPrefix =  getPrefix(bookImgPkg);
		resultVar.put(BOOK_PREFIX, bookPrefix);
		log.info("Prefix: " + bookPrefix);
		
		String bookCAId = getBookIdWithPrefix(bookPrefix);
		resultVar.put(BOOK_CA_ID, bookCAId);
		log.info("BookId for using prefix \""+ bookPrefix +"\": " + bookCAId);
		
		String outDir = unzipImgPkg(bookImgPkg);
		resultVar.put(OUTPUT_DIRECTORY, outDir);
		log.info("Images unzipping directory: " + outDir);
		
		return resultVar;
	}
	
	private String unzipImgPkg(File bookImgPkg) throws IOException, RSuiteException {
		File workflowDir = bookImgPkg.getParentFile();
		File imgDir = new File(workflowDir, "images");
		imgDir.mkdirs();
		File outDir = ZipUtils.unzip(bookImgPkg, imgDir);

		return outDir.getAbsolutePath();
	}

	private String getBookIdWithPrefix(String bookPrefix) throws RSuiteException {
		String bookWithPrefixQuery = "/rs_ca_map/rs_ca[rmd:get-type(.) = '" + CA_TYPE_STANDARDS_BOOK + "' "
				+ "and rmd:get-lmd-value(., '" + LMD_FIELD_STANDARDS_BOOK_PREFIX + "') = '" + bookPrefix + "']";

		int maxResult = 2;

		List<ManagedObject> booksList = SearchUtils.executeXpathSearch(user,
				bookWithPrefixQuery, srchSvc, maxResult);
		
		if (booksList.size() == 0) {
			throw new RSuiteException(RSuiteException.ERROR_NOT_DEFINED, "No book found with prefix: " + bookPrefix);
		} else if (booksList.size() > 1) {
			throw new RSuiteException(RSuiteException.ERROR_NOT_DEFINED, "Several books with prefix: " + bookPrefix);
		}

		return booksList.get(0).getId();
	}

	private String getPrefix(File bookImgPkg) throws RSuiteException {
		if (bookImgPkg.getName().indexOf("_") <= 0) {
			throw new RSuiteException(RSuiteException.ERROR_NOT_DEFINED, "No prefix found for images package: " + bookImgPkg.getName());
		}
		
		return bookImgPkg.getName().substring(0, bookImgPkg.getName().indexOf("_"));
	}
	
}