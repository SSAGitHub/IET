<?xml version="1.0" encoding="utf-8"?>
<!-- ============================================================= -->
<!--  MODULE:    set-opener-body-back-1-0.xsl                      -->
<!--  VERSION:   1.0                                               -->
<!--  DATE:      September 2004                                    -->
<!--                                                               -->
<!-- ============================================================= -->

<!-- ============================================================= -->
<!-- SYSTEM:     Archiving and Interchange DTD Suite               -->
<!--             Journal Article Formatting                        -->
<!--                                                               -->
<!-- PURPOSE:    A module of article-fo.xsl that structures        -->
<!--             the main flow of the article content: article     -->
<!--             opening, body, and back; and handleds the         -->
<!--             article-opener.                                   -->
<!--                                                               -->
<!-- CONTAINS:   1) Named template: set-article-opener             -->
<!--             2) Named template: set-article-body               -->
<!--             3) Named template: set-article-back               -->
<!--             The remaining templates in this module are all in -->
<!--             support of the article opener:                    -->
<!--             4) article title                                  -->
<!--             5) contributors                                   -->
<!--             6) Named template: set-copyright-note             -->
<!--             7) Mode: copyright-note                           -->
<!--             8) Named template: set-correspondence-note        -->
<!--             9) Mode: correspondence-note                      -->
<!--            10) Named template: set-affiliation-note           -->
<!--            11) Mode: affiliation-number                       -->
<!--            12) affiliation                                    -->
<!--            13) Suppress source affiliation numbering          -->
<!--            14) abstract                                       -->
<!--            15) Elements processed in no-mode                  -->
<!--            16) Elements suppressed in no-mode                 -->
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
    extension-element-prefixes="m" xmlns:axf="http://www.antennahouse.com/names/XSL/Extensions">


    <!-- ============================================================= -->
    <!-- NAMED TEMPLATE: SET-ARTICLE-OPENER (NO MODE)                  -->
    <!-- ============================================================= -->


    <!-- Publication order of article-meta notes:
        - copyright note, unnumbered
        - corresponding author (0 or more, in 1 block, unnumbered)
        - affiliations (0 or more), the note unnumbered but with 
          internal alpha numbering linking aff to contrib.
        - author-notes/fn (0 or more), marked with daggers.
  -->

    <xsl:template name="set-article-opener">

        <xsl:variable name="line-numbers">
            <xsl:choose>
                <xsl:when test="$type=$proof_type">
                    <xsl:text>show</xsl:text>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:text>none</xsl:text>
                </xsl:otherwise>
            </xsl:choose>
        </xsl:variable>
        <fo:block>

            <!-- change context node -->
            <xsl:for-each select="/article/front/article-meta">

                <xsl:call-template name="set-copyright-note"/>
                <xsl:call-template name="set-correspondence-note"/>
                <xsl:call-template name="set-affiliations-note"/>

                <xsl:apply-templates select="title-group/article-title"/>
                <xsl:apply-templates select="contrib-group"/>
                <xsl:apply-templates select="aff"/>
                <xsl:apply-templates select="author-notes"/>
                <xsl:apply-templates select="contrib-group/contrib/email"/>
                <xsl:call-template name="journalTitle"/>
                <xsl:apply-templates select="abstract[not(@abstract-type='toc')]"/>

                <!-- then a rule before the article body -->
                <!--fo:block space-before="8pt"
              border-bottom-style="solid"
              border-bottom-width="0.5pt"
              margin-left="{$mainindent}pc"
              margin-right="{$mainindent}pc"/-->
                <!-- SA00 No need for border -->

            </xsl:for-each>

        </fo:block>
        <fo:block text-align="start" span="all">
            <fo:leader leader-pattern="rule" rule-thickness="0.5pt" leader-length="180mm"/>
        </fo:block>

        <!-- SA00 extra space after horizontal rule -->
        <fo:block text-align="start" span="all" space-before="10pt">
            <xsl:text>&#x0a;</xsl:text>
        </fo:block>

    </xsl:template>

    <xsl:template name="journalTitle">
        <!--    <fo:block space-before="10pt" line-height="{$textleading}pt" text-align="left"
      start-indent="{$mainindent}pc" span="all" font-size="{$textsize +2}pt"
      font-family="{$textfont}">
      <xsl:text>Published in: </xsl:text>
      <fo:wrapper font-style="italic">
        <xsl:value-of select="//journal-meta/journal-title-group/journal-title"/>
      </fo:wrapper>
      <xsl:text>; Received on: </xsl:text>
      <xsl:value-of select="//history/date[@date-type='received']"/>
      <xsl:text>; Accepted on: </xsl:text>
      <xsl:value-of select="//history/date[@date-type='accepted']"/>
    </fo:block>
