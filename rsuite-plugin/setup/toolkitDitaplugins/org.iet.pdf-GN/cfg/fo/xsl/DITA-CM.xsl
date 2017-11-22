<?xml version='1.0'?>

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:fo="http://www.w3.org/1999/XSL/Format"
    xmlns:opentopic="http://www.idiominc.com/opentopic" xmlns:exsl="http://exslt.org/common"
    xmlns:opentopic-index="http://www.idiominc.com/opentopic/index"
    xmlns:exslf="http://exslt.org/functions"
    xmlns:opentopic-func="http://www.idiominc.com/opentopic/exsl/function"
    xmlns:dita2xslfo="http://dita-ot.sourceforge.net/ns/200910/dita2xslfo"
    extension-element-prefixes="exsl"
    exclude-result-prefixes="opentopic exsl opentopic-index exslf opentopic-func dita2xslfo xs"
    version="2.0">

<xsl:template name="add_change_bar">
	<xsl:attribute name="border-right">1.5pt solid black</xsl:attribute>
</xsl:template>

	<xsl:template match="*[contains(@class, ' topic/ph ')][@outputclass = 'change_wrapper'][@status = 'new']">
		<fo:inline color="green" font-weight="bold">
			<xsl:apply-templates/>
		</fo:inline>
	</xsl:template>

	<xsl:template match="*[contains(@class, ' topic/ph ')][@outputclass = 'change_wrapper'][@status = 'deleted']">
		<fo:inline color="red" text-decoration="line-through" font-weight="bold">
			<xsl:apply-templates/>
		</fo:inline>
	</xsl:template>

	<xsl:template match="text()[ancestor::*[@status='new']]">
		<xsl:choose>
			<xsl:when test="normalize-space(.) = ''">
				<xsl:value-of select="."/>
			</xsl:when>
			<xsl:otherwise>
				<fo:inline color="green" font-weight="bold">
					<xsl:value-of select="."/>
				</fo:inline>    
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<xsl:template match="text()[ancestor::*[@status='deleted']]">
		<xsl:choose>
			<xsl:when test="normalize-space(.) = ''">
				<xsl:value-of select="."/>
			</xsl:when>
			<xsl:otherwise>
				<fo:inline color="red" font-weight="bold" text-decoration="line-through">
					<xsl:value-of select="."/>
				</fo:inline>    
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<xsl:template match="comment()"/>
		
	
</xsl:stylesheet>
