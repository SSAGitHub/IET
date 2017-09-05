<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
  xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:local="urn:local-functions"
  xmlns:df="http://dita2indesign.org/dita/functions"
  xmlns:relpath="http://dita2indesign/functions/relpath"
  xmlns:e2s="http//dita2indesign.org/functions/element-to-style-mapping"
  xmlns:RSUITE="http://www.reallysi.com"
  xmlns:idsc="http://www.reallysi.com/namespaces/indesign_style_catalog"
  xmlns:incxgen="http//dita2indesign.org/functions/incx-generation"
  exclude-result-prefixes="xs local df relpath e2s RSUITE idsc" version="2.0">

  <!-- Topic to ICML Transformation.
    
       Into one or more InCopy (ICML) articles.
       
       This module handles the base (topic.mod) types. 
       Specialization modules should add their own
       XSL modules as necessary.
       
       Copyright (c) 2011 DITA2InDesign Project
       
  -->
  <xsl:import href="elem2styleMapper-IET.xsl"/>
  <xsl:import
    href="rsuite:/res/plugin/dita4publishers/toolkit_plugins/org.dita2indesign.dita2indesign/xsl/topic2icmlImpl.xsl"/>
  <xsl:include href="topic2inlineContentIcmlImpl-IET.xsl"/>
  <xsl:include href="icml_generation_util-IET.xsl"/>

  <!-- Directory, relative to result InDesign document, that
    contains linked articles:
  -->
  <!-- Doesn't need to be specified when the topic is being
       generated in isolation, only for generation from
       map-based processing.
    -->
  <xsl:param name="imagePath" as="xs:string" select="'media'"/>


  <xsl:template match="/*[df:class(., 'topic/topic')]" priority="5">
    <xsl:param name="docURI" tunnel="yes" as="xs:string" select="''"/>
    <!-- The topicref that points to this topic -->
    <xsl:param name="topicref" as="element()?" tunnel="yes"/>
    <xsl:param name="articleType" as="xs:string" tunnel="yes" select="$docURI"/>

    <xsl:message> + [DEBUG] topic2icmlImpl-IET.xsl: Processing root topic</xsl:message>
    <!-- Create a new output InCopy article. 
      
      NOTE: This code assumes that all chunking has been performed
      so that each document-root topic maps to one result
      InCopy article and all nested topics are output as
      part of the same story. This behavior can be
      overridden by providing templates that match on
      specific topic types or output classes.
    -->

    <xsl:variable name="articleUrl" as="xs:string" select="local:getArticleUrlForTopic(.)"/>
    <xsl:variable name="articlePath" as="xs:string"
      select="relpath:newFile($outputPath, $articleUrl)"/>
    <xsl:variable name="effectiveArticleType" as="xs:string"
      select="if ($articleType) then $articleType else name(.)"/>
    <xsl:message> + [DEBUG] effectiveArticleType="<xsl:sequence select="$effectiveArticleType"
      />"</xsl:message>


    <!-- Removed call to result-docs mode. -->

    <xsl:message> + [INFO] topic2icmlImpl-IET.xsl: Generating InCopy article "<xsl:sequence
        select="$articlePath"/>"...</xsl:message>
    <!-- Now generate the result document for the root topic -->
    <xsl:result-document href="{$articlePath}" format="icml">
      <xsl:call-template name="makeInCopyArticle">
        <xsl:with-param name="articleType" select="$effectiveArticleType" as="xs:string"
          tunnel="yes"/>
        <xsl:with-param name="styleCatalog" select="$styleCatalog" as="node()*"/>
      </xsl:call-template>
    </xsl:result-document>
    <xsl:call-template name="constructManifestFileEntry">
      <xsl:with-param name="incopyFileUri" select="$articlePath" as="xs:string"/>
    </xsl:call-template>

  </xsl:template>

  <xsl:template name="makeInCopyArticle">
    <xsl:param name="content" as="node()*"/>
    <xsl:param name="leadingParagraphs" as="node()*"/>
    <xsl:param name="trailingParagraphs" as="node()*"/>
    <xsl:param name="articleType" as="xs:string" tunnel="yes"/>
    <!-- The style catalog can be the styles.xml file from an IDML package -->
    <xsl:param name="styleCatalog" as="node()*"/>

    <xsl:if test="$debugBoolean">
      <xsl:message> + [DEBUG] makeInCopyArticle: Article type is "<xsl:sequence
          select="$articleType"/>"</xsl:message>
    </xsl:if>

    <xsl:variable name="effectiveContents" as="node()*"
      select="
      if (count($content) gt 0)
        then $content
        else ./node()
      "/>

    <!-- Get the generated paragraphs as a variable so we can
         then construct a set of stub style definitions for them.
      -->
    <xsl:variable name="articleContents" as="node()*">
      <xsl:sequence select="$leadingParagraphs"/>
      <xsl:apply-templates select="$effectiveContents"/>
      <xsl:sequence select="$trailingParagraphs"/>
    </xsl:variable>

    <xsl:variable name="effectiveStyleCatalog" as="node()*"
      select="local:generateStyleCatalog($articleContents, $styleCatalog)"/>

    <xsl:processing-instruction name="aid">
      style="50" type="snippet" readerVersion="6.0" featureSet="257" product="7.5(142)"
    </xsl:processing-instruction>
    <xsl:processing-instruction name="aid">
      SnippetType="InCopyInterchange"
    </xsl:processing-instruction>
    <Document DOMVersion="7.5" Self="d">
      <!-- FIXME: It may be sufficient to simply generate no-property style
           definitions for each style name or it may be possible to omit
           the styles entirely.
      -->
      <RootCellStyleGroup Self="u4ffc">
        <CellStyle Self="CellStyle/Headings"
          AppliedParagraphStyle="ParagraphStyle/$ID/[No paragraph style]"
          FillColor="Color/PANTONE 356 C" FillTint="100" LeftEdgeStrokeWeight="0"
          TopEdgeStrokeWeight="0" RightEdgeStrokeWeight="0" BottomEdgeStrokeWeight="0"
          KeyboardShortcut="0 0" Name="Headings">
          <Properties>
            <BasedOn type="string">$ID/[None]</BasedOn>
          </Properties>
        </CellStyle>
        <CellStyle Self="CellStyle/$ID/[None]"
          AppliedParagraphStyle="ParagraphStyle/$ID/[No paragraph style]" Name="$ID/[None]"/>
      </RootCellStyleGroup>
      <RootTableStyleGroup Self="u4ff9">
        <TableStyle Self="TableStyle/Table Box" Name="Table Box" TopBorderStrokeWeight="0"
          LeftBorderStrokeWeight="0" BottomBorderStrokeWeight="0" RightBorderStrokeWeight="0"
          StartRowFillColor="Color/PANTONE 356 C" StartRowFillCount="1" EndRowFillCount="1"
          EndRowFillColor="Color/PANTONE 356 C" EndRowFillTint="50" KeyboardShortcut="0 0">
          <Properties>
            <BasedOn type="string">$ID/[No table style]</BasedOn>
          </Properties>
        </TableStyle>
      </RootTableStyleGroup>
      <xsl:sequence select="$effectiveStyleCatalog"/>
      <!-- Create the "story" for the topic contents: -->
      <Story Self="{generate-id(.)}" AppliedTOCStyle="n" TrackChanges="false"
        StoryTitle="story-{generate-id(.)}" AppliedNamedGrid="n">
        <!-- include XMP:
          
          The XML metadata should include at least the topic
          title, if not the author and any copyright information
          in the topic.
        -->
        <MetadataPacketPreference>
          <Properties>
            <Contents>
              <xsl:text disable-output-escaping="yes">&lt;![CDATA[</xsl:text>
              <xsl:apply-templates mode="XMP" select="/*"/>
              <xsl:text disable-output-escaping="yes">]]&gt;</xsl:text>
            </Contents>
          </Properties>
        </MetadataPacketPreference>
        <!-- Core content elements go here -->
        <xsl:sequence select="$articleContents"/>
      </Story>
      <xsl:text>&#x0a;</xsl:text>
      <!-- all hyperlinks go here -->
      <xsl:call-template name="process_links"/>
    </Document>
  </xsl:template>

  <xsl:template name="process_links">
    <!-- this template builds the hyperlink that occurs at the end of the ICML document. Depending
      on the target of the hyperlink, it will have one or two components:
    1. if it's a link to a fragment within the same file it will have one component:
          <Hyperlink> 
      			<Destination type="object">HyperlinkTextDestination</Destination>
        	</Hyperlink>
    2. if it's a link to an external file it will have two components linked together by the "DestinationUniqueKey"   
      	<HyperlinkURLDestination DestinationUniqueKey="4">
        <Hyperlink DestinationUniqueKey="4"> 
      			<Destination type="object">HyperlinkURLDestination</Destination>
        </Hyperlink>
        
    Both <HyperlinkURLDestination> and <Hyperlink> have more information components than here
    indicated; the above examples are illustrative of the basic element relations only in the
    hyperlink.
    NOTE: the order of the two elements <HyperlinkURLDestination> and <Hyperlink> is:
    all <HyperlinkURLDestination> elements are listed first, followed by all <Hyperlink> elements.
    This will require passing over the <xref> elements in the document twice.
    -->
    <!-- First: build the list of <HyperlinkURLDestination> (if any) -->
    <xsl:for-each select="//*[df:class(., 'topic/xref')]">
      <xsl:variable name="link_num" select="count(preceding::*[df:class(., 'topic/xref')]) + 1"/>
      <xsl:variable name="link_target" select="@href"/>
      <xsl:variable name="protocol">
        <xsl:choose>
          <xsl:when test="starts-with($link_target, 'file:')">file</xsl:when>
          <xsl:when test="starts-with($link_target, 'http:')">http</xsl:when>
          <xsl:when test="starts-with($link_target, '#')">fragment</xsl:when>
          <xsl:otherwise>unknown</xsl:otherwise>
        </xsl:choose>
      </xsl:variable>
      <xsl:variable name="file_protocol_drive">
        <xsl:choose>
          <xsl:when test="$protocol = 'file'">
            <xsl:value-of select="substring(substring-after($link_target, 'file:/'), 1, 1)"/>
          </xsl:when>
          <xsl:otherwise>none</xsl:otherwise>
        </xsl:choose>
      </xsl:variable>
      <xsl:variable name="file_syntax">
        <xsl:choose>
          <xsl:when test="$protocol = 'file'">file%3a<xsl:value-of select="$file_protocol_drive"
              />%3a<xsl:value-of
              select="translate(substring-after(substring-after($link_target,
            ':'), ':'), '/', '\')"
            /></xsl:when>
          <xsl:when test="$protocol = 'http'">http%3a//<xsl:value-of
              select="substring-after($link_target,
            'http://')"/></xsl:when>
          <xsl:when test="$protocol = 'fragment'">?</xsl:when>
          <xsl:otherwise>unknown</xsl:otherwise>
        </xsl:choose>
      </xsl:variable>
      <xsl:variable name="name_syntax">
        <xsl:choose>
          <xsl:when test="$protocol = 'file'">file:<xsl:value-of
              select="translate(substring-after($link_target, '/'), '/', '\')"/></xsl:when>
          <xsl:when test="$protocol = 'http'">http://<xsl:value-of
              select="substring-after($link_target,
            'http://')"/></xsl:when>
          <xsl:when test="$protocol = 'fragment'">?</xsl:when>
          <xsl:otherwise>unknown</xsl:otherwise>
        </xsl:choose>
      </xsl:variable>
      <xsl:variable name="repeated_link_num"
        select="count(preceding::*[df:class(., 'topic/xref')][@href=$link_target]) + 1"/>
      <!-- if the link target is an external file, output the <HyperlinkURLDestination> here -->
      <!-- if the link target is a fragment within file it has no <HyperlinkURLDestination> component, so ignore -->
      <xsl:if test="not($protocol='fragment')">
        <HyperlinkURLDestination Self="HyperlinkURLDestination/{$file_syntax}"
          DestinationUniqueKey="{$link_num}" Name="{$name_syntax}" DestinationURL="{$name_syntax}"
          Hidden="false"/>
      </xsl:if>
    </xsl:for-each>
    <!-- Second: build the list of <Hyperlink> elements. Every link must have one no matter what
      kind of target it has -->
    <xsl:for-each select="//*[df:class(., 'topic/xref')]">
      <xsl:variable name="link_num" select="count(preceding::*[df:class(., 'topic/xref')]) + 1"/>
      <xsl:variable name="ID" select="generate-id()"/>
      <xsl:variable name="content" select="."/>
      <xsl:variable name="link_target" select="@href"/>
      <xsl:variable name="protocol">
        <xsl:choose>
          <xsl:when test="starts-with($link_target, 'file:')">file</xsl:when>
          <xsl:when test="starts-with($link_target, 'http:')">http</xsl:when>
          <xsl:when test="starts-with($link_target, '#')">fragment</xsl:when>
          <xsl:otherwise>unknown</xsl:otherwise>
        </xsl:choose>
      </xsl:variable>
      <xsl:variable name="file_protocol_drive">
        <xsl:choose>
          <xsl:when test="$protocol = 'file'">
            <xsl:value-of select="substring(substring-after($link_target, 'file:/'), 1, 1)"/>
          </xsl:when>
          <xsl:otherwise>none</xsl:otherwise>
        </xsl:choose>
      </xsl:variable>
      <xsl:variable name="file_syntax">
        <xsl:choose>
          <xsl:when test="$protocol = 'file'">file%3a<xsl:value-of select="$file_protocol_drive"
              />%3a<xsl:value-of
              select="translate(substring-after(substring-after($link_target,
              ':'), ':'), '/', '\')"
            /></xsl:when>
          <xsl:when test="$protocol = 'http'">http%3a//<xsl:value-of
              select="substring-after($link_target,
            'http://')"/></xsl:when>
          <xsl:when test="$protocol = 'fragment'">HyperlinkTextDestination/<xsl:value-of
              select="substring-after($link_target, '#')"/></xsl:when>
          <xsl:otherwise>unknown</xsl:otherwise>
        </xsl:choose>
      </xsl:variable>
      <Hyperlink Self="{$ID}-{$link_num}" Source="{$ID}" Visible="false" Highlight="None"
        Width="Thin" BorderStyle="Solid" Hidden="false" DestinationUniqueKey="{$link_num}">
        <xsl:attribute name="Name">
          <xsl:choose>
            <xsl:when test="$protocol = 'file'">
              <xsl:value-of select="normalize-space($content)"/>
            </xsl:when>
            <xsl:otherwise>
              <xsl:value-of select="$content"/>
            </xsl:otherwise>
          </xsl:choose>
        </xsl:attribute>
        <Properties>
          <BorderColor type="enumeration">Black</BorderColor>
          <xsl:choose>
            <xsl:when test="$protocol = 'fragment'">
              <Destination type="object">
                <xsl:value-of select="$file_syntax"/>
              </Destination>
            </xsl:when>
            <xsl:otherwise>
              <Destination type="object">HyperlinkURLDestination/<xsl:value-of select="$file_syntax"
                /></Destination>
            </xsl:otherwise>
          </xsl:choose>
        </Properties>
      </Hyperlink>
    </xsl:for-each>
  </xsl:template>

  <!-- ************************************************************ -->
  
  <xsl:template
    match="*[df:class(., 'index/index')]/*[df:class(., 'topic/body')]/*[df:class(., 'topic/section')]"
    priority="20">
    <xsl:param name="articleType" as="xs:string" tunnel="yes"/>
    <xsl:apply-templates select="title"/>
    <xsl:variable name="tempTable">
      <table class="- topic/table " frame="none" id="index">
        <tgroup class="- topic/tgroup " cols="3">
          <colspec class="- topic/colspec " colname="c1" colnum="1" colwidth="100pt"/>
          <colspec class="- topic/colspec " colname="c2" colnum="2" colwidth="1pt"/>
          <colspec class="- topic/colspec " colname="c3" colnum="3" colwidth="48pt"/>
          <tbody class="- topic/tbody ">
            <xsl:apply-templates mode="index2table"/>
          </tbody>
        </tgroup>
      </table>
    </xsl:variable>
    <xsl:apply-templates select="$tempTable/*"/>
    <!--    <xsl:result-document href="file:///C:/temp/temptable.xml">
      <xsl:sequence select="$tempTable"/>
    </xsl:result-document>
