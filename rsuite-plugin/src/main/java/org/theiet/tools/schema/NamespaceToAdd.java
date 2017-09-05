package org.theiet.tools.schema;

public class NamespaceToAdd {

    private String prefix;

    private String uri;

    private boolean defaultNamespace = false;

    NamespaceToAdd(String namespacePrefix, String namespaceUri, boolean defaultNamespace) {
        this.prefix = namespacePrefix;
        this.uri = namespaceUri;
        this.defaultNamespace = defaultNamespace;
    }

    NamespaceToAdd(String namespacePrefix, String namespaceUri) {
        this.prefix = namespacePrefix;
        this.uri = namespaceUri;
    }



    public String getPrefix() {
        return prefix;
    }



    public String getUri() {
        return uri;
    }



    public boolean isDefaultNamespace() {
        return defaultNamespace;
    }


}
