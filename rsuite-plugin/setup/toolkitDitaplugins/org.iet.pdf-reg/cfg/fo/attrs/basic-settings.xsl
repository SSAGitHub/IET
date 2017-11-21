<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
>
<!--
    This file copyright by Suite Solutions, released under the same licenses as 
    the rest of the DITA Open Toolkit project hosted on Sourceforge.net.
    See the accompanying license.txt file for applicable licenses.
    
    This file is a collection of basic settings for the FO plugin.  There are many
    more settings available in other files in the toolkit.  Please see the file
    README.txt in the main plugin directory for more information.
-->

    <!-- Values are COLLAPSED or EXPANDED. If a value is passed in from Ant, use that value. -->
    <xsl:param name="bookmarkStyle">
      <xsl:choose>
        <xsl:when test="$antArgsBookmarkStyle!=''"><xsl:value-of select="$antArgsBookmarkStyle"/></xsl:when>
        <xsl:otherwise>COLLAPSED</xsl:otherwise>
      </xsl:choose>
    </xsl:param>

    <!-- Determine how to style topics referenced by <chapter>, <part>, etc. Values are:
         MINITOC: render with a MiniToc on left, content indented on right.
         BASIC: render the same way as any topic. -->
    <xsl:param name="chapterLayout">
      <xsl:choose>
        <xsl:when test="$antArgsChapterLayout!=''"><xsl:value-of select="$antArgsChapterLayout"/></xsl:when>
        <xsl:otherwise>MINITOC</xsl:otherwise>
      </xsl:choose>
    </xsl:param>
    <xsl:param name="appendixLayout" select="$chapterLayout"/>
    <xsl:param name="appendicesLayout" select="$chapterLayout"/>
    <xsl:param name="partLayout" select="$chapterLayout"/>
    <xsl:param name="noticesLayout" select="$chapterLayout"/>

    <!-- Determine which links are included in the output.
         none:     no links are included. This is the default, to match previous settings.
         all:      all links are included. If the original parameter $disableRelatedLinks 
                   is customized to "no", this is the default, to match previous settings.
         nofamily: excludes links with @role = parent, child, next, previous, ancestor, descendant, sibling, cousin.
    -->
    <xsl:param name="includeRelatedLinks">
      <xsl:choose>
        <xsl:when test="$antArgsIncludeRelatedLinks!=''"><xsl:value-of select="$antArgsIncludeRelatedLinks"/></xsl:when>
        <xsl:when test="$disableRelatedLinks='no'">all</xsl:when>
        <xsl:otherwise>none</xsl:otherwise>
      </xsl:choose>
    </xsl:param>
    <!-- list of supported link roles -->
    <xsl:param name="includeRelatedLinkRoles" select="concat(' ', normalize-space($include.rellinks), ' ')"/>

    <!-- The default of 215.9mm x 279.4mm is US Letter size (8.5x11in) -->
    <!-- SA00 page height width -->
    <xsl:variable name="page-width">210mm</xsl:variable>
    <xsl:variable name="page-height">297mm</xsl:variable>

    <!-- This is the default, but you can set the margins individually below. -->
    <xsl:variable name="page-margins">20mm</xsl:variable>
    
    <!-- Change these if your page has different margins on different sides. -->
    <!-- SA00 left right page margins -->
    <xsl:variable name="page-margin-inside" select="'23mm'"/>
    <xsl:variable name="page-margin-outside" select="'23mm'"/>
    <xsl:variable name="page-margin-top" select="$page-margins"/>
    <xsl:variable name="page-margin-bottom" select="$page-margins"/>

    <!--The side column width is the amount the body text is indented relative to the margin. -->
    <!-- SA00 No margin -->
    <xsl:variable name="side-col-width">0pt</xsl:variable>

    <xsl:variable name="mirror-page-margins" select="false()"/>

    <xsl:variable name="default-font-size">10pt</xsl:variable>
    <xsl:variable name="default-line-height">12pt</xsl:variable>
</xsl:stylesheet>
