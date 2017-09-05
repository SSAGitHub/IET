<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
  xmlns:xs="http://www.w3.org/2001/XMLSchema"
  xmlns:xd="http://www.oxygenxml.com/ns/doc/xsl"
  exclude-result-prefixes="xs xd"
  version="2.0">
  
  <!-- INSPEC classification XML to DITA subject scheme markup transform -->
  
  <xsl:output method="xml"
    indent="yes"
    doctype-public="-//OASIS//DTD DITA Subject Scheme Map//EN" 
    doctype-system="subjectScheme.dtd"  
  />
  
  <xsl:template match="/">
    <subjectScheme>
      <xsl:apply-templates/>
    </subjectScheme>
  </xsl:template>
  
  <xsl:template match="inspclas">
    <xsl:apply-templates select="contg"/>
    <xsl:for-each-group select="rec" group-starting-with="rec[level = '1']">
      <xsl:call-template name="construct-record-group">
        <xsl:with-param name="currentGroup" select="current-group()" as="node()*"/>
        <xsl:with-param name="level" as="xs:integer" select="1"/>
      </xsl:call-template>
    </xsl:for-each-group>
  </xsl:template>
  
  <xsl:template name="construct-record-group">
    <xsl:param name="currentGroup" as="node()*"/>
    <xsl:param name="level" as="xs:integer"/>
    
    <xsl:variable name="rootRecord" as="element()"
      select="$currentGroup[1]"
    />
    
    <xsl:variable name="recordKey" as="xs:string"
      select="normalize-space($rootRecord/cc)"
    />
    <subjectdef keys="{$recordKey}">
      <xsl:apply-templates select="$rootRecord"/>
      <xsl:for-each-group select="$currentGroup[position() > 1]" group-starting-with="rec[level = $level]">
        <xsl:call-template name="construct-record-group">
          <xsl:with-param name="currentGroup" select="current-group()" as="node()*"/>
          <xsl:with-param name="level" as="xs:integer" select="$level + 1"/>
        </xsl:call-template>
      </xsl:for-each-group>
    </subjectdef>
  </xsl:template>
  
  <xsl:template match="rec">
    <!--
      <rec>
    <cc>A0000</cc>
    <status>C</status>
    <level>1</level>
    <title>General</title>
</rec>
  -->
    <topicmeta>
      <xsl:apply-templates select="title, * except (title)"/>
    </topicmeta>
    <xsl:if test="./*//cc ">
      <!-- WEK: As far as I can tell, cc elements that are not
                children of <rec> are not interesting. 
        -->
<!--      <hasRelated>
        <xsl:apply-templates select="*//cc " mode="xref"/>
      </hasRelated>
-->    </xsl:if>    
    <xsl:if test=".//scr">
      <hasRelated>
        <topicmeta><navtitle>See</navtitle></topicmeta>
        <xsl:apply-templates select=".//scr" mode="xref"/>
      </hasRelated>
    </xsl:if>    
    <xsl:if test=".//sacr">
      <hasRelated>
        <topicmeta><navtitle>See Also</navtitle></topicmeta>
        <xsl:apply-templates select=".//sacr" mode="xref"/>
      </hasRelated>
    </xsl:if>    
  </xsl:template>
  
  <xsl:template mode="xref" match="cc | link">
    <xsl:variable name="baseCode" select="normalize-space(.)" as="xs:string"/>
    <xsl:variable name="code" 
      select="
      if (contains($baseCode, '.')) 
         then substring-before($baseCode, '.') 
         else $baseCode" as="xs:string"/>
    <xsl:variable name="paddedCode" as="xs:string"
      select="if (string-length($code) lt 5) 
      then concat($code, ('0000', '000', '00', '0')[string-length($code)])
        else $code"
    />
    <subjectdef keyref="{$paddedCode}"/>
  </xsl:template>
  
  <xsl:template mode="xref" match="sacr | scr">
    <xsl:apply-templates select="link" mode="#current"/>
  </xsl:template>
  
  <xsl:template match="sacr | scr">
    <!-- Handled in xref mode -->
  </xsl:template>
  
  <xsl:template match="title">
    <navtitle><xsl:apply-templates/></navtitle>
  </xsl:template>
  
  <xsl:template match="rec/cc" priority="10">
    <!-- Suppress since we captured it for the key -->
  </xsl:template>
  
  <xsl:template match="contg">
    <title><xsl:apply-templates select="file"/></title>
    <topicmeta>
      <xsl:apply-templates select="* except (file)"/>
    </topicmeta>
  </xsl:template>
  
  <xsl:template match="file">
    <xsl:apply-templates/>
  </xsl:template>
  
  <xsl:template match="range | date | futg | fut | hisg | hist | ccg | scope | uf">
    <data name="{local-name(.)}">
      <xsl:apply-templates/>
    </data>
  </xsl:template>
  
  <xsl:template match="syr | eyr | day | mo | yr | level | status | cc | di | uft">
    <data name="{local-name(.)}" value="{string(.)}"/>
  </xsl:template>
  
  <xsl:template match="sup">
    <sup><xsl:apply-templates/></sup>
  </xsl:template>
  
  <xsl:template match="sub">
    <sub><xsl:apply-templates/></sub>
  </xsl:template>
  
  <xsl:template match="*">
    <xsl:message> - [WARN] Unhandled element <xsl:sequence select="name(..)"/>/<xsl:sequence select="name(.)"/></xsl:message>
  </xsl:template>
</xsl:stylesheet>