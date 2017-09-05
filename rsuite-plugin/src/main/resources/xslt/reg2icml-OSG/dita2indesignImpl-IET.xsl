<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
      xmlns:xs="http://www.w3.org/2001/XMLSchema"
      xmlns:local="urn:local-functions"
      xmlns:df="http://dita2indesign.org/dita/functions"
      xmlns:relpath="http://dita2indesign/functions/relpath"
      exclude-result-prefixes="xs local relpath df"
      version="2.0">
  
  <!-- =====================================================================
       DITA to InDesign Transform
       
       Generates InCopy articles (.incx) and/or InDesign INX files from 
       an input map or a single topic.
    
       Copyright (c) 2008, 2012 DITA2InDesign project
    
    Parameters:
    
    chunkLevel - Indicates hierarchical level at which result chunks are created. Default is "0", 
    indicating the entire map is produced as one chunk.
    
    debug - Turns template debugging on and off: 
    
    'true' - Turns debugging on
    'false' - Turns it off (the default)
    =====================================================================-->
  <xsl:import href="rsuite:/res/plugin/dita4publishers/toolkit_plugins/org.dita2indesign.dita2indesign/xsl/dita2indesignImpl.xsl"/>
  <xsl:include href="topic2icmlImpl-IET.xsl"/>

  <xsl:param name="debug" select="'true'"/>

  <xsl:template match="image" mode="images">
    <xsl:variable name="imageName" select="substring-after(@outputclass, 'idImageVar:')"/>
    <xsl:message>IMAGENAME:<xsl:value-of select="@href"/></xsl:message>
    <xsl:element name="{$imageName}">
      <xsl:attribute name="href">
        <xsl:text>file://media</xsl:text>
        <xsl:value-of select="@href"/>
      </xsl:attribute>
    </xsl:element>
  </xsl:template>
  
  <xsl:template match="*" mode="images">
    <xsl:apply-templates mode="images"/>
  </xsl:template>
  
  <xsl:template match="text()" mode="images"/>

</xsl:stylesheet>