<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
      xmlns:xs="http://www.w3.org/2001/XMLSchema"
      xmlns:local="urn:local-functions"
      xmlns:df="http://dita2indesign.org/dita/functions"
      xmlns:e2s="http//dita2indesign.org/functions/element-to-style-mapping"
      exclude-result-prefixes="xs local df e2s"
      version="2.0">
  
  <!-- Element-to-style  mapper
    
    This module provides the base implementation for
    the "style-map" modes, which map elements in context
    to InDesign style names (paragraph, character, frame,
    object, table).
    
    Copyright (c) 2009, 2012 DITA to InDesign 
    
    NOTE: This material is intended to be donated to the RSI-sponsored
    DITA2InDesign open-source project.

<xsl:import href="../../net.sourceforge.dita4publishers.common.xslt/xsl/lib/dita-support-lib.xsl"/>
  -->
  
  <xsl:template match="/*[df:class(., 'part/part')]/*[df:class(., 'topic/title')]" 
    mode="style-map-pstyle" priority="10">
    <xsl:sequence select="'Heading 1'"/>
  </xsl:template>

  <xsl:template match="/*[df:class(., 'topic/topic')]/*[df:class(., 'topic/title')]" 
    mode="style-map-pstyle">
    <xsl:sequence select="'Heading 2 - Chapter Heading*'"/>
  </xsl:template>
  
  <xsl:template match="*[df:class(., 'regulation/regulation')]/*[df:class(., 'topic/title')]" 
    mode="style-map-pstyle" priority="10">
    <xsl:variable name="nesting_level" select="count(ancestor::regulation)" />
    <xsl:choose>
      <xsl:when test="$nesting_level = 1">Heading 2 - Chapter Heading*</xsl:when>
      <xsl:when test="$nesting_level = 2">Heading 3 - Regulation title*</xsl:when>
      <xsl:when test="$nesting_level = 3">Heading 4 - Regulation title</xsl:when>
      <xsl:when test="$nesting_level = 4">Heading 5</xsl:when>
      <xsl:when test="$nesting_level = 5">Heading 6</xsl:when>
      <xsl:when test="$nesting_level = 6">Heading 7</xsl:when>
      <xsl:when test="$nesting_level = 7">Heading 8</xsl:when>      
      <xsl:otherwise>Default Title</xsl:otherwise>
    </xsl:choose>    
  </xsl:template>