-->
    </xsl:template>

    <!-- ============================================================= -->
    <!-- NAMED TEMPLATE: SET-ARTICLE-BODY (NO MODE)                    -->
    <!-- ============================================================= -->


    <xsl:template name="set-article-body">

        <xsl:variable name="line-numbers">
            <xsl:choose>
                <xsl:when test="$type=$proof_type">
                    <xsl:text>show</xsl:text>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:text>none</xsl:text>
                </xsl:otherwise>
            </xsl:choose>
        </xsl:variable>

        <fo:block space-before="0pt" space-after="0pt" start-indent="{$mainindent}pc"
            text-align="justify" font-size="{$textsize}pt" line-height="{$textleading}pt">

            <!-- This fo:wrapper establishes the default text setting
         for the entire article/body. -->
            <!--fo:wrapper font-family="{$textfont}" font-size="{$textsize}pt" line-height="{$textleading}pt"
        font-style="normal" font-weight="normal" text-align="justify"-->
            <!-- SA00 body of doc -->
            <xsl:apply-templates select="/article/body"/>
            <!--/fo:wrapper-->

            <xsl:apply-templates select="/article/front/article-meta/contrib-group/contrib/bio"/>
            <!--      <xsl:apply-templates select="/article/front/article-meta/contrib-group/contrib/bio[2]"/>
      <xsl:apply-templates select="/article/front/article-meta/contrib-group/contrib/bio[3]"/>
