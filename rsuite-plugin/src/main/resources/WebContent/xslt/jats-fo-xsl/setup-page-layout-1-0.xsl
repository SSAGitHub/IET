<?xml version="1.0" encoding="utf-8"?>
<!-- ============================================================= -->
<!--  MODULE:    setup-page-layout-1-0.xsl                         -->
<!--  VERSION:   1.0                                               -->
<!--  DATE:      September 2004                                    -->
<!--                                                               -->
<!-- ============================================================= -->

<!-- ============================================================= -->
<!-- SYSTEM:     Archiving and Interchange DTD Suite               -->
<!--             Journal Article Formatting                        -->
<!--                                                               -->
<!-- PURPOSE:    A module of article-fo.xsl that establishes the   -->
<!--             page layout.                                      -->
<!--                                                               -->
<!-- CONTAINS:   Templates for:                                    -->
<!--             1) Simple page masters                            -->
<!--             2) Page sequences                                 -->
<!--             3) Named-template: define-headers                 -->
<!--             4) Named-template: define-diagnostics-headers     -->
<!--             5) Named template: assemble-page-header-contents  -->
<!--             6) Named template: define-before-float-separator  -->
<!--             7) Named template: define-footnote-separator      -->
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
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xlink="http://www.w3.org/1999/xlink">


    <!-- ============================================================= -->
    <!-- SIMPLE PAGE MASTERS                                           -->
    <!-- ============================================================= -->


    <xsl:template name="define-simple-page-masters">

        <!-- blank and cover are similar -->

        <!-- cover page uses recto margins -->
        <fo:simple-page-master master-name="lo-cover" page-height="297mm" page-width="210mm"
            margin-top="1.0in" margin-bottom="1.0in" margin-left="1.25in" margin-right="1.25in">

            <fo:region-body region-name="body" margin-top="24pt" margin-bottom="1.0in"
                margin-left="0in" margin-right="0in"/>

            <fo:region-before region-name="no-header" display-align="before" extent="12pt"/>

            <fo:region-after region-name="no-footer" extent="0in"/>
        </fo:simple-page-master>

        <!-- blank page -->
        <fo:simple-page-master master-name="lo-blank" page-height="297mm" page-width="210mm"
            margin-top="0.5in" margin-bottom="1.0in" margin-left="1.25in" margin-right="1.25in">

            <fo:region-body region-name="body" margin-top="24pt" margin-bottom="0in"
                margin-left="0in" margin-right="0in"/>

            <fo:region-before region-name="no-header" display-align="before" extent="12pt"/>

            <fo:region-after region-name="no-footer" extent="0in"/>
        </fo:simple-page-master>


        <!-- first has recto margins -->
        <fo:simple-page-master master-name="lo-first" page-height="297mm" page-width="210mm"
            margin-top="0.4in" margin-bottom="0.4in" margin-left="15mm" margin-right="15mm">

            <fo:region-body region-name="body" margin-top="20pt" margin-bottom="28pt"
                margin-left="0mm" margin-right="0mm" column-count="2" column-gap="8mm"/>

            <fo:region-before region-name="first-header" display-align="before" extent="20pt"/>

            <fo:region-after region-name="first-footer" extent="22pt"/>
        </fo:simple-page-master>
        <!-- SA00 add space -->

        <!-- verso page -->
        <fo:simple-page-master master-name="lo-verso" page-height="297mm" page-width="210mm"
            margin-top="0.4in" margin-bottom="0.4in" margin-left="15mm" margin-right="15mm">

            <fo:region-body region-name="body" margin-top="20pt" margin-bottom="28pt"
                margin-left="0mm" margin-right="0mm" column-count="2" column-gap="8mm"/>

            <fo:region-before region-name="verso-header" display-align="before" extent="20pt"/>

            <fo:region-after region-name="verso-footer" extent="22pt"/>
        </fo:simple-page-master>
        <!-- SA00 add space -->

        <!-- recto page -->
        <fo:simple-page-master master-name="lo-recto" page-height="297mm" page-width="210mm"
            margin-top="0.4in" margin-bottom="0.4in" margin-left="15mm" margin-right="15mm">

            <fo:region-body region-name="body" margin-top="20pt" margin-bottom="28pt"
                margin-left="0mm" margin-right="0mm" column-count="2" column-gap="8mm"/>

            <fo:region-before region-name="recto-header" display-align="before" extent="20pt"/>

            <fo:region-after region-name="recto-footer" extent="22pt"/>
        </fo:simple-page-master>
        <!-- SA00 add space -->

    </xsl:template>


    <!-- ============================================================= -->
    <!-- PAGE SEQUENCES                                                -->
    <!-- ============================================================= -->


    <!-- There are three page sequences, for the cover,
     article, and diagnostics. -->

    <xsl:template name="define-page-sequences">

        <!-- seq-cover page sequence master is: 
         cover+, blank -->

        <fo:page-sequence-master master-name="seq-cover">

            <fo:repeatable-page-master-reference master-reference="lo-cover"/>
            <fo:single-page-master-reference master-reference="lo-blank"/>

        </fo:page-sequence-master>


        <!-- seq-content page sequence master is:  
         first, (verso, recto)+ -->

        <fo:page-sequence-master master-name="seq-content">

            <fo:single-page-master-reference master-reference="lo-first"/>

            <fo:repeatable-page-master-alternatives>
                <fo:conditional-page-master-reference odd-or-even="even" master-reference="lo-verso"/>
                <fo:conditional-page-master-reference odd-or-even="odd" master-reference="lo-recto"
                />
            </fo:repeatable-page-master-alternatives>

        </fo:page-sequence-master>


        <!-- seq-diagnostics page sequence master is: 
         (recto, verso)+ -->

        <fo:page-sequence-master master-name="seq-diagnostics">

            <fo:repeatable-page-master-alternatives>
                <fo:conditional-page-master-reference odd-or-even="odd" master-reference="lo-recto"/>
                <fo:conditional-page-master-reference odd-or-even="even" master-reference="lo-verso"
                />
            </fo:repeatable-page-master-alternatives>

        </fo:page-sequence-master>

    </xsl:template>



    <!-- ============================================================= -->
    <!-- DEFINE STATIC CONTENT FOs FOR PAGE HEADERS                    -->
    <!-- ============================================================= -->

    <!-- The actual assembly of content is complicated because
      it must decide what content is available. So, to keep
      the static-content flows easy to read, that work is 
      done in separate named templates. -->

    <!-- The article body pages and the diagnostics pages are
      handled by the same templates: the pagination is
      differentiated by the test is-diagnostics-page.
      The diagnostics page sequence master uses only verso
      and recto pages. -->


    <xsl:template name="define-headers">

        <xsl:param name="is-diagnostics-page"/>

        <!-- SA00 don't need first header -->
        <!--fo:static-content flow-name="first-header">
       <fo:block line-height="12pt" font-weight="bold">
         <fo:wrapper font-family="{$titlefont}"
           font-size="10pt">
           <xsl:call-template name="assemble-recto-header">
             <xsl:with-param name="is-diagnostics-page" select="$is-diagnostics-page"/>
           </xsl:call-template>
         </fo:wrapper>
       </fo:block>
     </fo:static-content-->

        <!-- SA00:footer -->
        <fo:static-content flow-name="first-footer">
            <fo:block-container position="absolute" text-align="left">
                <xsl:call-template name="createFooterSectionJournalTitle"/>
            </fo:block-container>

            <fo:block-container position="absolute" text-align="right">
                <xsl:call-template name="createFooterSectionPageNumber"/>
            </fo:block-container>
        </fo:static-content>

        <fo:static-content flow-name="recto-footer">
            <fo:block-container position="absolute" text-align="left">
                <xsl:call-template name="createFooterSectionJournalTitle"/>
            </fo:block-container>

            <fo:block-container position="absolute" text-align="right">
                <xsl:call-template name="createFooterSectionPageNumber"/>
            </fo:block-container>
        </fo:static-content>

        <fo:static-content flow-name="verso-footer">
            <fo:block-container position="absolute" text-align="left">
                <xsl:call-template name="createFooterSectionPageNumber"/>
            </fo:block-container>
            <fo:block-container position="absolute" text-align="right">
                <xsl:call-template name="createFooterSectionJournalTitle"/>
            </fo:block-container>
        </fo:static-content>


        <fo:static-content flow-name="first-header"> </fo:static-content>

        <fo:static-content flow-name="recto-header"> </fo:static-content>

        <fo:static-content flow-name="verso-header"> </fo:static-content>

        <fo:static-content flow-name="no-header">
            <fo:block space-before="0pt" line-height="0pt" space-after="0pt"/>
        </fo:static-content>

        <fo:static-content flow-name="no-footer">
            <fo:block space-before="0pt" line-height="0pt" space-after="0pt"/>
        </fo:static-content>
    </xsl:template>

    <xsl:template name="createFooterSectionJournalTitle">
        <xsl:call-template name="createFooterLineJournalTitle"/>
        <xsl:call-template name="createFooterLineCopyright"/>
    </xsl:template>

    <xsl:template name="createFooterSectionPageNumber">
        <fo:block font-size="8pt" line-height="11pt" font-family="Arial" id="pageNumber">
            <fo:page-number/>
        </fo:block>

    </xsl:template>

    <xsl:template name="createFooterLineJournalTitle">


        <fo:block font-size="8pt" line-height="11pt" font-family="Arial">
            <fo:inline font-style="italic">
                <xsl:value-of select="//abbrev-journal-title"/>
            </fo:inline>
            <xsl:choose>
                <xsl:when test="$issue-mode = 'proof-issue'">
                    <xsl:variable name="articleID"
                        select="translate(/article/front/article-meta/article-id[@pub-id-type='doi'], './-', '')"/>
                    <xsl:text>, </xsl:text>
                    <xsl:value-of select="replace($issue-abbreviated-cover-date, '([0-9]{1,2}[a-z]+\s)?([a-zA-Z]+\s+)?([0-9]{4})', '$3')"/>
                    <xsl:text>, Vol. </xsl:text>
                    <xsl:value-of select="$issue-volume"/>
                    <xsl:text> Iss. </xsl:text>
                    <xsl:value-of select="$issue-number"/>
                    <xsl:text>, pp. </xsl:text>
                    <fo:page-number-citation ref-id="{concat('startarticle_', $articleID)}"/>
                    <xsl:text>-</xsl:text>
                    <fo:page-number-citation ref-id="{concat('endarticle_', $articleID)}"/>
                </xsl:when>
                <xsl:when test="$issue-mode = 'final-issue-article'">
                    <xsl:text>, </xsl:text>
                    <xsl:value-of
                        select="/article/front/article-meta/pub-date[@pub-type='ppub']/year"/>
                    <xsl:text>, Vol. </xsl:text>
                    <xsl:value-of select="/article/front/article-meta/volume"/>
                    <xsl:text> Iss. </xsl:text>
                    <xsl:value-of select="/article/front/article-meta/issue"/>
                    <xsl:text>, pp. </xsl:text>
                    <xsl:value-of select="/article/front/article-meta/fpage"/>
                    <xsl:text>-</xsl:text>
                    <xsl:value-of select="/article/front/article-meta/lpage"/>
                </xsl:when>
            </xsl:choose>
        </fo:block>
    </xsl:template>

    <xsl:template name="createFooterLineCopyright">
        <fo:block font-size="8pt" line-height="11pt" font-family="Arial">
            <xsl:apply-templates select="//permissions/copyright-statement"/>
            <xsl:text> </xsl:text>
            <xsl:apply-templates select="//permissions/copyright-year"/>
        </fo:block>
        <xsl:for-each select="/article//permissions/license/license-p">
            <fo:block font-size="8pt" line-height="11pt" font-family="Arial">
                <xsl:choose>
                    <!-- Move parens to same line as ext-link -->
                    <xsl:when
                        test="count(node())=3 
                         and name(node()[2])='ext-link'
                         and normalize-space(node()[3])=')'">
                        <xsl:variable name="temp">
                            <license-p>
                                <xsl:apply-templates mode="temp"/>
                            </license-p>
                        </xsl:variable>
                        <xsl:apply-templates select="$temp/node()">
                            <xsl:with-param name="parens" tunnel="yes">TRUE</xsl:with-param>
                        </xsl:apply-templates>
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:apply-templates/>
                    </xsl:otherwise>
                </xsl:choose>
            </fo:block>
        </xsl:for-each>
    </xsl:template>

    <xsl:template match="text()" mode="temp" priority="20">
        <xsl:choose>
            <xsl:when test="matches(., ' ?\( ?$')">
                <xsl:value-of select="replace(.,' ?\( ?$', '')"/>
            </xsl:when>
            <xsl:when test="matches(., ' ?\) ?$')">
                <xsl:value-of select="replace(.,' ?\) ?$', '')"/>
            </xsl:when>
            <xsl:otherwise>
                <xsl:value-of select="."/>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

    <xsl:template match="@*|node()" mode="temp" priority="10">
        <xsl:copy>
            <xsl:apply-templates select="@*|node()" mode="temp"/>
        </xsl:copy>
    </xsl:template>

    <xsl:template match="ext-link[ancestor::license-p]" priority="15">
        <xsl:param name="parens" tunnel="yes">FALSE</xsl:param>
        <!--<fo:basic-link external-destination="{@xlink:href}">-->
        <fo:block/>
        <xsl:choose>
            <xsl:when test="$parens='TRUE'">
                <xsl:text>(</xsl:text>
                <xsl:apply-templates/>
                <xsl:text>)</xsl:text>
            </xsl:when>
            <xsl:otherwise>
                <xsl:apply-templates/>
            </xsl:otherwise>
        </xsl:choose>
        <!--</fo:basic-link>-->
    </xsl:template>

    <xsl:template match="ext-link" priority="10">
        <fo:block>
            <!--<fo:basic-link external-destination="{@xlink:href}">-->
            <xsl:apply-templates/>
            <!--</fo:basic-link>-->
        </fo:block>
    </xsl:template>

    <!-- ============================================================= -->
    <!-- NAMED TEMPLATES: ASSEMBLE PAGE-HEADER CONTENTS                -->
    <!-- ============================================================= -->


    <xsl:template name="assemble-first-page-footer">
        <!-- SA00 first page only -->

        <!-- change context node, choose journal title -->
        <xsl:for-each select="/article/front/journal-meta">
            <xsl:choose>
                <xsl:when test="journal-id[@journal-id-type='nlm-ta']">
                    <xsl:apply-templates select="journal-id[@journal-id-type='nlm-ta']"
                        mode="pass-through"/>
                </xsl:when>
                <xsl:when test="journal-title-group/abbrev-journal-title">
                    <xsl:apply-templates select="journal-title-group/abbrev-journal-title[1]"
                        mode="pass-through"/>
                </xsl:when>
                <xsl:when test="journal-title">
                    <xsl:apply-templates select="journal-title[1]" mode="pass-through"/>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:apply-templates select="journal-id[1]" mode="pass-through"/>
                </xsl:otherwise>
            </xsl:choose>
        </xsl:for-each>

        <!--    <xsl:text> 2014</xsl:text>
