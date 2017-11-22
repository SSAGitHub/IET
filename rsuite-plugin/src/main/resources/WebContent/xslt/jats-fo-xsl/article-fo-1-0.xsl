<?xml version="1.0" encoding="utf-8"?>
<!-- ============================================================= -->
<!--  MODULE:    Journal Article Formatting (article-fo.xsl)       -->
<!--  VERSION:   1.0                                               -->
<!--  DATE:      September 2004                                    -->
<!--                                                               -->
<!-- ============================================================= -->

<!-- ============================================================= -->
<!-- SYSTEM:     Archiving and Interchange DTD Suite               -->
<!--             Journal Article Formatting                        -->
<!--                                                               -->
<!-- PURPOSE:    A starter FO stylesheet for journal articles      -->
<!--             that are tagged according to the NCBI             -->
<!--             Journal Publishing DTD (with additional           -->
<!--             constraints that are described in                 -->
<!--             article-fo-constraints.txt)                       -->
<!--                                                               -->
<!-- CONTAINS:   1. Module includes                                -->
<!--             2. Templates for root and article                 -->
<!--             3. Mode: pass-through                             -->
<!--                                                               -->
<!-- INPUT:      1) An XML document valid to Version 1.1 of the    -->
<!--                NCBI Publishing DTD                            -->
<!--                                                               -->
<!-- OUTPUT:     1) A XSL-FO file of the journal article.          -->
<!--                Testing used AntennaHouse XSL Formatter        -->
<!--                2.5.2004.107                                   -->
<!--                The formatted article consists of:             -->
<!--                - cover page showing key article metadata      -->
<!--                - the formatted article (as many pages as      -->
<!--                   needed)                                     -->
<!--                - a final page or pages of user warnings       -->
<!--                  and notes relating to the transformation     --> 
<!--                  (see article-fo-constraints.txt for          -->
<!--                  explanations of these warnings and notes)    -->
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
    xmlns:mml="http://www.w3.org/1998/Math/MathML" xmlns:m="http://dtd.nlm.nih.gov/xsl/util"
    xmlns:AreaTree="http://www.antennahouse.com/names/XSL/AreaTree"
    xmlns:axf="http://www.antennahouse.com/names/XSL/Extensions" xmlns:local="http://www.rsicms.com"
    xmlns:xs="http://www.w3.org/2001/XMLSchema" extension-element-prefixes="m"
    exclude-result-prefixes="xs mml">

    <xsl:output method="xml" indent="no"/>
    <xsl:strip-space
        elements="abstract ack address annotation app app-group 
                           array article article-categories article-meta 
                           author-comment author-notes back bio body boxed-text 
                           break caption chem-struct chem-struct-wrapper 
                           citation col colgroup conference contrib contrib-group 
                           copyright-statement date def def-item def-list 
                           disp-quote etal fig fig-group fn fn-group front 
                           gloss-group glossary glyph-ref graphic history hr 
                           inline-graphic journal-meta kwd-group list list-item 
                           media mml:math name nlm-citation note notes page-count 
                           person-group private-char pub-date publisher ref 
                           ref-list response sec speech statement sub-article 
                           subj-group supplementary-material table table-wrap 
                           table-wrap-foot table-wrap-group tbody tfoot thead 
                           title-group tr trans-abstract verse-group
                           "/>
    <xsl:preserve-space elements="preformat"/>


    <!--<xsl:param name="section">refs</xsl:param>-->
    <xsl:param name="section">all</xsl:param>

    <xsl:param name="type">proof</xsl:param>
    <xsl:param name="area-tree-uri"/>
    <xsl:include href="issue-fo-params.xsl"/>


    <xsl:variable name="areaTree" select="document($area-tree-uri)"/>



    <!-- CONSTANTS ============================================================= -->
    <xsl:variable name="proof_type">proof</xsl:variable>
    <!-- ============================================================= -->

    <!-- ============================================================= -->
    <!-- TO GET STARTED                                                -->
    <!-- ============================================================= -->

    <!-- Put this file and all the xsl:include files listed below
     in a single directory.
     
     Edit util-variables-and-keys-.1-0.xsl to:

       - set the graphics location
       - toggle the display of diagnostic pages after the last
         page of the article.
       
     Make sure the XML document's DOCTYPE declaration points to the
     correct DTD location - otherwise special characters will fail.
     
     Document assumptions and constraints are detailed in
     the file article-fo-constraints.txt.