-->
        </fo:block>
    </xsl:template>


    <!-- ============================================================= -->
    <!-- NAMED TEMPLATE: SET-ARTICLE-BACK (NO MODE)                    -->
    <!-- ============================================================= -->


    <xsl:template name="set-article-back">

        <xsl:param name="pass">first</xsl:param>

        <xsl:variable name="line-numbers">
            <xsl:choose>
                <xsl:when test="$type=$proof_type">
                    <xsl:text>show</xsl:text>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:text>none</xsl:text>
                </xsl:otherwise>
            </xsl:choose>
        </xsl:variable>


        <!-- This fo:block and fo:wrapper establishes the default 
         text setting for the entire backmatter. -->

        <fo:block space-before="0pt" space-after="0pt" start-indent="0pc">

            <fo:wrapper font-family="{$textfont}" font-size="{$textsize}pt"
                line-height="{$textleading}pt" font-style="normal" font-weight="normal"
                text-align="justify">

                <xsl:choose>
                    <xsl:when test="$pass='first'">
                        <xsl:apply-templates select="/article/back/(* except (ref-list, app-group))"
                        />
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:if test=".//ref-list[@content-type='nextColumn']">
                            <fo:block space-before="20pt" break-after="column">&#xA0;</fo:block>
                        </xsl:if>
                        <xsl:if test=".//ref-list[@content-type='nextPage']">
                            <fo:block space-before="20pt" break-after="page">&#xA0;</fo:block>
                        </xsl:if>
                        <xsl:apply-templates select="/article/back/ref-list"/>
                        <xsl:apply-templates select="/article/back/app-group"/>
                        <xsl:apply-templates select="/article/back/comment()"/>
                    </xsl:otherwise>
                </xsl:choose>

            </fo:wrapper>

        </fo:block>
    </xsl:template>


    <!-- ============================================================= -->
    <!-- THE REST IS ALL SUPPORT FOR SET-ARTICLE-OPENER                -->
    <!-- ============================================================= -->



    <!-- ============================================================= -->
    <!-- ARTICLE TITLE on ARTICLE FIRST PAGE                           -->
    <!-- ============================================================= -->

    <xsl:template match="/article/front/article-meta/title-group/article-title">
        <fo:table-and-caption span="all">
            <fo:table>
                <fo:table-column column-width="110mm"/>
                <fo:table-column column-width="70mm"/>
                <fo:table-body>
                    <fo:table-row>
                        <fo:table-cell display-align="after">
                            <fo:block font-size="10pt" text-align="left" font-family="Arial">
                                <xsl:value-of select="//journal-meta//journal-title"/>
                            </fo:block>
                            <fo:block font-size="10pt" text-align="left" font-family="Arial"
                                padding-top="20pt" font-style="italic">
                                <xsl:choose>
                                    <xsl:when
                                        test="$special-issue = 'true' and /processing-instruction('lmd_is_special_issue') = 'yes'">
                                        <xsl:value-of select="$issue-title"/>
                                    </xsl:when>
                                    <xsl:when test="//article-categories/subj-group/subject">
                                        <xsl:value-of
                                            select="//article-categories/subj-group/subject"/>
                                    </xsl:when>
                                    <xsl:otherwise>
                                        <xsl:text>Research Article</xsl:text>
                                    </xsl:otherwise>
                                </xsl:choose>
                            </fo:block>
                        </fo:table-cell>
                        <fo:table-cell>
                            <fo:block text-align="right">
                                <fo:external-graphic
                                    src="url(&#34;static/IET_Journals_Logo.jpg&#34;)"/>
                            </fo:block>
                        </fo:table-cell>
                    </fo:table-row>
                </fo:table-body>
            </fo:table>
        </fo:table-and-caption>
        <fo:block text-align="start" span="all">
            <fo:leader leader-pattern="rule" rule-thickness="0.5pt" leader-length="180mm"/>
        </fo:block>
        <fo:table-and-caption span="all">
            <fo:table>
                <fo:table-column column-width="140mm"/>
                <fo:table-column column-width="40mm"/>
                <fo:table-body>
                    <fo:table-row>
                        <fo:table-cell>
                            <fo:block-container overflow="visible">
                                <fo:block font-family="Arial" font-size="18pt" font-weight="bold"
                                    line-height="20pt" padding-top="8pt">
                                    <xsl:apply-templates/>
                                </fo:block>
                            </fo:block-container>
                        </fo:table-cell>
                        <fo:table-cell>
                            <fo:block line-height="9pt" font-size="7pt" text-align="left"
                                font-family="Arial" padding-top="8pt">
                                <xsl:text>ISSN </xsl:text>
                                <xsl:value-of select="//issn[@pub-type='ppub']"/>
                            </fo:block>

                            <xsl:call-template name="printDateEntry">
                                <xsl:with-param name="entryName" select="'Received on '"/>
                                <xsl:with-param name="dateElement"
                                    select="/article/front/article-meta/history/date[@date-type='received']"
                                />
                            </xsl:call-template>

                            <xsl:call-template name="printDateEntry">
                                <xsl:with-param name="entryName" select="'Revised '"/>
                                <xsl:with-param name="dateElement"
                                    select="/article/front/article-meta/history/date[@date-type='rev-recd']"
                                />
                            </xsl:call-template>

                            <xsl:call-template name="printDateEntry">
                                <xsl:with-param name="entryName" select="'Accepted on '"/>
                                <xsl:with-param name="dateElement"
                                    select="/article/front/article-meta/history/date[@date-type='accepted']"
                                />
                            </xsl:call-template>

                            <xsl:call-template name="printDateEntry">
                                <xsl:with-param name="entryName" select="'E-First on '"/>
                                <xsl:with-param name="dateElement"
                                    select="/article/front/article-meta/pub-date[@pub-type = 'epub']"
                                />
                            </xsl:call-template>



                            <fo:block line-height="9pt" font-size="7pt" text-align="left"
                                font-family="Arial">
                                <xsl:text>doi: </xsl:text>
                                <xsl:value-of select="//article-id[@pub-id-type='doi']"/>
                            </fo:block>
                            <fo:block line-height="9pt" font-size="7pt" text-align="left"
                                font-family="Arial">
                                <xsl:text>www.ietdl.org</xsl:text>
                            </fo:block>

                        </fo:table-cell>
                    </fo:table-row>
                </fo:table-body>
            </fo:table>
        </fo:table-and-caption>
    </xsl:template>

    <xsl:template name="printDateEntry">
        <xsl:param name="entryName"/>
        <xsl:param name="dateElement"/>

        <xsl:if test="$dateElement">
            <fo:block line-height="9pt" font-size="7pt" text-align="left" font-family="Arial">
                <xsl:value-of select="$entryName"/>
                <xsl:call-template name="serializeDateElement">
                    <xsl:with-param name="dateElement" select="$dateElement"/>
                </xsl:call-template>
            </fo:block>
        </xsl:if>
    </xsl:template>

    <xsl:template name="serializeDateElement">
        <xsl:param name="dateElement"/>
        <xsl:call-template name="doDay">
            <xsl:with-param name="day">
                <xsl:value-of select="$dateElement/day"/>
            </xsl:with-param>
        </xsl:call-template>
        <xsl:call-template name="doMonth">
            <xsl:with-param name="month">
                <xsl:value-of select="$dateElement/month"/>
            </xsl:with-param>
        </xsl:call-template>
        <xsl:value-of select="$dateElement/year"/>
    </xsl:template>

    <xsl:template name="doDay">
        <xsl:param name="day">1</xsl:param>
        <xsl:variable name="lastDigit">
            <xsl:value-of select="number(replace($day, '.?(.)$', '$1'))"/>
        </xsl:variable>
        <xsl:value-of select="$day"/>
        <xsl:choose>
            <xsl:when test="number($day) >= 10 and number($day) &lt; 21">
                <xsl:text>th </xsl:text>
            </xsl:when>
            <xsl:when test="number($lastDigit)=1">
                <xsl:text>st </xsl:text>
            </xsl:when>
            <xsl:when test="number($lastDigit)=2">
                <xsl:text>nd </xsl:text>
            </xsl:when>
            <xsl:when test="number($lastDigit)=3">
                <xsl:text>rd </xsl:text>
            </xsl:when>
            <xsl:otherwise>
                <xsl:text>th </xsl:text>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

    <xsl:template name="doMonth">
        <xsl:param name="month">1</xsl:param>
        <xsl:choose>
            <xsl:when test="number($month)=1">
                <xsl:text>January </xsl:text>
            </xsl:when>
            <xsl:when test="number($month)=2">
                <xsl:text>February </xsl:text>
            </xsl:when>
            <xsl:when test="number($month)=3">
                <xsl:text>March </xsl:text>
            </xsl:when>
            <xsl:when test="number($month)=4">
                <xsl:text>April </xsl:text>
            </xsl:when>
            <xsl:when test="number($month)=5">
                <xsl:text>May </xsl:text>
            </xsl:when>
            <xsl:when test="number($month)=6">
                <xsl:text>June </xsl:text>
            </xsl:when>
            <xsl:when test="number($month)=7">
                <xsl:text>July </xsl:text>
            </xsl:when>
            <xsl:when test="number($month)=8">
                <xsl:text>August </xsl:text>
            </xsl:when>
            <xsl:when test="number($month)=9">
                <xsl:text>September </xsl:text>
            </xsl:when>
            <xsl:when test="number($month)=10">
                <xsl:text>October </xsl:text>
            </xsl:when>
            <xsl:when test="number($month)=11">
                <xsl:text>November </xsl:text>
            </xsl:when>
            <xsl:when test="number($month)=12">
                <xsl:text>December </xsl:text>
            </xsl:when>
        </xsl:choose>
    </xsl:template>
    <!-- ============================================================= -->
    <!-- ARTICLE CONTRIBUTOR                                           -->
    <!-- ============================================================= -->

    <!-- SA00:contrib -->
    <xsl:template match="/article/front/article-meta/contrib-group">

        <fo:block line-height="13pt" text-align="left" start-indent="{$mainindent}pc"
            space-before="4pt" span="all">
            <xsl:for-each select="contrib">
                <fo:wrapper font-style="italic" font-size="11pt" font-family="Arial">
                    <!-- SA00 Author name before abstract -->

                    <!-- probably it's a name, but perhaps it's a collab.
           Assumes that a contrib contains EITHER one name
           OR one collab. -->

                    <!-- change context for convenience -->
                    <!-- set the name -->
                    <xsl:for-each select="name | collab">

                        <xsl:if test="given-names">
                            <xsl:apply-templates select="given-names" mode="pass-through"/>
                            <xsl:text> </xsl:text>
                        </xsl:if>

                        <xsl:apply-templates select="surname" mode="pass-through"/>

                        <xsl:if test="suffix">
                            <xsl:text> </xsl:text>
                            <xsl:apply-templates select="suffix" mode="pass-through"/>
                        </xsl:if>

                        <xsl:apply-templates select="collab" mode="pass-through"/>

                    </xsl:for-each>

                    <!-- Now the role. -->
                    <!-- There are three pieces of role information: role,
           @contrib-type, and on-behalf-of. The xsl:choose expresses
           the preferences for which information to use. -->

                    <xsl:choose>

                        <!-- if we have an on-behalf-of with a role -->
                        <xsl:when test="role and on-behalf-of">
                            <xsl:text> [</xsl:text>
                            <xsl:apply-templates select="role" mode="pass-through"/>
                            <xsl:text> on behalf of </xsl:text>
                            <xsl:apply-templates select="on-behalf-of" mode="pass-through"/>
                            <xsl:text>]</xsl:text>
                        </xsl:when>

                        <!-- if an on-behalf-of with an @contrib-type that's not an author -->
                        <xsl:when test="on-behalf-of and not(@contrib-type='author')">
                            <xsl:text> [</xsl:text>
                            <xsl:value-of select="@contrib-type"/>
                            <xsl:text> on behalf of </xsl:text>
                            <xsl:apply-templates select="on-behalf-of" mode="pass-through"/>
                            <xsl:text>]</xsl:text>
                        </xsl:when>

                        <!-- if there's a role -->
                        <xsl:when test="role">
                            <xsl:text> [</xsl:text>
                            <xsl:apply-templates select="role" mode="pass-through"/>
                            <xsl:text>]</xsl:text>
                        </xsl:when>

                        <!-- if there's an @contrib-type -->
                        <xsl:when test="@contrib-type[not('author')]">
                            <xsl:text> [</xsl:text>
                            <xsl:value-of select="@contrib-type"/>
                            <xsl:text>]</xsl:text>
                        </xsl:when>

                        <!-- if there's an on-behalf of (but no @contrib-type or role) -->
                        <xsl:when test="on-behalf-of">
                            <xsl:text> [on behalf of </xsl:text>
                            <xsl:apply-templates select="on-behalf-of" mode="pass-through"/>
                            <xsl:text>]</xsl:text>
                        </xsl:when>

                    </xsl:choose>

        <xsl:variable name="affiliations-have-xrefs"
              select="boolean(/article/front/article-meta//xref[ancestor::article-meta][@ref-type='aff'])"/>

                    <!-- Finally set an affiliation reference, if any,
           using xref or aff as appropriate -->
                    <xsl:choose>
                        <xsl:when test="$affiliations-have-xrefs and xref[@ref-type='aff']">
                            <xsl:apply-templates select="xref[@ref-type='aff']"
                                mode="affiliation-number"/>
                            <xsl:apply-templates select="xref[@ref-type='author-notes']"
                                mode="affiliation-number"/>
                        </xsl:when>
                        <xsl:when test="$affiliations-have-xrefs and xref[@ref-type='author-notes']">
                            <xsl:apply-templates select="xref[@ref-type='author-notes']"
                                mode="affiliation-number"/>
                            <xsl:message>AUTHOR NOTES2</xsl:message>
                        </xsl:when>
                        <xsl:when test="aff">
                            <xsl:apply-templates select="aff" mode="affiliation-number"/>
                        </xsl:when>
                        <xsl:otherwise/>
                    </xsl:choose>
                    <xsl:if test="email">
                        <fo:inline baseline-shift="super">
                            <xsl:text> </xsl:text>
                            <fo:external-graphic src="url(&#34;static/com_envlope.tif&#34;)"/>
                        </fo:inline>
                    </xsl:if>
                    <xsl:if test="not(position()=last())">
                        <xsl:text>, </xsl:text>
                    </xsl:if>
                </fo:wrapper>

                <xsl:apply-templates select="processing-instruction('show')"/>

            </xsl:for-each>
        </fo:block>

    </xsl:template>


    <!-- ============================================================= -->
    <!-- NAMED TEMPLATE: SET-COPYRIGHT-NOTE (MODE copyright-note)      -->
    <!-- ============================================================= -->


    <xsl:template name="set-copyright-note">

        <!-- This note is set as a first-page footnote, 
       and has no number or other device. -->

        <!-- An fo:footnote-body which has no number or other device
       needs a block immediately surrounding it, to
       anchor it to the currently-setting page; otherwise, 
       it slides through to the last page. -->

        <!-- context node is /article/front/article-meta -->

        <xsl:if test="copyright-statement | copyright-year">

            <fo:block space-before="0pt" space-after="0pt">

                <!-- put out the note -->
                <fo:footnote>
                    <fo:inline>&#x200B;</fo:inline>
                    <fo:footnote-body>
                        <fo:block line-height="{$fnleading}pt" space-before="0pt"
                            space-after="{$leading-below-fn}">
                            <fo:wrapper font-size="{$fnsize}pt">
                                <xsl:for-each select="copyright-statement">
                                    <xsl:apply-templates mode="copyright-note"/>
                                </xsl:for-each>

                                <xsl:if test="not(copyright-statement)">
                                    <xsl:apply-templates select="copyright-year"
                                        mode="copyright-note"/>
                                </xsl:if>
                            </fo:wrapper>
                        </fo:block>
                    </fo:footnote-body>
                </fo:footnote>

            </fo:block>
        </xsl:if>

    </xsl:template>


    <!-- ============================================================= -->
    <!-- MODE: COPYRIGHT-NOTE                                          -->
    <!-- ============================================================= -->


    <xsl:template mode="copyright-note" match="copyright-statement | copyright-year">
        <xsl:apply-templates/>
    </xsl:template>


    <!-- ============================================================= -->
    <!-- NAMED TEMPLATE: SET-CORRESPONDENCE-NOTE                       -->
    <!-- ============================================================= -->


    <!-- This is set as a first-page footnote, unnumbered.
       Usually there is only 1 corresponding author; if there is 
       more than one, they're all set in this one note. -->

    <!-- context node is article-meta -->
    <xsl:template name="set-correspondence-note">

        <xsl:if test="contrib-group/contrib[@corresp='yes']">
            <!--      <fo:block space-before="0pt" space-after="0pt">
        <fo:footnote>
          <fo:inline>&#x200B;</fo:inline>

          <fo:footnote-body>

            <fo:block line-height="{$fnleading}pt" space-before="0pt"
              space-after="{$leading-below-fn}">
              <fo:wrapper font-size="{$fnsize}pt">

                <xsl:for-each select="contrib-group/contrib[@corresp='yes']">

                  <xsl:if test="not(preceding-sibling::contrib[@corresp='yes'])">
                    <xsl:text>Correspondence to: </xsl:text>
                  </xsl:if>

                  <xsl:apply-templates select="name | collab" mode="correspondence-note"/>

                  <xsl:choose>
                    <xsl:when test="email">
                      <xsl:text>, </xsl:text>
                      <fo:wrapper font-family="monospace">
                        <xsl:apply-templates select="email" mode="correspondence-note"/>
                      </fo:wrapper>
                    </xsl:when>
                    <xsl:when test="address">
                      <xsl:text>, </xsl:text>
                      <xsl:apply-templates select="address" mode="correspondence-note"/>
                    </xsl:when>
                  </xsl:choose>

                  <!-\- now post-contrib punctuation -\->
                  <xsl:choose>
                    <xsl:when test="not(following-sibling::contrib[@corresp='yes'])">
                      <xsl:text>.</xsl:text>
                    </xsl:when>
                    <xsl:otherwise>
                      <xsl:text>; </xsl:text>
                    </xsl:otherwise>
                  </xsl:choose>

                </xsl:for-each>

              </fo:wrapper>

            </fo:block>
          </fo:footnote-body>
        </fo:footnote>
      </fo:block>
