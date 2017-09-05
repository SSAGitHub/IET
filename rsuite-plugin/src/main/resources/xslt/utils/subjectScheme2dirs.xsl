<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
  xmlns:xs="http://www.w3.org/2001/XMLSchema"
  xmlns:xd="http://www.oxygenxml.com/ns/doc/xsl"
  exclude-result-prefixes="xs xd"
  version="2.0">
  
  <!-- Uses a subjectScheme map to create directories
       magic files for use by the RSI LoadInitialContent Groovy script.
    -->
  
  <xsl:output method="text"/>
  
  <xsl:template match="/">
    <xsl:variable name="myPath"  select="'/Users/ekimber/workspace/iet/sample_data/initial_browse_tree'" as="xs:string"/>
    <xsl:result-document href="{$myPath}/_rsuiteNodeType_root.txt">
      <xsl:text>ACL {
  roles {
    any="list,view"
  }
}</xsl:text>
    </xsl:result-document>
    <xsl:apply-templates>
      <xsl:with-param name="parentPath" as="xs:string"
         select="$myPath"
       />
    </xsl:apply-templates>
  </xsl:template>
  
  <xsl:template match="subjectdef">
    <xsl:param name="parentPath" as="xs:string"/>
    <xsl:variable name="myPath" select="normalize-space(topicmeta/navtitle)"/>
    <xsl:variable name="newDir" select="concat($parentPath, '/', $myPath)" as="xs:string"/>
    <xsl:variable name="resultUrl" select="concat($newDir, '/', '_rsuiteNodeType_folder.txt')" 
      as="xs:string"/>
    <xsl:message> + [INFO] File: <xsl:sequence select="$resultUrl"/></xsl:message>
    <xsl:result-document href="{$resultUrl}">
      <xsl:text>ACL {
  roles {
    any="list,view"
  }
}</xsl:text>
    </xsl:result-document>
    <xsl:apply-templates select="subjectdef">
      <xsl:with-param name="parentPath" as="xs:string"
        select="$newDir"
      />
    </xsl:apply-templates>
  </xsl:template>
  
  <xsl:template match="text()"/><!-- Suppress all text by default -->
  
</xsl:stylesheet>