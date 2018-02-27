<?xml version="1.0" encoding="utf-8"?>

<!-- =============================================================== -->
<!--                                                                 -->
<!-- xhtml-tables-3-0.xsl                                            -->
<!--                                                                 -->
<!-- Process XHTML style tables as used in the NCBI/NLM              -->
<!-- Journal Publishing Tag Set Tag Library, version 3.0.            -->
<!--                                                                 -->
<!-- Creation Date: 12 December 2008                                 -->
<!--                                                                 -->
<!-- =============================================================== -->


<!-- This module is derived from the xhtml2fo.xsl stylesheet
     found on the Antenna House web site.  The copyright and
     conditions of use information included with with that
     style sheet is reproduced below, and applies to this
     module/file as well.

     Note that notwithstanding the compatibility note in the
     following notice, there are no Antenna House specific
     features used in this module.

     The following changes have been made to the stylesheet:
     1) Only the matching templates that process the "table"
        element and its related children have been kept.
     2) Only the table processing and general attribute
        processing named templates have been kept.
     3) The "html:" namespace prefix and its binding have been
        removed from the code in all cases.
     4) In the named template "process-table", the code that
        determines the value to be used for the
        fo:table/@border-collapse attribute has been changed
        to select a value of "collapse" (rather than "separate"),
        if any child's @style attribute selects a border
        specification property.
  -->

<!--

