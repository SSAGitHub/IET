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
    *[df:class(., 'topic/text')] |
    *[df:class(., 'topic/entry')] |
    *[df:class(., 'topic/cite')]    
    "
    mode="block-children"> </xsl:template>

  <xsl:template match="
    *[df:class(., 'topic/xrefzz')]
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


  <xsl:template match="*[df:class(., 'd4p_simplenum-d/d4pSimpleEnumerator')]" mode="block-children">
    <!--xsl:message>d4pSimpleEnumerator<xsl:value-of select="."/></xsl:message-->
    <xsl:variable name="cStyle" select="e2s:getCStyleForElement(.)" as="xs:string"/>
    <xsl:apply-templates mode="cont">
      </xsl:apply-templates>
    <ParagraphStyleRange AppliedParagraphStyle="ParagraphStyle/Heading 3 - Regulation title*">
      <CharacterStyleRange AppliedCharacterStyle="CharacterStyle/$ID/[No character style]">
        <Content>&#x09;&#x200B;</Content>
      </CharacterStyleRange>
    </ParagraphStyleRange>

  </xsl:template>


  <xsl:template match="*[df:class(., 'd4p-formatting-d/br')]" mode="#all" priority="100">
    <xsl:variable name="cStyle" select="e2s:getCStyleForElement(.)" as="xs:string"/>
    <xsl:apply-templates mode="cont">
      <xsl:with-param name="cStyle" select="$cStyle" tunnel="yes"/>
    </xsl:apply-templates>
         <Br/>
   </xsl:template>
  
  <xsl:template match="*[df:class(., 'indexEntry-d/indexSee')]" mode="#all" priority="100">
    <xsl:variable name="cStyle" select="e2s:getCStyleForElement(.)" as="xs:string"/>
    <ParagraphStyleRange AppliedParagraphStyle="ParagraphStyle/$ID/[No paragraph style]">
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
    <ParagraphStyleRange AppliedParagraphStyle="ParagraphStyle/$ID/[No paragraph style]">
      <CharacterStyleRange AppliedCharacterStyle="CharacterStyle/{$cStyle}">
        <Content>(see also </Content>
      </CharacterStyleRange>
    </ParagraphStyleRange>
    <xsl:apply-templates mode="cont">
      <xsl:with-param name="cStyle" select="$cStyle" tunnel="yes"/>
    </xsl:apply-templates>
    <ParagraphStyleRange AppliedParagraphStyle="ParagraphStyle/$ID/[No paragraph style]">
      <CharacterStyleRange AppliedCharacterStyle="CharacterStyle/{$cStyle}">
        <Content>)</Content>
      </CharacterStyleRange>
    </ParagraphStyleRange>
  </xsl:template>
  
  <xsl:template match="ph[@status='deleted']" mode="#all" priority="100">
    <ParagraphStyleRange AppliedParagraphStyle="ParagraphStyle/$ID/[No paragraph style]">
      <CharacterStyleRange AppliedCharacterStyle="CharacterStyle/deleted_content">
        <Content>&#x2205;</Content>
      </CharacterStyleRange>
    </ParagraphStyleRange>
</xsl:template>
  

  <xsl:template match="ph[@status='new'][*]" mode="#all" priority="100">
    <xsl:message>PH with child elements: <xsl:sequence select="."/></xsl:message>
  <xsl:apply-templates mode="#current"/> 
  </xsl:template>

  <xsl:template match="ph[@status='new'][not(*)]" mode="#all" priority="100">
    <xsl:apply-templates mode="cont">
      <!--      <xsl:with-param name="cStyle" select="'changed'" tunnel="yes"/>
