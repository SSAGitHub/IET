<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
      xmlns:xs="http://www.w3.org/2001/XMLSchema"
      xmlns:idsc="http://www.reallysi.com/namespaces/indesign_style_catalog"
      xmlns:ditaarch="http://dita.oasis-open.org/architecture/2005/"
      xmlns:xmp-x="adobe:ns:meta/"
      xmlns:pam="http://prismstandard.org/namespaces/pam/1.0/"
      xmlns:prism="http://prismstandard.org/namespaces/basic/1.2/"
      xmlns:pim="http://prismstandard.org/namespaces/pim/1.2/"
      xmlns:dc="http://purl.org/dc/elements/1.1/"
      xmlns:xhtml="http://www.w3.org/1999/xhtml"
      xmlns:incxgen="http//dita2indesign.org/functions/incx-generation"
      xmlns:df="http://dita2indesign.org/dita/functions"
      exclude-result-prefixes="xs idsc incxgen ditaarch xmp-x pam prism dc pim xhtml df"
      version="2.0">
  <!-- =================================================
       Adobe InCopy Markup Language (ICML) generation utilities.
       
       Copyright (c) 2011, 2014 DITA for Publishers.
       
       ================================================= -->
  
  <!-- URL (relative to the stylesheet document if not absolute)
       of the InDesign style catalog to use in generating 
       the result InCopy articles.
    -->
  <xsl:param name="styleCatalogUri" select="''" as="xs:string"/>
  
  <xsl:variable name="defaultStyleCatalogUri"
    select="'default-dita-indesign-style-catalog.xml'"
    as="xs:string"
  />

  
  <xsl:template name="makeBlock-cont-nobreak">
    <xsl:param name="pStyle" select="'ID$/[No paragraph style]'" as="xs:string" tunnel="yes"/>
    <xsl:param name="cStyle" select="'Strong'" as="xs:string" tunnel="yes"/>
    <xsl:param name="txsrAtts" tunnel="yes" as="attribute()*"/>
    <xsl:param name="content" as="node()*"/>
    <xsl:param name="markerType" as="xs:string" select="'para'"/>
    
    <xsl:variable name="pStyleEscaped" as="xs:string" select="incxgen:escapeStyleName($pStyle)"/>
    <xsl:variable name="cStyleEscaped" as="xs:string" select="incxgen:escapeStyleName($cStyle)"/>
    
    <!--    <xsl:message> + [DEBUG] makeBlock-cont: pStyle="<xsl:sequence select="$pStyle"/>", cStyle="<xsl:sequence select="$cStyle"/>"</xsl:message>-->
    <xsl:variable name="pcntContent" as="node()*">
      <xsl:choose>
        <xsl:when test="count($content) gt 0">
          <xsl:apply-templates select="$content" mode="block-children"/>
        </xsl:when>
        <xsl:otherwise>
          <xsl:apply-templates select="text()|*|processing-instruction()" mode="block-children">
            <xsl:with-param name="cStyle" select="'Strong'" as="xs:string" tunnel="yes"/>
          </xsl:apply-templates>
        </xsl:otherwise>
      </xsl:choose>      
    </xsl:variable>
    
    <xsl:sequence select="$pcntContent"/>

