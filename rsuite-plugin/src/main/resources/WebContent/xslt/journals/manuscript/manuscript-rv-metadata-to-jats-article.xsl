<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
  xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:iet="http://www.iet.org/journals/xslt-functions"
  xmlns:xs="http://www.w3.org/2001/XMLSchema" exclude-result-prefixes="xs iet xlink" version="2.0">
  <xsl:output doctype-public="-//NLM//DTD JATS (Z39.96) Journal Publishing DTD v1.0 20120330//EN"
    doctype-system="JATS-journalpublishing1.dtd" indent="yes"/>

  <xsl:param name="page-count" select="0"/>
  <xsl:param name="iet-prefix" select="''"/>
  <xsl:param name="should-add-iet-prefix" select="false()"/>
  <xsl:param name="journal-abbrv-title" select="''"/>
  
  <xsl:variable name="journalAbbreviation"
    select="upper-case(normalize-space(/article/journal-meta/journal-title-group/abbrev-journal-title))"/>
  <xsl:variable name="journalCode" select="replace($journalAbbreviation, 'IET([ -]+)', '')"/>
  <xsl:variable name="orignalArticleId"
    select="/article/article-meta/article-id[@pub-id-type = 'manuscript']"/>
  <xsl:variable name="articleId" select="replace($orignalArticleId, 'ELL-', 'EL-')"/>
  <xsl:variable name="shortArticleId" select="replace($articleId, '\.[A-Z0-9]+$', '')"/>
  <xsl:variable name="currentDate" as="xs:date" select="current-date()"/>

  <xsl:variable name="openAccess"
    select="lower-case(/article/article-meta/configurable_data_fields/custom_fields[@cd_name = 'open-access']/@cd_value)"/>
  <xsl:variable name="licenseType"
    select="upper-case(/article/article-meta/permissions/license/license-p)"/>

  <xsl:template match="salutation | ringgold_id" priority="10"/>

  
  <xsl:template match="/" priority="2">
    <xsl:apply-templates/>
  </xsl:template>

  
  <xsl:template match="article">
    <xsl:copy>
      <xsl:apply-templates select="@*"/>
      <front>
        <xsl:apply-templates/>
      </front>
    </xsl:copy>
  </xsl:template>

  <xsl:template match="article-meta">
    <xsl:copy>
      <xsl:call-template name="create-artilce-ids"/>
      <xsl:apply-templates select="title-group | contrib-group"/>
      <xsl:call-template name="create-pub-date-element"/>
      <xsl:apply-templates select="history | permissions"/>
      <xsl:call-template name="create-counts"/>
    </xsl:copy>
  </xsl:template>

  <xsl:template match="journal_id">
    <journal-id journal-id-type="publisher-id">
      <xsl:value-of select="concat($iet-prefix, $journalCode)"/>
    </journal-id>
  </xsl:template>

  <xsl:template match="abbrev-journal-title">
    <abbrev-journal-title>
      <xsl:value-of select="$journal-abbrv-title"/>
    </abbrev-journal-title>
  </xsl:template>


  <xsl:template name="create-artilce-ids">
    <xsl:variable name="normalizedArticleId" select="replace($articleId, '\-', '.')"/>
    <xsl:variable name="publisherIdInitial"
      select="concat($iet-prefix, replace($shortArticleId, '\-', '.'))"/>
    <xsl:variable name="publisherId" select="replace($publisherIdInitial, '\.SI', '')"/>


    <article-id pub-id-type="doi">10.1049/<xsl:value-of select="lower-case($publisherId)"
      /></article-id>
    <article-id pub-id-type="publisher-id">
      <xsl:value-of select="$publisherId"/>
    </article-id>
    <article-id pub-id-type="manuscript">
      <xsl:value-of select="$normalizedArticleId"/>
    </article-id>
  </xsl:template>
  
  <xsl:template match="given-name">
    <given-names><xsl:apply-templates/></given-names>
  </xsl:template>
  
  <xsl:template name="create-pub-date-element">
    <pub-date pub-type="epreprint">
      <day><xsl:value-of select="day-from-date($currentDate)"/></day>
      <month><xsl:value-of select="month-from-date($currentDate)"/></month>
      <year><xsl:value-of select="year-from-date($currentDate)"/></year>
    </pub-date>
  </xsl:template>

  <xsl:template name="permissions">
    <permissions>
      <xsl:choose>
        <xsl:when test="$openAccess = 'no'">
          <xsl:call-template name="create-permissions-element-for-not-open-access"/>
        </xsl:when>
        <xsl:when test="$openAccess = 'yes'">
          <xsl:call-template name="create-permissions-element-for-open-access"/>
        </xsl:when>
      </xsl:choose>
    </permissions>
  </xsl:template>
  
  <xsl:template name="create-permissions-element-for-open-access">
    <xsl:variable name="type" select="lower-case(replace($licenseType, 'CC-', ''))"/>

    <xsl:variable name="licenseTypeName">
      <xsl:choose>
        <xsl:when test="$licenseType = 'CC-BY'"/>
        <xsl:when test="$licenseType = 'CC-BY-NC'">-NonCommercial</xsl:when>
        <xsl:when test="$licenseType = 'CC-BY-ND'">-NoDerivs</xsl:when>
        <xsl:when test="$licenseType = 'CC-BY-NC-ND'">-NonCommercial-NoDerivs</xsl:when>
      </xsl:choose>
    </xsl:variable>

    <license license-type="open-access"
      xlink:href="http://creativecommons.org/licenses/{$type}/3.0/">
      <license-p>This is an open access article published by the IET under the Creative Commons
          Attribution<xsl:value-of select="$licenseTypeName"/> License ( <ext-link
          xlink:href="http://creativecommons.org/licenses/{$type}/3.0/"
            >http://creativecommons.org/licenses/<xsl:value-of select="$type"
        />/3.0/</ext-link>)</license-p>
    </license>
  </xsl:template>

  <xsl:template name="create-permissions-element-for-not-open-access">
    <xsl:choose>
      <xsl:when test="$licenseType = 'IET'">
        <copyright-statement>© Institution of Engineering and Technology</copyright-statement>
      </xsl:when>
      <xsl:when test="$licenseType = 'OTHER'">
        <copyright-statement>© </copyright-statement>
      </xsl:when>
    </xsl:choose>
    <copyright-year>
      <xsl:value-of select="year-from-date($currentDate)"/>
    </copyright-year>
  </xsl:template>

  <xsl:template name="create-counts">
    <counts>
      <page-count count="{$page-count}"/>
    </counts>
  </xsl:template>

  <xsl:template match="*">
    <xsl:copy>
      <xsl:apply-templates select="@*"/>
      <xsl:apply-templates/>
    </xsl:copy>
  </xsl:template>
  
  <xsl:template match="@*">
    <xsl:copy-of select="."/>
  </xsl:template>
</xsl:stylesheet>
