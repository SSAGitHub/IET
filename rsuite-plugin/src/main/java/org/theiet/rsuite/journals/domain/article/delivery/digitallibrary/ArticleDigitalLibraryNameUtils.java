package org.theiet.rsuite.journals.domain.article.delivery.digitallibrary;

import org.apache.commons.lang.StringUtils;
import org.theiet.rsuite.journals.domain.journal.Journal;

import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.extensions.ExecutionContext;

public class ArticleDigitalLibraryNameUtils {

	private static final String DEFAULT_PREFIX = "IET-";

	private static final int JOURNAL_CODE_LENGTH = 3;

	public static String getFixedJournalName(Journal journal) throws RSuiteException {

		String journalCode = journal.getJournalCode();
		String customPrefix = journal.getPrefixForDigitaLibrary();

		if (StringUtils.isNotBlank(customPrefix)) {
			return journal.getPrefixForDigitaLibrary();
		} else if (journal.requiresPrefixForDigitaLibrary()) {
			return DEFAULT_PREFIX + journalCode;
		} else {
			return journalCode;
		}
	}

	public static String createDigitalLibraryFinalFileName(ExecutionContext context, String articleId, String ext)
			throws RSuiteException {

		String journalCode = articleId.substring(0, JOURNAL_CODE_LENGTH);
		Journal journal = new Journal(context, journalCode);

		return createDigitalLibraryFinalFileName(journal, articleId, ext);
	}

	public static String createDigitalLibraryBaseName(Journal journal, String articleId) throws RSuiteException {
		String journalCode = articleId.substring(0, JOURNAL_CODE_LENGTH);
		String remainder = articleId.substring(JOURNAL_CODE_LENGTH).replaceFirst(".R\\d+$", "").replaceFirst("SI-", "")
				.replaceAll("-", ".");

		if (journal.requiresPrefixForDigitaLibrary()) {
			journalCode = DEFAULT_PREFIX + journalCode;
		}

		String customPrefix = journal.getPrefixForDigitaLibrary();
		if (StringUtils.isNotBlank(customPrefix)) {
			journalCode = customPrefix;
		}

		return journalCode + remainder;
	}

	public static String createDigitalLibraryFinalFileName(Journal journal, String articleId, String fileExtension)
			throws RSuiteException {
		return createDigitalLibraryBaseName(journal, articleId) + "." + fileExtension;
	}
}
