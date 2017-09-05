<?xml version="1.0" encoding="utf-8"?>
<!-- ============================================================= -->
<!--  MODULE:    util-named-title-styles-1-0.xsl                   -->
<!--  VERSION:   1.0                                               -->
<!--  DATE:      September 2004                                    -->
<!--                                                               -->
<!-- ============================================================= -->

<!-- ============================================================= -->
<!-- SYSTEM:     Archiving and Interchange DTD Suite               -->
<!--             Journal Article Formatting                        -->
<!--                                                               -->
<!-- PURPOSE:    A module of article-fo.xsl that contains the      -->
<!--             named templates for block and run-in title        -->
<!--             styles.                                           -->
<!--                                                               -->
<!-- CONTAINS:   1) Named template: block-title-style-1            -->
<!--             2) Named template: block-title-style-2            -->
<!--             3) Named template: runin-title-style-1            -->
<!--             4) Named template: runin-title-style-2            -->
<!--             5) Mode run-in: sec/title                         -->
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

<xsl:transform version="2.0" xmlns:fo="http://www.w3.org/1999/XSL/Format"
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xlink="http://www.w3.org/1999/xlink"
  xmlns:mml="http://www.w3.org/1998/Math/MathML">


  <!-- ============================================================= -->
  <!-- TITLING STYLES: BLOCK-TITLE-STYLE-1                           -->
  <!-- ============================================================= -->


  <!-- The parameter title-string is passed in only by the
     major components of the back matter or diagnostics. 
     The named title templates are mostly used by sections 
     and paragraphs which do not pass a parameter; hence 
     we test for the parameter value. -->

  <xsl:template name="block-title-style-1">

    <xsl:param name="title-string"/>

    <fo:block start-indent="0pc" line-height="11pt" space-after="{$leading-below-titles-small}"
      space-after.precedence="force" keep-with-next.within-page="always">
      <fo:wrapper font-family="Arial" font-size="10pt" font-weight="bold">
        <xsl:choose>
          <xsl:when test="label">
            <xsl:value-of select="label"/>
            <!-- SA00:label -->
          </xsl:when>
          <xsl:otherwise>
            <xsl:value-of select="preceding-sibling::label"/>
            <!-- SA00:label -->
          </xsl:otherwise>
        </xsl:choose>
        <xsl:text>&#x2003;</xsl:text>
        <xsl:choose>
          <xsl:when test="boolean(normalize-space($title-string))">
            <xsl:value-of select="$title-string"/>
          </xsl:when>
          <xsl:otherwise>
            <xsl:apply-templates/>
          </xsl:otherwise>
        </xsl:choose>
      </fo:wrapper>
    </fo:block>

  </xsl:template>


  <!-- ============================================================= -->
  <!-- TITLING STYLES: BLOCK-TITLE-STYLE-2                           -->
  <!-- ============================================================= -->


  <!-- The parameter title-string is passed in only by the
     major components of the back matter or diagnostics. 
     The named title templates are mostly used by sections 
     and paragraphs which do not pass a parameter; hence 
     we test for the parameter value. -->

  <xsl:template name="block-title-style-2">

    <xsl:param name="title-string"/>

    <fo:block start-indent="0pc" line-height="{$textleading}pt"
      space-after="{$leading-below-titles-big}" space-after.precedence="force"
      keep-with-next="always" >
      <fo:wrapper font-family="Arial" font-size="{$textsize}pt" font-style="italic">

        <xsl:value-of select="preceding-sibling::label"/>
        <!-- SA00:label -->
        <xsl:text> </xsl:text>
        <xsl:choose>
          <xsl:when test="boolean(normalize-space($title-string))">
            <xsl:value-of select="$title-string"/>
          </xsl:when>

          <xsl:otherwise>
            <xsl:apply-templates/>
          </xsl:otherwise>
        </xsl:choose>

      </fo:wrapper>
    </fo:block>

  </xsl:template>

  <xsl:template name="block-title-style-3">

    <xsl:param name="title-string"/>

    <fo:block start-indent="0pc" line-height="10pt" space-after="{$leading-below-titles-small}"
      space-after.precedence="force" keep-with-next.within-page="always">
      <fo:wrapper font-family="{$textfont}" font-size="9pt" font-weight="normal">

        <xsl:value-of select="label"/>
        <!-- SA00:label -->
        <xsl:text>&#x2003;</xsl:text>
        <xsl:choose>
          <xsl:when test="boolean(normalize-space($title-string))">
            <xsl:value-of select="$title-string"/>
          </xsl:when>
          <xsl:otherwise>
            <xsl:apply-templates/>
          </xsl:otherwise>
        </xsl:choose>

      </fo:wrapper>
    </fo:block>

  </xsl:template>

  <!-- ============================================================= -->
  <!-- TITLING STYLES: RUNIN-TITLE-STYLE-1                           -->
  <!-- ============================================================= -->


  <!-- The run-in title-style templates are called by 
     the first paragraph following a title. -->


  <xsl:template name="runin-title-style-1">

    <fo:wrapper font-family="{$titlefont}" font-style="italic">
      <xsl:value-of select="preceding-sibling::label"/>
      <xsl:text> </xsl:text>
      <xsl:apply-templates select="preceding-sibling::title" mode="run-in"/>
      <!-- em-space -->
      <xsl:text>:&#x00A0;</xsl:text>
    </fo:wrapper>

  </xsl:template>


  <!-- ============================================================= -->
  <!-- TITLING STYLES: RUNIN-TITLE-STYLE-1                           -->
  <!-- ============================================================= -->


  <xsl:template name="runin-title-style-2">

    <fo:wrapper font-family="{$textfont}" font-style="italic">
      <!-- SA00 Modify Section Title -->
      <xsl:apply-templates select="preceding-sibling::title" mode="run-in"/>
      <!-- em-space -->
      <xsl:text>:&#x00A0;</xsl:text>
    </fo:wrapper>

  </xsl:template>


  <!-- ============================================================= -->
  <!-- SEC/TITLE MODE="RUN-IN"                                       -->
  <!-- ============================================================= -->


  <xsl:template match="sec/title" mode="run-in">
    <xsl:apply-templates/>
  </xsl:template>


</xsl:transform>
