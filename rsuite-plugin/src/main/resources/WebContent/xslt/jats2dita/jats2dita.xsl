<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
  xmlns:xs="http://www.w3.org/2001/XMLSchema"
  xmlns:xd="http://www.oxygenxml.com/ns/doc/xsl"
  xmlns:m="http://www.w3.org/1998/Math/MathML" 
  xmlns:xlink="http://www.w3.org/1999/xlink"
  exclude-result-prefixes="xs xd xlink m"
  version="2.0">
  <!-- JATS-to-DITA transform
    
       This is an experiment in generating DITA from JATS.
       
       It is not intended to be a production transform.
    -->
  
  <xsl:output doctype-public="urn:pubid:com.rsicms.rsuitebuiltin:doctypes:dita:article"
    doctype-system="article.dtd"
    />
  
  <xsl:template match="article">
    <article id="articleid">
      <xsl:apply-templates select="front/article-meta" mode="topic-title"/>
      <xsl:apply-templates select="front" mode="topic-prolog"/>
      <xsl:apply-templates select="body, back"/>
    </article>
  </xsl:template>
  
  <!-- ======================
       Default mode
       ====================== -->
  
  <xsl:template match="body | back">
    <xsl:apply-templates/>
  </xsl:template>
  
  <xsl:template match="sec | ack | ref-list">
    <subsection outputclass="{name(.)}">
      <xsl:apply-templates select="@*"/>
      <xsl:if test="not(@id)">
        <xsl:attribute name="id" select="generate-id(.)"/>
      </xsl:if>
      <title><xsl:apply-templates select="label, title"/></title>
      <body>
        <xsl:apply-templates select="*[not(preceding-sibling::sec)] except (label, title, sec)"/>
      </body>
      <xsl:apply-templates select="* except(*[not(preceding-sibling::sec)])"/>
    </subsection>
  </xsl:template>
  
  <xsl:template match="label">
    <d4pSimpleEnumerator><xsl:apply-templates/></d4pSimpleEnumerator>
  </xsl:template>
  
  <xsl:template match="title">
    <xsl:apply-templates mode="title"/>
  </xsl:template>
  
  <xsl:template match="caption">
    <xsl:apply-templates mode="title"/>
  </xsl:template>
  
  <xsl:template match="ref">
    <p outputclass="{name(.)}">
      <xsl:apply-templates/>
    </p>
  </xsl:template>
  
  <xsl:template match="mixed-citation |
    person-group |
    string-name |
    surname |
    given-names |
    article-title |
    volume |
    year |
    issue |
    source |
    lpage |
    fpage |
    elocation-id |
    publisher-name |
    edition |
    etal |
    conf-name |
    conf-date |
    comment |
    month |
    publisher-loc |
    uri |
    xxxx
    ">
    <ph outputclass="{name(.)}"><xsl:apply-templates/></ph>
  </xsl:template>
  
  
  <xsl:template match="p">
    <p><xsl:apply-templates/></p>
  </xsl:template>
  
  <xsl:template match="sub | sup">
    <xsl:element name="{name(.)}"><xsl:apply-templates/></xsl:element>
  </xsl:template>
  
  <xsl:template match="bold">
    <b><xsl:apply-templates/></b>
  </xsl:template>
  
  <xsl:template match="italic">
    <i><xsl:apply-templates/></i>
  </xsl:template>
  
  <xsl:template match="inline-formula">
    <equation-inline>
      <xsl:apply-templates/>
    </equation-inline>
  </xsl:template>
  
  <xsl:template match="disp-formula">
    <equation-figure>
      <xsl:apply-templates/>
    </equation-figure>
  </xsl:template>
  
  <xsl:template match="alternatives">
    <xsl:apply-templates/>
  </xsl:template>
  
  <xsl:template match="xref">
    <!-- For this to be real have to determine the topic ID of the container
         for the xref target.
      -->
    <xref href="#{@rid}"><xsl:apply-templates/></xref>
  </xsl:template>
  
  <xsl:template match="tex-math">
    <xsl:comment>tex-math: <xsl:value-of select="."/></xsl:comment>
  </xsl:template>
  
  <xsl:template match="m:math">
    <mathml>
    <xsl:apply-templates select="." mode="copy-math"/>
    </mathml>
  </xsl:template>
  
  <xsl:template match="alternatives/inline-graphic" priority="10">
    <xsl:comment> inline graphic: <xsl:value-of select="@xlink:href"/></xsl:comment>
  </xsl:template>

  <xsl:template match="inline-graphic">
    <image href="{@xlink:href}">
      <alt><xsl:value-of select="@xlink:href"/></alt>
    </image>
  </xsl:template>

  <xsl:template match="graphic">
    <p><image href="{@xlink:href}">
      <alt><xsl:value-of select="@xlink:href"/></alt>
    </image></p>
  </xsl:template>
  
  <xsl:template match="statement">
    <bodydiv outputclass="statement"><xsl:apply-templates select="@*"/>
      <xsl:apply-templates select="label" mode="div-label"/>
      <xsl:apply-templates select="* except (label)"/>
    </bodydiv>
  </xsl:template>
  
  <xsl:template match="table-wrap">
   <table>
     <title><xsl:apply-templates select="label, caption" mode="title"/></title>
     <tgroup cols="1">
       <tbody>
         <row>
           <entry>Table content goes here.</entry>
         </row>
       </tbody>
     </tgroup>
   </table>
  </xsl:template>
  
  <xsl:template match="list[@list-type = 'bullet']">
    <ul>
      <xsl:apply-templates/>
    </ul>
  </xsl:template>
  
  <xsl:template match="list[@list-type = 'simple']">
    <sl>
      <xsl:apply-templates/>
    </sl>
  </xsl:template>
  
  <xsl:template match="list-item">
    <li><xsl:apply-templates select="@*"/>
      <xsl:apply-templates/></li>
  </xsl:template>
  
  <xsl:template match="list[@list-type = 'simple']/list-item" priority="10">
    <sli><xsl:apply-templates select="@*"/>
      <xsl:apply-templates/></sli>
  </xsl:template>
  
  <xsl:template match="fig">
    <fig>
      <xsl:if test="@position or @orientation">
        <xsl:attribute name="outputclass"
          select="concat(@position, ' ', @orientation)"/>
      </xsl:if>
      <xsl:apply-templates select="@*"/>
      <title><xsl:apply-templates select="label, caption" mode="title"/></title>
      <xsl:apply-templates select="node() except(label, caption)"/>
    </fig>
  </xsl:template>
  
  <xsl:template match="front">
    <!-- Nothing to do in default mode -->
  </xsl:template>

  <xsl:template match="@*" priority="-1"/>
  
  <xsl:template match="@id">
    <xsl:sequence select="."/>
  </xsl:template>
  
  <!-- ======================
       Mode copy-math
       ====================== -->
  
  <xsl:template mode="copy-math" match="m:*">
    <xsl:element name="m:{local-name(.)}" namespace="http://www.w3.org/1998/Math/MathML"
      
      >
      <xsl:apply-templates select="@*,node()" mode="#current"/>
    </xsl:element>
  </xsl:template>
  
  <xsl:template mode="copy-math" match="@* | text() | processing-instruction()">
    <xsl:sequence select="."/>
  </xsl:template>
  
  <!-- ======================
       Mode div-label
       ====================== -->
  
  <xsl:template mode="div-label" match="label">
    <ph outputclass="label"><xsl:apply-templates/></ph>
  </xsl:template>

  <!-- ======================
       Mode topic-title
       ====================== -->

  <xsl:template mode="topic-title" match="article-meta">
    <xsl:apply-templates select="title-group/article-title" mode="#current"/>
  </xsl:template>
  
  <xsl:template mode="topic-title" match="article-title">
    <title><xsl:apply-templates mode="title"/></title>
  </xsl:template>
  
  <!-- ======================
       Mode topic-prolog
       ====================== -->
  
  <xsl:template mode="topic-prolog" match="front">
    <xsl:apply-templates mode="#current"/>
  </xsl:template>
  
  <xsl:template mode="topic-prolog" match="journal-meta">
    <!-- Do useful things here, like getting the journal details as prolog metadata -->
  </xsl:template>
  
  <xsl:template mode="topic-prolog" match="article-meta">
    <!-- Do useful things here, like getting the article details as prolog metadata -->
  </xsl:template>
  
  <!-- ======================
       Mode title
       ====================== -->
  
  <xsl:template mode="title" match="p" priority="10">
    <ph outputclass="p"><xsl:apply-templates/></ph>
  </xsl:template> 
 
  <xsl:template mode="title" match="*">
    <!-- Delegate to default mode by default -->
    <xsl:apply-templates select="."/>
  </xsl:template>
  
  <xsl:template match="*" priority="-1" mode="title">
    <xsl:message> - [WARN] title: Unhandled element <xsl:value-of select="concat(name(..), '/', name(.))"/></xsl:message>
  </xsl:template>
  <xsl:template match="*" priority="-1" mode="topic-title">
    <xsl:message> - [WARN] topic-title: Unhandled element <xsl:value-of select="concat(name(..), '/', name(.))"/></xsl:message>
  </xsl:template>
  <xsl:template match="*" priority="-1" mode="topic-prolog">
    <xsl:message> - [WARN] topic-prolog: Unhandled element <xsl:value-of select="concat(name(..), '/', name(.))"/></xsl:message>
  </xsl:template>
  <xsl:template match="*" priority="-1">
    <xsl:message> - [WARN] Unhandled element <xsl:value-of select="concat(name(..), '/', name(.))"/></xsl:message>
  </xsl:template>
  
</xsl:stylesheet>