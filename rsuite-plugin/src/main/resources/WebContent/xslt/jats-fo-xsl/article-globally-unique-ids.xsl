<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:saxon="http://saxon.sf.net/" xmlns:m="http://www.w3.org/1998/Math/MathML"
    exclude-result-prefixes="saxon" version="2.0">
    
    <xsl:variable name="articleDOI"
        select="article/front/article-meta/article-id[@pub-id-type='doi']" />
    <xsl:variable name="articleID"
        select="concat('i', replace($articleDOI, '([^a-zA-Z0-9])', ''))" />
    
    <xsl:template match="/">
        <xsl:apply-templates/>
    </xsl:template>
    
    
    <xsl:template match="@id | @rid" priority="10">
        <xsl:attribute name="{name()}" namespace="{namespace-uri()}">
            <xsl:value-of select="concat($articleID, '_', .)" />
        </xsl:attribute>
    </xsl:template>
    
    <xsl:template match="@* | node()">
        <xsl:copy>
            <xsl:apply-templates select="node() | @*"/>
        </xsl:copy>
    </xsl:template>
    
</xsl:stylesheet>