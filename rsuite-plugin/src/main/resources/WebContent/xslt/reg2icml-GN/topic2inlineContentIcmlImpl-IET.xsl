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

  <xsl:template match="ph[@outputclass = 'mandatory_text']" mode="#all" priority="100">
    <xsl:apply-templates mode="cont">
      <xsl:with-param name="cStyle" select="'Mandatory'" tunnel="yes"/>
    </xsl:apply-templates>
  </xsl:template>
  
  <xsl:template match="
    *[df:class(., 'topic/xrefzzz')]
    " mode="block-children">
    <xsl:variable name="link_num" select="count(preceding::xref) + 1"/>
    <ParagraphStyleRange AppliedParagraphStyle="ParagraphStyle/Regulation Paragraph">
      <xsl:variable name="link_target" select="@href"/>
      <CharacterStyleRange AppliedCharacterStyle="CharacterStyle/Hyperlink">
        <HyperlinkTextSource Self="{generate-id()}" Name="Hyperlink {$link_num}" Hidden="false"
          AppliedCharacterStyle="CharacterStyle/Hyperlink">
          <Content>
            <xsl:apply-templates/>
          </Content>
        </HyperlinkTextSource>
      </CharacterStyleRange>
    </ParagraphStyleRange>
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
  
  <xsl:template match="text()" mode="block-children cont">
    <xsl:param name="pStyle" select="'$ID/[No paragraph style]'" as="xs:string" tunnel="yes"/>
    <xsl:param name="cStyle" select="'$ID/[No character style]'" as="xs:string" tunnel="yes"/>
    <xsl:param name="txsrAtts" tunnel="yes" as="attribute()*"/>
    <xsl:param name="text" as="xs:string" select="''"/>
    <xsl:param name="link_target"/>
    
    <xsl:variable name="textValue" as="xs:string"
      select="
      if ($text = '')
         then string(.)
         else $text
      "/>
    <xsl:if test="matches(., '^&#916;.*') and $cStyle = 'Subscript'">
      <ParagraphStyleRange AppliedParagraphStyle="ParagraphStyle/{$pStyle}">
        <xsl:text>&#x0a;</xsl:text>
        <CharacterStyleRange AppliedCharacterStyle="CharacterStyle/Subscript-delta">
          <Content>
            <xsl:text>&#916;</xsl:text>
          </Content>
        </CharacterStyleRange>
        <xsl:text>&#x0a;</xsl:text>
      </ParagraphStyleRange>
      <xsl:text>&#x0a;</xsl:text>
    </xsl:if>
    <!--    <xsl:message> + [DEBUG] block-children/cont: text(): pStyle="<xsl:sequence select="$pStyle"/>", cStyle="<xsl:sequence select="$cStyle"/>"</xsl:message>-->
<!--    <xsl:message>TEXT:<xsl:sequence select="."/></xsl:message>
    <xsl:message>CSTYLE:<xsl:sequence select="$cStyle"/></xsl:message>
