<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xs="http://www.w3.org/2001/XMLSchema" 
    exclude-result-prefixes="xs" 
    version="2.0">
    <xsl:output encoding="UTF-8" method="xml" omit-xml-declaration="yes" indent="yes"/>
    
    <xsl:param name="skey" as="xs:string" select="'unset'"/>
    
    <xsl:template match="/">
        <html>
            <head>
            	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
                <title>IET Published Issue Report</title>
                <link rel="stylesheet" type="text/css" href="{concat('/rsuite/rest/v1/static/iet/css/reports/common-report.css?skey=', $skey)}"/>
                <script Language="JavaScript">self.moveTo(0,0);self.resizeTo(screen.availWidth*.75,screen.availHeight*.75);</script> 
            </head>
            <body>
                <h1>IET Published Issue Report</h1>
                <table>
                    <tr>
                        <th>Manuscript ID</th>
                        <th>Author</th>
                        <th>E-mail</th>
                        <th>Institution of Corresponding Author</th>
                        <th>Country of Corresponding Author</th>
                        <th>Licence Type</th>
                        <th>Submitted Date</th>
                        <th>Accepted Date</th>
                        <th>Article pass for press</th>
                        <th>Issue pass for press</th>
                        <th>Months from Submission to Acceptance</th>
                        <th>Months from Submission to E-first Publication</th>
                        <th>Months from Acceptance to E-first Publication</th>
                        <th>Months from Submission to Print Publication</th>
                        <th>Months from Acceptance to Print Publication</th>
                        <th>Volume Number</th>
                        <th>Issue Number</th>
                        <th>Typeset pages</th>
                    </tr>
                    <xsl:apply-templates/>
                </table>
            </body>
        </html>
    </xsl:template>
    
    <xsl:template match="production-report">
        <xsl:apply-templates/>
    </xsl:template>
    
    <xsl:template match="article">
        <tr>
            <td class="ca"><xsl:value-of select="@id">               
            </xsl:value-of></td>
            <xsl:apply-templates/>
        </tr>
    </xsl:template>
    
    <xsl:template match="meta-item">
        <td>
            <xsl:attribute name="class"><xsl:value-of select="@class"/></xsl:attribute>
            <xsl:value-of select="."/>
        </td>
    </xsl:template>
    
    <xsl:template match="summary">
        <tr class="sum"><td colspan="10">Average</td><xsl:apply-templates/><td colspan="3"/></tr>
    </xsl:template>
    
    <xsl:template match="avg-submit-to-accept|avg-submit-to-e-first-publication|avg-accept-to-e-first-publication|avg-submit-to-print-publication|avg-accept-to-print-publication">
        <td class="ca"><xsl:apply-templates/></td>
    </xsl:template>
    
    <xsl:template match="percent-within-12">
        <tr class="sum"><td colspan="11">Percent published within 12 months of submission</td><xsl:apply-templates/></tr>
    </xsl:template>
    
    <xsl:template match="percent-within-12-to-accept">
    	<td class="ca"><xsl:apply-templates/></td><td/>
    </xsl:template>
    
    <xsl:template match="percent-within-12-to-print-publication">
    	<td class="ca"><xsl:apply-templates/></td><td colspan="4"/>
    </xsl:template>
    
</xsl:stylesheet>
