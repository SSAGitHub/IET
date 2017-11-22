<?xml version="1.0" encoding="utf-8"?>
<!-- ============================================================= -->
<!--  MODULE:    basics-inlines-1-0.xsl                            -->
<!--  VERSION:   1.0                                               -->
<!--  DATE:      September 2004                                    -->
<!--                                                               -->
<!-- ============================================================= -->

<!-- ============================================================= -->
<!-- SYSTEM:     Archiving and Interchange DTD Suite               -->
<!--             Journal Article Formatting                        -->
<!--                                                               -->
<!-- PURPOSE:    A module of article-fo.xsl that handles           -->
<!--             inline elements such as italic and overline.      -->
<!--                                                               -->
<!-- CONTAINS:   Templates for:                                    -->
<!--             1) bold, italic, smallcap, monospace              -->
<!--             2) overline, underline, strikethrough             -->
<!--             3) superscript, subscript                         -->
<!--             4) private-char, glyph-data, glyph-ref            -->
<!--             5) inline-graphic, inline-formula                 -->
<!--             6) abbreviation, named-content                    -->
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
    xmlns:axf="http://www.antennahouse.com/names/XSL/Extensions"
    xmlns:xs="http://www.w3.org/2001/XMLSchema"
    xmlns:AHtree="http://www.antennahouse.com/names/XSL/AreaTree"
    xmlns:local="http://www.localFuntion">


    <!-- This fragment treats both the "regular" inline elements and 
     those which might equally be considered display objects, 
     such as inline graphic. 
     
     The referencing elements - primarily xref - are treated in a
     separate fragment                                             -->


    <!-- ============================================================= -->
    <!-- BOLD, ITALIC, SMALLCAP, AND MONOSPACE                         -->
    <!-- ============================================================= -->


    <xsl:template match="bold">
        <fo:wrapper font-weight="bold">
            <xsl:apply-templates/>
        </fo:wrapper>
    </xsl:template>


    <xsl:template match="italic">
        <fo:wrapper font-style="italic">
            <xsl:apply-templates/>
        </fo:wrapper>
    </xsl:template>


    <xsl:template match="monospace">
        <fo:wrapper font-family="monospace" font-size="{$textsize -1}pt">
            <xsl:apply-templates/>
        </fo:wrapper>
    </xsl:template>


    <xsl:template match="sc">
        <fo:wrapper text-transform="uppercase" font-size="{$textsize -2}pt">
            <xsl:apply-templates/>
        </fo:wrapper>
    </xsl:template>


    <!-- ============================================================= -->
    <!-- OVERLINE, UNDERLINE, STRIKE-THROUGH                           -->
    <!-- ============================================================= -->


    <xsl:template match="overline">
        <fo:wrapper text-decoration="overline">
            <xsl:apply-templates/>
        </fo:wrapper>
    </xsl:template>


    <xsl:template match="underline">
        <fo:wrapper text-decoration="underline">
            <xsl:apply-templates/>
        </fo:wrapper>
    </xsl:template>


    <xsl:template match="strike">
        <fo:wrapper text-decoration="line-through">
            <xsl:apply-templates/>
        </fo:wrapper>
    </xsl:template>


    <!-- ============================================================= -->
    <!-- SUPERSCRIPT AND SUBSCRIPT                                     -->
    <!-- ============================================================= -->


    <!-- Use fo:inline for sup and sub, not fo:wrapper, because
     fo:wrapper isn't meant to handle a change to baseline.        -->


    <xsl:template match="sup">
        <fo:inline baseline-shift="super" font-size="{$textsize -2}pt">
            <xsl:apply-templates/>
        </fo:inline>
    </xsl:template>


    <xsl:template match="sub">
        <fo:inline baseline-shift="sub" font-size="{$textsize -2}pt">
            <xsl:apply-templates/>
        </fo:inline>
    </xsl:template>


    <!-- ============================================================= -->
    <!-- PRIVATE-CHAR, GLYPH-DATA, GLYPH-REF                           -->
    <!-- ============================================================= -->


    <!-- By design: If there are graphics, display'em.
     Otherwise, just show @name. -->

    <xsl:template match="private-char">
        <xsl:choose>
            <xsl:when test="inline-graphic">
                <xsl:apply-templates/>
            </xsl:when>
            <xsl:otherwise>
                <fo:wrapper>
                    <xsl:text>[</xsl:text>
                    <xsl:value-of select="@name"/>
                    <xsl:text>]</xsl:text>
                </fo:wrapper>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>


    <xsl:template match="glyph-data | glyph-ref"/>


    <!-- ============================================================= -->
    <!-- INLINE-GRAPHIC AND INLINE-FORMULA                             -->
    <!-- ============================================================= -->


    <xsl:template match="inline-graphic">
        <fo:external-graphic src="url({@xlink:href})"/>
    </xsl:template>

    <!-- suppress alt-text if present -->
    <xsl:template match="inline-graphic/alt-text"/>


    <!-- inline formula -->

    <!-- math inside inline-formula -->

    <xsl:template match="inline-formula[alternatives/mml:math]" priority="10">
        <xsl:variable name="equation" select="alternatives/mml:math" as="element()"/>
        <xsl:variable name="preceding-equations" select="count(preceding::inline-formula)" as="xs:integer"/>
        <xsl:choose>
            <xsl:when test="$equation//*[@style='split']">
                <xsl:for-each select="0 to count($equation//*[@style='split'])">
                    <xsl:message>loop: <xsl:value-of select="count($equation//*[@style='split'])"/></xsl:message>
                    <fo:instream-foreign-object content-type="application/mathml+xml">
                        <xsl:apply-templates select="$equation" mode="split-equation">
                            <xsl:with-param name="split-point" select="." as="xs:integer"
                                tunnel="yes"/>
                            <xsl:with-param name="preceding-equations" select="$preceding-equations" as="xs:integer"
                                tunnel="yes"/>
                        </xsl:apply-templates>
                    </fo:instream-foreign-object>
                    <xsl:if test="position()!=last()">
                        <fo:block/>
                    </xsl:if>
                </xsl:for-each>
            </xsl:when>
            <xsl:otherwise>
                <fo:instream-foreign-object content-type="application/mathml+xml">
                    <xsl:apply-templates select="alternatives/mml:math" mode="math"/>
                </fo:instream-foreign-object>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

    <xsl:template match="*[@style='split']" mode="split-equation" priority="10">
        <xsl:param name="split-point" tunnel="yes"/>
        <xsl:if test="$split-point = 0">
            <xsl:copy-of select="."/>
        </xsl:if>
    </xsl:template>

    <xsl:template match="@overflow" mode="split-equation"/>

    <xsl:template match="@*|node()" mode="split-equation">
        <xsl:param name="split-point" tunnel="yes"/>
        <xsl:param name="preceding-equations" tunnel="yes"/>
        <xsl:choose>
            <xsl:when test="$split-point=0">
                <xsl:if test="following::*[count(ancestor::inline-formula/preceding::inline-formula)=$preceding-equations][@style='split'] or descendant::*[count(ancestor::inline-formula/preceding::inline-formula)=$preceding-equations][@style='split']">
                    <xsl:copy>
                        <xsl:apply-templates select="@*|node()" mode="split-equation"/>
                    </xsl:copy>
                </xsl:if>
            </xsl:when>
            <xsl:otherwise>
                <xsl:if test="preceding::*[count(ancestor::inline-formula/preceding::inline-formula)=$preceding-equations][@style='split'] or descendant::*[count(ancestor::inline-formula/preceding::inline-formula)=$preceding-equations][@style='split']">
                    <xsl:copy>
                        <xsl:apply-templates select="@*|node()" mode="split-equation"/>
                    </xsl:copy>
                </xsl:if>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

    <xsl:template match="disp-formula[alternatives/mml:math]" priority="10">

        <!--   <xsl:variable name="curID" select="@id"/>
    <xsl:variable name="mathWidth"
      select="number(substring-before($areaTree//AHtree:GraphicViewportArea[@id=$curID]/AHtree:GraphicArea/@width, 'pt'))"/>
