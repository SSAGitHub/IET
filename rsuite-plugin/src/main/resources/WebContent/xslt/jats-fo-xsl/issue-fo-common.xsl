<?xml version="1.0" encoding="utf-8"?>
<xsl:transform version="2.0" xmlns:fo="http://www.w3.org/1999/XSL/Format"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	
	<xsl:template name="issue-generate-footer-text">	
		<fo:inline font-style="italic"><xsl:value-of select="$journal-short-name"/> </fo:inline>, Vol. <xsl:value-of select="$issue-volume"/>, Iss. <xsl:value-of select="$issue-number"/>, <xsl:value-of select="$issue-abbreviated-cover-date"/>
	</xsl:template>

	<xsl:template match="processing-instruction('show')" priority="100" />
</xsl:transform>