<!--
    <xsl:choose>
      <xsl:when test="$markerType = 'nobreak'">
        <ParagraphStyleRange
          AppliedParagraphStyle="ParagraphStyle/{$pStyleEscaped}">
          <CharacterStyleRange AppliedCharacterStyle="CharacterStyle/$ID/[No character style]" ParagraphBreakType="NextFrame">
            <Brr/>
          </CharacterStyleRange>
        </ParagraphStyleRange>  
      </xsl:when>
      <xsl:when test="$markerType = 'framebreak'">
        <ParagraphStyleRange
          AppliedParagraphStyle="ParagraphStyle/{$pStyleEscaped}">
          <CharacterStyleRange AppliedCharacterStyle="CharacterStyle/$ID/[No character style]" ParagraphBreakType="NextFrame">
            <Br/>
          </CharacterStyleRange>
        </ParagraphStyleRange>  
      </xsl:when>
      <xsl:when test="$markerType = 'columnbreak'">
        <ParagraphStyleRange
          AppliedParagraphStyle="ParagraphStyle/{$pStyleEscaped}">
          <CharacterStyleRange AppliedCharacterStyle="CharacterStyle/$ID/[No character style]" ParagraphBreakType="NextColumn">
            <Br/>
          </CharacterStyleRange>
        </ParagraphStyleRange>  
      </xsl:when>
      <xsl:when test="$markerType = 'linebreak'">
        <ParagraphStyleRange
          AppliedParagraphStyle="ParagraphStyle/{$pStyleEscaped}">
          <CharacterStyleRange AppliedCharacterStyle="CharacterStyle/$ID/[No character style]">
            <Content><xsl:text>&#x2028;</xsl:text></Content>
          </CharacterStyleRange>
        </ParagraphStyleRange>  
      </xsl:when>
      <xsl:when test="$markerType = 'pagebreak'">
        <ParagraphStyleRange
          AppliedParagraphStyle="ParagraphStyle/{$pStyleEscaped}">
          <CharacterStyleRange AppliedCharacterStyle="CharacterStyle/$ID/[No character style]" ParagraphBreakType="NextPage">
            <Br/>
          </CharacterStyleRange>
        </ParagraphStyleRange>  
      </xsl:when>
      <xsl:when test="$markerType = 'oddpagebreak'">
        <ParagraphStyleRange
          AppliedParagraphStyle="ParagraphStyle/{$pStyleEscaped}">
          <CharacterStyleRange AppliedCharacterStyle="CharacterStyle/$ID/[No character style]" ParagraphBreakType="NextOddPage">
            <Br/>
          </CharacterStyleRange>
        </ParagraphStyleRange>  
      </xsl:when>
      <xsl:when test="$markerType = 'evenpagebreak'">
        <ParagraphStyleRange
          AppliedParagraphStyle="ParagraphStyle/{$pStyleEscaped}">
          <CharacterStyleRange AppliedCharacterStyle="CharacterStyle/$ID/[No character style]" ParagraphBreakType="NextEvenPage">
            <Br/>
          </CharacterStyleRange>
        </ParagraphStyleRange>  
      </xsl:when>
      <xsl:when test="$markerType = 'none'"/>
      <xsl:otherwise>
        <ParagraphStyleRange
          AppliedParagraphStyle="ParagraphStyle/{$pStyleEscaped}">
          <CharacterStyleRange AppliedCharacterStyle="CharacterStyle/$ID/[No character style]">
            <Br/>
          </CharacterStyleRange>
        </ParagraphStyleRange>  
      </xsl:otherwise>
    </xsl:choose>
-->    
  </xsl:template>
  

<xsl:template name="makeBlock-cont">
    <xsl:param name="pStyle" select="'ID$/[No paragraph style]'" as="xs:string" tunnel="yes"/>
    <xsl:param name="cStyle" select="'ID$/[No character style]'" as="xs:string" tunnel="yes"/>
    <xsl:param name="txsrAtts" tunnel="yes" as="attribute()*"/>
    <xsl:param name="content" as="node()*"/>
    <xsl:param name="markerType" as="xs:string" select="'para'"/>
    <xsl:param name="justification" as="xs:string?" select="''" tunnel="yes"/>
    
    <!--xsl:message>CONTENT1: <xsl:sequence select="$content"></xsl:sequence></xsl:message>
    <xsl:message>CONTENT2: <xsl:sequence select="$content/following-sibling::*"></xsl:sequence></xsl:message-->
    <xsl:variable name="pStyleEscaped" as="xs:string" select="incxgen:escapeStyleName($pStyle)"/>
    <xsl:variable name="cStyleEscaped" as="xs:string" select="incxgen:escapeStyleName($cStyle)"/>
    
    <!--    <xsl:message> + [DEBUG] makeBlock-cont: pStyle="<xsl:sequence select="$pStyle"/>", cStyle="<xsl:sequence select="$cStyle"/>"</xsl:message>-->
    
    <xsl:variable name="pcntContent" as="node()*">
      <xsl:choose>
        <xsl:when test="count($content) gt 0">
          <xsl:apply-templates select="$content" mode="block-children"/>
        </xsl:when>
        <xsl:otherwise>
          <xsl:apply-templates select="text()|*|processing-instruction()" mode="block-children"/>
        </xsl:otherwise>
      </xsl:choose>      
    </xsl:variable>
    
    <xsl:sequence select="$pcntContent"/>
    <xsl:if test="name(($content/following-sibling::*)[1])!='image' or ($content/following-sibling::*)[1]/@placement != 'inline'  or not(($content/following-sibling::*)[1]/@placement)">
      <ParagraphStyleRange
        AppliedParagraphStyle="ParagraphStyle/{$pStyleEscaped}">
        <xsl:if test="$justification != ''">
          <xsl:attribute name="Justification" select="$justification"/>
        </xsl:if>
        <xsl:choose>
          <!--xsl:when test="name($content/following-sibling::*[1])='image'"/-->
          <xsl:when test="$markerType = 'framebreak'">
            <CharacterStyleRange AppliedCharacterStyle="CharacterStyle/$ID/[No character style]"
              ParagraphBreakType="NextFrame">
              <Br/>
            </CharacterStyleRange>
          </xsl:when>
          <xsl:when test="$markerType = 'columnbreak'">
            <CharacterStyleRange AppliedCharacterStyle="CharacterStyle/$ID/[No character style]"
              ParagraphBreakType="NextColumn">
              <Br/>
            </CharacterStyleRange>
          </xsl:when>
          <xsl:when test="$markerType = 'linebreak'">
            <CharacterStyleRange AppliedCharacterStyle="CharacterStyle/$ID/[No character style]">
              <Content><xsl:text>&#x2028;</xsl:text></Content>
            </CharacterStyleRange>
          </xsl:when>
          <xsl:when test="$markerType = 'pagebreak'">
            <CharacterStyleRange AppliedCharacterStyle="CharacterStyle/$ID/[No character style]"
              ParagraphBreakType="NextPage">
              <Br/>
            </CharacterStyleRange>
          </xsl:when>
          <xsl:when test="$markerType = 'oddpagebreak'">
            <CharacterStyleRange AppliedCharacterStyle="CharacterStyle/$ID/[No character style]"
              ParagraphBreakType="NextOddPage">
              <Br/>
            </CharacterStyleRange>
          </xsl:when>
          <xsl:when test="$markerType = 'evenpagebreak'">
            <CharacterStyleRange AppliedCharacterStyle="CharacterStyle/$ID/[No character style]"
              ParagraphBreakType="NextEvenPage">
              <Br/>
            </CharacterStyleRange>
          </xsl:when>
          <xsl:when test="$markerType = 'none'"/>
          <xsl:otherwise>
            <CharacterStyleRange AppliedCharacterStyle="CharacterStyle/$ID/[No character style]">
              <Br/>
            </CharacterStyleRange>
          </xsl:otherwise>
        </xsl:choose>
      </ParagraphStyleRange>  
    </xsl:if>   
  </xsl:template>
  
