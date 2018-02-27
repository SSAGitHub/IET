<?xml version="1.0" encoding="utf-8"?>
<!-- ============================================================= -->
<!--  MODULE:    util-variables-and-keys-1-0.xsl                   -->
<!--  VERSION:   1.0                                               -->
<!--  DATE:      September 2004                                    -->
<!--                                                               -->
<!-- ============================================================= -->

<!-- ============================================================= -->
<!-- SYSTEM:     Archiving and Interchange DTD Suite               -->
<!--             Journal Article Formatting                        -->
<!--                                                               -->
<!-- PURPOSE:    A module of article-fo.xsl that contains the      -->
<!--             variables and keys used by the formatting.        -->
<!--                                                               -->
<!-- CONTAINS:   1) Toggle for producing diagnostics pages         -->
<!--             2) Parameters for graphics location               -->
<!--             3) Typographic variables                          -->
<!--             4) Keys by id and rid                             -->
<!--             5) Lookup table for object strings                -->
<!--             6) Lookup table for fig-type strings              -->
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
<!-- TOGGLE FOR PRODUCING DIAGNOSTICS PAGES                        -->
<!-- ============================================================= -->


<!-- to produce document diagnostics after end of article, 
     set to anything but 0 -->
<xsl:variable name="produce-diagnostics" select="boolean(0)"/> 


<!-- ============================================================= -->
<!-- GRAPHICS LOCATION                                             -->
<!-- ============================================================= -->


<xsl:param name="graphics-location-path"  select="''"/>


<!-- ============================================================= -->
<!-- TYPOGRAPHIC VARIABLES AND PARAMETERS                          -->
<!-- ============================================================= -->

<!-- SA00 -->
<xsl:variable name="abstractFont"   select="'Arial'"/>
<xsl:variable name="titlefont"   select="'Arial'"/>
<xsl:variable name="textfont"    select="'Times New Roman'"/>
<xsl:variable name="textsize"    select="9"/> <!-- SA00 Body of doc -->        <!-- points -->
<xsl:variable name="textleading" select="10"/> <!-- SA00 Body of doc -->         <!-- points -->

<xsl:variable name="textboxLMarg" select="1.25"/>      <!-- inches -->
<xsl:variable name="textboxRMarg" select="1.25"/>      <!-- inches -->

<xsl:variable name="mainindent" select="0"/><!-- SA01 no indent of content-->       <!-- picas (pc) -->

                   <!-- points -->
<xsl:variable name="leading-between-body-paras"      select="'0pt'"/>
<xsl:variable name="leading-between-wrapped-paras"    select="'6pt'"/>

<xsl:variable name="leading-below-titles-small">
	<xsl:choose>
		<xsl:when test="/article/@specific-use='instruct_page'">
			<xsl:text>0pt</xsl:text>
		</xsl:when>
		<xsl:otherwise>
			<xsl:text>4pt</xsl:text>
		</xsl:otherwise>
	</xsl:choose>
</xsl:variable>
<xsl:variable name="leading-below-titles-big"         select="'6pt'"/>

<xsl:variable name="leading-around-display-blocks"   select="'12pt'"/>
<xsl:variable name="leading-around-narrative-blocks"  select="'1pt'"/>
<xsl:variable name="leading-within-narrative-block"   select="'4pt'"/>

<xsl:variable name="leading-at-apparatus"             select="'4pt'"/>
<xsl:variable name="leading-in-apparatus"             select="'2pt'"/>

<!-- one singularity: -->
<xsl:variable name="leading-around-def-items"         select="'8pt'"/>

<!-- variables for footnotes -->
<xsl:variable name="fnsize"      select="8"/>          <!-- SA00 reduce for xref points -->
<xsl:variable name="fnleading"   select="10"/>         <!-- points -->
<xsl:variable name="leading-below-fn"                 select="'4pt'"/>


<xsl:variable name="title-for-running-head">

  <!-- change context node just for clarity -->
  <xsl:for-each select="/article/front/article-meta/title-group">
  
    <xsl:choose>
      <xsl:when test="alt-title[@alt-title-type='running-head']">
        <xsl:apply-templates select="alt-title[@alt-title-type='running-head']"
                             mode="running-head"/>
      </xsl:when>
      <xsl:otherwise>
        <xsl:apply-templates select="article-title"
                             mode="running-head"/>
      </xsl:otherwise>
    </xsl:choose>
    
  </xsl:for-each>
