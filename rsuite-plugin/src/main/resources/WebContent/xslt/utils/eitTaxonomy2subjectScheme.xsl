<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
  xmlns:xs="http://www.w3.org/2001/XMLSchema"
  xmlns:xd="http://www.oxygenxml.com/ns/doc/xsl"
  exclude-result-prefixes="xs xd"
  version="2.0">

  <xsl:output method="xml"
    indent="yes"
    doctype-public="-//OASIS//DTD DITA Subject Scheme Map//EN" 
    doctype-system="subjectScheme.dtd"  
  />
  
  
  <xsl:template match="/">
    <xsl:apply-templates/>
  </xsl:template>
  
  <xsl:template match="tree">
    <subjectScheme>
      <title>IET Taxonomy, Generated <xsl:value-of select="@Generated"/></title>
      <xsl:apply-templates/>
    </subjectScheme>
  </xsl:template>    
  
  <xsl:template match="node">
    <subjectdef keys="iettax_{@id}">
      <topicmeta>
        <navtitle><xsl:value-of select="@label"/></navtitle>
      </topicmeta>
      <xsl:apply-templates/>
    </subjectdef>
  </xsl:template>
</xsl:stylesheet>