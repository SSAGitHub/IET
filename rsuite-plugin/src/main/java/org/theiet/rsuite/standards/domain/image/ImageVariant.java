package org.theiet.rsuite.standards.domain.image;

public enum ImageVariant {


    THUMBNAIL1("thumbnail1"), THUMBNAIL2("thumbnail2"), MAIN_WEB("mainWeb"), MASTER("master"), ORIGINAL(
            "original");

    private String variantName;

    private ImageVariant(String variantName) {
        this.variantName = variantName;
    }

    public String getVariantName() {
        return variantName;
    }

    public boolean equals(String name){
        return variantName.equals(name);        
    }
}
