package org.theiet.rsuite.standards.datatype;

import com.rsicms.projectshelper.datatype.RSuiteCaType;

public enum StandardsCaTypes implements RSuiteCaType {
    
    STANDARDS_BOOK_EDITION("standardsBookEdition"),
    STANDARDS_PLUBISH_HISTORY("standardsPublishHistory"),
    STANDARDS_PRINT_FILES("printFiles"),
    STANDARDS_TYPESCRIPT("standardsTypescript"),
    STANDARDS_BOOK("standardsBook"),
    STANDARDS_BOOK_CORRECTIONS("bookCorrections"),
    STANDARDS_PRINTER_INSTRUCTIONS("printerInstructions"),
    STANDARDS_TEMPLATE("template"),
    STANDARDS_IMAGES("standardsImages");
    
    
    private String typeName;
    
    private StandardsCaTypes(String typeName) {
        this.typeName = typeName;
    }

    @Override
    public String getTypeName() {
        return typeName;
    }

    
}
