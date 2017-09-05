<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
  xmlns:xs="http://www.w3.org/2001/XMLSchema"
  xmlns:xd="http://www.oxygenxml.com/ns/doc/xsl"
  exclude-result-prefixes="xs xd"
  version="2.0">
  
  <xsl:template match="/">
    <html>
      <head>
        <xsl:apply-templates select="document/content/headline" mode="head"/>
      </head>
      <body>
        <xsl:apply-templates/>
      </body>
    </html>
  </xsl:template>
  
  <xsl:template mode="head" match="headline">
    <title><xsl:apply-templates/></title>
  </xsl:template>
  
  <xsl:template match="document">
    <div class="document">
      <xsl:apply-templates/>
    </div>
  </xsl:template>
  
  <xsl:template match="content/headline">
      <h1><xsl:apply-templates/></h1>
  </xsl:template>
  
  <xsl:template match="p">
    <br/>
    <br/>
  </xsl:template>
  
  <xsl:template match="*">
    <xsl:apply-templates/>
  </xsl:template>

</xsl:stylesheet>