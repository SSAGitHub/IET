<?xml version="1.0" encoding="utf-8"?>
<!-- ============================================================= -->
<!--  MODULE:    basics-display-objects-1-0.xsl                    -->
<!--  VERSION:   1.0                                               -->
<!--  DATE:      September 2004                                    -->
<!--                                                               -->
<!-- ============================================================= -->

<!-- ============================================================= -->
<!-- SYSTEM:     Archiving and Interchange DTD Suite               -->
<!--             Journal Article Formatting                        -->
<!--                                                               -->
<!-- PURPOSE:    A module of article-fo.xsl that handles           -->
<!--             display objects such as graphic and chem-struct,  -->
<!--             which are usually grouped with titling data       -->
<!--             inside an element such as fig or boxed-text.      -->
<!--                                                               -->
<!-- CONTAINS:   Templates for:                                    -->
<!--             1) graphic and media                              -->
<!--             2) disp-formula                                   -->
<!--             3) chem-struct                                    -->
<!--             4) array                                          -->
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


<xsl:transform version="1.0" xmlns:fo="http://www.w3.org/1999/XSL/Format"
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xlink="http://www.w3.org/1999/xlink"
  xmlns:mml="http://www.w3.org/1998/Math/MathML"
  xmlns:AHtree="http://www.antennahouse.com/names/XSL/AreaTree">


  <!-- ============================================================= -->
  <!-- DISPLAY OBJECTS                                               -->
  <!-- ============================================================= -->


  <!-- ============================================================= -->
  <!-- TEX-MATH                                                  -->
  <!-- ============================================================= -->


  <xsl:template match="tex-math">
    <!-- SA00 suppress TeX -->
    <!--fo:inline id="{@id}">
    <xsl:text>[TeX equation: not rendered by this stylesheet.] </xsl:text>
    <xsl:value-of select="."/>
  </fo:inline-->
  </xsl:template>


  <!-- ============================================================= -->
  <!-- GRAPHIC and MEDIA                                             -->
  <!-- ============================================================= -->


  <!-- Assumes graphic fits in the page width and height;
     doesn't specify size or scale. -->

  <!-- A graphic may float if it is NOT inside a container that
     might float. The contexts in which graphic may NOT float
     are listed. -->


  <xsl:template match="graphic | media">

    <xsl:param name="float">
      <xsl:choose>
        <xsl:when
          test="self::graphic and @position='float' 
                 and not(ancestor::boxed-text
                         | ancestor::chem-struct-wrapper
                         | ancestor::disp-formula
                         | ancestor::fig
                         | ancestor::aupplementary-material
                         | table-wrap)">
          <xsl:text>before</xsl:text>
        </xsl:when>
        <xsl:otherwise>
          <xsl:text>none</xsl:text>
        </xsl:otherwise>
      </xsl:choose>
    </xsl:param>

    <xsl:variable name="the-external-file">
      <xsl:text>url("</xsl:text>
      <xsl:value-of select="$graphics-location-path"/>
      <xsl:value-of select="@xlink:href"/>
      <!-- required -->
      <xsl:text>")</xsl:text>
    </xsl:variable>

    <!-- if it's inside something that's already floating,
       don't let this float inside that float! -->
    <xsl:variable name="currID">
      <xsl:choose>
        <xsl:when test="ancestor::fig">
          <xsl:value-of select="ancestor::fig[1]/@id"/>
        </xsl:when>
        <xsl:otherwise>
          <xsl:value-of select="ancestor::disp-formula[1]/@id"/>
        </xsl:otherwise>
      </xsl:choose>
    </xsl:variable>
    <xsl:choose>
      <xsl:when test="ancestor::*[@position='float']">
        <fo:external-graphic id="{ancestor::fig[1]/@id}" src="{$the-external-file}">
          <xsl:variable name="curID" select="ancestor::fig/@id"/>
          <xsl:variable name="figWidth" select="substring-before($areaTree//AHtree:GraphicViewportArea[@id=$curID]/@width, 'p')"/>
           <xsl:if test="number($figWidth) > 510.2">
            <xsl:attribute name="max-width">100%</xsl:attribute>  
            <xsl:attribute name="content-width">scale-down-to-fit</xsl:attribute>  
            <xsl:attribute name="content-height">scale-down-to-fit</xsl:attribute>  
          </xsl:if>
        </fo:external-graphic>
      </xsl:when>
      <xsl:otherwise>
        <!--fo:float float="{$float}"-->
        <!-- SA00 float not supported -->
        <fo:external-graphic id="{$currID}" src="{$the-external-file}"/>
        <!--/fo:float-->
      </xsl:otherwise>
    </xsl:choose>

  </xsl:template>


  <!-- ============================================================= -->
  <!-- DISP-FORMULA                                                  -->
  <!-- ============================================================= -->


  <!-- The contents of a disp-formula are not unique to disp-formula:
     they're things like text with emphases, math, graphic, etc.,
     all of which are handled by their respective templates. -->

  <!-- disp-formula is numbered only if referenced. -->

  <xsl:template match="disp-formula">

    <xsl:param name="object-id" select="@id"/>

    <fo:block id="{@id}" line-stacking-strategy="max-height">

      <xsl:if test="key('element-by-rid', $object-id)[self::xref]">
        <xsl:call-template name="calculate-disp-formula-number">
          <xsl:with-param name="object-id" select="$object-id"/>
        </xsl:call-template>
      </xsl:if>

      <xsl:apply-templates/>

    </fo:block>

  </xsl:template>


  <!-- ============================================================= -->
  <!-- CHEM-STRUCT                                                   -->
  <!-- ============================================================= -->


  <!-- chem-struct is contained within chem-struct-wrapper and many
     other contexts. -->

  <!-- The contents of a chem-struct are not unique to chem-struct:
     they're things like text with emphases, math, graphic, etc.,
     all of which are handled by their respective templates. -->

  <!-- Note: although chem-struct has an @id, the legitimate
     target for cross-referencing is the chem-struct-wrapper. -->


  <xsl:template match="chem-struct">

    <fo:block id="{@id}">
      <xsl:apply-templates/>
    </fo:block>

  </xsl:template>


  <!-- ============================================================= -->
  <!-- ARRAY                                                         -->
  <!-- ============================================================= -->


  <!-- Arrays do not have @id, label, title, or caption.
     They do not float.
     They are set in the regular content area, 
     NOT out at the textboxLMarg.
     
     An array can contain media, graphic, or tbody,
     and optionally a copyright-statement.
     The behavior for each of these parts is specified
     in its proper place, so all we have to do here is
     manage the array's space-above and space-below. -->


  <xsl:template match="array">

    <xsl:param name="space-before" select="$leading-around-narrative-blocks"/>
    <xsl:param name="space-after" select="$leading-around-narrative-blocks"/>

    <fo:block space-before="{$space-before}" space-before.precedence="1"
      space-after="{$space-after}" space-after.precedence="1">
      <xsl:apply-templates/>
    </fo:block>

  </xsl:template>


</xsl:transform>