<xsl:template name="makeBlock-cont_cell">
    <xsl:param name="pStyle" select="'ID$/[No paragraph style]'" as="xs:string" tunnel="yes"/>
    <xsl:param name="cStyle" select="'ID$/[No character style]'" as="xs:string" tunnel="yes"/>
    <xsl:param name="txsrAtts" tunnel="yes" as="attribute()*"/>
    <xsl:param name="content" as="node()*"/>
    <xsl:param name="markerType" as="xs:string" select="'para'"/>
    <xsl:param name="justification" as="xs:string?" select="''" tunnel="yes"/>
    
    <!--xsl:message>CONTENT1: <xsl:sequence select="$content"></xsl:sequence></xsl:message>
    <xsl:message>CONTENT2: <xsl:sequence select="$content/following-sibling::*"></xsl:sequence></xsl:message-->
    <xsl:variable name="pStyleEscaped" as="xs:string" select="incxgen:escapeStyleName($pStyle)"/>
    <xsl:variable name="cStyleEscaped" as="xs:string" select="incxgen:escapeStyleName($cStyle)"/>
    
    <!--    <xsl:message> + [DEBUG] makeBlock-cont: pStyle="<xsl:sequence select="$pStyle"/>", cStyle="<xsl:sequence select="$cStyle"/>"</xsl:message>-->
    
    <xsl:variable name="pcntContent" as="node()*">
      <xsl:choose>
        <xsl:when test="count($content) gt 0">
          <xsl:apply-templates select="$content" mode="block-children"/>
        </xsl:when>
        <xsl:otherwise>
          <xsl:apply-templates select="text()|*|processing-instruction()" mode="block-children"/>
        </xsl:otherwise>
      </xsl:choose>      
    </xsl:variable>
    
    <xsl:sequence select="$pcntContent"/>
  </xsl:template>
  
  <xsl:function name="df:isBlock_regnum" as="xs:boolean">
    <xsl:param name="context" as="element()"/>
    <xsl:variable name="result" as="xs:boolean">
      <xsl:choose>
        <xsl:when test="contains($context/@class, 'regnum')">
          <xsl:sequence select="true()"/>
        </xsl:when>
        <xsl:otherwise>
          <xsl:sequence select="false()"/>
        </xsl:otherwise>
      </xsl:choose>
    </xsl:variable>  
    <xsl:sequence select="$result"/>
  </xsl:function>
  
</xsl:stylesheet>
