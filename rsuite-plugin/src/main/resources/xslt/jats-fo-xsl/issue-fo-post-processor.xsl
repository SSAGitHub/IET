<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:fo="http://www.w3.org/1999/XSL/Format" xmlns:xs="http://www.w3.org/2001/XMLSchema"
	version="2.0">

	<xsl:param name="blank-pages-to-add" select="0" />
	<xsl:param name="extract-prelim-page" select="'false'" />
	<xsl:param name="prelim-page-uri" select="''" />


	<xsl:template match="/">
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*" mode="#all">
		<xsl:copy>
			<xsl:apply-templates select="@* | node()" />
		</xsl:copy>
	</xsl:template>

	<xsl:template match="fo:root" priority="10">
		<xsl:copy>
			<xsl:apply-templates select="@* | node()" />

			<xsl:for-each select="1 to xs:integer($blank-pages-to-add)">

				<fo:page-sequence master-reference="lo-blank">
					<fo:flow flow-name="body" />
				</fo:page-sequence>
			</xsl:for-each>
		</xsl:copy>

		<xsl:if test="$extract-prelim-page = 'true'">
			<xsl:result-document method="xml" href="{$prelim-page-uri}">
				<xsl:copy>
					<xsl:apply-templates select="@*" />
					<xsl:apply-templates select="fo:layout-master-set" />
					<xsl:apply-templates
						select="fo:page-sequence[@master-reference ='instruct-page']" mode="prelimPage" />
				</xsl:copy>
			</xsl:result-document>
		</xsl:if>


	</xsl:template>

	<xsl:template match="fo:page-sequence[@master-reference ='instruct-page']"
		mode="#default" priority="2">
		<xsl:if test="$extract-prelim-page = 'false'">
			<xsl:copy>
				<xsl:apply-templates select="@* | node()" />
			</xsl:copy>
		</xsl:if>
	</xsl:template>

	<xsl:template match="@*" mode="#all">
		<xsl:copy>
			<xsl:value-of select="." />
		</xsl:copy>
	</xsl:template>

</xsl:stylesheet>