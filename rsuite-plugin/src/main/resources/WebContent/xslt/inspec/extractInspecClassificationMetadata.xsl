<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    version="1.0">
    
    <xsl:output 
        doctype-public="-//NLM//DTD JATS (Z39.96) Journal Publishing DTD v1.0 20120330//EN"
        doctype-system="JATS-journalpublishing1.dtd"
    />
    
    <xsl:template match="/">
        <xsl:apply-templates></xsl:apply-templates>
    </xsl:template>
    
    <xsl:template match="article">
        <xsl:apply-templates select="front/article-meta"></xsl:apply-templates>
    </xsl:template>
    
    <xsl:template match="article-meta">
        <xsl:copy>
            <xsl:apply-templates></xsl:apply-templates>
        </xsl:copy>
    </xsl:template>
    
    <xsl:template match="pub-date | title-group| kwd-group | article-id">
        <xsl:copy-of select="."/>            
    </xsl:template>
    
    <xsl:template match="*" />
</xsl:stylesheet>