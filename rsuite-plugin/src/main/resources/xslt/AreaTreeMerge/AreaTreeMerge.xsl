<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xs="http://www.w3.org/2001/XMLSchema" exclude-result-prefixes="xs" version="2.0"
    xmlns:AreaTree="http://www.antennahouse.com/names/XSL/AreaTree">


    <xsl:param name="refs"></xsl:param>
    <xsl:variable name="references" select="document($refs)"/>

    <xsl:template match="/">
        <xsl:apply-templates/>
     </xsl:template>

    <xsl:template match="AreaTree:AreaRoot">
        <xsl:variable name="pageCount" select="count(AreaTree:PageViewportArea)"/>
        <xsl:element name="AreaTree:AreaRoot">
            <xsl:copy-of select="@*"/>
            <xsl:apply-templates/>
            <xsl:for-each select="$references//AreaTree:PageViewportArea">
                <xsl:if test="position() != 1">
                    <xsl:element name="AreaTree:PageViewportArea">
                        <xsl:copy-of select="(@* except @abs-page-number)"/>
                        <xsl:attribute name="abs-page-number" select="$pageCount+position()-1"/>
                        <xsl:copy-of select="*"/>
                    </xsl:element>
                </xsl:if>
            </xsl:for-each>
        </xsl:element>
    </xsl:template>

    <xsl:template match="AreaTree:ColumnReferenceArea">
        <xsl:choose>
            <xsl:when test="not(following::AreaTree:ColumnReferenceArea)">
                <xsl:copy-of select="."/>
                <xsl:for-each select="$references//AreaTree:PageViewportArea[1]//AreaTree:ColumnReferenceArea">
                    <xsl:copy-of select="."/>
                </xsl:for-each>
            </xsl:when>
            <xsl:otherwise>
                <xsl:copy-of select="."/>
            </xsl:otherwise>
        </xsl:choose>

    </xsl:template>
    <xsl:template match="@* | node()">
        <xsl:copy>
            <xsl:apply-templates select="@* | node()"/>
        </xsl:copy>
    </xsl:template>

</xsl:stylesheet>
