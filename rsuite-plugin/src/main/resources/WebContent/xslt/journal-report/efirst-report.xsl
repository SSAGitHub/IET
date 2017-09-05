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
                <title>IET E-First Report</title>
                <link rel="stylesheet" type="text/css" href="{concat('/rsuite/rest/v1/static/iet/css/reports/common-report.css?skey=', $skey)}"/>
                <script type="text/javascript" src="{concat('/rsuite/rest/v1/static/iet/scripts/sorttable.js?skey=', $skey)}"/>
                <script Language="JavaScript">self.moveTo(0,0);self.resizeTo(screen.availWidth*.75,screen.availHeight*.75);</script> 
            </head>
            <body>
                <h1>IET E-First Report</h1>
                <table class="sortable">
                	<thead>
	                    <tr>
	                        <th class="sorttable_alpha">Manuscript ID</th>
	                        <th class="sorttable_alpha">Author</th>
	                        <th class="sorttable_alpha">E-mail</th>
	                        <th class="sorttable_alpha">Licence Type</th>
	                        <th class="sorttable_ddmm">Submitted Date</th>
	                        <th class="sorttable_ddmm">Accepted Date</th>
	                        <th class="sorttable_ddmm">Article pass for press</th>
	                        <th class="sorttable_alpha">Months from Submission to Acceptance</th>
	                        <th class="sorttable_alpha">Months from Submission to E-first Publication</th>
	                        <th class="sorttable_alpha">Months from Acceptance to E-first Publication</th>
	                    </tr>
                    </thead>
                    <xsl:apply-templates/>
                </table>
            </body>
        </html>
    </xsl:template>
    
    <xsl:template match="production-report">
        <xsl:apply-templates/>
    </xsl:template>
    
    <xsl:template match="efirst-row">
    	<tbody>
        	<xsl:apply-templates/>
        </tbody>
    </xsl:template>
    
    <xsl:template match="efirst-summary">
    	<tfoot>
        	<xsl:apply-templates/>
        </tfoot>
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
        <tr class="sum"><xsl:apply-templates/></tr>
    </xsl:template>
    
    <xsl:template match="paper-count-summary">
        <td>Paper Count</td><td align="center"><xsl:apply-templates/></td><td colspan="4"></td>
    </xsl:template>
    
    <xsl:template match="average-summary">
        <td align="center">Average</td><xsl:apply-templates/>
    </xsl:template>
    
    <xsl:template match="avg-submit-to-accept|avg-submit-to-e-first-publication|avg-accept-to-e-first-publication|avg-accept-to-print-publication">
        <td class="ca"><xsl:apply-templates/></td>
    </xsl:template>
    
</xsl:stylesheet>
