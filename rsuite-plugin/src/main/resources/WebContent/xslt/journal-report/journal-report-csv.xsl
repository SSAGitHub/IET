<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xs="http://www.w3.org/2001/XMLSchema" exclude-result-prefixes="xs" version="2.0">
    <xsl:output method="text" indent="no"/>

    <xsl:param name="skey" as="xs:string" select="'unset'"/>
    
    <xsl:strip-space elements="*"/>
    
    <xsl:template match="/"><xsl:text>Manuscript ID,Author,E-mail,Submitted Date,Accepted Date,Article/issue pass for press,Months from Submission to Publication,Months from Acceptance to Publication,Volume Number,Issue Number&#x0D;</xsl:text><xsl:apply-templates/>
    </xsl:template>

    <xsl:template match="production-report">
        <xsl:apply-templates/>
    </xsl:template>

    <xsl:template match="article">
        <xsl:value-of select="@id"/><xsl:text>,</xsl:text><xsl:apply-templates/>
    </xsl:template>

    <xsl:template match="meta-item">
        <xsl:value-of select="."/><xsl:text>,</xsl:text>
    </xsl:template>

</xsl:stylesheet>
