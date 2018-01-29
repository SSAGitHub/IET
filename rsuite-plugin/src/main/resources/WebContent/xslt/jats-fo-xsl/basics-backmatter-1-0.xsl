<?xml version="1.0" encoding="utf-8"?>
<!-- ============================================================= -->
<!--  MODULE:    backmatter-1-0.xsl                                -->
<!--  VERSION:   1.0                                               -->
<!--  DATE:      September 2004                                    -->
<!--                                                               -->
<!-- ============================================================= -->

<!-- ============================================================= -->
<!-- SYSTEM:     Archiving and Interchange DTD Suite               -->
<!--             Journal Article Formatting                        -->
<!--                                                               -->
<!-- PURPOSE:    A module of article-fo.xsl that handles the       -->
<!--             grouping components such as appendix and bio      -->
<!--             *in backmatter*. (Some of these elements, such    -->
<!--             as reflist, may also be used in the article body, -->
<!--             where their formatting may be different than in   -->
<!--             backmatter.)                                      -->
<!--                                                               -->
<!-- CONTAINS:   Templates for:                                    -->
<!--             1) Acknowledgements (ack)                         -->
<!--             2) Appendix Group and Appendix (app-group, app)   -->
<!--             3) Biography (bio)                                -->
<!--             4) Footnote Group (fn-group)                      -->
<!--             5) Glossary (glossary)                            -->
<!--             6) Notes (notes)                                  -->
<!--             7) Reference list (ref-list)                      -->
<!--             8) Section (of backmatter)                        -->
<!--             9) Article response and sub-article               -->
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
    <!-- WHAT BACK CAN CONTAIN                                         -->
    <!-- ============================================================= -->

    <!-- back contains an optional title, not handled by this
     formatting, followed by any combination of:
     
     Acknowledgments <ack> 
     Appendix Matter <app-group> 
     Biography <bio> 
     Footnote Group <fn-group> 
     Glossary Elements List <glossary> 
     Notes <notes> 
     Reference List (Bibliographic Reference List) <ref-list> 
     Section <sec> 
     
     Each of these is treated as a top-level section within back,
     and formatted in the order encountered. -->


    <xsl:template match="processing-instruction('show')">
        <xsl:if test="$type=$proof_type">
            <xsl:variable name="show" select="substring-before(substring-after(.,'ID='), ']')"/>
            <fo:float axf:float="start">
                <fo:block color="blue" font-size="8pt" margin-left="-15pt">
                    <fo:basic-link internal-destination="{$show}">
                        <xsl:value-of select="$show"/>
                    </fo:basic-link>
                </fo:block>
            </fo:float>
        </xsl:if>


    </xsl:template>

    <xsl:template match="comment()">
        <xsl:if test="$type=$proof_type">
            <fo:table-and-caption span="all">
                <fo:table>
                    <fo:table-column column-width="170mm"/>
                    <fo:table-column column-width="10mm"/>
                    <fo:table-body keep-together.within-column="always">
                        <fo:table-row>
                            <fo:table-cell>
                                <fo:block font-size="14pt" span="all" space-after="10pt">
                                    <xsl:value-of
                                        select="translate(/article//article-id[@pub-id-type='publisher-id'], '.', '')"
                                    />
                                </fo:block>
                                <fo:block font-size="9pt" font-style="italic" span="all"
                                    space-after="10pt">
                                    <xsl:text>Author Queries</xsl:text>
                                </fo:block>
                                <fo:block-container overflow="visible">
                                    <xsl:variable name="tokenizedQueries" select="tokenize(.,'&lt;')"/>
                                    <xsl:for-each select="$tokenizedQueries">
                                        <xsl:if test="contains(., 'aq id')">
                                            <xsl:variable name="qValue"
                                                select="substring-before(substring-after(.,'id=&quot;'), '&quot;')"/>
                                            
                                            <fo:list-block id="{$qValue}" span="all">
                                                <fo:list-item>
                                                    <fo:list-item-label>
                                                        <fo:block color="blue">
                                                            <xsl:value-of select="$qValue"/>
                                                        </fo:block>
                                                    </fo:list-item-label>
                                                    <fo:list-item-body start-indent="4em">
                                                        <fo:block>
                                                            <xsl:value-of select="substring-after(.,'>')"/>
                                                        </fo:block>
                                                    </fo:list-item-body>
                                                </fo:list-item>
                                            </fo:list-block>
                                        </xsl:if>
                                    </xsl:for-each>
                                </fo:block-container>
                            </fo:table-cell>
                            <fo:table-cell>
                                <fo:block line-height="9pt" font-size="7pt" text-align="left" font-family="Arial"
                                    padding-top="8pt">&#xa0;</fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                    </fo:table-body>
                </fo:table>
            </fo:table-and-caption>

        </xsl:if>
    </xsl:template>
    <!-- ============================================================= -->
    <!-- BACK                                                          -->
    <!-- ============================================================= -->


    <xsl:template match="article/back">
        <xsl:apply-templates/>
    </xsl:template>


    <!-- ============================================================= -->
    <!-- BACK/ACK                                                      -->
    <!-- ============================================================= -->


    <!-- ack behaves like body section -->
    <xsl:template match="back/ack">

        <xsl:param name="space-before" select="$leading-around-display-blocks"/>
        <xsl:param name="space-after" select="'0pt'"/>

        <xsl:param name="title-string">
            <xsl:choose>
                <xsl:when test="title">
                    <xsl:apply-templates select="title" mode="pass-through"/>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:text>Acknowledgements</xsl:text>
                </xsl:otherwise>
            </xsl:choose>
        </xsl:param>

        <fo:block id="{@id}" space-before="{$space-before}" space-before.precedence="2"
            space-after="{$space-after}" space-after.precedence="2">
            <xsl:call-template name="block-title-style-1">
                <xsl:with-param name="title-string" select="$title-string"/>
            </xsl:call-template>

            <xsl:apply-templates select="*[not(title)]"/>

        </fo:block>

    </xsl:template>

    <xsl:template match="back/ack/label"/>

    <!-- ============================================================= -->
    <!-- APP-GROUP AND APPENDIX                                        -->
    <!-- ============================================================= -->


    <!-- app-group titles not handled; assumes the relevant content
     is the contained apps. -->

    <xsl:template match="back/app-group">

        <xsl:param name="space-before" select="$leading-around-display-blocks"/>
        <xsl:param name="space-after" select="'0pt'"/>

        <fo:block id="{@id}">
            <xsl:apply-templates select="app"/>
        </fo:block>

    </xsl:template>


    <!-- app behaves like body section -->
    <xsl:template match="app-group/app">

        <xsl:param name="space-before" select="$leading-around-display-blocks"/>
        <xsl:param name="space-after" select="'0pt'"/>

        <xsl:param name="title-string">
            <xsl:choose>
                <xsl:when test="title">
                    <xsl:apply-templates select="title" mode="pass-through"/>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:text>Appendix</xsl:text>
                </xsl:otherwise>
            </xsl:choose>
        </xsl:param>

        <fo:block id="{@id}" space-before="{$space-before}" space-before.precedence="2"
            space-after="{$space-after}" space-after.precedence="2">

            <xsl:call-template name="block-title-style-1">
                <xsl:with-param name="title-string" select="$title-string"/>
            </xsl:call-template>

            <xsl:apply-templates/>

        </fo:block>

    </xsl:template>


    <!-- ============================================================= -->
    <!-- BACK/BIO                                                      -->
    <!-- ============================================================= -->


    <!-- bio behaves like a body section -->
    <xsl:template match="app/label | app/title"/>

    <!-- ============================================================= -->
    <!-- BACK/BIO                                                      -->
    <!-- ============================================================= -->


    <!-- bio behaves like a body section -->
    <xsl:template match="back/bio">

        <xsl:param name="space-before" select="$leading-around-display-blocks"/>
        <xsl:param name="space-after" select="'0pt'"/>

        <xsl:param name="title-string">
            <xsl:choose>
                <xsl:when test="title">
                    <xsl:apply-templates select="title" mode="pass-through"/>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:text>Biography</xsl:text>
                </xsl:otherwise>
            </xsl:choose>
        </xsl:param>

        <fo:block id="{@id}" space-before="{$space-before}" space-before.precedence="2"
            space-after="{$space-after}" space-after.precedence="2">

            <xsl:call-template name="block-title-style-1">
                <xsl:with-param name="title-string" select="$title-string"/>
            </xsl:call-template>

            <xsl:apply-templates select="*[not(title)]"/>

        </fo:block>
    </xsl:template>


    <!-- ============================================================= -->
    <!-- BACK/FN-GROUP                                                 -->
    <!-- ============================================================= -->


    <!-- Footnotes are displayed on the page where they are
     referenced, or if xrefs are not used in the document
     then they are displayed on the body page where they 
     appear. They are not put out in the back-matter. -->

    <xsl:template match="back/fn-group"/>


    <!-- ============================================================= -->
    <!-- BACK/GLOSSARY                                                 -->
    <!-- ============================================================= -->


    <!-- back/glossary behaves like a body section -->
    <xsl:template match="back/glossary">

        <xsl:param name="space-before" select="$leading-around-display-blocks"/>
        <xsl:param name="space-after" select="'0pt'"/>

        <xsl:param name="title-string">
            <xsl:choose>
                <xsl:when test="title">
                    <xsl:apply-templates select="title" mode="pass-through"/>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:text>Glossary</xsl:text>
                </xsl:otherwise>
            </xsl:choose>
        </xsl:param>

        <fo:block id="{@id}" space-before="{$space-before}" space-before.precedence="2"
            space-after="{$space-after}" space-after.precedence="2">

            <xsl:call-template name="block-title-style-1">
                <xsl:with-param name="title-string" select="$title-string"/>
            </xsl:call-template>

            <xsl:apply-templates select="*[not(title)]"/>

        </fo:block>

    </xsl:template>



    <!-- ============================================================= -->
    <!-- BACK/NOTES                                                    -->
    <!-- ============================================================= -->


    <!-- back/notes behaves like a body section -->
    <xsl:template match="back/notes">

        <xsl:param name="space-before" select="$leading-around-display-blocks"/>
        <xsl:param name="space-after" select="'0pt'"/>

        <xsl:param name="title-string">
            <xsl:choose>
                <xsl:when test="title">
                    <xsl:apply-templates select="title" mode="pass-through"/>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:text>Notes</xsl:text>
                </xsl:otherwise>
            </xsl:choose>
        </xsl:param>

        <fo:block id="{@id}" space-before="{$space-before}" space-before.precedence="2"
            space-after="{$space-after}" space-after.precedence="2">

            <xsl:call-template name="block-title-style-1">
                <xsl:with-param name="title-string" select="$title-string"/>
            </xsl:call-template>

            <xsl:apply-templates select="*[not(title)]"/>

        </fo:block>

    </xsl:template>


    <!-- ============================================================= -->
    <!-- BACK/REF-LIST                                                 -->
    <!-- ============================================================= -->

    <!-- A ref-list in back behaves the same as anywhere else.
     See the module for ref-list.                                  -->


    <!-- ============================================================= -->
    <!-- BACK/SEC                                                      -->
    <!-- ============================================================= -->


    <!-- back/sec behaves like a body section.
     Title is required by DTD. -->
    <xsl:template match="back/sec" priority="10">

        <xsl:param name="space-before" select="$leading-around-display-blocks"/>
        <xsl:param name="space-after" select="'0pt'"/>

        <fo:block id="{@id}" space-before="{$space-before}" space-before.precedence="2"
            space-after="{$space-after}" space-after.precedence="2">

            <xsl:apply-templates/>
        </fo:block>

    </xsl:template>


    <!-- ============================================================= -->
    <!-- RESPONSE AND SUB-ARTICLE                                      -->
    <!-- ============================================================= -->

    <!-- The present formatting does not handle article responses
     and sub-articles (both of which follow article/back). -->

    <xsl:template match="article/response | article/subarticle"/>


    <!-- ============================================================= -->
    <!-- SUPPRESS TITLES OF BACKMATTER COMPONENTS                      -->
    <!-- ============================================================= -->

    <!-- These titles if present are all passed to 
     the named template block-title-styles-1 
     in mode="pass-through"                                        -->

    <xsl:template
        match="back/ack/title
                   | back/app/title
                   | back/bio/title
                   | back/glossary/title
                   | back/notes/title"
        priority="1"/>


</xsl:transform>