-->  </xsl:template>
  
  <xsl:template match="indexEntry" mode="index2table">
    <row class="- topic/row">
      <xsl:copy-of select="@status"/>
      <xsl:apply-templates select="* except indexEntry" mode="index2table"/>
    </row>
    <xsl:apply-templates select="indexEntry" mode="index2table"/>
  </xsl:template>
  
  <xsl:template match="indexTerm" mode="index2table">
    <xsl:variable name="nesting" select="count(ancestor::*[df:class(., 'topic/sectiondiv')])"/>
    <entry class="- topic/entry " valign="bottom" outputclass="index">
      <xsl:copy-of select="@status"/>
      <p class="- topic/p " nesting="{$nesting}">
        <xsl:attribute name="outputclass">
          <xsl:choose>
            <xsl:when test="count(following-sibling::ul/li) > 1">
              <xsl:text>index</xsl:text>
            </xsl:when>
            <xsl:otherwise>
              <xsl:text>indexLast</xsl:text>
            </xsl:otherwise>
          </xsl:choose>
        </xsl:attribute>
        <xsl:sequence select="./node()"/>
      </p>
      <xsl:for-each select="following-sibling::ul/li">
        <xsl:choose>
          <xsl:when test="position() = 1"> </xsl:when>
          <xsl:when test="position() = last()">
            <p class="- topic/p " outputclass="indexLast"/>
          </xsl:when>
          <xsl:otherwise>
            <p class="- topic/p " outputclass="index"/>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:for-each>
    </entry>
    <entry class="- topic/entry " outputclass="index"/>
    <xsl:if test="not(following-sibling::ul)">
      <entry class="- topic/entry " outputclass="index"/>
    </xsl:if>
  </xsl:template>
  
  <xsl:template match="*[df:class(., 'topic/ul')]" mode="index2table">
    <entry class="- topic/entry " valign="bottom" outputclass="index">
      <xsl:copy-of select="@status"/>
      <xsl:apply-templates mode="index2table"/>
    </entry>
  </xsl:template>
  
  <xsl:template match="*[df:class(., 'topic/li')]" mode="index2table">
    <xsl:choose>
      <xsl:when test="count(following-sibling::li) = 0">
        <p class="- topic/p " outputclass="indexLast" align="right">
          <xsl:copy-of select="@status"/>
          <xsl:apply-templates mode="index2table"/>
        </p>
      </xsl:when>
      <xsl:otherwise>
        <p class="- topic/p " outputclass="index" align="right">
          <xsl:copy-of select="@status"/>
          <xsl:apply-templates mode="index2table"/>
        </p>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>
  
  <xsl:template match="ph" mode="index2table" priority="110">
    <ph>
      <xsl:copy-of select="@*"/>
      <xsl:apply-templates mode="index2table"/>
    </ph>
  </xsl:template>
  
  <xsl:template match="b" mode="index2table" priority="110">
    <b>
      <xsl:copy-of select="@*"/>
      <xsl:apply-templates mode="index2table"/>
    </b>
  </xsl:template>
  
  <xsl:template match="*" mode="index2table" priority="-1">
    <xsl:apply-templates mode="index2table"/>
  </xsl:template>
  
  
  <xsl:template match="*[df:class(., 'topic/p')][@outputclass='indexLast']" priority="20">
    <!-- Correctly handle paragraphs that contain mixed content with block-creating elements.
      -->
    <xsl:param name="articleType" as="xs:string" tunnel="yes"/>
    <xsl:variable name="pStyle">
      <xsl:choose>
        <xsl:when test="@align='right'">
          <xsl:text>indexRight</xsl:text>
        </xsl:when>
        <xsl:when test="@nesting=2">
          <xsl:text>index2</xsl:text>
        </xsl:when>
        <xsl:when test="@nesting=3">
          <xsl:text>index3</xsl:text>
        </xsl:when>
        <xsl:when test="@nesting=4">
          <xsl:text>index4</xsl:text>
        </xsl:when>
        <xsl:otherwise>index</xsl:otherwise>
      </xsl:choose>
    </xsl:variable>
    <xsl:call-template name="makeBlock-cont-nobreak">
      <xsl:with-param name="pStyle" select="$pStyle" as="xs:string" tunnel="yes"/>
      <xsl:with-param name="content" as="node()*" select="."/>
    </xsl:call-template>
  </xsl:template>
  
  <xsl:template match="*[df:class(., 'topic/p')][@outputclass='index']" priority="20">
    <!-- Correctly handle paragraphs that contain mixed content with block-creating elements.
      -->
    <xsl:param name="articleType" as="xs:string" tunnel="yes"/>
    <xsl:variable name="cStyle" select="e2s:getCStyleForElement(.)" as="xs:string"/>
    <xsl:variable name="pStyle">
      <xsl:choose>
        <xsl:when test="@align='right'">
          <xsl:text>indexRight</xsl:text>
        </xsl:when>
        <xsl:when test="@nesting=2">
          <xsl:text>index2</xsl:text>
        </xsl:when>
        <xsl:when test="@nesting=3">
          <xsl:text>index3</xsl:text>
        </xsl:when>
        <xsl:when test="@nesting=4">
          <xsl:text>index4</xsl:text>
        </xsl:when>
        <xsl:otherwise>index</xsl:otherwise>
      </xsl:choose>
    </xsl:variable>
    <xsl:call-template name="makeBlock-cont">
      <xsl:with-param name="pStyle" select="$pStyle" as="xs:string" tunnel="yes"/>
      <xsl:with-param name="cStyle" select="$cStyle" as="xs:string" tunnel="yes"/>
      <xsl:with-param name="content" as="node()*" select="."/>
    </xsl:call-template>
  </xsl:template>
  
  
