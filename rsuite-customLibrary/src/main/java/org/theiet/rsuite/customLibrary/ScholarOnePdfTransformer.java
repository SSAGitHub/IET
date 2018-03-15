package org.theiet.rsuite.customLibrary;

import java.awt.Color;
import java.io.*;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.PDPageTree;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDTrueTypeFont;
import org.theiet.rsuite.journals.domain.article.manuscript.acceptance.IScholarOnePdfTransformer;
import com.reallysi.rsuite.api.RSuiteException;

public class ScholarOnePdfTransformer implements IScholarOnePdfTransformer {

	private static final String PATH_TO_FONT = "/WebContent/fonts/TimesBold.ttf";

	@SuppressWarnings("rawtypes")
	public int createPdfForPublishOnAcceptance(File scholarOnePdf, File outputPdfFile) throws RSuiteException {

		PDDocument pdfDocument = null;

		try (FileOutputStream resultFileOutputStream = new FileOutputStream(outputPdfFile)) {
			pdfDocument = PDDocument.load(scholarOnePdf);

			PDPageTree allPages =  pdfDocument.getPages();

			for (int i = 0; i < allPages.getCount(); i++) {
				PDPage page = (PDPage) allPages.get(i);
				addDisclaimerText(pdfDocument, page);
			}

			pdfDocument.removePage(0);
			pdfDocument.save(resultFileOutputStream);

			return pdfDocument.getNumberOfPages();
		} catch (IOException e) {
			throw new RSuiteException(0, e.getMessage(), e);
		}
	}

	@SuppressWarnings("deprecation")
	private void addDisclaimerText(PDDocument doc, PDPage page) throws IOException {
		PDPageContentStream contentStream = new PDPageContentStream(doc, page, true,false);
		
		PDRectangle mediabox = page.getMediaBox();
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
