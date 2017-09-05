<?xml version="1.0" encoding="utf-8"?>
<!-- ============================================================= -->
<!--  MODULE:    groups-footnotes-1-0.xsl                          -->
<!--  VERSION:   1.0                                               -->
<!--  DATE:      September 2004                                    -->
<!--                                                               -->
<!-- ============================================================= -->

<!-- ============================================================= -->
<!-- SYSTEM:     Archiving and Interchange DTD Suite               -->
<!--             Journal Article Formatting                        -->
<!--                                                               -->
<!-- PURPOSE:    A module of article-fo.xsl that handles true      -->
<!--             footnotes, i.e., those which float at the bottom  -->
<!--             of the page.                                      -->
<!--             Note: table footnotes are not "true" footnotes    -->
<!--             in the xsl-fo sense; they're functionally just    -->
<!--             cross-referencing, and are handled in the module  -->
<!--             basics-referencing-elements.xsl.                  -->
<!--                                                               -->
<!-- CONTAINS:   Keys for handling figures:                        -->
<!--             1) legit-figs-no-fig-type                         -->
<!--             2) legit-figs-by-fig-type                         -->
<!--             Templates for:                                    -->
<!--             1) fig                                            -->
<!--             2) Named template: decide-fig-type                -->
<!--             3) Named template: decide-make-label-for-fig-type -->
<!--             4) Named template: determine-fig-label            -->
<!--             5) fig-group                                      -->
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
<!-- VARIABLE RECORDING FN TAGGING STYLE                           -->
<!-- ============================================================= -->

<!-- There are two possible footnote tagging styles:
     - an xref at the location where the reference is wanted, 
       and the footnote itself somewhere else
     - the footnote at the location where the reference is wanted,
       presuming the reference will be generated.
     
     This transform supports both styles. -->
     
<!-- "Regular" footnotes that become fo:footnote are handled
     quite separately from table-wrap//fn, which are only
     "pseudo"-footnotes. -->
     
<!-- xrefs for "regular" footnotes must have @ref-type='fn'.
     If the source document does not contain the @ref-type value,
     it could be inserted by pre-processing xrefs whose target is 
     a fn.
     -->
     
<!-- xrefs for table footnotes must have @ref-type='table-fn'.
     If the source document does not contain the @ref-type value,
     it could be inserted by pre-processing xrefs whose target is 
     a table-wrap//fn.
     
     Table footnotes (a) are numbered alphabetically in a sequence
     that begins anew with each table-wrap; (b) are not "real"
     fo:footnotes: we call them "pseudo-footnotes".
     -->
     
<!-- If there is an xref @ref-type='fn' inside a table-wrap,
     it is assumed this is deliberate, i.e., that the author
     wanted a "real" footnote at this point. 
     
     If xrefs are not used in the document, no such distinction
     can be made: all table-wrap//fn are presumed to be
     pseudo-footnotes to the table-wrap. -->
     
<!-- Numbering excludes xrefs and fn in front, which are handled by
     front matter templates outside the main numbering sequence. -->


<!-- ============================================================= -->
<!-- FOOTNOTE VARIABLES AND KEYS                                   -->
<!-- ============================================================= -->


<xsl:variable name="footnotes-have-xrefs"
            select="boolean(//xref[@ref-type='fn'])"/>
            
<xsl:variable name="table-wrap-footnotes-have-xrefs"
            select="boolean(//xref[@ref-type='table-fn'])"/>


<xsl:key name="fn-refs" match="xref[not(ancestor::front)]
                                   [not(ancestor::table-wrap)]
                                   [@ref-type='fn']" 
                        use="@rid"/>

<xsl:key name="table-fn-refs" 
                        match="xref[@ref-type='table-fn']" 
                        use="@rid"/>

 
<!-- ============================================================= -->
<!-- XREF TO (NON-TABLE) FN: -ALSO- PROCESSES THE TARGET FN        -->
<!-- ============================================================= -->

<!-- Calculates the reference number AND processes the target fn
     (which sets the reference at the location of the xref
     and processes the target fn into the footnote body.) -->
     
<!-- Priority = 1 to resolve priority conflict with other xref
     templates. -->

<xsl:template match="xref[not(ancestor::front)][@ref-type='fn']"
                    priority="1">

  <xsl:param name="my-target-id" select="@rid"/>
  
  <!-- generalized the key, and used a choose,
       anticipating other xref keys -->
  <xsl:param name="the-key" select="concat(@ref-type, '-refs')"/>
  
  <xsl:param name="my-number">
    <xsl:choose>
    
      <xsl:when test="@ref-type='fn'">
        <xsl:for-each select="key($the-key, @rid)[1]">
          <xsl:number level="any"
                      count="xref[not(ancestor::front)][@ref-type='fn']
                                 [count(key($the-key, @rid)[1] | . ) = 1]"/>
        </xsl:for-each>
      </xsl:when>
      
      <xsl:otherwise>000</xsl:otherwise>
      
    </xsl:choose>
  </xsl:param>
  
  
  <xsl:param name="first-xref-to-this-target"
           select="count(key($the-key, @rid)[1] | . ) = 1"/>
  
  <!-- process the xref content, -->
  <xsl:apply-templates/>
  
  <!-- and process the xref's target -->
  <xsl:for-each select="key('element-by-id', $my-target-id)">
    <xsl:call-template name="make-footnote">
      <xsl:with-param name="my-target-id" select="$my-target-id"/>
      <xsl:with-param name="my-number" select="$my-number"/>
      <xsl:with-param name="first-xref-to-this-target" select="$first-xref-to-this-target"/>
    </xsl:call-template>
  </xsl:for-each>
  
