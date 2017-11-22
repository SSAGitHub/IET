<?xml version="1.0" encoding="utf-8"?>
<!-- ============================================================= -->
<!--  MODULE:    groups-by-content-1-0.xsl                         -->
<!--  VERSION:   1.0                                               -->
<!--  DATE:      September 2004                                    -->
<!--                                                               -->
<!-- ============================================================= -->

<!-- ============================================================= -->
<!-- SYSTEM:     Archiving and Interchange DTD Suite               -->
<!--             Journal Article Formatting                        -->
<!--                                                               -->
<!-- PURPOSE:    A module of article-fo.xsl that handles           -->
<!--             grouping elements that are named for their        -->
<!--             content type, such as statement and speech.       -->
<!--                                                               -->
<!-- CONTAINS:   Templates for:                                    -->
<!--             1) disp-quote                                     -->
<!--             2) statement and speech                           -->
<!--             3) verse-group and verse-line                     -->
<!--                                                               -->
<!-- CREATED FOR:                                                  -->
<!--             Digital Archive of Journal Articles               -->
<!--             National Center for Biotechnology Information     -->
<!--                (NCBI)                                         -->
<!--             National Library of Medicine (NLM)                -->
<!--                                                               -->
<!-- ORIGINAL CREATION DATE:                                       -->
<!--             September 2004                                    -->
<!--                                                               -->
<!-- CREATED BY: Kate Hamilton (Mulberry Technologies, Inc.)       -->
<!--             Deborah Lapeyre (Mulberry Technologies, Inc.)     -->
<!--                                                               -->
<!--             Suggestions for refinements and enhancements to   -->
<!--             this stylesheet suite should be sent in email to: -->
<!--                 publishing-dtd@ncbi.nlm.nih.gov               -->
<!-- ============================================================= -->


<!-- ============================================================= -->
<!--                    VERSION/CHANGE HISTORY                     -->
<!-- ============================================================= -->
<!--
     =============================================================

No.  Reason/Occasion                       (who) vx.x (yyyymmdd)

     =============================================================
 1.  Original version                            v1.0  20040823    
                                                                   -->
                                                                                                                                  
<!-- ============================================================= -->
<!--                    XSL STYLESHEET INVOCATION                  -->
<!-- ============================================================= -->


<xsl:transform version="1.0"
                xmlns:fo="http://www.w3.org/1999/XSL/Format"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:xlink="http://www.w3.org/1999/xlink"
                xmlns:mml="http://www.w3.org/1998/Math/MathML">


<!-- ============================================================= -->
<!-- DISPLAY QUOTE                                                 -->
<!-- ============================================================= -->


<xsl:template match="disp-quote">

  <xsl:param name="space-before" select="$leading-around-narrative-blocks"/>
  <xsl:param name="space-after" select="$leading-around-narrative-blocks"/>

  <fo:block margin-left="1.5pc"
            margin-right="1.5pc"
            space-before="{$space-before}" space-before.precedence="1"
            space-after="{$space-after}"    space-after.precedence="1"
            id="{@id}">
    <xsl:apply-templates/>
  </fo:block>
</xsl:template>


<!-- title centers on disp-quote margins -->

<xsl:template match="disp-quote/title">
  <fo:block text-align="center"
            space-after="{$leading-below-titles-small}"
            space-after.precedence="force">
    <fo:wrapper font-weight="bold">
      <xsl:apply-templates/>
    </fo:wrapper>
  </fo:block>
</xsl:template>


<!-- attrib block inherits disp-quote's right-indent,
     adds to the left indent in case of turnovers -->

<xsl:template match="disp-quote/attrib">
  <fo:block space-before="{$leading-at-apparatus}"
            space-before.precedence="force"
            margin-left="9pc"
            text-align="right">
     <xsl:apply-templates/>
  </fo:block>
</xsl:template>


<!-- ============================================================= -->
<!-- STATEMENT and SPEECH                                          -->
<!-- ============================================================= -->

<xsl:template match="statement | speech">

  <xsl:param name="space-before" select="$leading-between-body-paras"/>
  <xsl:param name="space-after" select="$leading-between-body-paras"/>

  <fo:block>&#x2003;</fo:block>
  <fo:block space-before="{$space-before}" space-before.precedence="1"
            space-after="{$space-after}"    space-after.precedence="1"
            id="{@id}">
    <xsl:apply-templates/>
  </fo:block>
</xsl:template>


<!-- ============================================================= -->
<!-- VERSE-GROUP and VERSE-LINE                                    -->
<!-- ============================================================= -->

<xsl:template match="verse-group">

  <xsl:param name="space-before" select="$leading-around-narrative-blocks"/>
      
  <xsl:param name="space-after" select="$leading-around-narrative-blocks"/>
  
  <!-- indent verse-group IF it's the outermost one.
       Verse-groups can nest but we DON'T want to
       accumulate a quarter-inch indent each time. -->
       
  <xsl:param name="relative-indent">
    <xsl:choose>
      <!-- 1.5 picas = 0.25in -->
      <xsl:when test="not(ancestor::verse-group)">1.5</xsl:when>
      <xsl:otherwise>0</xsl:otherwise>
    </xsl:choose>
  </xsl:param>
 
  <fo:block space-before="{$space-before}" space-before.precedence="1"
            space-after="{$space-after}"    space-after.precedence="1"
            start-indent="{$mainindent + $relative-indent}pc"
            end-indent="{$relative-indent}pc"
            id="{@id}">
    <xsl:apply-templates/>
  </fo:block>
  
</xsl:template>

<xsl:template match="verse-line">
  <fo:block>
    <xsl:apply-templates/>
  </fo:block>
</xsl:template>

<xsl:template match="verse-group/attrib">
  <fo:block start-indent="9pc"
            text-align="right">
    <fo:wrapper font-size="9pt">
      <xsl:apply-templates/>
    </fo:wrapper>
  </fo:block>
</xsl:template>



</xsl:transform>