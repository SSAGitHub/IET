<?xml version="1.0" encoding="utf-8"?>
<!-- ============================================================= -->
<!--  MODULE:    util-calculate-numbers-1-0.xsl                    -->
<!--  VERSION:   1.0                                               -->
<!--  DATE:      September 2004                                    -->
<!--                                                               -->
<!-- ============================================================= -->

<!-- ============================================================= -->
<!-- SYSTEM:     Archiving and Interchange DTD Suite               -->
<!--             Journal Article Formatting                        -->
<!--                                                               -->
<!-- PURPOSE:    A module of article-fo.xsl that contains the      -->
<!--             named templates to generate numbers               -->
<!--             for elements such as tables and figures.          -->
<!--                                                               -->
<!-- CONTAINS:   1) Named template: calculate-app-number           -->
<!--             2) Named template: calculate-table-wrap-number    -->
<!--             3) Named template: calculate-fig-number           -->
<!--             4) Named template: calculate-disp-formula-number  -->
<!--             5) Named template: calculate-chem-struct-number   -->
<!--             6) Named template: calculate-ref-number           -->
<!--             7) Named template: calculate-table-fn-number      -->
<!--             Note: List-item numbering is located in the       -->
<!--             module for lists. True footnote numbering is      -->
<!--             located in the module for footnotes.              -->
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
                xmlns:mml="http://www.w3.org/1998/Math/MathML"
                xmlns:m="http://dtd.nlm.nih.gov/xsl/util"
                extension-element-prefixes="m">
                

<!-- ============================================================= -->
<!-- NAMED TEMPLATES THAT CALCULATE OBJECT NUMBERING               -->
<!-- ============================================================= -->


<!-- These named templates are used both for xrefs to numbered
     objects AND for the objects themselves, so that the *same* 
     calculation is used for both object and xref to it. -->


<!-- ============================================================= -->
<!-- NAMED TEMPLATE: CALCULATE-APP-NUMBER                          -->
<!-- ============================================================= -->

<!-- Simplest logic: number'em all -->
<xsl:template name="calculate-app-number">

  <xsl:param name="object-id"/>
  
  <xsl:for-each select="key('element-by-id', $object-id)">
     <xsl:number level="any" count="app" from="/" format="A" />
     <xsl:text> </xsl:text>
     <xsl:apply-templates select="title" mode="xref"/>
  </xsl:for-each>
       
</xsl:template>

          

<!-- ============================================================= -->
<!-- NAMED TEMPLATE: CALCULATE-TABLE-WRAP-NUMBER                   -->
<!-- ============================================================= -->

<!-- Called IFF we generate table-wrap numbers.
     If so: number all the appropriate ones. -->
<xsl:template name="calculate-table-wrap-number">

  <xsl:param name="object-gid"/>
  
  <xsl:for-each select="key('element-by-gid', $object-gid)">
    <xsl:number count="key('legit-table-wraps', 'table-wrap')" 
                from="/" 
                level="any" 
                format="1" />
  </xsl:for-each>
  
</xsl:template>


<!-- ============================================================= -->
<!-- NAMED TEMPLATE: CALCULATE-FIG-NUMBER                          -->
<!-- ============================================================= -->

<!-- Called IFF we generate figure numbers.
     Then, number all the appropriate ones. -->
     
<!-- Like calculate-table-wrap-numbers, but complicated by the
     fact that a fig may have an @fig-type, such as 'plate', 'map', 
     etc., and (if the transform's doing the numbering) each
     @fig-type gets a separate numbering sequence. -->
     
<xsl:template name="calculate-fig-number">

  <xsl:param name="object-gid"/>
  <xsl:param name="object-fig-type"/>
  
  <xsl:for-each select="key('element-by-gid', $object-gid)">
  
    <xsl:choose>
      <!-- if fig had no @fig-type, value was set to generic by fig template -->
      <xsl:when test="$object-fig-type='generic'">
        <xsl:number count="key('legit-figs-no-fig-type', 'fig')"
                    from="/"
                    level="any"
                    format="1"/>
      </xsl:when>
      <xsl:when test="$object-fig-type='plate'">
        <xsl:number count="key('legit-figs-by-fig-type', 'plate')" 
                    from="/" 
                    level="any" 
                    format="1" />
      </xsl:when>
      <!-- If it's not handled above, we don't know -what- it is,
           and can't generate a number. -->
      <xsl:otherwise/>

    </xsl:choose>
  </xsl:for-each>
  
</xsl:template>


<!-- ============================================================= -->
<!-- NAMED TEMPLATE: CALCULATE-DISP-FORMULA-NUMBER                 -->
<!-- ============================================================= -->


<!-- for disp-formula, we number
     only those which are referenced -->
     
<xsl:template name="calculate-disp-formula-number">

  <xsl:param name="object-id"/>
  
  <xsl:for-each select="key('element-by-id', $object-id)">
    <xsl:number count="disp-formula[key('element-by-rid', @id)/self::xref]"
                from="/"
                level="any"
                format="1" />
  </xsl:for-each>
      
</xsl:template>


<!-- ============================================================= -->
<!-- NAMED TEMPLATE: CALCULATE-CHEM-STRUCT-NUMBER                  -->
<!-- ============================================================= -->


<!-- for chem-struct, we number
     only those which are referenced -->
     
<xsl:template name="calculate-chem-struct-number">

  <xsl:param name="object-id"/>
  
  <xsl:for-each select="key('element-by-id', $object-id)">
    <xsl:number count="chem-struct[key('element-by-rid', @id)/self::xref]"
                from="/"
                level="any"
                format="1" />
  </xsl:for-each>
      
</xsl:template>


<!-- ============================================================= -->
<!-- NAMED TEMPLATE: CALCULATE-REF-NUMBER                          -->
<!-- ============================================================= -->


<!-- This is for numbering ref-list/ref - NOT generic xrefs! -->
<xsl:template name="calculate-ref-number">

  <xsl:param name="object-id"/>
  
  <xsl:for-each select="key('ref-by-id', $object-id)">
    <xsl:number level="any" count="ref" from="/" format="1"/>
  </xsl:for-each>
  
</xsl:template>


<!-- ============================================================= -->
<!-- NUMBERING "REGULAR" FOOTNOTES                                 -->
<!-- ============================================================= -->


<!-- "Regular" footnotes are handled by the xref which points 
     to them or (if the document doesn't use xrefs for fn) by
     the fn itself. That handling includes the numbering. -->
     

<!-- ============================================================= -->
<!-- NAMED TEMPLATE: CALCULATE-TABLE-FN-NUMBER                     -->
<!-- ============================================================= -->


<!-- Table-fn have their own, per-table-wrap, numbering sequence.  -->
     
     
<xsl:template name="calculate-table-fn-number">

  <xsl:param name="object-id"/>
  <xsl:param name="the-key"/>
  
  <xsl:choose>
    <xsl:when test="not($table-wrap-footnotes-have-xrefs)">
      <xsl:number     level="any" 
                      from="table-wrap" 
                      format="a"
                      count="fn"/>
    </xsl:when>
    <xsl:otherwise>
      <xsl:for-each select="key($the-key, $object-id)">
          <xsl:number level="any"
                      from="table-wrap"
                      format="a"
                      count="xref[@ref-type='table-fn']
                             [count(key($the-key, @rid)[1] | . ) = 1]"/>
      </xsl:for-each>
    </xsl:otherwise>
  </xsl:choose>
     
</xsl:template>




</xsl:transform>