</xsl:variable>


<!-- ============================================================= -->
<!-- KEYS FOR ID AND RID                                           -->
<!-- ============================================================= -->


<xsl:key name="element-by-id"  match="*[@id]" use="@id"/>
<xsl:key name="element-by-rid" match="*[@rid]" use="@rid"/>

<xsl:key name="element-by-gid" match="*" use="generate-id()"/>

<!-- this next one means ref-list/ref, NOT generic xrefs! -->
<xsl:key name="ref-by-id" match="ref[@id]" use="@id"/>


<!-- ============================================================= -->
<!-- LOOKUP TABLES FOR OBJECT STRINGS                              -->
<!-- ============================================================= -->


<!-- Lookup table for strings to insert on objects such as tables,
     etc., and on cross-references to them. The source value
     is the value of the xref's @ref-type. 
     
     Figs are handled in a separate map because their strings 
     depend on fig/@fig-type. -->
     
     
<xsl:variable name="label-strings"
  select="document('')/*/m:map[@id='label-strings']/item"/>
  
<m:map id="label-strings">

  <!-- will be followed by "entitled..." -->
  
  <item  source-ref-type="sec" 
        source-elem-name="sec"
        display-string="the section "
        default-string="section"/>
  
  <!-- will be followed by "entitled..." if there's a title,
       and a page number -->
       
  <item  source-ref-type="boxed-text"
        source-elem-name="boxed-text"
        display-string="Boxed Text "
        default-string="Boxed Text"/>
                              
  <item  source-ref-type="list"
        source-elem-name="list"
        display-string="the list "
        default-string="list"/>
                              
  <item  source-ref-type="NoIdea"
        source-elem-name="disp-quote"
        display-string="the quote "
        default-string="the quote "/>

   <!-- will be followed by label if any,
        and a page number -->
   <item  source-ref-type="statement"
         source-elem-name="statement"
        display-string="Statement "
        default-string="the statement "/>

  <!-- will be followed by a page number
       (doesn't use label, even if present) -->
  <item  source-ref-type="supplementary-material"
        source-elem-name="supplementary-material"
        display-string="the additional material "
        default-string="the supplementary material"/>
  
  <!-- the rest get numbers -->
  
  <!-- will be followed by an alpha sequence number -->
  <item  source-ref-type="app"
        source-elem-name="app"
        display-string="Appendix "
        default-string="the appendix "/>
  
  <!-- numbered IFF referenced -->
  <item  source-ref-type="disp-formula"
        source-elem-name="disp-formula"
        display-string="Equation "
        default-string="equation or formula"/>
                              
  <!-- numbered IFF referenced -->
  <item  source-ref-type="chem"
        source-elem-name="chem-struct"
        display-string="Chemical Structure "
        default-string="Chemical Structure"/>
                              
  <!-- followed by a nav number, default is a page number -->
  <item  source-ref-type="scheme"
        source-elem-name="NoIdea"
        display-string="the Scheme "
        default-string="Scheme"/>
                                                    
  <item  source-ref-type="table"
        source-elem-name="table-wrap"
        display-string="Table "
        default-string="the table "/>

  <!-- there are several "types" of figures,
       so we set their strings in a separate
       map. We still need an entry in this map 
       in order to choose the right behavior
       for the general @ref-type. -->
      
  <item  source-ref-type="fig"
        source-elem-name="fig"/>
                                     
</m:map>


<!-- ============================================================= -->
<!-- LOOKUP TABLES FOR FIG-TYPE STRINGS                            -->
<!-- ============================================================= -->


<xsl:variable name="fig-type-strings"
  select="document('')/*/m:map[@id='fig-type-strings']/item"/>
  
<m:map id="fig-type-strings">

  <!-- will be followed by a nav number; default is a page number -->
  <item source-fig-type="generic"
        display-string="Figure "
        default-string="the figure "/>
                              
  <item source-fig-type="plate"
        display-string="Plate "
        default-string="the plate "/>
                              
</m:map>



</xsl:transform>