<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
  xmlns:xs="http://www.w3.org/2001/XMLSchema"
  xmlns:xd="http://www.oxygenxml.com/ns/doc/xsl"
  xmlns:local="urn:local"
  exclude-result-prefixes="xs xd local"
  version="2.0">
  
  <xsl:import href="inspectClass2subjectscheme.xsl"/>
  
  <xsl:key name="rec-by-term" match="rec" use="normalize-space(term)"/>
  
  <xsl:template match="inspthes">
    <xsl:apply-templates select="contg"/>
    <mapref href="inspect-classification.ditamap"/>
    <subjectdef>
      <topicmeta><navtitle>Thesaurus</navtitle></topicmeta>
      <xsl:for-each-group select="rec" group-by="local:getGroupingKey(.)">
        <subjectdef>
          <topicmeta>
            <navtitle><xsl:sequence select="local:getGroupingKey(.)"/></navtitle>
          </topicmeta>
          <xsl:apply-templates select="current-group()"/>
        </subjectdef>
      </xsl:for-each-group>
    </subjectdef>
  </xsl:template>
  
  <xsl:template match="rec">
    <xsl:variable name="recordKey" as="xs:string"
      select="local:getRecordKey(.)"
    />
    <subjectdef keys="{$recordKey}">
      <topicmeta>
        <xsl:apply-templates select="term, * except (term, bt, cc, pt, tt, nt, uf, rt, use)"/>
      </topicmeta>
      <xsl:apply-templates select="uf, bt, cc, pt, tt, nt, rt, use"/>
    </subjectdef>    
  </xsl:template>
  
  <xsl:template match="rt">
    <hasRelated>
      <topicmeta><navtitle>Related Terms</navtitle></topicmeta>
      <xsl:apply-templates/>
    </hasRelated>
  </xsl:template>
  
  <xsl:template match="rtt | btt | ttt | ptt | ntt | uset">
    <xsl:variable name="refkey" as="xs:string"
      select="local:resolveTermRef(.)"
    />
    <subjectdef>
      <xsl:choose>
        <xsl:when test="$refkey != ''">
          <xsl:attribute name="keyref" select="$refkey"/>
        </xsl:when>
        <xsl:otherwise>
          <xsl:sequence select="'unknown'"/>
        </xsl:otherwise>
      </xsl:choose>
      <topicmeta><navtitle><xsl:apply-templates/></navtitle></topicmeta>
    </subjectdef>
  </xsl:template>
  
  <xsl:template match="uft">
    <xsl:variable name="baseKey" select="local:getRecordKey((ancestor::rec)[1])" as="xs:string"/>
    <xsl:variable name="key" as="xs:string" select="concat($baseKey, '_', count(preceding-sibling::uft))"/>
    <subjectdef keys="{$key}">
      <topicmeta><navtitle><xsl:apply-templates/></navtitle></topicmeta>
    </subjectdef>
  </xsl:template>
  
  <xsl:template match="cc">
    <hasRelated>
      <topicmeta><navtitle>Classification Terms</navtitle></topicmeta>
      <xsl:apply-templates/>
    </hasRelated>
  </xsl:template>
    
  <xsl:template match="cct">
    <xsl:variable name="keyref" as="xs:string" select="normalize-space(.)"/>
    <subjectdef keyref="{$keyref}">
      <topicmeta><navtitle><xsl:apply-templates/></navtitle></topicmeta>
    </subjectdef>
  </xsl:template>
  
  <xsl:template match="bt">
    <hasRelated>
      <topicmeta><navtitle>Broader Terms</navtitle></topicmeta>
      <xsl:apply-templates/>
    </hasRelated>
  </xsl:template>
  
  <xsl:template match="tt">
    <hasRelated>
      <topicmeta><navtitle>Top Terms</navtitle></topicmeta>
      <xsl:apply-templates/>
    </hasRelated>
  </xsl:template>
  
  <xsl:template match="pt">
    <hasRelated>
      <topicmeta><navtitle>Prior Terms</navtitle></topicmeta>
      <xsl:apply-templates/>
    </hasRelated>
  </xsl:template>
  
  <xsl:template match="nt">
    <hasNarrower>
      <topicmeta><navtitle>Narrower Terms</navtitle></topicmeta>
      <xsl:apply-templates/>
    </hasNarrower>
  </xsl:template>
  
  <xsl:template match="uf">
    <hasNarrower>
      <topicmeta><navtitle>Used-For Terms</navtitle></topicmeta>
      <xsl:apply-templates/>
    </hasNarrower>
  </xsl:template>
  
  <xsl:template match="use">
    <hasNarrower>
      <topicmeta><navtitle>Use</navtitle></topicmeta>
      <xsl:apply-templates/>
    </hasNarrower>
  </xsl:template>
  
  <xsl:template match="term">
    <navtitle><xsl:apply-templates/></navtitle>
  </xsl:template>
  
  <xsl:function name="local:getGroupingKey" as="xs:string">
    <xsl:param name="context" as="element()"/>
    <xsl:variable name="baseKey" as="xs:string" select="upper-case(substring($context/term, 1,1))"/>
    <xsl:variable name="result" as="xs:string">
      <xsl:choose>
        <xsl:when test="matches($baseKey, '[A-Z]')">
          <xsl:sequence select="$baseKey"/>
        </xsl:when>
        <xsl:otherwise>
          <xsl:sequence select="'Numeric'"/>
        </xsl:otherwise>
      </xsl:choose>
      
    </xsl:variable>
    <xsl:sequence select="$result"/>
  </xsl:function>
  
  <xsl:function name="local:getRecordKey" as="xs:string">
    <xsl:param name="record" as="element(rec)"/>
    <xsl:variable name="recordKey" as="xs:string"
      select="concat('thes-', format-number(count($record/preceding-sibling::rec), '00000'))"
    />
    
    <xsl:variable name="result" as="xs:string"
      select="$recordKey"
    />
    <xsl:sequence select="$result"/>
  </xsl:function>
  
  <xsl:function name="local:resolveTermRef" as="xs:string">
    <xsl:param name="refElem" as="element()"/>
    <xsl:variable name="term" select="normalize-space($refElem)"/>
    <xsl:variable name="records" as="element(rec)*"
      select="key('rec-by-term', $term, root($refElem))"
    />
    <xsl:choose>
      <xsl:when test="count($records) gt 1">
        <xsl:message> - [WARN] Found <xsl:sequence select="count($records)"/> records for term "<xsl:sequence select="$term"/>"</xsl:message>          
      </xsl:when>
      <xsl:when test="count($records) = 0">
        <xsl:message> - [WARN] (<xsl:sequence select="name($refElem)"/>) No record for term "<xsl:sequence select="$term"/>"</xsl:message>          
      </xsl:when>
      <xsl:otherwise><!-- exactly one record --></xsl:otherwise>
    </xsl:choose>
    <xsl:variable name="record" select="$records[1]" as="element(rec)?"/>
    <xsl:variable name="recordKey" 
      select="
      if ($record)
         then local:getRecordKey($record)
         else 'unknown'" 
         as="xs:string"/>
    <xsl:variable name="result" as="xs:string"
       select="$recordKey"
    />
    <xsl:sequence select="$result"/>
  </xsl:function>
  
</xsl:stylesheet>