-->
        <xsl:variable name="float-reference">
            <xsl:choose>
                <xsl:when test="contains(@content-type, 'multicol')">
                    <xsl:text>multicol</xsl:text>
                </xsl:when>
                <xsl:when test="contains(@content-type, 'column')">
                    <xsl:text>column</xsl:text>
                </xsl:when>
                <xsl:when test="local:objectWidth(@id) > 244">
                    <xsl:text>multicol</xsl:text>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:text>column</xsl:text>
                </xsl:otherwise>
            </xsl:choose>
        </xsl:variable>

        <xsl:variable name="float-move">
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
        </xsl:variable>

        <xsl:variable name="float-y">
            <xsl:choose>
                <xsl:when test="contains(@content-type, 'top')">
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

        <xsl:choose>
            <xsl:when test="$float-reference = 'multicol'">
                <xsl:if
                    test="not(preceding-sibling::disp-formula[contains(@content-type, 'multicol')
          and local:objectWidth(preceding-sibling::disp-formula[1]/@id) > 244])">
                    <!--<xsl:message>ID: <xsl:value-of select="@id"/></xsl:message>-->
                    <xsl:text>(see </xsl:text>
                    <xsl:choose>
                        <xsl:when test="label">
                            <xsl:value-of select="normalize-space(label)"/>
                            <xsl:for-each
                                select="following-sibling::disp-formula[1][contains(@content-type, 'multicol') or
                local:objectWidth(@id) > 244]">
                                <xsl:text> and </xsl:text>
                                <!--<xsl:message>FOR-EACH: <xsl:value-of select="label"/></xsl:message>-->
                                <xsl:value-of select="normalize-space(label)"/>
                            </xsl:for-each>
                            <xsl:text>) </xsl:text>
                        </xsl:when>
                        <xsl:otherwise>
                            <xsl:text>equation below)</xsl:text>
                        </xsl:otherwise>
                    </xsl:choose>
                </xsl:if>
                <fo:float axf:float-reference="{$float-reference}" axf:float-x="{$float-x}"
                    axf:float-y="{$float-y}" axf:float-move="{$float-move}"
                    axf:float-margin-y="10pt" start-indent="0">
                    <fo:block text-align="start" span="all" text-indent="0pt">
                        <fo:leader leader-pattern="rule" rule-thickness="0.5pt"
                            leader-length="180mm"/>
                    </fo:block>
                    <fo:table table-layout="fixed" inline-progression-dimension="100%">
                        <fo:table-column column-width="95%"/>
                        <fo:table-column column-width="5%"/>
                        <fo:table-body>
                            <fo:table-row>
                                <fo:table-cell>
                                    <fo:block text-align="center">
                                        <fo:instream-foreign-object
                                            content-type="application/mathml+xml" id="{@id}">
                                            <xsl:apply-templates select="alternatives/mml:math"
                                                mode="mathBlock"/>
                                        </fo:instream-foreign-object>
                                    </fo:block>
                                </fo:table-cell>
                                <fo:table-cell display-align="center">
                                    <fo:block text-align="right" keep-together.within-line="always">
                                        <xsl:value-of select="label"/>
                                    </fo:block>
                                </fo:table-cell>
                            </fo:table-row>
                        </fo:table-body>
                    </fo:table>
                </fo:float>
            </xsl:when>
            <xsl:otherwise>
                <xsl:variable name="mathWidth" select="local:objectWidth(@id)"/>
                <xsl:choose>
                    <xsl:when test="$mathWidth >= 224">
                        <fo:block text-align="center" space-before="10pt">
                            <fo:instream-foreign-object content-type="application/mathml+xml"
                                id="{@id}">
                                <xsl:apply-templates select="alternatives/mml:math" mode="mathBlock"
                                />
                            </fo:instream-foreign-object>
                        </fo:block>
                        <fo:block text-align="right" keep-together.within-line="always"
                            space-after="10pt">
                            <xsl:value-of select="label"/>
                        </fo:block>
                    </xsl:when>
                    <xsl:otherwise>
                        <fo:table table-layout="fixed" inline-progression-dimension="100%"
                            space-before="10pt" space-after="10pt">
                            <fo:table-column column-width="94%"/>
                            <fo:table-column column-width="6%"/>
                            <fo:table-body>
                                <fo:table-row>
                                    <fo:table-cell>
                                        <fo:block-container background-color="white"
                                            overflow="hidden">
                                            <fo:block text-align="center">
                                                <fo:instream-foreign-object
                                                  content-type="application/mathml+xml" id="{@id}">
                                                  <xsl:apply-templates
                                                  select="alternatives/mml:math" mode="mathBlock"/>
                                                </fo:instream-foreign-object>
                                            </fo:block>
                                        </fo:block-container>
                                    </fo:table-cell>
                                    <fo:table-cell display-align="center">
                                        <fo:block text-align="right"
                                            keep-together.within-line="always">
                                            <xsl:value-of select="label"/>
                                        </fo:block>
                                    </fo:table-cell>
                                </fo:table-row>
                            </fo:table-body>
                        </fo:table>
                    </xsl:otherwise>
                </xsl:choose>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>


    <!-- ***************************** -->

    <xsl:template match="@overflow" mode="math mathBlock"/>

    <xsl:template
        match="mml:msub/mml:mrow[count(following-sibling::*)=0]/mml:mi | 
    mml:msub/mml:mrow[count(following-sibling::*)=0]/mml:mo | 
    mml:msub/mml:mrow[count(following-sibling::*)=0]/mml:mn | 
    mml:msub/mml:mrow[count(following-sibling::*)=0]/mml:ms"
        mode="math" priority="15">
        <xsl:call-template name="procMath"/>
    </xsl:template>

    <xsl:template
        match="mml:msub/mml:*[count(preceding-sibling::*)>0]
    | mml:msub/mml:*[count(preceding-sibling::*)>0]//mml:*"
        mode="math" priority="20">
        <xsl:call-template name="procMath"/>
    </xsl:template>

    <xsl:template
        match="mml:msup/mml:*[count(preceding-sibling::*)>0]
    | mml:msup/mml:*[count(preceding-sibling::*)>0]//mml:*"
        mode="math" priority="25">
        <xsl:call-template name="procMath"/>
    </xsl:template>

    <xsl:template
        match="mml:msubsup/mml:*[count(preceding-sibling::*)>0]
    | mml:msubsup/mml:*[count(preceding-sibling::*)>0]//mml:*"
        mode="math" priority="30">
        <xsl:call-template name="procMath"/>
    </xsl:template>

    <xsl:template
        match="mml:msub/mml:*[count(preceding-sibling::*)>0]
    | mml:msub/mml:*[count(preceding-sibling::*)>0]//mml:*"
        mode="mathBlock" priority="20">
        <xsl:call-template name="procMathBlock"/>
    </xsl:template>

    <xsl:template name="procMathBlock">
        <xsl:copy>
            <xsl:copy-of select="@* except @overflow"/>
            <xsl:if
                test="
            name() != 'mml:mrow' 
        and name() != 'mml:msubsup' 
        and name() != 'mml:mfenced'
        and name() != 'mml:msub'
        and name() != 'mml:msup'
        and name() != 'mml:mover'
        and name() != 'mml:msqrt'
        and name() != 'mml:mfrac'
        and name() != 'mml:mroot'
        ">
                <xsl:attribute name="fontsize">11pt</xsl:attribute>
                <!--<xsl:attribute name="color">blue</xsl:attribute>-->
            </xsl:if>
            <xsl:apply-templates mode="mathBlock"/>
        </xsl:copy>
    </xsl:template>

    <xsl:template name="procMath">
        <xsl:copy>
            <xsl:copy-of select="@* except @overflow"/>
            <xsl:if
                test="
        name() != 'mml:mrow' 
        and name() != 'mml:msubsup' 
        and name() != 'mml:mfenced'
        and name() != 'mml:msub'
        and name() != 'mml:msup'
        and name() != 'mml:mover'
        and name() != 'mml:msqrt'
        and name() != 'mml:mfrac'
        and name() != 'mml:mroot'
        and name() != 'mml:mi'
        ">
                <xsl:if test="not(@fontsize)">
                    <xsl:attribute name="fontsize">7pt</xsl:attribute>
                </xsl:if>
                <!--<xsl:attribute name="color">red</xsl:attribute>-->
            </xsl:if>
            <xsl:apply-templates mode="math"/>
        </xsl:copy>
    </xsl:template>

    <xsl:template
        match="mml:msubsup/mml:mrow[count(preceding-sibling::*)>0]//mml:* 
    | mml:msubsup/mml:*[count(preceding-sibling::*)>0]"
        mode="mathBlock" priority="15">
        <xsl:call-template name="procMathBlock"/>
    </xsl:template>

    <!-- ***************************** -->

    <xsl:template
        match="mml:msup/mml:*[count(preceding-sibling::*)>0]
    | mml:msup/mml:*[count(preceding-sibling::*)>0]//mml:* 
    | mml:msup/mml:mrow[count(preceding-sibling::*)>0]/mml:msub/mml:*"
        mode="math" priority="10">
        <xsl:call-template name="procMath"/>
    </xsl:template>

    <xsl:template
        match="mml:msup/mml:*[count(preceding-sibling::*)>0]
    | mml:msup/mml:*[count(preceding-sibling::*)>0]//mml:* 
    | mml:msup/mml:mrow[count(preceding-sibling::*)>0]/mml:msub/mml:*"
        mode="mathBlock" priority="15">
        <xsl:call-template name="procMathBlock"/>
    </xsl:template>


    <xsl:template
        match="mml:munder/mml:*[count(preceding-sibling::*)>0]
    | mml:munder/mml:*[count(preceding-sibling::*)>0]//mml:*"
        mode="mathBlock" priority="15">
        <xsl:call-template name="procMathBlock"/>
    </xsl:template>

    <xsl:template
        match="mml:munderover/mml:*[count(preceding-sibling::*)>0]
    | mml:munderover/mml:*[count(preceding-sibling::*)>0]//mml:*"
        mode="mathBlock" priority="25">
        <xsl:call-template name="procMathBlock"/>
    </xsl:template>

    <xsl:template match="mml:mfrac//mml:*" mode="mathBlock" priority="25">
        <xsl:call-template name="procMathBlock"/>
    </xsl:template>

    <!-- ***************************** -->

    <xsl:template match="mml:mover/mml:*[count(preceding-sibling::*)>0]" mode="math" priority="10">
        <xsl:call-template name="procMath"/>
    </xsl:template>

    <xsl:template match="mml:mover/mml:mo | 
    mml:mover/mml:mn | 
    mml:mover/mml:ms"
        mode="mathBlock" priority="10">
        <xsl:call-template name="procMathBlock"/>
    </xsl:template>

    <xsl:template match="mml:mo[.='^']" mode="math mathBlock" priority="35">
        <xsl:copy>
            <xsl:copy-of select="@* except @overflow"/>
            <xsl:attribute name="fontsize">5pt</xsl:attribute>
            <!--<xsl:attribute name="color">red</xsl:attribute>-->
            <xsl:apply-templates mode="#current"/>
        </xsl:copy>
    </xsl:template>

    <xsl:template match="mml:math" mode="math" priority="30">
        <xsl:copy>
            <xsl:copy-of select="@* except @overflow"/>
            <xsl:attribute name="display">inline</xsl:attribute>
            <xsl:apply-templates mode="math"/>
        </xsl:copy>
    </xsl:template>

    <xsl:template match="mml:math" mode="mathBlock" priority="30">
        <xsl:copy>
            <xsl:copy-of select="@* except @overflow"/>
            <xsl:attribute name="display">block</xsl:attribute>
            <xsl:apply-templates mode="math"/>
        </xsl:copy>
    </xsl:template>

    <!-- ***************************** -->

    <xsl:template match="@*|node()" mode="math">
        <xsl:copy>
            <xsl:apply-templates select="@*|node()" mode="math"/>
        </xsl:copy>
    </xsl:template>

    <xsl:template match="@*|node()" mode="mathBlock">
        <xsl:copy>
            <xsl:apply-templates select="@*|node()" mode="mathBlock"/>
        </xsl:copy>
    </xsl:template>

    <!-- ============================================================= -->
    <!-- ABBREVIATION AND NAMED-CONTENT                                -->
    <!-- ============================================================= -->

    <!-- Inline elements that have no formatting consequences -->

    <xsl:template match="abbreviation | named-content">
        <xsl:apply-templates/>
    </xsl:template>



    <xsl:function name="local:objectWidth">
        <xsl:param name="curID"/>
        <xsl:variable name="widths">
            <xsl:for-each select="$areaTree//AHtree:GraphicViewportArea[@id=$curID]">
                <width>
                    <xsl:value-of
                        select="number(substring-before(./AHtree:GraphicArea/@width, 'pt'))"/>
                </width>
            </xsl:for-each>
        </xsl:variable>
        <!--<xsl:value-of select="number(substring-before(($areaTree//AHtree:GraphicViewportArea[@id=$curID]/AHtree:GraphicArea)[1]/@width, 'pt'))"/>-->
        <xsl:value-of select="number(max($widths/width))"/>
    </xsl:function>
    <xsl:function name="local:tableWidth">
        <xsl:param name="curID"/>
        <xsl:value-of
            select="number(substring-before(($areaTree//AHtree:TableArea[@id=$curID])[1]/@width, 'pt'))"
        />
    </xsl:function>

    <xsl:function name="local:objectPage">
        <xsl:param name="curID"/>
        <xsl:sequence
            select="$areaTree//AHtree:GraphicViewportArea[@id=$curID]/ancestor::AHtree:PageViewportArea//AHtree:TextArea/@text='Abstract: '"
        />
    </xsl:function>

</xsl:transform>
