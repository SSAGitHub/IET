<?xml version="1.0" encoding="utf-8"?>
<!-- ============================================================= -->
<!--  MODULE:    groups-table-wraps-1-0.xsl                        -->
<!--  VERSION:   1.0                                               -->
<!--  DATE:      September 2004                                    -->
<!--                                                               -->
<!-- ============================================================= -->

<!-- ============================================================= -->
<!-- SYSTEM:     Archiving and Interchange DTD Suite               -->
<!--             Journal Article Formatting                        -->
<!--                                                               -->
<!-- PURPOSE:    A module of article-fo.xsl that handles           -->
<!--             tables.                                           -->
<!--                                                               -->
<!-- CONTAINS:   Key:                                              -->
<!--             1) legit-table-wraps                              -->
<!--             Templates for:                                    -->
<!--             1) table-wrap-group                               -->
<!--             2) table-wrap                                     -->
<!--             3) table-wrap/label, caption, and copyright       -->
<!--             4) table and its contents                         -->
<!--             5) table-wrap-foot                                -->
<!--             6) Named template: decide-make-table-wrap-labels  -->
<!--             7) Named template: determine-table-wrap-label     -->
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
  xmlns:mml="http://www.w3.org/1998/Math/MathML"
  xmlns:axf="http://www.antennahouse.com/names/XSL/Extensions">


  <!-- ============================================================= -->
  <!-- KEY: LEGIT-TABLE-WRAPS                                        -->
  <!-- ============================================================= -->


  <xsl:key name="legit-table-wraps"
    match="body/table-wrap
              | sec/table-wrap
              | app/table-wrap
              | table-wrap-group/table-wrap"
    use="local-name()"/>


  <!-- ============================================================= -->
  <!-- TABLE-WRAP-GROUP                                              -->
  <!-- ============================================================= -->


  <!-- Not handled this round (but the contained table-wraps are).   -->

  <xsl:template match="table-wrap-group">
    <fo:block id="{@id}">
      <xsl:apply-templates select="table-wrap"/>
    </fo:block>
  </xsl:template>


  <!-- ============================================================= -->
  <!-- TABLE-WRAP                                                    -->
  <!-- ============================================================= -->


  <!-- table-wrap: contains 
     label, caption, table or other content, and table-wrap-foot.  -->

  <xsl:template match="table-wrap">

    <xsl:param name="my-gid" select="generate-id()"/>

    <xsl:param name="make-table-wrap-labels">
      <!-- this logic is externalized because it is
         also used by xref -->
      <xsl:call-template name="decide-make-table-wrap-labels">
        <xsl:with-param name="object-gid" select="$my-gid"/>
      </xsl:call-template>
    </xsl:param>

    <xsl:param name="float-reference">
      <xsl:choose>
        <xsl:when test="contains(@content-type, 'multicol')">
          <xsl:text>multicol</xsl:text>
        </xsl:when>
        <xsl:otherwise>
          <xsl:text>column</xsl:text>
        </xsl:otherwise>
      </xsl:choose>
    </xsl:param>
    
    <xsl:param name="float-move">
      <xsl:choose>
        <xsl:when test="contains(@content-type, 'keep')">
          <xsl:text>keep</xsl:text>
        </xsl:when>
        <xsl:when test="contains(@content-type, 'next')">
          <xsl:text>next</xsl:text>
        </xsl:when>
        <xsl:otherwise>
          <xsl:text>auto-next</xsl:text>
        </xsl:otherwise>
      </xsl:choose>
    </xsl:param>

    <xsl:param name="float-y">
      <xsl:choose>
        <xsl:when test="contains(@content-type, 'top')">
          <xsl:text>top</xsl:text>
        </xsl:when>
        <xsl:otherwise>
          <xsl:text>bottom</xsl:text>
        </xsl:otherwise>
      </xsl:choose>
    </xsl:param>
    
    <xsl:param name="float-x">
      <xsl:choose>
        <xsl:when test="contains(@content-type, 'right')">
          <xsl:text>end</xsl:text>
        </xsl:when>
        <xsl:otherwise>
          <xsl:text>start</xsl:text>
        </xsl:otherwise>
      </xsl:choose>
    </xsl:param>
    
    <xsl:param name="space-before" select="$leading-around-narrative-blocks"/>
    <xsl:param name="space-after" select="$leading-around-narrative-blocks"/>

   <fo:float axf:float-reference="{$float-reference}"
              axf:float-x="{$float-x}"
              axf:float-y="{$float-y}"
              axf:float-move="{$float-move}"
              axf:float-margin-y="10pt">
    <!-- SA00 no fop support for float -->

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
    <xsl:apply-templates select="table-wrap-foot"/>

    </fo:float>
    <!-- no fop support for float -->

  </xsl:template>



  <!-- ============================================================= -->
  <!-- TABLE-WRAP LABEL, CAPTION, COPYRIGHT-STATEMENT                -->
  <!-- ============================================================= -->


  <!-- label in mode=display; this is the number -->
  <xsl:template match="table-wrap/label" mode="display">
    <xsl:apply-templates/>
  </xsl:template>

  <!-- a caption can contain ONLY title and p's, and we've
     already dealt with the title (if any) -->
  <xsl:template match="table-wrap/caption" mode="display">
    <fo:block space-after="8pt">
      <xsl:apply-templates select="p"/>
    </fo:block>
  </xsl:template>

  <!-- caption/title in mode=display -->
  <xsl:template match="table-wrap/caption/title" mode="display">
    <xsl:apply-templates/>
  </xsl:template>

  <xsl:template match="table-wrap/copyright-statement" mode="display">
    <fo:block>
      <fo:wrapper font-family="{$textfont}" font-size="9pt" font-weight="normal">
        <xsl:apply-templates select="copyright" mode="display"/>
      </fo:wrapper>
    </fo:block>
  </xsl:template>

  <!-- suppress in no-mode -->
  <xsl:template match="table-wrap/label | table-wrap/caption | table-wrap/copyright-statement"/>


  <!-- ============================================================= -->
  <!-- TABLE AND ITS CONTENT: THEAD, TFOOT, TR, TD                   -->
  <!-- ============================================================= -->


  <!-- the table itself -->
  <xsl:template match="table">
    <fo:table start-indent="0pc" border-collapse="collapse"
      border-separation.inline-progression-direction="6pt"
      border-separation.block-progression-direction="2pt" border-top="1pt solid black"
      border-bottom="1pt solid black" width="100%" id="{parent::table-wrap/@id}">
      <xsl:apply-templates/>
    </fo:table>
  </xsl:template>


  <!-- table/thead -->
  <xsl:template match="thead">
    <fo:table-header border-bottom="0.5pt solid black">
       <xsl:apply-templates/>
    </fo:table-header>
  </xsl:template>


  <!-- table/tfoot -->
  <xsl:template match="tfoot">
    <fo:table-footer>
      <xsl:apply-templates/>
    </fo:table-footer>
  </xsl:template>


  <!-- table/tbody -->
  <xsl:template match="tbody">
    <fo:table-body>
      <xsl:apply-templates/>
    </fo:table-body>
  </xsl:template>


  <!-- rows -->
  <xsl:template match="tr">

    <!-- pick up just two values from the 
       source attributes of row -->

    <xsl:param name="text-align">
      <xsl:choose>
        <xsl:when test="@align='center'">center</xsl:when>
        <xsl:when test="@align='right'">end</xsl:when>
        <xsl:when test="@align='left'">start</xsl:when>
        <xsl:otherwise>inherit</xsl:otherwise>
      </xsl:choose>
    </xsl:param>

    <xsl:param name="display-align">
      <xsl:choose>
        <xsl:when test="@valign='center'">center</xsl:when>
        <xsl:when test="@valign='bottom'">after</xsl:when>
        <xsl:when test="@valign='top'">before</xsl:when>
        <xsl:otherwise>inherit</xsl:otherwise>
      </xsl:choose>
    </xsl:param>

    <fo:table-row text-align="{$text-align}" display-align="{$display-align}">
      <xsl:apply-templates/>
    </fo:table-row>
  </xsl:template>


  <!-- cells -->
  <xsl:template match="td | th">

    <!-- first pick up in parameters several of the
       XHTML attributes used in the source -->
    <xsl:param name="colNum" select="count(preceding-sibling::td)+1"/>
    
    <xsl:param name="number-rows-spanned">
      <xsl:choose>
        <xsl:when test="@rowspan">
          <xsl:value-of select="@rowspan"/>
        </xsl:when>
        <xsl:otherwise>1</xsl:otherwise>
      </xsl:choose>
    </xsl:param>

    <xsl:param name="number-columns-spanned">
      <xsl:choose>
        <xsl:when test="@colspan">
          <xsl:value-of select="@colspan"/>
        </xsl:when>
        <xsl:otherwise>1</xsl:otherwise>
      </xsl:choose>
    </xsl:param>

    <xsl:param name="text-align">
       <xsl:choose>
        <xsl:when test="@align='center'">center</xsl:when>
        <xsl:when test="@align='right'">end</xsl:when>
        <xsl:when test="@align='left'">start</xsl:when>
        <xsl:when test="@align='char'">
          <xsl:value-of select="@char"/>
        </xsl:when>
        <xsl:otherwise>
          <xsl:value-of select="parent::tr/parent::tbody/preceding-sibling::colgroup/col[$colNum]/@align"/>
        </xsl:otherwise>
      </xsl:choose>
    </xsl:param>

    <xsl:param name="display-align">
      <xsl:choose>
        <xsl:when test="@valign='center'">center</xsl:when>
        <xsl:when test="@valign='bottom'">after</xsl:when>
        <xsl:when test="@valign='top'">before</xsl:when>
        <xsl:otherwise>inherit</xsl:otherwise>
      </xsl:choose>
    </xsl:param>

    <!-- embolden if we're in a (true) header or footer cell -->

    <xsl:param name="font-weight">
      <xsl:choose>
        <xsl:when test="ancestor::thead or ancestor::tfoot">
          <xsl:text>normal</xsl:text>
        </xsl:when>
        <xsl:otherwise>
          <xsl:text>normal</xsl:text>
        </xsl:otherwise>
      </xsl:choose>
    </xsl:param>

    <!-- OK, now get down to setting the table-cell -->

    <xsl:variable name="newText-align">

      <xsl:choose>
        <xsl:when test="$text-align='char'">
          <xsl:value-of select="'.'"/>
        </xsl:when>
      <xsl:otherwise>
          <xsl:value-of select="$text-align"/>
        </xsl:otherwise>
      </xsl:choose>
    </xsl:variable>
  <xsl:variable name="newTextsize">
      <xsl:choose>
        <xsl:when test="self::td">
          <xsl:value-of select="'8'"/>
        </xsl:when>
        <xsl:otherwise>
          <xsl:value-of select="$textsize"/>
        </xsl:otherwise>
      </xsl:choose>
    </xsl:variable>
    <fo:table-cell text-align="from-table-column()" display-align="{$display-align}"
      number-rows-spanned="{$number-rows-spanned}"
      number-columns-spanned="{$number-columns-spanned}">

      <fo:block line-height="{$textleading}pt" text-align="{$newText-align}" text-indent="0pt">
        <fo:wrapper font-family="Arial" font-size="{$newTextsize}pt" font-weight="{$font-weight}"
          >
          <xsl:apply-templates/>
        </fo:wrapper>
      </fo:block>
    </fo:table-cell>
  </xsl:template>


  <!-- ============================================================= -->
  <!-- TABLE-WRAP-FOOT                                               -->
  <!-- ============================================================= -->


  <!-- most usually contains table footnotes. 
     NOT the same as tfoot which becomes fo:table-footer. -->
  <xsl:template match="table-wrap-foot">
    <fo:block>
      <xsl:apply-templates select="fn" mode="pseudo-footnote"/>
    </fo:block>
  </xsl:template>


  <!-- ============================================================= -->
  <!-- NAMED TEMPLATE: DECIDE-MAKE-TABLE-WRAP-LABELS                 -->
  <!-- ============================================================= -->


  <!-- Figures use a similar mechanism. Here, though, we -could- 
     use a global variable, since tables are simpler than figures,
     but it seems more helpful to keep the fig and table-wrap 
     numbering mechanisms the same. -->

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


  <!-- ============================================================= -->
  <!-- NAMED TEMPLATE: DETERMINE-TABLE-WRAP-LABEL                    -->
  <!-- ============================================================= -->


  <!-- we get here IFF the table-wrap is in a legitimate context
     for labeling AND either we're generating the string and label
     or there is a label in the document -->

  <!-- if SOME legit table-wraps in this document have labels
  
         if this table-wrap HAS a label 
          - if it has no space in it, put out the appropriate word
          - then put out the label
          
         if this table-wrap HAS NO label
          - don't put one out
          
       if NO legit table-wraps in this document have labels
         create one
  -->

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


</xsl:transform>