-->
        </xsl:if>

    </xsl:template>


    <!-- ============================================================= -->
    <!-- MODE: CORRESPONDENCE-NOTE                                     -->
    <!-- ============================================================= -->


    <xsl:template mode="correspondence-note"
        match="collab | email | address | given-names | surname | suffix">
        <xsl:apply-templates/>
    </xsl:template>


    <xsl:template mode="correspondence-note" match="name">

        <xsl:if test="given-names">
            <xsl:apply-templates select="given-names" mode="correspondence-note"/>
            <xsl:text> </xsl:text>
        </xsl:if>

        <xsl:apply-templates select="surname" mode="correspondence-note"/>

        <xsl:if test="suffix">
            <xsl:text>, </xsl:text>
            <xsl:apply-templates select="suffix" mode="correspondence-note"/>
        </xsl:if>

    </xsl:template>



    <!-- ============================================================= -->
    <!-- NAMED TEMPLATE: SET-AFFILIATIONS-NOTE                         -->
    <!-- ============================================================= -->


    <!-- This is set as a first-page footnote. "Active" affiliations
     (either xref'd or inside contrib, whichever is the style) 
     are set here in a single note, each uniquely distinguished 
     within the note (and on the contrib) by an alpha superscript. -->


    <xsl:key name="aff-refs" match="xref[ancestor::article-meta][@ref-type='aff']" use="@rid"/>


    <xsl:template name="set-affiliations-note">

        <xsl:param name="the-key" select="concat('aff', '-refs')"/>

        <xsl:variable name="affiliations-have-xrefs"
        select="boolean(/article/front/article-meta//xref[ancestor::article-meta][@ref-type='aff'])"/>

        <xsl:if test="$affiliations-have-xrefs or /article/front/article-meta//aff">
            <!--      <fo:block>
        <fo:footnote>
          <fo:inline>&#x200B;</fo:inline>

          <fo:footnote-body>

            <fo:block line-height="{$fnleading}pt" space-before="0pt"
              space-after="{$leading-below-fn}">
              <fo:wrapper font-size="{$fnsize}pt">
                <xsl:text>Contributor affiliations: </xsl:text>

                <xsl:choose>
                  <xsl:when test="$affiliations-have-xrefs">
                    <xsl:for-each select="/article/front/article-meta//xref[@ref-type='aff']">

                      <!-\- if it's the first reference to this aff,
                       set its number and set the aff -\->
                      <xsl:if test="count(key($the-key, @rid)[1] | . ) = 1">
                        <xsl:apply-templates select="." mode="affiliation-number"/>
                        <xsl:apply-templates select="key('element-by-id', @rid)/self::aff"/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:when>

                  <xsl:otherwise>
                    <!-\- set the affiliations encountered in document order -\->
                    <xsl:for-each select="/article/front/article-meta//aff">
                      <xsl:apply-templates select="." mode="affiliation-number"/>
                      <xsl:apply-templates/>
                    </xsl:for-each>
                  </xsl:otherwise>

                </xsl:choose>

              </fo:wrapper>
            </fo:block>
          </fo:footnote-body>
        </fo:footnote>
      </fo:block>
-->
        </xsl:if>

    </xsl:template>


    <!-- ============================================================= -->
    <!-- MODE: AFFILIATION-NUMBER                                      -->
    <!-- ============================================================= -->

    <xsl:template mode="affiliation-number" match="xref[@ref-type='aff']" priority="1">

        <fo:inline baseline-shift="super" font-size="{$fnsize -1}pt">
            <xsl:for-each select="key('aff-refs', @rid)[1]">
                <xsl:number level="any" from="/" format="1"
                    count="xref[@ref-type='aff'][ancestor::article-meta]
                         [count(key('aff-refs', @rid)[1] | . ) = 1]"
                />
            </xsl:for-each>
            <!-- SA00 comma between refs -->
            <xsl:if
                test="following-sibling::xref[@ref-type='author-notes'] or
                    following-sibling::xref[@ref-type='aff']">
                <xsl:text>,</xsl:text>
            </xsl:if>
            <!-- SA00 change format -->
        </fo:inline>

    </xsl:template>

    <xsl:template mode="affiliation-number" match="xref[@ref-type='author-notes']" priority="1">
        <xsl:message>AUTH</xsl:message>

        <fo:inline baseline-shift="super" font-size="{$fnsize -1}pt">
            <xsl:apply-templates/>
            <!-- SA00 comma between refs -->
            <xsl:if test="following-sibling::xref[@ref-type='author-notes']">
                <xsl:text>,</xsl:text>
            </xsl:if>
            <!-- SA00 change format -->
        </fo:inline>

    </xsl:template>

    <xsl:template mode="affiliation-number" match="aff">
        <fo:inline baseline-shift="super" font-size="{$fnsize -1}pt">
            <xsl:number count="article-meta//aff" level="any" from="/" format="a"/>
        </fo:inline>

    </xsl:template>


    <!-- ============================================================= -->
    <!-- AFFILIATION AND ITS CONTENT IN NO-MODE                        -->
    <!-- ============================================================= -->

    <xsl:template match="aff">
        <fo:block line-height="10pt" text-align="left" start-indent="{$mainindent}pc" span="all">
            <fo:wrapper font-style="italic" font-size="8pt" line-height="10pt" font-family="Arial">
                <xsl:if test="not(label)">
                    <fo:inline baseline-shift="super" font-size="6pt">
                        <xsl:value-of select="substring-after(@id, 'af')"/>
                    </fo:inline>
                </xsl:if>
                <xsl:apply-templates/>
            </fo:wrapper>
        </fo:block>
    </xsl:template>

    <xsl:template match="author-notes">
        <fo:block line-height="10pt" text-align="left" start-indent="{$mainindent}pc" span="all">
            <fo:wrapper font-style="italic" font-size="8pt" line-height="10pt" font-family="Arial">
                <xsl:apply-templates select="fn/label/node()"/>
                <xsl:apply-templates select="fn/p/node()"/>
            </fo:wrapper>
        </fo:block>
    </xsl:template>

    <xsl:template match="aff/label">
        <fo:inline baseline-shift="super" font-size="6pt">
            <xsl:value-of select="."/>
        </fo:inline>
    </xsl:template>

    <xsl:template match="aff/institution | aff/country">
        <xsl:apply-templates/>
        <!--xsl:choose>
        <xsl:when test="not(following-sibling::*)">
          <xsl:text>.</xsl:text>
        </xsl:when>
        <xsl:otherwise>
          <xsl:text>, </xsl:text>
        </xsl:otherwise>
      </xsl:choose-->
    </xsl:template>

    <xsl:template match="aff/addr-line">
        <xsl:apply-templates/>
    </xsl:template>

    <xsl:template match="contrib/email">
        <fo:block line-height="10pt" text-align="left" start-indent="{$mainindent}pc"
            space-before="1pt" span="all">
            <fo:wrapper font-style="italic" font-size="8pt" font-family="Arial">
                <fo:external-graphic src="url(&#34;static/com_envlope.tif&#34;)"/>
                <xsl:text> E-mail: </xsl:text>
                <xsl:apply-templates/>
            </fo:wrapper>
        </fo:block>
    </xsl:template>

    <!-- ============================================================= -->
    <!-- SUPPRESS SOURCE DOCUMENT'S AFFILIATION NUMBERING              -->
    <!-- ============================================================= -->


    <!-- can't rely on the original numbers being accurate
     for this design's numbering scheme, so suppress them
     insofar as we can identify them. -->

    <xsl:template match="aff/child::node()[1][self::bold]"/>


    <!-- ============================================================= -->
    <!-- ARTICLE ABSTRACT                                              -->
    <!-- ============================================================= -->


    <xsl:template match="article-meta" mode="#all" priority="100">
        <xsl:message>META</xsl:message>
        <xsl:apply-templates/>
    </xsl:template>

    <xsl:template match="article-meta/abstract[not(@abstract-type='toc')]">

        <!--fo:block space-before="12pt"
            space-after="{$leading-below-titles-small}"
            line-height="{$textleading}pt"
            text-align="justify">
    <fo:wrapper font-family="{$titlefont}"
                font-size="10pt"
                font-weight="bold">
      <xsl:text>Abstract</xsl:text>
    </fo:wrapper>
  </fo:block-->
        <!-- SA00 don't use Abstract head -->

        <!-- SA00:abstract paragraph -->
        <fo:block font-size="9pt" text-align="justify" space-before="10pt" font-family="Arial"
            span="all">
            <xsl:apply-templates/>
        </fo:block>

        <!-- SA00 rule under abstract -->
        <!--    <fo:block text-align="start" span="all">
      <fo:leader leader-pattern="rule" rule-thickness="0.5pt" leader-length="180mm"/>
    </fo:block>
-->

        <!-- SA00 extra space after horizontal rule -->
        <!--   <fo:block text-align="start" span="all" space-before="10pt">
      <xsl:text>&#x0a;</xsl:text>
    </fo:block>
-->

    </xsl:template>


    <!-- ============================================================= -->
    <!-- ELEMENTS PROCESSED SIMPLY IN NO-MODE                          -->
    <!-- ============================================================= -->

    <!-- These elements are listed here only to help a developer 
     be sure of what's happening. -->

    <!-- journal meta -->
    <xsl:template
        match="journal-meta/abbrev-journal-title
                   | journal-meta/journal-title
                   | journal-meta/issn
                   | journal-meta/publisher/publisher-name
                   | journal-meta/publisher/publisher-loc
                   | article-meta/copyright-statement
                   | article-meta/copyright-year">
        <xsl:apply-templates/>
    </xsl:template>

    <!-- article-meta -->
    <xsl:template
        match="article-meta/volume
                   | article-meta/issue
                   | article-meta/fpage
                   | article-meta/lpage
                   | article-meta/self-uri
                   | article-meta/article-id
                   | article/body
                   | article-meta/subj-group/subject">
        <xsl:apply-templates/>
    </xsl:template>


    <!-- ============================================================= -->
    <!-- ELEMENTS SUPPRESSED IN NO-MODE                                -->
    <!-- ============================================================= -->

    <!-- The content of these elements is suppressed when walking the
     document tree (though some elements -are- used by a mode in a 
     select, e.g., email for a corresponding contributor).         -->

    <xsl:template match="title-group/trans-title 
                   | title-group/alt-title"/>

    <xsl:template
        match="article-meta/ext-link
                   | article-meta/elocation-id
                   | article-meta/product
                   | article-meta/supplementary-material
                   | article-meta/trans-abstract
                   | article-meta/contract-num
                   | article-meta/contract-sponsor
                   | article-meta/counts
                   | article-meta/author-notes/label
                   | article-meta/author-notes/title"/>

    <!-- loose information in article-meta/contrib-group -->
    <xsl:template
        match="article-meta/contrib-group/address 
                   | article-meta/contrib-group/author-comment 
                   | article-meta/contrib-group/biozz 
                   | article-meta/contrib-group/email 
                   | article-meta/contrib-group/ext-link 
                   | article-meta/contrib-group/on-behalf-of 
                   | article-meta/contrib-group/role 
                   | article-meta/contrib-group/xref"/>

    <!-- unused information in (any) contrib -->
    <xsl:template
        match="contrib/address
                   | contrib/author-comment
                   | contrib/degrees
                   | contrib/ext-link"/>

    <xsl:template match="alt-text | ext-link | long-desc"/>


</xsl:transform>