Copyright Antenna House, Inc. (http://www.antennahouse.com) 2001, 2002.

Since this stylesheet is originally developed by Antenna House to be
used with XSL Formatter, it may not be compatible with another XSL-FO
processors.

Permission is hereby granted, free of charge, to any person obtaining a
copy of this software and associated documentation files (the
"Software"), to deal in the Software without restriction, including
without limitation the rights to use, copy, modify, merge, publish,
distribute, and/or sell copies of the Software, and to permit persons
to whom the Software is furnished to do so, provided that the above
copyright notice(s) and this permission notice appear in all copies of
the Software and that both the above copyright notice(s) and this
permission notice appear in supporting documentation.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS
OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT
OF THIRD PARTY RIGHTS. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR HOLDERS
INCLUDED IN THIS NOTICE BE LIABLE FOR ANY CLAIM, OR ANY SPECIAL INDIRECT
OR CONSEQUENTIAL DAMAGES, OR ANY DAMAGES WHATSOEVER RESULTING FROM LOSS
OF USE, DATA OR PROFITS, WHETHER IN AN ACTION OF CONTRACT, NEGLIGENCE OR
OTHER TORTIOUS ACTION, ARISING OUT OF OR IN CONNECTION WITH THE USE OR
PERFORMANCE OF THIS SOFTWARE.

-->

<!-- Contents of this module:

     1. Table matching templates
     2. Table-specific named templates
     3. Table-specific attribute-sets
     4. General XHTML attribute processing named templates

  -->

<xsl:transform version="2.0" xmlns:fo="http://www.w3.org/1999/XSL/Format"
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
  xmlns:axf="http://www.antennahouse.com/names/XSL/Extensions" 
  xmlns:AHtree="http://www.antennahouse.com/names/XSL/AreaTree"
  xmlns:local="http://www.localFuntion">
  
  <xsl:key name="legit-table-wraps"
    match="body/table-wrap
    | sec/table-wrap
    | app/table-wrap
    | table-wrap-group/table-wrap"
    use="local-name()"/>
  
  <xsl:template name="decide-make-table-wrap-labels">
    
    <xsl:param name="object-gid"/>
    
    <xsl:choose>
      
      <!-- if ANY legit table-wrap has a label,
         DON'T generate numbers for table-wraps -->
      <xsl:when test="count(key('legit-table-wraps', 'table-wrap')[child::label]) != 0">
        <xsl:value-of select="'no'"/>
      </xsl:when>
      
      <!-- otherwise, in this document no legit table-wrap has a label, 
         so we'll set the value to true, and when the time comes 
         we'll use that to put out a string AND number -->
      <xsl:otherwise>
        <xsl:value-of select="'yes'"/>
      </xsl:otherwise>
      
    </xsl:choose>
    
  </xsl:template>
  
  <xsl:template name="determine-table-wrap-label">
    
    <!-- from table-wrap or xref template -->
    <xsl:param name="object-gid"/>
    
    <xsl:choose>
      
      <!-- if there's a label with a space in it, assume the
				 label contains both string and number -->
      <xsl:when test="contains(label, ' ')">
        <xsl:apply-templates select="label" mode="display"/>
      </xsl:when>
      
      <!-- if there's a label (with no space in it), 
				 generate a string and use the label -->
      <xsl:when test="label">
        <xsl:value-of select="$label-strings[@source-elem-name='table-wrap']/@display-string"/>
        <xsl:apply-templates select="label" mode="display"/>
      </xsl:when>
      
      <!-- otherwise, generate string AND number -->
      <xsl:otherwise>
        <xsl:value-of select="$label-strings[@source-elem-name='table-wrap']/@display-string"/>
        <xsl:call-template name="calculate-table-wrap-number">
          <xsl:with-param name="object-gid" select="$object-gid"/>
        </xsl:call-template>
      </xsl:otherwise>
      
    </xsl:choose>
    
  </xsl:template>
  
  <xsl:template name="process-style">
    <xsl:param name="style"/>
    <!-- e.g., style="text-align: center; color: red"
  converted to text-align="center" color="red" -->
    <xsl:variable name="okay-properties"
      select="' color; background-color; font-size; font-weight;
      font-style; font-family; text-decoration; text-align'"/>
    <xsl:variable name="name"
      select="normalize-space(substring-before($style, ':'))"/>
    <xsl:if test="$name">
      <xsl:variable name="value-and-rest"
        select="normalize-space(substring-after($style, ':'))"/>
      <xsl:variable name="value">
        <xsl:choose>
          <xsl:when test="contains($value-and-rest, ';')">
            <xsl:value-of
              select="normalize-space(substring-before(
              $value-and-rest, ';'))"/>
          </xsl:when>
          <xsl:otherwise>
            <xsl:value-of select="$value-and-rest"/>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:variable>
      <xsl:choose>
        <xsl:when test="$name = 'width' and (self::col or self::colgroup)">
          <xsl:attribute name="column-width">
            <xsl:value-of select="$value"/>
          </xsl:attribute>
        </xsl:when>
        <xsl:when
          test="$name = 'vertical-align' and (
          self::table or self::caption or
          self::thead or self::tfoot or
          self::tbody or self::colgroup or
          self::col or self::tr or
          self::th or self::td)">
          <xsl:choose>
            <xsl:when test="$value = 'top'">
              <xsl:attribute name="display-align">before</xsl:attribute>
            </xsl:when>
            <xsl:when test="$value = 'bottom'">
              <xsl:attribute name="display-align">after</xsl:attribute>
            </xsl:when>
            <xsl:when test="$value = 'middle'">
              <xsl:attribute name="display-align">center</xsl:attribute>
            </xsl:when>
            <xsl:otherwise>
              <xsl:attribute name="display-align">auto</xsl:attribute>
              <xsl:attribute name="relative-align">baseline</xsl:attribute>
            </xsl:otherwise>
          </xsl:choose>
        </xsl:when>
        <xsl:otherwise>
          <xsl:if test="contains($okay-properties,concat(' ',$name,';'))">
            <xsl:attribute name="{$name}">
              <xsl:value-of select="$value"/>
            </xsl:attribute>
          </xsl:if>
        </xsl:otherwise>
      </xsl:choose>
    </xsl:if>
    <xsl:variable name="rest"
      select="normalize-space(substring-after($style, ';'))"/>
    <xsl:if test="$rest">
      <xsl:call-template name="process-style">
        <xsl:with-param name="style" select="$rest"/>
      </xsl:call-template>
    </xsl:if>
  </xsl:template>

