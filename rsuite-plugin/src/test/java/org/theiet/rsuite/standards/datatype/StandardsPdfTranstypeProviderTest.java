package org.theiet.rsuite.standards.datatype;


import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import org.junit.Test;

public class StandardsPdfTranstypeProviderTest {

	@Test
	public void should_return_wreg_transtype_as_default() throws Exception {
		StandardsPdfTranstypeProvider transtype = StandardsPdfTranstypeProvider.getStandardsPdfTranstypeProvider(null);
		assertThat(transtype, equalTo(StandardsPdfTranstypeProvider.REG_2_PDF_WREG));
	}

	@Test
	public void should_return_gn_transtype_for_pdf_GN_value() throws Exception {
		StandardsPdfTranstypeProvider transtype = StandardsPdfTranstypeProvider.getStandardsPdfTranstypeProvider("pdf-GN");
		assertThat(transtype, equalTo(StandardsPdfTranstypeProvider.REG_2_PDF_GN));
	}
}
