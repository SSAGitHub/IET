package com.rsicms.external.mathml.image.jeuclid;

import java.util.ArrayList;
import java.util.List;

import org.theiet.rsuite.standards.domain.publish.mathml.mathmlToImage.MathMlToImageConventerParameters;

public class JEuclidMathMlEngineParameters implements MathMlToImageConventerParameters {

	private float mathSize;

	private List<String> FontSerif = new ArrayList<String>();

	public JEuclidMathMlEngineParameters(String fontName, float mathSize) {
        this.mathSize = mathSize;
        FontSerif.add(fontName);
    }

	@Override
	public Object getFont() {
		return FontSerif;
	}

	@Override
	public Object getMathSize() {
		return Float.valueOf(mathSize);
	}

}