<!--
 <xsl:template match="/*[df:class(., 'topic/topic')]/*[df:class(., 'topic/title')]" 
    mode="style-map-pstyle">
    <xsl:sequence select="'Regulation Chapter Title'"/>
  </xsl:template>-->
  
  <xsl:template match="*[df:class(., 'index/index')]/*[df:class(., 'topic/body')]/*[df:class(., 'topic/section')]/*[df:class(., 'topic/title')]" 
    mode="style-map-pstyle" priority="10">
    <xsl:sequence select="'Index Headings'"/>
  </xsl:template>

  <xsl:template match="*[df:class(., 'topic/li')][ancestor::*[df:class(., 'index/index')]]" 
    mode="style-map-pstyle">
    <xsl:sequence select="'index'"/>
  </xsl:template>
  
  <xsl:template match="*[df:class(., 'topic/p')][ancestor::*[df:class(., 'index/index')]]" 
    mode="style-map-pstyle">
    <xsl:sequence select="'index'"/>
  </xsl:template>
  
  <xsl:template match="*[df:class(., 'topic/p')]" 
    mode="style-map-pstyle">
    <xsl:sequence select="'ParaText - Regulations Text'"/>
  </xsl:template>

  <xsl:template match="*[df:class(., 'topic/p')][ancestor::*[df:class(., 'topic/fig')]]" 
    mode="style-map-pstyle" priority="10">
    <xsl:sequence select="'FigText'"/>
  </xsl:template>
  
  <xsl:template match="*[df:class(., 'topic/p')][@outputclass='subtitle']" 
    mode="style-map-pstyle">
    <xsl:sequence select="'Heading 1'"/>
  </xsl:template>
  
  <xsl:template match="*[df:class(., 'topic/p')][@outputclass='section_title']" 
    mode="style-map-pstyle">
    <xsl:sequence select="'Heading 3 - Regulation title*'"/>
  </xsl:template>
  
  <xsl:template match="*[df:class(., 'topic/note')]" 
    mode="style-map-pstyle">
    <xsl:sequence select="'Note - Note *'"/>
  </xsl:template>

  <xsl:template match="*[df:class(., 'topic/li')]/*[df:class(., 'topic/note')]" 
    mode="style-map-pstyle" priority="10">
    <xsl:sequence select="'NoteSub'"/>
  </xsl:template>
  
  <xsl:template match="*[df:class(., 'topic/lq')]" 
    mode="style-map-pstyle">
    <xsl:sequence select="'Long Quote Single Para'"/>
  </xsl:template>
  
  <xsl:template match="*[df:class(., 'topic/lq')]/*[df:class(., 'topic/p')]" priority="10" 
    mode="style-map-pstyle">
    <xsl:variable name="styleModifier"
      select="
      if (count(preceding-sibling::*[df:isBlock(.)]) = 0)
      then if (count(following-sibling::*) = 0)
      then ' Only'
      else ' First'
      else if (count(following-sibling::*) = 0)
      then ' Last'
      else ''
      "
      as="xs:string"
    />
    <xsl:sequence select="concat('Long Quote Para', $styleModifier)"/>
  </xsl:template>
  
  <xsl:template match="*[df:class(., 'topic/shortdesc')]" 
    mode="style-map-pstyle">
    <xsl:sequence select="'Short Description'"/>
  </xsl:template>
  
  <!-- Default list style, but may not be needed -->
  
  <xsl:template match="*[df:class(., 'topic/ul')]/*[df:class(., 'topic/li')]" 
    mode="style-map-pstyle">
    <xsl:variable name="listCount" select="count(ancestor::*[df:class(., 'topic/ol')]) + count(ancestor::*[df:class(., 'topic/ul')])"/>
  <xsl:choose>
      <xsl:when test="$listCount=1">
        <xsl:sequence select="'ParaTextSub - Ordered list*'"/>
      </xsl:when>
      <xsl:otherwise>
        <xsl:sequence select="'ParaTextSub2'"/>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>
  
