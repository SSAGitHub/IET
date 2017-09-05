package org.theiet.rsuite.journals.webservice;

import org.theiet.rsuite.journals.JournalConstants;
import org.theiet.rsuite.journals.domain.article.ArticleWithdraw;

import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.remoteapi.*;
import com.rsicms.projectshelper.webservice.ProjectRemoteApiHandler;

public class WithdrawArticleWebService extends ProjectRemoteApiHandler
		implements JournalConstants {

	private static final String DIALOG_TITLE = "Withdraw Article";

	private String moToRefresh = null;
	
	@Override
	protected String exectuteAction(RemoteApiExecutionContext context,
			CallArgumentList args) throws RSuiteException {

		StringBuilder successResponse = new StringBuilder(
				"Article has been withdrawn");
		
		String articleCaId = getMoId(args);
		String articleCaRefId = getMoSourceId(args);
		
		ArticleWithdraw articleWithdraw = new ArticleWithdraw(context, articleCaId, articleCaRefId);
		articleWithdraw.withdrawArticle();
		
		moToRefresh = articleWithdraw.getJournalCa().getId() + "," + articleWithdraw.getMonthCa().getId();

		return successResponse.toString() + ".";
	}

	



	@Override
	public String getMoIdToRefresh() {		
		return moToRefresh;
	}
	
	@Override
	protected String getDialogTitle() {
		return DIALOG_TITLE;
	}
}