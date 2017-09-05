package org.theiet.rsuite.journals.domain.article.metadata;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import com.reallysi.rsuite.api.ContentAssembly;
import com.reallysi.rsuite.api.RSuiteException;

public class ArticleMetadataTest {

	@Test
	public void article_availble_when_available_lmd_is_set_yes() throws RSuiteException {
		ContentAssembly contentAssemblyStub = Mockito.mock(ContentAssembly.class);
		Mockito.when(contentAssemblyStub.getLayeredMetadataValue(ArticleMetadataFields.LMD_FIELD_ARTICLE_AVAILABLE)).thenReturn("yes");
		
		ArticleMetadata articleMetadata = new ArticleMetadata(contentAssemblyStub);
		Assert.assertTrue(articleMetadata.isAvailable());
	}

	@Test
	public void article_not_availble_when_available_lmd_is_not_yes() throws RSuiteException {
		ContentAssembly contentAssemblyStub = Mockito.mock(ContentAssembly.class);
		Mockito.when(contentAssemblyStub.getLayeredMetadataValue(ArticleMetadataFields.LMD_FIELD_ARTICLE_AVAILABLE)).thenReturn(null);
		
		ArticleMetadata articleMetadata = new ArticleMetadata(contentAssemblyStub);
		Assert.assertFalse(articleMetadata.isAvailable());
	}
}
