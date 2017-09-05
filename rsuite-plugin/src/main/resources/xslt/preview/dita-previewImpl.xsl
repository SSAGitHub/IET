<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="2.0"
  xmlns:RSUITE="http://www.reallysi.com" 
  xmlns:xs="http://www.w3.org/2001/XMLSchema"
  xmlns:df="http://dita2indesign.org/dita/functions"
  exclude-result-prefixes="RSUITE df xs"
  >
  
  <xsl:template match="/topic" mode="handle-root-element">
    <!-- Elements that are not maps or topics -->
    <html>
      <head>
        <title><xsl:apply-templates select="." mode="page-title"/></title>
        <xsl:apply-templates select="." mode="head"/>
        <xsl:apply-templates select="." mode="linked-css-stylesheet"/>
        <xsl:apply-templates select="." mode="embedded-css-stylesheet"/>
      </head>
      <body>
        <xsl:apply-templates select="*"/>
      </body>
    </html>
  </xsl:template>  

</xsl:stylesheet>