</xsl:template>


<!-- ============================================================= -->
<!-- FOOTNOTE - WHEN NO XREFS AND NOT IN TABLE-WRAP                -->
<!-- ============================================================= -->


<!-- The fn calculates its own number. -->
<!-- Table-wrap/fn are handled separately and make "pseudo-footnotes" -->

<xsl:template match="fn">

  <xsl:choose>
    <xsl:when test="not($footnotes-have-xrefs) and not(ancestor::table-wrap)">
      <xsl:call-template name="make-footnote">
        <xsl:with-param name="my-target-id" select="@id"/>
        <xsl:with-param name="my-number">
          <xsl:number level="any" count="//fn[not(ancestor::table-wrap)][not(ancestor::front)]"/>
        </xsl:with-param>
      </xsl:call-template>
    </xsl:when>
    <xsl:otherwise/>
  </xsl:choose>
  
</xsl:template>


<!-- ============================================================= -->
<!-- NAMED TEMPLATE: MAKE-FOOTNOTE                                 -->
<!-- ============================================================= -->

<!-- context node is fn -->
<!-- fn may have been:
      - selected by xref
      - encountered in the document in its desired location
-->

<xsl:template name="make-footnote">

  <!-- captures parameter values from xref or from fn -->
       
  <xsl:param name="my-target-id"/>
  <xsl:param name="my-number"/>
  <xsl:param name="first-xref-to-this-target"/>

    <fo:footnote>
        <xsl:call-template name="set-footnote-reference">
          <xsl:with-param name="my-number" select="$my-number"/>
        </xsl:call-template>

       <!-- set the footnote body IFF:
            - fn is being processed by the first xref to encounter it, OR
            - in this document, footnotes have no xrefs -->
       <xsl:choose>
         <xsl:when test="$first-xref-to-this-target">
           <xsl:call-template name="set-footnote-body">
            <xsl:with-param name="my-target-id" select="$my-target-id"/>
            <xsl:with-param name="my-number" select="$my-number"/>
          </xsl:call-template>
        </xsl:when>
        
        <xsl:when test="(key('element-by-id', $my-target-id)/self::fn  and not($footnotes-have-xrefs))">
          <xsl:call-template name="set-footnote-body">
            <xsl:with-param name="my-target-id" select="$my-target-id"/>
            <xsl:with-param name="my-number" select="$my-number"/>
          </xsl:call-template>
        </xsl:when>
        
        <xsl:otherwise/>
      </xsl:choose>
      
      </fo:footnote>
  
</xsl:template>


<!-- ============================================================= -->
<!-- NAMED TEMPLATE: SET-FOOTNOTE-REFERENCE                        -->
<!-- ============================================================= -->


<xsl:template name="set-footnote-reference">

  <!-- receives the values from xref[@ref-type...] template -->
  <xsl:param name="my-number"/>

  <!-- usually goes inside an fo:footnote -->
    <fo:inline baseline-shift="super" 
               font-size="9pt">
      <xsl:value-of select="$my-number"/>
    </fo:inline>

</xsl:template>


<!-- ============================================================= -->
<!-- NAMED TEMPLATE: SET-FOOTNOTE-BODY                             -->
<!-- ============================================================= -->


<xsl:template name="set-footnote-body">

  <!-- receives the params from xref[@ref-type=...] template -->
  <xsl:param name="my-target-id"/>
  <xsl:param name="my-number"/>

  <!-- goes inside an fo:footnote -->
  <fo:footnote-body>
    <fo:block space-before="0pt"
              space-after="{$leading-at-apparatus}"
              space-after.precedence="1"
              start-indent="0pc"
              text-align="start">
              
      <xsl:apply-templates>
        <xsl:with-param name="my-number" select="$my-number"/>
      </xsl:apply-templates>

    </fo:block>
  </fo:footnote-body>
    
</xsl:template>


<!-- ============================================================= -->
<!-- FOOTNOTE - WHEN -IS- IN TABLE-WRAP, NO MODE                   -->
<!-- ============================================================= -->


<!-- With no xrefs @ref-type to guide us, 
     we have to handle the table fn separately -->
<!-- Where the table-wrap//fn is encountered, put out only the number.
     The table-wrap-foot will call these fn in mode=pseudo-footnote. -->
     
<xsl:template match="table-wrap//fn">

  <xsl:if test="not($table-wrap-footnotes-have-xrefs)">
    <xsl:call-template name="set-footnote-reference">
      <xsl:with-param name="my-number">
        <xsl:number level="any" 
                    count="fn" 
                    from="table-wrap" 
                    format="a"/>
      </xsl:with-param>
    </xsl:call-template>
  </xsl:if>
  
</xsl:template>


<!-- ============================================================= -->
<!-- FOOTNOTE IN TABLE-WRAP, MODE=PSEUDO-FOOTNOTE                  -->
<!-- ============================================================= -->


<!-- table-wrap-foot puts out its fn in the order encountered,
     = order of xref/@ref-type=table-fn if have xrefs
     = order of fn otherwise -->
     
<xsl:template match="table-wrap//fn" mode="pseudo-footnote">

  <xsl:param name="my-number">
    </xsl:param>
  
  <fo:block space-before="0pt"
            space-after="0"
            space-after.precedence="1"
            start-indent="0pc"
            text-align="start">
    <xsl:apply-templates>
      <xsl:with-param name="my-number" select="$my-number"/>
    </xsl:apply-templates>
  </fo:block>

</xsl:template>



</xsl:transform>