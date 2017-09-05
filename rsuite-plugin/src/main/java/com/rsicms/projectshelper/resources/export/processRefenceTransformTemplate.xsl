<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:r="http://www.rsuitecms.com/rsuite/ns/metadata"
    xmlns:exporter="java:com.rsicms.projectshelper.export.impl.refence.ReferenceProcessor"
    xmlns:m="http://www.w3.org/1998/Math/MathML"
    exclude-result-prefixes="r">

    <xsl:param name="rsuite.moId"/>
    <xsl:param name="rsuite.exporter"/>

    <xsl:param name="rsuite.system.id"/>
    <xsl:param name="rsuite.public.id"/>

	<xsl:param name="rsuite.lmd.document"/>

    <xsl:template match="/">
   	 <xsl:result-document doctype-public="{$rsuite.public.id}" doctype-system="{$rsuite.system.id}" method="xml">
        <xsl:apply-templates/>
   	     <xsl:call-template name="embedLmd"/>
     </xsl:result-document>
    </xsl:template>

    <xsl:template name="embedLmd">        
        <xsl:if test="$rsuite.lmd.document">
            <xsl:for-each select="$rsuite.lmd.document/*/*">
                <xsl:variable name="piName" select="concat('lmd_', @name)"/>
                <xsl:processing-instruction name="{$piName}"><xsl:value-of select="@value"/></xsl:processing-instruction>
            </xsl:for-each>
        </xsl:if>
    </xsl:template>
    
    <xsl:template match="$REFENCE_XPATH" priority="3">
        <xsl:variable name="newAttributeValue"
            select="exporter:handleRefernce($rsuite.exporter, $rsuite.moId, .)"/>
        <xsl:attribute name="{name()}" namespace="{namespace-uri()}" select="$newAttributeValue"/>
    </xsl:template>

    <xsl:template match="@* | node()">
        <xsl:copy>
            <xsl:apply-templates select="@*|node()"/>
        </xsl:copy>
    </xsl:template>

	<xsl:template match="@r:rsuiteId" priority="4" />
	
</xsl:stylesheet>