<!--  <xsl:template 
    match="*[df:class(., 'topic/ol')]/
    *[df:class(., 'topic/li')][count(preceding-sibling::*[df:class(.,'topic/li')]) = 0]"
    priority="10" 
    mode="style-map-pstyle"
    >
    <xsl:sequence select="'List Number First'"/>
  </xsl:template>-->
  
  <xsl:template match="*[df:class(., 'topic/dt')]" 
    mode="style-map-pstyle">
    <xsl:sequence select="'ParaText - Regulations Text'"/>
  </xsl:template>
  
  <xsl:template match="*[df:class(., 'topic/dt')]" 
    mode="style-map-cstyle">
    <xsl:sequence select="'Strong'"/>
  </xsl:template>
  
  <xsl:template match="*[df:class(., 'topic/dd')]" 
    mode="style-map-pstyle">
    <xsl:sequence select="'ParaText - Regulations Text'"/>
  </xsl:template>
  
  <!-- **************************************************************************************** -->
  
  <xsl:template match="*[df:class(., 'topic/ol')]/*[df:class(., 'topic/li')][count(preceding-sibling::*[df:class(., 'topic/li')])=0]" 
    mode="style-map-pstyle" priority="1">
    <xsl:call-template name="listLevel"/>
  </xsl:template>
  
  <xsl:template match="*[df:class(., 'topic/ol')]/*[df:class(., 'topic/li')]" 
    mode="style-map-pstyle">
    <xsl:call-template name="listLevel"/>
  </xsl:template>

  <!-- **************************************************************************************** -->
  
  <xsl:template match="*[df:class(., 'topic/ul')][@outputclass='no_label']/*[df:class(., 'topic/li')]" 
    mode="style-map-pstyle" priority="5">
    <xsl:call-template name="listLevel"/>
  </xsl:template>
  
  <!-- **************************************************************************************** -->
  
  <xsl:template match="*[df:class(., 'topic/ol')][@outputclass='arabicParen']
    /*[df:class(., 'topic/li')][count(preceding-sibling::*[df:class(., 'topic/li')])=0]" 
    mode="style-map-pstyle" priority="6">
    <xsl:call-template name="listLevel"/>
  </xsl:template>

  <xsl:template match="*[df:class(., 'topic/ol')][@outputclass='arabicParen']/*[df:class(., 'topic/li')]" 
    mode="style-map-pstyle" priority="5">
    <xsl:call-template name="listLevel"/>
  </xsl:template>

  <!-- **************************************************************************************** -->
  
  <xsl:template match="*[df:class(., 'topic/ol')][@outputclass='letterUpperCase']
    /*[df:class(., 'topic/li')][count(preceding-sibling::*[df:class(., 'topic/li')])=0]" 
    mode="style-map-pstyle" priority="6">
    <xsl:call-template name="listLevel"/>
  </xsl:template>
  
  <xsl:template match="*[df:class(., 'topic/ol')][@outputclass='letterUpperCase']/*[df:class(., 'topic/li')]" 
    mode="style-map-pstyle" priority="5">
    <xsl:call-template name="listLevel"/>
  </xsl:template>
  
  <!-- **************************************************************************************** -->

  <xsl:template match="*[df:class(., 'topic/ol')][@outputclass='letterLowerCase']
    /*[df:class(., 'topic/li')][count(preceding-sibling::*[df:class(., 'topic/li')])=0]" 
    mode="style-map-pstyle" priority="6">
    <xsl:call-template name="listLevel"/>
  </xsl:template>
   
  <xsl:template match="*[df:class(., 'topic/ol')][@outputclass='letterLowerCase']/*[df:class(., 'topic/li')]" 
    mode="style-map-pstyle" priority="5">
    <xsl:call-template name="listLevel"/>
  </xsl:template>
  
  <!-- **************************************************************************************** -->
  
  <xsl:template match="*[df:class(., 'topic/ol')][@outputclass='letterLowerCaseParen']
    /*[df:class(., 'topic/li')][count(preceding-sibling::*[df:class(., 'topic/li')])=0]" 
    mode="style-map-pstyle" priority="6">
    <xsl:call-template name="listLevel"/>
  </xsl:template>
  
  <xsl:template match="*[df:class(., 'topic/ol')][@outputclass='letterLowerCaseParen']/*[df:class(., 'topic/li')]" 
    mode="style-map-pstyle" priority="5">
    <xsl:call-template name="listLevel"/>
  </xsl:template>
  
  <!-- **************************************************************************************** -->
 
  <xsl:template match="*[df:class(., 'topic/ol')][@outputclass='romanUpperCase']
    /*[df:class(., 'topic/li')][count(preceding-sibling::*[df:class(., 'topic/li')])=0]" 
    mode="style-map-pstyle" priority="6">
    <xsl:call-template name="listLevel"/>
  </xsl:template>
  
  <xsl:template match="*[df:class(., 'topic/ol')][@outputclass='romanUpperCase']/*[df:class(., 'topic/li')]" 
    mode="style-map-pstyle" priority="5">
    <xsl:call-template name="listLevel"/>
  </xsl:template>
  
  <!-- **************************************************************************************** -->

  <xsl:template match="*[df:class(., 'topic/ol')][@outputclass='romanLowerCase']
    /*[df:class(., 'topic/li')][count(preceding-sibling::*[df:class(., 'topic/li')])=0]" 
    mode="style-map-pstyle" priority="6">
    <xsl:call-template name="listLevel"/>
  </xsl:template>
  
  <xsl:template match="*[df:class(., 'topic/ol')][@outputclass='romanLowerCase']/*[df:class(., 'topic/li')]" 
    mode="style-map-pstyle" priority="5">
    <xsl:call-template name="listLevel"/>
  </xsl:template>
  
  <!-- **************************************************************************************** -->
  
  <xsl:template match="*[df:class(., 'topic/ol')][@outputclass='romanLowerCaseParen']
    /*[df:class(., 'topic/li')][count(preceding-sibling::*[df:class(., 'topic/li')])=0]" 
    mode="style-map-pstyle" priority="6">
    <xsl:call-template name="listLevel"/>
  </xsl:template>
  
  <xsl:template match="*[df:class(., 'topic/ol')][@outputclass='romanLowerCaseParen']/*[df:class(., 'topic/li')]" 
    mode="style-map-pstyle" priority="5">
    <xsl:call-template name="listLevel"/>
  </xsl:template>
  
  <!-- **************************************************************************************** -->
  
   <xsl:template match="*[df:class(., 'topic/ul')][@outputclass='square']/*[df:class(., 'topic/li')]" 
    mode="style-map-pstyle" priority="5">
     <xsl:call-template name="listLevel"/>
  </xsl:template>
  
  <!-- **************************************************************************************** -->

  <xsl:template match="*[df:class(., 'topic/ul')][@outputclass='triangle']/*[df:class(., 'topic/li')]" 
    mode="style-map-pstyle" priority="5">
    <xsl:call-template name="listLevel"/>
  </xsl:template>
  
  <!-- **************************************************************************************** -->
  
  <xsl:template match="*[df:class(., 'topic/ul')][@outputclass='hyphen-long']/*[df:class(., 'topic/li')]" 
    mode="style-map-pstyle" priority="5">
    <xsl:call-template name="listLevel"/>
  </xsl:template>
  
  <!-- **************************************************************************************** -->
  
  <xsl:template match="*[df:class(., 'topic/ul')][@outputclass='hyphen-short']/*[df:class(., 'topic/li')]" 
    mode="style-map-pstyle" priority="5">
    <xsl:call-template name="listLevel"/>
  </xsl:template>
  
  <!-- **************************************************************************************** -->

  
  <xsl:template name="listLevel">
    <xsl:variable name="listCount" select="count(ancestor::*[df:class(., 'topic/ol')]) + count(ancestor::*[df:class(., 'topic/ul')])"/>
    <xsl:choose>
      <xsl:when test="ancestor::*[df:class(., 'topic/note')]">
        <xsl:sequence select="'NoteSub'"/>
      </xsl:when>
      <xsl:when test="$listCount=1">
        <xsl:sequence select="'ParaTextSub - Ordered list*'"/>
      </xsl:when>
      <xsl:otherwise>
        <xsl:sequence select="'ParaTextSub2'"/>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template> 

  <!-- **************************************************************************************** -->
  
  
  <xsl:template match="*[df:class(., 'topic/ph')]" 
    mode="style-map-pstyle">
    <xsl:sequence select="'ParaText - Regulations Text'"/>
  </xsl:template>
  
  
  <xsl:template match="ph[@outputclass='change_wrapper']" 
    mode="style-map-cstyle" priority="100">
    <xsl:sequence select="'changed'"/>
  </xsl:template>
  
  <xsl:template match="*[df:class(., 'topic/fig')]/*[df:class(., 'topic/title')]" 
    mode="style-map-pstyle">
    <xsl:sequence select="'TOC 3 tables/figs'"/>
  </xsl:template>
  
  <xsl:template match="*[df:class(., 'topic/figgroup')]/*[df:class(., 'topic/title')]" 
    mode="style-map-pstyle">
    <xsl:sequence select="'TOC 3 tables/figs'"/>
  </xsl:template>
  
  <xsl:template match="*[df:class(., 'topic/fig')]/*[df:class(., 'topic/data')]" 
    mode="style-map-pstyle">
    <xsl:sequence select="'Normal'"/>
  </xsl:template>

  <xsl:template match="*[df:class(., 'topic/figgroup')]/*[df:class(., 'topic/data')]" 
    mode="style-map-pstyle">
    <xsl:sequence select="'Normal'"/>
  </xsl:template>

  <xsl:template match="*[df:class(., 'topic/entry')]" 
    mode="style-map-pstyle">
    <xsl:sequence select="'ParaText - Regulations Text'"/>
  </xsl:template>

  <xsl:template match="*[df:class(., 'topic/entry')][@outputclass='symbols']" 
    mode="style-map-pstyle" priority="10">
    <xsl:sequence select="'symbols'"/>
  </xsl:template>
  
  <xsl:template match="*[df:class(., 'topic/entry')][@outputclass='index']" 
    mode="style-map-pstyle" priority="10">
    <xsl:sequence select="'index'"/>
  </xsl:template>

  <xsl:template match="*[df:class(., 'topic/entry')][@outputclass='index']/*[df:class(., 'topic/p')]" 
    mode="style-map-pstyle" priority="10">
    <xsl:sequence select="'index'"/>
  </xsl:template>

  <xsl:template match="*[df:class(., 'topic/p')][@outputclass='indexLast']" 
    mode="style-map-pstyle" priority="100">
    <xsl:sequence select="'index'"/>
  </xsl:template>
  
  <xsl:template match="*[df:class(., 'topic/entry')][@align='center']" 
    mode="style-map-pstyle" priority="10">
    <xsl:sequence select="'Table text cntr'"/>
  </xsl:template>

  <xsl:template match="*[df:class(., 'topic/entry')]/*[df:class(., 'topic/p')]" 
    mode="style-map-pstyle" priority="5">
    <xsl:sequence select="'ParaText - Regulations Text'"/>
  </xsl:template>
  
  <xsl:template match="*[df:class(., 'topic/entry')]/*[df:class(., 'topic/p')][@outputclass='table_footnote']" 
    mode="style-map-pstyle" priority="10">
    <xsl:sequence select="'Table text cntr'"/>
  </xsl:template>

  <xsl:template match="*[df:class(., 'topic/thead')]/*[df:class(., 'topic/row')]/*[df:class(., 'topic/entry')]" 
    mode="style-map-pstyle" priority="15">
    <xsl:sequence select="'Table text cntr'"/>
  </xsl:template>
  
  <xsl:template match="*[df:class(., 'topic/table')]/*[df:class(., 'topic/title')]" 
    mode="style-map-pstyle" priority="18">
    <xsl:sequence select="'TOC 3 tables/figs'"/>
  </xsl:template>

  <xsl:template match="*[df:class(., 'topic/table')][contains(@id, 'apptab4a2')]//*[df:class(., 'topic/thead')]/*[df:class(., 'topic/row')]/*[df:class(., 'topic/entry')]" 
    mode="style-map-pstyle" priority="18">
    <xsl:sequence select="'TABLE-col-heading'"/>
  </xsl:template>
  
  <xsl:template match="*[df:class(., 'topic/table')][contains(@id, 'apptab4a2')]//*[df:class(., 'topic/row')]/*[df:class(., 'topic/entry')]" 
    mode="style-map-pstyle" priority="17">
    <xsl:sequence select="'TABLE-cell'"/>
  </xsl:template>
  
  <xsl:template match="*[df:class(., 'topic/section')]/*[df:class(., 'topic/title')]" 
    mode="style-map-pstyle">
    <!-- FIXME: account for outputclass -->
    <xsl:variable name="nesting_level" select="count(ancestor::section)" />
    <xsl:choose>
      <xsl:when test="$nesting_level = 2">Regulation Heading Level 2</xsl:when>
      <xsl:when test="$nesting_level = 3">Regulation Heading Level 3</xsl:when>
      <xsl:when test="$nesting_level = 4">Regulation Heading Level 4</xsl:when>
      <xsl:when test="$nesting_level = 5">Regulation Heading Level 5</xsl:when>
      <xsl:when test="$nesting_level = 6">Regulation Heading Level 6</xsl:when>
      <xsl:when test="$nesting_level = 7">Regulation Heading Level 7</xsl:when>      
      <xsl:otherwise>Heading 3 - Regulation title*</xsl:otherwise>
    </xsl:choose>
  </xsl:template>
    
  <xsl:template match="*" priority="-1" mode="style-map-pstyle">
    <xsl:sequence
      select="
      if (string(@outputclass) != '')
         then e2s:getPStyleForOutputClass(., string(@outputclass))
         else df:getBaseClass(.)
      "
    />
  </xsl:template>
  
  
  <xsl:template match="*[df:class(., 'regnum-d/regnum')]" 
    priority="50"
    mode="style-map-cstyle">
   <xsl:sequence select="'StrongArial'"/>
  </xsl:template>

  <xsl:template match="*[df:class(., 'indexEntry-d/indexSee')]" 
    priority="50"
    mode="style-map-cstyle">  
    <xsl:sequence select="'Italic'"/>
  </xsl:template>
  
  
 
  <xsl:template match="*[df:class(., 'indexEntry-d/indexSeeAlso')]" 
    priority="50"
    mode="style-map-cstyle">  
    <xsl:sequence select="'Italic'"/>
  </xsl:template>
  
 
  <xsl:template match="*[df:class(., 'hi-d/i')]/*[df:class(., 'regnum-d/regnum')]" 
    priority="55"
    mode="style-map-cstyle">  
    <xsl:sequence select="'StrongArialItalic'"/>
  <!--xsl:variable name="nesting_level" select="count(ancestor::regulation)" />
  <xsl:choose>
      <xsl:when test="$nesting_level = 1">RegNum Chapter Title</xsl:when>
      <xsl:when test="$nesting_level = 2">RegNum Level 2</xsl:when>
      <xsl:when test="$nesting_level = 3">RegNum Level 3</xsl:when>
      <xsl:when test="$nesting_level = 4">RegNum Level 4</xsl:when>
      <xsl:when test="$nesting_level = 5">RegNum Level 5</xsl:when>
      <xsl:when test="$nesting_level = 6">RegNum Level 6</xsl:when>
      <xsl:when test="$nesting_level = 7">RegNum Level 7</xsl:when>      
      <xsl:otherwise>Default Title</xsl:otherwise>
    </xsl:choose-->
  </xsl:template>

  <xsl:template match="*[df:class(., 'topic/datazz')]" 
    mode="style-map-cstyle">
    <xsl:sequence select="'Title Bold'"/>
  </xsl:template>

<!--  <xsl:template match="*[df:class(., 'topic/xref')]" 
    mode="style-map-cstyle" priority="10">
    <xsl:sequence select="'Hyperlink'"/>
  </xsl:template>-->

  <xsl:template match="*[df:class(., 'topic/cite')]" 
    mode="style-map-cstyle">
    <xsl:sequence select="'Italic'"/>
  </xsl:template>
  
  <xsl:template match="*[df:class(., 'hi-d/i')]" 
    mode="style-map-cstyle" priority="10">
    <xsl:sequence select="'Italic'"/>
  </xsl:template>
  
  <xsl:template match="*[df:class(., 'topic/title')]/*[df:class(., 'hi-d/i')]" 
    mode="style-map-cstyle" priority="50">
    <xsl:sequence select="'ArialItalic'"/>
  </xsl:template>
  
  <xsl:template match="*[df:class(., 'hi-d/b')]" 
    mode="style-map-cstyle" priority="10">
    <xsl:sequence select="'Strong'"/>
  </xsl:template>

  <xsl:template match="*[df:class(., 'd4p-formatting-d/b-i')]" 
    mode="style-map-cstyle" priority="10">
    <xsl:sequence select="'StrongItalic'"/>
  </xsl:template>
  
  <xsl:template match="*[df:class(., 'hi-d/sub')]" 
    mode="style-map-cstyle" priority="10">
    <xsl:sequence select="'Subscript'"/>
  </xsl:template>

  <xsl:template match="*[df:class(., 'hi-d/sup')]" 
    mode="style-map-cstyle" priority="10">
    <xsl:sequence select="'Superscript'"/>
  </xsl:template>

  <xsl:template 
    match="
    *[df:class(., 'hi-d/b')]/*[df:class(., 'hi-d/sup')]" 
    mode="style-map-cstyle" priority="10">
    <xsl:sequence select="'Superscript'"/>
  </xsl:template>

  <xsl:template 
    match="
    *[df:class(., 'topic/b')]/*[df:class(., 'topic/i')] |
    *[df:class(., 'topic/i')]/*[df:class(., 'topic/b')]" 
    mode="style-map-cstyle">
    <xsl:sequence select="'bold-italic'"/>
  </xsl:template>
  
  <xsl:template 
    match="
    *[df:class(., 'topic/b')]/*[df:class(., 'topic/i')]/*[df:class(., 'topic/codeph')] |
    *[df:class(., 'topic/b')]/*[df:class(., 'topic/codeph')]/*[df:class(., 'topic/i')] |
    *[df:class(., 'topic/codeph')]/*[df:class(., 'topic/b')]/*[df:class(., 'topic/i')] |
    *[df:class(., 'topic/codeph')]/*[df:class(., 'topic/i')]/*[df:class(., 'topic/b')] |
    *[df:class(., 'topic/i')]/*[df:class(., 'topic/b')]/*[df:class(., 'topic/codeph')]
    " 
    mode="style-map-cstyle">
    <xsl:sequence select="'bold-italic-monospaced '"/>
  </xsl:template>
  
  <xsl:template match="*[df:class(., 'topic/codeph')]" 
    mode="style-map-cstyle">
    <xsl:sequence select="'monospaced'"/>
  </xsl:template>
  
  <xsl:template match="*" priority="-1" mode="style-map-cstyle">
    <xsl:sequence
      select="
      if (string(@outputclass) != '')
        then e2s:getCStyleForOutputClass(., string(@outputclass))
        else df:getBaseClass(.)
      "
    />
  </xsl:template>
  
  <xsl:template match="*" priority="-1" mode="style-map-tstyle">
    <xsl:sequence
      select="
      if (string(@outputclass) != '')
      then e2s:getTStyleForOutputClass(.)
      else '[Basic Table]'
      "
    />
  </xsl:template>
  
  <xsl:function name="e2s:getPStyleForElement" as="xs:string">
    <xsl:param name="context" as="element()"/>
    <xsl:param name="articleType" as="xs:string"/>
    <xsl:apply-templates select="$context" mode="style-map-pstyle">
      <xsl:with-param name="articleType" as="xs:string" tunnel="yes" select="$articleType"/>
    </xsl:apply-templates>
  </xsl:function>  
  
  <xsl:function name="e2s:getCStyleForElement" as="xs:string">
    <xsl:param name="context" as="element()"/>
    <xsl:choose>
      <xsl:when test="$context/@outputclass[. != 'change_wrapper']">
        <xsl:sequence select="e2s:getCStyleForOutputClass($context)"/>
      </xsl:when>
      <xsl:otherwise>
        <xsl:apply-templates select="$context" mode="style-map-cstyle"/>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:function>  
  
  <xsl:function name="e2s:getTStyleForElement" as="xs:string">
    <xsl:param name="context" as="element()"/>
    <xsl:apply-templates select="$context" mode="style-map-tstyle"/>
  </xsl:function>  
  
  <xsl:function name="e2s:getTStyleForOutputClass" as="xs:string">
    <xsl:param name="context" as="element()"/>
    <xsl:sequence select="e2s:getTStyleForOutputClass($context, string($context/@outputclass))"/>
  </xsl:function>  

  <xsl:function name="e2s:getTStyleForOutputClass" as="xs:string">
    <xsl:param name="context" as="element()"/><!-- Element that exhibits the outputclass value -->
    <xsl:param name="outputclass" as="xs:string"/><!-- The output class value. This is passed
      so this function can be used where the
      the "outputclass" is provided by means 
      other than an @outputclass attribute. -->
    <!-- Given an @outputclass value, maps it to an InDesign style.
      
      For now, this just returns the outputclass value, but this
      needs to be driven by a configuration file, ideally the
      same one used to map element types to styles.
    -->
    <xsl:sequence select="$outputclass"/>
  </xsl:function>  
  
  
  <xsl:function name="e2s:getPStyleForOutputClass" as="xs:string">
    <xsl:param name="context" as="element()"/><!-- Element that exhibits the outputclass value -->
    <xsl:param name="outputclass" as="xs:string"/><!-- The output class value. This is passed
      so this function can be used where the
      the "outputclass" is provided by means 
      other than an @outputclass attribute. -->
    <!-- Given an @outputclass value, maps it to an InDesign style.
      
      For now, this just returns the outputclass value, but this
      needs to be driven by a configuration file, ideally the
      same one used to map element types to styles.
    -->
    <xsl:sequence select="$outputclass"/>
  </xsl:function>  
  
  <xsl:function name="e2s:getCStyleForOutputClass" as="xs:string">
    <xsl:param name="context" as="element()"/>
    <xsl:sequence select="e2s:getCStyleForOutputClass($context, string($context/@outputclass))"/>
  </xsl:function>  
  
  <xsl:function name="e2s:getCStyleForOutputClass" as="xs:string">
    <xsl:param name="context" as="element()"/><!-- Element that exhibits the outputclass value -->
    <xsl:param name="outputclass" as="xs:string"/><!-- The output class value. This is passed
      so this function can be used where the
      the "outputclass" is provided by means 
      other than an @outputclass attribute. -->
    <!-- Given an @outputclass value, maps it to an InDesign style.
      
      For now, this just returns the outputclass value, but this
      needs to be driven by a configuration file, ideally the
      same one used to map element types to styles.
    -->
    <xsl:sequence select="$outputclass"/>
  </xsl:function>  
  
</xsl:stylesheet>
