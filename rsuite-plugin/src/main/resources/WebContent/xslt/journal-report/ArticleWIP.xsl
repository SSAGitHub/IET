<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xs="http://www.w3.org/2001/XMLSchema" exclude-result-prefixes="xs" version="2.0">
    <xsl:output method="html"/>
    
    <xsl:param name="skey" as="xs:string" select="'unset'"/>
    <xsl:param name="basedetailsurl" as="xs:string" select="'unset'"/>

    <xsl:template match="/">
        <html>
            <xsl:apply-templates/>          
        </html>
    </xsl:template>
    
    <xsl:template match="wip">
        <xsl:apply-templates/>
     </xsl:template>
    
    <xsl:template match="articles">
        <head>
            <title>IET Article Details Report</title>
            <link rel="stylesheet" type="text/css" href="{concat('/rsuite/rest/v1/static/iet/css/reports/common-report.css?skey=', $skey)}"/>
            <script Language="JavaScript">self.moveTo(0,0);self.resizeTo(screen.availWidth*.75,screen.availHeight*.75);</script> 
        </head>
        <body>
            <h1>IET Article Work in Progress Report</h1>
            <xsl:choose>
                <xsl:when test="article">
                    <table>
                        <tr>
                            <th>ArticleId</th>
                            <th>Date started</th>
                            <th>Special issue</th>
                            <th>Inspec required</th>
                            <th>Open access</th>
                            <th>Show details</th>
                        </tr>
                        <xsl:apply-templates/>
                    </table>
                </xsl:when>
                <xsl:otherwise>
                    <p>No articles returned</p>
                </xsl:otherwise>
            </xsl:choose>
        </body>
    </xsl:template>

    <xsl:template match="article">
        <tr>
            <td><xsl:sequence select="string(@id)"/></td>
            <td><xsl:sequence select="string(@dtstarted)"/></td>
            <td><xsl:sequence select="string(@isspecialissue)"/></td>
            <td><xsl:sequence select="string(@inspecrequired)"/></td>
            <td><xsl:sequence select="string(@isopenaccess)"/></td>
            <td><a href="{$basedetailsurl}&amp;details=yes&amp;pid={@process_id}">Show details</a></td>
        </tr>
    </xsl:template>
    
    <xsl:template match="details">
        <head>
            <title>Details for <xsl:sequence select="string(@id)"/></title>
            <link rel="stylesheet" type="text/css" href="{concat('/rsuite/rest/v1/static/iet/css/reports/common-report.css?skey=', $skey)}"/>
        </head>
        <body>
            <h1>Details for <xsl:sequence select="string(@id)"/></h1>
            <table>
                <tr>
                    <th>Event</th>
                    <th>Date</th>
                </tr>
            <xsl:apply-templates/>
            </table>
            <p><input type="button" value="Back" onclick="history.back(-1)"/></p>
        </body>
    </xsl:template>
    
    <xsl:template match="event">
        <tr>
            <td><xsl:sequence select="string(@name)"/></td>
            <td><xsl:apply-templates/></td>
        </tr>
    </xsl:template>

</xsl:stylesheet>