-->
    </xsl:apply-templates>
  </xsl:template>

  <xsl:template match="ph[@status='changed'][*]" mode="#all" priority="100">
    <xsl:apply-templates mode="#current"/>
  </xsl:template>
  
  <xsl:template match="text()" mode="block-children cont" name="makeTxsr">
    <xsl:param name="pStyle" select="'$ID/[No paragraph style]'" as="xs:string" tunnel="yes"/>
    <xsl:param name="cStyle" select="'$ID/[No character style]'" as="xs:string" tunnel="yes"/>
    <xsl:param name="txsrAtts" tunnel="yes" as="attribute()*"/>
    <xsl:param name="text" as="xs:string" select="''"/>
    <xsl:param name="link_target"/>
    <xsl:variable name="cStyle2">
      <xsl:choose>
        <xsl:when test="contains($cStyle, '/') or contains($cStyle, '[')">
          <xsl:choose>
            <xsl:when test="ancestor::*[@status='new']">
              <xsl:text>changed</xsl:text>
            </xsl:when>
            <xsl:otherwise>
              <xsl:value-of select="$cStyle"/>
            </xsl:otherwise>
          </xsl:choose>
        </xsl:when>
        <xsl:otherwise>
          <xsl:choose>
            <xsl:when test="$cStyle='changed'">
              <xsl:value-of select="$cStyle"/>
            </xsl:when>
            <xsl:when test="ancestor::*[@status='new']">
              <xsl:value-of select="concat($cStyle, '-changed')"/>
              </xsl:when>
            <xsl:otherwise>
              <xsl:value-of select="$cStyle"/>
            </xsl:otherwise>
          </xsl:choose>
        </xsl:otherwise>
      </xsl:choose>
    </xsl:variable>

    <xsl:variable name="textValue" as="xs:string"
      select="
      if ($text = '')
         then string(.)
         else $text
      "/>

    <!--    <xsl:message> + [DEBUG] block-children/cont: text(): pStyle="<xsl:sequence select="$pStyle"/>", cStyle="<xsl:sequence select="$cStyle"/>"</xsl:message>-->
     <ParagraphStyleRange AppliedParagraphStyle="ParagraphStyle/{$pStyle}">
      <xsl:text>&#x0a;</xsl:text>
      <CharacterStyleRange AppliedCharacterStyle="CharacterStyle/{$cStyle2}">
        <xsl:variable name="ID" select="ancestor-or-self::*[@id][1]/@id"/>
        <xsl:if test="$link_target = 'true'">
          <!--  this is a link target: ouput a link anchor -->
          <HyperlinkTextDestination Self="HyperlinkTextDestination/{$ID}" Name="{$ID}"
            Hidden="false" DestinationUniqueKey="{$ID}"/>
        </xsl:if>

        <Content>
          <xsl:choose>
            <xsl:when
              test="name(preceding-sibling::*[1])='regnum'
              and count(ancestor::*[df:class(., 'regulation/regulation')])>1">
              <xsl:value-of select="normalize-space($textValue)"/>
            </xsl:when>
            <xsl:when
              test="ancestor::*[df:class(., 'topic/title')]
              and count(ancestor::*[df:class(., 'regulation/regulation')])>1">
              <xsl:value-of select="normalize-space($textValue)"/>
            </xsl:when>
            <xsl:otherwise>
              <xsl:value-of select="incxgen:normalizeText($textValue)"/>
            </xsl:otherwise>
          </xsl:choose>
        </Content>
      </CharacterStyleRange>
      <xsl:text>&#x0a;</xsl:text>
    </ParagraphStyleRange>
    <xsl:text>&#x0a;</xsl:text>
  </xsl:template>

  <xsl:template mode="block-children" match="*[df:class(., 'regnum-d/regnum')]" priority="70">
    <xsl:variable name="cStyle" select="e2s:getCStyleForElement(.)" as="xs:string"/>
    <xsl:apply-templates mode="cont">
      <xsl:with-param name="link_target">false</xsl:with-param>
      <xsl:with-param name="cStyle" select="$cStyle" tunnel="yes"/>
    </xsl:apply-templates>
  <xsl:if test="count(ancestor::*[df:class(., 'regulation/regulation')])>1">
      <ParagraphStyleRange AppliedParagraphStyle="ParagraphStyle/$ID/[No paragraph style]">
        <CharacterStyleRange AppliedCharacterStyle="CharacterStyle/$ID/[No character style]">
          <Content>&#x200B;&#x09;</Content>
        </CharacterStyleRange>
      </ParagraphStyleRange>
    </xsl:if>
  </xsl:template>

</xsl:stylesheet>
