package org.theiet.rsuite.standards.domain.publish.mathml;

import org.apache.commons.lang.StringUtils;

public class MathMlEntryProcessorParameters {

    private String fontName = "times new roman";
    
    private float fontSize;
    
    public MathMlEntryProcessorParameters(String fontName, float fontSize) {
        this.fontSize = fontSize;
        
    	if (StringUtils.isNotEmpty(fontName)) {
        	this.fontName = fontName;
        }
    }

    public String getFontName() {
        return fontName;
    }

    public float getFontSize() {
        return fontSize;
    }    
}
