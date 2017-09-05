<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	version="2.0">

	<xsl:param name="year" />
	<xsl:param name="month" />
	<xsl:param name="day" />

	<xsl:param name="volume" />
	<xsl:param name="issue" />
	<xsl:param name="firstPage" />
	<xsl:param name="lastPage" />

	<xsl:param name="issue-title" />
	<xsl:param name="special-issue-article" />
	

	<xsl:variable name="printPublishDateCount"
		select="count(/article/front/article-meta/pub-date[@pub-type = 'ppub'])" />

	<xsl:output
		doctype-public="-//NLM//DTD JATS (Z39.96) Journal Publishing DTD v1.0 20120330//EN"
		doctype-system="JATS-journalpublishing1.dtd" />

	<xsl:template match="/">
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="@*|node()">
		<xsl:copy>
			<xsl:apply-templates select="@*|node()" />
		</xsl:copy>
	</xsl:template>

	<xsl:template match="/article/front/article-meta[not(article-categories)]"
		priority="10">
		<xsl:copy>
			<xsl:apply-templates select="@*" />
			<xsl:apply-templates select="article-id" />
			<xsl:if test="$issue-title and $special-issue-article = 'true'">
				<xsl:call-template name="create-article-categories" />
			</xsl:if>
			<xsl:apply-templates select="child::node()[not(self::article-id)]" />
		</xsl:copy>
	</xsl:template>

	<xsl:template name="create-article-categories">
		<article-categories>
			<xsl:call-template name="create-subj-group" />
		</article-categories>
	</xsl:template>


	<xsl:template name="create-subj-group">
		<subj-group subj-group-type="heading">
			<subject>
				<xsl:value-of select="$issue-title" />
			</subject>
		</subj-group>
	</xsl:template>

	<xsl:template match="article-categories" priority="10">
		<xsl:copy>
			<xsl:apply-templates select="@*|node()" />
			<xsl:if test="not(subj-group[@subj-group-type='heading'])">
				<xsl:call-template name="create-subj-group" />
			</xsl:if>
		</xsl:copy>

	</xsl:template>

	<xsl:template match="subj-group[@subj-group-type='heading']"
		priority="10">
		<xsl:choose>
            <xsl:when test="$special-issue-article = 'true'">
                <xsl:call-template name="create-subj-group" />
            </xsl:when>
            <xsl:otherwise>
                <xsl:copy-of select="."/>
            </xsl:otherwise>
        </xsl:choose>
	</xsl:template>

	<xsl:template
		match="/article/front/article-meta/pub-date[@pub-type = 'epub']"
		priority="10">
		<xsl:copy-of select="." />
		<xsl:if test="$printPublishDateCount = 0">
			<pub-date pub-type="ppub">
				<xsl:call-template name="create-date-element-content">
					<xsl:with-param name="day" select="$day" />
					<xsl:with-param name="month" select="$month" />
					<xsl:with-param name="year" select="$year" />
				</xsl:call-template>
			</pub-date>
		</xsl:if>
		
		<xsl:if test="not(following-sibling::pub-date)">
			<xsl:call-template name="add-issue-information" />
		</xsl:if>
	</xsl:template>

	<xsl:template
		match="/article/front/article-meta/pub-date[@pub-type = 'ppub']"
		priority="10">
		<xsl:call-template name="copy-pub-date-with-new-data" />
		<xsl:if test="not(following-sibling::pub-date)">
			<xsl:call-template name="add-issue-information" />
		</xsl:if>
	</xsl:template>

	<xsl:template match="/article/front/article-meta/pub-date[last()]"
		priority="8">
		<xsl:call-template name="add-issue-information" />
	</xsl:template>

	<xsl:template name="add-issue-information">
		<volume>
			<xsl:value-of select="$volume" />
		</volume>
		<issue>
			<xsl:value-of select="$issue" />
		</issue>
		<fpage seq="1">
			<xsl:value-of select="$firstPage" />
		</fpage>
		<lpage>
			<xsl:value-of select="$lastPage" />
		</lpage>
	</xsl:template>

	<xsl:template name="copy-pub-date-with-new-data">
		<xsl:copy>
			<xsl:apply-templates select="@*" />

			<xsl:call-template name="create-date-element-content">
				<xsl:with-param name="day" select="$day" />
				<xsl:with-param name="month" select="$month" />
				<xsl:with-param name="year" select="$year" />
			</xsl:call-template>

		</xsl:copy>
	</xsl:template>

	<xsl:template name="create-date-element-content">
		<xsl:param name="day" />
		<xsl:param name="month" />
		<xsl:param name="year" />

		<xsl:call-template name="create-element-if-not-empty-value">
			<xsl:with-param name="element-name" select="'day'" />
			<xsl:with-param name="value" select="$day" />
		</xsl:call-template>

		<xsl:call-template name="create-element-if-not-empty-value">
			<xsl:with-param name="element-name" select="'month'" />
			<xsl:with-param name="value" select="$month" />
		</xsl:call-template>

		<xsl:call-template name="create-element-if-not-empty-value">
			<xsl:with-param name="element-name" select="'year'" />
			<xsl:with-param name="value" select="$year" />
		</xsl:call-template>
	</xsl:template>

	<xsl:template name="create-element-if-not-empty-value">
		<xsl:param name="element-name" />
		<xsl:param name="value" />

		<xsl:if test="$value != ''">
			<xsl:element name="{$element-name}">
				<xsl:value-of select="$value"></xsl:value-of>
			</xsl:element>
		</xsl:if>
	</xsl:template>

	<xsl:template
		match="/article/front/article-meta/volume | /article/front/article-meta/issue | /article/front/article-meta/fpage | /article/front/article-meta/lpage"
		priority="10" />

</xsl:stylesheet>
