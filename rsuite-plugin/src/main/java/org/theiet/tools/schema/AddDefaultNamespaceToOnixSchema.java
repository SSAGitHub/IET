package org.theiet.tools.schema;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class AddDefaultNamespaceToOnixSchema {

    
    public static void main(String[] args) throws Exception {

        File ietTvSchemaFile =
                new File(
                        "doctypes/onix_iet/ONIX_BookProduct_3.0_reference_IET.xsd");

        List<NamespaceToAdd> namespacesToAdd = new ArrayList<NamespaceToAdd>();
        namespacesToAdd.add(new NamespaceToAdd("onix", "http://ns.editeur.org/onix/3.0/reference", true));
        namespacesToAdd.add(new NamespaceToAdd("r", "http://www.rsuitecms.com/rsuite/ns/metadata"));

        AddDefaultNamespaceToSchema namespaceAddition =
                new AddDefaultNamespaceToSchema(ietTvSchemaFile);
        namespaceAddition.addDefaultNamespace(namespacesToAdd);
        System.out.println("done");
    }
}