-->

    <!-- ============================================================= -->
    <!-- INCLUDED FRAGMENTS                                            -->
    <!-- ============================================================= -->


    <xsl:include href="setup-page-layout-1-0.xsl"/>
    <xsl:include href="set-cover-page-1-0.xsl"/>
    <xsl:include href="set-opener-body-back-1-0.xsl"/>

    <xsl:include href="basics-paragraphs-1-0.xsl"/>
    <xsl:include href="basics-inlines-1-0.xsl"/>
    <xsl:include href="basics-sections-1-0.xsl"/>
    <xsl:include href="basics-referencing-elements-1-0.xsl"/>
    <xsl:include href="basics-display-objects-1-0.xsl"/>
    <xsl:include href="basics-backmatter-1-0.xsl"/>

    <xsl:include href="groups-lists-1-0.xsl"/>
    <xsl:include href="groups-by-content-type-1-0.xsl"/>
    <xsl:include href="groups-footnotes-1-0.xsl"/>
    <xsl:include href="groups-ref-list-1-0.xsl"/>

    <xsl:include href="groups-figures-1-0.xsl"/>
    <xsl:include href="xhtml-tables-fo.xsl"/>
    <!--<xsl:include href="groups-table-wraps-1-0.xsl"/>-->
    <xsl:include href="groups-that-float-1-0.xsl"/>

    <xsl:include href="util-variables-and-keys-1-0.xsl"/>
    <xsl:include href="util-calculate-numbers-1-0.xsl"/>
    <xsl:include href="util-named-title-styles-1-0.xsl"/>

    <xsl:include href="util-diagnostics-1-0.xsl"/>


    <!-- ============================================================= -->
    <!-- ON ROOT, SET UP PAGE LAYOUTS and PROCESS DOCUMENT             -->
    <!-- ============================================================= -->


    <xsl:template match="/">

        <fo:root font-size="9pt" line-height="10pt" font-selection-strategy="character-by-character"
            font-family="Times New Roman, Arial Unicode MS, STIX">

            <fo:layout-master-set>
                <xsl:call-template name="define-simple-page-masters"/>
                <xsl:call-template name="define-page-sequences"/>
            </fo:layout-master-set>

            <xsl:apply-templates/>

        </fo:root>
    </xsl:template>


    <!-- ============================================================= -->
    <!-- ON DOCUMENT ELEMENT, SET UP AND POPULATE THE PAGE FLOWS       -->
    <!-- ============================================================= -->


    <!-- This template controls the order of the main parts:
     cover page, article-opener and body, and backmatter. 
     Each is handled by a named template. -->

    <!-- Call the document content into the pages.                     -->

    <xsl:template match="/article">

        <!-- Populate the cover-page sequence -->
        <!--fo:page-sequence master-reference="seq-cover">
  
    <xsl:call-template name="define-headers"/>
    <xsl:call-template name="define-before-float-separator"/>
    
    <fo:flow flow-name="body">
      <fo:block space-before="0pt"
                space-after="0pt"
                margin-top="0pt"
                margin-bottom="0pt"
                line-stacking-strategy="font-height"
                line-height-shift-adjustment="disregard-shifts"
                font-size="8pt">
        <xsl:call-template name="set-article-cover-page"/>
      </fo:block>
    </fo:flow>
    
  </fo:page-sequence-->

        <xsl:param name="start-page-param"/>

        <xsl:variable name="start-page-variable">
            <xsl:choose>
                <xsl:when test="$issue-mode = 'final-issue-article'">
                    <xsl:value-of select="/article/front/article-meta/fpage"/>
                </xsl:when>
                <xsl:when test="not($start-page-param)">
                    <xsl:text>1</xsl:text>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:value-of select="$start-page-param"/>
                </xsl:otherwise>
            </xsl:choose>
        </xsl:variable>


        <!-- Populate the content sequence -->
        <fo:page-sequence master-reference="seq-content" initial-page-number="1">
            <xsl:attribute name="initial-page-number">
                <xsl:value-of select="normalize-space($start-page-variable)"/>
            </xsl:attribute>

            <xsl:call-template name="define-headers"/>
            <xsl:call-template name="define-before-float-separator"/>
            <xsl:call-template name="define-footnote-separator"/>

            <xsl:variable name="articleID"
                select="translate(front/article-meta/article-id[@pub-id-type='doi'], './-', '')"/>


            <fo:flow flow-name="body">
                <fo:block id="{concat('startarticle_', $articleID)}"/>
                

                <!-- set the article opener, body, and backmatter -->
                <xsl:call-template name="set-article-opener"/>
                <xsl:call-template name="set-article-body"/>
              <fo:block id="{concat('endbodyarticle_', $articleID)}"/>
                <!--/fo:block-->

                
                <xsl:call-template name="set-article-back">
                    <xsl:with-param name="pass">first</xsl:with-param>
                </xsl:call-template>

                <xsl:variable name="startArticleId" select="concat('startarticle_', $articleID)"/>
                <xsl:variable name="endArticleId" select="concat('endarticle_', $articleID)"/>
                <xsl:message>startArticleID: <xsl:value-of select="$startArticleId"/></xsl:message>
                <xsl:message>endArticleID: <xsl:value-of select="$endArticleId"/></xsl:message>
                
                <xsl:if test="not($section='body')">
                    <xsl:variable name="endarticle"
                        select="$areaTree//AreaTree:BlockArea[@id =  $endArticleId]"/>
                    <xsl:variable name="totalDouble"
                        select="count($endarticle/ancestor::AreaTree:ColumnReferenceArea/following-sibling::AreaTree:AbsoluteFloatArea[not(@right-position) and not(@left-position)])"/>
                    <xsl:variable name="totalLeft"
                        select="count($endarticle/ancestor::AreaTree:ColumnReferenceArea/following-sibling::AreaTree:AbsoluteFloatArea[@right-position])"/>
                    <xsl:variable name="totalRight"
                        select="count($endarticle/ancestor::AreaTree:ColumnReferenceArea/following-sibling::AreaTree:AbsoluteFloatArea[@left-position])"/>
                    <xsl:message>DOUBLE:<xsl:value-of select="$totalDouble"/></xsl:message>
                    <xsl:message>LEFT:<xsl:value-of select="$totalLeft"/></xsl:message>
                    <xsl:message>RIGHT:<xsl:value-of select="$totalRight"/></xsl:message>
                    <xsl:choose>
                        <!-- Text ends in column 1 -->
                        <xsl:when
                            test="not($endarticle/ancestor::AreaTree:ColumnReferenceArea/preceding-sibling::AreaTree:ColumnReferenceArea)">
                            <xsl:message>Ends col 1</xsl:message>
                            <xsl:choose>
                                <xsl:when test="($totalDouble + $totalLeft + $totalRight) > 0">
                                    <xsl:variable name="top1"
                                        select="substring-before($endarticle/@top-position, 'p')"/>
                                    <xsl:variable name="top2">
                                        <xsl:choose>
                                            <xsl:when
                                                test="$endarticle/ancestor::AreaTree:MainReferenceArea//AreaTree:AbsoluteFloatArea[@top-position]">
                                                <xsl:value-of
                                                  select="substring-before($endarticle/ancestor::AreaTree:MainReferenceArea//AreaTree:AbsoluteFloatArea[@top-position][1]/@top-position, 'p')"
                                                />
                                            </xsl:when>
                                            <xsl:otherwise>0</xsl:otherwise>
                                        </xsl:choose>
                                    </xsl:variable>
                                    <xsl:choose>
                                        <xsl:when test="number($top2) > number($top1)">
                                            <fo:block-container background-color="white"
                                                height="{number($top2) - number($top1) - 10}pt">
                                                <fo:block>
                                                  <xsl:text> </xsl:text>
                                                </fo:block>
                                            </fo:block-container>
                                        </xsl:when>
                                        <xsl:otherwise>
                                            <xsl:choose>
                                                <xsl:when
                                                  test="$endarticle/following::AreaTree:PageViewportArea[(preceding::AreaTree:BlockArea[contains(@id, 'startarticle')])[position()=last()]/@id = $startArticleId and
                          not(.//AreaTree:BlockArea[contains(@id, 'startarticle')])]">
                                                  <fo:block-container background-color="white"
                                                  height="{736- number($top1)}pt">
                                                  <fo:block>
                                                  <xsl:text> </xsl:text>
                                                  </fo:block>
                                                  </fo:block-container>
                                                </xsl:when>
                                                <xsl:otherwise>
                                                  <xsl:variable name="singleColFloats"
                                                  select="count($endarticle/ancestor::AreaTree:MainReferenceArea//AreaTree:AbsoluteFloatArea[@right-position])"/>
                                                  <xsl:variable name="allFloats"
                                                  select="count($endarticle/ancestor::AreaTree:MainReferenceArea//AreaTree:AbsoluteFloatArea)"/>
                                                  <xsl:variable name="diffFloats"
                                                  select="$allFloats - $singleColFloats"/>
                                                  <xsl:variable name="singleColbeforeDoubleCol">
                                                  <xsl:for-each
                                                  select="$endarticle/ancestor::AreaTree:MainReferenceArea//AreaTree:AbsoluteFloatArea[@right-position]">
                                                  <xsl:if
                                                  test="following-sibling::AreaTree:AbsoluteFloatArea[not(@right-position) and not(@left-position)]">
                                                  <xsl:sequence select="."/>
                                                  </xsl:if>
                                                  </xsl:for-each>
                                                  </xsl:variable>
                                                  <xsl:message>$singleColbeforeDoubleCol:<xsl:sequence
                                                  select="$singleColbeforeDoubleCol"/></xsl:message>
                                                  <xsl:choose>
                                                  <xsl:when
                                                  test="not($singleColbeforeDoubleCol//AreaTree:AbsoluteFloatArea)"/>
                                                  <xsl:when
                                                  test="$diffFloats = 1 and (.//AreaTree:AbsoluteFloatArea[position()=last()])/@right-position"> </xsl:when>
                                                  <xsl:otherwise>
                                                  <fo:block-container background-color="white"
                                                  height="{736- number($top1)}pt">
                                                  <fo:block>
                                                  <xsl:text> </xsl:text>
                                                  </fo:block>

                                                  </fo:block-container>
                                                  <xsl:variable name="lastDblColPos">
                                                  <xsl:choose>
                                                  <xsl:when test="$totalDouble > 0">
                                                  <xsl:value-of
                                                  select="number(substring-before($endarticle/ancestor::AreaTree:MainReferenceArea//AreaTree:AbsoluteFloatArea[not(@right-position) and not(@left-position)][position()=last()]/@top-position, 'p'))"
                                                  />
                                                  </xsl:when>

                                                  <xsl:otherwise>
                                                  <xsl:value-of select="number(0)"/>
                                                  </xsl:otherwise>
                                                  </xsl:choose>
                                                  </xsl:variable>
                                                  <xsl:for-each
                                                  select="$endarticle/ancestor::AreaTree:MainReferenceArea//AreaTree:AbsoluteFloatArea[@right-position]">
                                                  <xsl:if
                                                  test="$lastDblColPos > number(substring-before(@top-position, 'p'))">
                                                  <fo:block-container background-color="white"
                                                  height="{@height}">
                                                  <fo:block>
                                                  <xsl:text> </xsl:text>
                                                  </fo:block>
                                                  </fo:block-container>
                                                  </xsl:if>
                                                  </xsl:for-each>
                                                  </xsl:otherwise>
                                                  </xsl:choose>
                                                </xsl:otherwise>
                                            </xsl:choose>
                                        </xsl:otherwise>
                                    </xsl:choose>
                                    <xsl:if
                                        test="$endarticle/following::AreaTree:PageViewportArea[(preceding::AreaTree:BlockArea[contains(@id, 'startarticle')])[position()=last()]/@id = $startArticleId and
                    not(.//AreaTree:BlockArea[contains(@id, 'startarticle')])]">
                                        <xsl:variable name="totalFloats" as="xs:double"
                                            select="sum(for $height in $endarticle/ancestor::AreaTree:MainReferenceArea//AreaTree:AbsoluteFloatArea[not(@right-position)]/@height 
                      return (number(substring-before($height, 'p'))))"/>
                                        <xsl:choose>
                                            <!-- No floats in column 2-->
                                            <xsl:when test="number($totalFloats)=0">
                                                <fo:block-container background-color="white"
                                                  height="{736 - 10}pt">
                                                  <fo:block>
                                                  <xsl:text> </xsl:text>
                                                  </fo:block>
                                                </fo:block-container>
                                            </xsl:when>
                                            <!-- Floats in column 2-->
                                            <xsl:when
                                                test="($endarticle/ancestor::AreaTree:MainReferenceArea//AreaTree:AbsoluteFloatArea[position()=last()])/@bottom-position">
                                                <xsl:message>COLUMN___2-B</xsl:message>
                                                <fo:block-container background-color="white"
                                                  height="{736 - number($totalFloats)-10}pt">
                                                  <fo:block>
                                                  <xsl:text> </xsl:text>
                                                  </fo:block>
                                                </fo:block-container>
                                            </xsl:when>
                                        </xsl:choose>
                                    </xsl:if>
                                </xsl:when>
                            </xsl:choose>
                        </xsl:when>
                        <!-- Text ends in column 2 -->
                        <xsl:when
                            test="$endarticle/ancestor::AreaTree:ColumnReferenceArea/preceding-sibling::AreaTree:ColumnReferenceArea">
                            <xsl:message>Ends-Col 2</xsl:message>
                            <xsl:choose>
                                <xsl:when
                                    test="$endarticle/following::AreaTree:PageViewportArea[(preceding::AreaTree:BlockArea[contains(@id, 'startarticle')])[position()=last()]/@id = $startArticleId and
                  not(.//AreaTree:BlockArea[contains(@id, 'startarticle')])]">
                                    <xsl:message>TEMP-1</xsl:message>
                                    <xsl:variable name="textTop" as="xs:double"
                                        select="number(substring-before($endarticle/@top-position, 'p'))"/>
                                    <!-- Determine if any single or double column objects fall below end of text -->
                                    <!-- Find floats that start below the end of the text -->
                                    <xsl:variable as="element()" name="topPos">
                                        <tops>
                                            <xsl:for-each
                                                select="$endarticle/ancestor::AreaTree:ColumnReferenceArea/following-sibling::AreaTree:AbsoluteFloatArea[@left-position]
                              | $endarticle/ancestor::AreaTree:ColumnReferenceArea/following-sibling::AreaTree:AbsoluteFloatArea[not(@left-position) and not(@right-position)]">
                                                <xsl:if
                                                  test="number(substring-before(@top-position, 'p')) > $textTop">
                                                  <top>
                                                  <xsl:value-of
                                                  select="number(substring-before(@top-position, 'p'))"
                                                  />
                                                  </top>
                                                </xsl:if>
                                            </xsl:for-each>
                                        </tops>
                                    </xsl:variable>
                                    <!-- Sort all floats that start after end of text -->
                                    <xsl:variable name="sortedTopPositions">
                                        <xsl:for-each select="$topPos//top">
                                            <xsl:sort select="."/>
                                            <xsl:sequence select="."/>
                                        </xsl:for-each>
                                    </xsl:variable>
                                    <xsl:variable name="floatBelowText">
                                        <xsl:value-of select="number($sortedTopPositions[1])"/>
                                    </xsl:variable>
                                    <xsl:choose>
                                        <xsl:when test="$floatBelowText > $textTop">
                                            <fo:block-container background-color="white"
                                                height="{$floatBelowText - $textTop - 10}pt">
                                                <fo:block>
                                                  <xsl:text> </xsl:text>
                                                </fo:block>
                                            </fo:block-container>
                                        </xsl:when>
                                        <xsl:otherwise>
                                            <fo:block-container background-color="white"
                                                height="{736 - $textTop - 10}pt">
                                                <fo:block>
                                                  <xsl:text> </xsl:text>
                                                </fo:block>
                                            </fo:block-container>
                                        </xsl:otherwise>
                                    </xsl:choose>
                                </xsl:when>
                                <xsl:otherwise>
                                    <xsl:variable name="textTop" as="xs:double"
                                        select="number(substring-before($endarticle/@top-position, 'p'))"/>
                                    <xsl:message>texttop: <xsl:value-of select="$textTop"
                                        /></xsl:message>
                                    <xsl:variable as="element()" name="topPos">
                                        <tops>
                                            <xsl:for-each
                                                select="$endarticle/ancestor::AreaTree:ColumnReferenceArea/following-sibling::AreaTree:AbsoluteFloatArea[not(@right-position)]">
                                                <xsl:if
                                                  test="number(substring-before(@top-position, 'p')) > $textTop">
                                                  <top>
                                                  <xsl:value-of
                                                  select="number(substring-before(@top-position, 'p'))"
                                                  />
                                                  </top>
                                                </xsl:if>
                                            </xsl:for-each>
                                        </tops>
                                    </xsl:variable>
                                    <xsl:message>topPos:<xsl:sequence select="$topPos"
                                        /></xsl:message>
                                    <xsl:variable name="sortedTopPositions">
                                        <xsl:for-each select="$topPos//top">
                                            <xsl:sort select="."/>
                                            <xsl:sequence select="."/>
                                        </xsl:for-each>
                                    </xsl:variable>
                                    <xsl:message>sortedTopPositions:<xsl:value-of
                                            select="$sortedTopPositions"/></xsl:message>
                                    <xsl:variable name="floatBelowText">
                                        <xsl:value-of
                                            select="number($sortedTopPositions//top[position()=last()])"
                                        />
                                    </xsl:variable>
                                    <xsl:message>floatBelowText: <xsl:value-of
                                            select="$floatBelowText"/></xsl:message>
                                    <xsl:if test="$floatBelowText > $textTop">
                                        <fo:block-container background-color="white"
                                            height="{$floatBelowText - $textTop - 5}pt">
                                            <fo:block>
                                                <xsl:text> </xsl:text>
                                            </fo:block>
                                        </fo:block-container>
                                    </xsl:if>
                                </xsl:otherwise>
                            </xsl:choose>
                        </xsl:when>
                    </xsl:choose>
                    <xsl:if test="$endarticle">
                        <xsl:call-template name="proc-following-pages">
                            <xsl:with-param name="startArticleId" select="$startArticleId"/>
                            <xsl:with-param name="endarticle" select="$endarticle"/>
                        </xsl:call-template>
                    </xsl:if>

                    <xsl:call-template name="set-article-back">
                        <xsl:with-param name="pass">second</xsl:with-param>
                    </xsl:call-template>
                </xsl:if>
                <!--/fo:block-->

                <!-- an id of 0 height on last article page, 
           for use in page headers -->
                <!--fo:block id="last-article-page"
                space-before="0pt"
                space-after="0pt"
                margin-top="0pt"
                margin-bottom="0pt"
                line-height="0pt"/-->
                <xsl:if
                    test="not(ancestor-or-self::*[@specific-use='unbalanced']) and not($section = 'body')">
                    <fo:block span="all">
                        <fo:leader leader-length="0pt" leader-pattern="space"/>
                    </fo:block>
                </xsl:if>
                
                <xsl:choose>
                  <xsl:when test="not($section='body')">
                    <fo:float xmlns:local="http://www.localFuntion"
                      xmlns:AHtree="http://www.antennahouse.com/names/XSL/AreaTree"
                      axf:float-x="start" axf:float-y="top">
                        <xsl:if test=".//back/app-group">
                            <xsl:attribute name="axf:float-reference" select="'multicol'"/>
                        </xsl:if>
                      <fo:block id="{$endArticleId}"/>
                    </fo:float>
                  </xsl:when>
                  <xsl:otherwise>
                    <fo:block id="{$endArticleId}"/>
                  </xsl:otherwise>
                </xsl:choose>
                
            </fo:flow>

        </fo:page-sequence>


        <!-- If requested, produce document diagnostics 
       (after the end of the article). -->

        <xsl:if test="$produce-diagnostics">
            <!-- has a page sequence in it and all else needed -->
            <xsl:call-template name="run-diagnostics"/>
        </xsl:if>



    </xsl:template>


    <!-- ============================================================= -->
    <!-- MODE = PASS-THROUGH                                           -->
    <!-- ============================================================= -->


    <!-- For use when the element must be handled in a mode
     (to avoid duplicate production) and the intention
     is simply to process the element's content, i.e.,
     without adding spaces, punctuation, fonts, etc. -->

    <!-- used on cover page -->

    <xsl:template name="proc-following-pages">
        <xsl:param name="startArticleId" as="xs:string"/>
        <xsl:param name="endarticle" as="node()"/>
        <xsl:for-each
            select="$endarticle/following::AreaTree:PageViewportArea[(preceding::AreaTree:BlockArea[contains(@id, 'startarticle')])[position()=last()]/@id = $startArticleId and
      not(.//AreaTree:BlockArea[contains(@id, 'startarticle')])]">
            <!-- Column 1 processing -->
            <xsl:variable name="totalDouble"
                select="count(.//AreaTree:AbsoluteFloatArea[not(@right-position) and not(@left-position)])"/>
            <xsl:variable name="totalLeft"
                select="count(.//AreaTree:AbsoluteFloatArea[@right-position])"/>
            <xsl:variable name="totalRight"
                select="count(.//AreaTree:AbsoluteFloatArea[@left-position])"/>
            <xsl:message>DOUBLE-2:<xsl:value-of select="$totalDouble"/></xsl:message>
            <xsl:message>LEFT-2:<xsl:value-of select="$totalLeft"/></xsl:message>
            <xsl:message>RIGHT-2:<xsl:value-of select="$totalRight"/></xsl:message>

            <!-- Column 1 processing -->
            <!-- find last float in first column -->
            <xsl:variable name="col1A">
                <xsl:value-of
                    select="(.//AreaTree:AbsoluteFloatArea[@right-position] | .//AreaTree:AbsoluteFloatArea[not(@right-position) and not(@left-position)])[position()=last()]/@bottom-position"
                />
            </xsl:variable>
            <xsl:variable name="col1">
                <xsl:value-of select="number(substring-before($col1A, 'p'))-10"/>
            </xsl:variable>
            <xsl:message>COL1:<xsl:value-of select="$col1"/></xsl:message>
            <xsl:choose>
                <xsl:when test="position()=last()">
                    <xsl:variable name="singleColbeforeDoubleCol">
                        <xsl:for-each
                            select=".//AreaTree:AbsoluteFloatArea[@right-position]">
                            <xsl:if
                                test="following-sibling::AreaTree:AbsoluteFloatArea[not(@right-position) and not(@left-position)]">
                                <xsl:sequence select="."/>
                            </xsl:if>
                        </xsl:for-each>
                    </xsl:variable>
                    <xsl:choose>
                        <xsl:when test="$totalRight + $totalLeft = 0"/>
                        <xsl:when test="not($singleColbeforeDoubleCol//AreaTree:AbsoluteFloatArea)">
                            <xsl:message>No Block Needed</xsl:message>
                        </xsl:when>
                        <!-- References can't start in column 1 because there are floats in col 2 -->
                        <xsl:when test="$totalRight > 0">
                            <fo:block-container background-color="white" height="{$col1}pt">
                                <fo:block>
                                    <xsl:text> </xsl:text>
                                </fo:block>

                            </fo:block-container>
                        </xsl:when>
                        <xsl:when test="$totalLeft > 0">
                            <fo:block-container background-color="white" height="{$col1}pt">
                                <fo:block>
                                    <xsl:text> </xsl:text>
                                </fo:block>

                            </fo:block-container>
                        </xsl:when>
                    </xsl:choose>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:variable name="totalFloats" as="xs:double"
                        select="sum(for $height in .//AreaTree:AbsoluteFloatArea[not(@left-position)]/@height 
            return (number(substring-before($height, 'p'))+5))"/>
                    <fo:block-container background-color="white"
                        height="{736 - number($totalFloats) - 10}pt">
                        <fo:block>
                            <xsl:text> </xsl:text>
                        </fo:block>
                    </fo:block-container>
                </xsl:otherwise>
            </xsl:choose>
            <!-- Column 2 processing -->
            <xsl:choose>
                <xsl:when test="position()=last()">
                    <xsl:choose>
                        <!-- there are floats in column 2 on last page: no blocks needed -->
                        <xsl:when test="$totalLeft > 0 and $totalDouble > 0">
                            <xsl:message>Last Page Col 2A</xsl:message>
                            <xsl:for-each
                                select=".//AreaTree:AbsoluteFloatArea[@right-position]
                [following-sibling::AreaTree:AbsoluteFloatArea[not(@right-position) and not(@left-position)]]">
                                <fo:block-container background-color="white" height="{@height}">
                                    <fo:block>
                                        <xsl:text> </xsl:text>
                                    </fo:block>
                                </fo:block-container>
                            </xsl:for-each>
                        </xsl:when>
                        <!-- there are no double column floats on last page: no block needed -->
                        <xsl:when test="$totalRight = 0"/>
                        <!-- there are no double column floats on last page: no block needed -->
                        <xsl:when test="$totalDouble = 0"/>
                        <xsl:otherwise>
                            <xsl:for-each
                                select=".//AreaTree:AbsoluteFloatArea[@right-position]
                [following-sibling::AreaTree:AbsoluteFloatArea[not(@right-position) and not(@left-position)]]">
                                <fo:block-container background-color="white" height="{@height}">
                                    <fo:block>
                                        <xsl:text> </xsl:text>
                                    </fo:block>
                                </fo:block-container>
                            </xsl:for-each>
                        </xsl:otherwise>
                    </xsl:choose>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:message>$totalRight:<xsl:value-of select="$totalRight"/></xsl:message>
                    <xsl:message>$totalDouble:<xsl:value-of select="$totalDouble"/></xsl:message>
                    <xsl:choose>
                        <xsl:when test="$totalRight = 0 and $totalDouble = 0">
                            <fo:block-container background-color="white" height="{736 - 30}pt">
                                <fo:block>
                                    <xsl:text> </xsl:text>
                                </fo:block>
                            </fo:block-container>
                        </xsl:when>
                        <xsl:when test="$totalRight > 0">
                            <!-- total floats in right column -->
                            <xsl:variable name="totalFloats" as="xs:double"
                                select="sum(for $height in .//AreaTree:AbsoluteFloatArea[@left-position]/@height |
                                .//AreaTree:AbsoluteFloatArea[not(@right-position)][not(@left-position)]/@height
                return (number(substring-before($height, 'p'))+5))"/>
                            <fo:block-container background-color="white"
                                height="{736 - number($totalFloats) - 10}pt">
                                <fo:block>
                                    <xsl:text> </xsl:text>
                                </fo:block>
                            </fo:block-container>
                        </xsl:when>
                        <!-- no floats in column 2 -->
                        <xsl:otherwise>
                            <!-- output block for each col 1 float followed by double col float -->
                            <xsl:for-each select=".//AreaTree:AbsoluteFloatArea[@right-position]">
                                <fo:block-container background-color="white" height="{@height}">
                                    <fo:block>
                                        <xsl:text> </xsl:text>
                                    </fo:block>
                                </fo:block-container>
                            </xsl:for-each>
                            <!-- sum of floats in column 1 -->
                            <xsl:variable name="lastDblFloat" as="xs:string"
                                select="substring-before((.//AreaTree:AbsoluteFloatArea[not(@right-position)][not(@left-position)])[last()]/@bottom-position, 'p')"/>
                            <xsl:variable name="lastSnglFloat" as="xs:string"
                                select="substring-before((.//AreaTree:AbsoluteFloatArea[@right-position | @left-position])[last()]/@bottom-position, 'p')"/>
                            <!-- @bottom-position indicates space left in column -->
                            <xsl:choose>
                                <xsl:when test="$lastDblFloat != '' and $lastSnglFloat != ''">
                                    <xsl:choose>
                                        <xsl:when test="number($lastDblFloat) > number($lastSnglFloat)">
                                            <fo:block-container background-color="white"
                                                height="{number($lastSnglFloat) - 10}pt">
                                                <fo:block>
                                                    <xsl:text> </xsl:text>
                                                </fo:block>
                                            </fo:block-container>
                                        </xsl:when>
                                        <xsl:otherwise>
                                            <fo:block-container background-color="white"
                                                height="{number($lastDblFloat) - 10}pt">
                                                <fo:block>
                                                    <xsl:text> </xsl:text>
                                                </fo:block>
                                            </fo:block-container>
                                        </xsl:otherwise>
                                    </xsl:choose>
                                  </xsl:when>
                                <xsl:when test="$lastDblFloat != '' and $lastSnglFloat = ''">
                                    <fo:block-container background-color="white"
                                        height="{number($lastDblFloat) - 10}pt">
                                        <fo:block>
                                            <xsl:text> </xsl:text>
                                        </fo:block>
                                    </fo:block-container>
                                </xsl:when>
                            </xsl:choose>
                        </xsl:otherwise>
                    </xsl:choose>
                </xsl:otherwise>
            </xsl:choose>
        </xsl:for-each>

    </xsl:template>

    <!-- ============================================================= -->
    <!-- MODE = PASS-THROUGH                                           -->
    <!-- ============================================================= -->


    <!-- For use when the element must be handled in a mode
     (to avoid duplicate production) and the intention
     is simply to process the element's content, i.e.,
     without adding spaces, punctuation, fonts, etc. -->

    <!-- used on cover page -->

    <xsl:template mode="pass-through"
        match="article-title | alt-title
       | abbrev-journal-title | journal-title | journal-id
       | volume | issue | supplement | fpage | lpage">
        <xsl:apply-templates/>
    </xsl:template>

    <!-- used in article-opener -->

    <xsl:template mode="pass-through"
        match="name | given-names | surname | suffix | collab
                   | role | on-behalf-of">
        <xsl:apply-templates mode="pass-through"/>
    </xsl:template>



    <xsl:function name="local:calcObjectHeight" as="xs:double">
        <xsl:param name="colspec" as="element()?"/>
        <xsl:param name="tableWidth" as="xs:double"/>
        <xsl:param name="numCols" as="xs:integer"/>
        <xsl:param name="availableWidth" as="xs:double"/>
        <!-- tableWidth - explicit widths -->
        <xsl:param name="proportionalColspecs" as="element()*"/>

        <!-- FIXME: Right now not trying to handle any measurement
        units other than points (which is what we get from Word
        tables) or proportional column widths.
     -->
        <xsl:variable name="result" as="xs:double">
            <xsl:choose>
                <xsl:when test="boolean($colspec)">
                    <xsl:variable name="baseWid" as="xs:string" select="$colspec/@colwidth"/>
                    <xsl:choose>
                        <xsl:when test="ends-with($baseWid, 'pt')">
                            <xsl:sequence select="number(substring-before($baseWid, 'pt'))"/>
                        </xsl:when>
                        <xsl:when test="ends-with($baseWid, '*')">
                            <!-- Proportional width: Divides amount of total width among all proportional columns. 
             240/(1+1.38)*1.38=139.1596
             240/(1+1.38)     =100.8403  //Multiplied by 1 in this case
             
             -->
                            <xsl:variable name="totalProportions" as="xs:double"
                                select="sum(for $width in $proportionalColspecs/@colwidth 
                return number(substring-before($width, '*')))"/>
                            <xsl:variable name="proportion" as="xs:double"
                                select="number(substring-before($baseWid, '*'))"/>
                            <xsl:sequence
                                select="($availableWidth div $totalProportions) * $proportion"/>
                        </xsl:when>
                        <xsl:otherwise>
                            <xsl:sequence select="$tableWidth div $numCols"/>
                        </xsl:otherwise>
                    </xsl:choose>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:sequence select="$tableWidth div $numCols"/>
                </xsl:otherwise>
            </xsl:choose>
        </xsl:variable>
        <xsl:sequence select="$result"/>
    </xsl:function>

</xsl:transform>
