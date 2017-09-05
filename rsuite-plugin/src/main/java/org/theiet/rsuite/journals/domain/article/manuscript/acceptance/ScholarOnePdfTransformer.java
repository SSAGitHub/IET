package org.theiet.rsuite.journals.domain.article.manuscript.acceptance;

import java.awt.Color;
import java.io.*;
import java.util.List;

import org.apache.pdfbox.exceptions.COSVisitorException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.*;

import com.reallysi.rsuite.api.RSuiteException;

public class ScholarOnePdfTransformer {

	private static final String PATH_TO_FONT = "/WebContent/fonts/TimesBold.ttf";

	@SuppressWarnings("rawtypes")
	public int createPdfForPublishOnAcceptance(File scholarOnePdf, File outputPdfFile) throws RSuiteException {

		PDDocument pdfDocument = null;

		try (FileOutputStream resultFileOutputStream = new FileOutputStream(outputPdfFile)) {
			pdfDocument = PDDocument.load(scholarOnePdf);

			List allPages = pdfDocument.getDocumentCatalog().getAllPages();

			for (int i = 0; i < allPages.size(); i++) {
				PDPage page = (PDPage) allPages.get(i);
				addDisclaimerText(pdfDocument, page);
			}

			pdfDocument.removePage(0);
			pdfDocument.save(resultFileOutputStream);

			return pdfDocument.getNumberOfPages();
		} catch (IOException | COSVisitorException e) {
			throw new RSuiteException(0, e.getMessage(), e);
		}
	}

	private void addDisclaimerText(PDDocument doc, PDPage page) throws IOException {
		PDPageContentStream contentStream = new PDPageContentStream(doc, page, true, false);
		
		PDRectangle mediabox = page.findMediaBox();
	    float verticalMargin = 140;
	    float horizontalMargin = 34;
	    float startX = mediabox.getLowerLeftX() + verticalMargin;
	    float startY = mediabox.getUpperRightY() - horizontalMargin;
		
		PDTrueTypeFont font = PDTrueTypeFont.loadTTF(doc, getFontStream());
		float fontSize = 8.0f;
		contentStream.beginText();
		// set font and font size
		contentStream.setFont(font, fontSize);
		contentStream.setNonStrokingColor(Color.RED);
		contentStream.moveTextPositionByAmount(startX, startY);
		contentStream.drawString(
				"This article has been accepted for publication in a future issue of this journal, but has not been fully edited.");

		contentStream.moveTextPositionByAmount(-80, -10);
		contentStream.drawString(
				"Content may change prior to final publication in an issue of the journal. To cite the paper please use the doi provided on the Digital Library page.");

		contentStream.endText();

		contentStream.close();
	}
	
	private InputStream getFontStream() throws IOException{		
		InputStream fontStream = this.getClass().getResourceAsStream(PATH_TO_FONT);

		if (fontStream == null){
			throw new IOException("Unable to localize font " + PATH_TO_FONT);
		}
		
		return fontStream;
	}
}