-->    <xsl:variable name="cStyle2">
      <xsl:choose>
        <xsl:when test="contains($cStyle, '/') or contains($cStyle, '[')">
          <xsl:choose>
            <xsl:when test="ancestor::*[@outputclass = 'mandatory_text']">
              <xsl:text>Mandatory</xsl:text>
            </xsl:when>
            <xsl:otherwise>
              <xsl:value-of select="$cStyle"/>
            </xsl:otherwise>
          </xsl:choose>
        </xsl:when>
        <xsl:otherwise>
          <xsl:choose>
            <xsl:when test="$cStyle = 'Mandatory'">
              <xsl:value-of select="$cStyle"/>
            </xsl:when>
            <xsl:when test="ancestor::*[@outputclass = 'mandatory_text']">
              <xsl:value-of select="concat($cStyle, '-Mandatory')"/>
            </xsl:when>
            <xsl:otherwise>
              <xsl:value-of select="$cStyle"/>
            </xsl:otherwise>
          </xsl:choose>
        </xsl:otherwise>
      </xsl:choose>
    </xsl:variable>
    
    
    <ParagraphStyleRange AppliedParagraphStyle="ParagraphStyle/{$pStyle}">
      <xsl:text>&#x0a;</xsl:text>
      <CharacterStyleRange AppliedCharacterStyle="CharacterStyle/{$cStyle2}">
        <xsl:variable name="ID" select="ancestor-or-self::*[@id][1]/@id"/>
        <Content>
          <xsl:choose>
            <xsl:when
              test="
                name(preceding-sibling::*[1]) = 'regnum'
                and count(ancestor::*[df:class(., 'regulation/regulation')]) > 1">
              <xsl:text>&#x200B;&#x09;</xsl:text>
              <xsl:choose>
                <xsl:when test="matches(., '^&#916;.*')">
                  <xsl:value-of select="normalize-space(substring-after($textValue, '&#916;'))"/>
                </xsl:when>
                <xsl:otherwise>
                  <xsl:value-of select="normalize-space($textValue)"/>
                </xsl:otherwise>
              </xsl:choose>
            </xsl:when>
            <xsl:otherwise>
              <xsl:choose>
                <xsl:when test="matches(., '^&#916;.*')">
                  <xsl:value-of select="incxgen:normalizeText(substring-after($textValue, '&#916;'))"/>
                </xsl:when>
                <xsl:when
                  test="parent::li and name(preceding-sibling::*[1]) = 'cite_margin' and substring($textValue, 1, 1) = ' '">
                  <xsl:value-of select="incxgen:normalizeText(substring-after($textValue, ' '))"/>
                </xsl:when>
                <xsl:when
                  test="parent::title and name(preceding-sibling::*[1]) = 'd4pSimpleEnumerator' and substring($textValue, 1, 1) = ' '">
                  <xsl:value-of select="incxgen:normalizeText(substring-after($textValue, ' '))"/>
                </xsl:when>
                <xsl:otherwise>
                  <xsl:value-of select="incxgen:normalizeText($textValue)"/>
                </xsl:otherwise>
              </xsl:choose>
            </xsl:otherwise>
          </xsl:choose>
        </Content>
      </CharacterStyleRange>
      <xsl:text>&#x0a;</xsl:text>
    </ParagraphStyleRange>
    <xsl:text>&#x0a;</xsl:text>
  </xsl:template>

  <xsl:template mode="block-children" match="*[df:class(., 'regnum-d/regnum')]" priority="100">
    <xsl:variable name="cStyle" select="e2s:getCStyleForElement(.)" as="xs:string"/>
    <xsl:apply-templates mode="cont">
      <!-- each <regnum> is a potential link target, so set the para to "true" so that an anchor will be output -->
      <xsl:with-param name="link_target">true</xsl:with-param>
      <xsl:with-param name="cStyle" select="$cStyle" tunnel="yes"/>
    </xsl:apply-templates>
  </xsl:template>

  <xsl:template mode="block-children" match="*[df:class(., 'd4p_simplenum-d/d4pSimpleEnumerator')]"
    priority="5">
    <xsl:apply-templates mode="cont"> </xsl:apply-templates>
    <ParagraphStyleRange AppliedParagraphStyle="ParagraphStyle/A head">
      <CharacterStyleRange AppliedCharacterStyle="CharacterStyle/$ID/[No character style]">
        <Content>
          <xsl:text>&#x09;</xsl:text>
        </Content>
      </CharacterStyleRange>
    </ParagraphStyleRange>
  </xsl:template>

  <xsl:template mode="block-children" match="*[df:class(., 'hi-d/b')][ancestor::*[df:class(., 'topic/figzz')]]"
    priority="5">
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
    <CharacterStyleRange AppliedCharacterStyle="CharacterStyle/$ID/[No character style]" Position="Superscript">
      <Footnote>
        <ParagraphStyleRange AppliedParagraphStyle="ParagraphStyle/Body Text Sub little notes">
          <CharacterStyleRange AppliedCharacterStyle="CharacterStyle/$ID/[No character style]">
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
  
<xsl:template mode="block-children" match="*[df:class(., 'topic/ph')]"
    priority="5">
    <xsl:variable name="cStyle" select="e2s:getCStyleForElement(.)" as="xs:string"/>
    <xsl:apply-templates mode="cont">
      <!-- each <regnum> is a potential link target, so set the para to "true" so that an anchor will be output -->
      <xsl:with-param name="cStyle" select="$cStyle" tunnel="yes"/>
    </xsl:apply-templates>
  </xsl:template>

  <xsl:template mode="cont" match="*[df:class(., 'topic/ph')]"
    priority="5">
    <xsl:variable name="cStyle" select="e2s:getCStyleForElement(.)" as="xs:string"/>
    <xsl:apply-templates mode="cont">
      <!-- each <regnum> is a potential link target, so set the para to "true" so that an anchor will be output -->
      <xsl:with-param name="cStyle" select="$cStyle" tunnel="yes"/>
    </xsl:apply-templates>
  </xsl:template>
  
</xsl:stylesheet>
