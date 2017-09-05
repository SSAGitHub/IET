<?xml version="1.0" encoding="utf-8"?>
<!-- ============================================================= -->
<!--  MODULE:    util-diagnostics-1-0.xsl                          -->
<!--  VERSION:   1.0                                               -->
<!--  DATE:      September 2004                                    -->
<!--                                                               -->
<!-- ============================================================= -->

<!-- ============================================================= -->
<!-- SYSTEM:     Archiving and Interchange DTD Suite               -->
<!--             Journal Article Formatting                        -->
<!--                                                               -->
<!-- PURPOSE:    A module of article-fo.xsl that contains the      -->
<!--             templates which generate diagnostic notes after   -->
<!--             the last page of the formatted article.           -->
<!--                                                               -->
<!-- CONTAINS:   Templates for:                                    -->
<!--             1. Named template: run-diagnostics                -->
<!--             2. Named template: warn-tex-math                  -->
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
<!-- DIAGNOSTICS AID                                               -->
<!-- ============================================================= -->


<xsl:template name="run-diagnostics">

    <fo:page-sequence master-reference="seq-diagnostics"
                      initial-page-number="1">

      <xsl:call-template name="define-headers">
        <xsl:with-param name="is-diagnostics-page" select="1"/>
      </xsl:call-template>

      <fo:flow flow-name="body">
        <fo:block space-before="0pt"
                  space-after="0pt"
                  margin-top="0pt"
                  margin-bottom="0pt"
                  line-stacking-strategy="font-height"
                  line-height-shift-adjustment="disregard-shifts">
                  
          <xsl:call-template name="block-title-style-1">
            <xsl:with-param name="title-string" select="'Diagnostics'"/>
          </xsl:call-template>
          
          <!-- add calls to diagnostic templates as desired here -->
          <!-- note the diagnostic template select the content they
               want to report on, in mode="diagnostics", so that
               processing won't interfer with the main
               article formatting. -->

          <xsl:call-template name="warn-tex-math"/>

        
        </fo:block>


        <!-- an id of 0 height on last diagnostics page,
             for use in diagnostics page headers -->
        <fo:block id="last-diagnostics-page"
                  space-before="0pt"
                  space-after="0pt"
                  margin-top="0pt"
                  margin-bottom="0pt"
                  line-height="0pt"/>
      </fo:flow>

    </fo:page-sequence>
    
</xsl:template>


<!-- ============================================================= -->
<!-- NAMED TEMPLATE: WARN-TEX-MATH                                 -->
<!-- ============================================================= -->


<xsl:template name="warn-tex-math">

  <xsl:param name="space-before" select="'0pt'"/>
  <xsl:param name="space-after"  select="$leading-around-display-blocks"/>
  
  <xsl:if test="//tex-math">
  
    <fo:block space-after="{$space-after}">

      <xsl:call-template name="block-title-style-2">
        <xsl:with-param name="title-string" select="'Tex-Math'"/>
      </xsl:call-template>

      <fo:list-block space-before="{$space-before}" space-before.precedence="1"
                     space-after="{$space-after}"    space-after.precedence="1"
                     margin-left="3pc"
                     provisional-distance-between-starts="4pc"
                     provisional-label-separation="1pc">

        <xsl:apply-templates select="//tex-math" mode="diagnostics"/>

     </fo:list-block>

    </fo:block>
  
  </xsl:if>

</xsl:template>


<xsl:template match="tex-math" mode="diagnostics">

  <fo:list-item>
    <fo:list-item-label end-indent="27pc">
      <fo:block text-align="left">&#x2022;</fo:block>
    </fo:list-item-label>
    
    <fo:list-item-body start-indent="4pc">
      <fo:block>on page
        <fo:page-number-citation ref-id="{@id}"/>
      </fo:block>
    </fo:list-item-body>
  </fo:list-item>
  
</xsl:template>


</xsl:transform>
