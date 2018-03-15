<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:df="http://dita2indesign.org/dita/functions"
    xmlns:relpath="http://dita2indesign/functions/relpath"
    xmlns:incxgen="http//dita2indesign.org/functions/incx-generation"
    xmlns:e2s="http//dita2indesign.org/functions/element-to-style-mapping"
    exclude-result-prefixes="xs df e2s relpath incxgen" version="2.0">

    <!-- IET overrides -->
    <!--
  
  Required modules:
  
  <xsl:import href="../../net.sourceforge.dita4publishers.common.xslt/xsl/lib/dita-support-lib.xsl"/>
  <xsl:import href="../../net.sourceforge.dita4publishers.common.xslt/xsl/lib/relpath_util.xsl"/>
  
  <xsl:import href="elem2styleMapper.xsl"/>
-->

    <xsl:template
        match="
            *[df:class(., 'topic/term')] |
            *[df:class(., 'topic/keyword')] |
            *[df:class(., 'topic/text')] |
            *[df:class(., 'topic/entry')] |
            *[df:class(., 'topic/cite')]
            "
        mode="block-children"> </xsl:template>

    <xsl:template match="
            *[df:class(., 'topic/xref')]
            "
        mode="block-children">
        <xsl:variable name="cStyle" select="e2s:getCStyleForElement(.)" as="xs:string"/>
        <xsl:apply-templates mode="cont">
            <!-- each <regnum> is a potential link target, so set the para to "true" so that an anchor will be output -->
            <xsl:with-param name="cStyle" select="$cStyle" tunnel="yes"/>
        </xsl:apply-templates>
    </xsl:template>


    <xsl:template match="*[df:class(., 'indexEntry-d/indexSee')]" mode="#all" priority="100">
        <xsl:variable name="cStyle" select="e2s:getCStyleForElement(.)" as="xs:string"/>
        <ParagraphStyleRange AppliedParagraphStyle="ParagraphStyle/index">
            <CharacterStyleRange AppliedCharacterStyle="CharacterStyle/{$cStyle}">
                <Content>see </Content>
            </CharacterStyleRange>
        </ParagraphStyleRange>
        <xsl:apply-templates mode="cont">
            <xsl:with-param name="cStyle" select="$cStyle" tunnel="yes"/>
        </xsl:apply-templates>
    </xsl:template>

    <xsl:template match="*[df:class(., 'indexEntry-d/indexSeeAlso')]" mode="#all" priority="100">
        <xsl:variable name="cStyle" select="e2s:getCStyleForElement(.)" as="xs:string"/>
        <ParagraphStyleRange AppliedParagraphStyle="ParagraphStyle/index">
            <CharacterStyleRange AppliedCharacterStyle="CharacterStyle/{$cStyle}">
                <Content>(see also </Content>
            </CharacterStyleRange>
        </ParagraphStyleRange>
        <xsl:apply-templates mode="cont">
            <xsl:with-param name="cStyle" select="$cStyle" tunnel="yes"/>
        </xsl:apply-templates>
        <ParagraphStyleRange AppliedParagraphStyle="ParagraphStyle/index">
            <CharacterStyleRange AppliedCharacterStyle="CharacterStyle/{$cStyle}">
                <Content>)</Content>
            </CharacterStyleRange>
        </ParagraphStyleRange>
    </xsl:template>

    <xsl:template match="text()" mode="block-children cont" name="makeTxsr">
        <xsl:param name="pStyle" select="'$ID/[No paragraph style]'" as="xs:string" tunnel="yes"/>
        <xsl:param name="cStyle" select="'$ID/[No character style]'" as="xs:string" tunnel="yes"/>
        <xsl:param name="txsrAtts" tunnel="yes" as="attribute()*"/>
        <xsl:param name="text" as="xs:string" select="''"/>
        <xsl:param name="link_target"/>
        <xsl:variable name="textValue" as="xs:string"
            select="
                if ($text = '')
                then
                    string(.)
                else
                    $text
                "/>
        <xsl:variable name="ID" select="ancestor-or-self::*[@id][1]/@id"/>
        <ParagraphStyleRange AppliedParagraphStyle="ParagraphStyle/{$pStyle}">
            <xsl:text>&#x0a;</xsl:text>
            <xsl:choose>
                <xsl:when
                    test="
                        name(preceding-sibling::*[1]) = 'regnum'
                        and count(ancestor::*[df:class(., 'regulation/regulation')]) > 1">
                    <xsl:text>&#x200B;&#x09;</xsl:text>
                    <xsl:call-template name="proc_range">
                        <xsl:with-param name="text">
                            <xsl:value-of select="incxgen:normalizeText($textValue)"/>
                        </xsl:with-param>
                        <xsl:with-param name="cStyle">
                            <xsl:value-of select="$cStyle"/>
                        </xsl:with-param>
                        <xsl:with-param name="link_target">
                            <xsl:value-of select="$link_target"/>
                        </xsl:with-param>
                        <xsl:with-param name="ID">
                            <xsl:value-of select="$ID"/>
                        </xsl:with-param>
                    </xsl:call-template>
                </xsl:when>
                <xsl:otherwise>
                    <!-- 916 is Greek Capital Letter Delta -->
                    <xsl:choose>
                        <xsl:when test="matches(., '^&#916;.*')">
                            <xsl:call-template name="proc_range">
                                <xsl:with-param name="text">
                                    <xsl:value-of
                                        select="incxgen:normalizeText(substring-after($textValue, '&#916;'))"
                                    />
                                </xsl:with-param>
                                <xsl:with-param name="cStyle">
                                    <xsl:value-of select="$cStyle"/>
                                </xsl:with-param>
                                <xsl:with-param name="link_target">
                                    <xsl:value-of select="$link_target"/>
                                </xsl:with-param>
                                <xsl:with-param name="ID">
                                    <xsl:value-of select="$ID"/>
                                </xsl:with-param>
                            </xsl:call-template>
                        </xsl:when>
                        <xsl:when
                            test="parent::li and name(preceding-sibling::*[1]) = 'cite_margin' and substring($textValue, 1, 1) = ' '">
                            <xsl:call-template name="proc_range">
                                <xsl:with-param name="text">
                                    <xsl:value-of
                                        select="incxgen:normalizeText(substring-after($textValue, ' '))"
                                    />
                                </xsl:with-param>
                                <xsl:with-param name="cStyle">
                                    <xsl:value-of select="$cStyle"/>
                                </xsl:with-param>
                                <xsl:with-param name="link_target">
                                    <xsl:value-of select="$link_target"/>
                                </xsl:with-param>
                                <xsl:with-param name="ID">
                                    <xsl:value-of select="$ID"/>
                                </xsl:with-param>
                            </xsl:call-template>
                        </xsl:when>
                        <xsl:when
                            test="parent::title and name(preceding-sibling::*[1]) = 'd4pSimpleEnumerator' and substring($textValue, 1, 1) = ' '">
                            <xsl:call-template name="proc_range">
                                <xsl:with-param name="text">
                                    <xsl:value-of
                                        select="incxgen:normalizeText(substring-after($textValue, ' '))"
                                    />
                                </xsl:with-param>
                                <xsl:with-param name="cStyle">
                                    <xsl:value-of select="$cStyle"/>
                                </xsl:with-param>
                                <xsl:with-param name="link_target">
                                    <xsl:value-of select="$link_target"/>
                                </xsl:with-param>
                                <xsl:with-param name="ID">
                                    <xsl:value-of select="$ID"/>
                                </xsl:with-param>
                            </xsl:call-template>
                        </xsl:when>
                        <xsl:otherwise>
                            <xsl:call-template name="proc_range">
                                <xsl:with-param name="text">
                                    <xsl:value-of select="incxgen:normalizeText($textValue)"/>
                                </xsl:with-param>
                                <xsl:with-param name="cStyle">
                                    <xsl:value-of select="$cStyle"/>
                                </xsl:with-param>
                                <xsl:with-param name="link_target">
                                    <xsl:value-of select="$link_target"/>
                                </xsl:with-param>
                                <xsl:with-param name="ID">
                                    <xsl:value-of select="$ID"/>
                                </xsl:with-param>
                            </xsl:call-template>
                        </xsl:otherwise>
                    </xsl:choose>
                </xsl:otherwise>
            </xsl:choose>
            <xsl:text>&#x0a;</xsl:text>
        </ParagraphStyleRange>
    </xsl:template>

    <!-- process range of spacial characters -->
    <xsl:template name="proc_range">
        <xsl:param name="text">''</xsl:param>
        <xsl:param name="cStyle">''</xsl:param>
        <xsl:param name="link_target"/>
        <xsl:param name="ID"/>
        <xsl:if test="$text != ''">
            <xsl:analyze-string select="$text" regex="([\s\S]*?)([&#x391;-&#x3a9;&#x3b1;-&#x3c9;&#x3c;&#x3e;&#x2264;&#x2265;&#x221e;&#x2248;])">
                <xsl:matching-substring>
                    <CharacterStyleRange AppliedCharacterStyle="CharacterStyle/{$cStyle}">
                        <xsl:if test="$link_target = 'true'">
                            <!-- this is a link target: ouput a link anchor -->
                            <HyperlinkTextDestination Self="HyperlinkTextDestination/{$ID}"
                                Name="{$ID}" Hidden="false" DestinationUniqueKey="{$ID}"/>
                        </xsl:if>
                        <Content>
                            <xsl:value-of select="regex-group(1)"/>
                        </Content>
                    </CharacterStyleRange>
                    <CharacterStyleRange
                        AppliedCharacterStyle="CharacterStyle/special_character">
                        <Content>
                            <xsl:value-of select="regex-group(2)"/>
                        </Content>
                    </CharacterStyleRange>
                </xsl:matching-substring>
                <xsl:non-matching-substring>
                    <xsl:choose>
                        <xsl:when test="matches(., '[\s\S]*?[&#x391;-&#x3a9;&#x3b1;-&#x3c9;&#x3c;&#x3e;&#x2264;&#x2265;&#x221e;&#x2248;]')">
                            <xsl:call-template name="proc_range">
                                <xsl:with-param name="text">
                                    <xsl:value-of select="."/>
                                </xsl:with-param>
                                <xsl:with-param name="cStyle">
                                    <xsl:value-of select="$cStyle"/>
                                </xsl:with-param>
                            </xsl:call-template>
                        </xsl:when>
                        <xsl:otherwise>
                            <CharacterStyleRange AppliedCharacterStyle="CharacterStyle/{$cStyle}">
                                <xsl:if test="$link_target = 'true'">
                                    <!-- this is a link target: ouput a link anchor -->
                                    <HyperlinkTextDestination Self="HyperlinkTextDestination/{$ID}"
                                        Name="{$ID}" Hidden="false" DestinationUniqueKey="{$ID}"/>
                                </xsl:if>
                               <Content>
                                    <xsl:value-of select="."/>
                                </Content>
                            </CharacterStyleRange>
                        </xsl:otherwise>
                    </xsl:choose>
                </xsl:non-matching-substring>
            </xsl:analyze-string>
        </xsl:if>
    </xsl:template>

    <xsl:template mode="block-children" match="*[df:class(., 'regnum-d/regnum')]" priority="100">
        <xsl:variable name="cStyle" select="e2s:getCStyleForElement(.)" as="xs:string"/>
        <xsl:apply-templates mode="cont">
            <!-- each <regnum> is a potential link target, so set the para to "true" so that an anchor will be output -->
            <xsl:with-param name="link_target">true</xsl:with-param>
            <xsl:with-param name="cStyle" select="$cStyle" tunnel="yes"/>
        </xsl:apply-templates>
    </xsl:template>

    <xsl:template mode="block-children"
        match="*[df:class(., 'd4p_simplenum-d/d4pSimpleEnumerator')]" priority="5">
        <xsl:apply-templates mode="cont"> </xsl:apply-templates>
        <ParagraphStyleRange AppliedParagraphStyle="ParagraphStyle/A head">
            <CharacterStyleRange AppliedCharacterStyle="CharacterStyle/$ID/[No character style]">
                <Content>
                    <xsl:text>&#x09;</xsl:text>
                </Content>
            </CharacterStyleRange>
        </ParagraphStyleRange>
    </xsl:template>

    <xsl:template mode="block-children"
        match="*[df:class(., 'hi-d/b')][ancestor::*[df:class(., 'topic/fig')]]" priority="5">
        <xsl:apply-templates mode="cont"> </xsl:apply-templates>
        <ParagraphStyleRange AppliedParagraphStyle="ParagraphStyle/Figure caption">
            <CharacterStyleRange AppliedCharacterStyle="CharacterStyle/Strong">
                <Content>
                    <xsl:text>&#x09;</xsl:text>
                </Content>
            </CharacterStyleRange>
        </ParagraphStyleRange>
    </xsl:template>

    <xsl:template mode="block-children" match="*[df:class(., 'topic/fn')]" priority="10">
        <xsl:variable name="cStyle" select="e2s:getCStyleForElement(.)" as="xs:string"/>
        <CharacterStyleRange AppliedCharacterStyle="CharacterStyle/$ID/[No character style]"
            Position="Superscript">
            <Footnote>
                <ParagraphStyleRange
                    AppliedParagraphStyle="ParagraphStyle/Body Text Sub little notes">
                    <CharacterStyleRange
                        AppliedCharacterStyle="CharacterStyle/$ID/[No character style]">
                        <Content>
                            <xsl:processing-instruction name="ACE">
                <xsl:text>4</xsl:text>
              </xsl:processing-instruction>
                            <xsl:text>&#x09;</xsl:text>
                            <xsl:value-of select="incxgen:normalizeText(.)"/>
                        </Content>
                    </CharacterStyleRange>
                </ParagraphStyleRange>
            </Footnote>
        </CharacterStyleRange>
    </xsl:template>

    <xsl:template mode="block-children" match="*[df:class(., 'topic/ph')]" priority="5">
        <xsl:variable name="cStyle" select="e2s:getCStyleForElement(.)" as="xs:string"/>
        <xsl:apply-templates mode="cont">
            <!-- each <regnum> is a potential link target, so set the para to "true" so that an anchor will be output -->
            <xsl:with-param name="cStyle" select="$cStyle" tunnel="yes"/>
        </xsl:apply-templates>
    </xsl:template>

    <xsl:template mode="cont" match="*[df:class(., 'topic/ph')]" priority="5">
        <xsl:variable name="cStyle" select="e2s:getCStyleForElement(.)" as="xs:string"/>
        <xsl:apply-templates mode="cont">
            <!-- each <regnum> is a potential link target, so set the para to "true" so that an anchor will be output -->
            <xsl:with-param name="cStyle" select="$cStyle" tunnel="yes"/>
        </xsl:apply-templates>
    </xsl:template>

</xsl:stylesheet>