<!-- ************************************************************ -->  
  <xsl:template
    match="*[df:class(., 'chapter/chapter')]/*[df:class(., 'topic/title')]
    /*[df:class(., 'd4p_simplenum-d/d4pSimpleEnumerator')]
    "
    mode="block-children" priority="10">
    <xsl:param name="articleType" as="xs:string" tunnel="yes"/>
    <xsl:variable name="pStyle" select="e2s:getPStyleForElement(., $articleType)" as="xs:string"/>
    <xsl:variable name="cStyle" select="e2s:getCStyleForElement(.)" as="xs:string"/>
    <xsl:for-each-group select="* | text()"
      group-adjacent="if (self::*) then if (df:isBlock(.)) then 'block' else 'text' else 'text'">
      <xsl:choose>
        <xsl:when test="self::* and df:isBlock(.)">
          <xsl:apply-templates select="current-group()"/>
        </xsl:when>
        <xsl:otherwise>
          <xsl:call-template name="makeBlock-cont">
            <xsl:with-param name="pStyle" select="$pStyle" as="xs:string" tunnel="yes"/>
            <xsl:with-param name="cStyle" select="$cStyle" as="xs:string" tunnel="yes"/>
            <xsl:with-param name="content" as="node()*" select="current-group()"/>
          </xsl:call-template>
        </xsl:otherwise>
      </xsl:choose>
    </xsl:for-each-group>
  </xsl:template>

  <xsl:template
    match="*[df:class(., 'topic/desc')]" priority="10">
    <xsl:param name="articleType" as="xs:string" tunnel="yes"/>
    <xsl:if test="child::cite_margin">
      <xsl:call-template name="OutputTextframe">
        <xsl:with-param name="citations">
          <xsl:sequence select="child::cite_margin"/>
        </xsl:with-param>
      </xsl:call-template>
    </xsl:if>
    <xsl:variable name="pStyle" select="e2s:getPStyleForElement(., $articleType)" as="xs:string"/>
    <xsl:variable name="cStyle" select="e2s:getCStyleForElement(.)" as="xs:string"/>
    <xsl:for-each-group select="* | text()"
      group-adjacent="if (self::*) then if (df:isBlock(.)) then 'block' else 'text' else 'text'">
      <xsl:choose>
        <xsl:when test="self::* and df:isBlock(.)">
          <xsl:apply-templates select="current-group()"/>
        </xsl:when>
        <xsl:otherwise>
          <xsl:call-template name="makeBlock-cont">
            <xsl:with-param name="pStyle" select="$pStyle" as="xs:string" tunnel="yes"/>
            <xsl:with-param name="cStyle" select="$cStyle" as="xs:string" tunnel="yes"/>
            <xsl:with-param name="content" as="node()*" select="current-group()"/>
          </xsl:call-template>
        </xsl:otherwise>
      </xsl:choose>
    </xsl:for-each-group>
  </xsl:template>

  
  <xsl:template match="*[df:class(., 'topic/title')][@outputclass='empty']" priority="10"/>

  <!-- Suppress all margin citations. These will be output as anchored text frames -->
  <xsl:template match="*[df:class(., 'cite_margin-d/cite_margin ')]" priority="100"/>

  <!-- Titles: set anchored text frame -->
  <xsl:template
    match="
    *[df:class(., 'topic/title')][following-sibling::*[df:class(., 'topic/body')]/*[df:class(., 'cite_margin-d/cite_margin ')]]
    "
    priority="10">
    <!-- Correctly handle paragraphs that contain mixed content with block-creating elements.
      -->
    <xsl:param name="articleType" as="xs:string" tunnel="yes"/>
    <xsl:call-template name="OutputTextframe">
      <xsl:with-param name="citations">
        <xsl:sequence
          select="following-sibling::*[df:class(., 'topic/body')]/*[df:class(., 'cite_margin-d/cite_margin ')]"
        />
      </xsl:with-param>
    </xsl:call-template>
    <xsl:variable name="pStyle" select="e2s:getPStyleForElement(., $articleType)" as="xs:string"/>
    <xsl:variable name="cStyle" select="e2s:getCStyleForElement(.)" as="xs:string"/>
    <xsl:for-each-group select="* | text()"
      group-adjacent="if (self::*) then if (df:isBlock(.)) then 'block' else 'text' else 'text'">
      <xsl:choose>
        <xsl:when test="self::* and df:isBlock(.)">
          <xsl:apply-templates select="current-group()"/>
        </xsl:when>
        <xsl:otherwise>
          <xsl:call-template name="makeBlock-cont">
            <xsl:with-param name="pStyle" select="$pStyle" as="xs:string" tunnel="yes"/>
            <xsl:with-param name="cStyle" select="$cStyle" as="xs:string" tunnel="yes"/>
            <xsl:with-param name="content" as="node()*" select="current-group()"/>
          </xsl:call-template>
        </xsl:otherwise>
      </xsl:choose>
    </xsl:for-each-group>
  </xsl:template>

  <!-- Notes: set anchored text frame -->
  <xsl:template match="*[df:class(., 'topic/note')]" priority="10">
    <xsl:param name="articleType" as="xs:string" tunnel="yes"/>
    <xsl:if test="child::cite_margin">
      <xsl:call-template name="OutputTextframe">
        <xsl:with-param name="citations">
          <xsl:sequence select="child::cite_margin"/>
        </xsl:with-param>
      </xsl:call-template>
    </xsl:if>
    <xsl:variable name="pStyle" select="e2s:getPStyleForElement(., $articleType)" as="xs:string"/>
    <xsl:variable name="cStyle" select="e2s:getCStyleForElement(.)" as="xs:string"/>
    <xsl:variable name="plural" select="count(descendant::*[df:class(., 'topic/li')])"/>
    <ParagraphStyleRange AppliedParagraphStyle="ParagraphStyle/{$pStyle}">
      <CharacterStyleRange AppliedCharacterStyle="CharacterStyle/bold">
        <xsl:choose>
          <xsl:when test="$plural &gt; 1">
            <Content>Notes:</Content>
          </xsl:when>
          <xsl:otherwise>
            <Content>Note:&#x09;</Content>
          </xsl:otherwise>
        </xsl:choose>
      </CharacterStyleRange>
    </ParagraphStyleRange>
    <xsl:if test="$plural &gt; 1">
      <ParagraphStyleRange AppliedParagraphStyle="ParagraphStyle/{$pStyle}">
        <CharacterStyleRange AppliedCharacterStyle="CharacterStyle/bold">
          <Br/>
        </CharacterStyleRange>
      </ParagraphStyleRange>
    </xsl:if>
    <xsl:for-each-group select="* | text()"
      group-adjacent="if (self::*) then if (df:isBlock(.)) then 'block' else 'text' else 'text'">
      <xsl:choose>
        <xsl:when test="self::* and df:isBlock(.)">
          <xsl:apply-templates select="current-group()"/>
        </xsl:when>
        <xsl:otherwise>
          <xsl:call-template name="makeBlock-cont">
            <xsl:with-param name="pStyle" select="$pStyle" as="xs:string" tunnel="yes"/>
            <!--xsl:with-param name="cStyle" select="$cStyle" as="xs:string" tunnel="yes"/-->
            <xsl:with-param name="content" as="node()*" select="current-group()"/>
          </xsl:call-template>
        </xsl:otherwise>
      </xsl:choose>
    </xsl:for-each-group>
  </xsl:template>


  <!-- list items: set anchored text frame when cite_margin present-->
  <xsl:template match="
    *[df:class(., 'topic/li')]
    " priority="10">
    <!-- Correctly handle paragraphs that contain mixed content with block-creating elements.
      -->
    <xsl:param name="articleType" as="xs:string" tunnel="yes"/>
    <xsl:variable name="li-count" select="count(preceding-sibling::li)+1"/>
    <xsl:variable name="listCount"
      select="count(ancestor::*[df:class(., 'topic/ol')]) + count(ancestor::*[df:class(., 'topic/ul')])"/>
    <xsl:choose>
      <xsl:when test="child::cite_margin and not(child::p)">
        <xsl:call-template name="OutputTextframe">
          <xsl:with-param name="citations">
            <xsl:sequence select="child::cite_margin"/>
          </xsl:with-param>
        </xsl:call-template>
      </xsl:when>
      <xsl:when
        test="parent::ol[name(preceding-sibling::*[1]) = 'cite_margin']
        and count(preceding-sibling::li)=0">
        <xsl:call-template name="OutputTextframe">
          <xsl:with-param name="citations">
            <xsl:sequence select="parent::ol/preceding-sibling::cite_margin"/>
          </xsl:with-param>
        </xsl:call-template>
      </xsl:when>
    </xsl:choose>
    <xsl:variable name="pStyle" select="e2s:getPStyleForElement(., $articleType)" as="xs:string"/>
    <!--xsl:variable name="cStyle" select="e2s:getCStyleForElement(.)" as="xs:string"/-->
    <ParagraphStyleRange AppliedParagraphStyle="ParagraphStyle/{$pStyle}">
      <CharacterStyleRange AppliedCharacterStyle="CharacterStyle/$ID/[No character style]">
        <Content>
          <!--          
          <xsl:choose>
            <xsl:when test="$listCount = 1">
              <xsl:text>&#x09;</xsl:text>
            </xsl:when>
            <xsl:when test="$listCount = 2">
              <xsl:text>&#x09;&#x09;</xsl:text>
            </xsl:when>
            <xsl:when test="$listCount = 3">
              <xsl:text>&#x09;&#x09;&#x09;</xsl:text>
            </xsl:when>
            <xsl:when test="$listCount = 4">
              <xsl:text>&#x09;&#x09;&#x09;&#x09;</xsl:text>
            </xsl:when>
            <xsl:when test="$listCount = 5">
              <xsl:text>&#x09;&#x09;&#x09;&#x09;&#x09;</xsl:text>
            </xsl:when>
            <xsl:when test="$listCount = 6">
              <xsl:text>&#x09;&#x09;&#x09;&#x09;&#x09;&#x09;</xsl:text>
            </xsl:when>
          </xsl:choose>
-->
          <xsl:choose>
            <xsl:when test="parent::*[df:class(., 'topic/ul')][@outputclass='no_label']"> </xsl:when>
            <xsl:when test="parent::*[df:class(., 'topic/ul')][@outputclass='arabic']">
              <xsl:number value="$li-count" format="1"/>
              <xsl:text>&#x09;&#x09;</xsl:text>
            </xsl:when>
            <xsl:when test="parent::*[df:class(., 'topic/ol')][@outputclass='arabicParen']">
              <xsl:text>(</xsl:text>
              <xsl:number value="$li-count" format="1"/>
              <xsl:text>)&#x09;</xsl:text>
            </xsl:when>
            <xsl:when test="parent::*[df:class(., 'topic/ol')][@outputclass='letterUpperCase']">
              <xsl:number value="$li-count" format="A"/>
              <xsl:text>&#x09;</xsl:text>
            </xsl:when>
            <xsl:when test="parent::*[df:class(., 'topic/ol')][@outputclass='letterLowerCase']">
              <xsl:number value="$li-count" format="a"/>
              <xsl:text>&#x09;</xsl:text>
            </xsl:when>
            <xsl:when test="parent::*[df:class(., 'topic/ol')][@outputclass='letterLowerCaseParen']">
              <xsl:text>(</xsl:text>
              <xsl:number value="$li-count" format="a"/>
              <xsl:text>)&#x09;</xsl:text>
            </xsl:when>
            <xsl:when test="parent::*[df:class(., 'topic/ol')][@outputclass='romanUpperCase']">
              <xsl:number value="$li-count" format="I"/>
              <xsl:text>&#x09;</xsl:text>
            </xsl:when>
            <xsl:when test="parent::*[df:class(., 'topic/ol')][@outputclass='romanLowerCase']">
              <xsl:number value="$li-count" format="i"/>
              <xsl:text>&#x09;</xsl:text>
            </xsl:when>
            <xsl:when test="parent::*[df:class(., 'topic/ol')][@outputclass='romanLowerCaseParen']">
              <xsl:text>(</xsl:text>
              <xsl:number value="$li-count" format="i"/>
              <xsl:text>)&#x09;</xsl:text>
            </xsl:when>
            <xsl:when test="parent::*[df:class(., 'topic/ul')][@outputclass='square']">
              <xsl:text>&#x25A0;</xsl:text>
              <xsl:text>&#x09;</xsl:text>
            </xsl:when>
            <xsl:when test="parent::*[df:class(., 'topic/ul')][@outputclass='triangle']">
              <xsl:choose>&#x09;<xsl:when test="$listCount=1"> </xsl:when>
                <xsl:otherwise>
                  <xsl:text></xsl:text>
                  <xsl:text></xsl:text>
                </xsl:otherwise>
              </xsl:choose>
            </xsl:when>
            <xsl:when test="parent::*[df:class(., 'topic/ul')][@outputclass='hyphen-long']">
              <xsl:text>&#x2013;</xsl:text>
              <xsl:text>&#x09;</xsl:text>
            </xsl:when>
            <xsl:when test="parent::*[df:class(., 'topic/ul')][@outputclass='hyphen-short']">
              <xsl:text>-</xsl:text>
              <xsl:text>&#x09;</xsl:text>
            </xsl:when>
            <xsl:when test="parent::*[df:class(., 'topic/ul')][@outputclass='asterix']">
              <xsl:text>*</xsl:text>
              <xsl:text>&#x09;</xsl:text>
            </xsl:when>
            <xsl:when test="parent::*[df:class(., 'topic/ul')][@outputclass='dagger']">
              <xsl:text>&#x2020;</xsl:text>
              <xsl:text>&#x09;</xsl:text>
            </xsl:when>
            <xsl:when test="parent::*[df:class(., 'topic/ul')][@outputclass='double-dagger']">
              <xsl:text>&#x2021;</xsl:text>
              <xsl:text>&#x09;&#x09;</xsl:text>
            </xsl:when>
            <xsl:when test="parent::*[df:class(., 'topic/ol')]">
              <xsl:number value="$li-count" format="1"/>
              <xsl:text>&#x09;</xsl:text>
            </xsl:when>
            <xsl:when test="parent::*[df:class(., 'topic/ul')]">
              <xsl:text>&#x2022;</xsl:text>
              <xsl:text>&#x09;</xsl:text>
            </xsl:when>
          </xsl:choose>
        </Content>
      </CharacterStyleRange>
    </ParagraphStyleRange>
    <xsl:for-each-group select="* | text()"
      group-adjacent="if (self::*) then if (df:isBlock(.)) then 'block' else 'text' else 'text'">
      <xsl:choose>
        <xsl:when test="self::* and df:isBlock(.)">
          <xsl:apply-templates select="current-group()"/>
        </xsl:when>
        <xsl:otherwise>
          <xsl:call-template name="makeBlock-cont">
            <xsl:with-param name="pStyle" select="$pStyle" as="xs:string" tunnel="yes"/>
            <!--xsl:with-param name="cStyle" select="$cStyle" as="xs:string" tunnel="yes"/-->
            <xsl:with-param name="content" as="node()*" select="current-group()"/>
          </xsl:call-template>
        </xsl:otherwise>
      </xsl:choose>
    </xsl:for-each-group>
  </xsl:template>

  <!-- Paragraphs: set anchored text frame -->
  <xsl:template
    match="
    *[df:class(., 'topic/p')]
    [name(preceding-sibling::*[1]) = 'cite_margin']
    [not(self::cite_margin)][not(parent::*[df:class(., 'topic/body')])]
    "
    priority="10">
    <!-- Correctly handle paragraphs that contain mixed content with block-creating elements.
      -->
    <xsl:param name="articleType" as="xs:string" tunnel="yes"/>
    <xsl:call-template name="OutputTextframe">
      <xsl:with-param name="citations">
        <xsl:sequence select="preceding-sibling::cite_margin"/>
      </xsl:with-param>
    </xsl:call-template>
    <xsl:variable name="pStyle" select="e2s:getPStyleForElement(., $articleType)" as="xs:string"/>
    <xsl:variable name="cStyle" select="e2s:getCStyleForElement(.)" as="xs:string"/>
    <xsl:if test="preceding-sibling::*[df:class(., 'topic/ul')] and parent::*[df:class(., 'topic/li')]">
      <ParagraphStyleRange AppliedParagraphStyle="ParagraphStyle/{$pStyle}">
        <CharacterStyleRange AppliedCharacterStyle="CharacterStyle/$ID/[No character style]">
          <Content>&#x200B;&#x09;&#x200B;</Content>
        </CharacterStyleRange>
      </ParagraphStyleRange>
    </xsl:if>
    <xsl:for-each-group select="* | text()"
      group-adjacent="if (self::*) then if (df:isBlock(.)) then 'block' else 'text' else 'text'">
      <xsl:choose>
        <xsl:when test="self::* and df:isBlock(.)">
          <xsl:apply-templates select="current-group()"/>
        </xsl:when>
        <xsl:otherwise>
          <xsl:call-template name="makeBlock-cont">
            <xsl:with-param name="pStyle" select="$pStyle" as="xs:string" tunnel="yes"/>
            <xsl:with-param name="cStyle" select="$cStyle" as="xs:string" tunnel="yes"/>
            <xsl:with-param name="content" as="node()*" select="current-group()"/>
          </xsl:call-template>
        </xsl:otherwise>
      </xsl:choose>
    </xsl:for-each-group>
  </xsl:template>

  <!-- Paragraphs: set anchored text frame -->
  <xsl:template match="
    *[df:class(., 'topic/figgroup')]/*[df:class(., 'topic/title')]
    " priority="10" mode="block-children">
    <!-- Correctly handle paragraphs that contain mixed content with block-creating elements.
      -->
    <xsl:param name="articleType" as="xs:string" tunnel="yes"/>
    <xsl:variable name="pStyle" select="e2s:getPStyleForElement(., $articleType)" as="xs:string"/>
    <xsl:variable name="cStyle" select="e2s:getCStyleForElement(.)" as="xs:string"/>
    <xsl:for-each-group select="* | text()"
      group-adjacent="if (self::*) then if (df:isBlock(.)) then 'block' else 'text' else 'text'">
      <xsl:choose>
        <xsl:when test="self::* and df:isBlock(.)">
          <xsl:apply-templates select="current-group()"/>
        </xsl:when>
        <xsl:otherwise>
          <xsl:call-template name="makeBlock-cont">
            <xsl:with-param name="pStyle" select="$pStyle" as="xs:string" tunnel="yes"/>
            <xsl:with-param name="content" as="node()*" select="current-group()"/>
          </xsl:call-template>
        </xsl:otherwise>
      </xsl:choose>
    </xsl:for-each-group>
  </xsl:template>
  
  <!-- Figures: set anchored text frame -->
  <xsl:template match="
    *[df:class(., 'topic/figgroup')]
    " priority="10">
    <!-- Correctly handle paragraphs that contain mixed content with block-creating elements.
      -->
    <xsl:param name="articleType" as="xs:string" tunnel="yes"/>
    <xsl:variable name="pStyle" select="e2s:getPStyleForElement(., $articleType)" as="xs:string"/>
    <xsl:variable name="cStyle" select="e2s:getCStyleForElement(.)" as="xs:string"/>
    <xsl:for-each-group select="* | text()"
      group-adjacent="if (self::*) then if (df:isBlock(.)) then 'block' else 'text' else 'text'">
      <xsl:choose>
        <xsl:when test="self::* and df:isBlock(.)">
          <xsl:apply-templates select="current-group()"/>
        </xsl:when>
        <xsl:otherwise>
          <xsl:call-template name="makeBlock-cont">
            <xsl:with-param name="pStyle" select="$pStyle" as="xs:string" tunnel="yes"/>
            <xsl:with-param name="cStyle" select="$cStyle" as="xs:string" tunnel="yes"/>
            <xsl:with-param name="content" as="node()*" select="current-group()"/>
          </xsl:call-template>
        </xsl:otherwise>
      </xsl:choose>
    </xsl:for-each-group>
  </xsl:template>
  
  <!-- Paragraphs: set anchored text frame -->
  <xsl:template match="
    *[df:class(., 'topic/dd')]
    " priority="10">
    <!-- Correctly handle paragraphs that contain mixed content with block-creating elements.
      -->
    <xsl:param name="articleType" as="xs:string" tunnel="yes"/>
    <xsl:variable name="pStyle" select="e2s:getPStyleForElement(., $articleType)" as="xs:string"/>
    <xsl:variable name="cStyle" select="e2s:getCStyleForElement(.)" as="xs:string"/>
    <xsl:for-each-group select="* | text()"
      group-adjacent="if (self::*) then if (df:isBlock(.)) then 'block' else 'text' else 'text'">
      <xsl:choose>
        <xsl:when test="self::* and df:isBlock(.)">
          <xsl:apply-templates select="current-group()"/>
        </xsl:when>
        <xsl:otherwise>
          <xsl:call-template name="makeBlock-cont">
            <xsl:with-param name="pStyle" select="$pStyle" as="xs:string" tunnel="yes"/>
            <xsl:with-param name="cStyle" select="$cStyle" as="xs:string" tunnel="yes"/>
            <xsl:with-param name="content" as="node()*" select="current-group()"/>
          </xsl:call-template>
        </xsl:otherwise>
      </xsl:choose>
    </xsl:for-each-group>
  </xsl:template>
  
  <!-- Figures: set anchored text frame -->
  <xsl:template match="*[df:class(., 'topic/fig')]">
    <xsl:param name="articleType" as="xs:string" tunnel="yes"/>
    <!-- Override this template to put the title before or after the 
         figure content.
      -->
    <xsl:if test="child::cite_margin">
      <xsl:call-template name="OutputTextframe">
        <xsl:with-param name="citations">
          <xsl:sequence select="child::cite_margin"/>
        </xsl:with-param>
      </xsl:call-template>
    </xsl:if>
    <xsl:apply-templates/>
  </xsl:template>

  <!-- Use appropriate text frame based on the count of cite_margin elements being set -->
  <xsl:template name="OutputTextframe">
    <xsl:param name="citations"/>
    <xsl:variable name="citation_count" select="count($citations/cite_margin)"/>
    <xsl:choose>
      <xsl:when test="$citation_count=1">

        <TextFrame Self="u218e" ParentStory="u217a" PreviousTextFrame="n" NextTextFrame="n"
          ContentType="TextType" ParentInterfaceChangeCount="" TargetInterfaceChangeCount=""
          LastUpdatedInterfaceChangeCount="" OverriddenPageItemProps=""
          HorizontalLayoutConstraints="FlexibleDimension FixedDimension FlexibleDimension"
          VerticalLayoutConstraints="FlexibleDimension FixedDimension FlexibleDimension"
          StrokeWeight="1" GradientFillStart="0 0" GradientFillLength="0" GradientFillAngle="0"
          GradientStrokeStart="0 0" GradientStrokeLength="0" GradientStrokeAngle="0" Locked="false"
          LocalDisplaySetting="Default" GradientFillHiliteLength="0" GradientFillHiliteAngle="0"
          GradientStrokeHiliteLength="0" GradientStrokeHiliteAngle="0"
          AppliedObjectStyle="ObjectStyle/$ID/[None]" Visible="true" Name="$ID/"
          ItemTransform="1 0 0 1 12.75590551181104 -3.0830435685285806">
          <Properties>
            <PathGeometry>
              <GeometryPathType PathOpen="false">
                <PathPointArray>
                  <PathPointType Anchor="-12.75590551181104 -6"
                    LeftDirection="-12.75590551181104 -6" RightDirection="-12.75590551181104 -6"/>
                  <PathPointType Anchor="-12.75590551181104 3.0830435685285815"
                    LeftDirection="-12.75590551181104 3.0830435685285815"
                    RightDirection="-12.75590551181104 3.0830435685285815"/>
                  <PathPointType Anchor="32.59842519685039 3.0830435685285815"
                    LeftDirection="32.59842519685039 3.0830435685285815"
                    RightDirection="32.59842519685039 3.0830435685285815"/>
                  <PathPointType Anchor="32.59842519685039 -6" LeftDirection="32.59842519685039 -6"
                    RightDirection="32.59842519685039 -6"/>
                </PathPointArray>
              </GeometryPathType>
            </PathGeometry>
          </Properties>
          <TextFramePreference TextColumnFixedWidth="45.354330708661436" TextColumnMaxWidth="0"
            AutoSizingType="Off" AutoSizingReferencePoint="CenterPoint"
            UseMinimumHeightForAutoSizing="false" MinimumHeightForAutoSizing="0"
            UseMinimumWidthForAutoSizing="false" MinimumWidthForAutoSizing="0"
            UseNoLineBreaksForAutoSizing="false"/>
          <AnchoredObjectSetting AnchoredPosition="Anchored" AnchorPoint="TopRightAnchor"
            HorizontalReferencePoint="PageMargins" VerticalAlignment="CenterAlign"
            VerticalReferencePoint="Capheight" AnchorXoffset="8.503937007874017"/>
          <TextWrapPreference Inverse="false" ApplyToMasterPageOnly="false" TextWrapSide="BothSides"
            TextWrapMode="None">
            <Properties>
              <TextWrapOffset Top="0" Left="0" Bottom="0" Right="0"/>
            </Properties>
          </TextWrapPreference>
          <ObjectExportOption AltTextSourceType="SourceXMLStructure"
            ActualTextSourceType="SourceXMLStructure" CustomAltText="$ID/" CustomActualText="$ID/"
            ApplyTagType="TagFromStructure" CustomImageConversion="false" ImageConversionType="JPEG"
            CustomImageSizeOption="SizeRelativeToPageWidth" ImageExportResolution="Ppi300"
            GIFOptionsPalette="AdaptivePalette" GIFOptionsInterlaced="true"
            JPEGOptionsQuality="High" JPEGOptionsFormat="BaselineEncoding"
            ImageAlignment="AlignLeft" ImageSpaceBefore="0" ImageSpaceAfter="0"
            UseImagePageBreak="false" ImagePageBreak="PageBreakBefore" CustomImageAlignment="false"
            SpaceUnit="CssPixel" CustomLayout="false" CustomLayoutType="AlignmentAndSpacing">
            <Properties>
              <AltMetadataProperty NamespacePrefix="$ID/" PropertyPath="$ID/"/>
              <ActualMetadataProperty NamespacePrefix="$ID/" PropertyPath="$ID/"/>
            </Properties>
          </ObjectExportOption>
          <xsl:for-each select="$citations/cite_margin">
            <ParagraphStyleRange AppliedParagraphStyle="ParagraphStyle/Link">
              <CharacterStyleRange AppliedCharacterStyle="CharacterStyle/$ID/[No character style]">
                <Content>
                  <xsl:value-of select="."/>
                </Content>
              </CharacterStyleRange>
            </ParagraphStyleRange>
            <ParagraphStyleRange AppliedParagraphStyle="ParagraphStyle/Link">
              <CharacterStyleRange AppliedCharacterStyle="CharacterStyle/$ID/[No character style]">
                <Br/>
              </CharacterStyleRange>
            </ParagraphStyleRange>
          </xsl:for-each>
        </TextFrame>

      </xsl:when>
      <xsl:when test="$citation_count=2">

        <TextFrame Self="u2a5e" ParentStory="u2a4a" PreviousTextFrame="n" NextTextFrame="n" ContentType="TextType" ParentInterfaceChangeCount="" TargetInterfaceChangeCount="" LastUpdatedInterfaceChangeCount="" OverriddenPageItemProps="" HorizontalLayoutConstraints="FlexibleDimension FixedDimension FlexibleDimension" VerticalLayoutConstraints="FlexibleDimension FixedDimension FlexibleDimension" StrokeWeight="1" GradientFillStart="0 0" GradientFillLength="0" GradientFillAngle="0" GradientStrokeStart="0 0" GradientStrokeLength="0" GradientStrokeAngle="0" Locked="false" LocalDisplaySetting="Default" GradientFillHiliteLength="0" GradientFillHiliteAngle="0" GradientStrokeHiliteLength="0" GradientStrokeHiliteAngle="0" AppliedObjectStyle="ObjectStyle/$ID/[None]" Visible="true" Name="$ID/" ItemTransform="1 0 0 1 13.322834645669317 16.15748031496063">
          <Properties>
            <PathGeometry>
              <GeometryPathType PathOpen="false">
                <PathPointArray>
                  <PathPointType Anchor="-13.322834645669317 -36" LeftDirection="-13.322834645669317 -36" RightDirection="-13.322834645669317 -36" />
                  <PathPointType Anchor="-13.322834645669317 -16.15748031496063" LeftDirection="-13.322834645669317 -16.15748031496063" RightDirection="-13.322834645669317 -16.15748031496063" />
                  <PathPointType Anchor="32.03149606299213 -16.15748031496063" LeftDirection="32.03149606299213 -16.15748031496063" RightDirection="32.03149606299213 -16.15748031496063" />
                  <PathPointType Anchor="32.03149606299213 -36" LeftDirection="32.03149606299213 -36" RightDirection="32.03149606299213 -36" />
                </PathPointArray>
              </GeometryPathType>
            </PathGeometry>
          </Properties>
          <TextFramePreference TextColumnFixedWidth="45.35433070866145" TextColumnMaxWidth="0" AutoSizingType="Off" AutoSizingReferencePoint="CenterPoint" UseMinimumHeightForAutoSizing="false" MinimumHeightForAutoSizing="0" UseMinimumWidthForAutoSizing="false" MinimumWidthForAutoSizing="0" UseNoLineBreaksForAutoSizing="false" />
          <AnchoredObjectSetting AnchoredPosition="Anchored" AnchorPoint="TopRightAnchor" VerticalAlignment="TopAlign" VerticalReferencePoint="Capheight" AnchorXoffset="8.503937007874017" />
          <TextWrapPreference Inverse="false" ApplyToMasterPageOnly="false" TextWrapSide="BothSides" TextWrapMode="None">
            <Properties>
              <TextWrapOffset Top="0" Left="0" Bottom="0" Right="0" />
            </Properties>
          </TextWrapPreference>
          <ObjectExportOption AltTextSourceType="SourceXMLStructure" ActualTextSourceType="SourceXMLStructure" CustomAltText="$ID/" CustomActualText="$ID/" ApplyTagType="TagFromStructure" CustomImageConversion="false" ImageConversionType="JPEG" CustomImageSizeOption="SizeRelativeToPageWidth" ImageExportResolution="Ppi300" GIFOptionsPalette="AdaptivePalette" GIFOptionsInterlaced="true" JPEGOptionsQuality="High" JPEGOptionsFormat="BaselineEncoding" ImageAlignment="AlignLeft" ImageSpaceBefore="0" ImageSpaceAfter="0" UseImagePageBreak="false" ImagePageBreak="PageBreakBefore" CustomImageAlignment="false" SpaceUnit="CssPixel" CustomLayout="false" CustomLayoutType="AlignmentAndSpacing">
            <Properties>
              <AltMetadataProperty NamespacePrefix="$ID/" PropertyPath="$ID/" />
              <ActualMetadataProperty NamespacePrefix="$ID/" PropertyPath="$ID/" />
            </Properties>
          </ObjectExportOption>
        <xsl:for-each select="$citations/cite_margin">
            <ParagraphStyleRange AppliedParagraphStyle="ParagraphStyle/Link">
              <CharacterStyleRange AppliedCharacterStyle="CharacterStyle/$ID/[No character style]">
                <Content>
                  <xsl:value-of select="."/>
                </Content>
              </CharacterStyleRange>
            </ParagraphStyleRange>
            <ParagraphStyleRange AppliedParagraphStyle="ParagraphStyle/Link">
              <CharacterStyleRange AppliedCharacterStyle="CharacterStyle/$ID/[No character style]">
                <Br/>
              </CharacterStyleRange>
            </ParagraphStyleRange>
          </xsl:for-each>
        </TextFrame>

      </xsl:when>

      <xsl:when test="$citation_count=3">

        <TextFrame Self="u1d2eb" ParentStory="u1d2d7" PreviousTextFrame="n" NextTextFrame="n"
          ContentType="TextType" ParentInterfaceChangeCount="" TargetInterfaceChangeCount=""
          LastUpdatedInterfaceChangeCount="" OverriddenPageItemProps=""
          HorizontalLayoutConstraints="FlexibleDimension FixedDimension FlexibleDimension"
          VerticalLayoutConstraints="FlexibleDimension FixedDimension FlexibleDimension"
          StrokeWeight="1" GradientFillStart="0 0" GradientFillLength="0" GradientFillAngle="0"
          GradientStrokeStart="0 0" GradientStrokeLength="0" GradientStrokeAngle="0" Locked="false"
          LocalDisplaySetting="Default" GradientFillHiliteLength="0" GradientFillHiliteAngle="0"
          GradientStrokeHiliteLength="0" GradientStrokeHiliteAngle="0"
          AppliedObjectStyle="ObjectStyle/$ID/[None]" Visible="true" Name="$ID/"
          ItemTransform="1 0 0 1 36 3.6854603692430885">
          <Properties>
            <PathGeometry>
              <GeometryPathType PathOpen="false">
                <PathPointArray>
                  <PathPointType Anchor="-36 -36" LeftDirection="-36 -36" RightDirection="-36 -36"/>
                  <PathPointType Anchor="-36 -3.685460369243089"
                    LeftDirection="-36 -3.685460369243089" RightDirection="-36 -3.685460369243089"/>
                  <PathPointType Anchor="32.03149606299213 -3.685460369243089"
                    LeftDirection="32.03149606299213 -3.685460369243089"
                    RightDirection="32.03149606299213 -3.685460369243089"/>
                  <PathPointType Anchor="32.03149606299213 -36"
                    LeftDirection="32.03149606299213 -36" RightDirection="32.03149606299213 -36"/>
                </PathPointArray>
              </GeometryPathType>
            </PathGeometry>
          </Properties>
          <TextFramePreference TextColumnFixedWidth="68.03149606299213" TextColumnMaxWidth="0"/>
          <AnchoredObjectSetting AnchoredPosition="Anchored" AnchorPoint="TopRightAnchor"
            VerticalAlignment="TopAlign" VerticalReferencePoint="Capheight"/>
          <TextWrapPreference Inverse="false" ApplyToMasterPageOnly="false" TextWrapSide="BothSides"
            TextWrapMode="None">
            <Properties>
              <TextWrapOffset Top="0" Left="0" Bottom="0" Right="0"/>
            </Properties>
          </TextWrapPreference>
          <ObjectExportOption AltTextSourceType="SourceXMLStructure"
            ActualTextSourceType="SourceXMLStructure" CustomAltText="$ID/" CustomActualText="$ID/"
            ApplyTagType="TagFromStructure" CustomImageConversion="false" ImageConversionType="JPEG"
            CustomImageSizeOption="SizeRelativeToPageWidth" ImageExportResolution="Ppi300"
            GIFOptionsPalette="AdaptivePalette" GIFOptionsInterlaced="true"
            JPEGOptionsQuality="High" JPEGOptionsFormat="BaselineEncoding"
            ImageAlignment="AlignLeft" ImageSpaceBefore="0" ImageSpaceAfter="0"
            UseImagePageBreak="false" ImagePageBreak="PageBreakBefore" CustomImageAlignment="false"
            SpaceUnit="CssPixel" CustomLayout="false" CustomLayoutType="AlignmentAndSpacing">
            <Properties>
              <AltMetadataProperty NamespacePrefix="$ID/" PropertyPath="$ID/"/>
              <ActualMetadataProperty NamespacePrefix="$ID/" PropertyPath="$ID/"/>
            </Properties>
          </ObjectExportOption>
          <xsl:for-each select="$citations/cite_margin">
            <ParagraphStyleRange AppliedParagraphStyle="ParagraphStyle/Link">
              <CharacterStyleRange AppliedCharacterStyle="CharacterStyle/$ID/[No character style]">
                <Content>
                  <xsl:value-of select="."/>
                </Content>
              </CharacterStyleRange>
            </ParagraphStyleRange>
            <ParagraphStyleRange AppliedParagraphStyle="ParagraphStyle/Link">
              <CharacterStyleRange AppliedCharacterStyle="CharacterStyle/$ID/[No character style]">
                <Br/>
              </CharacterStyleRange>
            </ParagraphStyleRange>
          <xsl:variable name="pStyle">
              <xsl:choose>
                <xsl:when test="./descendant::i">
                  <xsl:text>Link Italic</xsl:text> 
                </xsl:when>
                <xsl:otherwise>
                  <xsl:text>Link</xsl:text> 
                </xsl:otherwise>
              </xsl:choose>
            </xsl:variable>
            <ParagraphStyleRange AppliedParagraphStyle="ParagraphStyle/{$pStyle}">
              <CharacterStyleRange AppliedCharacterStyle="CharacterStyle/$ID/[No character style]">
                <Content>
                  <xsl:value-of select="normalize-space(.)"/>
                </Content>
              </CharacterStyleRange>
            </ParagraphStyleRange>
            <ParagraphStyleRange AppliedParagraphStyle="ParagraphStyle/{$pStyle}">
              <CharacterStyleRange AppliedCharacterStyle="CharacterStyle/$ID/[No character style]">
                <Br/>
              </CharacterStyleRange>
            </ParagraphStyleRange>
            
          </xsl:for-each>
        </TextFrame>

      </xsl:when>
      <xsl:otherwise>

        <TextFrame Self="u1d57f" ParentStory="u1d56b" PreviousTextFrame="n" NextTextFrame="n"
          ContentType="TextType" ParentInterfaceChangeCount="" TargetInterfaceChangeCount=""
          LastUpdatedInterfaceChangeCount="" OverriddenPageItemProps=""
          HorizontalLayoutConstraints="FlexibleDimension FixedDimension FlexibleDimension"
          VerticalLayoutConstraints="FlexibleDimension FixedDimension FlexibleDimension"
          StrokeWeight="1" GradientFillStart="0 0" GradientFillLength="0" GradientFillAngle="0"
          GradientStrokeStart="0 0" GradientStrokeLength="0" GradientStrokeAngle="0" Locked="false"
          LocalDisplaySetting="Default" GradientFillHiliteLength="0" GradientFillHiliteAngle="0"
          GradientStrokeHiliteLength="0" GradientStrokeHiliteAngle="0"
          AppliedObjectStyle="ObjectStyle/$ID/[None]" Visible="true" Name="$ID/"
          ItemTransform="1 0 0 1 36 -8.264665355094508">
          <Properties>
            <PathGeometry>
              <GeometryPathType PathOpen="false">
                <PathPointArray>
                  <PathPointType Anchor="-36 -36" LeftDirection="-36 -36" RightDirection="-36 -36"/>
                  <PathPointType Anchor="-36 8.264665355094506"
                    LeftDirection="-36 8.264665355094506" RightDirection="-36 8.264665355094506"/>
                  <PathPointType Anchor="32.03149606299213 8.264665355094506"
                    LeftDirection="32.03149606299213 8.264665355094506"
                    RightDirection="32.03149606299213 8.264665355094506"/>
                  <PathPointType Anchor="32.03149606299213 -36"
                    LeftDirection="32.03149606299213 -36" RightDirection="32.03149606299213 -36"/>
                </PathPointArray>
              </GeometryPathType>
            </PathGeometry>
          </Properties>
          <TextFramePreference TextColumnFixedWidth="68.03149606299213" TextColumnMaxWidth="0"/>
          <AnchoredObjectSetting AnchoredPosition="Anchored" AnchorPoint="TopRightAnchor"
            VerticalAlignment="TopAlign" VerticalReferencePoint="Capheight"/>
          <TextWrapPreference Inverse="false" ApplyToMasterPageOnly="false" TextWrapSide="BothSides"
            TextWrapMode="None">
            <Properties>
              <TextWrapOffset Top="0" Left="0" Bottom="0" Right="0"/>
            </Properties>
          </TextWrapPreference>
          <ObjectExportOption AltTextSourceType="SourceXMLStructure"
            ActualTextSourceType="SourceXMLStructure" CustomAltText="$ID/" CustomActualText="$ID/"
            ApplyTagType="TagFromStructure" CustomImageConversion="false" ImageConversionType="JPEG"
            CustomImageSizeOption="SizeRelativeToPageWidth" ImageExportResolution="Ppi300"
            GIFOptionsPalette="AdaptivePalette" GIFOptionsInterlaced="true"
            JPEGOptionsQuality="High" JPEGOptionsFormat="BaselineEncoding"
            ImageAlignment="AlignLeft" ImageSpaceBefore="0" ImageSpaceAfter="0"
            UseImagePageBreak="false" ImagePageBreak="PageBreakBefore" CustomImageAlignment="false"
            SpaceUnit="CssPixel" CustomLayout="false" CustomLayoutType="AlignmentAndSpacing">
            <Properties>
              <AltMetadataProperty NamespacePrefix="$ID/" PropertyPath="$ID/"/>
              <ActualMetadataProperty NamespacePrefix="$ID/" PropertyPath="$ID/"/>
            </Properties>
          </ObjectExportOption>
          <xsl:for-each select="$citations/cite_margin">
            <ParagraphStyleRange AppliedParagraphStyle="ParagraphStyle/Link">
              <CharacterStyleRange AppliedCharacterStyle="CharacterStyle/$ID/[No character style]">
                <Content>
                  <xsl:value-of select="."/>
                </Content>
              </CharacterStyleRange>
            </ParagraphStyleRange>
            <ParagraphStyleRange AppliedParagraphStyle="ParagraphStyle/Link">
              <CharacterStyleRange AppliedCharacterStyle="CharacterStyle/$ID/[No character style]">
                <Br/>
              </CharacterStyleRange>
            </ParagraphStyleRange>
          </xsl:for-each>

        </TextFrame>
      </xsl:otherwise>


    </xsl:choose>
  </xsl:template>

  <xsl:template match="*[df:class(., 'topic/image')][@placement='inline']" priority="20">
    <!-- FIXME: The Link URL can be relative as long as it still starts
         with file:/ (and CS6 and older only supports file:/ URLs as far
         as I can determine).
         
         e.g., "file:Links/image-01.jpg"
      -->
    <xsl:variable name="fileName1" select="relpath:getName(document-uri(root(@href)))"/>
    <xsl:variable name="fileName2" select="replace($fileName1, '.xml', '.jpeg')"/>
    <xsl:variable name="linkUri"
      select="
      if (starts-with(@href, 'file:') or starts-with(@href, 'http:'))
      then string(@href)
      else relpath:newFile(relpath:getParent(relpath:base-uri(.)),string(@href))
      "
      as="xs:string"/>
    <xsl:variable name="linkUri2" select="concat('file://media/', @href)" as="xs:string"/>
    <xsl:variable name="tokenizedPath" select="tokenize($linkUri2, '\.')"/>
    <xsl:variable name="extension" select="upper-case($tokenizedPath[count($tokenizedPath)])"/>
    <!--xsl:message>fileName1="<xsl:sequence select="$fileName1"/>"</xsl:message>
    <xsl:message>fileName2="<xsl:sequence select="$fileName2"/>"</xsl:message>
    <xsl:message>linkUri2="<xsl:sequence select="$linkUri2"/>"</xsl:message-->
    <Rectangle Self="u10cd5" ContentType="GraphicType" StoryTitle="$ID/" ParentInterfaceChangeCount="" TargetInterfaceChangeCount="" LastUpdatedInterfaceChangeCount="" OverriddenPageItemProps="" HorizontalLayoutConstraints="FlexibleDimension FixedDimension FlexibleDimension" VerticalLayoutConstraints="FlexibleDimension FixedDimension FlexibleDimension" StrokeWeight="0" StrokeColor="Swatch/None" GradientFillStart="0 0" GradientFillLength="0" GradientFillAngle="0" GradientStrokeStart="0 0" GradientStrokeLength="0" GradientStrokeAngle="0" Locked="false" LocalDisplaySetting="Default" GradientFillHiliteLength="0" GradientFillHiliteAngle="0" GradientStrokeHiliteLength="0" GradientStrokeHiliteAngle="0" AppliedObjectStyle="ObjectStyle/$ID/[Normal Graphics Frame]" Visible="true" Name="$ID/" ItemTransform="1 0 0 1 6 -16.525106544511857">
      <Properties>
        <PathGeometry>
          <GeometryPathType PathOpen="false">
            <PathPointArray>
              <PathPointType Anchor="-6 -6" LeftDirection="-6 -6" RightDirection="-6 -6" />
              <PathPointType Anchor="-6 16.525106544511857" LeftDirection="-6 16.525106544511857" RightDirection="-6 16.525106544511857" />
              <PathPointType Anchor="18.953605926273426 16.525106544511857" LeftDirection="18.953605926273426 16.525106544511857" RightDirection="18.953605926273426 16.525106544511857" />
              <PathPointType Anchor="18.953605926273426 -6" LeftDirection="18.953605926273426 -6" RightDirection="18.953605926273426 -6" />
            </PathPointArray>
          </GeometryPathType>
        </PathGeometry>
      </Properties>
      <AnchoredObjectSetting AnchoredPosition="InlinePosition" SpineRelative="false" LockPosition="false" PinPosition="true" AnchorPoint="BottomRightAnchor" HorizontalAlignment="LeftAlign" HorizontalReferencePoint="TextFrame" VerticalAlignment="TopAlign" VerticalReferencePoint="LineBaseline" AnchorXoffset="0" AnchorYoffset="-2.070679123929155" AnchorSpaceAbove="0" />
      <TextWrapPreference Inverse="false" ApplyToMasterPageOnly="false" TextWrapSide="BothSides" TextWrapMode="None">
        <Properties>
          <TextWrapOffset Top="0" Left="0" Bottom="0" Right="0" />
        </Properties>
        <ContourOption ContourType="SameAsClipping" IncludeInsideEdges="false" ContourPathName="$ID/" />
      </TextWrapPreference>
      <InCopyExportOption IncludeGraphicProxies="true" IncludeAllResources="false" />
      <FrameFittingOption AutoFit="false" LeftCrop="-6" TopCrop="-6" RightCrop="-9.28894281775562" BottomCrop="-6.860443435994052" FittingOnEmptyFrame="Proportionally" FittingAlignment="CenterAnchor" />
      <ObjectExportOption AltTextSourceType="SourceXMLStructure" ActualTextSourceType="SourceXMLStructure" CustomAltText="$ID/" CustomActualText="$ID/" ApplyTagType="TagFromStructure" CustomImageConversion="false" ImageConversionType="JPEG" CustomImageSizeOption="SizeRelativeToPageWidth" ImageExportResolution="Ppi300" GIFOptionsPalette="AdaptivePalette" GIFOptionsInterlaced="true" JPEGOptionsQuality="High" JPEGOptionsFormat="BaselineEncoding" ImageAlignment="AlignLeft" ImageSpaceBefore="0" ImageSpaceAfter="0" UseImagePageBreak="false" ImagePageBreak="PageBreakBefore" CustomImageAlignment="false" SpaceUnit="CssPixel" CustomLayout="false" CustomLayoutType="AlignmentAndSpacing">
        <Properties>
          <AltMetadataProperty NamespacePrefix="$ID/" PropertyPath="$ID/" />
          <ActualMetadataProperty NamespacePrefix="$ID/" PropertyPath="$ID/" />
        </Properties>
      </ObjectExportOption>
      <Image Self="u10cdb" Space="$ID/#Links_RGB" ActualPpi="149 149" EffectivePpi="149 149" ImageRenderingIntent="UseColorSettings" OverriddenPageItemProps="" LocalDisplaySetting="Default" ImageTypeName="$ID/Portable Network Graphics (PNG)" AppliedObjectStyle="ObjectStyle/$ID/[None]" ItemTransform="1 0 0 1 0 0" ParentInterfaceChangeCount="" TargetInterfaceChangeCount="" LastUpdatedInterfaceChangeCount="" HorizontalLayoutConstraints="FlexibleDimension FixedDimension FlexibleDimension" VerticalLayoutConstraints="FlexibleDimension FixedDimension FlexibleDimension" GradientFillStart="0 0" GradientFillLength="0" GradientFillAngle="0" GradientFillHiliteLength="0" GradientFillHiliteAngle="0" Visible="true" Name="$ID/">
        <Properties>
          <Profile type="string">$ID/None</Profile>
          <GraphicProxy><![CDATA[SUkqAIwCAACAACBP+BACCQOCweDQmGQiHQuHwqJQ2IRV/gSMPYQRtnRSJxGPSGQSOLSKDRgCRqOR
4Ey1/jWYP8dTN/jmbTWbzYczidzqeT+fUGbzMdP8b0d/gulP8BU2VCCOxGYDV/uurP9/Vl/1uuV2
vV+wWGvP2yP9v2d/iS1Qqn1GLUSvWdvv9LXV/pi8P9M3u9Xy9pm+4C/4HCXVLP9yYl/u7GP8R4+2
RuoR6fV3DR+SyTMZtaZ1/vHQY7IQW25Sb128JjNhzWWm1iLYP/YCLXCR/h/cP8A7uJLffZ/Q48R5
GVxHK1zBwnWBx/tDnP91dF/ubqdPq9F1P9i9t/hDvb3f6B46Lh6TJW6Jcet8mB8J/u34WKwtz6P8
H/fwLfgeP3ebios9TCIS2brHM+SwGtBL7PwhLfP08TyOIybjNO5C+QG2LqQNA6vQSa0Fge/L9wi/
0JwBCr1wu9rIPgdsOK8bsYu678GvC4LRoE0sKJ2rr2IMyR/mdIR/nHIp/nBJEjySxJyH+XUnqSpc
awfG7yxy87TR4rjUokAcvH+DEwn+DUyH+DMzzNNANzWf4JTczZbTjEb+yu/70xQf5Kz0zaTT4zU4
ltOccABHS3porpvUSf5SUYf5UUfR1IUeVFI0pSdK0wU9NUweVOn+eFQH+EtRwk9CEhtVCzLRFp/0
6eURwhWLQ1k8cIVdVtPVYZtdn+DtfVKjwFWE2tiPdYzIWO4dkwjZdRhKf4PWi3TeKaAR7twD5nJQ
e1t2qezSXBHNw0Jcdv3Fc9yXRc100JLwBnuloEnctQSGO897o5fCoX0Z1+X9fOAX3gN+o5bBnXoY
we4UUqAgAAAAlQAAAAEAAACVAAAAAQAAAA4AAAEEAAEAAAAUAAAAAQEEAAEAAAAUAAAAAgEDAAEA
AAAIANgnAwEDAAEAAAAFAAEABgEDAAEAAAACAAAAEQEEAAEAAAAIAAAAFQEDAAEAAAAEAAAAFgEE
AAEAAAAUAAAAFwEEAAEAAABxAgAAGgEFAAEAAAB8AgAAGwEFAAEAAACEAgAAHAEDAAEAAAABAAAA
UgEDAAEAAAACAAAANYIBAAEAAAAAAAAAAAAAAA==]]></GraphicProxy>
          <GraphicBounds Left="0" Top="0" Right="9.664663108517805" Bottom="9.664663108517805" />
        </Properties>
        <TextWrapPreference Inverse="false" ApplyToMasterPageOnly="false" TextWrapSide="BothSides" TextWrapMode="None">
          <Properties>
            <TextWrapOffset Top="0" Left="0" Bottom="0" Right="0" />
          </Properties>
          <ContourOption ContourType="SameAsClipping" IncludeInsideEdges="false" ContourPathName="$ID/" />
        </TextWrapPreference>
        <MetadataPacketPreference>
          <Properties>
            <Contents><![CDATA[<?xpacket begin="﻿" id="W5M0MpCehiHzreSzNTczkc9d"?>
<x:xmpmeta xmlns:x="adobe:ns:meta/" x:xmptk="Adobe XMP Core 5.3-c011 66.145661, 2012/02/06-14:56:27        ">
   <rdf:RDF xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
      <rdf:Description rdf:about=""
            xmlns:xmp="http://ns.adobe.com/xap/1.0/">
         <xmp:CreatorTool>Adobe Fireworks CS6 (Windows)</xmp:CreatorTool>
         <xmp:CreateDate>2014-09-04T17:18:43Z</xmp:CreateDate>
         <xmp:ModifyDate>2014-09-04T17:20:44Z</xmp:ModifyDate>
      </rdf:Description>
      <rdf:Description rdf:about=""
            xmlns:dc="http://purl.org/dc/elements/1.1/">
         <dc:format>image/png</dc:format>
      </rdf:Description>
   </rdf:RDF>
</x:xmpmeta>
<?xpacket end="r"?>]]></Contents>
          </Properties>
        </MetadataPacketPreference>
        <Link Self="u10cd8" AssetURL="$ID/" AssetID="$ID/" LinkResourceURI="{$linkUri2}"
          LinkResourceFormat="$ID/{$extension}" StoredState="Normal" LinkClassID="35906" LinkClientID="257"
          LinkResourceModified="false" LinkObjectModified="false" ShowInUI="true" CanEmbed="true"
          CanUnembed="true" CanPackage="true" ImportPolicy="NoAutoImport"
          ExportPolicy="NoAutoExport" LinkImportStamp="file 130543248448593825 52461"
          LinkImportModificationTime="2014-09-04T13:20:44" LinkImportTime="2014-09-09T15:08:45"
          LinkResourceSize="0~cced"/>
        <ClippingPathSettings ClippingType="None" InvertPath="false" IncludeInsideEdges="false" RestrictToFrame="false" UseHighResolutionImage="true" Threshold="25" Tolerance="2" InsetFrame="0" AppliedPathName="$ID/" Index="-1" />
        <ImageIOPreference ApplyPhotoshopClippingPath="true" AllowAutoEmbedding="true" AlphaChannelName="$ID/" />
      </Image>
    </Rectangle>
  </xsl:template>

  <xsl:template
    match="*[df:class(., 'topic/image')][@placement='inline'][ancestor::*[df:class(., 'topic/entry')]]"
    priority="25">
    <!-- FIXME: The Link URL can be relative as long as it still starts
         with file:/ (and CS6 and older only supports file:/ URLs as far
         as I can determine).
         
         e.g., "file:Links/image-01.jpg"
      -->
    <xsl:variable name="fileName1" select="relpath:getName(document-uri(root(@href)))"/>
    <xsl:variable name="fileName2" select="replace($fileName1, '.xml', '.jpeg')"/>
    <xsl:variable name="linkUri"
      select="
      if (starts-with(@href, 'file:') or starts-with(@href, 'http:'))
      then string(@href)
      else relpath:newFile(relpath:getParent(relpath:base-uri(.)),string(@href))
      "
      as="xs:string"/>
    <xsl:variable name="linkUri2" select="concat('file://media/', @href)" as="xs:string"/>
    <xsl:variable name="tokenizedPath" select="tokenize($linkUri2, '\.')"/>                
    <xsl:variable name="extension" select="upper-case($tokenizedPath[count($tokenizedPath)])"/>
    
    <!--xsl:message>fileName1="<xsl:sequence select="$fileName1"/>"</xsl:message>
    <xsl:message>fileName2="<xsl:sequence select="$fileName2"/>"</xsl:message>
    <xsl:message>linkUri2="<xsl:sequence select="$linkUri2"/>"</xsl:message-->
    <Rectangle Self="u10cd5" ContentType="GraphicType" StoryTitle="$ID/"
      ParentInterfaceChangeCount="" TargetInterfaceChangeCount="" LastUpdatedInterfaceChangeCount=""
      OverriddenPageItemProps=""
      HorizontalLayoutConstraints="FlexibleDimension FixedDimension FlexibleDimension"
      VerticalLayoutConstraints="FlexibleDimension FixedDimension FlexibleDimension"
      StrokeWeight="0" StrokeColor="Swatch/None" GradientFillStart="0 0" GradientFillLength="0"
      GradientFillAngle="0" GradientStrokeStart="0 0" GradientStrokeLength="0"
      GradientStrokeAngle="0" Locked="false" LocalDisplaySetting="Default"
      GradientFillHiliteLength="0" GradientFillHiliteAngle="0" GradientStrokeHiliteLength="0"
      GradientStrokeHiliteAngle="0" AppliedObjectStyle="ObjectStyle/$ID/[Normal Graphics Frame]"
      Visible="true" Name="$ID/" ItemTransform="1 0 0 1 6 -16.525106544511857">
      <Properties>
        <PathGeometry>
          <GeometryPathType PathOpen="false">
            <PathPointArray>
              <PathPointType Anchor="-6 -6" LeftDirection="-6 -6" RightDirection="-6 -6"/>
              <PathPointType Anchor="-6 51.72763787140658" LeftDirection="-6 51.72763787140658"
                RightDirection="-6 51.72763787140658"/>
              <PathPointType Anchor="95.4724409448819 51.72763787140658"
                LeftDirection="95.4724409448819 51.72763787140658"
                RightDirection="95.4724409448819 51.72763787140658"/>
              <PathPointType Anchor="95.4724409448819 -6" LeftDirection="95.4724409448819 -6"
                RightDirection="95.4724409448819 -6"/>
            </PathPointArray>
          </GeometryPathType>
        </PathGeometry>
      </Properties>
      <AnchoredObjectSetting AnchoredPosition="InlinePosition" SpineRelative="false"
        LockPosition="false" PinPosition="true" AnchorPoint="BottomRightAnchor"
        HorizontalAlignment="LeftAlign" HorizontalReferencePoint="TextFrame"
        VerticalAlignment="TopAlign" VerticalReferencePoint="LineBaseline" AnchorXoffset="0"
        AnchorYoffset="-2.070679123929155" AnchorSpaceAbove="0"/>
      <TextWrapPreference Inverse="false" ApplyToMasterPageOnly="false" TextWrapSide="BothSides"
        TextWrapMode="None">
        <Properties>
          <TextWrapOffset Top="0" Left="0" Bottom="0" Right="0"/>
        </Properties>
        <ContourOption ContourType="SameAsClipping" IncludeInsideEdges="false"
          ContourPathName="$ID/"/>
      </TextWrapPreference>
      <InCopyExportOption IncludeGraphicProxies="true" IncludeAllResources="false"/>
      <FrameFittingOption AutoFit="false" LeftCrop="-6" TopCrop="-6" RightCrop="-9.28894281775562"
        BottomCrop="-6.860443435994052" FittingOnEmptyFrame="Proportionally"
        FittingAlignment="CenterAnchor"/>
      <ObjectExportOption AltTextSourceType="SourceXMLStructure"
        ActualTextSourceType="SourceXMLStructure" CustomAltText="$ID/" CustomActualText="$ID/"
        ApplyTagType="TagFromStructure" CustomImageConversion="false" ImageConversionType="JPEG"
        CustomImageSizeOption="SizeRelativeToPageWidth" ImageExportResolution="Ppi300"
        GIFOptionsPalette="AdaptivePalette" GIFOptionsInterlaced="true" JPEGOptionsQuality="High"
        JPEGOptionsFormat="BaselineEncoding" ImageAlignment="AlignLeft" ImageSpaceBefore="0"
        ImageSpaceAfter="0" UseImagePageBreak="false" ImagePageBreak="PageBreakBefore"
        CustomImageAlignment="false" SpaceUnit="CssPixel" CustomLayout="false"
        CustomLayoutType="AlignmentAndSpacing">
        <Properties>
          <AltMetadataProperty NamespacePrefix="$ID/" PropertyPath="$ID/"/>
          <ActualMetadataProperty NamespacePrefix="$ID/" PropertyPath="$ID/"/>
        </Properties>
      </ObjectExportOption>
      <Image Self="u10cdb" Space="$ID/#Links_RGB" ActualPpi="149 149" EffectivePpi="149 149"
        ImageRenderingIntent="UseColorSettings" OverriddenPageItemProps=""
        LocalDisplaySetting="Default" ImageTypeName="$ID/Portable Network Graphics (PNG)"
        AppliedObjectStyle="ObjectStyle/$ID/[None]" ItemTransform="1 0 0 1 0 0"
        ParentInterfaceChangeCount="" TargetInterfaceChangeCount=""
        LastUpdatedInterfaceChangeCount=""
        HorizontalLayoutConstraints="FlexibleDimension FixedDimension FlexibleDimension"
        VerticalLayoutConstraints="FlexibleDimension FixedDimension FlexibleDimension"
        GradientFillStart="0 0" GradientFillLength="0" GradientFillAngle="0"
        GradientFillHiliteLength="0" GradientFillHiliteAngle="0" Visible="true" Name="$ID/">
        <Properties>
          <Profile type="string">$ID/None</Profile>
          <GraphicProxy><![CDATA[SUkqAIwCAACAACBP+BACCQOCweDQmGQiHQuHwqJQ2IRV/gSMPYQRtnRSJxGPSGQSOLSKDRgCRqOR
4Ey1/jWYP8dTN/jmbTWbzYczidzqeT+fUGbzMdP8b0d/gulP8BU2VCCOxGYDV/uurP9/Vl/1uuV2
vV+wWGvP2yP9v2d/iS1Qqn1GLUSvWdvv9LXV/pi8P9M3u9Xy9pm+4C/4HCXVLP9yYl/u7GP8R4+2
RuoR6fV3DR+SyTMZtaZ1/vHQY7IQW25Sb128JjNhzWWm1iLYP/YCLXCR/h/cP8A7uJLffZ/Q48R5
GVxHK1zBwnWBx/tDnP91dF/ubqdPq9F1P9i9t/hDvb3f6B46Lh6TJW6Jcet8mB8J/u34WKwtz6P8
H/fwLfgeP3ebios9TCIS2brHM+SwGtBL7PwhLfP08TyOIybjNO5C+QG2LqQNA6vQSa0Fge/L9wi/
0JwBCr1wu9rIPgdsOK8bsYu678GvC4LRoE0sKJ2rr2IMyR/mdIR/nHIp/nBJEjySxJyH+XUnqSpc
awfG7yxy87TR4rjUokAcvH+DEwn+DUyH+DMzzNNANzWf4JTczZbTjEb+yu/70xQf5Kz0zaTT4zU4
ltOccABHS3porpvUSf5SUYf5UUfR1IUeVFI0pSdK0wU9NUweVOn+eFQH+EtRwk9CEhtVCzLRFp/0
6eURwhWLQ1k8cIVdVtPVYZtdn+DtfVKjwFWE2tiPdYzIWO4dkwjZdRhKf4PWi3TeKaAR7twD5nJQ
e1t2qezSXBHNw0Jcdv3Fc9yXRc100JLwBnuloEnctQSGO897o5fCoX0Z1+X9fOAX3gN+o5bBnXoY
we4UUqAgAAAAlQAAAAEAAACVAAAAAQAAAA4AAAEEAAEAAAAUAAAAAQEEAAEAAAAUAAAAAgEDAAEA
AAAIANgnAwEDAAEAAAAFAAEABgEDAAEAAAACAAAAEQEEAAEAAAAIAAAAFQEDAAEAAAAEAAAAFgEE
AAEAAAAUAAAAFwEEAAEAAABxAgAAGgEFAAEAAAB8AgAAGwEFAAEAAACEAgAAHAEDAAEAAAABAAAA
UgEDAAEAAAACAAAANYIBAAEAAAAAAAAAAAAAAA==]]></GraphicProxy>
          <GraphicBounds Left="0" Top="0" Right="9.664663108517805" Bottom="9.664663108517805"/>
        </Properties>
        <TextWrapPreference Inverse="false" ApplyToMasterPageOnly="false" TextWrapSide="BothSides"
          TextWrapMode="None">
          <Properties>
            <TextWrapOffset Top="0" Left="0" Bottom="0" Right="0"/>
          </Properties>
          <ContourOption ContourType="SameAsClipping" IncludeInsideEdges="false"
            ContourPathName="$ID/"/>
        </TextWrapPreference>
        <MetadataPacketPreference>
          <Properties>
            <Contents><![CDATA[<?xpacket begin="﻿" id="W5M0MpCehiHzreSzNTczkc9d"?>
<x:xmpmeta xmlns:x="adobe:ns:meta/" x:xmptk="Adobe XMP Core 5.3-c011 66.145661, 2012/02/06-14:56:27        ">
   <rdf:RDF xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
      <rdf:Description rdf:about=""
            xmlns:xmp="http://ns.adobe.com/xap/1.0/">
         <xmp:CreatorTool>Adobe Fireworks CS6 (Windows)</xmp:CreatorTool>
         <xmp:CreateDate>2014-09-04T17:18:43Z</xmp:CreateDate>
         <xmp:ModifyDate>2014-09-04T17:20:44Z</xmp:ModifyDate>
      </rdf:Description>
      <rdf:Description rdf:about=""
            xmlns:dc="http://purl.org/dc/elements/1.1/">
         <dc:format>image/png</dc:format>
      </rdf:Description>
   </rdf:RDF>
</x:xmpmeta>
<?xpacket end="r"?>]]></Contents>
          </Properties>
        </MetadataPacketPreference>
        <Link Self="u10cd8" AssetURL="$ID/" AssetID="$ID/" LinkResourceURI="{$linkUri2}"
          LinkResourceFormat="$ID/{$extension}" StoredState="Normal" LinkClassID="35906" LinkClientID="257"
          LinkResourceModified="false" LinkObjectModified="false" ShowInUI="true" CanEmbed="true"
          CanUnembed="true" CanPackage="true" ImportPolicy="NoAutoImport"
          ExportPolicy="NoAutoExport" LinkImportStamp="file 130543248448593825 52461"
          LinkImportModificationTime="2014-09-04T13:20:44" LinkImportTime="2014-09-09T15:08:45"
          LinkResourceSize="0~cced"/>
        <ClippingPathSettings ClippingType="None" InvertPath="false" IncludeInsideEdges="false"
          RestrictToFrame="false" UseHighResolutionImage="true" Threshold="25" Tolerance="2"
          InsetFrame="0" AppliedPathName="$ID/" Index="-1"/>
        <ImageIOPreference ApplyPhotoshopClippingPath="true" AllowAutoEmbedding="true"
          AlphaChannelName="$ID/"/>
      </Image>
    </Rectangle>
  </xsl:template>
  
  
  <!-- d4p code -->

  <xsl:template match="*[df:class(., 'topic/image')]">
    <!-- FIXME: The Link URL can be relative as long as it still starts
         with file:/ (and CS6 and older only supports file:/ URLs as far
         as I can determine).
         
         e.g., "file:Links/image-01.jpg"
      -->
    <xsl:variable name="linkUri"
      select="
      if (starts-with(@href, 'file:') or starts-with(@href, 'http:'))
      then string(@href)
      else relpath:newFile(relpath:getParent(relpath:base-uri(.)),string(@href))
      "
      as="xs:string"
    />
    <xsl:variable name="linkUri2"
      select="concat('file://media/', @href)" as="xs:string"/>
    <xsl:variable name="tokenizedPath" select="tokenize($linkUri2, '\.')"/>                
    <xsl:variable name="extension" select="upper-case($tokenizedPath[count($tokenizedPath)])"/>
    <xsl:message> + [DEBUG] (mode images): linkUri="<xsl:sequence select="$linkUri"/>"</xsl:message>
    <Rectangle 
      Self="{generate-id()}">
      <Properties>
        <!-- NOTE: This geometry is totally bogus: it's just copied from a sample
          that worked. Probably not worth trying to generate usable
          geometry at this point.
        -->
        <PathGeometry>
          <GeometryPathType PathOpen="false">
            <PathPointArray>
              <PathPointType Anchor="-72 -47" LeftDirection="-72 -47" RightDirection="-72 -47" />
              <PathPointType Anchor="-72 260.559055128042" LeftDirection="-72 260.559055128042" RightDirection="-72 260.559055128042" />
              <PathPointType Anchor="242.64566929133863 260.559055128042" LeftDirection="242.64566929133863 260.559055128042" RightDirection="242.64566929133863 260.559055128042" />
              <PathPointType Anchor="242.64566929133863 -47" LeftDirection="242.64566929133863 -47" RightDirection="242.64566929133863 -47" />
            </PathPointArray>
          </GeometryPathType>
        </PathGeometry>
      </Properties>
      <!-- Proportional fitting -->
      <FrameFittingOption
        AutoFit="false"
        LeftCrop="0"
        TopCrop="-87.39155923273314"
        RightCrop="22.365016263100983"
        BottomCrop="143.23151786205875"
        FittingOnEmptyFrame="Proportionally"
        FittingAlignment="CenterAnchor"/>
      <Image 
        ImageRenderingIntent="UseColorSettings" 
        AppliedObjectStyle="ObjectStyle/$ID/[None]" 
        Visible="true" 
        Name="$ID/"
        Self="rc_{concat(generate-id(),'Image')}">
        <Link 
          Self="{concat(generate-id(),'Link')}" 
          AssetURL="$ID/" 
          AssetID="$ID/"
          LinkResourceURI="{$linkUri2}"
          LinkResourceFormat="$ID/{$extension}"
        />
      </Image>
    </Rectangle>
    <ParagraphStyleRange AppliedParagraphStyle="ParagraphStyle/Image">
      <CharacterStyleRange AppliedCharacterStyle="CharacterStyle/$ID/[No character style]">
        <Br/>
      </CharacterStyleRange>
    </ParagraphStyleRange>
  </xsl:template>
  
  <!-- Functions -->



  <!-- =============================
       Mode story
       ============================= -->

  <xsl:template match="p" mode="story">
    <xsl:element name="p">
      <xsl:apply-templates select="@*|node()" mode="story"/>
      <xsl:element name="triangle"/>
    </xsl:element>
  </xsl:template>

  <xsl:template match="@*|node()" mode="story">
    <xsl:copy>
      <xsl:apply-templates select="@*|node()" mode="story"/>
    </xsl:copy>
  </xsl:template>

</xsl:stylesheet>