-->
        <!-- change context node, get article info -->
        <xsl:for-each select="/article/front/article-meta">
            <!--      <xsl:if test="volume">
        <xsl:text>, Vol. </xsl:text>
        <xsl:apply-templates select="volume" mode="pass-through"/>
      </xsl:if>

      <xsl:if test="issue">
        <xsl:text>, No. </xsl:text>
        <xsl:apply-templates select="issue" mode="pass-through"/>
      </xsl:if>

      <xsl:if test="supplement">
        <xsl:text>, Supp. </xsl:text>
        <xsl:apply-templates select="supplement" mode="pass-through"/>
      </xsl:if>
-->
            <xsl:if test="fpage">
                <xsl:text>, pp. </xsl:text>
                <xsl:apply-templates select="fpage" mode="pass-through"/>

                <xsl:if test="lpage">
                    <xsl:text>&#x02013;</xsl:text>
                    <xsl:apply-templates select="lpage" mode="pass-through"/>
                </xsl:if>

            </xsl:if>
        </xsl:for-each>

    </xsl:template>


    <xsl:template name="assemble-recto-header">

        <xsl:param name="is-diagnostics-page"/>

        <fo:block margin-left="0in" start-indent="0pc" space-after="0pt" text-align-last="left">
            <!-- SA00 set position of page number -->

            <!-- RH/recto page: title at gutter, page# as thumb -->
            <!--xsl:value-of select="$title-for-running-head"/>
    <fo:leader leader-alignment="reference-area" leader-pattern="space"/-->
            <!-- SA00 no running head -->

            <xsl:if test="boolean($is-diagnostics-page)">
                <xsl:text>Diagnostics-</xsl:text>
            </xsl:if>

            <xsl:text>Page </xsl:text>

            <fo:page-number/>
            <xsl:text> of </xsl:text>

            <xsl:choose>
                <xsl:when test="boolean($is-diagnostics-page)">
                    <fo:page-number-citation ref-id="last-diagnostics-page"/>
                </xsl:when>
                <xsl:otherwise>
                    <fo:page-number-citation ref-id="last-article-page"/>
                </xsl:otherwise>
            </xsl:choose>

        </fo:block>

    </xsl:template>


    <xsl:template name="assemble-verso-header">

        <xsl:param name="is-diagnostics-page"/>

        <fo:block margin-left="0in" start-indent="0pc" space-after="0pt" text-align-last="right">

            <!-- LH/verso page: same as recto (suited to online PDF) -->
            <!--xsl:value-of select="$title-for-running-head"/>
    <fo:leader leader-alignment="reference-area" leader-pattern="space"/-->
            <!-- SA00 no running head -->

            <xsl:if test="boolean($is-diagnostics-page)">
                <xsl:text>Diagnostics-</xsl:text>
            </xsl:if>

            <xsl:text>Page </xsl:text>

            <fo:page-number/>
            <xsl:text> of </xsl:text>

            <xsl:choose>
                <xsl:when test="boolean($is-diagnostics-page)">
                    <fo:page-number-citation ref-id="last-diagnostics-page"/>
                </xsl:when>
                <xsl:otherwise>
                    <fo:page-number-citation ref-id="last-article-page"/>
                </xsl:otherwise>
            </xsl:choose>

        </fo:block>

    </xsl:template>


    <!-- ============================================================= -->
    <!-- NAMED TEMPLATE: DEFINE SEPARATOR FOR BEFORE-FLOAT             -->
    <!-- ============================================================= -->


    <xsl:template name="define-before-float-separator">

        <!-- don't really want "a leader" here, but have to have
				something to make a break occur in the static content -->

        <fo:static-content flow-name="xsl-before-float-separator">
            <fo:block line-height="2pt" space-before="0pt" space-after="0pt">
                <fo:leader leader-pattern="space" leader-length="100%"/>
            </fo:block>
        </fo:static-content>
    </xsl:template>


    <!-- ============================================================= -->
    <!-- NAMED TEMPLATE: DEFINE SEPARATOR FOR FOOTNOTES                -->
    <!-- ============================================================= -->

    <xsl:template name="define-footnote-separator">

        <fo:static-content flow-name="xsl-footnote-separator">
            <fo:block start-indent="0pc" end-indent="4in" margin-top="12pt" space-after="8pt"
                border-width="0.5pt" border-bottom-style="solid"/>
        </fo:static-content>

    </xsl:template>


</xsl:transform>