<!-- ************************************************* -->
  <xsl:template match="table-wrap">
    
    <xsl:param name="my-gid" select="generate-id()"/>
    
    <xsl:param name="space-before" select="$leading-around-narrative-blocks"/>
    <xsl:param name="space-after" select="$leading-around-narrative-blocks"/>
    
    <xsl:param name="make-table-wrap-labels">
      <!-- this logic is externalized because it is
         also used by xref -->
      <xsl:call-template name="decide-make-table-wrap-labels">
        <xsl:with-param name="object-gid" select="$my-gid"/>
      </xsl:call-template>
    </xsl:param>
    
    <xsl:variable name="rotation">
      <xsl:choose>
        <xsl:when test="@orientation='landscape'">
          <xsl:text>90</xsl:text>
        </xsl:when>
        <xsl:otherwise>
          <text>0</text>
       </xsl:otherwise>
      </xsl:choose>
    </xsl:variable>

    <xsl:variable name="float-reference">
      <xsl:choose>
        <xsl:when test="contains(@content-type, 'multicol')">
          <xsl:text>multicol</xsl:text>
        </xsl:when>
        <xsl:when test="contains(@content-type, 'column')">
          <xsl:text>column</xsl:text>
        </xsl:when>
      <xsl:when test="local:tableWidth(@id) > 244">
          <xsl:text>multicol</xsl:text>
        </xsl:when>
        <xsl:otherwise>
          <xsl:text>multicol</xsl:text>
        </xsl:otherwise>
      </xsl:choose>
    </xsl:variable>
    
    <xsl:variable name="float-move">
      <xsl:choose>
        <xsl:when test="contains(@content-type, 'keep')">
          <xsl:text>keep</xsl:text>
        </xsl:when>
        <xsl:otherwise>
          <xsl:text>auto-next</xsl:text>
        </xsl:otherwise>
      </xsl:choose>
    </xsl:variable>
    
    <xsl:variable name="curID" select="@id"/>
    <xsl:variable name="articleID" select="translate(/article/front/article-meta/article-id[@pub-id-type='doi'], './-', '')"/>
    <xsl:variable name="endbodyId" select="concat('endbodyarticle_', $articleID)"/>
    
    <xsl:variable name="float-y">
      <xsl:choose>
        <xsl:when test="contains(@content-type, 'top')">
          <xsl:text>top</xsl:text>
        </xsl:when>
        <xsl:when test="$areaTree//AHtree:BlockArea[@id=$curID]/ancestor::AHtree:PageViewportArea//AHtree:BlockArea[@id  = $endbodyId]">
          <xsl:text>top</xsl:text>
        </xsl:when>
        <xsl:when test="ancestor::app">
          <xsl:text>top</xsl:text>
        </xsl:when>
        <xsl:otherwise>
          <xsl:text>bottom</xsl:text>
        </xsl:otherwise>
      </xsl:choose>
    </xsl:variable>
    
    <xsl:variable name="float-x">
      <xsl:choose>
        <xsl:when test="contains(@content-type, 'right')">
          <xsl:text>end</xsl:text>
        </xsl:when>
         <xsl:otherwise>
          <xsl:text>start</xsl:text>
        </xsl:otherwise>
      </xsl:choose>
    </xsl:variable>
    
    <fo:float axf:float-reference="multicol" axf:float-x="{$float-x}" axf:float-y="{$float-y}"
      axf:float-margin-y="10pt">

      <xsl:if test="contains(@content-type, 'next')">
        <xsl:attribute name="axf:float-move">next</xsl:attribute>
      </xsl:if>
    <fo:block-container>
        <xsl:attribute name="break-before">
          
          <xsl:choose> 
            
            <xsl:when test="$rotation='90'">
              <xsl:text>page</xsl:text>
            </xsl:when>
            </xsl:choose>
          
          </xsl:attribute>
        
        <xsl:attribute name="reference-orientation">
          <xsl:value-of select="$rotation"/>
        </xsl:attribute>
        
        <xsl:attribute name="width">
          <xsl:choose>
          <xsl:when test="contains(@content-type,'multicol')">
            <xsl:text>3gr</xsl:text>
          </xsl:when>
            <xsl:when test="local:tableWidth(@id) > 244">
            <xsl:text>3gr</xsl:text>
          </xsl:when>
            <xsl:otherwise>1gr</xsl:otherwise>
          </xsl:choose>
        </xsl:attribute>
        
        <fo:table-and-caption>
          <!-- SA00 no support for table-and-caption -->

          <fo:table-caption>

            <fo:block id="{@id}" text-align="start" space-before="{$space-before}"
              space-before.precedence="1" space-after="{$space-after}" space-after.precedence="1"
              line-height="10pt" text-indent="0pt">

              <fo:wrapper font-family="Arial" font-size="{$textsize}pt" font-weight="bold">
                <xsl:value-of select="label"/>
              </fo:wrapper>
              <xsl:text>&#x2003;</xsl:text>
              <fo:wrapper font-family="Arial" font-size="{$textsize}pt" font-weight="normal">

                <!--xsl:if test="key('legit-table-wraps', 'table-wrap')[generate-id()=$my-gid]
                      and ($make-table-wrap-labels='yes' or child::label)">  
                <xsl:call-template name="determine-table-wrap-label">
                  <xsl:with-param name="object-gid" select="$my-gid"/>
                </xsl:call-template>
                <xsl:text>. </xsl:text>
            </xsl:if-->

                <xsl:apply-templates select="caption/p"/>
              </fo:wrapper>
            </fo:block>

            <!-- a block 9/10R for the caption paragraphs if any -->

            <!--fo:block>
          <fo:wrapper font-weight="normal">
            <xsl:apply-templates select="caption"/>
            <xsl:apply-templates select="copyright-statement" mode="display"/>
          </fo:wrapper>
        </fo:block-->

          </fo:table-caption>

          <!-- now at last the table or other table-wrap content -->

          <xsl:apply-templates
            select="*[not(self::label)
          and not(self::caption)
          and not(self::table-wrap-foot)
          and not(self::copyright-statement)]"/>

        </fo:table-and-caption>
        
      <!-- Finally: table-wrap-foot comes AFTER table is done.
         If present it probably contains table footnotes. -->
      <xsl:if test=".//fn-group/fn">
        <fo:block font-size="7pt" text-indent="0pt">
        <xsl:apply-templates select=".//fn-group/fn" mode="pseudo-footnote"/>
      </fo:block>
      </xsl:if>
    </fo:block-container>
      <fo:block font-size="8pt">
        <xsl:text>&#160;</xsl:text>
      </fo:block>
      </fo:float>
    <!-- no fop support for float -->
    
  </xsl:template>
  
  
  <!--=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-
       Table matching templates
  =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-->

  <xsl:template match="tablezz">
    <fo:table-and-caption xsl:use-attribute-sets="table-and-caption">
      <xsl:call-template name="make-table-caption"/>
      <fo:table xsl:use-attribute-sets="table">
        <xsl:call-template name="process-table"/>
      </fo:table>
    </fo:table-and-caption>
  </xsl:template>
  
  <xsl:template match="table">
    <fo:table id="{parent::table-wrap/@id}" start-indent="0pc" border-collapse="collapse"
      border-separation.inline-progression-direction="6pt"
      border-separation.block-progression-direction="2pt" border-top="1pt solid black"
      border-bottom="1pt solid black" width="100%">
      <xsl:call-template name="process-table"/>
    </fo:table>
  </xsl:template>
  

  <xsl:template match="thead">
    <fo:table-header border-bottom="0.5pt solid black">
      <xsl:apply-templates/>
    </fo:table-header>
  </xsl:template>
  
  <xsl:template match="tfoot">
    <fo:table-footer xsl:use-attribute-sets="tfoot">
      <xsl:call-template name="process-table-rowgroup"/>
    </fo:table-footer>
  </xsl:template>

  <xsl:template match="tbody">
    <fo:table-body xsl:use-attribute-sets="tbody">
      <xsl:call-template name="process-table-rowgroup"/>
    </fo:table-body>
  </xsl:template>

  <xsl:template match="colgroup">
    <fo:table-column xsl:use-attribute-sets="table-column">
      <xsl:call-template name="process-table-column"/>
    </fo:table-column>
  </xsl:template>

  <xsl:template match="colgroup[col]">
    <xsl:apply-templates/>
  </xsl:template>

  <xsl:template match="col[@align='char']" priority="10">
    <fo:table-column axf:text-align-string="center" xsl:use-attribute-sets="table-column">
      <xsl:call-template name="process-table-column"/>
    </fo:table-column>
  </xsl:template>

  <xsl:template match="col">
    <fo:table-column xsl:use-attribute-sets="table-column">
      <xsl:call-template name="process-table-column"/>
    </fo:table-column>
  </xsl:template>

  <xsl:template match="tr">
    <fo:table-row xsl:use-attribute-sets="tr">
      <xsl:call-template name="process-table-row"/>
    </fo:table-row>
  </xsl:template>

  <xsl:template match="tr[parent::table and th and not(td)]">
    <fo:table-row xsl:use-attribute-sets="tr" keep-with-next="always">
      <xsl:call-template name="process-table-row"/>
    </fo:table-row>
  </xsl:template>

  <xsl:template match="th">
    <fo:table-cell xsl:use-attribute-sets="th">
      <xsl:attribute name="font-family" select="'Arial'"/>
      <xsl:attribute name="font-size" select="'9pt'"/>
      <xsl:attribute name="font-weight" select="'normal'"/>
      <xsl:attribute name="text-indent" select="'0pt'"/>
      <xsl:if test="not(@align)">
        <xsl:attribute name="axf:text-align-string">from-table-column()</xsl:attribute>
      </xsl:if>
      <xsl:call-template name="process-table-cell"/>
    </fo:table-cell>
  </xsl:template>

  <xsl:template match="td">
    <fo:table-cell xsl:use-attribute-sets="td">
      <xsl:attribute name="font-family" select="'Arial'"/>
      <xsl:attribute name="font-size" select="'8pt'"/>
      <xsl:attribute name="text-indent" select="'0pt'"/>
      <xsl:if test="not(@align)">
        <xsl:attribute name="axf:text-align-string">from-table-column()</xsl:attribute>
      </xsl:if>
      <xsl:call-template name="process-table-cell"/>
    </fo:table-cell>
  </xsl:template>

  <!--=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-
       Table-specific named templates
  =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-->

  <xsl:template name="make-table-caption">
    <xsl:if test="caption/@align">
      <xsl:attribute name="caption-side">
        <xsl:value-of select="caption/@align"/>
      </xsl:attribute>
    </xsl:if>
    <xsl:apply-templates select="caption"/>
  </xsl:template>

  <xsl:template name="process-table">
    <xsl:if test="@width">
      <xsl:attribute name="inline-progression-dimension">
        <xsl:choose>
          <xsl:when test="contains(@width, '%')">
            <xsl:value-of select="@width"/>
          </xsl:when>
          <xsl:otherwise>
            <xsl:value-of select="@width"/>px</xsl:otherwise>
        </xsl:choose>
      </xsl:attribute>
    </xsl:if>
    <xsl:if test="@border or @frame">
      <xsl:choose>
        <xsl:when test="@border &gt; 0">
          <xsl:attribute name="border">
            <xsl:value-of select="@border"/>px</xsl:attribute>
        </xsl:when>
      </xsl:choose>
      <xsl:choose>
        <xsl:when test="@border = '0' or @frame = 'void'">
          <xsl:attribute name="border-style">hidden</xsl:attribute>
        </xsl:when>
        <xsl:when test="@frame = 'above'">
          <xsl:attribute name="border-style">outset hidden hidden hidden</xsl:attribute>
        </xsl:when>
        <xsl:when test="@frame = 'below'">
          <xsl:attribute name="border-style">hidden hidden outset hidden</xsl:attribute>
        </xsl:when>
        <xsl:when test="@frame = 'hsides'">
          <xsl:attribute name="border-style">outset hidden</xsl:attribute>
        </xsl:when>
        <xsl:when test="@frame = 'vsides'">
          <xsl:attribute name="border-left">0.5pt solid black</xsl:attribute>
          <xsl:attribute name="border-right">0.5pt solid black</xsl:attribute>
        </xsl:when>
        <xsl:when test="@frame = 'lhs'">
          <xsl:attribute name="border-style">hidden hidden hidden outset</xsl:attribute>
        </xsl:when>
        <xsl:when test="@frame = 'rhs'">
          <xsl:attribute name="border-style">hidden outset hidden hidden</xsl:attribute>
        </xsl:when>
        <xsl:otherwise>
          <xsl:attribute name="border-style">outset</xsl:attribute>
        </xsl:otherwise>
      </xsl:choose>
    </xsl:if>
    <xsl:if test="@rules and (@rules = 'groups' or
                      @rules = 'rows' or
                      @rules = 'cols' or
                      @rules = 'all' and (not(@border or @frame) or
                          @border = '0' or @frame and
                          not(@frame = 'box' or @frame = 'border')))">
      <xsl:attribute name="border-collapse">collapse</xsl:attribute>
      <xsl:if test="not(@border or @frame)">
        <xsl:attribute name="border-style">hidden</xsl:attribute>
      </xsl:if>
    </xsl:if>
    <xsl:choose>
      <xsl:when test="not(@rules) and .//*[contains(@style,'border')]">
	<xsl:attribute name="border-collapse">collapse</xsl:attribute>
	<xsl:if test="@cellspacing">
	  <xsl:attribute name="border-spacing">
            <xsl:value-of select="@cellspacing"/>px</xsl:attribute>
	</xsl:if>
      </xsl:when>
      <xsl:when test="@cellspacing">
	<xsl:attribute name="border-spacing">
          <xsl:value-of select="@cellspacing"/>px</xsl:attribute>
	<xsl:attribute name="border-collapse">separate</xsl:attribute>
      </xsl:when>
    </xsl:choose>
    <xsl:call-template name="process-common-attributes"/>
    <xsl:apply-templates select="col | colgroup"/>
    <xsl:apply-templates select="thead"/>
    <xsl:apply-templates select="tfoot"/>
    <xsl:choose>
      <xsl:when test="tbody">
        <xsl:apply-templates select="tbody"/>
      </xsl:when>
      <xsl:otherwise>
        <fo:table-body xsl:use-attribute-sets="tbody">
          <xsl:apply-templates select="tr"/>
        </fo:table-body>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>

  <xsl:template name="process-table-rowgroup">
    <xsl:if test="ancestor::table[1]/@rules = 'groups'">
      <xsl:attribute name="border">1px solid</xsl:attribute>
    </xsl:if>
    <xsl:call-template name="process-common-attributes-and-children"/>
  </xsl:template>

  <xsl:template name="process-table-column">
    <xsl:if test="parent::colgroup">
      <xsl:call-template name="process-col-width">
        <xsl:with-param name="width" select="../@width"/>
      </xsl:call-template>
      <xsl:call-template name="process-cell-align">
        <xsl:with-param name="align" select="../@align"/>
      </xsl:call-template>
      <xsl:call-template name="process-cell-valign">
        <xsl:with-param name="valign" select="../@valign"/>
      </xsl:call-template>
    </xsl:if>
    <xsl:if test="@span">
      <xsl:attribute name="number-columns-repeated">
        <xsl:value-of select="@span"/>
      </xsl:attribute>
    </xsl:if>
    <xsl:call-template name="process-col-width">
      <xsl:with-param name="width" select="@width"/>
      <!-- it may override parent colgroup's width -->
    </xsl:call-template>
    <xsl:if test="ancestor::table[1]/@rules = 'cols'">
      <xsl:attribute name="border">1px solid</xsl:attribute>
    </xsl:if>
    <xsl:call-template name="process-common-attributes"/>
    <!-- this processes also align and valign -->
  </xsl:template>

  <xsl:template name="process-table-row">
    <xsl:if test="ancestor::table[1]/@rules = 'rows'">
      <xsl:attribute name="border">1px solid</xsl:attribute>
    </xsl:if>
    <xsl:call-template name="process-common-attributes-and-children"/>
  </xsl:template>

  <xsl:template name="process-table-cell">
    <xsl:if test="@colspan">
      <xsl:attribute name="number-columns-spanned">
        <xsl:value-of select="@colspan"/>
      </xsl:attribute>
    </xsl:if>
    <xsl:if test="@rowspan">
      <xsl:attribute name="number-rows-spanned">
        <xsl:value-of select="@rowspan"/>
      </xsl:attribute>
    </xsl:if>
    <xsl:for-each select="ancestor::table[1]">
      <xsl:if test="(@border or @rules) and (@rules = 'all' or
                    not(@rules) and not(@border = '0'))">
        <xsl:attribute name="border-style">inset</xsl:attribute>
      </xsl:if>
      <xsl:if test="@cellpadding">
        <xsl:attribute name="padding">
          <xsl:choose>
            <xsl:when test="contains(@cellpadding, '%')">
              <xsl:value-of select="@cellpadding"/>
            </xsl:when>
            <xsl:otherwise>
              <xsl:value-of select="@cellpadding"/>px</xsl:otherwise>
          </xsl:choose>
        </xsl:attribute>
      </xsl:if>
    </xsl:for-each>
    <xsl:if test="not(@align or ../@align or
                      ../parent::*[self::thead or self::tfoot or
                      self::tbody]/@align) and
                  ancestor::table[1]/*[self::col or
                      self::colgroup]/descendant-or-self::*/@align">
      <xsl:attribute name="text-align">from-table-column()</xsl:attribute>
    </xsl:if>
    <xsl:if test="not(@valign or ../@valign or
                      ../parent::*[self::thead or self::tfoot or
                      self::tbody]/@valign) and
                  ancestor::table[1]/*[self::col or
                      self::colgroup]/descendant-or-self::*/@valign">
      <xsl:attribute name="display-align">from-table-column()</xsl:attribute>
      <xsl:attribute name="relative-align">from-table-column()</xsl:attribute>
    </xsl:if>
    <xsl:call-template name="process-common-attributes"/>
    <fo:block>
      <xsl:apply-templates/>
    </fo:block>
  </xsl:template>

  <xsl:template name="process-col-width">
    <xsl:param name="width"/>
    <xsl:if test="$width and $width != '0*'">
      <xsl:attribute name="column-width">
        <xsl:choose>
          <xsl:when test="contains($width, '*')">
            <xsl:text>proportional-column-width(</xsl:text>
            <xsl:value-of select="substring-before($width, '*')"/>
            <xsl:text>)</xsl:text>
          </xsl:when>
          <xsl:when test="contains($width, '%')">
            <xsl:value-of select="$width"/>
          </xsl:when>
          <xsl:otherwise>
            <xsl:value-of select="$width"/>px</xsl:otherwise>
        </xsl:choose>
      </xsl:attribute>
    </xsl:if>
  </xsl:template>

  <xsl:template name="process-cell-align">
    <xsl:param name="align"/>
    <xsl:if test="$align">
      <xsl:attribute name="text-align">
        <xsl:choose>
          <xsl:when test="$align = 'char'">
            <xsl:choose>
              <xsl:when test="$align/../@char">
                <xsl:value-of select="$align/../@char"/>
              </xsl:when>
              <xsl:otherwise>
                <xsl:value-of select="'.'"/>
                <!-- todo: it should depend on xml:lang ... -->
              </xsl:otherwise>
            </xsl:choose>
          </xsl:when>
          <xsl:otherwise>
            <xsl:value-of select="$align"/>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:attribute>
    </xsl:if>
  </xsl:template>

  <xsl:template name="process-cell-valign">
    <xsl:param name="valign"/>
    <xsl:if test="$valign">
      <xsl:attribute name="display-align">
        <xsl:choose>
          <xsl:when test="$valign = 'middle'">center</xsl:when>
          <xsl:when test="$valign = 'bottom'">after</xsl:when>
          <xsl:when test="$valign = 'baseline'">auto</xsl:when>
          <xsl:otherwise>before</xsl:otherwise>
        </xsl:choose>
      </xsl:attribute>
      <xsl:if test="$valign = 'baseline'">
        <xsl:attribute name="relative-align">baseline</xsl:attribute>
      </xsl:if>
    </xsl:if>
  </xsl:template>

  <!--=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-
       Table-specific attribute-sets
  =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-->

  <xsl:attribute-set name="inside-table">
    <!-- prevent unwanted inheritance -->
    <xsl:attribute name="start-indent">0pt</xsl:attribute>
    <xsl:attribute name="end-indent">0pt</xsl:attribute>
    <xsl:attribute name="text-indent">0pt</xsl:attribute>
    <xsl:attribute name="last-line-end-indent">0pt</xsl:attribute>
    <xsl:attribute name="text-align">start</xsl:attribute>
    <xsl:attribute name="text-align-last">relative</xsl:attribute>
  </xsl:attribute-set>

  <xsl:attribute-set name="table-and-caption" >
    <!-- horizontal alignment of table itself
    <xsl:attribute name="text-align">center</xsl:attribute>
    -->
    <!-- vertical alignment in table-cell -->
    <xsl:attribute name="display-align">center</xsl:attribute>
  </xsl:attribute-set>

  <xsl:attribute-set name="table">
    <xsl:attribute name="border-collapse">separate</xsl:attribute>
    <xsl:attribute name="border-spacing">2px</xsl:attribute>
    <xsl:attribute name="border">1px</xsl:attribute>
    <!--
    <xsl:attribute name="border-style">outset</xsl:attribute>
    -->
  </xsl:attribute-set>

  <xsl:attribute-set name="table-column">
  </xsl:attribute-set>

  <xsl:attribute-set name="thead" use-attribute-sets="inside-table">
  </xsl:attribute-set>

  <xsl:attribute-set name="tfoot" use-attribute-sets="inside-table">
  </xsl:attribute-set>

  <xsl:attribute-set name="tbody" use-attribute-sets="inside-table">
  </xsl:attribute-set>

  <xsl:attribute-set name="tr">
  </xsl:attribute-set>

  <xsl:attribute-set name="th">
    <xsl:attribute name="font-weight">bolder</xsl:attribute>
    <xsl:attribute name="border">1px</xsl:attribute>
    <!--
    <xsl:attribute name="border-style">inset</xsl:attribute>
    -->
    <xsl:attribute name="padding">1px</xsl:attribute>
  </xsl:attribute-set>

  <xsl:attribute-set name="td">
    <xsl:attribute name="border">1px</xsl:attribute>
    <!--
    <xsl:attribute name="border-style">inset</xsl:attribute>
    -->
    <xsl:attribute name="padding">1px</xsl:attribute>
  </xsl:attribute-set>

  <!--=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-
   General XHTML attribute processing named templates
  =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-->

  <xsl:template name="process-common-attributes-and-children">
    <xsl:call-template name="process-common-attributes"/>
    <xsl:apply-templates/>
  </xsl:template>

  <xsl:template name="process-common-attributes">
    <xsl:attribute name="role">
      <xsl:value-of select="local-name()"/>
    </xsl:attribute>

    <xsl:choose>
      <xsl:when test="@xml:lang">
        <xsl:attribute name="xml:lang">
          <xsl:value-of select="@xml:lang"/>
        </xsl:attribute>
      </xsl:when>
      <xsl:when test="@lang">
        <xsl:attribute name="xml:lang">
          <xsl:value-of select="@lang"/>
        </xsl:attribute>
      </xsl:when>
    </xsl:choose>

    <xsl:choose>
      <xsl:when test="@id">
        <xsl:attribute name="id">
          <xsl:value-of select="@id"/>
        </xsl:attribute>
      </xsl:when>
      <xsl:when test="self::a/@name">
        <xsl:attribute name="id">
          <xsl:value-of select="@name"/>
        </xsl:attribute>
      </xsl:when>
    </xsl:choose>

    <xsl:if test="@align">
      <xsl:choose>
        <xsl:when test="self::caption">
        </xsl:when>
        <xsl:when test="self::img or self::object">
          <xsl:if test="@align = 'bottom' or @align = 'middle' or @align = 'top'">
            <xsl:attribute name="vertical-align">
              <xsl:value-of select="@align"/>
            </xsl:attribute>
          </xsl:if>
        </xsl:when>
        <xsl:otherwise>
          <xsl:call-template name="process-cell-align">
            <xsl:with-param name="align" select="@align"/>
          </xsl:call-template>
        </xsl:otherwise>
      </xsl:choose>
    </xsl:if>
    <xsl:if test="@valign">
      <xsl:call-template name="process-cell-valign">
        <xsl:with-param name="valign" select="@valign"/>
      </xsl:call-template>
    </xsl:if>

    <xsl:if test="@style">
      <xsl:call-template name="process-style">
        <xsl:with-param name="style" select="@style"/>
      </xsl:call-template>
    </xsl:if>

  </xsl:template>


</xsl:transform>
